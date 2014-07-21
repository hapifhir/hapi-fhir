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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;

public class RuntimeChildResourceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private BaseRuntimeElementDefinition<?> myRuntimeDef;
	private List<Class<? extends IResource>> myResourceTypes;
	private Set<String> myValidChildNames;

	public RuntimeChildResourceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, List<Class<? extends IResource>> theResourceTypes) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		myResourceTypes = theResourceTypes;

		if (theResourceTypes == null || theResourceTypes.isEmpty()) {
			throw new ConfigurationException("Field '" + theField.getName() + "' on type '" + theField.getDeclaringClass().getCanonicalName() + "' has no resource types noted");
		}
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		if (BaseResourceReferenceDt.class.isAssignableFrom(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		if (BaseResourceReferenceDt.class.isAssignableFrom(theDatatype)) {
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
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myRuntimeDef = new RuntimeResourceReferenceDefinition(getElementName(), myResourceTypes);
		myRuntimeDef.sealAndInitialize(theClassToElementDefinitions);

		myValidChildNames = new HashSet<String>();
		myValidChildNames.add(getElementName());
		myValidChildNames.add(getElementName() + "Resource");

		for (Class<? extends IResource> next : myResourceTypes) {
			if (next == IResource.class) {
				for (Entry<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> nextEntry : theClassToElementDefinitions.entrySet()) {
					if (IResource.class.isAssignableFrom(nextEntry.getKey())) {
						RuntimeResourceDefinition nextDef = (RuntimeResourceDefinition) nextEntry.getValue();
						myValidChildNames.add(getElementName() + nextDef.getName());
					}
				}
			} else {
				RuntimeResourceDefinition nextDef = (RuntimeResourceDefinition) theClassToElementDefinitions.get(next);
				if (nextDef == null) {
					throw new ConfigurationException("Can't find child of type: " + next.getCanonicalName() + " in " + getField().getDeclaringClass());
				}
				myValidChildNames.add(getElementName() + nextDef.getName());
			}
		}

		myResourceTypes = Collections.unmodifiableList(myResourceTypes);
		myValidChildNames = Collections.unmodifiableSet(myValidChildNames);
	}

	public List<Class<? extends IResource>> getResourceTypes() {
		return myResourceTypes;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getElementName() + "]";
	}
}
