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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.BaseOutcomeReturningMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.CreateMethodBinding;
import ca.uhn.fhir.rest.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.method.HistoryMethodBinding;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.UpdateMethodBinding;
import ca.uhn.fhir.rest.method.ValidateMethodBinding;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class GenericClient extends BaseClient implements IGenericClient {

	private FhirContext myContext;
	private HttpRequestBase myLastRequest;

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as
	 * this method may change!
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
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseResource(theType, theResponseReader);
			}
		};

		@SuppressWarnings("unchecked")
		T resp = (T) invokeClient(binding, invocation);
		return resp;
	}


	@Override
	public MethodOutcome delete(final Class<? extends IResource> theType, IdDt theId) {
		DeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(toResourceName(theType), theId);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		final String resourceName = myContext.getResourceDefinition(theType).getName();
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				MethodOutcome response = BaseOutcomeReturningMethodBinding.process2xxResponse(myContext, resourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
				return response;
			}
		};

		MethodOutcome resp =  (MethodOutcome) invokeClient(binding, invocation);
		return resp;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as
	 * this method may change!
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
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseResource(theType, theResponseReader);
			}
		};

		@SuppressWarnings("unchecked")
		T resp = (T) invokeClient(binding, invocation);
		return resp;
	}

	@Override
	public <T extends IResource> Bundle search(final Class<T> theType, Map<String, List<IQueryParameterType>> theParams) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		for (Entry<String, List<IQueryParameterType>> nextEntry : theParams.entrySet()) {
			ArrayList<String> valueList = new ArrayList<String>();
			params.put(nextEntry.getKey(), valueList);
			for (IQueryParameterType nextValue : nextEntry.getValue()) {
				valueList.add(nextValue.getValueAsQueryToken());
			}
		}

		GetClientInvocation invocation = SearchMethodBinding.createSearchInvocation(toResourceName(theType), params);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseBundle(theType, theResponseReader);
			}
		};

		Bundle resp = (Bundle) invokeClient(binding, invocation);
		return resp;
	}

	@Override
	public MethodOutcome create(IResource theResource) {
		BaseClientInvocation invocation = CreateMethodBinding.createCreateInvocation(theResource, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();
		
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				MethodOutcome response = BaseOutcomeReturningMethodBinding.process2xxResponse(myContext, resourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
				return response;
			}
		};

		MethodOutcome resp = (MethodOutcome) invokeClient(binding, invocation);
		return resp;

	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IResource theResource) {
		BaseClientInvocation invocation = UpdateMethodBinding.createUpdateInvocation(theResource, theIdDt, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();
		
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				MethodOutcome response = BaseOutcomeReturningMethodBinding.process2xxResponse(myContext, resourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
				return response;
			}
		};

		MethodOutcome resp = (MethodOutcome) invokeClient(binding, invocation);
		return resp;
	}

	@Override
	public MethodOutcome validate(IResource theResource) {
		BaseClientInvocation invocation = ValidateMethodBinding.createValidateInvocation(theResource, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();
		
		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				MethodOutcome response = BaseOutcomeReturningMethodBinding.process2xxResponse(myContext, resourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
				return response;
			}
		};

		MethodOutcome resp = (MethodOutcome) invokeClient(binding, invocation);
		return resp;
	}

	@Override
	public <T extends IResource> Bundle history(final Class<T> theType, IdDt theIdDt) {
		GetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(toResourceName(theType), theIdDt);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseBundle(theType, theResponseReader);
			}
		};

		Bundle resp = (Bundle) invokeClient(binding, invocation);
		return resp;

	}

	@Override
	public Conformance conformance() {
		GetClientInvocation invocation = ConformanceMethodBinding.createConformanceInvocation();
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams());
		}

		IClientResponseHandler binding = new IClientResponseHandler() {
			@Override
			public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
				EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
				IParser parser = respType.newParser(myContext);
				return parser.parseResource(Conformance.class, theResponseReader);
			}
		};

		Conformance resp = (Conformance) invokeClient(binding, invocation);
		return resp;
	}

}
