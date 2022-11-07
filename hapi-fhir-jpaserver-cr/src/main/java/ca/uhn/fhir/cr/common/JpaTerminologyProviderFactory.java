package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.RequestDetails;

@FunctionalInterface
public interface JpaTerminologyProviderFactory{
	JpaTerminologyProvider create(RequestDetails requestDetails);
}
