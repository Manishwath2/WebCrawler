package com.webcrawler;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for sorting word counts with multi-level criteria.
 */
public final class WordCounts {
    
    private WordCounts() {
        // Utility class - prevent instantiation
    }

    /**
     * Sorts word counts using Stream API with multi-level sorting:
     * 1. By frequency (descending)
     * 2. By word length (descending) - tie breaker
     * 3. By alphabetical order (ascending) - final tie breaker
     * 
     * @param wordCounts Map of words to their frequencies
     * @param limit Maximum number of entries to return
     * @return Sorted and limited map maintaining insertion order
     */
    public static Map<String, Integer> sort(Map<String, Integer> wordCounts, int limit) {
        return wordCounts.entrySet()
                .stream()
                .sorted(
                    Comparator
                        .<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(e -> e.getKey().length(), Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey)
                )
                .limit(limit)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
}
