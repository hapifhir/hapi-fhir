package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.RestfulClientFactory;

public class FhirContext {

	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition;
	private final Map<String, RuntimeResourceDefinition> myNameToElementDefinition;
	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	public FhirContext(Class<? extends IResource> theResourceType) {
		this(toCollection(theResourceType));
	}

	public FhirContext(Class<? extends IResource>[] theResourceTypes) {
		this(Arrays.asList(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToResourceDefinitions());
		myClassToElementDefinition = Collections.unmodifiableMap(scanner.getClassToElementDefinitions());
		myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
	}

	public Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> getClassToElementDefinition() {
		return myClassToElementDefinition;
	}

	public RuntimeResourceDefinition getResourceDefinition(Class<? extends IResource> theResourceType) {
		return (RuntimeResourceDefinition) myClassToElementDefinition.get(theResourceType);
	}

	public RuntimeResourceDefinition getResourceDefinition(IResource theResource) {
		return (RuntimeResourceDefinition) myClassToElementDefinition.get(theResource.getClass());
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
	
	private static Collection<Class<? extends IResource>> toCollection(Class<? extends IResource> theResourceType) {
		ArrayList<Class<? extends IResource>> retVal = new ArrayList<Class<? extends IResource>>(1);
		retVal.add(theResourceType);
		return retVal;
	}

	public BaseRuntimeElementDefinition<?> getResourceDefinition(String theResourceName) {
		return myNameToElementDefinition.get(theResourceName);
	}

}
