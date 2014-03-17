package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ca.uhn.fhir.model.api.BaseResourceReference;
import ca.uhn.fhir.model.api.CodeableConceptElement;
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
import ca.uhn.fhir.model.api.annotation.CodeTableDef;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
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

	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	private Set<Class<? extends IElement>> myScanAlso = new HashSet<Class<? extends IElement>>();

	private Set<Class<? extends ICodeEnum>> myScanAlsoCodeTable = new HashSet<Class<? extends ICodeEnum>>();

	ModelScanner(Class<? extends IResource> theResourceTypes) throws ConfigurationException {
		Set<Class<? extends IElement>> singleton = new HashSet<Class<? extends IElement>>();
		singleton.add(theResourceTypes);
		init(singleton);
	}

	ModelScanner(Collection<Class<? extends IResource>> theResourceTypes) throws ConfigurationException {
		init(new HashSet<Class<? extends IElement>>(theResourceTypes));
	}

	public Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> getClassToElementDefinitions() {
		return myClassToElementDefinitions;
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinitions() {
		return (myNameToResourceDefinitions);
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

	private void init(Set<Class<? extends IElement>> toScan) {
		toScan.add(DateDt.class);
		toScan.add(CodeDt.class);

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

		for (BaseRuntimeElementDefinition<?> next : myClassToElementDefinitions.values()) {
			next.sealAndInitialize(myClassToElementDefinitions);
		}

		myRuntimeChildUndeclaredExtensionDefinition = new RuntimeChildUndeclaredExtensionDefinition();
		myRuntimeChildUndeclaredExtensionDefinition.sealAndInitialize(myClassToElementDefinitions);

		ourLog.info("Done scanning FHIR library, found {} model entries", myClassToElementDefinitions.size());
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

		CodeTableDef codeTableDefinition = theClass.getAnnotation(CodeTableDef.class);
		if (codeTableDefinition != null) {
			if (ICodeEnum.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends ICodeEnum> resClass = (Class<? extends ICodeEnum>) theClass;
				scanCodeTable(resClass, codeTableDefinition);
			} else {
				throw new ConfigurationException("Type contains a @" + CodeTableDef.class.getSimpleName() + " annotation but does not implement " + ICodeEnum.class.getCanonicalName() + ": " + theClass.getCanonicalName());
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

		if (blockDefinition == null && codeTableDefinition == null && datatypeDefinition == null && resourceDefinition == null) {
			throw new ConfigurationException("Resource type does not contain any valid HAPI-FHIR annotations: " + theClass.getCanonicalName());
		}
	}

	private void scanBlock(Class<? extends IResourceBlock> theClass, Block theBlockDefinition) {
		ourLog.debug("Scanning resource block class: {}", theClass.getName());

		String resourceName = theBlockDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Block type @" + Block.class.getSimpleName() + " annotation contains no name: " + theClass.getCanonicalName());
		}

		RuntimeResourceBlockDefinition resourceDef = new RuntimeResourceBlockDefinition(resourceName, theClass);
		myClassToElementDefinitions.put(theClass, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef, null);
	}

	private String scanCodeTable(Class<? extends ICodeEnum> theCodeType, CodeTableDef theCodeTableDefinition) {
		return null; // TODO: implement
	}

	private void scanCompositeDatatype(Class<? extends ICompositeDatatype> theClass, DatatypeDef theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		RuntimeCompositeDatatypeDefinition resourceDef = new RuntimeCompositeDatatypeDefinition(theDatatypeDefinition, theClass);
		myClassToElementDefinitions.put(theClass, resourceDef);
		scanCompositeElementForChildren(theClass, resourceDef, null);
	}

	@SuppressWarnings("unchecked")
	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, BaseRuntimeElementCompositeDefinition<?> theDefinition, Integer theIdentifierOrder) {
		Set<String> elementNames = new HashSet<String>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToElementDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();
		TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderToExtensionDef = new TreeMap<Integer, BaseRuntimeDeclaredChildDefinition>();

		LinkedList<Class<? extends ICompositeElement>> classes = new LinkedList<Class<? extends ICompositeElement>>();
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
			scanCompositeElementForChildren(next, theDefinition, elementNames, orderToElementDef, orderToExtensionDef);
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
	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, BaseRuntimeElementCompositeDefinition<?> theDefinition, Set<String> elementNames, TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToElementDef,
			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> theOrderToExtensionDef) {
		int baseElementOrder = theOrderToElementDef.isEmpty() ? 0 : theOrderToElementDef.lastEntry().getKey() + 1;

		for (Field next : theClass.getDeclaredFields()) {

			Child childAnnotation = next.getAnnotation(Child.class);
			if (childAnnotation == null) {
				ourLog.debug("Ignoring non-type field '" + next.getName() + "' on target type: " + theClass);
				continue;
			}

			Description descriptionAnnotation = next.getAnnotation(Description.class);

			String elementName = childAnnotation.name();
			int order = childAnnotation.order() + baseElementOrder;
			int min = childAnnotation.min();
			int max = childAnnotation.max();
			TreeMap<Integer, BaseRuntimeDeclaredChildDefinition> orderMap = theOrderToElementDef;

			Extension extensionAttr = next.getAnnotation(Extension.class);
			if (extensionAttr != null) {
				orderMap = theOrderToExtensionDef;
			}

			/*
			 * Anything that's marked as unknown is given a new ID that is <0 so
			 * that it doesn't conflict wityh any given IDs and can be figured
			 * out later
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
				
			} else if (choiceTypes.size() > 1 && !ResourceReferenceDt.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a choice element
				 */
				for (Class<? extends IElement> nextType : choiceTypes) {
					addScanAlso(nextType);
				}
				RuntimeChildChoiceDefinition def = new RuntimeChildChoiceDefinition(next, elementName, childAnnotation, descriptionAnnotation, choiceTypes);
				orderMap.put(order, def);

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
			} else if (BaseResourceReference.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource reference
				 */
				List<Class<? extends IResource>> refTypesList = new ArrayList<Class<? extends IResource>>();
				for (Class<? extends IElement> nextType : childAnnotation.type()) {
					if (IResource.class.isAssignableFrom(nextType) == false) {
						throw new ConfigurationException("Field '" + next.getName() + "' in class '" + next.getDeclaringClass().getSimpleName() + "' is of type " + ResourceReferenceDt.class + " but contains a non-resource type: " + nextType.getCanonicalName());
					}
					refTypesList.add((Class<? extends IResource>) nextType);
					addScanAlso(nextType);
				}
				RuntimeChildResourceDefinition def = new RuntimeChildResourceDefinition(next, elementName, childAnnotation, descriptionAnnotation, refTypesList);
				orderMap.put(order, def);

			} else if (IResourceBlock.class.isAssignableFrom(nextElementType)) {
				/*
				 * Child is a resource block (i.e. a sub-tag within a resource)
				 * TODO: do these have a better name according to HL7?
				 */

				Class<? extends IResourceBlock> blockDef = (Class<? extends IResourceBlock>) nextElementType;
				addScanAlso(blockDef);
				RuntimeChildResourceBlockDefinition def = new RuntimeChildResourceBlockDefinition(next, childAnnotation, descriptionAnnotation, elementName, blockDef);
				orderMap.put(order, def);

			} else if (IDatatype.class.equals(nextElementType)) {
				
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
					if (nextElementType.equals(BoundCodeableConceptDt.class)) {
						IValueSetEnumBinder<Enum<?>> binder = getBoundCodeBinder(next);
						def = new RuntimeChildCompositeBoundDatatypeDefinition(next, elementName, childAnnotation, descriptionAnnotation, nextDatatype, binder);
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

		String resourceName = resourceDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDef.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToResourceDefinitions.containsKey(resourceName)) {
			if (!myNameToResourceDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				// throw new
				// ConfigurationException("Detected duplicate element name '" +
				// resourceName + "' in types '" + theClass.getCanonicalName() +
				// "' and '"
				// +
				// myNameToResourceDefinitions.get(resourceName).getImplementingClass()
				// + "'");
			} else {
				return resourceName;
			}
		}

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

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(theClass, resourceDefinition);
		myClassToElementDefinitions.put(theClass, resourceDef);
		myNameToResourceDefinitions.put(resourceName, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef, resourceDefinition.identifierOrder());

		myIdToResourceDefinition.put(resourceId, resourceDef);
		return resourceName;
	}

	public Map<String, RuntimeResourceDefinition> getIdToResourceDefinition() {
		return myIdToResourceDefinition;
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
