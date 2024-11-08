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
