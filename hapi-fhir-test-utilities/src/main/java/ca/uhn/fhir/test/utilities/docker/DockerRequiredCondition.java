/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities.docker;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;


/**
 * Execution condition which will skip test classes that require docker if it is not present on the host machine
 */
public class DockerRequiredCondition implements ExecutionCondition {
	public static final String AVAILABLE_MSG = "Docker is installed so we can run these tests!";
	public static final String UNAVAILABLE_MSG = "It appears as though docker is not installed on the host machine!";
	private static final Logger ourLog = LoggerFactory.getLogger(DockerRequiredCondition.class);
	private static Boolean ourIsDockerAvailable;

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext theExtensionContext) {
		boolean isDockerAvailable;
		isDockerAvailable = isDockerAvailable();
		return isDockerAvailable
			? ConditionEvaluationResult.enabled(AVAILABLE_MSG)
			: ConditionEvaluationResult.disabled(UNAVAILABLE_MSG);
	}

	public static boolean isDockerAvailable() {
		Boolean retVal = ourIsDockerAvailable;
		if (retVal != null) {
			return retVal;
		}
		try {
			retVal = DockerClientFactory.instance().isDockerAvailable();
		} catch (Exception e) {
			retVal = false;
		}
		ourLog.info("Docker available: {}", retVal);
		ourIsDockerAvailable = retVal;
		return retVal;
	}
}
