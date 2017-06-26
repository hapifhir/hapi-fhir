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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * A Http Response based on Apache. This is an adapter around the class
 * {@link org.apache.http.HttpResponse HttpResponse}
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class ApacheHttpResponse implements IHttpResponse {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ApacheHttpResponse.class);

	private boolean myEntityBuffered = false;
	private byte[] myEntityBytes;
	private final HttpResponse myResponse;

	public ApacheHttpResponse(HttpResponse theResponse) {
		this.myResponse = theResponse;
	}

	@Deprecated // override deprecated method
	@Override
	public void bufferEntitity() throws IOException {
		bufferEntity();
	}

	@Override
	public void bufferEntity() throws IOException {
		if (myEntityBuffered) {
			return;
		}
		InputStream respEntity = readEntity();
		if (respEntity != null) {
			this.myEntityBuffered = true;
			try {
				this.myEntityBytes = IOUtils.toByteArray(respEntity);
			} catch (IllegalStateException e) {
				// FIXME resouce leak
				throw new InternalErrorException(e);
			}
		}
	}

	@Override
	public void close() {
		if (myResponse instanceof CloseableHttpResponse) {
			try {
				((CloseableHttpResponse) myResponse).close();
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
		if (entity.getContentType() != null && entity.getContentType().getElements() != null
				&& entity.getContentType().getElements().length > 0) {
			ContentType ct = ContentType.get(entity);
			charset = ct.getCharset();
		}
		if (charset == null) {
			if (Constants.STATUS_HTTP_204_NO_CONTENT != myResponse.getStatusLine().getStatusCode()) {
				ourLog.warn("Response did not specify a charset.");
			}
			charset = Charset.forName("UTF-8");
		}

		Reader reader = new InputStreamReader(readEntity(), charset);
		return reader;
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		if (myResponse.getAllHeaders() != null) {
			for (Header next : myResponse.getAllHeaders()) {
				String name = next.getName().toLowerCase();
				List<String> list = headers.get(name);
				if (list == null) {
					list = new ArrayList<String>();
					headers.put(name, list);
				}
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
		List<String> retVal = new ArrayList<String>();
		for (Header next : headers) {
			retVal.add(next.getValue());
		}
		return retVal;
	}

	@Override
	public String getMimeType() {
		ContentType ct = ContentType.get(myResponse.getEntity());
		return ct != null ? ct.getMimeType() : null;
	}

	@Override
	public HttpResponse getResponse() {
		return myResponse;
	}

	@Override
	public int getStatus() {
		return myResponse.getStatusLine().getStatusCode();
	}

	@Override
	public String getStatusInfo() {
		return myResponse.getStatusLine().getReasonPhrase();
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
