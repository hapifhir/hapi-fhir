package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;

import static ca.uhn.fhir.jpa.searchparam.SearchParameterMap.INTEGER_0;

public abstract class AbstractSearchSvc {
	public static final int DEFAULT_SYNC_SIZE = 250;

	abstract DaoConfig getDaoConfig();

	boolean isWantCount(SearchParameterMap myParams, boolean wantOnlyCount) {
		return wantOnlyCount ||
			SearchTotalModeEnum.ACCURATE.equals(myParams.getSearchTotalMode()) ||
			(myParams.getSearchTotalMode() == null && SearchTotalModeEnum.ACCURATE.equals(getDaoConfig().getDefaultTotalMode()));
	}

	static boolean isWantOnlyCount(SearchParameterMap myParams) {
		return SummaryEnum.COUNT.equals(myParams.getSummaryMode())
			| INTEGER_0.equals(myParams.getCount());
	}

}
