package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This class acts as a central spot for all of the many Caffeine caches we use in HAPI FHIR.
 * <p>
 * The API is super simplistic, and caches are all 1-minute, max 10000 entries for starters. We could definitely add nuance to this,
 * which will be much easier now that this is being centralized. Some logging/monitoring would be good too.
 */
public class MemoryCacheService {

	private EnumMap<CacheEnum, Cache<?, ?>> myCaches;

	@PostConstruct
	public void start() {

		myCaches = new EnumMap<>(CacheEnum.class);

		for (CacheEnum next : CacheEnum.values()) {
			Cache<Object, Object> nextCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(10000).build();
			myCaches.put(next, nextCache);
		}

	}


	public <K, T> T get(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		Cache<K, T> cache = getCache(theCache);
		return cache.get(theKey, theSupplier);
	}

	public <K, V> V getIfPresent(CacheEnum theCache, K theKey) {
		return (V) getCache(theCache).getIfPresent(theKey);
	}

	public <K, V> void put(CacheEnum theCache, K theKey, V theValue) {
		getCache(theCache).put(theKey, theValue);
	}

	public <K, V> Map<K, V> getAllPresent(CacheEnum theCache, Iterable<K> theKeys) {
		return (Map<K, V>) getCache(theCache).getAllPresent(theKeys);
	}

	public void invalidateAllCaches() {
		myCaches.values().forEach(t -> t.invalidateAll());
	}

	private <K, T> Cache<K, T> getCache(CacheEnum theCache) {
		return (Cache<K, T>) myCaches.get(theCache);
	}

	public enum CacheEnum {

		TAG_DEFINITION,
		PERSISTENT_ID,
		RESOURCE_LOOKUP,
		FORCED_ID,

	}


}
