package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.Collection;

/**
 * @see AuthorizationInterceptor#setFlags(Collection)
 */
public enum AuthorizationFlagsEnum {

	/**
	 * If this flag is set, attempts to perform read operations
	 * (read/search/history) will be matched by the interceptor before
	 * the method handler is called.
	 * <p>
	 * For example, suppose a rule set is in place that only allows read
	 * access to compartment <code>Patient/123</code>. With this flag set,
	 * any attempts
	 * to perform a FHIR read/search/history operation will be permitted
	 * to proceed to the method handler, and responses will be blocked
	 * by the AuthorizationInterceptor if the response contains a resource
	 * that is not in the given compartment.
	 * </p>
	 * <p>
	 * Setting this flag is less secure, since the interceptor can potentially leak
	 * information about the existence of data, but it is useful in some
	 * scenarios.
	 * </p>
	 *
	 * @since This flag has existed since HAPI FHIR 3.5.0. Prior to this
	 * version, this flag was the default and there was no ability to
	 * proactively block compartment read access.
	 */
	NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS;

}
