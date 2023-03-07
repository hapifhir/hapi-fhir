package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.rest.api.server.RequestDetails;
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

}
