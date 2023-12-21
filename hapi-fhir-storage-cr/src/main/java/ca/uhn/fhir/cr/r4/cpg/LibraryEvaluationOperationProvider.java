package ca.uhn.fhir.cr.r4.cpg;

import ca.uhn.fhir.cr.r4.ILibraryEvaluationServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LibraryEvaluationOperationProvider {
	@Autowired
	ILibraryEvaluationServiceFactory myLibraryEvaluationServiceFactory;
}
