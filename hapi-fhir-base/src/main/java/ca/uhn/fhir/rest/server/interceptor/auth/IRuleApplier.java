package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;

public interface IRuleApplier {

	Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource);

}
