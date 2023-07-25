package ca.uhn.fhir.cr.dstu3;


import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;

@FunctionalInterface
public interface IMeasureServiceFactory {
	MeasureService create(RequestDetails theRequestDetails);
}
