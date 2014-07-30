package ca.uhn.fhir.rest.method;

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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

abstract class BaseHttpClientInvocationWithContents extends BaseHttpClientInvocation {

	private final FhirContext myContext;
	private final IResource myResource;
	private final String myUrlExtension;
	private final TagList myTagList;
	private final List<IResource> myResources;
	private final Bundle myBundle;
	private final String myContents;
	private boolean myContentsIsBundle;
	private Map<String, List<String>> myParams;

	public BaseHttpClientInvocationWithContents(FhirContext theContext, IResource theResource, String theUrlExtension) {
		super();
		myContext = theContext;
		myResource = theResource;
		myUrlExtension = theUrlExtension;
		myTagList = null;
		myResources = null;
		myBundle = null;
		myContents = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, TagList theTagList, String... theUrlExtension) {
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

		myUrlExtension = StringUtils.join(theUrlExtension, '/');
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, List<IResource> theResources) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlExtension = null;
		myResources = theResources;
		myBundle = null;
		myContents = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Bundle theBundle) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlExtension = null;
		myResources = null;
		myBundle = theBundle;
		myContents = null;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, String theContents, boolean theIsBundle, String theUrlExtension) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlExtension = theUrlExtension;
		myResources = null;
		myBundle = null;
		myContents = theContents;
		myContentsIsBundle = theIsBundle;
	}

	public BaseHttpClientInvocationWithContents(FhirContext theContext, Map<String, List<String>> theParams, String... theUrlExtension) {
		myContext = theContext;
		myResource = null;
		myTagList = null;
		myUrlExtension = StringUtils.join(theUrlExtension, '/');
		myResources = null;
		myBundle = null;
		myContents = null;
		myContentsIsBundle = false;
		myParams = theParams;
	}

	@Override
	public HttpRequestBase asHttpRequest(String theUrlBase, Map<String, List<String>> theExtraParams, EncodingEnum theEncoding) throws DataFormatException {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (isNotBlank(myUrlExtension)) {
			if (!theUrlBase.endsWith("/")) {
				b.append('/');
			}
			b.append(myUrlExtension);
		}

		appendExtraParamsWithQuestionMark(theExtraParams, b, b.indexOf("?") == -1);
		String url = b.toString();

		if (myResource != null && Binary.class.isAssignableFrom(myResource.getClass())) {
			Binary binary = (Binary) myResource;
			ByteArrayEntity entity = new ByteArrayEntity(binary.getContent(), ContentType.parse(binary.getContentType()));
			HttpRequestBase retVal = createRequest(url, entity);
			return retVal;
		}

		IParser parser;
		String contentType;
		EncodingEnum encoding = null;
		encoding = theEncoding;

		if (encoding == EncodingEnum.JSON) {
			parser = myContext.newJsonParser();
		} else {
			encoding = EncodingEnum.XML;
			parser = myContext.newXmlParser();
		}

		AbstractHttpEntity entity;
		if (myParams != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Entry<String, List<String>> nextParam : myParams.entrySet()) {
				parameters.add(new BasicNameValuePair(nextParam.getKey(), StringUtils.join(nextParam.getValue(), ',')));
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
				Bundle bundle = RestfulServer.createBundleFromResourceList(myContext, "", myResources, "", "", myResources.size());
				contents = parser.encodeBundleToString(bundle);
				contentType = encoding.getBundleContentType();
			} else if (myContents != null) {
				contents = myContents;
				if (myContentsIsBundle) {
					contentType = encoding.getBundleContentType();
				} else {
					contentType = encoding.getResourceContentType();
				}
			} else {
				contents = parser.encodeResourceToString(myResource);
				contentType = encoding.getResourceContentType();
			}
			entity = new StringEntity(contents, ContentType.create(contentType, "UTF-8"));
		}

		HttpRequestBase retVal = createRequest(url, entity);
		super.addHeadersToRequest(retVal);

		// retVal.addHeader(Constants.HEADER_CONTENT_TYPE, con);

		return retVal;
	}

	protected abstract HttpRequestBase createRequest(String url, AbstractHttpEntity theEntity);

}
