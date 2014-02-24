package ca.uhn.fhir.model.api;

import java.util.Calendar;

public enum TemporalPrecisionEnum {

	YEAR(Calendar.YEAR),
	MONTH(Calendar.MONTH),
	DAY(Calendar.DATE),
	SECOND(Calendar.SECOND),
	MILLI(Calendar.MILLISECOND),
	;
	
	private int myCalendarConstant;

	TemporalPrecisionEnum(int theCalendarConstant) {
		myCalendarConstant = theCalendarConstant;
	}

	public int getCalendarConstant() {
		return myCalendarConstant;
	}
}
