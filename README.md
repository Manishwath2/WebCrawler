# WebCrawler

A parallel web crawler implementation in Java using ForkJoinPool for concurrent page fetching and parsing.

## Features

- **Parallel Crawling**: Uses Java's ForkJoinPool to fetch and parse web pages concurrently
- **JSON Configuration**: Load crawl settings (URLs, depth, timeout) from JSON config file
- **Thread-Safe Data Structures**: Uses ConcurrentHashMap for visited URLs and word counts
- **Performance Profiling**: Java Dynamic Proxies profile @Profiled methods automatically
- **Stream API Sorting**: Word counts sorted using Java Stream API
- **JSON Output**: Results (word counts, URLs visited) written to JSON file

## Project Structure

```
src/main/java/com/webcrawler/
├── Main.java                    # Application entry point
├── CrawlerConfiguration.java    # Configuration data class
├── ConfigurationLoader.java     # Loads config from JSON
├── WebCrawler.java             # Web crawler interface
├── ParallelWebCrawler.java     # ForkJoinPool-based parallel crawler
├── CrawlResult.java            # Result data class
├── ResultWriter.java           # Writes results to JSON
├── Profiled.java               # Annotation for profiling
└── PerformanceProfiler.java    # Dynamic proxy profiler

src/main/resources/
└── config.json                 # Default configuration

src/test/java/com/webcrawler/
├── CrawlerConfigurationTest.java
└── CrawlResultTest.java
```

## Configuration

Edit `src/main/resources/config.json`:

```json
{
  "startUrls": [
    "https://example.com"
  ],
  "maxDepth": 2,
  "timeoutSeconds": 5,
  "popularWordCount": 10,
  "outputPath": "output.json"
}
```

- `startUrls`: List of URLs to start crawling from
- `maxDepth`: Maximum depth to crawl (1 = start URLs only, 2 = start URLs + 1 level of links)
- `timeoutSeconds`: HTTP connection timeout in seconds
- `popularWordCount`: Number of top words to include in results
- `outputPath`: Path to output JSON file

## Building

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Crawler

```bash
# Build the project
mvn clean package

# Run with default config (src/main/resources/config.json)
java -jar target/parallel-web-crawler-1.0-SNAPSHOT.jar

# Run with custom config
java -jar target/parallel-web-crawler-1.0-SNAPSHOT.jar path/to/config.json
```

Or run directly with Maven:

```bash
mvn exec:java -Dexec.mainClass="com.webcrawler.Main"
```

## Output

The crawler produces a JSON file with:

```json
{
  "wordCounts": {
    "word1": 100,
    "word2": 50,
    ...
  },
  "urlsVisited": [
    "https://example.com",
    "https://example.com/page1",
    ...
  ]
}
```

## Implementation Details

### Parallel Crawling with ForkJoinPool

The `ParallelWebCrawler` uses a `RecursiveTask` to crawl pages concurrently:
- Each URL is processed as a separate task
- Tasks fork new subtasks for discovered links
- Thread-safe data structures prevent race conditions

### Performance Profiling

Methods annotated with `@Profiled` are automatically timed using Java Dynamic Proxies:
- ConfigurationLoader.load()
- WebCrawler.crawl()
- ResultWriter.write()

Timing information is printed to console during execution.

### Thread Safety

- `ConcurrentHashMap` for word counts
- `ConcurrentHashMap.newKeySet()` for visited URLs
- Atomic operations for concurrent updates

## Dependencies

- Gson 2.10.1 - JSON processing
- JSoup 1.16.1 - HTML parsing
- JUnit 4.13.2 - Testing (test scope)