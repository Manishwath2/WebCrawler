package com.webcrawler;

import java.io.IOException;

/**
 * Main application entry point for the parallel web crawler.
 */
public class Main {
    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : "src/main/resources/config.json";

        try {
            System.out.println("=== Parallel Web Crawler ===");
            System.out.println("Loading configuration from: " + configPath);

            // Load configuration with profiling
            ConfigurationLoader loader = new ConfigurationLoader();
            ConfigurationLoader profiledLoader = PerformanceProfiler.wrap(loader);
            CrawlerConfiguration config = profiledLoader.load(configPath);

            System.out.println("Configuration loaded:");
            System.out.println("  Start URLs: " + config.getStartUrls());
            System.out.println("  Max Depth: " + config.getMaxDepth());
            System.out.println("  Timeout: " + config.getTimeoutSeconds() + " seconds");
            System.out.println("  Popular Words: " + config.getPopularWordCount());
            System.out.println("  Output: " + config.getOutputPath());

            // Create and run crawler with profiling
            System.out.println("\nStarting crawl...");
            WebCrawler crawler = new ParallelWebCrawler(config);
            WebCrawler profiledCrawler = PerformanceProfiler.wrap(crawler);
            CrawlResult result = profiledCrawler.crawl();

            System.out.println("\nCrawl completed:");
            System.out.println("  URLs visited: " + result.getUrlsVisited().size());
            System.out.println("  Unique words: " + result.getWordCounts().size());

            // Write results with profiling
            System.out.println("\nWriting results...");
            ResultWriter writer = new ResultWriter();
            ResultWriter profiledWriter = PerformanceProfiler.wrap(writer);
            profiledWriter.write(result, config.getOutputPath(), config.getPopularWordCount());

            System.out.println("\n=== Crawl Complete ===");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
