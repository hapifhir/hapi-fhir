package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

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
 * Represents an <b>HTTP 400 Bad Request</b> response.
 * This status indicates that the client's message was invalid (e.g. not a valid FHIR Resource
 * per the specifications), as opposed to the {@link UnprocessableEntityException} which indicates
 * that data does not pass business rule validation on the server.
 * 
 * <p>
 * Note that a complete list of RESTful exceptions is available in the
 * <a href="./package-summary.html">Package Summary</a>.
 * </p>
 * 
 * @see UnprocessableEntityException Which should be used for business level validation failures
 */
@CoverageIgnore
public class InvalidRequestException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_400_BAD_REQUEST;
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public InvalidRequestException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	/**
	 * Constructor
	 */
	public InvalidRequestException(String theMessage, Throwable theCause) {
		super(STATUS_CODE, theMessage, theCause);
	}

	/**
	 * Constructor
	 */
	public InvalidRequestException(Throwable theCause) {
		super(STATUS_CODE, theCause);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public InvalidRequestException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}


}
