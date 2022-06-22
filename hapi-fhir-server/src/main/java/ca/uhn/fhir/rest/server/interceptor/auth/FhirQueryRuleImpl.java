package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class FhirQueryRuleImpl extends RuleImplOp {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirQueryRuleImpl.class);

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
		ourLog.trace("applyRuleLogic {} {}", theOperation, theRuleTarget);
		if (theOutputResource == null) {
			return super.applyRuleLogic(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theFlags, theFhirContext, theRuleTarget, theRuleApplier);
		}
		IAuthorizationSearchParamMatcher matcher = theRuleApplier.getSearchParamMatcher();
		if (matcher == null) {
			ourLog.warn("No matcher provided.  Can't apply filter permission.");
			if ( PolicyEnum.DENY.equals(getMode())) {
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
			return null;
		}

		// fixme myFilter needs to turn into a proper FHIR expression
		// fixme check in vs out resource
		// fixme has the logic already considered theOutputResource.fhirType() vs myTypes?
		IAuthorizationSearchParamMatcher.MatchResult mr = matcher.match(theOutputResource.fhirType() + "?" + myFilter, theOutputResource);

		AuthorizationInterceptor.Verdict result;
		switch (mr.getMatch()) {
			case MATCH:
				result = this.newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
				break;
			case UNSUPPORTED:
				// fixme log a warning to the troubleshooting log
				// fixme if deny mode, we should deny here.
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
