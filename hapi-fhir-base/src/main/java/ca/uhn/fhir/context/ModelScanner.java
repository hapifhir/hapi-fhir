package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.ctc.wstx.util.StringUtil;

import ca.uhn.fhir.model.api.CodeableConceptElement;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceChoiceElement;
import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.ResourceReferenceElement;
import ca.uhn.fhir.model.resource.ResourceDefinition;

class ModelScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelScanner.class);

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToResourceDefinitions = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToResourceDefinitions = new HashMap<String, BaseRuntimeElementDefinition<?>>();

	// private Map<String, RuntimeResourceDefinition>
	// myNameToDatatypeDefinitions = new HashMap<String,
	// RuntimeDatatypeDefinition>();

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

		DatatypeDefinition datatypeDefinition = theClass.getAnnotation(DatatypeDefinition.class);
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
				throw new ConfigurationException("Resource type contains a @" + DatatypeDefinition.class.getSimpleName() + " annotation but does not implement " + IDatatype.class.getCanonicalName() + ": " + theClass.getCanonicalName());
			}
		}

		throw new ConfigurationException("Resource type does not contain a @" + ResourceDefinition.class.getSimpleName() + " annotation or a @" + DatatypeDefinition.class.getSimpleName() + " annotation: " + theClass.getCanonicalName());
	}

	private String scanCompositeDatatype(Class<? extends ICompositeDatatype> theClass, DatatypeDefinition theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToResourceDefinitions.containsKey(resourceName)) {
			if (!myNameToResourceDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToResourceDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimeCompositeDatatypeDefinition resourceDef = new RuntimeCompositeDatatypeDefinition(resourceName, theClass);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToResourceDefinitions.put(resourceName, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);

		return resourceName;
	}

	private String scanPrimitiveDatatype(Class<? extends IPrimitiveDatatype> theClass, DatatypeDefinition theDatatypeDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = theDatatypeDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToResourceDefinitions.containsKey(resourceName)) {
			if (!myNameToResourceDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToResourceDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimePrimitiveDatatypeDefinition resourceDef = new RuntimePrimitiveDatatypeDefinition(resourceName, theClass);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToResourceDefinitions.put(resourceName, resourceDef);

		return resourceName;
	}

	private String scanResource(Class<? extends IResource> theClass, ResourceDefinition resourceDefinition) {
		ourLog.debug("Scanning resource class: {}", theClass.getName());

		String resourceName = resourceDefinition.name();
		if (isBlank(resourceName)) {
			throw new ConfigurationException("Resource type @" + ResourceDefinition.class.getSimpleName() + " annotation contains no resource name: " + theClass.getCanonicalName());
		}

		if (myNameToResourceDefinitions.containsKey(resourceName)) {
			if (!myNameToResourceDefinitions.get(resourceName).getImplementingClass().equals(theClass)) {
				throw new ConfigurationException("Detected duplicate element name '" + resourceName + "' in types '" + theClass.getCanonicalName() + "' and '" + myNameToResourceDefinitions.get(resourceName).getImplementingClass() + "'");
			}
			return resourceName;
		}

		RuntimeResourceDefinition resourceDef = new RuntimeResourceDefinition(theClass, resourceName);
		myClassToResourceDefinitions.put(theClass, resourceDef);
		myNameToResourceDefinitions.put(resourceName, resourceDef);

		scanCompositeElementForChildren(theClass, resourceDef);

		return resourceName;
	}

	private void scanCompositeElementForChildren(Class<? extends ICompositeElement> theClass, BaseRuntimeCompositeElementDefinition<?> theDefinition) {
		Set<String> elementNames = new HashSet<String>();
		Map<Integer, BaseRuntimeChildDefinition> orderToElementDef = new HashMap<Integer, BaseRuntimeChildDefinition>();
		for (Field next : theClass.getFields()) {
			ResourceElement element = next.getAnnotation(ResourceElement.class);
			if (element == null) {
				ourLog.debug("Ignoring non-type field: " + next.getName());
				continue;
			}

			String elementName = element.name();
			int order = element.order();
			int min = element.min();
			int max = element.max();

			ResourceChoiceElement choiceAttr = element.choice();
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

			ResourceReferenceElement resRefAnnotation = next.getAnnotation(ResourceReferenceElement.class);
			if (choiceTypes.isEmpty() == false) {

				List<String> choiceNames = new ArrayList<String>();
				for (Class<? extends IElement> nextChoiceType : choiceTypes) {
					choiceNames.add(scan(nextChoiceType));
				}

				RuntimeChildChoiceDefinition def = new RuntimeChildChoiceDefinition(next, elementName, min, max, choiceNames);
				orderToElementDef.put(order, def);

			} else if (ResourceReference.class.isAssignableFrom(next.getType())) {
				if (resRefAnnotation == null) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is a resource reference but does not have a @" + ResourceReferenceElement.class.getSimpleName() + " annotation");
				}

				Class<? extends IResource> refType = resRefAnnotation.type();
				String elementResourceName = scan(refType);

				if (!(myNameToResourceDefinitions.get(elementResourceName) instanceof IResource)) {
					throw new ConfigurationException("Resource reference '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is a resource reference but references a non-resource type: "
							+ myNameToResourceDefinitions.get(elementResourceName).getClass().getCanonicalName());
				}

				RuntimeChildResourceDefinition def = new RuntimeChildResourceDefinition(next, elementName, elementResourceName, min, max);
				orderToElementDef.put(order, def);
			} else {
				if (resRefAnnotation != null) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a resource reference but has a @" + ResourceReferenceElement.class.getSimpleName() + " annotation");
				}
				if (!IDatatype.class.isAssignableFrom(next.getType())) {
					throw new ConfigurationException("Field '" + elementName + "' in type '" + theClass.getCanonicalName() + "' is not a resource reference and is not an instance of type " + IDatatype.class.getName());
				}

				CodeableConceptElement concept = next.getAnnotation(CodeableConceptElement.class);
				if (concept != null) {
					Class<? extends ICodeEnum> codeType = concept.type();
					String codeTableName = scanCodeTable(codeType);
					@SuppressWarnings("unchecked")
					String datatypeName = scan((Class<? extends IDatatype>) next.getType());
					RuntimeChildDatatypeDefinition def = new RuntimeChildCodedDatatypeDefinition(next, elementName, datatypeName, min, max, codeTableName);
					orderToElementDef.put(order, def);
				} else {
					@SuppressWarnings("unchecked")
					String datatypeName = scan((Class<? extends IDatatype>) next.getType());
					RuntimeChildDatatypeDefinition def = new RuntimeChildDatatypeDefinition(next, elementName, datatypeName, min, max);
					orderToElementDef.put(order, def);
				}
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
