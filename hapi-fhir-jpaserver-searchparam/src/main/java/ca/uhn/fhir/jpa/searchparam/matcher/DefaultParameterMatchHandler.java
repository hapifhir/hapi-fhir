package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.models.SearchMatchParameters;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams.isMatchSearchParam;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultParameterMatchHandler implements IParameterMatchHandler {

	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(DefaultParameterMatchHandler.class);

	private IMatchingServices myServices;

	@Override
	public void registerServices(IMatchingServices theServices) {
		myServices = theServices;
	}

	public InMemoryMatchResult checkForUnsupportedParameters(
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


	public InMemoryMatchResult matchResourceByParameters(
		String theParamName,
		RuntimeSearchParam theParamDef,
		List<List<IQueryParameterType>> theAndOrParams,
		SearchMatchParameters theParameters
	) {
		IBaseResource theResource = theParameters.getBaseResource();
		String resourceName = theParameters.getRuntimeResourceDefinition().getName();

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
				return matchResourceParam(theParamName, theAndOrParams, theParameters.getIndexedSearchParams(), resourceName, theParamDef);
		}
	}

	public InMemoryMatchResult matchResourceParam(
		String theParamName,
		List<List<IQueryParameterType>> theAndOrParams,
		ResourceIndexedSearchParams theSearchParams,
		String theResourceName,
		RuntimeSearchParam theParamDef
	) {
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
		UriParam paramProfile = new UriParam(theProfileParam.getValueAsQueryToken(myServices.getFhirContext()));

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
		SourceParam paramSource = new SourceParam(theSourceParam.getValueAsQueryToken(myServices.getFhirContext()));
		SourceParam resourceSource = new SourceParam(MetaUtil.getSource(myServices.getFhirContext(), theResource.getMeta()));
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

	protected boolean matchParams(
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
					theResourceName, theParamName, theParamDef, theSearchParams, token));
		} else {
			return theOrList.stream()
				.anyMatch(token -> matchParam(
					theResourceName, theParamName, theParamDef, theSearchParams, token));
		}
	}

	private boolean matchId(String theValue, IIdType theId) {
		return theValue.equals(theId.getValue()) || theValue.equals(theId.getIdPart());
	}

	/**
	 * Some modifiers are negative, and must match NONE of their or-list
	 */
	protected boolean isNegative(RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theOrList) {
		if (theParamDef.getParamType().equals(RestSearchParameterTypeEnum.TOKEN)) {
			TokenParam tokenParam = (TokenParam) theOrList.get(0);
			TokenParamModifier modifier = tokenParam.getModifier();
			return modifier != null && modifier.isNegative();
		} else {
			return false;
		}
	}

	protected boolean matchParam(
		String theResourceName,
		String theParamName,
		RuntimeSearchParam theParamDef,
		ResourceIndexedSearchParams theSearchParams,
		IQueryParameterType theToken
	) {
		if (theParamDef.getParamType().equals(RestSearchParameterTypeEnum.TOKEN)) {
			return matchTokenParam(
				theResourceName, theParamName, theParamDef, theSearchParams, (TokenParam)
					theToken);
		} else {
			return theSearchParams.matchParam(myServices.getStorageSettings(), theResourceName, theParamName, theParamDef, theToken);
		}
	}

	/**
	 * Checks whether a query parameter of type token matches one of the search parameters of an in-memory resource.
	 * The :not modifier is supported.
	 * The :in and :not-in qualifiers are supported only if a bean implementing IValidationSupport is available.
	 * Any other qualifier will be ignored and the match will be treated as unqualified.
	 *
	 * @param theResourceName    the name of the resource type being matched
	 * @param theParamName       the name of the parameter
	 * @param theParamDef        the definition of the search parameter
	 * @param theSearchParams    the search parameters derived from the target resource
	 * @param theQueryParam      the query parameter to compare with theSearchParams
	 * @return true if theQueryParam matches the collection of theSearchParams, otherwise false
	 */
	protected boolean matchTokenParam(
		String theResourceName,
		String theParamName,
		RuntimeSearchParam theParamDef,
		ResourceIndexedSearchParams theSearchParams,
		TokenParam theQueryParam) {
		if (theQueryParam.getModifier() != null) {
			switch (theQueryParam.getModifier()) {
				case IN:
					return theSearchParams.myTokenParams.stream()
						.filter(t -> isMatchSearchParam(myServices.getStorageSettings(), theResourceName, theParamName, t))
						.anyMatch(t -> systemContainsCode(theQueryParam, t));
				case NOT_IN:
					return theSearchParams.myTokenParams.stream()
						.filter(t -> isMatchSearchParam(myServices.getStorageSettings(), theResourceName, theParamName, t))
						.noneMatch(t -> systemContainsCode(theQueryParam, t));
				case NOT:
					return !theSearchParams.matchParam(
						myServices.getStorageSettings(), theResourceName, theParamName, theParamDef, theQueryParam);
				default:
					return theSearchParams.matchParam(
						myServices.getStorageSettings(), theResourceName, theParamName, theParamDef, theQueryParam);
			}
		} else {
			return theSearchParams.matchParam(
				myServices.getStorageSettings(), theResourceName, theParamName, theParamDef, theQueryParam);
		}
	}

	private boolean systemContainsCode(TokenParam theQueryParam, ResourceIndexedSearchParamToken theSearchParamToken) {
		IValidationSupport validationSupport = myServices.getValidationSupportOrNull();
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

	protected InMemoryMatchResult checkOneParameterForUnsupportedModifiers(
		String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		// Assume we're ok until we find evidence we aren't
		InMemoryMatchResult checkUnsupportedResult = InMemoryMatchResult.successfulMatch();

		if (!supportsChains() && hasChain(theParam)) {
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

	protected boolean supportsChains() {
		return false;
	}

	protected boolean hasChain(IQueryParameterType theParam) {
		return theParam instanceof ReferenceParam && ((ReferenceParam) theParam).getChain() != null;
	}

	private boolean hasQualifiers(IQueryParameterType theParam) {
		return theParam.getQueryParameterQualifier() != null;
	}

	protected InMemoryMatchResult checkUnsupportedPrefixes(
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
	protected boolean supportedPrefix(ParamPrefixEnum theParam, RestSearchParameterTypeEnum theParamType) {
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

	protected InMemoryMatchResult checkUnsupportedQualifiers(
		String theParamName, RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
		if (hasQualifiers(theParam) && !supportedQualifier(theParamDef, theParam)) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(
				theParamName + theParam.getQueryParameterQualifier(), InMemoryMatchResult.QUALIFIER);
		}
		return InMemoryMatchResult.successfulMatch();
	}

	protected boolean supportedQualifier(RuntimeSearchParam theParamDef, IQueryParameterType theParam) {
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
						return myServices.getValidationSupportOrNull() != null;
					case NOT:
						return true;
					default:
						return false;
				}
			case REFERENCE:
				return true;
			default:
				return false;
		}
	}
}
