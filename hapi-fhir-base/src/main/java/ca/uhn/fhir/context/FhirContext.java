package ca.uhn.fhir.context;

import java.util.Collections;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;

public class FhirContext {

	private final Map<String, RuntimeResourceDefinition> myNameToElementDefinition;

	public FhirContext(Class<? extends IResource>... theResourceTypes) {
		ModelScanner scanner = new ModelScanner(theResourceTypes);
		myNameToElementDefinition = Collections.unmodifiableMap(scanner.getNameToResourceDefinitions());
	}

	public Map<String, RuntimeResourceDefinition> getNameToResourceDefinition() {
		return myNameToElementDefinition;
	}

}
