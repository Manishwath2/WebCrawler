package com.webcrawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Writes crawl results to JSON file.
 */
public class ResultWriter {
    private final Gson gson;

    public ResultWriter() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Writes the crawl result to a JSON file.
     *
     * @param result The crawl result
     * @param outputPath Path to the output file
     * @param topWordCount Number of top words to include
     * @throws IOException if the file cannot be written
     */
    @Profiled
    public void write(CrawlResult result, String outputPath, int topWordCount) throws IOException {
        JsonObject output = new JsonObject();

        // Sort word counts using Stream API
        Map<String, Integer> sortedWords = result.getWordCounts().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topWordCount)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        output.add("wordCounts", gson.toJsonTree(sortedWords));
        output.add("urlsVisited", gson.toJsonTree(new ArrayList<>(result.getUrlsVisited())));

        try (Writer writer = new FileWriter(outputPath)) {
            gson.toJson(output, writer);
        }

        System.out.println("Results written to: " + outputPath);
    }
}
