package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;

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

	public static boolean isAppliesToAnyType(IAuthRule theRule) {
		return ((OperationRule)theRule).isAppliesToAnyType();
	}

	public static String getGroupId(IAuthRule theRule) {
		return ((RuleBulkExportImpl)theRule).getGroupId();
	}

	public static BulkDataExportOptions.ExportStyle getWantExportStyle(IAuthRule theRule) {
		return ((RuleBulkExportImpl)theRule).getWantExportStyle();
	}
}
