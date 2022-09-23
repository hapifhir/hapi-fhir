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
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NUMBER_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_CODE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_CODE_NORM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_IDX_NAME;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_SYSTEM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.QTY_VALUE_NORM;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.URI_VALUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExtendedHSearchClauseBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedHSearchClauseBuilder.class);

	private static final double QTY_APPROX_TOLERANCE_PERCENT = .10;

	final FhirContext myFhirContext;
	public final SearchPredicateFactory myPredicateFactory;
	public final BooleanPredicateClausesStep<?> myRootClause;
	public final ModelConfig myModelConfig;

	final List<TemporalPrecisionEnum> ordinalSearchPrecisions = Arrays.asList(TemporalPrecisionEnum.YEAR, TemporalPrecisionEnum.MONTH, TemporalPrecisionEnum.DAY);

	public ExtendedHSearchClauseBuilder(FhirContext myFhirContext, ModelConfig theModelConfig,
													BooleanPredicateClausesStep<?> myRootClause, SearchPredicateFactory myPredicateFactory) {
		this.myFhirContext = myFhirContext;
		this.myModelConfig = theModelConfig;
		this.myRootClause = myRootClause;
		this.myPredicateFactory = myPredicateFactory;
	}

	/**
	 * Restrict search to resources of a type
	 * @param theResourceType the type to match.  e.g. "Observation"
	 */
	public void addResourceTypeClause(String theResourceType) {
		myRootClause.must(myPredicateFactory.match().field("myResourceType").matching(theResourceType));
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
	 * @return a predicate providing or-semantics over the list.
	 */
	private PredicateFinalStep orPredicateOrSingle(List<? extends PredicateFinalStep> theOrList) {
		PredicateFinalStep finalClause;
		if (theOrList.size() == 1) {
			finalClause = theOrList.get(0);
		} else {
			BooleanPredicateClausesStep<?> orClause = myPredicateFactory.bool();
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
			String spPath = SEARCH_PARAM_ROOT + "." + theSearchParamName;
			List<? extends PredicateFinalStep> clauses = nextAnd.stream()
				.map(orTerm -> buildTokenUnmodifiedMatchOn(spPath, orTerm))
				.collect(Collectors.toList());
			PredicateFinalStep finalClause = orPredicateOrSingle(clauses);

			myRootClause.must(finalClause);
		}

	}

	private PredicateFinalStep buildTokenUnmodifiedMatchOn(String thePathPrefix, IQueryParameterType orTerm) {
		if (orTerm instanceof TokenParam) {
			TokenParam token = (TokenParam) orTerm;
			if (StringUtils.isBlank(token.getSystem())) {
				// bare value
				return myPredicateFactory.match().field(thePathPrefix + ".token" + ".code").matching(token.getValue());
			} else if (StringUtils.isBlank(token.getValue())) {
				// system without value
				return myPredicateFactory.match().field(thePathPrefix + ".token" + ".system").matching(token.getSystem());
			} else {
				// system + value
				return myPredicateFactory.match().field(thePathPrefix + ".token" + ".code-system").matching(token.getValueAsQueryToken(this.myFhirContext));
			}
		} else if (orTerm instanceof StringParam) {
			// MB I don't quite understand why FhirResourceDaoR4SearchNoFtTest.testSearchByIdParamWrongType() uses String but here we are
			StringParam string = (StringParam) orTerm;
			// treat a string as a code with no system (like _id)
			return myPredicateFactory.match().field(thePathPrefix + ".token" + ".code").matching(string.getValue());
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
				fieldName = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_TEXT;
				break;
		}

		for (List<? extends IQueryParameterType> nextAnd : stringAndOrTerms) {
			Set<String> terms = TermHelper.makePrefixSearchTerm(extractOrStringParams(nextAnd));
			ourLog.debug("addStringTextSearch {}, {}", theSearchParamName, terms);
			if (!terms.isEmpty()) {
				String query = terms.stream()
					.map(s -> "( " + s + " )")
					.collect(Collectors.joining(" | "));
				myRootClause.must(myPredicateFactory
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
		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_EXACT;

		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringExactSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s -> myPredicateFactory.match().field(fieldPath).matching(s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
		}
	}

	public void addStringContainsSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_NORMALIZED;
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringContainsSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
				.map(s-> normalize(s))
				.map(s -> myPredicateFactory
					.wildcard().field(fieldPath)
					.matching("*" + s + "*"))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
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
		String spPath = SEARCH_PARAM_ROOT + "." + theSearchParamName;
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringUnmodifiedSearch {} {}", theSearchParamName, terms);
			List<PredicateFinalStep> orTerms = terms.stream()
				.map(s ->
					buildStringUnmodifiedClause(spPath, s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
		}
	}

	private WildcardPredicateOptionsStep<?> buildStringUnmodifiedClause(String theSPPath, String theString) {
		return myPredicateFactory.wildcard()
			.field(theSPPath + ".string." + IDX_STRING_NORMALIZED)
			// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the normStringAnalyzer
			.matching(normalize(theString) + "*");
	}

	public void addReferenceUnchainedSearch(String theSearchParamName, List<List<IQueryParameterType>> theReferenceAndOrTerms) {
		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".reference.value";
		for (List<? extends IQueryParameterType> nextAnd : theReferenceAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.trace("reference unchained search {}", terms);

			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s -> myPredicateFactory.match().field(fieldPath).matching(s))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
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

			String spPath = SEARCH_PARAM_ROOT + "." + theSearchParamName;

			List<PredicateFinalStep> clauses = nextOrList.stream()
				.map(d -> buildDateTermClause(spPath, d))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(clauses));
		}
	}

	private PredicateFinalStep buildDateTermClause(String thePath, IQueryParameterType theParam) {
		DateParam dateParam = (DateParam) theParam;
		boolean isOrdinalSearch = ordinalSearchPrecisions.contains(dateParam.getPrecision());
		PredicateFinalStep searchPredicate = isOrdinalSearch
			? generateDateOrdinalSearchTerms(thePath, dateParam)
			: generateDateInstantSearchTerms(thePath, dateParam);
		return searchPredicate;
	}

	private PredicateFinalStep generateDateOrdinalSearchTerms(String spPath, DateParam theDateParam) {
		String lowerOrdinalField = spPath + ".dt.lower-ord";
		String upperOrdinalField = spPath + ".dt.upper-ord";
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
				myPredicateFactory.range().field(lowerOrdinalField).atLeast(lowerBoundAsOrdinal),
				myPredicateFactory.range().field(upperOrdinalField).atMost(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = myPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			// TODO JB: more fine tuning needed for STARTS_AFTER
			return myPredicateFactory.range().field(upperOrdinalField).greaterThan(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return myPredicateFactory.range().field(upperOrdinalField).atLeast(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			// TODO JB: more fine tuning needed for END_BEFORE
			return myPredicateFactory.range().field(lowerOrdinalField).lessThan(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return myPredicateFactory.range().field(lowerOrdinalField).atMost(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myPredicateFactory.range().field(upperOrdinalField).lessThan(lowerBoundAsOrdinal),
				myPredicateFactory.range().field(lowerOrdinalField).greaterThan(upperBoundAsOrdinal)
			);
			BooleanPredicateClausesStep<?> booleanStep = myPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}
		throw new IllegalArgumentException(Msg.code(2025) + "Date search param does not support prefix of type: " + prefix);
	}

	private PredicateFinalStep generateDateInstantSearchTerms(String spPath, DateParam theDateParam) {
		String lowerInstantField = spPath + ".dt.lower";
		String upperInstantField = spPath + ".dt.upper";
		final ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(theDateParam.getPrefix(), ParamPrefixEnum.EQUAL);

		if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			Instant dateInstant = theDateParam.getValue().toInstant();
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
				myPredicateFactory.range().field(upperInstantField).lessThan(dateInstant),
				myPredicateFactory.range().field(lowerInstantField).greaterThan(dateInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = myPredicateFactory.bool();
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
				myPredicateFactory.range().field(lowerInstantField).atLeast(lowerBoundAsInstant),
				myPredicateFactory.range().field(upperInstantField).atMost(upperBoundAsInstant)
			);
			BooleanPredicateClausesStep<?> booleanStep = myPredicateFactory.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			return myPredicateFactory.range().field(upperInstantField).greaterThan(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return myPredicateFactory.range().field(upperInstantField).atLeast(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			return myPredicateFactory.range().field(lowerInstantField).lessThan(upperBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return myPredicateFactory.range().field(lowerInstantField).atMost(upperBoundAsInstant);
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

		for (List<IQueryParameterType> nextOrList : theQuantityAndOrTerms) {
			List<PredicateFinalStep> orClauses = nextOrList.stream()
				.map(quantityTerm -> buildQuantityTermClause(NESTED_SEARCH_PARAM_ROOT + "." + theSearchParamName, quantityTerm))
				.collect(Collectors.toList());

			PredicateFinalStep quantityTerms = orPredicateOrSingle(orClauses);

			myRootClause.must(quantityTerms);
		}
	}

	private BooleanPredicateClausesStep<?> buildQuantityTermClause(String spPath, IQueryParameterType paramType) {
		BooleanPredicateClausesStep<?> quantityClause = myPredicateFactory.bool();
		boolean finished = false;

		QuantityParam qtyParam = QuantityParam.toQuantityParam(paramType);
		ParamPrefixEnum activePrefix = qtyParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : qtyParam.getPrefix();
		String quantityElement = spPath + "." + QTY_IDX_NAME;

		if (myModelConfig.getNormalizedQuantitySearchLevel() == NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED) {
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull(qtyParam);
			if (canonicalQty != null) {
				String valueFieldPath = quantityElement + "." + QTY_VALUE_NORM;
				setPrefixedNumericPredicate(quantityClause, activePrefix, canonicalQty.getValue(), valueFieldPath, true);
				quantityClause.must(myPredicateFactory.match()
					.field(quantityElement + "." + QTY_CODE_NORM)
					.matching(canonicalQty.getUnits()));
				finished = true;
			}
		}
		if (!finished) {// not NORMALIZED_QUANTITY_SEARCH_SUPPORTED or non-canonicalizable parameter
			String valueFieldPath = quantityElement + "." + QTY_VALUE;
			setPrefixedNumericPredicate(quantityClause, activePrefix, qtyParam.getValue(), valueFieldPath, true);

			if ( isNotBlank(qtyParam.getSystem()) ) {
				quantityClause.must(
					myPredicateFactory.match()
						.field(quantityElement + "." + QTY_SYSTEM).matching(qtyParam.getSystem()) );
			}

			if ( isNotBlank(qtyParam.getUnits()) ) {
				quantityClause.must(
					myPredicateFactory.match()
						.field(quantityElement + "." + QTY_CODE).matching(qtyParam.getUnits()) );
			}
		}

		return quantityClause;
	}


	private void setPrefixedNumericPredicate(BooleanPredicateClausesStep<?> theQuantityTerms,
				ParamPrefixEnum thePrefix, BigDecimal theNumberValue, String valueFieldPath, boolean theIsMust) {

		double value = theNumberValue.doubleValue();
		Pair<BigDecimal, BigDecimal> range = NumericParamRangeUtil.getRange(theNumberValue);
		double approxTolerance = value * QTY_APPROX_TOLERANCE_PERCENT;

		ParamPrefixEnum activePrefix = thePrefix == null ? ParamPrefixEnum.EQUAL : thePrefix;
		switch (activePrefix) {
			//	searches for resource quantity between passed param value +/- 10%
			case APPROXIMATE:
				var predApp = myPredicateFactory.range().field(valueFieldPath)
					.between(value-approxTolerance, value+approxTolerance);
				addMustOrShouldPredicate(theQuantityTerms, predApp, theIsMust);
				break;

			// searches for resource quantity between passed param value +/- 5%
			case EQUAL:
				var predEq = myPredicateFactory.range().field(valueFieldPath)
					.between(range.getLeft().doubleValue(), range.getRight().doubleValue());
				addMustOrShouldPredicate(theQuantityTerms, predEq, theIsMust);
				break;

			// searches for resource quantity > param value
			case GREATERTHAN:
			case STARTS_AFTER:  // treated as GREATERTHAN because search doesn't handle ranges
				var predGt = myPredicateFactory.range().field(valueFieldPath).greaterThan(value);
				addMustOrShouldPredicate(theQuantityTerms, predGt, theIsMust);
				break;

			// searches for resource quantity not < param value
			case GREATERTHAN_OR_EQUALS:
				theQuantityTerms.must(myPredicateFactory.range().field(valueFieldPath).atLeast(value));

				var predGe = myPredicateFactory.range().field(valueFieldPath).atLeast(value);
				addMustOrShouldPredicate(theQuantityTerms, predGe, theIsMust);
				break;

			// searches for resource quantity < param value
			case LESSTHAN:
			case ENDS_BEFORE:  // treated as LESSTHAN because search doesn't handle ranges
				var predLt = myPredicateFactory.range().field(valueFieldPath).lessThan(value);
				addMustOrShouldPredicate(theQuantityTerms, predLt, theIsMust);
				break;

			// searches for resource quantity not > param value
			case LESSTHAN_OR_EQUALS:
				var predLe = myPredicateFactory.range().field(valueFieldPath).atMost(value);
				addMustOrShouldPredicate(theQuantityTerms, predLe, theIsMust);
				break;

			// NOT_EQUAL: searches for resource quantity not between passed param value +/- 5%
			case NOT_EQUAL:
				theQuantityTerms.mustNot(myPredicateFactory.range()
					.field(valueFieldPath).between(range.getLeft().doubleValue(), range.getRight().doubleValue()));
				break;
		}
	}

	private void addMustOrShouldPredicate(BooleanPredicateClausesStep<?> theQuantityTerms,
			 RangePredicateOptionsStep<?> thePredicateToAdd, boolean theIsMust) {

		if (theIsMust) {
			theQuantityTerms.must(thePredicateToAdd);
		} else {
			theQuantityTerms.should(thePredicateToAdd);
		}
	}


	public void addUriUnmodifiedSearch(String theParamName, List<List<IQueryParameterType>> theUriUnmodifiedAndOrTerms) {
		for (List<IQueryParameterType> nextAnd : theUriUnmodifiedAndOrTerms) {

			List<String> orTerms = nextAnd.stream().map(p -> ((UriParam) p).getValue()).collect(Collectors.toList());
			PredicateFinalStep orTermPredicate = myPredicateFactory.terms()
				.field(String.join(".", SEARCH_PARAM_ROOT, theParamName, URI_VALUE))
				.matchingAny(orTerms);

			myRootClause.must(orTermPredicate);
		}
	}

	public void addNumberUnmodifiedSearch(String theParamName, List<List<IQueryParameterType>> theNumberUnmodifiedAndOrTerms) {
		String fieldPath = String.join(".", SEARCH_PARAM_ROOT, theParamName, NUMBER_VALUE);

		for (List<IQueryParameterType> nextAnd : theNumberUnmodifiedAndOrTerms) {
			List<NumberParam> orTerms = nextAnd.stream().map(NumberParam.class::cast).collect(Collectors.toList());

			BooleanPredicateClausesStep<?> numberPredicateStep = myPredicateFactory.bool();
			numberPredicateStep.minimumShouldMatchNumber(1);

			for (NumberParam orTerm : orTerms) {
				setPrefixedNumericPredicate(numberPredicateStep, orTerm.getPrefix(), orTerm.getValue(), fieldPath, false);
			}

			myRootClause.must(numberPredicateStep);
		}
	}



	public void addCompositeUnmodifiedSearch(RuntimeSearchParam theSearchParam, List<RuntimeSearchParam> theSubSearchParams, List<List<IQueryParameterType>> theCompositeAndOrTerms) {
		for (List<IQueryParameterType> nextAnd : theCompositeAndOrTerms) {
			List<PredicateFinalStep> orClauses =
				nextAnd.stream()
					.map(term -> computeCompositeTermClause(theSearchParam, theSubSearchParams, (CompositeParam) term))
					.collect(Collectors.toList());

			PredicateFinalStep combinedOrClauses = orPredicateOrSingle(orClauses);

			myRootClause.must(f-> f
				// wipmb make the top nsp the single nested object
				.nested().objectField("nsp." + theSearchParam.getName())
				.nest(combinedOrClauses));
		}
	}

	/**
	 * Compute the match clause for all the ocm
	 * @param theSearchParam
	 * @param theSubSearchParams
	 * @param theTerm
	 * @return
	 */
	private PredicateFinalStep computeCompositeTermClause(RuntimeSearchParam theSearchParam, List<RuntimeSearchParam> theSubSearchParams, CompositeParam theTerm) {
		Validate.notNull(theSearchParam);
		Validate.notNull(theSubSearchParams);
		Validate.notNull(theTerm);
		Validate.isTrue(theSubSearchParams.size() == 2, "Hapi only supports composite search parameters with 2 components. %s %d", theSearchParam.getName(), theSubSearchParams.size());
		List<IQueryParameterType> values = theTerm.getValues();
		Validate.isTrue(theSubSearchParams.size() == values.size(), "Different number of query components than defined. %s %d %d", theSearchParam.getName(), theSubSearchParams.size(), values.size());

		String nestedBase = NESTED_SEARCH_PARAM_ROOT + "." + theSearchParam.getName();
		BooleanPredicateClausesStep<?> compositeClause = myPredicateFactory.bool();
		for (int i = 0; i < theSubSearchParams.size(); i+=1) {
			RuntimeSearchParam component = theSubSearchParams.get(i);
			IQueryParameterType value = values.get(i);
			PredicateFinalStep subMatch = null;
			String subComponentPath = nestedBase + "." + component.getName();
			switch (component.getParamType()) {
				case DATE:
					subMatch = buildDateTermClause(subComponentPath, value);
					break;
				case STRING:
					subMatch = buildStringUnmodifiedClause(subComponentPath, value.getValueAsQueryToken(myFhirContext));
						break;
				case TOKEN:
					subMatch= buildTokenUnmodifiedMatchOn(subComponentPath, value);
					break;
				case QUANTITY:
					subMatch = buildQuantityTermClause(subComponentPath, value);
					break;
				// wipmb implement other types

				default:
					break;

			}

			Validate.notNull(subMatch);
			compositeClause.must(subMatch);
		}

		return compositeClause;
	}


}
