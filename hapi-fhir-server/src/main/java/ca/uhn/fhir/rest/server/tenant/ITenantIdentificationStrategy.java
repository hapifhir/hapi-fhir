package ca.uhn.fhir.rest.server.tenant;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlPathTokenizer;

public interface ITenantIdentificationStrategy {

	/**
	 * Implementations should use this method to determine the tenant ID
	 * based on the incoming request andand populate it in the
	 * {@link RequestDetails#setTenantId(String)}.
	 *
	 * @param theUrlPathTokenizer The tokenizer which is used to parse the request path
	 * @param theRequestDetails   The request details object which can be used to access headers and to populate the tenant ID to
	 */
	void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails);

	/**
	 * Implementations may use this method to tweak the server base URL
	 * if neccesary based on the tenant ID
	 */
	String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails);
}
