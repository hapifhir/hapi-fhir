package ca.uhn.fhir.jaxrs.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * A Http Response based on JaxRs. This is an adapter around the class {@link javax.ws.rs.core.Response Response}
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsHttpResponse implements IHttpResponse {
	
	private final Response myResponse;
	private boolean myBufferedEntity = false;
	
	public JaxRsHttpResponse(Response theResponse) {
		this.myResponse = theResponse;
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
	public String getMimeType() {
		MediaType mediaType = myResponse.getMediaType();
		//Keep only type and subtype and do not include the parameters such as charset
		return new MediaType(mediaType.getType(), mediaType.getSubtype()).toString();
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
	public String getStatusInfo() {
		return myResponse.getStatusInfo().getReasonPhrase();
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
	public InputStream readEntity() {
		return myResponse.readEntity(java.io.InputStream.class);
	}
	
	@Override
	public void bufferEntitity() {
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
	

}
