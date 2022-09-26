package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.util.DateUtils;
import ca.uhn.fhir.util.NumericParamRangeUtil;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.NestedPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.RangePredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.WildcardPredicateOptionsStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_TEXT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_QUANTITY;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_STRING;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_TOKEN;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NUMBER_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_CODE_NORM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE_NORM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.TOKEN_CODE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.TOKEN_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.TOKEN_SYSTEM_CODE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.URI_VALUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExtendedHSearchClauseBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedHSearchClauseBuilder.class);

	private static final double QTY_APPROX_TOLERANCE_PERCENT = .10;
	public static final String PATH_JOINER = ".";

	final FhirContext myFhirContext;
	public final SearchPredicateFactory myRootPredicateFactory;
	public final BooleanPredicateClausesStep<?> myRootClause;
	public final ModelConfig myModelConfig;

	final List<TemporalPrecisionEnum> ordinalSearchPrecisions = Arrays.asList(TemporalPrecisionEnum.YEAR, TemporalPrecisionEnum.MONTH, TemporalPrecisionEnum.DAY);

	public ExtendedHSearchClauseBuilder(FhirContext myFhirContext, ModelConfig theModelConfig,
													BooleanPredicateClausesStep<?> myRootClause, SearchPredicateFactory myPredicateFactory) {
		this.myFhirContext = myFhirContext;
		this.myModelConfig = theModelConfig;
		this.myRootClause = myRootClause;
		this.myRootPredicateFactory = myPredicateFactory;
	}

	/**
	 * Restrict search to resources of a type
	 * @param theResourceType the type to match.  e.g. "Observation"
	 */
	public void addResourceTypeClause(String theResourceType) {
		myRootClause.must(myRootPredicateFactory.match().field("myResourceType").matching(theResourceType));
	}

	@Nonnull
	private Set<String> extractOrStringParams(List<? extends IQueryParameterType> nextAnd) {
		Set<String> terms = new HashSet<>();
		for (IQueryParameterType nextOr : nextAnd) {
			String nextValueTrimmed;
			if (nextOr instanceof StringParam) {
				StringParam nextOrString = (StringParam) nextOr;
				nextValueTrimmed = StringUtils.defaultString(nextOrString.getValue()).trim();
			} else if (nextOr instanceof TokenParam) {
				TokenParam nextOrToken = (TokenParam) nextOr;
				nextValueTrimmed = nextOrToken.getValue();
			} else if (nextOr instanceof ReferenceParam) {
				ReferenceParam referenceParam = (ReferenceParam) nextOr;
				nextValueTrimmed = referenceParam.getValue();
				if (nextValueTrimmed.contains("/_history")) {
					nextValueTrimmed = nextValueTrimmed.substring(0, nextValueTrimmed.indexOf("/_history"));
				}
			} else {
				throw new IllegalArgumentException(Msg.code(1088) + "Unsupported full-text param type: " + nextOr.getClass());
			}
			if (isNotBlank(nextValueTrimmed)) {
				terms.add(nextValueTrimmed);
			}
		}
		return terms;
	}


	/**
	 * Provide an OR wrapper around a list of predicates.
	 *
	 * Wrap the predicates under a bool as should clauses with minimumShouldMatch=1 for OR semantics.
	 * As an optimization, when there is only one clause, we avoid the redundant boolean wrapper
	 * and return the first item as is.
	 *
	 * @param theOrList a list containing at least 1 predicate
	 * @param thePredicateFactory the current context - the root, or a nested parent.
	 * @return a predicate providing or-semantics over the list.
	 */
	private PredicateFinalStep orPredicateOrSingle(List<? extends PredicateFinalStep> theOrList, SearchPredicateFactory thePredicateFactory) {
		PredicateFinalStep finalClause;
		if (theOrList.size() == 1) {
			finalClause = theOrList.get(0);
		} else {
			BooleanPredicateClausesStep<?> orClause = thePredicateFactory.bool();
			orClause.minimumShouldMatchNumber(1);
			theOrList.forEach(orClause::should);
			finalClause = orClause;
		}
		return finalClause;
	}

	public void addTokenUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theAndOrTerms) {
		if (CollectionUtils.isEmpty(theAndOrTerms)) {
			return;
		}
		for (List<? extends IQueryParameterType> nextAnd : theAndOrTerms) {

			ourLog.debug("addTokenUnmodifiedSearch {} {}", theSearchParamName, nextAnd);
			String spPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName);
			List<? extends PredicateFinalStep> clauses = nextAnd.stream()
				.map(orTerm -> buildTokenUnmodifiedMatchOn(spPath, orTerm))
				.collect(Collectors.toList());
			PredicateFinalStep finalClause = orPredicateOrSingle(clauses, myRootPredicateFactory);

			myRootClause.must(finalClause);
		}

	}

	private PredicateFinalStep buildTokenUnmodifiedMatchOn(String thePathPrefix, IQueryParameterType orTerm) {
		if (orTerm instanceof TokenParam) {
			TokenParam token = (TokenParam) orTerm;
			if (StringUtils.isBlank(token.getSystem())) {
				// bare value
				return myRootPredicateFactory.match().field(joinPath(thePathPrefix, INDEX_TYPE_TOKEN, TOKEN_CODE)).matching(token.getValue());
			} else if (StringUtils.isBlank(token.getValue())) {
				// system without value
				return myRootPredicateFactory.match().field(joinPath(thePathPrefix, INDEX_TYPE_TOKEN,  TOKEN_SYSTEM)).matching(token.getSystem());
			} else {
				// system + value
				return myRootPredicateFactory.match().field(joinPath(thePathPrefix, INDEX_TYPE_TOKEN,  TOKEN_SYSTEM_CODE)).matching(token.getValueAsQueryToken(this.myFhirContext));
			}
		} else if (orTerm instanceof StringParam) {
			// MB I don't quite understand why FhirResourceDaoR4SearchNoFtTest.testSearchByIdParamWrongType() uses String but here we are
			StringParam string = (StringParam) orTerm;
			// treat a string as a code with no system (like _id)
			return myRootPredicateFactory.match().field(joinPath(thePathPrefix, INDEX_TYPE_TOKEN,  TOKEN_CODE)).matching(string.getValue());
		} else {
			throw new IllegalArgumentException(Msg.code(1089) + "Unexpected param type for token search-param: " + orTerm.getClass().getName());
		}
	}

	public void addStringTextSearch(String theSearchParamName, List<List<IQueryParameterType>> stringAndOrTerms) {
		if (CollectionUtils.isEmpty(stringAndOrTerms)) {
			return;
		}
		String fieldName;
		switch (theSearchParamName) {
			// _content and _text were here first, and don't obey our mapping.
			// Leave them as-is for backwards compatibility.
			case Constants.PARAM_CONTENT:
				fieldName = "myContentText";
				break;
			case Constants.PARAM_TEXT:
				fieldName = "myNarrativeText";
				break;
			default:
				fieldName = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, INDEX_TYPE_STRING, IDX_STRING_TEXT);
				break;
		}

		for (List<? extends IQueryParameterType> nextAnd : stringAndOrTerms) {
			Set<String> terms = TermHelper.makePrefixSearchTerm(extractOrStringParams(nextAnd));
			ourLog.debug("addStringTextSearch {}, {}", theSearchParamName, terms);
			if (!terms.isEmpty()) {
				String query = terms.stream()
					.map(s -> "( " + s + " )")
					.collect(Collectors.joining(" | "));
				myRootClause.must(myRootPredicateFactory
					.simpleQueryString()
					.field(fieldName)
					.matching(query)
					.defaultOperator(BooleanOperator.AND)); // term value may contain multiple tokens.  Require all of them to be present.
			} else {
				ourLog.warn("No Terms found in query parameter {}", nextAnd);
			}
		}
	}


	public void addStringExactSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, INDEX_TYPE_STRING, IDX_STRING_EXACT);

		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringExactSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s -> myRootPredicateFactory.match().field(fieldPath).matching(s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms, myRootPredicateFactory));
		}
	}

	public void addStringContainsSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, INDEX_TYPE_STRING, IDX_STRING_NORMALIZED);
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringContainsSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
				.map(this::normalize)
				.map(s -> myRootPredicateFactory
					.wildcard().field(fieldPath)
					.matching("*" + s + "*"))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms, myRootPredicateFactory));
		}
	}

	/**
	 * Normalize the string to match our standardAnalyzer.
	 * @see HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer#STANDARD_ANALYZER
	 *
	 * @param theString the raw string
	 * @return a case and accent normalized version of the input
	 */
	@Nonnull
	private String normalize(String theString) {
		return StringUtil.normalizeStringForSearchIndexing(theString).toLowerCase(Locale.ROOT);
	}

	public void addStringUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String spPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName);
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringUnmodifiedSearch {} {}", theSearchParamName, terms);
			List<PredicateFinalStep> orTerms = terms.stream()
				.map(s ->
					buildStringUnmodifiedClause(spPath, s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms, myRootPredicateFactory));
		}
	}

	private WildcardPredicateOptionsStep<?> buildStringUnmodifiedClause(String theSPPath, String theString) {
		return myRootPredicateFactory.wildcard()
			.field(joinPath(theSPPath, INDEX_TYPE_STRING, IDX_STRING_NORMALIZED))
			// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the normStringAnalyzer
			.matching(normalize(theString) + "*");
	}

	public void addReferenceUnchainedSearch(String theSearchParamName, List<List<IQueryParameterType>> theReferenceAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, "reference", "value");
		for (List<? extends IQueryParameterType> nextAnd : theReferenceAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.trace("reference unchained search {}", terms);

			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s -> myRootPredicateFactory.match().field(fieldPath).matching(s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms, myRootPredicateFactory));
		}
	}

	/**
	 * Create date clause from date params. The date lower and upper bounds are taken
	 * into considertion when generating date query ranges
	 *
	 * <p>Example 1 ('eq' prefix/empty): <code>http://fhirserver/Observation?date=eq2020</code>
	 * would generate the following search clause
	 * <pre>
	 * {@code
	 * {
	 *  "bool": {
	 *    "must": [{
	 *      "range": {
	 *        "sp.date.dt.lower-ord": { "gte": "20200101" }
	 *      }
	 *    }, {
	 *      "range": {
	 *        "sp.date.dt.upper-ord": { "lte": "20201231" }
	 *      }
	 *    }]
	 *  }
	 * }
	 * }
	 * </pre>
	 *
	 * <p>Example 2 ('gt' prefix): <code>http://fhirserver/Observation?date=gt2020-01-01T08:00:00.000</code>
	 * <p>No timezone in the query will be taken as localdatetime(for e.g MST/UTC-07:00 in this case) converted to UTC before comparison</p>
	 * <pre>
	 * {@code
	 * {
	 *   "range":{
	 *     "sp.date.dt.upper":{ "gt": "2020-01-01T15:00:00.000000000Z" }
	 *   }
	 * }
	 * }
	 * </pre>
	 *
	 * <p>Example 3 between dates: <code>http://fhirserver/Observation?date=ge2010-01-01&date=le2020-01</code></p>
	 * <pre>
	 * {@code
	 * {
	 *   "range":{
	 *     "sp.date.dt.upper-ord":{ "gte":"20100101" }
	 *   },
	 *   "range":{
	 *     "sp.date.dt.lower-ord":{ "lte":"20200101" }
	 *   }
	 * }
	 * }
	 * </pre>
	 *
	 * <p>Example 4 not equal: <code>http://fhirserver/Observation?date=ne2021</code></p>
	 * <pre>
	 * {@code
	 * {
	 *    "bool": {
	 *       "should": [{
	 *          "range": {
	 *             "sp.date.dt.upper-ord": { "lt": "20210101" }
	 *          }
	 *       }, {
	 *          "range": {
	 *             "sp.date.dt.lower-ord": { "gt": "20211231" }
	 *          }
	 *       }],
	 *       "minimum_should_match": "1"
	 *    }
	 * }
	 * }
	 * </pre>
	 *
	 * @param theSearchParamName e.g code
	 * @param theDateAndOrTerms The and/or list of DateParam values
	 *
	 * buildDateTermClause(subComponentPath, value);
	 */
	public void addDateUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theDateAndOrTerms) {
		for (List<? extends IQueryParameterType> nextOrList : theDateAndOrTerms) {

			String spPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName);

			List<PredicateFinalStep> clauses = nextOrList.stream()
				.map(d -> buildDateTermClause(spPath, d))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(clauses, myRootPredicateFactory));
		}
	}

	private PredicateFinalStep buildDateTermClause(String thePath, IQueryParameterType theQueryParameter) {
		DateParam dateParam = (DateParam) theQueryParameter;
		boolean isOrdinalSearch = ordinalSearchPrecisions.contains(dateParam.getPrecision());
		return isOrdinalSearch
			? generateDateOrdinalSearchTerms(thePath, dateParam)
			: generateDateInstantSearchTerms(thePath, dateParam);
	}

	private PredicateFinalStep generateDateOrdinalSearchTerms(String spPath, DateParam theDateParam) {
		String lowerOrdinalField = joinPath(spPath, "dt", "lower-ord");
		String upperOrdinalField = joinPath(spPath, "dt", "upper-ord");
		int lowerBoundAsOrdinal;
		int upperBoundAsOrdinal;
		ParamPrefixEnum prefix = theDateParam.getPrefix();

		// default when handling 'Day' temporal types
		lowerBoundAsOrdinal = upperBoundAsOrdinal = DateUtils.convertDateToDayInteger(theDateParam.getValue());
		TemporalPrecisionEnum precision = theDateParam.getPrecision();
		// complete the date from 'YYYY' and 'YYYY-MM' temporal types
		if (precision == TemporalPrecisionEnum.YEAR || precision == TemporalPrecisionEnum.MONTH) {
			Pair<String, String> completedDate = DateUtils.getCompletedDate(theDateParam.getValueAsString());
			lowerBoundAsOrdinal = Integer.parseInt(completedDate.getLeft().replace("-", ""));
			upperBoundAsOrdinal = Integer.parseInt(completedDate.getRight().replace("-", ""));
		}

		if (Objects.isNull(prefix) || prefix == ParamPrefixEnum.EQUAL) {
			// For equality prefix we would like the date to fall between the lower and upper bound
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myRootPredicateFactory.range().field(lowerOrdinalField).atLeast(lowerBoundAsOrdinal),
				myRootPredicateFactory.range().field(upperOrdinalField).atMost(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = myRootPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			// TODO JB: more fine tuning needed for STARTS_AFTER
			return myRootPredicateFactory.range().field(upperOrdinalField).greaterThan(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return myRootPredicateFactory.range().field(upperOrdinalField).atLeast(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			// TODO JB: more fine tuning needed for END_BEFORE
			return myRootPredicateFactory.range().field(lowerOrdinalField).lessThan(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return myRootPredicateFactory.range().field(lowerOrdinalField).atMost(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myRootPredicateFactory.range().field(upperOrdinalField).lessThan(lowerBoundAsOrdinal),
				myRootPredicateFactory.range().field(lowerOrdinalField).greaterThan(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = myRootPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}
		throw new IllegalArgumentException(Msg.code(2025) + "Date search param does not support prefix of type: " + prefix);
	}

	private PredicateFinalStep generateDateInstantSearchTerms(String spPath, DateParam theDateParam) {
		String lowerInstantField = joinPath(spPath, "dt", "lower");
		String upperInstantField = joinPath(spPath, "dt", "upper");
		final ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(theDateParam.getPrefix(), ParamPrefixEnum.EQUAL);

		if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			Instant dateInstant = theDateParam.getValue().toInstant();
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myRootPredicateFactory.range().field(upperInstantField).lessThan(dateInstant),
				myRootPredicateFactory.range().field(lowerInstantField).greaterThan(dateInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = myRootPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}

		// Consider lower and upper bounds for building range predicates
		DateRangeParam dateRange = new DateRangeParam(theDateParam);
		Instant lowerBoundAsInstant = Optional.ofNullable(dateRange.getLowerBound()).map(param -> param.getValue().toInstant()).orElse(null);
		Instant upperBoundAsInstant = Optional.ofNullable(dateRange.getUpperBound()).map(param -> param.getValue().toInstant()).orElse(null);

		if (prefix == ParamPrefixEnum.EQUAL) {
			// For equality prefix we would like the date to fall between the lower and upper bound
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myRootPredicateFactory.range().field(lowerInstantField).atLeast(lowerBoundAsInstant),
				myRootPredicateFactory.range().field(upperInstantField).atMost(upperBoundAsInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = myRootPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			return myRootPredicateFactory.range().field(upperInstantField).greaterThan(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return myRootPredicateFactory.range().field(upperInstantField).atLeast(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			return myRootPredicateFactory.range().field(lowerInstantField).lessThan(upperBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return myRootPredicateFactory.range().field(lowerInstantField).atMost(upperBoundAsInstant);
		}

		throw new IllegalArgumentException(Msg.code(2026) + "Date search param does not support prefix of type: " + prefix);
	}


	/**
	 * Differences with DB search:
	 *  _ is not all-normalized-or-all-not. Each parameter is applied on quantity or normalized quantity depending on UCUM fitness
	 *  _ respects ranges for equal and approximate qualifiers
	 *
	 * Strategy: For each parameter, if it can be canonicalized, it is, and used against 'normalized-value-quantity' index
	 * 	otherwise it is applied as-is to 'value-quantity'
	 */
	public void addQuantityUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theQuantityAndOrTerms) {

		String nestedRootPath = joinPath(NESTED_SEARCH_PARAM_ROOT, theSearchParamName);

		for (List<IQueryParameterType> nextOrList : theQuantityAndOrTerms) {
			NestedPredicateOptionsStep<?> orListPredicate = myRootPredicateFactory
				.nested().objectField(nestedRootPath)
				.nest(nestedRootPredicateFactory -> {
						List<PredicateFinalStep> orClauses = nextOrList.stream()
							.map(quantityTerm -> buildQuantityTermClause(nestedRootPath, quantityTerm, nestedRootPredicateFactory))
							.collect(Collectors.toList());

					return orPredicateOrSingle(orClauses, nestedRootPredicateFactory);
					});

			myRootClause.must(orListPredicate);
		}
	}

	private BooleanPredicateClausesStep<?> buildQuantityTermClause(String theContextPath, IQueryParameterType theQueryParameter, SearchPredicateFactory theContextPredicateFactory) {

		BooleanPredicateClausesStep<?> quantityClause = theContextPredicateFactory.bool();

		QuantityParam qtyParam = QuantityParam.toQuantityParam(theQueryParameter);
		ParamPrefixEnum activePrefix = qtyParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : qtyParam.getPrefix();
		String quantityElement = joinPath(theContextPath, INDEX_TYPE_QUANTITY);

		if (myModelConfig.getNormalizedQuantitySearchLevel() == NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED) {
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull(qtyParam);
			if (canonicalQty != null) {
				String valueFieldPath = joinPath(quantityElement, QTY_VALUE_NORM);

				quantityClause.must(buildNumericClause(valueFieldPath, activePrefix, canonicalQty.getValue(), theContextPredicateFactory));
				quantityClause.must(theContextPredicateFactory.match()
					.field(joinPath(quantityElement, QTY_CODE_NORM))
					.matching(canonicalQty.getUnits()));
				return quantityClause;
			}
		}

		String valueFieldPath = joinPath(quantityElement, QTY_VALUE);

		quantityClause.must(buildNumericClause(valueFieldPath, activePrefix, qtyParam.getValue(), theContextPredicateFactory));

		if ( isNotBlank(qtyParam.getSystem()) ) {
			quantityClause.must(
				theContextPredicateFactory.match()
					.field(joinPath(quantityElement, QTY_SYSTEM)).matching(qtyParam.getSystem()) );
		}

		if ( isNotBlank(qtyParam.getUnits()) ) {
			quantityClause.must(
				theContextPredicateFactory.match()
					.field(joinPath(quantityElement, QTY_CODE)).matching(qtyParam.getUnits()) );
		}

		return quantityClause;
	}


	/**
	 * Shared helper between quantity and number
	 * @param valueFieldPath The path leading to index node
	 * @param thePrefix the query prefix (e.g. lt).  Null means eq
	 * @param theNumberValue the query value
	 * @param theContextPredicateFactory HSearch builder
	 * @return a query predicate applying the prefix to the value
	 */
	@Nonnull
	private PredicateFinalStep buildNumericClause(String valueFieldPath, ParamPrefixEnum thePrefix, BigDecimal theNumberValue, SearchPredicateFactory theContextPredicateFactory) {
		PredicateFinalStep predicate = null;

		double value = theNumberValue.doubleValue();
		Pair<BigDecimal, BigDecimal> range = NumericParamRangeUtil.getRange(theNumberValue);
		double approxTolerance = value * QTY_APPROX_TOLERANCE_PERCENT;

		ParamPrefixEnum activePrefix = thePrefix == null ? ParamPrefixEnum.EQUAL : thePrefix;
		switch (activePrefix) {
			//	searches for resource quantity between passed param value +/- 10%
			case APPROXIMATE:
				predicate = theContextPredicateFactory.range().field(valueFieldPath)
					.between(value-approxTolerance, value+approxTolerance);
				break;

			// searches for resource quantity between passed param value +/- 5%
			case EQUAL:
				predicate  = theContextPredicateFactory.range().field(valueFieldPath)
					.between(range.getLeft().doubleValue(), range.getRight().doubleValue());
				break;

			// searches for resource quantity > param value
			case GREATERTHAN:
			case STARTS_AFTER:  // treated as GREATERTHAN because search doesn't handle ranges
				predicate  = theContextPredicateFactory.range().field(valueFieldPath).greaterThan(value);
				break;

			// searches for resource quantity not < param value
			case GREATERTHAN_OR_EQUALS:
				predicate  = theContextPredicateFactory.range().field(valueFieldPath).atLeast(value);
				break;

			// searches for resource quantity < param value
			case LESSTHAN:
			case ENDS_BEFORE:  // treated as LESSTHAN because search doesn't handle ranges
				predicate  = theContextPredicateFactory.range().field(valueFieldPath).lessThan(value);
				break;

			// searches for resource quantity not > param value
			case LESSTHAN_OR_EQUALS:
				predicate  = theContextPredicateFactory.range().field(valueFieldPath).atMost(value);
				break;

			// NOT_EQUAL: searches for resource quantity not between passed param value +/- 5%
			case NOT_EQUAL:
				RangePredicateOptionsStep<?> negRange = theContextPredicateFactory.range()
					.field(valueFieldPath).between(range.getLeft().doubleValue(), range.getRight().doubleValue());
				predicate = theContextPredicateFactory.bool().mustNot(negRange);
				break;
		}
		Validate.notNull(predicate, "Unsupported prefix: %s", thePrefix);
		return predicate;
	}


	public void addUriUnmodifiedSearch(String theParamName, List<List<IQueryParameterType>> theUriUnmodifiedAndOrTerms) {
		String spContext = joinPath(SEARCH_PARAM_ROOT, theParamName);
		for (List<IQueryParameterType> nextOrList : theUriUnmodifiedAndOrTerms) {

			PredicateFinalStep orListPredicate = buildURIClause(spContext, nextOrList);

			myRootClause.must(orListPredicate);
		}
	}

	private PredicateFinalStep buildURIClause(String spContext, List<IQueryParameterType> theOrList) {
		List<String> orTerms = theOrList.stream()
			.map(p -> ((UriParam) p).getValue())
			.collect(Collectors.toList());

		return myRootPredicateFactory.terms()
			.field(joinPath(spContext, URI_VALUE))
			.matchingAny(orTerms);
	}

	public void addNumberUnmodifiedSearch(String theParamName, List<List<IQueryParameterType>> theNumberUnmodifiedAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theParamName, NUMBER_VALUE);

		for (List<IQueryParameterType> nextOrList : theNumberUnmodifiedAndOrTerms) {
			List<PredicateFinalStep> orTerms = nextOrList.stream()
				.map(NumberParam.class::cast)
				.map(orTerm -> buildNumericClause(fieldPath, orTerm.getPrefix(), orTerm.getValue(), myRootPredicateFactory))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms, myRootPredicateFactory));
		}
	}

	private PredicateFinalStep buildNumericClause(String theSubComponentPath, IQueryParameterType theValue, SearchPredicateFactory theContextPredicateFactory) {
		NumberParam p = (NumberParam) theValue;

		return buildNumericClause(joinPath(theSubComponentPath, NUMBER_VALUE), p.getPrefix(), p.getValue(), theContextPredicateFactory);
	}

	public void addCompositeUnmodifiedSearch(RuntimeSearchParam theSearchParam, List<RuntimeSearchParam> theSubSearchParams, List<List<IQueryParameterType>> theCompositeAndOrTerms) {
		for (List<IQueryParameterType> nextOrList : theCompositeAndOrTerms) {
			String nestedRootPath = joinPath(NESTED_SEARCH_PARAM_ROOT, theSearchParam.getName());

			// The index data for each extracted element is stored in a separate nested HSearch document.
			// Create a nested parent node for all component predicates.
			// Each can share this nested beacuse all nested docs share a parent id.
			NestedPredicateOptionsStep<?> orListPredicate = myRootPredicateFactory
				.nested().objectField(nestedRootPath)
				.nest(nestedRootPredicateFactory -> {
					List<PredicateFinalStep> orClauses =
						nextOrList.stream()
							.map(term -> computeCompositeTermClause(theSearchParam, theSubSearchParams, (CompositeParam<?,?>) term, nestedRootPredicateFactory))
							.collect(Collectors.toList());

					return orPredicateOrSingle(orClauses, nestedRootPredicateFactory);
				});

			myRootClause.must(orListPredicate);

		}
	}

	/**
	 * Compute the match clause for all the components of theCompositeQueryParam.
	 *
	 * @param theSearchParam The composite SP
	 * @param theSubSearchParams the composite component SPs
	 * @param theCompositeQueryParam the query param values
	 * @param theContextPredicateFactory HSearch infrastructure
	 */
	private PredicateFinalStep computeCompositeTermClause(RuntimeSearchParam theSearchParam, List<RuntimeSearchParam> theSubSearchParams, CompositeParam<?,?> theCompositeQueryParam, SearchPredicateFactory theContextPredicateFactory) {
		Validate.notNull(theSearchParam);
		Validate.notNull(theSubSearchParams);
		Validate.notNull(theCompositeQueryParam);
		Validate.isTrue(theSubSearchParams.size() == 2, "Hapi only supports composite search parameters with 2 components. %s %d", theSearchParam.getName(), theSubSearchParams.size());
		List<IQueryParameterType> values = theCompositeQueryParam.getValues();
		Validate.isTrue(theSubSearchParams.size() == values.size(), "Different number of query components than defined. %s %d %d", theSearchParam.getName(), theSubSearchParams.size(), values.size());

		// The index data for each extracted element is stored in a separate nested HSearch document.

		// Create a nested parent node for all component predicates.
		String nestedRootPath = joinPath(NESTED_SEARCH_PARAM_ROOT, theSearchParam.getName());
		BooleanPredicateClausesStep<?> compositeClause = theContextPredicateFactory.bool();
		for (int i = 0; i < theSubSearchParams.size(); i += 1) {
			RuntimeSearchParam component = theSubSearchParams.get(i);
			IQueryParameterType value = values.get(i);
			PredicateFinalStep subMatch = null;
			String subComponentPath = joinPath(nestedRootPath, component.getName());
			switch (component.getParamType()) {
				case DATE:
					subMatch = buildDateTermClause(subComponentPath, value);
					break;
				case STRING:
					subMatch = buildStringUnmodifiedClause(subComponentPath, value.getValueAsQueryToken(myFhirContext));
					break;
				case TOKEN:
					subMatch = buildTokenUnmodifiedMatchOn(subComponentPath, value);
					break;
				case QUANTITY:
					subMatch = buildQuantityTermClause(subComponentPath, value, theContextPredicateFactory);
					break;
				case URI:
					// wipmb neat - we could combine subComponentPath with predicate factory
					subMatch = buildURIClause(subComponentPath, List.of(value));
					break;
				case NUMBER:
					subMatch = buildNumericClause(subComponentPath, value, theContextPredicateFactory);
					break;
				case REFERENCE:
				// wipmb Can we use reference in composite?

				default:
					break;

			}

			Validate.notNull(subMatch, "Unsupported composite type in %s: %s %s", theSearchParam.getName(), component.getName(), component.getParamType());
			compositeClause.must(subMatch);
		}

		return compositeClause;
	}

	// HSearch uses a dotted path
	// Some private static helpers that can be inlined.
	@Nonnull
	private static String joinPath(String thePath0, String thePath1) {
		return thePath0 + PATH_JOINER + thePath1;
	}

	private static String joinPath(String thePath0, String thePath1, String thePath2) {
		return thePath0 + PATH_JOINER + thePath1 + PATH_JOINER + thePath2;
	}

	@Nonnull
	private static String joinPath(String thePath0, String thePath1, String thePath2, String thePath3) {
		return thePath0 + PATH_JOINER + thePath1 + PATH_JOINER + thePath2 + PATH_JOINER + thePath3;
	}

}
