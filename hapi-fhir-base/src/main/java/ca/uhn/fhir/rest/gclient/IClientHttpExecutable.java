package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.api.CacheControlDirective;

/**
 * The non-FHIR bits of IClientExecutable.
 *
 * @param <T> the builder self type
 * @param <Y> the result type
 */
public interface IClientHttpExecutable<T extends IClientHttpExecutable<?, Y>, Y> {

	/**
	 * Sets the <code>Cache-Control</code> header value, which advises the server (or any cache in front of it)
	 * how to behave in terms of cached requests
	 */
	T cacheControl(CacheControlDirective theCacheControlDirective);

	/**
	 * Set a HTTP header not explicitly defined in FHIR but commonly used in real-world scenarios. One
	 * important example is to set the Authorization header (e.g. Basic Auth or OAuth2-based Bearer auth),
	 * which tends to be cumbersome using {@link ca.uhn.fhir.rest.client.api.IClientInterceptor IClientInterceptors},
	 * particularly when REST clients shall be reused and are thus supposed to remain stateless.
	 * <p>It is the responsibility of the caller to care for proper encoding of the header value, e.g.
	 * using Base64.</p>
	 * <p>This is a short-cut alternative to using a corresponding client interceptor</p>
	 *
	 * @param theHeaderName header name
	 * @param theHeaderValue header value
	 * @return
	 */
	T withAdditionalHeader(String theHeaderName, String theHeaderValue);

	/**
	 * Actually execute the client operation
	 */
	Y execute();

	/**
	 * Specifies a custom <code>Accept</code> header that should be supplied with the
	 * request.
	 *
	 * @param theHeaderValue The header value, e.g. "application/fhir+json". Constants such
	 *                       as {@link ca.uhn.fhir.rest.api.Constants#CT_FHIR_XML_NEW} and
	 *                       {@link ca.uhn.fhir.rest.api.Constants#CT_FHIR_JSON_NEW} may
	 *                       be useful. If set to <code>null</code> or an empty string, the
	 *                       default Accept header will be used.
	 */
	T accept(String theHeaderValue);
}
