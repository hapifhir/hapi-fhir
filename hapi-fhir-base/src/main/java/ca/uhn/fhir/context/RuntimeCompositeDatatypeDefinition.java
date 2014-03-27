package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

public class RuntimeCompositeDatatypeDefinition extends BaseRuntimeElementCompositeDefinition<ICompositeDatatype> implements IRuntimeDatatypeDefinition {

	private boolean mySpecialization;

	public RuntimeCompositeDatatypeDefinition(DatatypeDef theDef, Class<? extends ICompositeDatatype> theImplementingClass) {
		super(theDef.name(), theImplementingClass);
		
		String resourceName = theDef.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theImplementingClass.getCanonicalName());
		}
		
		mySpecialization = theDef.isSpecialization();

	}

	public boolean isSpecialization() {
		return mySpecialization;
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.COMPOSITE_DATATYPE;
	}


}
