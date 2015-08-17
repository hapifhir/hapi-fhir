package ca.uhn.fhir.jpa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public class DaoConfig {

	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;
	private int myIncludeLimit = 2000;
	private List<IServerInterceptor> myInterceptors;
	private ResourceEncodingEnum myResourceEncoding = ResourceEncodingEnum.JSONC;

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
		return myInterceptors;
	}

	public ResourceEncodingEnum getResourceEncoding() {
		return myResourceEncoding;
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
		if (theInterceptor == null || theInterceptor.length==0){
			setInterceptors(new ArrayList<IServerInterceptor>());
		} else {
			setInterceptors(Arrays.asList(theInterceptor));
		}
	}

}
