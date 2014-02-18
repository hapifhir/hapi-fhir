package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildResource;
import ca.uhn.fhir.model.api.annotation.Datatype;
import ca.uhn.fhir.model.api.annotation.Choice;
import ca.uhn.fhir.model.resource.ResourceDefinition;

class ModelScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelScanner.class);

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToResourceDefinitions = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
	private Map<String, RuntimeResourceDefinition> myNameToElementDefinitions = new HashMap<String, RuntimeResourceDefinition>();
	private Set<Class<? extends IElement>> myScanAlso = new HashSet<Class<? extends IElement>>();
	
	// private Map<String, RuntimeResourceDefinition>
	// myNameToDatatypeDefinitions = new HashMap<String,
	// RuntimeDatatypeDefinition>();

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinitions() {
		return (myNameToElementDefinitions);
	}

	ModelScanner(Class<? extends IResource>... theResourceTypes) throws ConfigurationException {
		for (Class<? extends IResource> nextClass : theResourceTypes) {
			scan(nextClass);
		}
	}

	private String scan(Class<? extends IElement> theClass) throws ConfigurationException {
		BaseRuntimeElementDefinition<?> existingDef = myClassToResourceDefinitions.get(theClass);
		if (existingDef != null) {
			return existingDef.getName();
		}

		ResourceDefinition resourceDefinition = theClass.getAnnotation(ResourceDefinition.class);
		if (resourceDefinition != null) {
			if (!IResource.class.isAssignableFrom(theClass)) {
				throw new ConfigurationException("Resource type contains a @" + ResourceDefinition.class.getSimpleName() + " annotation but does not implement " + IResource.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
			@SuppressWarnings("unchecked")
			Class<? extends IResource> resClass = (Class<? extends IResource>) theClass;
			return scanResource(resClass, resourceDefinition);
		}

		Datatype datatypeDefinition = theClass.getAnnotation(Datatype.class);
		if (datatypeDefinition != null) {
			if (ICompositeDatatype.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends ICompositeDatatype> resClass = (Class<? extends ICompositeDatatype>) theClass;
				return scanCompositeDatatype(resClass, datatypeDefinition);
			} else if (IPrimitiveDatatype.class.isAssignableFrom(theClass)) {
				@SuppressWarnings("unchecked")
				Class<? extends IPrimitiveDatatype> resClass = (Class<? extends IPrimitiveDatatype>) theClass;
				return scanPrimitiveDatatype(resClass, datatypeDefinition);
			} else {
				throw new ConfigurationException("Resource type contains a @" + Datatype.class.getSimpleName() + " annotation but does not implement " + IDatatype.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		throw new ConfigurationException("Resource type does not contain a @" + ResourceDefinition.class.getSimpleName() + " annotation or a @" + Datatype.class.getSimpleName() + " annotation: " + theClass.getCanonicalName());
	}

	private String scanCompositeDatatype(Class<? extends ICompositeDatatype> theClass, Datatype theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToElementDefinitions.containsKey(resourceName)) {
			if (!myNameToElementDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToElementDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimeCompositeDatatypeDefinition resourceDef = new RuntimeCompositeDatatypeDefinition(resourceName, theClass);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToElementDefinitions.put(resourceName, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);

		return resourceName;
	}

	private String scanPrimitiveDatatype(Class<? extends IPrimitiveDatatype> theClass, Datatype theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToElementDefinitions.containsKey(resourceName)) {
			if (!myNameToElementDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToElementDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimePrimitiveDatatypeDefinition resourceDef = new RuntimePrimitiveDatatypeDefinition(resourceName, theClass);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToElementDefinitions.put(resourceName, resourceDef);

		return resourceName;
	}

	private String scanResource(Class<? extends IResource> theClass, ResourceDefinition resourceDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = resourceDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToElementDefinitions.containsKey(resourceName)) {
			if (!myNameToElementDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToElementDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(theClass, resourceName);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToElementDefinitions.put(resourceName, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);

		return resourceName;
	}

	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, BaseRuntimeElementCompositeDefinition<?> theDefinition) {
		Set<String> elementNames = new HashSet<String>();
		Map<Integer, BaseRuntimeChildDefinition> orderToElementDef = new HashMap<Integer, BaseRuntimeChildDefinition>();
		for (Field next : theClass.getFields()) {
			Child element = next.getAnnotation(Child.class);
			if (element == null) {
				ourLog.debug("Ignoring non-type field: " + next.getName());
				continue;
			}

			String elementName = element.name();
			int order = element.order();
			int min = element.min();
			int max = element.max();

			Choice choiceAttr = element.choice();
			List<Class<? extends IElement>> choiceTypes = new ArrayList<Class<? extends IElement>>();
			for (Class<? extends IElement> nextChoiceType : choiceAttr.types()) {
				choiceTypes.add(nextChoiceType);
			}

			if (orderToElementDef.containsKey(order)) {
				throw new ConfigurationException("Detected duplicate field order '" + order + "' in type '" + theClass.getCanonicalName() + "'");
			}

			if (elementNames.contains(elementName)) {
				throw new ConfigurationException("Detected duplicate field name '" + elementName + "' in type '" + theClass.getCanonicalName() + "'");
			}

			ChildResource resRefAnnotation = next.getAnnotation(ChildResource.class);
			if (choiceTypes.isEmpty() == false) {
				/*
				 * Child is a choice element
				 */
				myScanAlso.addAll(choiceTypes);
				RuntimeChildChoiceDefinition def = new RuntimeChildChoiceDefinition(next, elementName, min, max, choiceTypes);
				orderToElementDef.put(order, def);

			} else if (ResourceReference.class.isAssignableFrom(next.getType())) {
				/*
				 * Child is a resource reference
				 */
				if (resRefAnnotation == null) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is a resource reference but does not have a @" + ChildResource.class.getSimpleName() + " annotation");
				}

				Class<? extends IResource>[] refType = resRefAnnotation.types();
				List<Class<? extends IResource>> refTypesList = Arrays.asList(refType);
				myScanAlso.addAll(refTypesList);
				RuntimeChildResourceDefinition def = new RuntimeChildResourceDefinition(next, elementName, min, max, refTypesList);
				orderToElementDef.put(order, def);
				
			} else {
				if (resRefAnnotation != null) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a resource reference but has a @" + ChildResource.class.getSimpleName() + " annotation");
				}
				if (!IDatatype.class.isAssignableFrom(next.getType())) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a resource reference and is not an instance of type " + IDatatype.class.getName());
				}

				@SuppressWarnings("unchecked")
				Class<? extends IDatatype> nextDatatype = (Class<? extends IDatatype>) next.getType();

				CodeableConceptElement concept = next.getAnnotation(CodeableConceptElement.class);
				
				if (IPrimitiveDatatype.class.isAssignableFrom(next.getType())) {
					myScanAlso.add(nextDatatype);
					RuntimeChildPrimitiveDatatypeDefinition def = new RuntimeChildPrimitiveDatatypeDefinition(next, elementName, datatypeName, min, max);
				}else {
					@SuppressWarnings("unchecked")
					String datatypeName = scan(nextDatatype);
					RuntimeChildCompositeDatatypeDefinition def = new RuntimeChildCompositeDatatypeDefinition(next, elementName, datatypeName, min, max, datatypeName);
					orderToElementDef.put(order, def);
				}

//				TODO: handle codes
//				if (concept != null) {
//					Class<? extends ICodeEnum> codeType = concept.type();
//					String codeTableName = scanCodeTable(codeType);
//					@SuppressWarnings("unchecked")
//					String datatypeName = scan((Class<? extends IDatatype>) next.getType());
//					RuntimeChildCompositeDatatypeDefinition def = new RuntimeChildCompositeDatatypeDefinition(next, elementName, datatypeName, min, max, codeTableName);
//					orderToElementDef.put(order, def);
//				} else {
//					@SuppressWarnings("unchecked")
//					orderToElementDef.put(order, def);
//				}
			}

			elementNames.add(elementName);
		}

		for (int i = 0; i < orderToElementDef.size(); i++) {
			if (!orderToElementDef.containsKey(i)) {
				throw new ConfigurationException("Type '" + theClass.getCanonicalName() + "' does not have a child with order " + i + " (in other words, there are gaps between specified child orders)");
			}
			BaseRuntimeChildDefinition next = orderToElementDef.get(i);
			theDefinition.addChild(next);
		}

	}

	private String scanCodeTable(Class<? extends ICodeEnum> theCodeType) {
		// TODO Auto-generated method stub
		return null;
	}

}
