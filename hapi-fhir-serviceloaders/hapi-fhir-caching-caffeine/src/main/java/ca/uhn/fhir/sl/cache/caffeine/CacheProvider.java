package ca.uhn.fhir.sl.cache.caffeine;

import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheLoader;
import ca.uhn.fhir.sl.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheProvider<K,V> implements ca.uhn.fhir.sl.cache.CacheProvider<K,V> {

	public Cache<K,V> create(long timeoutMillis) {
		return new CacheDelegator<K,V>(
			Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build()
		);
	 }

	public LoadingCache<K,V> create(long timeoutMillis, CacheLoader<K,V> loading) {
		return new LoadingCacheDelegator<K,V>(
			Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.build(loading::load)
		);
	}

	public Cache<K,V> create(long timeoutMillis, long maximumSize) {
		return new CacheDelegator<K,V>(
			Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build()
		);
	}

	public LoadingCache<K,V> create(long timeoutMillis, long maximumSize, CacheLoader<K,V> loading) {
		return new LoadingCacheDelegator<K,V>(
			Caffeine.newBuilder()
				.expireAfterWrite(timeoutMillis, TimeUnit.MILLISECONDS)
				.maximumSize(maximumSize)
				.build(loading::load)
		);
	}

}
