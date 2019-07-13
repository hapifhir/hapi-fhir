package ca.uhn.fhir.jpa.searchparam.matcher;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class InMemoryResourceMatcher {

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	FhirContext myFhirContext;

	/**
	 * This method is called in two different scenarios.  With a null theResource, it determines whether database matching might be required.
	 * Otherwise, it tries to perform the match in-memory, returning UNSUPPORTED if it's not possible.
	 *
	 * Note that there will be cases where it returns UNSUPPORTED with a null resource, but when a non-null resource it returns supported and no match.
	 * This is because an earlier parameter may be matchable in-memory in which case processing stops and we never get to the parameter
	 * that would have required a database call.
	 *
	 */

	public InMemoryMatchResult match(String theCriteria, IBaseResource theResource, ResourceIndexedSearchParams theSearchParams) {
		RuntimeResourceDefinition resourceDefinition;
		if (theResource == null) {
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
		if (searchParameterMap.getLastUpdated() != null) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(Constants.PARAM_LASTUPDATED, InMemoryMatchResult.STANDARD_PARAMETER);
		}

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : searchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<IQueryParameterType>> theAndOrParams = entry.getValue();
			InMemoryMatchResult result = matchIdsWithAndOr(theParamName, theAndOrParams, resourceDefinition, theResource, theSearchParams);
			if (!result.matched()){
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
			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.STANDARD_PARAMETER);
		}
		if (hasPrefixes(theAndOrParams)) {

			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PREFIX);

		}
		if (hasChain(theAndOrParams)) {
			return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.CHAIN);
		}
		switch (theParamName) {
			case IAnyResource.SP_RES_ID:

				return InMemoryMatchResult.fromBoolean(matchIdsAndOr(theAndOrParams, theResource));

			case IAnyResource.SP_RES_LANGUAGE:
			case Constants.PARAM_HAS:
			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:

				return InMemoryMatchResult.unsupportedFromParameterAndReason(theParamName, InMemoryMatchResult.PARAM);

			default:

				String resourceName = theResourceDefinition.getName();
				RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
				return matchResourceParam(theParamName, theAndOrParams, theSearchParams, resourceName, paramDef);
		}
	}

	private boolean matchIdsAndOr(List<List<IQueryParameterType>> theAndOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theAndOrParams.stream().allMatch(nextAnd -> matchIdsOr(nextAnd, theResource));
	}
	private boolean matchIdsOr(List<IQueryParameterType> theOrParams, IBaseResource theResource) {
		if (theResource == null) {
			return true;
		}
		return theOrParams.stream().anyMatch(param -> param instanceof StringParam && matchId(((StringParam)param).getValue(), theResource.getIdElement()));
	}

	private boolean matchId(String theValue, IIdType theId) {
		return theValue.equals(theId.getValue()) || theValue.equals(theId.getIdPart());
	}

	private InMemoryMatchResult matchResourceParam(String theParamName, List<List<IQueryParameterType>> theAndOrParams, ResourceIndexedSearchParams theSearchParams, String theResourceName, RuntimeSearchParam theParamDef) {
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
						return InMemoryMatchResult.fromBoolean(theAndOrParams.stream().anyMatch(nextAnd -> matchParams(theResourceName, theParamName, theParamDef, nextAnd, theSearchParams)));
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
				throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + theResourceName);
			}
		}
	}

	private boolean matchParams(String theResourceName, String theParamName, RuntimeSearchParam paramDef, List<? extends IQueryParameterType> theNextAnd, ResourceIndexedSearchParams theSearchParams) {
		return theNextAnd.stream().anyMatch(token -> theSearchParams.matchParam(theResourceName, theParamName, paramDef, token));
	}

	private boolean hasChain(List<List<IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param instanceof ReferenceParam && ((ReferenceParam)param).getChain() != null);
	}

	private boolean hasQualifiers(List<List<IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param.getQueryParameterQualifier() != null);
	}

	private boolean hasPrefixes(List<List<IQueryParameterType>> theAndOrParams) {
		Predicate<IQueryParameterType> hasPrefixPredicate = param -> param instanceof BaseParamWithPrefix &&
			((BaseParamWithPrefix) param).getPrefix() != null;
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(hasPrefixPredicate);
	}
}
