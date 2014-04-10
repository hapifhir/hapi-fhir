package ca.uhn.fhir.model.primitive;

import java.util.Date;
import java.util.TimeZone;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name="instant")
public class InstantDt extends BaseDateTimeDt {

	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.MILLI;

	/**
	 * Constructor
	 */
	public InstantDt() {
		super();
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 */
	@SimpleSetter
	public InstantDt(@SimpleSetter.Parameter(name = "theDate") Date theDate, @SimpleSetter.Parameter(name = "thePrecision") TemporalPrecisionEnum thePrecision) {
		setValue(theDate);
		setPrecision(thePrecision);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Create a new DateTimeDt
	 */
	@SimpleSetter(suffix="WithMillisPrecision")
	public InstantDt(@SimpleSetter.Parameter(name="theDate") Date theDate) {
		setValue(theDate);
		setPrecision(DEFAULT_PRECISION);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Create a new InstantDt from a string value
	 * 
	 * @param theString The string representation of the string. Must be in 
	 * a valid format according to the FHIR specification
	 * @throws DataFormatException 
	 */
	public InstantDt(String theString) {
		setValueAsString(theString);
	}

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
