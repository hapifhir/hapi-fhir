package ca.uhn.fhir.rest.server.interceptor.validation.fields;

public interface IValidator {

	public boolean isValid(String theString);

}
