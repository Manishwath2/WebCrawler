package com.webcrawler;

import java.io.IOException;
import java.time.Clock;

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
            ConfigLoader loader = new ConfigurationLoader();
            CrawlerConfiguration config = loader.load(configPath);
            
            // Create profiled loader for timing
            ConfigLoader profiledLoader = PerformanceProfiler.wrap(
                loader, 
                Clock.systemDefaultZone(), 
                config.getProfileOutputPath()
            );

            System.out.println("Configuration loaded:");
            System.out.println("  Start URLs: " + config.getStartUrls());
            System.out.println("  Max Depth: " + config.getMaxDepth());
            System.out.println("  Timeout: " + config.getTimeoutSeconds() + " seconds");
            System.out.println("  Popular Words: " + config.getPopularWordCount());
            System.out.println("  Parallelism: " + (config.getParallelism() < 1 ? 
                "auto (" + Runtime.getRuntime().availableProcessors() + " cores)" : 
                config.getParallelism()));
            System.out.println("  Output: " + (config.getOutputPath().isEmpty() ? 
                "stdout" : config.getOutputPath()));
            System.out.println("  Profile Output: " + (config.getProfileOutputPath().isEmpty() ? 
                "stdout" : config.getProfileOutputPath()));

            // Create and run crawler with profiling
            System.out.println("\nStarting crawl...");
            WebCrawler crawler = new ParallelWebCrawler(config);
            WebCrawler profiledCrawler = PerformanceProfiler.wrap(
                crawler, 
                Clock.systemDefaultZone(), 
                config.getProfileOutputPath()
            );
            CrawlResult result = profiledCrawler.crawl();

            System.out.println("\nCrawl completed:");
            System.out.println("  URLs visited: " + result.getUrlsVisited());
            System.out.println("  Unique words: " + result.getWordCounts().size());

            // Write results with profiling
            System.out.println("\nWriting results...");
            ResultWriterInterface writer = new ResultWriter();
            ResultWriterInterface profiledWriter = PerformanceProfiler.wrap(
                writer, 
                Clock.systemDefaultZone(), 
                config.getProfileOutputPath()
            );
            profiledWriter.write(result, config.getOutputPath(), config.getPopularWordCount());

            System.out.println("\n=== Crawl Complete ===");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
