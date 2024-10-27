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

import java.util.ArrayList;
import java.util.List;

public class ResourceMetricTestParams {
	/**
	 * The initial state, as consumable by
	 * MdmLinkHelper.
	 */
	private String myInitialState;

	/**
	 * The list of Golden Resource Ids (in initial state) that should be
	 * saved as BlockedResources
	 */
	private List<String> myBlockedResourceGoldenResourceIds;

	private long myExpectedResourceCount;

	private long myExpectedGoldenResourceCount;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public List<String> getBlockedResourceGoldenResourceIds() {
		if (myBlockedResourceGoldenResourceIds == null) {
			myBlockedResourceGoldenResourceIds = new ArrayList<>();
		}
		return myBlockedResourceGoldenResourceIds;
	}

	public void addBlockedResourceGoldenResources(String theBlockedResourceId) {
		getBlockedResourceGoldenResourceIds().add(theBlockedResourceId);
	}

	public long getExpectedResourceCount() {
		return myExpectedResourceCount;
	}

	public void setExpectedResourceCount(long theExpectedResourceCount) {
		myExpectedResourceCount = theExpectedResourceCount;
	}

	public long getExpectedGoldenResourceCount() {
		return myExpectedGoldenResourceCount;
	}

	public void setExpectedGoldenResourceCount(long theExpectedGoldenResourceCount) {
		myExpectedGoldenResourceCount = theExpectedGoldenResourceCount;
	}

	public long getExpectedBlockedResourceCount() {
		return getBlockedResourceGoldenResourceIds().size();
	}
}
