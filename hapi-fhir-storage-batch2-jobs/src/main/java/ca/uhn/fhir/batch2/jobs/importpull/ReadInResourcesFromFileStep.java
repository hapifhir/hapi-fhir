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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.util.IoUtil;
import com.google.common.io.LineReader;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;

public class ReadInResourcesFromFileStep
		implements IJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult, BulkImportRecord> {

	private static final Logger ourLog = LoggerFactory.getLogger(ReadInResourcesFromFileStep.class);

	private final IBulkDataImportSvc myBulkDataImportSvc;

	public ReadInResourcesFromFileStep(IBulkDataImportSvc theBulkDataImportSvc) {
		myBulkDataImportSvc = theBulkDataImportSvc;
	}

	// because we are using an unstable Google api
	@SuppressWarnings("UnstableApiUsage")
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkImportRecord> theDataSink)
			throws JobExecutionFailedException {
		String jobId = theStepExecutionDetails.getParameters().getJobId();
		int fileIndex = theStepExecutionDetails.getData().getFileIndex();
		JobFileRowProcessingModeEnum mode = theStepExecutionDetails.getData().getProcessingMode();

		ourLog.info("ReadInResourcesFromFileStep for jobId {} begin", jobId);

		BulkImportJobFileJson file = myBulkDataImportSvc.fetchFile(jobId, fileIndex);
		String tenantName = file.getTenantName();
		String contents = file.getContents();

		StringReader reader = new StringReader(contents);

		// data explodes into even more chunks
		LineReader lineReader = new LineReader(reader);
		try {
			int lineIndex = 0;
			String nextLine;
			do {
				nextLine = lineReader.readLine();

				if (nextLine != null) {
					BulkImportRecord record = new BulkImportRecord();
					record.setResourceString(nextLine);
					record.setLineIndex(lineIndex);
					record.setTenantName(tenantName);
					record.setProcessingMode(mode);
					record.setFileIndex(fileIndex);

					theDataSink.accept(record);
				}
				lineIndex++;
			} while (nextLine != null);
		} catch (IOException ex) {
			ourLog.error("Failed to read file : " + ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2107) + " : Could not read file");
		} finally {
			IoUtil.closeQuietly(reader);
		}

		ourLog.info("ReadInResourcesFromFileStep for jobId {} end", jobId);

		return RunOutcome.SUCCESS;
	}
}
