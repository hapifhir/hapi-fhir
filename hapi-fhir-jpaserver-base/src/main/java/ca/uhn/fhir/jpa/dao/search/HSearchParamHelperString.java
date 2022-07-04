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

import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_EXACT;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_LOWER;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.IDX_STRING_NORMALIZED;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.SEARCH_PARAM_ROOT;

public class HSearchParamHelperString extends HSearchParamHelper<StringParam> {

	private static final String NORM_PATH =  String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_NORMALIZED );
	private static final String EXACT_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_EXACT );
	private static final String LOWER_PATH = String.join(".", SEARCH_PARAM_ROOT, "*", "string", IDX_STRING_LOWER );

	private static final List<String> mySearchProperties = List.of( NORM_PATH, EXACT_PATH, TEXT_PATH, LOWER_PATH );


	@Override
	public void processOrTerms(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
				List<IQueryParameterType> theOrTerms, String theParamName) {

		if (theOrTerms.isEmpty()) { return; }

		StringParam stringParam = (StringParam) theOrTerms.iterator().next();

		Set<String> terms = extractOrStringParams(theOrTerms);
		if (terms.isEmpty()) {
			ourLog.warn("No Terms found in query parameter {}", theParamName);
			return;
		}

		if (theParamName.equals(Constants.PARAM_CONTENT) || theParamName.equals(Constants.PARAM_TEXT)) {
			var predicates = getPredicatesForTextQualifier(theFactory, terms, theParamName);
			theBool.must( predicates );
			return;
		}

		PredicateFinalStep predicates = getPredicatesForQualifier(theFactory, theParamName, stringParam, terms);

		theBool.must(predicates);
	}


	private PredicateFinalStep getPredicatesForQualifier(SearchPredicateFactory theFactory, String theParamName, StringParam stringParam, Set<String> terms) {
		if ( stringParam.getQueryParameterQualifier() != null ) {
			switch (stringParam.getQueryParameterQualifier()) {
				case Constants.PARAMQUALIFIER_STRING_EXACT:
					ourLog.debug("addStringExactSearch {} {}", theParamName, terms);
					return getPredicatesForExactQualifier(theFactory, terms, theParamName);

				case Constants.PARAMQUALIFIER_STRING_CONTAINS:
					ourLog.debug("addStringContainsSearch {} {}", theParamName, terms);
					return getPredicatesForContainsQualifier(theFactory, terms, theParamName);

				default:
					break;
			}
		}

		ourLog.debug("addStringUnqualifiedSearch {} {}", theParamName, terms);
		return getPredicatesUnqualified(theFactory, terms, theParamName);
	}




	protected PredicateFinalStep getPredicatesForExactQualifier(
		SearchPredicateFactory theFactory, Set<String> terms, String theParamName) {

		String fieldPath = mergeParamIntoProperty(EXACT_PATH, theParamName);

		List<? extends PredicateFinalStep> orTerms = terms.stream()
			.map(s -> theFactory.match().field(fieldPath).matching(s))
			.collect(Collectors.toList());
		return orPredicateOrSingle(orTerms, theFactory);
	}


	private PredicateFinalStep getPredicatesForContainsQualifier(SearchPredicateFactory theFactory, Set<String> terms, String theParamName) {
		String fieldPath = mergeParamIntoProperty(NORM_PATH, theParamName);

		List<? extends PredicateFinalStep> orTerms = terms.stream()
			// wildcard is a term-level query, so queries aren't analyzed.  Do our own normalization first.
			.map(this::normalize)
			.map(s -> theFactory .wildcard().field(fieldPath) .matching("*" + s + "*"))
			.collect(Collectors.toList());
		return orPredicateOrSingle(orTerms, theFactory);
	}


	private PredicateFinalStep getPredicatesUnqualified(SearchPredicateFactory theFactory, Set<String> terms, String theParamName) {
		String fieldPath = mergeParamIntoProperty(NORM_PATH, theParamName);

		List<? extends PredicateFinalStep> orTerms = terms.stream()
			.map( s -> theFactory.wildcard() .field(fieldPath)
				// wildcard is a term-level query, so it isn't analyzed.  Do our own case-folding to match the normStringAnalyzer
				.matching(normalize(s) + "*"))
			.collect(Collectors.toList());
		return  orPredicateOrSingle(orTerms, theFactory);
	}


	/**
	 * Normalize the string to match our standardAnalyzer.
	 * @see ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer#STANDARD_ANALYZER
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


	private Set<String> extractOrStringParams(List<IQueryParameterType> theOrTerms) {
		return theOrTerms.stream()
			.map( StringParam.class::cast )
			.map( p -> StringUtils.defaultString(p.getValue()) )
			.map( String::trim )
			.collect(Collectors.toSet());
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
	public boolean isNested() { return false; }


}
