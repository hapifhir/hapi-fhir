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
package ca.uhn.fhir.mdm.api.params;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.ArrayList;
import java.util.List;

public class GenerateScoreMetricsParameters {
	/**
	 * The resource type of interest.
	 */
	private final String myResourceType;

	/**
	 * MatchResult types to filter for.
	 * Specified MatchResults will be included.
	 * If none specified, all will be included.
	 */
	private List<MdmMatchResultEnum> myMatchTypeFilters;

	public GenerateScoreMetricsParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public List<MdmMatchResultEnum> getMatchTypes() {
		if (myMatchTypeFilters == null) {
			myMatchTypeFilters = new ArrayList<>();
		}
		return myMatchTypeFilters;
	}

	public void addMatchType(MdmMatchResultEnum theMatchType) {
		getMatchTypes().add(theMatchType);
	}
}
