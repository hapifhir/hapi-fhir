package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderOperationNamedAndScoped {

	/**
	 * Responses for this operation will not be checked
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponses();

	/**
	 * Responses for this operation must be authorized by other rules. For example, if this
	 * rule is authorizing the Patient $everything operation, there must be a separate
	 * rule (or rules) that actually authorize the user to read the
	 * resources being returned
	 */
	IAuthRuleBuilderRuleOpClassifierFinished andRequireExplicitResponseAuthorization();


}
