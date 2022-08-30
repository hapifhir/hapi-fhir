package org.hl7.fhir.cache;

public interface CacheProvider<K,V> {
    Cache create(long timeoutMillis);

    Cache create(long timeoutMillis, long maximumSize);

    LoadingCache create(long timeoutMillis, CacheLoader<K,V> cacheLoader);

    LoadingCache create(long timeoutMillis, long maximumSize, CacheLoader<K,V> cacheLoader);
}
