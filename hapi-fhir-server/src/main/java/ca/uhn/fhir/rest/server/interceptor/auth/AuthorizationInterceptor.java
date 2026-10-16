/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
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
@SuppressWarnings("unused")
@Interceptor(order = AuthorizationConstants.ORDER_AUTH_INTERCEPTOR)
public class AuthorizationInterceptor implements IRuleApplier {

	public static final String REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS =
			AuthorizationInterceptor.class.getName() + "_BulkDataExportOptions";
	private static final AtomicInteger ourInstanceCount = new AtomicInteger(0);
	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	private static final Set<BundleTypeEnum> STANDALONE_BUNDLE_RESOURCE_TYPES =
			Set.of(BundleTypeEnum.DOCUMENT, BundleTypeEnum.MESSAGE, BundleTypeEnum.COLLECTION);

	private final int myInstanceIndex = ourInstanceCount.incrementAndGet();
	private final String myRequestSeenResourcesKey =
			AuthorizationInterceptor.class.getName() + "_" + myInstanceIndex + "_SEENRESOURCES";
	private final String myRequestRuleListKey =
			AuthorizationInterceptor.class.getName() + "_" + myInstanceIndex + "_RULELIST";
	private PolicyEnum myDefaultPolicy = PolicyEnum.DENY;
	private Set<AuthorizationFlagsEnum> myFlags = Collections.emptySet();
	public static final List<RestOperationTypeEnum> REST_OPERATIONS_TO_EXCLUDE_SECURITY_FOR_OPERATION_OUTCOME = List.of(
			RestOperationTypeEnum.SEARCH_TYPE, RestOperationTypeEnum.SEARCH_SYSTEM, RestOperationTypeEnum.GET_PAGE);
	private IValidationSupport myValidationSupport;

	private IAuthorizationSearchParamMatcher myAuthorizationSearchParamMatcher;
	private IAuthResourceResolver myAuthResourceResolver;
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

	private void applyRulesAndFailIfDeny(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			Pointcut thePointcut) {
		Verdict decision = applyRulesAndReturnDecision(
				theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, thePointcut);

		if (decision.getDecision() == PolicyEnum.ALLOW) {
			return;
		}

		handleDeny(theRequestDetails, decision);
	}

