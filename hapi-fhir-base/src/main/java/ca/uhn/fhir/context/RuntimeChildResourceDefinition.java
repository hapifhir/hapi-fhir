package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.BaseResourceReference;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildResourceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private BaseRuntimeElementDefinition<?> myRuntimeDef;
	private List<Class<? extends IResource>> myResourceTypes;

	public RuntimeChildResourceDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, List<Class<? extends IResource>> theResourceTypes) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		myResourceTypes = theResourceTypes;
		
		if (theResourceTypes == null || theResourceTypes.isEmpty()) {
			throw new ConfigurationException("Field '" + theField.getName() + "' on type '" + theField.getDeclaringClass().getCanonicalName() + "' has no resource types noted");
		}
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		if (BaseResourceReference.class.isAssignableFrom(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		if (BaseResourceReference.class.isAssignableFrom(theDatatype)) {
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
		myRuntimeDef.sealAndInitialize(theClassToElementDefinitions);
	}
}
