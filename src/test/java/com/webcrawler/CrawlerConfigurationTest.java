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
        CrawlerConfiguration config = new CrawlerConfiguration(
                urls, 2, 5, 10, "output.json"
        );

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
    }
}
