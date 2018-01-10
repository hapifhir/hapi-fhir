package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public abstract class RestfulResponse<T extends RequestDetails> implements IRestfulResponse {

	private IIdType myOperationResourceId;
	private IPrimitiveType<Date> myOperationResourceLastUpdated;
	private ConcurrentHashMap<String, String> theHeaders = new ConcurrentHashMap<String, String>();
	private T theRequestDetails;

	public RestfulResponse(T requestDetails) {
		this.theRequestDetails = requestDetails;
	}

	@Override
	public void addHeader(String headerKey, String headerValue) {
		this.getHeaders().put(headerKey, headerValue);
	}

	/**
	 * Get the http headers
	 * @return the headers
	 */
	public ConcurrentHashMap<String, String> getHeaders() {
		return theHeaders;
	}

	/**
	 * Get the requestDetails
	 * @return the requestDetails
	 */
	public T getRequestDetails() {
		return theRequestDetails;
	}

	@Override
	public void setOperationResourceId(IIdType theOperationResourceId) {
		myOperationResourceId = theOperationResourceId;
	}

	@Override
	public void setOperationResourceLastUpdated(IPrimitiveType<Date> theOperationResourceLastUpdated) {
		myOperationResourceLastUpdated = theOperationResourceLastUpdated;
	}

	/**
	 * Set the requestDetails
	 * @param requestDetails the requestDetails to set
	 */
	public void setRequestDetails(T requestDetails) {
		this.theRequestDetails = requestDetails;
	}

	@Override
	public final Object streamResponseAsResource(IBaseResource theResource, boolean thePrettyPrint, Set<SummaryEnum> theSummaryMode,
			int theStatusCode, String theStatusMessage, boolean theRespondGzip, boolean theAddContentLocation)
					throws IOException {
		return RestfulServerUtils.streamResponseAsResource(theRequestDetails.getServer(), theResource, theSummaryMode, theStatusCode, theStatusMessage, theAddContentLocation, theRespondGzip, getRequestDetails(), myOperationResourceId, myOperationResourceLastUpdated);

	}

}
