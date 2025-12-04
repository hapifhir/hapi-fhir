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
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public class BulkModifyResourcesChunkOutcomeJson implements IModelJson {

	@JsonProperty("changedIds")
	private List<String> myChangedIds;

	@JsonProperty("changedResourceBodies")
	@JsonRawValue
	@JsonDeserialize(using = ArrayOfRawJsonObjectsAsStringsDeserializer.class)
	private List<String> myChangedResourceBodies;

	@JsonProperty("deletedIds")
	private List<String> myDeletedIds;

	@JsonProperty("unchangedIds")
	private List<String> myUnchangedIds;

	@JsonProperty("failures")
	private Map<String, String> myFailures;

	@JsonProperty("chunkRetryCount")
	private Integer myChunkRetryCount;

	@JsonProperty("resourceRetryCount")
	private Integer myResourceRetryCount;

	public void addChangedId(String theId) {
		getChangedIds().add(theId);
	}

	public void addDeletedId(String theId) {
		getDeletedIds().add(theId);
	}

	public List<String> getChangedResourceBodies() {
		if (myChangedResourceBodies == null) {
			myChangedResourceBodies = new ArrayList<>();
		}
		return myChangedResourceBodies;
	}

	public List<String> getChangedIds() {
		if (myChangedIds == null) {
			myChangedIds = new ArrayList<>();
		}
		return myChangedIds;
	}

	public List<String> getDeletedIds() {
		if (myDeletedIds == null) {
			myDeletedIds = new ArrayList<>();
		}
		return myDeletedIds;
	}

	public void addUnchangedId(String theId) {
		getUnchangedIds().add(theId);
	}

	public List<String> getUnchangedIds() {
		if (myUnchangedIds == null) {
			myUnchangedIds = new ArrayList<>();
		}
		return myUnchangedIds;
	}

	public void addFailure(String theId, String theMessage) {
		Validate.notBlank(theId, "theId must not be blank");
		getFailures().put(theId, theMessage);
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
		return getIfNull(myChunkRetryCount, 0);
	}

	public void setChunkRetryCount(int theChunkRetryCount) {
		myChunkRetryCount = theChunkRetryCount;
	}

	public int getResourceRetryCount() {
		return getIfNull(myResourceRetryCount, 0);
	}

	public void setResourceRetryCount(int theResourceRetryCount) {
		myResourceRetryCount = theResourceRetryCount;
	}

	public void addChangedResourceBody(@Nonnull String theChangedResourceBody) {
		Validate.notBlank(theChangedResourceBody, "theChangedResourceBody must not be blank");
		Validate.isTrue(
				theChangedResourceBody.startsWith("{") && theChangedResourceBody.endsWith("}"),
				"theChangedResourceBody must be JSON encoded");
		getChangedResourceBodies().add(theChangedResourceBody);
	}
}
