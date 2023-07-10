package ca.uhn.hapi.fhir.cdshooks.api;

public enum CdsResolutionStrategyEnum {
	/**
	 * There are no means to fetch missing prefetch elements.  A precondition failure should be thrown.
	 */
	NONE,

	/**
	 * Fhir Server details were provided with the request.  Smile CDR will use them to fetch missing resources before
	 * calling the CDS Service
	 */
	FHIR_CLIENT,

	/**
	 * The CDS Hooks service method will be responsible for fetching the missing resources using the FHIR Server
	 * in the request
	 */
	SERVICE,

	/**
	 * Missing prefetch elements will be retrieved directly from the configured Storage Module
	 */
	DAO
}
