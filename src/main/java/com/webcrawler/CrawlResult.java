package com.webcrawler;

import java.util.Map;
import java.util.Set;

/**
 * Represents the result of a web crawl operation.
 */
public class CrawlResult {
    private final Map<String, Integer> wordCounts;
    private final Set<String> urlsVisited;

    public CrawlResult(Map<String, Integer> wordCounts, Set<String> urlsVisited) {
        this.wordCounts = wordCounts;
        this.urlsVisited = urlsVisited;
    }

    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    public Set<String> getUrlsVisited() {
        return urlsVisited;
    }
}
