package ca.uhn.fhir.rest.server.interceptor.validation.address;

public class AddressValidationException extends RuntimeException {

	public AddressValidationException() {}

	public AddressValidationException(String theMessage) {
		super(theMessage);
	}

	public AddressValidationException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	public AddressValidationException(Throwable theCause) {
		super(theCause);
	}

}
