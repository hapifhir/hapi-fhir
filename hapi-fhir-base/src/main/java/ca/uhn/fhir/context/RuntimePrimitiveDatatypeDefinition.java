package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IPrimitiveDatatype;

public class RuntimePrimitiveDatatypeDefinition extends BaseRuntimeElementDefinition<IPrimitiveDatatype>{

	public RuntimePrimitiveDatatypeDefinition(String theName, Class<? extends IPrimitiveDatatype> theImplementingClass) {
		super(theName, theImplementingClass);
	}

}
