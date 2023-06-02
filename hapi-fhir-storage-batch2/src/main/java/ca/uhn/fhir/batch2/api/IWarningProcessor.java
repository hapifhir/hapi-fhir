package ca.uhn.fhir.batch2.api;

public interface IWarningProcessor {

	/**
	 * Data Sink may invoke this method to indicate that an error occurred during
	 * processing in work chunks but that it is non-fatal and should be saved as a warning.
	 *
	 * @param theErrorMessage An error message to be processed.
	 */
	public void recoverWarningMessage(String theErrorMessage);

	public String getRecoveredWarningMessage();
}
