package ca.uhn.fhir.context;

import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.datatype.XhtmlDt;

public class RuntimePrimitiveDatatypeNarrativeDefinition  extends BaseRuntimeElementDefinition<XhtmlDt> {

	public RuntimePrimitiveDatatypeNarrativeDefinition(String theName, Class<XhtmlDt> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_XHTML;
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// nothing
	}

}
