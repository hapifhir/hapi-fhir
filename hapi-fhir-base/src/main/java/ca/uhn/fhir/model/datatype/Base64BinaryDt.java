package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="base64Binary")
public class Base64BinaryDt extends BasePrimitiveDatatype {

	private byte[] myValue;
	
}
