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

import ca.uhn.fhir.rest.client.api.BaseHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;

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
 * {@link HttpUriRequest HttpRequestBase }
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttp5Request extends BaseHttpRequest implements IHttpRequest {

	private HttpClient myClient;
	private HttpUriRequest myRequest;

	public ApacheHttp5Request(HttpClient theClient, HttpUriRequest theApacheRequest) {
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
		return myClient.execute(myRequest, httpResponse -> new ApacheHttp5Response(httpResponse, responseStopWatch));
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> result = new HashMap<>();
		for (Header header : myRequest.getHeaders()) {
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
	public HttpUriRequest getApacheRequest() {
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
		if (myRequest != null) {
			HttpEntity entity = myRequest.getEntity();
			if (entity.isRepeatable()) {
				final Header contentTypeHeader = myRequest.getFirstHeader("Content-Type");
				Charset charset = contentTypeHeader == null
						? null
						: ContentType.parse(contentTypeHeader.getValue()).getCharset();
				return IOUtils.toString(entity.getContent(), charset);
			}
		}
		return null;
	}

	@Override
	public String getUri() {
		return myRequest.getRequestUri().toString();
	}

	@Override
	public void setUri(String theUrl) {
		myRequest.setUri(URI.create(theUrl));
	}

	@Override
	public String toString() {
		return myRequest.toString();
	}
}
