package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is a base class for interceptors which can be used to
 * inspect requests and responses to determine whether the calling user
 * has permission to perform the given action.
 * <p>
 * See the HAPI FHIR
 * <a href="https://hapifhir.io/hapi-fhir/docs/security/introduction.html">Documentation on Server Security</a>
 * for information on how to use this interceptor.
 * </p>
 *
 * @see SearchNarrowingInterceptor
 */
@Interceptor(order = AuthorizationConstants.ORDER_AUTH_INTERCEPTOR)
public class AuthorizationInterceptor implements IRuleApplier {

	public static final String REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS = AuthorizationInterceptor.class.getName() + "_BulkDataExportOptions";
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	private final int myInstanceIndex = ourInstanceCount.incrementAndGet();
	private final String myRequestSeenResourcesKey = AuthorizationInterceptor.class.getName() + "_" + myInstanceIndex + "_SEENRESOURCES";
	private final String myRequestRuleListKey = AuthorizationInterceptor.class.getName() + "_" + myInstanceIndex + "_RULELIST";
	private PolicyEnum myDefaultPolicy = PolicyEnum.DENY;
	private Set<AuthorizationFlagsEnum> myFlags = Collections.emptySet();
	private IValidationSupport myValidationSupport;
	private Logger myTroubleshootingLog;

