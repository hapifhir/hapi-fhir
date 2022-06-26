package ca.uhn.fhir.jpa.dao.search;


import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

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
	private static final List<String> mySkipParamNames = List.of( "myContentText", "myNarrativeText" );


	@Override
	public void processOrTerms(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
				List<IQueryParameterType> theOrTerms, String theParamName, HSearchParamHelper<?> theParamHelper) {

		if (theOrTerms.isEmpty()) { return; }

		StringParam stringParam = (StringParam) theOrTerms.iterator().next();
		if ( stringParam.getQueryParameterQualifier() != null &&
				stringParam.getQueryParameterQualifier().equals(Constants.PARAMQUALIFIER_TOKEN_TEXT) &&
				mySkipParamNames.contains(theParamName)) {
			return;
		}

		Set<String> terms = extractOrStringParams(theOrTerms);
		if (terms.isEmpty()) {
			ourLog.warn("No Terms found in query parameter {}", theParamName);
			return;
		}

		PredicateFinalStep predicates = null;

		if (stringParam.getQueryParameterQualifier() == null) {
			ourLog.debug("addStringUnmodifiedSearch {} {}", theParamName, terms);
			predicates = getPredicatesNoQualifier(theFactory, terms);

		} else if ( stringParam.getQueryParameterQualifier().equals(Constants.PARAMQUALIFIER_TOKEN_TEXT) ) {
			ourLog.debug("addStringTextSearch {}, {}", theParamName, terms);
			predicates = getPredicatesForTextQualifier(theFactory, terms);

		} else if ( stringParam.getQueryParameterQualifier().equals(Constants.PARAMQUALIFIER_STRING_EXACT) ) {
			ourLog.debug("addStringExactSearch {} {}", theParamName, terms);
			predicates = getPredicatesForExactQualifier(theFactory, terms);

		} else if ( stringParam.getQueryParameterQualifier().equals(Constants.PARAMQUALIFIER_STRING_CONTAINS) ) {
			ourLog.debug("addStringContainsSearch {} {}", theParamName, terms);
			predicates = getPredicatesForContainsQualifier(theFactory, terms);
		}

		theBool.must(predicates);
	}


	private PredicateFinalStep getPredicatesForContainsQualifier(SearchPredicateFactory theFactory, Set<String> terms) {
		List<? extends PredicateFinalStep> orTerms = terms.stream()
			// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
			.map(this::normalize)
			.map(s -> theFactory
				.wildcard().field(NORM_PATH)
				.matching("*" + s + "*"))
			.collect(Collectors.toList());
		return orPredicateOrSingle(orTerms, theFactory);
	}


	private PredicateFinalStep getPredicatesForExactQualifier(SearchPredicateFactory theFactory, Set<String> terms) {
		List<? extends PredicateFinalStep> orTerms = terms.stream()
			.map(s -> theFactory.match().field(EXACT_PATH).matching(s))
			.collect(Collectors.toList());
		return orPredicateOrSingle(orTerms, theFactory);
	}


	private PredicateFinalStep getPredicatesForTextQualifier(SearchPredicateFactory theFactory, Set<String> terms) {
		String query = terms.stream() .map(s -> "( " + s + " )") .collect(Collectors.joining(" | "));
		return theFactory.simpleQueryString().field(TEXT_PATH).matching(query);
	}


	private PredicateFinalStep getPredicatesNoQualifier(SearchPredicateFactory theFactory, Set<String> terms) {
		List<? extends PredicateFinalStep> orTerms = terms.stream()
			.map( s -> theFactory.wildcard() .field(NORM_PATH)
				// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the normStringAnalyzer
				.matching(normalize(s) + "*"))
			.collect(Collectors.toList());
		return  orPredicateOrSingle(orTerms, theFactory);
	}


	private Set<String> extractOrStringParams(List<IQueryParameterType> theOrTerms) {
		return theOrTerms.stream()
			.map( StringParam.class::cast )
			.map( p -> StringUtils.defaultString(p.getValue()) )
			.map( String::trim )
			.collect(Collectors.toSet());
	}


	/**
	 * Normalize the string to match our standardAnalyzer.
	 * @see ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer#STANDARD_ANALYZER
	 *
	 * @param theString the raw string
	 * @return a case and accent normalized version of the input
	 */
	@Nonnull
	private String normalize(String theString) {
		return StringUtil.normalizeStringForSearchIndexing(theString).toLowerCase(Locale.ROOT);
	}

	/**
	 * Provide an OR wrapper around a list of predicates.
	 * Returns the sole predicate if it solo, or wrap as a bool/should for OR semantics.
	 *
	 * @param theOrList a list containing at least 1 predicate
	 * @return a predicate providing or-sematics over the list.
	 */
	private PredicateFinalStep orPredicateOrSingle(List<? extends PredicateFinalStep> theOrList, SearchPredicateFactory theFactory) {
		if (theOrList.size() == 1) {
			return theOrList.get(0);
		}

		PredicateFinalStep finalClause;
		BooleanPredicateClausesStep<?> orClause = theFactory.bool();
		theOrList.forEach(orClause::should);
		return orClause;
	}


	@Override
	public <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName) {
		return Optional.empty();
	}

	@Override
	protected RestSearchParameterTypeEnum getParamEnumType() { return RestSearchParameterTypeEnum.STRING; }

	@Override
	public List<String> getParamProperties(IQueryParameterType theParam) { return mySearchProperties; }

	@Override
	public boolean isNested() { return NORM_PATH.startsWith(NESTED_SEARCH_PARAM_ROOT); }


}
