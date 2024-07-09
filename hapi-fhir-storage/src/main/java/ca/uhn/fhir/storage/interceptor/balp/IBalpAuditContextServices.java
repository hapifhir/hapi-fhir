/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Reference;

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
	 * Provide the requesting network address to include in the AuditEvent.
	 *
	 * @see #getNetworkAddressType(RequestDetails) If this method is returning an adress type that is not
	 *      an IP address, you must also oerride this method and return the correct code.
	 */
	default String getNetworkAddress(RequestDetails theRequestDetails) {
		String remoteAddr = null;
		if (theRequestDetails instanceof ServletRequestDetails) {
			remoteAddr = ((ServletRequestDetails) theRequestDetails)
					.getServletRequest()
					.getRemoteAddr();
		}
		return remoteAddr;
	}

	/**
	 * Provides a code representing the appropriate return type for {@link #getNetworkAddress(RequestDetails)}. The
	 * default implementation returns {@link BalpConstants#AUDIT_EVENT_AGENT_NETWORK_TYPE_IP_ADDRESS}.
	 *
	 * @param theRequestDetails The request details object
	 * @see #getNetworkAddress(RequestDetails)
	 * @see BalpConstants#AUDIT_EVENT_AGENT_NETWORK_TYPE_MACHINE_NAME Potential return type for this method
	 * @see BalpConstants#AUDIT_EVENT_AGENT_NETWORK_TYPE_IP_ADDRESS Potential return type for this method
	 * @see BalpConstants#AUDIT_EVENT_AGENT_NETWORK_TYPE_URI Potential return type for this method
	 */
	default AuditEvent.AuditEventAgentNetworkType getNetworkAddressType(RequestDetails theRequestDetails) {
		return BalpConstants.AUDIT_EVENT_AGENT_NETWORK_TYPE_IP_ADDRESS;
	}

	/**
	 * Turns an entity resource ID from an {@link IIdType} to a String.
	 * The default implementation injects the server's base URL into
	 * the ID in order to create fully qualified URLs for resource
	 * references within BALP events.
	 */
	@Nonnull
	default String massageResourceIdForStorage(
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull IBaseResource theResource,
			@Nonnull IIdType theResourceId) {
		String serverBaseUrl = theRequestDetails.getFhirServerBase();
		String resourceName = theResourceId.getResourceType();
		return theResourceId.withServerBase(serverBaseUrl, resourceName).getValue();
	}
}
