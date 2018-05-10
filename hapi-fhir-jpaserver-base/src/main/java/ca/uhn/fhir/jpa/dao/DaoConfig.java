package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public class DaoConfig {

	/**
	 * Default {@link #getTreatReferencesAsLogical() logical URL bases}. Includes the following
	 * values:
	 * <ul>
	 * <li><code>"http://hl7.org/fhir/valueset-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/codesystem-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/StructureDefinition/*"</code></li>
	 * </ul>
	 */
	public static final Set<String> DEFAULT_LOGICAL_BASE_URLS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
		"http://hl7.org/fhir/ValueSet/*",
		"http://hl7.org/fhir/CodeSystem/*",
		"http://hl7.org/fhir/valueset-*",
		"http://hl7.org/fhir/codesystem-*",
		"http://hl7.org/fhir/StructureDefinition/*")));
	/**
	 * Default value for {@link #setReuseCachedSearchResultsForMillis(Long)}: 60000ms (one minute)
	 */
	public static final Long DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS = DateUtils.MILLIS_PER_MINUTE;
	/**
	 * Default value for {@link #setMaximumSearchResultCountInTransaction(Integer)}
	 *
	 * @see #setMaximumSearchResultCountInTransaction(Integer)
	 */
	private static final Integer DEFAULT_MAXIMUM_SEARCH_RESULT_COUNT_IN_TRANSACTION = null;
	private IndexEnabledEnum myIndexMissingFieldsEnabled = IndexEnabledEnum.DISABLED;
	/**
	 * Default value for {@link #setTranslationCachesExpireAfterWriteInMinutes(Long)}: 60 minutes
	 *
	 * @see #setTranslationCachesExpireAfterWriteInMinutes(Long)
	 */
	public static final Long DEFAULT_TRANSLATION_CACHES_EXPIRE_AFTER_WRITE_IN_MINUTES = 60L;
	/**
	 * update setter javadoc if default changes
	 */
	private Long myTranslationCachesExpireAfterWriteInMinutes = DEFAULT_TRANSLATION_CACHES_EXPIRE_AFTER_WRITE_IN_MINUTES;
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowExternalReferences = false;
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowContainsSearches = true;

	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowInlineMatchUrlReferences = true;
	private boolean myAllowMultipleDelete;
	private boolean myDefaultSearchParamsCanBeOverridden = false;
	/**
	 * update setter javadoc if default changes
	 */
	private int myDeferIndexingForCodesystemsOfSize = 2000;
	private boolean myDeleteStaleSearches = true;
	private boolean myEnforceReferentialIntegrityOnDelete = true;
	private boolean myUniqueIndexesEnabled = true;
	private boolean myUniqueIndexesCheckedBeforeSave = true;
	private boolean myEnforceReferentialIntegrityOnWrite = true;
	private int myEverythingIncludesFetchPageSize = 50;
	/**
	 * update setter javadoc if default changes
	 */
	private long myExpireSearchResultsAfterMillis = DateUtils.MILLIS_PER_HOUR;
	/**
	 * update setter javadoc if default changes
	 */
	private Integer myFetchSizeDefaultMaximum = null;
	private int myHardTagListLimit = 1000;
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myIndexContainedResources = true;
	private List<IServerInterceptor> myInterceptors;
	/**
	 * update setter javadoc if default changes
	 */
	private int myMaximumExpansionSize = 5000;
	private Integer myMaximumSearchResultCountInTransaction = DEFAULT_MAXIMUM_SEARCH_RESULT_COUNT_IN_TRANSACTION;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;
	/**
	 * update setter javadoc if default changes
	 */
	private Integer myResourceMetaCountHardLimit = 1000;
	private Long myReuseCachedSearchResultsForMillis = DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS;
	private boolean mySchedulingDisabled;
	private boolean mySuppressUpdatesWithNoChange = true;
	private Set<String> myTreatBaseUrlsAsLocal = new HashSet<>();
	private Set<String> myTreatReferencesAsLogical = new HashSet<>(DEFAULT_LOGICAL_BASE_URLS);
	private boolean myAutoCreatePlaceholderReferenceTargets;
	private Integer myCacheControlNoStoreMaxResultsUpperLimit = 1000;
	private Integer myCountSearchResultsUpTo = null;
	private IdStrategyEnum myResourceServerIdStrategy = IdStrategyEnum.SEQUENTIAL_NUMERIC;
	private boolean myMarkResourcesForReindexingUponSearchParameterChange;
	private boolean myExpungeEnabled;

	/**
	 * Constructor
	 */
	public DaoConfig() {
		setSubscriptionEnabled(true);
		setSubscriptionPollDelay(0);
		setSubscriptionPurgeInactiveAfterMillis(Long.MAX_VALUE);
		setMarkResourcesForReindexingUponSearchParameterChange(true);
	}

	/**
	 * Add a value to the {@link #setTreatReferencesAsLogical(Set) logical references list}.
	 *
	 * @see #setTreatReferencesAsLogical(Set)
	 */
	public void addTreatReferencesAsLogical(String theTreatReferencesAsLogical) {
		validateTreatBaseUrlsAsLocal(theTreatReferencesAsLogical);

		if (myTreatReferencesAsLogical == null) {
			myTreatReferencesAsLogical = new HashSet<>();
		}
		myTreatReferencesAsLogical.add(theTreatReferencesAsLogical);
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
	 * Defaults to 2000
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
	 * Defaults to 2000
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
	 * Gets the maximum number of results to return in a GetTags query (DSTU1 only)
	 */
	public int getHardTagListLimit() {
		return myHardTagListLimit;
	}

	/**
	 * Gets the maximum number of results to return in a GetTags query (DSTU1 only)
	 */
	public void setHardTagListLimit(int theHardTagListLimit) {
		myHardTagListLimit = theHardTagListLimit;
	}

	/**
	 * If set to {@link IndexEnabledEnum#DISABLED} (default is {@link IndexEnabledEnum#DISABLED})
	 * the server will not create search indexes for search parameters with no values in resources.
	 * <p>
	 * Disabling this feature means that the <code>:missing</code> search modifier will not be
	 * supported on the server, but also means that storage and indexing (i.e. writes to the
	 * database) may be much faster on servers which have lots of search parameters and need
	 * to write quickly.
	 * </p>
	 * <p>
	 * This feature may be enabled on servers where supporting the use of the :missing parameter is
	 * of higher importance than raw write performance
	 * </p>
	 */
	public IndexEnabledEnum getIndexMissingFields() {
		return myIndexMissingFieldsEnabled;
	}

	/**
	 * If set to {@link IndexEnabledEnum#DISABLED} (default is {@link IndexEnabledEnum#DISABLED})
	 * the server will not create search indexes for search parameters with no values in resources.
	 * <p>
	 * Disabling this feature means that the <code>:missing</code> search modifier will not be
	 * supported on the server, but also means that storage and indexing (i.e. writes to the
	 * database) may be much faster on servers which have lots of search parameters and need
	 * to write quickly.
	 * </p>
	 * <p>
	 * This feature may be enabled on servers where supporting the use of the :missing parameter is
	 * of higher importance than raw write performance
	 * </p>
	 * <p>
	 * Note that this setting also has an impact on sorting (i.e. using the
	 * <code>_sort</code> parameter on searches): If the server is configured
	 * to not index missing field.
	 * </p>
	 */
	public void setIndexMissingFields(IndexEnabledEnum theIndexMissingFields) {
		Validate.notNull(theIndexMissingFields, "theIndexMissingFields must not be null");
		myIndexMissingFieldsEnabled = theIndexMissingFields;
	}

	/**
	 * Returns the interceptors which will be notified of operations.
	 *
	 * @see #setInterceptors(List)
	 */
	public List<IServerInterceptor> getInterceptors() {
		if (myInterceptors == null) {
			myInterceptors = new ArrayList<>();
		}
		return myInterceptors;
	}

	/**
	 * This may be used to optionally register server interceptors directly against the DAOs.
	 */
	public void setInterceptors(IServerInterceptor... theInterceptor) {
		setInterceptors(new ArrayList<IServerInterceptor>());
		if (theInterceptor != null && theInterceptor.length != 0) {
			getInterceptors().addAll(Arrays.asList(theInterceptor));
		}
	}

	/**
	 * See {@link #setMaximumExpansionSize(int)}
	 */
	public int getMaximumExpansionSize() {
		return myMaximumExpansionSize;
	}

	/**
	 * Sets the maximum number of codes that will be added to a valueset expansion before
	 * the operation will be failed as too costly
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
	 * This setting configures the strategy to use in generating IDs for newly
	 * created resources on the server. The default is {@link IdStrategyEnum#SEQUENTIAL_NUMERIC}.
	 */
	public IdStrategyEnum getResourceServerIdStrategy() {
		return myResourceServerIdStrategy;
	}

	/**
	 * This setting configures the strategy to use in generating IDs for newly
	 * created resources on the server. The default is {@link IdStrategyEnum#SEQUENTIAL_NUMERIC}.
	 *
	 * @param theResourceIdStrategy The strategy. Must not be null.
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
	 * Specifies the duration in minutes for which values will be retained after being
	 * written to the terminology translation cache. Defaults to 60.
	 */
	public Long getTranslationCachesExpireAfterWriteInMinutes() {
		return myTranslationCachesExpireAfterWriteInMinutes;
	}

	/**
	 * Specifies the duration in minutes for which values will be retained after being
	 * written to the terminology translation cache. Defaults to 60.
	 */
	public void setTranslationCachesExpireAfterWriteInMinutes(Long translationCachesExpireAfterWriteInMinutes) {
		myTranslationCachesExpireAfterWriteInMinutes = translationCachesExpireAfterWriteInMinutes;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 * <p>
	 * Note that this property has different behaviour from {@link DaoConfig#getTreatReferencesAsLogical()}
	 * </p>
	 *
	 * @see #getTreatReferencesAsLogical()
	 */
	public Set<String> getTreatBaseUrlsAsLocal() {
		return myTreatBaseUrlsAsLocal;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 *
	 * @param theTreatBaseUrlsAsLocal The set of base URLs. May be <code>null</code>, which
	 *                                means no references will be treated as external
	 */
	public void setTreatBaseUrlsAsLocal(Set<String> theTreatBaseUrlsAsLocal) {
		if (theTreatBaseUrlsAsLocal != null) {
			for (String next : theTreatBaseUrlsAsLocal) {
				validateTreatBaseUrlsAsLocal(next);
			}
		}

		HashSet<String> treatBaseUrlsAsLocal = new HashSet<String>();
		for (String next : ObjectUtils.defaultIfNull(theTreatBaseUrlsAsLocal, new HashSet<String>())) {
			while (next.endsWith("/")) {
				next = next.substring(0, next.length() - 1);
			}
			treatBaseUrlsAsLocal.add(next);
		}
		myTreatBaseUrlsAsLocal = treatBaseUrlsAsLocal;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See {@link "http://hl7.org/fhir/references.html"} for
	 * a description of logical references. For example, the valueset
	 * {@link "http://hl7.org/fhir/valueset-quantity-comparator.html"} is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public Set<String> getTreatReferencesAsLogical() {
		return myTreatReferencesAsLogical;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See {@link "http://hl7.org/fhir/references.html"} for
	 * a description of logical references. For example, the valueset
	 * {@link "http://hl7.org/fhir/valueset-quantity-comparator.html"} is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public DaoConfig setTreatReferencesAsLogical(Set<String> theTreatReferencesAsLogical) {
		myTreatReferencesAsLogical = theTreatReferencesAsLogical;
		return this;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>true</code>
	 */
	public boolean isAllowContainsSearches() {
		return myAllowContainsSearches;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>true</code>
	 */
	public void setAllowContainsSearches(boolean theAllowContainsSearches) {
		this.myAllowContainsSearches = theAllowContainsSearches;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public boolean isAllowExternalReferences() {
		return myAllowExternalReferences;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public void setAllowExternalReferences(boolean theAllowExternalReferences) {
		myAllowExternalReferences = theAllowExternalReferences;
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
	 * was an experimental/rpposed feature)
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
	 * When creating or updating a resource: If this property is set to <code>true</code>
	 * (default is <code>false</code>), if the resource has a reference to another resource
	 * on the local server but that reference does not exist, a placeholder resource will be
	 * created.
	 * <p>
	 * In other words, if an observation with subject <code>Patient/FOO</code> is created, but
	 * there is no resource called <code>Patient/FOO</code> on the server, this property causes
	 * an empty patient with ID "FOO" to be created in order to prevent this operation
	 * from failing.
	 * </p>
	 * <p>
	 * This property can be useful in cases where replication between two servers is wanted.
	 * Note however that references containing purely numeric IDs will not be auto-created
	 * as they are never allowed to be client supplied in HAPI FHIR JPA.
	 * </p>
	 */
	public boolean isAutoCreatePlaceholderReferenceTargets() {
		return myAutoCreatePlaceholderReferenceTargets;
	}

	/**
	 * When creating or updating a resource: If this property is set to <code>true</code>
	 * (default is <code>false</code>), if the resource has a reference to another resource
	 * on the local server but that reference does not exist, a placeholder resource will be
	 * created.
	 * <p>
	 * In other words, if an observation with subject <code>Patient/FOO</code> is created, but
	 * there is no resource called <code>Patient/FOO</code> on the server, this property causes
	 * an empty patient with ID "FOO" to be created in order to prevent this operation
	 * from failing.
	 * </p>
	 * <p>
	 * This property can be useful in cases where replication between two servers is wanted.
	 * Note however that references containing purely numeric IDs will not be auto-created
	 * as they are never allowed to be client supplied in HAPI FHIR JPA.
	 * </p>
	 */
	public void setAutoCreatePlaceholderReferenceTargets(boolean theAutoCreatePlaceholderReferenceTargets) {
		myAutoCreatePlaceholderReferenceTargets = theAutoCreatePlaceholderReferenceTargets;
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code false}
	 * </p>
	 */
	public boolean isDefaultSearchParamsCanBeOverridden() {
		return myDefaultSearchParamsCanBeOverridden;
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code false}
	 * </p>
	 */
	public void setDefaultSearchParamsCanBeOverridden(boolean theDefaultSearchParamsCanBeOverridden) {
		myDefaultSearchParamsCanBeOverridden = theDefaultSearchParamsCanBeOverridden;
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
	 * If set to <code>false</code> (default is <code>true</code>) resources will be permitted to be
	 * created or updated even if they contain references to local resources that do not exist.
	 * <p>
	 * For example, if a patient contains a reference to managing organization <code>Organization/FOO</code>
	 * but FOO is not a valid ID for an organization on the server, the operation will be blocked unless
	 * this propery has been set to <code>false</code>
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
	 * this propery has been set to <code>false</code>
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
	 * Should contained IDs be indexed the same way that non-contained IDs are (default is
	 * <code>true</code>)
	 */
	public boolean isIndexContainedResources() {
		return myIndexContainedResources;
	}

	/**
	 * Should contained IDs be indexed the same way that non-contained IDs are (default is
	 * <code>true</code>)
	 */
	public void setIndexContainedResources(boolean theIndexContainedResources) {
		myIndexContainedResources = theIndexContainedResources;
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
	public void setMarkResourcesForReindexingUponSearchParameterChange(boolean theMarkResourcesForReindexingUponSearchParameterChange) {
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
	 * created for search parameters marked as {@link JpaConstants#EXT_SP_UNIQUE}.
	 * This is a HAPI FHIR specific extension which can be used to specify that no more than one
	 * resource can exist which matches a given criteria, using a database constraint to
	 * enforce this.
	 */
	public boolean isUniqueIndexesEnabled() {
		return myUniqueIndexesEnabled;
	}

	/**
	 * If set to <code>true</code> (default is <code>true</code>), indexes will be
	 * created for search parameters marked as {@link JpaConstants#EXT_SP_UNIQUE}.
	 * This is a HAPI FHIR specific extension which can be used to specify that no more than one
	 * resource can exist which matches a given criteria, using a database constraint to
	 * enforce this.
	 */
	public void setUniqueIndexesEnabled(boolean theUniqueIndexesEnabled) {
		myUniqueIndexesEnabled = theUniqueIndexesEnabled;
	}

	/**
	 * Do not call this method, it exists only for legacy reasons. It
	 * will be removed in a future version. Configure the page size on your
	 * paging provider instead.
	 *
	 * @deprecated This method does not do anything. Configure the page size on your
	 * paging provider instead. Deprecated in HAPI FHIR 2.3 (Jan 2017)
	 */
	@Deprecated
	public void setHardSearchLimit(int theHardSearchLimit) {
		// this method does nothing
	}

	/**
	 * This is the maximum number of resources that will be added to a single page of returned resources. Because of
	 * includes with wildcards and other possibilities it is possible for a client to make requests that include very
	 * large amounts of data, so this hard limit can be imposed to prevent runaway requests.
	 *
	 * @deprecated Deprecated in HAPI FHIR 3.2.0 as this method doesn't actually do anything
	 */
	@Deprecated
	public void setIncludeLimit(@SuppressWarnings("unused") int theIncludeLimit) {
		// nothing
	}

	/**
	 * This may be used to optionally register server interceptors directly against the DAOs.
	 */
	public void setInterceptors(List<IServerInterceptor> theInterceptors) {
		myInterceptors = theInterceptors;
	}

	/**
	 * @deprecated As of HAPI FHIR 3.0.0, subscriptions no longer use polling for
	 * detecting changes, so this setting has no effect
	 */
	@Deprecated
	public void setSubscriptionEnabled(boolean theSubscriptionEnabled) {
		// nothing
	}

	/**
	 * @deprecated As of HAPI FHIR 3.0.0, subscriptions no longer use polling for
	 * detecting changes, so this setting has no effect
	 */
	@Deprecated
	public void setSubscriptionPollDelay(long theSubscriptionPollDelay) {
		// ignore
	}

	/**
	 * @deprecated As of HAPI FHIR 3.0.0, subscriptions no longer use polling for
	 * detecting changes, so this setting has no effect
	 */
	@Deprecated
	public void setSubscriptionPurgeInactiveAfterMillis(Long theMillis) {
		// ignore
	}

	public void setSubscriptionPurgeInactiveAfterSeconds(int theSeconds) {
		setSubscriptionPurgeInactiveAfterMillis(theSeconds * DateUtils.MILLIS_PER_SECOND);
	}

	private static void validateTreatBaseUrlsAsLocal(String theUrl) {
		Validate.notBlank(theUrl, "Base URL must not be null or empty");

		int starIdx = theUrl.indexOf('*');
		if (starIdx != -1) {
			if (starIdx != theUrl.length() - 1) {
				throw new IllegalArgumentException("Base URL wildcard character (*) can only appear at the end of the string: " + theUrl);
			}
		}

	}

	public enum IndexEnabledEnum {
		ENABLED,
		DISABLED
	}

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

}
