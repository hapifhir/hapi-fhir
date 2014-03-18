package ca.uhn.fhir.rest.param;

import java.util.List;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IParameter {

	public abstract List<List<String>> encode(Object theObject) throws InternalErrorException;

	public abstract String getName();

	public abstract Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException;

	public abstract boolean isRequired();

}