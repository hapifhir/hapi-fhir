package ca.uhn.fhir.model.datatype;

import java.util.Calendar;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="instant")
public class InstantDt extends BaseDateTimeDt {

	@Override
	boolean isPrecisionAllowed(int thePrecision) {
		return thePrecision == Calendar.SECOND;
	}
	
}
