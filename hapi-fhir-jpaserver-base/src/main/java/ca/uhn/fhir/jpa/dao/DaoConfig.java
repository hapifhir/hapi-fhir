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
 * Copyright (C) 2014 - 2015 University Health Network
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

import ca.uhn.fhir.dao.BaseDaoConfig;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public class DaoConfig extends BaseDaoConfig {

	private List<IServerInterceptor> myInterceptors;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;
	private boolean mySchedulingDisabled;
	private boolean mySubscriptionEnabled;
	private long mySubscriptionPollDelay = 1000;
	private Long mySubscriptionPurgeInactiveAfterMillis;

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
