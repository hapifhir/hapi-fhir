package ca.uhn.fhir.context;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceReference;

public class RuntimeResourceReferenceDefinition extends BaseRuntimeElementDefinition<ResourceReference> {

	public RuntimeResourceReferenceDefinition(String theName, List<Class<? extends IResource>> theResourceTypes) {
		super(theName, ResourceReference.class);
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
