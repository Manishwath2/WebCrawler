package com.webcrawler;

import java.util.List;

/**
 * Configuration class for web crawler settings.
 */
public class CrawlerConfiguration {
    private List<String> startUrls;
    private int maxDepth;
    private int timeoutSeconds;
    private int popularWordCount;
    private String outputPath;

    public CrawlerConfiguration() {
    }

    public CrawlerConfiguration(List<String> startUrls, int maxDepth, int timeoutSeconds, 
                                int popularWordCount, String outputPath) {
        this.startUrls = startUrls;
        this.maxDepth = maxDepth;
        this.timeoutSeconds = timeoutSeconds;
        this.popularWordCount = popularWordCount;
        this.outputPath = outputPath;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public int getPopularWordCount() {
        return popularWordCount;
    }

    public void setPopularWordCount(int popularWordCount) {
        this.popularWordCount = popularWordCount;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
