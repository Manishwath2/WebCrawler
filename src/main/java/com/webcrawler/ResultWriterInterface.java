package com.webcrawler;

import java.io.IOException;

/**
 * Interface for result writer.
 */
public interface ResultWriterInterface {
    /**
     * Writes the crawl result to a JSON file.
     *
     * @param result The crawl result
     * @param outputPath Path to the output file
     * @param topWordCount Number of top words to include
     * @throws IOException if the file cannot be written
     */
    @Profiled
    void write(CrawlResult result, String outputPath, int topWordCount) throws IOException;
}
