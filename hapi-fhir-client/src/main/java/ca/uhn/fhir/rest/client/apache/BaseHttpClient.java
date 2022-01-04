package ca.uhn.fhir.rest.client.apache;

/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.HttpClientUtil;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.method.MethodUtil;

public abstract class BaseHttpClient implements IHttpClient {

	private final List<Header> myHeaders;
	private final Map<String, List<String>> myIfNoneExistParams;
	private final String myIfNoneExistString;
	protected final RequestTypeEnum myRequestType;
	protected final StringBuilder myUrl;

	/**
	 * Constructor
	 */
	public BaseHttpClient(StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
		this.myUrl = theUrl;
		this.myIfNoneExistParams = theIfNoneExistParams;
		this.myIfNoneExistString = theIfNoneExistString;
		this.myRequestType = theRequestType;
		this.myHeaders = theHeaders;
	}

	private void addHeaderIfNoneExist(IHttpRequest result) {
		if (myIfNoneExistParams != null) {
			StringBuilder b = newHeaderBuilder(myUrl);
			BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(myIfNoneExistParams, b, b.indexOf("?") == -1);
			result.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}

		if (myIfNoneExistString != null) {
			StringBuilder b = newHeaderBuilder(myUrl);
			b.append(b.indexOf("?") == -1 ? '?' : '&');
			b.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
			result.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}
	}

	public void addHeadersToRequest(IHttpRequest theHttpRequest, EncodingEnum theEncoding, FhirContext theContext) {
		if (myHeaders != null) {
			for (Header next : myHeaders) {
				theHttpRequest.addHeader(next.getName(), next.getValue());
			}
		}

		theHttpRequest.addHeader("User-Agent", HttpClientUtil.createUserAgentString(theContext, "apache"));
		theHttpRequest.addHeader("Accept-Charset", "utf-8");
		theHttpRequest.addHeader("Accept-Encoding", "gzip");

		addHeaderIfNoneExist(theHttpRequest);

		MethodUtil.addAcceptHeaderToRequest(theEncoding, theHttpRequest, theContext);
	}

	@Override
	public IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary) {
		byte[] content = theBinary.getContent();
		IHttpRequest retVal = createHttpRequest(content);
		addHeadersToRequest(retVal, null, theContext);
		retVal.addHeader(Constants.HEADER_CONTENT_TYPE, theBinary.getContentType());
		return retVal;
	}

	@Override
	public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
		IHttpRequest retVal = createHttpRequest(theContents);
		addHeadersToRequest(retVal, theEncoding, theContext);
		retVal.addHeader(Constants.HEADER_CONTENT_TYPE, theContentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		return retVal;
	}

	@Override
	public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
		IHttpRequest retVal = createHttpRequest();
		addHeadersToRequest(retVal, theEncoding, theContext);
		return retVal;
	}

	protected abstract IHttpRequest createHttpRequest();

	protected abstract IHttpRequest createHttpRequest(byte[] theContent);

	protected abstract IHttpRequest createHttpRequest(Map<String, List<String>> theParams);

	protected abstract IHttpRequest createHttpRequest(String theContents);

	@Override
	public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
		IHttpRequest retVal = createHttpRequest(theParams);
		addHeadersToRequest(retVal, theEncoding, theContext);
		return retVal;
	}

	private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
			b.deleteCharAt(b.length() - 1);
		}
		return b;
	}
}
