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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_TEXT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ExtendedLuceneClauseBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedLuceneClauseBuilder.class);

	final FhirContext myFhirContext;
	final SearchPredicateFactory myPredicateFactory;
	final BooleanPredicateClausesStep<?> myRootClause;

	final List<TemporalPrecisionEnum> ordinalSearchPrecisions = Arrays.asList(TemporalPrecisionEnum.YEAR, TemporalPrecisionEnum.MONTH, TemporalPrecisionEnum.DAY);

	public ExtendedLuceneClauseBuilder(FhirContext myFhirContext, BooleanPredicateClausesStep<?> myRootClause, SearchPredicateFactory myPredicateFactory) {
		this.myFhirContext = myFhirContext;
		this.myRootClause = myRootClause;
		this.myPredicateFactory = myPredicateFactory;
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
	 * Returns the sole predicate if it solo, or wrap as a bool/should for OR semantics.
	 *
	 * @param theOrList a list containing at least 1 predicate
	 * @return a predicate providing or-sematics over the list.
	 */
	private PredicateFinalStep orPredicateOrSingle(List<? extends PredicateFinalStep> theOrList) {
		PredicateFinalStep finalClause;
		if (theOrList.size() == 1) {
			finalClause = theOrList.get(0);
		} else {
			BooleanPredicateClausesStep<?> orClause = myPredicateFactory.bool();
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
			List<? extends PredicateFinalStep> clauses = nextAnd.stream().map(orTerm -> {
				if (orTerm instanceof TokenParam) {
					TokenParam token = (TokenParam) orTerm;
					if (StringUtils.isBlank(token.getSystem())) {
						// bare value
						return myPredicateFactory.match().field("sp." + theSearchParamName + ".token" + ".code").matching(token.getValue());
					} else if (StringUtils.isBlank(token.getValue())) {
						// system without value
						return myPredicateFactory.match().field("sp." + theSearchParamName + ".token" + ".system").matching(token.getSystem());
					} else {
						// system + value
						return myPredicateFactory.match().field(getTokenSystemCodeFieldPath(theSearchParamName)).matching(token.getValueAsQueryToken(this.myFhirContext));
					}
				} else if (orTerm instanceof StringParam) {
					// MB I don't quite understand why FhirResourceDaoR4SearchNoFtTest.testSearchByIdParamWrongType() uses String but here we are
					StringParam string = (StringParam) orTerm;
					// treat a string as a code with no system (like _id)
					return myPredicateFactory.match().field("sp." + theSearchParamName + ".token" + ".code").matching(string.getValue());
				} else {
					throw new IllegalArgumentException(Msg.code(1089) + "Unexpected param type for token search-param: " + orTerm.getClass().getName());
				}
			}).collect(Collectors.toList());

			PredicateFinalStep finalClause = orPredicateOrSingle(clauses);
			myRootClause.must(finalClause);
		}

	}

	@Nonnull
	public static String getTokenSystemCodeFieldPath(@Nonnull String theSearchParamName) {
		return "sp." + theSearchParamName + ".token" + ".code-system";
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
				fieldName = "sp." + theSearchParamName + ".string." + IDX_STRING_TEXT;
				break;
		}

		for (List<? extends IQueryParameterType> nextAnd : stringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringTextSearch {}, {}", theSearchParamName, terms);
			if (terms.size() >= 1) {
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
		String fieldPath = "sp." + theSearchParamName + ".string." + IDX_STRING_EXACT;

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
		String fieldPath = "sp." + theSearchParamName + ".string." + IDX_STRING_NORMALIZED;
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringContainsSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s ->
					myPredicateFactory.wildcard().field(fieldPath).matching("*" + s + "*"))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
		}
	}

	public void addStringUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = "sp." + theSearchParamName + ".string." + IDX_STRING_NORMALIZED;
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			ourLog.debug("addStringUnmodifiedSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
				.map(s ->
					myPredicateFactory.wildcard().field(fieldPath).matching(s + "*"))
				.collect(Collectors.toList());

			myRootClause.must(orPredicateOrSingle(orTerms));
		}
	}

	public void addReferenceUnchainedSearch(String theSearchParamName, List<List<IQueryParameterType>> theReferenceAndOrTerms) {
		String fieldPath = "sp." + theSearchParamName + ".reference.value";
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
	 * @param theSearchParamName
	 * @param theDateAndOrTerms
	 */
	public void addDateUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theDateAndOrTerms) {
		for (List<? extends IQueryParameterType> nextAnd : theDateAndOrTerms) {
			// comma separated list of dates(OR list) on a date param is not applicable so grab
			// first from default list
			if (nextAnd.size() > 1) {
				throw new IllegalArgumentException(Msg.code(2032) + "OR (,) searches on DATE search parameters are not supported for ElasticSearch/Lucene");
			}
			DateParam dateParam = (DateParam) nextAnd.stream().findFirst()
				.orElseThrow(() -> new InvalidRequestException("Date param is missing value"));

			boolean isOrdinalSearch = ordinalSearchPrecisions.contains(dateParam.getPrecision());

			PredicateFinalStep searchPredicate = isOrdinalSearch
				? generateDateOrdinalSearchTerms(theSearchParamName, dateParam)
				: generateDateInstantSearchTerms(theSearchParamName, dateParam);

			myRootClause.must(searchPredicate);
		}
	}

	private PredicateFinalStep generateDateOrdinalSearchTerms(String theSearchParamName, DateParam theDateParam) {
		String lowerOrdinalField = "sp." + theSearchParamName + ".dt.lower-ord";
		String upperOrdinalField = "sp." + theSearchParamName + ".dt.upper-ord";
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

	private PredicateFinalStep generateDateInstantSearchTerms(String theSearchParamName, DateParam theDateParam) {
		String lowerInstantField = "sp." + theSearchParamName + ".dt.lower";
		String upperInstantField = "sp." + theSearchParamName + ".dt.upper";
		ParamPrefixEnum prefix = theDateParam.getPrefix();

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

		if (Objects.isNull(prefix) || prefix == ParamPrefixEnum.EQUAL) {
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
}
