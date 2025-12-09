package ca.uhn.fhir.jpa.packages;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NpmPackageUtils {

	/**
	 * Msg structure when an npm package has successfully been added to the cache
	 */
	public static String SUCCESSFULLY_INSTALLED_MSG_TEMPLATE = "Successfully added package %s#%s to registry";

	public static boolean isSuccessfulMsg(String theMsg) {
		return isNotBlank(theMsg) && theMsg.contains("Successfully added package");
	}
}
