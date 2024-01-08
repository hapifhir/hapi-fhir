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

import java.util.LinkedHashMap;
import java.util.Map;

public class MdmLinkScoreMetrics {

	private String myResourceType;

	/**
	 * Map of Score:Count
	 * Scores are typically Doubles. But we cast to string because
	 * Score is not a non-null field, and so "NULL" is a value.
	 */
	private Map<String, Long> myScoreCounts;

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
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
}
