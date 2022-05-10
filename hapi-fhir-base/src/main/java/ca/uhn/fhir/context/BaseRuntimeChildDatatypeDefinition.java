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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRuntimeChildDatatypeDefinition extends BaseRuntimeDeclaredChildDefinition {
	Logger ourLog = LoggerFactory.getLogger(BaseRuntimeChildDatatypeDefinition.class);

	private Class<? extends IBase> myDatatype;

	private BaseRuntimeElementDefinition<?> myElementDefinition;

	public BaseRuntimeChildDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, Class<? extends IBase> theDatatype) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		// should use RuntimeChildAny
		assert Modifier.isInterface(theDatatype.getModifiers()) == false : "Type of " + theDatatype + " shouldn't be here";
		assert Modifier.isAbstract(theDatatype.getModifiers()) == false : "Type of " + theDatatype + " shouldn't be here";
		myDatatype = theDatatype;
	}

	/**
	 * If this child has a bound type, this method will return the Enum type that
	 * it is bound to. Otherwise, will return <code>null</code>.
	 */
	public Class<? extends Enum<?>> getBoundEnumType() {
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		if (getElementName().equals(theName)) {
			return myElementDefinition;
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		Class<?> nextType = theDatatype;
		while (nextType.equals(Object.class) == false) {
			if (myDatatype.equals(nextType)) {
				return myElementDefinition;
			}
			nextType = nextType.getSuperclass();
		}
		return null;
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		Class<?> nextType = theDatatype;
		while (nextType.equals(Object.class) == false) {
			if (myDatatype.equals(nextType)) {
				return getElementName();
			}
			nextType = nextType.getSuperclass();
		}
		return null;
	}

	public Class<? extends IBase> getDatatype() {
		return myDatatype;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myElementDefinition = theClassToElementDefinitions.get(getDatatype());
		if (myElementDefinition == null) {
			myElementDefinition = theContext.getElementDefinition(getDatatype());
		}
		assert myElementDefinition != null : "Unknown type: " + getDatatype();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getElementName() + "]";
	}

}
