package ca.uhn.fhir.model.datatype;

import java.util.GregorianCalendar;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="dateTime")
public class DateTimeDt extends BasePrimitiveDatatype {

	private GregorianCalendar myValue;
	
	private int myPrecision;
	
}
