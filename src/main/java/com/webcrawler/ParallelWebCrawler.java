package com.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;

/**
 * Parallel implementation of WebCrawler using ForkJoinPool.
 */
public class ParallelWebCrawler implements WebCrawler {
    private final CrawlerConfiguration config;
    private final ConcurrentHashMap<String, Integer> wordCounts;
    private final Set<String> visitedUrls;
    private final ForkJoinPool forkJoinPool;
    private final Clock clock;
    private final Instant deadline;
    private final List<Pattern> ignoredUrlPatterns;
    private final List<Pattern> ignoredWordPatterns;

    public ParallelWebCrawler(CrawlerConfiguration config) {
        this(config, Clock.systemUTC());
    }

    public ParallelWebCrawler(CrawlerConfiguration config, Clock clock) {
        this.config = config;
        this.clock = clock;
        this.wordCounts = new ConcurrentHashMap<>();
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        
        // Configure parallelism
        int parallelism = config.getParallelism();
        if (parallelism < 1) {
            parallelism = Runtime.getRuntime().availableProcessors();
        }
        this.forkJoinPool = new ForkJoinPool(parallelism);
        
        // Set deadline for timeout
        this.deadline = clock.instant().plus(Duration.ofSeconds(config.getTimeoutSeconds()));
        
        // Compile regex patterns for ignored URLs
        this.ignoredUrlPatterns = new ArrayList<>();
        for (String regex : config.getIgnoredUrls()) {
            ignoredUrlPatterns.add(Pattern.compile(regex));
        }
        
        // Compile regex patterns for ignored words
        this.ignoredWordPatterns = new ArrayList<>();
        for (String regex : config.getIgnoredWords()) {
            ignoredWordPatterns.add(Pattern.compile(regex));
        }
    }

    @Override
    @Profiled
    public CrawlResult crawl() {
        for (String url : config.getStartUrls()) {
            forkJoinPool.invoke(new CrawlTask(url, config.getMaxDepth()));
        }
        
        forkJoinPool.shutdown();
        
        return new CrawlResult(
            new HashMap<>(wordCounts),
            visitedUrls.size()
        );
    }

    /**
     * ForkJoinTask for crawling a single URL and its links.
     */
    private class CrawlTask extends RecursiveTask<Void> {
        private final String url;
        private final int depth;

        public CrawlTask(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }

        @Override
        protected Void compute() {
            // Check timeout - stop fetching new pages if deadline passed
            if (clock.instant().isAfter(deadline)) {
                return null;
            }
            
            // Base cases
            if (depth <= 0 || !visitedUrls.add(url)) {
                return null;
            }
            
            // Check if URL should be ignored
            if (shouldIgnoreUrl(url)) {
                return null;
            }

            try {
                // Fetch and parse the page
                Document doc = Jsoup.connect(url)
                        .timeout(config.getTimeoutSeconds() * 1000)
                        .get();

                // Extract and count words
                String text = doc.body().text();
                countWords(text);

                // Extract links and create subtasks only if we haven't timed out
                if (depth > 1 && clock.instant().isBefore(deadline)) {
                    Elements links = doc.select("a[href]");
                    List<CrawlTask> subtasks = new ArrayList<>();

                    for (Element link : links) {
                        String nextUrl = link.absUrl("href");
                        if (isValidUrl(nextUrl)) {
                            subtasks.add(new CrawlTask(nextUrl, depth - 1));
                        }
                    }

                    // Fork subtasks
                    invokeAll(subtasks);
                }
            } catch (IOException e) {
                // URL is still considered "visited" even if request fails
                // Silently ignore connection errors
            }

            return null;
        }

        private boolean shouldIgnoreUrl(String url) {
            for (Pattern pattern : ignoredUrlPatterns) {
                if (pattern.matcher(url).matches()) {
                    return true;
                }
            }
            return false;
        }

        private boolean isValidUrl(String url) {
            return url != null 
                    && !url.isEmpty() 
                    && (url.startsWith("http://") || url.startsWith("https://"))
                    && !visitedUrls.contains(url)
                    && !shouldIgnoreUrl(url);
        }

        private void countWords(String text) {
            if (text == null || text.isEmpty()) {
                return;
            }

            String[] words = text.toLowerCase().split("\\W+");
            for (String word : words) {
                if (word.length() > 0 && !shouldIgnoreWord(word)) {
                    wordCounts.merge(word, 1, Integer::sum);
                }
            }
        }

        private boolean shouldIgnoreWord(String word) {
            for (Pattern pattern : ignoredWordPatterns) {
                if (pattern.matcher(word).matches()) {
                    return true;
                }
            }
            return false;
        }
    }
}
