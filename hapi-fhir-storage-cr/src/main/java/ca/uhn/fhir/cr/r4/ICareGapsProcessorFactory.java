package ca.uhn.fhir.cr.r4;


import org.opencds.cqf.cql.evaluator.measure.r4.R4CareGapsService;
import org.opencds.cqf.fhir.api.Repository;

@FunctionalInterface
public interface ICareGapsProcessorFactory {
	 R4CareGapsService create(Repository theRepository);
}
