package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import org.hl7.fhir.instance.model.api.IBase;

public class ChildDefinition {
	/**
	 * This child's parent definition.
	 *
	 * Useful if the field definitions for the provided
	 * childelement are primitive, composite, or sub-resource elements
	 *
	 * Eg, if given the path:
	 * Encounter.name.given[0]
	 *
	 * ChildDefinition will be a primitive (string) type at path "Encounter.name[0].given",
	 * but the ParentDefinition will be the "complex" HumanName type at path "Encounter.name"
	 */
	public static class ParentDefinition {
		/**
		 * The parent field of the field that needs to be changed.
		 */
		private final IBase myParentField;
		private final BaseRuntimeChildDefinition myParentDef;
		private final BaseRuntimeElementDefinition<?> myParentElement;

		private final String myFhirPath;

		public ParentDefinition(IBase theParentField, BaseRuntimeElementDefinition<?> theParentElement, BaseRuntimeChildDefinition theParentDef, String theFhirPath) {
			myParentField = theParentField;
			myParentElement = theParentElement;
			myParentDef = theParentDef;

			myFhirPath = theFhirPath;
		}

		public IBase getParentField() {
			return myParentField;
		}

		public BaseRuntimeChildDefinition getParentDef() {
			return myParentDef;
		}

		public BaseRuntimeElementDefinition<?> getParentElement() {
			return myParentElement;
		}

		public String getFhirPath() {
			return myFhirPath;
		}
	}

	private final BaseRuntimeChildDefinition myChildDef;
	private final BaseRuntimeElementDefinition<?> myChildElement;

	/**
	 * The information about the parent of the current child definition.
	 * Required for when this is a primitive or composite type child definition
	 */
	private ParentDefinition myParentDefinition;

	/**
	 * This is the element that needs to be replaced.
	 * For most cases, this is the element directly.
	 * But in some cases (say a Backbone element needing replacement)
	 * this will be the nearest parent element.
	 */
	private IBase myContainingElement;

	public ChildDefinition(
		BaseRuntimeChildDefinition theChildDef, BaseRuntimeElementDefinition<?> theChildElement) {
		this.myChildDef = theChildDef;
		this.myChildElement = theChildElement;
	}

	public void setContainingElement(IBase theBaseElement) {
		myContainingElement = theBaseElement;
	}

	public IBase getContainingElement() {
		if (hasParentDefinition()) {
			return myParentDefinition.getParentField();
		}
		return myContainingElement;
	}

	public BaseRuntimeChildDefinition getChildDef() {
		return myChildDef;
	}

	public boolean hasParentDefinition() {
		return myParentDefinition != null;
	}

	public BaseRuntimeChildDefinition getUseableChildDef() {
		if (hasParentDefinition()) {
			return myParentDefinition.getParentDef();
		}
		return myChildDef;
	}

	public void setParentDefinition(ParentDefinition theParentDefinition) {
		myParentDefinition = theParentDefinition;
	}

	public boolean isPrimitive() {
		return myChildDef == null;
	}

	public BaseRuntimeElementDefinition<?> getChildElement() {
		return myChildElement;
	}

	public BaseRuntimeElementDefinition<?> getUsableChildElement() {
		if (hasParentDefinition()) {
			return myParentDefinition.getParentElement();
		}
		return myChildElement;
	}
}
