package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.fhir.cr.cpg.r4.R4LibraryEvaluationService;

@FunctionalInterface
public interface ILibraryEvaluationServiceFactory {
	R4LibraryEvaluationService create(RequestDetails theRequestDetails);
}
