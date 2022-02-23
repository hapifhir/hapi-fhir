package ca.uhn.fhir.rest.client.impl;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.IRuntimeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.UrlSourceEnum;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.client.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.client.method.HistoryMethodBinding;
import ca.uhn.fhir.rest.client.method.HttpDeleteClientInvocation;
import ca.uhn.fhir.rest.client.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.client.method.HttpSimpleGetClientInvocation;
import ca.uhn.fhir.rest.client.method.IClientResponseHandler;
import ca.uhn.fhir.rest.client.method.MethodUtil;
import ca.uhn.fhir.rest.client.method.OperationMethodBinding;
import ca.uhn.fhir.rest.client.method.ReadMethodBinding;
import ca.uhn.fhir.rest.client.method.SearchMethodBinding;
import ca.uhn.fhir.rest.client.method.SortParameter;
import ca.uhn.fhir.rest.client.method.TransactionMethodBinding;
import ca.uhn.fhir.rest.client.method.ValidateMethodBindingDstu2Plus;
import ca.uhn.fhir.rest.gclient.IBaseQuery;
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
import ca.uhn.fhir.rest.gclient.IHistory;
import ca.uhn.fhir.rest.gclient.IHistoryTyped;
import ca.uhn.fhir.rest.gclient.IHistoryUntyped;
import ca.uhn.fhir.rest.gclient.IMeta;
import ca.uhn.fhir.rest.gclient.IMetaAddOrDeleteSourced;
import ca.uhn.fhir.rest.gclient.IMetaAddOrDeleteUnsourced;
import ca.uhn.fhir.rest.gclient.IMetaGetUnsourced;
import ca.uhn.fhir.rest.gclient.IOperation;
import ca.uhn.fhir.rest.gclient.IOperationProcessMsg;
import ca.uhn.fhir.rest.gclient.IOperationProcessMsgMode;
import ca.uhn.fhir.rest.gclient.IOperationUnnamed;
import ca.uhn.fhir.rest.gclient.IOperationUntyped;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.gclient.IPatch;
import ca.uhn.fhir.rest.gclient.IPatchExecutable;
import ca.uhn.fhir.rest.gclient.IPatchWithBody;
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
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.util.ICallable;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
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

import java.io.IOException;
import java.io.InputStream;
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
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
public class GenericClient extends BaseClient implements IGenericClient {

	private static final String I18N_CANNOT_DETEMINE_RESOURCE_TYPE = GenericClient.class.getName() + ".cannotDetermineResourceTypeFromUri";
	private static final String I18N_INCOMPLETE_URI_FOR_READ = GenericClient.class.getName() + ".incompleteUriForRead";
	private static final String I18N_NO_VERSION_ID_FOR_VREAD = GenericClient.class.getName() + ".noVersionIdForVread";
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
	public IFetchConformanceUntyped capabilities() {
		return new FetchConformanceInternal();
	}

	@Override
	public ICreate create() {
		return new CreateInternal();
	}

	@Override
	public IDelete delete() {
		return new DeleteInternal();
	}

