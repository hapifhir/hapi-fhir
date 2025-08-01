package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.repository.impl.ISearchQueryBuilder;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTAINED;
import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE_ITERATE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE_ITERATE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SEARCH_TOTAL_MODE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SORT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SUMMARY;

record SearchParameterMapContributor(SearchParameterMap mySearchParameterMap, ISearchQueryBuilder myBuilder) {

	public void contributeToQuery() {
		addSearchParameters();
		addToken(PARAM_CONTAINED, mySearchParameterMap.getSearchContainedMode(), SearchContainedModeEnum::getCode);
		addNumeric(PARAM_COUNT, mySearchParameterMap.getCount());
		addSort(mySearchParameterMap.getSort());
		addToken(PARAM_SUMMARY, mySearchParameterMap.getSummaryMode(), SummaryEnum::getCode);
		addToken(PARAM_SEARCH_TOTAL_MODE, mySearchParameterMap.getSearchTotalMode(), SearchTotalModeEnum::getCode);
		addIncludes(PARAM_INCLUDE, PARAM_INCLUDE_ITERATE, mySearchParameterMap.getIncludes());
		addIncludes(PARAM_REVINCLUDE, PARAM_REVINCLUDE_ITERATE, mySearchParameterMap.getRevIncludes());
	}

	private void addSearchParameters() {
		mySearchParameterMap.entrySet().forEach(nextAndOrListEntry -> {
			for (List<IQueryParameterType> nextOrList : nextAndOrListEntry.getValue()) {
				if (nextOrList.isEmpty()) {
					continue;
				}
				myBuilder.addOrList(nextAndOrListEntry.getKey(), nextOrList);
			}
		});
	}

	private void addIncludes(String paramInclude, String paramIncludeIterate, Set<Include> theIncludes) {
		for (Include nextInclude : theIncludes) {
			if (nextInclude.isRecurse()) {
				myBuilder.addOrList(paramIncludeIterate, new TokenParam(nextInclude.getValue()));
			} else {
				myBuilder.addOrList(paramInclude, new TokenParam(nextInclude.getValue()));
			}
		}
	}

	private void addNumeric(String theParamName, Integer theValue) {
		if (theValue != null) {
			myBuilder.addNumericParameter(theParamName, theValue);
		}
	}

	private void addSort(SortSpec theSort) {
		SortSpec sort = theSort;
		if (sort != null) {
			List<IQueryParameterType> paraValues = new ArrayList<>();
			for (; sort != null; sort = sort.getChain()) {
				paraValues.add(new TokenParam(toSortString(sort)));
			}
			myBuilder.addOrList(PARAM_SORT, paraValues);
		}
	}

	@Nonnull
	private static String toSortString(SortSpec sort) {
		if (SortOrderEnum.DESC.equals(sort.getOrder())) {
			return "-" + sort.getParamName();
		} else {
			return sort.getParamName();
		}
	}

	private <T> void addToken(String theParamName, T theValue, Function<T, String> theConverter) {
		if (theValue != null) {
			myBuilder.addOrList(theParamName, new TokenParam(theConverter.apply(theValue)));
		}
	}
}
