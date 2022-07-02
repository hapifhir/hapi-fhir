package ca.uhn.fhir.batch2.progress;

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

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import static ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor.updateInstanceStatus;

public class JobInstanceProgressCalculator {
	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceProgressCalculator.class);
	private final IJobPersistence myJobPersistence;
	private final JobInstance myInstance;
	private final JobChunkProgressAccumulator myProgressAccumulator;

	public JobInstanceProgressCalculator(IJobPersistence theJobPersistence, JobInstance theInstance, JobChunkProgressAccumulator theProgressAccumulator) {
		myJobPersistence = theJobPersistence;
		myInstance = theInstance;
		myProgressAccumulator = theProgressAccumulator;
	}

	public void calculateAndStoreInstanceProgress() {
		InstanceProgress instanceProgress = new InstanceProgress();

		Iterator<WorkChunk> workChunkIterator = myJobPersistence.fetchAllWorkChunksIterator(myInstance.getInstanceId(), false);

		while (workChunkIterator.hasNext()) {
			WorkChunk next = workChunkIterator.next();
			myProgressAccumulator.addChunk(next);
			instanceProgress.addChunk(next);
		}

		instanceProgress.updateInstance(myInstance);

		if (instanceProgress.failed()) {
			updateInstanceStatus(myInstance, StatusEnum.FAILED);
			myJobPersistence.updateInstance(myInstance);
			return;
		}

		if (instanceProgress.changed()) {
			boolean statusChanged = myJobPersistence.updateInstance(myInstance);
			if (statusChanged && myInstance.getStatus() == StatusEnum.COMPLETED) {
				ourLog.info("Status changed to COMPLETED.  Invoking completion handler.");
				invokeCompletionHandler();
			}
		}
	}

	private  <PT extends IModelJson> void invokeCompletionHandler() {
		JobDefinition<PT> definition = (JobDefinition<PT>) myInstance.getJobDefinition();
		IJobCompletionHandler<PT> completionHandler = definition.getCompletionHandler();
		if (completionHandler != null) {
			String instanceId = myInstance.getInstanceId();
			PT jobParameters = myInstance.getParameters(definition.getParametersType());
			JobCompletionDetails<PT> completionDetails = new JobCompletionDetails<>(jobParameters, instanceId);
			completionHandler.jobComplete(completionDetails);
		}
	}
}
