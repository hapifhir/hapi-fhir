/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
// TODO: JA2 extract an interface for this class and use it everywhere
public class MemoryCacheService {

	private final JpaStorageSettings myStorageSettings;
	private final EnumMap<CacheEnum, Cache<?, ?>> myCaches = new EnumMap<>(CacheEnum.class);

	public MemoryCacheService(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;

		populateCaches();
	}

	private void populateCaches() {
		for (CacheEnum next : CacheEnum.values()) {

			long timeoutSeconds;
			int maximumSize;

			Cache<Object, Object> nextCache;
			switch (next) {
				case RES_TYPE_TO_RES_TYPE_ID:
					nextCache = CacheFactory.buildEternal(250, 500);
					break;
				case HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY:
					nextCache = CacheFactory.buildEternal(5_000, 50_000);
					break;
				case RESOURCE_IDENTIFIER_SYSTEM_TO_PID:
					nextCache = CacheFactory.buildEternal(250, 1_000);
					break;
				case PATIENT_IDENTIFIER_TO_FHIR_ID:
				case NAME_TO_PARTITION:
				case ID_TO_PARTITION:
				case PID_TO_FORCED_ID:
				case MATCH_URL:
				case RESOURCE_LOOKUP_BY_FORCED_ID:
				case HISTORY_COUNT:
				case TAG_DEFINITION:
				case RESOURCE_CONDITIONAL_CREATE_VERSION:
				case FHIRPATH_EXPRESSION:
				default:
					timeoutSeconds = SECONDS.convert(1, MINUTES);
					maximumSize = 10000;
					if (myStorageSettings.isMassIngestionMode()) {
						timeoutSeconds = SECONDS.convert(50, MINUTES);
						maximumSize = 100000;
					}
					nextCache = CacheFactory.build(SECONDS.toMillis(timeoutSeconds), maximumSize);
					break;
			}

			myCaches.put(next, nextCache);
		}
	}

