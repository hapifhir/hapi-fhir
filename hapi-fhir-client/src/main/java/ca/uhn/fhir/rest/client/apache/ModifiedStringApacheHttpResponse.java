/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Process a modified copy of an existing {@link IHttpResponse} with a String containing new content.
 * <p/>
 * Meant to be used with custom interceptors that need to hijack an existing IHttpResponse with new content.
 */
public class ModifiedStringApacheHttpResponse extends BaseHttpResponse implements IHttpResponse {
	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(ModifiedStringApacheHttpResponse.class);
	private boolean myEntityBuffered = false;
	private final String myNewContent;
	private final IHttpResponse myOrigHttpResponse;
	private byte[] myEntityBytes = null;

	public ModifiedStringApacheHttpResponse(
			IHttpResponse theOrigHttpResponse, String theNewContent, StopWatch theResponseStopWatch) {
		super(theResponseStopWatch);
		myOrigHttpResponse = theOrigHttpResponse;
		myNewContent = theNewContent;
	}

	@Override
	public void bufferEntity() throws IOException {
		if (myEntityBuffered) {
			return;
		}
		try (InputStream respEntity = readEntity()) {
			if (respEntity != null) {
				try {
					myEntityBytes = IOUtils.toByteArray(respEntity);
				} catch (IllegalStateException exception) {
					throw new InternalErrorException(Msg.code(2447) + exception);
				}
				myEntityBuffered = true;
			}
		}
	}

	@Override
	public void close() {
		if (myOrigHttpResponse instanceof CloseableHttpResponse) {
			try {
				((CloseableHttpResponse) myOrigHttpResponse).close();
			} catch (IOException exception) {
				ourLog.debug("Failed to close response", exception);
			}
		}
	}

	@Override
	public Reader createReader() throws IOException {
		return new InputStreamReader(readEntity(), StandardCharsets.UTF_8);
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		return myOrigHttpResponse.getAllHeaders();
	}

	@Override
	public List<String> getHeaders(String theName) {
		return myOrigHttpResponse.getHeaders(theName);
	}

	@Override
	public String getMimeType() {
		return myOrigHttpResponse.getMimeType();
	}

	@Override
	public StopWatch getRequestStopWatch() {
		return myOrigHttpResponse.getRequestStopWatch();
	}

	@Override
	public Object getResponse() {
		return null;
	}

	@Override
	public int getStatus() {
		return myOrigHttpResponse.getStatus();
	}

	@Override
	public String getStatusInfo() {
		return myOrigHttpResponse.getStatusInfo();
	}

	@Override
	public InputStream readEntity() {
		if (myEntityBuffered) {
			return new ByteArrayInputStream(myEntityBytes);
		} else {
			return new ByteArrayInputStream(myNewContent.getBytes());
		}
	}
}
