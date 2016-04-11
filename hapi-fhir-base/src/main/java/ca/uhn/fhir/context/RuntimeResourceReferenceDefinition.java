package ca.uhn.fhir.context;

import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;

public class RuntimeResourceReferenceDefinition extends BaseRuntimeElementCompositeDefinition<IBaseReference> {


	public RuntimeResourceReferenceDefinition(String theName, Class<? extends IBaseReference> theImplementingClass, boolean theStandardType) {
		super(theName, theImplementingClass, theStandardType);
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theContext, theClassToElementDefinitions);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE_REF;
	}

}
