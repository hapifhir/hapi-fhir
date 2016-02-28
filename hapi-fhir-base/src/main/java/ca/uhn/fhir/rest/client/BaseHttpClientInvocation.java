package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.EncodingEnum;

public abstract class BaseHttpClientInvocation {

	private final FhirContext myContext;
	private final List<Header> myHeaders;

	public BaseHttpClientInvocation(FhirContext myContext) {
		this.myContext = myContext;
		this.myHeaders = new ArrayList<Header>();
	}

	public void addHeader(String theName, String theValue) {
		myHeaders.add(new Header(theName, theValue));
	}

	/**
	 * Create an HTTP request out of this client request
	 * 
	 * @param theUrlBase
	 *            The FHIR server base url (with a trailing "/")
	 * @param theExtraParams
	 *            Any extra request parameters the server wishes to add
	 * @param theEncoding
	 *            The encoding to use for any serialized content sent to the
	 *            server
	 */
	public abstract IHttpRequest asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint);

	/**
	 * Create an HTTP request for the given url, encoding and request-type
	 * 
	 * @param theUrl
	 *            The complete FHIR url to which the http request will be sent
	 * @param theEncoding
	 *            The encoding to use for any serialized content sent to the
	 *            server
	 * @param theRequestType
	 *            the type of HTTP request (GET, DELETE, ..) 
	 */	
	protected IHttpRequest createHttpRequest(String theUrl, EncodingEnum theEncoding, RequestTypeEnum theRequestType) {
		IHttpClient httpClient = getRestfulClientFactory().getHttpClient(new StringBuilder(theUrl), null, null, theRequestType, myHeaders);
		return httpClient.createGetRequest(getContext(), theEncoding);
	}

	/**
	 * Returns the FHIR context associated with this client
	 * @return the myContext
	 */
	public FhirContext getContext() {
		return myContext;
	}

	/**
	 * Returns the http headers to be sent with the request
	 */
	public List<Header> getHeaders() {
		return myHeaders;
	}

	/**
	 * Get the restfull client factory
	 */
	public IRestfulClientFactory getRestfulClientFactory() {
		return myContext.getRestfulClientFactory();
	}

	public static void appendExtraParamsWithQuestionMark(Map<String, List<String>> theExtraParams, StringBuilder theUrlBuilder, boolean theWithQuestionMark) {
		if (theExtraParams == null) {
			return;
		}
		boolean first = theWithQuestionMark;

		if (theExtraParams.isEmpty() == false) {
			for (Entry<String, List<String>> next : theExtraParams.entrySet()) {
				for (String nextValue : next.getValue()) {
					if (first) {
						theUrlBuilder.append('?');
						first = false;
					} else {
						theUrlBuilder.append('&');
					}
					try {
						theUrlBuilder.append(URLEncoder.encode(next.getKey(), "UTF-8"));
						theUrlBuilder.append('=');
						theUrlBuilder.append(URLEncoder.encode(nextValue, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new Error("UTF-8 not supported - This should not happen");
					}
				}
			}
		}
	}

}
