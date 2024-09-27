package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public final class DatabaseSupportUtil {

	private DatabaseSupportUtil() {}

	public static boolean canUseMsSql2019() {
		return isSupportAmd64Architecture();
	}

	public static boolean canUseOracle() {
		return isSupportAmd64Architecture();
	}

	private static boolean isSupportAmd64Architecture() {
		if (!isMac()) {
			return true;
		}
		return isColimaConfigured();
	}

	private static boolean isMac() {
		return SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX;
	}

	private static boolean isColimaConfigured() {
		return StringUtils.isNotBlank(System.getenv("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"))
				&& StringUtils.isNotBlank(System.getenv("DOCKER_HOST"))
				&& System.getenv("DOCKER_HOST").contains("colima");
	}
}
