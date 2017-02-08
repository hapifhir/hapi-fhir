package ca.uhn.fhir.util;

import static org.apache.commons.lang3.StringUtils.defaultString;

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
import java.util.*;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
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

	private List<String> addNameToList(List<String> theCurrentList, BaseRuntimeChildDefinition theChildDefinition) {
		if (theChildDefinition == null)
			return null;
		if (theCurrentList == null || theCurrentList.isEmpty())
			return new ArrayList<String>(Arrays.asList(theChildDefinition.getElementName()));
		List<String> newList = new ArrayList<String>(theCurrentList);
		newList.add(theChildDefinition.getElementName());
		return newList;
	}
	

	/**
	 * Clones all values from a source object into the equivalent fields in a target object
	 * @param theSource The source object (must not be null)
	 * @param theTarget The target object to copy values into (must not be null)
	 * @param theIgnoreMissingFields The ignore fields in the target which do not exist (if false, an exception will be thrown if the target is unable to accept a value from the source)
	 */
	public void cloneInto(IBase theSource, IBase theTarget, boolean theIgnoreMissingFields) {
		Validate.notNull(theSource, "theSource must not be null");
		Validate.notNull(theTarget, "theTarget must not be null");
		
		if (theSource instanceof IPrimitiveType<?>) {
			if (theTarget instanceof IPrimitiveType<?>) {
				((IPrimitiveType<?>)theTarget).setValueAsString(((IPrimitiveType<?>)theSource).getValueAsString());
				return;
			}
			if (theIgnoreMissingFields) {
				return;
			}
			throw new DataFormatException("Can not copy value from primitive of type " + theSource.getClass().getName() + " into type " + theTarget.getClass().getName());
		}
		
		BaseRuntimeElementCompositeDefinition<?> sourceDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theSource.getClass()); 
		BaseRuntimeElementCompositeDefinition<?> targetDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(theTarget.getClass());
		
		List<BaseRuntimeChildDefinition> children = sourceDef.getChildren();
		if (sourceDef instanceof RuntimeExtensionDtDefinition) {
			children = ((RuntimeExtensionDtDefinition)sourceDef).getChildrenIncludingUrl();
		}
		
		for (BaseRuntimeChildDefinition nextChild : children) {
			for (IBase nextValue : nextChild.getAccessor().getValues(theSource)) {
				String elementName = nextChild.getChildNameByDatatype(nextValue.getClass());
				BaseRuntimeChildDefinition targetChild = targetDef.getChildByName(elementName);
				if (targetChild == null) {
					if (theIgnoreMissingFields) {
						continue;
					}
					throw new DataFormatException("Type " + theTarget.getClass().getName() + " does not have a child with name " + elementName);
				}
				
				BaseRuntimeElementDefinition<?> childDef = targetChild.getChildByName(elementName);
				IBase target = childDef.newInstance();
				targetChild.getMutator().addValue(theTarget, target);
				cloneInto(nextValue, target, theIgnoreMissingFields);
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
		visit(new IdentityHashMap<Object, Object>(), theResource, theResource, null, null, def, new IModelVisitor() {
			@SuppressWarnings("unchecked")
			@Override
			public void acceptElement(IBaseResource theOuterResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}

				if (theType.isAssignableFrom(theElement.getClass())) {
					retVal.add((T) theElement);
				}
			}
		});
		return retVal;
	}

	public List<ResourceReferenceInfo> getAllResourceReferences(final IBaseResource theResource) {
		final ArrayList<ResourceReferenceInfo> retVal = new ArrayList<ResourceReferenceInfo>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(new IdentityHashMap<Object, Object>(), theResource, theResource, null, null, def, new IModelVisitor() {
			@Override
			public void acceptElement(IBaseResource theOuterResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}
				if (IBaseReference.class.isAssignableFrom(theElement.getClass())) {
					retVal.add(new ResourceReferenceInfo(myContext, theOuterResource, thePathToElement, (IBaseReference) theElement));
				}
			}
		});
		return retVal;
	}

	private BaseRuntimeChildDefinition getDefinition(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, List<String> theSubList) {
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(theSubList.get(0));

		if (theSubList.size() == 1) {
			return nextDef;
		}
		BaseRuntimeElementCompositeDefinition<?> cmp = (BaseRuntimeElementCompositeDefinition<?>) nextDef.getChildByName(theSubList.get(0));
		return getDefinition(cmp, theSubList.subList(1, theSubList.size()));
	}

	public BaseRuntimeChildDefinition getDefinition(Class<? extends IBaseResource> theResourceType, String thePath) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResourceType);

		BaseRuntimeElementCompositeDefinition<?> currentDef = def;

		List<String> parts = Arrays.asList(thePath.split("\\."));
		List<String> subList = parts.subList(1, parts.size());
		if (subList.size() < 1) {
			throw new ConfigurationException("Invalid path: " + thePath);
		}
		return getDefinition(currentDef, subList);

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
		}
		return retVal.get(0);
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

	/**
	 * Returns <code>true</code> if <code>theSource</code> is in the compartment named <code>theCompartmentName</code>
	 * belonging to resource <code>theTarget</code>
	 * 
	 * @param theCompartmentName The name of the compartment
	 * @param theSource The potential member of the compartment
	 * @param theTarget The owner of the compartment. Note that both the resource type and ID must be filled in on this IIdType or the method will throw an {@link IllegalArgumentException}
	 * @return <code>true</code> if <code>theSource</code> is in the compartment
	 * @throws IllegalArgumentException If theTarget does not contain both a resource type and ID
	 */
	public boolean isSourceInCompartmentForTarget(String theCompartmentName, IBaseResource theSource, IIdType theTarget) {
		Validate.notBlank(theCompartmentName, "theCompartmentName must not be null or blank");
		Validate.notNull(theSource, "theSource must not be null");
		Validate.notNull(theTarget, "theTarget must not be null");
		Validate.notBlank(defaultString(theTarget.getResourceType()), "theTarget must have a populated resource type (theTarget.getResourceType() does not return a value)");
		Validate.notBlank(defaultString(theTarget.getIdPart()), "theTarget must have a populated ID (theTarget.getIdPart() does not return a value)");
		
		String wantRef = theTarget.toUnqualifiedVersionless().getValue();
		
		RuntimeResourceDefinition sourceDef = myContext.getResourceDefinition(theSource);
		if (theSource.getIdElement().hasIdPart()) {
			if (wantRef.equals(sourceDef.getName() + '/' + theSource.getIdElement().getIdPart())) {
				return true;
			}
		}
		
		List<RuntimeSearchParam> params = sourceDef.getSearchParamsForCompartmentName(theCompartmentName);
		for (RuntimeSearchParam nextParam : params) {
			for (String nextPath : nextParam.getPathsSplit()) {
				for (IBaseReference nextValue : getValues(theSource, nextPath, IBaseReference.class)) {
					String nextRef = nextValue.getReferenceElement().toUnqualifiedVersionless().getValue();
					if (wantRef.equals(nextRef)) {
						return true;
					}
				}
			}
		}
		
		return false;
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
		visit(new IdentityHashMap<Object, Object>(), theResource, theResource, null, null, def, theVisitor);
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

	private void visit(IdentityHashMap<Object, Object> theStack, IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition,
			BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor theCallback) {
		List<String> pathToElement = addNameToList(thePathToElement, theChildDefinition);

		if (theStack.put(theElement, theElement) != null) {
			return;
		}
		
		theCallback.acceptElement(theResource, theElement, pathToElement, theChildDefinition, theDefinition);

		BaseRuntimeElementDefinition<?> def = theDefinition;
		if (def.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) {
			def = myContext.getElementDefinition(theElement.getClass());
		}
		
		if (theElement instanceof IBaseReference) {
			IBaseResource target = ((IBaseReference)theElement).getResource();
			if (target != null) {
				if (target.getIdElement().hasIdPart() == false || target.getIdElement().isLocal()) {
					RuntimeResourceDefinition targetDef = myContext.getResourceDefinition(target);
					visit(theStack, target, target, pathToElement, null, targetDef, theCallback);
				}
			}
		}
		
		switch (def.getChildType()) {
		case ID_DATATYPE:
		case PRIMITIVE_XHTML_HL7ORG:
		case PRIMITIVE_XHTML:
		case PRIMITIVE_DATATYPE:
			// These are primitive types
			break;
		case RESOURCE:
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			BaseRuntimeElementCompositeDefinition<?> childDef = (BaseRuntimeElementCompositeDefinition<?>) def;
			for (BaseRuntimeChildDefinition nextChild : childDef.getChildrenAndExtension()) {
				
				List<?> values = nextChild.getAccessor().getValues(theElement);
				if (values != null) {
					for (Object nextValueObject : values) {
						IBase nextValue;
						try {
							nextValue = (IBase) nextValueObject;
						} catch (ClassCastException e) {
							String s = "Found instance of " + nextValueObject.getClass() + " - Did you set a field value to the incorrect type? Expected " + IBase.class.getName();
							throw new ClassCastException(s);
						}
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
							theCallback.acceptElement(theResource, nextValue, null, nextChild, childElementDef);
						} else {
							visit(theStack, theResource, nextValue, pathToElement, nextChild, childElementDef, theCallback);
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
				visit(theStack, next, next, pathToElement, null, def, theCallback);
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

}
