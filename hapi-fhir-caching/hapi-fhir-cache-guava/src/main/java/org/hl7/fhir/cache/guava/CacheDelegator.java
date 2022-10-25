package org.hl7.fhir.cache.guava;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;

public class CacheDelegator<K, V> implements org.hl7.fhir.cache.Cache<K, V> {

	com.google.common.cache.Cache<K, V> cache;

	public CacheDelegator(com.google.common.cache.Cache<K, V> impl) {
		this.cache = impl;
	}

	@Override
	public V getIfPresent(K key) {
		return cache.getIfPresent(key);
	}

	@Override
	public V get(K key, Function<? super K, ? extends V> mappingFunction) {
		try {
			return cache.get(key, () -> mappingFunction.apply(key));
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException)e.getCause();
			}
			throw e;
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception
			// This matches the behaviour of Caffeine
			return null;
		}
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
		return cache.size();
	}

	@Override
	public void cleanUp(){
		cache.cleanUp();
	}
}
