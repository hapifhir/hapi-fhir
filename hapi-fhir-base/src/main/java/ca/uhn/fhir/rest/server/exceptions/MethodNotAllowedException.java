package ca.uhn.fhir.rest.server.exceptions;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;

/*
 * #%L
 * HAPI FHIR - Core Library
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

/**
 * Represents an <b>HTTP 405 Method Not Allowed</b> response.
 * 
 * <p>
 * Note that a complete list of RESTful exceptions is available in the <a href="./package-summary.html">Package Summary</a>.
 * </p>
 * 
 * @see UnprocessableEntityException Which should be used for business level validation failures
 */
public class MethodNotAllowedException extends BaseServerResponseException {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_CODE = Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED;
	private Set<RequestTypeEnum> myAllowedMethods;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *           The message
	 * @param theOperationOutcome
	 *           The OperationOutcome resource to return to the client
	 * @param theAllowedMethods
	 *           A list of allowed methods (see {@link #setAllowedMethods(RequestTypeEnum...)} )
	 */
	public MethodNotAllowedException(String theMessage, IBaseOperationOutcome theOperationOutcome, RequestTypeEnum... theAllowedMethods) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
		setAllowedMethods(theAllowedMethods);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *           The message
	 * @param theAllowedMethods
	 *           A list of allowed methods (see {@link #setAllowedMethods(RequestTypeEnum...)} )
	 */
	public MethodNotAllowedException(String theMessage, RequestTypeEnum... theAllowedMethods) {
		super(STATUS_CODE, theMessage);
		setAllowedMethods(theAllowedMethods);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *           The message
	 * @param theOperationOutcome
	 *           The OperationOutcome resource to return to the client
	 */
	public MethodNotAllowedException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *           The message
	 */
	public MethodNotAllowedException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	/**
	 * Specifies the list of allowed HTTP methods (GET, POST, etc). This is provided in an <code>Allow</code> header, as required by the HTTP specification (RFC 2616).
	 */
	public Set<RequestTypeEnum> getAllowedMethods() {
		return myAllowedMethods;
	}

	/**
	 * Specifies the list of allowed HTTP methods (GET, POST, etc). This is provided in an <code>Allow</code> header, as required by the HTTP specification (RFC 2616).
	 */
	public void setAllowedMethods(RequestTypeEnum... theAllowedMethods) {
		if (theAllowedMethods == null || theAllowedMethods.length == 0) {
			myAllowedMethods = null;
		} else {
			myAllowedMethods = new LinkedHashSet<RequestTypeEnum>();
			for (RequestTypeEnum next : theAllowedMethods) {
				myAllowedMethods.add(next);
			}
		}
		updateAllowHeader();
	}

	/**
	 * Specifies the list of allowed HTTP methods (GET, POST, etc). This is provided in an <code>Allow</code> header, as required by the HTTP specification (RFC 2616).
	 */
	public void setAllowedMethods(Set<RequestTypeEnum> theAllowedMethods) {
		myAllowedMethods = theAllowedMethods;
		updateAllowHeader();
	}

	private void updateAllowHeader() {
		getResponseHeaders().remove(Constants.HEADER_ALLOW);

		StringBuilder b = new StringBuilder();
		for (RequestTypeEnum next : myAllowedMethods) {
			if (b.length() > 0) {
				b.append(',');
			}
			b.append(next.name());
		}
		addResponseHeader(Constants.HEADER_ALLOW, b.toString());
	}

}
