package org.hl7.fhir.cache.caffeine;

import java.util.Map;
import java.util.function.Function;

public class CacheDelegator<K, V> implements org.hl7.fhir.cache.Cache<K, V> {

	com.github.benmanes.caffeine.cache.Cache<K, V> cache;

	public CacheDelegator(com.github.benmanes.caffeine.cache.Cache<K, V> impl) {
		this.cache = impl;
	}

	@Override
	public V getIfPresent(K key) {
		return cache.getIfPresent(key);
	}

	@Override
	public V get(K key, Function<? super K, ? extends V> mappingFunction) {
		return cache.get(key, mappingFunction);
	}

	@Override
	public Map<K, V> getAllPresent(Iterable<? extends K> keys) { return cache.getAllPresent(keys); }

	@Override
	public void put(K key, V value) {
		cache.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		cache.putAll(map);
	}

	@Override
	public void invalidate(K key) { cache.invalidate(key); }

	@Override
	public void invalidateAll(Iterable<? extends K> keys)  {
		cache.invalidateAll(keys);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}

	@Override
	public long estimatedSize() {
		return cache.estimatedSize();
	}

	@Override
	public void cleanUp(){
		cache.cleanUp();
	}
}
