/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.parameters.BatchJobParametersWithTaskId;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplaceReferencesJobParameters extends BatchJobParametersWithTaskId {

	@JsonProperty("sourceId")
	private FhirIdJson mySourceId;

	@JsonProperty("targetId")
	private FhirIdJson myTargetId;

	@JsonProperty(
			value = "resourceLimit",
			defaultValue = ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT_STRING,
			required = false)
	private int myResourceLimit;

	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	public ReplaceReferencesJobParameters() {}

	public ReplaceReferencesJobParameters(ReplaceReferencesRequest theRequest) {
		mySourceId = new FhirIdJson(theRequest.sourceId);
		myTargetId = new FhirIdJson(theRequest.targetId);
		myResourceLimit = theRequest.resourceLimit;
		myPartitionId = theRequest.partitionId;
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

	public int getResourceLimit() {
		if (myResourceLimit <= 0) {
			myResourceLimit = ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT;
		}
		return myResourceLimit;
	}

	public void setResourceLimit(int theResourceLimit) {
		myResourceLimit = theResourceLimit;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public ReplaceReferencesRequest asReplaceReferencesRequest() {
		return new ReplaceReferencesRequest(mySourceId.asIdDt(), myTargetId.asIdDt(), myResourceLimit, myPartitionId);
	}
}
