package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.util.CoverageIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * This class is a base class for interceptors which can be used to
 * inspect requests and responses to determine whether the calling user
 * has permission to perform the given action.
 * <p>
 * See the HAPI FHIR
 * <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_server_security.html">Documentation on Server Security</a>
 * for information on how to use this interceptor.
 * </p>
 *
 * @see SearchNarrowingInterceptor
 */
public class AuthorizationInterceptor extends ServerOperationInterceptorAdapter implements IRuleApplier {

	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptor.class);

	private PolicyEnum myDefaultPolicy = PolicyEnum.DENY;
	private Set<AuthorizationFlagsEnum> myFlags = Collections.emptySet();

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

	private void applyRulesAndFailIfDeny(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId,
													 IBaseResource theOutputResource) {
		Verdict decision = applyRulesAndReturnDecision(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);

		if (decision.getDecision() == PolicyEnum.ALLOW) {
			return;
		}

		handleDeny(theRequestDetails, decision);
	}

	@Override
	public Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId,
															 IBaseResource theOutputResource) {
		List<IAuthRule> rules = buildRuleList(theRequestDetails);
		Set<AuthorizationFlagsEnum> flags = getFlags();
		ourLog.trace("Applying {} rules to render an auth decision for operation {}", rules.size(), theOperation);

		Verdict verdict = null;
		for (IAuthRule nextRule : rules) {
			verdict = nextRule.applyRule(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, this, flags);
			if (verdict != null) {
				ourLog.trace("Rule {} returned decision {}", nextRule, verdict.getDecision());
				break;
			}
		}

		if (verdict == null) {
			ourLog.trace("No rules returned a decision, applying default {}", myDefaultPolicy);
			return new Verdict(getDefaultPolicy(), null);
		}

		return verdict;
	}

	/**
	 * Subclasses should override this method to supply the set of rules to be applied to
	 * this individual request.
	 * <p>
	 * Typically this is done by examining <code>theRequestDetails</code> to find
	 * out who the current user is and then using a {@link RuleBuilder} to create
	 * an appropriate rule chain.
	 * </p>
	 *
	 * @param theRequestDetails The individual request currently being applied
	 */
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		return new ArrayList<>();
	}

	private OperationExamineDirection determineOperationDirection(RestOperationTypeEnum theOperation, IBaseResource theRequestResource) {
		switch (theOperation) {
			case ADD_TAGS:
			case DELETE_TAGS:
			case GET_TAGS:
				// These are DSTU1 operations and not relevant
				return OperationExamineDirection.NONE;

			case EXTENDED_OPERATION_INSTANCE:
			case EXTENDED_OPERATION_SERVER:
			case EXTENDED_OPERATION_TYPE:
				return OperationExamineDirection.BOTH;

			case METADATA:
				// Security does not apply to these operations
				return OperationExamineDirection.IN;

			case DELETE:
				// Delete is a special case
				return OperationExamineDirection.NONE;

			case CREATE:
			case UPDATE:
			case PATCH:
				// if (theRequestResource != null) {
				// if (theRequestResource.getIdElement() != null) {
				// if (theRequestResource.getIdElement().hasIdPart() == false) {
				// return OperationExamineDirection.IN_UNCATEGORIZED;
				// }
				// }
				// }
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

			case GRAPHQL_REQUEST:
				return OperationExamineDirection.IN;

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
	 * The default policy if no rules have been found to apply. Default value for this setting is {@link PolicyEnum#DENY}
	 *
	 * @param theDefaultPolicy The policy (must not be <code>null</code>)
	 */
	public void setDefaultPolicy(PolicyEnum theDefaultPolicy) {
		Validate.notNull(theDefaultPolicy, "theDefaultPolicy must not be null");
		myDefaultPolicy = theDefaultPolicy;
	}

	/**
	 * This property configures any flags affecting how authorization is
	 * applied. By default no flags are applied.
	 *
	 * @see #setFlags(Collection)
	 */
	public Set<AuthorizationFlagsEnum> getFlags() {
		return Collections.unmodifiableSet(myFlags);
	}

	/**
	 * This property configures any flags affecting how authorization is
	 * applied. By default no flags are applied.
	 *
	 * @param theFlags The flags (must not be null)
	 * @see #setFlags(AuthorizationFlagsEnum...)
	 */
	public AuthorizationInterceptor setFlags(Collection<AuthorizationFlagsEnum> theFlags) {
		Validate.notNull(theFlags, "theFlags must not be null");
		myFlags = new HashSet<>(theFlags);
		return this;
	}

	/**
	 * This property configures any flags affecting how authorization is
	 * applied. By default no flags are applied.
	 *
	 * @param theFlags The flags (must not be null)
	 * @see #setFlags(Collection)
	 */
	public AuthorizationInterceptor setFlags(AuthorizationFlagsEnum... theFlags) {
		Validate.notNull(theFlags, "theFlags must not be null");
		return setFlags(Lists.newArrayList(theFlags));
	}

	/**
	 * Handle an access control verdict of {@link PolicyEnum#DENY}.
	 * <p>
	 * Subclasses may override to implement specific behaviour, but default is to
	 * throw {@link ForbiddenOperationException} (HTTP 403) with error message citing the
	 * rule name which trigered failure
	 * </p>
	 *
	 * @since HAPI FHIR 3.6.0
	 */
	protected void handleDeny(RequestDetails theRequestDetails, Verdict decision) {
		handleDeny(decision);
	}

	/**
	 * This method should not be overridden. As of HAPI FHIR 3.6.0, you
	 * should override {@link #handleDeny(RequestDetails, Verdict)} instead. This
	 * method will be removed in the future.
	 */
	protected void handleDeny(Verdict decision) {
		if (decision.getDecidingRule() != null) {
			String ruleName = defaultString(decision.getDecidingRule().getName(), "(unnamed rule)");
			throw new ForbiddenOperationException("Access denied by rule: " + ruleName);
		}
		throw new ForbiddenOperationException("Access denied by default policy (no applicable rules)");
	}

	private void handleUserOperation(RequestDetails theRequest, IBaseResource theResource, RestOperationTypeEnum operation) {
		applyRulesAndFailIfDeny(operation, theRequest, theResource, theResource.getIdElement(), null);
	}

	@Override
	public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
		IBaseResource inputResource = null;
		IIdType inputResourceId = null;

		switch (determineOperationDirection(theOperation, theProcessedRequest.getResource())) {
			case IN:
			case BOTH:
				inputResource = theProcessedRequest.getResource();
				inputResourceId = theProcessedRequest.getId();
				break;
			case OUT:
				// inputResource = null;
				inputResourceId = theProcessedRequest.getId();
				break;
			case NONE:
				return;
		}

		RequestDetails requestDetails = theProcessedRequest.getRequestDetails();
		applyRulesAndFailIfDeny(theOperation, requestDetails, inputResource, inputResourceId, null);
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		switch (determineOperationDirection(theRequestDetails.getRestOperationType(), null)) {
			case IN:
			case NONE:
				return true;
			case BOTH:
			case OUT:
				break;
		}

		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();
		List<IBaseResource> resources = Collections.emptyList();

		switch (theRequestDetails.getRestOperationType()) {
			case SEARCH_SYSTEM:
			case SEARCH_TYPE:
			case HISTORY_INSTANCE:
			case HISTORY_SYSTEM:
			case HISTORY_TYPE:
			case TRANSACTION:
			case GET_PAGE:
			case EXTENDED_OPERATION_SERVER:
			case EXTENDED_OPERATION_TYPE:
			case EXTENDED_OPERATION_INSTANCE: {
				if (theResponseObject != null) {
					resources = toListOfResourcesAndExcludeContainer(theResponseObject, fhirContext);
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
			applyRulesAndFailIfDeny(theRequestDetails.getRestOperationType(), theRequestDetails, null, null, nextResponse);
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
	public boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws AuthenticationException {
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
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		if (theOldResource != null) {
			handleUserOperation(theRequest, theOldResource, RestOperationTypeEnum.UPDATE);
		}
		handleUserOperation(theRequest, theNewResource, RestOperationTypeEnum.UPDATE);
	}

	private enum OperationExamineDirection {
		BOTH,
		IN,
		NONE,
		OUT,
	}

	public static class Verdict {

		private final IAuthRule myDecidingRule;
		private final PolicyEnum myDecision;

		Verdict(PolicyEnum theDecision, IAuthRule theDecidingRule) {
			Validate.notNull(theDecision);

			myDecision = theDecision;
			myDecidingRule = theDecidingRule;
		}

		public IAuthRule getDecidingRule() {
			return myDecidingRule;
		}

		public PolicyEnum getDecision() {
			return myDecision;
		}

		@Override
		public String toString() {
			ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
			String ruleName;
			if (myDecidingRule != null) {
				ruleName = myDecidingRule.getName();
			} else {
				ruleName = "(none)";
			}
			b.append("rule", ruleName);
			b.append("decision", myDecision.name());
			return b.build();
		}

	}

	private static UnsupportedOperationException failForDstu1() {
		return new UnsupportedOperationException("Use of this interceptor on DSTU1 servers is not supportd");
	}

	static List<IBaseResource> toListOfResourcesAndExcludeContainer(IBaseResource theResponseObject, FhirContext fhirContext) {
		if (theResponseObject == null) {
			return Collections.emptyList();
		}

		List<IBaseResource> retVal;

		boolean isContainer = false;
		if (theResponseObject instanceof IBaseBundle) {
			isContainer = true;
		} else if (theResponseObject instanceof IBaseParameters) {
			isContainer = true;
		}

		if (!isContainer) {
			return Collections.singletonList(theResponseObject);
		}

		retVal = fhirContext.newTerser().getAllPopulatedChildElementsOfType(theResponseObject, IBaseResource.class);

		// Exclude the container
		if (retVal.size() > 0 && retVal.get(0) == theResponseObject) {
			retVal = retVal.subList(1, retVal.size());
		}

		return retVal;
	}

}
