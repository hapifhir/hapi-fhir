package ca.uhn.fhir.model.datatype;

import java.util.Date;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="instant")
public class InstantDt extends BasePrimitiveDatatype {

	private Date myValue;
	
}
