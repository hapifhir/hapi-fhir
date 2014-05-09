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
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.ISort;
import ca.uhn.fhir.rest.gclient.Include;
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
import ca.uhn.fhir.rest.server.Constants;
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

	@Override
	public Conformance conformance() {
		GetClientInvocation invocation = ConformanceMethodBinding.createConformanceInvocation();
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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

	@Override
	public MethodOutcome create(IResource theResource) {
		BaseClientInvocation invocation = CreateMethodBinding.createCreateInvocation(theResource, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public MethodOutcome delete(final Class<? extends IResource> theType, IdDt theId) {
		DeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(toResourceName(theType), theId);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		final String resourceName = myContext.getResourceDefinition(theType).getName();
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
	public MethodOutcome delete(Class<? extends IResource> theType, String theId) {
		return delete(theType, new IdDt(theId));
	}

	public HttpRequestBase getLastRequest() {
		return myLastRequest;
	}

	@Override
	public <T extends IResource> Bundle history(final Class<T> theType, IdDt theIdDt) {
		GetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(toResourceName(theType), theIdDt);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public <T extends IResource> Bundle history(Class<T> theType, String theId) {
		return history(theType, new IdDt(theId));
	}

	@Override
	public <T extends IResource> T read(final Class<T> theType, IdDt theId) {
		GetClientInvocation invocation = ReadMethodBinding.createReadInvocation(theId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public <T extends IResource> T read(Class<T> theType, String theId) {
		return read(theType, new IdDt(theId));
	}

	@Override
	public IUntypedQuery search() {
		return new QueryInternal();
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
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as
	 * this method may change!
	 */
	public void setLastRequest(HttpRequestBase theLastRequest) {
		myLastRequest = theLastRequest;
	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IResource theResource) {
		BaseClientInvocation invocation = UpdateMethodBinding.createUpdateInvocation(theResource, theIdDt, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public MethodOutcome update(String theId, IResource theResource) {
		return update(new IdDt(theId), theResource);
	}

	@Override
	public MethodOutcome validate(IResource theResource) {
		BaseClientInvocation invocation = ValidateMethodBinding.createValidateInvocation(theResource, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public <T extends IResource> T vread(final Class<T> theType, IdDt theId, IdDt theVersionId) {
		GetClientInvocation invocation = ReadMethodBinding.createVReadInvocation(theId, theVersionId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public <T extends IResource> T vread(Class<T> theType, String theId, String theVersionId) {
		return vread(theType, new IdDt(theId), new IdDt(theVersionId));
	}

	private String toResourceName(Class<? extends IResource> theType) {
		return myContext.getResourceDefinition(theType).getName();
	}

	private class ForInternal implements IQuery {

		private List<ICriterionInternal> myCriterion = new ArrayList<ICriterionInternal>();
		private List<Include> myInclude = new ArrayList<Include>();
		private boolean myLogRequestAndResponse;
		private EncodingEnum myParamEncoding;
		private Integer myParamLimit;
		private String myResourceName;
		private Class<? extends IResource> myResourceType;
		private List<SortInternal> mySort = new ArrayList<SortInternal>();

		public ForInternal(Class<? extends IResource> theResourceType) {
			myResourceType = theResourceType;
			myResourceName = myContext.getResourceDefinition(theResourceType).getName();
		}

		public ForInternal(String theResourceName) {
			myResourceType = myContext.getResourceDefinition(theResourceName).getImplementingClass();
			myResourceName = theResourceName;
		}

		@Override
		public IQuery and(ICriterion theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IQuery andLogRequestAndResponse(boolean theLogRequestAndResponse) {
			myLogRequestAndResponse = theLogRequestAndResponse;
			return this;
		}

		@Override
		public IQuery encodedJson() {
			myParamEncoding = EncodingEnum.JSON;
			return this;
		}

		@Override
		public IQuery encodedXml() {
			myParamEncoding = EncodingEnum.XML;
			return null;
		}

		@Override
		public Bundle execute() {

			StringBuilder b = new StringBuilder();
			b.append(getServerBase());
			b.append('/');
			b.append(myResourceType);
			b.append('?');

			Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
			for (ICriterionInternal next : myCriterion) {
				String parameterName = next.getParameterName();
				String parameterValue = next.getParameterValue();
				addParam(params, parameterName, parameterValue);
			}

			for (Include next : myInclude) {
				addParam(params, Constants.PARAM_INCLUDE, next.getInclude());
			}

			for (SortInternal next : mySort) {
				addParam(params, next.getParamName(), next.getParamValue());
			}

			if (myParamEncoding != null) {
				addParam(params, Constants.PARAM_FORMAT, myParamEncoding.getFormatContentType());
			}

			if (myParamLimit != null) {
				addParam(params, Constants.PARAM_COUNT, Integer.toString(myParamLimit));
			}

			GetClientInvocation invocation = new GetClientInvocation(params, myResourceName);
			if (isKeepResponses()) {
				myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
			}

			IClientResponseHandler binding = new IClientResponseHandler() {
				@Override
				public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
					EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
					IParser parser = respType.newParser(myContext);
					return parser.parseBundle(myResourceType, theResponseReader);
				}
			};

			Bundle resp = (Bundle) invokeClient(binding, invocation, myLogRequestAndResponse);
			return resp;

		}

		@Override
		public IQuery include(Include theInclude) {
			myInclude.add(theInclude);
			return this;
		}

		@Override
		public IQuery limitTo(int theLimitTo) {
			if (theLimitTo > 0) {
				myParamLimit = theLimitTo;
			} else {
				myParamLimit = null;
			}
			return this;
		}

		@Override
		public ISort sort() {
			SortInternal retVal = new SortInternal(this);
			mySort.add(retVal);
			return retVal;
		}

		@Override
		public IQuery where(ICriterion theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		private void addParam(Map<String, List<String>> params, String parameterName, String parameterValue) {
			if (!params.containsKey(parameterName)) {
				params.put(parameterName, new ArrayList<String>());
			}
			params.get(parameterName).add(parameterValue);
		}

	}

	private class QueryInternal implements IUntypedQuery {

		@Override
		public IQuery forResource(Class<? extends IResource> theResourceType) {
			return new ForInternal(theResourceType);
		}

		@Override
		public IQuery forResource(String theResourceName) {
			return new ForInternal(theResourceName);
		}

	}

	private class SortInternal implements ISort {

		private ForInternal myFor;
		private String myParamName;
		private String myParamValue;

		public SortInternal(ForInternal theFor) {
			myFor = theFor;
		}

		@Override
		public IQuery ascending(IParam theParam) {
			myParamName = Constants.PARAM_SORT_ASC;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		@Override
		public IQuery defaultOrder(IParam theParam) {
			myParamName = Constants.PARAM_SORT;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		@Override
		public IQuery descending(IParam theParam) {
			myParamName = Constants.PARAM_SORT_DESC;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		public String getParamName() {
			return myParamName;
		}

		public String getParamValue() {
			return myParamValue;
		}

	}

}
