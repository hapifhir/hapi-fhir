package ca.uhn.fhir.jpa.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
	 * This class acts as a central spot for all of the many Caffeine caches we use in HAPI FHIR.
 */
public class MemoryCacheService {

	private EnumMap<CacheEnum, Cache<?,?>> myCaches;

	@PostConstruct
	public void start() {

		myCaches = new EnumMap<>(CacheEnum.class);

		for (CacheEnum next : CacheEnum.values()) {
			Cache<Object, Object> nextCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(10000).build();
			myCaches.put(next, nextCache);
		}

	}


	public <K,T> T get(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		Cache<K, T> cache = (Cache<K, T>) myCaches.get(theCache);
		return cache.get(theKey, theSupplier);
	}


	public void invalidateAllCaches() {
		myCaches.values().forEach(t->t.invalidateAll());
	}

	public enum CacheEnum {

		TAG_DEFINITION

	}


}
