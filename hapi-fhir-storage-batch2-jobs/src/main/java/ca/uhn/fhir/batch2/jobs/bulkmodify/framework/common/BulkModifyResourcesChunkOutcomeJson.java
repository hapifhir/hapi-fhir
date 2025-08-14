/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkModifyResourcesChunkOutcomeJson implements IModelJson {

	@JsonProperty("changedIds")
	private List<String> myChangedIds;

	@JsonProperty("unchangedIds")
	private List<String> myUnchangedIds;

	@JsonProperty("failures")
	private Map<String, String> myFailures;

	@JsonProperty("chunkRetryCount")
	private int myChunkRetryCount;

	@JsonProperty("resourceRetryCount")
	private int myResourceRetryCount;

	public void addChangedId(IIdType theIdElement) {
		getChangedIds().add(theIdElement.toUnqualified().getValue());
	}

	public List<String> getChangedIds() {
		if (myChangedIds == null) {
			myChangedIds = new ArrayList<>();
		}
		return myChangedIds;
	}

	public void addUnchangedId(IIdType theIdElement) {
		getUnchangedIds().add(theIdElement.toUnqualified().getValue());
	}

	public List<String> getUnchangedIds() {
		if (myUnchangedIds == null) {
			myUnchangedIds = new ArrayList<>();
		}
		return myUnchangedIds;
	}

	public void addFailure(IIdType theId, String theMessage) {
		getFailures().put(theId.toUnqualified().getValue(), theMessage);
	}

	/**
	 * Key: Resource ID
	 * Value: Failure Message
	 */
	public Map<String, String> getFailures() {
		if (myFailures == null) {
			myFailures = new HashMap<>();
		}
		return myFailures;
	}

	public int getChunkRetryCount() {
		return myChunkRetryCount;
	}

	public void setChunkRetryCount(int theChunkRetryCount) {
		myChunkRetryCount = theChunkRetryCount;
	}

	public int getResourceRetryCount() {
		return myResourceRetryCount;
	}

	public void setResourceRetryCount(int theResourceRetryCount) {
		myResourceRetryCount = theResourceRetryCount;
	}
}
