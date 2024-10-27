/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmMetrics;

import java.util.ArrayList;
import java.util.List;

public class LinkMetricTestParameters {
	/**
	 * The initial state (as to be fed into MdmLinkHelper)
	 */
	private String myInitialState;

	/**
	 * The filters for MatchResult
	 */
	private List<MdmMatchResultEnum> myMatchFilters;

	/**
	 * The filters for LinkSource
	 */
	private List<MdmLinkSourceEnum> myLinkSourceEnums;

	/**
	 * The expected metrics to be returned
	 */
	private MdmMetrics myExpectedMetrics;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public List<MdmMatchResultEnum> getMatchFilters() {
		if (myMatchFilters == null) {
			myMatchFilters = new ArrayList<>();
		}
		return myMatchFilters;
	}

	public void setMatchFilters(List<MdmMatchResultEnum> theMatchFilters) {
		myMatchFilters = theMatchFilters;
	}

	public List<MdmLinkSourceEnum> getLinkSourceFilters() {
		if (myLinkSourceEnums == null) {
			myLinkSourceEnums = new ArrayList<>();
		}
		return myLinkSourceEnums;
	}

	public void setLinkSourceFilters(List<MdmLinkSourceEnum> theLinkSourceEnums) {
		myLinkSourceEnums = theLinkSourceEnums;
	}

	public MdmMetrics getExpectedMetrics() {
		return myExpectedMetrics;
	}

	public void setExpectedMetrics(MdmMetrics theExpectedMetrics) {
		myExpectedMetrics = theExpectedMetrics;
	}
}
