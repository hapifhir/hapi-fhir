package ca.uhn.fhir.batch2.impl;

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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;

import java.util.concurrent.atomic.AtomicInteger;

class JobDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> extends BaseDataSink<PT,IT,OT> {
	private final BatchJobSender myBatchJobSender;
	private final IJobPersistence myJobPersistence;
	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final JobDefinitionStep<PT, OT, ?> myTargetStep;
	private final AtomicInteger myChunkCounter = new AtomicInteger(0);
	private final boolean myGatedExecution;

	JobDataSink(BatchJobSender theBatchJobSender, IJobPersistence theJobPersistence, JobDefinition<?> theDefinition, String theInstanceId, JobWorkCursor<PT, IT, OT> theJobWorkCursor) {
		super(theInstanceId, theJobWorkCursor);
		myBatchJobSender = theBatchJobSender;
		myJobPersistence = theJobPersistence;
		myJobDefinitionId = theDefinition.getJobDefinitionId();
		myJobDefinitionVersion = theDefinition.getJobDefinitionVersion();
		myTargetStep = theJobWorkCursor.nextStep;
		myGatedExecution = theDefinition.isGatedExecution();
	}

	@Override
	public void accept(WorkChunkData<OT> theData) {
		String instanceId = getInstanceId();
		String targetStepId = myTargetStep.getStepId();
		int sequence = myChunkCounter.getAndIncrement();
		OT dataValue = theData.getData();
		String dataValueString = JsonUtil.serialize(dataValue, false);

		BatchWorkChunk batchWorkChunk = new BatchWorkChunk(myJobDefinitionId, myJobDefinitionVersion, targetStepId, instanceId, sequence, dataValueString);
		String chunkId = myJobPersistence.storeWorkChunk(batchWorkChunk);

		if (!myGatedExecution) {
			JobWorkNotification workNotification = new JobWorkNotification(myJobDefinitionId, myJobDefinitionVersion, instanceId, targetStepId, chunkId);
			myBatchJobSender.sendWorkChannelMessage(workNotification);
		}
	}

	@Override
	public int getWorkChunkCount() {
		return myChunkCounter.get();
	}

}
