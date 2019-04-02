package ca.uhn.fhir.jpa.model.search;

public class PerformanceMessage {

	private String myMessage;

	public String getMessage() {
		return myMessage;
	}

	public PerformanceMessage setMessage(String theMessage) {
		myMessage = theMessage;
		return this;
	}

}
