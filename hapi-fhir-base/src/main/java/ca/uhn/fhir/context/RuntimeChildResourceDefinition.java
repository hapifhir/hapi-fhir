package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class RuntimeChildResourceDefinition extends BaseRuntimeChildDefinition {


	public RuntimeChildResourceDefinition(Field theField, String theElementName, int theMin, int theMax, List<Class<? extends IResource>> theChildTypes) {
		super(theField, theMin, theMax, theElementName);
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return new RuntimeResourceReferenceDefinition(null, null);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		// nothing
	}

}
