package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.List;

public interface IAuthRuleFinished {

	/**
	 * Start another rule
	 */
	IAuthRuleBuilder andThen();

	/**
	 * Build the rule list
	 */
	List<IAuthRule> build();

}
