package ca.uhn.fhirtest.config;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditContextServices;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Reference;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is an implementation of the {@link IBalpAuditContextServices}
 * interface for the public HAPI FHIR
 */
public class FhirTestBalpAuditContextServices implements IBalpAuditContextServices {

	/**
	 * Create and return a Reference to the client that was used to
	 * perform the action in question.
	 * <p>
	 * In this case we are simply returning the HTTP User Agent (ie the name of the
	 * browser or HTTP client) because this server is anonymous. In a real implementation
	 * it would make more sense to return the identity of the SMART on FHIR
	 * client or something similar.
	 */
	@Nonnull
	@Override
	public Reference getAgentClientWho(RequestDetails theRequestDetails) {
		String userAgent = theRequestDetails.getHeader("User-Agent");
		if (isBlank(userAgent)) {
			userAgent = "Unknown User Agent";
		}
		Reference retVal = new Reference();
		retVal.setDisplay(userAgent);
		return retVal;
	}

	/**
	 * Create and return a Reference to the user that was used to
	 * perform the action in question.
	 * <p>
	 * In this case because this is an anoymous server we simply use
	 * a masked version of the user's IP as their identity. In a real
	 * server this should have details about the user account.
	 */
	@Nonnull
	@Override
	public Reference getAgentUserWho(RequestDetails theRequestDetails) {
		Reference retVal = new Reference();
		retVal.setDisplay(getNetworkAddress(theRequestDetails));
		return retVal;
	}

	/**
	 * Provide the requesting network address to include in the AuditEvent
	 * <p>
	 * Because this is a public server and these audit events will be visible
	 * to the outside world, we mask the latter half of the requesting IP
	 * address in order to not leak the identity of our users.
	 */
	@Override
	public String getNetworkAddress(RequestDetails theRequestDetails) {
		ServletRequestDetails srd = (ServletRequestDetails) theRequestDetails;

		String remoteAddr = defaultString(srd.getServletRequest().getRemoteAddr(), "UNKNOWN");

		String[] parts = remoteAddr.split("\\.");
		// Obscure part of the IP address
		if (parts.length >= 4) {
			parts[2] = "X";
			parts[3] = "X";
		}
		return String.join(".", parts);
	}
}
