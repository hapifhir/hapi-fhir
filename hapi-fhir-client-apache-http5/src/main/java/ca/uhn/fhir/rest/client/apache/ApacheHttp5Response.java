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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Http Response based on Apache. This is an adapter around the class
 * {@link org.apache.hc.core5.http.ClassicHttpResponse HttpResponse}
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttp5Response extends BaseHttpResponse implements IHttpResponse {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ApacheHttp5Response.class);

	private boolean myEntityBuffered = false;
	private byte[] myEntityBytes;
	private final ClassicHttpResponse myResponse;

	public ApacheHttp5Response(ClassicHttpResponse theResponse, StopWatch theResponseStopWatch) {
		super(theResponseStopWatch);
		this.myResponse = theResponse;
	}

	@Override
	public void bufferEntity() throws IOException {
		if (myEntityBuffered) {
			return;
		}
		try (InputStream respEntity = readEntity()) {
			if (respEntity != null) {
				this.myEntityBuffered = true;
				try {
					this.myEntityBytes = IOUtils.toByteArray(respEntity);
				} catch (IllegalStateException e) {
					throw new InternalErrorException(Msg.code(2581) + e);
				}
			}
		}
	}

	@Override
	public void close() {
		if (myResponse instanceof CloseableHttpResponse) {
			try {
				myResponse.close();
			} catch (IOException e) {
				ourLog.debug("Failed to close response", e);
			}
		}
	}

	@Override
	public Reader createReader() throws IOException {
		HttpEntity entity = myResponse.getEntity();
		if (entity == null) {
			return new StringReader("");
		}
		Charset charset = null;
		String contentType = entity.getContentType();
		if (StringUtils.isNotBlank(contentType)) {
			ContentType ct = ContentType.parse(contentType);
			charset = ct.getCharset();
		}
		if (charset == null) {
			if (Constants.STATUS_HTTP_204_NO_CONTENT != myResponse.getCode()) {
				ourLog.debug("Response did not specify a charset, defaulting to utf-8");
			}
			charset = StandardCharsets.UTF_8;
		}

		return new InputStreamReader(readEntity(), charset);
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> headers = new HashMap<>();
		Header[] allHeaders = myResponse.getHeaders();
		if (allHeaders != null) {
			for (Header next : allHeaders) {
				String name = next.getName().toLowerCase();
				List<String> list = headers.computeIfAbsent(name, k -> new ArrayList<>());
				list.add(next.getValue());
			}
		}
		return headers;
	}

	@Override
	public List<String> getHeaders(String theName) {
		Header[] headers = myResponse.getHeaders(theName);
		if (headers == null) {
			headers = new Header[0];
		}
		List<String> retVal = new ArrayList<>();
		for (Header next : headers) {
			retVal.add(next.getValue());
		}
		return retVal;
	}

	@Override
	public String getMimeType() {
		ContentType ct = ContentType.parse(myResponse.getEntity().getContentType());
		return ct != null ? ct.getMimeType() : null;
	}

	@Override
	public HttpResponse getResponse() {
		return myResponse;
	}

	@Override
	public int getStatus() {
		return myResponse.getCode();
	}

	@Override
	public String getStatusInfo() {
		return myResponse.getReasonPhrase();
	}

	@Override
	public InputStream readEntity() throws IOException {
		if (this.myEntityBuffered) {
			return new ByteArrayInputStream(myEntityBytes);
		} else if (myResponse.getEntity() != null) {
			return myResponse.getEntity().getContent();
		} else {
			return null;
		}
	}
}
