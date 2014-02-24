package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.primitive.NarrativeDt;

public class RuntimeChildNarrativeDefinition extends BaseRuntimeChildDatatypeDefinition {

	public RuntimeChildNarrativeDefinition(Field theField, String theElementName) {
		super(theField, theElementName, 1, 1, NarrativeDt.class);
	}

}
