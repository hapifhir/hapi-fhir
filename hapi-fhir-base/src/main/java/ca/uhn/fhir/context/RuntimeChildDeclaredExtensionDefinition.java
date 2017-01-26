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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;

public class RuntimeChildDeclaredExtensionDefinition extends RuntimeChildChoiceDefinition {

	private boolean myDefinedLocally;
	private String myExtensionUrl;
	private boolean myModifier;
	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToChildExtension;
	private volatile Object myInstanceConstructorArguments;
	private Class<?> myEnumerationType;
	private Class<? extends IBase> myChildType;
	private RuntimeResourceBlockDefinition myChildResourceBlock;

	/**
	 * @param theBoundTypeBinder
	 *            If the child is of a type that requires a constructor argument to instantiate, this is the argument to
	 *            use
	 * @param theDefinedLocally
	 *            See {@link Extension#definedLocally()}
	 */
	@SuppressWarnings("unchecked")
	RuntimeChildDeclaredExtensionDefinition(Field theField, Child theChild, Description theDescriptionAnnotation, Extension theExtension, String theElementName, String theExtensionUrl, Class<? extends IBase> theChildType, Object theBoundTypeBinder)
			throws ConfigurationException {
		super(theField, theElementName, theChild, theDescriptionAnnotation);
		assert isNotBlank(theExtensionUrl);
		myExtensionUrl = theExtensionUrl;
		myChildType = theChildType;
		myDefinedLocally = theExtension.definedLocally();
		myModifier = theExtension.isModifier();
		myInstanceConstructorArguments = theBoundTypeBinder;
		
		List<Class<? extends IBase>> choiceTypes = new ArrayList<Class<? extends IBase>>();
		for (Class<? extends IElement> next : theChild.type()) {
			choiceTypes.add(next);
		}
		if (Modifier.isAbstract(theField.getType().getModifiers()) == false) {
			choiceTypes.add((Class<? extends IBase>) theField.getType());
		}
		setChoiceTypes(choiceTypes);
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
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		String retVal = super.getChildNameByDatatype(theDatatype);
		if (retVal == null) {
			if (myModifier) {
				return "modifierExtension";
			}else {
				return "extension";
			}
		}
		return retVal;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
		if (myChildResourceBlock != null) {
			if (myChildResourceBlock.getImplementingClass().equals(theDatatype)) {
				return myChildResourceBlock;
			}
		}
		return super.getChildElementDefinitionByDatatype(theDatatype);
	}

	public RuntimeChildDeclaredExtensionDefinition getChildExtensionForUrl(String theUrl) {
		return myUrlToChildExtension.get(theUrl);
	}

//	@Override
//	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
//		String retVal = super.getChildNameByDatatype(theDatatype);
//		if (retVal )
//		if (myChildType.equals(theDatatype) && myDatatypeChildName != null) {
//			return myDatatypeChildName;
//		} else {
//			return "extension";
//		}
//	}


	@Override
	public String getExtensionUrl() {
		return myExtensionUrl;
	}


	public boolean isDefinedLocally() {
		return myDefinedLocally;
	}

	@Override
	public boolean isModifier() {
		return myModifier;
	}


	@Override
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myUrlToChildExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();
		
		BaseRuntimeElementDefinition<?> elementDef = theClassToElementDefinitions.get(myChildType);
		
		/*
		 * This will happen for any type that isn't defined in the base set of 
		 * built-in types, e.g. custom structures or custom extensions
		 */
		if (elementDef == null) {
			if (Modifier.isAbstract(myChildType.getModifiers()) == false) {
				elementDef = theContext.getElementDefinition(myChildType);
			}
		}
		
		if (elementDef instanceof RuntimeResourceBlockDefinition) {
			RuntimeResourceBlockDefinition extDef = ((RuntimeResourceBlockDefinition) elementDef);
			for (RuntimeChildDeclaredExtensionDefinition next : extDef.getExtensions()) {
				myUrlToChildExtension.put(next.getExtensionUrl(), next);
			}
			myChildResourceBlock = (RuntimeResourceBlockDefinition)elementDef;
		}

		myUrlToChildExtension = Collections.unmodifiableMap(myUrlToChildExtension);
		
		super.sealAndInitialize(theContext, theClassToElementDefinitions);
	}

}
