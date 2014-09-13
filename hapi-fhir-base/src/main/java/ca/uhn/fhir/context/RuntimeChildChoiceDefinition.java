package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;

public class RuntimeChildChoiceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private List<Class<? extends IElement>> myChoiceTypes;
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChildDefinition;
	private Map<Class<? extends IElement>, String> myDatatypeToElementName;
	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myDatatypeToElementDefinition;

	public RuntimeChildChoiceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, List<Class<? extends IElement>> theChoiceTypes) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);

		myChoiceTypes = Collections.unmodifiableList(theChoiceTypes);
	}

	/**
	 * For extension, if myChoiceTypes will be set some other way
	 */
	RuntimeChildChoiceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
	}

	void setChoiceTypes(List<Class<? extends IElement>> theChoiceTypes) {
		myChoiceTypes = Collections.unmodifiableList(theChoiceTypes);
	}

	public List<Class<? extends IElement>> getChoices() {
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
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myNameToChildDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		myDatatypeToElementName = new HashMap<Class<? extends IElement>, String>();
		myDatatypeToElementDefinition = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();

		for (Class<? extends IElement> next : myChoiceTypes) {

			String elementName;
			String alternateElementName = null;
			BaseRuntimeElementDefinition<?> nextDef;
			if (IResource.class.isAssignableFrom(next)) {
				elementName = getElementName() + StringUtils.capitalize(next.getSimpleName());
				alternateElementName = getElementName() + "Resource";
				List<Class<? extends IResource>> types = new ArrayList<Class<? extends IResource>>();
				types.add((Class<? extends IResource>) next);
				nextDef = new RuntimeResourceReferenceDefinition(elementName, types);
				nextDef.sealAndInitialize(theClassToElementDefinitions);
			} else {
				nextDef = theClassToElementDefinitions.get(next);
				elementName = getElementName() + StringUtils.capitalize(nextDef.getName());
			}

			myNameToChildDefinition.put(elementName, nextDef);
			if (alternateElementName != null) {
				myNameToChildDefinition.put(alternateElementName, nextDef);
			}
			
			if (IResource.class.isAssignableFrom(next)) {
				myDatatypeToElementDefinition.put(ResourceReferenceDt.class, nextDef);
				alternateElementName = getElementName() + "Resource";
				myDatatypeToElementName.put(ResourceReferenceDt.class, alternateElementName);
			}
			
			myDatatypeToElementDefinition.put(next, nextDef);
			myDatatypeToElementName.put(next, elementName);
		}

		myNameToChildDefinition = Collections.unmodifiableMap(myNameToChildDefinition);
		myDatatypeToElementName = Collections.unmodifiableMap(myDatatypeToElementName);
		myDatatypeToElementDefinition = Collections.unmodifiableMap(myDatatypeToElementDefinition);

	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		return myDatatypeToElementName.get(theDatatype);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		return myDatatypeToElementDefinition.get(theDatatype);
	}

	public Set<Class<? extends IElement>> getValidChildTypes() {
		return Collections.unmodifiableSet((myDatatypeToElementDefinition.keySet()));
	}

}
