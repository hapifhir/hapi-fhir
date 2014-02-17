package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;

@DatatypeDefinition(name="integer")
public class IntegerDt extends BasePrimitiveDatatype {

	private int myValue;
	
}
