package ca.uhn.fhir.batch2.jobs.parameters;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class PartitionedJobParameters implements IModelJson {
	@JsonProperty(value = "partitionId")
	@Nullable
	private RequestPartitionId myRequestPartitionId;
	@JsonProperty("batchSize")
	@Nullable
	private Integer myBatchSize;

	@Nullable
	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(@Nullable RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Nullable
	public Integer getBatchSize() {
		return myBatchSize;
	}
}
