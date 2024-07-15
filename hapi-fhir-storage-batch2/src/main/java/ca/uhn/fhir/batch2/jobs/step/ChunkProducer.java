/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

public class ChunkProducer implements IIdChunkProducer<ChunkRangeJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	private final IBatch2DaoSvc myBatch2DaoSvc;

	public ChunkProducer(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	@Override
	public IResourcePidStream fetchResourceIdStream(ChunkRangeJson theData) {
		String theUrl = theData.getUrl();
		RequestPartitionId targetPartitionId = theData.getPartitionId();
		ourLog.info(
				"Fetching resource ID chunk in partition {} for URL {} - Range {} - {}",
				targetPartitionId,
				theUrl,
				theData.getStart(),
				theData.getEnd());

		return myBatch2DaoSvc.fetchResourceIdStream(theData.getStart(), theData.getEnd(), targetPartitionId, theUrl);
	}
}
