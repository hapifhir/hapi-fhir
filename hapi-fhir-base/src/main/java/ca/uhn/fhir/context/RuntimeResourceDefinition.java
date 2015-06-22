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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.UrlUtil;

public class RuntimeResourceDefinition extends BaseRuntimeElementCompositeDefinition<IBaseResource> {

	private RuntimeResourceDefinition myBaseDefinition;
	private Map<String, RuntimeSearchParam> myNameToSearchParam = new LinkedHashMap<String, RuntimeSearchParam>();
	private IBaseResource myProfileDef;
	private String myResourceProfile;
	private List<RuntimeSearchParam> mySearchParams;
	private FhirContext myContext;
	private String myId;
	private final FhirVersionEnum myStructureVersion;

	public FhirVersionEnum getStructureVersion() {
		return myStructureVersion;
	}

	public RuntimeResourceDefinition(FhirContext theContext, String theResourceName, Class<? extends IBaseResource> theClass, ResourceDef theResourceAnnotation, boolean theStandardType) {
		super(theResourceName, theClass, theStandardType);
		myContext= theContext;
		myResourceProfile = theResourceAnnotation.profile();
		myId = theResourceAnnotation.id();
		
		try {
			IBaseResource instance = theClass.newInstance();
			if (instance instanceof IAnyResource) {
				myStructureVersion = FhirVersionEnum.DSTU2_HL7ORG;
			} else {
				myStructureVersion = ((IResource)instance).getStructureFhirVersionEnum();
			}
		} catch (Exception e) {
			throw new ConfigurationException(myContext.getLocalizer().getMessage(getClass(), "nonInstantiableType", theClass.getName(), e.toString()), e);
		}
		
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

	@Deprecated
	public String getResourceProfile() {
		return myResourceProfile;
	}

	public String getResourceProfile(String theServerBase) {
		String profile;
		if (!myResourceProfile.isEmpty()) {
			profile = myResourceProfile;
		} else if (!myId.isEmpty()) {
			profile = myId;
		} else {
			return "";
		}

		if (!UrlUtil.isValid(profile)) {
			String profileWithUrl = theServerBase + "/Profile/" + profile;
			if (UrlUtil.isValid(profileWithUrl)) {
				return profileWithUrl;
			}
		}
		return profile;
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
	public void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theContext, theClassToElementDefinitions);

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

	@Deprecated
	public synchronized IBaseResource toProfile() {
		if (myProfileDef != null) {
			return myProfileDef;
		}

		IBaseResource retVal = myContext.getVersion().generateProfile(this, null);
		myProfileDef = retVal;

		return retVal;
	}

	public synchronized IBaseResource toProfile(String theServerBase) {
		if (myProfileDef != null) {
			return myProfileDef;
		}

		IBaseResource retVal = myContext.getVersion().generateProfile(this, theServerBase);
		myProfileDef = retVal;

		return retVal;
	}

	public boolean isBundle() {
		return "Bundle".equals(getName());
	}
}
