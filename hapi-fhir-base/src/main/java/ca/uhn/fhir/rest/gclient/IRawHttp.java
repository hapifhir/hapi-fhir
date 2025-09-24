package ca.uhn.fhir.rest.gclient;

/**
* Interface for making raw HTTP requests using the GenericClient infrastructure.
 * This allows non-FHIR HTTP calls to benefit from the same interceptor chain,
 * authentication, and configuration as regular FHIR operations.
 *
 * <p>Particularly useful for operations like polling $export status endpoints
 * or calling external services that return non-FHIR content.</p>
 *
 * @since 8.6.0
 */
public interface IRawHttp {
	IClientHttpExecutable<IClientHttpExecutable<?, IEntityResult>, IEntityResult> get(String theUrl);
}
