package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;

public class ChildDefinition {

	private final BaseRuntimeChildDefinition myChildDef;
	private final BaseRuntimeElementDefinition<?> myChildElement;

	public ChildDefinition(BaseRuntimeChildDefinition theChildDef, BaseRuntimeElementDefinition<?> theChildElement) {
		this.myChildDef = theChildDef;
		this.myChildElement = theChildElement;
	}

	public BaseRuntimeChildDefinition getUseableChildDef() {
		return myChildDef;
	}

	public boolean isPrimitive() {
		return myChildDef == null;
	}

	public BaseRuntimeElementDefinition<?> getChildElement() {
		return myChildElement;
	}

	public BaseRuntimeElementDefinition<?> getUsableChildElement() {
		return myChildElement;
	}
}
