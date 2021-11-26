package ca.uhn.fhir.jpa.subscription.log;

import ca.uhn.fhir.i18n.ModuleErrorCodeEnum;
import ca.uhn.fhir.i18n.HapiErrorCode;

public final class Msg {
	private Msg() {}

	public static String code(int theCode) {
		return HapiErrorCode.code(ModuleErrorCodeEnum.SUBS, theCode);
	}
}
