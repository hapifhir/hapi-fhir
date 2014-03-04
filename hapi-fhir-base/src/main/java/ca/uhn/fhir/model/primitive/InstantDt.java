package ca.uhn.fhir.model.primitive;

import java.util.Date;
import java.util.TimeZone;

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

	/**
	 * Sets the value of this instant to the current time (from the system clock)
	 * and the local/default timezone (as retrieved using {@link TimeZone#getDefault()}. This
	 * TimeZone is generally obtained from the underlying OS.
	 */
	public void setToCurrentTimeInLocalTimeZone() {
		setValue(new Date());
		setTimeZone(TimeZone.getDefault());
	}

}
