package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingUtil;
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
	private List<IParameter> myParameters;
	private boolean myReturnVoid;

	public BaseOutcomeReturningMethodBinding(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theProvider);
		myParameters = Util.getResourceParameters(theMethod);

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			if (!allowVoidReturnType()) {
				throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " is a @" + theMethodAnnotation.getSimpleName()
						+ " method but it does not return " + MethodOutcome.class);
			} else if (theMethod.getReturnType() == void.class) {
				myReturnVoid = true;
			}
		}

	}

	public abstract String getResourceName();

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		switch (theResponseStatusCode) {
		case Constants.STATUS_HTTP_200_OK:
		case Constants.STATUS_HTTP_201_CREATED:
		case Constants.STATUS_HTTP_204_NO_CONTENT:
			if (myReturnVoid) {
				return null;
			}
			List<String> locationHeaders = theHeaders.get("location");
			MethodOutcome retVal = new MethodOutcome();
			if (locationHeaders != null && locationHeaders.size() > 0) {
				String locationHeader = locationHeaders.get(0);
				parseContentLocation(retVal, locationHeader);
			}
			if (theResponseStatusCode != Constants.STATUS_HTTP_204_NO_CONTENT) {
				EncodingUtil ct = EncodingUtil.forContentType(theResponseMimeType);
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
						IParser parser = ct.newParser(getContext());
						OperationOutcome outcome = parser.parseResource(OperationOutcome.class, reader);
						retVal.setOperationOutcome(outcome);
					}

				} else {
					ourLog.debug("Ignoring response content of type: {}", theResponseMimeType);
				}
			}
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
		Object[] params = new Object[myParameters.size()];

		addParametersForServerRequest(theRequest, params);

		MethodOutcome response;
		try {
			response = (MethodOutcome) this.getMethod().invoke(getProvider(), params);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (IllegalArgumentException e) {
			throw new InternalErrorException(e);
		} catch (InvocationTargetException e) {
			throw new InternalErrorException(e);
		}

		if (response == null) {
			if (myReturnVoid == false) {
				throw new ConfigurationException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
			} else {
				theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT);
			}
		} else if (!myReturnVoid) {
			if (response.isCreated()) {
				theResponse.setStatus(Constants.STATUS_HTTP_201_CREATED);
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
			} else {
				theResponse.setStatus(Constants.STATUS_HTTP_200_OK);
			}
		} else {
			theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT);
		}

		theServer.addHapiHeader(theResponse);

		Writer writer = theResponse.getWriter();
		try {
			if (response != null) {
				OperationOutcome outcome = new OperationOutcome();
				if (response.getOperationOutcome() != null && response.getOperationOutcome().getIssue() != null) {
					outcome.getIssue().addAll(response.getOperationOutcome().getIssue());
				}
				EncodingUtil encoding = BaseMethodBinding.determineResponseEncoding(theRequest.getServletRequest(), theRequest.getParameters());
				theResponse.setContentType(encoding.getResourceContentType());
				IParser parser = encoding.newParser(getContext());
				parser.encodeResourceToWriter(outcome, writer);
			}
		} finally {
			writer.close();
		}
		// getMethod().in
	}

	protected abstract void addParametersForServerRequest(Request theRequest, Object[] theParams);

	public boolean isReturnVoid() {
		return myReturnVoid;
	}

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
		if (StringUtils.isNotBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

	/**
	 * Subclasses may override to allow a void method return type, which is allowable for some methods (e.g. delete)
	 */
	protected boolean allowVoidReturnType() {
		return false;
	}

	protected abstract BaseClientInvocation createClientInvocation(Object[] theArgs, IResource resource, String resourceName);

	protected List<IParameter> getParameters() {
		return myParameters;
	}

	protected void parseContentLocation(MethodOutcome theOutcomeToPopulate, String theLocationHeader) {
		String resourceNamePart = "/" + getResourceName() + "/";
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

	protected abstract Set<RequestType> provideAllowableRequestTypes();

}
