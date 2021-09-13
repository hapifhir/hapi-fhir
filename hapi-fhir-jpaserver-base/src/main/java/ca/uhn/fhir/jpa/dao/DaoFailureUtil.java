package ca.uhn.fhir.jpa.dao;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to help identify classes of failure.
 */
public class DaoFailureUtil {

	public static boolean isTagStorageFailure(Throwable t) {
		if (StringUtils.isBlank(t.getMessage())) {
			return false;
		} else {
			String msg = t.getMessage().toLowerCase();
			return msg.contains("hfj_tag_def") || msg.contains("hfj_res_tag");
		}
	}
}
