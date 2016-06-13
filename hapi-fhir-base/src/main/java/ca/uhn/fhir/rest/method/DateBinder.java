package ca.uhn.fhir.rest.method;

import java.util.Date;

import ca.uhn.fhir.model.primitive.InstantDt;

final class DateBinder extends BaseJavaPrimitiveBinder<Date> {
	DateBinder() {
	}

	@Override
	protected String doEncode(Date theString) {
		return new InstantDt(theString).getValueAsString();
	}

	@Override
	protected Date doParse(String theString) {
		return new InstantDt(theString).getValue();
	}


}
