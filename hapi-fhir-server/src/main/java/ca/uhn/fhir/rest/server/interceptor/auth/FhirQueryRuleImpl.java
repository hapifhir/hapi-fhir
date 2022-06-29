package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Extension to rules that also requires matching a query filter, e.g. code=foo
 */
public class FhirQueryRuleImpl extends RuleImplOp {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirQueryRuleImpl.class);

	private String myFilter;

	/**
	 * Constructor
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

	/**
	 * Our override that first checks our filter against the resource if present.
	 */
	@Override
	protected AuthorizationInterceptor.Verdict applyRuleLogic(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Set<AuthorizationFlagsEnum> theFlags, FhirContext theFhirContext, RuleTarget theRuleTarget, IRuleApplier theRuleApplier) {
		ourLog.trace("applyRuleLogic {} {}", theOperation, theRuleTarget);
		// Note - theOutputResource == null means we're in some other pointcut and don't have a result yet.
		if (theOutputResource == null) {
			return super.applyRuleLogic(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theFlags, theFhirContext, theRuleTarget, theRuleApplier);
		}

		// look for a matcher
		IAuthorizationSearchParamMatcher matcher = theRuleApplier.getSearchParamMatcher();
		if (matcher == null) {
			theRuleApplier.getTroubleshootingLog().warn("No matcher provided.  Can't apply filter permission.");
			if ( PolicyEnum.DENY.equals(getMode())) {
				return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
			}
			return null;
		}

		// wipjv check in vs out resource - write will need to check write.
		// wipjv what about the id case - does that path doesn't call applyRuleLogic()
		IAuthorizationSearchParamMatcher.MatchResult mr = matcher.match(theOutputResource.fhirType() + "?" + myFilter, theOutputResource);

		AuthorizationInterceptor.Verdict result;
		switch (mr.getMatch()) {
			case MATCH:
				result = super.applyRuleLogic(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theFlags, theFhirContext, theRuleTarget, theRuleApplier);
				break;
			case UNSUPPORTED:
				theRuleApplier.getTroubleshootingLog().warn("Unsupported matcher expression {}: {}.  Abstaining.", myFilter, mr.getUnsupportedReason());
				if ( PolicyEnum.DENY.equals(getMode())) {
					result = new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
				} else {
					result = null;
				}
				break;
			case NO_MATCH:
			default:
				result = null;
				break;
		}
		return result;
	}

	@Nonnull
	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder()
			.append("filter", myFilter);
	}
}
