package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;

public class FhirContext {

	private volatile Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myIdToResourceDefinition = Collections.emptyMap();
	private volatile Map<String, RuntimeResourceDefinition> myNameToElementDefinition = Collections.emptyMap();
	private volatile RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	public FhirContext(Class<? extends IResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		scanResourceTypes(theResourceTypes);
	}

	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IElement> theElementType) {
		return myClassToElementDefinition.get(theElementType);
	}
	
	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IResource> theResourceType) {
		RuntimeResourceDefinition retVal = (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
		if (retVal == null) {
			retVal = scanResourceType(theResourceType);
		}
		return retVal;
	}

	public RuntimeResourceDefinition getResourceDefinition(IResource theResource) {
		return getResourceDefinition(theResource.getClass());
	}

	@SuppressWarnings("unchecked")
	public BaseRuntimeElementDefinition<?> getResourceDefinition(String theResourceName) {
		RuntimeResourceDefinition retVal = myNameToElementDefinition.get(theResourceName);
		
		if (retVal == null) {
			try {
				String candidateName = Patient.class.getPackage().getName() + "." + theResourceName;
				Class<?> clazz = Class.forName(candidateName);
				if (IResource.class.isAssignableFrom(clazz)) {
					retVal = scanResourceType((Class<? extends IResource>) clazz);
				}
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		
		return retVal;
	}

	public RuntimeResourceDefinition getResourceDefinitionById(String theId) {
		return myIdToResourceDefinition.get(theId);
	}

	public Collection<RuntimeResourceDefinition> getResourceDefinitions() {
		return myIdToResourceDefinition.values();
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	public IParser newJsonParser() {
		return new JsonParser(this);
	}

	public IRestfulClientFactory newRestfulClientFactory() {
		return new RestfulClientFactory(this);
	}

	public IParser newXmlParser() {
		return new XmlParser(this);
	}
	
	private RuntimeResourceDefinition scanResourceType(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> resourceTypes = new ArrayList<Class<? extends IResource>>();
		resourceTypes.add(theResourceType);
		Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> defs = scanResourceTypes(resourceTypes);
		return (RuntimeResourceDefinition) defs.get(theResourceType);
	}

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> scanResourceTypes(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		if (myRuntimeChildUndeclaredExtensionDefinition == null) {
			myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
		}
		
		Map<String, RuntimeResourceDefinition> nameToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		nameToElementDefinition.putAll(myNameToElementDefinition);
		nameToElementDefinition.putAll(scanner.getNameToResourceDefinitions());
		
		Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> classToElementDefinition = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
		classToElementDefinition.putAll(myClassToElementDefinition);
		classToElementDefinition.putAll(scanner.getClassToElementDefinitions());

		Map<String, RuntimeResourceDefinition> idToElementDefinition = new HashMap<String, RuntimeResourceDefinition>();
		idToElementDefinition.putAll(myIdToResourceDefinition);
		idToElementDefinition.putAll(scanner.getIdToResourceDefinition());

		myNameToElementDefinition = idToElementDefinition;
		myClassToElementDefinition = classToElementDefinition;
		myIdToResourceDefinition = idToElementDefinition;
		
		return classToElementDefinition;
	}

	private static Collection<Class<? extends IResource>> toCollection(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends IResource>> toCollection(Class<?>[] theResourceTypes) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		for (Class<?> clazz : theResourceTypes) {
			if (!IResource.class.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException(clazz.getCanonicalName() + " is not an instance of " + IResource.class.getSimpleName());
			}
			retVal.add((Class<? extends IResource>) clazz);
		}
		return retVal;
	}

}
