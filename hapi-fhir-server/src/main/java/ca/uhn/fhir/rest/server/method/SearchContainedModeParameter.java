package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SearchContainedModeParameter implements IParameter {

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		return getTypeForRequestOrThrowInvalidRequestException(theRequest);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore
	}

	public static SearchContainedModeEnum getTypeForRequestOrThrowInvalidRequestException(RequestDetails theRequest) {
		String[] paramValues = theRequest.getParameters().getOrDefault(Constants.PARAM_CONTAINED, Constants.EMPTY_STRING_ARRAY);
		if (paramValues.length > 0 && isNotBlank(paramValues[0])) {
			return SearchContainedModeEnum.fromCode(paramValues[0]);
		}
		return null;
	}

}
