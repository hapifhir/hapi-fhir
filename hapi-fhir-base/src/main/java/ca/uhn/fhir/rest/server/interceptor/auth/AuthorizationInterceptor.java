package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * This class is a base class for interceptors which can be used to
 * inspect requests and responses to determine whether the calling user
 * has permission to perform the given action.
 */
public class AuthorizationInterceptor extends InterceptorAdapter implements IServerOperationInterceptor {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AuthorizationInterceptor.class);
	
	private PolicyEnum myDefaultPolicy = PolicyEnum.DENY;

	/**
	 * Constructor
	 */
	public AuthorizationInterceptor() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param theDefaultPolicy The default policy if no rules apply (must not be null)
	 */
	public AuthorizationInterceptor(PolicyEnum theDefaultPolicy) {
		this();
		setDefaultPolicy(theDefaultPolicy);
	}

	private void applyRulesAndFailIfDeny(List<IAuthRule> theRules, RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource) {
		ourLog.trace("Applying {} rules to render an auth decision for operation {}", theRules.size(), theOperation);
		Verdict decision = applyRulesAndReturnDecision(theRules, theOperation, theRequestDetails, theInputResource, theOutputResource);
		
		if (decision.getDecision() == PolicyEnum.ALLOW) {
			return;
		}
		
		handleDeny(decision);
	}

	private Verdict applyRulesAndReturnDecision(List<IAuthRule> theRules, RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource) {
		PolicyEnum result = null;
//		PolicyEnum preference = null;
		IAuthRule decidingRule = null;
		
		for (IAuthRule nextRule : theRules) {
			RuleModeEnum decision = nextRule.applyRule(theOperation, theRequestDetails, theInputResource, theOutputResource);
			
			switch (decision) {
			case NO_DECISION:
				continue;
			case ALLOW:
				result = PolicyEnum.ALLOW;
				decidingRule = nextRule;
				break;
			case DENY:
				result = PolicyEnum.DENY;
				decidingRule = nextRule;
				break;
			}
			
			if (result != null) {
				ourLog.trace("Rule {} returned decision {}", nextRule, result);
				break;
			}
		}
		
		if (result == null) {
			ourLog.trace("No rules returned a decision, applying default {}", myDefaultPolicy);
			result = myDefaultPolicy;
		}
		return new Verdict(result, decidingRule);
	}

	private List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		ArrayList<IAuthRule> retVal = new ArrayList<IAuthRule>();
		buildRuleList(theRequestDetails, new RuleBuilder(retVal));
		return retVal;
	}

	/**
	 * Subclasses should override this method to supply the set of rules to be applied to
	 * this individual request.
	 * 
	 * @param theRequestDetails The individual request currently being applied
	 * @param theRuleBuilder The builder used to create the rules 
	 */
	protected void buildRuleList(RequestDetails theRequestDetails, IAuthRuleBuilder theRuleBuilder) {
		// nothing by default
	}
	
	private OperationExamineDirection determineOperationDirection(RestOperationTypeEnum theOperation) {
		switch (theOperation) {
		case ADD_TAGS:
		case DELETE_TAGS:
		case GET_TAGS:
			// These are DSTU1 operations and not relevant
			return OperationExamineDirection.NONE;
			
		case EXTENDED_OPERATION_INSTANCE:
		case EXTENDED_OPERATION_SERVER:
		case EXTENDED_OPERATION_TYPE:
		case METADATA:
			// Security does not apply to these operations 
			return OperationExamineDirection.NONE;
			
		case DELETE:
			// Delete is a special case
			return OperationExamineDirection.NONE;
			
		case CREATE:
		case UPDATE:
			return OperationExamineDirection.IN;

		case META:
		case META_ADD:
		case META_DELETE:
			// meta operations do not apply yet
			return OperationExamineDirection.NONE;
			
		case GET_PAGE:
		case HISTORY_INSTANCE:
		case HISTORY_SYSTEM:
		case HISTORY_TYPE:
		case READ:
		case SEARCH_SYSTEM:
		case SEARCH_TYPE:
		case VREAD:
			return OperationExamineDirection.OUT;
			
		case TRANSACTION:
			return OperationExamineDirection.BOTH;
			
		case VALIDATE:
			// Nothing yet
			return OperationExamineDirection.NONE;
			
		default:
			// Should not happen
			throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

	}

	/**
	 * The default policy if no rules have been found to apply. Default value for this setting is {@link PolicyEnum#DENY}
	 */
	public PolicyEnum getDefaultPolicy() {
		return myDefaultPolicy;
	}

	/**
	 * Handle an access control verdict of {@link PolicyEnum#DENY}.
	 * <p>
	 * Subclasses may override to implement specific behaviour, but default is to 
	 * throw {@link AuthenticationException} (HTTP 401) with error message citing the
	 * rule name which trigered failure
	 * </p>
	 */
	protected void handleDeny(Verdict decision) {
		if (decision.getDecidingRule() != null) {
			String ruleName = defaultString(decision.getDecidingRule().getName(), "(unnamed rule)");
			throw new AuthenticationException("Access denied by rule: " + ruleName);
		} else {
			throw new AuthenticationException("Access denied by default policy (no applicable rules)");
		}
	}
	
	private void handleUserOperation(RequestDetails theRequest, IBaseResource theResource, RestOperationTypeEnum operation) {
		List<IAuthRule> rules = buildRuleList(theRequest);
		applyRulesAndFailIfDeny(rules, operation, theRequest, theResource, null);
	}
	
	@Override
	public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
		switch (determineOperationDirection(theOperation)) {
		case IN:
		case BOTH:
			break;
		case NONE:
		case OUT:
			return;
		}

		RequestDetails requestDetails = theProcessedRequest.getRequestDetails();
		List<IAuthRule> rules = buildRuleList(requestDetails);
		applyRulesAndFailIfDeny(rules, theOperation, requestDetails, theProcessedRequest.getResource(), null);
	}
	
	@Override
	@CoverageIgnore
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theBundle) {
		throw failForDstu1();
	}

	@Override
	@CoverageIgnore
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		throw failForDstu1();
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		switch (determineOperationDirection(theRequestDetails.getRestOperationType())) {
		case IN:
		case NONE:
			return true;
		case BOTH:
		case OUT:
			break;
		}

		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();
		List<IAuthRule> rules = buildRuleList(theRequestDetails);
		
		List<IBaseResource> resources = Collections.emptyList();
		
		switch (theRequestDetails.getRestOperationType()) {
		case SEARCH_SYSTEM:
		case SEARCH_TYPE:
		case HISTORY_INSTANCE:
		case HISTORY_SYSTEM:
		case HISTORY_TYPE:
		case TRANSACTION:{
			if (theResponseObject != null) {
				IBaseBundle responseBundle = (IBaseBundle) theResponseObject;
				resources = toListOfResources(fhirContext, responseBundle);
			}
			break;
		}
		default: {
			if (theResponseObject != null) {
				resources = Collections.singletonList(theResponseObject);
			}
			break;
		}
		}
		
		for (IBaseResource nextResponse : resources) {
			applyRulesAndFailIfDeny(rules, theRequestDetails.getRestOperationType(), theRequestDetails, null, nextResponse);
		}

		return true;
	}

	@CoverageIgnore
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject) {
		throw failForDstu1();
	}

	@CoverageIgnore
	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		throw failForDstu1();
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		handleUserOperation(theRequest, theResource, RestOperationTypeEnum.CREATE);
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		handleUserOperation(theRequest, theResource, RestOperationTypeEnum.DELETE);
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theResource) {
		handleUserOperation(theRequest, theResource, RestOperationTypeEnum.UPDATE);
	}

	/**
	 * The default policy if no rules have been found to apply. Default value for this setting is {@link PolicyEnum#DENY}
	 * 
	 * @param theDefaultPolicy The policy (must not be <code>null</code>)
	 */
	public void setDefaultPolicy(PolicyEnum theDefaultPolicy) {
		Validate.notNull(theDefaultPolicy, "theDefaultPolicy must not be null");
		myDefaultPolicy = theDefaultPolicy;
	}

	private List<IBaseResource> toListOfResources(FhirContext fhirContext, IBaseBundle responseBundle) {
		List<IBaseResource> retVal = BundleUtil.toListOfResources(fhirContext, responseBundle);
		for (int i = 0; i < retVal.size(); i++) {
			IBaseResource nextResource = retVal.get(i);
			if (nextResource instanceof IBaseBundle) {
				retVal.addAll(BundleUtil.toListOfResources(fhirContext, (IBaseBundle) nextResource));
				retVal.remove(i);
				i--;
			}
		}
		return retVal;
	}

	private static UnsupportedOperationException failForDstu1() {
		return new UnsupportedOperationException("Use of this interceptor on DSTU1 servers is not supportd");
	}

	private enum OperationExamineDirection {
		IN,
		NONE,
		OUT, 
		BOTH
	}

	public static class Verdict {

		private final IAuthRule myDecidingRule;
		private final PolicyEnum myDecision;

		public Verdict(PolicyEnum theDecision, IAuthRule theDecidingRule) {
			myDecision = theDecision;
			myDecidingRule = theDecidingRule;
		}

		public IAuthRule getDecidingRule() {
			return myDecidingRule;
		}

		public PolicyEnum getDecision() {
			return myDecision;
		}
		
	}
	
}
