package ca.uhn.fhir.rest.client.exceptions;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;

@CoverageIgnore
public class InvalidResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 */
	public InvalidResponseException(int theStatusCode, String theMessage) {
		super(theStatusCode, theMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public InvalidResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theStatusCode, theMessage, theCause);
	}

	/**
	 * Constructor
	 * 
	 * @param theCause
	 *            The underlying cause exception
	 */
	public InvalidResponseException(int theStatusCode, Throwable theCause) {
		super(theStatusCode, theCause.toString(), theCause);
	}

}
