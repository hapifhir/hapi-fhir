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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RuntimeResourceDefinition extends BaseRuntimeElementCompositeDefinition<IBaseResource> {

	private Class<? extends IBaseResource> myBaseType;
	private Map<String, List<RuntimeSearchParam>> myCompartmentNameToSearchParams;
	private FhirContext myContext;
	private String myId;
	private Map<String, RuntimeSearchParam> myNameToSearchParam = new LinkedHashMap<String, RuntimeSearchParam>();
	private IBaseResource myProfileDef;
	private String myResourceProfile;
	private List<RuntimeSearchParam> mySearchParams;
	private final FhirVersionEnum myStructureVersion;
	private volatile RuntimeResourceDefinition myBaseDefinition;



	public RuntimeResourceDefinition(FhirContext theContext, String theResourceName, Class<? extends IBaseResource> theClass, ResourceDef theResourceAnnotation, boolean theStandardType, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super(theResourceName, theClass, theStandardType, theContext, theClassToElementDefinitions);
		myContext = theContext;
		myResourceProfile = theResourceAnnotation.profile();
		myId = theResourceAnnotation.id();

		IBaseResource instance;
		try {
			instance = theClass.getConstructor().newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(1730) + myContext.getLocalizer().getMessage(getClass(), "nonInstantiableType", theClass.getName(), e.toString()), e);
		}
		myStructureVersion = instance.getStructureFhirVersionEnum();
		if (myStructureVersion != theContext.getVersion().getVersion()) {
			throw new ConfigurationException(Msg.code(1731) + myContext.getLocalizer().getMessage(getClass(), "typeWrongVersion", theContext.getVersion().getVersion(), theClass.getName(), myStructureVersion));
		}

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
		validateSealed();
		if (myBaseDefinition == null) {
			myBaseDefinition = myContext.getResourceDefinition(myBaseType);
		}
		return myBaseDefinition;
	}

	@Override
	public ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum getChildType() {
		return ChildTypeEnum.RESOURCE;
	}

	public String getId() {
		return myId;
	}

	/**
	 * Express {@link #getImplementingClass()} as theClass (to prevent casting warnings)
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getImplementingClass(Class<T> theClass) {
		if (!theClass.isAssignableFrom(getImplementingClass())) {
			throw new ConfigurationException(Msg.code(1732) + "Unable to convert " + getImplementingClass() + " to " + theClass);
		}
		return (Class<T>) getImplementingClass();
	}

	@Deprecated
	public String getResourceProfile() {
		return myResourceProfile;
	}

	public String getResourceProfile(String theServerBase) {
		validateSealed();
		String profile;
		if (!myResourceProfile.isEmpty()) {
			profile = myResourceProfile;
		} else if (!myId.isEmpty()) {
			profile = myId;
		} else {
			return "";
		}

		if (!UrlUtil.isValid(profile)) {
			String resourceName = "/StructureDefinition/";
			String profileWithUrl = theServerBase + resourceName + profile;
			if (UrlUtil.isValid(profileWithUrl)) {
				return profileWithUrl;
			}
		}
		return profile;
	}

	public RuntimeSearchParam getSearchParam(String theName) {
		validateSealed();
		return myNameToSearchParam.get(theName);
	}

	public List<RuntimeSearchParam> getSearchParams() {
		validateSealed();
		return mySearchParams;
	}

	/**
	 * Will not return null
	 */
	public List<RuntimeSearchParam> getSearchParamsForCompartmentName(String theCompartmentName) {
		validateSealed();
		List<RuntimeSearchParam> retVal = myCompartmentNameToSearchParams.get(theCompartmentName);
		if (retVal == null) {
			return Collections.emptyList();
		}
		return retVal;
	}

	public FhirVersionEnum getStructureVersion() {
		return myStructureVersion;
	}

	public boolean isBundle() {
		return "Bundle".equals(getName());
	}

	@SuppressWarnings("unchecked")
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

		Map<String, List<RuntimeSearchParam>> compartmentNameToSearchParams = new HashMap<>();
		for (RuntimeSearchParam next : searchParams) {
			if (next.getProvidesMembershipInCompartments() != null) {
				for (String nextCompartment : next.getProvidesMembershipInCompartments()) {

					if (nextCompartment.startsWith("Base FHIR compartment definition for ")) {
						nextCompartment = nextCompartment.substring("Base FHIR compartment definition for ".length());
					}

					if (!compartmentNameToSearchParams.containsKey(nextCompartment)) {
						compartmentNameToSearchParams.put(nextCompartment, new ArrayList<>());
					}
					List<RuntimeSearchParam> searchParamsForCompartment = compartmentNameToSearchParams.get(nextCompartment);
					searchParamsForCompartment.add(next);

					/*
					 * If one search parameter marks an SP as making a resource
					 * a part of a compartment, let's also denote all other
					 * SPs with the same path the same way. This behaviour is
					 * used by AuthorizationInterceptor
					 */
					String nextPath = massagePathForCompartmentSimilarity(next.getPath());
					for (RuntimeSearchParam nextAlternate : searchParams) {
						String nextAlternatePath = massagePathForCompartmentSimilarity(nextAlternate.getPath());
						if (nextAlternatePath.equals(nextPath)) {
							if (!nextAlternate.getName().equals(next.getName())) {
								searchParamsForCompartment.add(nextAlternate);
							}
						}
					}
				}
			}
		}

		// Make the map of lists completely unmodifiable
		for (String nextKey : new ArrayList<>(compartmentNameToSearchParams.keySet())) {
			List<RuntimeSearchParam> nextList = compartmentNameToSearchParams.get(nextKey);
			compartmentNameToSearchParams.put(nextKey, Collections.unmodifiableList(nextList));
		}
		myCompartmentNameToSearchParams = Collections.unmodifiableMap(compartmentNameToSearchParams);

		Class<?> target = getImplementingClass();
		myBaseType = (Class<? extends IBaseResource>) target;
		do {
			target = target.getSuperclass();
			if (IBaseResource.class.isAssignableFrom(target) && target.getAnnotation(ResourceDef.class) != null) {
				myBaseType = (Class<? extends IBaseResource>) target;
			}
		} while (target.equals(Object.class) == false);
		
		/*
		 * See #504:
		 * Bundle types may not have extensions
		 */
		if (hasExtensions()) {
			if (IAnyResource.class.isAssignableFrom(getImplementingClass())) {
				if (!IDomainResource.class.isAssignableFrom(getImplementingClass())) {
					throw new ConfigurationException(Msg.code(1733) + "Class \"" + getImplementingClass() + "\" is invalid. This resource type is not a DomainResource, it must not have extensions");
				}
			}
		}

	}

	private String massagePathForCompartmentSimilarity(String thePath) {
		String path = thePath;
		if (path.matches(".*\\.where\\(resolve\\(\\) is [a-zA-Z]+\\)")) {
			path = path.substring(0, path.indexOf(".where"));
		}
		return path;
	}

	@Deprecated
	public synchronized IBaseResource toProfile() {
		validateSealed();
		if (myProfileDef != null) {
			return myProfileDef;
		}

		IBaseResource retVal = myContext.getVersion().generateProfile(this, null);
		myProfileDef = retVal;

		return retVal;
	}

	public synchronized IBaseResource toProfile(String theServerBase) {
		validateSealed();
		if (myProfileDef != null) {
			return myProfileDef;
		}

		IBaseResource retVal = myContext.getVersion().generateProfile(this, theServerBase);
		myProfileDef = retVal;

		return retVal;
	}

}
