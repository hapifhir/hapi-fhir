package ca.uhn.fhir.batch2.api;

public class JobExecutionFailedException extends RuntimeException {

	private static final long serialVersionUID = 4871161727526723730L;

	/**
	 * Constructor
	 */
	public JobExecutionFailedException(String theMessage) {
		super(theMessage);
	}
}
