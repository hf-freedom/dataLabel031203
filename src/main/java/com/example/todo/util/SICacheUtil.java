package com.example.todo.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存工具类
 */
public class SICacheUtil {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private SICacheUtil() {
    }

    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }

    public static Object get(String key) {
        return CACHE.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        Object value = CACHE.get(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    public static void remove(String key) {
        CACHE.remove(key);
    }

    public static boolean containsKey(String key) {
        return CACHE.containsKey(key);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static Map<String, Object> getAll() {
        return new ConcurrentHashMap<>(CACHE);
    }
}
