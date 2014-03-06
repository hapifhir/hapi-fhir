package ca.uhn.fhir.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.rest.client.RestfulClientFactory;

public class FhirContext {

	private final Map<String, RuntimeResourceDefinition> myNameToElementDefinition;
	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition;
	private RuntimeChildUndeclaredExtensionDefinition myRuntimeChildUndeclaredExtensionDefinition;

	public FhirContext(Class<? extends IResource>... theResourceTypes) {
		this(Arrays.asList(theResourceTypes));
	}

	public FhirContext(Collection<Class<? extends IResource>> theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToResourceDefinitions());
		myClassToElementDefinition = Collections.unmodifiableMap(scanner.getClassToElementDefinitions());
		myRuntimeChildUndeclaredExtensionDefinition = scanner.getRuntimeChildUndeclaredExtensionDefinition();
	}

	public RuntimeChildUndeclaredExtensionDefinition getRuntimeChildUndeclaredExtensionDefinition() {
		return myRuntimeChildUndeclaredExtensionDefinition;
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinition() {
		return myNameToElementDefinition;
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

	public IParser newXmlParser() {
		return new XmlParser(this);
	}
	
	public RestfulClientFactory newClientFactory() {
		return new RestfulClientFactory(this);
	}

}
