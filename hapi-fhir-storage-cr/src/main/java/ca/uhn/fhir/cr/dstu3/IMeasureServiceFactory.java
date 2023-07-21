package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import org.opencds.cqf.fhir.api.Repository;

@FunctionalInterface
public interface IMeasureServiceFactory {
	MeasureService create(Repository theRepository);
}
