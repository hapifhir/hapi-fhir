package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.ICompositeDatatype;

public class RuntimeCompositeDatatypeDefinition extends BaseRuntimeCompositeElementDefinition<ICompositeDatatype> {

	public RuntimeCompositeDatatypeDefinition(String theName, Class<? extends ICompositeDatatype> theImplementingClass) {
		super(theName, theImplementingClass);
	}


}
