package ca.uhn.fhir.rest.server.interceptor.auth;

public interface IAuthRuleBuilderGraphQL {

	/**
	 * Note that this is an all-or-nothing grant for now, it
	 * is not yet possible to specify individual resource security when
	 * using GraphQL.
	 */
	IAuthRuleFinished any();
}
