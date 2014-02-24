package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "date")
public class DateDt extends BaseDateTimeDt {

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case YEAR:
		case MONTH:
		case DAY:
			return true;
		default:
			return false;
		}
	}


}