	private <T extends IBaseResource> T doReadOrVRead(final Class<T> theType, IIdType theId, boolean theVRead, ICallable<T> theNotModifiedHandler, String theIfVersionMatches, Boolean thePrettyPrint,
																	  SummaryEnum theSummary, EncodingEnum theEncoding, Set<String> theSubsetElements, String theCustomAcceptHeaderValue,
																	  Map<String, List<String>> theCustomHeaders) {
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
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(theCustomAcceptHeaderValue), getEncoding(), isPrettyPrint());
		}

		if (theIfVersionMatches != null) {
			invocation.addHeader(Constants.HEADER_IF_NONE_MATCH, '"' + theIfVersionMatches + '"');
		}

		boolean allowHtmlResponse = SummaryEnum.TEXT.equals(theSummary);
		ResourceResponseHandler<T> binding = new ResourceResponseHandler<>(theType, (Class<? extends IBaseResource>) null, id, allowHtmlResponse);

		if (theNotModifiedHandler == null) {
			return invokeClient(myContext, binding, invocation, theEncoding, thePrettyPrint, myLogRequestAndResponse, theSummary, theSubsetElements, null, theCustomAcceptHeaderValue, theCustomHeaders);
		}
		try {
			return invokeClient(myContext, binding, invocation, theEncoding, thePrettyPrint, myLogRequestAndResponse, theSummary, theSubsetElements, null, theCustomAcceptHeaderValue, theCustomHeaders);
		} catch (NotModifiedException e) {
			return theNotModifiedHandler.call();
		}

	}

	@Override
	public IFetchConformanceUntyped fetchConformance() {
		return new FetchConformanceInternal();
	}

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

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastRequest(IHttpRequest theLastRequest) {
		myLastRequest = theLastRequest;
	}

	protected String getPreferredId(IBaseResource theResource, String theId) {
		if (isNotBlank(theId)) {
			return theId;
		}
		return theResource.getIdElement().getIdPart();
	}

	@Override
	public IHistory history() {
		return new HistoryInternal();
	}

	/**
	 * @deprecated Use {@link LoggingInterceptor} as a client interceptor registered to your
	 * client instead, as this provides much more fine-grained control over what is logged. This
	 * method will be removed at some point (deprecated in HAPI 1.6 - 2016-06-16)
	 */
	@Deprecated
	public boolean isLogRequestAndResponse() {
		return myLogRequestAndResponse;
	}

	@Deprecated // override deprecated method
	@Override
	public void setLogRequestAndResponse(boolean theLogRequestAndResponse) {
		myLogRequestAndResponse = theLogRequestAndResponse;
	}

	@Override
	public IGetPage loadPage() {
		return new LoadPageInternal();
	}

	@Override
	public IMeta meta() {
		return new MetaInternal();
	}

	@Override
	public IOperation operation() {
		return new OperationInternal();
	}

	@Override
	public IPatch patch() {
		return new PatchInternal();
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
		return doReadOrVRead(theType, id, false, null, null, false, null, null, null, null, null);
	}

	@Override
	public IBaseResource read(UriDt theUrl) {
		IdDt id = new IdDt(theUrl);
		String resourceType = id.getResourceType();
		if (isBlank(resourceType)) {
			throw new IllegalArgumentException(Msg.code(1365) + myContext.getLocalizer().getMessage(I18N_INCOMPLETE_URI_FOR_READ, theUrl.getValueAsString()));
		}
		RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);
		if (def == null) {
			throw new IllegalArgumentException(Msg.code(1366) + myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theUrl.getValueAsString()));
		}
		return read(def.getImplementingClass(), id);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public IUntypedQuery search() {
		return new SearchInternal();
	}

	private String toResourceName(Class<? extends IBaseResource> theType) {
		return myContext.getResourceType(theType);
	}

	@Override
	public ITransaction transaction() {
		return new TransactionInternal();
	}

	@Override
	public IUpdate update() {
		return new UpdateInternal();
	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IBaseResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createUpdateInvocation(theResource, null, theIdDt, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(null), getEncoding(), isPrettyPrint());
		}

		OutcomeResponseHandler binding = new OutcomeResponseHandler();
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
		invocation = ValidateMethodBindingDstu2Plus.createValidateInvocation(myContext, theResource);

		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(null), getEncoding(), isPrettyPrint());
		}

		OutcomeResponseHandler binding = new OutcomeResponseHandler();
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IBaseResource> T vread(final Class<T> theType, IdDt theId) {
		if (!theId.hasVersionIdPart()) {
			throw new IllegalArgumentException(Msg.code(1367) + myContext.getLocalizer().getMessage(I18N_NO_VERSION_ID_FOR_VREAD, theId.getValue()));
		}
		return doReadOrVRead(theType, theId, true, null, null, false, null, null, null, null, null);
	}

	@Override
	public <T extends IBaseResource> T vread(Class<T> theType, String theId, String theVersionId) {
		IdDt resId = new IdDt(toResourceName(theType), theId, theVersionId);
		return vread(theType, resId);
	}

	private enum MetaOperation {
		ADD,
		DELETE,
		GET
	}

	private abstract class BaseClientExecutable<T extends IClientExecutable<?, Y>, Y> implements IClientExecutable<T, Y> {

		EncodingEnum myParamEncoding;
		Boolean myPrettyPrint;
		SummaryEnum mySummaryMode;
		CacheControlDirective myCacheControlDirective;
		Map<String, List<String>> myCustomHeaderValues = new HashMap<>();
		private String myCustomAcceptHeaderValue;
		private List<Class<? extends IBaseResource>> myPreferResponseTypes;
		private boolean myQueryLogRequestAndResponse;
		private Set<String> mySubsetElements;

		public String getCustomAcceptHeaderValue() {
			return myCustomAcceptHeaderValue;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T accept(String theHeaderValue) {
			myCustomAcceptHeaderValue = theHeaderValue;
			return (T) this;
		}

		@Deprecated // override deprecated method
		@SuppressWarnings("unchecked")
		@Override
		public T andLogRequestAndResponse(boolean theLogRequestAndResponse) {
			myQueryLogRequestAndResponse = theLogRequestAndResponse;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T cacheControl(CacheControlDirective theCacheControlDirective) {
			myCacheControlDirective = theCacheControlDirective;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T elementsSubset(String... theElements) {
			if (theElements != null && theElements.length > 0) {
				mySubsetElements = new HashSet<>(Arrays.asList(theElements));
			} else {
				mySubsetElements = null;
			}
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T encoded(EncodingEnum theEncoding) {
			Validate.notNull(theEncoding, "theEncoding must not be null");
			myParamEncoding = theEncoding;
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

		@SuppressWarnings("unchecked")
		@Override
		public T withAdditionalHeader(String theHeaderName, String theHeaderValue) {
			Objects.requireNonNull(theHeaderName, "headerName cannot be null");
			Objects.requireNonNull(theHeaderValue, "headerValue cannot be null");
			if (!myCustomHeaderValues.containsKey(theHeaderName)) {
				myCustomHeaderValues.put(theHeaderName, new ArrayList<>());
			}
			myCustomHeaderValues.get(theHeaderName).add(theHeaderValue);
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
			}
			return toTypeList(theDefault);
		}

		protected Set<String> getSubsetElements() {
			return mySubsetElements;
		}

		protected <Z> Z invoke(Map<String, List<String>> theParams, IClientResponseHandler<Z> theHandler, BaseHttpClientInvocation theInvocation) {
			if (isKeepResponses()) {
				myLastRequest = theInvocation.asHttpRequest(getServerBase(), theParams, getEncoding(), myPrettyPrint);
			}

			Z resp = invokeClient(myContext, theHandler, theInvocation, myParamEncoding, myPrettyPrint, myQueryLogRequestAndResponse || myLogRequestAndResponse, mySummaryMode, mySubsetElements, myCacheControlDirective, myCustomAcceptHeaderValue, myCustomHeaderValues);
			return resp;
		}

		protected IBaseResource parseResourceBody(String theResourceBody) {
			EncodingEnum encoding = EncodingEnum.detectEncodingNoDefault(theResourceBody);
			if (encoding == null) {
				throw new IllegalArgumentException(Msg.code(1368) + myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
			}
			return encoding.newParser(myContext).parseResource(theResourceBody);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T preferResponseType(Class<? extends IBaseResource> theClass) {
			myPreferResponseTypes = null;
			if (theClass != null) {
				myPreferResponseTypes = new ArrayList<>();
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

	private abstract class BaseSearch<EXEC extends IClientExecutable<?, OUTPUT>, QUERY extends IBaseQuery<QUERY>, OUTPUT> extends BaseClientExecutable<EXEC, OUTPUT> implements IBaseQuery<QUERY> {

		private Map<String, List<String>> myParams = new LinkedHashMap<>();

		@Override
		public QUERY and(ICriterion<?> theCriterion) {
			return where(theCriterion);
		}

		public Map<String, List<String>> getParamMap() {
			return myParams;
		}

		@SuppressWarnings("unchecked")
		@Override
		public QUERY where(ICriterion<?> theCriterion) {
			ICriterionInternal criterion = (ICriterionInternal) theCriterion;

			String parameterName = criterion.getParameterName();
			String parameterValue = criterion.getParameterValue(myContext);
			if (isNotBlank(parameterValue)) {
				addParam(myParams, parameterName, parameterValue);
			}

			return (QUERY) this;
		}

		@Override
		public QUERY whereMap(Map<String, List<String>> theRawMap) {
			if (theRawMap != null) {
				for (String nextKey : theRawMap.keySet()) {
					for (String nextValue : theRawMap.get(nextKey)) {
						addParam(myParams, nextKey, nextValue);
					}
				}
			}

			return (QUERY) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public QUERY where(Map<String, List<IQueryParameterType>> theCriterion) {
			Validate.notNull(theCriterion, "theCriterion must not be null");
			for (Entry<String, List<IQueryParameterType>> nextEntry : theCriterion.entrySet()) {
				String nextKey = nextEntry.getKey();
				List<IQueryParameterType> nextValues = nextEntry.getValue();
				for (IQueryParameterType nextValue : nextValues) {
					String value = nextValue.getValueAsQueryToken(myContext);
					String qualifier = nextValue.getQueryParameterQualifier();
					if (isNotBlank(qualifier)) {
						nextKey = nextKey + qualifier;
					}
					addParam(myParams, nextKey, value);
				}
			}
			return (QUERY) this;
		}

	}

	private class CreateInternal extends BaseSearch<ICreateTyped, ICreateWithQueryTyped, MethodOutcome> implements ICreate, ICreateTyped, ICreateWithQuery, ICreateWithQueryTyped {

		private boolean myConditional;
		private PreferReturnEnum myPrefer;
		private IBaseResource myResource;
		private String myResourceBody;
		private String mySearchUrl;

		@Override
		public ICreateWithQuery conditional() {
			myConditional = true;
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

			// If an explicit encoding is chosen, we will re-serialize to ensure the right encoding
			if (getParamEncoding() != null) {
				myResourceBody = null;
			}

			BaseHttpClientInvocation invocation;
			if (mySearchUrl != null) {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myContext, mySearchUrl);
			} else if (myConditional) {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myContext, getParamMap());
			} else {
				invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myContext);
			}

			addPreferHeader(myPrefer, invocation);

			OutcomeResponseHandler binding = new OutcomeResponseHandler(myPrefer);

			Map<String, List<String>> params = new HashMap<>();
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

	}

	private class DeleteInternal extends BaseSearch<IDeleteTyped, IDeleteWithQueryTyped, MethodOutcome> implements IDelete, IDeleteTyped, IDeleteWithQuery, IDeleteWithQueryTyped {

		private boolean myConditional;
		private IIdType myId;
		private String myResourceType;
		private String mySearchUrl;
		private DeleteCascadeModeEnum myCascadeMode;

		@Override
		public MethodOutcome execute() {

			Map<String, List<String>> additionalParams = new HashMap<>();
			if (myCascadeMode != null) {
				switch (myCascadeMode) {
					case DELETE:
						addParam(getParamMap(), Constants.PARAMETER_CASCADE_DELETE, Constants.CASCADE_DELETE);
						break;
					default:
					case NONE:
						break;
				}
			}

			HttpDeleteClientInvocation invocation;
			if (myId != null) {
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), myId, getParamMap());
			} else if (myConditional) {
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), myResourceType, getParamMap());
			} else {
				invocation = DeleteMethodBinding.createDeleteInvocation(getFhirContext(), mySearchUrl, getParamMap());
			}

			OutcomeResponseHandler binding = new OutcomeResponseHandler();

			return invoke(additionalParams, binding, invocation);
		}

		@Override
		public IDeleteTyped resource(IBaseResource theResource) {
			Validate.notNull(theResource, "theResource can not be null");
			IIdType id = theResource.getIdElement();
			Validate.notNull(id, "theResource.getIdElement() can not be null");
			if (!id.hasResourceType() || !id.hasIdPart()) {
				throw new IllegalArgumentException(Msg.code(1369) + "theResource.getId() must contain a resource type and logical ID at a minimum (e.g. Patient/1234), found: " + id.getValue());
			}
			myId = id;
			return this;
		}

		@Override
		public IDeleteTyped resourceById(IIdType theId) {
			Validate.notNull(theId, "theId can not be null");
			if (!theId.hasResourceType() || !theId.hasIdPart()) {
				throw new IllegalArgumentException(Msg.code(1370) + "theId must contain a resource type and logical ID at a minimum (e.g. Patient/1234)found: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IDeleteTyped resourceById(String theResourceType, String theLogicalId) {
			Validate.notBlank(theResourceType, "theResourceType can not be blank/null");
			if (myContext.getResourceDefinition(theResourceType) == null) {
				throw new IllegalArgumentException(Msg.code(1371) + "Unknown resource type");
			}
			Validate.notBlank(theLogicalId, "theLogicalId can not be blank/null");
			if (theLogicalId.contains("/")) {
				throw new IllegalArgumentException(Msg.code(1372) + "LogicalId can not contain '/' (should only be the logical ID portion, not a qualified ID)");
			}
			myId = new IdDt(theResourceType, theLogicalId);
			return this;
		}

		@Override
		public IDeleteWithQuery resourceConditionalByType(Class<? extends IBaseResource> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType can not be null");
			myConditional = true;
			myResourceType = myContext.getResourceType(theResourceType);
			return this;
		}

		@Override
		public IDeleteWithQuery resourceConditionalByType(String theResourceType) {
			Validate.notBlank(theResourceType, "theResourceType can not be blank/null");
			if (myContext.getResourceDefinition(theResourceType) == null) {
				throw new IllegalArgumentException(Msg.code(1373) + "Unknown resource type: " + theResourceType);
			}
			myResourceType = theResourceType;
			myConditional = true;
			return this;
		}

		@Override
		public IDeleteTyped resourceConditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public IDeleteTyped cascade(DeleteCascadeModeEnum theDelete) {
			myCascadeMode = theDelete;
			return this;
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class FetchConformanceInternal extends BaseClientExecutable implements IFetchConformanceUntyped, IFetchConformanceTyped {
		private RuntimeResourceDefinition myType;

		@Override
		public Object execute() {
			ResourceResponseHandler binding = new ResourceResponseHandler(myType.getImplementingClass());
			FhirContext fhirContext = getFhirContext();
			HttpGetClientInvocation invocation = MethodUtil.createConformanceInvocation(fhirContext);
			return super.invoke(null, binding, invocation);
		}

		@Override
		public <T extends IBaseConformance> IFetchConformanceTyped<T> ofType(Class<T> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType must not be null");
			myType = myContext.getResourceDefinition(theResourceType);
			if (myType == null) {
				throw new IllegalArgumentException(Msg.code(1374) + myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceType));
			}
			return this;
		}

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private class GetPageInternal extends BaseClientExecutable<IGetPageTyped<Object>, Object> implements IGetPageTyped<Object> {

		private Class<? extends IBaseBundle> myBundleType;
		private String myUrl;

		public GetPageInternal(String theUrl, Class<? extends IBaseBundle> theBundleType) {
			myUrl = theUrl;
			myBundleType = theBundleType;
		}

		@Override
		public Object execute() {
			IClientResponseHandler binding;
			binding = new ResourceResponseHandler(myBundleType, getPreferResponseTypes());
			HttpSimpleGetClientInvocation invocation = new HttpSimpleGetClientInvocation(myContext, myUrl);
			invocation.setUrlSource(UrlSourceEnum.EXPLICIT);

			Map<String, List<String>> params = null;
			return invoke(params, binding, invocation);
		}

	}

	@SuppressWarnings("rawtypes")
	private class HistoryInternal extends BaseClientExecutable implements IHistory, IHistoryUntyped, IHistoryTyped {

		private Integer myCount;
		private IIdType myId;
		private Class<? extends IBaseBundle> myReturnType;
		private IPrimitiveType mySince;
		private Class<? extends IBaseResource> myType;
		private DateRangeParam myAt;

		@SuppressWarnings("unchecked")
		@Override
		public IHistoryTyped andReturnBundle(Class theType) {
			return returnBundle(theType);
		}

		@Override
		public IHistoryTyped returnBundle(Class theType) {
			Validate.notNull(theType, "theType must not be null on method andReturnBundle(Class)");
			myReturnType = theType;
			return this;
		}

		@Override
		public IHistoryTyped at(DateRangeParam theDateRangeParam) {
			myAt = theDateRangeParam;
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
				resourceName = myContext.getResourceType(myType);
				id = null;
			} else if (myId != null) {
				resourceName = myId.getResourceType();
				id = myId.getIdPart();
			} else {
				resourceName = null;
				id = null;
			}

			HttpGetClientInvocation invocation = HistoryMethodBinding.createHistoryInvocation(myContext, resourceName, id, mySince, myCount, myAt);

			IClientResponseHandler handler;
			handler = new ResourceResponseHandler(myReturnType, getPreferResponseTypes(myType));

			return invoke(null, handler, invocation);
		}

		@Override
		public IHistoryUntyped onInstance(IIdType theId) {
			if (!theId.hasResourceType()) {
				throw new IllegalArgumentException(Msg.code(1375) + "Resource ID does not have a resource type: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IHistoryUntyped onInstance(String theId) {
			Validate.notBlank(theId, "theId must not be null or blank");
			IIdType id = myContext.getVersion().newIdType();
			id.setValue(theId);
			return onInstance(id);
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
		public IHistoryUntyped onType(String theResourceType) {
			myType = myContext.getResourceDefinition(theResourceType).getImplementingClass();
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

	@SuppressWarnings({"unchecked", "rawtypes"})
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
		public IGetPageUntyped byUrl(String thePageUrl) {
			if (isBlank(thePageUrl)) {
				throw new IllegalArgumentException(Msg.code(1376) + "thePagingUrl must not be blank or null");
			}
			myPageUrl = thePageUrl;
			return this;
		}

		@Override
		public <T extends IBaseBundle> IGetPageTyped<T> next(T theBundle) {
			return nextOrPrevious("next", theBundle);
		}

		private <T extends IBaseBundle> IGetPageTyped<T> nextOrPrevious(String theWantRel, T theBundle) {
			RuntimeResourceDefinition def = myContext.getResourceDefinition(theBundle);
			List<IBase> links = def.getChildByName("link").getAccessor().getValues(theBundle);
			if (links == null || links.isEmpty()) {
				throw new IllegalArgumentException(Msg.code(1377) + myContext.getLocalizer().getMessage(GenericClient.class, "noPagingLinkFoundInBundle", theWantRel));
			}
			for (IBase nextLink : links) {
				BaseRuntimeElementCompositeDefinition linkDef = (BaseRuntimeElementCompositeDefinition) myContext.getElementDefinition(nextLink.getClass());
				List<IBase> rel = linkDef.getChildByName("relation").getAccessor().getValues(nextLink);
				if (rel == null || rel.isEmpty()) {
					continue;
				}
				String relation = ((IPrimitiveType<?>) rel.get(0)).getValueAsString();
				if (theWantRel.equals(relation) || (PREVIOUS.equals(theWantRel) && PREV.equals(relation))) {
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
			throw new IllegalArgumentException(Msg.code(1378) + myContext.getLocalizer().getMessage(GenericClient.class, "noPagingLinkFoundInBundle", theWantRel));
		}

		@Override
		public <T extends IBaseBundle> IGetPageTyped<T> previous(T theBundle) {
			return nextOrPrevious(PREVIOUS, theBundle);
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
					ParametersUtil.addParameterToParameters(myContext, parameters, "meta", myMeta);
					invocation = OperationMethodBinding.createOperationInvocation(myContext, myId.getResourceType(), myId.getIdPart(), null, "$meta-add", parameters, false);
					break;
				case DELETE:
					ParametersUtil.addParameterToParameters(myContext, parameters, "meta", myMeta);
					invocation = OperationMethodBinding.createOperationInvocation(myContext, myId.getResourceType(), myId.getIdPart(), null, "$meta-delete", parameters, false);
					break;
				case GET:
					if (myId != null) {
						invocation = OperationMethodBinding.createOperationInvocation(myContext, myOnType, myId.getIdPart(), null, "$meta", parameters, true);
					} else if (myOnType != null) {
						invocation = OperationMethodBinding.createOperationInvocation(myContext, myOnType, null, null, "$meta", parameters, true);
					} else {
						invocation = OperationMethodBinding.createOperationInvocation(myContext, null, null, null, "$meta", parameters, true);
					}
					break;
			}

			// Should not happen
			if (invocation == null) {
				throw new IllegalStateException(Msg.code(1379));
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
		public <T extends IBaseMetaType> IClientExecutable<IClientExecutable<?, T>, T> meta(T theMeta) {
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

	private final class MetaParametersResponseHandler<T extends IBaseMetaType> implements IClientResponseHandler<T> {

		private Class<T> myType;

		public MetaParametersResponseHandler(Class<T> theMetaType) {
			myType = theMetaType;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseInputStream);
			}
			IParser parser = respType.newParser(myContext);
			RuntimeResourceDefinition type = myContext.getResourceDefinition("Parameters");
			IBaseResource retVal = parser.parseResource(type.getImplementingClass(), theResponseInputStream);

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
	private class OperationInternal extends BaseClientExecutable
		implements IOperation, IOperationUnnamed, IOperationUntyped, IOperationUntypedWithInput, IOperationUntypedWithInputAndPartialOutput, IOperationProcessMsg, IOperationProcessMsgMode {

		private IIdType myId;
		private Boolean myIsAsync;
		private IBaseBundle myMsgBundle;
		private String myOperationName;
		private IBaseParameters myParameters;
		private RuntimeResourceDefinition myParametersDef;
		private String myResponseUrl;
		private Class myReturnResourceType;
		private Class<? extends IBaseResource> myType;
		private boolean myUseHttpGet;
		private boolean myReturnMethodOutcome;

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
				throw new IllegalArgumentException(Msg.code(1380) + "Don't know how to handle parameter of type " + theValue.getClass());
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

		@Override
		public IOperationProcessMsgMode asynchronous(Class theResponseClass) {
			myIsAsync = true;
			Validate.notNull(theResponseClass, "theReturnType must not be null");
			Validate.isTrue(IBaseResource.class.isAssignableFrom(theResponseClass), "theReturnType must be a class which extends from IBaseResource");
			myReturnResourceType = theResponseClass;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object execute() {
			if (myOperationName != null && myOperationName.equals(Constants.EXTOP_PROCESS_MESSAGE) && myMsgBundle != null) {
				Map<String, List<String>> urlParams = new LinkedHashMap<String, List<String>>();
				// Set Url parameter Async and Response-Url
				if (myIsAsync != null) {
					urlParams.put(Constants.PARAM_ASYNC, Arrays.asList(String.valueOf(myIsAsync)));
				}

				if (myResponseUrl != null && isNotBlank(myResponseUrl)) {
					urlParams.put(Constants.PARAM_RESPONSE_URL, Arrays.asList(String.valueOf(myResponseUrl)));
				}
				// If is $process-message operation
				BaseHttpClientInvocation invocation = OperationMethodBinding.createProcessMsgInvocation(myContext, myOperationName, myMsgBundle, urlParams);

				ResourceResponseHandler handler = new ResourceResponseHandler();
				handler.setPreferResponseTypes(getPreferResponseTypes(myType));

				Object retVal = invoke(null, handler, invocation);
				return retVal;
			}

			String resourceName;
			String id;
			String version;
			if (myType != null) {
				resourceName = myContext.getResourceType(myType);
				id = null;
				version = null;
			} else if (myId != null) {
				resourceName = myId.getResourceType();
				Validate.notBlank(defaultString(resourceName), "Can not invoke operation \"$%s\" on instance \"%s\" - No resource type specified", myOperationName, myId.getValue());
				id = myId.getIdPart();
				version = myId.getVersionIdPart();
			} else {
				resourceName = null;
				id = null;
				version = null;
			}

			BaseHttpClientInvocation invocation = OperationMethodBinding.createOperationInvocation(myContext, resourceName, id, version, myOperationName, myParameters, myUseHttpGet);

			if (myReturnResourceType != null) {
				ResourceResponseHandler handler;
				handler = new ResourceResponseHandler(myReturnResourceType);
				Object retVal = invoke(null, handler, invocation);
				return retVal;
			}
			IClientResponseHandler handler = new ResourceOrBinaryResponseHandler()
				.setPreferResponseTypes(getPreferResponseTypes(myType));

			if (myReturnMethodOutcome) {
				handler = new MethodOutcomeResponseHandler(handler);
			}

			Object retVal = invoke(null, handler, invocation);

			if (myReturnMethodOutcome) {
				return retVal;
			}

			if (myContext.getResourceDefinition((IBaseResource) retVal).getName().equals("Parameters")) {
				return retVal;
			}
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

		@Override
		public IOperationUntyped named(String theName) {
			Validate.notBlank(theName, "theName can not be null");
			myOperationName = theName;
			return this;
		}

		@Override
		public IOperationUnnamed onInstance(IIdType theId) {
			myId = theId.toVersionless();
			return this;
		}

		@Override
		public IOperationUnnamed onInstance(String theId) {
			Validate.notBlank(theId, "theId must not be null or blank");
			IIdType id = myContext.getVersion().newIdType();
			id.setValue(theId);
			return onInstance(id);
		}

		@Override
		public IOperationUnnamed onInstanceVersion(IIdType theId) {
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
		public IOperationUnnamed onType(String theResourceType) {
			myType = myContext.getResourceDefinition(theResourceType).getImplementingClass();
			return this;
		}

		@Override
		public IOperationProcessMsg processMessage() {
			myOperationName = Constants.EXTOP_PROCESS_MESSAGE;
			return this;
		}

		@Override
		public IOperationUntypedWithInput returnResourceType(Class theReturnType) {
			Validate.notNull(theReturnType, "theReturnType must not be null");
			Validate.isTrue(IBaseResource.class.isAssignableFrom(theReturnType), "theReturnType must be a class which extends from IBaseResource");
			myReturnResourceType = theReturnType;
			return this;
		}

		@Override
		public IOperationUntypedWithInput returnMethodOutcome() {
			myReturnMethodOutcome = true;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public IOperationProcessMsgMode setMessageBundle(IBaseBundle theMsgBundle) {

			Validate.notNull(theMsgBundle, "theMsgBundle must not be null");
			/*
			 * Validate.isTrue(theMsgBundle.getType().getValueAsEnum() == BundleTypeEnum.MESSAGE);
			 * Validate.isTrue(theMsgBundle.getEntries().size() > 0);
			 * Validate.notNull(theMsgBundle.getEntries().get(0).getResource(), "Message Bundle first entry must be a MessageHeader resource");
			 * Validate.isTrue(theMsgBundle.getEntries().get(0).getResource().getResourceName().equals("MessageHeader"), "Message Bundle first entry must be a MessageHeader resource");
			 */
			myMsgBundle = theMsgBundle;
			return this;
		}

		@Override
		public IOperationProcessMsg setResponseUrlParam(String responseUrl) {
			Validate.notEmpty(responseUrl, "responseUrl must not be null");
			Validate.matchesPattern(responseUrl, "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "responseUrl must be a valid URL");
			myResponseUrl = responseUrl;
			return this;
		}

		@Override
		public IOperationProcessMsgMode synchronous(Class theResponseClass) {
			myIsAsync = false;
			Validate.notNull(theResponseClass, "theReturnType must not be null");
			Validate.isTrue(IBaseResource.class.isAssignableFrom(theResponseClass), "theReturnType must be a class which extends from IBaseResource");
			myReturnResourceType = theResponseClass;
			return this;
		}

		@Override
		public IOperationUntypedWithInput useHttpGet() {
			myUseHttpGet = true;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends IBaseParameters> IOperationUntypedWithInputAndPartialOutput<T> withNoParameters(Class<T> theOutputParameterType) {
			Validate.notNull(theOutputParameterType, "theOutputParameterType may not be null");
			RuntimeResourceDefinition def = myContext.getResourceDefinition(theOutputParameterType);
			if (def == null) {
				throw new IllegalArgumentException(Msg.code(1381) + "theOutputParameterType must refer to a HAPI FHIR Resource type: " + theOutputParameterType.getName());
			}
			if (!"Parameters".equals(def.getName())) {
				throw new IllegalArgumentException(Msg.code(1382) + "theOutputParameterType must refer to a HAPI FHIR Resource type for a resource named " + "Parameters" + " - " + theOutputParameterType.getName()
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

		@SuppressWarnings({"unchecked"})
		@Override
		public IOperationUntypedWithInputAndPartialOutput withParameters(IBaseParameters theParameters) {
			Validate.notNull(theParameters, "theParameters can not be null");
			myParameters = theParameters;
			myParametersDef = myContext.getResourceDefinition(theParameters.getClass());
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

	}


	private final class MethodOutcomeResponseHandler implements IClientResponseHandler<MethodOutcome> {
		private final IClientResponseHandler<? extends IBaseResource> myWrap;

		private MethodOutcomeResponseHandler(IClientResponseHandler<? extends IBaseResource> theWrap) {
			myWrap = theWrap;
		}

		@Override
		public MethodOutcome invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
			IBaseResource response = myWrap.invokeClient(theResponseMimeType, theResponseInputStream, theResponseStatusCode, theHeaders);

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(response);
			retVal.setCreatedUsingStatusCode(theResponseStatusCode);
			retVal.setResponseHeaders(theHeaders);
			return retVal;
		}
	}

	private final class OutcomeResponseHandler implements IClientResponseHandler<MethodOutcome> {
		private PreferReturnEnum myPrefer;

		private OutcomeResponseHandler() {
			super();
		}

		private OutcomeResponseHandler(PreferReturnEnum thePrefer) {
			this();
			myPrefer = thePrefer;
		}

		@Override
		public MethodOutcome invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			MethodOutcome response = MethodUtil.process2xxResponse(myContext, theResponseStatusCode, theResponseMimeType, theResponseInputStream, theHeaders);
			response.setCreatedUsingStatusCode(theResponseStatusCode);

			if (myPrefer == PreferReturnEnum.REPRESENTATION) {
				if (response.getResource() == null) {
					if (response.getId() != null && isNotBlank(response.getId().getValue()) && response.getId().hasBaseUrl()) {
						ourLog.info("Server did not return resource for Prefer-representation, going to fetch: {}", response.getId().getValue());
						IBaseResource resource = read().resource(response.getId().getResourceType()).withUrl(response.getId()).execute();
						response.setResource(resource);
					}
				}
			}

			response.setResponseHeaders(theHeaders);

			return response;
		}
	}

	private class PatchInternal extends BaseSearch<IPatchExecutable, IPatchWithQueryTyped, MethodOutcome> implements IPatch, IPatchWithBody, IPatchExecutable, IPatchWithQuery, IPatchWithQueryTyped {

		private boolean myConditional;
		private IIdType myId;
		private String myPatchBody;
		private PatchTypeEnum myPatchType;
		private PreferReturnEnum myPrefer;
		private String myResourceType;
		private String mySearchUrl;

		@Override
		public IPatchWithQuery conditional(Class<? extends IBaseResource> theClass) {
			Validate.notNull(theClass, "theClass must not be null");
			String resourceType = myContext.getResourceType(theClass);
			return conditional(resourceType);
		}

		@Override
		public IPatchWithQuery conditional(String theResourceType) {
			Validate.notBlank(theResourceType, "theResourceType must not be null");
			myResourceType = theResourceType;
			myConditional = true;
			return this;
		}

		// TODO: This is not longer used.. Deprecate it or just remove it?
		@Override
		public IPatchWithBody conditionalByUrl(String theSearchUrl) {
			mySearchUrl = validateAndEscapeConditionalUrl(theSearchUrl);
			return this;
		}

		@Override
		public MethodOutcome execute() {

			if (myPatchType == null) {
				throw new InvalidRequestException(Msg.code(1383) + "No patch type supplied, cannot invoke server");
			}
			if (myPatchBody == null) {
				throw new InvalidRequestException(Msg.code(1384) + "No patch body supplied, cannot invoke server");
			}

			BaseHttpClientInvocation invocation;
			if (isNotBlank(mySearchUrl)) {
				invocation = MethodUtil.createPatchInvocation(myContext, mySearchUrl, myPatchType, myPatchBody);
			} else if (myConditional) {
				invocation = MethodUtil.createPatchInvocation(myContext, myPatchType, myPatchBody, myResourceType, getParamMap());
			} else {
				if (myId == null || myId.hasIdPart() == false) {
					throw new InvalidRequestException(Msg.code(1385) + "No ID supplied for resource to patch, can not invoke server");
				}
				invocation = MethodUtil.createPatchInvocation(myContext, myId, myPatchType, myPatchBody);
			}

			addPreferHeader(myPrefer, invocation);

			OutcomeResponseHandler binding = new OutcomeResponseHandler(myPrefer);

			Map<String, List<String>> params = new HashMap<>();
			return invoke(params, binding, invocation);

		}

		@Override
		public IPatchExecutable prefer(PreferReturnEnum theReturn) {
			myPrefer = theReturn;
			return this;
		}

		@Override
		public IPatchWithBody withBody(String thePatchBody) {
			Validate.notBlank(thePatchBody, "thePatchBody must not be blank");

			myPatchBody = thePatchBody;

			EncodingEnum encoding = EncodingEnum.detectEncodingNoDefault(thePatchBody);
			if (encoding == EncodingEnum.XML) {
				myPatchType = PatchTypeEnum.XML_PATCH;
			} else if (encoding == EncodingEnum.JSON) {
				myPatchType = PatchTypeEnum.JSON_PATCH;
			} else {
				throw new IllegalArgumentException(Msg.code(1386) + "Unable to determine encoding of patch");
			}

			return this;
		}

		@Override
		public IPatchWithBody withFhirPatch(IBaseParameters thePatchBody) {
			Validate.notNull(thePatchBody, "thePatchBody must not be null");

			myPatchType = PatchTypeEnum.FHIR_PATCH_JSON;
			myPatchBody = myContext.newJsonParser().encodeResourceToString(thePatchBody);

			return this;
		}

		@Override
		public IPatchExecutable withId(IIdType theId) {
			if (theId == null) {
				throw new NullPointerException(Msg.code(1387) + "theId can not be null");
			}
			Validate.notBlank(theId.getIdPart(), "theId must not be blank and must contain a resource type and ID (e.g. \"Patient/123\"), found: %s", UrlUtil.sanitizeUrlPart(theId.getValue()));
			Validate.notBlank(theId.getResourceType(), "theId must not be blank and must contain a resource type and ID (e.g. \"Patient/123\"), found: %s", UrlUtil.sanitizeUrlPart(theId.getValue()));
			myId = theId;
			return this;
		}

		@Override
		public IPatchExecutable withId(String theId) {
			if (theId == null) {
				throw new NullPointerException(Msg.code(1388) + "theId can not be null");
			}
			return withId(new IdDt(theId));
		}

	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class ReadInternal extends BaseClientExecutable implements IRead, IReadTyped, IReadExecutable {
		private IIdType myId;
		private String myIfVersionMatches;
		private ICallable myNotModifiedHandler;
		private RuntimeResourceDefinition myType;

		@Override
		public Object execute() {// AAA
			if (myId.hasVersionIdPart()) {
				return doReadOrVRead(myType.getImplementingClass(), myId, true, myNotModifiedHandler, myIfVersionMatches, myPrettyPrint, mySummaryMode, myParamEncoding, getSubsetElements(), getCustomAcceptHeaderValue(), myCustomHeaderValues);
			}
			return doReadOrVRead(myType.getImplementingClass(), myId, false, myNotModifiedHandler, myIfVersionMatches, myPrettyPrint, mySummaryMode, myParamEncoding, getSubsetElements(), getCustomAcceptHeaderValue(), myCustomHeaderValues);
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
				throw new IllegalArgumentException(Msg.code(1389) + myContext.getLocalizer().getMessage(I18N_INCOMPLETE_URI_FOR_READ, myId));
			}
			myType = myContext.getResourceDefinition(resourceType);
			if (myType == null) {
				throw new IllegalArgumentException(Msg.code(1390) + myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, myId));
			}
		}

		@Override
		public <T extends IBaseResource> IReadTyped<T> resource(Class<T> theResourceType) {
			Validate.notNull(theResourceType, "theResourceType must not be null");
			myType = myContext.getResourceDefinition(theResourceType);
			if (myType == null) {
				throw new IllegalArgumentException(Msg.code(1391) + myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceType));
			}
			return this;
		}

		@Override
		public IReadTyped<IBaseResource> resource(String theResourceAsText) {
			Validate.notBlank(theResourceAsText, "You must supply a value for theResourceAsText");
			myType = myContext.getResourceDefinition(theResourceAsText);
			if (myType == null) {
				throw new IllegalArgumentException(Msg.code(1392) + myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theResourceAsText));
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

		@SuppressWarnings("unchecked")
		@Override
		public List<IBaseResource> invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders)
			throws BaseServerResponseException {
			Class<? extends IBaseResource> bundleType = myContext.getResourceDefinition("Bundle").getImplementingClass();
			ResourceResponseHandler<IBaseResource> handler = new ResourceResponseHandler<>((Class<IBaseResource>) bundleType);
			IBaseResource response = handler.invokeClient(theResponseMimeType, theResponseInputStream, theResponseStatusCode, theHeaders);
			IVersionSpecificBundleFactory bundleFactory = myContext.newBundleFactory();
			bundleFactory.initializeWithBundleResource(response);
			return bundleFactory.toListOfResources();
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private class SearchInternal<OUTPUT> extends BaseSearch<IQuery<OUTPUT>, IQuery<OUTPUT>, OUTPUT> implements IQuery<OUTPUT>, IUntypedQuery<IQuery<OUTPUT>> {

		private String myCompartmentName;
		private List<Include> myInclude = new ArrayList<>();
		private DateRangeParam myLastUpdated;
		private Integer myParamLimit;
		private Integer myParamOffset;
		private List<Collection<String>> myProfiles = new ArrayList<>();
		private String myResourceId;
		private String myResourceName;
		private Class<? extends IBaseResource> myResourceType;
		private Class<? extends IBaseBundle> myReturnBundleType;
		private List<Include> myRevInclude = new ArrayList<>();
		private SearchStyleEnum mySearchStyle;
		private String mySearchUrl;
		private List<TokenParam> mySecurity = new ArrayList<>();
		private List<SortInternal> mySort = new ArrayList<>();
		private List<TokenParam> myTags = new ArrayList<>();
		private SearchTotalModeEnum myTotalMode;

		public SearchInternal() {
			myResourceType = null;
			myResourceName = null;
			mySearchUrl = null;
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
					throw new IllegalArgumentException(Msg.code(1393) + "Search URL must be either a complete URL starting with http: or https:, or a relative FHIR URL in the form [ResourceType]?[Params]");
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
		public IQuery offset(int theOffset) {
			if (theOffset >= 0) {
				myParamOffset = theOffset;
			} else {
				myParamOffset = null;
			}
			return this;
		}

		@Override
		public OUTPUT execute() {

			Map<String, List<String>> params = getParamMap();

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
					if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
						addParam(params, Constants.PARAM_INCLUDE_ITERATE, next.getValue());
					} else {
						addParam(params, Constants.PARAM_INCLUDE_RECURSE, next.getValue());
					}
				} else {
					addParam(params, Constants.PARAM_INCLUDE, next.getValue());
				}
			}

			for (Include next : myRevInclude) {
				if (next.isRecurse()) {
					if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
						addParam(params, Constants.PARAM_REVINCLUDE_ITERATE, next.getValue());
					} else {
						addParam(params, Constants.PARAM_REVINCLUDE_RECURSE, next.getValue());
					}
				} else {
					addParam(params, Constants.PARAM_REVINCLUDE, next.getValue());
				}
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

			if (myParamOffset != null) {
				addParam(params, Constants.PARAM_OFFSET, Integer.toString(myParamOffset));
			}

			if (myLastUpdated != null) {
				for (DateParam next : myLastUpdated.getValuesAsQueryTokens()) {
					addParam(params, Constants.PARAM_LASTUPDATED, next.getValueAsQueryToken(myContext));
				}
			}

			if (myTotalMode != null) {
				addParam(params, Constants.PARAM_SEARCH_TOTAL_MODE, myTotalMode.getCode());
			}

			IClientResponseHandler<? extends IBase> binding;
			binding = new ResourceResponseHandler(myReturnBundleType, getPreferResponseTypes(myResourceType));

			IdDt resourceId = myResourceId != null ? new IdDt(myResourceId) : null;

			BaseHttpClientInvocation invocation;
			if (mySearchUrl != null) {
				invocation = SearchMethodBinding.createSearchInvocation(myContext, mySearchUrl, UrlSourceEnum.EXPLICIT, params);
			} else {
				invocation = SearchMethodBinding.createSearchInvocation(myContext, myResourceName, params, resourceId, myCompartmentName, mySearchStyle);
			}

			return (OUTPUT) invoke(params, binding, invocation);

		}

		@Override
		public IQuery forAllResources() {
			return this;
		}

		@Override
		public IQuery forResource(Class theResourceType) {
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

		@Deprecated // override deprecated method
		@Override
		public IQuery limitTo(int theLimitTo) {
			return count(theLimitTo);
		}

		@Override
		public IQuery<OUTPUT> totalMode(SearchTotalModeEnum theSearchTotalModeEnum) {
			myTotalMode = theSearchTotalModeEnum;
			return this;
		}

		@Override
		public IQuery returnBundle(Class theClass) {
			if (theClass == null) {
				throw new NullPointerException(Msg.code(1394) + "theClass must not be null");
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
		public IQuery sort(SortSpec theSortSpec) {
			SortSpec sortSpec = theSortSpec;
			while (sortSpec != null) {
				mySort.add(new SortInternal(sortSpec));
				sortSpec = sortSpec.getChain();
			}
			return this;
		}

		@Override
		public IQuery usingStyle(SearchStyleEnum theStyle) {
			mySearchStyle = theStyle;
			return this;
		}

		@Override
		public IQuery withAnyProfile(Collection theProfileUris) {
			Validate.notEmpty(theProfileUris, "theProfileUris must not be null or empty");
			myProfiles.add(theProfileUris);
			return this;
		}

		@Override
		public IQuery withIdAndCompartment(String theResourceId, String theCompartmentName) {
			myResourceId = theResourceId;
			myCompartmentName = theCompartmentName;
			return this;
		}

		@Override
		public IQuery withProfile(String theProfileUri) {
			Validate.notBlank(theProfileUri, "theProfileUri must not be null or empty");
			myProfiles.add(Collections.singletonList(theProfileUri));
			return this;
		}

		@Override
		public IQuery withSecurity(String theSystem, String theCode) {
			Validate.notBlank(theCode, "theCode must not be null or empty");
			mySecurity.add(new TokenParam(theSystem, theCode));
			return this;
		}

		@Override
		public IQuery withTag(String theSystem, String theCode) {
			Validate.notBlank(theCode, "theCode must not be null or empty");
			myTags.add(new TokenParam(theSystem, theCode));
			return this;
		}

	}

	private final class StringResponseHandler implements IClientResponseHandler<String> {

		@Override
		public String invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders)
			throws IOException, BaseServerResponseException {
			return IOUtils.toString(theResponseInputStream, Charsets.UTF_8);
		}
	}

	private final class TransactionExecutable<T> extends BaseClientExecutable<ITransactionTyped<T>, T> implements ITransactionTyped<T> {

		private IBaseBundle myBaseBundle;
		private String myRawBundle;
		private EncodingEnum myRawBundleEncoding;
		private List<? extends IBaseResource> myResources;

		public TransactionExecutable(IBaseBundle theBundle) {
			myBaseBundle = theBundle;
		}

		public TransactionExecutable(List<? extends IBaseResource> theResources) {
			myResources = theResources;
		}

		public TransactionExecutable(String theBundle) {
			myRawBundle = theBundle;
			myRawBundleEncoding = EncodingEnum.detectEncodingNoDefault(myRawBundle);
			if (myRawBundleEncoding == null) {
				throw new IllegalArgumentException(Msg.code(1395) + myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
			}
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		@Override
		public T execute() {
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			if (myResources != null) {
				ResourceListResponseHandler binding = new ResourceListResponseHandler();
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myResources, myContext);
				return (T) invoke(params, binding, invocation);
			} else if (myBaseBundle != null) {
				ResourceResponseHandler binding = new ResourceResponseHandler(myBaseBundle.getClass(), getPreferResponseTypes());
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myBaseBundle, myContext);
				return (T) invoke(params, binding, invocation);
				// } else if (myRawBundle != null) {
			} else {
				StringResponseHandler binding = new StringResponseHandler();
				/*
				 * If the user has explicitly requested a given encoding, we may need to re-encode the raw string
				 */
				if (getParamEncoding() != null) {
					if (EncodingEnum.detectEncodingNoDefault(myRawBundle) != getParamEncoding()) {
						IBaseResource parsed = parseResourceBody(myRawBundle);
						myRawBundle = getParamEncoding().newParser(getFhirContext()).encodeResourceToString(parsed);
					}
				}
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myRawBundle, myContext);
				return (T) invoke(params, binding, invocation);
			}
		}

	}

	private final class TransactionInternal implements ITransaction {

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

			for (IBaseResource next : theResources) {
				String entryMethod = null;
				if (next instanceof IResource) {
					BundleEntryTransactionMethodEnum entryMethodEnum = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get((IResource) next);
					if (entryMethodEnum != null) {
						entryMethod = entryMethodEnum.getCode();
					}
				} else {
					entryMethod = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get((IAnyResource) next);
				}

				if (isBlank(entryMethod)) {
					if (isBlank(next.getIdElement().getValue())) {
						entryMethod = "POST";
					} else {
						entryMethod = "PUT";
					}
					if (next instanceof IResource) {
						ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put((IResource) next, BundleEntryTransactionMethodEnum.valueOf(entryMethod));
					} else {
						ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put((IAnyResource) next, entryMethod);
					}

				}

			}

			return new TransactionExecutable<>(theResources);
		}

	}

	private class UpdateInternal extends BaseSearch<IUpdateExecutable, IUpdateWithQueryTyped, MethodOutcome>
		implements IUpdate, IUpdateTyped, IUpdateExecutable, IUpdateWithQuery, IUpdateWithQueryTyped {

		private boolean myConditional;
		private IIdType myId;
		private PreferReturnEnum myPrefer;
		private IBaseResource myResource;
		private String myResourceBody;
		private String mySearchUrl;

		@Override
		public IUpdateWithQuery conditional() {
			myConditional = true;
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
			} else if (myConditional) {
				invocation = MethodUtil.createUpdateInvocation(myContext, myResource, myResourceBody, getParamMap());
			} else {
				if (myId == null) {
					myId = myResource.getIdElement();
				}

				if (myId == null || myId.hasIdPart() == false) {
					throw new InvalidRequestException(Msg.code(1396) + "No ID supplied for resource to update, can not invoke server");
				}
				invocation = MethodUtil.createUpdateInvocation(myResource, myResourceBody, myId, myContext);
			}

			addPreferHeader(myPrefer, invocation);

			OutcomeResponseHandler binding = new OutcomeResponseHandler(myPrefer);

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
		public IUpdateExecutable withId(IIdType theId) {
			if (theId == null) {
				throw new NullPointerException(Msg.code(1397) + "theId can not be null");
			}
			if (theId.hasIdPart() == false) {
				throw new NullPointerException(Msg.code(1398) + "theId must not be blank and must contain an ID, found: " + theId.getValue());
			}
			myId = theId;
			return this;
		}

		@Override
		public IUpdateExecutable withId(String theId) {
			if (theId == null) {
				throw new NullPointerException(Msg.code(1399) + "theId can not be null");
			}
			if (isBlank(theId)) {
				throw new NullPointerException(Msg.code(1400) + "theId must not be blank and must contain an ID, found: " + theId);
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

			EncodingEnum enc = EncodingEnum.detectEncodingNoDefault(theResourceRaw);
			if (enc == null) {
				throw new IllegalArgumentException(Msg.code(1401) + myContext.getLocalizer().getMessage(GenericClient.class, "cantDetermineRequestType"));
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

	@SuppressWarnings("rawtypes")
	private static class SortInternal implements ISort {

		private SortOrderEnum myDirection;
		private SearchInternal myFor;
		private String myParamName;
		private String myParamValue;

		public SortInternal(SearchInternal theFor) {
			myFor = theFor;
		}

		public SortInternal(SortSpec theSortSpec) {
			if (theSortSpec.getOrder() == null) {
				myParamName = Constants.PARAM_SORT;
			} else if (theSortSpec.getOrder() == SortOrderEnum.ASC) {
				myParamName = Constants.PARAM_SORT_ASC;
			} else if (theSortSpec.getOrder() == SortOrderEnum.DESC) {
				myParamName = Constants.PARAM_SORT_DESC;
			}
			myDirection = theSortSpec.getOrder();
			myParamValue = theSortSpec.getParamName();
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
		public IQuery defaultOrder(String theParam) {
			myParamName = Constants.PARAM_SORT;
			myDirection = null;
			myParamValue = theParam;
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
					throw new IllegalArgumentException(Msg.code(1402) + "Conditional URL must be in the format \"[ResourceType]?[Params]\" and must not have a base URL - Found: " + theSearchUrl);
				}
				b.append(nextChar);
			} else {
				switch (nextChar) {
					case '|':
					case '?':
					case '$':
					case ':':
						b.append(UrlUtil.escapeUrlParam(Character.toString(nextChar)));
						break;
					default:
						b.append(nextChar);
						break;
				}
			}
		}
		return b.toString();
	}

}
