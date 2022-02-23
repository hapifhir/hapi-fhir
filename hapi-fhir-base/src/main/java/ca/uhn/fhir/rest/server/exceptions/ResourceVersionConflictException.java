package ca.uhn.fhir.rest.server.exceptions;

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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 409 Conflict</b> response. This exception should be 
 * thrown in methods which accept a version (e.g. {@link Update}, {@link Delete}) 
 * when the operation fails because of a version conflict as specified in the FHIR specification. 
 */
@CoverageIgnore
public class ResourceVersionConflictException extends BaseServerResponseException {
	public static final int STATUS_CODE = Constants.STATUS_HTTP_409_CONFLICT;
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ResourceVersionConflictException(String error) {
		super(STATUS_CODE, error);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceVersionConflictException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	/**
	 * Constructor
	 *
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceVersionConflictException(String theMessage, Throwable theCause, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theCause, theOperationOutcome);
	}

}
