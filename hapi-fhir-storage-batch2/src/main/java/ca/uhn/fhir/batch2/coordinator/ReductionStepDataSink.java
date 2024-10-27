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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.batch2.progress.InstanceProgress;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.util.Date;

public class ReductionStepDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
		extends BaseDataSink<PT, IT, OT> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;

	public ReductionStepDataSink(
			String theInstanceId,
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
		OT data = theData.getData();
		String dataString = JsonUtil.serialize(data, false);
		JobChunkProgressAccumulator progressAccumulator = new JobChunkProgressAccumulator();
		JobInstanceProgressCalculator myJobInstanceProgressCalculator =
				new JobInstanceProgressCalculator(myJobPersistence, progressAccumulator, myJobDefinitionRegistry);

		InstanceProgress progress = myJobInstanceProgressCalculator.calculateInstanceProgress(instanceId);
		boolean changed = myJobPersistence.updateInstance(instanceId, instance -> {
			Validate.validState(
					StatusEnum.FINALIZE.equals(instance.getStatus()),
					"Job %s must be in FINALIZE state.  In %s",
					instanceId,
					instance.getStatus());

			if (instance.getReport() != null) {
				// last in wins - so we won't throw
				ourLog.error("Report has already been set. Now it is being overwritten. Last in will win!");
			}

			/*
			 * For jobs without a reduction step at the end, the maintenance service marks the job instance
			 * as COMPLETE when all chunks are complete, and calculates the final counts and progress.
			 * However, for jobs with a reduction step at the end the maintenance service stops working
			 * on the job while the job is in FINALIZE state, and this sink is ultimately responsible
			 * for marking the instance as COMPLETE at the end of the reduction.
			 *
			 * So, make sure we update the stats and counts before marking as complete here.
			 *
			 * I could envision a better setup where the stuff that the maintenance service touches
			 * is moved into separate DB tables or transactions away from the stuff that the
			 * reducer touches. If the two could never collide we wouldn't need this duplication
			 * here. Until then though, this is safer.
			 */

			progress.updateInstanceForReductionStep(instance);

			instance.setReport(dataString);
			instance.setStatus(StatusEnum.COMPLETED);
			instance.setEndTime(new Date());

			ourLog.info(
					"Finalizing job instance {} with report length {} chars",
					instance.getInstanceId(),
					dataString.length());
			if (ourLog.isTraceEnabled()) {
				ourLog.trace("New instance state: {}", JsonUtil.serialize(instance));
			}

			return true;
		});

		if (!changed) {
			ourLog.error("No instance found with Id {} in FINALIZE state", instanceId);

			throw new JobExecutionFailedException(Msg.code(2097) + ("No instance found with Id " + instanceId));
		}
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
