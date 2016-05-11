package ca.uhn.fhir.rest.client.apache;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.HttpClientUtil;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.VersionUtil;

/**
 * A Http Client based on Apache. This is an adapter around the class
 * {@link org.apache.http.client.HttpClient HttpClient}
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpClient implements IHttpClient {

	private HttpClient myClient;
	private List<Header> myHeaders;
	private StringBuilder myUrl;
	private Map<String, List<String>> myIfNoneExistParams;
	private String myIfNoneExistString;
	private RequestTypeEnum myRequestType;

	public ApacheHttpClient(HttpClient theClient, StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
		this.myClient = theClient;
		this.myUrl = theUrl;
		this.myIfNoneExistParams = theIfNoneExistParams;
		this.myIfNoneExistString = theIfNoneExistString;
		this.myRequestType = theRequestType;
		this.myHeaders = theHeaders;
	}

	@Override
	public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
		/*
		 * We aren't using a StringEntity here because the constructors
		 * supported by Android aren't available in non-Android, and vice versa.
		 * Since we add the content type header manually, it makes no difference
		 * which one we use anyhow.
		 */
		ByteArrayEntity entity = new ByteArrayEntity(theContents.getBytes(Constants.CHARSET_UTF8));
		ApacheHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, theEncoding, theContext);
		retVal.addHeader(Constants.HEADER_CONTENT_TYPE, theContentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		return retVal;
	}

	@Override
	public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Entry<String, List<String>> nextParam : theParams.entrySet()) {
			List<String> value = nextParam.getValue();
			for (String s : value) {
				parameters.add(new BasicNameValuePair(nextParam.getKey(), s));
			}
		}
		UrlEncodedFormEntity entity = createFormEntity(parameters);
		ApacheHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, theEncoding, theContext);
		return retVal;
	}

	@CoverageIgnore
	private UrlEncodedFormEntity createFormEntity(List<NameValuePair> parameters) {
		try {
			return new UrlEncodedFormEntity(parameters, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException("Server does not support UTF-8 (should not happen)", e);
		}
	}

	@Override
	public IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary) {
		/*
		 * Note: Be careful about changing which constructor we use for
		 * ByteArrayEntity, as Android's version of HTTPClient doesn't support
		 * the newer ones for whatever reason.
		 */
		ByteArrayEntity entity = new ByteArrayEntity(theBinary.getContent());
		ApacheHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, null, theContext);
		retVal.addHeader(Constants.HEADER_CONTENT_TYPE, theBinary.getContentType());
		return retVal;
	}

	@Override
	public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
		ApacheHttpRequest retVal = createHttpRequest(null);
		addHeadersToRequest(retVal, theEncoding, theContext);
		return retVal;
	}

	public void addHeadersToRequest(ApacheHttpRequest theHttpRequest, EncodingEnum theEncoding, FhirContext theContext) {
		if (myHeaders != null) {
			for (Header next : myHeaders) {
				theHttpRequest.addHeader(next.getName(), next.getValue());
			}
		}

		theHttpRequest.addHeader("User-Agent", HttpClientUtil.createUserAgentString(theContext, "apache"));
		theHttpRequest.addHeader("Accept-Charset", "utf-8");
		theHttpRequest.addHeader("Accept-Encoding", "gzip");

		if (theEncoding == null) {
			theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON);
		} else if (theEncoding == EncodingEnum.JSON) {
			theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);
		} else if (theEncoding == EncodingEnum.XML) {
			theHttpRequest.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML);
		}
	}

	private ApacheHttpRequest createHttpRequest(HttpEntity theEntity) {
		HttpRequestBase request = constructRequestBase(theEntity);
		ApacheHttpRequest result = new ApacheHttpRequest(myClient, request);
		addHeaderIfNoneExist(result);
		return result;
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

	private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
			b.deleteCharAt(b.length() - 1);
		}
		return b;
	}

	private HttpRequestBase constructRequestBase(HttpEntity theEntity) {
		String url = myUrl.toString();
		switch (myRequestType) {
		case DELETE:
			return new HttpDelete(url);
		case OPTIONS:
			return new HttpOptions(url);
		case POST:
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(theEntity);
			return httpPost;
		case PUT:
			HttpPut httpPut = new HttpPut(url);
			httpPut.setEntity(theEntity);
			return httpPut;
		case GET:
		default:
			return new HttpGet(url);
		}
	}

}
