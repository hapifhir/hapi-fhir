/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
