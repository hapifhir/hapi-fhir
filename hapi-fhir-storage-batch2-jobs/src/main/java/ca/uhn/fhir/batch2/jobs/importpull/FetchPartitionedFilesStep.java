/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class FetchPartitionedFilesStep
		implements IFirstJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult> {
	private static final Logger ourLog = getLogger(FetchPartitionedFilesStep.class);

	private final IBulkDataImportSvc myBulkDataImportSvc;

	public FetchPartitionedFilesStep(IBulkDataImportSvc theBulkDataImportSvc) {
		myBulkDataImportSvc = theBulkDataImportSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<Batch2BulkImportPullJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkImportFilePartitionResult> theDataSink)
			throws JobExecutionFailedException {
		String jobId = theStepExecutionDetails.getParameters().getJobId();

		ourLog.info("Start FetchPartitionedFilesStep for jobID {} ", jobId);

		BulkImportJobJson job = myBulkDataImportSvc.fetchJob(jobId);

		for (int i = 0; i < job.getFileCount(); i++) {
			String fileDescription = myBulkDataImportSvc.getFileDescription(jobId, i);

			BulkImportFilePartitionResult result = new BulkImportFilePartitionResult();
			result.setFileIndex(i);
			result.setProcessingMode(job.getProcessingMode());
			result.setFileDescription(fileDescription);
			result.setJobDescription(job.getJobDescription());

			theDataSink.accept(result);
		}

		ourLog.info(
				"FetchPartitionedFilesStep complete for jobID {}.  Submitted {} files to next step.",
				jobId,
				job.getFileCount());

		return RunOutcome.SUCCESS;
	}
}
