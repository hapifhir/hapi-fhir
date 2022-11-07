package ca.uhn.fhir.cr.common.behavior;

import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface ConfigurationUser {
	public abstract void validateConfiguration(RequestDetails theRequestDetails);
}
