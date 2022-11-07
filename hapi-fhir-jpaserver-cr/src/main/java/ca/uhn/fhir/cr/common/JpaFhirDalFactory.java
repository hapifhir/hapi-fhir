package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface JpaFhirDalFactory {
	JpaFhirDal create(RequestDetails requestDetails);
}
