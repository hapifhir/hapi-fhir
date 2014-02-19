package ca.uhn.fhir.model.datatype;

import java.util.Calendar;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "date")
public class DateDt extends BaseDateTimeDt {

	@Override
	boolean isPrecisionAllowed(int thePrecision) {
		switch (thePrecision) {
		case Calendar.YEAR:
		case Calendar.MONTH:
		case Calendar.DATE:
			return true;
		default:
			return false;
		}
	}


}
