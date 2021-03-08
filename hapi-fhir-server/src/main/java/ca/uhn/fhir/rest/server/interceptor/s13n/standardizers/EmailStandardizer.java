package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/**
 * Standardizes email addresses by removing whitespace, ISO control characters and applying lower-case to the values.
 */
public class EmailStandardizer implements IStandardizer {

	@Override
	public String standardize(String theString) {
		StringBuilder buf = new StringBuilder();
		for (int offset = 0; offset < theString.length(); ) {
			int codePoint = theString.codePointAt(offset);
			offset += Character.charCount(codePoint);

			if (Character.isISOControl(codePoint)) {
				continue;
			}

			if (!Character.isWhitespace(codePoint)) {
				buf.append(new String(Character.toChars(codePoint)).toLowerCase());
			}
		}
		return buf.toString();
	}
}
