package ca.uhn.fhir.batch2.coordinator;

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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.slf4j.Logger;

public class ReductionStepDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends BaseDataSink<PT, IT, OT> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IJobPersistence myJobPersistence;

	protected ReductionStepDataSink(String theInstanceId,
											  JobWorkCursor<PT, IT, OT> theJobWorkCursor,
											  JobDefinition<PT> theDefinition,
											  IJobPersistence thePersistence) {
		super(theInstanceId, theJobWorkCursor);
		myJobPersistence = thePersistence;
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		String dataString = JsonUtil.serialize(theData.getData(), false);
		// fixme mb move out to reduction executor and merge with final chunk resolution tx.
		myJobPersistence.updateReducerReport(getInstanceId(), dataString);
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
