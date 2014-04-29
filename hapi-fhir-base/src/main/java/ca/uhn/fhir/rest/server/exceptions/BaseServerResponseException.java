package ca.uhn.fhir.rest.server.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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

	private static final Map<Integer, Class<? extends BaseServerResponseException>> ourStatusCodeToExceptionType = new HashMap<Integer, Class<? extends BaseServerResponseException>>();
	private static final long serialVersionUID = 1L;

	static {
		registerExceptionType(AuthenticationException.STATUS_CODE, AuthenticationException.class);
		registerExceptionType(InternalErrorException.STATUS_CODE, InternalErrorException.class);
		registerExceptionType(InvalidRequestException.STATUS_CODE, InvalidRequestException.class);
		registerExceptionType(MethodNotAllowedException.STATUS_CODE, MethodNotAllowedException.class);
		registerExceptionType(ResourceNotFoundException.STATUS_CODE, ResourceNotFoundException.class);
		registerExceptionType(ResourceVersionNotSpecifiedException.STATUS_CODE, ResourceVersionNotSpecifiedException.class);
		registerExceptionType(UnprocessableEntityException.STATUS_CODE, UnprocessableEntityException.class);
	}

	private final OperationOutcome myOperationOutcome;

	private int myStatusCode;

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
		myOperationOutcome = null;
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
		myOperationOutcome = theOperationOutcome;
	}

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 * @param theCause
	 *            The cause
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theMessage, theCause);
		myStatusCode = theStatusCode;
		myOperationOutcome = null;
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
		myOperationOutcome = null;
	}

	/**
	 * Returns the {@link OperationOutcome} resource if any which was supplied in the response, or <code>null</code>
	 */
	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	/**
	 * Returns the HTTP status code corresponding to this problem
	 */
	public int getStatusCode() {
		return myStatusCode;
	}

	public static BaseServerResponseException newInstance(int theStatusCode, String theMessage) {
		if (ourStatusCodeToExceptionType.containsKey(theStatusCode)) {
			try {
				return ourStatusCodeToExceptionType.get(theStatusCode).getConstructor(new Class[] { String.class }).newInstance(theMessage);
			} catch (InstantiationException e) {
				throw new InternalErrorException(e);
			} catch (IllegalAccessException e) {
				throw new InternalErrorException(e);
			} catch (IllegalArgumentException e) {
				throw new InternalErrorException(e);
			} catch (InvocationTargetException e) {
				throw new InternalErrorException(e);
			} catch (NoSuchMethodException e) {
				throw new InternalErrorException(e);
			} catch (SecurityException e) {
				throw new InternalErrorException(e);
			}
		} else {
			return new UnclassifiedServerFailureException(theStatusCode, theMessage);
		}
	}

	static void registerExceptionType(int theStatusCode, Class<? extends BaseServerResponseException> theType) {
		if (ourStatusCodeToExceptionType.containsKey(theStatusCode)) {
			throw new Error("Can not register " + theType + " to status code " + theStatusCode + " because " + ourStatusCodeToExceptionType.get(theStatusCode) + " already registers that code");
		}
	}
}
