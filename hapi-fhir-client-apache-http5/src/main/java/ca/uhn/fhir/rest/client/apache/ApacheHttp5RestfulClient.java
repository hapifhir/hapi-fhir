/*
 * #%L
 * HAPI FHIR - Client Framework using Apache HttpClient 5
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.hc.core5.http.ContentType.APPLICATION_OCTET_STREAM;

/**
 * A Http Client based on Apache. This is an adapter around the class
 * {@link org.apache.hc.client5.http.classic HttpClient}
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttp5RestfulClient extends BaseHttpClient implements IHttpClient {

	private final HttpClient myClient;
	private final HttpHost host;

	public ApacheHttp5RestfulClient(
			HttpClient theClient,
			StringBuilder theUrl,
			Map<String, List<String>> theIfNoneExistParams,
			String theIfNoneExistString,
			RequestTypeEnum theRequestType,
			List<Header> theHeaders) {
		super(theUrl, theIfNoneExistParams, theIfNoneExistString, theRequestType, theHeaders);
		this.myClient = theClient;
		this.host = new HttpHost(theUrl.toString());
	}

	private HttpUriRequestBase constructRequestBase(HttpEntity theEntity) {
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
		return new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8);
	}

	@Override
	protected IHttpRequest createHttpRequest() {
		return createHttpRequest((HttpEntity) null);
	}

	@Override
	protected IHttpRequest createHttpRequest(byte[] content) {
		/*
		 * Note: Be careful about changing which constructor we use for
		 * ByteArrayEntity, as Android's version of HTTPClient doesn't support
		 * the newer ones for whatever reason.
		 */
		ByteArrayEntity entity = new ByteArrayEntity(content, APPLICATION_OCTET_STREAM);
		return createHttpRequest(entity);
	}

	private ApacheHttp5Request createHttpRequest(HttpEntity theEntity) {
		HttpUriRequest request = constructRequestBase(theEntity);
		return new ApacheHttp5Request(myClient, request);
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
		ByteArrayEntity entity =
				new ByteArrayEntity(theContents.getBytes(StandardCharsets.UTF_8), APPLICATION_OCTET_STREAM);
		return createHttpRequest(entity);
	}
}
