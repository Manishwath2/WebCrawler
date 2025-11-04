package com.webcrawler;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Test for WordCounts utility
 */
public class WordCountsTest {

    @Test
    public void testSortByFrequency() {
        Map<String, Integer> words = new HashMap<>();
        words.put("apple", 10);
        words.put("banana", 5);
        words.put("cherry", 15);
        
        Map<String, Integer> sorted = WordCounts.sort(words, 10);
        
        List<String> keys = new ArrayList<>(sorted.keySet());
        assertEquals("cherry", keys.get(0)); // 15 occurrences
        assertEquals("apple", keys.get(1));  // 10 occurrences
        assertEquals("banana", keys.get(2)); // 5 occurrences
    }

    @Test
    public void testSortByLength() {
        Map<String, Integer> words = new HashMap<>();
        words.put("a", 5);
        words.put("abc", 5);
        words.put("ab", 5);
        
        Map<String, Integer> sorted = WordCounts.sort(words, 10);
        
        List<String> keys = new ArrayList<>(sorted.keySet());
        assertEquals("abc", keys.get(0)); // Longest word
        assertEquals("ab", keys.get(1));
        assertEquals("a", keys.get(2));   // Shortest word
    }

    @Test
    public void testSortAlphabetically() {
        Map<String, Integer> words = new HashMap<>();
        words.put("cat", 5);
        words.put("bat", 5);
        words.put("ant", 5);
        
        Map<String, Integer> sorted = WordCounts.sort(words, 10);
        
        List<String> keys = new ArrayList<>(sorted.keySet());
        assertEquals("ant", keys.get(0)); // Alphabetically first
        assertEquals("bat", keys.get(1));
        assertEquals("cat", keys.get(2)); // Alphabetically last
    }

    @Test
    public void testLimit() {
        Map<String, Integer> words = new HashMap<>();
        words.put("a", 1);
        words.put("b", 2);
        words.put("c", 3);
        words.put("d", 4);
        words.put("e", 5);
        
        Map<String, Integer> sorted = WordCounts.sort(words, 3);
        
        assertEquals(3, sorted.size());
        assertTrue(sorted.containsKey("e"));
        assertTrue(sorted.containsKey("d"));
        assertTrue(sorted.containsKey("c"));
    }
}
