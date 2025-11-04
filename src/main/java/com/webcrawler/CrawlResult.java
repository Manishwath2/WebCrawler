package com.webcrawler;

import java.util.Map;

/**
 * Represents the result of a web crawl operation.
 */
public class CrawlResult {
    private final Map<String, Integer> wordCounts;
    private final int urlsVisited;

    public CrawlResult(Map<String, Integer> wordCounts, int urlsVisited) {
        this.wordCounts = wordCounts;
        this.urlsVisited = urlsVisited;
    }

    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    public int getUrlsVisited() {
        return urlsVisited;
    }
}
