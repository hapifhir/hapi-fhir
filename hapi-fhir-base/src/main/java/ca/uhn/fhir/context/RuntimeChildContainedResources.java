package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;

public class RuntimeChildContainedResources extends BaseRuntimeDeclaredChildDefinition {

	private RuntimeElemContainedResources myElem;

	RuntimeChildContainedResources(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName) throws ConfigurationException {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		assert theName.equals(getElementName());
		return myElem;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theType) {
		assert theType.equals(ContainedDt.class);
		return myElem;		
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		assert theDatatype.equals(ContainedDt.class);
		return getElementName();
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myElem = new RuntimeElemContainedResources();
	}

}
