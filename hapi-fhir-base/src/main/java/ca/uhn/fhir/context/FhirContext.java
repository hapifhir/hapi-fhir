package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;

public class FhirContext {

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition;
	private final Map<String, RuntimeResourceDefinition> myNameToElementDefinition;
	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;
	private Map<String, RuntimeResourceDefinition> myIdToResourceDefinition;

	public FhirContext(Class<?>... theResourceTypes) {
		this(toCollection(theResourceTypes));
	}

	public FhirContext(Class<? extends IResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToResourceDefinitions());
		myClassToElementDefinition = Collections.unmodifiableMap(scanner.getClassToElementDefinitions());
		myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
		myIdToResourceDefinition = scanner.getIdToResourceDefinition();
	}
	
	public RuntimeResourceDefinition getResourceDefinitionById(String theId) {
		return myIdToResourceDefinition.get(theId);
	}

	public BaseRuntimeElementDefinition<?> getElementDefinition(Class<? extends IElement> theElementType) {
		return myClassToElementDefinition.get(theElementType);
	}

	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IResource> theResourceType) {
		// TODO: parse this type if we would otherwise return null
		return (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
	}

	public RuntimeResourceDefinition getResourceDefinition(IResource theResource) {
		// TODO: parse this type if we would otherwise return null
		return (RuntimeResourceDefinition) myClassToElementDefinition.get(theResource.getClass());
	}

	public BaseRuntimeElementDefinition<?> getResourceDefinition(String theResourceName) {
		return myNameToElementDefinition.get(theResourceName);
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	public IRestfulClientFactory newRestfulClientFactory() {
		return new RestfulClientFactory(this);
	}

	public IParser newXmlParser() {
		return new XmlParser(this);
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

	private static Collection<Class<? extends IResource>> toCollection(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	public Collection<RuntimeResourceDefinition> getResourceDefinitions() {
		return myIdToResourceDefinition.values();
	}

	public IParser newJsonParser() {
		return new JsonParser(this);
	}

}
