package ca.uhn.fhir.context;

import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;

public class RuntimePrimitiveDatatypeDefinition extends BaseRuntimeElementDefinition<IPrimitiveDatatype<?>>{

	public RuntimePrimitiveDatatypeDefinition(String theName, Class<? extends IPrimitiveDatatype<?>> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// nothing
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_DATATYPE;
	}

}
