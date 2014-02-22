package ca.uhn.fhir.model.datatype;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseDateTimeDt extends BasePrimitiveDatatype<Date> {

	private static final FastDateFormat ourYearFormat = FastDateFormat.getInstance("yyyy");
	private static final FastDateFormat ourYearMonthDayFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	private static final FastDateFormat ourYearMonthFormat = FastDateFormat.getInstance("yyyy-MM");
	private static final FastDateFormat ourYearMonthDayTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
	private static final FastDateFormat ourYearMonthDayTimeZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");

	private int myPrecision = Calendar.SECOND;
	private Date myValue;
	private TimeZone myTimeZone;
	private boolean myTimeZoneZulu = false;

	
	/**
	 * Gets the precision for this datatype using field values from
	 * {@link Calendar}, such as {@link Calendar#MONTH}. Default is
	 * {@link Calendar#DAY_OF_MONTH}
	 * 
	 * @see #setPrecision(int)
	 */
	public int getPrecision() {
		return myPrecision;
	}

	@Override
	public Date getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		} else {
			switch (myPrecision) {
			case Calendar.DAY_OF_MONTH:
				return ourYearMonthDayFormat.format(myValue);
			case Calendar.MONTH:
				return ourYearMonthFormat.format(myValue);
			case Calendar.YEAR:
				return ourYearFormat.format(myValue);
			case Calendar.SECOND:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(myValue);
					return ourYearMonthDayTimeZoneFormat.format(cal)+"Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(myValue);
					return ourYearMonthDayTimeZoneFormat.format(cal);
				} else {
					return ourYearMonthDayTimeFormat.format(myValue);
				}
			}
			throw new IllegalStateException("Invalid precition (this is a HAPI bug, shouldn't happen): " + myPrecision);
		}
	}

	/**
	 * Sets the precision for this datatype using field values from
	 * {@link Calendar}. Valid values are:
	 * <ul>
	 * <li>{@link Calendar#SECOND}
	 * <li>{@link Calendar#DAY_OF_MONTH}
	 * <li>{@link Calendar#MONTH}
	 * <li>{@link Calendar#YEAR}
	 * </ul>
	 * 
	 * @throws DataFormatException
	 */
	public void setPrecision(int thePrecision) throws DataFormatException {
		switch (thePrecision) {
		case Calendar.DAY_OF_MONTH:
		case Calendar.MONTH:
		case Calendar.YEAR:
		case Calendar.SECOND:
			myPrecision = thePrecision;
			break;
		default:
			throw new DataFormatException("Invalid precision value");
		}
	}
	
	@Override
	public void setValue(Date theValue) throws DataFormatException {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		try {
			if (theValue == null) {
				myValue = null;
				clearTimeZone();
			} else if (theValue.length() == 4 && isPrecisionAllowed(Calendar.YEAR)) {
				setValue(ourYearFormat.parse(theValue));
				setPrecision(Calendar.YEAR);
				clearTimeZone();
			} else if (theValue.length() == 7 && isPrecisionAllowed(Calendar.MONTH)) {
				setValue(ourYearMonthFormat.parse(theValue));
				setPrecision(Calendar.MONTH);
				clearTimeZone();
			} else if (theValue.length() == 10 && isPrecisionAllowed(Calendar.DAY_OF_MONTH)) {
				setValue(ourYearMonthDayFormat.parse(theValue));
				setPrecision(Calendar.DAY_OF_MONTH);
				clearTimeZone();
			} else if ((theValue.length() == 18 || theValue.length() == 19 || theValue.length() == 24) && isPrecisionAllowed(Calendar.SECOND)) {
				ourYearMonthDayTimeZoneFormat.parse(theValue);
				setPrecision(Calendar.SECOND);
				clearTimeZone();
				if (theValue.length() == 19 && theValue.charAt(theValue.length()-1) == 'Z') {
					myTimeZoneZulu = true;
				}
			} else {
				throw new DataFormatException("Invalid date string");
			}
		} catch (ParseException e) {
			throw new DataFormatException("Invalid date string");
		}
	}

	public TimeZone getTimeZone() {
		return myTimeZone;
	}

	public void setTimeZone(TimeZone theTimeZone) {
		myTimeZone = theTimeZone;
	}

	public boolean isTimeZoneZulu() {
		return myTimeZoneZulu;
	}

	public void setTimeZoneZulu(boolean theTimeZoneZulu) {
		myTimeZoneZulu = theTimeZoneZulu;
	}

	private void clearTimeZone() {
		myTimeZone=null;
		myTimeZoneZulu=false;
	}

	/**
	 * To be implemented by subclasses to indicate whether the given precision is allowed by this type
	 */
	abstract boolean isPrecisionAllowed(int thePrecision);
	
}
