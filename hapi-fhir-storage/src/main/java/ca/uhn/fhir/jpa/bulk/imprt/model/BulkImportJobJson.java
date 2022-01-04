package ca.uhn.fhir.jpa.bulk.imprt.model;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportJobJson implements IModelJson {

	@JsonProperty("processingMode")
	private JobFileRowProcessingModeEnum myProcessingMode;
	@JsonProperty("jobDescription")
	private String myJobDescription;
	@JsonProperty("fileCount")
	private int myFileCount;
	@JsonProperty("batchSize")
	private int myBatchSize;

	public String getJobDescription() {
		return myJobDescription;
	}

	public BulkImportJobJson setJobDescription(String theJobDescription) {
		myJobDescription = theJobDescription;
		return this;
	}

	public JobFileRowProcessingModeEnum getProcessingMode() {
		return myProcessingMode;
	}

	public BulkImportJobJson setProcessingMode(JobFileRowProcessingModeEnum theProcessingMode) {
		myProcessingMode = theProcessingMode;
		return this;
	}

	public int getFileCount() {
		return myFileCount;
	}

	public BulkImportJobJson setFileCount(int theFileCount) {
		myFileCount = theFileCount;
		return this;
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public BulkImportJobJson setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
		return this;
	}
}
