package org.hl7.fhir.cache.caffeine;

import java.util.Map;
import java.util.function.Function;

public class CacheDelegator<K, V> implements org.hl7.fhir.cache.Cache<K, V> {

	com.github.benmanes.caffeine.cache.Cache<K, V> cache;

	public CacheDelegator(com.github.benmanes.caffeine.cache.Cache<K, V> impl) {
		this.cache = impl;
	}

	public V getIfPresent(Object key) {
		return cache.getIfPresent(key);
	}

	public V get(K key, Function<? super K, ? extends V> mappingFunction) {
		return cache.get(key, mappingFunction);
	}

	public Map<K, V> getAllPresent(Iterable<? extends K> keys) { return cache.getAllPresent(keys); }

	public void put(K key, V value) {
		cache.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		cache.putAll(map);
	}

	public void invalidate(K key) { cache.invalidate(key); }

	public void invalidateAll(Iterable<? extends K> keys)  {
		cache.invalidateAll(keys);
	}

	public void invalidateAll() {
		cache.invalidateAll();
	}

	public long estimatedSize() {
		return cache.estimatedSize();
	}

	public void cleanUp(){
		cache.cleanUp();
	}
}
