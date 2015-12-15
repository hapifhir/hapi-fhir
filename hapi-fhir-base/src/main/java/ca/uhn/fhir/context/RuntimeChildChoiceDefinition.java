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
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildChoiceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private List<Class<? extends IBase>> myChoiceTypes;
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChildDefinition;
	private Map<Class<? extends IBase>, String> myDatatypeToElementName;
	private Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> myDatatypeToElementDefinition;
	private String myReferenceSuffix;

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

		if (theContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
			myReferenceSuffix = "Resource";
		} else {
			myReferenceSuffix = "Reference";
		}

		for (Class<? extends IBase> next : myChoiceTypes) {

			String elementName = null;
			BaseRuntimeElementDefinition<?> nextDef;
			boolean nonPreferred = false;
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
				BaseRuntimeElementDefinition<?> nextDefForChoice = nextDef;
				/*
				 * In HAPI 1.3 the following applied:
				 * Elements which are called foo[x] and have a choice which is a profiled datatype must use the
				 * unprofiled datatype as the element name. E.g. if foo[x] allows markdown as a datatype, it calls the
				 * element fooString when encoded, because markdown is a profile of string. This is according to the
				 * FHIR spec
				 * 
				 * As of HAPI 1.4 this has been disabled after conversation with Grahame. It appears
				 * that it is not correct behaviour.
				 */
//				if (nextDef instanceof IRuntimeDatatypeDefinition) {
//					IRuntimeDatatypeDefinition nextDefDatatype = (IRuntimeDatatypeDefinition) nextDef;
//					if (nextDefDatatype.getProfileOf() != null) {
//						nextDefForChoice = null;
//						nonPreferred = true;
//						Class<? extends IBaseDatatype> profileType = nextDefDatatype.getProfileOf();
//						BaseRuntimeElementDefinition<?> elementDef = theClassToElementDefinitions.get(profileType);
//						elementName = getElementName() + StringUtils.capitalize(elementDef.getName());
//					}
//				}
				if (nextDefForChoice != null) {
					elementName = getElementName() + StringUtils.capitalize(nextDefForChoice.getName());
				}
			}

			// I don't see how elementName could be null here, but eclipse complains..
			if (elementName != null) {
				if (myNameToChildDefinition.containsKey(elementName) == false || !nonPreferred) {
					myNameToChildDefinition.put(elementName, nextDef);
				}
			}

			/*
			 * If this is a resource reference, the element name is "fooNameReference"
			 */
			if (IBaseResource.class.isAssignableFrom(next) || IBaseReference.class.isAssignableFrom(next)) {
				next = theContext.getVersion().getResourceReferenceType();
				elementName = getElementName() + myReferenceSuffix;
			}

			myDatatypeToElementDefinition.put(next, nextDef);

			if (myDatatypeToElementName.containsKey(next)) {
				String existing = myDatatypeToElementName.get(next);
				if (!existing.equals(elementName)) {
					throw new ConfigurationException("Already have element name " + existing + " for datatype " + next.getSimpleName() + " in " + getElementName() + ", cannot add " + elementName);
				}
			} else {
				myDatatypeToElementName.put(next, elementName);
			}
		}

		myNameToChildDefinition = Collections.unmodifiableMap(myNameToChildDefinition);
		myDatatypeToElementName = Collections.unmodifiableMap(myDatatypeToElementName);
		myDatatypeToElementDefinition = Collections.unmodifiableMap(myDatatypeToElementDefinition);

	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		String retVal = myDatatypeToElementName.get(theDatatype);
		return retVal;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		return myDatatypeToElementDefinition.get(theDatatype);
	}

	public Set<Class<? extends IBase>> getValidChildTypes() {
		return Collections.unmodifiableSet((myDatatypeToElementDefinition.keySet()));
	}

}
