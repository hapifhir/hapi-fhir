package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

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

/**
 * This Represents an <b>HTTP 413 Payload Too Large</b> response, which means the request body
 * was too big for the server to accept
 * 
 * <p>
 * Note that a complete list of RESTful exceptions is available in the <a href="./package-summary.html">Package
 * Summary</a>.
 * </p>
 */
@CoverageIgnore
public class PayloadTooLargeException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_412_PAYLOAD_TOO_LARGE;
	private static final long serialVersionUID = 1L;

	public PayloadTooLargeException(String theMessage) {
		this(theMessage, null);
	}

	/**
	 * Constructor
	 *
	 * @param theMessage
	 *            The message
	 * @param theOperationOutcome
	 *            The OperationOutcome resource to return to the client
	 */
	public PayloadTooLargeException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

}
