package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public abstract class BaseRuntimeChildDatatypeDefinition extends BaseRuntimeDeclaredChildDefinition {

	private Class<? extends ICodeEnum> myCodeType;
	private Class<? extends IDatatype> myDatatype;

	private BaseRuntimeElementDefinition<?> myElementDefinition;

	public BaseRuntimeChildDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, Class<? extends IDatatype> theDatatype) {
		super(theField, theChildAnnotation, theDescriptionAnnotation, theElementName);
		assert theDatatype != IResource.class; // shouldn't exist
		assert theDatatype != IDatatype.class; // should use RuntimeChildAny
		myDatatype = theDatatype;
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		if (myDatatype.equals(theDatatype)) {
			return getElementName();
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		if (myDatatype.equals(theDatatype)) {
			return myElementDefinition;
		}
		return null;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		if (getElementName().equals(theName)) {
			return myElementDefinition;
		}
		return null;
	}

	public Class<? extends ICodeEnum> getCodeType() {
		return myCodeType;
	}

	public Class<? extends IDatatype> getDatatype() {
		return myDatatype;
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myElementDefinition = theClassToElementDefinitions.get(getDatatype());
		assert myElementDefinition != null : "Unknown type: " + getDatatype();
	}

	public void setCodeType(Class<? extends ICodeEnum> theType) {
		if (myElementDefinition != null) {
			throw new IllegalStateException("Can not set code type at runtime");
		}
		myCodeType = theType;
	}
}
