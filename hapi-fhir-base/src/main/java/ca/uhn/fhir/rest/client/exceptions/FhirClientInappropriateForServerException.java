package ca.uhn.fhir.rest.client.exceptions;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

/**
 * This exception will be thrown by FHIR clients if the client attempts to
 * communicate with a server which is a valid FHIR server but is incompatible
 * with this client for some reason.
 */
@CoverageIgnore
public class FhirClientInappropriateForServerException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public FhirClientInappropriateForServerException(Throwable theCause) {
		super(0, theCause);
	}

	public FhirClientInappropriateForServerException(String theMessage, Throwable theCause) {
		super(0, theMessage, theCause);
	}

	public FhirClientInappropriateForServerException(String theMessage) {
		super(0, theMessage);
	}

}
