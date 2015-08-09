package ca.uhn.fhir.util;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildDirectResource;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class FhirTerser {

	private FhirContext myContext;

	public FhirTerser(FhirContext theContext) {
		super();
		myContext = theContext;
	}

	private void addUndeclaredExtensions(IBase theElement, BaseRuntimeElementDefinition<?> theDefinition, BaseRuntimeChildDefinition theChildDefinition, IModelVisitor theCallback) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions containingElement = (ISupportsUndeclaredExtensions) theElement;
			for (ExtensionDt nextExt : containingElement.getUndeclaredExtensions()) {
				theCallback.acceptUndeclaredExtension(containingElement, null, theChildDefinition, theDefinition, nextExt);
				addUndeclaredExtensions(nextExt, theDefinition, theChildDefinition, theCallback);
			}
		}
	}

	/**
	 * Returns a list containing all child elements (including the resource itself) which are <b>non-empty</b> and are either of the exact type specified, or are a subclass of that type.
	 * <p>
	 * For example, specifying a type of {@link StringDt} would return all non-empty string instances within the message. Specifying a type of {@link IResource} would return the resource itself, as
	 * well as any contained resources.
	 * </p>
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 * 
	 * @param theResource
	 *           The resource instance to search. Must not be null.
	 * @param theType
	 *           The type to search for. Must not be null.
	 * @return Returns a list of all matching elements
	 */
	public <T extends IBase> List<T> getAllPopulatedChildElementsOfType(IBaseResource theResource, final Class<T> theType) {
		final ArrayList<T> retVal = new ArrayList<T>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(new IdentityHashMap<Object, Object>(), theResource, null, null, def, new IModelVisitor() {
			@SuppressWarnings("unchecked")
			@Override
			public void acceptElement(IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}

				if (theType.isAssignableFrom(theElement.getClass())) {
					retVal.add((T) theElement);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition,
					BaseRuntimeElementDefinition<?> theDefinition, ExtensionDt theNextExt) {
				if (theType.isAssignableFrom(theNextExt.getClass())) {
					retVal.add((T) theNextExt);
				}
				if (theNextExt.getValue() != null && theType.isAssignableFrom(theNextExt.getValue().getClass())) {
					retVal.add((T) theNextExt.getValue());
				}
			}
		});
		return retVal;
	}

	public List<ResourceReferenceInfo> getAllResourceReferences(final IBaseResource theResource) {
		final ArrayList<ResourceReferenceInfo> retVal = new ArrayList<ResourceReferenceInfo>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(new IdentityHashMap<Object, Object>(),theResource, null, null, def, new IModelVisitor() {
			@Override
			public void acceptElement(IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}
				if (IBaseReference.class.isAssignableFrom(theElement.getClass())) {
					retVal.add(new ResourceReferenceInfo(myContext, theResource, thePathToElement, (IBaseReference) theElement));
				}
			}

			@Override
			public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition,
					BaseRuntimeElementDefinition<?> theDefinition, ExtensionDt theNextExt) {
				if (theNextExt.getValue() != null && BaseResourceReferenceDt.class.isAssignableFrom(theNextExt.getValue().getClass())) {
					retVal.add(new ResourceReferenceInfo(myContext, theResource, thePathToElement, (BaseResourceReferenceDt) theNextExt.getValue()));
				}
			}
		});
		return retVal;
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

	@SuppressWarnings("unchecked")
	private <T> List<T> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, Object theCurrentObj, List<String> theSubList, Class<T> theWantedClass) {
		String name = theSubList.get(0);
				
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(name);
		List<? extends IBase> values = nextDef.getAccessor().getValues(theCurrentObj);
		List<T> retVal = new ArrayList<T>();

		if (theSubList.size() == 1) {
			if (nextDef instanceof RuntimeChildChoiceDefinition) {
				for (IBase next : values) {
					if (next != null) {
						if (name.endsWith("[x]")) {
							if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
								retVal.add((T) next);
							}
						} else {
							String childName = nextDef.getChildNameByDatatype(next.getClass());
							if (theSubList.get(0).equals(childName)) {
								if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
									retVal.add((T) next);
								}
							}
						}
					}
				}
			} else {
				for (IBase next : values) {
					if (next != null) {
						if (theWantedClass == null || theWantedClass.isAssignableFrom(next.getClass())) {
							retVal.add((T) next);
						}
					}
				}
			}
		} else {
			for (IBase nextElement : values) {
				BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
				List<T> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()), theWantedClass);
				retVal.addAll(foundValues);
			}
		}
		return retVal;
	}

	public List<Object> getValues(IBaseResource theResource, String thePath) {
		Class<Object> wantedClass = Object.class;

		return getValues(theResource, thePath, wantedClass);

	}

	public <T> List<T> getValues(IBaseResource theResource, String thePath, Class<T> theWantedClass) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);

		BaseRuntimeElementCompositeDefinition<?> currentDef = def;
		Object currentObj = theResource;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size());
		if (subList.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getValues(currentDef, currentObj, subList, theWantedClass);
	}

	private List<String> addNameToList(List<String> theCurrentList, BaseRuntimeChildDefinition theChildDefinition) {
		if (theChildDefinition == null)
			return null;
		if (theCurrentList == null || theCurrentList.isEmpty())
			return new ArrayList<String>(Arrays.asList(theChildDefinition.getElementName()));
		List<String> newList = new ArrayList<String>(theCurrentList);
		newList.add(theChildDefinition.getElementName());
		return newList;
	}

	private void visit(IdentityHashMap<Object, Object> theStack, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition,
			BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor theCallback) {
		List<String> pathToElement = addNameToList(thePathToElement, theChildDefinition);

		if (theStack.put(theElement, theElement) != null) {
			return;
		}
		
		theCallback.acceptElement(theElement, pathToElement, theChildDefinition, theDefinition);
		addUndeclaredExtensions(theElement, theDefinition, theChildDefinition, theCallback);

		BaseRuntimeElementDefinition<?> def = theDefinition;
		if (def.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) {
			def = myContext.getElementDefinition(theElement.getClass());
		}
		
		switch (def.getChildType()) {
		case ID_DATATYPE:
		case PRIMITIVE_XHTML_HL7ORG:
		case PRIMITIVE_XHTML:
		case PRIMITIVE_DATATYPE:
			// These are primitive types
			break;
		case RESOURCE_REF:
			IBaseReference resRefDt = (IBaseReference) theElement;
			if (resRefDt.getReferenceElement().getValue() == null && resRefDt.getResource() != null) {
				IBaseResource theResource = resRefDt.getResource();
				if (theResource.getIdElement() == null || theResource.getIdElement().isEmpty() || theResource.getIdElement().isLocal()) {
					def = myContext.getResourceDefinition(theResource);
					visit(theStack, theResource, pathToElement, null, def, theCallback);
				}
			}
			break;
		case RESOURCE:
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) def;
			for (BaseRuntimeChildDefinition nextChild : childDef.getChildrenAndExtension()) {
				List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
				if (values != null) {
					for (IBase nextValue : values) {
						if (nextValue == null) {
							continue;
						}
						if (nextValue.isEmpty()) {
							continue;
						}
						BaseRuntimeElementDefinition<?> childElementDef;
						childElementDef = nextChild.getChildElementDefinitionByDatatype(nextValue.getClass());

						if (childElementDef == null) {
							childElementDef = myContext.getElementDefinition(nextValue.getClass());
						}

						if (nextChild instanceof RuntimeChildDirectResource) {
							// Don't descend into embedded resources
							theCallback.acceptElement(nextValue, null, nextChild, childElementDef);
						} else {
							visit(theStack, nextValue, pathToElement, nextChild, childElementDef, theCallback);
						}
					}
				}
			}
			break;
		}
		case CONTAINED_RESOURCES: {
			BaseContainedDt value = (BaseContainedDt) theElement;
			for (IResource next : value.getContainedResources()) {
				def = myContext.getResourceDefinition(next);
				visit(theStack, next, pathToElement, null, def, theCallback);
			}
			break;
		}
		case CONTAINED_RESOURCE_LIST:
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + def.getChildType());
		}
		}
		
		theStack.remove(theElement);
		
	}

	private void visit(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor2 theCallback, List<IBase> theContainingElementPath,
			List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
		if (theChildDefinition != null) {
			theChildDefinitionPath.add(theChildDefinition);
		}
		theContainingElementPath.add(theElement);
		theElementDefinitionPath.add(theDefinition);

		theCallback.acceptElement(theElement, Collections.unmodifiableList(theContainingElementPath), Collections.unmodifiableList(theChildDefinitionPath),
				Collections.unmodifiableList(theElementDefinitionPath));

		/*
		 * Visit undeclared extensions
		 */
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions containingElement = (ISupportsUndeclaredExtensions) theElement;
			for (ExtensionDt nextExt : containingElement.getUndeclaredExtensions()) {
				theContainingElementPath.add(nextExt);
				theCallback.acceptUndeclaredExtension(nextExt, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
				theContainingElementPath.remove(theContainingElementPath.size() - 1);
			}
		}

		/*
		 * Now visit the children of the given element
		 */
		switch (theDefinition.getChildType()) {
		case ID_DATATYPE:
		case PRIMITIVE_XHTML_HL7ORG:
		case PRIMITIVE_XHTML:
		case PRIMITIVE_DATATYPE:
			// These are primitive types, so we don't need to visit their children
			break;
		case RESOURCE_REF:
			IBaseReference resRefDt = (IBaseReference) theElement;
			if (resRefDt.getReferenceElement().getValue() == null && resRefDt.getResource() != null) {
				IBaseResource theResource = resRefDt.getResource();
				if (theResource.getIdElement() == null || theResource.getIdElement().isEmpty() || theResource.getIdElement().isLocal()) {
					BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
					visit(theResource, null, def, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
				}
			}
			break;
		case RESOURCE:
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) theDefinition;
			for (BaseRuntimeChildDefinition nextChild : childDef.getChildrenAndExtension()) {
				List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
				if (values != null) {
					for (IBase nextValue : values) {
						if (nextValue == null) {
							continue;
						}
						if (nextValue.isEmpty()) {
							continue;
						}
						BaseRuntimeElementDefinition<?> childElementDef;
						childElementDef = nextChild.getChildElementDefinitionByDatatype(nextValue.getClass());

						if (childElementDef == null) {
							StringBuilder b = new StringBuilder();
							b.append("Found value of type[");
							b.append(nextValue.getClass().getSimpleName());
							b.append("] which is not valid for field[");
							b.append(nextChild.getElementName());
							b.append("] in ");
							b.append(childDef.getName());
							b.append(" - Valid types: ");
							for (Iterator<String> iter = new TreeSet<String>(nextChild.getValidChildNames()).iterator(); iter.hasNext();) {
								BaseRuntimeElementDefinition<?> childByName = nextChild.getChildByName(iter.next());
								b.append(childByName.getImplementingClass().getSimpleName());
								if (iter.hasNext()) {
									b.append(", ");
								}
							}
							throw new DataFormatException(b.toString());
						}

						if (nextChild instanceof RuntimeChildDirectResource) {
							// Don't descend into embedded resources
							theContainingElementPath.add(nextValue);
							theChildDefinitionPath.add(nextChild);
							theElementDefinitionPath.add(myContext.getElementDefinition(nextValue.getClass()));
							theCallback.acceptElement(nextValue, Collections.unmodifiableList(theContainingElementPath), Collections.unmodifiableList(theChildDefinitionPath),
									Collections.unmodifiableList(theElementDefinitionPath));
							theChildDefinitionPath.remove(theChildDefinitionPath.size() - 1);
							theContainingElementPath.remove(theContainingElementPath.size() - 1);
							theElementDefinitionPath.remove(theElementDefinitionPath.size() - 1);
						} else {
							visit(nextValue, nextChild, childElementDef, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
						}
					}
				}
			}
			break;
		}
		case CONTAINED_RESOURCES: {
			BaseContainedDt value = (BaseContainedDt) theElement;
			for (IResource next : value.getContainedResources()) {
				BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(next);
				visit(next, null, def, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
			}
			break;
		}
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + theDefinition.getChildType());
		}
		case CONTAINED_RESOURCE_LIST: {
			if (theElement != null) {
				BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theElement.getClass());
				visit(theElement, null, def, theCallback, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath);
			}
			break;
		}
		}

		if (theChildDefinition != null) {
			theChildDefinitionPath.remove(theChildDefinitionPath.size() - 1);
		}
		theContainingElementPath.remove(theContainingElementPath.size() - 1);
		theElementDefinitionPath.remove(theElementDefinitionPath.size() - 1);
	}

	/**
	 * Visit all elements in a given resource
	 * 
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 * 
	 * @param theResource
	 *           The resource to visit
	 * @param theVisitor
	 *           The visitor
	 */
	public void visit(IBaseResource theResource, IModelVisitor theVisitor) {
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(new IdentityHashMap<Object, Object>(), theResource, null, null, def, theVisitor);
	}

	/**
	 * Visit all elements in a given resource
	 * 
	 * THIS ALTERNATE METHOD IS STILL EXPERIMENTAL
	 * 
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 * 
	 * @param theResource
	 *           The resource to visit
	 * @param theVisitor
	 *           The visitor
	 */
	void visit(IBaseResource theResource, IModelVisitor2 theVisitor) {
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(theResource, null, def, theVisitor, new ArrayList<IBase>(), new ArrayList<BaseRuntimeChildDefinition>(), new ArrayList<BaseRuntimeElementDefinition<?>>());
	}

	public Object getSingleValueOrNull(IBase theTarget, String thePath) {
		Class<Object> wantedType = Object.class;

		return getSingleValueOrNull(theTarget, thePath, wantedType);
	}

	public <T> T getSingleValueOrNull(IBase theTarget, String thePath, Class<T> theWantedType) {
		Validate.notNull(theTarget, "theTarget must not be null");
		Validate.notBlank(thePath, "thePath must not be empty");

		BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(theTarget.getClass());
		if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
			throw new IllegalArgumentException("Target is not a composite type: " + theTarget.getClass().getName());
		}

		BaseRuntimeElementCompositeDefinition<?> currentDef = (BaseRuntimeElementCompositeDefinition<?>) def;
		Object currentObj = theTarget;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<T> retVal = getValues(currentDef, currentObj, parts, theWantedType);
		if (retVal.isEmpty()) {
			return null;
		} else {
			return retVal.get(0);
		}
	}

}
