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
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.util.DatabaseSupportUtil;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class OracleCondition implements ExecutionCondition {

	public static final String ENABLED_MSG = "Environment is able to run Oracle using TestContainers.";
	public static final String DISABLED_MSG = "Environment is not able to run Oracle using TestContainers. If you "
			+ "are a Mac user, please ensure Colima is running. See: https://java.testcontainers.org/supported_docker_environment/#using-colima.";

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext theExtensionContext) {
		return DatabaseSupportUtil.canUseOracle()
				? ConditionEvaluationResult.enabled(ENABLED_MSG)
				: ConditionEvaluationResult.disabled(DISABLED_MSG);
	}
}
