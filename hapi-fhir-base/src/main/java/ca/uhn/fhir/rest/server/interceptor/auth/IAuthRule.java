package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;

public interface IAuthRule {

	/**
	 * Applies the rule and returns a policy decision, or <code>null</code> if the rule does not apply
	 * 
	 * @param theOperation
	 *           The operation type
	 * @param theRequestDetails
	 *           The request
	 * @param theInputResource
	 *           The resource being input by the client, or <code>null</code>
	 * @param theOutputResource
	 *           The resource being returned by the server, or <code>null</code>
	 * @return Returns a policy decision, or <code>null</code> if the rule does not apply
	 */
	RuleModeEnum applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource);

	/**
	 * Returns a name for this rule, to be used in logs and error messages
	 */
	String getName();

}