	/**
	 * Constructor
	 */
	public AuthorizationInterceptor() {
		super();
		setTroubleshootingLog(ourLog);
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

	@Nonnull
	@Override
	public Logger getTroubleshootingLog() {
		return myTroubleshootingLog;
	}

	public void setTroubleshootingLog(@Nonnull Logger theTroubleshootingLog) {
		Validate.notNull(theTroubleshootingLog, "theTroubleshootingLog must not be null");
		myTroubleshootingLog = theTroubleshootingLog;
	}

	private void applyRulesAndFailIfDeny(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId,
													 IBaseResource theOutputResource, Pointcut thePointcut) {
		Verdict decision = applyRulesAndReturnDecision(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, thePointcut);

		if (decision.getDecision() == PolicyEnum.ALLOW) {
			return;
		}

		handleDeny(theRequestDetails, decision);
	}

	@Override
	public Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId,
															 IBaseResource theOutputResource, Pointcut thePointcut) {
		@SuppressWarnings("unchecked")
		List<IAuthRule> rules = (List<IAuthRule>) theRequestDetails.getUserData().get(myRequestRuleListKey);
		if (rules == null) {
			rules = buildRuleList(theRequestDetails);
			theRequestDetails.getUserData().put(myRequestRuleListKey, rules);
		}
		Set<AuthorizationFlagsEnum> flags = getFlags();
		ourLog.trace("Applying {} rules to render an auth decision for operation {}, theInputResource type={}, theOutputResource type={} ", rules.size(), theOperation,
			((theInputResource != null) && (theInputResource.getIdElement() != null)) ? theInputResource.getIdElement().getResourceType() : "",
			((theOutputResource != null) && (theOutputResource.getIdElement() != null)) ? theOutputResource.getIdElement().getResourceType() : "");

		Verdict verdict = null;
		for (IAuthRule nextRule : rules) {
			ourLog.trace("Rule being applied - {}", nextRule);
			verdict = nextRule.applyRule(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, this, flags, thePointcut);
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
	 * @since 6.0.0
	 */
	@Nullable
	@Override
	public IValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

	/**
	 * Sets a validation support module that will be used for terminology-based rules
	 *
	 * @param theValidationSupport The validation support. Null is also acceptable (this is the default),
	 *                             in which case the validation support module associated with the {@link FhirContext}
	 *                             will be used.
	 * @since 6.0.0
	 */
	public AuthorizationInterceptor setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
		return this;
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
				return OperationExamineDirection.IN;

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
				return OperationExamineDirection.BOTH;

			default:
				// Should not happen
				throw new IllegalStateException(Msg.code(332) + "Unable to apply security to event of type " + theOperation);
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
	public AuthorizationInterceptor setDefaultPolicy(PolicyEnum theDefaultPolicy) {
		Validate.notNull(theDefaultPolicy, "theDefaultPolicy must not be null");
		myDefaultPolicy = theDefaultPolicy;
		return this;
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
			throw new ForbiddenOperationException(Msg.code(333) + "Access denied by rule: " + ruleName);
		}
		throw new ForbiddenOperationException(Msg.code(334) + "Access denied by default policy (no applicable rules)");
	}

	private void handleUserOperation(RequestDetails theRequest, IBaseResource theResource, RestOperationTypeEnum theOperation, Pointcut thePointcut) {
		applyRulesAndFailIfDeny(theOperation, theRequest, theResource, theResource.getIdElement(), null, thePointcut);
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void incomingRequestPreHandled(RequestDetails theRequest, Pointcut thePointcut) {
		IBaseResource inputResource = null;
		IIdType inputResourceId = null;

		switch (determineOperationDirection(theRequest.getRestOperationType(), theRequest.getResource())) {
			case IN:
			case BOTH:
				inputResource = theRequest.getResource();
				inputResourceId = theRequest.getId();
				if (inputResourceId == null && isNotBlank(theRequest.getResourceName())) {
					inputResourceId = theRequest.getFhirContext().getVersion().newIdType();
					inputResourceId.setParts(null, theRequest.getResourceName(), null, null);
				}
				break;
			case OUT:
				// inputResource = null;
				inputResourceId = theRequest.getId();
				break;
			case NONE:
				return;
		}

		applyRulesAndFailIfDeny(theRequest.getRestOperationType(), theRequest, inputResource, inputResourceId, null, thePointcut);
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void hookPreShow(RequestDetails theRequestDetails, IPreResourceShowDetails theDetails, Pointcut thePointcut) {
		for (int i = 0; i < theDetails.size(); i++) {
			IBaseResource next = theDetails.getResource(i);
			checkOutgoingResourceAndFailIfDeny(theRequestDetails, next, thePointcut);
		}
	}

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void hookOutgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, Pointcut thePointcut) {
		checkOutgoingResourceAndFailIfDeny(theRequestDetails, theResponseObject, thePointcut);
	}

	@Hook(Pointcut.STORAGE_CASCADE_DELETE)
	public void hookCascadeDeleteForConflict(RequestDetails theRequestDetails, Pointcut thePointcut, IBaseResource theResourceToDelete) {
		Validate.notNull(theResourceToDelete); // just in case
		checkPointcutAndFailIfDeny(theRequestDetails, thePointcut, theResourceToDelete);
	}

	@Hook(Pointcut.STORAGE_PRE_DELETE_EXPUNGE)
	public void hookDeleteExpunge(RequestDetails theRequestDetails, Pointcut thePointcut) {
		applyRulesAndFailIfDeny(theRequestDetails.getRestOperationType(), theRequestDetails, null, null, null, thePointcut);
	}

	@Hook(Pointcut.STORAGE_INITIATE_BULK_EXPORT)
	public void initiateBulkExport(RequestDetails theRequestDetails, BulkDataExportOptions theBulkExportOptions, Pointcut thePointcut) {
		RestOperationTypeEnum restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		if (theRequestDetails != null) {
			theRequestDetails.setAttribute(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS, theBulkExportOptions);
		}
		applyRulesAndFailIfDeny(restOperationType, theRequestDetails, null, null, null, thePointcut);
	}

	private void checkPointcutAndFailIfDeny(RequestDetails theRequestDetails, Pointcut thePointcut, @Nonnull IBaseResource theInputResource) {
		applyRulesAndFailIfDeny(theRequestDetails.getRestOperationType(), theRequestDetails, theInputResource, theInputResource.getIdElement(), null, thePointcut);
	}

	private void checkOutgoingResourceAndFailIfDeny(RequestDetails theRequestDetails, IBaseResource theResponseObject, Pointcut thePointcut) {
		switch (determineOperationDirection(theRequestDetails.getRestOperationType(), null)) {
			case IN:
			case NONE:
				return;
			case BOTH:
			case OUT:
				break;
		}

		// Don't check the value twice
		IdentityHashMap<IBaseResource, Boolean> alreadySeenMap = ConsentInterceptor.getAlreadySeenResourcesMap(theRequestDetails, myRequestSeenResourcesKey);
		if (alreadySeenMap.putIfAbsent(theResponseObject, Boolean.TRUE) != null) {
			return;
		}

		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();
		List<IBaseResource> resources = Collections.emptyList();

		//noinspection EnumSwitchStatementWhichMissesCases
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
			applyRulesAndFailIfDeny(theRequestDetails.getRestOperationType(), theRequestDetails, null, null, nextResponse, thePointcut);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void hookResourcePreCreate(RequestDetails theRequest, IBaseResource theResource, Pointcut thePointcut) {
		handleUserOperation(theRequest, theResource, RestOperationTypeEnum.CREATE, thePointcut);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	public void hookResourcePreDelete(RequestDetails theRequest, IBaseResource theResource, Pointcut thePointcut) {
		handleUserOperation(theRequest, theResource, RestOperationTypeEnum.DELETE, thePointcut);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void hookResourcePreUpdate(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource, Pointcut thePointcut) {
		if (theOldResource != null) {
			handleUserOperation(theRequest, theOldResource, RestOperationTypeEnum.UPDATE, thePointcut);
		}
		handleUserOperation(theRequest, theNewResource, RestOperationTypeEnum.UPDATE, thePointcut);
	}

	private enum OperationExamineDirection {
		BOTH,
		IN,
		NONE,
		OUT,
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

	public static class Verdict {

		private final IAuthRule myDecidingRule;
		private final PolicyEnum myDecision;

		public Verdict(PolicyEnum theDecision, IAuthRule theDecidingRule) {
			Validate.notNull(theDecision);

			myDecision = theDecision;
			myDecidingRule = theDecidingRule;
		}

		IAuthRule getDecidingRule() {
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

}
