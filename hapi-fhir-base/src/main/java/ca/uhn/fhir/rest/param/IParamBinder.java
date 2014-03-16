package ca.uhn.fhir.rest.param;

import java.util.List;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

interface IParamBinder {
	
	List<List<String>> encode(Object theString) throws InternalErrorException;

	Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException;

}