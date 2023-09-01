package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.cr.measure.r4.R4SubmitDataService;

public interface ISubmitDataProcessorFactory {
	R4SubmitDataService create(RequestDetails theRequestDetails);
}
