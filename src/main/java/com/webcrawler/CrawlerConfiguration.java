package com.webcrawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for web crawler settings.
 */
public class CrawlerConfiguration {
    private List<String> startUrls = new ArrayList<>();
    private List<String> ignoredUrls = new ArrayList<>();
    private List<String> ignoredWords = new ArrayList<>();
    private int maxDepth;
    private int timeoutSeconds;
    private int popularWordCount;
    private int parallelism = -1;
    private String outputPath = "";
    private String profileOutputPath = "";

    public CrawlerConfiguration() {
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls != null ? startUrls : new ArrayList<>();
    }

    public List<String> getIgnoredUrls() {
        return ignoredUrls;
    }

    public void setIgnoredUrls(List<String> ignoredUrls) {
        this.ignoredUrls = ignoredUrls != null ? ignoredUrls : new ArrayList<>();
    }

    public List<String> getIgnoredWords() {
        return ignoredWords;
    }

    public void setIgnoredWords(List<String> ignoredWords) {
        this.ignoredWords = ignoredWords != null ? ignoredWords : new ArrayList<>();
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

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath != null ? outputPath : "";
    }

    public String getProfileOutputPath() {
        return profileOutputPath;
    }

    public void setProfileOutputPath(String profileOutputPath) {
        this.profileOutputPath = profileOutputPath != null ? profileOutputPath : "";
    }
}
