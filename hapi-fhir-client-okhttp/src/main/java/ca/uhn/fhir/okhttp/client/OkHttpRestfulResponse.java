package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.i18n.Msg;
import java.io.*;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.rest.api.Constants;

/*
 * #%L
 * HAPI FHIR OkHttp Client
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Wraps an OkHttp {@link Response}
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulResponse extends BaseHttpResponse implements IHttpResponse {

	private boolean myEntityBuffered = false;
	private byte[] myEntityBytes;
	private Response myResponse;

	public OkHttpRestfulResponse(Response theResponse, StopWatch theResponseStopWatch) {
		super(theResponseStopWatch);
		this.myResponse = theResponse;
	}

	@Override
	public void bufferEntity() throws IOException {
		if (myEntityBuffered) {
			return;
		}
		try (InputStream responseEntity = readEntity()) {
			if (responseEntity != null) {
				myEntityBuffered = true;
				try {
					myEntityBytes = IOUtils.toByteArray(responseEntity);
				} catch (IllegalStateException e) {
					throw new InternalErrorException(Msg.code(465) + e);
				}
			}
		}
	}

	@Override
	public void close() {
		myResponse.close();
	}

	@Override
	public Reader createReader() throws IOException {
		if (!myEntityBuffered && myResponse.body() == null) {
			return new StringReader("");
		} else {
			return new InputStreamReader(readEntity());
		}
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		return myResponse.headers().toMultimap();
	}

	@Override
	public List<String> getHeaders(String theName) {
		return myResponse.headers(theName);
	}

	@Override
	public String getMimeType() {
		String contentType = myResponse.header(Constants.HEADER_CONTENT_TYPE);
		MediaType mediaType = null;
		if (contentType == null) {
			if (myResponse.body() != null) {
				mediaType = myResponse.body().contentType();
			}
		} else {
			mediaType = MediaType.parse(contentType);
		}

		if (mediaType == null) {
			return null;
		}

		return typeAndSubtypeOnly(mediaType).toString();
	}

	@Override
	public Object getResponse() {
		return myResponse;
	}

	@Override
	public int getStatus() {
		return myResponse.code();
	}

	@Override
	public String getStatusInfo() {
		return myResponse.message();
	}

	@Override
	public InputStream readEntity() throws IOException {
		if (this.myEntityBuffered) {
			return new ByteArrayInputStream(myEntityBytes);
		} else if (myResponse.body() != null) {
			return myResponse.body().byteStream();
		} else {
			return null;
		}
	}

	private MediaType typeAndSubtypeOnly(MediaType input) {
		return MediaType.parse(input.type() + "/" + input.subtype());
	}

}
