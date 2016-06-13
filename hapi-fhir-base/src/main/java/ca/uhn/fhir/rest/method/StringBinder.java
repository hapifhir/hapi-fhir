package ca.uhn.fhir.rest.method;

final class StringBinder extends BaseJavaPrimitiveBinder<String> {
	StringBinder() {
	}

	@Override
	protected String doEncode(String theString) {
		return theString;
	}

	@Override
	protected String doParse(String theString) {
		return theString;
	}


}
