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

import java.lang.reflect.Field;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseEnumFactory;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildEnumerationDatatypeDefinition extends RuntimeChildPrimitiveDatatypeDefinition {

	private Class<? extends IBaseEnumFactory<?>> myBinderType;
	private volatile IBaseEnumFactory<?> myBinder;

	public RuntimeChildEnumerationDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, Class<? extends IBase> theDatatype,
			Class<? extends IBaseEnumFactory<?>> theBinderType) {
		super(theField, theElementName, theDescriptionAnnotation, theChildAnnotation, theDatatype);

		myBinderType = theBinderType;
	}

	@Override
	public IBaseEnumFactory<?> getInstanceConstructorArguments() {
		IBaseEnumFactory<?> retVal = myBinder;
		if (retVal == null) {
			try {
				retVal = myBinderType.newInstance();
			} catch (InstantiationException e) {
				throw new IllegalStateException("Failed to instantiate " + myBinderType, e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Failed to instantiate " + myBinderType, e);
			}
			myBinder = retVal;
		}
		return retVal;
	}

}
