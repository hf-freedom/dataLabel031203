package com.example.todoplugin.map;

import java.util.concurrent.ConcurrentHashMap;

public class SICache {
    private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static Object get(String key) {
        return cache.get(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public static ConcurrentHashMap<String, Object> getAll() {
        return new ConcurrentHashMap<>(cache);
    }
}
