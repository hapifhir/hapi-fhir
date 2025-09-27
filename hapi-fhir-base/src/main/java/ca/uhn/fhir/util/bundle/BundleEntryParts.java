/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class BundleEntryParts {
	protected RequestTypeEnum myRequestType;
	protected IBaseResource myResource;
	protected String myUrl;
	protected String myConditionalUrl;
	protected String myFullUrl;
	protected RequestTypeEnum myMethod;

	/**
	 * Constructor
	 */
	public BundleEntryParts(
			String theFullUrl,
			RequestTypeEnum theRequestType,
			String theUrl,
			IBaseResource theResource,
			String theConditionalUrl,
			RequestTypeEnum theMethod) {
		super();
		myFullUrl = theFullUrl;
		myRequestType = theRequestType;
		myUrl = theUrl;
		myResource = theResource;
		myConditionalUrl = theConditionalUrl;
		myMethod = theMethod;
	}

	/**
	 * Copy Constructor
	 */
	public BundleEntryParts(BundleEntryParts theBundleEntryParts) {
		this(
				theBundleEntryParts.getFullUrl(),
				theBundleEntryParts.getRequestType(),
				theBundleEntryParts.getUrl(),
				theBundleEntryParts.getResource(),
				theBundleEntryParts.getConditionalUrl(),
				theBundleEntryParts.getMethod());
	}

	/**
	 * Returns the <code>Bundle.entry.fulUrl</code> value
	 */
	public String getFullUrl() {
		return myFullUrl;
	}

	public RequestTypeEnum getRequestType() {
		return myRequestType;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	/**
	 * Returns the conditional URL associated with this request, if any.
	 * <ul>
	 * <li>
	 * If the {@link #getMethod() method} is <code>PUT</code>, <code>PATCH</code>, or
	 * <code>DELETE</code>, and the {@link #getUrl() request URL}
	 * contains <code>'?'</code>, returns the {@link #getUrl() request URL}
	 * </li>
	 * <li>
	 * If the {@link #getMethod() method} is <code>POST</code>, and the
	 * <code>Bundle.entry.request.ifNoneExist</code>
	 * contains <code>'?'</code>, returns the <codd>ifNoneExist</codd>
	 * value.
	 * </li>
	 * </ul>
	 * Returns the <code>Bundle.entry.request.url</code> value
	 */
	public String getConditionalUrl() {
		return myConditionalUrl;
	}

	/**
	 * Returns the <code>Bundle.entry.request.url</code> value
	 */
	public String getUrl() {
		return myUrl;
	}

	public RequestTypeEnum getMethod() {
		return myMethod;
	}
}
