package ca.uhn.fhir.rest.server.tenant;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlPathTokenizer;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a tenant identification strategy which assumes that a single path
 * element will be present between the server base URL and the beginning
 */
public class UrlBaseTenantIdentificationStrategy implements ITenantIdentificationStrategy {

	private static final Logger ourLog = LoggerFactory.getLogger(UrlBaseTenantIdentificationStrategy.class);

	@Override
	public void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails) {
		if (theUrlPathTokenizer.hasMoreTokens()) {
			String tenantId = theUrlPathTokenizer.nextToken();
			ourLog.trace("Found tenant ID {} in request string", tenantId);
			theRequestDetails.setTenantId(tenantId);
		}
	}

	@Override
	public String massageServerBaseUrl(String theFhirServerBase, RequestDetails theRequestDetails) {
		Validate.notNull(theRequestDetails.getTenantId(), "theTenantId is not populated on this request");
		return theFhirServerBase + '/' + theRequestDetails.getTenantId();
	}

}
