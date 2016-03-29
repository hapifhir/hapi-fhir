package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderRuleTransaction {

	/**
	 * This rules applies to transactions with any operation nested
	 * in them.
	 * <p>
	 * This call completes the rule and adds the rule to the chain.
	 * </p>
	 */
	IAuthRuleBuilderRuleOpClassifierFinished withAnyOperation();
}
