package ca.uhn.fhir.jpa.api.model;

public enum HistoryCountModeEnum {

	/**
	 * Always include an accurate count in the response
	 */
	COUNT_ACCURATE,

	/**
	 * Always include a count in the response, but cache the count so that the count may be slightly out of date (but resource usage will be much lower)
	 */
	COUNT_CACHED,

	/**
	 * Do not include a count in history responses
	 */
	COUNT_DISABLED

}
