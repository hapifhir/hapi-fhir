package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderOperation {

	/**
	 * This rule applies to the operation with the given name
	 * 
	 * @param The operation name, e.g. "validate" or "$validate" (either form may be used here)
	 */
	IAuthRuleBuilderOperationNamed named(String theOperationName);

	/**
	 * This rule applies to any operation
	 */
	IAuthRuleBuilderOperationNamed withAnyName();

}
