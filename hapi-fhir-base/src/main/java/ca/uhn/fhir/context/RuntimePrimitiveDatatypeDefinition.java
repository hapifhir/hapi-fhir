package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

public class RuntimePrimitiveDatatypeDefinition extends BaseRuntimeElementDefinition<IPrimitiveType<?>> implements IRuntimeDatatypeDefinition {

	private Class<?> myNativeType;
	private BaseRuntimeElementDefinition<?> myProfileOf;
	private Class<? extends IBaseDatatype> myProfileOfType;
	private boolean mySpecialization;

	public RuntimePrimitiveDatatypeDefinition(DatatypeDef theDef, Class<? extends IPrimitiveType<?>> theImplementingClass, boolean theStandardType) {
		super(theDef.name(), theImplementingClass, theStandardType);

		String resourceName = theDef.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theImplementingClass.getCanonicalName());
		}

		mySpecialization = theDef.isSpecialization();
		myProfileOfType = theDef.profileOf();
		if (myProfileOfType.equals(IBaseDatatype.class)) {
			myProfileOfType = null;
		}

		determineNativeType(theImplementingClass);
	}

	private void determineNativeType(Class<? extends IPrimitiveType<?>> theImplementingClass) {
		Class<?> clazz = theImplementingClass;
		while (clazz.equals(Object.class) == false) {
			Type type = clazz.getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType superPt = (ParameterizedType) type;
				Type rawType = superPt.getRawType();
				if (rawType instanceof Class) {
					Class<?> rawClass = (Class<?>) rawType;
					if (rawClass.getName().endsWith(".BasePrimitive") || rawClass.getName().endsWith(".PrimitiveType")) {
						Type typeVariable = superPt.getActualTypeArguments()[0];
						if (typeVariable instanceof Class) {
							myNativeType = (Class<?>) typeVariable;
							break;
						}
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.PRIMITIVE_DATATYPE;
	}

	public Class<?> getNativeType() {
		return myNativeType;
	}

	@Override
	public Class<? extends IBaseDatatype> getProfileOf() {
		return myProfileOfType;
	}

	@Override
	public boolean isProfileOf(Class<? extends IBaseDatatype> theType) {
		if (myProfileOfType != null) {
			if (myProfileOfType.equals(theType)) {
				return true;
			} else if (myProfileOf instanceof IRuntimeDatatypeDefinition) {
				return ((IRuntimeDatatypeDefinition) myProfileOf).isProfileOf(theType);
			}
		}
		return false;
	}

	@Override
	public boolean isSpecialization() {
		return mySpecialization;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theContext, theClassToElementDefinitions);

		if (myProfileOfType != null) {
			myProfileOf = theClassToElementDefinitions.get(myProfileOfType);
			if (myProfileOf == null) {
				StringBuilder b = new StringBuilder();
				b.append("Unknown profileOf value: ");
				b.append(myProfileOfType);
				b.append(" in type ");
				b.append(getImplementingClass().getName());
				b.append(" - Valid types: ");
				b.append(theClassToElementDefinitions.keySet());
				throw new ConfigurationException(b.toString());
			}
		}
	}

}
