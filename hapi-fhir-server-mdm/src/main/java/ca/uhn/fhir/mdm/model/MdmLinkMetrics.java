package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.HashMap;
import java.util.Map;

public class MdmLinkMetrics {
	/**
	 * The resource type to which these metrics apply.
	 */
	private String myResourceType;

	/**
	 * A mapping of MatchType -> LinkSource -> count.
	 * Eg:
	 * MATCH
	 * 		AUTO    - 2
	 * 	 	MANUAL  - 1
	 * NO_MATCH
	 *      AUTO    - 1
	 *      MANUAL  - 3
	 */
	private Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> myMatchTypeToLinkToCountMap;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> getMatchTypeToLinkToCountMap() {
		if (myMatchTypeToLinkToCountMap == null) {
			myMatchTypeToLinkToCountMap = new HashMap<>();
		}
		return myMatchTypeToLinkToCountMap;
	}

	public void addMetric(
			MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theLinkSourceEnum, long theCount) {
		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> map = getMatchTypeToLinkToCountMap();

		if (!map.containsKey(theMdmMatchResultEnum)) {
			map.put(theMdmMatchResultEnum, new HashMap<>());
		}
		Map<MdmLinkSourceEnum, Long> lsToCountMap = map.get(theMdmMatchResultEnum);
		lsToCountMap.put(theLinkSourceEnum, theCount);
	}
}
