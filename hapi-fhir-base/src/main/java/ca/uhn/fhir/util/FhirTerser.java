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
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.hl7.fhir.instance.model.IBase;
import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildDirectResource;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
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

	private <T extends IBase> void addUndeclaredExtensions(IBase theElement, BaseRuntimeElementDefinition<?> theDefinition, BaseRuntimeChildDefinition theChildDefinition,
			IModelVisitor theCallback) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions containingElement = (ISupportsUndeclaredExtensions) theElement;
			for (ExtensionDt nextExt : containingElement.getUndeclaredExtensions()) {
				theCallback.acceptUndeclaredExtension(containingElement, theChildDefinition, theDefinition, nextExt);
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
	 *            The resource instance to search. Must not be null.
	 * @param theType
	 *            The type to search for. Must not be null.
	 * @return Returns a list of all matching elements
	 */
	public <T extends IBase> List<T> getAllPopulatedChildElementsOfType(IBaseResource theResource, final Class<T> theType) {
		final ArrayList<T> retVal = new ArrayList<T>();
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(theResource, null, def, new IModelVisitor() {
			@SuppressWarnings("unchecked")
			@Override
			public void acceptElement(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
				if (theElement == null || theElement.isEmpty()) {
					return;
				}

				if (theType.isAssignableFrom(theElement.getClass())) {
					retVal.add((T) theElement);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition,
					ExtensionDt theNextExt) {
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

    public <T extends IBase> List<ResourceReferenceInfo> getAllResourceReferences(final IBaseResource theResource) {
        final ArrayList<ResourceReferenceInfo> retVal = new ArrayList<ResourceReferenceInfo>();
        BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
        visit(theResource, null, def, new IModelVisitor() {
            @SuppressWarnings("unchecked")
            @Override
            public void acceptElement(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
                if (theElement == null || theElement.isEmpty()) {
                    return;
                }
                String name = null;
                if (theChildDefinition != null) {
                    name = theChildDefinition.getElementName();
                }
                if (BaseResourceReferenceDt.class.isAssignableFrom(theElement.getClass())) {
                    retVal.add(new ResourceReferenceInfo(theResource, name, (BaseResourceReferenceDt)theElement));
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition,
                                                  ExtensionDt theNextExt) {
                String name = null;
                if (theChildDefinition != null) {
                    name = theChildDefinition.getElementName();
                }
                if (theNextExt.getValue() != null && BaseResourceReferenceDt.class.isAssignableFrom(theNextExt.getValue().getClass())) {
                    retVal.add(new ResourceReferenceInfo(theResource, name, (BaseResourceReferenceDt)theNextExt.getValue()));
                }
            }
        });
        return retVal;    }	private BaseRuntimeChildDefinition getDefinition(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, List<String> theSubList) {
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

	private List<Object> getValues(BaseRuntimeElementCompositeDefinition<?> theCurrentDef, Object theCurrentObj, List<String> theSubList) {
		String name = theSubList.get(0);
		BaseRuntimeChildDefinition nextDef = theCurrentDef.getChildByNameOrThrowDataFormatException(name);
		List<? extends IBase> values = nextDef.getAccessor().getValues(theCurrentObj);
		List<Object> retVal = new ArrayList<Object>();

		if (theSubList.size() == 1) {
			if (nextDef instanceof RuntimeChildChoiceDefinition) {
				for (IBase next : values) {
					if (next != null) {
						String childName = nextDef.getChildNameByDatatype(next.getClass());
						if (theSubList.get(0).equals(childName)) {
							retVal.add(next);
						}
					}
				}
			} else {
				retVal.addAll(values);
			}
		} else {
			for (IBase nextElement : values) {
				BaseRuntimeElementCompositeDefinition<?> nextChildDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(nextElement.getClass());
				List<?> foundValues = getValues(nextChildDef, nextElement, theSubList.subList(1, theSubList.size()));
				retVal.addAll(foundValues);
			}
		}
		return retVal;
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

	private <T extends IElement> void visit(IBase theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition, IModelVisitor theCallback) {
		theCallback.acceptElement(theElement, theChildDefinition, theDefinition);
		addUndeclaredExtensions(theElement, theDefinition, theChildDefinition, theCallback);

		// if (theElement.isEmpty()) {
		// return;
		// }

		switch (theDefinition.getChildType()) {
//		case RESOURCE:
//			// Don't descend into embedded resources
//			break;
		case PRIMITIVE_XHTML:
		case PRIMITIVE_DATATYPE:
			// These are primitive types
			break;
		case RESOURCE_REF:
			BaseResourceReferenceDt resRefDt = (BaseResourceReferenceDt) theElement;
			if (resRefDt.getReference().getValue() == null && resRefDt.getResource() != null) {
				IResource theResource = resRefDt.getResource();
				if (theResource.getId() == null || theResource.getId().isEmpty() || theResource.getId().isLocal()) {
					BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
					visit(theResource, null, def, theCallback);
				}
			}
			break;
		case RESOURCE:
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			if (theChildDefinition instanceof RuntimeChildDirectResource) {
				// Don't descend into embedded resources
				return;
			}
			
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
						visit(nextValue, nextChild, childElementDef, theCallback);
					}
				}
			}
			break;
		}
		case CONTAINED_RESOURCES: {
			BaseContainedDt value = (BaseContainedDt) theElement;
			for (IResource next : value.getContainedResources()) {
				BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(next);
				visit(next, null, def, theCallback);
			}
			break;
		}
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + theDefinition.getChildType());
		}
		}
	}

	/**
	 * Visit all elements in a given resource
	 * 
	 * <p>
	 * Note on scope: This method will descend into any contained resources ({@link IResource#getContained()}) as well, but will not descend into linked resources (e.g.
	 * {@link BaseResourceReferenceDt#getResource()}) or embedded resources (e.g. Bundle.entry.resource)
	 * </p>
	 * @param theResource
	 *            The resource to visit
	 * @param theVisitor
	 *            The visitor
	 */
	public void visit(IBaseResource theResource, IModelVisitor theVisitor) {
		BaseRuntimeElementCompositeDefinition<?> def = myContext.getResourceDefinition(theResource);
		visit(theResource, null, def, theVisitor);
	}

}
