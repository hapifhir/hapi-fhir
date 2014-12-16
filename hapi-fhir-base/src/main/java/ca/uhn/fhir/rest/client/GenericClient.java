package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICreate;
import ca.uhn.fhir.rest.gclient.ICreateTyped;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.ICriterionInternal;
import ca.uhn.fhir.rest.gclient.IDelete;
import ca.uhn.fhir.rest.gclient.IDeleteTyped;
import ca.uhn.fhir.rest.gclient.IGetPage;
import ca.uhn.fhir.rest.gclient.IGetPageTyped;
import ca.uhn.fhir.rest.gclient.IGetTags;
import ca.uhn.fhir.rest.gclient.IParam;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.ISort;
import ca.uhn.fhir.rest.gclient.ITransaction;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.IUpdate;
import ca.uhn.fhir.rest.gclient.IUpdateTyped;
import ca.uhn.fhir.rest.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.method.HistoryMethodBinding;
import ca.uhn.fhir.rest.method.HttpDeleteClientInvocation;
import ca.uhn.fhir.rest.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.method.HttpSimpleGetClientInvocation;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.method.TransactionMethodBinding;
import ca.uhn.fhir.rest.method.ValidateMethodBinding;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

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
	public BaseConformance conformance() {
		HttpGetClientInvocation invocation = MethodUtil.createConformanceInvocation();
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		@SuppressWarnings("unchecked")
		Class<BaseConformance> conformance = (Class<BaseConformance>) myContext.getResourceDefinition("Conformance").getImplementingClass();
		
		ResourceResponseHandler<? extends BaseConformance> binding = new ResourceResponseHandler<BaseConformance>(conformance, null);
		BaseConformance resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public ICreate create() {
		return new CreateInternal();
	}

	@Override
	public MethodOutcome create(IResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createCreateInvocation(theResource, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
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
	public MethodOutcome delete(final Class<? extends IResource> theType, IdDt theId) {
		HttpDeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(theId.withResourceType(toResourceName(theType)));
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		final String resourceName = myContext.getResourceDefinition(theType).getName();
		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public MethodOutcome delete(Class<? extends IResource> theType, String theId) {
		return delete(theType, new IdDt(theId));
	}

	public HttpRequestBase getLastRequest() {
		return myLastRequest;
	}

	protected String getPreferredId(IResource theResource, String theId) {
		if (isNotBlank(theId)) {
			return theId;
		}
		return theResource.getId().getIdPart();
	}

	@Override
	public IGetTags getTags() {
		return new GetTagsInternal();
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
		Bundle resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
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
	public IGetPage loadPage() {
		return new LoadPageInternal();
	}

	@Override
	public <T extends IResource> T read(final Class<T> theType, IdDt theId) {
		if (theId == null || theId.hasIdPart() == false) {
			throw new IllegalArgumentException("theId does not contain a valid ID, is: " + theId);
		}

		HttpGetClientInvocation invocation;
		if (theId.hasBaseUrl()) {
			invocation = ReadMethodBinding.createAbsoluteReadInvocation(theId.toVersionless());
		} else {
			invocation = ReadMethodBinding.createReadInvocation(theId, toResourceName(theType));
		}
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theType, theId);
		T resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IResource> T read(Class<T> theType, String theId) {
		return read(theType, new IdDt(theId));
	}

	@Override
	public <T extends IResource> T read(final Class<T> theType, UriDt theUrl) {
		return read(theType, new IdDt(theUrl));
	}

	@Override
	public IResource read(UriDt theUrl) {
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

		BaseHttpClientInvocation invocation = SearchMethodBinding.createSearchInvocation(myContext, toResourceName(theType), params, null, null, null);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		BundleResponseHandler binding = new BundleResponseHandler(theType);
		Bundle resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IResource> Bundle search(final Class<T> theType, UriDt theUrl) {
		BaseHttpClientInvocation invocation = new HttpGetClientInvocation(theUrl.getValueAsString());
		return invokeClient(myContext, new BundleResponseHandler(theType), invocation);
	}

	@Override
	public Bundle search(UriDt theUrl) {
		return search(inferResourceClass(theUrl), theUrl);
	}

	private Class<? extends IResource> inferResourceClass(UriDt theUrl) {
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

		throw new RuntimeException(myContext.getLocalizer().getMessage(I18N_CANNOT_DETEMINE_RESOURCE_TYPE, theUrl.getValueAsString()));

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
	public ITransaction transaction() {
		return new TransactionInternal();
	}

	@Override
	public List<IResource> transaction(List<IResource> theResources) {
		BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(theResources, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		Bundle resp = invokeClient(myContext, new BundleResponseHandler(null), invocation, myLogRequestAndResponse);

		return resp.toListOfResources();
	}

	@Override
	public IUpdate update() {
		return new UpdateInternal();
	}

	@Override
	public MethodOutcome update(IdDt theIdDt, IResource theResource) {
		BaseHttpClientInvocation invocation = MethodUtil.createUpdateInvocation(theResource, null, theIdDt, myContext);
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
		final String resourceName = def.getName();

		OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
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
		MethodOutcome resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	@Override
	public <T extends IResource> T vread(final Class<T> theType, IdDt theId) {
		String resName = toResourceName(theType);
		IdDt id = theId;
		if (!id.hasBaseUrl()) {
			id = new IdDt(resName, id.getIdPart(), id.getVersionIdPart());
		}

		if (id.hasVersionIdPart() == false) {
			throw new IllegalArgumentException(myContext.getLocalizer().getMessage(I18N_NO_VERSION_ID_FOR_VREAD, theId.getValue()));
		}

		HttpGetClientInvocation invocation;
		if (id.hasBaseUrl()) {
			invocation = ReadMethodBinding.createAbsoluteVReadInvocation(id);
		} else {
			invocation = ReadMethodBinding.createVReadInvocation(id);
		}
		if (isKeepResponses()) {
			myLastRequest = invocation.asHttpRequest(getServerBase(), createExtraParams(), getEncoding());
		}

		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theType, id);
		T resp = invokeClient(myContext, binding, invocation, myLogRequestAndResponse);
		return resp;
	}

	/* also deprecated in interface */
	@Deprecated
	@Override
	public <T extends IResource> T vread(final Class<T> theType, IdDt theId, IdDt theVersionId) {
		return vread(theType, theId.withVersion(theVersionId.getIdPart()));
	}

	@Override
	public <T extends IResource> T vread(Class<T> theType, String theId, String theVersionId) {
		IdDt resId = new IdDt(toResourceName(theType), theId, theVersionId);
		return vread(theType, resId);
	}

	private abstract class BaseClientExecutable<T extends IClientExecutable<?, ?>, Y> implements IClientExecutable<T, Y> {
		private EncodingEnum myParamEncoding;
		private Boolean myPrettyPrint;
		private boolean myQueryLogRequestAndResponse;

		protected void addParam(Map<String, List<String>> params, String parameterName, String parameterValue) {
			if (!params.containsKey(parameterName)) {
				params.put(parameterName, new ArrayList<String>());
			}
			params.get(parameterName).add(parameterValue);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T andLogRequestAndResponse(boolean theLogRequestAndResponse) {
			myQueryLogRequestAndResponse = theLogRequestAndResponse;
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

		protected <Z> Z invoke(Map<String, List<String>> theParams, IClientResponseHandler<Z> theHandler, BaseHttpClientInvocation theInvocation) {
			// if (myParamEncoding != null) {
			// theParams.put(Constants.PARAM_FORMAT, Collections.singletonList(myParamEncoding.getFormatContentType()));
			// }
			//
			// if (myPrettyPrint != null) {
			// theParams.put(Constants.PARAM_PRETTY, Collections.singletonList(myPrettyPrint.toString()));
			// }

			if (isKeepResponses()) {
				myLastRequest = theInvocation.asHttpRequest(getServerBase(), theParams, getEncoding());
			}

			Z resp = invokeClient(myContext, theHandler, theInvocation, myParamEncoding, myPrettyPrint, myQueryLogRequestAndResponse || myLogRequestAndResponse);
			return resp;
		}

		protected EncodingEnum getParamEncoding() {
			return myParamEncoding;
		}

		protected IResource parseResourceBody(String theResourceBody) {
			EncodingEnum encoding = null;
			for (int i = 0; i < theResourceBody.length() && encoding == null; i++) {
				switch (theResourceBody.charAt(i)) {
				case '<':
					encoding = EncodingEnum.XML;
					break;
				case '{':
					encoding = EncodingEnum.JSON;
					break;
				}
			}
			if (encoding == null) {
				throw new InvalidRequestException("FHIR client can't determine resource encoding");
			}
			return encoding.newParser(myContext).parseResource(theResourceBody);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T prettyPrint() {
			myPrettyPrint = true;
			return (T) this;
		}

	}

	private final class BundleResponseHandler implements IClientResponseHandler<Bundle> {

		private Class<? extends IResource> myType;

		public BundleResponseHandler(Class<? extends IResource> theType) {
			myType = theType;
		}

		@Override
		public Bundle invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
				BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			return parser.parseBundle(myType, theResponseReader);
		}
	}

	private class CreateInternal extends BaseClientExecutable<ICreateTyped, MethodOutcome> implements ICreate, ICreateTyped {

		private String myId;
		private IResource myResource;
		private String myResourceBody;

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
			
			BaseHttpClientInvocation invocation = MethodUtil.createCreateInvocation(myResource, myResourceBody, myId, myContext);

			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResource);
			final String resourceName = def.getName();

			OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);

			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);

		}

		@Override
		public ICreateTyped resource(IResource theResource) {
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

	private class DeleteInternal extends BaseClientExecutable<IDeleteTyped, BaseOperationOutcome> implements IDelete, IDeleteTyped {

		private IdDt myId;

		@Override
		public BaseOperationOutcome execute() {
			HttpDeleteClientInvocation invocation = DeleteMethodBinding.createDeleteInvocation(myId);
			OperationOutcomeResponseHandler binding = new OperationOutcomeResponseHandler();
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);
		}

		@Override
		public IDeleteTyped resource(IResource theResource) {
			Validate.notNull(theResource, "theResource can not be null");
			IdDt id = theResource.getId();
			Validate.notNull(id, "theResource.getId() can not be null");
			if (id.hasResourceType() == false || id.hasIdPart() == false) {
				throw new IllegalArgumentException("theResource.getId() must contain a resource type and logical ID at a minimum (e.g. Patient/1234), found: " + id.getValue());
			}
			myId = id;
			return this;
		}

		@Override
		public IDeleteTyped resourceById(IdDt theId) {
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
	}

	private class GetPageInternal extends BaseClientExecutable<IGetPageTyped, Bundle> implements IGetPageTyped {

		private String myUrl;

		public GetPageInternal(String theUrl) {
			myUrl = theUrl;
		}

		@Override
		public Bundle execute() {

			BundleResponseHandler binding = new BundleResponseHandler(null);
			HttpSimpleGetClientInvocation invocation = new HttpSimpleGetClientInvocation(myUrl);

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

			HttpGetClientInvocation invocation = new HttpGetClientInvocation(params, urlFragments);

			return invoke(params, binding, invocation);

		}

		@Override
		public IGetTags forResource(Class<? extends IResource> theClass) {
			setResourceClass(theClass);
			return this;
		}

		@Override
		public IGetTags forResource(Class<? extends IResource> theClass, String theId) {
			setResourceClass(theClass);
			myId = theId;
			return this;
		}

		@Override
		public IGetTags forResource(Class<? extends IResource> theClass, String theId, String theVersionId) {
			setResourceClass(theClass);
			myId = theId;
			myVersionId = theVersionId;
			return this;
		}

		private void setResourceClass(Class<? extends IResource> theClass) {
			if (theClass != null) {
				myResourceName = myContext.getResourceDefinition(theClass).getName();
			} else {
				myResourceName = null;
			}
		}

	}

	private final class LoadPageInternal implements IGetPage {

		@Override
		public IGetPageTyped next(Bundle theBundle) {
			return new GetPageInternal(theBundle.getLinkNext().getValue());
		}

		@Override
		public IGetPageTyped previous(Bundle theBundle) {
			return new GetPageInternal(theBundle.getLinkPrevious().getValue());
		}

		@Override
		public IGetPageTyped url(String thePageUrl) {
			return new GetPageInternal(thePageUrl);
		}

	}

	private final class OperationOutcomeResponseHandler implements IClientResponseHandler<BaseOperationOutcome> {

		@Override
		public BaseOperationOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
				BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				return null;
			}
			IParser parser = respType.newParser(myContext);
			BaseOperationOutcome retVal;
			try {
				// TODO: handle if something else than OO comes back
				retVal = (BaseOperationOutcome) parser.parseResource(theResponseReader);
			} catch (DataFormatException e) {
				ourLog.warn("Failed to parse OperationOutcome response", e);
				return null;
			}
			MethodUtil.parseClientRequestResourceHeaders(theHeaders, retVal);

			return retVal;
		}
	}

	private final class OutcomeResponseHandler implements IClientResponseHandler<MethodOutcome> {
		private final String myResourceName;

		private OutcomeResponseHandler(String theResourceName) {
			myResourceName = theResourceName;
		}

		@Override
		public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
				BaseServerResponseException {
			MethodOutcome response = MethodUtil.process2xxResponse(myContext, myResourceName, theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			if (theResponseStatusCode == Constants.STATUS_HTTP_201_CREATED) {
				response.setCreated(true);
			}
			return response;
		}
	}

	private final class ResourceListResponseHandler implements IClientResponseHandler<List<IResource>> {

		private Class<? extends IResource> myType;

		public ResourceListResponseHandler(Class<? extends IResource> theType) {
			myType = theType;
		}

		@Override
		public List<IResource> invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
				BaseServerResponseException {
			return new BundleResponseHandler(myType).invokeClient(theResponseMimeType, theResponseReader, theResponseStatusCode, theHeaders).toListOfResources();
		}
	}

	private final class ResourceResponseHandler<T extends IResource> implements IClientResponseHandler<T> {

		private IdDt myId;
		private Class<T> myType;

		public ResourceResponseHandler(Class<T> theType, IdDt theId) {
			myType = theType;
			myId = theId;
		}

		@Override
		public T invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			T retVal = parser.parseResource(myType, theResponseReader);

			if (myId != null) {
				retVal.setId(myId);
			}

			MethodUtil.parseClientRequestResourceHeaders(theHeaders, retVal);

			return retVal;
		}
	}

	private class SearchInternal extends BaseClientExecutable<IQuery, Bundle> implements IQuery, IUntypedQuery {

		private String myCompartmentName;
		private List<ICriterionInternal> myCriterion = new ArrayList<ICriterionInternal>();
		private List<Include> myInclude = new ArrayList<Include>();
		private Integer myParamLimit;
		private String myResourceId;
		private String myResourceName;
		private Class<? extends IResource> myResourceType;
		private SearchStyleEnum mySearchStyle;
		private List<SortInternal> mySort = new ArrayList<SortInternal>();

		public SearchInternal() {
			myResourceType = null;
			myResourceName = null;
		}

		@Override
		public IQuery and(ICriterion<?> theCriterion) {
			myCriterion.add((ICriterionInternal) theCriterion);
			return this;
		}

		@Override
		public Bundle execute() {

			Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
			// Map<String, List<String>> initial = createExtraParams();
			// if (initial != null) {
			// params.putAll(initial);
			// }

			for (ICriterionInternal next : myCriterion) {
				String parameterName = next.getParameterName();
				String parameterValue = next.getParameterValue();
				addParam(params, parameterName, parameterValue);
			}

			for (Include next : myInclude) {
				addParam(params, Constants.PARAM_INCLUDE, next.getValue());
			}

			for (SortInternal next : mySort) {
				addParam(params, next.getParamName(), next.getParamValue());
			}

			if (myParamLimit != null) {
				addParam(params, Constants.PARAM_COUNT, Integer.toString(myParamLimit));
			}

			BundleResponseHandler binding = new BundleResponseHandler(myResourceType);

			IdDt resourceId = myResourceId != null ? new IdDt(myResourceId) : null;

			BaseHttpClientInvocation invocation = SearchMethodBinding.createSearchInvocation(myContext, myResourceName, params, resourceId, myCompartmentName, mySearchStyle);

			return invoke(params, binding, invocation);

		}

		@Override
		public IQuery forAllResources() {
			return this;
		}

		@Override
		public IQuery forResource(Class<? extends IResource> theResourceType) {
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
		public IQuery limitTo(int theLimitTo) {
			if (theLimitTo > 0) {
				myParamLimit = theLimitTo;
			} else {
				myParamLimit = null;
			}
			return this;
		}

		private void setType(Class<? extends IResource> theResourceType) {
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

	}

	private static class SortInternal implements ISort {

		private SearchInternal myFor;
		private String myParamName;
		private String myParamValue;

		public SortInternal(SearchInternal theFor) {
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

	private final class TagListResponseHandler implements IClientResponseHandler<TagList> {

		@Override
		public TagList invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
				BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(myContext);
			return parser.parseTagList(theResponseReader);
		}
	}

	private final class TransactionExecutable<T> extends BaseClientExecutable<ITransactionTyped<T>, T> implements ITransactionTyped<T> {

		private Bundle myBundle;
		private List<IResource> myResources;

		public TransactionExecutable(Bundle theResources) {
			myBundle = theResources;
		}

		public TransactionExecutable(List<IResource> theResources) {
			myResources = theResources;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T execute() {
			if (myResources != null) {
				ResourceListResponseHandler binding = new ResourceListResponseHandler(null);
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myResources, myContext);
				Map<String, List<String>> params = new HashMap<String, List<String>>();
				return (T) invoke(params, binding, invocation);
			} else {
				BundleResponseHandler binding = new BundleResponseHandler(null);
				BaseHttpClientInvocation invocation = TransactionMethodBinding.createTransactionInvocation(myBundle, myContext);
				Map<String, List<String>> params = new HashMap<String, List<String>>();
				return (T) invoke(params, binding, invocation);
			}
		}

	}

	private final class TransactionInternal implements ITransaction {

		@Override
		public ITransactionTyped<Bundle> withBundle(Bundle theResources) {
			return new TransactionExecutable<Bundle>(theResources);
		}

		@Override
		public ITransactionTyped<List<IResource>> withResources(List<IResource> theResources) {
			return new TransactionExecutable<List<IResource>>(theResources);
		}

	}

	private class UpdateInternal extends BaseClientExecutable<IUpdateTyped, MethodOutcome> implements IUpdate, IUpdateTyped {

		private IdDt myId;
		private IResource myResource;
		private String myResourceBody;

		@Override
		public MethodOutcome execute() {
			if (myResource == null) {
				myResource = parseResourceBody(myResourceBody);
			}
			if (myId == null) {
				myId = myResource.getId();
			}
			if (myId == null || myId.hasIdPart() == false) {
				throw new InvalidRequestException("No ID supplied for resource to update, can not invoke server");
			}

			// If an explicit encoding is chosen, we will re-serialize to ensure the right encoding
			if (getParamEncoding() != null) {
				myResourceBody = null;
			}			

			BaseHttpClientInvocation invocation = MethodUtil.createUpdateInvocation(myResource, myResourceBody, myId, myContext);

			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResource);
			final String resourceName = def.getName();

			OutcomeResponseHandler binding = new OutcomeResponseHandler(resourceName);

			Map<String, List<String>> params = new HashMap<String, List<String>>();
			return invoke(params, binding, invocation);

		}

		@Override
		public IUpdateTyped resource(IResource theResource) {
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
		public IUpdateTyped withId(IdDt theId) {
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
		public IUpdateTyped withId(String theId) {
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

}
