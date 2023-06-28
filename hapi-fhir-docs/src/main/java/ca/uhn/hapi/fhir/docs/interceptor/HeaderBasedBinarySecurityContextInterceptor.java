package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.binary.BinarySecurityContextInterceptor;
import org.hl7.fhir.instance.model.api.IBaseBinary;

/**
 * This class is mostly intended as an example implementation of the
 * {@link BinarySecurityContextInterceptor} although it could be used if
 * you wanted its specific rules.
 */
public class HeaderBasedBinarySecurityContextInterceptor extends BinarySecurityContextInterceptor {

	/**
	 * Header name
	 */
	public static final String X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER = "X-SecurityContext-Allowed-Identifier";

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FHIR context
	 */
	public HeaderBasedBinarySecurityContextInterceptor(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	/**
	 * This method should be overridden in order to determine whether the security
	 * context identifier is allowed for the user.
	 *
	 * @param theSecurityContextSystem The <code>Binary.securityContext.identifier.system</code> value
	 * @param theSecurityContextValue  The <code>Binary.securityContext.identifier.value</code> value
	 * @param theRequestDetails        The request details associated with this request
	 */
	@Override
	protected boolean securityContextIdentifierAllowed(String theSecurityContextSystem, String theSecurityContextValue, RequestDetails theRequestDetails) {

		// In our simple example, we will use an incoming header called X-SecurityContext-Allowed-Identifier
		// to determine whether the security context is allowed. This is typically not what you
		// would want, since this is trusting the client to tell us what they are allowed
		// to see. You would typically verify an access token or user session with something
		// external, but this is a simple demonstration.
		String actualHeaderValue = theRequestDetails.getHeader(X_SECURITY_CONTEXT_ALLOWED_IDENTIFIER);
		String expectedHeaderValue = theSecurityContextSystem + "|" + theSecurityContextValue;
		return expectedHeaderValue.equals(actualHeaderValue);
	}
}
