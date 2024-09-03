package ca.uhn.fhir.jpa.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
	This class encapsulate the implementation providing a workaround to a known issue involving Hibernate. If queries are used with "in" clauses with large and varying
	numbers of parameters, this can overwhelm Hibernate's QueryPlanCache and deplete heap space. See the following link for more info:
	https://stackoverflow.com/questions/31557076/spring-hibernate-query-plan-cache-memory-usage.

	Normalizing the number of parameters in the "in" clause stabilizes the size of the QueryPlanCache, so long as the number of
	arguments never exceeds the maximum specified below.
*/
public class InClauseNormalizer {

	public static List<Long> normalizeIdListForInClause(List<Long> theResourceIds) {

		List<Long> retVal = theResourceIds;

		int listSize = theResourceIds.size();

		if (listSize > 1 && listSize < 10) {
			retVal = padIdListWithPlaceholders(theResourceIds, 10);
		} else if (listSize > 10 && listSize < 50) {
			retVal = padIdListWithPlaceholders(theResourceIds, 50);
		} else if (listSize > 50 && listSize < 100) {
			retVal = padIdListWithPlaceholders(theResourceIds, 100);
		} else if (listSize > 100 && listSize < 200) {
			retVal = padIdListWithPlaceholders(theResourceIds, 200);
		} else if (listSize > 200 && listSize < 500) {
			retVal = padIdListWithPlaceholders(theResourceIds, 500);
		} else if (listSize > 500 && listSize < 800) {
			retVal = padIdListWithPlaceholders(theResourceIds, 800);
		}

		return retVal;
	}

	private static List<Long> padIdListWithPlaceholders(List<Long> theIdList, int preferredListSize) {
		List<Long> retVal = theIdList;

		if (isUnmodifiableList(theIdList)) {
			retVal = new ArrayList<>(preferredListSize);
			retVal.addAll(theIdList);
		}

		while (retVal.size() < preferredListSize) {
			retVal.add(-1L);
		}

		return retVal;
	}

	private static boolean isUnmodifiableList(List<Long> theList) {
		try {
			theList.addAll(Collections.emptyList());
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	private InClauseNormalizer() {}
}
