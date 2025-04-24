/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
