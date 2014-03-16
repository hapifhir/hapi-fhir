package ca.uhn.fhir.rest.param;

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class QueryParameterOrBinder implements IParamBinder {
	private final Class<? extends IQueryParameterOr> myType;

	QueryParameterOrBinder(Class<? extends IQueryParameterOr> theType) {
		myType = theType;
	}

	@Override
	public List<List<String>> encode(Object theString) throws InternalErrorException {
		List<String> retVal = ((IQueryParameterOr) theString).getValuesAsQueryTokens();
		return Collections.singletonList(retVal);
	}

	@Override
	public Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException {
		IQueryParameterOr dt;
		try {
			dt = myType.newInstance();
			if (theString.size() == 0 || theString.get(0).size() == 0) {
				return dt;
			}
			if (theString.size() > 1) {
				throw new InvalidRequestException("Multiple values detected");
			}
			
			dt.setValuesAsQueryTokens(theString.get(0));
		} catch (InstantiationException e) {
			throw new InternalErrorException(e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (SecurityException e) {
			throw new InternalErrorException(e);
		}
		return dt;
	}
}