	public <K, T> T get(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass());
		return doGet(theCache, theKey, theSupplier);
	}

	protected <K, T> T doGet(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
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
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass());
		T retVal = getIfPresent(theCache, theKey);
		if (retVal == null) {
			retVal = theSupplier.apply(theKey);
			putAfterCommit(theCache, theKey, retVal);
		}
		return retVal;
	}

	/**
	 * Fetch an item from the cache if it exists and use the loading function to
	 * obtain it otherwise. If the loading function returns null, the item will not
	 * be placed in the cache and <code>null</code> will be returned.
	 * <p>
	 * This method will put the value into the cache using {@link #putAfterCommit(CacheEnum, Object, Object)}.
	 *
	 * @since 8.6.0
	 */
	public <K, T> T getThenPutAfterCommitIfNotNull(CacheEnum theCache, K theKey, Function<K, T> theSupplier) {
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass());
		T retVal = getIfPresent(theCache, theKey);
		if (retVal == null) {
			retVal = theSupplier.apply(theKey);
			if (retVal != null) {
				putAfterCommit(theCache, theKey, retVal);
			}
		}
		return retVal;
	}

	public <K, V> V getIfPresent(CacheEnum theCache, K theKey) {
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass());
		return doGetIfPresent(theCache, theKey);
	}

	protected <K, V> V doGetIfPresent(CacheEnum theCache, K theKey) {
		return (V) getCache(theCache).getIfPresent(theKey);
	}

	public <K, V> void put(CacheEnum theCache, K theKey, V theValue) {
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass())
				: "Key type " + theKey.getClass() + " doesn't match expected " + theCache.getKeyType() + " for cache "
						+ theCache;
		doPut(theCache, theKey, theValue);
	}

	protected <K, V> void doPut(CacheEnum theCache, K theKey, V theValue) {
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
		assert theCache.getKeyType().isAssignableFrom(theKey.getClass())
				: "Key type " + theKey.getClass() + " doesn't match expected " + theCache.getKeyType() + " for cache "
						+ theCache;

		HapiTransactionService.executeAfterCommitOrExecuteNowIfNoTransactionIsActive(
				() -> put(theCache, theKey, theValue));
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getAllPresent(CacheEnum theCache, Collection<K> theKeys) {
		return doGetAllPresent(theCache, theKeys);
	}

	@SuppressWarnings("unchecked")
	protected <K, V> Map<K, V> doGetAllPresent(CacheEnum theCache, Collection<K> theKeys) {
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

	public void invalidateCaches(CacheEnum... theCaches) {
		for (CacheEnum next : theCaches) {
			getCache(next).invalidateAll();
		}
	}

	public enum CacheEnum {
		TAG_DEFINITION(TagDefinitionCacheKey.class),
		/**
		 * Key type: {@link ForcedIdCacheKey}
		 * Value type: {@literal JpaResourceLookup}
		 */
		RESOURCE_LOOKUP_BY_FORCED_ID(ForcedIdCacheKey.class),
		FHIRPATH_EXPRESSION(String.class),
		/**
		 * Key type: {@literal Long}
		 * Value type: {@literal Optional<String>}
		 */
		PID_TO_FORCED_ID(JpaPid.class),
		MATCH_URL(String.class),
		RESOURCE_CONDITIONAL_CREATE_VERSION(JpaPid.class),
		HISTORY_COUNT(HistoryCountKey.class),
		NAME_TO_PARTITION(String.class),
		ID_TO_PARTITION(Integer.class),
		HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY(Long.class),
		RES_TYPE_TO_RES_TYPE_ID(String.class),
		RESOURCE_IDENTIFIER_SYSTEM_TO_PID(String.class),
		PATIENT_IDENTIFIER_TO_FHIR_ID(IdentifierKey.class);

		private final Class<?> myKeyType;

		CacheEnum(Class<?> theKeyType) {
			myKeyType = theKeyType;
		}

		public Class<?> getKeyType() {
			return myKeyType;
		}
	}

	public record IdentifierKey(String system, String value) {}

	public static class TagDefinitionCacheKey {

		private final TagTypeEnum myType;
		private final String mySystem;
		private final String myCode;
		private final String myVersion;
		private final int myHashCode;
		private Boolean myUserSelected;

		public TagDefinitionCacheKey(
				TagTypeEnum theType, String theSystem, String theCode, String theVersion, Boolean theUserSelected) {
			myType = theType;
			mySystem = theSystem;
			myCode = theCode;
			myVersion = theVersion;
			myUserSelected = theUserSelected;
			myHashCode = new HashCodeBuilder(17, 37)
					.append(myType)
					.append(mySystem)
					.append(myCode)
					.append(myVersion)
					.append(myUserSelected)
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
		private final Integer myPartitionId;
		private final int myHashCode;

		private HistoryCountKey(@Nullable String theTypeName, @Nullable JpaPid theInstanceId) {
			myTypeName = theTypeName;
			if (theInstanceId != null) {
				myInstanceId = theInstanceId.getId();
				myPartitionId = theInstanceId.getPartitionId();
			} else {
				myInstanceId = null;
				myPartitionId = null;
			}
			myHashCode = new HashCodeBuilder()
					.append(myTypeName)
					.append(myInstanceId)
					.append(myPartitionId)
					.toHashCode();
		}

		@Override
		public boolean equals(Object theO) {
			boolean retVal = false;
			if (theO instanceof HistoryCountKey) {
				HistoryCountKey that = (HistoryCountKey) theO;
				retVal = new EqualsBuilder()
						.append(myTypeName, that.myTypeName)
						.append(myInstanceId, that.myInstanceId)
						.isEquals();
			}
			return retVal;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		public static HistoryCountKey forSystem() {
			return new HistoryCountKey(null, null);
		}

		public static HistoryCountKey forType(@Nonnull String theType) {
			assert isNotBlank(theType);
			return new HistoryCountKey(theType, null);
		}

		public static HistoryCountKey forInstance(@Nonnull JpaPid theInstanceId) {
			return new HistoryCountKey(null, theInstanceId);
		}
	}

	public static class ForcedIdCacheKey {

		private final String myResourceType;
		private final String myResourceId;
		private final List<Integer> myRequestPartitionIds;
		private final int myHashCode;

		public ForcedIdCacheKey(
				@Nullable String theResourceType,
				@Nonnull String theResourceId,
				@Nonnull RequestPartitionId theRequestPartitionId) {
			myResourceType = theResourceType;
			myResourceId = theResourceId;
			if (theRequestPartitionId.hasPartitionIds()) {
				myRequestPartitionIds = theRequestPartitionId.getPartitionIds();
			} else {
				myRequestPartitionIds = null;
			}
			myHashCode = Objects.hash(myResourceType, myResourceId, myRequestPartitionIds);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("resType", myResourceType)
					.append("resId", myResourceId)
					.append("partId", myRequestPartitionIds)
					.toString();
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}
			if (!(theO instanceof ForcedIdCacheKey)) {
				return false;
			}
			ForcedIdCacheKey that = (ForcedIdCacheKey) theO;
			return Objects.equals(myResourceType, that.myResourceType)
					&& Objects.equals(myResourceId, that.myResourceId)
					&& Objects.equals(myRequestPartitionIds, that.myRequestPartitionIds);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		/**
		 * Creates and returns a new unqualified versionless IIdType instance
		 */
		public IIdType toIdType(FhirContext theFhirCtx) {
			return theFhirCtx.getVersion().newIdType(myResourceType, myResourceId);
		}
	}
}
