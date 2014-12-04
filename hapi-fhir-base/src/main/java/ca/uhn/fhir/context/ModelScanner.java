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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IBoundCodeableConcept;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.ICodedDatatype;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.util.ReflectionUtil;

class ModelScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelScanner.class);

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinitions = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
	private Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = new HashMap<String, RuntimeResourceDefinition>();
	private Map<String, RuntimeResourceDefinition> myNameToResourceDefinitions = new HashMap<String, RuntimeResourceDefinition>();

	// private Map<String, RuntimeResourceDefinition>
	// myNameToDatatypeDefinitions = new HashMap<String,
	// RuntimeDatatypeDefinition>();

	private Map<String, String> myNameToResourceType = new HashMap<String, String>();

	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	private Set<Class<? extends IElement>> myScanAlso = new HashSet<Class<? extends IElement>>();

	private Set<Class<? extends ICodeEnum>> myScanAlsoCodeTable = new HashSet<Class<? extends ICodeEnum>>();

	private FhirContext myContext;

	ModelScanner(FhirContext theContext, Class<? extends IResource> theResourceTypes) throws ConfigurationException {
		myContext=theContext;
		Set<Class<? extends IElement>> singleton = new HashSet<Class<? extends IElement>>();
		singleton.add(theResourceTypes);
		init(null, singleton);
	}

	ModelScanner(FhirContext theContext, Collection<Class<? extends IResource>> theResourceTypes) throws ConfigurationException {
		myContext=theContext;
		init(null, new HashSet<Class<? extends IElement>>(theResourceTypes));
	}

	ModelScanner(FhirContext theContext, Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theExistingDefinitions, Collection<Class<? extends IResource>> theResourceTypes) throws ConfigurationException {
		myContext=theContext;
		init(theExistingDefinitions, new HashSet<Class<? extends IElement>>(theResourceTypes));
	}

	public Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> getClassToElementDefinitions() {
		return myClassToElementDefinitions;
	}

	public Map<String, RuntimeResourceDefinition> getIdToResourceDefinition() {
		return myIdToResourceDefinition;
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinitions() {
		return (myNameToResourceDefinitions);
	}

	public Map<String, String> getNameToResourceType() {
		return myNameToResourceType;
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	private void addScanAlso(Class<? extends IElement> theType) {
		if (theType.isInterface()) {
			return;
		}
		myScanAlso.add(theType);
	}

	private Class<?> determineElementType(Field next) {
		Class<?> nextElementType = next.getType();
		if (List.class.equals(nextElementType)) {
			nextElementType = ReflectionUtil.getGenericCollectionTypeOfField(next);
		} else if (Collection.class.isAssignableFrom(nextElementType)) {
			throw new ConfigurationException("Field '" + next.getName() + "' in type '" + next.getClass().getCanonicalName() + "' is a Collection - Only java.util.List curently supported");
		}
		return nextElementType;
	}

	@SuppressWarnings("unchecked")
	private IValueSetEnumBinder<Enum<?>> getBoundCodeBinder(Field theNext) {
		Class<?> bound = getGenericCollectionTypeOfCodedField(theNext);
		if (bound == null) {
			throw new ConfigurationException("Field '" + theNext + "' has no parameter for " + BoundCodeDt.class.getSimpleName() + " to determine enum type");
		}

		try {
			Field bindingField = bound.getField("VALUESET_BINDER");
			return (IValueSetEnumBinder<Enum<?>>) bindingField.get(null);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (NoSuchFieldException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Field '" + theNext + "' has type parameter " + bound.getCanonicalName() + " but this class has no valueset binding field", e);
		}
	}

	private void init(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theExistingDefinitions, Set<Class<? extends IElement>> toScan) {
		if (theExistingDefinitions != null) {
			myClassToElementDefinitions.putAll(theExistingDefinitions);
		}

		int startSize = myClassToElementDefinitions.size();
		long start = System.currentTimeMillis();

		InputStream str = ModelScanner.class.getResourceAsStream("/ca/uhn/fhir/model/dstu/fhirversion.properties");
		if (str == null) {
			str = ModelScanner.class.getResourceAsStream("ca/uhn/fhir/model/dstu/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}
		Properties prop = new Properties();
		try {
			prop.load(str);
			for (Entry<Object, Object> nextEntry : prop.entrySet()) {
				String nextKey = nextEntry.getKey().toString();
				String nextValue = nextEntry.getValue().toString();

				if (!nextKey.startsWith("datatype.")) {
					if (nextKey.startsWith("resource.")) {
						String resName = nextKey.substring("resource.".length()).toLowerCase();
						myNameToResourceType.put(resName, nextValue);
					}
					continue;
				}

				try {
					@SuppressWarnings("unchecked")
					Class<? extends IElement> nextClass = (Class<? extends IElement>) Class.forName((String) nextValue);
					if (!IElement.class.isAssignableFrom(nextClass)) {
						ourLog.warn("Class is not assignable from " + IElement.class.getSimpleName() + ": " + nextValue);
						continue;
					}

					toScan.add(nextClass);
				} catch (ClassNotFoundException e) {
					ourLog.warn("Unknown class exception: " + nextValue, e);
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Failed to load model property file from classpath: " + "/ca/uhn/fhir/model/dstu/model.properties");
		}

		// toScan.add(DateDt.class);
		// toScan.add(CodeDt.class);
		// toScan.add(DecimalDt.class);
		// toScan.add(AttachmentDt.class);
		// toScan.add(ResourceReferenceDt.class);
		// toScan.add(QuantityDt.class);

		do {
			for (Class<? extends IElement> nextClass : toScan) {
				scan(nextClass);
			}
			for (Iterator<Class<? extends IElement>> iter = myScanAlso.iterator(); iter.hasNext();) {
				if (myClassToElementDefinitions.containsKey(iter.next())) {
					iter.remove();
				}
			}
			toScan.clear();
			toScan.addAll(myScanAlso);
			myScanAlso.clear();
		} while (!toScan.isEmpty());

		for (Entry<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> nextEntry : myClassToElementDefinitions.entrySet()) {
			if (theExistingDefinitions != null && theExistingDefinitions.containsKey(nextEntry.getKey())) {
				continue;
			}
			BaseRuntimeElementDefinition<?> next = nextEntry.getValue();
			next.sealAndInitialize(myClassToElementDefinitions);
		}

		myRuntimeChildUndeclaredExtensionDefinition = new RuntimeChildUndeclaredExtensionDefinition();
		myRuntimeChildUndeclaredExtensionDefinition.sealAndInitialize(myClassToElementDefinitions);

		long time = System.currentTimeMillis() - start;
		int size = myClassToElementDefinitions.size() - startSize;
		ourLog.info("Done scanning FHIR library, found {} model entries in {}ms", size, time);
	}

	private void scan(Class<? extends IElement> theClass) throws ConfigurationException {
		BaseRuntimeElementDefinition<?> existingDef = myClassToElementDefinitions.get(theClass);
		if (existingDef != null) {
			return;
		}

		ResourceDef resourceDefinition = theClass.getAnnotation(ResourceDef.class);
		if (resourceDefinition != null) {
			if (!IResource.class.isAssignableFrom(theClass)) {
				throw new ConfigurationException("Resource type contains a @" + ResourceDef.class.getSimpleName() + " annotation but does not implement " + IResource.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
			@SuppressWarnings("unchecked")
			Class<? extends IResource> resClass = (Class<? extends IResource>) theClass;
			scanResource(resClass, resourceDefinition);
		}

		DatatypeDef datatypeDefinition = theClass.getAnnotation(DatatypeDef.class);
		if (datatypeDefinition != null) {
			if (ICompositeDatatype.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends ICompositeDatatype> resClass = (Class<? extends ICompositeDatatype>) theClass;
				scanCompositeDatatype(resClass, datatypeDefinition);
			} else if (IPrimitiveDatatype.class.isAssignableFrom(theClass)) {
				@SuppressWarnings({ "unchecked" })
				Class<? extends IPrimitiveDatatype<?>> resClass = (Class<? extends IPrimitiveDatatype<?>>) theClass;
				scanPrimitiveDatatype(resClass, datatypeDefinition);
			} else {
				throw new ConfigurationException("Resource type contains a @" + DatatypeDef.class.getSimpleName() + " annotation but does not implement " + IDatatype.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		Block blockDefinition = theClass.getAnnotation(Block.class);
		if (blockDefinition != null) {
			if (IResourceBlock.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends IResourceBlock> blockClass = (Class<? extends IResourceBlock>) theClass;
				scanBlock(blockClass, blockDefinition);
			} else {
				throw new ConfigurationException("Type contains a @" + Block.class.getSimpleName() + " annotation but does not implement " + IResourceBlock.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		if (blockDefinition == null && datatypeDefinition == null && resourceDefinition == null) {
			throw new ConfigurationException("Resource class[" + theClass.getName() + "] does not contain any valid HAPI-FHIR annotations");
		}
	}

	private void scanBlock(Class<? extends IResourceBlock> theClass, Block theBlockDefinition) {
		ourLog.debug("Scanning resource block class: {}", theClass.getName());

		String resourceName = theClass.getCanonicalName();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Block type @" + Block.class.getSimpleName() + " annotation contains no name: " + theClass.getCanonicalName());
		}

		RuntimeResourceBlockDefinition resourceDef = new RuntimeResourceBlockDefinition(resourceName, theClass);
		myClassToElementDefinitions.put(theClass, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);
	}

	private void scanCompositeDatatype(Class<? extends ICompositeDatatype> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		RuntimeCompositeDatatypeDefinition resourceDef;
		if (theClass.equals(ExtensionDt.class)) {
			resourceDef = new RuntimeExtensionDtDefinition(theDatatypeDefinition, theClass);
		} else {
			resourceDef = new RuntimeCompositeDatatypeDefinition(theDatatypeDefinition, theClass);
		}
		myClassToElementDefinitions.put(theClass, resourceDef);
		scanCompositeElementForChildren(theClass, resourceDef);
	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, BaseRuntimeElementCompositeDefinition<?> theDefinition) {
		Set<String> elementNames = new HashSet<String>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToElementDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToExtensionDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();

		LinkedList<Class<? extends ICompositeElement>> classes = new LinkedList<Class<? extends ICompositeElement>>();

		/*
		 * We scan classes for annotated fields in the class but also all of its superclasses
		 */
		Class<? extends ICompositeElement> current = theClass;
		do {
			classes.push(current);
			if (ICompositeElement.class.isAssignableFrom(current.getSuperclass())) {
				current = (Class<? extends ICompositeElement>) current.getSuperclass();
			} else {
				current = null;
			}
		} while (current != null);

		for (Class<? extends ICompositeElement> next : classes) {
			scanCompositeElementForChildren(next, elementNames, orderToElementDef, orderToExtensionDef);
		}

		// while (orderToElementDef.size() > 0 && orderToElementDef.firstKey() <
		// 0) {
		// BaseRuntimeDeclaredChildDefinition elementDef =
		// orderToElementDef.remove(orderToElementDef.firstKey());
		// if (elementDef.getElementName().equals("identifier")) {
		// orderToElementDef.put(theIdentifierOrder, elementDef);
		// } else {
		// throw new ConfigurationException("Don't know how to handle element: "
		// + elementDef.getElementName());
		// }
		// }

		TreeSet<Integer> orders = new TreeSet<Integer>();
		orders.addAll(orderToElementDef.keySet());
		orders.addAll(orderToExtensionDef.keySet());

		for (Integer i : orders) {
			BaseRuntimeChildDefinition nextChild = orderToElementDef.get(i);
			if (nextChild != null) {
				theDefinition.addChild(nextChild);
			}
			BaseRuntimeDeclaredChildDefinition nextExt = orderToExtensionDef.get(i);
			if (nextExt != null) {
				theDefinition.addExtension((RuntimeChildDeclaredExtensionDefinition) nextExt);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, Set<String> elementNames, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToElementDef, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToExtensionDef) {
		int baseElementOrder = theOrderToElementDef.isEmpty() ? 0 : theOrderToElementDef.lastEntry().getKey() + 1;

		for (Field next : theClass.getDeclaredFields()) {

			if (Modifier.isFinal(next.getModifiers())) {
				ourLog.trace("Ignoring constant {} on target type {}",  next.getName(), theClass);
				continue;
			}
			
			Child childAnnotation = next.getAnnotation(Child.class);
			if (childAnnotation == null) {
				ourLog.trace("Ignoring non @Child field {} on target type {}",next.getName() , theClass);
				continue;
			}

			Description descriptionAnnotation = next.getAnnotation(Description.class);

			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderMap = theOrderToElementDef;
			Extension extensionAttr = next.getAnnotation(Extension.class);
			if (extensionAttr != null) {
				orderMap = theOrderToExtensionDef;
			}

			String elementName = childAnnotation.name();
			int order = childAnnotation.order();
			if (order == Child.REPLACE_PARENT) {

				if (extensionAttr != null) {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (nextDef instanceof RuntimeChildDeclaredExtensionDefinition) {
							if (nextDef.getExtensionUrl().equals(extensionAttr.url())) {
								order = nextEntry.getKey();
								orderMap.remove(nextEntry.getKey());
								elementNames.remove(elementName);
								break;
							}
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException("Field " + next.getName() + "' on target type " + theClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT + ") but no parent element with extension URL " + extensionAttr.url()
								+ " could be found on type " + next.getDeclaringClass().getSimpleName());
					}

				} else {

					for (Entry<Integer, BaseRuntimeDeclaredChildDefinition> nextEntry : orderMap.entrySet()) {
						BaseRuntimeDeclaredChildDefinition nextDef = nextEntry.getValue();
						if (elementName.equals(nextDef.getElementName())) {
							order = nextEntry.getKey();
							orderMap.remove(nextEntry.getKey());
							elementNames.remove(elementName);
							break;
						}
					}
					if (order == Child.REPLACE_PARENT) {
						throw new ConfigurationException("Field " + next.getName() + "' on target type " + theClass.getSimpleName() + " has order() of REPLACE_PARENT (" + Child.REPLACE_PARENT + ") but no parent element with name " + elementName + " could be found on type "
								+ next.getDeclaringClass().getSimpleName());
					}

				}

			}
			if (order < 0 && order != Child.ORDER_UNKNOWN) {
				throw new ConfigurationException("Invalid order '" + order + "' on @Child for field '" + next.getName() + "' on target type: " + theClass);
			}
			if (order != Child.ORDER_UNKNOWN) {
				order = order + baseElementOrder;
			}
			int min = childAnnotation.min();
			int max = childAnnotation.max();
			/*
			 * Anything that's marked as unknown is given a new ID that is <0 so that it doesn't conflict wityh any
			 * given IDs and can be figured out later
			 */
			while (order == Child.ORDER_UNKNOWN && orderMap.containsKey(order)) {
				order--;
			}

			List<Class<? extends IElement>> choiceTypes = new ArrayList<Class<? extends IElement>>();
			for (Class<? extends IElement> nextChoiceType : childAnnotation.type()) {
				choiceTypes.add(nextChoiceType);
			}

			if (orderMap.containsKey(order)) {
				throw new ConfigurationException("Detected duplicate field order '" + childAnnotation.order() + "' for element named '" + elementName + "' in type '" + theClass.getCanonicalName() + "'");
			}

			if (elementNames.contains(elementName)) {
				throw new ConfigurationException("Detected duplicate field name '" + elementName + "' in type '" + theClass.getCanonicalName() + "'");
			}

			Class<?> nextElementType = determineElementType(next);

			if (nextElementType.equals(ContainedDt.class)) {
				/*
				 * Child is contained resources
				 */
				RuntimeChildContainedResources def = new RuntimeChildContainedResources(next, childAnnotation, descriptionAnnotation, elementName);
				orderMap.put(order, def);

			} else if (choiceTypes.size() > 1 && !BaseResourceReferenceDt.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a choice element
				 */
				for (Class<? extends IElement> nextType : choiceTypes) {
					addScanAlso(nextType);
				}
				RuntimeChildChoiceDefinition def = new RuntimeChildChoiceDefinition(next, elementName, childAnnotation, descriptionAnnotation, choiceTypes);
				orderMap.put(order, def);

			} else if (next.getType().equals(ExtensionDt.class)) {

				RuntimeChildExtensionDt def = new RuntimeChildExtensionDt(next, elementName, childAnnotation, descriptionAnnotation);
				orderMap.put(order, def);
				if (IElement.class.isAssignableFrom(nextElementType)) {
					addScanAlso((Class<? extends IElement>) nextElementType);
				}

			} else if (extensionAttr != null) {
				/*
				 * Child is an extension
				 */
				Class<? extends IElement> et = (Class<? extends IElement>) nextElementType;
				RuntimeChildDeclaredExtensionDefinition def = new RuntimeChildDeclaredExtensionDefinition(next, childAnnotation, descriptionAnnotation, extensionAttr, elementName, extensionAttr.url(), et);
				orderMap.put(order, def);
				if (IElement.class.isAssignableFrom(nextElementType)) {
					addScanAlso((Class<? extends IElement>) nextElementType);
				}
			} else if (BaseResourceReferenceDt.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource reference
				 */
				List<Class<? extends IResource>> refTypesList = new ArrayList<Class<? extends IResource>>();
				for (Class<? extends IElement> nextType : childAnnotation.type()) {
					if (IResource.class.isAssignableFrom(nextType) == false) {
						throw new ConfigurationException("Field '" + next.getName() + "' in class '" + next.getDeclaringClass().getCanonicalName() + "' is of type " + BaseResourceReferenceDt.class + " but contains a non-resource type: " + nextType.getCanonicalName());
					}
					refTypesList.add((Class<? extends IResource>) nextType);
					addScanAlso(nextType);
				}
				RuntimeChildResourceDefinition def = new RuntimeChildResourceDefinition(next, elementName, childAnnotation, descriptionAnnotation, refTypesList);
				orderMap.put(order, def);

			} else if (IResourceBlock.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource block (i.e. a sub-tag within a resource) TODO: do these have a better name
				 * according to HL7?
				 */

				Class<? extends IResourceBlock> blockDef = (Class<? extends IResourceBlock>) nextElementType;
				addScanAlso(blockDef);
				RuntimeChildResourceBlockDefinition def = new RuntimeChildResourceBlockDefinition(next, childAnnotation, descriptionAnnotation, elementName, blockDef);
				orderMap.put(order, def);

			} else if (IDatatype.class.equals(nextElementType) || IElement.class.equals(nextElementType)) {

				RuntimeChildAny def = new RuntimeChildAny(next, elementName, childAnnotation, descriptionAnnotation);
				orderMap.put(order, def);

			} else if (IDatatype.class.isAssignableFrom(nextElementType)) {
				Class<? extends IDatatype> nextDatatype = (Class<? extends IDatatype>) nextElementType;

				addScanAlso(nextDatatype);
				BaseRuntimeChildDatatypeDefinition def;
				if (IPrimitiveDatatype.class.isAssignableFrom(nextElementType)) {
					if (nextElementType.equals(BoundCodeDt.class)) {
						IValueSetEnumBinder<Enum<?>> binder = getBoundCodeBinder(next);
						def = new RuntimeChildPrimitiveBoundCodeDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder);
					} else {
						def = new RuntimeChildPrimitiveDatatypeDefinition(next, elementName, descriptionAnnotation, childAnnotation, nextDatatype);
					}
				} else {
					if (IBoundCodeableConcept.class.isAssignableFrom(nextElementType)) {
						IValueSetEnumBinder<Enum<?>> binder = getBoundCodeBinder(next);
						def = new RuntimeChildCompositeBoundDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder);
					} else if (NarrativeDt.class.getSimpleName().equals(nextElementType.getSimpleName())) {
						def = new RuntimeChildNarrativeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
					} else {
						def = new RuntimeChildCompositeDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype);
					}
				}

				CodeableConceptElement concept = next.getAnnotation(CodeableConceptElement.class);
				if (concept != null) {
					if (!ICodedDatatype.class.isAssignableFrom(nextDatatype)) {
						throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is marked as @" + CodeableConceptElement.class.getCanonicalName() + " but type is not a subtype of " + ICodedDatatype.class.getName());
					} else {
						Class<? extends ICodeEnum> type = concept.type();
						myScanAlsoCodeTable.add(type);
						def.setCodeType(type);
					}
				}

				orderMap.put(order, def);

			} else {
				throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a valid child type: " + nextElementType);
			}

			elementNames.add(elementName);
		}
	}

	private String scanPrimitiveDatatype(Class<? extends IPrimitiveDatatype<?>> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		BaseRuntimeElementDefinition<?> resourceDef;
		if (theClass.equals(XhtmlDt.class)) {
			@SuppressWarnings("unchecked")
			Class<XhtmlDt> clazz = (Class<XhtmlDt>) theClass;
			resourceDef = new RuntimePrimitiveDatatypeNarrativeDefinition(resourceName, clazz);
		} else {
			resourceDef = new RuntimePrimitiveDatatypeDefinition(theDatatypeDefinition, theClass);
		}
		myClassToElementDefinitions.put(theClass, resourceDef);

		return resourceName;
	}

	private String scanResource(Class<? extends IResource> theClass, ResourceDef resourceDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		boolean primaryNameProvider = true;
		String resourceName = resourceDefinition.name();
		if (isBlank(resourceName)) {
			Class<?> parent = theClass.getSuperclass();
			primaryNameProvider = false;
			while (parent.equals(Object.class) == false && isBlank(resourceName)) {
				ResourceDef nextDef = parent.getAnnotation(ResourceDef.class);
				if (nextDef != null) {
					resourceName = nextDef.name();
				}
				parent = parent.getSuperclass();
			}
			if (isBlank(resourceName)) {
				throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name(): " + theClass.getCanonicalName() + " - This is only allowed for types that extend other resource types ");
			}
		}

		// if (myNameToResourceDefinitions.containsKey(resourceName)) {
		// if (!myNameToResourceDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
		// // throw new
		// // ConfigurationException("Detected duplicate element name '" +
		// // resourceName + "' in types '" + theClass.getCanonicalName() +
		// // "' and '"
		// // +
		// // myNameToResourceDefinitions.get(resourceName).getImplementingClass()
		// // + "'");
		// } else {
		// return resourceName;
		// }
		// }

		String resourceId = resourceDefinition.id();
		if (isBlank(resourceId)) {
			// throw new ConfigurationException("Resource type @" +
			// ResourceDef.class.getSimpleName() +
			// " annotation contains no resource ID: " +
			// theClass.getCanonicalName());
		} else {
			if (myIdToResourceDefinition.containsKey(resourceId)) {
				throw new ConfigurationException("The following resource types have the same ID of '" + resourceId + "' - " + theClass.getCanonicalName() + " and " + myIdToResourceDefinition.get(resourceId).getImplementingClass().getCanonicalName());
			}
		}

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(myContext, resourceName, theClass, resourceDefinition);
		myClassToElementDefinitions.put(theClass, resourceDef);
		if (primaryNameProvider) {
			myNameToResourceDefinitions.put(resourceName, resourceDef);
		}
		scanCompositeElementForChildren(theClass, resourceDef);

		myIdToResourceDefinition.put(resourceId, resourceDef);

		scanResourceForSearchParams(theClass, resourceDef);

		return resourceName;
	}

	private void scanResourceForSearchParams(Class<? extends IResource> theClass, RuntimeResourceDefinition theResourceDef) {

		Map<String, RuntimeSearchParam> nameToParam = new HashMap<String, RuntimeSearchParam>();
		Map<Field, SearchParamDefinition> compositeFields = new LinkedHashMap<Field, SearchParamDefinition>();

		for (Field nextField : theClass.getFields()) {
			SearchParamDefinition searchParam = nextField.getAnnotation(SearchParamDefinition.class);
			if (searchParam != null) {
				SearchParamTypeEnum paramType = SearchParamTypeEnum.valueOf(searchParam.type().toUpperCase());
				if (paramType == null) {
					throw new ConfigurationException("Searc param " + searchParam.name() + " has an invalid type: " + searchParam.type());
				}
				if (paramType == SearchParamTypeEnum.COMPOSITE) {
					compositeFields.put(nextField, searchParam);
					continue;
				}
				RuntimeSearchParam param = new RuntimeSearchParam(searchParam.name(), searchParam.description(), searchParam.path(), paramType);
				theResourceDef.addSearchParam(param);
				nameToParam.put(param.getName(), param);
			}
		}

		for (Entry<Field, SearchParamDefinition> nextEntry : compositeFields.entrySet()) {
			SearchParamDefinition searchParam = nextEntry.getValue();

			List<RuntimeSearchParam> compositeOf = new ArrayList<RuntimeSearchParam>();
			for (String nextName : searchParam.compositeOf()) {
				RuntimeSearchParam param = nameToParam.get(nextName);
				if (param == null) {
					ourLog.warn("Search parameter {}.{} declares that it is a composite with compositeOf value '{}' but that is not a valid parametr name itself. Valid values are: {}", new Object[] { theResourceDef.getName(), searchParam.name(), nextName, nameToParam.keySet() });
					continue;
				}
				compositeOf.add(param);
			}

			RuntimeSearchParam param = new RuntimeSearchParam(searchParam.name(), searchParam.description(), searchParam.path(), SearchParamTypeEnum.COMPOSITE, compositeOf);
			theResourceDef.addSearchParam(param);
		}
	}

	private static Class<?> getGenericCollectionTypeOfCodedField(Field next) {
		Class<?> type;
		ParameterizedType collectionType = (ParameterizedType) next.getGenericType();
		Type firstArg = collectionType.getActualTypeArguments()[0];
		if (ParameterizedType.class.isAssignableFrom(firstArg.getClass())) {
			ParameterizedType pt = ((ParameterizedType) firstArg);
			firstArg = pt.getActualTypeArguments()[0];
			type = (Class<?>) firstArg;
		} else {
			type = (Class<?>) firstArg;
		}
		return type;
	}

}
