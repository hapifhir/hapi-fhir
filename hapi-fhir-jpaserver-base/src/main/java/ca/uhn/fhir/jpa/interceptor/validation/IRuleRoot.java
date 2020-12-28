package ca.uhn.fhir.jpa.interceptor.validation;

import java.util.List;

interface IRuleRoot {

	/**
	 * Starts a new rule that applies to any resources of type {@literal theType}
	 *
	 * @param theType The resource type e.g. "Patient" (must not be null)
	 */
	RepositoryValidatingRuleBuilder.RepositoryValidatingRuleBuilderTyped forResourcesOfType(String theType);

	/**
	 * Create the repository validation rules
	 */
	List<IRepositoryValidatingRule> build();

}
