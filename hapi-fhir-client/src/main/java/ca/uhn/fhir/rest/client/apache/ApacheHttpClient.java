package ca.uhn.fhir.rest.client.apache;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * A Http Client based on Apache. This is an adapter around the class
 * {@link org.apache.http.client.HttpClient HttpClient}
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpClient extends BaseHttpClient implements IHttpClient {

	private final HttpClient myClient;

	public ApacheHttpClient(HttpClient theClient, StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
		super(theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
		this.myClient = theClient;
	}

	private HttpRequestBase constructRequestBase(HttpEntity theEntity) {
		String url = myUrl.toString();
		switch (myRequestType) {
		case DELETE:
			return new HttpDelete(url);
		case PATCH:
			HttpPatch httpPatch = new HttpPatch(url);
			httpPatch.setEntity(theEntity);
			return httpPatch;
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


	private UrlEncodedFormEntity createFormEntity(List<NameValuePair> parameters) {
		try {
			return new UrlEncodedFormEntity(parameters, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalErrorException(Msg.code(1479) + "Server does not support UTF-8 (should not happen)", e);
		}
	}


	@Override
	protected IHttpRequest createHttpRequest() {
		return createHttpRequest((HttpEntity)null);
	}

	@Override
	protected IHttpRequest createHttpRequest(byte[] content) {
		/*
		 * Note: Be careful about changing which constructor we use for
		 * ByteArrayEntity, as Android's version of HTTPClient doesn't support
		 * the newer ones for whatever reason.
		 */
		ByteArrayEntity entity = new ByteArrayEntity(content);
		return createHttpRequest(entity);
	}

	private ApacheHttpRequest createHttpRequest(HttpEntity theEntity) {
		HttpRequestBase request = constructRequestBase(theEntity);
		return new ApacheHttpRequest(myClient, request);
	}

	@Override
	protected IHttpRequest createHttpRequest(Map<String, List<String>> theParams) {
		List<NameValuePair> parameters = new ArrayList<>();
		for (Entry<String, List<String>> nextParam : theParams.entrySet()) {
			List<String> value = nextParam.getValue();
			for (String s : value) {
				parameters.add(new BasicNameValuePair(nextParam.getKey(), s));
			}
		}

		UrlEncodedFormEntity entity = createFormEntity(parameters);
		return createHttpRequest(entity);
	}


	@Override
	protected IHttpRequest createHttpRequest(String theContents) {
		/*
		 * We aren't using a StringEntity here because the constructors
		 * supported by Android aren't available in non-Android, and vice versa.
		 * Since we add the content type header manually, it makes no difference
		 * which one we use anyhow.
		 */
		ByteArrayEntity entity = new ByteArrayEntity(theContents.getBytes(Constants.CHARSET_UTF8));
		return createHttpRequest(entity);
	}
}
