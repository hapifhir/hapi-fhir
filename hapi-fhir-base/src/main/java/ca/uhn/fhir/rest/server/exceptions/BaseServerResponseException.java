package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

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

public abstract class BaseServerResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int myStatusCode;
	private final OperationOutcome myOperationOutcome;

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage) {
		super(theMessage);
		myStatusCode = theStatusCode;
		myOperationOutcome=null;
	}

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, OperationOutcome theOperationOutcome) {
		super(theMessage);
		myStatusCode = theStatusCode;
		myOperationOutcome=theOperationOutcome;
	}
	
	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theMessage, theCause);
		myStatusCode = theStatusCode;
		myOperationOutcome=null;
	}

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theCause
	 *            The underlying cause exception
	 */
	public BaseServerResponseException(int theStatusCode, Throwable theCause) {
		super(theCause.toString(), theCause);
		myStatusCode = theStatusCode;
		myOperationOutcome=null;
	}

	/**
	 * Returns the HTTP status code corresponding to this problem
	 */
	public int getStatusCode() {
		return myStatusCode;
	}

	/**
	 * Returns the {@link OperationOutcome} resource if any which was supplied in the response,
	 * or <code>null</code>
	 */
	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}
}
