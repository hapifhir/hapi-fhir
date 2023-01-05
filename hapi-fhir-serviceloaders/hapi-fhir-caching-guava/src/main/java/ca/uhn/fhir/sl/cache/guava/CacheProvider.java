package ca.uhn.fhir.sl.cache.guava;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheLoader;
import ca.uhn.fhir.sl.cache.LoadingCache;

public class CacheProvider<K,V> implements ca.uhn.fhir.sl.cache.CacheProvider<K,V> {

	public Cache<K,V> create(long timeoutMillis) {
		return new CacheDelegator<K,V>(
			CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build()
		);
	 }

	public LoadingCache<K,V> create(long timeoutMillis, CacheLoader<K,V> loading) {
		return new LoadingCacheDelegator<K,V>(
			CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build(new com.google.common.cache.CacheLoader<>() {
					@Override
					public V load(K k) throws Exception {
						return loading.load(k);
					}
				})
		);
	}

	public Cache<K,V> create(long timeoutMillis, long maximumSize) {
		return new CacheDelegator<K,V>(
			CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build()
		);
	}

	public LoadingCache<K,V> create(long timeoutMillis, long maximumSize, CacheLoader<K,V> loading) {
		return new LoadingCacheDelegator<K,V>(
			CacheBuilder.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build(new com.google.common.cache.CacheLoader<>() {
					@Override
					public V load(K k) throws Exception {
						return loading.load(k);
					}
				})
		);
	}

}
