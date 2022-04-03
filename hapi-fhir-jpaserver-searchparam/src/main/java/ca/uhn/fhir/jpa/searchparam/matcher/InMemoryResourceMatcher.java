package ca.uhn.fhir.jpa.searchparam.matcher;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryResourceMatcher {

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	ModelConfig myModelConfig;
	@Autowired
	FhirContext myFhirContext;

	public InMemoryResourceMatcher() {}

	/**
	 * This method is called in two different scenarios.  With a null theResource, it determines whether database matching might be required.
	 * Otherwise, it tries to perform the match in-memory, returning UNSUPPORTED if it's not possible.
	 * <p>
	 * Note that there will be cases where it returns UNSUPPORTED with a null resource, but when a non-null resource it returns supported and no match.
	 * This is because an earlier parameter may be matchable in-memory in which case processing stops and we never get to the parameter
	 * that would have required a database call.
	 */

	public InMemoryMatchResult match(String theCriteria, IBaseResource theResource, ResourceIndexedSearchParams theSearchParams) {
		RuntimeResourceDefinition resourceDefinition;
		if (theResource == null) {
			Validate.isTrue(!theCriteria.startsWith("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
			Validate.isTrue(theCriteria.contains("?"), "Invalid match URL format (must match \"[resourceType]?[params]\")");
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
		return match(searchParameterMap, theResource, resourceDefinition, theSearchParams);
	}

	/**
	 *
	 * @param theCriteria
	 * @return result.supported() will be true if theCriteria can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(String theCriteria) {
		return match(theCriteria, null, null);
	}

	/**
	 *
	 * @param theSearchParameterMap
	 * @param theResourceDefinition
	 * @return result.supported() will be true if theSearchParameterMap can be evaluated in-memory
	 */
	public InMemoryMatchResult canBeEvaluatedInMemory(SearchParameterMap theSearchParameterMap, RuntimeResourceDefinition theResourceDefinition) {
		return match(theSearchParameterMap, null, theResourceDefinition, null);
	}


	@Nonnull
	public InMemoryMatchResult match(SearchParameterMap theSearchParameterMap, IBaseResource theResource, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		if (theSearchParameterMap.getLastUpdated() != null) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(Constants.PARAM_LASTUPDATED, InMemoryMatchResult.STANDARD_PARAMETER);
		}
		if (theSearchParameterMap.containsKey(Location.SP_NEAR)) {
			return InMemoryMatchResult.unsupportedFromReason(InMemoryMatchResult.LOCATION_NEAR);
		}

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : theSearchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<IQueryParameterType>> theAndOrParams = entry.getValue();
			InMemoryMatchResult result = matchIdsWithAndOr(theParamName, theAndOrParams, theResourceDefinition, theResource, theSearchParams);
			if (!result.matched()) {
				return result;
			}
		}
		return InMemoryMatchResult.successfulMatch();
	}

	// This method is modelled from SearchBuilder.searchForIdsWithAndOr()
	private InMemoryMatchResult matchIdsWithAndOr(String theParamName, List<List<IQueryParameterType>> theAndOrParams, RuntimeResourceDefinition theResourceDefinition, IBaseResource theResource, ResourceIndexedSearchParams theSearchParams) {
		if (theAndOrParams.isEmpty()) {
			return InMemoryMatchResult.successfulMatch();
		}

		if (hasQualifiers(theAndOrParams)) {
			Optional<IQueryParameterType> optionalParameter = theAndOrParams.stream().flatMap(List::stream).filter(param -> param.getQueryParameterQualifier() != null).findAny();
			if (optionalParameter.isPresent()) {
				IQueryParameterType parameter = optionalParameter.get();
				if (parameter instanceof ReferenceParam) {
					ReferenceParam referenceParam = (ReferenceParam) parameter;
					return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName + "." + referenceParam.getChain(), InMemoryMatchResult.CHAIN);
				}
				return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName + parameter.getQueryParameterQualifier(), InMemoryMatchResult.QUALIFIER);
			}
		}

		if (hasChain(theAndOrParams)) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.CHAIN);
		}

		String resourceName = theResourceDefinition.getName();
		RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
		InMemoryMatchResult checkUnsupportedResult = checkUnsupportedPrefixes(theParamName, paramDef, theAndOrParams);

		if (!checkUnsupportedResult.supported()) {
			return checkUnsupportedResult;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				return InMemoryMatchResult.fromBoolean(matchIdsAndOr(theAndOrParams, theResource));

			case Constants.PARAM_HAS:
			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);
			case Constants.PARAM_SOURCE:
				return InMemoryMatchResult.fromBoolean(matchSourcesAndOr(theAndOrParams, theResource));
			default:
				return matchResourceParam(myModelConfig, theParamName, theAndOrParams, theSearchParams, resourceName, paramDef);
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
			matches = paramSource.getSourceUri().equals(resourceSource.getSourceUri());
		}
		if (paramSource.getRequestId() != null) {
			matches &= paramSource.getRequestId().equals(resourceSource.getRequestId());
		}
		return matches;
	}

	private boolean matchIdsAndOr(List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchIdsOr(nextAnd, theResource));
	}

	private boolean matchIdsOr(List<IQueryParameterType> theOrParams, IBaseResource theResource) {
		return theOrParams.stream().anyMatch(param -> param instanceof StringParam && matchId(((StringParam) param).getValue(), theResource.getIdElement()));
	}

	private boolean matchId(String theValue, IIdType theId) {
		return theValue.equals(theId.getValue()) || theValue.equals(theId.getIdPart());
	}

	private InMemoryMatchResult matchResourceParam(ModelConfig theModelConfig, String theParamName, List<List<IQueryParameterType>> theAndOrParams, ResourceIndexedSearchParams theSearchParams, String theResourceName, RuntimeSearchParam theParamDef) {
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
						return InMemoryMatchResult.fromBoolean(theAndOrParams.stream().allMatch(nextAnd -> matchParams(theModelConfig, theResourceName, theParamName, theParamDef, nextAnd, theSearchParams)));
					}
				case COMPOSITE:
				case HAS:
				case SPECIAL:
				default:
					return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);
			}
		} else {
			if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
				return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);
			} else {
				throw new InvalidRequestException(Msg.code(509) + "Unknown search parameter " + theParamName + " for resource type " + theResourceName);
			}
		}
	}

	private boolean matchParams(ModelConfig theModelConfig, String theResourceName, String theParamName, RuntimeSearchParam paramDef, List<? extends IQueryParameterType> theNextAnd, ResourceIndexedSearchParams theSearchParams) {
		return theNextAnd.stream().anyMatch(token -> theSearchParams.matchParam(theModelConfig, theResourceName, theParamName, paramDef, token));
	}

	private boolean hasChain(List<List<IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param instanceof ReferenceParam && ((ReferenceParam) param).getChain() != null);
	}

	private boolean hasQualifiers(List<List<IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param.getQueryParameterQualifier() != null);
	}

	private InMemoryMatchResult checkUnsupportedPrefixes(String theParamName, RuntimeSearchParam theParamDef, List<List<IQueryParameterType>> theAndOrParams) {
		if (theParamDef != null) {
			for (List<IQueryParameterType> theAndOrParam : theAndOrParams) {
				for (IQueryParameterType param : theAndOrParam) {
					if (param instanceof BaseParamWithPrefix) {
						ParamPrefixEnum prefix = ((BaseParamWithPrefix<?>) param).getPrefix();
						RestSearchParameterTypeEnum paramType = theParamDef.getParamType();
						if (!supportedPrefix(prefix, paramType)) {
							return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, String.format("The prefix %s is not supported for param type %s", prefix, paramType));
						}
					}
				}
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
}
