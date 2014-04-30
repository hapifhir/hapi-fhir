package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.server.EncodingUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class GenericClient extends BaseClient implements IGenericClient {

	private FhirContext myContext;
	private HttpRequestBase myLastRequest;

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public GenericClient(FhirContext theContext, HttpClient theHttpClient, String theServerBase) {
		super(theHttpClient, theServerBase);
		myContext = theContext;
	}

	public HttpRequestBase getLastRequest() {
		return myLastRequest;
	}

	@Override
	public <T extends IResource> T read(final Class<T> theType, IdDt theId) {
		GetClientInvocation invocation = ReadMethodBinding.createReadInvocation(theId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase());
		}
		
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
					BaseServerResponseException {
				EncodingUtil respType = EncodingUtil.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseResource(theType, theResponseReader);
			}
		};

		@SuppressWarnings("unchecked")
		T resp = (T) invokeClient(binding, invocation);
		return resp;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastRequest(HttpRequestBase theLastRequest) {
		myLastRequest = theLastRequest;
	}

	private String toResourceName(Class<? extends IResource> theType) {
		return myContext.getResourceDefinition(theType).getName();
	}

	
	@Override
	public <T extends IResource> T vread(final Class<T> theType, IdDt theId, IdDt theVersionId) {
		GetClientInvocation invocation = ReadMethodBinding.createVReadInvocation(theId, theVersionId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase());
		}
		
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
					BaseServerResponseException {
				EncodingUtil respType = EncodingUtil.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseResource(theType, theResponseReader);
			}
		};

		@SuppressWarnings("unchecked")
		T resp = (T) invokeClient(binding, invocation);
		return resp;
	}
}
