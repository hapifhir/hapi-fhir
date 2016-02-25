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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * A Http Request based on JaxRs. This is an adapter around the class
 * {@link javax.ws.rs.client.Invocation Invocation}
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsHttpRequest implements IHttpRequest {

	private Invocation.Builder myRequest;
	private RequestTypeEnum myRequestType;
	private Entity<?> myEntity;
	private final Map<String, List<String>> myHeaders = new HashMap<String, List<String>>();

	public JaxRsHttpRequest(Invocation.Builder theRequest, RequestTypeEnum theRequestType, Entity<?> theEntity) {
		this.myRequest = theRequest;
		this.myRequestType = theRequestType;
		this.myEntity = theEntity;
	}

	@Override
	public void addHeader(String theName, String theValue) {
		if (!myHeaders.containsKey(theName)) {
			myHeaders.put(theName, new LinkedList<String>());
		}
		myHeaders.get(theName).add(theValue);
		getRequest().header(theName, theValue);
	}

	/**
	 * Get the Request
	 * @return the Request
	 */
	public Invocation.Builder getRequest() {
		return myRequest;
	}

	/**
	 * Get the Request Type
	 * @return the request type
	 */
	public RequestTypeEnum getRequestType() {
		return myRequestType == null ? RequestTypeEnum.GET : myRequestType;
	}

	/**
	 * Get the Entity
	 * @return the entity
	 */
	public Entity<?> getEntity() {
		return myEntity;
	}

	@Override
	public IHttpResponse execute() {
		Invocation invocation = getRequest().build(getRequestType().name(), getEntity());
		Response response = invocation.invoke();
		return new JaxRsHttpResponse(response);
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		return this.myHeaders;
	}

	@Override
	public String getRequestBodyFromStream() {
		// not supported
		return null;
	}

}
