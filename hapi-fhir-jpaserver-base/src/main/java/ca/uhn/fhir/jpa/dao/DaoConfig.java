package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public class DaoConfig {

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myAllowExternalReferences = false; 

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myAllowInlineMatchUrlReferences = false; 

	private boolean myAllowMultipleDelete;
	// ***
	// update setter javadoc if default changes
	// ***
	private long myExpireSearchResultsAfterMillis = DateUtils.MILLIS_PER_HOUR;
	private int myHardSearchLimit = 1000;
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
	
	/**
	 * Sets the number of milliseconds that search results for a given client search 
	 * should be preserved before being purged from the database.
	 * <p>
	 * Search results are stored in the database so that they can be paged over multiple 
	 * requests. After this
	 * number of milliseconds, they will be deleted from the database, and any paging links
	 * (next/prev links in search response bundles) will become invalid. Defaults to 1 hour. 
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
	 */
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
	 */
	public Set<String> getTreatBaseUrlsAsLocal() {
		return myTreatBaseUrlsAsLocal;
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
	 * Default is false for now, as this is an experimental feature.
	 * </p>
	 * @since 1.5
	 */
	public void setAllowInlineMatchUrlReferences(boolean theAllowInlineMatchUrlReferences) {
		myAllowInlineMatchUrlReferences = theAllowInlineMatchUrlReferences;
	}

	public void setAllowMultipleDelete(boolean theAllowMultipleDelete) {
		myAllowMultipleDelete = theAllowMultipleDelete;
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
	 * 
	 * @since 1.5
	 */
	public void setExpireSearchResultsAfterMillis(long theExpireSearchResultsAfterMillis) {
		myExpireSearchResultsAfterMillis = theExpireSearchResultsAfterMillis;
	}

	public void setHardSearchLimit(int theHardSearchLimit) {
		myHardSearchLimit = theHardSearchLimit;
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
	 * <p>
	 * Registering server action interceptors against the JPA DAOs can be more powerful than registering them against the
	 * {@link RestfulServer}, since the DAOs are able to break transactions into individual actions, and will account for
	 * match URLs (e.g. if a request contains an If-None-Match URL, the ID will be adjusted to account for the matching
	 * ID).
	 * </p>
	 */
	public void setInterceptors(IServerInterceptor... theInterceptor) {
		setInterceptors(new ArrayList<IServerInterceptor>());
		if (theInterceptor != null && theInterceptor.length != 0) {
			getInterceptors().addAll(Arrays.asList(theInterceptor));
		}
	}

	/**
	 * This may be used to optionally register server interceptors directly against the DAOs.
	 * <p>
	 * Registering server action interceptors against the JPA DAOs can be more powerful than registering them against the
	 * {@link RestfulServer}, since the DAOs are able to break transactions into individual actions, and will account for
	 * match URLs (e.g. if a request contains an If-None-Match URL, the ID will be adjusted to account for the matching
	 * ID).
	 * </p>
	 */
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
	 * @param theTreatBaseUrlsAsLocal The set of base URLs. May be <code>null</code>, which
	 * means no references will be treated as external
	 */
	public void setTreatBaseUrlsAsLocal(Set<String> theTreatBaseUrlsAsLocal) {
		HashSet<String> treatBaseUrlsAsLocal = new HashSet<String>();
		for (String next : ObjectUtils.defaultIfNull(theTreatBaseUrlsAsLocal, new HashSet<String>())) {
			while (next.endsWith("/")) {
				next = next.substring(0, next.length() - 1);
			}
			treatBaseUrlsAsLocal.add(next);
		}
		myTreatBaseUrlsAsLocal = treatBaseUrlsAsLocal;
	}

}
