package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IAuthRuleBuilderOperationNamed {

	/**
	 * Rule applies to invocations of this operation at the <code>server</code> level
	 */
	IAuthRuleBuilderRuleOpClassifierFinished onServer();

	/**
	 * Rule applies to invocations of this operation at the <code>type</code> level
	 */
	IAuthRuleBuilderRuleOpClassifierFinished onType(Class<? extends IBaseResource> theType);

	/**
	 * Rule applies to invocations of this operation at the <code>instance</code> level
	 */
	IAuthRuleBuilderRuleOpClassifierFinished onInstance(IIdType theInstanceId);

}
