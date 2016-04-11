package ca.uhn.fhir.context;

import static org.hamcrest.Matchers.emptyCollectionOf;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildExtension extends RuntimeChildAny {

	private RuntimeChildUndeclaredExtensionDefinition myExtensionElement;

	public RuntimeChildExtension(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation);
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IBase> theDatatype) {
		return getElementName();
	}

	@Override
	public Set<String> getValidChildNames() {
		return Collections.singleton(getElementName());
	}
	
//	@Override
//	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IBase> theDatatype) {
//		if (IBaseExtension.class.isAssignableFrom(theDatatype)) {
//			return myExtensionElement;
//		}
//		return super.getChildElementDefinitionByDatatype(theDatatype);
//	}
//
//	@Override
//	void sealAndInitialize(FhirContext theContext, Map<Class<? extends IBase>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
//		super.sealAndInitialize(theContext, theClassToElementDefinitions);
//		
//		myExtensionElement = theContext.getRuntimeChildUndeclaredExtensionDefinition();
//	}


}
