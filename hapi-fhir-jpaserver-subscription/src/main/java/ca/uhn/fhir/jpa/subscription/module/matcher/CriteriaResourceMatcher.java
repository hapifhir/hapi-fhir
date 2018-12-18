package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class CriteriaResourceMatcher {

	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	public SubscriptionMatchResult match(String theCriteria, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		SearchParameterMap searchParameterMap;
		try {
			searchParameterMap = myMatchUrlService.translateMatchUrl(theCriteria, theResourceDefinition);
		} catch (UnsupportedOperationException e) {
			return new SubscriptionMatchResult(theCriteria);
		}
		searchParameterMap.clean();
		if (searchParameterMap.getLastUpdated() != null) {
			return new SubscriptionMatchResult(Constants.PARAM_LASTUPDATED, "Qualifiers not supported");
		}

		for (Map.Entry<String, List<List<? extends IQueryParameterType>>> entry : searchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<? extends IQueryParameterType>> theAndOrParams = entry.getValue();
			SubscriptionMatchResult result = matchIdsWithAndOr(theParamName, theAndOrParams, theResourceDefinition, theSearchParams);
			if (!result.matched()){
				return result;
			}
		}
		return SubscriptionMatchResult.MATCH;
	}

	// This method is modelled from SearchBuilder.searchForIdsWithAndOr()
	private SubscriptionMatchResult matchIdsWithAndOr(String theParamName, List<List<? extends IQueryParameterType>> theAndOrParams, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		if (theAndOrParams.isEmpty()) {
			return SubscriptionMatchResult.MATCH;
		}

		if (hasQualifiers(theAndOrParams)) {

			return new SubscriptionMatchResult(theParamName, "Qualifiers not supported.");

		}
		if (hasPrefixes(theAndOrParams)) {

			return new SubscriptionMatchResult(theParamName, "Prefixes not supported.");

		}
		if (hasChain(theAndOrParams)) {
			return new SubscriptionMatchResult(theParamName, "Chained references are not supported");
		}
		if (theParamName.equals(IAnyResource.SP_RES_ID)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(IAnyResource.SP_RES_LANGUAGE)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(Constants.PARAM_HAS)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(Constants.PARAM_TAG) || theParamName.equals(Constants.PARAM_PROFILE) || theParamName.equals(Constants.PARAM_SECURITY)) {

			return new SubscriptionMatchResult(theParamName);

		} else {

			String resourceName = theResourceDefinition.getName();
			RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
			return matchResourceParam(theParamName, theAndOrParams, theSearchParams, resourceName, paramDef);
		}
	}

	private SubscriptionMatchResult matchResourceParam(String theParamName, List<List<? extends IQueryParameterType>> theAndOrParams, ResourceIndexedSearchParams theSearchParams, String theResourceName, RuntimeSearchParam theParamDef) {
		if (theParamDef != null) {
			switch (theParamDef.getParamType()) {
				case QUANTITY:
				case TOKEN:
				case STRING:
				case NUMBER:
				case URI:
				case DATE:
				case REFERENCE:
					return new SubscriptionMatchResult(theAndOrParams.stream().anyMatch(nextAnd -> matchParams(theResourceName, theParamName, theParamDef, nextAnd, theSearchParams)));
				case COMPOSITE:
				case HAS:
				case SPECIAL:
				default:
					return new SubscriptionMatchResult(theParamName);
			}
		} else {
			if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
				return new SubscriptionMatchResult(theParamName);
			} else {
				throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + theResourceName);
			}
		}
	}

	private boolean matchParams(String theResourceName, String theParamName, RuntimeSearchParam paramDef, List<? extends IQueryParameterType> theNextAnd, ResourceIndexedSearchParams theSearchParams) {
		return theNextAnd.stream().anyMatch(token -> theSearchParams.matchParam(theResourceName, theParamName, paramDef, token));
	}

	private boolean hasChain(List<List<? extends IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param instanceof ReferenceParam && ((ReferenceParam)param).getChain() != null);
	}

	private boolean hasQualifiers(List<List<? extends IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param.getQueryParameterQualifier() != null);
	}

	private boolean hasPrefixes(List<List<? extends IQueryParameterType>> theAndOrParams) {
		Predicate<IQueryParameterType> hasPrefixPredicate = param -> param instanceof BaseParamWithPrefix &&
			((BaseParamWithPrefix) param).getPrefix() != null;
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(hasPrefixPredicate);
	}
}
