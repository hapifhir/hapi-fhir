package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.List;

import ca.uhn.fhir.model.api.IElement;

public class RuntimeChildChoiceDefinition extends BaseRuntimeChildDefinition {

	private List<Class<? extends IElement>> myChoiceTypes;

	public RuntimeChildChoiceDefinition(Field theField, String theElementName, int theMin, int theMax, List<Class<? extends IElement>> theChoiceTypes) {
		super(theField, theMin,theMax, theElementName);
		
		myChoiceTypes= theChoiceTypes;
	}

	public List<Class<? extends IElement>> getChoices() {
		return myChoiceTypes;
	}


}
