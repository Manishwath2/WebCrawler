package com.webcrawler;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test for CrawlResult
 */
public class CrawlResultTest {

    @Test
    public void testCrawlResult() {
        Map<String, Integer> wordCounts = new HashMap<>();
        wordCounts.put("test", 5);
        wordCounts.put("example", 3);

        Set<String> urls = new HashSet<>();
        urls.add("http://example.com");
        urls.add("http://test.com");

        CrawlResult result = new CrawlResult(wordCounts, urls);

        assertEquals(wordCounts, result.getWordCounts());
        assertEquals(urls, result.getUrlsVisited());
    }
}
