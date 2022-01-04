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
import java.util.Collections;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildExtension extends RuntimeChildAny {

//	private RuntimeChildUndeclaredExtensionDefinition myExtensionElement;

	public RuntimeChildExtension(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation);
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		return getElementName();
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		if ("extension".equals(theName) || "modifierExtension".equals(theName)) {
			return super.getChildByName("extensionExtension");
		}
		return super.getChildByName(theName);
	}
	
//	@Override
//	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
//		if (IBaseExtension.class.isAssignableFrom(theDatatype)) {
//			return myExtensionElement;
//		}
//		return super.getChildElementDefinitionByDatatype(theDatatype);
//	}
//
//	@Override
//	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
//		super.sealAndInitialize(theContext, theClassToElementDefinitions);
//		
//		myExtensionElement = theContext.getRuntimeChildUndeclaredExtensionDefinition();
//	}


}
