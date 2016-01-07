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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;

public class RuntimeResourceReferenceDefinition extends BaseRuntimeElementDefinition<BaseResourceReferenceDt> {

	private final List<Class<? extends IBaseResource>> myResourceTypes;
	private HashMap<Class<? extends IBaseResource>, RuntimeResourceDefinition> myResourceTypeToDefinition;

	/**
	 * Constructor
	 * @param theStandardType 
	 */
	public RuntimeResourceReferenceDefinition(String theName, List<Class<? extends IBaseResource>> theResourceTypes, boolean theStandardType) {
		super(theName, BaseResourceReferenceDt.class, theStandardType);
		if (theResourceTypes == null || theResourceTypes.isEmpty()) {
			throw new ConfigurationException("Element '" + theName + "' has no resource types noted");
		}
		myResourceTypes = theResourceTypes;
	}

	public List<Class<? extends IBaseResource>> getResourceTypes() {
		return myResourceTypes;
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myResourceTypeToDefinition = new HashMap<Class<? extends IBaseResource>, RuntimeResourceDefinition>();
		for (Class<? extends IBaseResource> next : myResourceTypes) {
			if (next.equals(IResource.class) || next.equals(IAnyResource.class) || next.equals(IBaseResource.class)) {
				continue;
			}
			RuntimeResourceDefinition definition = (RuntimeResourceDefinition) theClassToElementDefinitions.get(next);
			if (definition == null) {
				throw new ConfigurationException("Couldn't find definition for: " + next.getCanonicalName());
			}
			myResourceTypeToDefinition.put(next, definition);
		}
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE_REF;
	}

	public RuntimeResourceDefinition getDefinitionForResourceType(Class<? extends IResource> theType) {
		RuntimeResourceDefinition retVal = myResourceTypeToDefinition.get(theType);
		if (retVal == null) {
			throw new ConfigurationException("Unknown type:  " + theType.getCanonicalName());
		}
		return retVal;
	}

}
