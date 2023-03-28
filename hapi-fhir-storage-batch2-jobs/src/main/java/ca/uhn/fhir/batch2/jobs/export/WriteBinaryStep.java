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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BinaryUtil;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static org.slf4j.LoggerFactory.getLogger;

public class WriteBinaryStep implements IJobStepWorker<BulkExportJobParameters, ExpandedResourcesList, BulkExportBinaryFileId> {
	private static final Logger ourLog = getLogger(WriteBinaryStep.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, ExpandedResourcesList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportBinaryFileId> theDataSink) throws JobExecutionFailedException {

		ExpandedResourcesList expandedResources = theStepExecutionDetails.getData();
		final int numResourcesProcessed = expandedResources.getStringifiedResources().size();

		ourLog.info("Write binary step of Job Export");
		ourLog.info("Writing {} resources to binary file", numResourcesProcessed);

		@SuppressWarnings("unchecked")
		IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);

		// TODO
		// should be dependent on the
		// output format in parameters
		// but for now, only NDJSON is supported
		binary.setContentType(Constants.CT_FHIR_NDJSON);

		int processedRecordsCount = 0;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			try (OutputStreamWriter streamWriter = getStreamWriter(outputStream)) {
				for (String stringified : expandedResources.getStringifiedResources()) {
					streamWriter.append(stringified);
					streamWriter.append("\n");
					processedRecordsCount++;
				}
				streamWriter.flush();
				outputStream.flush();
			}
			binary.setContent(outputStream.toByteArray());
		} catch (IOException ex) {
			String errorMsg = String.format("Failure to process resource of type %s : %s",
				expandedResources.getResourceType(),
				ex.getMessage());
			ourLog.error(errorMsg);

			throw new JobExecutionFailedException(Msg.code(2238) + errorMsg);
		}

		SystemRequestDetails srd = new SystemRequestDetails();
		RequestPartitionId partitionId = theStepExecutionDetails.getParameters().getPartitionId();
		if (partitionId == null){
			srd.setRequestPartitionId(RequestPartitionId.defaultPartition());
		} else {
			srd.setRequestPartitionId(partitionId);
		}
		DaoMethodOutcome outcome = binaryDao.create(binary,srd);
		IIdType id = outcome.getId();

		BulkExportBinaryFileId bulkExportBinaryFileId = new BulkExportBinaryFileId();
		bulkExportBinaryFileId.setBinaryId(id.getValueAsString());
		bulkExportBinaryFileId.setResourceType(expandedResources.getResourceType());
		theDataSink.accept(bulkExportBinaryFileId);

		ourLog.info("Binary writing complete for {} resources of type {}.",
			processedRecordsCount,
			expandedResources.getResourceType());

		return new RunOutcome(numResourcesProcessed);
	}

	/**
	 * Returns an output stream writer
	 * (exposed for testing)
	 */
	protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
		return new OutputStreamWriter(theOutputStream, Constants.CHARSET_UTF8);
	}
}
