package ca.uhn.fhir.model.datatype;

import java.util.Calendar;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "dateTime")
public class DateTimeDt extends BaseDateTimeDt {

	@Override
	boolean isPrecisionAllowed(int thePrecision) {
		switch (thePrecision) {
		case Calendar.YEAR:
		case Calendar.MONTH:
		case Calendar.DATE:
		case Calendar.SECOND:
			return true;
		default:
			return false;
		}
	}

}
