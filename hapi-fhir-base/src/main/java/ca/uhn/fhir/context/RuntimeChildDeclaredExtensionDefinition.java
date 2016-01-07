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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;

public class RuntimeChildDeclaredExtensionDefinition extends BaseRuntimeDeclaredChildDefinition {

	private BaseRuntimeElementDefinition<?> myChildDef;
	private Class<? extends IBase> myChildType;
	private String myDatatypeChildName;
	private boolean myDefinedLocally;
	private String myExtensionUrl;
	private boolean myModifier;
	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToChildExtension;
	private volatile Object myInstanceConstructorArguments;
	private Class<?> myEnumerationType;

	/**
	 * @param theBoundTypeBinder
	 *            If the child is of a type that requires a constructor argument to instantiate, this is the argument to
	 *            use
	 * @param theDefinedLocally
	 *            See {@link Extension#definedLocally()}
	 */
	RuntimeChildDeclaredExtensionDefinition(Field theField, Child theChild, Description theDescriptionAnnotation, Extension theExtension, String theElementName, String theExtensionUrl, Class<? extends IBase> theChildType, Object theBoundTypeBinder)
			throws ConfigurationException {
		super(theField, theChild, theDescriptionAnnotation, theElementName);
		assert isNotBlank(theExtensionUrl);
		myExtensionUrl = theExtensionUrl;
		myChildType = theChildType;
		myDefinedLocally = theExtension.definedLocally();
		myModifier = theExtension.isModifier();
		myInstanceConstructorArguments = theBoundTypeBinder;
	}

	@Override
	public Object getInstanceConstructorArguments() {
		Object retVal = myInstanceConstructorArguments;
		if (retVal == null && myEnumerationType != null) {
			retVal = RuntimeChildPrimitiveEnumerationDatatypeDefinition.toEnumFactory(myEnumerationType);
			myInstanceConstructorArguments = retVal;
		}
		return retVal;
	}

	public void setEnumerationType(Class<?> theEnumerationType) {
		myEnumerationType = theEnumerationType;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		if (myDatatypeChildName != null) {
			if (myDatatypeChildName.equals(theName)) {
				return myChildDef;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theType) {
		if (myChildType.equals(theType)) {
			return myChildDef;
		}
		return null;
	}

	public RuntimeChildDeclaredExtensionDefinition getChildExtensionForUrl(String theUrl) {
		return myUrlToChildExtension.get(theUrl);
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		if (myChildType.equals(theDatatype) && myDatatypeChildName != null) {
			return myDatatypeChildName;
		} else {
			return "extension";
		}
	}

	public Class<? extends IBase> getChildType() {
		return myChildType;
	}

	@Override
	public String getExtensionUrl() {
		return myExtensionUrl;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getSingleChildOrThrow() {
		return myChildDef;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.emptySet();
	}

	public boolean isDefinedLocally() {
		return myDefinedLocally;
	}

	public boolean isModifier() {
		return myModifier;
	}

	public IBase newInstance() {
		try {
			return myChildType.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type:" + myChildType.getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type:" + myChildType.getName(), e);
		}
	}

	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myUrlToChildExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();
		
		BaseRuntimeElementDefinition<?> elementDef = theClassToElementDefinitions.get(myChildType);
		if (elementDef instanceof RuntimePrimitiveDatatypeDefinition || elementDef instanceof RuntimeCompositeDatatypeDefinition) {
			myDatatypeChildName = "value" + elementDef.getName().substring(0, 1).toUpperCase() + elementDef.getName().substring(1);
			if ("valueResourceReference".equals(myDatatypeChildName)) {
				// Per one of the examples here: http://hl7.org/implement/standards/fhir/extensibility.html#extension
				myDatatypeChildName = "valueResource";
				List<Class<? extends IBaseResource>> types = new ArrayList<Class<? extends IBaseResource>>();
				types.add(IResource.class);
				myChildDef = new RuntimeResourceReferenceDefinition("valueResource", types, false);
			} else {
				myChildDef = elementDef;
			}
		} else {
			RuntimeResourceBlockDefinition extDef = ((RuntimeResourceBlockDefinition) elementDef);
			for (RuntimeChildDeclaredExtensionDefinition next : extDef.getExtensions()) {
				myUrlToChildExtension.put(next.getExtensionUrl(), next);
			}
			myChildDef = extDef;
		}

		myUrlToChildExtension = Collections.unmodifiableMap(myUrlToChildExtension);
	}

}
