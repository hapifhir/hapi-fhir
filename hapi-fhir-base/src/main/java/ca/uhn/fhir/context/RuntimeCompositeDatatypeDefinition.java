package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.ICompositeDatatype;

public class RuntimeCompositeDatatypeDefinition extends BaseRuntimeElementCompositeDefinition<ICompositeDatatype> {

	public RuntimeCompositeDatatypeDefinition(String theName, Class<? extends ICompositeDatatype> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.COMPOSITE_DATATYPE;
	}


}
