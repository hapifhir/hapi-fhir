package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
 * Base class for RESTful client and server exceptions. RESTful client methods will only throw exceptions which are subclasses of this exception type, and RESTful server methods should also only call
 * subclasses of this exception type.
 * <p>
 * HAPI provides a number of subclasses of BaseServerResponseException, and each one corresponds to a specific
 * HTTP status code. For example, if a IResourceProvider method throws
 * {@link ResourceNotFoundException}, this is a signal to the server that an <code>HTTP 404</code> should
 * be returned to the client.
 * </p>
 * <p>
 * <b>See:</b> A complete list of available exceptions is in the <a href="./package-summary.html">package summary</a>.
 * If an exception doesn't exist for a condition you want to represent, let us know by filing an
 * <a href="https://github.com/hapifhir/hapi-fhir/issues">issue in our tracker</a>. You may also
 * use {@link UnclassifiedServerFailureException} to represent any error code you want.
 * </p>
 */
public abstract class BaseServerResponseException extends RuntimeException {

	private static final Map<Integer, Class<? extends BaseServerResponseException>> ourStatusCodeToExceptionType = new HashMap<Integer, Class<? extends BaseServerResponseException>>();
	private static final long serialVersionUID = 1L;

	static {
		registerExceptionType(PayloadTooLargeException.STATUS_CODE, PayloadTooLargeException.class);
		registerExceptionType(AuthenticationException.STATUS_CODE, AuthenticationException.class);
		registerExceptionType(InternalErrorException.STATUS_CODE, InternalErrorException.class);
		registerExceptionType(InvalidRequestException.STATUS_CODE, InvalidRequestException.class);
		registerExceptionType(MethodNotAllowedException.STATUS_CODE, MethodNotAllowedException.class);
		registerExceptionType(NotImplementedOperationException.STATUS_CODE, NotImplementedOperationException.class);
		registerExceptionType(NotModifiedException.STATUS_CODE, NotModifiedException.class);
		registerExceptionType(ResourceNotFoundException.STATUS_CODE, ResourceNotFoundException.class);
		registerExceptionType(ResourceGoneException.STATUS_CODE, ResourceGoneException.class);
		registerExceptionType(PreconditionFailedException.STATUS_CODE, PreconditionFailedException.class);
		registerExceptionType(ResourceVersionConflictException.STATUS_CODE, ResourceVersionConflictException.class);
		registerExceptionType(UnprocessableEntityException.STATUS_CODE, UnprocessableEntityException.class);
		registerExceptionType(ForbiddenOperationException.STATUS_CODE, ForbiddenOperationException.class);
	}

	private List<String> myAdditionalMessages = null;
	private IBaseOperationOutcome myBaseOperationOutcome;
	private String myResponseBody;
	private Map<String, List<String>> myResponseHeaders;
	private String myResponseMimeType;
	private int myStatusCode;
	private boolean myErrorMessageTrusted;

