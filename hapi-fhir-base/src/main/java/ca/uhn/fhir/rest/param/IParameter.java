package ca.uhn.fhir.rest.param;

import java.util.List;

import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IParameter {

	List<List<String>> encode(Object theObject) throws InternalErrorException;

	String getName();

	Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException;

	boolean isRequired();
	
	/**
	 * Parameter should return true if {@link #parse(List)} should be called even
	 * if the query string contained no values for the given parameter 
	 */
	boolean handlesMissing();

	SearchParamTypeEnum getParamType();

}