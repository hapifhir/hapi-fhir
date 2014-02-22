package ca.uhn.fhir.context;

import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.ResourceReference;

public class RuntimeResourceReferenceDefinition extends BaseRuntimeElementDefinition<ResourceReference> {

	public RuntimeResourceReferenceDefinition(String theName) {
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
