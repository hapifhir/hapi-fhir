package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
abstract class BaseHttpClientInvocationWithContents extends BaseHttpClientInvocation {

	private final Bundle myBundle;
	private final BundleTypeEnum myBundleType;
	private final String myContents;
	private boolean myContentsIsBundle;
	private Map<String, List<String>> myIfNoneExistParams;
	private String myIfNoneExistString;
	private boolean myOmitResourceId = false;
	private Map<String, List<String>> myParams;
	private final IBaseResource myResource;
	private final List<? extends IBaseResource> myResources;
	private final TagList myTagList;
	private final String myUrlPath;

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Bundle theBundle) {
		super(theContext);
		myResource = null;
		myTagList = null;
		myUrlPath = null;
		myResources = null;
		myBundle = theBundle;
		myContents = null;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IBaseResource theResource, Map<String, List<String>> theParams, String... theUrlPath) {
		super(theContext);
		myResource = theResource;
		myTagList = null;
		myUrlPath = StringUtils.join(theUrlPath, '/');
		myResources = null;
		myBundle = null;
		myContents = null;
		myContentsIsBundle = false;
		myParams = theParams;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IBaseResource theResource, String theUrlPath) {
		super(theContext);
		myResource = theResource;
		myUrlPath = theUrlPath;
		myTagList = null;
		myResources = null;
		myBundle = null;
		myContents = null;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, List<? extends IBaseResource> theResources, BundleTypeEnum theBundleType) {
		super(theContext);
		myResource = null;
		myTagList = null;
		myUrlPath = null;
		myResources = theResources;
		myBundle = null;
		myContents = null;
		myBundleType = theBundleType;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Map<String, List<String>> theParams, String... theUrlPath) {
		super(theContext);
		myResource = null;
		myTagList = null;
		myUrlPath = StringUtils.join(theUrlPath, '/');
		myResources = null;
		myBundle = null;
		myContents = null;
		myContentsIsBundle = false;
		myParams = theParams;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, String theContents, boolean theIsBundle, String theUrlPath) {
		super(theContext);
		myResource = null;
		myTagList = null;
		myUrlPath = theUrlPath;
		myResources = null;
		myBundle = null;
		myContents = theContents;
		myContentsIsBundle = theIsBundle;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, String theContents, Map<String, List<String>> theParams, String... theUrlPath) {
		super(theContext);
		myResource = null;
		myTagList = null;
		myUrlPath = StringUtils.join(theUrlPath, '/');
		myResources = null;
		myBundle = null;
		myContents = theContents;
		myContentsIsBundle = false;
		myParams = theParams;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, TagList theTagList, String... theUrlPath) {
		super(theContext);
		if (theTagList == null) {
			throw new NullPointerException("Tag list must not be null");
		}

		myResource = null;
		myTagList = theTagList;
		myResources = null;
		myBundle = null;
		myContents = null;
		myBundleType = null;

		myUrlPath = StringUtils.join(theUrlPath, '/');
	}

	@Override
	public IHttpRequest asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint) throws DataFormatException {
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
		IHttpClient httpClient = getRestfulClientFactory().getHttpClient(url, myIfNoneExistParams, myIfNoneExistString, getRequestType(), getHeaders());

		if (myResource != null && IBaseBinary.class.isAssignableFrom(myResource.getClass())) {
			IBaseBinary binary = (IBaseBinary) myResource;
			if (isNotBlank(binary.getContentType()) && EncodingEnum.forContentTypeStrict(binary.getContentType()) == null) {
				return httpClient.createBinaryRequest(getContext(), binary);
			}
		}

		EncodingEnum encoding = theEncoding;
		if (myContents != null) {
			encoding = MethodUtil.detectEncoding(myContents);
		}


		if (myParams != null) {
			return httpClient.createParamRequest(getContext(), myParams, encoding);
		} else {
			if (encoding == null) {
				encoding = EncodingEnum.XML;
			}
			String contents = parseContents(thePrettyPrint, encoding);
			String contentType = getContentType(encoding);
			return httpClient.createByteRequest(getContext(), contents, contentType, encoding);
		}
	}

	private String getContentType(EncodingEnum encoding) {
		if (myBundle != null || (getContext().getVersion().getVersion() == FhirVersionEnum.DSTU1 && ((myContents != null && myContentsIsBundle) || myResources != null))) {
			return encoding.getBundleContentType();
		} else {
			return encoding.getResourceContentType();
		}
	}

	private String parseContents(Boolean thePrettyPrint, EncodingEnum encoding) {
		IParser parser;

		if (encoding == EncodingEnum.JSON) {
			parser = getContext().newJsonParser();
		} else {
			parser = getContext().newXmlParser();
		}

		if (thePrettyPrint != null) {
			parser.setPrettyPrint(thePrettyPrint);
		}

		parser.setOmitResourceId(myOmitResourceId);
		if (myTagList != null) {
			return parser.encodeTagListToString(myTagList);
		} else if (myBundle != null) {
			return parser.encodeBundleToString(myBundle);
		} else if (myResources != null) {
			IVersionSpecificBundleFactory bundleFactory = getContext().newBundleFactory();
			bundleFactory.initializeBundleFromResourceList("", myResources, "", "", myResources.size(), myBundleType);
			Bundle bundle = bundleFactory.getDstu1Bundle();
			if (bundle != null) {
				return parser.encodeBundleToString(bundle);
			} else {
				IBaseResource bundleRes = bundleFactory.getResourceBundle();
				return parser.encodeResourceToString(bundleRes);
			}
		} else if (myContents != null) {
			return myContents;
		} else {
			return parser.encodeResourceToString(myResource);
		}
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

	/**
	 * Get the HTTP request type.
	 */
	protected abstract RequestTypeEnum getRequestType();

}
