package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;

import java.util.Optional;

/**
 * This class is used to capture the details of an operation's ID parameter to be used by {@link OperationMethodBinding}
 */
class OperationIdParamDetails {

	@Nullable
	private final IdParam myIdParam;

	@Nullable
	private final Integer myIdParamIndex;

	public static final OperationIdParamDetails EMPTY = new OperationIdParamDetails(null, null);

	public OperationIdParamDetails(@Nullable IdParam theIdParam, @Nullable Integer theIdParamIndex) {
		myIdParam = theIdParam;
		myIdParamIndex = theIdParamIndex;
	}

	public boolean isFound() {
		return myIdParamIndex != null;
	}

	public boolean setOrReturnPreviousValue(boolean thePreviousValue) {
		return Optional.ofNullable(myIdParam).map(IdParam::optional).orElse(thePreviousValue);
	}

	public Object[] alterMethodParamsIfNeeded(RequestDetails theRequest, Object[] theMethodParams) {
		if (myIdParamIndex == null) {
			// no-op
			return theMethodParams;
		}

		final Object[] clonedMethodParams = theMethodParams.clone();

		clonedMethodParams[myIdParamIndex] = theRequest.getId();

		return clonedMethodParams;
	}
}
