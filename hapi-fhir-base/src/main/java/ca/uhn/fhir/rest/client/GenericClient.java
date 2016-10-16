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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.IRuntimeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICreate;
import ca.uhn.fhir.rest.gclient.ICreateTyped;
import ca.uhn.fhir.rest.gclient.ICreateWithQuery;
import ca.uhn.fhir.rest.gclient.ICreateWithQueryTyped;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IDelete;
import ca.uhn.fhir.rest.gclient.IDeleteTyped;
import ca.uhn.fhir.rest.gclient.IDeleteWithQuery;
import ca.uhn.fhir.rest.gclient.IDeleteWithQueryTyped;
import ca.uhn.fhir.rest.gclient.IFetchConformanceTyped;
import ca.uhn.fhir.rest.gclient.IFetchConformanceUntyped;
import ca.uhn.fhir.rest.gclient.IGetPage;
import ca.uhn.fhir.rest.gclient.IGetPageTyped;
import ca.uhn.fhir.rest.gclient.IGetPageUntyped;
import ca.uhn.fhir.rest.gclient.IGetTags;
import ca.uhn.fhir.rest.gclient.IHistory;
import ca.uhn.fhir.rest.gclient.IHistoryTyped;
import ca.uhn.fhir.rest.gclient.IHistoryUntyped;
import ca.uhn.fhir.rest.gclient.IMeta;
import ca.uhn.fhir.rest.gclient.IMetaAddOrDeleteSourced;
import ca.uhn.fhir.rest.gclient.IMetaAddOrDeleteUnsourced;
import ca.uhn.fhir.rest.gclient.IMetaGetUnsourced;
import ca.uhn.fhir.rest.gclient.IOperation;
import ca.uhn.fhir.rest.gclient.IOperationUnnamed;
import ca.uhn.fhir.rest.gclient.IOperationUntyped;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.gclient.IPatch;
import ca.uhn.fhir.rest.gclient.IPatchExecutable;
import ca.uhn.fhir.rest.gclient.IPatchTyped;
import ca.uhn.fhir.rest.gclient.IPatchWithQuery;
import ca.uhn.fhir.rest.gclient.IPatchWithQueryTyped;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IRead;
import ca.uhn.fhir.rest.gclient.IReadExecutable;
import ca.uhn.fhir.rest.gclient.IReadIfNoneMatch;
import ca.uhn.fhir.rest.gclient.IReadTyped;
import ca.uhn.fhir.rest.gclient.ISort;
import ca.uhn.fhir.rest.gclient.ITransaction;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.IUpdate;
import ca.uhn.fhir.rest.gclient.IUpdateExecutable;
import ca.uhn.fhir.rest.gclient.IUpdateTyped;
import ca.uhn.fhir.rest.gclient.IUpdateWithQuery;
import ca.uhn.fhir.rest.gclient.IUpdateWithQueryTyped;
import ca.uhn.fhir.rest.gclient.IValidate;
import ca.uhn.fhir.rest.gclient.IValidateUntyped;
import ca.uhn.fhir.rest.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.method.HistoryMethodBinding;
import ca.uhn.fhir.rest.method.HttpDeleteClientInvocation;
import ca.uhn.fhir.rest.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.method.HttpSimpleGetClientInvocation;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.method.SortParameter;
import ca.uhn.fhir.rest.method.TransactionMethodBinding;
import ca.uhn.fhir.rest.method.ValidateMethodBindingDstu1;
import ca.uhn.fhir.rest.method.ValidateMethodBindingDstu2Plus;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.util.ICallable;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
public class GenericClient extends BaseClient implements IGenericClient {

