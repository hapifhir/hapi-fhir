package ca.uhn.fhir.rest.server.interceptor.validation.address;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.HashMap;
import java.util.Map;

public class AddressValidationResult {

	private boolean myIsValid;
	private String myValidatedAddressString;
	private Map<String, String> myValidationResults = new HashMap<>();
	private String myRawResponse;
	private IBase myValidatedAddress;

	public boolean isValid() {
		return myIsValid;
	}

	public void setValid(boolean theIsValid) {
		this.myIsValid = theIsValid;
	}

	public Map<String, String> getValidationResults() {
		return myValidationResults;
	}

	public void setValidationResults(Map<String, String> myValidationResults) {
		this.myValidationResults = myValidationResults;
	}

	public String getValidatedAddressString() {
		return myValidatedAddressString;
	}

	public void setValidatedAddressString(String theValidatedAddressString) {
		this.myValidatedAddressString = theValidatedAddressString;
	}

	public IBase getValidatedAddress() {
		return myValidatedAddress;
	}

	public void setValidatedAddress(IBase theValidatedAddress) {
		this.myValidatedAddress = theValidatedAddress;
	}

	public String getRawResponse() {
		return myRawResponse;
	}

	public void setRawResponse(String theRawResponse) {
		this.myRawResponse = theRawResponse;
	}
}
