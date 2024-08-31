package me.yukun.spaceflares.util;

/**
 * Simple pair class to store K-V pairs in order.
 *
 * @param key   First value in pair.
 * @param value Second value in pair.
 * @param <K>   Type of first value in pair.
 * @param <V>   Type of second value in pair.
 */
public record Pair<K, V>(K key, V value) {

}
