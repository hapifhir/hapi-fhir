package ca.uhn.fhir.jpa.migrate.taskdef;

public enum MigrationTaskExecutionResultEnum {
	/**
	 * Was either skipped via the `skip-versions` flag or the migration task was stubbed
	 */
	NOT_APPLIED_SKIPPED,

	/**
	 * This migration task does not apply to this database
	 */
	NOT_APPLIED_NOT_FOR_THIS_DATABASE,

	/**
	 * This migration task had precondition criteria (expressed as SQL) that was not met
	 */
	NOT_APPLIED_PRECONDITION_NOT_MET,

	/**
	 * The migration failed, but the task has the FAILURE_ALLOWED flag set.
	 */
	NOT_APPLIED_ALLOWED_FAILURE,

	/**
	 * The migration was applied
	 */
	APPLIED,
}