	@Override
	public Verdict applyRulesAndReturnDecision(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			Pointcut thePointcut) {
		@SuppressWarnings("unchecked")
		List<IAuthRule> rules =
				(List<IAuthRule>) theRequestDetails.getUserData().get(myRequestRuleListKey);
		if (rules == null) {
			rules = buildRuleList(theRequestDetails);
			theRequestDetails.getUserData().put(myRequestRuleListKey, rules);
		}
		Set<AuthorizationFlagsEnum> flags = getFlags();

		ourLog.trace(
				"Applying {} rules to render an auth decision for operation {}, theInputResource type={}, theOutputResource type={}, thePointcut={} ",
				rules.size(),
				getPointcutNameOrEmpty(thePointcut),
				getResourceTypeOrEmpty(theInputResource),
				getResourceTypeOrEmpty(theOutputResource),
				thePointcut);

		Verdict verdict = null;
		for (IAuthRule nextRule : rules) {
			ourLog.trace("Rule being applied - {}", nextRule);
			verdict = nextRule.applyRule(
					theOperation,
					theRequestDetails,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					this,
					flags,
					thePointcut);
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
	 * Sets a search parameter matcher for use in handling SMART v2 filter scopes
	 *
	 * @param theAuthorizationSearchParamMatcher The search parameter matcher. Defaults to null.
	 */
	public void setAuthorizationSearchParamMatcher(
			@Nullable IAuthorizationSearchParamMatcher theAuthorizationSearchParamMatcher) {
		this.myAuthorizationSearchParamMatcher = theAuthorizationSearchParamMatcher;
	}

	@Override
	@Nullable
	public IAuthorizationSearchParamMatcher getSearchParamMatcher() {
		return myAuthorizationSearchParamMatcher;
	}

	/**
	 * Sets a resource resolver to resolve resources during authorization rule evaluation.
	 *
	 * @param theAuthResourceResolver The resource resolver. Defaults to null.
	 */
	public void setAuthResourceResolver(@Nullable IAuthResourceResolver theAuthResourceResolver) {
		this.myAuthResourceResolver = theAuthResourceResolver;
	}

	@Nullable
	@Override
	public IAuthResourceResolver getAuthResourceResolver() {
		return myAuthResourceResolver;
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

	private OperationExamineDirection determineOperationDirection(RestOperationTypeEnum theOperation) {

		switch (theOperation) {
			case ADD_TAGS:
			case DELETE_TAGS:
			case GET_TAGS:
				// These are DSTU1 operations and not relevant
				return OperationExamineDirection.NONE;
			case PATCH:
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
				throw new IllegalStateException(
						Msg.code(332) + "Unable to apply security to event of type " + theOperation);
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
			String ruleName = Objects.toString(decision.getDecidingRule().getName(), "(unnamed rule)");
			throw new ForbiddenOperationException(Msg.code(333) + "Access denied by rule: " + ruleName);
		}
		throw new ForbiddenOperationException(Msg.code(334) + "Access denied by default policy (no applicable rules)");
	}

	private void handleUserOperation(
			RequestDetails theRequest,
			IBaseResource theResource,
			RestOperationTypeEnum theOperation,
			Pointcut thePointcut) {
		applyRulesAndFailIfDeny(theOperation, theRequest, theResource, theResource.getIdElement(), null, thePointcut);
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
	public void incomingRequestPreHandled(RequestDetails theRequest, Pointcut thePointcut) {
		IBaseResource inputResource = null;
		IIdType inputResourceId = null;

		switch (determineOperationDirection(theRequest.getRestOperationType())) {
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

		applyRulesAndFailIfDeny(
				theRequest.getRestOperationType(), theRequest, inputResource, inputResourceId, null, thePointcut);

		/*
		 * A search with _has (reverse chain) parameters filters the results using resources of
		 * other types. Those joined types are not covered by the type-level authorization above,
		 * nor by the per-resource checks on the way out (which only see the searched type), so
		 * every joined type must be authorized here. See https://github.com/hapifhir/hapi-fhir/issues/8446
		 */
		if (theRequest.getRestOperationType() == RestOperationTypeEnum.SEARCH_TYPE) {
			checkHasParameterTypesAuthorized(theRequest, thePointcut);
		}
	}

	/**
	 * Authorizes every resource type joined by {@code _has} (reverse chain) search parameters on a
	 * type search. A {@code _has} parameter filters the searched resources using resources of
	 * another type, so the search may only run if the client is authorized to read every type the
	 * query can reach. Denies the request through {@link #handleDeny} otherwise.
	 * <p>
	 * A type-level read rule, a read-all rule or {@code allowAll()} on the joined type qualifies,
	 * as does a compartment read rule when the search is limited by {@code _id} to that
	 * compartment's owners and the join goes through one of the compartment's search parameters.
	 * Every level of a nested {@code _has} needs its own access.
	 * </p>
	 */
	private void checkHasParameterTypesAuthorized(RequestDetails theRequestDetails, Pointcut thePointcut) {
		Map<String, String[]> parameters = theRequestDetails.getParameters();
		if (parameters == null || parameters.isEmpty()) {
			return;
		}

		List<String> hasParamNames = new ArrayList<>();
		for (String paramName : parameters.keySet()) {
			if (Constants.PARAM_HAS.equals(paramName) || paramName.startsWith(Constants.PARAM_HAS + ":")) {
				hasParamNames.add(paramName);
			}
		}
		if (hasParamNames.isEmpty()) {
			return;
		}

		List<String> outerIds = extractIdParameterValues(parameters);

		for (String hasParamName : hasParamNames) {
			List<HasJoin> joins = parseHasParameterName(hasParamName);
			if (joins == null) {
				// Malformed _has parameter: fail closed
				ourLog.debug("Denying search with malformed _has parameter: {}", hasParamName);
				handleDeny(theRequestDetails, new Verdict(PolicyEnum.DENY, null));
			}
			for (HasJoin join : joins) {
				if (!isHasJoinAuthorized(join, outerIds, theRequestDetails, thePointcut)) {
					ourLog.debug(
							"Denying search with _has parameter {}: no read access to joined type {}",
							hasParamName,
							join.myTargetType);
					handleDeny(theRequestDetails, new Verdict(PolicyEnum.DENY, null));
				}
			}
		}
	}

	/**
	 * Parses a {@code _has} parameter name (e.g. {@code _has:Observation:subject:code} or a nested
	 * {@code _has:Observation:subject:_has:AuditEvent:entity:code}) into the
	 * (joined type, join search parameter) pair for every {@code _has} level.
	 * Returns {@code null} if the name is malformed.
	 */
	@Nullable
	private static List<HasJoin> parseHasParameterName(String theParamName) {
		String[] parts = theParamName.split(":");
		List<HasJoin> retVal = new ArrayList<>();
		for (int i = 0; i < parts.length; i++) {
			if (Constants.PARAM_HAS.equals(parts[i])) {
				if (i + 2 >= parts.length) {
					return null;
				}
				retVal.add(new HasJoin(parts[i + 1], parts[i + 2]));
			}
		}
		return retVal.isEmpty() ? null : retVal;
	}

	/**
	 * Extracts the individual values of the {@code _id} search parameter, splitting
	 * comma-separated OR values.
	 */
	private static List<String> extractIdParameterValues(Map<String, String[]> theParameters) {
		List<String> retVal = new ArrayList<>();
		String[] idValues = theParameters.get(IAnyResource.SP_RES_ID);
		if (idValues != null) {
			for (String idValue : idValues) {
				QualifiedParamList orValues = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, idValue);
				retVal.addAll(orValues);
			}
		}
		return retVal;
	}

	/**
	 * Determines whether the client is authorized to read the resources joined by a single
	 * {@code _has} level: those of {@code theJoin.myTargetType} reached through
	 * {@code theJoin.myJoinSearchParam}.
	 */
	private boolean isHasJoinAuthorized(
			HasJoin theJoin, List<String> theOuterIds, RequestDetails theRequestDetails, Pointcut thePointcut) {
		// A type-level (or broader) read rule on the joined type authorizes the join outright.
		if (isSearchAllowedForType(theJoin.myTargetType, Collections.emptyMap(), theRequestDetails, thePointcut)) {
			return true;
		}

		/*
		 * Otherwise a compartment read rule can still authorize the join, but only when the search
		 * is limited by _id to the compartment's owners and the join goes through one of the
		 * compartment's search parameters. Each _id value is tested separately so that a search
		 * limited to a mix of owned and unowned IDs is still denied.
		 */
		if (theOuterIds.isEmpty()) {
			return false;
		}
		for (String outerId : theOuterIds) {
			Map<String, String[]> params = Collections.singletonMap(theJoin.myJoinSearchParam, new String[] {outerId});
			if (!isSearchAllowedForType(theJoin.myTargetType, params, theRequestDetails, thePointcut)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluates the rule list as if the current request were a type search for
	 * {@code theResourceType} with {@code theParameters}, without disturbing the actual request.
	 */
	private boolean isSearchAllowedForType(
			String theResourceType,
			Map<String, String[]> theParameters,
			RequestDetails theRequestDetails,
			Pointcut thePointcut) {
		String originalResourceName = theRequestDetails.getResourceName();
		Map<String, String[]> originalParameters = theRequestDetails.getParameters();
		try {
			theRequestDetails.setResourceName(theResourceType);
			theRequestDetails.setParameters(theParameters);
			Verdict verdict = applyRulesAndReturnDecision(
					RestOperationTypeEnum.SEARCH_TYPE, theRequestDetails, null, null, null, thePointcut);
			return verdict != null && verdict.getDecision() == PolicyEnum.ALLOW;
		} finally {
			// Restore a modifiable copy: getParameters() returns an unmodifiable
			// view, and later request processing (e.g. removeParameter during
			// exception handling) mutates the parameter map directly.
			theRequestDetails.setResourceName(originalResourceName);
			theRequestDetails.setParameters(new HashMap<>(originalParameters));
		}
	}

	/**
	 * One level of a {@code _has} (reverse chain) search parameter: the joined resource type and
	 * the search parameter on that type used for the join.
	 */
	private static class HasJoin {
		final String myTargetType;
		final String myJoinSearchParam;

		HasJoin(String theTargetType, String theJoinSearchParam) {
			myTargetType = theTargetType;
			myJoinSearchParam = theJoinSearchParam;
		}
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void hookPreShow(
			RequestDetails theRequestDetails, IPreResourceShowDetails theDetails, Pointcut thePointcut) {
		for (IBaseResource resource : theDetails.getAllResources()) {
			checkOutgoingResourceAndFailIfDeny(theRequestDetails, resource, thePointcut);
		}
	}

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void hookOutgoingResponse(
			RequestDetails theRequestDetails, IBaseResource theResponseObject, Pointcut thePointcut) {
		checkOutgoingResourceAndFailIfDeny(theRequestDetails, theResponseObject, thePointcut);
	}

	@Hook(Pointcut.STORAGE_CASCADE_DELETE)
	public void hookCascadeDeleteForConflict(
			RequestDetails theRequestDetails, Pointcut thePointcut, IBaseResource theResourceToDelete) {
		Objects.requireNonNull(theResourceToDelete); // just in case
		checkPointcutAndFailIfDeny(theRequestDetails, thePointcut, theResourceToDelete);
	}

	@Hook(Pointcut.STORAGE_PRE_DELETE_EXPUNGE)
	public void hookDeleteExpunge(RequestDetails theRequestDetails, Pointcut thePointcut) {
		applyRulesAndFailIfDeny(
				theRequestDetails.getRestOperationType(), theRequestDetails, null, null, null, thePointcut);
	}

	@Hook(Pointcut.STORAGE_INITIATE_BULK_EXPORT)
	public void initiateBulkExport(
			RequestDetails theRequestDetails, BulkExportJobParameters theBulkExportOptions, Pointcut thePointcut) {
		//		RestOperationTypeEnum restOperationType =
		// determineRestOperationTypeFromBulkExportOptions(theBulkExportOptions);
		RestOperationTypeEnum restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;

		if (theRequestDetails != null) {
			theRequestDetails.getUserData().put(REQUEST_ATTRIBUTE_BULK_DATA_EXPORT_OPTIONS, theBulkExportOptions);
		}
		applyRulesAndFailIfDeny(restOperationType, theRequestDetails, null, null, null, thePointcut);
	}

	/**
	 * TODO GGG This method should eventually be used when invoking the rules applier.....however we currently rely on the incorrect
	 * behaviour of passing down `EXTENDED_OPERATION_SERVER`.
	 */
	private RestOperationTypeEnum determineRestOperationTypeFromBulkExportOptions(
			BulkExportJobParameters theBulkExportOptions) {
		RestOperationTypeEnum restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_SERVER;
		BulkExportJobParameters.ExportStyle exportStyle = theBulkExportOptions.getExportStyle();
		if (exportStyle.equals(BulkExportJobParameters.ExportStyle.PATIENT)) {
			if (theBulkExportOptions.getPatientIds().size() == 1) {
				restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
			} else {
				restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_TYPE;
			}
		} else if (exportStyle.equals(BulkExportJobParameters.ExportStyle.GROUP)) {
			restOperationType = RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE;
		}
		return restOperationType;
	}

	private void checkPointcutAndFailIfDeny(
			RequestDetails theRequestDetails, Pointcut thePointcut, @Nonnull IBaseResource theInputResource) {
		applyRulesAndFailIfDeny(
				theRequestDetails.getRestOperationType(),
				theRequestDetails,
				theInputResource,
				theInputResource.getIdElement(),
				null,
				thePointcut);
	}

	private void checkOutgoingResourceAndFailIfDeny(
			RequestDetails theRequestDetails, IBaseResource theResponseObject, Pointcut thePointcut) {

		switch (determineOperationDirection(theRequestDetails.getRestOperationType())) {
			case IN:
			case NONE:
				return;
			case BOTH:
			case OUT:
				break;
		}

		// Don't check the value twice
		IdentityHashMap<IBaseResource, Boolean> alreadySeenMap = getAlreadySeenResourcesMap(theRequestDetails);
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
					resources = toListOfResourcesAndExcludeContainerUnlessStandalone(
							theResponseObject, fhirContext, theRequestDetails);
				}
				break;
			}
			default: {
				if (theResponseObject != null && !isStatusOnlyOperationOutcome(theRequestDetails, theResponseObject)) {
					resources = Collections.singletonList(theResponseObject);
				}
				break;
			}
		}

		for (IBaseResource nextResponse : resources) {
			applyRulesAndFailIfDeny(
					theRequestDetails.getRestOperationType(), theRequestDetails, null, null, nextResponse, thePointcut);
		}
	}

	private static boolean isStatusOnlyOperationOutcome(
			RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		return theResponseObject instanceof IBaseOperationOutcome
				&& !"OperationOutcome".equals(theRequestDetails.getResourceName());
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
	public void hookResourcePreUpdate(
			RequestDetails theRequest,
			IBaseResource theOldResource,
			IBaseResource theNewResource,
			Pointcut thePointcut) {
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

	protected static List<IBaseResource> toListOfResourcesAndExcludeContainerUnlessStandalone(
			IBaseResource theResponseObject, FhirContext fhirContext, RequestDetails theRequestDetails) {

		if (theResponseObject == null) {
			return Collections.emptyList();
		}

		boolean shouldExamineChildResources = shouldExamineChildResources(theResponseObject, fhirContext);
		if (!shouldExamineChildResources) {
			return toListOfResourcesAndExcludeOperationOutcomeBasedOnRestOperationType(
					theResponseObject, theRequestDetails);
		}

		return toListOfResourcesAndExcludeContainer(theResponseObject, fhirContext);
	}

	/**
	 *
	 * @param theResponseObject The resource to convert to a list.
	 * @param theRequestDetails The request details.
	 * @return The response object (a resource) as a list. If the REST operation type in the request details is a
	 * search, and the search is for resources that aren't the OperationOutcome, any OperationOutcome resource is removed from the list.
	 * e.g. A GET [base]/Patient?parameter(s) search may return a bundle containing an OperationOutcome. The OperationOutcome will be removed from the
	 * list to exclude from security.
	 */
	private static List<IBaseResource> toListOfResourcesAndExcludeOperationOutcomeBasedOnRestOperationType(
			IBaseResource theResponseObject, RequestDetails theRequestDetails) {
		List<IBaseResource> resources = new ArrayList<>();
		RestOperationTypeEnum restOperationType = theRequestDetails.getRestOperationType();
		String resourceName = theRequestDetails.getResourceName();
		resources.add(theResponseObject);

		if (resourceName != null
				&& !resourceName.equals("OperationOutcome")
				&& REST_OPERATIONS_TO_EXCLUDE_SECURITY_FOR_OPERATION_OUTCOME.contains(restOperationType)) {
			resources.removeIf(t -> t instanceof IBaseOperationOutcome);
		}

		return resources;
	}

	@Nonnull
	public static List<IBaseResource> toListOfResourcesAndExcludeContainer(
			IBaseResource theResponseObject, FhirContext fhirContext) {
		List<IBaseResource> retVal;
		retVal = fhirContext.newTerser().getAllPopulatedChildElementsOfType(theResponseObject, IBaseResource.class);

		// Exclude the container
		if (!retVal.isEmpty() && retVal.get(0) == theResponseObject) {
			retVal = retVal.subList(1, retVal.size());
		}

		// Don't apply security to OperationOutcome
		retVal.removeIf(t -> t instanceof IBaseOperationOutcome);

		return retVal;
	}

	/**
	 * This method determines if the given Resource should have permissions applied
	 * to the resources inside or to the Resource itself.
	 * For Parameters resources, we include child resources when checking the permissions.
	 * For Bundle resources, we look at resources inside if the Bundle type is not in
	 * STANDALONE_BUNDLE_RESOURCE_TYPES set.
	 */
	protected static boolean shouldExamineChildResources(IBaseResource theResource, FhirContext theFhirContext) {
		if (theResource instanceof IBaseParameters) {
			return true;
		}

		if (theResource instanceof IBaseBundle baseBundle) {
			BundleTypeEnum bundleType = BundleUtil.getBundleTypeEnum(theFhirContext, baseBundle);
			boolean isStandaloneBundleResource =
					bundleType != null && STANDALONE_BUNDLE_RESOURCE_TYPES.contains(bundleType);
			return !isStandaloneBundleResource;
		}

		return false;
	}

	public static class Verdict {

		private final IAuthRule myDecidingRule;
		private final PolicyEnum myDecision;

		public Verdict(PolicyEnum theDecision, IAuthRule theDecidingRule) {
			Objects.requireNonNull(theDecision);

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

	private Object getPointcutNameOrEmpty(Pointcut thePointcut) {
		return nonNull(thePointcut) ? thePointcut.name() : EMPTY;
	}

	private String getResourceTypeOrEmpty(IBaseResource theResource) {
		String retVal = StringUtils.EMPTY;

		if (isNull(theResource)) {
			return retVal;
		}

		if (isNull(theResource.getIdElement())) {
			return retVal;
		}

		if (isNull(theResource.getIdElement().getResourceType())) {
			return retVal;
		}

		return theResource.getIdElement().getResourceType();
	}

	@SuppressWarnings("unchecked")
	private IdentityHashMap<IBaseResource, Boolean> getAlreadySeenResourcesMap(RequestDetails theRequestDetails) {
		IdentityHashMap<IBaseResource, Boolean> alreadySeenResources = (IdentityHashMap<IBaseResource, Boolean>)
				theRequestDetails.getUserData().get(myRequestSeenResourcesKey);
		if (alreadySeenResources == null) {
			alreadySeenResources = new IdentityHashMap<>();
			theRequestDetails.getUserData().put(myRequestSeenResourcesKey, alreadySeenResources);
		}
		return alreadySeenResources;
	}
}
