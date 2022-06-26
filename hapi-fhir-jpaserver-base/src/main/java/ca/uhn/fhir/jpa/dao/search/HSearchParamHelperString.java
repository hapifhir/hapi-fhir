package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_LOWER;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_TEXT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperString extends HSearchParamHelper<StringParam> {

	private static final String NORM_PATH =  String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_NORMALIZED );
	private static final String EXACT_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_EXACT );
	private static final String TEXT_PATH =  String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_TEXT );
	private static final String LOWER_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_LOWER );

	private static final List<String> mySearchProperties = List.of( NORM_PATH, EXACT_PATH, TEXT_PATH, LOWER_PATH );



	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		StringParam stringParam = (StringParam) theParam;

//		switch (thePropName) {
//			case IDX_STRING_NORMALIZED:
//			case IDX_STRING_EXACT:
//			case IDX_STRING_TEXT:
//			case IDX_STRING_LOWER:
//		}

		return Optional.empty();
	}


	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.STRING; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return NORM_PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }


//	public void addStringTextSearch(String theSearchParamName, List<List<IQueryParameterType>> stringAndOrTerms) {
//		if (CollectionUtils.isEmpty(stringAndOrTerms)) {
//			return;
//		}
//		String fieldName;
//		switch (theSearchParamName) {
//			// _content and _text were here first, and don't obey our mapping.
//			// Leave them as-is for backwards compatibility.
//			case Constants.PARAM_CONTENT:
//				fieldName = "myContentText";
//				break;
//			case Constants.PARAM_TEXT:
//				fieldName = "myNarrativeText";
//				break;
//			default:
//				fieldName = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_TEXT;
//				break;
//		}
//
//		for (List<? extends IQueryParameterType> nextAnd : stringAndOrTerms) {
//			Set<String> terms = extractOrStringParams(nextAnd);
//			ourLog.debug("addStringTextSearch {}, {}", theSearchParamName, terms);
//			if (!terms.isEmpty()) {
//				String query = terms.stream()
//					.map(s -> "( " + s + " )")
//					.collect(Collectors.joining(" | "));
//				myRootClause.must(myPredicateFactory
//					.simpleQueryString()
//					.field(fieldName)
//					.matching(query)
//					.defaultOperator(BooleanOperator.AND)); // term value may contain multiple tokens.  Require all of them to be present.
//			} else {
//				ourLog.warn("No Terms found in query parameter {}", nextAnd);
//			}
//		}
//	}
//
//	public void addStringExactSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
//		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_EXACT;
//
//		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
//			Set<String> terms = extractOrStringParams(nextAnd);
//			ourLog.debug("addStringExactSearch {} {}", theSearchParamName, terms);
//			List<? extends PredicateFinalStep> orTerms = terms.stream()
//				.map(s -> myPredicateFactory.match().field(fieldPath).matching(s))
//				.collect(Collectors.toList());
//
//			myRootClause.must(orPredicateOrSingle(orTerms));
//		}
//	}
//
//	public void addStringContainsSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
//		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_NORMALIZED;
//		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
//			Set<String> terms = extractOrStringParams(nextAnd);
//			ourLog.debug("addStringContainsSearch {} {}", theSearchParamName, terms);
//			List<? extends PredicateFinalStep> orTerms = terms.stream()
//				// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
//				.map(s-> normalize(s))
//				.map(s -> myPredicateFactory
//					.wildcard().field(fieldPath)
//					.matching("*" + s + "*"))
//				.collect(Collectors.toList());
//
//			myRootClause.must(orPredicateOrSingle(orTerms));
//		}
//	}
//
//	/**
//	 * Normalize the string to match our standardAnalyzer.
//	 * @see ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer#STANDARD_ANALYZER
//	 *
//	 * @param theString the raw string
//	 * @return a case and accent normalized version of the input
//	 */
//	@Nonnull
//	private String normalize(String theString) {
//		return StringUtil.normalizeStringForSearchIndexing(theString).toLowerCase(Locale.ROOT);
//	}
//
//	public void addStringUnmodifiedSearch(String theSearchParamName, List<List<IQueryParameterType>> theStringAndOrTerms) {
//		String fieldPath = SEARCH_PARAM_ROOT + "." + theSearchParamName + ".string." + IDX_STRING_NORMALIZED;
//		for (List<? extends IQueryParameterType> nextAnd : theStringAndOrTerms) {
//			Set<String> terms = extractOrStringParams(nextAnd);
//			ourLog.debug("addStringUnmodifiedSearch {} {}", theSearchParamName, terms);
//			List<? extends PredicateFinalStep> orTerms = terms.stream()
//				.map(s ->
//					myPredicateFactory.wildcard()
//						.field(fieldPath)
//						// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the normStringAnalyzer
//						.matching(normalize(s) + "*"))
//				.collect(Collectors.toList());
//
//			myRootClause.must(orPredicateOrSingle(orTerms));
//		}
//	}



}
