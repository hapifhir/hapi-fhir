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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

public class JobStepExecutorFactory {
	private final IJobPersistence myJobPersistence;
	private final BatchJobSender myBatchJobSender;
	private final StepExecutionSvc myJobStepExecutorSvc;

	public JobStepExecutorFactory(@Nonnull IJobPersistence theJobPersistence,
											@Nonnull BatchJobSender theBatchJobSender,
											@Nonnull StepExecutionSvc theExecutorSvc) {
		myJobPersistence = theJobPersistence;
		myBatchJobSender = theBatchJobSender;
		myJobStepExecutorSvc = theExecutorSvc;
	}

	public <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> JobStepExecutor<PT,IT,OT> newJobStepExecutor(@Nonnull JobInstance theInstance, @Nonnull WorkChunk theWorkChunk, @Nonnull JobWorkCursor<PT, IT, OT> theCursor) {
		return new JobStepExecutor<>(myJobPersistence, myBatchJobSender, theInstance, theWorkChunk, theCursor, myJobStepExecutorSvc);
	}
}
