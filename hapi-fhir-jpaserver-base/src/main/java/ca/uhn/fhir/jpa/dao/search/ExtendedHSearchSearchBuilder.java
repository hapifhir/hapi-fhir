/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAMQUALIFIER_MISSING;

/**
 * Search builder for HSearch for token, string, and reference parameters.
 */
public class ExtendedHSearchSearchBuilder {
	public static final String EMPTY_MODIFIER = "";

	/**
	 * These params have complicated semantics, or are best resolved at the JPA layer for now.
	 */
	public static final Set<String> ourUnsafeSearchParmeters = Sets.newHashSet("_id", "_meta");

	/**
	 * Determine if ExtendedHibernateSearchBuilder can support this parameter
	 * @param theParamName param name
	 * @param theActiveParamsForResourceType active search parameters for the desired resource type
	 * @return whether or not this search parameter is supported in hibernate
	 */
	public boolean supportsSearchParameter(String theParamName, ResourceSearchParams theActiveParamsForResourceType) {
		if (theActiveParamsForResourceType == null) {
			return false;
		}
		if (ourUnsafeSearchParmeters.contains(theParamName)) {
			return false;
		}
		if (!theActiveParamsForResourceType.containsParamName(theParamName)) {
			return false;
		}
		return true;
	}

	/**
	 * Are any of the queries supported by our indexing?
	 * -
	 * If not, do not use hibernate, because the results will
	 * be inaccurate and wrong.
	 */
	public boolean canUseHibernateSearch(
			String theResourceType, SearchParameterMap myParams, ISearchParamRegistry theSearchParamRegistry) {
		boolean canUseHibernate = true;

		ResourceSearchParams resourceActiveSearchParams = theSearchParamRegistry.getActiveSearchParams(theResourceType);
		for (String paramName : myParams.keySet()) {
			// is this parameter supported?
			if (!supportsSearchParameter(paramName, resourceActiveSearchParams)) {
				canUseHibernate = false;
			} else {
				// are the parameter values supported?
				canUseHibernate =
						myParams.get(paramName).stream()
								.flatMap(Collection::stream)
								.collect(Collectors.toList())
								.stream()
								.anyMatch(this::isParamTypeSupported);
			}

			// if not supported, don't use
			if (!canUseHibernate) {
				return false;
			}
		}

		return canUseHibernate;
	}

	/**
	 * Are all the queries supported by our indexing?
	 */
	public boolean isSupportsAllOf(SearchParameterMap myParams) {
		return CollectionUtils.isEmpty(myParams.getRevIncludes())
				&& // ???
				CollectionUtils.isEmpty(myParams.getIncludes())
				&& // ???
				myParams.getEverythingMode() == null
				&& // ???
				BooleanUtils.isFalse(myParams.isDeleteExpunge())
				&& // ???

				// not yet supported in HSearch
				myParams.getNearDistanceParam() == null
				&& // ???

				// not yet supported in HSearch
				myParams.getSearchContainedMode() == SearchContainedModeEnum.FALSE
				&& // ???
				myParams.entrySet().stream()
						.filter(e -> !ourUnsafeSearchParmeters.contains(e.getKey()))
						// each and clause may have a different modifier, so split down to the ORs
						.flatMap(andList -> andList.getValue().stream())
						.flatMap(Collection::stream)
						.allMatch(this::isParamTypeSupported);
	}

	/**
	 * Do we support this query param type+modifier?
	 * <p>
	 * NOTE - keep this in sync with addAndConsumeAdvancedQueryClauses() below.
	 */
	private boolean isParamTypeSupported(IQueryParameterType param) {
		String modifier = StringUtils.defaultString(param.getQueryParameterQualifier(), EMPTY_MODIFIER);
		if (param instanceof TokenParam) {
			switch (modifier) {
				case Constants.PARAMQUALIFIER_TOKEN_TEXT:
				case "":
					// we support plain token and token:text
					return true;
				default:
					return false;
			}
		} else if (param instanceof StringParam) {
			switch (modifier) {
					// we support string:text, string:contains, string:exact, and unmodified string.
				case Constants.PARAMQUALIFIER_STRING_TEXT:
				case Constants.PARAMQUALIFIER_STRING_EXACT:
				case Constants.PARAMQUALIFIER_STRING_CONTAINS:
				case EMPTY_MODIFIER:
					return true;
				default:
					return false;
			}
		} else if (param instanceof QuantityParam) {
			return modifier.equals(EMPTY_MODIFIER);

		} else if (param instanceof CompositeParam) {
			switch (modifier) {
				case PARAMQUALIFIER_MISSING:
					return false;
				default:
					return true;
			}

		} else if (param instanceof ReferenceParam) {
			// We cannot search by chain.
			if (((ReferenceParam) param).getChain() != null) {
				return false;
			}
			switch (modifier) {
				case EMPTY_MODIFIER:
					return true;
				case Constants.PARAMQUALIFIER_MDM:
				case Constants.PARAMQUALIFIER_NICKNAME:
				default:
					return false;
			}
		} else if (param instanceof DateParam) {
			return modifier.equals(EMPTY_MODIFIER);

		} else if (param instanceof UriParam) {
			return modifier.equals(EMPTY_MODIFIER);

		} else if (param instanceof NumberParam) {
			return modifier.equals(EMPTY_MODIFIER);

		} else {
			return false;
		}
	}

