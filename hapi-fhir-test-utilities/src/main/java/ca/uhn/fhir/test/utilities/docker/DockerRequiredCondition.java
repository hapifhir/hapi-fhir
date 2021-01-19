package ca.uhn.fhir.test.utilities.docker;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;


/**
 * Execution condition which will skip test classes that require docker if it is not present on the host machine
 */
public class DockerRequiredCondition implements ExecutionCondition {

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext theExtensionContext) {
		try {
			new ElasticsearchContainer();
			return ConditionEvaluationResult.enabled("Docker is installed so we can run these tests!");
		} catch (Exception e) {
			return ConditionEvaluationResult.disabled("It appears as though docker is not installed on the host machine!");
		}
	}
}
