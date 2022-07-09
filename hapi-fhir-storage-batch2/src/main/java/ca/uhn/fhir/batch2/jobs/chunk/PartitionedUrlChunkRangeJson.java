package ca.uhn.fhir.batch2.jobs.chunk;

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

import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class PartitionedUrlChunkRangeJson extends ChunkRangeJson {
	@Nullable
	@JsonProperty("partitionedUrl")
	private PartitionedUrl myPartitionedUrl;

	@Nullable
	public PartitionedUrl getPartitionedUrl() {
		return myPartitionedUrl;
	}

	public void setPartitionedUrl(@Nullable PartitionedUrl thePartitionedUrl) {
		myPartitionedUrl = thePartitionedUrl;
	}
}
