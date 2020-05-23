package ca.uhn.fhir.context.support;

import java.util.HashSet;
import java.util.Set;

public class ValidationSupportContext {

	private final IValidationSupport myRootValidationSupport;
	private final Set<String> myCurrentlyGeneratingSnapshots = new HashSet<>();

	public ValidationSupportContext(IValidationSupport theRootValidationSupport) {
		myRootValidationSupport = theRootValidationSupport;
	}

	public IValidationSupport getRootValidationSupport() {
		return myRootValidationSupport;
	}

	public Set<String> getCurrentlyGeneratingSnapshots() {
		return myCurrentlyGeneratingSnapshots;
	}
}
