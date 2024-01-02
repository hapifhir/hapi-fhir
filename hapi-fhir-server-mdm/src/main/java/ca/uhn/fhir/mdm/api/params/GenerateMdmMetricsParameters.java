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

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.ArrayList;
import java.util.List;

public class GenerateMdmMetricsParameters {

	/**
	 * We only allow finding metrics by resource type
	 */
	private final String myResourceType;

	/**
	 * The MDM MatchResult types of interest.
	 * Specified MatchResults will be included.
	 * If none are specified, all will be included.
	 */
	private List<MdmMatchResultEnum> myMatchResultFilters;

	/**
	 * The MDM Link values of interest.
	 * Specified LinkSources will be included.
	 * If none are specified, all are included.
	 */
	private List<MdmLinkSourceEnum> myLinkSourceFilters;

	public GenerateMdmMetricsParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public List<MdmMatchResultEnum> getMatchResultFilters() {
		if (myMatchResultFilters == null) {
			myMatchResultFilters = new ArrayList<>();
		}
		return myMatchResultFilters;
	}

	public void addMatchResult(MdmMatchResultEnum theMdmMatchResultEnum) {
		getMatchResultFilters().add(theMdmMatchResultEnum);
	}

	public List<MdmLinkSourceEnum> getLinkSourceFilters() {
		if (myLinkSourceFilters == null) {
			myLinkSourceFilters = new ArrayList<>();
		}
		return myLinkSourceFilters;
	}

	public void addLinkSource(MdmLinkSourceEnum theLinkSource) {
		getLinkSourceFilters().add(theLinkSource);
	}

	//	public GenerateMdmLinkMetricParameters toLinkMetricParams() {
	//
	//	}
	//
	//	public GenerateMdmResourceMetricsParameters toResourceMetricParams() {
	//
	//	}
	//

}
