package org.hl7.fhir.cache.guava;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.hl7.fhir.cache.LoadingCache;

public class LoadingCacheDelegator<K, V> extends CacheDelegator<K, V> implements LoadingCache<K,V> {

	public LoadingCacheDelegator(com.google.common.cache.LoadingCache<K, V> impl) { super(impl); }

	public com.google.common.cache.LoadingCache<K, V> getCache() {
		return (com.google.common.cache.LoadingCache<K, V>) cache;
	}

	public V get(K key) {
		try {
			return getCache().get(key);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Map<K, V> getAll(Iterable<? extends K> keys) {
		try {
			return getCache().getAll(keys);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void refresh(K key) { getCache().refresh(key); }
}
