package ca.uhn.fhir.context.support;

public class ConceptValidationOptions {

	public boolean isInferSystem() {
		return myInferSystem;
	}

	public ConceptValidationOptions setInferSystem(boolean theInferSystem) {
		myInferSystem = theInferSystem;
		return this;
	}

	private boolean myInferSystem;

}
