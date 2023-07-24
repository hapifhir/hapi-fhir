package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.api.Repository;

@FunctionalInterface
public interface IMeasureServiceFactory {
	MeasureService create(RequestDetails theRepository);
}
