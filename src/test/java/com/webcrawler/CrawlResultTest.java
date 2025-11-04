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

        int urlsVisited = 2;

        CrawlResult result = new CrawlResult(wordCounts, urlsVisited);

        assertEquals(wordCounts, result.getWordCounts());
        assertEquals(urlsVisited, result.getUrlsVisited());
    }
}
