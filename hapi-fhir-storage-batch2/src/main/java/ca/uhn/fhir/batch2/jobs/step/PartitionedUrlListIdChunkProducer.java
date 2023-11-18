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

import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import java.util.Date;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class PartitionedUrlListIdChunkProducer implements IIdChunkProducer<PartitionedUrlChunkRangeJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public PartitionedUrlListIdChunkProducer(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Override
	public IResourcePidStream fetchResourceIdStream(
			Date theStart,
			Date theEnd,
			@Nullable RequestPartitionId theRequestPartitionId,
			PartitionedUrlChunkRangeJson theData) {
		PartitionedUrl partitionedUrl = theData.getPartitionedUrl();

		RequestPartitionId targetPartitionId;
		String theUrl;

		if (partitionedUrl == null) {
			theUrl = null;
			targetPartitionId = theRequestPartitionId;
			ourLog.info("Fetching resource ID chunk for everything - Range {} - {}", theStart, theEnd);
		} else {
			theUrl = partitionedUrl.getUrl();
			targetPartitionId = defaultIfNull(partitionedUrl.getRequestPartitionId(), theRequestPartitionId);
			ourLog.info(
					"Fetching resource ID chunk for URL {} - Range {} - {}", partitionedUrl.getUrl(), theStart, theEnd);
		}

		return myBatch2DaoSvc.fetchResourceIdStream(theStart, theEnd, targetPartitionId, theUrl);
	}
}
