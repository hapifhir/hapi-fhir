package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderRuleOpClassifierFinished {

	/**
	 * Start another rule
	 */
	IAuthRuleBuilder andThen();
	
}
