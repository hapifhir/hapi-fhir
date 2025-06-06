package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import org.hl7.fhir.instance.model.api.IBase;

public class FhirPathChildDefinition {
	// our parent element; if null, this is the top level element
	private IBase myBase;

	private BaseRuntimeChildDefinition myBaseRuntimeDefinition;

	private BaseRuntimeElementDefinition<?> myElementDefinition;

	private String myFhirPath;

	private FhirPathChildDefinition myChild;

	private FhirPathChildDefinition myParent;

	public IBase getBase() {
		return myBase;
	}

	public void setBase(IBase theBase) {
		myBase = theBase;
	}

	public BaseRuntimeChildDefinition getBaseRuntimeDefinition() {
		return myBaseRuntimeDefinition;
	}

	public void setBaseRuntimeDefinition(BaseRuntimeChildDefinition theChildDefinition) {
		myBaseRuntimeDefinition = theChildDefinition;
	}

	public BaseRuntimeElementDefinition<?> getElementDefinition() {
		return myElementDefinition;
	}

	public void setElementDefinition(BaseRuntimeElementDefinition<?> theElementDefinition) {
		myElementDefinition = theElementDefinition;
	}

	public String getFhirPath() {
		return myFhirPath;
	}

	public void setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
	}

	public FhirPathChildDefinition getChild() {
		return myChild;
	}

	public void setChild(FhirPathChildDefinition theChild) {
		myChild = theChild;
		if (myChild != null) {
			myChild.setParent(this);
		}
	}

	public FhirPathChildDefinition getParent() {
		return myParent;
	}

	public void setParent(FhirPathChildDefinition theParent) {
		myParent = theParent;
	}
}
