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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding<MethodOutcome> {
	private static final String LABEL = "label=\"";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);
	private static final String SCHEME = "scheme=\"";

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
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
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

	@Override
	public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		switch (theResponseStatusCode) {
		case Constants.STATUS_HTTP_200_OK:
		case Constants.STATUS_HTTP_201_CREATED:
		case Constants.STATUS_HTTP_204_NO_CONTENT:
			if (myReturnVoid) {
				return null;
			}
			MethodOutcome retVal = process2xxResponse(getContext(), getResourceName(), theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			return retVal;
		default:
			throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
		}

	}

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		EncodingEnum encoding = determineResponseEncoding(theRequest);
		IParser parser = encoding.newParser(getContext());
		IResource resource;
		if (requestContainsResource()) {
			resource = parser.parseResource(theRequest.getInputReader());
			TagList tagList = new TagList();
			for (Enumeration<String> enumeration = theRequest.getServletRequest().getHeaders(Constants.HEADER_CATEGORY); enumeration.hasMoreElements();) {
				String nextTagComplete = enumeration.nextElement();
				StringBuilder next = new StringBuilder(nextTagComplete);
				parseTagValue(tagList, nextTagComplete, next);
			}
			if (tagList.isEmpty() == false) {
				resource.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
			}
		} else {
			resource = null;
		}

		Object[] params = createParametersForServerRequest(theRequest, resource);
		addParametersForServerRequest(theRequest, params);

		MethodOutcome response;
		try {
			response = (MethodOutcome) invokeServerMethod(params);
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

	public boolean isReturnVoid() {
		return myReturnVoid;
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

	private void parseTagValue(TagList theTagList, String theCompleteHeaderValue, StringBuilder theBuffer) {
		int firstSemicolon = theBuffer.indexOf(";");
		int deleteTo;
		if (firstSemicolon == -1) {
			firstSemicolon = theBuffer.indexOf(",");
			if (firstSemicolon == -1) {
				firstSemicolon = theBuffer.length();
				deleteTo = theBuffer.length();
			} else {
				deleteTo = firstSemicolon;
			}
		} else {
			deleteTo = firstSemicolon + 1;
		}

		String term = theBuffer.substring(0, firstSemicolon);
		String scheme = null;
		String label = null;
		if (isBlank(term)) {
			return;
		}

		theBuffer.delete(0, deleteTo);
		while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
			theBuffer.deleteCharAt(0);
		}

		while (theBuffer.length() > 0) {
			boolean foundSomething = false;
			if (theBuffer.length() > SCHEME.length() && theBuffer.substring(0, SCHEME.length()).equals(SCHEME)) {
				int closeIdx = theBuffer.indexOf("\"", SCHEME.length());
				scheme = theBuffer.substring(SCHEME.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			if (theBuffer.length() > LABEL.length() && theBuffer.substring(0, LABEL.length()).equals(LABEL)) {
				int closeIdx = theBuffer.indexOf("\"", LABEL.length());
				label = theBuffer.substring(LABEL.length(), closeIdx);
				theBuffer.delete(0, closeIdx + 1);
				foundSomething = true;
			}
			// TODO: support enc2231-string as described in
			// http://tools.ietf.org/html/draft-johnston-http-category-header-02
			// TODO: support multiple tags in one header as described in
			// http://hl7.org/implement/standards/fhir/http.html#tags

			while (theBuffer.length() > 0 && (theBuffer.charAt(0) == ' ' || theBuffer.charAt(0) == ';')) {
				theBuffer.deleteCharAt(0);
			}

			if (!foundSomething) {
				break;
			}
		}

		if (theBuffer.length() > 0 && theBuffer.charAt(0) == ',') {
			theBuffer.deleteCharAt(0);
			while (theBuffer.length() > 0 && theBuffer.charAt(0) == ' ') {
				theBuffer.deleteCharAt(0);
			}
			theTagList.add(new Tag(term, label, scheme));
			parseTagValue(theTagList, theCompleteHeaderValue, theBuffer);
		} else {
			theTagList.add(new Tag(term, label, scheme));
		}

		if (theBuffer.length() > 0) {
			ourLog.warn("Ignoring extra text at the end of " + Constants.HEADER_CATEGORY + " tag '" + theBuffer.toString() + "' - Complete tag value was: " + theCompleteHeaderValue);
		}

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
					IResource outcome = parser.parseResource(reader);
					if (outcome instanceof OperationOutcome) {
						retVal.setOperationOutcome((OperationOutcome) outcome);
					}
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
