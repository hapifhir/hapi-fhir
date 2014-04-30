package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR Library
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public abstract class BaseClient {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseClient.class);

	private final HttpClient myClient;
	private boolean myKeepResponses = false;
	private HttpResponse myLastResponse;
	private String myLastResponseBody;
	private final String myUrlBase;

	BaseClient(HttpClient theClient, String theUrlBase) {
		super();
		myClient = theClient;
		myUrlBase = theUrlBase;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public HttpResponse getLastResponse() {
		return myLastResponse;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public String getLastResponseBody() {
		return myLastResponseBody;
	}

	String getServerBase() {
		return myUrlBase;
	}

	Object invokeClient(IClientResponseHandler binding, BaseClientInvocation clientInvocation) {
		// TODO: handle non 2xx status codes by throwing the correct exception,
		// and ensure it's passed upwards
		HttpRequestBase httpRequest;
		HttpResponse response;
		try {
			httpRequest = clientInvocation.asHttpRequest(myUrlBase);
			response = myClient.execute(httpRequest);
		} catch (DataFormatException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			throw new FhirClientConnectionException(e);
		}
		
		if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
			throw BaseServerResponseException.newInstance(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
		}

		try {

			Reader reader = createReaderFromResponse(response);

			if (ourLog.isTraceEnabled() || myKeepResponses) {
				String responseString = IOUtils.toString(reader);
				if (myKeepResponses) {
					myLastResponse = response;
					myLastResponseBody = responseString; 
				}
				ourLog.trace("FHIR response:\n{}\n{}", response, responseString);
				reader = new StringReader(responseString);
			}

			ContentType ct = ContentType.get(response.getEntity());
			String mimeType = ct.getMimeType();

			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			if (response.getAllHeaders() != null) {
				for (Header next : response.getAllHeaders()) {
					String name = next.getName().toLowerCase();
					List<String> list = headers.get(name);
					if (list == null) {
						list = new ArrayList<String>();
						headers.put(name, list);
					}
					list.add(next.getValue());
				}
			}

			return binding.invokeClient(mimeType, reader, response.getStatusLine().getStatusCode(), headers);

		} catch (IllegalStateException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			throw new FhirClientConnectionException(e);
		} finally {
			if (response instanceof CloseableHttpResponse) {
				try {
					((CloseableHttpResponse) response).close();
				} catch (IOException e) {
					ourLog.debug("Failed to close response", e);
				}
			}
		}
	}
	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public boolean isKeepResponses() {
		return myKeepResponses;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setKeepResponses(boolean theKeepResponses) {
		myKeepResponses = theKeepResponses;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastResponse(HttpResponse theLastResponse) {
		myLastResponse = theLastResponse;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastResponseBody(String theLastResponseBody) {
		myLastResponseBody = theLastResponseBody;
	}

	public static Reader createReaderFromResponse(HttpResponse theResponse) throws IllegalStateException, IOException {
		HttpEntity entity = theResponse.getEntity();
		if (entity == null) {
			return new StringReader("");
		}
		Charset charset = null;
		if (entity.getContentType().getElements() != null) {
			ContentType ct = ContentType.get(entity);
			charset = ct.getCharset();
		}
		if (charset == null) {
			ourLog.warn("Response did not specify a charset.");
			charset = Charset.forName("UTF-8");
		}

		Reader reader = new InputStreamReader(theResponse.getEntity().getContent(), charset);
		return reader;
	}

}
