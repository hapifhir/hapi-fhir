package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.lang.reflect.Constructor;
import java.util.*;
import ca.uhn.fhir.util.UrlUtil;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

public abstract class BaseRuntimeElementDefinition<T extends IBase> {

	private static final Class<Void> VOID_CLASS = Void.class;
	
	private Map<Class<?>, Constructor<T>> myConstructors = Collections.synchronizedMap(new HashMap<Class<?>, Constructor<T>>());
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensions = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsNonModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private final Class<? extends T> myImplementingClass;
	private final String myName;
	private final boolean myStandardType;
	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();

	public BaseRuntimeElementDefinition(String theName, Class<? extends T> theImplementingClass, boolean theStandardType) {
		assert StringUtils.isNotBlank(theName);
		assert theImplementingClass != null;

		String name = theName;
		// TODO: remove this and fix for the model
		if (name.endsWith("Dt")) {
			name = name.substring(0, name.length() - 2);
		}
		
		
		myName = name;
		myStandardType = theStandardType;
		myImplementingClass = theImplementingClass;
	}

	public void addExtension(RuntimeChildDeclaredExtensionDefinition theExtension) {
		if (theExtension == null) {
			throw new NullPointerException();
		}
		myExtensions.add(theExtension);
	}

	public abstract ChildTypeEnum getChildType();

	@SuppressWarnings("unchecked")
	private Constructor<T> getConstructor(Object theArgument) {
		
		Class<? extends Object> argumentType;
		if (theArgument == null) {
			argumentType = VOID_CLASS;
		} else {
			argumentType = theArgument.getClass();
		}
		
		Constructor<T> retVal = myConstructors.get(argumentType);
		if (retVal == null) {
			for (Constructor<?> next : getImplementingClass().getConstructors()) {
				if (argumentType == VOID_CLASS) {
					if (next.getParameterTypes().length == 0) {
						retVal = (Constructor<T>) next;
						break;
					}
				} else if (next.getParameterTypes().length == 1) {
					if (next.getParameterTypes()[0].isAssignableFrom(argumentType)) {
						retVal = (Constructor<T>) next;
						break;
					}
				}
			}
			if (retVal == null) {
				throw new ConfigurationException("Class " + getImplementingClass() + " has no constructor with a single argument of type " + argumentType);
			}
			myConstructors.put(argumentType, retVal);
		}
		return retVal;
	}

	/**
	 * @return Returns null if none
	 */
	public RuntimeChildDeclaredExtensionDefinition getDeclaredExtension(String theExtensionUrl, final String serverBaseUrl) {
		validateSealed();
		RuntimeChildDeclaredExtensionDefinition definition = myUrlToExtension.get(theExtensionUrl);
		if (definition == null && StringUtils.isNotBlank(serverBaseUrl)) {
			for (final Map.Entry<String, RuntimeChildDeclaredExtensionDefinition> entry : myUrlToExtension.entrySet()) {
				final String key = (!UrlUtil.isValid(entry.getKey()) && StringUtils.isNotBlank(serverBaseUrl)) ? serverBaseUrl + entry.getKey() : entry.getKey();
				if (key.equals(theExtensionUrl)) {
					definition = entry.getValue();
					break;
				}
			}
		}
		return definition;
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensions() {
		validateSealed();
		return myExtensions;
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensionsModifier() {
		validateSealed();
		return myExtensionsModifier;
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensionsNonModifier() {
		validateSealed();
		return myExtensionsNonModifier;
	}

	public Class<? extends T> getImplementingClass() {
		return myImplementingClass;
	}

	/**
	 * @return Returns the runtime name for this resource (i.e. the name that
	 *         will be used in encoded messages)
	 */
	public String getName() {
		return myName;
	}

	public boolean hasExtensions() {
		validateSealed();
		return myExtensions.size() > 0;
	}

	public boolean isStandardType() {
		return myStandardType;
	}

	public T newInstance() {
		return newInstance(null);
	}

	public T newInstance(Object theArgument) {
		try {
			if (theArgument == null) {
				return getConstructor(null).newInstance();
			}
			return getConstructor(theArgument).newInstance(theArgument);

		} catch (Exception e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		}
	}

	/**
	 * Invoked prior to use to perform any initialization and make object
	 * mutable.
	 * @param theContext TODO
	 */
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		for (BaseRuntimeChildDefinition next : myExtensions) {
			next.sealAndInitialize(theContext, theClassToElementDefinitions);
		}

		for (RuntimeChildDeclaredExtensionDefinition next : myExtensions) {
			String extUrl = next.getExtensionUrl();
			if (myUrlToExtension.containsKey(extUrl)) {
				throw new ConfigurationException("Duplicate extension URL[" + extUrl + "] in Element[" + getName() + "]");
			}
			myUrlToExtension.put(extUrl, next);
			if (next.isModifier()) {
				myExtensionsModifier.add(next);
			} else {
				myExtensionsNonModifier.add(next);
			}

		}

		myExtensions = Collections.unmodifiableList(myExtensions);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"[" + getName() + ", " + getImplementingClass().getSimpleName() + "]";
	}

	protected void validateSealed() {
		/* 
		 * this does nothing, but BaseRuntimeElementCompositeDefinition
		 * overrides this method to provide functionality because that class
		 * defers the dealing process
		 */
		
	}

	public enum ChildTypeEnum {
		COMPOSITE_DATATYPE, /**
		 * HL7.org structure style.
		 */
		CONTAINED_RESOURCE_LIST, /**
		 * HAPI structure style.
		 */
		CONTAINED_RESOURCES, EXTENSION_DECLARED, 
		ID_DATATYPE, 
		PRIMITIVE_DATATYPE, /**
		 * HAPI style.
		 */
		PRIMITIVE_XHTML, 
		/**
		 * HL7.org style.
		 */
		PRIMITIVE_XHTML_HL7ORG, 
		RESOURCE, 
		RESOURCE_BLOCK, 
		
		UNDECL_EXT, 
		
	}

}
