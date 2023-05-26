package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class RuntimeSearchParamHelper {

	public static boolean isResourceLevel(RuntimeSearchParam theSearchParam) {
		return startsWith(theSearchParam.getPath(), "Resource.");
	}
}
