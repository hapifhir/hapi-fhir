package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

public class RuntimePrimitiveDatatypeDefinition extends BaseRuntimeElementDefinition<IPrimitiveDatatype<?>> implements IRuntimeDatatypeDefinition {

	private boolean mySpecialization;

	public RuntimePrimitiveDatatypeDefinition(DatatypeDef theDef, Class<? extends IPrimitiveDatatype<?>> theImplementingClass) {
		super(theDef.name(), theImplementingClass);
		
		String resourceName = theDef.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theImplementingClass.getCanonicalName());
		}
		
		mySpecialization = theDef.isSpecialization();
	}

	@Override
	public boolean isSpecialization() {
		return mySpecialization;
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
