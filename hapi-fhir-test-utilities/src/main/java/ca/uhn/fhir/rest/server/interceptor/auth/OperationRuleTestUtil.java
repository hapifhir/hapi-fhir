package ca.uhn.fhir.rest.server.interceptor.auth;

public final class OperationRuleTestUtil {
	private OperationRuleTestUtil() {}

	public static String getOperationName(IAuthRule theRule) {
		return ((OperationRule)theRule).getOperationName();
	}

	public static boolean isAppliesToServer(IAuthRule theRule) {
		return ((OperationRule)theRule).isAppliesToServer();
	}

	public static boolean isAllowAllResponses(IAuthRule theRule) {
		return ((OperationRule)theRule).isAllowAllResponses();
	}
}
