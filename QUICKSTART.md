# Quick Start Guide

## Prerequisites
- Java 11 or higher
- Maven 3.6+

## Installation

```bash
# Clone the repository
git clone https://github.com/Manishwath2/WebCrawler.git
cd WebCrawler

# Build the project
mvn clean package
```

## Configuration

Create or edit `config.json`:

```json
{
  "startUrls": [
    "https://example.com",
    "https://another-site.com"
  ],
  "maxDepth": 2,
  "timeoutSeconds": 5,
  "popularWordCount": 10,
  "outputPath": "output.json"
}
```

**Parameters:**
- `startUrls`: Array of URLs to start crawling
- `maxDepth`: How deep to crawl (1 = start URLs only, 2 = start + 1 level of links)
- `timeoutSeconds`: HTTP connection timeout
- `popularWordCount`: Number of top words to include in results
- `outputPath`: Where to save results

## Running

```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.webcrawler.Main" -Dexec.args="config.json"

# Using JAR
java -jar target/parallel-web-crawler-1.0-SNAPSHOT.jar config.json

# With default config (src/main/resources/config.json)
java -jar target/parallel-web-crawler-1.0-SNAPSHOT.jar
```

## Example Output

```
=== Parallel Web Crawler ===
Loading configuration from: config.json
[PROFILER] load took 8.36 ms
Configuration loaded:
  Start URLs: [https://example.com]
  Max Depth: 2
  Timeout: 5 seconds
  Popular Words: 10
  Output: output.json

Starting crawl...
[PROFILER] crawl took 2543.45 ms

Crawl completed:
  URLs visited: 15
  Unique words: 342

Writing results...
Results written to: output.json
[PROFILER] write took 24.95 ms

=== Crawl Complete ===
```

## Output Format

The `output.json` file contains:

```json
{
  "wordCounts": {
    "the": 145,
    "and": 98,
    "to": 87,
    "of": 76,
    "a": 65
  },
  "urlsVisited": [
    "https://example.com",
    "https://example.com/page1",
    "https://example.com/page2"
  ]
}
```

## Features

- ⚡ **Parallel Processing**: Uses Java ForkJoinPool for concurrent crawling
- 🔒 **Thread-Safe**: ConcurrentHashMap ensures data integrity
- ⏱️ **Performance Profiling**: Automatic timing of key operations
- 📊 **Smart Sorting**: Stream API sorts words by frequency
- 🎯 **Configurable**: JSON-based configuration
- 🛡️ **Secure**: Zero vulnerabilities (CodeQL verified)

## Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=CrawlerConfigurationTest
```

## Troubleshooting

**Issue**: `java.net.SocketTimeoutException`
- **Solution**: Increase `timeoutSeconds` in config.json

**Issue**: Too many URLs visited
- **Solution**: Decrease `maxDepth` in config.json

**Issue**: OutOfMemoryError
- **Solution**: Reduce `maxDepth` or run with: `java -Xmx2g -jar ...`

## Performance Tips

1. Adjust `maxDepth` based on your needs (lower = faster)
2. Increase `timeoutSeconds` for slow websites
3. Use specific start URLs to focus the crawl
4. Monitor memory usage for large crawls

## Advanced Usage

### Custom ForkJoinPool Size

Edit `ParallelWebCrawler.java`:
```java
this.forkJoinPool = new ForkJoinPool(4); // 4 threads
```

### Custom Word Filtering

Edit `countWords()` method in `ParallelWebCrawler.java` to filter words by length, pattern, etc.

## Support

For issues or questions, please create an issue in the GitHub repository.