	private static final String I18N_CANNOT_DETEMINE_RESOURCE_TYPE = "ca.uhn.fhir.rest.client.GenericClient.cannotDetermineResourceTypeFromUri";
	private static final String I18N_INCOMPLETE_URI_FOR_READ = "ca.uhn.fhir.rest.client.GenericClient.incompleteUriForRead";
	private static final String I18N_NO_VERSION_ID_FOR_VREAD = "ca.uhn.fhir.rest.client.GenericClient.noVersionIdForVread";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClient.class);
	private FhirContext myContext;
	private IHttpRequest myLastRequest;
	private boolean myLogRequestAndResponse;

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public GenericClient(FhirContext theContext, IHttpClient theHttpClient, String theServerBase, RestfulClientFactory theFactory) {
		super(theHttpClient, theServerBase, theFactory);
		myContext = theContext;
	}

	@Override
	public IBaseConformance conformance() {
		if (myContext.getVersion().getVersion().isRi()) {
			throw new IllegalArgumentException("Must call fetchConformance() instead of conformance() for RI/STU3+ structures");
		}

		HttpGetClientInvocation invocation = MethodUtil.createConformanceInvocation(getFhirContext());
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		@SuppressWarnings("unchecked")
		Class<BaseConformance> conformance = (Class<BaseConformance>) myContext.getResourceDefinition("Conformance").getImplementingClass();

		ResourceResponseHandler<? extends BaseConformance> binding = new ResourceResponseHandler<BaseConformance>(conformance);
		IBaseConformance resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public ICreate create() {
		return new CreateInternal();
	}

	@Override
	public MethodOutcome create(IBaseResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createCreateInvocation(theResource, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);

		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;

	}

	@Override
	public IDelete delete() {
		return new DeleteInternal();
	}

	@Override
	public MethodOutcome delete(final Class<? extends IBaseResource> theType, IdDt theId) {
		HttpDeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), theId.withResourceType(toResourceName(theType)));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		final String resourceName = myContext.getResourceDefinition(theType).getName();
		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome delete(Class<? extends IBaseResource> theType, String theId) {
		return delete(theType, new IdDt(theId));
	}

	private <T extends IBaseResource> T doReadOrVRead(final Class<T> theType, IIdType theId, boolean theVRead, ICallable<T> theNotModifiedHandler, String theIfVersionMatches, Boolean thePrettyPrint,
			SummaryEnum theSummary, EncodingEnum theEncoding, Set<String> theSubsetElements) {
		String resName = toResourceName(theType);
		IIdType id = theId;
		if (!id.hasBaseUrl()) {
			id = new IdDt(resName, id.getIdPart(), id.getVersionIdPart());
		}

		HttpGetClientInvocation invocation;
		if (id.hasBaseUrl()) {
			if (theVRead) {
				invocation = ReadMethodBinding.createAbsoluteVReadInvocation(getFhirContext(), id);
			} else {
				invocation = ReadMethodBinding.createAbsoluteReadInvocation(getFhirContext(), id);
			}
		} else {
			if (theVRead) {
				invocation = ReadMethodBinding.createVReadInvocation(getFhirContext(), id, resName);
			} else {
				invocation = ReadMethodBinding.createReadInvocation(getFhirContext(), id, resName);
			}
		}
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		if (theIfVersionMatches != null) {
			invocation.addHeader(Constants.HEADER_IF_NONE_MATCH, '"' + theIfVersionMatches + '"');
		}

		boolean allowHtmlResponse = (theSummary == SummaryEnum.TEXT) || (theSummary == null && getSummary() == SummaryEnum.TEXT);
		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theType, (Class<? extends IBaseResource>) null, id, allowHtmlResponse);

		if (theNotModifiedHandler == null) {
			return invokeClient(myContext, binding, invocation, theEncoding, thePrettyPrint, myLogRequestAndResponse, theSummary, theSubsetElements);
		} else {
			try {
				return invokeClient(myContext, binding, invocation, theEncoding, thePrettyPrint, myLogRequestAndResponse, theSummary, theSubsetElements);
			} catch (NotModifiedException e) {
				return theNotModifiedHandler.call();
			}
		}

	}

	@Override
	public IFetchConformanceUntyped fetchConformance() {
		return new FetchConformanceInternal();
	}

	// public IResource read(UriDt url) {
	// return read(inferResourceClass(url), url);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public <T extends IResource> T read(final Class<T> theType, UriDt url) {
	// return (T) invoke(theType, url, new ResourceResponseHandler<T>(theType));
	// }
	//
	// public Bundle search(UriDt url) {
	// return search(inferResourceClass(url), url);
	// }

	@Override
	public void forceConformanceCheck() {
		super.forceConformanceCheck();
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

	public IHttpRequest getLastRequest() {
		return myLastRequest;
	}

	protected String getPreferredId(IBaseResource theResource, String theId) {
		if (isNotBlank(theId)) {
			return theId;
		}
		return theResource.getIdElement().getIdPart();
	}

	@Override
	public IGetTags getTags() {
		return new GetTagsInternal();
	}

	@Override
	public IHistory history() {
		return new HistoryInternal();
	}

	@Override
	public <T extends IBaseResource> Bundle history(final Class<T> theType, IdDt theIdDt, DateTimeDt theSince, Integer theLimit) {
		String resourceName = theType != null ? toResourceName(theType) : null;
		String id = theIdDt != null && theIdDt.isEmpty() == false ? theIdDt.getValue() : null;
		HttpGetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(myContext, resourceName, id, theSince, theLimit);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		BundleResponseHandler binding = new BundleResponseHandler(theType);
		Bundle resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;

	}

	@Override
	public <T extends IBaseResource> Bundle history(Class<T> theType, String theId, DateTimeDt theSince, Integer theLimit) {
		return history(theType, new IdDt(theId), theSince, theLimit);
	}

	private Class<? extends IBaseResource> inferResourceClass(UriDt theUrl) {
		String urlString = theUrl.getValueAsString();
		int i = urlString.indexOf('?');

		if (i >= 0) {
			urlString = urlString.substring(0, i);
		}

		i = urlString.indexOf("://");

		if (i >= 0) {
			urlString = urlString.substring(i + 3);
		}

		String[] pcs = urlString.split("\\/");

		for (i = pcs.length - 1; i >= 0; i--) {
			String s = pcs[i].trim();

			if (!s.isEmpty()) {
				RuntimeResourceDefinition def = myContext.getResourceDefinition(s);
				if (def != null) {
					return def.getImplementingClass();
				}
			}
		}

		throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theUrl.getValueAsString()));

	}

	// @Override
	// public <T extends IBaseResource> T read(final Class<T> theType, IdDt theId) {
	// return doReadOrVRead(theType, theId, false, null, null);
	// }

	/**
	 * @deprecated Use {@link LoggingInterceptor} as a client interceptor registered to your
	 *             client instead, as this provides much more fine-grained control over what is logged. This
	 *             method will be removed at some point (deprecated in HAPI 1.6 - 2016-06-16)
	 */
	@Deprecated
	public boolean isLogRequestAndResponse() {
		return myLogRequestAndResponse;
	}

	@Override
	public IGetPage loadPage() {
		return new LoadPageInternal();
	}

	@Override
	public IMeta meta() {
		if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
			throw new IllegalStateException("Can not call $meta operations on a DSTU1 client");
		}
		return new MetaInternal();
	}

	@Override
	public IOperation operation() {
		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1) == false) {
			throw new IllegalStateException("Operations are only supported in FHIR DSTU2 and later. This client was created using a context configured for " + myContext.getVersion().getVersion().name());
		}
		return new OperationInternal();
	}

	@Override
	public IRead read() {
		return new ReadInternal();
	}

	@Override
	public <T extends IBaseResource> T read(Class<T> theType, String theId) {
		return read(theType, new IdDt(theId));
	}

	@Override
	public <T extends IBaseResource> T read(final Class<T> theType, UriDt theUrl) {
		IdDt id = theUrl instanceof IdDt ? ((IdDt) theUrl) : new IdDt(theUrl);
		return doReadOrVRead(theType, id, false, null, null, false, null, null, null);
	}

	@Override
	public IBaseResource read(UriDt theUrl) {
		IdDt id = new IdDt(theUrl);
		String resourceType = id.getResourceType();
		if (isBlank(resourceType)) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_INCOMPLETE_URI_FOR_READ, theUrl.getValueAsString()));
		}
		RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);
		if (def == null) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theUrl.getValueAsString()));
		}
		return read(def.getImplementingClass(), id);
	}

	@Override
	public IUntypedQuery search() {
		return new SearchInternal();
	}

	@Override
	public <T extends IBaseResource> Bundle search(final Class<T> theType, Map<String, List<IQueryParameterType>> theParams) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		for (Entry<String, List<IQueryParameterType>> nextEntry : theParams.entrySet()) {
			ArrayList<String> valueList = new ArrayList<String>();
			String qualifier = null;
			for (IQueryParameterType nextValue : nextEntry.getValue()) {
				valueList.add(nextValue.getValueAsQueryToken(myContext));
				qualifier = nextValue.getQueryParameterQualifier();
			}
			qualifier = StringUtils.defaultString(qualifier);
			params.put(nextEntry.getKey() + qualifier, valueList);
		}

		BaseHttpClientInvocation invocation = SearchMethodBinding.createSearchInvocation(myContext, toResourceName(theType), params, null, null, null);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		BundleResponseHandler binding = new BundleResponseHandler(theType);
		Bundle resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IBaseResource> Bundle search(final Class<T> theType, UriDt theUrl) {
		BaseHttpClientInvocation invocation = new HttpGetClientInvocation(getFhirContext(), theUrl.getValueAsString());
		return invokeClient(myContext, new BundleResponseHandler(theType), invocation);
	}

	@Override
	public Bundle search(UriDt theUrl) {
		return search(inferResourceClass(theUrl), theUrl);
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastRequest(IHttpRequest theLastRequest) {
		myLastRequest = theLastRequest;
	}

	@Override
	public void setLogRequestAndResponse(boolean theLogRequestAndResponse) {
		myLogRequestAndResponse = theLogRequestAndResponse;
	}

	private String toResourceName(Class<? extends IBaseResource> theType) {
		return myContext.getResourceDefinition(theType).getName();
	}

	@Override
	public ITransaction transaction() {
		return new TransactionInternal();
	}

	@Override
	public List<IBaseResource> transaction(List<IBaseResource> theResources) {
		BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(theResources, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		Bundle resp = invokeClient(myContext, new BundleResponseHandler(null), invocation, myLogRequestAndResponse);

		return new ArrayList<IBaseResource>(resp.toListOfResources());
	}

	@Override
	public IPatch patch() {
		return new PatchInternal();
	}

	@Override
	public MethodOutcome patch(IdDt theIdDt, IBaseResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createUpdateInvocation(theResource, null, theIdDt, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome patch(String theId, IBaseResource theResource) {
		return update(new IdDt(theId), theResource);
	}

	@Override
	public IUpdate update() {
		return new UpdateInternal();
	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IBaseResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createUpdateInvocation(theResource, null, theIdDt, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome update(String theId, IBaseResource theResource) {
		return update(new IdDt(theId), theResource);
	}

	@Override
	public IValidate validate() {
		return new ValidateInternal();
	}

	@Override
	public MethodOutcome validate(IBaseResource theResource) {
		BaseHttpClientInvocation invocation;
		if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
			invocation = ValidateMethodBindingDstu1.createValidateInvocation(theResource, null, myContext);
		} else {
			invocation = ValidateMethodBindingDstu2Plus.createValidateInvocation(myContext, theResource);
		}

		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding(), isPrettyPrint());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IBaseResource> T vread(final Class<T> theType, IdDt theId) {
		if (theId.hasVersionIdPart() == false) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_NO_VERSION_ID_FOR_VREAD, theId.getValue()));
		}
		return doReadOrVRead(theType, theId, true, null, null, false, null, null, null);
	}

	/* also deprecated in interface */
	@Deprecated
	@Override
	public <T extends IBaseResource> T vread(final Class<T> theType, IdDt theId, IdDt theVersionId) {
		return vread(theType, theId.withVersion(theVersionId.getIdPart()));
	}

	@Override
	public <T extends IBaseResource> T vread(Class<T> theType, String theId, String theVersionId) {
		IdDt resId = new IdDt(toResourceName(theType), theId, theVersionId);
		return vread(theType, resId);
	}

	private static void addParam(Map<String, List<String>> params, String parameterName, String parameterValue) {
		if (!params.containsKey(parameterName)) {
			params.put(parameterName, new ArrayList<String>());
		}
		params.get(parameterName).add(parameterValue);
	}

	private static void addPreferHeader(PreferReturnEnum thePrefer, BaseHttpClientInvocation theInvocation) {
		if (thePrefer != null) {
			theInvocation.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + '=' + thePrefer.getHeaderValue());
		}
	}

	private static String validateAndEscapeConditionalUrl(String theSearchUrl) {
		Validate.notBlank(theSearchUrl, "Conditional URL can not be blank/null");
		StringBuilder b = new StringBuilder();
		boolean haveHadQuestionMark = false;
		for (int i = 0; i < theSearchUrl.length(); i++) {
			char nextChar = theSearchUrl.charAt(i);
			if (!haveHadQuestionMark) {
				if (nextChar == '?') {
					haveHadQuestionMark = true;
				} else if (!Character.isLetter(nextChar)) {
					throw new IllegalArgumentException("Conditional URL must be in the format \"[ResourceType]?[Params]\" and must not have a base URL - Found: " + theSearchUrl);
				}
				b.append(nextChar);
			} else {
				switch (nextChar) {
				case '|':
				case '?':
				case '$':
				case ':':
					b.append(UrlUtil.escape(Character.toString(nextChar)));
					break;
				default:
					b.append(nextChar);
					break;
				}
			}
		}
		return b.toString();
	}

	private abstract class BaseClientExecutable<T extends IClientExecutable<?, ?>, Y> implements IClientExecutable<T, Y> {

		protected EncodingEnum myParamEncoding;

		private List<Class<? extends IBaseResource>> myPreferResponseTypes;

		protected Boolean myPrettyPrint;

		private boolean myQueryLogRequestAndResponse;

		private HashSet<String> mySubsetElements;

		protected SummaryEnum mySummaryMode;

		@SuppressWarnings("unchecked")
		@Override
		public T andLogRequestAndResponse(boolean theLogRequestAndResponse) {
			myQueryLogRequestAndResponse = theLogRequestAndResponse;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T elementsSubset(String... theElements) {
			if (theElements != null && theElements.length > 0) {
				mySubsetElements = new HashSet<String>(Arrays.asList(theElements));
			} else {
				mySubsetElements = null;
			}
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T encodedJson() {
			myParamEncoding = EncodingEnum.JSON;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T encodedXml() {
			myParamEncoding = EncodingEnum.XML;
			return (T) this;
		}

		protected EncodingEnum getParamEncoding() {
			return myParamEncoding;
		}

		public List<Class<? extends IBaseResource>> getPreferResponseTypes() {
			return myPreferResponseTypes;
		}

		public List<Class<? extends IBaseResource>> getPreferResponseTypes(Class<? extends IBaseResource> theDefault) {
			if (myPreferResponseTypes != null) {
				return myPreferResponseTypes;
			} else {
				return toTypeList(theDefault);
			}
		}

		protected HashSet<String> getSubsetElements() {
			return mySubsetElements;
		}

		protected <Z> Z invoke(Map<String, List<String>> theParams, IClientResponseHandler<Z> theHandler, BaseHttpClientInvocation theInvocation) {
			// if (myParamEncoding != null) {
			// theParams.put(Constants.PARAM_FORMAT, Collections.singletonList(myParamEncoding.getFormatContentType()));
			// }
			//
			// if (myPrettyPrint != null) {
			// theParams.put(Constants.PARAM_PRETTY, Collections.singletonList(myPrettyPrint.toString()));
			// }

			if (isKeepResponses()) {
				myLastRequest = theInvocation.asHttpRequest(getServerBase(), theParams, getEncoding(), myPrettyPrint);
			}

			Z resp = invokeClient(myContext, theHandler, theInvocation, myParamEncoding, myPrettyPrint, myQueryLogRequestAndResponse || myLogRequestAndResponse, mySummaryMode, mySubsetElements);
			return resp;
		}

		protected IBaseResource parseResourceBody(String theResourceBody) {
			EncodingEnum encoding = MethodUtil.detectEncodingNoDefault(theResourceBody);
			if (encoding == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
			}
			return encoding.newParser(myContext).parseResource(theResourceBody);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T preferResponseType(Class<? extends IBaseResource> theClass) {
			myPreferResponseTypes = null;
			if (theClass != null) {
				myPreferResponseTypes = new ArrayList<Class<? extends IBaseResource>>();
				myPreferResponseTypes.add(theClass);
			}
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T preferResponseTypes(List<Class<? extends IBaseResource>> theClass) {
			myPreferResponseTypes = theClass;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T prettyPrint() {
			myPrettyPrint = true;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T summaryMode(SummaryEnum theSummary) {
			mySummaryMode = theSummary;
			return ((T) this);
		}

	}

	private final class BundleResponseHandler implements IClientResponseHandler<Bundle> {

		private Class<? extends IBaseResource> myType;

		public BundleResponseHandler(Class<? extends IBaseResource> theType) {
			myType = theType;
		}

		@Override
		public Bundle invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			return parser.parseBundle(myType, theResponseReader);
		}
	}

	private class CreateInternal extends BaseClientExecutable<ICreateTyped, MethodOutcome> implements ICreate, ICreateTyped, ICreateWithQuery, ICreateWithQueryTyped {

		private CriterionList myCriterionList;
		private String myId;
		private PreferReturnEnum myPrefer;
		private IBaseResource myResource;
		private String myResourceBody;
		private String mySearchUrl;

		@Override
		public ICreateWithQueryTyped and(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public ICreateWithQuery conditional() {
			myCriterionList = new CriterionList();
			return this;
		}

		@Override
		public ICreateTyped conditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public MethodOutcome execute() {
			if (myResource == null) {
				myResource = parseResourceBody(myResourceBody);
			}
			myId = getPreferredId(myResource, myId);

			// If an explicit encoding is chosen, we will re-serialize to ensure the right encoding
			if (getParamEncoding() != null) {
				myResourceBody = null;
			}

			BaseHttpClientInvocation invocation;
			if (mySearchUrl != null) {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myId, myContext, mySearchUrl);
			} else if (myCriterionList != null) {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myId, myContext, myCriterionList.toParamList());
			} else {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myId, myContext);
			}

			addPreferHeader(myPrefer, invocation);

			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResource);
			final String resourceName = def.getName();

			OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName, myPrefer);

			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);

		}

		@Override
		public ICreateTyped prefer(PreferReturnEnum theReturn) {
			myPrefer = theReturn;
			return this;
		}

		@Override
		public ICreateTyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "Resource can not be null");
			myResource = theResource;
			return this;
		}

		@Override
		public ICreateTyped resource(String theResourceBody) {
			Validate.notBlank(theResourceBody, "Body can not be null or blank");
			myResourceBody = theResourceBody;
			return this;
		}

		@Override
		public ICreateWithQueryTyped where(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public CreateInternal withId(IdDt theId) {
			myId = theId.getIdPart();
			return this;
		}

		@Override
		public CreateInternal withId(String theId) {
			myId = theId;
			return this;
		}

	}

	private class CriterionList extends ArrayList<ICriterionInternal> {

		private static final long serialVersionUID = 1L;

		public void populateParamList(Map<String, List<String>> theParams) {
			for (ICriterionInternal next : this) {
				String parameterName = next.getParameterName();
				String parameterValue = next.getParameterValue(myContext);
				if (isNotBlank(parameterValue)) {
					addParam(theParams, parameterName, parameterValue);
				}
			}
		}

		public Map<String, List<String>> toParamList() {
			LinkedHashMap<String, List<String>> retVal = new LinkedHashMap<String, List<String>>();
			populateParamList(retVal);
			return retVal;
		}

	}

	private class DeleteInternal extends BaseClientExecutable<IDeleteTyped, IBaseOperationOutcome> implements IDelete, IDeleteTyped, IDeleteWithQuery, IDeleteWithQueryTyped {

		private CriterionList myCriterionList;
		private IIdType myId;
		private String myResourceType;
		private String mySearchUrl;

		@Override
		public IDeleteWithQueryTyped and(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IBaseOperationOutcome execute() {
			HttpDeleteClientInvocation invocation;
			if (myId != null) {
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), myId);
			} else if (myCriterionList != null) {
				Map<String, List<String>> params = myCriterionList.toParamList();
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), myResourceType, params);
			} else {
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), mySearchUrl);
			}
			OperationOutcomeResponseHandler binding = new OperationOutcomeResponseHandler();
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);
		}

		@Override
		public IDeleteTyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "theResource can not be null");
			IIdType id = theResource.getIdElement();
			Validate.notNull(id, "theResource.getIdElement() can not be null");
			if (id.hasResourceType() == false || id.hasIdPart() == false) {
				throw new IllegalArgumentException("theResource.getId() must contain a resource type and logical ID at a minimum (e.g. Patient/1234), found: " + id.getValue());
			}
			myId = id;
			return this;
		}

		@Override
		public IDeleteTyped resourceById(IIdType theId) {
			Validate.notNull(theId, "theId can not be null");
			if (theId.hasResourceType() == false || theId.hasIdPart() == false) {
				throw new IllegalArgumentException("theId must contain a resource type and logical ID at a minimum (e.g. Patient/1234)found: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IDeleteTyped resourceById(String theResourceType, String theLogicalId) {
			Validate.notBlank(theResourceType, "theResourceType can not be blank/null");
			if (myContext.getResourceDefinition(theResourceType) == null) {
				throw new IllegalArgumentException("Unknown resource type");
			}
			Validate.notBlank(theLogicalId, "theLogicalId can not be blank/null");
			if (theLogicalId.contains("/")) {
				throw new IllegalArgumentException("LogicalId can not contain '/' (should only be the logical ID portion, not a qualified ID)");
			}
			myId = new IdDt(theResourceType, theLogicalId);
			return this;
		}

		@Override
		public IDeleteWithQuery resourceConditionalByType(Class<? extends IBaseResource> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType can not be null");
			myCriterionList = new CriterionList();
			myResourceType = myContext.getResourceDefinition(theResourceType).getName();
			return this;
		}

		@Override
		public IDeleteWithQuery resourceConditionalByType(String theResourceType) {
			Validate.notBlank(theResourceType, "theResourceType can not be blank/null");
			if (myContext.getResourceDefinition(theResourceType) == null) {
				throw new IllegalArgumentException("Unknown resource type: " + theResourceType);
			}
			myResourceType = theResourceType;
			myCriterionList = new CriterionList();
			return this;
		}

		@Override
		public IDeleteTyped resourceConditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public IDeleteWithQueryTyped where(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private class FetchConformanceInternal extends BaseClientExecutable implements IFetchConformanceUntyped, IFetchConformanceTyped {
		private RuntimeResourceDefinition myType;

		@Override
		public Object execute() {
			ResourceResponseHandler binding = new ResourceResponseHandler(myType.getImplementingClass());
			HttpGetClientInvocation invocation = MethodUtil.createConformanceInvocation(getFhirContext());
			return super.invoke(null, binding, invocation);
		}

		@Override
		public <T extends IBaseConformance> IFetchConformanceTyped<T> ofType(Class<T> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType must not be null");
			myType = myContext.getResourceDefinition(theResourceType);
			if (myType == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceType));
			}
			return this;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private class GetPageInternal extends BaseClientExecutable<IGetPageTyped<Object>, Object> implements IGetPageTyped<Object> {

		private Class<? extends IBaseBundle> myBundleType;
		private String myUrl;

		public GetPageInternal(String theUrl) {
			myUrl = theUrl;
		}

		public GetPageInternal(String theUrl, Class<? extends IBaseBundle> theBundleType) {
			myUrl = theUrl;
			myBundleType = theBundleType;
		}

		@Override
		public Object execute() {
			IClientResponseHandler binding;
			if (myBundleType == null) {
				binding = new BundleResponseHandler(null);
			} else {
				binding = new ResourceResponseHandler(myBundleType, getPreferResponseTypes());
			}
			HttpSimpleGetClientInvocation invocation = new HttpSimpleGetClientInvocation(myContext, myUrl);

			Map<String, List<String>> params = null;
			return invoke(params, binding, invocation);
		}

	}

	private class GetTagsInternal extends BaseClientExecutable<IGetTags, TagList> implements IGetTags {

		private String myId;
		private String myResourceName;
		private String myVersionId;

		@Override
		public TagList execute() {

			Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> initial = createExtraParams();
			if (initial != null) {
				params.putAll(initial);
			}

			TagListResponseHandler binding = new TagListResponseHandler();
			List<String> urlFragments = new ArrayList<String>();
			if (isNotBlank(myResourceName)) {
				urlFragments.add(myResourceName);
				if (isNotBlank(myId)) {
					urlFragments.add(myId);
					if (isNotBlank(myVersionId)) {
						urlFragments.add(Constants.PARAM_HISTORY);
						urlFragments.add(myVersionId);
					}
				}
			}
			urlFragments.add(Constants.PARAM_TAGS);

			HttpGetClientInvocation invocation = new HttpGetClientInvocation(myContext, params, urlFragments);

			return invoke(params, binding, invocation);

		}

		@Override
		public IGetTags forResource(Class<? extends IBaseResource> theClass) {
			setResourceClass(theClass);
			return this;
		}

		@Override
		public IGetTags forResource(Class<? extends IBaseResource> theClass, String theId) {
			setResourceClass(theClass);
			myId = theId;
			return this;
		}

		@Override
		public IGetTags forResource(Class<? extends IBaseResource> theClass, String theId, String theVersionId) {
			setResourceClass(theClass);
			myId = theId;
			myVersionId = theVersionId;
			return this;
		}

		private void setResourceClass(Class<? extends IBaseResource> theClass) {
			if (theClass != null) {
				myResourceName = myContext.getResourceDefinition(theClass).getName();
			} else {
				myResourceName = null;
			}
		}

	}

	@SuppressWarnings("rawtypes")
	private class HistoryInternal extends BaseClientExecutable implements IHistory, IHistoryUntyped, IHistoryTyped {

		private Integer myCount;
		private IIdType myId;
		private Class<? extends IBaseBundle> myReturnType;
		private IPrimitiveType mySince;
		private Class<? extends IBaseResource> myType;

		@SuppressWarnings("unchecked")
		@Override
		public IHistoryTyped andReturnBundle(Class theType) {
			myReturnType = theType;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public IHistoryTyped andReturnDstu1Bundle() {
			return this;
		}

		@Override
		public IHistoryTyped count(Integer theCount) {
			myCount = theCount;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object execute() {
			String resourceName;
			String id;
			if (myType != null) {
				resourceName = myContext.getResourceDefinition(myType).getName();
				id = null;
			} else if (myId != null) {
				resourceName = myId.getResourceType();
				id = myId.getIdPart();
			} else {
				resourceName = null;
				id = null;
			}

			HttpGetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(myContext, resourceName, id, mySince, myCount);

			IClientResponseHandler handler;
			if (myReturnType != null) {
				handler = new ResourceResponseHandler(myReturnType, getPreferResponseTypes(myType));
			} else {
				handler = new BundleResponseHandler(null);
			}

			return invoke(null, handler, invocation);
		}

		@Override
		public IHistoryUntyped onInstance(IIdType theId) {
			if (theId.hasResourceType() == false) {
				throw new IllegalArgumentException("Resource ID does not have a resource type: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IHistoryUntyped onServer() {
			return this;
		}

		@Override
		public IHistoryUntyped onType(Class<? extends IBaseResource> theResourceType) {
			myType = theResourceType;
			return this;
		}

		@Override
		public IHistoryTyped since(Date theCutoff) {
			if (theCutoff != null) {
				mySince = new InstantDt(theCutoff);
			} else {
				mySince = null;
			}
			return this;
		}

		@Override
		public IHistoryTyped since(IPrimitiveType theCutoff) {
			mySince = theCutoff;
			return this;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final class LoadPageInternal implements IGetPage, IGetPageUntyped {

		private static final String PREV = "prev";
		private static final String PREVIOUS = "previous";
		private String myPageUrl;

		@Override
		public <T extends IBaseBundle> IGetPageTyped andReturnBundle(Class<T> theBundleType) {
			Validate.notNull(theBundleType, "theBundleType must not be null");
			return new GetPageInternal(myPageUrl, theBundleType);
		}

		@Override
		public IGetPageTyped andReturnDstu1Bundle() {
			return new GetPageInternal(myPageUrl);
		}

		@Override
		public IGetPageUntyped byUrl(String thePageUrl) {
			if (isBlank(thePageUrl)) {
				throw new IllegalArgumentException("thePagingUrl must not be blank or null");
			}
			myPageUrl = thePageUrl;
			return this;
		}

		@Override
		public IGetPageTyped next(Bundle theBundle) {
			return new GetPageInternal(theBundle.getLinkNext().getValue());
		}

		@Override
		public <T extends IBaseBundle> IGetPageTyped<T> next(T theBundle) {
			return nextOrPrevious("next", theBundle);
		}

		private <T extends IBaseBundle> IGetPageTyped<T> nextOrPrevious(String theWantRel, T theBundle) {
			RuntimeResourceDefinition def = myContext.getResourceDefinition(theBundle);
			List<IBase> links = def.getChildByName("link").getAccessor().getValues(theBundle);
			if (links == null || links.isEmpty()) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(GenericClient.class, "noPagingLinkFoundInBundle", theWantRel));
			}
			for (IBase nextLink : links) {
				BaseRuntimeElementCompositeDefinition linkDef = (BaseRuntimeElementCompositeDefinition) myContext.getElementDefinition(nextLink.getClass());
				List<IBase> rel = linkDef.getChildByName("relation").getAccessor().getValues(nextLink);
				if (rel == null || rel.isEmpty()) {
					continue;
				}
				String relation = ((IPrimitiveType<?>) rel.get(0)).getValueAsString();
				if (theWantRel.equals(relation) || (theWantRel == PREVIOUS && PREV.equals(relation))) {
					List<IBase> urls = linkDef.getChildByName("url").getAccessor().getValues(nextLink);
					if (urls == null || urls.isEmpty()) {
						continue;
					}
					String url = ((IPrimitiveType<?>) urls.get(0)).getValueAsString();
					if (isBlank(url)) {
						continue;
					}
					return (IGetPageTyped<T>) byUrl(url).andReturnBundle(theBundle.getClass());
				}
			}
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(GenericClient.class, "noPagingLinkFoundInBundle", theWantRel));
		}

		@Override
		public IGetPageTyped previous(Bundle theBundle) {
			return new GetPageInternal(theBundle.getLinkPrevious().getValue());
		}

		@Override
		public <T extends IBaseBundle> IGetPageTyped<T> previous(T theBundle) {
			return nextOrPrevious(PREVIOUS, theBundle);
		}

		@Override
		public IGetPageTyped url(String thePageUrl) {
			return new GetPageInternal(thePageUrl);
		}

	}

	@SuppressWarnings("rawtypes")
	private class MetaInternal extends BaseClientExecutable implements IMeta, IMetaAddOrDeleteUnsourced, IMetaGetUnsourced, IMetaAddOrDeleteSourced {

		private IIdType myId;
		private IBaseMetaType myMeta;
		private Class<? extends IBaseMetaType> myMetaType;
		private String myOnType;
		private MetaOperation myOperation;

		@Override
		public IMetaAddOrDeleteUnsourced add() {
			myOperation = MetaOperation.ADD;
			return this;
		}

		@Override
		public IMetaAddOrDeleteUnsourced delete() {
			myOperation = MetaOperation.DELETE;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object execute() {

			BaseHttpClientInvocation invocation = null;

			IBaseParameters parameters = ParametersUtil.newInstance(myContext);
			switch (myOperation) {
			case ADD:
				ParametersUtil.addParameterToParameters(myContext, parameters, myMeta, "meta");
				invocation = OperationMethodBinding.createOperationInvocation(myContext, myId.getResourceType(), myId.getIdPart(), "$meta-add", parameters, false);
				break;
			case DELETE:
				ParametersUtil.addParameterToParameters(myContext, parameters, myMeta, "meta");
				invocation = OperationMethodBinding.createOperationInvocation(myContext, myId.getResourceType(), myId.getIdPart(), "$meta-delete", parameters, false);
				break;
			case GET:
				if (myId != null) {
					invocation = OperationMethodBinding.createOperationInvocation(myContext, myOnType, myId.getIdPart(), "$meta", parameters, true);
				} else if (myOnType != null) {
					invocation = OperationMethodBinding.createOperationInvocation(myContext, myOnType, null, "$meta", parameters, true);
				} else {
					invocation = OperationMethodBinding.createOperationInvocation(myContext, null, null, "$meta", parameters, true);
				}
				break;
			}

			// Should not happen
			if (invocation == null) {
				throw new IllegalStateException();
			}

			IClientResponseHandler handler;
			handler = new MetaParametersResponseHandler(myMetaType);
			return invoke(null, handler, invocation);
		}

		@Override
		public IClientExecutable fromResource(IIdType theId) {
			setIdInternal(theId);
			return this;
		}

		@Override
		public IClientExecutable fromServer() {
			return this;
		}

		@Override
		public IClientExecutable fromType(String theResourceName) {
			Validate.notBlank(theResourceName, "theResourceName must not be blank");
			myOnType = theResourceName;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseMetaType> IMetaGetUnsourced<T> get(Class<T> theType) {
			myMetaType = theType;
			myOperation = MetaOperation.GET;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseMetaType> IClientExecutable<IClientExecutable<?, ?>, T> meta(T theMeta) {
			Validate.notNull(theMeta, "theMeta must not be null");
			myMeta = theMeta;
			myMetaType = myMeta.getClass();
			return this;
		}

		@Override
		public IMetaAddOrDeleteSourced onResource(IIdType theId) {
			setIdInternal(theId);
			return this;
		}

		private void setIdInternal(IIdType theId) {
			Validate.notBlank(theId.getResourceType(), "theId must contain a resource type");
			Validate.notBlank(theId.getIdPart(), "theId must contain an ID part");
			myOnType = theId.getResourceType();
			myId = theId;
		}

	}

	private enum MetaOperation {
		ADD, DELETE, GET
	}

	private final class MetaParametersResponseHandler<T extends IBaseMetaType> implements IClientResponseHandler<T> {

		private Class<T> myType;

		public MetaParametersResponseHandler(Class<T> theMetaType) {
			myType = theMetaType;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			RuntimeResourceDefinition type = myContext.getResourceDefinition("Parameters");
			IBaseResource retVal = parser.parseResource(type.getImplementingClass(), theResponseReader);

			BaseRuntimeChildDefinition paramChild = type.getChildByName("parameter");
			BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");
			List<IBase> parameter = paramChild.getAccessor().getValues(retVal);
			if (parameter == null || parameter.isEmpty()) {
				return (T) myContext.getElementDefinition(myType).newInstance();
			}
			IBase param = parameter.get(0);

			List<IBase> meta = paramChildElem.getChildByName("value[x]").getAccessor().getValues(param);
			if (meta.isEmpty()) {
				return (T) myContext.getElementDefinition(myType).newInstance();
			}
			return (T) meta.get(0);

		}
	}

	@SuppressWarnings("rawtypes")
	private class OperationInternal extends BaseClientExecutable implements IOperation, IOperationUnnamed, IOperationUntyped, IOperationUntypedWithInput, IOperationUntypedWithInputAndPartialOutput {

		private IIdType myId;
		private String myOperationName;
		private IBaseParameters myParameters;
		private RuntimeResourceDefinition myParametersDef;
		private Class<? extends IBaseResource> myType;
		private boolean myUseHttpGet;
		private Class myReturnResourceType;

		@SuppressWarnings("unchecked")
		private void addParam(String theName, IBase theValue) {
			BaseRuntimeChildDefinition parameterChild = myParametersDef.getChildByName("parameter");
			BaseRuntimeElementCompositeDefinition<?> parameterElem = (BaseRuntimeElementCompositeDefinition<?>) parameterChild.getChildByName("parameter");

			IBase parameter = parameterElem.newInstance();
			parameterChild.getMutator().addValue(myParameters, parameter);

			IPrimitiveType<String> name = (IPrimitiveType<String>) myContext.getElementDefinition("string").newInstance();
			name.setValue(theName);
			parameterElem.getChildByName("name").getMutator().setValue(parameter, name);

			if (theValue instanceof IBaseDatatype) {
				BaseRuntimeElementDefinition<?> datatypeDef = myContext.getElementDefinition(theValue.getClass());
				if (datatypeDef instanceof IRuntimeDatatypeDefinition) {
					Class<? extends IBaseDatatype> profileOf = ((IRuntimeDatatypeDefinition) datatypeDef).getProfileOf();
					if (profileOf != null) {
						datatypeDef = myContext.getElementDefinition(profileOf);
					}
				}
				String childElementName = "value" + StringUtils.capitalize(datatypeDef.getName());
				BaseRuntimeChildDefinition childByName = parameterElem.getChildByName(childElementName);
				childByName.getMutator().setValue(parameter, theValue);
			} else if (theValue instanceof IBaseResource) {
				parameterElem.getChildByName("resource").getMutator().setValue(parameter, theValue);
			} else {
				throw new IllegalArgumentException("Don't know how to handle parameter of type " + theValue.getClass());
			}
		}

		private void addParam(String theName, IQueryParameterType theValue) {
			IPrimitiveType<?> stringType = ParametersUtil.createString(myContext, theValue.getValueAsQueryToken(myContext));
			addParam(theName, stringType);
		}

		@Override
		public IOperationUntypedWithInputAndPartialOutput andParameter(String theName, IBase theValue) {
			Validate.notEmpty(theName, "theName must not be null");
			Validate.notNull(theValue, "theValue must not be null");
			addParam(theName, theValue);
			return this;
		}

		@Override
		public IOperationUntypedWithInputAndPartialOutput andSearchParameter(String theName, IQueryParameterType theValue) {
			addParam(theName, theValue);

			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object execute() {
			String resourceName;
			String id;
			if (myType != null) {
				resourceName = myContext.getResourceDefinition(myType).getName();
				id = null;
			} else if (myId != null) {
				resourceName = myId.getResourceType();
				id = myId.getIdPart();
			} else {
				resourceName = null;
				id = null;
			}

			BaseHttpClientInvocation invocation = OperationMethodBinding.createOperationInvocation(myContext, resourceName, id, myOperationName, myParameters, myUseHttpGet);

			if (myReturnResourceType != null) {
				ResourceResponseHandler handler;
				handler = new ResourceResponseHandler(myReturnResourceType);
				Object retVal = invoke(null, handler, invocation);
				return retVal;
			} else {
				ResourceResponseHandler handler;
				handler = new ResourceResponseHandler();
				handler.setPreferResponseTypes(getPreferResponseTypes(myType));

				Object retVal = invoke(null, handler, invocation);
				if (myContext.getResourceDefinition((IBaseResource) retVal).getName().equals("Parameters")) {
					return retVal;
				} else {
					RuntimeResourceDefinition def = myContext.getResourceDefinition("Parameters");
					IBaseResource parameters = def.newInstance();

					BaseRuntimeChildDefinition paramChild = def.getChildByName("parameter");
					BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");
					IBase parameter = paramChildElem.newInstance();
					paramChild.getMutator().addValue(parameters, parameter);

					BaseRuntimeChildDefinition resourceElem = paramChildElem.getChildByName("resource");
					resourceElem.getMutator().addValue(parameter, (IBase) retVal);

					return parameters;
				}
			}
		}

		@Override
		public IOperationUntyped named(String theName) {
			Validate.notBlank(theName, "theName can not be null");
			myOperationName = theName;
			return this;
		}

		@Override
		public IOperationUnnamed onInstance(IIdType theId) {
			myId = theId;
			return this;
		}

		@Override
		public IOperationUnnamed onServer() {
			return this;
		}

		@Override
		public IOperationUnnamed onType(Class<? extends IBaseResource> theResourceType) {
			myType = theResourceType;
			return this;
		}

		@Override
		public IOperationUntypedWithInput useHttpGet() {
			myUseHttpGet = true;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseParameters> IOperationUntypedWithInput<T> withNoParameters(Class<T> theOutputParameterType) {
			Validate.notNull(theOutputParameterType, "theOutputParameterType may not be null");
			RuntimeResourceDefinition def = myContext.getResourceDefinition(theOutputParameterType);
			if (def == null) {
				throw new IllegalArgumentException("theOutputParameterType must refer to a HAPI FHIR Resource type: " + theOutputParameterType.getName());
			}
			if (!"Parameters".equals(def.getName())) {
				throw new IllegalArgumentException("theOutputParameterType must refer to a HAPI FHIR Resource type for a resource named " + "Parameters" + " - " + theOutputParameterType.getName()
						+ " is a resource named: " + def.getName());
			}
			myParameters = (IBaseParameters) def.newInstance();
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withParameter(Class<T> theParameterType, String theName, IBase theValue) {
			Validate.notNull(theParameterType, "theParameterType must not be null");
			Validate.notEmpty(theName, "theName must not be null");
			Validate.notNull(theValue, "theValue must not be null");

			myParametersDef = myContext.getResourceDefinition(theParameterType);
			myParameters = (IBaseParameters) myParametersDef.newInstance();

			addParam(theName, theValue);

			return this;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public IOperationUntypedWithInput withParameters(IBaseParameters theParameters) {
			Validate.notNull(theParameters, "theParameters can not be null");
			myParameters = theParameters;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withSearchParameter(Class<T> theParameterType, String theName, IQueryParameterType theValue) {
			Validate.notNull(theParameterType, "theParameterType must not be null");
			Validate.notEmpty(theName, "theName must not be null");
			Validate.notNull(theValue, "theValue must not be null");

			myParametersDef = myContext.getResourceDefinition(theParameterType);
			myParameters = (IBaseParameters) myParametersDef.newInstance();

			addParam(theName, theValue);

			return this;
		}

		@Override
		public IOperationUntypedWithInput returnResourceType(Class theReturnType) {
			Validate.notNull(theReturnType, "theReturnType must not be null");
			Validate.isTrue(IBaseResource.class.isAssignableFrom(theReturnType), "theReturnType must be a class which extends from IBaseResource");
			myReturnResourceType = theReturnType;
			return this;
		}

	}

	private final class OperationOutcomeResponseHandler implements IClientResponseHandler<IBaseOperationOutcome> {

		@Override
		public IBaseOperationOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders)
				throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				return null;
			}
			IParser parser = respType.newParser(myContext);
			IBaseOperationOutcome retVal;
			try {
				// TODO: handle if something else than OO comes back
				retVal = (IBaseOperationOutcome) parser.parseResource(theResponseReader);
			} catch (DataFormatException e) {
				ourLog.warn("Failed to parse OperationOutcome response", e);
				return null;
			}
			MethodUtil.parseClientRequestResourceHeaders(null, theHeaders, retVal);

			return retVal;
		}
	}

	private final class OutcomeResponseHandler implements IClientResponseHandler<MethodOutcome> {
		private PreferReturnEnum myPrefer;
		private final String myResourceName;

		private OutcomeResponseHandler(String theResourceName) {
			myResourceName = theResourceName;
		}

		private OutcomeResponseHandler(String theResourceName, PreferReturnEnum thePrefer) {
			this(theResourceName);
			myPrefer = thePrefer;
		}

		@Override
		public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			MethodOutcome response = MethodUtil.process2xxResponse(myContext, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			if (theResponseStatusCode == Constants.STATUS_HTTP_201_CREATED) {
				response.setCreated(true);
			}

			if (myPrefer == PreferReturnEnum.REPRESENTATION) {
				if (response.getResource() == null) {
					if (response.getId() != null && isNotBlank(response.getId().getValue()) && response.getId().hasBaseUrl()) {
						ourLog.info("Server did not return resource for Prefer-representation, going to fetch: {}", response.getId().getValue());
						IBaseResource resource = read().resource(response.getId().getResourceType()).withUrl(response.getId()).execute();
						response.setResource(resource);
					}
				}
			}

			return response;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private class ReadInternal extends BaseClientExecutable implements IRead, IReadTyped, IReadExecutable {
		private IIdType myId;
		private String myIfVersionMatches;
		private ICallable myNotModifiedHandler;
		private RuntimeResourceDefinition myType;

		@Override
		public Object execute() {// AAA
			if (myId.hasVersionIdPart()) {
				return doReadOrVRead(myType.getImplementingClass(), myId, true, myNotModifiedHandler, myIfVersionMatches, myPrettyPrint, mySummaryMode, myParamEncoding, getSubsetElements());
			} else {
				return doReadOrVRead(myType.getImplementingClass(), myId, false, myNotModifiedHandler, myIfVersionMatches, myPrettyPrint, mySummaryMode, myParamEncoding, getSubsetElements());
			}
		}

		@Override
		public IReadIfNoneMatch ifVersionMatches(String theVersion) {
			myIfVersionMatches = theVersion;
			return new IReadIfNoneMatch() {

				@Override
				public IReadExecutable returnNull() {
					myNotModifiedHandler = new ICallable() {
						@Override
						public Object call() {
							return null;
						}
					};
					return ReadInternal.this;
				}

				@Override
				public IReadExecutable returnResource(final IBaseResource theInstance) {
					myNotModifiedHandler = new ICallable() {
						@Override
						public Object call() {
							return theInstance;
						}
					};
					return ReadInternal.this;
				}

				@Override
				public IReadExecutable throwNotModifiedException() {
					myNotModifiedHandler = null;
					return ReadInternal.this;
				}
			};
		}

		private void processUrl() {
			String resourceType = myId.getResourceType();
			if (isBlank(resourceType)) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_INCOMPLETE_URI_FOR_READ, myId));
			}
			myType = myContext.getResourceDefinition(resourceType);
			if (myType == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, myId));
			}
		}

		@Override
		public <T extends IBaseResource> IReadTyped<T> resource(Class<T> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType must not be null");
			myType = myContext.getResourceDefinition(theResourceType);
			if (myType == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceType));
			}
			return this;
		}

		@Override
		public IReadTyped<IBaseResource> resource(String theResourceAsText) {
			Validate.notBlank(theResourceAsText, "You must supply a value for theResourceAsText");
			myType = myContext.getResourceDefinition(theResourceAsText);
			if (myType == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceAsText));
			}
			return this;
		}

		@Override
		public IReadExecutable withId(IIdType theId) {
			Validate.notNull(theId, "The ID can not be null");
			Validate.notBlank(theId.getIdPart(), "The ID can not be blank");
			myId = theId.toUnqualified();
			return this;
		}

		@Override
		public IReadExecutable withId(Long theId) {
			Validate.notNull(theId, "The ID can not be null");
			myId = new IdDt(myType.getName(), theId);
			return this;
		}

		@Override
		public IReadExecutable withId(String theId) {
			Validate.notBlank(theId, "The ID can not be blank");
			if (theId.indexOf('/') == -1) {
				myId = new IdDt(myType.getName(), theId);
			} else {
				myId = new IdDt(theId);
			}
			return this;
		}

		@Override
		public IReadExecutable withIdAndVersion(String theId, String theVersion) {
			Validate.notBlank(theId, "The ID can not be blank");
			myId = new IdDt(myType.getName(), theId, theVersion);
			return this;
		}

		@Override
		public IReadExecutable withUrl(IIdType theUrl) {
			Validate.notNull(theUrl, "theUrl can not be null");
			myId = theUrl;
			processUrl();
			return this;
		}

		@Override
		public IReadExecutable withUrl(String theUrl) {
			myId = new IdDt(theUrl);
			processUrl();
			return this;
		}

	}

	private final class ResourceListResponseHandler implements IClientResponseHandler<List<IBaseResource>> {

		private Class<? extends IBaseResource> myType;

		public ResourceListResponseHandler(Class<? extends IBaseResource> theType) {
			myType = theType;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<IBaseResource> invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders)
				throws BaseServerResponseException {
			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				Class<? extends IBaseResource> bundleType = myContext.getResourceDefinition("Bundle").getImplementingClass();
				ResourceResponseHandler<IBaseResource> handler = new ResourceResponseHandler<IBaseResource>((Class<IBaseResource>) bundleType);
				IBaseResource response = handler.invokeClient(theResponseMimeType, theResponseReader, theResponseStatusCode, theHeaders);
				IVersionSpecificBundleFactory bundleFactory = myContext.newBundleFactory();
				bundleFactory.initializeWithBundleResource(response);
				return bundleFactory.toListOfResources();
			} else {
				return new ArrayList<IBaseResource>(new BundleResponseHandler(myType).invokeClient(theResponseMimeType, theResponseReader, theResponseStatusCode, theHeaders).toListOfResources());
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private class SearchInternal extends BaseClientExecutable<IQuery<Object>, Object> implements IQuery<Object>, IUntypedQuery {

		private String myCompartmentName;
		private CriterionList myCriterion = new CriterionList();
		private List<Include> myInclude = new ArrayList<Include>();
		private DateRangeParam myLastUpdated;
		private Integer myParamLimit;
		private List<Collection<String>> myProfiles = new ArrayList<Collection<String>>();
		private String myResourceId;
		private String myResourceName;
		private Class<? extends IBaseResource> myResourceType;
		private Class<? extends IBaseBundle> myReturnBundleType;
		private List<Include> myRevInclude = new ArrayList<Include>();
		private SearchStyleEnum mySearchStyle;
		private String mySearchUrl;
		private List<TokenParam> mySecurity = new ArrayList<TokenParam>();
		private List<SortInternal> mySort = new ArrayList<SortInternal>();
		private List<TokenParam> myTags = new ArrayList<TokenParam>();

		public SearchInternal() {
			myResourceType = null;
			myResourceName = null;
			mySearchUrl = null;
		}

		@Override
		public IQuery and(ICriterion<?> theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IQuery byUrl(String theSearchUrl) {
			Validate.notBlank(theSearchUrl, "theSearchUrl must not be blank/null");

			if (theSearchUrl.startsWith("http://") || theSearchUrl.startsWith("https://")) {
				mySearchUrl = theSearchUrl;
				int qIndex = mySearchUrl.indexOf('?');
				if (qIndex != -1) {
					mySearchUrl = mySearchUrl.substring(0, qIndex) + validateAndEscapeConditionalUrl(mySearchUrl.substring(qIndex));
				}
			} else {
				String searchUrl = theSearchUrl;
				if (searchUrl.startsWith("/")) {
					searchUrl = searchUrl.substring(1);
				}
				if (!searchUrl.matches("[a-zA-Z]+($|\\?.*)")) {
					throw new IllegalArgumentException("Search URL must be either a complete URL starting with http: or https:, or a relative FHIR URL in the form [ResourceType]?[Params]");
				}
				int qIndex = searchUrl.indexOf('?');
				if (qIndex == -1) {
					mySearchUrl = getUrlBase() + '/' + searchUrl;
				} else {
					mySearchUrl = getUrlBase() + '/' + validateAndEscapeConditionalUrl(searchUrl);
				}
			}
			return this;
		}

		@Override
		public IQuery count(int theLimitTo) {
			if (theLimitTo > 0) {
				myParamLimit = theLimitTo;
			} else {
				myParamLimit = null;
			}
			return this;
		}

		@Override
		public IBase execute() {

			Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
			// Map<String, List<String>> initial = createExtraParams();
			// if (initial != null) {
			// params.putAll(initial);
			// }

			myCriterion.populateParamList(params);

			for (TokenParam next : myTags) {
				addParam(params, Constants.PARAM_TAG, next.getValueAsQueryToken(myContext));
			}

			for (TokenParam next : mySecurity) {
				addParam(params, Constants.PARAM_SECURITY, next.getValueAsQueryToken(myContext));
			}

			for (Collection<String> profileUris : myProfiles) {
				StringBuilder builder = new StringBuilder();
				for (Iterator<String> profileItr = profileUris.iterator(); profileItr.hasNext(); ) {
					builder.append(profileItr.next());
					if (profileItr.hasNext()) {
						builder.append(',');
					}
				}
				addParam(params, Constants.PARAM_PROFILE, builder.toString());
			}

			for (Include next : myInclude) {
				if (next.isRecurse()) {
					addParam(params, Constants.PARAM_INCLUDE_RECURSE, next.getValue());
				} else {
					addParam(params, Constants.PARAM_INCLUDE, next.getValue());
				}
			}

			for (Include next : myRevInclude) {
				addParam(params, Constants.PARAM_REVINCLUDE, next.getValue());
			}

			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
				SortSpec rootSs = null;
				SortSpec lastSs = null;
				for (SortInternal next : mySort) {
					SortSpec nextSortSpec = new SortSpec();
					nextSortSpec.setParamName(next.getParamValue());
					nextSortSpec.setOrder(next.getDirection());
					if (rootSs == null) {
						rootSs = nextSortSpec;
					} else {
						lastSs.setChain(nextSortSpec);
					}
					lastSs = nextSortSpec;
				}
				if (rootSs != null) {
					addParam(params, Constants.PARAM_SORT, SortParameter.createSortStringDstu3(rootSs));
				}
			} else {
				for (SortInternal next : mySort) {
					addParam(params, next.getParamName(), next.getParamValue());
				}
			}

			if (myParamLimit != null) {
				addParam(params, Constants.PARAM_COUNT, Integer.toString(myParamLimit));
			}

			if (myLastUpdated != null) {
				for (DateParam next : myLastUpdated.getValuesAsQueryTokens()) {
					addParam(params, Constants.PARAM_LASTUPDATED, next.getValueAsQueryToken(myContext));
				}
			}

			if (myReturnBundleType == null && myContext.getVersion().getVersion().isRi()) {
				throw new IllegalArgumentException("When using the client with HL7.org structures, you must specify "
						+ "the bundle return type for the client by adding \".returnBundle(org.hl7.fhir.instance.model.Bundle.class)\" to your search method call before the \".execute()\" method");
			}

			IClientResponseHandler<? extends IBase> binding;
			if (myReturnBundleType != null) {
				binding = new ResourceResponseHandler(myReturnBundleType, getPreferResponseTypes(myResourceType));
			} else {
				binding = new BundleResponseHandler(myResourceType);
			}

			IdDt resourceId = myResourceId != null ? new IdDt(myResourceId) : null;

			BaseHttpClientInvocation invocation;
			if (mySearchUrl != null) {
				invocation = SearchMethodBinding.createSearchInvocation(myContext, mySearchUrl, params);
			} else {
				invocation = SearchMethodBinding.createSearchInvocation(myContext, myResourceName, params, resourceId, myCompartmentName, mySearchStyle);
			}

			return invoke(params, binding, invocation);

		}

		@Override
		public IQuery forAllResources() {
			return this;
		}

		@Override
		public IQuery forResource(Class<? extends IBaseResource> theResourceType) {
			setType(theResourceType);
			return this;
		}

		@Override
		public IQuery forResource(String theResourceName) {
			setType(theResourceName);
			return this;
		}

		@Override
		public IQuery include(Include theInclude) {
			myInclude.add(theInclude);
			return this;
		}

		@Override
		public IQuery lastUpdated(DateRangeParam theLastUpdated) {
			myLastUpdated = theLastUpdated;
			return this;
		}

		@Override
		public IQuery limitTo(int theLimitTo) {
			return count(theLimitTo);
		}

		@Override
		public IQuery returnBundle(Class theClass) {
			if (theClass == null) {
				throw new NullPointerException("theClass must not be null");
			}
			myReturnBundleType = theClass;
			return this;
		}

		@Override
		public IQuery revInclude(Include theInclude) {
			myRevInclude.add(theInclude);
			return this;
		}

		private void setType(Class<? extends IBaseResource> theResourceType) {
			myResourceType = theResourceType;
			RuntimeResourceDefinition definition = myContext.getResourceDefinition(theResourceType);
			myResourceName = definition.getName();
		}

		private void setType(String theResourceName) {
			myResourceType = myContext.getResourceDefinition(theResourceName).getImplementingClass();
			myResourceName = theResourceName;
		}

		@Override
		public ISort sort() {
			SortInternal retVal = new SortInternal(this);
			mySort.add(retVal);
			return retVal;
		}

		@Override
		public IQuery usingStyle(SearchStyleEnum theStyle) {
			mySearchStyle = theStyle;
			return this;
		}

		@Override
		public IQuery where(ICriterion<?> theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IQuery withIdAndCompartment(String theResourceId, String theCompartmentName) {
			myResourceId = theResourceId;
			myCompartmentName = theCompartmentName;
			return this;
		}

		@Override
		public IQuery<Object> withProfile(String theProfileUri) {
			Validate.notBlank(theProfileUri, "theProfileUri must not be null or empty");
			myProfiles.add(Collections.singletonList(theProfileUri));
			return this;
		}
		
		@Override
		public IQuery<Object> withAnyProfile(Collection<String> theProfileUris) {
			Validate.notEmpty(theProfileUris, "theProfileUris must not be null or empty");
			myProfiles.add(theProfileUris);
			return this;
		}

		@Override
		public IQuery<Object> withSecurity(String theSystem, String theCode) {
			Validate.notBlank(theCode, "theCode must not be null or empty");
			mySecurity.add(new TokenParam(theSystem, theCode));
			return this;
		}

		@Override
		public IQuery<Object> withTag(String theSystem, String theCode) {
			Validate.notBlank(theCode, "theCode must not be null or empty");
			myTags.add(new TokenParam(theSystem, theCode));
			return this;
		}

	}

	@SuppressWarnings("rawtypes")
	private static class SortInternal implements ISort {

		private SearchInternal myFor;
		private String myParamName;
		private String myParamValue;
		private SortOrderEnum myDirection;

		public SortInternal(SearchInternal theFor) {
			myFor = theFor;
		}

		@Override
		public IQuery ascending(IParam theParam) {
			myParamName = Constants.PARAM_SORT_ASC;
			myDirection = SortOrderEnum.ASC;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		@Override
		public IQuery ascending(String theParam) {
			myParamName = Constants.PARAM_SORT_ASC;
			myDirection = SortOrderEnum.ASC;
			myParamValue = theParam;
			return myFor;
		}

		@Override
		public IQuery defaultOrder(IParam theParam) {
			myParamName = Constants.PARAM_SORT;
			myDirection = null;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		@Override
		public IQuery descending(IParam theParam) {
			myParamName = Constants.PARAM_SORT_DESC;
			myDirection = SortOrderEnum.DESC;
			myParamValue = theParam.getParamName();
			return myFor;
		}

		@Override
		public IQuery descending(String theParam) {
			myParamName = Constants.PARAM_SORT_DESC;
			myDirection = SortOrderEnum.DESC;
			myParamValue = theParam;
			return myFor;
		}

		public SortOrderEnum getDirection() {
			return myDirection;
		}

		public String getParamName() {
			return myParamName;
		}

		public String getParamValue() {
			return myParamValue;
		}

	}

	private final class StringResponseHandler implements IClientResponseHandler<String> {

		@Override
		public String invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders)
				throws IOException, BaseServerResponseException {
			return IOUtils.toString(theResponseReader);
		}
	}

	private final class TagListResponseHandler implements IClientResponseHandler<TagList> {

		@Override
		public TagList invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			return parser.parseTagList(theResponseReader);
		}
	}

	private final class TransactionExecutable<T> extends BaseClientExecutable<ITransactionTyped<T>, T> implements ITransactionTyped<T> {

		private IBaseBundle myBaseBundle;
		private Bundle myBundle;
		private String myRawBundle;
		private EncodingEnum myRawBundleEncoding;
		private List<? extends IBaseResource> myResources;

		public TransactionExecutable(Bundle theResources) {
			myBundle = theResources;
		}

		public TransactionExecutable(IBaseBundle theBundle) {
			myBaseBundle = theBundle;
		}

		public TransactionExecutable(List<? extends IBaseResource> theResources) {
			myResources = theResources;
		}

		public TransactionExecutable(String theBundle) {
			myRawBundle = theBundle;
			myRawBundleEncoding = MethodUtil.detectEncodingNoDefault(myRawBundle);
			if (myRawBundleEncoding == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public T execute() {
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			if (myResources != null) {
				ResourceListResponseHandler binding = new ResourceListResponseHandler(null);
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myResources, myContext);
				return (T) invoke(params, binding, invocation);
			} else if (myBaseBundle != null) {
				ResourceResponseHandler binding = new ResourceResponseHandler(myBaseBundle.getClass(), getPreferResponseTypes());
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myBaseBundle, myContext);
				return (T) invoke(params, binding, invocation);
			} else if (myRawBundle != null) {
				StringResponseHandler binding = new StringResponseHandler();
				/*
				 * If the user has explicitly requested a given encoding, we may need to re-encode the raw string
				 */
				if (getParamEncoding() != null) {
					if (MethodUtil.detectEncodingNoDefault(myRawBundle) != getParamEncoding()) {
						IBaseResource parsed = parseResourceBody(myRawBundle);
						myRawBundle = getParamEncoding().newParser(getFhirContext()).encodeResourceToString(parsed);
					}
				}
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myRawBundle, myContext);
				return (T) invoke(params, binding, invocation);
			} else {
				BundleResponseHandler binding = new BundleResponseHandler(null);
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myBundle, myContext);
				return (T) invoke(params, binding, invocation);
			}
		}

	}

	private final class TransactionInternal implements ITransaction {

		@Override
		public ITransactionTyped<Bundle> withBundle(Bundle theBundle) {
			Validate.notNull(theBundle, "theBundle must not be null");
			return new TransactionExecutable<Bundle>(theBundle);
		}

		@Override
		public ITransactionTyped<String> withBundle(String theBundle) {
			Validate.notBlank(theBundle, "theBundle must not be null");
			return new TransactionExecutable<String>(theBundle);
		}

		@Override
		public <T extends IBaseBundle> ITransactionTyped<T> withBundle(T theBundle) {
			Validate.notNull(theBundle, "theBundle must not be null");
			return new TransactionExecutable<T>(theBundle);
		}

		@Override
		public ITransactionTyped<List<IBaseResource>> withResources(List<? extends IBaseResource> theResources) {
			Validate.notNull(theResources, "theResources must not be null");
			return new TransactionExecutable<List<IBaseResource>>(theResources);
		}

	}

	private class PatchInternal extends BaseClientExecutable<IPatchExecutable, MethodOutcome> implements IPatch, IPatchTyped, IPatchExecutable, IPatchWithQuery, IPatchWithQueryTyped {

		private CriterionList myCriterionList;
		private IIdType myId;
		private PreferReturnEnum myPrefer;
		private IBaseResource myResource;
		private String myResourceBody;
		private String mySearchUrl;
		private PatchTypeEnum myPatchType;
		private String myPatchBody;

		@Override
		public IPatchWithQueryTyped and(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IPatchWithQuery conditional() {
			myCriterionList = new CriterionList();
			return this;
		}

		// TODO: This is not longer used.. Deprecate it or just remove it?
		@Override
		public IPatchTyped conditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public MethodOutcome execute() {
			if (myResource == null) {
				myResource = parseResourceBody(myResourceBody);
			}

			// If an explicit encoding is chosen, we will re-serialize to ensure the right encoding
			if (getParamEncoding() != null) {
				myResourceBody = null;
			}

			if (myPatchType == null) {
				throw new InvalidRequestException("No patch type supplied, cannot invoke server");
			}
			if (myPatchBody == null) {
				throw new InvalidRequestException("No patch body supplied, cannot invoke server");
			}

			if (myId == null) {
				myId = myResource.getIdElement();
			}

			if (myId == null || myId.hasIdPart() == false) {
				throw new InvalidRequestException("No ID supplied for resource to update, can not invoke server");
			}
			BaseHttpClientInvocation invocation = MethodUtil.createPatchInvocation(myContext, myId, myPatchType, myPatchBody);

			addPreferHeader(myPrefer, invocation);

			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResource);
			final String resourceName = def.getName();

			OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName, myPrefer);

			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);

		}

		@Override
		public IPatchExecutable prefer(PreferReturnEnum theReturn) {
			myPrefer = theReturn;
			return this;
		}

		@Override
		public IPatchTyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "Resource can not be null");
			myResource = theResource;
			return this;
		}

		@Override
		public IPatchTyped resource(String theResourceBody) {
			Validate.notBlank(theResourceBody, "Body can not be null or blank");
			myResourceBody = theResourceBody;
			return this;
		}

		@Override
		public IPatchWithQueryTyped where(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IPatchExecutable withId(IIdType theId) {
			if (theId == null) {
				throw new NullPointerException("theId can not be null");
			}
			if (theId.hasIdPart() == false) {
				throw new NullPointerException("theId must not be blank and must contain an ID, found: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IPatchExecutable withId(String theId) {
			if (theId == null) {
				throw new NullPointerException("theId can not be null");
			}
			if (isBlank(theId)) {
				throw new NullPointerException("theId must not be blank and must contain an ID, found: " + theId);
			}
			myId = new IdDt(theId);
			return this;
		}

		@Override
		public IPatchTyped patchType(PatchTypeEnum patchType) {
			myPatchType = patchType;
			return this;
		}

		@Override
		public IPatchTyped patchBody(String patchBody) {
			myPatchBody = patchBody;
			return this;
		}

	}

	private class UpdateInternal extends BaseClientExecutable<IUpdateExecutable, MethodOutcome> implements IUpdate, IUpdateTyped, IUpdateExecutable, IUpdateWithQuery, IUpdateWithQueryTyped {

		private CriterionList myCriterionList;
		private IIdType myId;
		private PreferReturnEnum myPrefer;
		private IBaseResource myResource;
		private String myResourceBody;
		private String mySearchUrl;

		@Override
		public IUpdateWithQueryTyped and(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IUpdateWithQuery conditional() {
			myCriterionList = new CriterionList();
			return this;
		}

		@Override
		public IUpdateTyped conditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public MethodOutcome execute() {
			if (myResource == null) {
				myResource = parseResourceBody(myResourceBody);
			}

			// If an explicit encoding is chosen, we will re-serialize to ensure the right encoding
			if (getParamEncoding() != null) {
				myResourceBody = null;
			}

			BaseHttpClientInvocation invocation;
			if (mySearchUrl != null) {
				invocation = MethodUtil.createUpdateInvocation(myContext, myResource, myResourceBody, mySearchUrl);
			} else if (myCriterionList != null) {
				invocation = MethodUtil.createUpdateInvocation(myContext, myResource, myResourceBody, myCriterionList.toParamList());
			} else {
				if (myId == null) {
					myId = myResource.getIdElement();
				}

				if (myId == null || myId.hasIdPart() == false) {
					throw new InvalidRequestException("No ID supplied for resource to update, can not invoke server");
				}
				invocation = MethodUtil.createUpdateInvocation(myResource, myResourceBody, myId, myContext);
			}

			addPreferHeader(myPrefer, invocation);

			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResource);
			final String resourceName = def.getName();

			OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName, myPrefer);

			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);

		}

		@Override
		public IUpdateExecutable prefer(PreferReturnEnum theReturn) {
			myPrefer = theReturn;
			return this;
		}

		@Override
		public IUpdateTyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "Resource can not be null");
			myResource = theResource;
			return this;
		}

		@Override
		public IUpdateTyped resource(String theResourceBody) {
			Validate.notBlank(theResourceBody, "Body can not be null or blank");
			myResourceBody = theResourceBody;
			return this;
		}

		@Override
		public IUpdateWithQueryTyped where(ICriterion<?> theCriterion) {
			myCriterionList.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public IUpdateExecutable withId(IIdType theId) {
			if (theId == null) {
				throw new NullPointerException("theId can not be null");
			}
			if (theId.hasIdPart() == false) {
				throw new NullPointerException("theId must not be blank and must contain an ID, found: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IUpdateExecutable withId(String theId) {
			if (theId == null) {
				throw new NullPointerException("theId can not be null");
			}
			if (isBlank(theId)) {
				throw new NullPointerException("theId must not be blank and must contain an ID, found: " + theId);
			}
			myId = new IdDt(theId);
			return this;
		}

	}

	private class ValidateInternal extends BaseClientExecutable<IValidateUntyped, MethodOutcome> implements IValidate, IValidateUntyped {
		private IBaseResource myResource;

		@Override
		public MethodOutcome execute() {
			BaseHttpClientInvocation invocation = ValidateMethodBindingDstu2Plus.createValidateInvocation(myContext, myResource);
			ResourceResponseHandler<BaseOperationOutcome> handler = new ResourceResponseHandler<BaseOperationOutcome>(null, null);
			IBaseOperationOutcome outcome = invoke(null, handler, invocation);
			MethodOutcome retVal = new MethodOutcome();
			retVal.setOperationOutcome(outcome);
			return retVal;
		}

		@Override
		public IValidateUntyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "theResource must not be null");
			myResource = theResource;
			return this;
		}

		@Override
		public IValidateUntyped resource(String theResourceRaw) {
			Validate.notBlank(theResourceRaw, "theResourceRaw must not be null or blank");
			myResource = parseResourceBody(theResourceRaw);

			EncodingEnum enc = MethodUtil.detectEncodingNoDefault(theResourceRaw);
			if (enc == null) {
				throw new IllegalArgumentException(myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
			}
			switch (enc) {
			case XML:
				encodedXml();
				break;
			case JSON:
				encodedJson();
				break;
			}
			return this;
		}

	}

}
