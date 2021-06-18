package ca.uhn.fhir.jpa.mdm.svc.candidate;

public class TooManyCandidatesException extends RuntimeException {
	public TooManyCandidatesException(String theMessage) {
		super(theMessage);
	}
}
