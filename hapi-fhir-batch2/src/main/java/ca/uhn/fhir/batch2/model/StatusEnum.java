package ca.uhn.fhir.batch2.model;

public enum StatusEnum {

	/**
	 * Task is waiting to execute and should begin with no intervention required.
	 */
	QUEUED,

	/**
	 * Task is current executing
	 */
	IN_PROGRESS,

	/**
	 * Task completed successfully
	 */
	COMPLETED,

	/**
	 * Task execution resulted in an error but the error may be transient (or transient status is unknown).
	 * Retrying may result in success.
	 */
	ERRORED,

	/**
	 * Task has failed and is known to be unrecoverable. There is no reason to believe that retrying will
	 * result in a different outcome.
	 */
	FAILED

}
