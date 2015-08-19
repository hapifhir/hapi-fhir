package ca.uhn.fhir.rest.client.interceptor;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.server.Constants;
import net.sourceforge.cobertura.CoverageIgnore;

/**
 * HTTP interceptor to be used for adding HTTP Authorization using "bearer tokens" to requests. Bearer tokens are used for protocols such as OAUTH2 (see the
 * <a href="http://tools.ietf.org/html/rfc6750">RFC 6750</a> specification on bearer token usage for more information).
 * <p>
 * This interceptor adds a header resembling the following:<br>
 * &nbsp;&nbsp;&nbsp;<code>Authorization: Bearer dsfu9sd90fwp34.erw0-reu</code><br>
 * where the token portion (at the end of the header) is supplied by the invoking code.
 * </p>
 * <p>
 * See the <a href="http://hl7api.sourceforge.net/hapi-fhir/doc_rest_client.html#HTTP_Basic_Authorization">HAPI Documentation</a> for information on how to use this class.
 * </p>
 */
public class BearerTokenAuthInterceptor implements IClientInterceptor {

	private String myToken;

	/**
	 * Constructor. If this constructor is used, a token must be supplied later
	 */
	@CoverageIgnore
	public BearerTokenAuthInterceptor() {
		// nothing
	}

	/**
	 * Constructor
	 * 
	 * @param theToken
	 *           The bearer token to use (must not be null)
	 */
	public BearerTokenAuthInterceptor(String theToken) {
		Validate.notNull("theToken must not be null");
		myToken = theToken;
	}

	/**
	 * Returns the bearer token to use
	 */
	public String getToken() {
		return myToken;
	}

	@Override
	public void interceptRequest(HttpRequestBase theRequest) {
		theRequest.addHeader(Constants.HEADER_AUTHORIZATION, (Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER + myToken));
	}

	@Override
	public void interceptResponse(HttpResponse theResponse) {
		// nothing
	}

	/**
	 * Sets the bearer token to use
	 */
	public void setToken(String theToken) {
		myToken = theToken;
	}

}
