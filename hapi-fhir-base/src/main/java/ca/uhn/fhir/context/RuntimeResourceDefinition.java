package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IResource;

public class RuntimeResourceDefinition extends BaseRuntimeCompositeElementDefinition<IResource> {

	public RuntimeResourceDefinition(Class<? extends IResource> theClass, String theName) {
		super(theName, theClass);
	}

}
