package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.annotations.VisibleForTesting;

import java.lang.reflect.Method;
import java.util.Collection;

// LUKETODO:  javadoc
public class HeaderParameter implements IParameter {

	private final String myHeaderName;

	public HeaderParameter(String theHeaderName) {
		myHeaderName = theHeaderName;
	}

	@VisibleForTesting
	public String getValue() {
		return myHeaderName;
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(
			RequestDetails theRequest, BaseMethodBinding theMethodBinding)
			throws InternalErrorException, InvalidRequestException {

		return theRequest.getHeader(myHeaderName);
	}

	@Override
	public void initializeTypes(
			Method theMethod,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<?> theParameterType) {
		// LUKETODO:  anything to do here?
	}
}
