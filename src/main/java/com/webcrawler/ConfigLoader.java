package com.webcrawler;

import java.io.IOException;

/**
 * Interface for configuration loader.
 */
public interface ConfigLoader {
    /**
     * Loads configuration from a JSON file.
     *
     * @param configPath Path to the JSON configuration file
     * @return CrawlerConfiguration object
     * @throws IOException if the file cannot be read
     */
    @Profiled
    CrawlerConfiguration load(String configPath) throws IOException;
}
