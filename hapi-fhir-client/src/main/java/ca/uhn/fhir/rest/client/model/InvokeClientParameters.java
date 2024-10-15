/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.client.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.method.IClientResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvokeClientParameters<T> {
	private FhirContext myContext;
	private IClientResponseHandler<T> myBinding;
	private BaseHttpClientInvocation myClientInvocation;
	private EncodingEnum myEncoding;
	private Boolean myPrettyPrint;
	private boolean theLogRequestAndResponse;
	private SummaryEnum mySummaryMode;
	private Set<String> mySubsetElements;
	private CacheControlDirective myCacheControlDirective;
	private String myCustomAcceptHeader;
	private Map<String, List<String>> myCustomHeaders;

	public FhirContext getContext() {
		return myContext;
	}

	public InvokeClientParameters<T> setContext(FhirContext theContext) {
		myContext = theContext;
		return this;
	}

	public IClientResponseHandler<T> getBinding() {
		return myBinding;
	}

	public InvokeClientParameters<T> setBinding(IClientResponseHandler<T> theBinding) {
		myBinding = theBinding;
		return this;
	}

	public BaseHttpClientInvocation getClientInvocation() {
		return myClientInvocation;
	}

	public InvokeClientParameters<T> setClientInvocation(BaseHttpClientInvocation theClientInvocation) {
		myClientInvocation = theClientInvocation;
		return this;
	}

	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	public InvokeClientParameters<T> setEncoding(EncodingEnum theEncoding) {
		myEncoding = theEncoding;
		return this;
	}

	public Boolean getPrettyPrint() {
		return myPrettyPrint;
	}

	public InvokeClientParameters<T> setPrettyPrint(Boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	public boolean isTheLogRequestAndResponse() {
		return theLogRequestAndResponse;
	}

	public InvokeClientParameters<T> setTheLogRequestAndResponse(boolean theTheLogRequestAndResponse) {
		theLogRequestAndResponse = theTheLogRequestAndResponse;
		return this;
	}

	public SummaryEnum getSummaryMode() {
		return mySummaryMode;
	}

	public InvokeClientParameters<T> setSummaryMode(SummaryEnum theSummaryMode) {
		mySummaryMode = theSummaryMode;
		return this;
	}

	public Set<String> getSubsetElements() {
		if (mySubsetElements == null) {
			mySubsetElements = new HashSet<>();
		}
		return mySubsetElements;
	}

	public InvokeClientParameters<T> setSubsetElements(Set<String> theSubsetElements) {
		mySubsetElements = theSubsetElements;
		return this;
	}

	public void addSubsetElements(String theEl) {
		getSubsetElements().add(theEl);
	}

	public CacheControlDirective getCacheControlDirective() {
		return myCacheControlDirective;
	}

	public InvokeClientParameters<T> setCacheControlDirective(CacheControlDirective theCacheControlDirective) {
		myCacheControlDirective = theCacheControlDirective;
		return this;
	}

	public String getCustomAcceptHeader() {
		return myCustomAcceptHeader;
	}

	public InvokeClientParameters<T> setCustomAcceptHeader(String theCustomAcceptHeader) {
		myCustomAcceptHeader = theCustomAcceptHeader;
		return this;
	}

	public Map<String, List<String>> getCustomHeaders() {
		if (myCustomHeaders == null) {
			myCustomHeaders = new HashMap<>();
		}
		return myCustomHeaders;
	}

	public InvokeClientParameters<T> setCustomHeaders(Map<String, List<String>> theCustomHeaders) {
		myCustomHeaders = theCustomHeaders;
		return this;
	}

	public void addCustomHeader(String theKey, String theValue) {
		Map<String, List<String>> headers = getCustomHeaders();
		if (!headers.containsKey(theKey)) {
			headers.put(theKey, new ArrayList<>());
		}
		headers.get(theKey).add(theValue);
	}
}
