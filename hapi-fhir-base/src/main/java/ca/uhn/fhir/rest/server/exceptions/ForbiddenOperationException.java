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
 * This Represents an <b>HTTP 403 Forbidden</b> response, which generally indicates one of two conditions:
 * <ul>
 * <li>Authentication was provided, but the authenticated user is not permitted to perform the requested operation.</li>
 * <li>The operation is forbidden to all users. Repeating the request with authentication would serve no purpose.</li>
 * </ul>
 * 
 * <p>
 * For security failures, you should use
 * {@link AuthenticationException} if you want to indicate that the
 * user could not be authenticated (e.g. credential failures), also 
 * known as an <b>authentication</b> failure. 
 * You should use {@link ForbiddenOperationException} if you want to 
 * indicate that the authenticated user does not have permission to
 * perform the requested operation, also known as an <b>authorization</b>
 * failure.
 * </p>
 * <p>
 * Note that a complete list of RESTful exceptions is available in the <a href="./package-summary.html">Package
 * Summary</a>.
 * </p>
 */
@CoverageIgnore
public class ForbiddenOperationException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_403_FORBIDDEN;
	private static final long serialVersionUID = 1L;

	public ForbiddenOperationException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theOperationOutcome
	 *            The OperationOutcome resource to return to the client
	 */
	public ForbiddenOperationException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

}
