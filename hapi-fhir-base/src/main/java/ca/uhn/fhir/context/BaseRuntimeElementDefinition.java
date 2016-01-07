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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

public abstract class BaseRuntimeElementDefinition<T extends IBase> {

	private static final Class<Void> VOID_CLASS = Void.class;
	
	private final String myName;
	private final Class<? extends T> myImplementingClass;
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensions = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsNonModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private final boolean myStandardType;
	private Map<Class<?>, Constructor<T>> myConstructors = Collections.synchronizedMap(new HashMap<Class<?>, Constructor<T>>());

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

	public boolean isStandardType() {
		return myStandardType;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"[" + getName() + ", " + getImplementingClass().getSimpleName() + "]";
	}

	public void addExtension(RuntimeChildDeclaredExtensionDefinition theExtension) {
		if (theExtension == null) {
			throw new NullPointerException();
		}
		myExtensions.add(theExtension);
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensions() {
		return myExtensions;
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensionsModifier() {
		return myExtensionsModifier;
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensionsNonModifier() {
		return myExtensionsNonModifier;
	}

	/**
	 * @return Returns null if none
	 */
	public RuntimeChildDeclaredExtensionDefinition getDeclaredExtension(String theExtensionUrl) {
		return myUrlToExtension.get(theExtensionUrl);
	}

	/**
	 * @return Returns the runtime name for this resource (i.e. the name that
	 *         will be used in encoded messages)
	 */
	public String getName() {
		return myName;
	}

	public T newInstance() {
		return newInstance(null);
	}

	public T newInstance(Object theArgument) {
		try {
			if (theArgument == null) {
				return getConstructor(null).newInstance(null);
			} else {
				return getConstructor(theArgument).newInstance(theArgument);
			}
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		}
	}

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

	public Class<? extends T> getImplementingClass() {
		return myImplementingClass;
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
			} else {
				myUrlToExtension.put(extUrl, next);
			}
			if (next.isModifier()) {
				myExtensionsModifier.add(next);
			} else {
				myExtensionsNonModifier.add(next);
			}

		}

		myExtensions = Collections.unmodifiableList(myExtensions);
	}

	public abstract ChildTypeEnum getChildType();

	public enum ChildTypeEnum {
		COMPOSITE_DATATYPE, PRIMITIVE_DATATYPE, RESOURCE, RESOURCE_REF, RESOURCE_BLOCK, 
		/**
		 * HAPI style.
		 */
		PRIMITIVE_XHTML, 
		UNDECL_EXT, EXTENSION_DECLARED, 
		/**
		 * HAPI structure style.
		 */
		CONTAINED_RESOURCES, 
		ID_DATATYPE, 
		/**
		 * HL7.org structure style.
		 */
		CONTAINED_RESOURCE_LIST, 
		
		/**
		 * HL7.org style.
		 */
		PRIMITIVE_XHTML_HL7ORG, 
		
	}

}
