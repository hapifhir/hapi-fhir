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
package ca.uhn.fhir.rest.client.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.model.AsHttpRequestParams;
import ca.uhn.fhir.rest.param.HttpClientRequestParameters;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
abstract class BaseHttpClientInvocationWithContents extends BaseHttpClientInvocation {

	private final BundleTypeEnum myBundleType;
	private final String myContents;
	private Map<String, List<String>> myIfNoneExistParams;
	private String myIfNoneExistString;
	private boolean myOmitResourceId = false;
	private Map<String, List<String>> myParams;
	private final IBaseResource myResource;
	private final List<IBaseResource> myResources;
	private final String myUrlPath;
	private IIdType myForceResourceId;

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IBaseResource theResource, String theUrlPath) {
		super(theContext);
		myResource = theResource;
		myUrlPath = theUrlPath;
		myResources = null;
		myContents = null;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(
			FhirContext theContext, List<? extends IBaseResource> theResources, BundleTypeEnum theBundleType) {
		super(theContext);
		myResource = null;
		myUrlPath = null;
		myResources = new ArrayList<>(theResources);
		myContents = null;
		myBundleType = theBundleType;
	}

	public BaseHttpClientInvocationWithContents(
			FhirContext theContext, Map<String, List<String>> theParams, String... theUrlPath) {
		super(theContext);
		myResource = null;
		myUrlPath = StringUtils.join(theUrlPath, '/');
		myResources = null;
		myContents = null;
		myParams = theParams;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(
			FhirContext theContext, String theContents, boolean theIsBundle, String theUrlPath) {
		super(theContext);
		myResource = null;
		myUrlPath = theUrlPath;
		myResources = null;
		myContents = theContents;
		myBundleType = null;
	}

	@Override
	public IHttpRequest asHttpRequest(
			String theUrlBase,
			Map<String, List<String>> theExtraParams,
			EncodingEnum theEncoding,
			Boolean thePrettyPrint)
			throws DataFormatException {
		return asHttpRequest(new AsHttpRequestParams()
				.setUrlBase(theUrlBase)
				.setExtraParams(theExtraParams)
				.setEncodingEnum(theEncoding)
				.setPrettyPrint(thePrettyPrint));
	}

	@Override
	public IHttpRequest asHttpRequest(AsHttpRequestParams theParams) {
		String theUrlBase = theParams.getUrlBase();
		Map<String, List<String>> theExtraParams = theParams.getExtraParams();
		EncodingEnum theEncoding = theParams.getEncodingEnum();
		Boolean thePrettyPrint = theParams.getPrettyPrint();

		StringBuilder url = new StringBuilder();

		if (myUrlPath == null) {
			url.append(theUrlBase);
		} else {
			if (!myUrlPath.contains("://")) {
				url.append(theUrlBase);
				if (!theUrlBase.endsWith("/")) {
					url.append('/');
				}
			}
			url.append(myUrlPath);
		}

		appendExtraParamsWithQuestionMark(theExtraParams, url, url.indexOf("?") == -1);
		IHttpClient httpClient;
		if (theParams.getClient() != null) {
			// use the provided one
			httpClient = theParams.getClient();
			// update the url to the one we want (in case the
			// previous client did not have the correct one
			httpClient.setNewUrl(url, myIfNoneExistString, myIfNoneExistParams);
		} else {
			// make a new one
			httpClient = getRestfulClientFactory()
					.getHttpClient(url, myIfNoneExistParams, myIfNoneExistString, getRequestType(), getHeaders());
		}

		if (myResource != null && IBaseBinary.class.isAssignableFrom(myResource.getClass())) {
			IBaseBinary binary = (IBaseBinary) myResource;
			if (isNotBlank(binary.getContentType())
					&& EncodingEnum.forContentTypeStrict(binary.getContentType()) == null) {
				if (binary.hasData()) {
					return httpClient.createBinaryRequest(getContext(), binary);
				}
			}
		}

		EncodingEnum encoding = theEncoding;
		if (myContents != null) {
			encoding = EncodingEnum.detectEncoding(myContents);
		}

		if (myParams != null) {
			IHttpRequest request = httpClient.createParamRequest(getContext(), myParams, encoding);
			return request;
		}
		encoding = ObjectUtils.defaultIfNull(encoding, EncodingEnum.JSON);
		String contents = encodeContents(thePrettyPrint, encoding);
		String contentType = getContentType(encoding);
		HttpClientRequestParameters parameters = new HttpClientRequestParameters(url.toString(), getRequestType());
		parameters.setContents(contents);
		parameters.setContentType(contentType);
		parameters.setFhirContext(getContext());
		parameters.setEncodingEnum(encoding);
		parameters.setRequestTypeEnum(getRequestType());
		IHttpRequest request = httpClient.createRequest(parameters);
		for (Header header : getHeaders()) {
			request.addHeader(header.getName(), header.getValue());
		}
		httpClient.addHeadersToRequest(request, encoding, parameters.getFhirContext());
		request.addHeader(Constants.HEADER_CONTENT_TYPE, contentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		return request;
	}

	private String getContentType(EncodingEnum encoding) {
		if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
			// application/xml+fhir
			return encoding.getResourceContentType();
		} else {
			// application/fhir+xml
			return encoding.getResourceContentTypeNonLegacy();
		}
	}

	/**
	 * Get the HTTP request type.
	 */
	protected abstract RequestTypeEnum getRequestType();

	private String encodeContents(Boolean thePrettyPrint, EncodingEnum encoding) {
		IParser parser;

		if (encoding == EncodingEnum.JSON) {
			parser = getContext().newJsonParser();
		} else {
			parser = getContext().newXmlParser();
		}

		if (thePrettyPrint != null) {
			parser.setPrettyPrint(thePrettyPrint);
		}

		if (myForceResourceId != null) {
			parser.setEncodeForceResourceId(myForceResourceId);
		}

		parser.setOmitResourceId(myOmitResourceId);
		if (myResources != null) {
			IVersionSpecificBundleFactory bundleFactory = getContext().newBundleFactory();
			bundleFactory.addTotalResultsToBundle(myResources.size(), myBundleType);
			bundleFactory.addResourcesToBundle(myResources, myBundleType, null, null, null);
			IBaseResource bundleRes = bundleFactory.getResourceBundle();
			return parser.encodeResourceToString(bundleRes);
		} else if (myContents != null) {
			return myContents;
		} else {
			return parser.encodeResourceToString(myResource);
		}
	}

	public void setForceResourceId(IIdType theId) {
		myForceResourceId = theId;
	}

	public void setIfNoneExistParams(Map<String, List<String>> theIfNoneExist) {
		myIfNoneExistParams = theIfNoneExist;
	}

	public void setIfNoneExistString(String theIfNoneExistString) {
		myIfNoneExistString = theIfNoneExistString;
	}

	public void setOmitResourceId(boolean theOmitResourceId) {
		myOmitResourceId = theOmitResourceId;
	}
}
