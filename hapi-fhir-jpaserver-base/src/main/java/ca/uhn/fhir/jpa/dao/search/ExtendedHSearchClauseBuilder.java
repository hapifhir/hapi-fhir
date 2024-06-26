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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
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
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.util.DateUtils;
import ca.uhn.fhir.util.NumericParamRangeUtil;
import ca.uhn.fhir.util.StringUtil;
import jakarta.annotation.Nonnull;
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

import static ca.uhn.fhir.jpa.dao.search.PathContext.joinPath;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_TEXT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_QUANTITY;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_STRING;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.INDEX_TYPE_TOKEN;
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
	public final BooleanPredicateClausesStep<?> myRootClause;
	public final StorageSettings myStorageSettings;
	final PathContext myRootContext;

	final List<TemporalPrecisionEnum> ordinalSearchPrecisions =
			Arrays.asList(TemporalPrecisionEnum.YEAR, TemporalPrecisionEnum.MONTH, TemporalPrecisionEnum.DAY);

	public ExtendedHSearchClauseBuilder(
			FhirContext myFhirContext,
			StorageSettings theStorageSettings,
			BooleanPredicateClausesStep<?> theRootClause,
			SearchPredicateFactory thePredicateFactory) {
		this.myFhirContext = myFhirContext;
		this.myStorageSettings = theStorageSettings;
		this.myRootClause = theRootClause;
		myRootContext = PathContext.buildRootContext(theRootClause, thePredicateFactory);
	}

	/**
	 * Restrict search to resources of a type
	 * @param theResourceType the type to match.  e.g. "Observation"
	 */
	public void addResourceTypeClause(String theResourceType) {
		myRootClause.must(myRootContext.match().field("myResourceType").matching(theResourceType));
	}

	@Nonnull
	private Set<String> extractOrStringParams(String theSearchParamName, List<? extends IQueryParameterType> nextAnd) {
		Set<String> terms = new HashSet<>();
		for (IQueryParameterType nextOr : nextAnd) {
			String nextValueTrimmed;
			if (isStringParamOrEquivalent(theSearchParamName, nextOr)) {
				nextValueTrimmed = getTrimmedStringValue(nextOr);
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
				throw new IllegalArgumentException(
						Msg.code(1088) + "Unsupported full-text param type: " + nextOr.getClass());
			}
			if (isNotBlank(nextValueTrimmed)) {
				terms.add(nextValueTrimmed);
			}
		}
		return terms;
	}

	private String getTrimmedStringValue(IQueryParameterType nextOr) {
		String value;
		if (nextOr instanceof StringParam) {
			value = ((StringParam) nextOr).getValue();
		} else if (nextOr instanceof SpecialParam) {
			value = ((SpecialParam) nextOr).getValue();
		} else {
			throw new IllegalArgumentException(Msg.code(2535)
					+ "Failed to extract value for fulltext search from parameter. Needs to be a `string` parameter, or `_text` or `_content` special parameter."
					+ nextOr);
		}
		return StringUtils.defaultString(value).trim();
	}

	/**
	 * String Search params are valid, so are two special params, _content and _text.
	 *
	 * @param theSearchParamName The name of the SP
	 * @param nextOr the or values of the query parameter.
	 *
	 * @return a boolean indicating whether we can treat this as a string.
	 */
	private static boolean isStringParamOrEquivalent(String theSearchParamName, IQueryParameterType nextOr) {
		List<String> specialSearchParamsToTreatAsStrings = List.of(Constants.PARAM_TEXT, Constants.PARAM_CONTENT);
		return (nextOr instanceof StringParam)
				|| (nextOr instanceof SpecialParam && specialSearchParamsToTreatAsStrings.contains(theSearchParamName));
	}

	public void addTokenUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theAndOrTerms) {
		if (CollectionUtils.isEmpty(theAndOrTerms)) {
			return;
		}
		PathContext spContext = contextForFlatSP(theSearchParamName);
		for (List<? extends IQueryParameterType> nextAnd : theAndOrTerms) {

			ourLog.debug("addTokenUnmodifiedSearch {} {}", theSearchParamName, nextAnd);
			List<? extends PredicateFinalStep> clauses = nextAnd.stream()
					.map(orTerm -> buildTokenUnmodifiedMatchOn(orTerm, spContext))
					.collect(Collectors.toList());
			PredicateFinalStep finalClause = spContext.orPredicateOrSingle(clauses);

			myRootClause.must(finalClause);
		}
	}

	private PathContext contextForFlatSP(String theSearchParamName) {
		String path = joinPath(SEARCH_PARAM_ROOT, theSearchParamName);
		return myRootContext.forAbsolutePath(path);
	}

	private PredicateFinalStep buildTokenUnmodifiedMatchOn(IQueryParameterType orTerm, PathContext thePathContext) {
		String pathPrefix = thePathContext.getContextPath();
		if (orTerm instanceof TokenParam) {
			TokenParam token = (TokenParam) orTerm;
			if (StringUtils.isBlank(token.getSystem())) {
				// bare value
				return thePathContext
						.match()
						.field(joinPath(pathPrefix, INDEX_TYPE_TOKEN, TOKEN_CODE))
						.matching(token.getValue());
			} else if (StringUtils.isBlank(token.getValue())) {
				// system without value
				return thePathContext
						.match()
						.field(joinPath(pathPrefix, INDEX_TYPE_TOKEN, TOKEN_SYSTEM))
						.matching(token.getSystem());
			} else {
				// system + value
				return thePathContext
						.match()
						.field(joinPath(pathPrefix, INDEX_TYPE_TOKEN, TOKEN_SYSTEM_CODE))
						.matching(token.getValueAsQueryToken(this.myFhirContext));
			}
		} else if (orTerm instanceof StringParam) {
			// MB I don't quite understand why FhirResourceDaoR4SearchNoFtTest.testSearchByIdParamWrongType() uses
			// String but here we are
			StringParam string = (StringParam) orTerm;
			// treat a string as a code with no system (like _id)
			return thePathContext
					.match()
					.field(joinPath(pathPrefix, INDEX_TYPE_TOKEN, TOKEN_CODE))
					.matching(string.getValue());
		} else {
			throw new IllegalArgumentException(Msg.code(1089) + "Unexpected param type for token search-param: "
					+ orTerm.getClass().getName());
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

		if (isContainsSearch(theSearchParamName, stringAndOrTerms)) {
			for (List<? extends IQueryParameterType> nextOrList : stringAndOrTerms) {
				addPreciseMatchClauses(theSearchParamName, nextOrList, fieldName);
			}
		} else {
			for (List<? extends IQueryParameterType> nextOrList : stringAndOrTerms) {
				addSimpleQueryMatchClauses(theSearchParamName, nextOrList, fieldName);
			}
		}
	}

	/**
	 * This route is used for standard string searches, or `_text` or `_content`. For each term, we build a `simpleQueryString `element which allows hibernate search to search on normalized, analyzed, indexed fields.
	 *
	 * @param theSearchParamName The name of the search parameter
	 * @param nextOrList the list of query parameters
	 * @param fieldName  the field name in the index document to compare with.
	 */
	private void addSimpleQueryMatchClauses(
			String theSearchParamName, List<? extends IQueryParameterType> nextOrList, String fieldName) {
		Set<String> orTerms = TermHelper.makePrefixSearchTerm(extractOrStringParams(theSearchParamName, nextOrList));
		ourLog.debug("addStringTextSearch {}, {}", theSearchParamName, orTerms);
		if (!orTerms.isEmpty()) {
			String query = orTerms.stream().map(s -> "( " + s + " )").collect(Collectors.joining(" | "));
			myRootClause.must(myRootContext
					.simpleQueryString()
					.field(fieldName)
					.matching(query)
					.defaultOperator(
							BooleanOperator.AND)); // term value may contain multiple tokens.  Require all of them to
			// be
			// present.

		} else {
			ourLog.warn("No Terms found in query parameter {}", nextOrList);
		}
	}

	/**
	 * Note that this `match()` operation is different from out standard behaviour, which uses simpleQueryString(). This `match()` forces a precise string match, Whereas `simpleQueryString()` uses a more nebulous
	 * and loose check against a collection of terms. We only use this when we see ` _text:contains=` or `_content:contains=` search.
	 *
	 * @param theSearchParamName the Name of the search parameter
	 * @param nextOrList the list of query parameters
	 * @param fieldName the field name in the index document to compare with.
	 */
	private void addPreciseMatchClauses(
			String theSearchParamName, List<? extends IQueryParameterType> nextOrList, String fieldName) {
		Set<String> orTerms = TermHelper.makePrefixSearchTerm(extractOrStringParams(theSearchParamName, nextOrList));
		for (String orTerm : orTerms) {
			myRootClause.must(myRootContext.match().field(fieldName).matching(orTerm));
		}
	}

	public void addStringExactSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, INDEX_TYPE_STRING, IDX_STRING_EXACT);

		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(theSearchParamName, nextAnd);
			ourLog.debug("addStringExactSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
					.map(s -> myRootContext.match().field(fieldPath).matching(s))
					.collect(Collectors.toList());

			myRootClause.must(myRootContext.orPredicateOrSingle(orTerms));
		}
	}

	public void addStringContainsSearch(
			String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, INDEX_TYPE_STRING, IDX_STRING_NORMALIZED);
		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(theSearchParamName, nextAnd);
			ourLog.debug("addStringContainsSearch {} {}", theSearchParamName, terms);
			List<? extends PredicateFinalStep> orTerms = terms.stream()
					// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
					.map(this::normalize)
					.map(s -> myRootContext.wildcard().field(fieldPath).matching("*" + s + "*"))
					.collect(Collectors.toList());

			myRootClause.must(myRootContext.orPredicateOrSingle(orTerms));
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

	public void addStringUnmodifiedSearch(
			String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
		PathContext context = contextForFlatSP(theSearchParamName);
		for (List<? extends IQueryParameterType> nextOrList : theStringAndOrTerms) {
			Set<String> terms = extractOrStringParams(theSearchParamName, nextOrList);
			ourLog.debug("addStringUnmodifiedSearch {} {}", theSearchParamName, terms);
			List<PredicateFinalStep> orTerms = terms.stream()
					.map(s -> buildStringUnmodifiedClause(s, context))
					.collect(Collectors.toList());

			myRootClause.must(context.orPredicateOrSingle(orTerms));
		}
	}

	private WildcardPredicateOptionsStep<?> buildStringUnmodifiedClause(String theString, PathContext theContext) {
		return theContext
				.wildcard()
				.field(joinPath(theContext.getContextPath(), INDEX_TYPE_STRING, IDX_STRING_NORMALIZED))
				// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the
				// normStringAnalyzer
				.matching(normalize(theString) + "*");
	}

	public void addReferenceUnchainedSearch(
			String theSearchParamName, List<List<IQueryParameterType>> theReferenceAndOrTerms) {
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theSearchParamName, "reference", "value");
		for (List<? extends IQueryParameterType> nextAnd : theReferenceAndOrTerms) {
			Set<String> terms = extractOrStringParams(theSearchParamName, nextAnd);
			ourLog.trace("reference unchained search {}", terms);

			List<? extends PredicateFinalStep> orTerms = terms.stream()
					.map(s -> myRootContext.match().field(fieldPath).matching(s))
					.collect(Collectors.toList());

			myRootClause.must(myRootContext.orPredicateOrSingle(orTerms));
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

			PathContext spContext = contextForFlatSP(theSearchParamName);

			List<PredicateFinalStep> clauses = nextOrList.stream()
					.map(d -> buildDateTermClause(d, spContext))
					.collect(Collectors.toList());

			myRootClause.must(myRootContext.orPredicateOrSingle(clauses));
		}
	}

	private PredicateFinalStep buildDateTermClause(IQueryParameterType theQueryParameter, PathContext theSpContext) {
		DateParam dateParam = (DateParam) theQueryParameter;
		boolean isOrdinalSearch = ordinalSearchPrecisions.contains(dateParam.getPrecision());
		return isOrdinalSearch
				? generateDateOrdinalSearchTerms(dateParam, theSpContext)
				: generateDateInstantSearchTerms(dateParam, theSpContext);
	}

	private PredicateFinalStep generateDateOrdinalSearchTerms(DateParam theDateParam, PathContext theSpContext) {

		String lowerOrdinalField = joinPath(theSpContext.getContextPath(), "dt", "lower-ord");
		String upperOrdinalField = joinPath(theSpContext.getContextPath(), "dt", "upper-ord");
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
					theSpContext.range().field(lowerOrdinalField).atLeast(lowerBoundAsOrdinal),
					theSpContext.range().field(upperOrdinalField).atMost(upperBoundAsOrdinal));
			BooleanPredicateClausesStep<?> booleanStep = theSpContext.bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			// TODO JB: more fine tuning needed for STARTS_AFTER
			return theSpContext.range().field(upperOrdinalField).greaterThan(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return theSpContext.range().field(upperOrdinalField).atLeast(upperBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			// TODO JB: more fine tuning needed for END_BEFORE
			return theSpContext.range().field(lowerOrdinalField).lessThan(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return theSpContext.range().field(lowerOrdinalField).atMost(lowerBoundAsOrdinal);
		} else if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
					theSpContext.range().field(upperOrdinalField).lessThan(lowerBoundAsOrdinal),
					theSpContext.range().field(lowerOrdinalField).greaterThan(upperBoundAsOrdinal));
			BooleanPredicateClausesStep<?> booleanStep = theSpContext.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}
		throw new IllegalArgumentException(
				Msg.code(2255) + "Date search param does not support prefix of type: " + prefix);
	}

	private PredicateFinalStep generateDateInstantSearchTerms(DateParam theDateParam, PathContext theSpContext) {
		String lowerInstantField = joinPath(theSpContext.getContextPath(), "dt", "lower");
		String upperInstantField = joinPath(theSpContext.getContextPath(), "dt", "upper");
		final ParamPrefixEnum prefix = ObjectUtils.defaultIfNull(theDateParam.getPrefix(), ParamPrefixEnum.EQUAL);

		if (ParamPrefixEnum.NOT_EQUAL == prefix) {
			Instant dateInstant = theDateParam.getValue().toInstant();
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
					theSpContext.range().field(upperInstantField).lessThan(dateInstant),
					theSpContext.range().field(lowerInstantField).greaterThan(dateInstant));
			BooleanPredicateClausesStep<?> booleanStep = theSpContext.bool();
			predicateSteps.forEach(booleanStep::should);
			booleanStep.minimumShouldMatchNumber(1);
			return booleanStep;
		}

		// Consider lower and upper bounds for building range predicates
		DateRangeParam dateRange = new DateRangeParam(theDateParam);
		Instant lowerBoundAsInstant = Optional.ofNullable(dateRange.getLowerBound())
				.map(param -> param.getValue().toInstant())
				.orElse(null);
		Instant upperBoundAsInstant = Optional.ofNullable(dateRange.getUpperBound())
				.map(param -> param.getValue().toInstant())
				.orElse(null);

		if (prefix == ParamPrefixEnum.EQUAL) {
			// For equality prefix we would like the date to fall between the lower and upper bound
			List<? extends PredicateFinalStep> predicateSteps = Arrays.asList(
					((SearchPredicateFactory) theSpContext)
							.range()
							.field(lowerInstantField)
							.atLeast(lowerBoundAsInstant),
					((SearchPredicateFactory) theSpContext)
							.range()
							.field(upperInstantField)
							.atMost(upperBoundAsInstant));
			BooleanPredicateClausesStep<?> booleanStep = ((SearchPredicateFactory) theSpContext).bool();
			predicateSteps.forEach(booleanStep::must);
			return booleanStep;
		} else if (ParamPrefixEnum.GREATERTHAN == prefix || ParamPrefixEnum.STARTS_AFTER == prefix) {
			return ((SearchPredicateFactory) theSpContext)
					.range()
					.field(upperInstantField)
					.greaterThan(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.GREATERTHAN_OR_EQUALS == prefix) {
			return ((SearchPredicateFactory) theSpContext)
					.range()
					.field(upperInstantField)
					.atLeast(lowerBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN == prefix || ParamPrefixEnum.ENDS_BEFORE == prefix) {
			return ((SearchPredicateFactory) theSpContext)
					.range()
					.field(lowerInstantField)
					.lessThan(upperBoundAsInstant);
		} else if (ParamPrefixEnum.LESSTHAN_OR_EQUALS == prefix) {
			return ((SearchPredicateFactory) theSpContext)
					.range()
					.field(lowerInstantField)
					.atMost(upperBoundAsInstant);
		}

		throw new IllegalArgumentException(
				Msg.code(2256) + "Date search param does not support prefix of type: " + prefix);
	}

	/**
	 * Differences with DB search:
	 *  _ is not all-normalized-or-all-not. Each parameter is applied on quantity or normalized quantity depending on UCUM fitness
	 *  _ respects ranges for equal and approximate qualifiers
	 *
	 * Strategy: For each parameter, if it can be canonicalized, it is, and used against 'normalized-value-quantity' index
	 * 	otherwise it is applied as-is to 'value-quantity'
	 */
	public void addQuantityUnmodifiedSearch(
			String theSearchParamName, List<List<IQueryParameterType>> theQuantityAndOrTerms) {

		for (List<IQueryParameterType> nextOrList : theQuantityAndOrTerms) {
			// we build quantity predicates in a nested context so we can match units and systems with values.
			PredicateFinalStep nestedClause =
					myRootContext.buildPredicateInNestedContext(theSearchParamName, nextedContext -> {
						List<PredicateFinalStep> orClauses = nextOrList.stream()
								.map(quantityTerm -> buildQuantityTermClause(quantityTerm, nextedContext))
								.collect(Collectors.toList());

						return nextedContext.orPredicateOrSingle(orClauses);
					});

			myRootClause.must(nestedClause);
		}
	}

	private BooleanPredicateClausesStep<?> buildQuantityTermClause(
			IQueryParameterType theQueryParameter, PathContext thePathContext) {

		BooleanPredicateClausesStep<?> quantityClause = ((SearchPredicateFactory) thePathContext).bool();

		QuantityParam qtyParam = QuantityParam.toQuantityParam(theQueryParameter);
		ParamPrefixEnum activePrefix = qtyParam.getPrefix() == null ? ParamPrefixEnum.EQUAL : qtyParam.getPrefix();
		String quantityElement = joinPath(thePathContext.getContextPath(), INDEX_TYPE_QUANTITY);

		if (myStorageSettings.getNormalizedQuantitySearchLevel()
				== NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED) {
			QuantityParam canonicalQty = UcumServiceUtil.toCanonicalQuantityOrNull(qtyParam);
			if (canonicalQty != null) {
				String valueFieldPath = joinPath(quantityElement, QTY_VALUE_NORM);

				quantityClause.must(
						buildNumericClause(valueFieldPath, activePrefix, canonicalQty.getValue(), thePathContext));
				quantityClause.must(((SearchPredicateFactory) thePathContext)
						.match()
						.field(joinPath(quantityElement, QTY_CODE_NORM))
						.matching(canonicalQty.getUnits()));
				return quantityClause;
			}
		}

		String valueFieldPath = joinPath(quantityElement, QTY_VALUE);

		quantityClause.must(buildNumericClause(valueFieldPath, activePrefix, qtyParam.getValue(), thePathContext));

		if (isNotBlank(qtyParam.getSystem())) {
			quantityClause.must(((SearchPredicateFactory) thePathContext)
					.match()
					.field(joinPath(quantityElement, QTY_SYSTEM))
					.matching(qtyParam.getSystem()));
		}

		if (isNotBlank(qtyParam.getUnits())) {
			quantityClause.must(((SearchPredicateFactory) thePathContext)
					.match()
					.field(joinPath(quantityElement, QTY_CODE))
					.matching(qtyParam.getUnits()));
		}

		return quantityClause;
	}

	/**
	 * Shared helper between quantity and number
	 * @param valueFieldPath The path leading to index node
	 * @param thePrefix the query prefix (e.g. lt).  Null means eq
	 * @param theNumberValue the query value
	 * @param thePathContext HSearch builder
	 * @return a query predicate applying the prefix to the value
	 */
	@Nonnull
	private PredicateFinalStep buildNumericClause(
			String valueFieldPath, ParamPrefixEnum thePrefix, BigDecimal theNumberValue, PathContext thePathContext) {
		PredicateFinalStep predicate = null;

		double value = theNumberValue.doubleValue();
		Pair<BigDecimal, BigDecimal> range = NumericParamRangeUtil.getRange(theNumberValue);
		double approxTolerance = value * QTY_APPROX_TOLERANCE_PERCENT;

		ParamPrefixEnum activePrefix = thePrefix == null ? ParamPrefixEnum.EQUAL : thePrefix;
		switch (activePrefix) {
				//	searches for resource quantity between passed param value +/- 10%
			case APPROXIMATE:
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.between(value - approxTolerance, value + approxTolerance);
				break;

				// searches for resource quantity between passed param value +/- 5%
			case EQUAL:
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.between(range.getLeft().doubleValue(), range.getRight().doubleValue());
				break;

				// searches for resource quantity > param value
			case GREATERTHAN:
			case STARTS_AFTER: // treated as GREATERTHAN because search doesn't handle ranges
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.greaterThan(value);
				break;

				// searches for resource quantity not < param value
			case GREATERTHAN_OR_EQUALS:
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.atLeast(value);
				break;

				// searches for resource quantity < param value
			case LESSTHAN:
			case ENDS_BEFORE: // treated as LESSTHAN because search doesn't handle ranges
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.lessThan(value);
				break;

				// searches for resource quantity not > param value
			case LESSTHAN_OR_EQUALS:
				predicate = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.atMost(value);
				break;

				// NOT_EQUAL: searches for resource quantity not between passed param value +/- 5%
			case NOT_EQUAL:
				RangePredicateOptionsStep<?> negRange = ((SearchPredicateFactory) thePathContext)
						.range()
						.field(valueFieldPath)
						.between(range.getLeft().doubleValue(), range.getRight().doubleValue());
				predicate = ((SearchPredicateFactory) thePathContext).bool().mustNot(negRange);
				break;
		}
		Validate.notNull(predicate, "Unsupported prefix: %s", thePrefix);
		return predicate;
	}

	public void addUriUnmodifiedSearch(
			String theParamName, List<List<IQueryParameterType>> theUriUnmodifiedAndOrTerms) {
		PathContext spContext = this.contextForFlatSP(theParamName);
		for (List<IQueryParameterType> nextOrList : theUriUnmodifiedAndOrTerms) {

			PredicateFinalStep orListPredicate = buildURIClause(nextOrList, spContext);

			myRootClause.must(orListPredicate);
		}
	}

	private PredicateFinalStep buildURIClause(List<IQueryParameterType> theOrList, PathContext thePathContext) {
		List<String> orTerms =
				theOrList.stream().map(p -> ((UriParam) p).getValue()).collect(Collectors.toList());

		return ((SearchPredicateFactory) thePathContext)
				.terms()
				.field(joinPath(thePathContext.getContextPath(), URI_VALUE))
				.matchingAny(orTerms);
	}

	public void addNumberUnmodifiedSearch(
			String theParamName, List<List<IQueryParameterType>> theNumberUnmodifiedAndOrTerms) {
		PathContext pathContext = contextForFlatSP(theParamName);
		String fieldPath = joinPath(SEARCH_PARAM_ROOT, theParamName, NUMBER_VALUE);

		for (List<IQueryParameterType> nextOrList : theNumberUnmodifiedAndOrTerms) {
			List<PredicateFinalStep> orTerms = nextOrList.stream()
					.map(NumberParam.class::cast)
					.map(orTerm -> buildNumericClause(fieldPath, orTerm.getPrefix(), orTerm.getValue(), pathContext))
					.collect(Collectors.toList());

			myRootClause.must(pathContext.orPredicateOrSingle(orTerms));
		}
	}

	private PredicateFinalStep buildNumericClause(IQueryParameterType theValue, PathContext thePathContext) {
		NumberParam p = (NumberParam) theValue;

		return buildNumericClause(
				joinPath(thePathContext.getContextPath(), NUMBER_VALUE), p.getPrefix(), p.getValue(), thePathContext);
	}

	public void addCompositeUnmodifiedSearch(
			RuntimeSearchParam theSearchParam,
			List<RuntimeSearchParam> theSubSearchParams,
			List<List<IQueryParameterType>> theCompositeAndOrTerms) {
		for (List<IQueryParameterType> nextOrList : theCompositeAndOrTerms) {

			// The index data for each extracted element is stored in a separate nested HSearch document.
			// Create a nested parent node for all component predicates.
			// Each can share this nested beacuse all nested docs share a parent id.

			PredicateFinalStep nestedClause =
					myRootContext.buildPredicateInNestedContext(theSearchParam.getName(), nestedContext -> {
						List<PredicateFinalStep> orClauses = nextOrList.stream()
								.map(term -> computeCompositeTermClause(
										theSearchParam, theSubSearchParams, (CompositeParam<?, ?>) term, nestedContext))
								.collect(Collectors.toList());

						return nestedContext.orPredicateOrSingle(orClauses);
					});
			myRootClause.must(nestedClause);
		}
	}

	/**
	 * Compute the match clause for all the components of theCompositeQueryParam.
	 *
	 * @param theSearchParam The composite SP
	 * @param theSubSearchParams the composite component SPs
	 * @param theCompositeQueryParam the query param values
	 * @param theCompositeContext the root of the nested SP query.
	 */
	private PredicateFinalStep computeCompositeTermClause(
			RuntimeSearchParam theSearchParam,
			List<RuntimeSearchParam> theSubSearchParams,
			CompositeParam<?, ?> theCompositeQueryParam,
			PathContext theCompositeContext) {
		Validate.notNull(theSearchParam);
		Validate.notNull(theSubSearchParams);
		Validate.notNull(theCompositeQueryParam);
		Validate.isTrue(
				theSubSearchParams.size() == 2,
				"Hapi only supports composite search parameters with 2 components. %s %d",
				theSearchParam.getName(),
				theSubSearchParams.size());
		List<IQueryParameterType> values = theCompositeQueryParam.getValues();
		Validate.isTrue(
				theSubSearchParams.size() == values.size(),
				"Different number of query components than defined. %s %d %d",
				theSearchParam.getName(),
				theSubSearchParams.size(),
				values.size());

		// The index data for each extracted element is stored in a separate nested HSearch document.

		// Create a nested parent node for all component predicates.
		BooleanPredicateClausesStep<?> compositeClause = ((SearchPredicateFactory) theCompositeContext).bool();
		for (int i = 0; i < theSubSearchParams.size(); i += 1) {
			RuntimeSearchParam component = theSubSearchParams.get(i);
			IQueryParameterType value = values.get(i);
			PredicateFinalStep subMatch = null;
			PathContext componentContext = theCompositeContext.getSubComponentContext(component.getName());
			switch (component.getParamType()) {
				case DATE:
					subMatch = buildDateTermClause(value, componentContext);
					break;
				case STRING:
					subMatch = buildStringUnmodifiedClause(value.getValueAsQueryToken(myFhirContext), componentContext);
					break;
				case TOKEN:
					subMatch = buildTokenUnmodifiedMatchOn(value, componentContext);
					break;
				case QUANTITY:
					subMatch = buildQuantityTermClause(value, componentContext);
					break;
				case URI:
					subMatch = buildURIClause(List.of(value), componentContext);
					break;
				case NUMBER:
					subMatch = buildNumericClause(value, componentContext);
					break;
				case REFERENCE:

				default:
					break;
			}

			Validate.notNull(
					subMatch,
					"Unsupported composite type in %s: %s %s",
					theSearchParam.getName(),
					component.getName(),
					component.getParamType());
			compositeClause.must(subMatch);
		}

		return compositeClause;
	}

	private boolean hasAContainsModifier(List<List<IQueryParameterType>> stringAndOrTerms) {
		return stringAndOrTerms.stream()
				.flatMap(List::stream)
				.anyMatch(next ->
						Constants.PARAMQUALIFIER_STRING_CONTAINS.equalsIgnoreCase(next.getQueryParameterQualifier()));
	}

	private boolean isContainsSearch(String theSearchParamName, List<List<IQueryParameterType>> stringAndOrTerms) {
		return (Constants.PARAM_TEXT.equalsIgnoreCase(theSearchParamName)
						|| Constants.PARAM_CONTENT.equalsIgnoreCase(theSearchParamName))
				&& hasAContainsModifier(stringAndOrTerms);
	}
}
