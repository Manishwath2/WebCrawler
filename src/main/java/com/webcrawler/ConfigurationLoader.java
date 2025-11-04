package com.webcrawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Loads crawler configuration from JSON file.
 */
public class ConfigurationLoader {
    private final Gson gson;

    public ConfigurationLoader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Loads configuration from a JSON file.
     *
     * @param configPath Path to the JSON configuration file
     * @return CrawlerConfiguration object
     * @throws IOException if the file cannot be read
     */
    @Profiled
    public CrawlerConfiguration load(String configPath) throws IOException {
        try (Reader reader = new FileReader(configPath)) {
            return gson.fromJson(reader, CrawlerConfiguration.class);
        }
    }
}
