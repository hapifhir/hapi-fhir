package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildResourceBlockDefinition extends BaseRuntimeDeclaredChildDefinition {

	private RuntimeResourceBlockDefinition myElementDef;
	private Class<? extends IBase> myResourceBlockType;

	public RuntimeChildResourceBlockDefinition(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName, Class<? extends IBase> theResourceBlockType) throws ConfigurationException {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		myResourceBlockType = theResourceBlockType;
	}

	@Override
	public RuntimeResourceBlockDefinition getChildByName(String theName) {
		if (getElementName().equals(theName)) {
			return myElementDef;
		}else {
			return null;
		}
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		if (myResourceBlockType.equals(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		if (myResourceBlockType.equals(theDatatype)) {
			return myElementDef;
		}
		return null;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myElementDef = (RuntimeResourceBlockDefinition) theClassToElementDefinitions.get(myResourceBlockType);
	}

}
