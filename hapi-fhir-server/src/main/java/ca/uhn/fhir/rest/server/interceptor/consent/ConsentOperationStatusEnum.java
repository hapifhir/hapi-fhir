package ca.uhn.fhir.rest.server.interceptor.consent;

public enum ConsentOperationStatusEnum {

	/**
	 * The requested operation cannot proceed, and an operation outcome suitable for
	 * the user is available
	 */
	REJECT,

	/**
	 * The requested operation is allowed to proceed, but the engine will review each
	 * resource before sending to the client
	 */
	PROCEED,

	/**
	 * The engine has nothing to say about the operation  (same as proceed, but the
	 * host application need not consult the engine - can use more efficient
	 * counting/caching methods)
	 */
	AUTHORIZED,

}
