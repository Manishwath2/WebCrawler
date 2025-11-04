package com.webcrawler;

/**
 * Interface for web crawlers.
 */
public interface WebCrawler {
    /**
     * Crawls the web starting from configured URLs.
     *
     * @return CrawlResult containing word counts and visited URLs
     */
    @Profiled
    CrawlResult crawl();
}
