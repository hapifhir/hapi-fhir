package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;

import javax.annotation.Nonnull;

public interface IAuditContextServices {

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
	 * Turns an entity resource ID from an {@link IIdType} to a String.
	 * The default implementation injects the server's base URL into
	 * the ID.
	 */
	@Nonnull
	default String massageResourceIdForStorage(@Nonnull RequestDetails theRequestDetails, @Nonnull IBaseResource theResource, @Nonnull IIdType theResourceId) {
		String serverBaseUrl = theRequestDetails.getServerBaseForRequest();
		String resourceName = theResourceId.getResourceType();
		return theResourceId.withServerBase(serverBaseUrl, resourceName).getValue();
	}
}
