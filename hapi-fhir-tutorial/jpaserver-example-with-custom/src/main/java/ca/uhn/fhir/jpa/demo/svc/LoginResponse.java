package ca.uhn.fhir.jpa.demo.svc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

	@JsonProperty(value = "success")
	private boolean mySuccess;

	public boolean isSuccess() {
		return mySuccess;
	}

	public void setSuccess(boolean theSuccess) {
		mySuccess = theSuccess;
	}

}
