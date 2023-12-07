/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeleteExpungeStep
		implements IJobStepWorker<DeleteExpungeJobParameters, ResourceIdListWorkChunkJson, VoidModel> {

	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeStep.class);
	private final IDeleteExpungeSvc myDeleteExpungeSvc;
	private final IIdHelperService myIdHelperService;

	public DeleteExpungeStep(IDeleteExpungeSvc theDeleteExpungeSvc, IIdHelperService theIdHelperService) {
		myDeleteExpungeSvc = theDeleteExpungeSvc;
		myIdHelperService = theIdHelperService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<DeleteExpungeJobParameters, ResourceIdListWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		boolean cascade = theStepExecutionDetails.getParameters().isCascade();
		Integer cascadeMaxRounds = theStepExecutionDetails.getParameters().getCascadeMaxRounds();
		return doDeleteExpunge(
				data,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				cascade,
				cascadeMaxRounds);
	}

	@Nonnull
	public RunOutcome doDeleteExpunge(
			ResourceIdListWorkChunkJson theData,
			String theInstanceId,
			String theChunkId,
			boolean theCascade,
			Integer theCascadeMaxRounds) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(theData.getRequestPartitionId());

		DeleteExpungeJob job = new DeleteExpungeJob(
				theData, theInstanceId, theChunkId, theCascade, theCascadeMaxRounds, requestDetails);

		job.executeJob();

		return new RunOutcome(job.getRecordCount());
	}

	private class DeleteExpungeJob {
		private final ResourceIdListWorkChunkJson myData;
		private final String myChunkId;
		private final String myInstanceId;
		private final boolean myCascade;
		private final Integer myCascadeMaxRounds;
		private int myRecordCount;
		private final RequestDetails myRequestDetails;

		public DeleteExpungeJob(
				ResourceIdListWorkChunkJson theData,
				String theInstanceId,
				String theChunkId,
				boolean theCascade,
				Integer theCascadeMaxRounds,
				RequestDetails theRequestDetails) {
			myData = theData;
			myInstanceId = theInstanceId;
			myChunkId = theChunkId;
			myCascade = theCascade;
			myCascadeMaxRounds = theCascadeMaxRounds;
			myRequestDetails = theRequestDetails;
		}

		public int getRecordCount() {
			return myRecordCount;
		}

		public void executeJob() {

			List<JpaPid> persistentIds = myData.getResourcePersistentIds(myIdHelperService);

			if (persistentIds.isEmpty()) {
				ourLog.info(
						"Starting delete expunge work chunk.  There are no resources to delete expunge - Instance[{}] Chunk[{}]",
						myInstanceId,
						myChunkId);
			}

			ourLog.info(
					"Starting delete expunge work chunk with {} resources - Instance[{}] Chunk[{}]",
					persistentIds.size(),
					myInstanceId,
					myChunkId);

			// not creating a transaction here, as deleteExpungeBatch manages
			// its transactions internally to prevent deadlocks
			myRecordCount = myDeleteExpungeSvc.deleteExpungeBatch(
					persistentIds, myCascade, myCascadeMaxRounds, myRequestDetails);
		}
	}
}
