package ca.uhn.fhir.context;

import java.util.Map;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class RuntimeResourceReferenceDefinition extends BaseRuntimeElementDefinition<IResource> {

	public RuntimeResourceReferenceDefinition(String theName, Class<? extends IResource> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE_REF;
	}

}
