package ca.uhn.fhir.sl.cache.guava;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.sl.cache.LoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoadingCacheDelegator<K, V> extends CacheDelegator<K, V> implements LoadingCache<K,V> {

	public LoadingCacheDelegator(com.google.common.cache.LoadingCache<K, V> impl) { super(impl); }

	public com.google.common.cache.LoadingCache<K, V> getCache() {
		return (com.google.common.cache.LoadingCache<K, V>) cache;
	}

	@Override
	public V get(K key) {
		try {
			return getCache().get(key);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException)e.getCause();
			}
			throw e;
		} catch (ExecutionException e) {
			throw new RuntimeException(Msg.code(2204) + "Failed to red from cache", e);
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception.
			// This matches the behaviour of Caffeine
			return null;
		}
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		try {
			return getCache().getAll(keys);
		} catch (UncheckedExecutionException e) {
			if (e.getCause() instanceof RuntimeException) {
				// Unwrap exception to match Caffeine
				throw (RuntimeException)e.getCause();
			}
			throw e;
		} catch (ExecutionException e) {
			throw new RuntimeException(Msg.code(2205) + "Failed to red from cache", e);
		} catch (CacheLoader.InvalidCacheLoadException e) {
			// If the entry is not found or load as null, returns null instead of an exception
			// This matches the behaviour of Caffeine
			return null;
		}
	}

	@Override
	public void refresh(K key) { getCache().refresh(key); }
}
