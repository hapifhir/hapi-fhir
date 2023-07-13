package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.measure.MeasureService;
import org.opencds.cqf.fhir.api.Repository;

@FunctionalInterface
public interface IMeasureProcessorFactory {
	MeasureService create(Repository theRepository);
}
