package ca.uhn.fhir.batch2.jobs.step;

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

import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class PartitionedUrlListIdChunkProducer implements IIdChunkProducer<PartitionedUrlChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionedUrlListIdChunkProducer.class);
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public PartitionedUrlListIdChunkProducer(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(Date theNextStart, Date theEnd, @Nonnull Integer thePageSize, @Nullable RequestPartitionId theRequestPartitionId, PartitionedUrlChunkRangeJson theData) {
		PartitionedUrl partitionedUrl = theData.getPartitionedUrl();

		if (partitionedUrl == null) {
			ourLog.info("Fetching resource ID chunk for everything - Range {} - {}", theNextStart, theEnd);
			return myBatch2DaoSvc.fetchResourceIdsPage(theNextStart, theEnd, thePageSize, theRequestPartitionId, null);
		} else {
			ourLog.info("Fetching resource ID chunk for URL {} - Range {} - {}", partitionedUrl.getUrl(), theNextStart, theEnd);
			return myBatch2DaoSvc.fetchResourceIdsPage(theNextStart, theEnd, thePageSize, partitionedUrl.getRequestPartitionId(), partitionedUrl.getUrl());
		}
	}
}
