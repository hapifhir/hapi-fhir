package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.impl.DefaultSearchQueryBuilder;
import ca.uhn.fhir.repository.impl.ISearchQueryBuilder;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.repository.searchparam.IncludeParameterProcessor.includeProcessor;
import static ca.uhn.fhir.jpa.repository.searchparam.IncludeParameterProcessor.revincludeProcessor;
import static ca.uhn.fhir.jpa.repository.searchparam.LastValueWinsParameterProcessor.lastValueWins;
import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTAINED;
import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE_ITERATE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE_RECURSE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE_ITERATE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE_RECURSE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SEARCH_TOTAL_MODE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SORT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SUMMARY;

public class SearchParameterMapQueryBuilder extends DefaultSearchQueryBuilder {
	/** Our unsupported specials */
	private static final Set<String> ourIgnoredSpecials = Set.of(
			"_elements", // implemented at the FHIR endpoint - not applicable in the repository
			"_containedType" // unsupported by JPA repositories
			);

	final SearchParameterMap mySearchParameterMapResult = new SearchParameterMap();

	SearchParameterMapQueryBuilder() {}

	@Override
	public ISearchQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theOrList) {
		if (ourIgnoredSpecials.contains(theParamName)) {
			return this;
		}
		// Is this a special parameter like _sort, _count, etc.?
		ISpecialParameterProcessor specialParameterProcessor = ourSpecialParamHandlers.get(theParamName);
		if (specialParameterProcessor != null) {
			specialParameterProcessor.process(theParamName, theOrList, mySearchParameterMapResult);
		} else {
			// this is a normal query parameter.
			mySearchParameterMapResult.addOrList(theParamName, theOrList);
		}

		return this;
	}

	/**
	 * Main entry point for converting from the IRepository interfaces to a JPA SearchParameterMap.
	 * @param theQueryContributor the query callback to convert
	 * @return a SearchParameterMap for use with JPA repositories
	 */
	public static SearchParameterMap buildFromQueryContributor(ISearchQueryContributor theQueryContributor) {
		SearchParameterMap searchParameterMap;
		// If the contributor is already a SearchParameterMap, use it directly.
		// This allows pass-though of stuff like $everything that isn't part of the main rest-query syntax.
		if (theQueryContributor instanceof SearchParameterMap theSp) {
			searchParameterMap = theSp;
		} else {
			// Build a SearchParameterMap from scratch.
			SearchParameterMapQueryBuilder builder = new SearchParameterMapQueryBuilder();
			theQueryContributor.contributeToQuery(builder);
			searchParameterMap = builder.build();
		}
		return searchParameterMap;
	}

	public SearchParameterMap build() {
		return mySearchParameterMapResult;
	}

	/**
	 * Handlers for all the specials
	 */
	Map<String, ISpecialParameterProcessor> ourSpecialParamHandlers = buildHandlerTable();

	@Nonnull
	private static Map<String, ISpecialParameterProcessor> buildHandlerTable() {
		Map<String, ISpecialParameterProcessor> lastWins = Map.of(
				PARAM_CONTAINED,
						lastValueWins(SearchContainedModeEnum::fromCode, SearchParameterMap::setSearchContainedMode),
				PARAM_COUNT, lastValueWins(Integer::parseInt, SearchParameterMap::setCount),
				PARAM_OFFSET, lastValueWins(Integer::parseInt, SearchParameterMap::setOffset),
				PARAM_SUMMARY, lastValueWins(SummaryEnum::fromCode, SearchParameterMap::setSummaryMode),
				PARAM_SEARCH_TOTAL_MODE,
						lastValueWins(SearchTotalModeEnum::fromCode, SearchParameterMap::setSearchTotalMode));

		Map<String, ISpecialParameterProcessor> includeProcessors = Map.of(
				PARAM_INCLUDE, includeProcessor(false),
				PARAM_INCLUDE_ITERATE, includeProcessor(true),
				PARAM_INCLUDE_RECURSE, includeProcessor(true),
				PARAM_REVINCLUDE, revincludeProcessor(false),
				PARAM_REVINCLUDE_ITERATE, revincludeProcessor(true),
				PARAM_REVINCLUDE_RECURSE, revincludeProcessor(true));

		Map<String, ISpecialParameterProcessor> handlers = new HashMap<>();
		handlers.put(PARAM_SORT, new SortProcessor());
		handlers.putAll(lastWins);
		handlers.putAll(includeProcessors);

		return Collections.unmodifiableMap(handlers);
	}

}
