package org.hl7.fhir.dstu2016may.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public enum TemporalPrecisionEnum {

	YEAR(Calendar.YEAR) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addYears(theInput, theAmount);
		}
	},

	MONTH(Calendar.MONTH) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addMonths(theInput, theAmount);
		}
	},
	DAY(Calendar.DATE) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addDays(theInput, theAmount);
		}
	},
	MINUTE(Calendar.MINUTE) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addMinutes(theInput, theAmount);
		}
	},
	SECOND(Calendar.SECOND) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addSeconds(theInput, theAmount);
		}
	},
	MILLI(Calendar.MILLISECOND) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addMilliseconds(theInput, theAmount);
		}
	},

	;

	private int myCalendarConstant;

	TemporalPrecisionEnum(int theCalendarConstant) {
		myCalendarConstant = theCalendarConstant;
	}

	public abstract Date add(Date theInput, int theAmount);

	public int getCalendarConstant() {
		return myCalendarConstant;
	}

}
