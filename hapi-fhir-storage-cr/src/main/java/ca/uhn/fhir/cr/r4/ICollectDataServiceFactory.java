package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.cr.measure.r4.R4CollectDataService;

@FunctionalInterface
public interface ICollectDataServiceFactory {
	R4CollectDataService create(RequestDetails theRequestDetails);
}
