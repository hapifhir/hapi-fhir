package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.impl.DefaultSearchQueryBuilder;
import ca.uhn.fhir.repository.impl.ISearchQueryBuilder;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTAINED;
import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SEARCH_TOTAL_MODE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SORT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SUMMARY;
import static com.google.common.base.Strings.isNullOrEmpty;

public class SearchParameterMapQueryBuilder extends DefaultSearchQueryBuilder {
	private final SearchParameterMap myResult = new SearchParameterMap();

	static final Set<String> ourUnsupportedSearchParameters = Set.of(
			"_include",
			"_revinclude",
			"_elements", // implemented at the FHIR endpoint.
			"_containedType" // unsupported by JPA repositories
			);
	final Map<String, Consumer<List<IQueryParameterType>>> mySpecialParamHandlers = Map.of(
			PARAM_COUNT, v -> lastValueWins(v, Integer::parseInt, myResult::setCount),
			PARAM_OFFSET, v -> lastValueWins(v, Integer::parseInt, myResult::setOffset),
			PARAM_SORT, this::addSortChain,
			PARAM_SEARCH_TOTAL_MODE, v -> lastValueWins(v, SearchTotalModeEnum::fromCode, myResult::setSearchTotalMode),
			PARAM_SUMMARY, v -> lastValueWins(v, SummaryEnum::fromCode, myResult::setSummaryMode),
			PARAM_CONTAINED,
					v -> lastValueWins(v, SearchContainedModeEnum::fromCode, myResult::setSearchContainedMode));

	private void addSortChain(List<IQueryParameterType> theSortItems) {

		List<SortSpec> sortSpecs = theSortItems.stream()
				.map(this::paramAsQueryString)
				.map(SortSpec::fromR3OrLaterParameterValue)
				.toList();

		// Sort is an intrusive linked list, but the head is a bare pointer in the SearchParameterMap.
		Consumer<SortSpec> sortAppendAction = myResult::setSort;
		for (SortSpec sortSpec : sortSpecs) {
			sortAppendAction.accept(sortSpec);
			sortAppendAction = sortSpec::setChain;
		}
	}

	public static SearchParameterMap buildFromQueryContributor(ISearchQueryContributor theQueryContributor) {
		SearchParameterMap searchParameterMap;
		if (theQueryContributor instanceof SearchParameterMap theSp) {
			searchParameterMap = theSp;
		} else {
			// Build a SearchParameterMap using the provided contributor
			SearchParameterMapQueryBuilder builder = new SearchParameterMapQueryBuilder();
			theQueryContributor.contributeToQuery(builder);
			searchParameterMap = builder.build();
		}
		return searchParameterMap;
	}

	@Override
	public ISearchQueryBuilder addOrList(String theParamName, List<IQueryParameterType> theOrList) {
		if (ourUnsupportedSearchParameters.contains(theParamName)) {
			throw new IllegalArgumentException(
					"Parameter '" + theParamName + "' is not currently supported by SearchParameterMapQueryBuilder. ");
		}
		Consumer<List<IQueryParameterType>> processor = mySpecialParamHandlers.get(theParamName);
		if (processor == null) {
			myResult.addOrList(theParamName, theOrList);
		} else {
			processor.accept(theOrList);
		}
		return this;
	}

	<T> void lastValueWins(
			List<IQueryParameterType> theValues, Function<String, T> theConverter, Consumer<T> theSPMapUpdater) {

		String lastValue = theValues.stream().map(this::paramAsQueryString).reduce(null, (l, r) -> r);

		T converted = isNullOrEmpty(lastValue) ? null : theConverter.apply(lastValue);

		theSPMapUpdater.accept(converted);
	}

	private String paramAsQueryString(IQueryParameterType theParameter) {
		return theParameter.getValueAsQueryToken(null);
	}

	public SearchParameterMap build() {
		return myResult;
	}
}
