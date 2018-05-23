package ca.uhn.fhir.jpa.util;

public interface IReindexController {

	/**
	 * This method is called automatically by the scheduler
	 */
	void performReindexingPass();

	/**
	 * This method requests that the reindex process happen as soon as possible
	 */
	void requestReindex();
}
