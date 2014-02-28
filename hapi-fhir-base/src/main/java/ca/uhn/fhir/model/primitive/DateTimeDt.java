package ca.uhn.fhir.model.primitive;

import java.util.Date;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "dateTime")
public class DateTimeDt extends BaseDateTimeDt {

	/**
	 * Create a new DateTimeDt
	 */
	public DateTimeDt() {
		super();
	}

	/**
	 * Create a new DateTimeDt
	 */
	@SimpleSetter(suffix="WithSecondsPrecision")
	public DateTimeDt(@SimpleSetter.Parameter(name="theDate") Date theDate) {
		setValue(theDate);
		setPrecision(TemporalPrecisionEnum.SECOND);
	}

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case YEAR:
		case MONTH:
		case DAY:
		case SECOND:
		case MILLI:
			return true;
		default:
			return false;
		}
	}

}
