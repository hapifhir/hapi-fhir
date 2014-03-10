package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildResourceBlockDefinition extends BaseRuntimeDeclaredChildDefinition {

	private RuntimeResourceBlockDefinition myElementDef;
	private Class<? extends IResourceBlock> myResourceBlockType;

	public RuntimeChildResourceBlockDefinition(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName, Class<? extends IResourceBlock> theResourceBlockType) throws ConfigurationException {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		myResourceBlockType = theResourceBlockType;
	}

	@Override
	public RuntimeResourceBlockDefinition getChildByName(String theName) {
		if (getElementName().equals(theName)) {
			return myElementDef;
		}else {
			return null;
		}
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		if (myResourceBlockType.equals(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		if (myResourceBlockType.equals(theDatatype)) {
			return myElementDef;
		}
		return null;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myElementDef = (RuntimeResourceBlockDefinition) theClassToElementDefinitions.get(myResourceBlockType);
	}

}
