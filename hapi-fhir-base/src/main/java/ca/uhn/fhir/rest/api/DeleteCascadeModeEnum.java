package ca.uhn.fhir.rest.api;

/**
 * Used by the client to indicate the cascade mode associated with a delete operation.
 * <p>
 * Note that this is a HAPI FHIR specific feature, and may not work on other platforms.
 * </p>
 */
public enum DeleteCascadeModeEnum {

	NONE,

	DELETE

}
