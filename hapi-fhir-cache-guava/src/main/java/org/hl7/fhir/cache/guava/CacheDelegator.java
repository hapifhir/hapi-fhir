package org.hl7.fhir.cache.guava;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class CacheDelegator<K, V> implements org.hl7.fhir.cache.Cache<K, V> {

	com.google.common.cache.Cache<K, V> cache;

	public CacheDelegator(com.google.common.cache.Cache<K, V> impl) {
		this.cache = impl;
	}

	public V getIfPresent(Object key) {
		return cache.getIfPresent(key);
	}

	public V get(K key, Function<? super K, ? extends V> mappingFunction) {
		try {
			return cache.get(key, () -> mappingFunction.apply(key));
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
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
		return cache.size();
	}

	public void cleanUp(){
		cache.cleanUp();
	}
}
