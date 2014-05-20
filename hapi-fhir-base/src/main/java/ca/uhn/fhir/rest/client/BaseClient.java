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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public abstract class BaseClient {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseClient.class);

	private final HttpClient myClient;
	private boolean myKeepResponses = false;
	private HttpResponse myLastResponse;
	private String myLastResponseBody;
	private final String myUrlBase;
	private EncodingEnum myEncoding = null; // default unspecified (will be XML)
	private boolean myPrettyPrint = false;

	/**
	 * Returns the encoding that will be used on requests. Default is <code>null</code>, which means the client will not explicitly request an encoding. (This is standard behaviour according to the
	 * FHIR specification)
	 */
	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	/**
	 * Sets the encoding that will be used on requests. Default is <code>null</code>, which means the client will not explicitly request an encoding. (This is standard behaviour according to the FHIR
	 * specification)
	 */
	public BaseClient setEncoding(EncodingEnum theEncoding) {
		myEncoding = theEncoding;
		return this;
	}

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

	<T> T invokeClient(IClientResponseHandler<T> binding, BaseClientInvocation clientInvocation) {
		return invokeClient(binding, clientInvocation, false);
	}

	<T> T invokeClient(IClientResponseHandler<T> binding, BaseClientInvocation clientInvocation, boolean theLogRequestAndResponse) {
		// TODO: handle non 2xx status codes by throwing the correct exception,
		// and ensure it's passed upwards
		HttpRequestBase httpRequest;
		HttpResponse response;
		try {
			httpRequest = clientInvocation.asHttpRequest(myUrlBase, createExtraParams(), getEncoding());

			if (theLogRequestAndResponse) {
				ourLog.info("Client invoking: {}", httpRequest);
			}

			response = myClient.execute(httpRequest);
		} catch (DataFormatException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			throw new FhirClientConnectionException(e);
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
			String mimeType = ct != null ? ct.getMimeType() : null;

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

			if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
				String body=null;
				try {
					body = IOUtils.toString(reader);
				} catch (Exception e) {
					ourLog.debug("Failed to read input stream", e);
				} finally {
					IOUtils.closeQuietly(reader);
				}
				
				String message = "HTTP " + response.getStatusLine().getStatusCode()+" " +response.getStatusLine().getReasonPhrase();
				if (Constants.CT_TEXT.equals(mimeType)) {
					message = message+": " + body;
				}
				
				BaseServerResponseException exception = BaseServerResponseException.newInstance(response.getStatusLine().getStatusCode(), message);

				if(body!=null) {
					exception.setResponseBody(body);
				}

				throw exception;
			}

			try {
				return binding.invokeClient(mimeType, reader, response.getStatusLine().getStatusCode(), headers);
			} finally {
				IOUtils.closeQuietly(reader);
			}

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

	protected Map<String, List<String>> createExtraParams() {
		HashMap<String, List<String>> retVal = new LinkedHashMap<String, List<String>>();

		if (getEncoding() == EncodingEnum.XML) {
			retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
		} else if (getEncoding() == EncodingEnum.JSON) {
			retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
		}

		if (isPrettyPrint()) {
			retVal.put(Constants.PARAM_PRETTY, Collections.singletonList(Constants.PARAM_PRETTY_VALUE_TRUE));
		}

		return retVal;
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
		if (entity.getContentType() != null && entity.getContentType().getElements() != null && entity.getContentType().getElements().length > 0) {
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

	/**
	 * Returns the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note that this is currently a non-standard flag (_pretty) which is supported only by
	 * HAPI based servers (and any other servers which might implement it).
	 */
	public boolean isPrettyPrint() {
		return myPrettyPrint;
	}

	/**
	 * Sets the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note that this is currently a non-standard flag (_pretty) which is supported only by
	 * HAPI based servers (and any other servers which might implement it).
	 */
	public BaseClient setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

}
