package ca.uhn.fhir.context;

import java.util.Collections;
import java.util.Map;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class FhirContext {

	private final Map<String, RuntimeResourceDefinition> myNameToElementDefinition;
	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myClassToElementDefinition;

	public FhirContext(Class<? extends IResource>... theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToResourceDefinitions());
		myClassToElementDefinition = scanner.getClassToElementDefinitions();
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinition() {
		return myNameToElementDefinition;
	}

	public Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> getClassToElementDefinition() {
		return myClassToElementDefinition;
	}

}
