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

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmMetrics;

import java.util.ArrayList;
import java.util.List;

public class LinkScoreMetricTestParams {
	private String myInitialState;

	private List<MdmMatchResultEnum> myMatchFilter;

	private MdmMetrics myExpectedMetrics;

	/**
	 * The scores for each link.
	 * The order should match the order of the
	 * links listed in initial state.
	 */
	private List<Double> myScores;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public MdmMetrics getExpectedMetrics() {
		return myExpectedMetrics;
	}

	public void setExpectedMetrics(MdmMetrics theExpectedMetrics) {
		myExpectedMetrics = theExpectedMetrics;
	}

	public List<MdmMatchResultEnum> getMatchFilter() {
		if (myMatchFilter == null) {
			myMatchFilter = new ArrayList<>();
		}
		return myMatchFilter;
	}

	public void addMatchType(MdmMatchResultEnum theResultEnum) {
		getMatchFilter().add(theResultEnum);
	}

	public List<Double> getScores() {
		if (myScores == null) {
			myScores = new ArrayList<>();
		}
		return myScores;
	}

	public void setScores(List<Double> theScores) {
		myScores = theScores;
	}
}
