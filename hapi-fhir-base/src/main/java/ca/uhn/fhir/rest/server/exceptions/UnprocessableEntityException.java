package ca.uhn.fhir.rest.server.exceptions;

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

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

/**
 * Represents an <b>HTTP 422 Unprocessable Entity</b> response, which means that a resource was rejected by the server because it "violated applicable FHIR profiles or server business rules".
 * 
 * <p>
 * This exception will generally contain an {@link OperationOutcome} instance which details the failure.
 * </p>
 */
public class UnprocessableEntityException extends BaseServerResponseException {

	private static final String DEFAULT_MESSAGE = "Unprocessable Entity";
	private static final long serialVersionUID = 1L;

	private final OperationOutcome myOperationOutcome;

	/**
	 * Constructor which accepts an {@link OperationOutcome} resource which will be supplied in the response
	 */
	public UnprocessableEntityException(OperationOutcome theOperationOutcome) {
		super(422, theOperationOutcome == null || theOperationOutcome.getIssueFirstRep().getDetails().isEmpty() ? DEFAULT_MESSAGE : theOperationOutcome.getIssueFirstRep().getDetails().getValue());
		if (theOperationOutcome == null) {
			myOperationOutcome = new OperationOutcome();
		} else {
			myOperationOutcome = theOperationOutcome;
		}
	}

	/**
	 * Constructor which accepts a String describing the issue. This string will be translated into an {@link OperationOutcome} resource which will be supplied in the response.
	 */
	public UnprocessableEntityException(String theMessage) {
		super(422, theMessage);

		myOperationOutcome = new OperationOutcome();
		myOperationOutcome.addIssue().setDetails(theMessage);
	}

	/**
	 * Constructor which accepts an array of Strings describing the issue. This strings will be translated into an {@link OperationOutcome} resource which will be supplied in the response.
	 */
	public UnprocessableEntityException(String... theMessage) {
		super(422, theMessage != null && theMessage.length > 0 ? theMessage[0] : DEFAULT_MESSAGE);

		myOperationOutcome = new OperationOutcome();
		if (theMessage != null) {
			for (String next : theMessage) {
				myOperationOutcome.addIssue().setDetails(next);
			}
		}
	}

	/**
	 * Returns the {@link OperationOutcome} resource if any which was supplied in the response. Will not return null.
	 */
	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

}
