package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IPrimitiveDatatype;

public class RuntimePrimitiveDatatypeNarrativeDefinition extends RuntimePrimitiveDatatypeDefinition {

	public RuntimePrimitiveDatatypeNarrativeDefinition(String theName, Class<? extends IPrimitiveDatatype<?>> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_XHTML;
	}

}
