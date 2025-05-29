package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;

public class ChildDefinition {
	private final BaseRuntimeChildDefinition myChildDef;
	private final BaseRuntimeElementDefinition<?> myChildElement;

	private BaseRuntimeChildDefinition myParentDef;

	public ChildDefinition(
		BaseRuntimeChildDefinition theChildDef, BaseRuntimeElementDefinition<?> theChildElement) {
		this.myChildDef = theChildDef;
		this.myChildElement = theChildElement;
	}

	public BaseRuntimeChildDefinition getChildDef() {
		return myChildDef;
	}

	public BaseRuntimeChildDefinition getUseableChildDef() {
		if (hasParentDef()) {
			return myParentDef;
		}
		return myChildDef;
	}

	public void setParentDef(BaseRuntimeChildDefinition theParentDef) {
		myParentDef = theParentDef;
	}

	public boolean hasParentDef() {
		return myParentDef != null;
	}

	public boolean hasChildDef() {
		return myChildDef != null;
	}

	public boolean isPrimitive() {
		return myChildDef == null;
	}

	public BaseRuntimeElementDefinition<?> getChildElement() {
		return myChildElement;
	}
}
