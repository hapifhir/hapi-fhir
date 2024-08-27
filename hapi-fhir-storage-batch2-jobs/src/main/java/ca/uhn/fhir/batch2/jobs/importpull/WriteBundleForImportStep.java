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
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteBundleForImportStep
		implements ILastJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportRecord> {

	private static final Logger ourLog = LoggerFactory.getLogger(WriteBundleForImportStep.class);

	private final FhirContext myFhirContext;

	private final DaoRegistry myDaoRegistry;

	public WriteBundleForImportStep(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "rawtypes", "unchecked"})
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<Batch2BulkImportPullJobParameters, BulkImportRecord> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		BulkImportRecord record = theStepExecutionDetails.getData();

		JobFileRowProcessingModeEnum mode = record.getProcessingMode();
		int fileIndex = record.getFileIndex();
		String content = record.getResourceString();
		String tenantName = record.getTenantName();
		int lineIndex = record.getLineIndex();
		String jobId = theStepExecutionDetails.getParameters().getJobId();

		ourLog.info("Beginning bulk import write row {} for Job[{}] FileIndex[{}]", lineIndex, jobId, fileIndex);

		IParser parser = myFhirContext.newJsonParser();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(tenantName);

		IBaseResource bundle = parser.parseResource(content);

		// Yeah this is a lame switch - We'll add more later I swear
		switch (mode) {
			default:
			case FHIR_TRANSACTION:
				IFhirSystemDao systemDao = myDaoRegistry.getSystemDao();
				systemDao.transaction(requestDetails, bundle);
				break;
		}

		ourLog.info("Completed bulk import write for row {} Job[{}] FileIndex[{}]", lineIndex, jobId, fileIndex);
		return RunOutcome.SUCCESS;
	}
}
