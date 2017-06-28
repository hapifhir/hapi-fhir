package ca.uhn.fhir.rest.client.method;

import java.io.Reader;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding<MethodOutcome> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);

	private static EnumSet<RestOperationTypeEnum> ourOperationsWhichAllowPreferHeader = EnumSet.of(RestOperationTypeEnum.CREATE, RestOperationTypeEnum.UPDATE);

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

	/**
	 * Subclasses may override to allow a void method return type, which is allowable for some methods (e.g. delete)
	 */
	protected boolean allowVoidReturnType() {
		return false;
	}

	protected abstract BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource resource);

	/**
	 * For servers, this method will match only incoming requests that match the given operation, or which have no
	 * operation in the URL if this method returns null.
	 */
	protected abstract String getMatchingOperation();

	private int getOperationStatus(MethodOutcome response) {
		switch (getRestOperationType()) {
		case CREATE:
			if (response == null) {
				throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null, which is not allowed for create operation");
			}
			if (response.getCreated() == null || Boolean.TRUE.equals(response.getCreated())) {
				return Constants.STATUS_HTTP_201_CREATED;
			}
			return Constants.STATUS_HTTP_200_OK;

		case UPDATE:
			if (response == null || response.getCreated() == null || Boolean.FALSE.equals(response.getCreated())) {
				return Constants.STATUS_HTTP_200_OK;
			}
			return Constants.STATUS_HTTP_201_CREATED;

		case VALIDATE:
		case DELETE:
		default:
			if (response == null) {
				if (isReturnVoid() == false) {
					throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
				}
				return Constants.STATUS_HTTP_204_NO_CONTENT;
			}
			if (response.getOperationOutcome() == null) {
				return Constants.STATUS_HTTP_204_NO_CONTENT;
			}
			return Constants.STATUS_HTTP_200_OK;
		}
	}


	@Override
	public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		if (theResponseStatusCode >= 200 && theResponseStatusCode < 300) {
			if (myReturnVoid) {
				return null;
			}
			MethodOutcome retVal = MethodUtil.process2xxResponse(getContext(), theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			return retVal;
		}
		throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
	}

	public boolean isReturnVoid() {
		return myReturnVoid;
	}

	protected abstract Set<RequestTypeEnum> provideAllowableRequestTypes();



	protected static void parseContentLocation(FhirContext theContext, MethodOutcome theOutcomeToPopulate, String theLocationHeader) {
		if (StringUtils.isBlank(theLocationHeader)) {
			return;
		}

		IIdType id = theContext.getVersion().newIdType();
		id.setValue(theLocationHeader);
		theOutcomeToPopulate.setId(id);
	}

}
