package ca.uhn.fhir.rest.server.interceptor.validation.fields;

import java.util.regex.Pattern;

public class EmailValidator implements IValidator {

	private Pattern myPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
		Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isValid(String theString) {
		return myPattern.matcher(theString).matches();
	}
}
