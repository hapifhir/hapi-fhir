package ca.uhn.fhir.rest.client.apache;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * A Http Request based on Apache. This is an adapter around the class
 * {@link org.apache.http.client.methods.HttpRequestBase HttpRequestBase}
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpRequest implements IHttpRequest {

	private HttpClient myClient;
	private HttpRequestBase myRequest;

	public ApacheHttpRequest(HttpClient theClient, HttpRequestBase theApacheRequest) {
		this.myClient = theClient;
		this.myRequest = theApacheRequest;
	}

	@Override
	public void addHeader(String theName, String theValue) {
		myRequest.addHeader(theName, theValue);
	}

	@Override
	public IHttpResponse execute() throws IOException {
		return new ApacheHttpResponse(myClient.execute(myRequest));
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (Header header : myRequest.getAllHeaders()) {
			if (!result.containsKey(header.getName())) {
				result.put(header.getName(), new LinkedList<String>());
			}
			result.get(header.getName()).add(header.getValue());
		}
		return result;
	}

	/**
	 * Get the ApacheRequest
	 * @return the ApacheRequest
	 */
	public HttpRequestBase getApacheRequest() {
		return myRequest;
	}

	@Override
	public String getHttpVerbName() {
		return myRequest.getMethod();
	}

	@Override
	public String getRequestBodyFromStream() throws IOException {
		if (myRequest instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) myRequest).getEntity();
			if (entity.isRepeatable()) {
				final Header contentTypeHeader = myRequest.getFirstHeader("Content-Type");
				Charset charset = contentTypeHeader == null ? null : ContentType.parse(contentTypeHeader.getValue()).getCharset();
				return IOUtils.toString(entity.getContent(), charset);
			}
		}
		return null;
	}

	@Override
	public String getUri() {
		return myRequest.getURI().toString();
	}

	@Override
	public String toString() {
		return myRequest.toString();
	}

}
