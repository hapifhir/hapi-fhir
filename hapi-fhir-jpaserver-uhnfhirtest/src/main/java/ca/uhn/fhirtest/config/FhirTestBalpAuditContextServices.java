package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.interceptor.balp.IBalpAuditContextServices;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import joptsimple.internal.Strings;
import org.hl7.fhir.r4.model.Reference;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class FhirTestBalpAuditContextServices implements IBalpAuditContextServices {
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
		return Strings.join(parts, ".");
	}

	@Nonnull
	@Override
	public Reference getAgentUserWho(RequestDetails theRequestDetails) {
		Reference retVal = new Reference();
		retVal.setDisplay(getNetworkAddress(theRequestDetails));
		return retVal;
	}
}
