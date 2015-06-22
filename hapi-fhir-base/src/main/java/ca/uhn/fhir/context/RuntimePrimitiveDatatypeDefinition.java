package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

public class RuntimePrimitiveDatatypeDefinition extends BaseRuntimeElementDefinition<IPrimitiveType<?>> implements IRuntimeDatatypeDefinition {

	private boolean mySpecialization;

	public RuntimePrimitiveDatatypeDefinition(DatatypeDef theDef, Class<? extends IPrimitiveType<?>> theImplementingClass, boolean theStandardType) {
		super(theDef.name(), theImplementingClass, theStandardType);
		
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
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// nothing
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_DATATYPE;
	}


}
