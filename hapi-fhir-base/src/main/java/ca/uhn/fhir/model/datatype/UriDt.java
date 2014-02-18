package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="uri")
public class UriDt extends BaseDatatype implements IPrimitiveDatatype {

	private String myValue;
	
}
