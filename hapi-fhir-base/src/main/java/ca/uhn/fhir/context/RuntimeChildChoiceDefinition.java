package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.List;

public class RuntimeChildChoiceDefinition extends BaseRuntimeChildDefinition {

	private List<String> myChoices;

	public RuntimeChildChoiceDefinition(Field theField, String theElementName, int theMin, int theMax, List<String> theChoiceNames) {
		super(theField, theElementName,theMin,theMax);
		
		myChoices= theChoiceNames;
	}

	public List<String> getChoices() {
		return myChoices;
	}


}
