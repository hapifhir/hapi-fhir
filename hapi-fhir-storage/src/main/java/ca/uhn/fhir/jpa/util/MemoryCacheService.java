package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class acts as a central spot for all of the many Caffeine caches we use in HAPI FHIR.
 * <p>
 * The API is super simplistic, and caches are all 1-minute, max 10000 entries for starters. We could definitely add nuance to this,
 * which will be much easier now that this is being centralized. Some logging/monitoring would be good too.
 */
public class MemoryCacheService {

	@Autowired
	DaoConfig myDaoConfig;

	private EnumMap<CacheEnum, Cache<?, ?>> myCaches;

	@PostConstruct
	public void start() {

		myCaches = new EnumMap<>(CacheEnum.class);

		for (CacheEnum next : CacheEnum.values()) {

			long timeoutSeconds;
			int maximumSize;

			switch (next) {
				case CONCEPT_TRANSLATION:
				case CONCEPT_TRANSLATION_REVERSE:
					timeoutSeconds = SECONDS.convert(myDaoConfig.getTranslationCachesExpireAfterWriteInMinutes(), MINUTES);
					maximumSize = 10000;
					break;
				case PID_TO_FORCED_ID:
				case FORCED_ID_TO_PID:
				case MATCH_URL:
				case RESOURCE_LOOKUP:
				case HISTORY_COUNT:
				case TAG_DEFINITION:
				case RESOURCE_CONDITIONAL_CREATE_VERSION:
				default:
					timeoutSeconds = SECONDS.convert(1, MINUTES);
					maximumSize = 10000;
					if (myDaoConfig.isMassIngestionMode()) {
						timeoutSeconds = SECONDS.convert(50, MINUTES);
						maximumSize = 100000;
					}
					break;
			}

			Cache<Object, Object> nextCache = Caffeine.newBuilder()
				.expireAfterWrite(timeoutSeconds, SECONDS)
				.maximumSize(maximumSize)
				.build();

			myCaches.put(next, nextCache);
		}

	}


	public <K, T> T get(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
		Cache<K, T> cache = getCache(theCache);
		return cache.get(theKey, theSupplier);
	}

	/**
	 * Fetch an item from the cache if it exists, and use the loading function to
	 * obtain it otherwise.
	 * <p>
	 * This method will put the value into the cache using {@link #putAfterCommit(CacheEnum, Object, Object)}.
	 */
	public <K, T> T getThenPutAfterCommit(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());

		Cache<K, T> cache = getCache(theCache);
		T retVal = cache.getIfPresent(theKey);
		if (retVal == null) {
			retVal = theSupplier.apply(theKey);
			putAfterCommit(theCache, theKey, retVal);
		}
		return retVal;
	}

	public <K, V> V getIfPresent(CacheEnum theCache, K theKey) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
		return (V) getCache(theCache).getIfPresent(theKey);
	}

	public <K, V> void put(CacheEnum theCache, K theKey, V theValue) {
		assert theCache.myKeyType.isAssignableFrom(theKey.getClass());
		getCache(theCache).put(theKey, theValue);
	}

	/**
	 * This method registers a transaction synchronization that puts an entry in the cache
	 * if and when the current database transaction successfully commits. If the
	 * transaction is rolled back, the key+value passed into this method will
	 * not be added to the cache.
	 * <p>
	 * This is useful for situations where you want to store something that has been
	 * resolved in the DB during the current transaction, but it's not yet guaranteed
	 * that this item will successfully save to the DB. Use this method in that case
	 * in order to avoid cache poisoning.
	 */
	public <K, V> void putAfterCommit(CacheEnum theCache, K theKey, V theValue) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					put(theCache, theKey, theValue);
				}
			});
		} else {
			put(theCache, theKey, theValue);
		}
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getAllPresent(CacheEnum theCache, Iterable<K> theKeys) {
		return (Map<K, V>) getCache(theCache).getAllPresent(theKeys);
	}

	public void invalidateAllCaches() {
		myCaches.values().forEach(Cache::invalidateAll);
	}

	private <K, T> Cache<K, T> getCache(CacheEnum theCache) {
		return (Cache<K, T>) myCaches.get(theCache);
	}

	public long getEstimatedSize(CacheEnum theCache) {
		return getCache(theCache).estimatedSize();
	}

	public enum CacheEnum {

		TAG_DEFINITION(TagDefinitionCacheKey.class),
		RESOURCE_LOOKUP(String.class),
		FORCED_ID_TO_PID(String.class),
		/**
		 * Key type: {@literal Long}
		 * Value type: {@literal Optional<String>}
		 */
		PID_TO_FORCED_ID(Long.class),
		CONCEPT_TRANSLATION(TranslationQuery.class),
		MATCH_URL(String.class),
		CONCEPT_TRANSLATION_REVERSE(TranslationQuery.class),
		RESOURCE_CONDITIONAL_CREATE_VERSION(Long.class),
		HISTORY_COUNT(HistoryCountKey.class);

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
	}


	public static class HistoryCountKey {
		private final String myTypeName;
		private final Long myInstanceId;
		private final int myHashCode;

		private HistoryCountKey(String theTypeName, Long theInstanceId) {
			myTypeName = theTypeName;
			myInstanceId = theInstanceId;
			myHashCode = new HashCodeBuilder().append(myTypeName).append(myInstanceId).toHashCode();
		}

		public static HistoryCountKey forSystem() {
			return new HistoryCountKey(null, null);
		}

		public static HistoryCountKey forType(@Nonnull String theType) {
			assert isNotBlank(theType);
			return new HistoryCountKey(theType, null);
		}

		public static HistoryCountKey forInstance(@Nonnull Long theInstanceId) {
			assert theInstanceId != null;
			return new HistoryCountKey(null, theInstanceId);
		}

		@Override
		public boolean equals(Object theO) {
			boolean retVal = false;
			if (theO instanceof HistoryCountKey) {
				HistoryCountKey that = (HistoryCountKey) theO;
				retVal = new EqualsBuilder().append(myTypeName, that.myTypeName).append(myInstanceId, that.myInstanceId).isEquals();
			}
			return retVal;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

	}

}
