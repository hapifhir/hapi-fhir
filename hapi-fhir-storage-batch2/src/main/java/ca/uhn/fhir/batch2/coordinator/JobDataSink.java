package ca.uhn.fhir.batch2.coordinator;

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
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class JobDataSink<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> extends BaseDataSink<PT,IT,OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(JobDataSink.class);

	private final BatchJobSender myBatchJobSender;
	private final IJobPersistence myJobPersistence;
	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final JobDefinitionStep<PT, OT, ?> myTargetStep;
	private final AtomicInteger myChunkCounter = new AtomicInteger(0);
	private final AtomicReference<String> myLastChunkId = new AtomicReference<>();
	private final boolean myGatedExecution;

	JobDataSink(@Nonnull BatchJobSender theBatchJobSender,
					@Nonnull IJobPersistence theJobPersistence,
					@Nonnull JobDefinition<?> theDefinition,
					@Nonnull String theInstanceId,
					@Nonnull JobWorkCursor<PT, IT, OT> theJobWorkCursor) {
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
		myLastChunkId.set(chunkId);

		if (!myGatedExecution) {
			JobWorkNotification workNotification = new JobWorkNotification(myJobDefinitionId, myJobDefinitionVersion, instanceId, targetStepId, chunkId);
			myBatchJobSender.sendWorkChannelMessage(workNotification);
		}
	}

	@Override
	public int getWorkChunkCount() {
		return myChunkCounter.get();
	}

	public String getOnlyChunkId() {
		if (getWorkChunkCount() != 1) {
			String msg = String.format("Expected this sink to have exactly one work chunk but there are %d.  Job %s v%s step %s", getWorkChunkCount(), myJobDefinitionId, myJobDefinitionVersion, myTargetStep);
			throw new IllegalStateException(Msg.code(2082) + msg);
		}
		return myLastChunkId.get();
	}
}
