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
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Optional;

public class ReductionStepDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends BaseDataSink<PT, IT, OT> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	public ReductionStepDataSink(String theInstanceId,
										  JobWorkCursor<PT, IT, OT> theJobWorkCursor,
										  IJobPersistence thePersistence,
										  JobDefinitionRegistry theJobDefinitionRegistry) {
		super(theInstanceId, theJobWorkCursor);
		myJobPersistence = thePersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		String instanceId = getInstanceId();
		Optional<JobInstance> instanceOp = myJobPersistence.fetchInstance(instanceId);
		if (instanceOp.isPresent()) {
			JobInstance instance = instanceOp.get();

			if (instance.getReport() != null) {
				// last in wins - so we won't throw
				ourLog.error("Report has already been set. Now it is being overwritten. Last in will win!");
			}

			JobChunkProgressAccumulator progressAccumulator = new JobChunkProgressAccumulator();
			JobInstanceProgressCalculator myJobInstanceProgressCalculator = new JobInstanceProgressCalculator(myJobPersistence, progressAccumulator, myJobDefinitionRegistry);
			myJobInstanceProgressCalculator.calculateInstanceProgressAndPopulateInstance(instance);

			OT data = theData.getData();
			String dataString = JsonUtil.serialize(data, false);
			instance.setReport(dataString);
			instance.setStatus(StatusEnum.COMPLETED);
			instance.setEndTime(new Date());

			ourLog.info("Finalizing job instance {} with report length {} chars", instance.getInstanceId(), dataString.length());
			ourLog.atTrace()
				.addArgument(() -> JsonUtil.serialize(instance))
				.log("New instance state: {}");

			myJobPersistence.updateInstance(instance);

			ourLog.info("Finalized job instance {} with report length {} chars", instance.getInstanceId(), dataString.length());

		} else {
			String msg = "No instance found with Id " + instanceId;
			ourLog.error(msg);

			throw new JobExecutionFailedException(Msg.code(2097) + msg);
		}
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
