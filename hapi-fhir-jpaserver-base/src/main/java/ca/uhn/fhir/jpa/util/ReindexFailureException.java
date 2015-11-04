package ca.uhn.fhir.jpa.util;

public class ReindexFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private Long myResourceId;

	public ReindexFailureException(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public Long getResourceId() {
		return myResourceId;
	}

}
