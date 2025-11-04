package com.webcrawler;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test for CrawlerConfiguration
 */
public class CrawlerConfigurationTest {

    @Test
    public void testConfigurationGettersAndSetters() {
        List<String> urls = Arrays.asList("http://example.com");
        CrawlerConfiguration config = new CrawlerConfiguration();
        config.setStartUrls(urls);
        config.setMaxDepth(2);
        config.setTimeoutSeconds(5);
        config.setPopularWordCount(10);
        config.setOutputPath("output.json");

        assertEquals(urls, config.getStartUrls());
        assertEquals(2, config.getMaxDepth());
        assertEquals(5, config.getTimeoutSeconds());
        assertEquals(10, config.getPopularWordCount());
        assertEquals("output.json", config.getOutputPath());
    }

    @Test
    public void testConfigurationDefaultConstructor() {
        CrawlerConfiguration config = new CrawlerConfiguration();
        assertNotNull(config);
        assertNotNull(config.getStartUrls());
        assertNotNull(config.getIgnoredUrls());
        assertNotNull(config.getIgnoredWords());
    }

    @Test
    public void testConfigurationIgnoredPatterns() {
        CrawlerConfiguration config = new CrawlerConfiguration();
        List<String> ignoredUrls = Arrays.asList(".*\\.pdf$", ".*\\.jpg$");
        List<String> ignoredWords = Arrays.asList("the", "a", "an");
        
        config.setIgnoredUrls(ignoredUrls);
        config.setIgnoredWords(ignoredWords);
        
        assertEquals(ignoredUrls, config.getIgnoredUrls());
        assertEquals(ignoredWords, config.getIgnoredWords());
    }
}
