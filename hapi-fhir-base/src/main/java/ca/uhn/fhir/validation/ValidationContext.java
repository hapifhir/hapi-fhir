package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

class ValidationContext {

	private FhirContext myFhirContext;
	private OperationOutcome myOperationOutcome;
	private IResource myResource;
	private String myXmlEncodedResource;

	public ValidationContext(FhirContext theContext, IResource theResource) {
		myFhirContext = theContext;
		myResource = theResource;
	}

	public String getXmlEncodedResource() {
		if (myXmlEncodedResource == null) {
			myXmlEncodedResource = myFhirContext.newXmlParser().encodeResourceToString(myResource);
		}
		return myXmlEncodedResource;
	}

	public OperationOutcome getOperationOutcome() {
		if (myOperationOutcome == null) {
			myOperationOutcome = new OperationOutcome();
		}
		return myOperationOutcome;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public IResource getResource() {
		return myResource;
	}

}
