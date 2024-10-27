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
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MdmMetrics extends MdmResourceMetrics implements IModelJson {

	@JsonProperty("resourceType")
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
	@JsonProperty("matchResult2linkSource2count")
	private Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> myMatchTypeToLinkToCountMap;

	/**
	 * Score buckets (in brackets of 0.01 size, and null) to counts.
	 */
	@JsonProperty("scoreCounts")
	private Map<String, Long> myScoreCounts;

	/**
	 * The number of golden resources.
	 */
	@JsonProperty("goldenResources")
	private long myGoldenResourcesCount;

	/**
	 * The number of source resources.
	 */
	@JsonProperty("sourceResources")
	private long mySourceResourcesCount;

	/**
	 * The number of excluded resources.
	 * These are necessarily a subset of both
	 * GoldenResources and SourceResources
	 * (as each Blocked resource will still generate
	 * a GoldenResource)
	 */
	@JsonProperty("excludedResources")
	private long myExcludedResources;

	public String getResourceType() {
		return myResourceType;
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

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public long getGoldenResourcesCount() {
		return myGoldenResourcesCount;
	}

	public void setGoldenResourcesCount(long theGoldenResourcesCount) {
		myGoldenResourcesCount = theGoldenResourcesCount;
	}

	public long getSourceResourcesCount() {
		return mySourceResourcesCount;
	}

	public void setSourceResourcesCount(long theSourceResourcesCount) {
		mySourceResourcesCount = theSourceResourcesCount;
	}

	public long getExcludedResources() {
		return myExcludedResources;
	}

	public void setExcludedResources(long theExcludedResources) {
		myExcludedResources = theExcludedResources;
	}

	public Map<String, Long> getScoreCounts() {
		if (myScoreCounts == null) {
			myScoreCounts = new LinkedHashMap<>();
		}
		return myScoreCounts;
	}

	public void addScore(String theScore, Long theCount) {
		getScoreCounts().put(theScore, theCount);
	}

	public static MdmMetrics fromSeperableMetrics(
			MdmResourceMetrics theMdmResourceMetrics,
			MdmLinkMetrics theLinkMetrics,
			MdmLinkScoreMetrics theLinkScoreMetrics) {
		MdmMetrics metrics = new MdmMetrics();
		metrics.setResourceType(theMdmResourceMetrics.getResourceType());
		metrics.setExcludedResources(theMdmResourceMetrics.getExcludedResources());
		metrics.setGoldenResourcesCount(theMdmResourceMetrics.getGoldenResourcesCount());
		metrics.setSourceResourcesCount(theMdmResourceMetrics.getSourceResourcesCount());
		metrics.myMatchTypeToLinkToCountMap = theLinkMetrics.getMatchTypeToLinkToCountMap();
		metrics.myScoreCounts = theLinkScoreMetrics.getScoreCounts();
		return metrics;
	}
}
