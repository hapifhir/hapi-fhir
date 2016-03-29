package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderRule {

	/**
	 * This rule applies to any FHIR operation involving reading, including
	 * <code>read</code>, <code>vread</code>, <code>search</code>, and
	 * <code>history</code>
	 */
	IAuthRuleBuilderRuleOp read();

	/**
	 * This rule applies to any FHIR operation involving writing, including
	 * <code>create</code>, and <code>update</code>
	 */
	IAuthRuleBuilderRuleOp write();

	/**
	 * This rule applies to the FHIR transaction operation. Transaction is a special
	 * case in that it bundles other operations
	 */
	IAuthRuleBuilderRuleTransaction transaction();

}
