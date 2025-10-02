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
	/**
	 * Creates a GET request to the specified URL.
	 *
	 * @param theUrl The URL to send the GET request to. Can be absolute or relative
	 *               to the client's base URL.
	 * @return A builder for configuring and executing the HTTP GET request
	 */
	IClientHttpExecutable<IClientHttpExecutable<?, IEntityResult>, IEntityResult> get(String theUrl);
}
