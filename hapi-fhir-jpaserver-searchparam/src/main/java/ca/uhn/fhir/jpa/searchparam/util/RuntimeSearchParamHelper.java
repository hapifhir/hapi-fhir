package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class RuntimeSearchParamHelper {

	/**
	 * Helper function to determine if a RuntimeSearchParam is a resource level search param.
	 *
	 * @param theSearchParam the parameter to check
	 * @return return boolean
	 */
	public static boolean isResourceLevel(RuntimeSearchParam theSearchParam) {
		return startsWith(theSearchParam.getPath(), "Resource.");
	}
}