	/**
	 * Constructor
	 *
	 * @param theStatusCode The HTTP status code corresponding to this problem
	 * @param theMessage    The message
	 */
	public 	/**
	 * Interceptor hook method. This method should not be called directly.
	 */
	BaseServerResponseException(int theStatusCode, String theMessage) {
		super(theMessage);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = null;
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode The HTTP status code corresponding to this problem
	 * @param theMessages   The messages
	 */
	public BaseServerResponseException(int theStatusCode, String... theMessages) {
		super(theMessages != null && theMessages.length > 0 ? theMessages[0] : null);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = null;
		if (theMessages != null && theMessages.length > 1) {
			myAdditionalMessages = Arrays.asList(Arrays.copyOfRange(theMessages, 1, theMessages.length, String[].class));
		}
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode           The HTTP status code corresponding to this problem
	 * @param theMessage              The message
	 * @param theBaseOperationOutcome An BaseOperationOutcome resource to return to the calling client (in a server) or the BaseOperationOutcome that was returned from the server (in a client)
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, IBaseOperationOutcome theBaseOperationOutcome) {
		super(theMessage);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode The HTTP status code corresponding to this problem
	 * @param theMessage    The message
	 * @param theCause      The cause
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theMessage, theCause);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = null;
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode           The HTTP status code corresponding to this problem
	 * @param theMessage              The message
	 * @param theCause                The underlying cause exception
	 * @param theBaseOperationOutcome An BaseOperationOutcome resource to return to the calling client (in a server) or the BaseOperationOutcome that was returned from the server (in a client)
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, Throwable theCause, IBaseOperationOutcome theBaseOperationOutcome) {
		super(theMessage, theCause);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode The HTTP status code corresponding to this problem
	 * @param theCause      The underlying cause exception
	 */
	public BaseServerResponseException(int theStatusCode, Throwable theCause) {
		super(theCause.getMessage(), theCause);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = null;
	}

	/**
	 * Constructor
	 *
	 * @param theStatusCode           The HTTP status code corresponding to this problem
	 * @param theCause                The underlying cause exception
	 * @param theBaseOperationOutcome An BaseOperationOutcome resource to return to the calling client (in a server) or the BaseOperationOutcome that was returned from the server (in a client)
	 */
	public BaseServerResponseException(int theStatusCode, Throwable theCause, IBaseOperationOutcome theBaseOperationOutcome) {
		super(theCause.toString(), theCause);
		myStatusCode = theStatusCode;
		myBaseOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * This flag can be used to signal to server infrastructure that the message supplied
	 * to this exception (ie to the constructor) is considered trusted and is safe to
	 * return to the calling client.
	 */
	public boolean isErrorMessageTrusted() {
		return myErrorMessageTrusted;
	}

	/**
	 * This flag can be used to signal to server infrastructure that the message supplied
	 * to this exception (ie to the constructor) is considered trusted and is safe to
	 * return to the calling client.
	 */
	public BaseServerResponseException setErrorMessageTrusted(boolean theErrorMessageTrusted) {
		myErrorMessageTrusted = theErrorMessageTrusted;
		return this;
	}

	/**
	 * Add a header which will be added to any responses
	 *
	 * @param theName  The header name
	 * @param theValue The header value
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 * @since 2.0
	 */
	public BaseServerResponseException addResponseHeader(String theName, String theValue) {
		Validate.notBlank(theName, "theName must not be null or empty");
		Validate.notBlank(theValue, "theValue must not be null or empty");
		if (getResponseHeaders().containsKey(theName) == false) {
			getResponseHeaders().put(theName, new ArrayList<>());
		}
		getResponseHeaders().get(theName).add(theValue);
		return this;
	}

	public List<String> getAdditionalMessages() {
		return myAdditionalMessages;
	}

	/**
	 * Returns the {@link IBaseOperationOutcome} resource if any which was supplied in the response, or <code>null</code>
	 */
	public IBaseOperationOutcome getOperationOutcome() {
		return myBaseOperationOutcome;
	}

	/**
	 * Sets the BaseOperationOutcome resource associated with this exception. In server implementations, this is the OperartionOutcome resource to include with the HTTP response. In client
	 * implementations you should not call this method.
	 *
	 * @param theBaseOperationOutcome The BaseOperationOutcome resource Sets the BaseOperationOutcome resource associated with this exception. In server implementations, this is the OperartionOutcome resource to include
	 *                                with the HTTP response. In client implementations you should not call this method.
	 */
	public void setOperationOutcome(IBaseOperationOutcome theBaseOperationOutcome) {
		myBaseOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * In a RESTful client, this method will be populated with the body of the HTTP respone if one was provided by the server, or <code>null</code> otherwise.
	 * <p>
	 * In a restful server, this method is currently ignored.
	 * </p>
	 */
	public String getResponseBody() {
		return myResponseBody;
	}

	/**
	 * This method is currently only called internally by HAPI, it should not be called by user code.
	 */
	public void setResponseBody(String theResponseBody) {
		myResponseBody = theResponseBody;
	}

	/**
	 * Returns a map containing any headers which should be added to the outgoing
	 * response. This methos creates the map if none exists, so it will never
	 * return <code>null</code>
	 *
	 * @since 2.0 (note that this method existed in previous versions of HAPI but the method
	 * signature has been changed from <code>Map&lt;String, String[]&gt;</code> to <code>Map&lt;String, List&lt;String&gt;&gt;</code>
	 */
	public Map<String, List<String>> getResponseHeaders() {
		if (myResponseHeaders == null) {
			myResponseHeaders = new HashMap<>();
		}
		return myResponseHeaders;
	}

	/**
	 * In a RESTful client, this method will be populated with the HTTP status code that was returned with the HTTP response.
	 * <p>
	 * In a restful server, this method is currently ignored.
	 * </p>
	 */
	public String getResponseMimeType() {
		return myResponseMimeType;
	}

	/**
	 * This method is currently only called internally by HAPI, it should not be called by user code.
	 */
	public void setResponseMimeType(String theResponseMimeType) {
		myResponseMimeType = theResponseMimeType;
	}

	/**
	 * Returns the HTTP status code corresponding to this problem
	 */
	public int getStatusCode() {
		return myStatusCode;
	}

	/**
	 * Does the exception have any headers which should be added to the outgoing response?
	 *
	 * @see #getResponseHeaders()
	 * @since 2.0
	 */
	public boolean hasResponseHeaders() {
		return myResponseHeaders != null && myResponseHeaders.isEmpty() == false;
	}

	/**
	 * For unit tests only
	 */
	static boolean isExceptionTypeRegistered(Class<?> theType) {
		return ourStatusCodeToExceptionType.values().contains(theType);
	}

	public static BaseServerResponseException newInstance(int theStatusCode, String theMessage) {
		if (ourStatusCodeToExceptionType.containsKey(theStatusCode)) {
			try {
				return ourStatusCodeToExceptionType.get(theStatusCode).getConstructor(new Class[]{String.class}).newInstance(theMessage);
			} catch (InstantiationException e) {
				throw new InternalErrorException(Msg.code(1912) + e);
			} catch (IllegalAccessException e) {
				throw new InternalErrorException(Msg.code(1913) + e);
			} catch (IllegalArgumentException e) {
				throw new InternalErrorException(Msg.code(1914) + e);
			} catch (InvocationTargetException e) {
				throw new InternalErrorException(Msg.code(1915) + e);
			} catch (NoSuchMethodException e) {
				throw new InternalErrorException(Msg.code(1916) + e);
			} catch (SecurityException e) {
				throw new InternalErrorException(Msg.code(1917) + e);
			}
		}
		return new UnclassifiedServerFailureException(theStatusCode, theMessage);
	}

	static void registerExceptionType(int theStatusCode, Class<? extends BaseServerResponseException> theType) {
		if (ourStatusCodeToExceptionType.containsKey(theStatusCode)) {
			throw new Error(Msg.code(1918) + "Can not register " + theType + " to status code " + theStatusCode + " because " + ourStatusCodeToExceptionType.get(theStatusCode) + " already registers that code");
		}
		ourStatusCodeToExceptionType.put(theStatusCode, theType);
	}

}
