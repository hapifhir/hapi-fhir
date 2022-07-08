package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.ICompositeType;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class RuntimeCompositeDatatypeDefinition extends BaseRuntimeElementCompositeDefinition<ICompositeType> implements IRuntimeDatatypeDefinition {

	private boolean mySpecialization;
	private Class<? extends IBaseDatatype> myProfileOfType;
	private BaseRuntimeElementDefinition<?> myProfileOf;

	public RuntimeCompositeDatatypeDefinition(DatatypeDef theDef, Class<? extends ICompositeType> theImplementingClass, boolean theStandardType, FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super(theDef.name(), theImplementingClass, theStandardType, theContext, theClassToElementDefinitions);
		
		String resourceName = theDef.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException(Msg.code(1712) + "Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theImplementingClass.getCanonicalName());
		}
		
		mySpecialization = theDef.isSpecialization();
		myProfileOfType = theDef.profileOf();
		if (myProfileOfType.equals(IBaseDatatype.class)) {
			myProfileOfType = null;
		}

	}

	@Override
	public void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theContext, theClassToElementDefinitions);
		
		if (myProfileOfType != null) {
			myProfileOf = theClassToElementDefinitions.get(myProfileOfType);
			if (myProfileOf == null) {
				throw new ConfigurationException(Msg.code(1713) + "Unknown profileOf value: " + myProfileOfType);
			}
		}
	}

	@Override
	public Class<? extends IBaseDatatype> getProfileOf() {
		return myProfileOfType;
	}

	@Override
	public boolean isSpecialization() {
		return mySpecialization;
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.COMPOSITE_DATATYPE;
	}

	@Override
	public boolean isProfileOf(Class<? extends IBaseDatatype> theType) {
		validateSealed();
		if (myProfileOfType != null) {
			if (myProfileOfType.equals(theType)) {
				return true;
			} else if (myProfileOf instanceof IRuntimeDatatypeDefinition) {
				return ((IRuntimeDatatypeDefinition) myProfileOf).isProfileOf(theType);
			}
		}
		return false;
	}


}
