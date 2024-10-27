/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams.isMatchSearchParam;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class InMemoryResourceMatcher {

	public static final Set<String> UNSUPPORTED_PARAMETER_NAMES = Sets.newHashSet(Constants.PARAM_HAS);
	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(InMemoryResourceMatcher.class);

	@Autowired
	ApplicationContext myApplicationContext;

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	StorageSettings myStorageSettings;

	@Autowired
	FhirContext myFhirContext;

	@Autowired
	SearchParamExtractorService mySearchParamExtractorService;

	@Autowired
	IndexedSearchParamExtractor myIndexedSearchParamExtractor;

	@Autowired
	private MatchUrlService myMatchUrlService;

	private ValidationSupportInitializationState validationSupportState =
			ValidationSupportInitializationState.NOT_INITIALIZED;
	private IValidationSupport myValidationSupport = null;

	public InMemoryResourceMatcher() {}

	/**
	 * Lazy loads a {@link IValidationSupport} implementation just-in-time.
	 * If no suitable bean is available, or if a {@link ca.uhn.fhir.context.ConfigurationException} is thrown, matching
	 * can proceed, but the qualifiers that depend on the validation support will be disabled.
	 *
	 * @return A bean implementing {@link IValidationSupport} if one is available, otherwise null
	 */
	private IValidationSupport getValidationSupportOrNull() {
		if (validationSupportState == ValidationSupportInitializationState.NOT_INITIALIZED) {
			try {
				myValidationSupport = myApplicationContext.getBean(IValidationSupport.class);
				validationSupportState = ValidationSupportInitializationState.INITIALIZED;
			} catch (BeansException | ConfigurationException ignore) {
				// We couldn't get a validation support bean, and we don't want to waste cycles trying again
				ourLog.warn(
						Msg.code(2100)
								+ "No bean satisfying IValidationSupport could be initialized. Qualifiers dependent on IValidationSupport will not be supported.");
				validationSupportState = ValidationSupportInitializationState.FAILED;
			}
		}
		return myValidationSupport;
	}

	/**
	 * @deprecated Use {@link #match(String, IBaseResource, ResourceIndexedSearchParams, RequestDetails)}
	 */
	@Deprecated
	public InMemoryMatchResult match(
			String theCriteria,
			IBaseResource theResource,
			@Nullable ResourceIndexedSearchParams theIndexedSearchParams) {
		return match(theCriteria, theResource, theIndexedSearchParams, null);
	}

	/**
	 * This method is called in two different scenarios.  With a null theResource, it determines whether database matching might be required.
	 * Otherwise, it tries to perform the match in-memory, returning UNSUPPORTED if it's not possible.
	 * <p>
	 * Note that there will be cases where it returns UNSUPPORTED with a null resource, but when a non-null resource it returns supported and no match.
	 * This is because an earlier parameter may be matchable in-memory in which case processing stops and we never get to the parameter
	 * that would have required a database call.
	 *
	 * @param theIndexedSearchParams If the search params have already been calculated for the given resource,
	 *                               they can be passed in. Passing in {@literal null} is also fine, in which
	 *                               case they will be calculated for the resource. It can be preferable to
	 *                               pass in {@literal null} unless you already actually had to calculate the
	 *                               indexes for another reason, since we can be efficient here and only calculate
	 *                               the params that are actually relevant for the given search expression.
	 */
	public InMemoryMatchResult match(
			String theCriteria,
			IBaseResource theResource,
			@Nullable ResourceIndexedSearchParams theIndexedSearchParams,
			RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDefinition;
		if (theResource == null) {
			Validate.isTrue(
					!theCriteria.startsWith("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
			Validate.isTrue(
					theCriteria.contains("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
			resourceDefinition = UrlUtil.parseUrlResourceType(myFhirContext, theCriteria);
		} else {
			resourceDefinition = myFhirContext.getResourceDefinition(theResource);
		}
		SearchParameterMap searchParameterMap;
		try {
			searchParameterMap = myMatchUrlService.translateMatchUrl(theCriteria, resourceDefinition);
		} catch (UnsupportedOperationException e) {
			return InMemoryMatchResult.unsupportedFromReason(InMemoryMatchResult.PARSE_FAIL);
		}
		searchParameterMap.clean();

		ResourceIndexedSearchParams relevantSearchParams = null;
		if (theIndexedSearchParams != null) {
			relevantSearchParams = theIndexedSearchParams;
		} else if (theResource != null) {
			// Don't index search params we don't actully need for the given criteria
			ISearchParamExtractor.ISearchParamFilter filter = theSearchParams -> theSearchParams.stream()
					.filter(t -> searchParameterMap.containsKey(t.getName()))
					.collect(Collectors.toList());
			relevantSearchParams =
					myIndexedSearchParamExtractor.extractIndexedSearchParams(theResource, theRequestDetails, filter);
		}

		return match(searchParameterMap, theResource, resourceDefinition, relevantSearchParams);
	}

	/**
	 * @param theCriteria
	 * @return result.supported() will be true if theCriteria can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(String theCriteria) {
		return match(theCriteria, null, null, null);
	}

	/**
	 * @param theSearchParameterMap
	 * @param theResourceDefinition
	 * @return result.supported() will be true if theSearchParameterMap can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(
			SearchParameterMap theSearchParameterMap, RuntimeResourceDefinition theResourceDefinition) {
		return match(theSearchParameterMap, null, theResourceDefinition, null);
	}

	@Nonnull
	public InMemoryMatchResult match(
			SearchParameterMap theSearchParameterMap,
			IBaseResource theResource,
			RuntimeResourceDefinition theResourceDefinition,
			ResourceIndexedSearchParams theSearchParams) {
		if (theSearchParameterMap.getLastUpdated() != null) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(
					Constants.PARAM_LASTUPDATED, InMemoryMatchResult.STANDARD_PARAMETER);
		}
		if (theSearchParameterMap.containsKey(Location.SP_NEAR)) {
			return InMemoryMatchResult.unsupportedFromReason(InMemoryMatchResult.LOCATION_NEAR);
		}

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : theSearchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<IQueryParameterType>> theAndOrParams = entry.getValue();
			InMemoryMatchResult result = matchIdsWithAndOr(
					theParamName, theAndOrParams, theResourceDefinition, theResource, theSearchParams);
			if (!result.matched()) {
				return result;
			}
		}
		return InMemoryMatchResult.successfulMatch();
	}

	// This method is modelled from SearchBuilder.searchForIdsWithAndOr()
	private InMemoryMatchResult matchIdsWithAndOr(
			String theParamName,
			List<List<IQueryParameterType>> theAndOrParams,
			RuntimeResourceDefinition theResourceDefinition,
			IBaseResource theResource,
			ResourceIndexedSearchParams theSearchParams) {
		if (theAndOrParams.isEmpty()) {
			return InMemoryMatchResult.successfulMatch();
		}

		String resourceName = theResourceDefinition.getName();
		RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
		InMemoryMatchResult checkUnsupportedResult =
				checkForUnsupportedParameters(theParamName, paramDef, theAndOrParams);
		if (!checkUnsupportedResult.supported()) {
			return checkUnsupportedResult;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				return InMemoryMatchResult.fromBoolean(matchIdsAndOr(theAndOrParams, theResource));
			case Constants.PARAM_SOURCE:
				return InMemoryMatchResult.fromBoolean(matchSourcesAndOr(theAndOrParams, theResource));
			case Constants.PARAM_TAG:
				return InMemoryMatchResult.fromBoolean(matchTagsOrSecurityAndOr(theAndOrParams, theResource, true));
			case Constants.PARAM_SECURITY:
				return InMemoryMatchResult.fromBoolean(matchTagsOrSecurityAndOr(theAndOrParams, theResource, false));
			case Constants.PARAM_PROFILE:
				return InMemoryMatchResult.fromBoolean(matchProfilesAndOr(theAndOrParams, theResource));
			default:
				return matchResourceParam(
						myStorageSettings, theParamName, theAndOrParams, theSearchParams, resourceName, paramDef);
		}
	}

	private InMemoryMatchResult checkForUnsupportedParameters(
			String theParamName, RuntimeSearchParam theParamDef, List<List<IQueryParameterType>> theAndOrParams) {

		if (UNSUPPORTED_PARAMETER_NAMES.contains(theParamName)) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);
		}

		for (List<IQueryParameterType> orParams : theAndOrParams) {
			// The list should never be empty, but better safe than sorry
			if (orParams.size() > 0) {
				// The params in each OR list all share the same qualifier, prefix, etc., so we only need to check the
				// first one
				InMemoryMatchResult checkUnsupportedResult =
						checkOneParameterForUnsupportedModifiers(theParamName, theParamDef, orParams.get(0));
				if (!checkUnsupportedResult.supported()) {
					return checkUnsupportedResult;
				}
			}
		}

		return InMemoryMatchResult.successfulMatch();
	}

	private InMemoryMatchResult checkOneParameterForUnsupportedModifiers(
			String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		// Assume we're ok until we find evidence we aren't
		InMemoryMatchResult checkUnsupportedResult = InMemoryMatchResult.successfulMatch();

		if (hasChain(theParam)) {
			checkUnsupportedResult = InMemoryMatchResult.unsupportedFromParameterAndReason(
					theParamName + "." + ((ReferenceParam) theParam).getChain(), InMemoryMatchResult.CHAIN);
		}

		if (checkUnsupportedResult.supported()) {
			checkUnsupportedResult = checkUnsupportedQualifiers(theParamName, theParamDef, theParam);
		}

		if (checkUnsupportedResult.supported()) {
			checkUnsupportedResult = checkUnsupportedPrefixes(theParamName, theParamDef, theParam);
		}

		return checkUnsupportedResult;
	}

	private boolean matchProfilesAndOr(List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchProfilesOr(nextAnd, theResource));
	}

	private boolean matchProfilesOr(List<IQueryParameterType> theOrParams, IBaseResource theResource) {
		return theOrParams.stream().anyMatch(param -> matchProfile(param, theResource));
	}

	private boolean matchProfile(IQueryParameterType theProfileParam, IBaseResource theResource) {
		UriParam paramProfile = new UriParam(theProfileParam.getValueAsQueryToken(myFhirContext));

		String paramProfileValue = paramProfile.getValue();
		if (isBlank(paramProfileValue)) {
			return false;
		} else {
			return theResource.getMeta().getProfile().stream()
					.map(IPrimitiveType::getValueAsString)
					.anyMatch(profileValue -> profileValue != null && profileValue.equals(paramProfileValue));
		}
	}

	private boolean matchSourcesAndOr(List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchSourcesOr(nextAnd, theResource));
	}

	private boolean matchSourcesOr(List<IQueryParameterType> theOrParams, IBaseResource theResource) {
		return theOrParams.stream().anyMatch(param -> matchSource(param, theResource));
	}

	private boolean matchSource(IQueryParameterType theSourceParam, IBaseResource theResource) {
		SourceParam paramSource = new SourceParam(theSourceParam.getValueAsQueryToken(myFhirContext));
		SourceParam resourceSource = new SourceParam(MetaUtil.getSource(myFhirContext, theResource.getMeta()));
		boolean matches = true;
		if (paramSource.getSourceUri() != null) {
			matches = matchSourceWithModifiers(theSourceParam, paramSource, resourceSource.getSourceUri());
		}
		if (paramSource.getRequestId() != null) {
			matches &= paramSource.getRequestId().equals(resourceSource.getRequestId());
		}
		return matches;
	}

	private boolean matchSourceWithModifiers(
			IQueryParameterType parameterType, SourceParam paramSource, String theSourceUri) {
		// process :missing modifier
		if (parameterType.getMissing() != null) {
			return parameterType.getMissing() == StringUtils.isBlank(theSourceUri);
		}
		// process :above, :below, :contains modifiers
		if (parameterType instanceof UriParam && ((UriParam) parameterType).getQualifier() != null) {
			UriParam uriParam = ((UriParam) parameterType);
			switch (uriParam.getQualifier()) {
				case ABOVE:
					return UrlUtil.getAboveUriCandidates(paramSource.getSourceUri()).stream()
							.anyMatch(candidate -> candidate.equals(theSourceUri));
				case BELOW:
					return theSourceUri.startsWith(paramSource.getSourceUri());
				case CONTAINS:
					return StringUtils.containsIgnoreCase(theSourceUri, paramSource.getSourceUri());
				default:
					// Unsupported modifier specified - no match
					return false;
			}
		} else {
			// no modifiers specified - use equals operator
			return paramSource.getSourceUri().equals(theSourceUri);
		}
	}

	private boolean matchTagsOrSecurityAndOr(
			List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource, boolean theTag) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchTagsOrSecurityOr(nextAnd, theResource, theTag));
	}

	private boolean matchTagsOrSecurityOr(
			List<IQueryParameterType> theOrParams, IBaseResource theResource, boolean theTag) {
		return theOrParams.stream().anyMatch(param -> matchTagOrSecurity(param, theResource, theTag));
	}

	private boolean matchTagOrSecurity(IQueryParameterType theParam, IBaseResource theResource, boolean theTag) {
		TokenParam param = (TokenParam) theParam;

		List<? extends IBaseCoding> list;
		if (theTag) {
			list = theResource.getMeta().getTag();
		} else {
			list = theResource.getMeta().getSecurity();
		}
		boolean haveMatch = false;
		boolean haveCandidate = false;
		for (IBaseCoding next : list) {
			if (param.getSystem() == null && param.getValue() == null) {
				continue;
			}
			haveCandidate = true;
			if (isNotBlank(param.getSystem())) {
				if (!param.getSystem().equals(next.getSystem())) {
					continue;
				}
			}
			if (isNotBlank(param.getValue())) {
				if (!param.getValue().equals(next.getCode())) {
					continue;
				}
			}
			haveMatch = true;
			break;
		}

		if (param.getModifier() == TokenParamModifier.NOT) {
			haveMatch = !haveMatch;
		}

		return haveMatch && haveCandidate;
	}

	private boolean matchIdsAndOr(List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchIdsOr(nextAnd, theResource));
	}

	private boolean matchIdsOr(List<IQueryParameterType> theOrParams, IBaseResource theResource) {
		return theOrParams.stream()
				.anyMatch(param -> param instanceof StringParam
						&& matchId(((StringParam) param).getValue(), theResource.getIdElement()));
	}

	private boolean matchId(String theValue, IIdType theId) {
		return theValue.equals(theId.getValue()) || theValue.equals(theId.getIdPart());
	}

	private InMemoryMatchResult matchResourceParam(
			StorageSettings theStorageSettings,
			String theParamName,
			List<List<IQueryParameterType>> theAndOrParams,
			ResourceIndexedSearchParams theSearchParams,
			String theResourceName,
			RuntimeSearchParam theParamDef) {
		if (theParamDef != null) {
			switch (theParamDef.getParamType()) {
				case QUANTITY:
				case TOKEN:
				case STRING:
				case NUMBER:
				case URI:
				case DATE:
				case REFERENCE:
					if (theSearchParams == null) {
						return InMemoryMatchResult.successfulMatch();
					} else {
						return InMemoryMatchResult.fromBoolean(theAndOrParams.stream()
								.allMatch(nextAnd -> matchParams(
										theStorageSettings,
										theResourceName,
										theParamName,
										theParamDef,
										nextAnd,
										theSearchParams)));
					}
				case COMPOSITE:
				case HAS:
				case SPECIAL:
				default:
					return InMemoryMatchResult.unsupportedFromParameterAndReason(
							theParamName, InMemoryMatchResult.PARAM);
			}
		} else {
			if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
				return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);
			} else {
				throw new InvalidRequestException(Msg.code(509) + "Unknown search parameter " + theParamName
						+ " for resource type " + theResourceName);
			}
		}
	}

	private boolean matchParams(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			RuntimeSearchParam theParamDef,
			List<? extends IQueryParameterType> theOrList,
			ResourceIndexedSearchParams theSearchParams) {

		boolean isNegativeTest = isNegative(theParamDef, theOrList);
		// negative tests like :not and :not-in must not match any or-clause, so we invert the quantifier.
		if (isNegativeTest) {
			return theOrList.stream()
					.allMatch(token -> matchParam(
							theStorageSettings, theResourceName, theParamName, theParamDef, theSearchParams, token));
		} else {
			return theOrList.stream()
					.anyMatch(token -> matchParam(
							theStorageSettings, theResourceName, theParamName, theParamDef, theSearchParams, token));
		}
	}

	/**
	 * Some modifiers are negative, and must match NONE of their or-list
	 */
	private boolean isNegative(RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theOrList) {
		if (theParamDef.getParamType().equals(RestSearchParameterTypeEnum.TOKEN)) {
			TokenParam tokenParam = (TokenParam) theOrList.get(0);
			TokenParamModifier modifier = tokenParam.getModifier();
			return modifier != null && modifier.isNegative();
		} else {
			return false;
		}
	}

	private boolean matchParam(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			RuntimeSearchParam theParamDef,
			ResourceIndexedSearchParams theSearchParams,
			IQueryParameterType theToken) {
		if (theParamDef.getParamType().equals(RestSearchParameterTypeEnum.TOKEN)) {
			return matchTokenParam(
					theStorageSettings, theResourceName, theParamName, theParamDef, theSearchParams, (TokenParam)
							theToken);
		} else {
			return theSearchParams.matchParam(theStorageSettings, theResourceName, theParamName, theParamDef, theToken);
		}
	}

	/**
	 * Checks whether a query parameter of type token matches one of the search parameters of an in-memory resource.
	 * The :not modifier is supported.
	 * The :in and :not-in qualifiers are supported only if a bean implementing IValidationSupport is available.
	 * Any other qualifier will be ignored and the match will be treated as unqualified.
	 *
	 * @param theStorageSettings a model configuration
	 * @param theResourceName    the name of the resource type being matched
	 * @param theParamName       the name of the parameter
	 * @param theParamDef        the definition of the search parameter
	 * @param theSearchParams    the search parameters derived from the target resource
	 * @param theQueryParam      the query parameter to compare with theSearchParams
	 * @return true if theQueryParam matches the collection of theSearchParams, otherwise false
	 */
	private boolean matchTokenParam(
			StorageSettings theStorageSettings,
			String theResourceName,
			String theParamName,
			RuntimeSearchParam theParamDef,
			ResourceIndexedSearchParams theSearchParams,
			TokenParam theQueryParam) {
		if (theQueryParam.getModifier() != null) {
			switch (theQueryParam.getModifier()) {
				case IN:
					return theSearchParams.myTokenParams.stream()
							.filter(t -> isMatchSearchParam(theStorageSettings, theResourceName, theParamName, t))
							.anyMatch(t -> systemContainsCode(theQueryParam, t));
				case NOT_IN:
					return theSearchParams.myTokenParams.stream()
							.filter(t -> isMatchSearchParam(theStorageSettings, theResourceName, theParamName, t))
							.noneMatch(t -> systemContainsCode(theQueryParam, t));
				case NOT:
					return !theSearchParams.matchParam(
							theStorageSettings, theResourceName, theParamName, theParamDef, theQueryParam);
				default:
					return theSearchParams.matchParam(
							theStorageSettings, theResourceName, theParamName, theParamDef, theQueryParam);
			}
		} else {
			return theSearchParams.matchParam(
					theStorageSettings, theResourceName, theParamName, theParamDef, theQueryParam);
		}
	}

	private boolean systemContainsCode(TokenParam theQueryParam, ResourceIndexedSearchParamToken theSearchParamToken) {
		IValidationSupport validationSupport = getValidationSupportOrNull();
		if (validationSupport == null) {
			ourLog.error(Msg.code(2096) + "Attempting to evaluate an unsupported qualifier. This should not happen.");
			return false;
		}

		IValidationSupport.CodeValidationResult codeValidationResult = validationSupport.validateCode(
				new ValidationSupportContext(validationSupport),
				new ConceptValidationOptions(),
				theSearchParamToken.getSystem(),
				theSearchParamToken.getValue(),
				null,
				theQueryParam.getValue());
		if (codeValidationResult != null) {
			return codeValidationResult.isOk();
		} else {
			return false;
		}
	}

	private boolean hasChain(IQueryParameterType theParam) {
		return theParam instanceof ReferenceParam && ((ReferenceParam) theParam).getChain() != null;
	}

	private boolean hasQualifiers(IQueryParameterType theParam) {
		return theParam.getQueryParameterQualifier() != null;
	}

	private InMemoryMatchResult checkUnsupportedPrefixes(
			String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		if (theParamDef != null && theParam instanceof BaseParamWithPrefix) {
			ParamPrefixEnum prefix = ((BaseParamWithPrefix<?>) theParam).getPrefix();
			RestSearchParameterTypeEnum paramType = theParamDef.getParamType();
			if (!supportedPrefix(prefix, paramType)) {
				return InMemoryMatchResult.unsupportedFromParameterAndReason(
						theParamName,
						String.format("The prefix %s is not supported for param type %s", prefix, paramType));
			}
		}
		return InMemoryMatchResult.successfulMatch();
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private boolean supportedPrefix(ParamPrefixEnum theParam, RestSearchParameterTypeEnum theParamType) {
		if (theParam == null || theParamType == null) {
			return true;
		}
		switch (theParamType) {
			case DATE:
				switch (theParam) {
					case GREATERTHAN:
					case GREATERTHAN_OR_EQUALS:
					case LESSTHAN:
					case LESSTHAN_OR_EQUALS:
					case EQUAL:
						return true;
				}
				break;
			default:
				return false;
		}
		return false;
	}

	private InMemoryMatchResult checkUnsupportedQualifiers(
			String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		if (hasQualifiers(theParam) && !supportedQualifier(theParamDef, theParam)) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(
					theParamName + theParam.getQueryParameterQualifier(), InMemoryMatchResult.QUALIFIER);
		}
		return InMemoryMatchResult.successfulMatch();
	}

	private boolean supportedQualifier(RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		if (theParamDef == null || theParam == null) {
			return true;
		}
		switch (theParamDef.getParamType()) {
			case TOKEN:
				TokenParam tokenParam = (TokenParam) theParam;
				switch (tokenParam.getModifier()) {
					case IN:
					case NOT_IN:
						// Support for these qualifiers is dependent on an implementation of IValidationSupport being
						// available to delegate the check to
						return getValidationSupportOrNull() != null;
					case NOT:
						return true;
					default:
						return false;
				}
			default:
				return false;
		}
	}

	private enum ValidationSupportInitializationState {
		NOT_INITIALIZED,
		INITIALIZED,
		FAILED
	}
}
