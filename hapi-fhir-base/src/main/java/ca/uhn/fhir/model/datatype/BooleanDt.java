package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;

@DatatypeDefinition(name="boolean")
public class BooleanDt extends BasePrimitiveDatatype {

	private boolean myValue;
	
}
