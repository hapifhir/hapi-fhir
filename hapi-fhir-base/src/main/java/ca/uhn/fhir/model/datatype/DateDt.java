package ca.uhn.fhir.model.datatype;

import java.util.GregorianCalendar;

import ca.uhn.fhir.model.api.BaseDatatype;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.Datatype;

@Datatype(name="date")
public class DateDt extends BaseDatatype implements IPrimitiveDatatype {

	private GregorianCalendar myValue;
	private int myPrecision;
	
}
