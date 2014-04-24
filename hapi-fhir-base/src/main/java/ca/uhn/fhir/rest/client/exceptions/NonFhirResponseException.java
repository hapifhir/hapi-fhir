package ca.uhn.fhir.rest.client.exceptions;

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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class NonFhirResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;
	private final String myContentType;
	private final String myResponseText;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theResponseText
	 * @param theStatusCode
	 * @param theContentType
	 */
	public NonFhirResponseException(String theMessage, String theContentType, int theStatusCode, String theResponseText) {
		super(theStatusCode, theMessage);
		myContentType = theContentType;
		myResponseText = theResponseText;
	}

	public String getContentType() {
		return myContentType;
	}

	public String getResponseText() {
		return myResponseText;
	}

}
