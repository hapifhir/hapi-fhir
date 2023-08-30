package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.evaluator.cql.r4.R4CqlExecutionService;

@FunctionalInterface
public interface ICqlExecutionServiceFactory {
	R4CqlExecutionService create(RequestDetails theRequestDetails);
}
