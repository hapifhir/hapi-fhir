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

public class MdmResourceMetrics {

	/**
	 * The resource type to which these metrics apply.
	 */
	private String myResourceType;

	/**
	 * The number of golden resources.
	 */
	private long myGoldenResourcesCount;

	/**
	 * The number of source resources.
	 */
	private long mySourceResourcesCount;

	/**
	 * The number of excluded resources.
	 * These are necessarily a subset of both
	 * GoldenResources and SourceResources
	 * (as each Blocked resource will still generate
	 * a GoldenResource)
	 */
	private long myExcludedResources;

	public String getResourceType() {
		return myResourceType;
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
}
