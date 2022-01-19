package ca.uhn.fhir.i18n;

public final class Msg {
	private static final String ERROR_CODE_PREFIX = "HAPI";

	/**
	 * IMPORTANT: Please update the following comment after you add a new code
	 * Last code value: 2006
	 */

	private Msg() {}

	public static String code(int theCode) {
		return String.format("%s-%04d: ", ERROR_CODE_PREFIX, theCode);
	}
}
