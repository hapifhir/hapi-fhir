package org.hl7.fhir.cache.caffeine;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.hl7.fhir.cache.Cache;
import org.hl7.fhir.cache.CacheLoader;
import org.hl7.fhir.cache.LoadingCache;

public class CacheProvider<K,V> implements org.hl7.fhir.cache.CacheProvider<K,V> {

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