	public void addAndConsumeAdvancedQueryClauses(
			ExtendedHSearchClauseBuilder theBuilder,
			ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams theMethodParams) {
		SearchParameterMap searchParameterMap = theMethodParams.getSearchParameterMap();
		String resourceType = theMethodParams.getResourceType();
		ISearchParamRegistry searchParamRegistry = theMethodParams.getSearchParamRegistry();

		// copy the keys to avoid concurrent modification error
		ArrayList<String> paramNames = compileParamNames(searchParameterMap);
		ResourceSearchParams activeSearchParams = searchParamRegistry.getActiveSearchParams(resourceType);
		for (String nextParam : paramNames) {
			if (!supportsSearchParameter(nextParam, activeSearchParams)) {
				// ignore magic params handled in JPA
				continue;
			}
			RuntimeSearchParam activeParam = activeSearchParams.get(nextParam);

			// NOTE - keep this in sync with isParamSupported() above.
			switch (activeParam.getParamType()) {
				case TOKEN:
					List<List<IQueryParameterType>> tokenTextAndOrTerms =
							searchParameterMap.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					theBuilder.addStringTextSearch(nextParam, tokenTextAndOrTerms);

					List<List<IQueryParameterType>> tokenUnmodifiedAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addTokenUnmodifiedSearch(nextParam, tokenUnmodifiedAndOrTerms);
					break;

				case STRING:
					List<List<IQueryParameterType>> stringTextAndOrTerms =
							searchParameterMap.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					theBuilder.addStringTextSearch(nextParam, stringTextAndOrTerms);

					List<List<IQueryParameterType>> stringExactAndOrTerms = searchParameterMap.removeByNameAndModifier(
							nextParam, Constants.PARAMQUALIFIER_STRING_EXACT);
					theBuilder.addStringExactSearch(nextParam, stringExactAndOrTerms);

					List<List<IQueryParameterType>> stringContainsAndOrTerms =
							searchParameterMap.removeByNameAndModifier(
									nextParam, Constants.PARAMQUALIFIER_STRING_CONTAINS);
					theBuilder.addStringContainsSearch(nextParam, stringContainsAndOrTerms);

					List<List<IQueryParameterType>> stringAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addStringUnmodifiedSearch(nextParam, stringAndOrTerms);
					break;

				case QUANTITY:
					List<List<IQueryParameterType>> quantityAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addQuantityUnmodifiedSearch(nextParam, quantityAndOrTerms);
					break;

				case REFERENCE:
					List<List<IQueryParameterType>> referenceAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addReferenceUnchainedSearch(nextParam, referenceAndOrTerms);
					break;

				case DATE:
					List<List<IQueryParameterType>> dateAndOrTerms = nextParam.equalsIgnoreCase("_lastupdated")
							? getLastUpdatedAndOrList(searchParameterMap)
							: searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addDateUnmodifiedSearch(nextParam, dateAndOrTerms);
					break;

				case COMPOSITE:
					List<List<IQueryParameterType>> compositeAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					// RuntimeSearchParam only points to the subs by reference.  Resolve here while we have
					// ISearchParamRegistry
					List<RuntimeSearchParam> subSearchParams =
							JpaParamUtil.resolveCompositeComponentsDeclaredOrder(searchParamRegistry, activeParam);
					theBuilder.addCompositeUnmodifiedSearch(activeParam, subSearchParams, compositeAndOrTerms);
					break;

				case URI:
					List<List<IQueryParameterType>> uriUnmodifiedAndOrTerms =
							searchParameterMap.removeByNameUnmodified(nextParam);
					theBuilder.addUriUnmodifiedSearch(nextParam, uriUnmodifiedAndOrTerms);
					break;

				case NUMBER:
					List<List<IQueryParameterType>> numberUnmodifiedAndOrTerms = searchParameterMap.remove(nextParam);
					theBuilder.addNumberUnmodifiedSearch(nextParam, numberUnmodifiedAndOrTerms);
					break;

				default:
					// ignore unsupported param types/modifiers.  They will be processed up in SearchBuilder.
			}
		}
	}

	private List<List<IQueryParameterType>> getLastUpdatedAndOrList(SearchParameterMap theParams) {
		DateParam activeBound = theParams.getLastUpdated().getLowerBound() != null
				? theParams.getLastUpdated().getLowerBound()
				: theParams.getLastUpdated().getUpperBound();

		List<List<IQueryParameterType>> result = List.of(List.of(activeBound));

		// indicate parameter was processed
		theParams.setLastUpdated(null);

		return result;
	}

	/**
	 * Param name list is not only the params.keySet, but also the "special" parameters extracted from input
	 * (as _lastUpdated when the input myLastUpdated field is not null, etc).
	 */
	private ArrayList<String> compileParamNames(SearchParameterMap theParams) {
		ArrayList<String> nameList = Lists.newArrayList(theParams.keySet());

		if (theParams.getLastUpdated() != null) {
			nameList.add("_lastUpdated");
		}

		return nameList;
	}
}
