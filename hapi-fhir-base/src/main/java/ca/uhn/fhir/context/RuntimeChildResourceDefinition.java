package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceReference;

public class RuntimeChildResourceDefinition extends BaseRuntimeChildDefinition {

	private BaseRuntimeElementDefinition<?> myRuntimeDef;
	private List<Class<? extends IResource>> myResourceTypes;

	public RuntimeChildResourceDefinition(Field theField, String theElementName, int theMin, int theMax, List<Class<? extends IResource>> theResourceTypes) {
		super(theField, theMin, theMax, theElementName);
		myResourceTypes = theResourceTypes;
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		if (ResourceReference.class.equals(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		if (ResourceReference.class.equals(theDatatype)) {
			return myRuntimeDef;
		}
		return null;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return myRuntimeDef;
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myRuntimeDef = new RuntimeResourceReferenceDefinition(getElementName(), myResourceTypes);
	}
}
