package ca.uhn.fhir.model.primitive;

import java.util.Date;
import java.util.TimeZone;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "dateTime")
public class DateTimeDt extends BaseDateTimeDt {

	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.SECOND;

	/**
	 * Constructor
	 */
	public DateTimeDt() {
		super();
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#YEAR}
	 * <li>{@link TemporalPrecisionEnum#MONTH}
	 * <li>{@link TemporalPrecisionEnum#DAY}
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 */
	@SimpleSetter
	public DateTimeDt(@SimpleSetter.Parameter(name = "theDate") Date theDate, @SimpleSetter.Parameter(name = "thePrecision") TemporalPrecisionEnum thePrecision) {
		setValue(theDate);
		setPrecision(thePrecision);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Create a new DateTimeDt
	 */
	@SimpleSetter(suffix="WithSecondsPrecision")
	public DateTimeDt(@SimpleSetter.Parameter(name="theDate") Date theDate) {
		setValue(theDate);
		setPrecision(DEFAULT_PRECISION);
		setTimeZone(TimeZone.getDefault());
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
