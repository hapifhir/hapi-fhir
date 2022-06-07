package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;

import static ca.uhn.fhir.jpa.searchparam.SearchParameterMap.INTEGER_0;

public class SearchParameterMapCalculator {

	static public boolean isWantCount(SearchParameterMap myParams) {
		return isWantCount(myParams.getSearchTotalMode());
	}

	static public boolean isWantCount(SearchTotalModeEnum theSearchTotalModeEnum){
		return SearchTotalModeEnum.ACCURATE.equals(theSearchTotalModeEnum);
	}

	static public boolean isWantOnlyCount(SearchParameterMap myParams) {
		return SummaryEnum.COUNT.equals(myParams.getSummaryMode())
			| INTEGER_0.equals(myParams.getCount());
	}

}
