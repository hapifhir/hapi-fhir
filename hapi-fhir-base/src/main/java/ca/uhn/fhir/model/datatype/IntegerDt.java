package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="integer")
public class IntegerDt extends BasePrimitiveDatatype {

	private int myValue;
	
}
