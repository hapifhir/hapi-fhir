package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * @author James Agnew
 * @author Doug Martin (Regenstrief Center for Biomedical Informatics)
 */
abstract class BaseHttpClientInvocationWithContents extends BaseHttpClientInvocation {

	private final Bundle myBundle;
	private final BundleTypeEnum myBundleType;
	private final String myContents;
	private boolean myContentsIsBundle;
	private final FhirContext myContext;
	private Map<String, List<String>> myIfNoneExistParams;
	private String myIfNoneExistString;
	private boolean myOmitResourceId = false;
	private Map<String, List<String>> myParams;
	private final IBaseResource myResource;
	private final List<? extends IBaseResource> myResources;
	private final TagList myTagList;
	private final String myUrlPath;

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Bundle theBundle) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlPath = null;
		myResources = null;
		myBundle = theBundle;
		myContents = null;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IBaseResource theResource, Map<String, List<String>> theParams, String... theUrlPath) {
		myContext = theContext;
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
		super();
		myContext = theContext;
		myResource = theResource;
		myUrlPath = theUrlPath;
		myTagList = null;
		myResources = null;
		myBundle = null;
		myContents = null;
		myBundleType = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, List<? extends IBaseResource> theResources, BundleTypeEnum theBundleType) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlPath = null;
		myResources = theResources;
		myBundle = null;
		myContents = null;
		myBundleType = theBundleType;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Map<String, List<String>> theParams, String... theUrlPath) {
		myContext = theContext;
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
		myContext = theContext;
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
		myContext = theContext;
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
		super();
		if (theTagList == null) {
			throw new NullPointerException("Tag list must not be null");
		}

		myResource = null;
		myContext = theContext;
		myTagList = theTagList;
		myResources = null;
		myBundle = null;
		myContents = null;
		myBundleType = null;

		myUrlPath = StringUtils.join(theUrlPath, '/');
	}

	private void addMatchHeaders(HttpRequestBase theHttpRequest, StringBuilder theUrlBase) {
		if (myIfNoneExistParams != null) {
			StringBuilder b = newHeaderBuilder(theUrlBase);
			appendExtraParamsWithQuestionMark(myIfNoneExistParams, b, b.indexOf("?") == -1);
			theHttpRequest.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}

		if (myIfNoneExistString != null) {
			StringBuilder b = newHeaderBuilder(theUrlBase);
			b.append(b.indexOf("?") == -1 ? '?' : '&');
			b.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
			theHttpRequest.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding, Boolean thePrettyPrint) throws DataFormatException {
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

		if (myResource != null && IBaseBinary.class.isAssignableFrom(myResource.getClass())) {
			IBaseBinary binary = (IBaseBinary) myResource;
			
			/*
			 * Note: Be careful about changing which constructor we use for ByteArrayEntity,
			 * as Android's version of HTTPClient doesn't support the newer ones for
			 * whatever reason.
			 */
			ByteArrayEntity entity = new ByteArrayEntity(binary.getContent());
			entity.setContentType(binary.getContentType());
			HttpRequestBase retVal = createRequest(url, entity);
			addMatchHeaders(retVal, url);
			return retVal;
		}

		IParser parser;
		String contentType;
		EncodingEnum encoding = null;
		encoding = theEncoding;

		if (myContents != null) {
			encoding = MethodUtil.detectEncoding(myContents);
		}

		if (encoding == EncodingEnum.JSON) {
			parser = myContext.newJsonParser();
		} else {
			encoding = EncodingEnum.XML;
			parser = myContext.newXmlParser();
		}
		
		if (thePrettyPrint != null) {
			parser.setPrettyPrint(thePrettyPrint);
		}
		
		parser.setOmitResourceId(myOmitResourceId);

		AbstractHttpEntity entity;
		if (myParams != null) {
			contentType = null;
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Entry<String, List<String>> nextParam : myParams.entrySet()) {
				List<String> value = nextParam.getValue();
				for (String s : value) {
					parameters.add(new BasicNameValuePair(nextParam.getKey(), s));
				}
			}
			try {
				entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new InternalErrorException("Server does not support UTF-8 (should not happen)", e);
			}
		} else {
			String contents;
			if (myTagList != null) {
				contents = parser.encodeTagListToString(myTagList);
				contentType = encoding.getResourceContentType();
			} else if (myBundle != null) {
				contents = parser.encodeBundleToString(myBundle);
				contentType = encoding.getBundleContentType();
			} else if (myResources != null) {
				IVersionSpecificBundleFactory bundleFactory = myContext.newBundleFactory();
				bundleFactory.initializeBundleFromResourceList("", myResources, "", "", myResources.size(), myBundleType);
				Bundle bundle = bundleFactory.getDstu1Bundle();
				if (bundle != null) {
					contents = parser.encodeBundleToString(bundle);
					contentType = encoding.getBundleContentType();
				} else {
					IBaseResource bundleRes = bundleFactory.getResourceBundle();
					contents = parser.encodeResourceToString(bundleRes);
					contentType = encoding.getResourceContentType();
				}
			} else if (myContents != null) {
				contents = myContents;
				if (myContentsIsBundle && myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
					contentType = encoding.getBundleContentType();
				} else {
					contentType = encoding.getResourceContentType();
				}
			} else {
				contents = parser.encodeResourceToString(myResource);
				contentType = encoding.getResourceContentType();
			}
			
			/*
			 * We aren't using a StringEntity here because the constructors supported by
			 * Android aren't available in non-Android, and vice versa. Since we add the
			 * content type header manually, it makes no difference which one
			 * we use anyhow.
			 */
			entity = new ByteArrayEntity(contents.getBytes(Constants.CHARSET_UTF8));
		}

		HttpRequestBase retVal = createRequest(url, entity);
		super.addHeadersToRequest(retVal);
		addMatchHeaders(retVal, url);

		if (contentType != null) {
			retVal.addHeader(Constants.HEADER_CONTENT_TYPE, contentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		}

		return retVal;
	}

	protected abstract HttpRequestBase createRequest(StringBuilder theUrl, AbstractHttpEntity theEntity);

	private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
			b.deleteCharAt(b.length() - 1);
		}
		return b;
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
