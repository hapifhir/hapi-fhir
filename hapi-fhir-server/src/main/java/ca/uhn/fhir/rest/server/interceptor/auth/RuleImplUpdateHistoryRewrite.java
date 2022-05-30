package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public class RuleImplUpdateHistoryRewrite extends BaseRule {

	private boolean myAllRequests;

	RuleImplUpdateHistoryRewrite(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource,
																	  IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {
		if (myAllRequests) {
			if (theRequestDetails.getId() != null && theRequestDetails.getId().hasVersionIdPart() && theOperation == RestOperationTypeEnum.UPDATE) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}
		}

		return null;
	}

	RuleImplUpdateHistoryRewrite setAllRequests(boolean theAllRequests) {
		myAllRequests = theAllRequests;
		return this;
	}
}
