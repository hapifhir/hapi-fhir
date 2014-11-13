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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

public class RuntimeResourceDefinition extends BaseRuntimeElementCompositeDefinition<IResource> {

	private RuntimeResourceDefinition myBaseDefinition;
	private Map<String, RuntimeSearchParam> myNameToSearchParam = new LinkedHashMap<String, RuntimeSearchParam>();
	private IResource myProfileDef;
	private String myResourceProfile;
	private List<RuntimeSearchParam> mySearchParams;
	private FhirContext myContext;
	private String myId;

	public RuntimeResourceDefinition(FhirContext theContext, String theResourceName, Class<? extends IResource> theClass, ResourceDef theResourceAnnotation) {
		super(theResourceName, theClass);
		myContext= theContext;
		myResourceProfile = theResourceAnnotation.profile();
		myId = theResourceAnnotation.id();
	}

	public String getId() {
		return myId;
	}

	public void addSearchParam(RuntimeSearchParam theParam) {
		myNameToSearchParam.put(theParam.getName(), theParam);
	}

	/**
	 * If this definition refers to a class which extends another resource definition type, this
	 * method will return the definition of the topmost resource. For example, if this definition
	 * refers to MyPatient2, which extends MyPatient, which in turn extends Patient, this method
	 * will return the resource definition for Patient.
	 * <p>
	 * If the definition has no parent, returns <code>this</code>
	 * </p>
	 */
	public RuntimeResourceDefinition getBaseDefinition() {
		return myBaseDefinition;
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE;
	}

	public String getResourceProfile() {
		return myResourceProfile;
	}

	public RuntimeSearchParam getSearchParam(String theName) {
		return myNameToSearchParam.get(theName);
	}

	public List<RuntimeSearchParam> getSearchParams() {
		return mySearchParams;
	}

	public boolean isStandardProfile() {
		return myResourceProfile.startsWith("http://hl7.org/fhir/profiles");
	}

	@Override
	public void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theClassToElementDefinitions);

		myNameToSearchParam = Collections.unmodifiableMap(myNameToSearchParam);

		ArrayList<RuntimeSearchParam> searchParams = new ArrayList<RuntimeSearchParam>(myNameToSearchParam.values());
		Collections.sort(searchParams, new Comparator<RuntimeSearchParam>() {
			@Override
			public int compare(RuntimeSearchParam theArg0, RuntimeSearchParam theArg1) {
				return theArg0.getName().compareTo(theArg1.getName());
			}
		});
		mySearchParams = Collections.unmodifiableList(searchParams);
		
		Class<?> target = getImplementingClass();
		myBaseDefinition = this;
		do {
			target = target.getSuperclass();
			if (IResource.class.isAssignableFrom(target) && target.getAnnotation(ResourceDef.class)!=null) {
				myBaseDefinition = (RuntimeResourceDefinition) theClassToElementDefinitions.get(target);
			}
		} while (target.equals(Object.class)==false);
	}

	public synchronized IResource toProfile() {
		if (myProfileDef != null) {
			return myProfileDef;
		}

		IResource retVal = myContext.getVersion().generateProfile(this);
		myProfileDef = retVal;

		return retVal;
	}



}
