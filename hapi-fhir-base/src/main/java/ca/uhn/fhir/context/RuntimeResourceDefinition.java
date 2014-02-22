package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IResource;

public class RuntimeResourceDefinition extends BaseRuntimeElementCompositeDefinition<IResource> {

	public RuntimeResourceDefinition(Class<? extends IResource> theClass, String theName) {
		super(theName, theClass);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE;
	}

}
