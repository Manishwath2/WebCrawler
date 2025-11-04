package com.webcrawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

/**
 * Writes crawl results to JSON file or stdout.
 */
public class ResultWriter implements ResultWriterInterface {
    private final Gson gson;

    public ResultWriter() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Writes the crawl result to a JSON file or stdout.
     *
     * @param result The crawl result
     * @param outputPath Path to the output file (empty string = stdout)
     * @param topWordCount Number of top words to include
     * @throws IOException if the file cannot be written
     */
    @Profiled
    public void write(CrawlResult result, String outputPath, int topWordCount) throws IOException {
        JsonObject output = new JsonObject();

        // Sort word counts using WordCounts utility with multi-level sorting
        Map<String, Integer> sortedWords = WordCounts.sort(result.getWordCounts(), topWordCount);

        output.add("wordCounts", gson.toJsonTree(sortedWords));
        output.addProperty("urlsVisited", result.getUrlsVisited());

        // Write to file or stdout based on outputPath
        if (outputPath == null || outputPath.trim().isEmpty()) {
            // Write to stdout
            try (Writer writer = new OutputStreamWriter(System.out)) {
                gson.toJson(output, writer);
                writer.flush();
            }
        } else {
            // Write to file (overwrite if exists)
            try (Writer writer = new FileWriter(outputPath)) {
                gson.toJson(output, writer);
            }
            System.out.println("Results written to: " + outputPath);
        }
    }
}
