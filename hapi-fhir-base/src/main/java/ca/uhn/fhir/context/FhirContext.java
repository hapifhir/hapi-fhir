package ca.uhn.fhir.context;

import java.util.Collections;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;

public class FhirContext {

	private final Map<String, BaseRuntimeElementDefinition<?>> myNameToElementDefinition;

	public FhirContext(Class<? extends IResource>... theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToElementDefinitions());
	}

	public Map<String, BaseRuntimeElementDefinition<?>> getNameToElementDefinition() {
		return myNameToElementDefinition;
	}

}
