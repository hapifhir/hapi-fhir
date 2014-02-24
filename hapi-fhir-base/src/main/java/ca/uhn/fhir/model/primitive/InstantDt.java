package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name="instant")
public class InstantDt extends BaseDateTimeDt {

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case SECOND:
		case MILLI:
			return true;
		default:
			return false;
		}
	}
	
}
