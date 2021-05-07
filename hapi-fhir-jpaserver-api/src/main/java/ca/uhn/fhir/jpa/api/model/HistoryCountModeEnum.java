package ca.uhn.fhir.jpa.api.model;

public enum HistoryCountModeEnum {

	/**
	 * Always include an accurate count in the response
	 */
	COUNT_ACCURATE,

	/**
	 * For history invocations with no offset (i.e. no since-date specified), always include a count in the response,
	 * but cache the count so that the count may be slightly out of date (but resource usage will be much lower). For
	 * history invocations with an offset, never return a count.
	 */
	CACHED_ONLY_WITHOUT_OFFSET,

	/**
	 * Do not include a count in history responses
	 */
	COUNT_DISABLED

}
