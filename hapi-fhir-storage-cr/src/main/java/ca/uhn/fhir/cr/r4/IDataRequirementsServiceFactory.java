package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.cr.measure.r4.R4DataRequirementsService;

@FunctionalInterface
public interface IDataRequirementsServiceFactory {
	R4DataRequirementsService create(RequestDetails theRequestDetails);
}
