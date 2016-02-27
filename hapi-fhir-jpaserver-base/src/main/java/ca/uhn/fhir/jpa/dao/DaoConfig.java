package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	private boolean myAllowInlineMatchUrlReferences = false; 

	private boolean myAllowMultipleDelete; 
	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;
	private int myIncludeLimit = 2000;

	// ***
	// update setter javadoc if default changes
	// ***
	private boolean myIndexContainedResources = true;

	private List<IServerInterceptor> myInterceptors;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;
	private boolean mySchedulingDisabled;
	private boolean mySubscriptionEnabled;
	private long mySubscriptionPollDelay = 1000;
	private Long mySubscriptionPurgeInactiveAfterMillis;
	/**
	 * See {@link #setIncludeLimit(int)}
	 */
	public int getHardSearchLimit() {
		return myHardSearchLimit;
	}
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

	public void setHardSearchLimit(int theHardSearchLimit) {
		myHardSearchLimit = theHardSearchLimit;
	}

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

	public void setResourceEncoding(ResourceEncodingEnum theResourceEncoding) {
		myResourceEncoding = theResourceEncoding;
	}

	public void setSchedulingDisabled(boolean theSchedulingDisabled) {
		mySchedulingDisabled = theSchedulingDisabled;
	}

	/**
	 * Does this server support subscription? If set to true, the server will enable the subscription monitoring mode,
	 * which adds a bit of overhead. Note that if this is enabled, you must also include Spring task scanning to your XML
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

}
