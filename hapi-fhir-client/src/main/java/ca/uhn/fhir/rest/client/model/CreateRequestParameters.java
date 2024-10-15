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

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;

public class CreateRequestParameters {

	/**
	 * The complete FHIR url to which the http request will be sent
	 */
	private String myUrl;
	/**
	 * 	The encoding to use for any serialized content sent to the
	 * 	server
	 */
	private EncodingEnum myEncodingEnum;
	/**
	 * the type of HTTP request (GET, DELETE, ..)
	 */
	private RequestTypeEnum myRequestTypeEnum;

	private IHttpClient myClient;

	public String getUrl() {
		return myUrl;
	}

	public CreateRequestParameters setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public EncodingEnum getEncodingEnum() {
		return myEncodingEnum;
	}

	public CreateRequestParameters setEncodingEnum(EncodingEnum theEncodingEnum) {
		myEncodingEnum = theEncodingEnum;
		return this;
	}

	public RequestTypeEnum getRequestTypeEnum() {
		return myRequestTypeEnum;
	}

	public CreateRequestParameters setRequestTypeEnum(RequestTypeEnum theRequestTypeEnum) {
		myRequestTypeEnum = theRequestTypeEnum;
		return this;
	}

	public IHttpClient getClient() {
		return myClient;
	}

	public CreateRequestParameters setClient(IHttpClient theClient) {
		myClient = theClient;
		return this;
	}
}
