package ca.uhn.fhir.sl.cache.caffeine;

import ca.uhn.fhir.sl.cache.LoadingCache;

import java.util.Map;

public class LoadingCacheDelegator<K, V> extends CacheDelegator<K, V> implements LoadingCache<K,V> {

	public LoadingCacheDelegator(com.github.benmanes.caffeine.cache.LoadingCache<K, V> impl) {
		super(impl);
	}

	public com.github.benmanes.caffeine.cache.LoadingCache<K, V> getCache() {
		return (com.github.benmanes.caffeine.cache.LoadingCache<K, V>) cache;
	}

	@Override
	public V get(K key) {
		return getCache().get(key);
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		return getCache().getAll(keys);
	}

	@Override
	public void refresh(K key) { getCache().refresh(key); }
}
