package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;

@FunctionalInterface
public interface IMeasureServiceFactory {
	MeasureService create(RequestDetails theRequestDetails);
}
