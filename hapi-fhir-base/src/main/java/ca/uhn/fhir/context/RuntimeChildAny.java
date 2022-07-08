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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseReference;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class RuntimeChildAny extends RuntimeChildChoiceDefinition {

	public RuntimeChildAny(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation);
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		List<Class<? extends IBase>> choiceTypes = new ArrayList<Class<? extends IBase>>();
		
		for (Class<? extends IBase> next : theClassToElementDefinitions.keySet()) {
			if (next.equals(XhtmlDt.class)) {
				continue;
			}
			
			BaseRuntimeElementDefinition<?> nextDef = theClassToElementDefinitions.get(next);
			if (nextDef instanceof IRuntimeDatatypeDefinition) {
				if (((IRuntimeDatatypeDefinition) nextDef).isSpecialization()) {
					/*
					 * Things like BoundCodeDt shoudn't be considered as valid options for an "any" choice, since 
					 * we'll already have CodeDt as an option 
					 */
					continue;
				}
			}
			
			if (IResource.class.isAssignableFrom(next) || IDatatype.class.isAssignableFrom(next) || IBaseDatatype.class.isAssignableFrom(next) || IBaseReference.class.isAssignableFrom(next)) {
				choiceTypes.add(next);
			}
		}
		Collections.sort(choiceTypes,new Comparator<Class<?>>(){
			@Override
			public int compare(Class<?> theO1, Class<?> theO2) {
				boolean o1res = IResource.class.isAssignableFrom(theO1);
				boolean o2res = IResource.class.isAssignableFrom(theO2);
				if (o1res && o2res) {
					return theO1.getSimpleName().compareTo(theO2.getSimpleName());
				} else if (o1res) {
					return -1;
				} else if (o1res == false && o2res == false) {
					return 0;
				}else {
					return 1;
				}
			}});
		
		setChoiceTypes(choiceTypes);
		
		super.sealAndInitialize(theContext, theClassToElementDefinitions);
	}

	
	
}
