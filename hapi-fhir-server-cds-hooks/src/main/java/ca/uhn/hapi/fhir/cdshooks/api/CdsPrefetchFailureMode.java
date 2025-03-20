package ca.uhn.hapi.fhir.cdshooks.api;

/**
 * Enum representing different modes of handling CDS Hooks prefetch failures.
 * This applies
 *  - if a failure occurs when auto-prefetching a resource, or
 *  - if a CDS Client sends an OperationOutcome as a prefetch resource. With version 2.0 of the CDS Hooks specification, CDS Clients are
 * allowed to send an OperationOutcome as a prefetch if they encounter a failure prefetching a resource.
 */
public enum CdsPrefetchFailureMode {
	/**
	 * The CDS Hooks request will fail.
	 */
	FAIL,
	/**
	 * The prefetch resource will be omitted from the CDS Request.
	 * i.e. {@link ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson} object will not contain the failed prefetch key.
	 */
	OMIT,
	/**
	 * The prefetch key will map to OperationOutcome resource. That is, if the CDS Client sends an OperationOutcome,that will be passed on
	 * as is. If the auto-prefetch fails, and then an OperationOutcome will be either extracted from the FHIR server's response, or created
	 * if one is not available in the response.
	 */
	OPERATION_OUTCOME
}
