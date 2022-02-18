package ca.uhn.fhir.rest.client.exceptions;

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
 * Represents a failure by the HAPI FHIR Client to successfully communicate
 * with a FHIR server, because of IO failures, incomprehensible response, etc.
 */
@CoverageIgnore
public class FhirClientConnectionException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public FhirClientConnectionException(Throwable theCause) {
		super(500, theCause);
	}

	public FhirClientConnectionException(String theMessage, Throwable theCause) {
		super(500, theMessage, theCause);
	}

	public FhirClientConnectionException(String theMessage) {
		super(500, theMessage);
	}

}
