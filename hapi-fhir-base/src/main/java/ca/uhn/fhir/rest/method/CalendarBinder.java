package ca.uhn.fhir.rest.method;

import java.util.Calendar;

import ca.uhn.fhir.model.primitive.InstantDt;

final class CalendarBinder extends BaseJavaPrimitiveBinder<Calendar> {
	CalendarBinder() {
	}

	@Override
	protected String doEncode(Calendar theString) {
		return new InstantDt(theString).getValueAsString();
	}

	@Override
	protected Calendar doParse(String theString) {
		return new InstantDt(theString).getValueAsCalendar();
	}


}
