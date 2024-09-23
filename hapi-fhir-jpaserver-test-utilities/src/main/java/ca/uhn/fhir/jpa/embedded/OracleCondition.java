/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.embedded;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class OracleCondition implements ExecutionCondition {

	public static final String ENABLED_MSG = "Environment is able to run Oracle using TestContainers.";
	public static final String DISABLED_MSG = "Environment is not able to run Oracle using TestContainers. If you "
			+ "are a Mac user, please ensure Colima is running. See: https://java.testcontainers.org/supported_docker_environment/#using-colima.";

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext theExtensionContext) {
		return canUseOracle()
				? ConditionEvaluationResult.enabled(ENABLED_MSG)
				: ConditionEvaluationResult.disabled(DISABLED_MSG);
	}

	public static boolean canUseOracle() {
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
