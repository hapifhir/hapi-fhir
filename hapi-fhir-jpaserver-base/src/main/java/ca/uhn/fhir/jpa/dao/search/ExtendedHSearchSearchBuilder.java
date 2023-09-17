/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
	 * Are any of the queries supported by our indexing?
	 */
	public boolean isSupportsSomeOf(SearchParameterMap myParams) {
		return myParams.getSort() != null
				|| myParams.getLastUpdated() != null
				|| myParams.entrySet().stream()
						.filter(e -> !ourUnsafeSearchParmeters.contains(e.getKey()))
						// each and clause may have a different modifier, so split down to the ORs
						.flatMap(andList -> andList.getValue().stream())
						.flatMap(Collection::stream)
						.anyMatch(this::isParamTypeSupported);
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
			ExtendedHSearchClauseBuilder builder,
			String theResourceType,
			SearchParameterMap theParams,
			ISearchParamRegistry theSearchParamRegistry) {
		// copy the keys to avoid concurrent modification error
		ArrayList<String> paramNames = compileParamNames(theParams);
		for (String nextParam : paramNames) {
			if (ourUnsafeSearchParmeters.contains(nextParam)) {
				continue;
			}
			RuntimeSearchParam activeParam = theSearchParamRegistry.getActiveSearchParam(theResourceType, nextParam);
			if (activeParam == null) {
				// ignore magic params handled in JPA
				continue;
			}

			// NOTE - keep this in sync with isParamSupported() above.
			switch (activeParam.getParamType()) {
				case TOKEN:
					List<List<IQueryParameterType>> tokenTextAndOrTerms =
							theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					builder.addStringTextSearch(nextParam, tokenTextAndOrTerms);

					List<List<IQueryParameterType>> tokenUnmodifiedAndOrTerms =
							theParams.removeByNameUnmodified(nextParam);
					builder.addTokenUnmodifiedSearch(nextParam, tokenUnmodifiedAndOrTerms);
					break;

				case STRING:
					List<List<IQueryParameterType>> stringTextAndOrTerms =
							theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					builder.addStringTextSearch(nextParam, stringTextAndOrTerms);

					List<List<IQueryParameterType>> stringExactAndOrTerms =
							theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_EXACT);
					builder.addStringExactSearch(nextParam, stringExactAndOrTerms);

					List<List<IQueryParameterType>> stringContainsAndOrTerms =
							theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_CONTAINS);
					builder.addStringContainsSearch(nextParam, stringContainsAndOrTerms);

					List<List<IQueryParameterType>> stringAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addStringUnmodifiedSearch(nextParam, stringAndOrTerms);
					break;

				case QUANTITY:
					List<List<IQueryParameterType>> quantityAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addQuantityUnmodifiedSearch(nextParam, quantityAndOrTerms);
					break;

				case REFERENCE:
					List<List<IQueryParameterType>> referenceAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addReferenceUnchainedSearch(nextParam, referenceAndOrTerms);
					break;

				case DATE:
					List<List<IQueryParameterType>> dateAndOrTerms = nextParam.equalsIgnoreCase("_lastupdated")
							? getLastUpdatedAndOrList(theParams)
							: theParams.removeByNameUnmodified(nextParam);
					builder.addDateUnmodifiedSearch(nextParam, dateAndOrTerms);
					break;

				case COMPOSITE:
					List<List<IQueryParameterType>> compositeAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					// RuntimeSearchParam only points to the subs by reference.  Resolve here while we have
					// ISearchParamRegistry
					List<RuntimeSearchParam> subSearchParams =
							JpaParamUtil.resolveCompositeComponentsDeclaredOrder(theSearchParamRegistry, activeParam);
					builder.addCompositeUnmodifiedSearch(activeParam, subSearchParams, compositeAndOrTerms);
					break;

				case URI:
					List<List<IQueryParameterType>> uriUnmodifiedAndOrTerms =
							theParams.removeByNameUnmodified(nextParam);
					builder.addUriUnmodifiedSearch(nextParam, uriUnmodifiedAndOrTerms);
					break;

				case NUMBER:
					List<List<IQueryParameterType>> numberUnmodifiedAndOrTerms = theParams.remove(nextParam);
					builder.addNumberUnmodifiedSearch(nextParam, numberUnmodifiedAndOrTerms);
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
