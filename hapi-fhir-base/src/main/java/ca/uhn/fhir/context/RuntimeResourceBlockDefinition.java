package ca.uhn.fhir.context;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.Map;

public class RuntimeResourceBlockDefinition extends BaseRuntimeElementCompositeDefinition<IBase> {

	public RuntimeResourceBlockDefinition(String theName, Class<? extends IBase> theImplementingClass, boolean theStandardType, FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super(theName, theImplementingClass, theStandardType, theContext, theClassToElementDefinitions);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE_BLOCK;
	}

}
