package ca.uhn.fhir.rest.api;

/**
 * during invocation of certain pointcuts, it is important to know whether they are being executed in
 * active or deferred fashion. This enum allows the pointcuts to see how they were invoked.
 */
public enum InterceptorInvocationTimingEnum {
	ACTIVE,
	DEFERRED
}
