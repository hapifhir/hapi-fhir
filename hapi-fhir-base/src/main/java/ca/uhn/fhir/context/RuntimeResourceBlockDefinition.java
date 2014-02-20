package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IResourceBlock;

public class RuntimeResourceBlockDefinition extends BaseRuntimeElementCompositeDefinition<IResourceBlock> {

	public RuntimeResourceBlockDefinition(String theName, Class<? extends IResourceBlock> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE_BLOCK;
	}

}
