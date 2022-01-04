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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import org.hl7.fhir.instance.model.api.IBase;

import java.lang.reflect.Field;

public class RuntimeChildPrimitiveEnumerationDatatypeDefinition extends RuntimeChildPrimitiveDatatypeDefinition {

	private Object myBinder;
	private Class<? extends Enum<?>> myEnumType;

	public RuntimeChildPrimitiveEnumerationDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation,  Class<? extends IBase> theDatatype, Class<? extends Enum<?>> theBinderType) {
		super(theField, theElementName, theDescriptionAnnotation, theChildAnnotation, theDatatype);

		myEnumType = theBinderType;
	}

	@Override
	public Class<? extends Enum<?>> getBoundEnumType() {
		return myEnumType;
	}

	@Override
	public Object getInstanceConstructorArguments() {
		Object retVal = myBinder;
		if (retVal == null) {
			retVal = toEnumFactory(myEnumType);
			myBinder = retVal;
		}
		return retVal;
	}

	static Object toEnumFactory(Class<?> theEnumerationType) {
		Class<?> clazz;
		String className = theEnumerationType.getName() + "EnumFactory";
		Object retVal;
		try {
			clazz = Class.forName(className);
			retVal = clazz.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1694) + "Failed to instantiate " + className, e);
		}
		return retVal;
	}

}
