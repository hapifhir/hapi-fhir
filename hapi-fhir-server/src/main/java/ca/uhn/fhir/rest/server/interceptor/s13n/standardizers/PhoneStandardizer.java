package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/**
 * Standardizes phone numbers to fit 123-456-7890 patter.
 */
public class PhoneStandardizer implements IStandardizer {

	public static final String PHONE_NUMBER_PATTERN = "(\\d{3})(\\d{3})(\\d+)";
	public static final String PHONE_NUMBER_REPLACE_PATTERN = "$1-$2-$3";

	@Override
	public String standardize(String thePhone) {
		StringBuilder buf = new StringBuilder(thePhone.length());
		for (char ch : thePhone.toCharArray()) {
			if (Character.isDigit(ch)) {
				buf.append(ch);
			}
		}
		return buf.toString().replaceFirst(PHONE_NUMBER_PATTERN, PHONE_NUMBER_REPLACE_PATTERN);
	}

}
