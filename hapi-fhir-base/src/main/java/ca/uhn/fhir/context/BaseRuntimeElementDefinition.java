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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public abstract class BaseRuntimeElementDefinition<T extends IElement> {

	private String myName;
	private Class<? extends T> myImplementingClass;
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensions = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensionsNonModifier = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();

	public BaseRuntimeElementDefinition(String theName, Class<? extends T> theImplementingClass) {
		assert StringUtils.isNotBlank(theName);
		assert theImplementingClass != null;

		myName = theName;
		
		// TODO: remove this and fix for the model
		if (myName.endsWith("Dt")) {
			myName = myName.substring(0, myName.length() - 2);
		}
			
		
		myImplementingClass = theImplementingClass;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"[" + getName() + "]";
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
	 * Returns null if none
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
				return getImplementingClass().newInstance();
			} else {
				return getImplementingClass().getConstructor(IValueSetEnumBinder.class).newInstance(theArgument);
			}
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to instantiate type:" + getImplementingClass().getName(), e);
		}
	}

	public Class<? extends T> getImplementingClass() {
		return myImplementingClass;
	}

	/**
	 * Invoked prior to use to perform any initialization and make object
	 * mutable
	 */
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		for (BaseRuntimeChildDefinition next : myExtensions) {
			next.sealAndInitialize(theClassToElementDefinitions);
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
		COMPOSITE_DATATYPE, PRIMITIVE_DATATYPE, RESOURCE, RESOURCE_REF, RESOURCE_BLOCK, PRIMITIVE_XHTML, UNDECL_EXT, EXTENSION_DECLARED, CONTAINED_RESOURCES

	}

}
