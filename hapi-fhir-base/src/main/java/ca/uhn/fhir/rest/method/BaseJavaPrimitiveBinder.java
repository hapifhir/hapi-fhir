package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

abstract class BaseJavaPrimitiveBinder<T>implements IParamBinder<T> {

	public BaseJavaPrimitiveBinder() {
		super();
	}

	protected abstract String doEncode(T theString);

	protected abstract T doParse(String theString);

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, T theString) throws InternalErrorException {
		String retVal = doEncode(theString);
		if (isBlank(retVal)) {
			return Collections.emptyList();
		}
		List<?> retValList = Collections.singletonList(MethodUtil.singleton(new StringParam(retVal)));
		return (List<IQueryParameterOr<?>>) retValList;
	}

	@Override
	public T parse(String theName, List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		if (theParams.size() == 0 || theParams.get(0).size() == 0) {
			return null;
		}
		if (theParams.size() > 1 || theParams.get(0).size() > 1) {
			throw new InvalidRequestException("Multiple values detected for non-repeatable parameter '" + theName + "'. This server is not configured to allow multiple (AND) values for this param.");
		}
	
		T value = doParse(theParams.get(0).get(0));
		return value;
	}

}