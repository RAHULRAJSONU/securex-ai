package io.github.rahulrajsonu.securexai.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;
    private final Lock lock = new ReentrantLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        try {
            lock.lock();
            return cache.get(key);
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        try {
            lock.lock();
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        try {
            lock.lock();
            cache.clear();
        } finally {
            lock.unlock();
        }
    }
}
