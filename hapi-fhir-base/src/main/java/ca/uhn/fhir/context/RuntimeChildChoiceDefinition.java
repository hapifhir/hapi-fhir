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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildChoiceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private List<Class<? extends IBase>> myChoiceTypes;
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChildDefinition;
	private Map<Class<? extends IBase>, String> myDatatypeToElementName;
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myDatatypeToElementDefinition;

	public RuntimeChildChoiceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, List<Class<? extends IBase>> theChoiceTypes) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);

		myChoiceTypes = Collections.unmodifiableList(theChoiceTypes);
	}

	/**
	 * For extension, if myChoiceTypes will be set some other way
	 */
	RuntimeChildChoiceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
	}

	void setChoiceTypes(List<Class<? extends IBase>> theChoiceTypes) {
		myChoiceTypes = Collections.unmodifiableList(theChoiceTypes);
	}

	public List<Class<? extends IBase>> getChoices() {
		return myChoiceTypes;
	}

	@Override
	public Set<String> getValidChildNames() {
		return myNameToChildDefinition.keySet();
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		assert myNameToChildDefinition.containsKey(theName);

		return myNameToChildDefinition.get(theName);
	}

	@SuppressWarnings("unchecked")
	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myNameToChildDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		myDatatypeToElementName = new HashMap<Class<? extends IBase>, String>();
		myDatatypeToElementDefinition = new HashMap<Class<? extends IBase>, BaseRuntimeElementDefinition<?>>();

		for (Class<? extends IBase> next : myChoiceTypes) {

			String elementName;
			BaseRuntimeElementDefinition<?> nextDef;
			if (IBaseResource.class.isAssignableFrom(next)) {
				elementName = getElementName() + StringUtils.capitalize(next.getSimpleName());
				List<Class<? extends IBaseResource>> types = new ArrayList<Class<? extends IBaseResource>>();
				types.add((Class<? extends IBaseResource>) next);
				nextDef = new RuntimeResourceReferenceDefinition(elementName, types, false);
				nextDef.sealAndInitialize(theContext, theClassToElementDefinitions);
				
				myNameToChildDefinition.put(getElementName() + "Reference", nextDef);
				myNameToChildDefinition.put(getElementName() + "Resource", nextDef);
				
			} else {
				nextDef = theClassToElementDefinitions.get(next);
				elementName = getElementName() + StringUtils.capitalize(nextDef.getName());
			}

			myNameToChildDefinition.put(elementName, nextDef);
			
			if (IBaseResource.class.isAssignableFrom(next)) {
				Class<? extends IBase> refType = theContext.getVersion().getResourceReferenceType();
				myDatatypeToElementDefinition.put(refType, nextDef);
				
				String alternateElementName;
				if (theContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
					alternateElementName = getElementName() + "Resource";
				} else {
					alternateElementName = getElementName() + "Reference";
				}
				myDatatypeToElementName.put(refType, alternateElementName);
			}
			
			myDatatypeToElementDefinition.put(next, nextDef);
			myDatatypeToElementName.put(next, elementName);
		}

		myNameToChildDefinition = Collections.unmodifiableMap(myNameToChildDefinition);
		myDatatypeToElementName = Collections.unmodifiableMap(myDatatypeToElementName);
		myDatatypeToElementDefinition = Collections.unmodifiableMap(myDatatypeToElementDefinition);

	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		return myDatatypeToElementName.get(theDatatype);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		return myDatatypeToElementDefinition.get(theDatatype);
	}

	public Set<Class<? extends IBase>> getValidChildTypes() {
		return Collections.unmodifiableSet((myDatatypeToElementDefinition.keySet()));
	}

}
