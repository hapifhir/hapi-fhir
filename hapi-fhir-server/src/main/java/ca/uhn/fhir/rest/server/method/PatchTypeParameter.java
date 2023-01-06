package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

class PatchTypeParameter implements IParameter {

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding theMethodBinding) throws InternalErrorException, InvalidRequestException {
		return getTypeForRequestOrThrowInvalidRequestException(theRequest);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore
	}

	public static PatchTypeEnum getTypeForRequestOrThrowInvalidRequestException(RequestDetails theRequest) {
		String contentTypeAll = defaultString(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE));

		int semicolonIndex = contentTypeAll.indexOf(';');
		if (semicolonIndex > 0) {
			contentTypeAll = contentTypeAll.substring(0, semicolonIndex);
		}

		contentTypeAll = trim(contentTypeAll);

		return PatchTypeEnum.forContentTypeOrThrowInvalidRequestException(theRequest.getFhirContext(), contentTypeAll);
	}

}
