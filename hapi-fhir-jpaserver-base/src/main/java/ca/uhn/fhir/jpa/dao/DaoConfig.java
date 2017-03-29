package ca.uhn.fhir.jpa.dao;

import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

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

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myAllowExternalReferences = false;

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myAllowInlineMatchUrlReferences = true;

	private boolean myAllowMultipleDelete;
	private boolean myDefaultSearchParamsCanBeOverridden = false;
	// ***
	// update setter javadoc if default changes
	// ***
	private int myDeferIndexingForCodesystemsOfSize = 2000;

	private boolean myDeleteStaleSearches = true;

	// ***
	// update setter javadoc if default changes
	// ***
	private long myExpireSearchResultsAfterMillis = DateUtils.MILLIS_PER_HOUR;

	private int myHardTagListLimit = 1000;

	private int myIncludeLimit = 2000;

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myIndexContainedResources = true;
	private List<IServerInterceptor> myInterceptors;

	// ***
	// update setter javadoc if default changes
	// ***
	private int myMaximumExpansionSize = 5000;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;
	private boolean mySchedulingDisabled;
	private boolean mySubscriptionEnabled;
	private long mySubscriptionPollDelay = 1000;
	private Long mySubscriptionPurgeInactiveAfterMillis;
	private Set<String> myTreatBaseUrlsAsLocal = new HashSet<String>();
	private Set<String> myTreatReferencesAsLogical = new HashSet<String>(DEFAULT_LOGICAL_BASE_URLS);

	/**
	 * Add a value to the {@link #setTreatReferencesAsLogical(Set) logical references list}.
	 * 
	 * @see #setTreatReferencesAsLogical(Set)
	 */
	public void addTreatReferencesAsLogical(String theTreatReferencesAsLogical) {
		validateTreatBaseUrlsAsLocal(theTreatReferencesAsLogical);

		if (myTreatReferencesAsLogical == null) {
			myTreatReferencesAsLogical = new HashSet<String>();
		}
		myTreatReferencesAsLogical.add(theTreatReferencesAsLogical);
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
	 * Sets the number of milliseconds that search results for a given client search
	 * should be preserved before being purged from the database.
	 * <p>
	 * Search results are stored in the database so that they can be paged over multiple
	 * requests. After this
	 * number of milliseconds, they will be deleted from the database, and any paging links
	 * (next/prev links in search response bundles) will become invalid. Defaults to 1 hour.
	 * </p>
	 * <p>
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
	 * Gets the maximum number of results to return in a GetTags query (DSTU1 only)
	 */
	public int getHardTagListLimit() {
		return myHardTagListLimit;
	}

	public int getIncludeLimit() {
		return myIncludeLimit;
	}

	/**
	 * Returns the interceptors which will be notified of operations.
	 * 
	 * @see #setInterceptors(List)
	 * @deprecated As of 2.2 this method is deprecated. There is no good reason to register an interceptor
	 *             with the DaoConfig and not with the server via {@link RestfulServer#registerInterceptor(IServerInterceptor)}
	 */
	@Deprecated
	public List<IServerInterceptor> getInterceptors() {
		if (myInterceptors == null) {
			return Collections.emptyList();
		}
		return myInterceptors;
	}

	/**
	 * See {@link #setMaximumExpansionSize(int)}
	 */
	public int getMaximumExpansionSize() {
		return myMaximumExpansionSize;
	}

	public ResourceEncodingEnum getResourceEncoding() {
		return myResourceEncoding;
	}

	public long getSubscriptionPollDelay() {
		return mySubscriptionPollDelay;
	}

	public Long getSubscriptionPurgeInactiveAfterMillis() {
		return mySubscriptionPurgeInactiveAfterMillis;
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
	 * @see #setAllowInlineMatchUrlReferences(boolean)
	 */
	public boolean isAllowInlineMatchUrlReferences() {
		return myAllowInlineMatchUrlReferences;
	}

	public boolean isAllowMultipleDelete() {
		return myAllowMultipleDelete;
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
	 * Should contained IDs be indexed the same way that non-contained IDs are (default is
	 * <code>true</code>)
	 */
	public boolean isIndexContainedResources() {
		return myIndexContainedResources;
	}

	public boolean isSchedulingDisabled() {
		return mySchedulingDisabled;
	}

	/**
	 * See {@link #setSubscriptionEnabled(boolean)}
	 */
	public boolean isSubscriptionEnabled() {
		return mySubscriptionEnabled;
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

	public void setAllowMultipleDelete(boolean theAllowMultipleDelete) {
		myAllowMultipleDelete = theAllowMultipleDelete;
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
	 * Sets the number of milliseconds that search results for a given client search
	 * should be preserved before being purged from the database.
	 * <p>
	 * Search results are stored in the database so that they can be paged over multiple
	 * requests. After this
	 * number of milliseconds, they will be deleted from the database, and any paging links
	 * (next/prev links in search response bundles) will become invalid. Defaults to 1 hour.
	 * </p>
	 * <p>
	 * 
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
	 * Do not call this method, it exists only for legacy reasons. It
	 * will be removed in a future version. Configure the page size on your
	 * paging provider instead.
	 * 
	 * @deprecated This method does not do anything. Configure the page size on your
	 *             paging provider instead. Deprecated in HAPI FHIR 2.3 (Jan 2017)
	 */
	@Deprecated
	public void setHardSearchLimit(int theHardSearchLimit) {
		// this method does nothing
	}

	/**
	 * Gets the maximum number of results to return in a GetTags query (DSTU1 only)
	 */
	public void setHardTagListLimit(int theHardTagListLimit) {
		myHardTagListLimit = theHardTagListLimit;
	}

	/**
	 * This is the maximum number of resources that will be added to a single page of returned resources. Because of
	 * includes with wildcards and other possibilities it is possible for a client to make requests that include very
	 * large amounts of data, so this hard limit can be imposed to prevent runaway requests.
	 */
	public void setIncludeLimit(int theIncludeLimit) {
		myIncludeLimit = theIncludeLimit;
	}

	/**
	 * Should contained IDs be indexed the same way that non-contained IDs are (default is
	 * <code>true</code>)
	 */
	public void setIndexContainedResources(boolean theIndexContainedResources) {
		myIndexContainedResources = theIndexContainedResources;
	}

	/**
	 * This may be used to optionally register server interceptors directly against the DAOs.
	 * 
	 * @deprecated As of 2.2 this method is deprecated. There is no good reason to register an interceptor
	 *             with the DaoConfig and not with the server via {@link RestfulServer#registerInterceptor(IServerInterceptor)}
	 */
	@Deprecated
	public void setInterceptors(IServerInterceptor... theInterceptor) {
		setInterceptors(new ArrayList<IServerInterceptor>());
		if (theInterceptor != null && theInterceptor.length != 0) {
			getInterceptors().addAll(Arrays.asList(theInterceptor));
		}
	}

	/**
	 * This may be used to optionally register server interceptors directly against the DAOs.
	 * 
	 * @deprecated As of 2.2 this method is deprecated. There is no good reason to register an interceptor
	 *             with the DaoConfig and not with the server via {@link RestfulServer#registerInterceptor(IServerInterceptor)}
	 */
	@Deprecated
	public void setInterceptors(List<IServerInterceptor> theInterceptors) {
		myInterceptors = theInterceptors;
	}

	/**
	 * Sets the maximum number of codes that will be added to a valueset expansion before
	 * the operation will be failed as too costly
	 */
	public void setMaximumExpansionSize(int theMaximumExpansionSize) {
		Validate.isTrue(theMaximumExpansionSize > 0, "theMaximumExpansionSize must be > 0");
		myMaximumExpansionSize = theMaximumExpansionSize;
	}

	public void setResourceEncoding(ResourceEncodingEnum theResourceEncoding) {
		myResourceEncoding = theResourceEncoding;
	}

	public void setSchedulingDisabled(boolean theSchedulingDisabled) {
		mySchedulingDisabled = theSchedulingDisabled;
	}

	/**
	 * If set to true, the server will enable support for subscriptions. Subscriptions
	 * will by default be handled via a polling task. Note that if this is enabled, you must also include Spring task scanning to your XML
	 * config for the scheduled tasks used by the subscription module.
	 */
	public void setSubscriptionEnabled(boolean theSubscriptionEnabled) {
		mySubscriptionEnabled = theSubscriptionEnabled;
	}

	public void setSubscriptionPollDelay(long theSubscriptionPollDelay) {
		mySubscriptionPollDelay = theSubscriptionPollDelay;
	}

	public void setSubscriptionPurgeInactiveAfterMillis(Long theMillis) {
		if (theMillis != null) {
			Validate.exclusiveBetween(0, Long.MAX_VALUE, theMillis);
		}
		mySubscriptionPurgeInactiveAfterMillis = theMillis;
	}

	public void setSubscriptionPurgeInactiveAfterSeconds(int theSeconds) {
		setSubscriptionPurgeInactiveAfterMillis(theSeconds * DateUtils.MILLIS_PER_SECOND);
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
	 * @param theTreatBaseUrlsAsLocal
	 *           The set of base URLs. May be <code>null</code>, which
	 *           means no references will be treated as external
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
	public DaoConfig setTreatReferencesAsLogical(Set<String> theTreatReferencesAsLogical) {
		myTreatReferencesAsLogical = theTreatReferencesAsLogical;
		return this;
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

}
