package ca.uhn.fhir.jpa.migrate.taskdef;

public enum MigrationTaskExecutionResultEnum {
	/**
	 * Was either skipped via the `skip-versions` flag or the migration task was stubbed
	 */
	SKIPPED,

	/**
	 * This migration task does not apply to this database
	 */
	DOES_NOT_APPLY,

	/**
	 * This migration task had precondition criteria (expressed as SQL) that was not met
	 */
	PRECONDITION_FAILED,

	/**
	 * The migration failed, but the task has the FAILURE_ALLOWED flag set.
	 */
	ALLOWED_TO_FAIL,

	/**
	 * The migration executed successfully
	 */
	SUCCESS,
}
