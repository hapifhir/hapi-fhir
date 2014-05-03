package ca.uhn.fhir.rest.method;

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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionNotSpecifiedException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);
	private boolean myReturnVoid;

	public BaseOutcomeReturningMethodBinding(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theProvider);

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			if (!allowVoidReturnType()) {
				throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " is a @" + theMethodAnnotation.getSimpleName() + " method but it does not return " + MethodOutcome.class);
			} else if (theMethod.getReturnType() == void.class) {
				myReturnVoid = true;
			}
		}
	}

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		switch (theResponseStatusCode) {
		case Constants.STATUS_HTTP_200_OK:
		case Constants.STATUS_HTTP_201_CREATED:
		case Constants.STATUS_HTTP_204_NO_CONTENT:
			if (myReturnVoid) {
				return null;
			}
			MethodOutcome retVal = process2xxResponse(getContext(), getResourceName(), theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			return retVal;
		case Constants.STATUS_HTTP_400_BAD_REQUEST:
			throw new InvalidRequestException("Server responded with: " + IOUtils.toString(theResponseReader));
		case Constants.STATUS_HTTP_404_NOT_FOUND:
			throw new ResourceNotFoundException("Server responded with: " + IOUtils.toString(theResponseReader));
		case Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED:
			throw new MethodNotAllowedException("Server responded with: " + IOUtils.toString(theResponseReader));
		case Constants.STATUS_HTTP_409_CONFLICT:
			throw new ResourceVersionConflictException("Server responded with: " + IOUtils.toString(theResponseReader));
		case Constants.STATUS_HTTP_412_PRECONDITION_FAILED:
			throw new ResourceVersionNotSpecifiedException("Server responded with: " + IOUtils.toString(theResponseReader));
		case Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY:
			IParser parser = createAppropriateParser(theResponseMimeType, theResponseReader, theResponseStatusCode);
			OperationOutcome operationOutcome = parser.parseResource(OperationOutcome.class, theResponseReader);
			throw new UnprocessableEntityException(operationOutcome);
		default:
			throw new UnclassifiedServerFailureException(theResponseStatusCode, IOUtils.toString(theResponseReader));
		}

	}

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		EncodingEnum encoding = BaseMethodBinding.determineResponseEncoding(theRequest.getServletRequest(), theRequest.getParameters());
		IParser parser = encoding.newParser(getContext());
		IResource resource;
		if (requestContainsResource()) {
			resource = parser.parseResource(theRequest.getInputReader());
		} else {
			resource = null;
		}

		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param == null) {
				continue;
			}
			params[i] = param.translateQueryParametersIntoServerArgument(theRequest, resource);
		}

		addParametersForServerRequest(theRequest, params);

		MethodOutcome response;
		try {
			response = (MethodOutcome) invokeServerMethod(getProvider(), params);
		} catch (InternalErrorException e) {
			ourLog.error("Internal error during method invocation", e);
			streamOperationOutcome(e, theServer, encoding, theResponse, theRequest);
			return;
		} catch (BaseServerResponseException e) {
			ourLog.info("Exception during method invocation: " + e.getMessage());
			streamOperationOutcome(e, theServer, encoding, theResponse, theRequest);
			return;
		}

		if (getResourceOperationType() == RestfulOperationTypeEnum.CREATE) {
			if (response == null) {
				throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null, which is not allowed for create operation");
			}
			theResponse.setStatus(Constants.STATUS_HTTP_201_CREATED);
			addLocationHeader(theRequest, theResponse, response);
		} else if (response == null) {
			if (isReturnVoid() == false) {
				throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
			}
			theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT);
		} else {
			if (response.getOperationOutcome() == null) {
				theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT);
			} else {
				theResponse.setStatus(Constants.STATUS_HTTP_200_OK);
			}
			if (getResourceOperationType() == RestfulOperationTypeEnum.UPDATE) {
				addLocationHeader(theRequest, theResponse, response);
			}

		}

		theServer.addHeadersToResponse(theResponse);

		if (response != null && response.getOperationOutcome() != null) {
			theResponse.setContentType(encoding.getResourceContentType());
			Writer writer = theResponse.getWriter();
			parser.setPrettyPrint(prettyPrintResponse(theRequest));
			try {
				parser.encodeResourceToWriter(response.getOperationOutcome(), writer);
			} finally {
				writer.close();
			}
		} else {
			theResponse.setContentType(Constants.CT_TEXT);
			Writer writer = theResponse.getWriter();
			writer.close();
		}

		// getMethod().in
	}

	public boolean isReturnVoid() {
		return myReturnVoid;
	}

	/*
	 * @Override public void invokeServer(RestfulServer theServer, Request
	 * theRequest, HttpServletResponse theResponse) throws
	 * BaseServerResponseException, IOException { Object[] params = new
	 * Object[getParameters().size()]; for (int i = 0; i <
	 * getParameters().size(); i++) { IParameter param = getParameters().get(i);
	 * if (param != null) { params[i] =
	 * param.translateQueryParametersIntoServerArgument(theRequest, null); } }
	 * 
	 * addParametersForServerRequest(theRequest, params);
	 * 
	 * MethodOutcome response = (MethodOutcome)
	 * invokeServerMethod(getProvider(), params);
	 * 
	 * if (response == null) { if (myReturnVoid == false) { throw new
	 * ConfigurationException("Method " + getMethod().getName() + " in type " +
	 * getMethod().getDeclaringClass().getCanonicalName() + " returned null"); }
	 * else { theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT); } }
	 * else if (!myReturnVoid) { if (response.isCreated()) {
	 * theResponse.setStatus(Constants.STATUS_HTTP_201_CREATED); StringBuilder b
	 * = new StringBuilder(); b.append(theRequest.getFhirServerBase());
	 * b.append('/'); b.append(getResourceName()); b.append('/');
	 * b.append(response.getId().getValue()); if (response.getVersionId() !=
	 * null && response.getVersionId().isEmpty() == false) {
	 * b.append("/_history/"); b.append(response.getVersionId().getValue()); }
	 * theResponse.addHeader("Location", b.toString()); } else {
	 * theResponse.setStatus(Constants.STATUS_HTTP_200_OK); } } else {
	 * theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT); }
	 * 
	 * theServer.addHeadersToResponse(theResponse);
	 * 
	 * Writer writer = theResponse.getWriter(); try { if (response != null) {
	 * OperationOutcome outcome = new OperationOutcome(); if
	 * (response.getOperationOutcome() != null &&
	 * response.getOperationOutcome().getIssue() != null) {
	 * outcome.getIssue().addAll(response.getOperationOutcome().getIssue()); }
	 * EncodingUtil encoding =
	 * BaseMethodBinding.determineResponseEncoding(theRequest
	 * .getServletRequest(), theRequest.getParameters());
	 * theResponse.setContentType(encoding.getResourceContentType()); IParser
	 * parser = encoding.newParser(getContext());
	 * parser.encodeResourceToWriter(outcome, writer); } } finally {
	 * writer.close(); } // getMethod().in }
	 */

	@Override
	public boolean matches(Request theRequest) {
		Set<RequestType> allowableRequestTypes = provideAllowableRequestTypes();
		RequestType requestType = theRequest.getRequestType();
		if (!allowableRequestTypes.contains(requestType)) {
			return false;
		}
		if (!getResourceName().equals(theRequest.getResourceName())) {
			return false;
		}
		if (getMatchingOperation() == null && StringUtils.isNotBlank(theRequest.getOperation())) {
			return false;
		}
		if (getMatchingOperation() != null && !getMatchingOperation().equals(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

	private void addLocationHeader(Request theRequest, HttpServletResponse theResponse, MethodOutcome response) {
		StringBuilder b = new StringBuilder();
		b.append(theRequest.getFhirServerBase());
		b.append('/');
		b.append(getResourceName());
		b.append('/');
		b.append(response.getId().getValue());
		if (response.getVersionId() != null && response.getVersionId().isEmpty() == false) {
			b.append("/_history/");
			b.append(response.getVersionId().getValue());
		}
		theResponse.addHeader("Location", b.toString());
	}

	protected abstract void addParametersForServerRequest(Request theRequest, Object[] theParams);

	/**
	 * Subclasses may override to allow a void method return type, which is
	 * allowable for some methods (e.g. delete)
	 */
	protected boolean allowVoidReturnType() {
		return false;
	}

	protected abstract BaseClientInvocation createClientInvocation(Object[] theArgs, IResource resource);

	/**
	 * For servers, this method will match only incoming requests that match the
	 * given operation, or which have no operation in the URL if this method
	 * returns null.
	 */
	protected abstract String getMatchingOperation();

	protected abstract Set<RequestType> provideAllowableRequestTypes();

	/**
	 * Subclasses may override if the incoming request should not contain a
	 * resource
	 */
	protected boolean requestContainsResource() {
		return true;
	}

	protected void streamOperationOutcome(BaseServerResponseException theE, RestfulServer theServer, EncodingEnum theEncoding, HttpServletResponse theResponse, Request theRequest) throws IOException {
		theResponse.setStatus(theE.getStatusCode());

		theServer.addHeadersToResponse(theResponse);

		if (theE.getOperationOutcome() != null) {
			theResponse.setContentType(theEncoding.getResourceContentType());
			IParser parser = theEncoding.newParser(theServer.getFhirContext());
			parser.setPrettyPrint(prettyPrintResponse(theRequest));
			Writer writer = theResponse.getWriter();
			try {
				parser.encodeResourceToWriter(theE.getOperationOutcome(), writer);
			} finally {
				writer.close();
			}
		} else {
			theResponse.setContentType(Constants.CT_TEXT);
			Writer writer = theResponse.getWriter();
			try {
				writer.append(theE.getMessage());
			} finally {
				writer.close();
			}
		}
	}

	public static MethodOutcome process2xxResponse(FhirContext theContext, String theResourceName, int theResponseStatusCode, String theResponseMimeType, Reader theResponseReader, Map<String, List<String>> theHeaders) {
		List<String> locationHeaders = theHeaders.get("location");
		MethodOutcome retVal = new MethodOutcome();
		if (locationHeaders != null && locationHeaders.size() > 0) {
			String locationHeader = locationHeaders.get(0);
			parseContentLocation(retVal, theResourceName, locationHeader);
		}
		if (theResponseStatusCode != Constants.STATUS_HTTP_204_NO_CONTENT) {
			EncodingEnum ct = EncodingEnum.forContentType(theResponseMimeType);
			if (ct != null) {
				PushbackReader reader = new PushbackReader(theResponseReader);

				try {
					int firstByte = reader.read();
					if (firstByte == -1) {
						ourLog.debug("No content in response, not going to read");
						reader = null;
					} else {
						reader.unread(firstByte);
					}
				} catch (IOException e) {
					ourLog.debug("No content in response, not going to read", e);
					reader = null;
				}

				if (reader != null) {
					IParser parser = ct.newParser(theContext);
					OperationOutcome outcome = parser.parseResource(OperationOutcome.class, reader);
					retVal.setOperationOutcome(outcome);
				}

			} else {
				ourLog.debug("Ignoring response content of type: {}", theResponseMimeType);
			}
		}
		return retVal;
	}

	protected static void parseContentLocation(MethodOutcome theOutcomeToPopulate, String theResourceName, String theLocationHeader) {
		String resourceNamePart = "/" + theResourceName + "/";
		int resourceIndex = theLocationHeader.lastIndexOf(resourceNamePart);
		if (resourceIndex > -1) {
			int idIndexStart = resourceIndex + resourceNamePart.length();
			int idIndexEnd = theLocationHeader.indexOf('/', idIndexStart);
			if (idIndexEnd == -1) {
				theOutcomeToPopulate.setId(new IdDt(theLocationHeader.substring(idIndexStart)));
			} else {
				theOutcomeToPopulate.setId(new IdDt(theLocationHeader.substring(idIndexStart, idIndexEnd)));
				String versionIdPart = "/_history/";
				int historyIdStart = theLocationHeader.indexOf(versionIdPart, idIndexEnd);
				if (historyIdStart != -1) {
					theOutcomeToPopulate.setVersionId(new IdDt(theLocationHeader.substring(historyIdStart + versionIdPart.length())));
				}
			}
		}
	}

}
