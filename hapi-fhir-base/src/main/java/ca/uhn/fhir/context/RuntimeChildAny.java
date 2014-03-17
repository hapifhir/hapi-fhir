package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class RuntimeChildAny extends RuntimeChildChoiceDefinition {

	public RuntimeChildAny(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		List<Class<? extends IElement>> choiceTypes = new ArrayList<Class<? extends IElement>>();
		
		for (Class<? extends IElement> next : theClassToElementDefinitions.keySet()) {
			if (next.equals(XhtmlDt.class)) {
				continue;
			}
			if (IResource.class.isAssignableFrom(next) || IDatatype.class.isAssignableFrom(next)) {
				choiceTypes.add(next);
			}
		}
		Collections.sort(choiceTypes,new Comparator<Class<?>>(){
			@Override
			public int compare(Class<?> theO1, Class<?> theO2) {
				boolean o1res = IResource.class.isAssignableFrom(theO1);
				boolean o2res = IResource.class.isAssignableFrom(theO2);
				if (o1res && o2res) {
					return theO1.getSimpleName().compareTo(theO2.getSimpleName());
				} else if (o1res) {
					return 1;
				}else {
					return -1;
				}
			}});
		
		setChoiceTypes(choiceTypes);
		
		super.sealAndInitialize(theClassToElementDefinitions);
	}

	
	
}
