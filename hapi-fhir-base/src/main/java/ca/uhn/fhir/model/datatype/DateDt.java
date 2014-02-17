package ca.uhn.fhir.model.datatype;

import java.util.GregorianCalendar;

import ca.uhn.fhir.model.api.BaseDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;

@DatatypeDefinition(name="date")
public class DateDt extends BaseDatatype implements IPrimitiveDatatype {

	private GregorianCalendar myValue;
	private int myPrecision;
	
}
