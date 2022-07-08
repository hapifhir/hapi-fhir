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

import ca.uhn.fhir.rest.client.api.BaseHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A Http Request based on Apache. This is an adapter around the class
 * {@link org.apache.http.client.methods.HttpRequestBase HttpRequestBase}
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpRequest extends BaseHttpRequest implements IHttpRequest {

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
		StopWatch responseStopWatch = new StopWatch();
		HttpResponse httpResponse = myClient.execute(myRequest);
		return new ApacheHttpResponse(httpResponse, responseStopWatch);
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> result = new HashMap<>();
		for (Header header : myRequest.getAllHeaders()) {
			if (!result.containsKey(header.getName())) {
				result.put(header.getName(), new LinkedList<>());
			}
			result.get(header.getName()).add(header.getValue());
		}
		return Collections.unmodifiableMap(result);
	}

	/**
	 * Get the ApacheRequest
	 *
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
	public void removeHeaders(String theHeaderName) {
		Validate.notBlank(theHeaderName, "theHeaderName must not be null or blank");
		myRequest.removeHeaders(theHeaderName);
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
	public void setUri(String theUrl) {
		myRequest.setURI(URI.create(theUrl));
	}

	@Override
	public String toString() {
		return myRequest.toString();
	}

}
