package ca.uhn.fhir.rest.client.api;

public enum UrlSourceEnum {

	/**
	 * URL was generated (typically by adding the base URL + other things)
	 */
	GENERATED,

	/**
	 * URL was supplied (i.e. it came from a paging link in a bundle)
	 */
	EXPLICIT

}
