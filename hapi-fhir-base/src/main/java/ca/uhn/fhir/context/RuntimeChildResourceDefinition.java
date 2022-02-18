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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuntimeChildResourceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private BaseRuntimeElementDefinition<?> myRuntimeDef;
	private List<Class<? extends IBaseResource>> myResourceTypes;
	private Set<String> myValidChildNames;

	/**
	 * Constructor
	 */
	public RuntimeChildResourceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, List<Class<? extends IBaseResource>> theResourceTypes) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		myResourceTypes = theResourceTypes;

		if (theResourceTypes == null || theResourceTypes.isEmpty()) {
			myResourceTypes = new ArrayList<>();
			myResourceTypes.add(IBaseResource.class);
		}
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		if (IBaseReference.class.isAssignableFrom(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		if (IBaseReference.class.isAssignableFrom(theDatatype)) {
			return myRuntimeDef;
		}
		return null;
	}

	@Override
	public Set<String> getValidChildNames() {
		return myValidChildNames;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return myRuntimeDef;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myRuntimeDef = findResourceReferenceDefinition(theClassToElementDefinitions);

		myValidChildNames = new HashSet<String>();
		myValidChildNames.add(getElementName());
		
		/*
		 * [elementName]Resource is not actually valid FHIR but we've encountered it in the wild
		 * so we'll accept it just to be nice
		 */
		myValidChildNames.add(getElementName() + "Resource");

		/*
		 * Below has been disabled- We used to allow field names to contain the name of the resource
		 * that they accepted. This wasn't valid but we accepted it just to be flexible because there
		 * were some bad examples containing this. This causes conflicts with actual field names in 
		 * recent definitions though, so it has been disabled as of HAPI 0.9 
		 */
//		for (Class<? extends IBaseResource> next : myResourceTypes) {
//			if (next == IResource.class) {
//				for (Entry<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> nextEntry : theClassToElementDefinitions.entrySet()) {
//					if (IResource.class.isAssignableFrom(nextEntry.getKey())) {
//						RuntimeResourceDefinition nextDef = (RuntimeResourceDefinition) nextEntry.getValue();
//						myValidChildNames.add(getElementName() + nextDef.getName());
//					}
//				}
//			} 
//			else {
//				RuntimeResourceDefinition nextDef = (RuntimeResourceDefinition) theClassToElementDefinitions.get(next);
//				if (nextDef == null) {
//					throw new ConfigurationException(Msg.code(1691) + "Can't find child of type: " + next.getCanonicalName() + " in " + getField().getDeclaringClass());
//				}
//				myValidChildNames.add(getElementName() + nextDef.getName());
//			}
//		}

		myResourceTypes = Collections.unmodifiableList(myResourceTypes);
		myValidChildNames = Collections.unmodifiableSet(myValidChildNames);
	}

	public List<Class<? extends IBaseResource>> getResourceTypes() {
		return myResourceTypes;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getElementName() + "]";
	}
}
