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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.ISort;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.method.BaseOutcomeReturningMethodBinding;
import ca.uhn.fhir.rest.method.ConformanceMethodBinding;
import ca.uhn.fhir.rest.method.CreateMethodBinding;
import ca.uhn.fhir.rest.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.method.HistoryMethodBinding;
import ca.uhn.fhir.rest.method.HttpDeleteClientInvocation;
import ca.uhn.fhir.rest.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.TransactionMethodBinding;
import ca.uhn.fhir.rest.method.UpdateMethodBinding;
import ca.uhn.fhir.rest.method.ValidateMethodBinding;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class GenericClient extends BaseClient implements IGenericClient {

	private FhirContext myContext;

	private HttpRequestBase myLastRequest;

	private boolean myLogRequestAndResponse;

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public GenericClient(FhirContext theContext, HttpClient theHttpClient, String theServerBase) {
		super(theHttpClient, theServerBase);
		myContext = theContext;
	}

	@Override
	public Conformance conformance() {
		HttpGetClientInvocation invocation = ConformanceMethodBinding.createConformanceInvocation();
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		ResourceResponseHandler<Conformance> binding = new ResourceResponseHandler<Conformance>(Conformance.class);
		Conformance resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome create(IResource theResource) {
		BaseHttpClientInvocation invocation = CreateMethodBinding.createCreateInvocation(theResource, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);

		MethodOutcome resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;

	}

	@Override
	public MethodOutcome delete(final Class<? extends IResource> theType, IdDt theId) {
		HttpDeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(toResourceName(theType), theId);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		final String resourceName = myContext.getResourceDefinition(theType).getName();
		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(binding, invocation, myLogRequestAndResponse);
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
	public <T extends IResource> Bundle history(final Class<T> theType, IdDt theIdDt, DateTimeDt theSince, Integer theLimit) {
		String resourceName = theType != null ? toResourceName(theType) : null;
		IdDt id = theIdDt != null && theIdDt.isEmpty() == false ? theIdDt : null;
		HttpGetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(resourceName, id, theSince, theLimit);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		BundleResponseHandler binding = new BundleResponseHandler(theType);
		Bundle resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;

	}

	@Override
	public <T extends IResource> Bundle history(Class<T> theType, String theId, DateTimeDt theSince, Integer theLimit) {
		return history(theType, new IdDt(theId), theSince, theLimit);
	}

	public boolean isLogRequestAndResponse() {
		return myLogRequestAndResponse;
	}

	@Override
	public <T extends IResource> T read(final Class<T> theType, IdDt theId) {
		HttpGetClientInvocation invocation = ReadMethodBinding.createReadInvocation(theId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theType);
		T resp = invokeClient(binding, invocation, myLogRequestAndResponse);
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
			String qualifier = null;
			for (IQueryParameterType nextValue : nextEntry.getValue()) {
				valueList.add(nextValue.getValueAsQueryToken());
				qualifier = nextValue.getQueryParameterQualifier();
			}
			qualifier = StringUtils.defaultString(qualifier);
			params.put(nextEntry.getKey() + qualifier, valueList);
		}

		HttpGetClientInvocation invocation = SearchMethodBinding.createSearchInvocation(toResourceName(theType), params);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		BundleResponseHandler binding = new BundleResponseHandler(theType);
		Bundle resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastRequest(HttpRequestBase theLastRequest) {
		myLastRequest = theLastRequest;
	}

	@Override
	public void setLogRequestAndResponse(boolean theLogRequestAndResponse) {
		myLogRequestAndResponse = theLogRequestAndResponse;
	}

	private String toResourceName(Class<? extends IResource> theType) {
		return myContext.getResourceDefinition(theType).getName();
	}

	@Override
	public List<IResource> transaction(List<IResource> theResources) {
		BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(theResources, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		Bundle resp = invokeClient(new BundleResponseHandler(null), invocation, myLogRequestAndResponse);

		return resp.toListOfResources();
	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IResource theResource) {
		BaseHttpClientInvocation invocation = UpdateMethodBinding.createUpdateInvocation(theResource, theIdDt, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome update(String theId, IResource theResource) {
		return update(new IdDt(theId), theResource);
	}

	@Override
	public MethodOutcome validate(IResource theResource) {
		BaseHttpClientInvocation invocation = ValidateMethodBinding.createValidateInvocation(theResource, null, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IResource> T vread(final Class<T> theType, IdDt theId, IdDt theVersionId) {
		HttpGetClientInvocation invocation = ReadMethodBinding.createVReadInvocation(theId, theVersionId, toResourceName(theType));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theType);
		T resp = invokeClient(binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IResource> T vread(Class<T> theType, String theId, String theVersionId) {
		return vread(theType, new IdDt(theId), new IdDt(theVersionId));
	}

	private final class BundleResponseHandler implements IClientResponseHandler<Bundle> {

		private Class<? extends IResource> myType;

		public BundleResponseHandler(Class<? extends IResource> theType) {
			myType = theType;
		}

		@Override
		public Bundle invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			return parser.parseBundle(myType, theResponseReader);
		}
	}

	private class ForInternal implements IQuery {

		private List<ICriterionInternal> myCriterion = new ArrayList<ICriterionInternal>();
		private List<Include> myInclude = new ArrayList<Include>();
		private EncodingEnum myParamEncoding;
		private Integer myParamLimit;
		private boolean myQueryLogRequestAndResponse;
		private final String myResourceName;
		private final Class<? extends IResource> myResourceType;
		private List<SortInternal> mySort = new ArrayList<SortInternal>();

		public ForInternal(Class<? extends IResource> theResourceType) {
			myResourceType = theResourceType;
			myResourceName = myContext.getResourceDefinition(theResourceType).getName();
		}

		public ForInternal(String theResourceName) {
			myResourceType = myContext.getResourceDefinition(theResourceName).getImplementingClass();
			myResourceName = theResourceName;
		}

		public ForInternal() {
			myResourceType = null;
			myResourceName = null;
		}

		private void addParam(Map<String, List<String>> params, String parameterName, String parameterValue) {
			if (!params.containsKey(parameterName)) {
				params.put(parameterName, new ArrayList<String>());
			}
			params.get(parameterName).add(parameterValue);
		}

		@Override
		public IQuery and(ICriterion theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IQuery andLogRequestAndResponse(boolean theLogRequestAndResponse) {
			myQueryLogRequestAndResponse = theLogRequestAndResponse;
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
			if (myResourceType != null) {
				b.append('/');
				b.append(myResourceType);
			}
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

			HttpGetClientInvocation invocation = new HttpGetClientInvocation(params, myResourceName);
			if (isKeepResponses()) {
				myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
			}

			BundleResponseHandler binding = new BundleResponseHandler(myResourceType);

			Bundle resp = invokeClient(binding, invocation, myQueryLogRequestAndResponse || myLogRequestAndResponse);
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
		public IQuery prettyPrint() {
			setPrettyPrint(true);
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

	}

	private final class OutcomeResponseHandler implements IClientResponseHandler<MethodOutcome> {
		private final String myResourceName;

		private OutcomeResponseHandler(String theResourceName) {
			myResourceName = theResourceName;
		}

		@Override
		public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
			MethodOutcome response = BaseOutcomeReturningMethodBinding.process2xxResponse(myContext, myResourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			return response;
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

		@Override
		public IQuery forAllResources() {
			return new ForInternal();
		}

	}

	private final class ResourceResponseHandler<T extends IResource> implements IClientResponseHandler<T> {

		private Class<T> myType;

		public ResourceResponseHandler(Class<T> theType) {
			myType = theType;
		}

		@Override
		public T invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			IParser parser = respType.newParser(myContext);
			return parser.parseResource(myType, theResponseReader);
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
