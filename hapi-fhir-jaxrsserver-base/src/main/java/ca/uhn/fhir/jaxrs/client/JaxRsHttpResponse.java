package ca.uhn.fhir.jaxrs.client;

import java.io.*;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.apache.commons.io.IOUtils;

/**
 * A Http Response based on JaxRs. This is an adapter around the class {@link javax.ws.rs.core.Response Response}
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsHttpResponse extends BaseHttpResponse implements IHttpResponse {
	
	private boolean myBufferedEntity = false;
	private final Response myResponse;
	
	public JaxRsHttpResponse(Response theResponse, StopWatch theResponseStopWatch) {
		super(theResponseStopWatch);
		this.myResponse = theResponse;
	}

	@Override
	public void bufferEntity() throws IOException {
		if(!myBufferedEntity && myResponse.hasEntity()) {
			myBufferedEntity = true;
			myResponse.bufferEntity();
		} else {
			myResponse.bufferEntity();
		}
	}

	@Override
	public void close() {
		// automatically done by jax-rs
	}

	@Override
	public Reader createReader() {
		if (!myBufferedEntity && !myResponse.hasEntity()) {
			return new StringReader("");
		} else {
			return new StringReader(myResponse.readEntity(String.class));
		}
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		Map<String, List<String>> theHeaders = new ConcurrentHashMap<String, List<String>>();
		for (Entry<String, List<String>> iterable_element : myResponse.getStringHeaders().entrySet()) {
			theHeaders.put(iterable_element.getKey().toLowerCase(), iterable_element.getValue());
		}
		return theHeaders;
	}

	@Override
	public String getMimeType() {
		MediaType mediaType = myResponse.getMediaType();
		if (mediaType == null) {
			return null;
		}
		//Keep only type and subtype and do not include the parameters such as charset
		return new MediaType(mediaType.getType(), mediaType.getSubtype()).toString();
	}
	
	@Override
	public Response getResponse() {
		return myResponse;
	}
	
	@Override
	public int getStatus() {
		return myResponse.getStatus();
	}
	

	@Override
	public String getStatusInfo() {
		return myResponse.getStatusInfo().getReasonPhrase();
	}

	@Override
	public InputStream readEntity() {
		if (!myBufferedEntity && !myResponse.hasEntity()) {
			return new ByteArrayInputStream(new byte[0]);
		} else {
			return new ByteArrayInputStream(myResponse.readEntity(byte[].class));
		}
	}

	@Override
	public List<String> getHeaders(String theName) {
		List<String> retVal = myResponse.getStringHeaders().get(theName);
		return retVal;
	}	
	

}
