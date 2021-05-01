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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.TranslationQuery;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This class acts as a central spot for all of the many Caffeine caches we use in HAPI FHIR.
 * <p>
 * The API is super simplistic, and caches are all 1-minute, max 10000 entries for starters. We could definitely add nuance to this,
 * which will be much easier now that this is being centralized. Some logging/monitoring would be good too.
 */
public class MemoryCacheService {

	@Autowired
	private DaoConfig myDaoConfig;

	private EnumMap<CacheEnum, Cache<?, ?>> myCaches;

	@PostConstruct
	public void start() {

		myCaches = new EnumMap<>(CacheEnum.class);

		for (CacheEnum next : CacheEnum.values()) {

			long timeoutSeconds;
			switch (next) {
				case CONCEPT_TRANSLATION:
				case CONCEPT_TRANSLATION_REVERSE:
					timeoutSeconds = myDaoConfig.getTranslationCachesExpireAfterWriteInMinutes() * 1000;
					break;
				case TAG_DEFINITION:
				case PERSISTENT_ID:
				case RESOURCE_LOOKUP:
				case PID_TO_FORCED_ID:
				case FORCED_ID_TO_PID:
				case MATCH_URL:
				case RESOURCE_CONDITIONAL_CREATE_VERSION:
				default:
					timeoutSeconds = 60;
					break;
			}

			Cache<Object, Object> nextCache = Caffeine.newBuilder().expireAfterWrite(timeoutSeconds, TimeUnit.MINUTES).maximumSize(10000).build();
			myCaches.put(next, nextCache);
		}

	}


	public <K, T> T get(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
		Cache<K, T> cache = getCache(theCache);
		return cache.get(theKey, theSupplier);
	}

	public <K, V> V getIfPresent(CacheEnum theCache, K theKey) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
		return (V) getCache(theCache).getIfPresent(theKey);
	}

	public <K, V> void put(CacheEnum theCache, K theKey, V theValue) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
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

		TAG_DEFINITION(TagDefinitionCacheKey.class),
		PERSISTENT_ID(String.class),
		RESOURCE_LOOKUP(String.class),
		FORCED_ID_TO_PID(String.class),
		PID_TO_FORCED_ID(Long.class),
		CONCEPT_TRANSLATION(TranslationQuery.class),
		MATCH_URL(String.class),
		CONCEPT_TRANSLATION_REVERSE(TranslationQuery.class),
		RESOURCE_CONDITIONAL_CREATE_VERSION(IIdType.class);

		private final Class<?> myKeyType;

		CacheEnum(Class<?> theKeyType) {
			myKeyType = theKeyType;
		}
	}


	public static class TagDefinitionCacheKey {

		private final TagTypeEnum myType;
		private final String mySystem;
		private final String myCode;
		private final int myHashCode;

		@Override
		public boolean equals(Object theO) {
			boolean retVal = false;
			if (theO instanceof TagDefinitionCacheKey) {
				TagDefinitionCacheKey that = (TagDefinitionCacheKey) theO;

				retVal = new EqualsBuilder()
					.append(myType, that.myType)
					.append(mySystem, that.mySystem)
					.append(myCode, that.myCode)
					.isEquals();
			}
			return retVal;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		public TagDefinitionCacheKey(TagTypeEnum theType, String theSystem, String theCode) {
			myType = theType;
			mySystem = theSystem;
			myCode = theCode;
			myHashCode = new HashCodeBuilder(17, 37)
				.append(myType)
				.append(mySystem)
				.append(myCode)
				.toHashCode();
		}
	}

}
