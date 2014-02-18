package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="string")
public class StringDt extends BasePrimitiveDatatype {

	private String myValue;
	
}
