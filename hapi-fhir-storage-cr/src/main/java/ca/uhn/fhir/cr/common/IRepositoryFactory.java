package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.rest.api.server.RequestDetails;

@FunctionalInterface
public interface IRepositoryFactory {
	HapiFhirRepository create(RequestDetails theRequestDetails);
}
