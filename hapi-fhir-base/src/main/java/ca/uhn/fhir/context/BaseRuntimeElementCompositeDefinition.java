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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseRuntimeElementCompositeDefinition<T extends IBase> extends BaseRuntimeElementDefinition<T> {

	private List<BaseRuntimeChildDefinition> myChildren = new ArrayList<BaseRuntimeChildDefinition>();
	private List<BaseRuntimeChildDefinition> myChildrenAndExtensions;
	private Map<String, BaseRuntimeChildDefinition> myNameToChild = new HashMap<String, BaseRuntimeChildDefinition>();

	public BaseRuntimeElementCompositeDefinition(String theName, Class<? extends T> theImplementingClass, boolean theStandardType) {
		super(theName, theImplementingClass, theStandardType);
	}

	void addChild(BaseRuntimeChildDefinition theNext) {
		if (theNext == null) {
			throw new NullPointerException();
		}
		if (theNext.getExtensionUrl() != null) {
			throw new IllegalArgumentException("Shouldn't haven an extension URL, use addExtension instead");
		}
		myChildren.add(theNext);
	}

	public BaseRuntimeChildDefinition getChildByName(String theName){
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		return retVal;
	}

	public BaseRuntimeChildDefinition getChildByNameOrThrowDataFormatException(String theName) throws DataFormatException {
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		if (retVal == null) {
			throw new DataFormatException("Unknown child name '" + theName + "' in element " + getName() + " - Valid names are: " + new TreeSet<String>(myNameToChild.keySet()));
		}
		return retVal;
	}

	public List<BaseRuntimeChildDefinition> getChildren() {
		return myChildren;
	}

	public List<BaseRuntimeChildDefinition> getChildrenAndExtension() {
		return myChildrenAndExtensions;
	}

	@Override 
	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		super.sealAndInitialize(theContext, theClassToElementDefinitions);

		for (BaseRuntimeChildDefinition next : myChildren) {
			next.sealAndInitialize(theContext, theClassToElementDefinitions);
		}

		myNameToChild = new HashMap<String, BaseRuntimeChildDefinition>();
		for (BaseRuntimeChildDefinition next : myChildren) {	
			if (next instanceof RuntimeChildChoiceDefinition) {
				String key = ((RuntimeChildChoiceDefinition) next).getElementName()+"[x]";
				myNameToChild.put(key, next);
			}
			for (String nextName : next.getValidChildNames()) {
				if (myNameToChild.containsKey(nextName)) {
					throw new ConfigurationException("Duplicate child name[" + nextName + "] in Element[" + getName() + "]");
				} else {
					myNameToChild.put(nextName, next);
				}
			}
		}

		myChildren = Collections.unmodifiableList(myChildren);
		myNameToChild = Collections.unmodifiableMap(myNameToChild);
		
		List<BaseRuntimeChildDefinition> children = new ArrayList<BaseRuntimeChildDefinition>();
		children.addAll(myChildren);
		
		/*
		 * Because of the way the type hierarchy works for DSTU2 resources,
		 * things end up in the wrong order
		 */
		if (theContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
			int extIndex = findIndex(children, "extension", false);
			int containedIndex = findIndex(children, "contained", false);
			if (containedIndex != -1 && extIndex != -1 && extIndex < containedIndex) {
				BaseRuntimeChildDefinition extension = children.remove(extIndex);
				if (containedIndex > children.size()) {
					children.add(extension);
				} else {
					children.add(containedIndex, extension);
				}
				int modIndex = findIndex(children, "modifierExtension", false);
				if (modIndex < containedIndex) {
					extension = children.remove(modIndex);
					if (containedIndex > children.size()) {
						children.add(extension);
					} else {
						children.add(containedIndex, extension);
					}
				}
			}
		}
		
		/*
		 * Add declared extensions alongside the undeclared ones
		 */
		if (getExtensionsNonModifier().isEmpty() == false) {
			children.addAll(findIndex(children, "extension", true), getExtensionsNonModifier());
		}
		if (getExtensionsModifier().isEmpty() == false) {
			children.addAll(findIndex(children, "modifierExtension", true), getExtensionsModifier());
		}
		
		myChildrenAndExtensions=Collections.unmodifiableList(children);
	}

	private static int findIndex(List<BaseRuntimeChildDefinition> theChildren, String theName, boolean theDefaultAtEnd) {
		int index = theDefaultAtEnd ? theChildren.size() : -1;
		for (ListIterator<BaseRuntimeChildDefinition> iter = theChildren.listIterator(); iter.hasNext(); ) {
			if (iter.next().getElementName().equals(theName)) {
				index = iter.previousIndex();
				break;
			}
		}
		return index;
	}

}
