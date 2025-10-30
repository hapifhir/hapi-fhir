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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * During the bulk import process, we break the import files into smaller collections of resources  for manageable
 * ingestion transactions. Each of these collections is a batch2 chunk which is processed by {@link ConsumeFilesStepV2},
 * which ingests the collection and produces one of these objects containing details about how the ingestion of
 * that collection went. The {@link GenerateReportReductionStep} takes these chunks and generates a consolidated
 * report at the end.
 */
public class ConsumeFilesOutcomeJson implements IModelJson {

	@JsonProperty("sourceName")
	private String mySourceName;

	@JsonProperty("outcomeCount")
	private Map<StorageResponseCodeEnum, Integer> myOutcomeCount;

	@JsonProperty("errors")
	private List<String> myErrors;

	public String getSourceName() {
		return mySourceName;
	}

	public void setSourceName(String theSourceName) {
		mySourceName = theSourceName;
	}

	public Map<StorageResponseCodeEnum, Integer> getOutcomeCount() {
		if (myOutcomeCount == null) {
			myOutcomeCount = new HashMap<>();
		}
		return myOutcomeCount;
	}

	public List<String> getErrors() {
		if (myErrors == null) {
			myErrors = new ArrayList<>();
		}
		return myErrors;
	}

	public void addOutcome(StorageResponseCodeEnum theStorageResponseCode) {
		addOutcome(theStorageResponseCode, 1);
	}

	public void addOutcome(StorageResponseCodeEnum theStorageResponseCode, int theDelta) {
		Map<StorageResponseCodeEnum, Integer> outcomeCount = getOutcomeCount();
		Integer existing = outcomeCount.getOrDefault(theStorageResponseCode, 0);
		outcomeCount.put(theStorageResponseCode, existing + theDelta);
	}

	public void addError(String theError) {
		getErrors().add(theError);
	}

	/**
	 * Adds the given data into this instance
	 *
	 * @param theData The data to add
	 */
	public void add(ConsumeFilesOutcomeJson theData) {
		for (String error : theData.getErrors()) {
			addError(error);
		}
		for (Map.Entry<StorageResponseCodeEnum, Integer> outcomeCount :
				theData.getOutcomeCount().entrySet()) {
			addOutcome(outcomeCount.getKey(), outcomeCount.getValue());
		}
	}

	public boolean hasOutcomes() {
		return !getOutcomeCount().isEmpty();
	}

	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}
}
