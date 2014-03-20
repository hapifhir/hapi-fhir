package ca.uhn.fhir.rest.param;

import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class QueryParameterAndBinder implements IParamBinder {
	private final Class<? extends IQueryParameterAnd> myType;

	QueryParameterAndBinder(Class<? extends IQueryParameterAnd> theType) {
		myType = theType;
	}

	@Override
	public List<List<String>> encode(Object theString) throws InternalErrorException {
		List<List<String>> retVal = ((IQueryParameterAnd) theString).getValuesAsQueryTokens();
		return retVal;
	}

	@Override
	public Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException {
		IQueryParameterAnd dt;
		try {
			dt = myType.newInstance();
			dt.setValuesAsQueryTokens(theString);
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