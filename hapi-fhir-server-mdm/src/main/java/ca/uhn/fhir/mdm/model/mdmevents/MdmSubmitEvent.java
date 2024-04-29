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
package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmSubmitEvent implements IModelJson {

	/**
	 * Batch size; only applicable if this is a batch job
	 */
	@JsonProperty("batchSize")
	private Long myBatchSize;

	/**
	 * The search/resource urls used in this job
	 */
	@JsonProperty("urls")
	private List<String> myUrls;

	/**
	 * True if this submit was done asynchronously
	 * (ie, was submitted as a batch job).
	 * False if submitted directly to mdm.
	 */
	@JsonProperty("batch_job")
	private boolean myIsBatchJob;

	public Long getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(Long theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public List<String> getUrls() {
		if (myUrls == null) {
			myUrls = new ArrayList<>();
		}
		return myUrls;
	}

	public void setUrls(List<String> theUrls) {
		myUrls = theUrls;
	}

	public MdmSubmitEvent addUrl(String theUrl) {
		getUrls().add(theUrl);
		return this;
	}

	public boolean isBatchJob() {
		return myIsBatchJob;
	}

	public void setBatchJob(boolean theBatchJob) {
		myIsBatchJob = theBatchJob;
	}
}
