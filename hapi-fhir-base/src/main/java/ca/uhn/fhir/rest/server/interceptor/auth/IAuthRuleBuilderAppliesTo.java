package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IAuthRuleBuilderAppliesTo<T> {

	/**
	 * Rule applies to resources of the given type
	 */
	T resourcesOfType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to all resources
	 */
	T allResources();

}
