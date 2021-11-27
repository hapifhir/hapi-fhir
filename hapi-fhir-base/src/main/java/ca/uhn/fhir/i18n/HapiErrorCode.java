package ca.uhn.fhir.i18n;

public final class HapiErrorCode {
	private static final String ERROR_CODE_PREFIX = "HAPI";

	private HapiErrorCode() {}

	public static String code(ModuleErrorCodeEnum moduleErrorCode, int theCode) {
		return String.join("-", ERROR_CODE_PREFIX, moduleErrorCode.toString(), String.format("%03d", theCode)) + ": ";
	}

}

