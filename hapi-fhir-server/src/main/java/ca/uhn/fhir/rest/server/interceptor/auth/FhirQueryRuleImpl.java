package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public class FhirQueryRuleImpl extends RuleImplOp {
	private String myFilter;

	private IAuthorizationSearchParamMatcher myAuthorizationSearchParamMatcher;


	/**
	 * Constructor
	 *
	 * @param theRuleName
	 */
	public FhirQueryRuleImpl(String theRuleName) {
		super(theRuleName);
	}

	public void setFilter(String theFilter) {
		myFilter = theFilter;
	}

	public String getFilter() {
		return myFilter;
	}

	@Override
	protected AuthorizationInterceptor.Verdict applyRuleLogic(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Set<AuthorizationFlagsEnum> theFlags, FhirContext theFhirContext, RuleTarget theRuleTarget, IRuleApplier theRuleApplier) {
		IAuthorizationSearchParamMatcher matcher = theRuleApplier.getSearchParamMatcher();
		if (matcher == null) {
			 return null;
		}

		// fixme myFilter needs to turn into a proper FHIR expression
		// fixme check in vs out resource
		IAuthorizationSearchParamMatcher.MatchResult mr = matcher.match(myFilter, theOutputResource);

		AuthorizationInterceptor.Verdict result;
		switch (mr.getMatch()) {
			case MATCH:
				result = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
				break;
			case UNSUPPORTED:
				// fixme log a warning to the troubleshooting log
				theRuleApplier.getTroubleshootingLog().warn("Unsupported matcher expression {}: {}.  Abstaining.", myFilter, mr.getUnsupportedReason());
				result = null;
				break;
			case NO_MATCH:
			default:
				result = null;
				break;
		}
		return result;
	}
}
