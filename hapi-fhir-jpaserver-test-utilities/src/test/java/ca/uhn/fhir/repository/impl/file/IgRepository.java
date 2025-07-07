package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepositoryOperationProvider;

import java.nio.file.Path;

/**
 * Dummy to allow the test utilities to use the file system repository
 * fixme - delete
 */
public class IgRepository extends FileSystemFhirRepository {
	public IgRepository(FhirContext fhirContext, Path root) {
		super(fhirContext, root);
	}

	public IgRepository(FhirContext fhirContext, Path root, IgConventions conventions, EncodingBehavior encodingBehavior, IRepositoryOperationProvider operationProvider) {
		super(fhirContext, root, conventions, encodingBehavior, operationProvider);
	}
}
