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
package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.parameters.BatchJobParametersWithTaskId;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.util.List;

public class ReplaceReferencesJobParameters extends BatchJobParametersWithTaskId {

	@JsonProperty("sourceId")
	private FhirIdJson mySourceId;

	@JsonProperty("targetId")
	private FhirIdJson myTargetId;

	@JsonProperty(
			value = "batchSize",
			defaultValue = ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT_STRING,
			required = false)
	private int myBatchSize;

	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	/**
	 * If true, a Provenance resource will be created at the end of the job.
	 */
	@JsonProperty(value = "createProvenance", defaultValue = "true", required = false)
	private boolean myCreateProvenance;

	/**
	 * The version of the source resource that should be used in the Provenance resource.
	 * The $merge operation populates this after the source resource is updated as a result of the merge.
	 */
	@JsonProperty("sourceVersionForProvenance")
	private String mySourceVersionForProvenance;

	/**
	 * The version of the target resource that should be used in the Provenance resource.
	 * The $merge operation populates this after the target resource is updated as a result of the merge.
	 */
	@JsonProperty("targetVersionForProvenance")
	private String myTargetVersionForProvenance;

	/**
	 * The agents to be used in the Provenance resource.
	 */
	@JsonProperty("provenanceAgents")
	private List<ProvenanceAgentJson> myProvenanceAgents;

	public ReplaceReferencesJobParameters() {}

	public ReplaceReferencesJobParameters(
			ReplaceReferencesRequest theReplaceReferencesRequest,
			int theBatchSize,
			String theSourceVersionForProvenance,
			String theTargetVersionForProvenance,
			@Nullable List<ProvenanceAgentJson> theProvenanceAgents) {

		mySourceId = new FhirIdJson(theReplaceReferencesRequest.sourceId);
		myTargetId = new FhirIdJson(theReplaceReferencesRequest.targetId);
		// Note theReplaceReferencesRequest.resourceLimit is only used for the synchronous case. It is ignored in this
		// async case.
		myBatchSize = theBatchSize;
		myPartitionId = theReplaceReferencesRequest.partitionId;
		myCreateProvenance = theReplaceReferencesRequest.createProvenance;
		mySourceVersionForProvenance = theSourceVersionForProvenance;
		myTargetVersionForProvenance = theTargetVersionForProvenance;
		myProvenanceAgents = theProvenanceAgents;
	}

	public FhirIdJson getSourceId() {
		return mySourceId;
	}

	public void setSourceId(FhirIdJson theSourceId) {
		mySourceId = theSourceId;
	}

	public FhirIdJson getTargetId() {
		return myTargetId;
	}

	public void setTargetId(FhirIdJson theTargetId) {
		myTargetId = theTargetId;
	}

	public int getBatchSize() {
		if (myBatchSize <= 0) {
			myBatchSize = JpaStorageSettings.DEFAULT_TRANSACTION_ENTRIES_FOR_WRITE;
		}
		return myBatchSize;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public ReplaceReferencesRequest asReplaceReferencesRequest(FhirContext theFhirContext) {
		return new ReplaceReferencesRequest(
				mySourceId.asIdDt(),
				myTargetId.asIdDt(),
				myBatchSize,
				myPartitionId,
				myCreateProvenance,
				ProvenanceAgentJson.toIProvenanceAgents(myProvenanceAgents, theFhirContext));
	}

	public boolean getCreateProvenance() {
		return myCreateProvenance;
	}

	public void setCreateProvenance(boolean theCreateProvenance) {
		this.myCreateProvenance = theCreateProvenance;
	}

	public String getSourceVersionForProvenance() {
		return mySourceVersionForProvenance;
	}

	public void setSourceVersionForProvenance(String theSourceVersionForProvenance) {
		this.mySourceVersionForProvenance = theSourceVersionForProvenance;
	}

	public String getTargetVersionForProvenance() {
		return myTargetVersionForProvenance;
	}

	public void setTargetVersionForProvenance(String theTargetVersionForProvenance) {
		this.myTargetVersionForProvenance = theTargetVersionForProvenance;
	}

	public List<ProvenanceAgentJson> getProvenanceAgents() {
		return myProvenanceAgents;
	}

	public void setProvenanceAgents(List<ProvenanceAgentJson> theAgents) {
		this.myProvenanceAgents = theAgents;
	}
}
