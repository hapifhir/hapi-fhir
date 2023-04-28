package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;

import javax.annotation.Nonnull;

/**
 * This interface is intended to be implemented in order to supply implementation
 * strategy details to the {@link BalpAuditCaptureInterceptor}.
 *
 * @since 6.6.0
 */
public interface IBalpAuditContextServices {

	/**
	 * Create and return a Reference to the client that was used to
	 * perform the action in question.
	 *
	 * @param theRequestDetails The request details object
	 */
	@Nonnull
	Reference getAgentClientWho(RequestDetails theRequestDetails);

	/**
	 * Create and return a Reference to the user that was used to
	 * perform the action in question.
	 *
	 * @param theRequestDetails The request details object
	 */
	@Nonnull
	Reference getAgentUserWho(RequestDetails theRequestDetails);

	/**
	 * Provide the requesting network address to include in the AuditEvent
	 */
	default String getNetworkAddress(RequestDetails theRequestDetails) {
		String remoteAddr = null;
		if (theRequestDetails instanceof ServletRequestDetails) {
			remoteAddr = ((ServletRequestDetails) theRequestDetails).getServletRequest().getRemoteAddr();
		}
		return remoteAddr;
	}

	/**
	 * Turns an entity resource ID from an {@link IIdType} to a String.
	 * The default implementation injects the server's base URL into
	 * the ID in order to create fully qualified URLs for resource
	 * references within BALP events.
	 */
	@Nonnull
	default String massageResourceIdForStorage(@Nonnull RequestDetails theRequestDetails, @Nonnull IBaseResource theResource, @Nonnull IIdType theResourceId) {
		String serverBaseUrl = theRequestDetails.getServerBaseForRequest();
		String resourceName = theResourceId.getResourceType();
		return theResourceId.withServerBase(serverBaseUrl, resourceName).getValue();
	}
}
