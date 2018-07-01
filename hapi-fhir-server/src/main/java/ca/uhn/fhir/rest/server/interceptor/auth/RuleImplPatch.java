package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

class RuleImplPatch extends BaseRule {
	private boolean myAllRequests;

	RuleImplPatch(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags) {
		if (isOtherTenant(theRequestDetails)) {
			return null;
		}

		if (myAllRequests) {
			if (theOperation == RestOperationTypeEnum.PATCH) {
				if (theInputResource == null && theOutputResource == null) {
					return newVerdict();
				}
			}
		}

		return null;
	}

	RuleImplPatch setAllRequests(boolean theAllRequests) {
		myAllRequests = theAllRequests;
		return this;
	}
}
