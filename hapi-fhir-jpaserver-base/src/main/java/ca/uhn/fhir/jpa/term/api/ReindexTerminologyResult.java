package ca.uhn.fhir.jpa.term.api;

public enum ReindexTerminologyResult {
	SUCCESS,

	// search service is not enabled
	SEARCH_SVC_DISABLED,

	// batch terminology tasks other than re-indexing are currently running
	OTHER_BATCH_TERMINOLOGY_TASKS_RUNNING
}
