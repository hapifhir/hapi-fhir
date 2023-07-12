/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;

import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A service that produces pages of resource pids based on the data provided by a previous batch step.  Typically the
 * first step in a batch job produces work chunks that define what types of data the batch operation will be performing
 * (e.g. a list of resource types and date ranges). This service is then used by the second step to actually query and
 * page through resource pids based on the chunk definitions produced by the first step.
 * @param <IT> This parameter defines constraints on the types of pids we are pulling (e.g. resource type, url, etc).
 */
public interface IIdChunkProducer<IT extends ChunkRangeJson> {
	/**
	 * Actually fetch the resource pids
	 * @param theNextStart pids are pulled with lastUpdated >= this date
	 * @param theEnd pids are pulled with lastUpdate <= this date
	 * @param thePageSize the number of pids to query at a time
	 * @param theRequestPartitionId partition for operation if rtequired
	 * @param theData defines the query we are using
	 * @return a list of Resource pids
	 */
	IResourcePidList fetchResourceIdsPage(
			Date theNextStart,
			Date theEnd,
			@Nonnull Integer thePageSize,
			@Nullable RequestPartitionId theRequestPartitionId,
			IT theData);
}
