/*
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.api.config;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.jpa.api.model.WarmCacheEntry;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.validation.FhirValidator;
import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("JavadocLinkAsPlainText")
public class JpaStorageSettings extends StorageSettings {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaStorageSettings.class);

	/**
	 * Default value for {@link #getBulkExportFileMaximumSize()}: 100 MB
	 */
	public static final long DEFAULT_BULK_EXPORT_MAXIMUM_WORK_CHUNK_SIZE = 100 * FileUtils.ONE_MB;
	/**
	 * Default value for {@link #setReuseCachedSearchResultsForMillis(Long)}: 60000ms (one minute)
	 */
	public static final Long DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS = DateUtils.MILLIS_PER_MINUTE;
	/**
	 * Default value for {@link #myTranslationCachesExpireAfterWriteInMinutes}: 60 minutes
	 *
	 * @see #myTranslationCachesExpireAfterWriteInMinutes
	 */
	public static final Long DEFAULT_TRANSLATION_CACHES_EXPIRE_AFTER_WRITE_IN_MINUTES = 60L;
	/**
	 * Default {@link #setBundleTypesAllowedForStorage(Set)} value:
	 * <ul>
	 * <li>collection</li>
	 * <li>document</li>
	 * <li>message</li>
	 * </ul>
	 */
	@SuppressWarnings("WeakerAccess")
	public static final Set<String> DEFAULT_BUNDLE_TYPES_ALLOWED_FOR_STORAGE =
			Collections.unmodifiableSet(new TreeSet<>(Sets.newHashSet(
					Bundle.BundleType.COLLECTION.toCode(),
					Bundle.BundleType.DOCUMENT.toCode(),
					Bundle.BundleType.MESSAGE.toCode())));
	// update setter javadoc if default changes
	public static final int DEFAULT_MAX_EXPANSION_SIZE = 1000;
	public static final HistoryCountModeEnum DEFAULT_HISTORY_COUNT_MODE =
			HistoryCountModeEnum.CACHED_ONLY_WITHOUT_OFFSET;
	/**
	 * This constant applies to task enablement, e.g. {@link #setEnableTaskStaleSearchCleanup(boolean)}.
	 * <p>
	 * By default, all are enabled.
	 */
	public static final boolean DEFAULT_ENABLE_TASKS = true;

	public static final int DEFAULT_MAXIMUM_INCLUDES_TO_LOAD_PER_PAGE = 1000;

	public static final int DEFAULT_EXPUNGE_BATCH_SIZE = 800;
	public static final int DEFAULT_BUNDLE_BATCH_QUEUE_CAPACITY = 200;

	public static final int DEFAULT_BULK_EXPORT_FILE_MAXIMUM_CAPACITY = 1_000;
	/**
	 * Default value for {@link #setMaximumSearchResultCountInTransaction(Integer)}
	 *
	 * @see #setMaximumSearchResultCountInTransaction(Integer)
	 */
	private static final Integer DEFAULT_MAXIMUM_SEARCH_RESULT_COUNT_IN_TRANSACTION = null;

	private static final int DEFAULT_REINDEX_BATCH_SIZE = 800;
	private static final int DEFAULT_MAXIMUM_DELETE_CONFLICT_COUNT = 60;
	/**
	 * Child Configurations
	 */
	private static final Integer DEFAULT_INTERNAL_SYNCHRONOUS_SEARCH_SIZE = 10000;

	private static final boolean DEFAULT_PREVENT_INVALIDATING_CONDITIONAL_MATCH_CRITERIA = false;
	private static final long DEFAULT_REST_DELETE_BY_URL_RESOURCE_ID_THRESHOLD = 10000;

	/**
	 * If we are batching write operations in transactions, what should the maximum number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public static final String DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE_STRING = "10000";

	public static final int DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE =
			Integer.parseInt(DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE_STRING);

	/**
	 * If we are batching write operations in transactions, what should the default number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public static final String DEFAULT_TRANSACTION_ENTRIES_FOR_WRITE_STRING = "1024";

	public static final int DEFAULT_TRANSACTION_ENTRIES_FOR_WRITE =
			Integer.parseInt(DEFAULT_TRANSACTION_ENTRIES_FOR_WRITE_STRING);

	public static final List<Integer> DEFAULT_SEARCH_PRE_FETCH_THRESHOLDS = Arrays.asList(13, 503, 2003, 1000003, -1);

	/**
	 * Do not change default of {@code 0}!
	 *
	 * @since 4.1.0
	 */
	private final int myPreExpandValueSetsDefaultOffset = 0;
	/**
	 * update setter javadoc if default changes
	 */
	@Nonnull
	private final Long myTranslationCachesExpireAfterWriteInMinutes =
			DEFAULT_TRANSLATION_CACHES_EXPIRE_AFTER_WRITE_IN_MINUTES;
	/**
	 * @since 5.5.0
	 */
	@Nullable
	private Integer myMaximumIncludesToLoadPerPage = DEFAULT_MAXIMUM_INCLUDES_TO_LOAD_PER_PAGE;
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowInlineMatchUrlReferences = true;

	private boolean myAllowMultipleDelete;
	/**
	 * update setter javadoc if default changes
	 */
	private int myDeferIndexingForCodesystemsOfSize = 100;

	private boolean myDeleteStaleSearches = true;
	private boolean myEnforceReferentialIntegrityOnDelete = true;
	private Set<String> myEnforceReferentialIntegrityOnDeleteDisableForPaths = Collections.emptySet();
	private boolean myUniqueIndexesEnabled = true;
	private boolean myUniqueIndexesCheckedBeforeSave = true;
	private boolean myEnforceReferentialIntegrityOnWrite = true;
	private SearchTotalModeEnum myDefaultTotalMode = null;
	private int myEverythingIncludesFetchPageSize = 50;
	/**
	 * update setter javadoc if default changes
	 */
	private long myExpireSearchResultsAfterMillis = DateUtils.MILLIS_PER_HOUR;
	/**
	 * update setter javadoc if default changes
	 */
	private Integer myFetchSizeDefaultMaximum = null;

	private int myMaximumExpansionSize = DEFAULT_MAX_EXPANSION_SIZE;
	private Integer myMaximumSearchResultCountInTransaction = DEFAULT_MAXIMUM_SEARCH_RESULT_COUNT_IN_TRANSACTION;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;
	/**
	 * update setter javadoc if default changes
	 */
	private Integer myResourceMetaCountHardLimit = 1000;

	private Long myReuseCachedSearchResultsForMillis = DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS;
	private boolean mySchedulingDisabled;
	private boolean mySuppressUpdatesWithNoChange = true;
	private Integer myCacheControlNoStoreMaxResultsUpperLimit = 1000;
	private Integer myCountSearchResultsUpTo = null;
	private boolean myStatusBasedReindexingDisabled;
	private IdStrategyEnum myResourceServerIdStrategy = IdStrategyEnum.SEQUENTIAL_NUMERIC;
	private boolean myMarkResourcesForReindexingUponSearchParameterChange;
	private boolean myExpungeEnabled;
	private boolean myDeleteExpungeEnabled;
	private int myExpungeBatchSize = DEFAULT_EXPUNGE_BATCH_SIZE;
	private int myReindexThreadCount;
	private int myExpungeThreadCount;
	private Set<String> myBundleTypesAllowedForStorage;
	private boolean myValidateSearchParameterExpressionsOnSave = true;

	// start with a tiny number so our first page always loads quickly.
	// If they fetch the second page, fetch more.
	// we'll only fetch (by default) up to 1 million records, because after that, deduplication in local memory is
	// prohibitive
	private List<Integer> mySearchPreFetchThresholds = DEFAULT_SEARCH_PRE_FETCH_THRESHOLDS;
	private List<WarmCacheEntry> myWarmCacheEntries = new ArrayList<>();
	private boolean myEnforceReferenceTargetTypes = true;
	private ClientIdStrategyEnum myResourceClientIdStrategy = ClientIdStrategyEnum.ALPHANUMERIC;
	private boolean myFilterParameterEnabled = false;
	private StoreMetaSourceInformationEnum myStoreMetaSourceInformation =
			StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID;
	private HistoryCountModeEnum myHistoryCountMode = DEFAULT_HISTORY_COUNT_MODE;
	private int myInternalSynchronousSearchSize = DEFAULT_INTERNAL_SYNCHRONOUS_SEARCH_SIZE;
	/**
	 * update setter javadoc if default changes
	 */
	private Integer myMaximumDeleteConflictQueryCount = DEFAULT_MAXIMUM_DELETE_CONFLICT_COUNT;
	/**
	 * Do not change default of {@code true}!
	 *
	 * @since 4.1.0
	 */
	private boolean myPreExpandValueSets = true;
	/**
	 * Do not change default of {@code 1000}!
	 *
	 * @since 4.1.0
	 */
	private int myPreExpandValueSetsDefaultCount = 1000;
	/**
	 * Do not change default of {@code 1000}!
	 *
	 * @since 4.1.0
	 */
	private int myPreExpandValueSetsMaxCount = 1000;
	/**
	 * Do not change default of {@code true}!
	 *
	 * @since 4.2.0
	 */
	private boolean myPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets = true;
	/**
	 * @since 5.0.0
	 */
	private boolean myDeleteEnabled = true;
	/**
	 * @since 5.1.0
	 */
	private boolean myLastNEnabled = false;
	/**
	 * @since 5.4.0
	 */
	private boolean myMatchUrlCacheEnabled;
	/**
	 * @since 5.5.0
	 */
	private boolean myEnableTaskBulkImportJobExecution;
	/**
	 * @since 5.5.0
	 */
	private boolean myEnableTaskStaleSearchCleanup;
	/**
	 * @since 5.5.0
	 */
	private boolean myEnableTaskPreExpandValueSets;
	/**
	 * @since 5.5.0
	 */
	private boolean myEnableTaskResourceReindexing;
	/**
	 * @since 5.5.0
	 */
	private boolean myEnableTaskBulkExportJobExecution;

	private boolean myAccountForDateIndexNulls;
	/**
	 * @since 5.6.0
	 */
	private String myHSearchIndexPrefix;

	/**
	 * Activates the new HSearch indexing of search parameters.
	 * When active, string, token, and reference parameters will be indexed and
	 * queried within Hibernate Search.
	 *
	 * @since 5.6.0
	 */
	private boolean myHibernateSearchIndexSearchParams = false;

	/**
	 * @since 5.7.0
	 */
	private boolean myStoreResourceInHSearchIndex;

	/**
	 * @see FhirValidator#isConcurrentBundleValidation()
	 * @since 5.7.0
	 */
	private boolean myConcurrentBundleValidation;

	/**
	 * Since 6.0.0
	 */
	private boolean myAllowAutoInflateBinaries = true;
	/**
	 * Since 6.0.0
	 */
	private long myAutoInflateBinariesMaximumBytes = 10 * FileUtils.ONE_MB;

	/**
	 * Since 6.0.0
	 */
	private int myBulkExportFileRetentionPeriodHours = 2;

	/**
	 * Since 6.2.0
	 */
	private boolean myEnableBulkExportJobReuse = true;

	/**
	 * Since 6.1.0
	 */
	private boolean myUpdateWithHistoryRewriteEnabled = false;

	/**
	 * Since 6.2.0
	 */
	private boolean myPreserveRequestIdInResourceBody = false;

	/**
	 * Since 6.2.0
	 */
	private int myBulkExportFileMaximumCapacity = DEFAULT_BULK_EXPORT_FILE_MAXIMUM_CAPACITY;
	/**
	 * Since 7.2.0
	 */
	private long myBulkExportFileMaximumSize = DEFAULT_BULK_EXPORT_MAXIMUM_WORK_CHUNK_SIZE;
	/**
	 * Since 6.4.0
	 */
	private boolean myJobFastTrackingEnabled = false;

	/**
	 * Since 6.6.0
	 * Applies to MDM links.
	 */
	private boolean myNonResourceDbHistoryEnabled = true;
	/**
	 * Since 7.0.0
	 */
	private boolean myResourceHistoryDbEnabled = true;

	/**
	 * @since 7.0.0
	 */
	@Nonnull
	private IValidationSupport.IssueSeverity myIssueSeverityForCodeDisplayMismatch =
			IValidationSupport.IssueSeverity.WARNING;

	/**
	 * This setting allows preventing a conditional update to invalidate the match criteria.
	 * <p/>
	 * By default, this is disabled unless explicitly enabled.
	 *
	 * @since 6.8.2
	 */
	private boolean myPreventInvalidatingConditionalMatchCriteria =
			DEFAULT_PREVENT_INVALIDATING_CONDITIONAL_MATCH_CRITERIA;

	/**
	 * This setting helps to enforce a threshold in number of resolved resources for DELETE by URL REST calls
	 *
	 * @since 7.2.0
	 */
	private long myRestDeleteByUrlResourceIdThreshold = DEFAULT_REST_DELETE_BY_URL_RESOURCE_ID_THRESHOLD;

	/**
	 * If enabled, this setting causes persisting data to legacy LOB columns as well as columns introduced
	 * to migrate away from LOB columns which effectively duplicates stored information.
	 *
	 * @since 7.2.0
	 */
	private boolean myWriteToLegacyLobColumns = false;
	/**
	 * @since 8.0.0
	 */
	private boolean myAccessMetaSourceInformationFromProvenanceTable = false;

	/**
	 * If this is enabled (default is {@literal false}), searches on token indexes will
	 * include the {@literal HASH_IDENTITY} column on all generated FHIR search query SQL.
	 * This is an experimental flag that may be changed or removed in a future release.
	 *
	 * @since 7.6.0
	 */
	@Beta
	private boolean myIncludeHashIdentityForTokenSearches = false;

	/**
	 * If we are batching write operations in transactions, what should the maximum number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	private int myMaxTransactionEntriesForWrite = DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE;

	/**
	 * If we are batching write operations in transactions, what should the default number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	private int myDefaultTransactionEntriesForWrite = DEFAULT_TRANSACTION_ENTRIES_FOR_WRITE;

	/**
	 * Controls whether the server writes data to the <code>HFJ_SPIDX_IDENTITY</code> table.
	 * <p>
	 * Defaults to {@code true}. If set to {@code false}, the server will skip writing to the table.
	 * This should normally remain {@code true}, but is configurable for use in unit tests.
	 *
	 * @since 8.2.0
	 */
	private boolean myWriteToSearchParamIdentityTable = true;

	/**
	 * Constructor
	 */
	public JpaStorageSettings() {
		setMarkResourcesForReindexingUponSearchParameterChange(true);
		setReindexThreadCount(Runtime.getRuntime().availableProcessors());
		setExpungeThreadCount(Runtime.getRuntime().availableProcessors());
		setBundleTypesAllowedForStorage(DEFAULT_BUNDLE_TYPES_ALLOWED_FOR_STORAGE);

		// Scheduled tasks are all enabled by default
		setEnableTaskBulkImportJobExecution(DEFAULT_ENABLE_TASKS);
		setEnableTaskBulkExportJobExecution(DEFAULT_ENABLE_TASKS);
		setEnableTaskStaleSearchCleanup(DEFAULT_ENABLE_TASKS);
		setEnableTaskPreExpandValueSets(DEFAULT_ENABLE_TASKS);
		setEnableTaskResourceReindexing(DEFAULT_ENABLE_TASKS);

		if (HapiSystemProperties.isDisableStatusBasedReindex()) {
			ourLog.info("Status based reindexing is DISABLED");
			setStatusBasedReindexingDisabled(true);
		}
		if (HapiSystemProperties.isUnitTestModeEnabled()) {
			setJobFastTrackingEnabled(true);
		}
		if (HapiSystemProperties.isPreventInvalidatingConditionalMatchCriteria()) {
			setPreventInvalidatingConditionalMatchCriteria(true);
		}
	}

	/**
	 * If this is enabled (default is {@literal false}), searches on token indexes will
	 * include the {@literal HASH_IDENTITY} column on all generated FHIR search query SQL.
	 * This is an experimental flag that may be changed or removed in a future release.
	 *
	 * @since 7.6.0
	 */
	public boolean isIncludeHashIdentityForTokenSearches() {
		return myIncludeHashIdentityForTokenSearches;
	}

	/**
	 * If this is enabled (default is {@literal false}), searches on token indexes will
	 * include the {@literal HASH_IDENTITY} column on all generated FHIR search query SQL.
	 * This is an experimental flag that may be changed or removed in a future release.
	 *
	 * @since 7.6.0
	 */
	public void setIncludeHashIdentityForTokenSearches(boolean theIncludeHashIdentityForTokenSearches) {
		myIncludeHashIdentityForTokenSearches = theIncludeHashIdentityForTokenSearches;
	}

	/**
	 * @since 5.7.0
	 * @deprecated This setting no longer does anything as of HAPI FHIR 7.0.0
	 */
	@Deprecated
	public int getInlineResourceTextBelowSize() {
		return 0;
	}

	/**
	 * @since 5.7.0
	 * @deprecated This setting no longer does anything as of HAPI FHIR 7.0.0
	 */
	@Deprecated
	public void setInlineResourceTextBelowSize(int theInlineResourceTextBelowSize) {
		// ignored
	}

	/**
	 * Specifies the maximum number of <code>_include</code> and <code>_revinclude</code> results to return in a
	 * single page of results. The default is <code>1000</code>, and <code>null</code> may be used
	 * to indicate that there is no limit.
	 *
	 * @since 5.5.0
	 */
	@Nullable
	public Integer getMaximumIncludesToLoadPerPage() {
		return myMaximumIncludesToLoadPerPage;
	}

	/**
	 * Specifies the maximum number of <code>_include</code> and <code>_revinclude</code> results to return in a
	 * single page of results. The default is <code>1000</code>, and <code>null</code> may be used
	 * to indicate that there is no limit.
	 *
	 * @since 5.5.0
	 */
	public void setMaximumIncludesToLoadPerPage(@Nullable Integer theMaximumIncludesToLoadPerPage) {
		myMaximumIncludesToLoadPerPage = theMaximumIncludesToLoadPerPage;
	}

	/**
	 * When performing a FHIR history operation, a <code>Bundle.total</code> value is included in the
	 * response, indicating the total number of history entries. This response is calculated using a
	 * SQL COUNT query statement which can be expensive. This setting allows the results of the count
	 * query to be cached, resulting in a much lighter load on the server, at the expense of
	 * returning total values that may be slightly out of date. Total counts can also be disabled,
	 * or forced to always be accurate.
	 * <p>
	 * In {@link HistoryCountModeEnum#CACHED_ONLY_WITHOUT_OFFSET} mode, a loading cache is used to fetch the value,
	 * meaning that only one thread per JVM will fetch the count, and others will block while waiting
	 * for the cache to load, avoiding excessive load on the database.
	 * </p>
	 * <p>
	 * Default is {@link HistoryCountModeEnum#CACHED_ONLY_WITHOUT_OFFSET}
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public HistoryCountModeEnum getHistoryCountMode() {
		return myHistoryCountMode;
	}

	/**
	 * When performing a FHIR history operation, a <code>Bundle.total</code> value is included in the
	 * response, indicating the total number of history entries. This response is calculated using a
	 * SQL COUNT query statement which can be expensive. This setting allows the results of the count
	 * query to be cached, resulting in a much lighter load on the server, at the expense of
	 * returning total values that may be slightly out of date. Total counts can also be disabled,
	 * or forced to always be accurate.
	 * <p>
	 * In {@link HistoryCountModeEnum#CACHED_ONLY_WITHOUT_OFFSET} mode, a loading cache is used to fetch the value,
	 * meaning that only one thread per JVM will fetch the count, and others will block while waiting
	 * for the cache to load, avoiding excessive load on the database.
	 * </p>
	 * <p>
	 * Default is {@link HistoryCountModeEnum#CACHED_ONLY_WITHOUT_OFFSET}
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public void setHistoryCountMode(@Nonnull HistoryCountModeEnum theHistoryCountMode) {

		Validate.notNull(theHistoryCountMode, "theHistoryCountMode must not be null");
		myHistoryCountMode = theHistoryCountMode;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the <code>$lastn</code> operation will be enabled for
	 * indexing Observation resources. This operation involves creating a special set of tables in hsearch for
	 * discovering Observation resources. Enabling this setting increases the amount of storage space required, and can
	 * slow write operations, but can be very useful for searching for collections of Observations for some applications.
	 *
	 * @since 5.1.0
	 */
	public boolean isLastNEnabled() {
		return myLastNEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the <code>$lastn</code> operation will be enabled for
	 * indexing Observation resources. This operation involves creating a special set of tables in hsearch for
	 * discovering Observation resources. Enabling this setting increases the amount of storage space required, and can
	 * slow write operations, but can be very useful for searching for collections of Observations for some applications.
	 *
	 * @since 5.1.0
	 */
	public void setLastNEnabled(boolean theLastNEnabled) {
		myLastNEnabled = theLastNEnabled;
	}

	/**
	 * Specifies the duration in minutes for which values will be retained after being
	 * written to the terminology translation cache. Defaults to 60.
	 */
	@Nonnull
	public Long getTranslationCachesExpireAfterWriteInMinutes() {
		return myTranslationCachesExpireAfterWriteInMinutes;
	}

	/**
	 * If enabled, resolutions for match URLs (e.g. conditional create URLs, conditional update URLs, etc) will be
	 * cached in an in-memory cache. This cache can have a noticeable improvement on write performance on servers
	 * where conditional operations are frequently performed, but note that this cache will not be
	 * invalidated based on updates to resources so this may have detrimental effects.
	 * <p>
	 * Default is <code>false</code>
	 *
	 * @since 5.4.0
	 * @deprecated Deprecated in 5.5.0. Use {@link #isMatchUrlCacheEnabled()} instead (the name of this method is misleading)
	 */
	@Deprecated
	public boolean getMatchUrlCache() {
		return myMatchUrlCacheEnabled;
	}

	/**
	 * If enabled, resolutions for match URLs (e.g. conditional create URLs, conditional update URLs, etc) will be
	 * cached in an in-memory cache. This cache can have a noticeable improvement on write performance on servers
	 * where conditional operations are frequently performed, but note that this cache will not be
	 * invalidated based on updates to resources so this may have detrimental effects.
	 * <p>
	 * Default is <code>false</code>
	 *
	 * @since 5.4.0
	 * @deprecated Deprecated in 5.5.0. Use {@link #setMatchUrlCacheEnabled(boolean)} instead (the name of this method is misleading)
	 */
	@Deprecated
	public void setMatchUrlCache(boolean theMatchUrlCache) {
		myMatchUrlCacheEnabled = theMatchUrlCache;
	}

	/**
	 * If enabled, resolutions for match URLs (e.g. conditional create URLs, conditional update URLs, etc) will be
	 * cached in an in-memory cache. This cache can have a noticeable improvement on write performance on servers
	 * where conditional operations are frequently performed, but note that this cache will not be
	 * invalidated based on updates to resources so this may have detrimental effects.
	 * <p>
	 * Default is <code>false</code>
	 *
	 * @since 5.5.0
	 */
	public boolean isMatchUrlCacheEnabled() {
		return getMatchUrlCache();
	}

	/**
	 * If enabled, resolutions for match URLs (e.g. conditional create URLs, conditional update URLs, etc) will be
	 * cached in an in-memory cache. This cache can have a noticeable improvement on write performance on servers
	 * where conditional operations are frequently performed, but note that this cache will not be
	 * invalidated based on updates to resources so this may have detrimental effects.
	 * <p>
	 * Default is <code>false</code>
	 *
	 * @since 5.5.0
	 */
	public void setMatchUrlCacheEnabled(boolean theMatchUrlCache) {
		setMatchUrlCache(theMatchUrlCache);
	}

	/**
	 * If set to <code>true</code> (default is true) when a resource is being persisted,
	 * the target resource types of references will be validated to ensure that they
	 * are appropriate for the field containing the reference. This is generally a good idea
	 * because invalid reference target types may not be searchable.
	 */
	public boolean isEnforceReferenceTargetTypes() {
		return myEnforceReferenceTargetTypes;
	}

	/**
	 * If set to <code>true</code> (default is true) when a resource is being persisted,
	 * the target resource types of references will be validated to ensure that they
	 * are appropriate for the field containing the reference. This is generally a good idea
	 * because invalid reference target types may not be searchable.
	 */
	public void setEnforceReferenceTargetTypes(boolean theEnforceReferenceTargetTypes) {
		myEnforceReferenceTargetTypes = theEnforceReferenceTargetTypes;
	}

	/**
	 * If a non-null value is supplied (default is <code>null</code>), a default
	 * for the <code>_total</code> parameter may be specified here. For example,
	 * setting this value to {@link SearchTotalModeEnum#ACCURATE} will force a
	 * count to always be calculated for all searches. This can have a performance impact
	 * since it means that a count query will always be performed, but this is desirable
	 * for some solutions.
	 */
	public SearchTotalModeEnum getDefaultTotalMode() {
		return myDefaultTotalMode;
	}

	/**
	 * If a non-null value is supplied (default is <code>null</code>), a default
	 * for the <code>_total</code> parameter may be specified here. For example,
	 * setting this value to {@link SearchTotalModeEnum#ACCURATE} will force a
	 * count to always be calculated for all searches. This can have a performance impact
	 * since it means that a count query will always be performed, but this is desirable
	 * for some solutions.
	 */
	public void setDefaultTotalMode(SearchTotalModeEnum theDefaultTotalMode) {
		myDefaultTotalMode = theDefaultTotalMode;
	}

	/**
	 * Returns a set of searches that should be kept "warm", meaning that
	 * searches will periodically be performed in the background to
	 * keep results ready for this search
	 */
	public List<WarmCacheEntry> getWarmCacheEntries() {
		if (myWarmCacheEntries == null) {
			myWarmCacheEntries = new ArrayList<>();
		}
		return myWarmCacheEntries;
	}

	public void setWarmCacheEntries(List<WarmCacheEntry> theWarmCacheEntries) {
		myWarmCacheEntries = theWarmCacheEntries;
	}

	/**
	 * If set to <code>true</code> (default is false), the reindexing of search parameters
	 * using a query on the HFJ_RESOURCE.SP_INDEX_STATUS column will be disabled completely.
	 * This query is just not efficient on Oracle and bogs the system down when there are
	 * a lot of resources. A more efficient way of doing this will be introduced
	 * in the next release of HAPI FHIR.
	 *
	 * @since 3.5.0
	 */
	public boolean isStatusBasedReindexingDisabled() {
		return myStatusBasedReindexingDisabled;
	}

	/**
	 * If set to <code>true</code> (default is false), the reindexing of search parameters
	 * using a query on the HFJ_RESOURCE.SP_INDEX_STATUS column will be disabled completely.
	 * This query is just not efficient on Oracle and bogs the system down when there are
	 * a lot of resources. A more efficient way of doing this will be introduced
	 * in the next release of HAPI FHIR.
	 *
	 * @since 3.5.0
	 */
	public void setStatusBasedReindexingDisabled(boolean theStatusBasedReindexingDisabled) {
		myStatusBasedReindexingDisabled = theStatusBasedReindexingDisabled;
	}

	/**
	 * This setting specifies the bundle types (<code>Bundle.type</code>) that
	 * are allowed to be stored as-is on the /Bundle endpoint.
	 *
	 * @see #DEFAULT_BUNDLE_TYPES_ALLOWED_FOR_STORAGE
	 */
	public Set<String> getBundleTypesAllowedForStorage() {
		return myBundleTypesAllowedForStorage;
	}

	/**
	 * This setting specifies the bundle types (<code>Bundle.type</code>) that
	 * are allowed to be stored as-is on the /Bundle endpoint.
	 *
	 * @see #DEFAULT_BUNDLE_TYPES_ALLOWED_FOR_STORAGE
	 */
	public void setBundleTypesAllowedForStorage(Set<String> theBundleTypesAllowedForStorage) {
		Validate.notNull(theBundleTypesAllowedForStorage, "theBundleTypesAllowedForStorage must not be null");
		myBundleTypesAllowedForStorage = theBundleTypesAllowedForStorage;
	}

	/**
	 * Specifies the highest number that a client is permitted to use in a
	 * <code>Cache-Control: nostore, max-results=NNN</code>
	 * directive. If the client tries to exceed this limit, the
	 * request will be denied. Defaults to 1000.
	 */
	public Integer getCacheControlNoStoreMaxResultsUpperLimit() {
		return myCacheControlNoStoreMaxResultsUpperLimit;
	}

	/**
	 * Specifies the highest number that a client is permitted to use in a
	 * <code>Cache-Control: nostore, max-results=NNN</code>
	 * directive. If the client tries to exceed this limit, the
	 * request will be denied. Defaults to 1000.
	 */
	public void setCacheControlNoStoreMaxResultsUpperLimit(Integer theCacheControlNoStoreMaxResults) {
		myCacheControlNoStoreMaxResultsUpperLimit = theCacheControlNoStoreMaxResults;
	}

	/**
	 * When searching, if set to a non-null value (default is <code>null</code>) the
	 * search coordinator will attempt to find at least this many results
	 * before returning a response to the client. This parameter mainly affects
	 * whether a "total count" is included in the response bundle for searches that
	 * return large amounts of data.
	 * <p>
	 * For a search that returns 10000 results, if this value is set to
	 * 10000 the search coordinator will find all 10000 results
	 * prior to returning, so the initial response bundle will have the
	 * total set to 10000. If this value is null (or less than 10000)
	 * the response bundle will likely return slightly faster, but will
	 * not include the total. Subsequent page requests will likely
	 * include the total however, if they are performed after the
	 * search coordinator has found all results.
	 * </p>
	 * <p>
	 * Set this value to <code>0</code> to always load all
	 * results before returning.
	 * </p>
	 */
	public Integer getCountSearchResultsUpTo() {
		return myCountSearchResultsUpTo;
	}

	/**
	 * When searching, if set to a non-null value (default is <code>null</code>) the
	 * search coordinator will attempt to find at least this many results
	 * before returning a response to the client. This parameter mainly affects
	 * whether a "total count" is included in the response bundle for searches that
	 * return large amounts of data.
	 * <p>
	 * For a search that returns 10000 results, if this value is set to
	 * 10000 the search coordinator will find all 10000 results
	 * prior to returning, so the initial response bundle will have the
	 * total set to 10000. If this value is null (or less than 10000)
	 * the response bundle will likely return slightly faster, but will
	 * not include the total. Subsequent page requests will likely
	 * include the total however, if they are performed after the
	 * search coordinator has found all results.
	 * </p>
	 * <p>
	 * Set this value to <code>0</code> to always load all
	 * results before returning.
	 * </p>
	 */
	public void setCountSearchResultsUpTo(Integer theCountSearchResultsUpTo) {
		myCountSearchResultsUpTo = theCountSearchResultsUpTo;
	}

	/**
	 * When a code system is added that contains more than this number of codes,
	 * the code system will be indexed later in an incremental process in order to
	 * avoid overwhelming Lucene with a huge number of codes in a single operation.
	 * <p>
	 * Defaults to 100
	 * </p>
	 */
	public int getDeferIndexingForCodesystemsOfSize() {
		return myDeferIndexingForCodesystemsOfSize;
	}

	/**
	 * When a code system is added that contains more than this number of codes,
	 * the code system will be indexed later in an incremental process in order to
	 * avoid overwhelming Lucene with a huge number of codes in a single operation.
	 * <p>
	 * Defaults to 100
	 * </p>
	 */
	public void setDeferIndexingForCodesystemsOfSize(int theDeferIndexingForCodesystemsOfSize) {
		myDeferIndexingForCodesystemsOfSize = theDeferIndexingForCodesystemsOfSize;
	}

	/**
	 * Unlike with normal search queries, $everything queries have their _includes loaded by the main search thread and these included results
	 * are added to the normal search results instead of being added on as extras in a page. This means that they will not appear multiple times
	 * as the search results are paged over.
	 * <p>
	 * In order to recursively load _includes, we process the original results in batches of this size. Adjust with caution, increasing this
	 * value may improve performance but may also cause memory issues.
	 * </p>
	 * <p>
	 * The default value is 50
	 * </p>
	 */
	public int getEverythingIncludesFetchPageSize() {
		return myEverythingIncludesFetchPageSize;
	}

	/**
	 * Unlike with normal search queries, $everything queries have their _includes loaded by the main search thread and these included results
	 * are added to the normal search results instead of being added on as extras in a page. This means that they will not appear multiple times
	 * as the search results are paged over.
	 * <p>
	 * In order to recursively load _includes, we process the original results in batches of this size. Adjust with caution, increasing this
	 * value may improve performance but may also cause memory issues.
	 * </p>
	 * <p>
	 * The default value is 50
	 * </p>
	 */
	public void setEverythingIncludesFetchPageSize(int theEverythingIncludesFetchPageSize) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theEverythingIncludesFetchPageSize);
		myEverythingIncludesFetchPageSize = theEverythingIncludesFetchPageSize;
	}

	/**
	 * Sets the number of milliseconds that search results for a given client search
	 * should be preserved before being purged from the database.
	 * <p>
	 * Search results are stored in the database so that they can be paged over multiple
	 * requests. After this
	 * number of milliseconds, they will be deleted from the database, and any paging links
	 * (next/prev links in search response bundles) will become invalid. Defaults to 1 hour.
	 * </p>
	 * <p>
	 * To disable this feature entirely, see {@link #setExpireSearchResults(boolean)}
	 * </p>
	 *
	 * @since 1.5
	 */
	public long getExpireSearchResultsAfterMillis() {
		return myExpireSearchResultsAfterMillis;
	}

	/**
	 * Sets the number of milliseconds that search results for a given client search
	 * should be preserved before being purged from the database.
	 * <p>
	 * Search results are stored in the database so that they can be paged over multiple
	 * requests. After this
	 * number of milliseconds, they will be deleted from the database, and any paging links
	 * (next/prev links in search response bundles) will become invalid. Defaults to 1 hour.
	 * </p>
	 * <p>
	 * To disable this feature entirely, see {@link #setExpireSearchResults(boolean)}
	 * </p>
	 *
	 * @since 1.5
	 */
	public void setExpireSearchResultsAfterMillis(long theExpireSearchResultsAfterMillis) {
		myExpireSearchResultsAfterMillis = theExpireSearchResultsAfterMillis;
	}

	/**
	 * Gets the default maximum number of results to load in a query.
	 * <p>
	 * For example, if the database has a million Patient resources in it, and
	 * the client requests <code>GET /Patient</code>, if this value is set
	 * to a non-null value (default is <code>null</code>) only this number
	 * of results will be fetched. Setting this value appropriately
	 * can be useful to improve performance in some situations.
	 * </p>
	 */
	public Integer getFetchSizeDefaultMaximum() {
		return myFetchSizeDefaultMaximum;
	}

	/**
	 * Gets the default maximum number of results to load in a query.
	 * <p>
	 * For example, if the database has a million Patient resources in it, and
	 * the client requests <code>GET /Patient</code>, if this value is set
	 * to a non-null value (default is <code>null</code>) only this number
	 * of results will be fetched. Setting this value appropriately
	 * can be useful to improve performance in some situations.
	 * </p>
	 */
	public void setFetchSizeDefaultMaximum(Integer theFetchSizeDefaultMaximum) {
		myFetchSizeDefaultMaximum = theFetchSizeDefaultMaximum;
	}

	/**
	 * See {@link #setMaximumExpansionSize(int)}
	 */
	public int getMaximumExpansionSize() {
		return myMaximumExpansionSize;
	}

	/**
	 * Sets the maximum number of codes that will be added to an in-memory valueset expansion before
	 * the operation will be failed as too costly. Note that this setting applies only to
	 * in-memory expansions and does not apply to expansions that are being pre-calculated.
	 * <p>
	 * The default value for this setting is 1000.
	 * </p>
	 */
	public void setMaximumExpansionSize(int theMaximumExpansionSize) {
		Validate.isTrue(theMaximumExpansionSize > 0, "theMaximumExpansionSize must be > 0");
		myMaximumExpansionSize = theMaximumExpansionSize;
	}

	/**
	 * Provides the maximum number of results which may be returned by a search (HTTP GET) which
	 * is executed as a sub-operation within within a FHIR <code>transaction</code> or
	 * <code>batch</code> operation. For example, if this value is set to <code>100</code> and
	 * a FHIR transaction is processed with a sub-request for <code>Patient?gender=male</code>,
	 * the server will throw an error (and the transaction will fail) if there are more than
	 * 100 resources on the server which match this query.
	 * <p>
	 * The default value is <code>null</code>, which means that there is no limit.
	 * </p>
	 */
	public Integer getMaximumSearchResultCountInTransaction() {
		return myMaximumSearchResultCountInTransaction;
	}

	/**
	 * Provides the maximum number of results which may be returned by a search (HTTP GET) which
	 * is executed as a sub-operation within within a FHIR <code>transaction</code> or
	 * <code>batch</code> operation. For example, if this value is set to <code>100</code> and
	 * a FHIR transaction is processed with a sub-request for <code>Patient?gender=male</code>,
	 * the server will throw an error (and the transaction will fail) if there are more than
	 * 100 resources on the server which match this query.
	 * <p>
	 * The default value is <code>null</code>, which means that there is no limit.
	 * </p>
	 */
	public void setMaximumSearchResultCountInTransaction(Integer theMaximumSearchResultCountInTransaction) {
		myMaximumSearchResultCountInTransaction = theMaximumSearchResultCountInTransaction;
	}

	/**
	 * This setting controls the number of threads allocated to resource reindexing
	 * (which is only ever used if SearchParameters change, or a manual reindex is
	 * triggered due to a HAPI FHIR upgrade or some other reason).
	 * <p>
	 * The default value is set to the number of available processors
	 * (via <code>Runtime.getRuntime().availableProcessors()</code>). Value
	 * for this setting must be a positive integer.
	 * </p>
	 */
	public int getReindexThreadCount() {
		return myReindexThreadCount;
	}

	/**
	 * This setting controls the number of threads allocated to resource reindexing
	 * (which is only ever used if SearchParameters change, or a manual reindex is
	 * triggered due to a HAPI FHIR upgrade or some other reason).
	 * <p>
	 * The default value is set to the number of available processors
	 * (via <code>Runtime.getRuntime().availableProcessors()</code>). Value
	 * for this setting must be a positive integer.
	 * </p>
	 */
	public void setReindexThreadCount(int theReindexThreadCount) {
		myReindexThreadCount = theReindexThreadCount;
		myReindexThreadCount = Math.max(myReindexThreadCount, 1); // Minimum of 1
	}

	/**
	 * This setting controls the number of threads allocated to the expunge operation
	 * <p>
	 * The default value is set to the number of available processors
	 * (via <code>Runtime.getRuntime().availableProcessors()</code>). Value
	 * for this setting must be a positive integer.
	 * </p>
	 */
	public int getExpungeThreadCount() {
		return myExpungeThreadCount;
	}

	/**
	 * This setting controls the number of threads allocated to the expunge operation
	 * <p>
	 * The default value is set to the number of available processors
	 * (via <code>Runtime.getRuntime().availableProcessors()</code>). Value
	 * for this setting must be a positive integer.
	 * </p>
	 */
	public void setExpungeThreadCount(int theExpungeThreadCount) {
		myExpungeThreadCount = theExpungeThreadCount;
		myExpungeThreadCount = Math.max(myExpungeThreadCount, 1); // Minimum of 1
	}

	public ResourceEncodingEnum getResourceEncoding() {
		return myResourceEncoding;
	}

	public void setResourceEncoding(ResourceEncodingEnum theResourceEncoding) {
		myResourceEncoding = theResourceEncoding;
	}

	/**
	 * If set, an individual resource will not be allowed to have more than the
	 * given number of tags, profiles, and security labels (the limit is for the combined
	 * total for all of these things on an individual resource).
	 * <p>
	 * If set to <code>null</code>, no limit will be applied.
	 * </p>
	 * <p>
	 * The default value for this setting is 1000.
	 * </p>
	 */
	public Integer getResourceMetaCountHardLimit() {
		return myResourceMetaCountHardLimit;
	}

	/**
	 * If set, an individual resource will not be allowed to have more than the
	 * given number of tags, profiles, and security labels (the limit is for the combined
	 * total for all of these things on an individual resource).
	 * <p>
	 * If set to <code>null</code>, no limit will be applied.
	 * </p>
	 * <p>
	 * The default value for this setting is 1000.
	 * </p>
	 */
	public void setResourceMetaCountHardLimit(Integer theResourceMetaCountHardLimit) {
		myResourceMetaCountHardLimit = theResourceMetaCountHardLimit;
	}

	/**
	 * Controls the behaviour when a client-assigned ID is encountered, i.e. an HTTP PUT
	 * on a resource ID that does not already exist in the database.
	 * <p>
	 * Default is {@link ClientIdStrategyEnum#ALPHANUMERIC}
	 * </p>
	 */
	public ClientIdStrategyEnum getResourceClientIdStrategy() {
		return myResourceClientIdStrategy;
	}

	/**
	 * Controls the behaviour when a client-assigned ID is encountered, i.e. an HTTP PUT
	 * on a resource ID that does not already exist in the database.
	 * <p>
	 * Default is {@link ClientIdStrategyEnum#ALPHANUMERIC}
	 * </p>
	 *
	 * @param theResourceClientIdStrategy Must not be <code>null</code>
	 */
	public void setResourceClientIdStrategy(ClientIdStrategyEnum theResourceClientIdStrategy) {
		Validate.notNull(theResourceClientIdStrategy, "theClientIdStrategy must not be null");
		myResourceClientIdStrategy = theResourceClientIdStrategy;
	}

	/**
	 * This setting configures the strategy to use in generating IDs for newly
	 * created resources on the server. The default is {@link IdStrategyEnum#SEQUENTIAL_NUMERIC}.
	 * <p>
	 * This strategy is only used for server-assigned IDs, i.e. for HTTP POST
	 * where the client is requesing that the server store a new resource and give
	 * it an ID.
	 * </p>
	 */
	public IdStrategyEnum getResourceServerIdStrategy() {
		return myResourceServerIdStrategy;
	}

	/**
	 * This setting configures the strategy to use in generating IDs for newly
	 * created resources on the server. The default is {@link IdStrategyEnum#SEQUENTIAL_NUMERIC}.
	 * <p>
	 * This strategy is only used for server-assigned IDs, i.e. for HTTP POST
	 * where the client is requesing that the server store a new resource and give
	 * it an ID.
	 * </p>
	 *
	 * @param theResourceIdStrategy The strategy. Must not be <code>null</code>.
	 */
	public void setResourceServerIdStrategy(IdStrategyEnum theResourceIdStrategy) {
		Validate.notNull(theResourceIdStrategy, "theResourceIdStrategy must not be null");
		myResourceServerIdStrategy = theResourceIdStrategy;
	}

	/**
	 * If set to a non {@literal null} value (default is {@link #DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS non null})
	 * if an identical search is requested multiple times within this window, the same results will be returned
	 * to multiple queries. For example, if this value is set to 1 minute and a client searches for all
	 * patients named "smith", and then a second client also performs the same search within 1 minute,
	 * the same cached results will be returned.
	 * <p>
	 * This approach can improve performance, especially under heavy load, but can also mean that
	 * searches may potentially return slightly out-of-date results.
	 * </p>
	 * <p>
	 * Note that if this is set to a non-null value, clients may override this setting by using
	 * the <code>Cache-Control</code> header. If this is set to <code>null</code>, the Cache-Control
	 * header will be ignored.
	 * </p>
	 */
	public Long getReuseCachedSearchResultsForMillis() {
		return myReuseCachedSearchResultsForMillis;
	}

	/**
	 * If set to a non {@literal null} value (default is {@link #DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS non null})
	 * if an identical search is requested multiple times within this window, the same results will be returned
	 * to multiple queries. For example, if this value is set to 1 minute and a client searches for all
	 * patients named "smith", and then a second client also performs the same search within 1 minute,
	 * the same cached results will be returned.
	 * <p>
	 * This approach can improve performance, especially under heavy load, but can also mean that
	 * searches may potentially return slightly out-of-date results.
	 * </p>
	 * <p>
	 * Note that if this is set to a non-null value, clients may override this setting by using
	 * the <code>Cache-Control</code> header. If this is set to <code>null</code>, the Cache-Control
	 * header will be ignored.
	 * </p>
	 */
	public void setReuseCachedSearchResultsForMillis(Long theReuseCachedSearchResultsForMillis) {
		myReuseCachedSearchResultsForMillis = theReuseCachedSearchResultsForMillis;
	}

	/**
	 * @see #setAllowInlineMatchUrlReferences(boolean)
	 */
	public boolean isAllowInlineMatchUrlReferences() {
		return myAllowInlineMatchUrlReferences;
	}

	/**
	 * Should references containing match URLs be resolved and replaced in create and update operations. For
	 * example, if this property is set to true and a resource is created containing a reference
	 * to "Patient?identifier=12345", this is reference match URL will be resolved and replaced according
	 * to the usual match URL rules.
	 * <p>
	 * Default is {@literal true} beginning in HAPI FHIR 2.4, since this
	 * feature is now specified in the FHIR specification. (Previously it
	 * was an experimental/proposed feature)
	 * </p>
	 *
	 * @since 1.5
	 */
	public void setAllowInlineMatchUrlReferences(boolean theAllowInlineMatchUrlReferences) {
		myAllowInlineMatchUrlReferences = theAllowInlineMatchUrlReferences;
	}

	public boolean isAllowMultipleDelete() {
		return myAllowMultipleDelete;
	}

	public void setAllowMultipleDelete(boolean theAllowMultipleDelete) {
		myAllowMultipleDelete = theAllowMultipleDelete;
	}

	/**
	 * When {@link #setAutoCreatePlaceholderReferenceTargets(boolean)} is enabled, if this
	 * setting is set to <code>true</code> (default is <code>true</code>) and the source
	 * reference has an identifier populated, the identifier will be copied to the target
	 * resource.
	 * <p>
	 * When enabled, if an Observation contains a reference like the one below,
	 * and no existing resource was found that matches the given ID, a new
	 * one will be created and its <code>Patient.identifier</code> value will be
	 * populated using the value from <code>Observation.subject.identifier</code>.
	 * </p>
	 * <pre>
	 * {
	 *   "resourceType": "Observation",
	 *   "subject": {
	 *     "reference": "Patient/ABC",
	 *     "identifier": {
	 *       "system": "http://foo",
	 *       "value": "123"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * This method is often combined with {@link #setAllowInlineMatchUrlReferences(boolean)}.
	 * </p>
	 * <p>
	 * In other words if an Observation contains a reference like the one below,
	 * and no existing resource was found that matches the given match URL, a new
	 * one will be created and its <code>Patient.identifier</code> value will be
	 * populated using the value from <code>Observation.subject.identifier</code>.
	 * </p>
	 * <pre>
	 * {
	 *   "resourceType": "Observation",
	 *   "subject": {
	 *     "reference": "Patient?identifier=http://foo|123",
	 *     "identifier": {
	 *       "system": "http://foo",
	 *       "value": "123"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * Note that the default for this setting was previously <code>false</code>, and was changed to <code>true</code>
	 * in 5.4.0 with consideration to the following:
	 * </p>
	 * <pre>
	 * CP = Auto-Create Placeholder Reference Targets
	 * PI = Populate Identifier in Auto-Created Placeholder Reference Targets
	 *
	 * CP | PI
	 * -------
	 *  F | F  {@code <-} PI=F is ignored
	 *  F | T  {@code <-} PI=T is ignored
	 *  T | F  {@code <-} resources may reference placeholder reference targets that are never updated : (
	 *  T | T  {@code <-} placeholder reference targets can be updated : )
	 * </pre>
	 * <p>
	 * Where CP=T and PI=F, the following could happen:
	 * </p>
	 * <ol>
	 *    <li>
	 *       Resource instance A is created with a reference to resource instance B. B is a placeholder reference target
	 *       without an identifier.
	 *    </li>
	 *    <li>
	 * 	   Resource instance C is conditionally created using a match URL. It is not matched to B although these
	 * 	   resources represent the same entity.
	 *    </li>
	 *    <li>
	 *       A continues to reference placeholder B, and does not reference populated C.
	 *    </li>
	 * </ol>
	 * <p>
	 * There may be cases where configuring this setting to <code>false</code> would be appropriate; however, these are
	 * exceptional cases that should be opt-in.
	 * </p>
	 *
	 * @since 4.2.0
	 */
	public boolean isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets() {
		return myPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets;
	}

	/**
	 * When {@link #setAutoCreatePlaceholderReferenceTargets(boolean)} is enabled, if this
	 * setting is set to <code>true</code> (default is <code>true</code>) and the source
	 * reference has an identifier populated, the identifier will be copied to the target
	 * resource.
	 * <p>
	 * When enabled, if an Observation contains a reference like the one below,
	 * and no existing resource was found that matches the given ID, a new
	 * one will be created and its <code>Patient.identifier</code> value will be
	 * populated using the value from <code>Observation.subject.identifier</code>.
	 * </p>
	 * <pre>
	 * {
	 *   "resourceType": "Observation",
	 *   "subject": {
	 *     "reference": "Patient/ABC",
	 *     "identifier": {
	 *       "system": "http://foo",
	 *       "value": "123"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * This method is often combined with {@link #setAllowInlineMatchUrlReferences(boolean)}.
	 * </p>
	 * <p>
	 * In other words if an Observation contains a reference like the one below,
	 * and no existing resource was found that matches the given match URL, a new
	 * one will be created and its <code>Patient.identifier</code> value will be
	 * populated using the value from <code>Observation.subject.identifier</code>.
	 * </p>
	 * <pre>
	 * {
	 *   "resourceType": "Observation",
	 *   "subject": {
	 *     "reference": "Patient?identifier=http://foo|123",
	 *     "identifier": {
	 *       "system": "http://foo",
	 *       "value": "123"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * Note that the default for this setting was previously <code>false</code>, and was changed to <code>true</code>
	 * in 5.4.0 with consideration to the following:
	 * </p>
	 * <pre>
	 * CP = Auto-Create Placeholder Reference Targets
	 * PI = Populate Identifier in Auto-Created Placeholder Reference Targets
	 *
	 * CP | PI
	 * -------
	 *  F | F  {@code <-} PI=F is ignored
	 *  F | T  {@code <-} PI=T is ignored
	 *  T | F  {@code <-} resources may reference placeholder reference targets that are never updated : (
	 *  T | T  {@code <-} placeholder reference targets can be updated : )
	 * </pre>
	 * <p>
	 * Where CP=T and PI=F, the following could happen:
	 * </p>
	 * <ol>
	 *    <li>
	 *       Resource instance A is created with a reference to resource instance B. B is a placeholder reference target
	 *       without an identifier.
	 *    </li>
	 *    <li>
	 * 	   Resource instance C is conditionally created using a match URL. It is not matched to B although these
	 * 	   resources represent the same entity.
	 *    </li>
	 *    <li>
	 *       A continues to reference placeholder B, and does not reference populated C.
	 *    </li>
	 * </ol>
	 * <p>
	 * There may be cases where configuring this setting to <code>false</code> would be appropriate; however, these are
	 * exceptional cases that should be opt-in.
	 * </p>
	 *
	 * @since 4.2.0
	 */
	public void setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(
			boolean thePopulateIdentifierInAutoCreatedPlaceholderReferenceTargets) {
		myPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets =
				thePopulateIdentifierInAutoCreatedPlaceholderReferenceTargets;
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) resources will be permitted to be
	 * deleted even if other resources currently contain references to them.
	 * <p>
	 * This property can cause confusing results for clients of the server since searches, includes,
	 * and other FHIR features may not behave as expected when referential integrity is not
	 * preserved. Use this feature with caution.
	 * </p>
	 */
	public boolean isEnforceReferentialIntegrityOnDelete() {
		return myEnforceReferentialIntegrityOnDelete;
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) resources will be permitted to be
	 * deleted even if other resources currently contain references to them.
	 * <p>
	 * This property can cause confusing results for clients of the server since searches, includes,
	 * and other FHIR features may not behave as expected when referential integrity is not
	 * preserved. Use this feature with caution.
	 * </p>
	 */
	public void setEnforceReferentialIntegrityOnDelete(boolean theEnforceReferentialIntegrityOnDelete) {
		myEnforceReferentialIntegrityOnDelete = theEnforceReferentialIntegrityOnDelete;
	}

	/**
	 * When {@link #setEnforceReferentialIntegrityOnDelete(boolean)} is set to <code>true</code>, this setting may
	 * be used to selectively disable the referential integrity checking only for specific paths. It applies to
	 * both Delete and Delete with Expunge operations.
	 * <p>
	 * For example, if the property contains the FHIR path expression <code>Encounter.subject</code> , deleting
	 * the Patient referenced by an Encounter's subject is allowed without deleting the Encounter first.
	 * </p>
	 */
	public Set<String> getEnforceReferentialIntegrityOnDeleteDisableForPaths() {
		return myEnforceReferentialIntegrityOnDeleteDisableForPaths;
	}

	/**
	 * When {@link #setEnforceReferentialIntegrityOnDelete(boolean)} is set to <code>true</code>, this setting
	 * allows you to selectively disable integrity checks for specific paths. It applies to both Delete and
	 * Delete with Expunge operations.
	 * <p>
	 * For example, if the property contains the FHIR path expression <code>Encounter.subject</code> , deleting
	 * the Patient referenced by an Encounter's subject is allowed without deleting the Encounter first.
	 * </p>
	 */
	public void setEnforceReferentialIntegrityOnDeleteDisableForPaths(
			Set<String> theEnforceReferentialIntegrityOnDeleteDisableForPaths) {
		myEnforceReferentialIntegrityOnDeleteDisableForPaths = theEnforceReferentialIntegrityOnDeleteDisableForPaths;
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) resources will be permitted to be
	 * created or updated even if they contain references to local resources that do not exist.
	 * <p>
	 * For example, if a patient contains a reference to managing organization <code>Organization/FOO</code>
	 * but FOO is not a valid ID for an organization on the server, the operation will be blocked unless
	 * this property has been set to <code>false</code>
	 * </p>
	 * <p>
	 * This property can cause confusing results for clients of the server since searches, includes,
	 * and other FHIR features may not behave as expected when referential integrity is not
	 * preserved. Use this feature with caution.
	 * </p>
	 */
	public boolean isEnforceReferentialIntegrityOnWrite() {
		return myEnforceReferentialIntegrityOnWrite;
	}

	/**
	 * If set to <code>false</code> (default is <code>true</code>) resources will be permitted to be
	 * created or updated even if they contain references to local resources that do not exist.
	 * <p>
	 * For example, if a patient contains a reference to managing organization <code>Organization/FOO</code>
	 * but FOO is not a valid ID for an organization on the server, the operation will be blocked unless
	 * this property has been set to <code>false</code>
	 * </p>
	 * <p>
	 * This property can cause confusing results for clients of the server since searches, includes,
	 * and other FHIR features may not behave as expected when referential integrity is not
	 * preserved. Use this feature with caution.
	 * </p>
	 */
	public void setEnforceReferentialIntegrityOnWrite(boolean theEnforceReferentialIntegrityOnWrite) {
		myEnforceReferentialIntegrityOnWrite = theEnforceReferentialIntegrityOnWrite;
	}

	/**
	 * If this is set to <code>false</code> (default is <code>true</code>) the stale search deletion
	 * task will be disabled (meaning that search results will be retained in the database indefinitely). USE WITH CAUTION.
	 * <p>
	 * This feature is useful if you want to define your own process for deleting these (e.g. because
	 * you are running in a cluster)
	 * </p>
	 */
	public boolean isExpireSearchResults() {
		return myDeleteStaleSearches;
	}

	/**
	 * If this is set to <code>false</code> (default is <code>true</code>) the stale search deletion
	 * task will be disabled (meaning that search results will be retained in the database indefinitely). USE WITH CAUTION.
	 * <p>
	 * This feature is useful if you want to define your own process for deleting these (e.g. because
	 * you are running in a cluster)
	 * </p>
	 */
	public void setExpireSearchResults(boolean theDeleteStaleSearches) {
		myDeleteStaleSearches = theDeleteStaleSearches;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), the $expunge operation
	 * will be enabled on this server. This operation is potentially dangerous since it allows
	 * a client to physically delete data in a way that can not be recovered (without resorting
	 * to backups).
	 * <p>
	 * It is recommended to not enable this setting without appropriate security
	 * in place on your server to prevent non-administrators from using this
	 * operation.
	 * </p>
	 */
	public boolean isExpungeEnabled() {
		return myExpungeEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), the $expunge operation
	 * will be enabled on this server. This operation is potentially dangerous since it allows
	 * a client to physically delete data in a way that can not be recovered (without resorting
	 * to backups).
	 * <p>
	 * It is recommended to not enable this setting without appropriate security
	 * in place on your server to prevent non-administrators from using this
	 * operation.
	 * </p>
	 */
	public void setExpungeEnabled(boolean theExpungeEnabled) {
		myExpungeEnabled = theExpungeEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), the _expunge parameter on the DELETE
	 * operation will be enabled on this server. DELETE _expunge removes all data associated with a resource in a highly performant
	 * way, skipping most of the the checks that are enforced with usual DELETE operations.  The only check
	 * that is performed before deleting the data is that no other resources reference the resources about to
	 * be deleted.  This operation is potentially dangerous since it allows
	 * a client to physically delete data in a way that can not be recovered (without resorting
	 * to backups).
	 * <p>
	 * It is recommended to not enable this setting without appropriate security
	 * in place on your server to prevent non-administrators from using this
	 * operation.
	 * </p>
	 */
	public boolean isDeleteExpungeEnabled() {
		return myDeleteExpungeEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), the _expunge parameter on the DELETE
	 * operation will be enabled on this server. DELETE _expunge removes all data associated with a resource in a highly performant
	 * way, skipping most of the the checks that are enforced with usual DELETE operations.  The only check
	 * that is performed before deleting the resources and their indexes is that no other resources reference the resources about to
	 * be deleted.  This operation is potentially dangerous since it allows
	 * a client to physically delete data in a way that can not be recovered (without resorting
	 * to backups).
	 * <p>
	 * It is recommended to not enable this setting without appropriate security
	 * in place on your server to prevent non-administrators from using this
	 * operation.
	 * </p>
	 */
	public void setDeleteExpungeEnabled(boolean theDeleteExpungeEnabled) {
		myDeleteExpungeEnabled = theDeleteExpungeEnabled;
	}

	/**
	 * The expunge batch size (default 800) determines the number of records deleted within a single transaction by the
	 * expunge operation.  When expunging via DELETE ?_expunge=true, then this value determines the batch size for
	 * the number of resources deleted and expunged at a time.
	 */
	public int getExpungeBatchSize() {
		return myExpungeBatchSize;
	}

	/**
	 * The expunge batch size (default 800) determines the number of records deleted within a single transaction by the
	 * expunge operation.  When expunging via DELETE ?_expunge=true, then this value determines the batch size for
	 * the number of resources deleted and expunged at a time.
	 */
	public void setExpungeBatchSize(int theExpungeBatchSize) {
		myExpungeBatchSize = theExpungeBatchSize;
	}

	/**
	 * Should resources be marked as needing reindexing when a
	 * SearchParameter resource is added or changed. This should generally
	 * be true (which is the default)
	 */
	public boolean isMarkResourcesForReindexingUponSearchParameterChange() {
		return myMarkResourcesForReindexingUponSearchParameterChange;
	}

	/**
	 * Should resources be marked as needing reindexing when a
	 * SearchParameter resource is added or changed. This should generally
	 * be true (which is the default)
	 */
	public void setMarkResourcesForReindexingUponSearchParameterChange(
			boolean theMarkResourcesForReindexingUponSearchParameterChange) {
		myMarkResourcesForReindexingUponSearchParameterChange = theMarkResourcesForReindexingUponSearchParameterChange;
	}

	public boolean isSchedulingDisabled() {
		return mySchedulingDisabled;
	}

	public void setSchedulingDisabled(boolean theSchedulingDisabled) {
		mySchedulingDisabled = theSchedulingDisabled;
	}

	/**
	 * If set to {@literal true} (default is true), if a client performs an update which does not actually
	 * result in any chance to a given resource (e.g. an update where the resource body matches the
	 * existing resource body in the database) the operation will succeed but a new version (and corresponding history
	 * entry) will not actually be created. The existing resource version will be returned to the client.
	 * <p>
	 * If set to {@literal false}, all updates will result in the creation of a new version
	 * </p>
	 */
	public boolean isSuppressUpdatesWithNoChange() {
		return mySuppressUpdatesWithNoChange;
	}

	/**
	 * If set to {@literal true} (default is true), if a client performs an update which does not actually
	 * result in any chance to a given resource (e.g. an update where the resource body matches the
	 * existing resource body in the database) the operation will succeed but a new version (and corresponding history
	 * entry) will not actually be created. The existing resource version will be returned to the client.
	 * <p>
	 * If set to {@literal false}, all updates will result in the creation of a new version
	 * </p>
	 */
	public void setSuppressUpdatesWithNoChange(boolean theSuppressUpdatesWithNoChange) {
		mySuppressUpdatesWithNoChange = theSuppressUpdatesWithNoChange;
	}

	/**
	 * When using {@link #setUniqueIndexesEnabled(boolean) unique indexes}, if this
	 * setting is set to <code>true</code> (default is <code>true</code>) the system
	 * will test for the existence of a particular unique index value prior to saving
	 * a new one.
	 * <p>
	 * This causes friendlier error messages to be generated, but adds an
	 * extra round-trip to the database for eavh save so it can cause
	 * a small performance hit.
	 * </p>
	 */
	public boolean isUniqueIndexesCheckedBeforeSave() {
		return myUniqueIndexesCheckedBeforeSave;
	}

	/**
	 * When using {@link #setUniqueIndexesEnabled(boolean) unique indexes}, if this
	 * setting is set to <code>true</code> (default is <code>true</code>) the system
	 * will test for the existence of a particular unique index value prior to saving
	 * a new one.
	 * <p>
	 * This causes friendlier error messages to be generated, but adds an
	 * extra round-trip to the database for each save so it can cause
	 * a small performance hit.
	 * </p>
	 */
	public void setUniqueIndexesCheckedBeforeSave(boolean theUniqueIndexesCheckedBeforeSave) {
		myUniqueIndexesCheckedBeforeSave = theUniqueIndexesCheckedBeforeSave;
	}

	/**
	 * If set to <code>true</code> (default is <code>true</code>), indexes will be
	 * created for search parameters marked as {@link HapiExtensions#EXT_SP_UNIQUE}.
	 * This is a HAPI FHIR specific extension which can be used to specify that no more than one
	 * resource can exist which matches a given criteria, using a database constraint to
	 * enforce this.
	 */
	public boolean isUniqueIndexesEnabled() {
		return myUniqueIndexesEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>true</code>), indexes will be
	 * created for search parameters marked as {@link HapiExtensions#EXT_SP_UNIQUE}.
	 * This is a HAPI FHIR specific extension which can be used to specify that no more than one
	 * resource can exist which matches a given criteria, using a database constraint to
	 * enforce this.
	 */
	public void setUniqueIndexesEnabled(boolean theUniqueIndexesEnabled) {
		myUniqueIndexesEnabled = theUniqueIndexesEnabled;
	}

	/**
	 * If <code>true</code> (default is <code>true</code>), before allowing a
	 * SearchParameter resource to be stored (create, update, etc.) the
	 * expression will be performed against an empty resource to ensure that
	 * the FHIRPath executor is able to process it.
	 * <p>
	 * This should proabably always be set to true, but is configurable
	 * in order to support some unit tests.
	 * </p>
	 */
	public boolean isValidateSearchParameterExpressionsOnSave() {
		return myValidateSearchParameterExpressionsOnSave;
	}

	/**
	 * If <code>true</code> (default is <code>true</code>), before allowing a
	 * SearchParameter resource to be stored (create, update, etc.) the
	 * expression will be performed against an empty resource to ensure that
	 * the FHIRPath executor is able to process it.
	 * <p>
	 * This should proabably always be set to true, but is configurable
	 * in order to support some unit tests.
	 * </p>
	 */
	public void setValidateSearchParameterExpressionsOnSave(boolean theValidateSearchParameterExpressionsOnSave) {
		myValidateSearchParameterExpressionsOnSave = theValidateSearchParameterExpressionsOnSave;
	}

	/**
	 * This setting sets the number of search results to prefetch. For example, if this list
	 * is set to [100, 1000, -1] then the server will initially load 100 results and not
	 * attempt to load more. If the user requests subsequent page(s) of results and goes
	 * past 100 results, the system will load the next 900 (up to the following threshold of 1000).
	 * The system will progressively work through these thresholds.
	 *
	 * <p>
	 * A threshold of -1 means to load all results. Note that if the final threshold is a
	 * number other than <code>-1</code>, the system will never prefetch more than the
	 * given number.
	 * </p>
	 */
	public List<Integer> getSearchPreFetchThresholds() {
		return mySearchPreFetchThresholds;
	}

	/**
	 * This setting sets the number of search results to prefetch. For example, if this list
	 * is set to [100, 1000, -1] then the server will initially load 100 results and not
	 * attempt to load more. If the user requests subsequent page(s) of results and goes
	 * past 100 results, the system will load the next 900 (up to the following threshold of 1000).
	 * The system will progressively work through these thresholds.
	 *
	 * <p>
	 * A threshold of -1 means to load all results. Note that if the final threshold is a
	 * number other than <code>-1</code>, the system will never prefetch more than the
	 * given number.
	 * </p>
	 */
	public void setSearchPreFetchThresholds(List<Integer> thePreFetchThresholds) {
		Validate.isTrue(thePreFetchThresholds.size() > 0, "thePreFetchThresholds must not be empty");
		int last = 0;
		for (Integer nextInt : thePreFetchThresholds) {
			Validate.isTrue(nextInt > 0 || nextInt == -1, nextInt + " is not a valid prefetch threshold");
			Validate.isTrue(nextInt != last, "Prefetch thresholds must be sequential");
			Validate.isTrue(nextInt > last || nextInt == -1, "Prefetch thresholds must be sequential");
			Validate.isTrue(last != -1, "Prefetch thresholds must be sequential");
			last = nextInt;
		}
		mySearchPreFetchThresholds = thePreFetchThresholds;
	}

	/**
	 * If set to <code>true</code> the _filter search parameter will be enabled on this server. Note that _filter
	 * is very powerful, but also potentially dangerous as it can allow a user to create a query for which there
	 * are no indexes or efficient query plans for the database to leverage while performing the query.
	 * As a result, this feature is recommended only for servers where the querying applications are known in advance
	 * and a database administrator can properly tune the database for the resulting queries.
	 */
	public boolean isFilterParameterEnabled() {
		return myFilterParameterEnabled;
	}

	/**
	 * If set to <code>true</code> the _filter search parameter will be enabled on this server. Note that _filter
	 * is very powerful, but also potentially dangerous as it can allow a user to create a query for which there
	 * are no indexes or efficient query plans for the database to leverage while performing the query.
	 * As a result, this feature is recommended only for servers where the querying applications are known in advance
	 * and a database administrator can properly tune the database for the resulting queries.
	 */
	public void setFilterParameterEnabled(boolean theFilterParameterEnabled) {
		myFilterParameterEnabled = theFilterParameterEnabled;
	}

	/**
	 * If enabled, resource source information (<code>Resource.meta.source</code>) will be persisted along with
	 * each resource. This adds extra table and index space so it should be disabled if it is not being
	 * used.
	 * <p>
	 * Default is {@link StoreMetaSourceInformationEnum#SOURCE_URI_AND_REQUEST_ID}
	 * </p>
	 */
	public StoreMetaSourceInformationEnum getStoreMetaSourceInformation() {
		return myStoreMetaSourceInformation;
	}

	/**
	 * If enabled, resource source information (<code>Resource.meta.source</code>) will be persisted along with
	 * each resource. This adds extra table and index space so it should be disabled if it is not being
	 * used.
	 * <p>
	 * Default is {@link StoreMetaSourceInformationEnum#SOURCE_URI_AND_REQUEST_ID}
	 * </p>
	 */
	public void setStoreMetaSourceInformation(StoreMetaSourceInformationEnum theStoreMetaSourceInformation) {
		Validate.notNull(theStoreMetaSourceInformation, "theStoreMetaSourceInformation must not be null");
		myStoreMetaSourceInformation = theStoreMetaSourceInformation;
	}

	/**
	 * If set to <code>true</code> (default is false), the system will read
	 * <code>Resource.meta.source</code> values from the <code>HFJ_RES_VER_PROV</code>
	 * table. This table was replaced by dedicated columns in the <code>HFJ_RES_VER</code>
	 * table as of HAPI FHIR 6.8.0 (Smile CDR 2023.08.R01) and as of that version
	 * there is no need to read from the dedicated table. However, if old data still
	 * remains and has not been migrated (using a $reindex operation) then you can
	 * enable this setting in order to read from the old table.
	 *
	 * @since 8.0.0
	 */
	public boolean isAccessMetaSourceInformationFromProvenanceTable() {
		return myAccessMetaSourceInformationFromProvenanceTable;
	}

	/**
	 * If set to <code>true</code> (default is false), the system will read
	 * <code>Resource.meta.source</code> values from the <code>HFJ_RES_VER_PROV</code>
	 * table. This table was replaced by dedicated columns in the <code>HFJ_RES_VER</code>
	 * table as of HAPI FHIR 6.8.0 (Smile CDR 2023.08.R01) and as of that version
	 * there is no need to read from the dedicated table. However, if old data still
	 * remains and has not been migrated (using a $reindex operation) then you can
	 * enable this setting in order to read from the old table.
	 *
	 * @since 8.0.0
	 */
	public void setAccessMetaSourceInformationFromProvenanceTable(
			boolean theAccessMetaSourceInformationFromProvenanceTable) {
		myAccessMetaSourceInformationFromProvenanceTable = theAccessMetaSourceInformationFromProvenanceTable;
	}

	/**
	 * <p>
	 * If set to {@code true}, ValueSets and expansions are stored in terminology tables. This is to facilitate
	 * optimization of the $expand operation on large ValueSets.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public boolean isPreExpandValueSets() {
		return myPreExpandValueSets;
	}

	/**
	 * <p>
	 * If set to {@code true}, ValueSets and expansions are stored in terminology tables. This is to facilitate
	 * optimization of the $expand operation on large ValueSets.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public void setPreExpandValueSets(boolean thePreExpandValueSets) {
		myPreExpandValueSets = thePreExpandValueSets;
	}

	/**
	 * <p>
	 * This is the default value of {@code offset} parameter for the ValueSet {@code $expand} operation when
	 * {@link JpaStorageSettings#isPreExpandValueSets()} returns {@code true}.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 0}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public int getPreExpandValueSetsDefaultOffset() {
		return myPreExpandValueSetsDefaultOffset;
	}

	/**
	 * <p>
	 * This is the default value of {@code count} parameter for the ValueSet {@code $expand} operation when
	 * {@link JpaStorageSettings#isPreExpandValueSets()} returns {@code true}.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 1000}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public int getPreExpandValueSetsDefaultCount() {
		return myPreExpandValueSetsDefaultCount;
	}

	/**
	 * <p>
	 * This is the default value of {@code count} parameter for the ValueSet {@code $expand} operation when
	 * {@link JpaStorageSettings#isPreExpandValueSets()} returns {@code true}.
	 * </p>
	 * <p>
	 * If {@code thePreExpandValueSetsDefaultCount} is greater than
	 * {@link JpaStorageSettings#getPreExpandValueSetsMaxCount()}, the lesser value is used.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 1000}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public void setPreExpandValueSetsDefaultCount(int thePreExpandValueSetsDefaultCount) {
		myPreExpandValueSetsDefaultCount = Math.min(thePreExpandValueSetsDefaultCount, getPreExpandValueSetsMaxCount());
	}

	/**
	 * <p>
	 * This is the max value of {@code count} parameter for the ValueSet {@code $expand} operation when
	 * {@link JpaStorageSettings#isPreExpandValueSets()} returns {@code true}.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 1000}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public int getPreExpandValueSetsMaxCount() {
		return myPreExpandValueSetsMaxCount;
	}

	/**
	 * <p>
	 * This is the max value of {@code count} parameter for the ValueSet {@code $expand} operation when
	 * {@link JpaStorageSettings#isPreExpandValueSets()} returns {@code true}.
	 * </p>
	 * <p>
	 * If {@code thePreExpandValueSetsMaxCount} is lesser than
	 * {@link JpaStorageSettings#getPreExpandValueSetsDefaultCount()}, the default {@code count} is lowered to the
	 * new max {@code count}.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 1000}.
	 * </p>
	 *
	 * @since 4.1.0
	 */
	public void setPreExpandValueSetsMaxCount(int thePreExpandValueSetsMaxCount) {
		myPreExpandValueSetsMaxCount = thePreExpandValueSetsMaxCount;
		setPreExpandValueSetsDefaultCount(
				Math.min(getPreExpandValueSetsDefaultCount(), getPreExpandValueSetsMaxCount()));
	}

	/**
	 * This setting should be disabled (set to <code>false</code>) on servers that are not allowing
	 * deletes. Default is <code>true</code>. If deletes are disabled, some checks for resource
	 * deletion can be skipped, which improves performance. This is particularly helpful when large
	 * amounts of data containing client-assigned IDs are being loaded, but it can also improve
	 * search performance.
	 * <p>
	 * If deletes are disabled, it is also not possible to un-delete a previously deleted
	 * resource.
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public boolean isDeleteEnabled() {
		return myDeleteEnabled;
	}

	/**
	 * This setting should be disabled (set to <code>false</code>) on servers that are not allowing
	 * deletes. Default is <code>true</code>. If deletes are disabled, some checks for resource
	 * deletion can be skipped, which improves performance. This is particularly helpful when large
	 * amounts of data containing client-assigned IDs are being loaded, but it can also improve
	 * search performance.
	 * <p>
	 * If deletes are disabled, it is also not possible to un-delete a previously deleted
	 * resource.
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public void setDeleteEnabled(boolean theDeleteEnabled) {
		myDeleteEnabled = theDeleteEnabled;
	}

	/**
	 * <p>
	 * This determines the maximum number of conflicts that should be fetched and handled while retrying a delete of a resource.
	 * This can also be thought of as the maximum number of rounds of cascading deletion.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 60}.
	 * </p>
	 *
	 * @since 5.1.0
	 */
	public Integer getMaximumDeleteConflictQueryCount() {
		return myMaximumDeleteConflictQueryCount;
	}

	/**
	 * <p>
	 * This determines the maximum number of conflicts that should be fetched and handled while retrying a delete of a resource.
	 * This can also be thought of as the maximum number of rounds of cascading deletion.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code 60}.
	 * </p>
	 *
	 * @since 5.1.0
	 */
	public void setMaximumDeleteConflictQueryCount(Integer theMaximumDeleteConflictQueryCount) {
		myMaximumDeleteConflictQueryCount = theMaximumDeleteConflictQueryCount;
	}

	/**
	 * <p>
	 * This determines whether $binary-access-write operations should first load the InputStream into memory before persisting the
	 * contents to the database. This needs to be enabled for MS SQL Server as this DB requires that the blob size be known
	 * in advance.
	 * </p>
	 * <p>
	 * Note that this setting should be enabled with caution as it can lead to significant demands on memory.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code false}.
	 * </p>
	 *
	 * @since 5.1.0
	 * @deprecated In 5.2.0 this setting no longer does anything
	 */
	@Deprecated
	public void setPreloadBlobFromInputStream(Boolean thePreloadBlobFromInputStream) {
		// ignore
	}

	/**
	 * <p>
	 * This determines the internal search size that is run synchronously during operations such as searching for
	 * Code System IDs by System and Code
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public Integer getInternalSynchronousSearchSize() {
		return myInternalSynchronousSearchSize;
	}

	/**
	 * <p>
	 * This determines the internal search size that is run synchronously during operations such as searching for
	 * Code System IDs by System and Code
	 * </p>
	 *
	 * @since 5.4.0
	 */
	public void setInternalSynchronousSearchSize(Integer theInternalSynchronousSearchSize) {
		myInternalSynchronousSearchSize = theInternalSynchronousSearchSize;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to activate and run <b>Bulk Import</b>
	 * batch jobs. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public boolean isEnableTaskBulkImportJobExecution() {
		return myEnableTaskBulkImportJobExecution;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to activate and run <b>Bulk Import</b>
	 * batch jobs. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public void setEnableTaskBulkImportJobExecution(boolean theEnableTaskBulkImportJobExecution) {
		myEnableTaskBulkImportJobExecution = theEnableTaskBulkImportJobExecution;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to activate and run <b>Bulk Export</b>
	 * batch jobs. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public boolean isEnableTaskBulkExportJobExecution() {
		return myEnableTaskBulkExportJobExecution;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to activate and run <b>Bulk Export</b>
	 * batch jobs. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public void setEnableTaskBulkExportJobExecution(boolean theEnableTaskBulkExportJobExecution) {
		myEnableTaskBulkExportJobExecution = theEnableTaskBulkExportJobExecution;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to pre-expand any ValueSets that
	 * have been uploaded and are not yet pre-expanded. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public boolean isEnableTaskPreExpandValueSets() {
		return myEnableTaskPreExpandValueSets;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to pre-expand any ValueSets that
	 * have been uploaded and are not yet pre-expanded. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public void setEnableTaskPreExpandValueSets(boolean theEnableTaskPreExpandValueSets) {
		myEnableTaskPreExpandValueSets = theEnableTaskPreExpandValueSets;
	}

	/**
	 * If this is enabled (this is the default), this server will periodically scan for and try to delete
	 * stale searches in the database. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public boolean isEnableTaskStaleSearchCleanup() {
		return myEnableTaskStaleSearchCleanup;
	}

	/**
	 * If this is enabled (this is the default), this server will periodically scan for and try to delete
	 * stale searches in the database. Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public void setEnableTaskStaleSearchCleanup(boolean theEnableTaskStaleSearchCleanup) {
		myEnableTaskStaleSearchCleanup = theEnableTaskStaleSearchCleanup;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to run resource reindexing jobs.
	 * Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public boolean isEnableTaskResourceReindexing() {
		return myEnableTaskResourceReindexing;
	}

	/**
	 * If this is enabled (this is the default), this server will attempt to run resource reindexing jobs.
	 * Otherwise, this server will not.
	 *
	 * @since 5.5.0
	 */
	public void setEnableTaskResourceReindexing(boolean theEnableTaskResourceReindexing) {
		myEnableTaskResourceReindexing = theEnableTaskResourceReindexing;
	}

	/**
	 * If set to true (default is false), date indexes will account for null values in the range columns. As of 5.3.0
	 * we no longer place null values in these columns, but legacy data may exist that still has these values. Note that
	 * enabling this results in more complexity in the search SQL.
	 *
	 * @since 5.5.0
	 */
	public boolean isAccountForDateIndexNulls() {
		return myAccountForDateIndexNulls;
	}

	/**
	 * If set to true (default is false), date indexes will account for null values in the range columns. As of 5.3.0
	 * we no longer place null values in these columns, but legacy data may exist that still has these values. Note that
	 * enabling this results in more complexity in the search SQL.
	 *
	 * @since 5.5.0
	 */
	public void setAccountForDateIndexNulls(boolean theAccountForDateIndexNulls) {
		myAccountForDateIndexNulls = theAccountForDateIndexNulls;
	}

	public boolean canDeleteExpunge() {
		return isAllowMultipleDelete() && isExpungeEnabled() && isDeleteExpungeEnabled();
	}

	public String cannotDeleteExpungeReason() {
		List<String> reasons = new ArrayList<>();
		if (!isAllowMultipleDelete()) {
			reasons.add("Multiple Delete");
		}
		if (!isExpungeEnabled()) {
			reasons.add("Expunge");
		}
		if (!isDeleteExpungeEnabled()) {
			reasons.add("Delete Expunge");
		}
		String retval = "Delete Expunge is not supported on this server.  ";
		if (reasons.size() == 1) {
			retval += reasons.get(0) + " is disabled.";
		} else {
			retval += "The following configurations are disabled: " + StringUtils.join(reasons, ", ");
		}
		return retval;
	}

	/**
	 * Sets a prefix for any indexes created when interacting with hsearch. This will apply to fulltext search indexes
	 * and terminology expansion indexes.
	 *
	 * @since 5.6.0
	 */
	public String getHSearchIndexPrefix() {
		return myHSearchIndexPrefix;
	}

	/**
	 * Sets a prefix for any indexes created when interacting with hsearch. This will apply to fulltext search indexes
	 * and terminology expansion indexes.
	 *
	 * @since 5.6.0
	 */
	public void setHSearchIndexPrefix(String thePrefix) {
		myHSearchIndexPrefix = thePrefix;
	}

	/**
	 * @deprecated Use {@link #isHibernateSearchIndexSearchParams()} instead
	 */
	@Deprecated(since = "8.0.0", forRemoval = true)
	public boolean isAdvancedHSearchIndexing() {
		return isHibernateSearchIndexSearchParams();
	}

	/**
	 * @deprecated Use {@link #setHibernateSearchIndexSearchParams(boolean)} instead
	 */
	@Deprecated(since = "8.0.0", forRemoval = true)
	public void setAdvancedHSearchIndexing(boolean theAdvancedHSearchIndexing) {
		setHibernateSearchIndexSearchParams(theAdvancedHSearchIndexing);
	}

	/**
	 * Is HSearch indexing enabled beyond {@literal _content} or {@literal _text}?
	 * If this setting is enabled, other search parameters will also be indexed using
	 * Hibernate Search, allowing more kinds of searches to be performed using the
	 * fulltext engine.
	 *
	 * <p>
	 * Note that this property was called "setAdvancedHSearchIndexing" prior to HAPI FHIR 8.0.0
	 * </p>
	 *
	 * @since 5.6.0
	 */
	public boolean isHibernateSearchIndexSearchParams() {
		return myHibernateSearchIndexSearchParams;
	}

	/**
	 * Enable/disable HSearch indexing enabled beyond _contains or _text.
	 * <p>
	 * String, token, and reference parameters can be indexed in HSearch.
	 * This extends token search to support :text searches, as well as supporting
	 * :contains and :text on string parameters.
	 * </p>
	 * <p>
	 * Note that this property was called "setAdvancedHSearchIndexing" prior to HAPI FHIR 8.0.0
	 * </p>
	 *
	 * @since 5.6.0
	 */
	public void setHibernateSearchIndexSearchParams(boolean theAdvancedHSearchIndexing) {
		this.myHibernateSearchIndexSearchParams = theAdvancedHSearchIndexing;
	}

	/**
	 * Is storing of Resource in HSearch index enabled?
	 *
	 * @since 5.7.0
	 */
	public boolean isStoreResourceInHSearchIndex() {
		return myStoreResourceInHSearchIndex;
	}

	/**
	 * <p>
	 * Enable Resource to be stored inline with HSearch index mappings.
	 * This is useful in cases where after performing a search operation the resulting resource identifiers don't have to be
	 * looked up in the persistent storage, but rather the inline stored resource can be used instead.
	 * </p>
	 * <p>
	 * For e.g - Storing Observation resource in HSearch index would be useful when performing
	 * <a href="https://www.hl7.org/fhir/observation-operation-lastn.html">$lastn</a> operation.
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public void setStoreResourceInHSearchIndex(boolean theStoreResourceInHSearchIndex) {
		myStoreResourceInHSearchIndex = theStoreResourceInHSearchIndex;
	}

	/**
	 * @see FhirValidator#isConcurrentBundleValidation()
	 * @since 5.7.0
	 */
	public boolean isConcurrentBundleValidation() {
		return myConcurrentBundleValidation;
	}

	/**
	 * @see FhirValidator#isConcurrentBundleValidation()
	 * @since 5.7.0
	 */
	public JpaStorageSettings setConcurrentBundleValidation(boolean theConcurrentBundleValidation) {
		myConcurrentBundleValidation = theConcurrentBundleValidation;
		return this;
	}

	/**
	 * This setting indicates whether binaries are allowed to be automatically inflated from external storage during requests.
	 * Default is true.
	 *
	 * @return whether binaries are allowed to be automatically inflated from external storage during requests.
	 * @since 6.0.0
	 */
	public boolean isAllowAutoInflateBinaries() {
		return myAllowAutoInflateBinaries;
	}

	/**
	 * This setting indicates whether binaries are allowed to be automatically inflated from external storage during requests.
	 * Default is true.
	 *
	 * @param theAllowAutoDeExternalizingBinaries the value to set.
	 * @since 6.0.0
	 */
	public void setAllowAutoInflateBinaries(boolean theAllowAutoDeExternalizingBinaries) {
		myAllowAutoInflateBinaries = theAllowAutoDeExternalizingBinaries;
	}

	/**
	 * This setting controls how many bytes of binaries will be automatically inflated from external storage during requests.
	 * which contain binary data.
	 * Default is 10MB
	 *
	 * @return the number of bytes to de-externalize during requests.
	 * @since 6.0.0
	 */
	public long getAutoInflateBinariesMaximumBytes() {
		return myAutoInflateBinariesMaximumBytes;
	}

	/**
	 * This setting controls how many bytes of binaries will be automatically inflated from external storage during requests.
	 * which contain binary data.
	 * Default is 10MB
	 *
	 * @param theAutoInflateBinariesMaximumBytes the maximum number of bytes to de-externalize.
	 * @since 6.0.0
	 */
	public void setAutoInflateBinariesMaximumBytes(long theAutoInflateBinariesMaximumBytes) {
		myAutoInflateBinariesMaximumBytes = theAutoInflateBinariesMaximumBytes;
	}

	/**
	 * This setting controls how long Bulk Export collection entities will be retained after job start.
	 * Default is 2 hours. Setting this value to 0 or less will cause Bulk Export collection entities to never be expired.
	 *
	 * @since 6.0.0
	 */
	public int getBulkExportFileRetentionPeriodHours() {
		return myBulkExportFileRetentionPeriodHours;
	}

	/**
	 * This setting controls how long Bulk Export collection entities will be retained after job start.
	 * Default is 2 hours. Setting this value to 0 or less will cause Bulk Export collection entities to never be expired.
	 *
	 * @since 6.0.0
	 */
	public void setBulkExportFileRetentionPeriodHours(int theBulkExportFileRetentionPeriodHours) {
		myBulkExportFileRetentionPeriodHours = theBulkExportFileRetentionPeriodHours;
	}

	/**
	 * This setting controls whether, upon receiving a request for an $export operation, if a batch job already exists
	 * that exactly matches the new request, the system should attempt to reuse the batch job. Default is true.
	 */
	public boolean getEnableBulkExportJobReuse() {
		return myEnableBulkExportJobReuse;
	}

	/**
	 * This setting controls whether, upon receiving a request for an $export operation, if a batch job already exists
	 * that exactly matches the new request, the system should attempt to reuse the batch job. Default is true.
	 */
	public void setEnableBulkExportJobReuse(boolean theEnableBulkExportJobReuse) {
		myEnableBulkExportJobReuse = theEnableBulkExportJobReuse;
	}

	/**
	 * This setting indicates whether updating the history of a resource is allowed.
	 * Default is false.
	 *
	 * @since 6.1.0
	 */
	public boolean isUpdateWithHistoryRewriteEnabled() {
		return myUpdateWithHistoryRewriteEnabled;
	}

	/**
	 * This setting indicates whether updating the history of a resource is allowed.
	 * Default is false.
	 *
	 * @since 6.1.0
	 */
	public void setUpdateWithHistoryRewriteEnabled(boolean theUpdateWithHistoryRewriteEnabled) {
		myUpdateWithHistoryRewriteEnabled = theUpdateWithHistoryRewriteEnabled;
	}

	/**
	 * This setting indicate whether a providedResource.meta.source requestID (source#requestID)
	 * should be preserved or overwritten.
	 *
	 * @since 6.2.0
	 */
	public boolean isPreserveRequestIdInResourceBody() {
		return myPreserveRequestIdInResourceBody;
	}

	/**
	 * This setting indicate whether a providedResource.meta.source requestID (source#requestID)
	 * should be preserved or overwritten.
	 * Default is false. This means that a client provided requestId will be overwritten.
	 *
	 * @since 6.2.0
	 */
	public void setPreserveRequestIdInResourceBody(boolean thePreserveRequestIdInResourceBody) {
		myPreserveRequestIdInResourceBody = thePreserveRequestIdInResourceBody;
	}

	/**
	 * This setting controls how many resources will be stored in each binary file created by a bulk export.
	 * Default is 1000 resources per file.
	 *
	 * @since 6.2.0
	 */
	public int getBulkExportFileMaximumCapacity() {
		return myBulkExportFileMaximumCapacity;
	}

	/**
	 * This setting controls how many resources will be stored in each binary file created by a bulk export.
	 * Default is 1000 resources per file.
	 *
	 * @since 6.2.0
	 * @see #setBulkExportFileMaximumCapacity(int)
	 */
	public void setBulkExportFileMaximumCapacity(int theBulkExportFileMaximumCapacity) {
		myBulkExportFileMaximumCapacity = theBulkExportFileMaximumCapacity;
	}

	/**
	 * Defines the maximum size for a single work chunk or report file to be held in
	 * memory or stored in the database for bulk export jobs.
	 * Note that the framework will attempt to not exceed this limit, but will only
	 * estimate the actual chunk size as it works, so this value should be set
	 * below any hard limits that may be present.
	 *
	 * @since 7.2.0
	 * @see #DEFAULT_BULK_EXPORT_MAXIMUM_WORK_CHUNK_SIZE The default value for this setting
	 */
	public long getBulkExportFileMaximumSize() {
		return myBulkExportFileMaximumSize;
	}

	/**
	 * Defines the maximum size for a single work chunk or report file to be held in
	 * memory or stored in the database for bulk export jobs. Default is 100 MB.
	 * Note that the framework will attempt to not exceed this limit, but will only
	 * estimate the actual chunk size as it works, so this value should be set
	 * below any hard limits that may be present.
	 *
	 * @since 7.2.0
	 * @see #setBulkExportFileMaximumCapacity(int)
	 * @see #DEFAULT_BULK_EXPORT_MAXIMUM_WORK_CHUNK_SIZE The default value for this setting
	 */
	public void setBulkExportFileMaximumSize(long theBulkExportFileMaximumSize) {
		Validate.isTrue(theBulkExportFileMaximumSize > 0, "theBulkExportFileMaximumSize must be positive");
		myBulkExportFileMaximumSize = theBulkExportFileMaximumSize;
	}

	/**
	 * If this setting is enabled, then gated batch jobs that produce only one chunk will immediately trigger a batch
	 * maintenance job.  This may be useful for testing, but is not recommended for production use.
	 *
	 * @since 6.4.0
	 */
	public boolean isJobFastTrackingEnabled() {
		return myJobFastTrackingEnabled;
	}

	/**
	 * If this setting is enabled, then gated batch jobs that produce only one chunk will immediately trigger a batch
	 * maintenance job.  This may be useful for testing, but is not recommended for production use.
	 *
	 * @since 6.4.0
	 */
	public void setJobFastTrackingEnabled(boolean theJobFastTrackingEnabled) {
		myJobFastTrackingEnabled = theJobFastTrackingEnabled;
	}

	/**
	 * If set to {@literal false} (default is {@literal true}), the server will not
	 * preserve resource history and will delete previous versions of resources when
	 * a resource is updated.
	 * <p>
	 * Note that this does not make the server completely version-less. Resources will
	 * still have a version number which increases every time a resource is modified,
	 * operations such as vread and history will still be supported, and features
	 * such as ETags and ETag-aware updates will still work. Disabling this setting
	 * simply means that when a resource is updated, the previous version of the
	 * resource will be expunged. This could be done in order to conserve space, or
	 * in cases where there is no business value to storing previous versions of
	 * resources.
	 * </p>
	 *
	 * @since 7.0.0
	 */
	public boolean isResourceDbHistoryEnabled() {
		return myResourceHistoryDbEnabled;
	}

	/**
	 * If set to {@literal false} (default is {@literal true}), the server will not
	 * preserve resource history and will delete previous versions of resources when
	 * a resource is updated.
	 * <p>
	 * Note that this does not make the server completely version-less. Resources will
	 * still have a version number which increases every time a resource is modified,
	 * operations such as vread and history will still be supported, and features
	 * such as ETags and ETag-aware updates will still work. Disabling this setting
	 * simply means that when a resource is updated, the previous version of the
	 * resource will be expunged. This could be done in order to conserve space, or
	 * in cases where there is no business value to storing previous versions of
	 * resources.
	 * </p>
	 *
	 * @since 7.0.0
	 */
	public void setResourceDbHistoryEnabled(boolean theResourceHistoryEnabled) {
		myResourceHistoryDbEnabled = theResourceHistoryEnabled;
	}

	/**
	 * This setting controls whether MdmLink and other non-resource DB history is enabled.
	 * <p/>
	 * By default, this is enabled unless explicitly disabled.
	 *
	 * @return Whether non-resource DB history is enabled (default is true);
	 * @since 6.6.0
	 */
	public boolean isNonResourceDbHistoryEnabled() {
		return myNonResourceDbHistoryEnabled;
	}

	/**
	 * This setting controls the validation issue severity to report when a code validation
	 * finds that the code is present in the given CodeSystem, but the display name being
	 * validated doesn't match the expected value(s). Defaults to
	 * {@link IValidationSupport.IssueSeverity#WARNING}. Set this
	 * value to {@link IValidationSupport.IssueSeverity#INFORMATION}
	 * if you don't want to see display name validation issues at all in resource validation
	 * outcomes.
	 *
	 * @since 7.0.0
	 */
	@Nonnull
	public IValidationSupport.IssueSeverity getIssueSeverityForCodeDisplayMismatch() {
		return myIssueSeverityForCodeDisplayMismatch;
	}

	/**
	 * This setting controls the validation issue severity to report when a code validation
	 * finds that the code is present in the given CodeSystem, but the display name being
	 * validated doesn't match the expected value(s). Defaults to
	 * {@link IValidationSupport.IssueSeverity#WARNING}. Set this
	 * value to {@link IValidationSupport.IssueSeverity#INFORMATION}
	 * if you don't want to see display name validation issues at all in resource validation
	 * outcomes.
	 *
	 * @param theIssueSeverityForCodeDisplayMismatch The severity. Must not be {@literal null}.
	 * @since 7.0.0
	 */
	public void setIssueSeverityForCodeDisplayMismatch(
			@Nonnull IValidationSupport.IssueSeverity theIssueSeverityForCodeDisplayMismatch) {
		Validate.notNull(
				theIssueSeverityForCodeDisplayMismatch, "theIssueSeverityForCodeDisplayMismatch must not be null");
		myIssueSeverityForCodeDisplayMismatch = theIssueSeverityForCodeDisplayMismatch;
	}

	/**
	 * This method returns whether data will be stored in LOB columns as well as the columns
	 * introduced to migrate away from LOB.  Writing to LOB columns is set to false by
	 * default.  Enabling the setting will effectively double the persisted information.
	 * If enabled, a careful monitoring of LOB table (if applicable) is required to avoid
	 * exceeding the table maximum capacity.
	 *
	 * @since 7.2.0
	 */
	public boolean isWriteToLegacyLobColumns() {
		return myWriteToLegacyLobColumns;
	}

	/**
	 * This setting controls whether data will be stored in LOB columns as well as the columns
	 * introduced to migrate away from LOB.  Writing to LOB columns is set to false by
	 * default.  Enabling the setting will effectively double the persisted information.
	 * When enabled, a careful monitoring of LOB table (if applicable) is required to avoid
	 * exceeding the table maximum capacity.
	 *
	 * @param theWriteToLegacyLobColumns
	 * @since 7.2.0
	 */
	public void setWriteToLegacyLobColumns(boolean theWriteToLegacyLobColumns) {
		myWriteToLegacyLobColumns = theWriteToLegacyLobColumns;
	}

	/**
	 * This setting controls whether MdmLink and other non-resource DB history is enabled.
	 * <p/>
	 * By default, this is enabled unless explicitly disabled.
	 *
	 * @param theNonResourceDbHistoryEnabled Whether non-resource DB history is enabled (default is true);
	 * @since 6.6.0
	 */
	public void setNonResourceDbHistoryEnabled(boolean theNonResourceDbHistoryEnabled) {
		myNonResourceDbHistoryEnabled = theNonResourceDbHistoryEnabled;
	}

	public void setPreventInvalidatingConditionalMatchCriteria(boolean theCriteria) {
		myPreventInvalidatingConditionalMatchCriteria = theCriteria;
	}

	public boolean isPreventInvalidatingConditionalMatchCriteria() {
		return myPreventInvalidatingConditionalMatchCriteria;
	}

	public long getRestDeleteByUrlResourceIdThreshold() {
		return myRestDeleteByUrlResourceIdThreshold;
	}

	public void setRestDeleteByUrlResourceIdThreshold(long theRestDeleteByUrlResourceIdThreshold) {
		myRestDeleteByUrlResourceIdThreshold = theRestDeleteByUrlResourceIdThreshold;
	}

	/**
	 * If we are batching write operations in transactions, what should the maximum number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public int getMaxTransactionEntriesForWrite() {
		return myMaxTransactionEntriesForWrite;
	}

	/**
	 * If we are batching write operations in transactions, what should the maximum number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public void setMaxTransactionEntriesForWrite(int theMaxTransactionEntriesForWrite) {
		myMaxTransactionEntriesForWrite = theMaxTransactionEntriesForWrite;
	}

	/**
	 * If we are batching write operations in transactions, what should the default number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public int getDefaultTransactionEntriesForWrite() {
		return myDefaultTransactionEntriesForWrite;
	}

	/**
	 * If we are batching write operations in transactions, what should the default number of write operations per
	 * transaction be?
	 * @since 8.0.0
	 */
	public void setDefaultTransactionEntriesForWrite(int theDefaultTransactionEntriesForWrite) {
		myDefaultTransactionEntriesForWrite = theDefaultTransactionEntriesForWrite;
	}

	/**
	 * Controls whether the server writes data to the <code>HFJ_SPIDX_IDENTITY</code> table.
	 * <p>
	 * Defaults to {@code true}. If set to {@code false}, the server will skip writing to the table.
	 * This should normally remain {@code true}, but is configurable for use in unit tests.
	 *
	 * @since 8.2.0
	 */
	public boolean isWriteToSearchParamIdentityTable() {
		return myWriteToSearchParamIdentityTable;
	}

	/**
	 * Controls whether the server writes data to the <code>HFJ_SPIDX_IDENTITY</code> table.
	 * <p>
	 * Defaults to {@code true}. If set to {@code false}, the server will skip writing to the table.
	 * This should normally remain {@code true}, but is configurable for use in unit tests.
	 *
	 * @since 8.2.0
	 */
	public void setWriteToSearchParamIdentityTable(boolean theWriteToSearchParamIdentityTable) {
		myWriteToSearchParamIdentityTable = theWriteToSearchParamIdentityTable;
	}

	public enum StoreMetaSourceInformationEnum {
		NONE(false, false),
		SOURCE_URI(true, false),
		REQUEST_ID(false, true),
		SOURCE_URI_AND_REQUEST_ID(true, true);

		private final boolean myStoreSourceUri;
		private final boolean myStoreRequestId;

		StoreMetaSourceInformationEnum(boolean theStoreSourceUri, boolean theStoreRequestId) {
			myStoreSourceUri = theStoreSourceUri;
			myStoreRequestId = theStoreRequestId;
		}

		public boolean isStoreSourceUri() {
			return myStoreSourceUri;
		}

		public boolean isStoreRequestId() {
			return myStoreRequestId;
		}
	}

	/**
	 * This enum provides allowable options for {@link #setResourceServerIdStrategy(IdStrategyEnum)}
	 */
	public enum IdStrategyEnum {
		/**
		 * This strategy is the default strategy, and it simply uses a sequential
		 * numeric ID for each newly created resource.
		 */
		SEQUENTIAL_NUMERIC,
		/**
		 * Each resource will receive a randomly generated UUID
		 */
		UUID
	}

	/**
	 * This enum provides allowable options for {@link #setResourceClientIdStrategy(ClientIdStrategyEnum)}
	 */
	public enum ClientIdStrategyEnum {
		/**
		 * Clients are not allowed to supply IDs for resources that do not
		 * already exist
		 */
		NOT_ALLOWED,

		/**
		 * Clients may supply IDs but these IDs are not permitted to be purely
		 * numeric. In other words, values such as "A", "A1" and "000A" would be considered
		 * valid but "123" would not.
		 * <p><b>This is the default setting.</b></p>
		 */
		ALPHANUMERIC,

		/**
		 * Clients may supply any ID including purely numeric IDs. Note that this setting should
		 * only be set on an empty database, or on a database that has always had this setting
		 * set as it causes a "forced ID" to be used for all resources.
		 * <p>
		 * Note that if you use this setting, it is highly recommended that you also
		 * set the {@link #setResourceServerIdStrategy(IdStrategyEnum) ResourceServerIdStrategy}
		 * to {@link IdStrategyEnum#UUID} in order to avoid any potential for conflicts. Otherwise
		 * a database sequence will be used to generate IDs and these IDs can conflict with
		 * client-assigned numeric IDs.
		 * </p>
		 */
		ANY
	}
}
