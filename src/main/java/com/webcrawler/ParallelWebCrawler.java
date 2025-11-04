package com.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parallel implementation of WebCrawler using ForkJoinPool.
 */
public class ParallelWebCrawler implements WebCrawler {
    private final CrawlerConfiguration config;
    private final ConcurrentHashMap<String, Integer> wordCounts;
    private final Set<String> visitedUrls;
    private final ForkJoinPool forkJoinPool;
    private final Pattern wordPattern;

    public ParallelWebCrawler(CrawlerConfiguration config) {
        this.config = config;
        this.wordCounts = new ConcurrentHashMap<>();
        this.visitedUrls = ConcurrentHashMap.newKeySet();
        this.forkJoinPool = new ForkJoinPool();
        this.wordPattern = Pattern.compile("\\w+");
    }

    @Override
    @Profiled
    public CrawlResult crawl() {
        List<CrawlTask> tasks = config.getStartUrls().stream()
                .map(url -> new CrawlTask(url, config.getMaxDepth()))
                .collect(Collectors.toList());

        for (CrawlTask task : tasks) {
            forkJoinPool.invoke(task);
        }

        forkJoinPool.shutdown();

        return new CrawlResult(
                new HashMap<>(wordCounts),
                new HashSet<>(visitedUrls)
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
            // Base cases
            if (depth <= 0 || !visitedUrls.add(url)) {
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

                // Extract links and create subtasks
                if (depth > 1) {
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
                // Silently ignore connection errors
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }

            return null;
        }

        private boolean isValidUrl(String url) {
            return url != null 
                    && !url.isEmpty() 
                    && (url.startsWith("http://") || url.startsWith("https://"))
                    && !visitedUrls.contains(url);
        }

        private void countWords(String text) {
            if (text == null || text.isEmpty()) {
                return;
            }

            String[] words = text.toLowerCase().split("\\W+");
            for (String word : words) {
                if (word.length() > 0 && wordPattern.matcher(word).matches()) {
                    wordCounts.merge(word, 1, Integer::sum);
                }
            }
        }
    }
}
