package ca.uhn.fhir.util;

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
import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class FhirTerser {

	public FhirTerser(FhirContext theContext) {
		super();
		myContext = theContext;
	}

	private FhirContext myContext;

	public BaseRuntimeChildDefinition getDefinition(Class<? extends IResource> theResourceType, String thePath) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResourceType);

		BaseRuntimeElementCompositeDefinition<?> currentDef = def;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size());
		if (subList.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getDefinition(currentDef, subList);

	}

	private BaseRuntimeChildDefinition getDefinition(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, List<String> theSubList) {
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(theSubList.get(0));

		if (theSubList.size() == 1) {
			return nextDef;
		} else {
			BaseRuntimeElementCompositeDefinition<?> cmp = (BaseRuntimeElementCompositeDefinition<?>) nextDef.getChildByName(theSubList.get(0));
			return getDefinition(cmp, theSubList.subList(1, theSubList.size()));
		}
	}

	public List<Object> getValues(IResource theResource, String thePath) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);

		BaseRuntimeElementCompositeDefinition<?> currentDef = def;
		Object currentObj = theResource;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size());
		if (subList.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getValues(currentDef, currentObj, subList);

	}

	private List<Object> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, Object theCurrentObj, List<String> theSubList) {
		String name = theSubList.get(0);
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(name);
		List<? extends IElement> values = nextDef.getAccessor().getValues(theCurrentObj);
		List<Object> retVal = new ArrayList<Object>();

		if (theSubList.size() == 1) {
			retVal.addAll(values);
		} else {
			for (IElement nextElement : values) {
				BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
				List<?> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()));
				retVal.addAll(foundValues);
			}
		}
		return retVal;
	}

	/**
	 * Returns a list containing all child elements (including the resource itself) which are <b>non-empty</b>
	 * and are either of the exact type specified, or are a subclass of that type.
	 * <p>
	 * For example, specifying a type of {@link StringDt} would return all non-empty string instances within
	 * the message. Specifying a type of {@link IResource} would return the resource itself, as well as any contained resources. 
	 * </p>  
	 * @param theResourceT The resource instance to search. Must not be null.
	 * @param theType The type to search for. Must not be null.
	 * @return
	 */
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(IResource theResource, Class<T> theType) {
		ArrayList<T> retVal = new ArrayList<T>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		getAllChildElementsOfType(theResource, def, theType, retVal);
		return retVal;
	}

	private <T extends IElement> void getAllChildElementsOfType(IElement theElement, BaseRuntimeElementDefinition<?> theDefinition, Class<T> theType, ArrayList<T> theList) {
		if (theElement.isEmpty()) {
			return;
		}
		
		addIfCorrectType(theElement, theType, theList);
		addUndeclaredExtensions(theElement, theType, theList);

		switch (theDefinition.getChildType()) {
		case PRIMITIVE_XHTML:
		case PRIMITIVE_DATATYPE:
		case RESOURCE_REF:
			// These are primitive types
			break;
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE:
		case RESOURCE: {
			BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) theDefinition;
			for (BaseRuntimeChildDefinition nextChild : childDef.getChildren()) {
				List<? extends IElement> values = nextChild.getAccessor().getValues(theElement);
				if (values != null) {
					for (IElement nextValue : values) {
						if (nextValue == null) {
							continue;
						}
						if (nextValue.isEmpty()) {
							continue;
						}
						BaseRuntimeElementDefinition<?> childElementDef = nextChild.getChildElementDefinitionByDatatype(nextValue.getClass());
						if (childElementDef == null) {
							throw new DataFormatException("Found value of type[" + nextValue.getClass().getSimpleName() + "] which is not valid for field[" + nextChild.getElementName() + "] in " + childDef.getName());
						}
						getAllChildElementsOfType(nextValue, childElementDef, theType, theList);
					}
				}
			}
			break;
		}
		case CONTAINED_RESOURCES: {
			ContainedDt value = (ContainedDt) theElement;
			for (IResource next : value.getContainedResources()) {
				BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(next);
				getAllChildElementsOfType(next, def, theType, theList);
			}
			break;
		}
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + theDefinition.getChildType());
		}
		}
	}

	private <T extends IElement> void addUndeclaredExtensions(IElement theElement, Class<T> theType, ArrayList<T> theList) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions elem = (ISupportsUndeclaredExtensions) theElement;
			for (ExtensionDt nextExt : elem.getUndeclaredExtensions()) {
				addIfCorrectType(nextExt, theType, theList);
				addIfCorrectType(nextExt.getValue(), theType, theList);
				addUndeclaredExtensions(nextExt, theType, theList);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends IElement> void addIfCorrectType(IElement theElement, Class<T> theType, ArrayList<T> theList) {
		if (theElement != null && theType.isAssignableFrom(theElement.getClass())) {
			theList.add((T) theElement);
		}
	}

}
