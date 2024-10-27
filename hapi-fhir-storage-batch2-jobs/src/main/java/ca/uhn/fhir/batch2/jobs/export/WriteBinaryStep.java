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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class WriteBinaryStep
		implements IJobStepWorker<BulkExportJobParameters, ExpandedResourcesList, BulkExportBinaryFileId> {
	private static final Logger ourLog = getLogger(WriteBinaryStep.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ExpandedResourcesList> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportBinaryFileId> theDataSink)
			throws JobExecutionFailedException {

		ExpandedResourcesList expandedResources = theStepExecutionDetails.getData();
		final int numResourcesProcessed =
				expandedResources.getStringifiedResources().size();

		ourLog.info("Write binary step of Job Export");
		ourLog.info("Writing {} resources to binary file", numResourcesProcessed);

		@SuppressWarnings("unchecked")
		IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);

		addMetadataExtensionsToBinary(theStepExecutionDetails, expandedResources, binary);

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
			String errorMsg = String.format(
					"Failure to process resource of type %s : %s",
					expandedResources.getResourceType(), ex.getMessage());
			ourLog.error(errorMsg);

			throw new JobExecutionFailedException(Msg.code(2238) + errorMsg);
		}

		SystemRequestDetails srd = new SystemRequestDetails();
		BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();
		RequestPartitionId partitionId = jobParameters.getPartitionId();
		if (partitionId == null) {
			srd.setRequestPartitionId(RequestPartitionId.defaultPartition());
		} else {
			srd.setRequestPartitionId(partitionId);
		}

		// Pick a unique ID and retry until we get one that isn't already used. This is just to
		// avoid any possibility of people guessing the IDs of these Binaries and fishing for them.
		while (true) {
			// Use a random ID to make it harder to guess IDs - 32 characters of a-zA-Z0-9
			// has 190 bts of entropy according to https://www.omnicalculator.com/other/password-entropy
			String proposedId = RandomTextUtils.newSecureRandomAlphaNumericString(32);
			binary.setId(proposedId);

			// Make sure we don't accidentally reuse an ID. This should be impossible given the
			// amount of entropy in the IDs but might as well be sure.
			try {
				IBaseBinary output = binaryDao.read(binary.getIdElement(), new SystemRequestDetails(), true);
				if (output != null) {
					continue;
				}
			} catch (ResourceNotFoundException e) {
				// good
			}

			break;
		}

		if (myFhirContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
			if (isNotBlank(jobParameters.getBinarySecurityContextIdentifierSystem())
					|| isNotBlank(jobParameters.getBinarySecurityContextIdentifierValue())) {
				FhirTerser terser = myFhirContext.newTerser();
				terser.setElement(
						binary,
						"securityContext.identifier.system",
						jobParameters.getBinarySecurityContextIdentifierSystem());
				terser.setElement(
						binary,
						"securityContext.identifier.value",
						jobParameters.getBinarySecurityContextIdentifierValue());
			}
		}

		DaoMethodOutcome outcome = binaryDao.update(binary, srd);
		IIdType id = outcome.getId();

		BulkExportBinaryFileId bulkExportBinaryFileId = new BulkExportBinaryFileId();
		bulkExportBinaryFileId.setBinaryId(id.getValueAsString());
		bulkExportBinaryFileId.setResourceType(expandedResources.getResourceType());
		theDataSink.accept(bulkExportBinaryFileId);

		ourLog.info(
				"Binary writing complete for {} resources of type {}.",
				processedRecordsCount,
				expandedResources.getResourceType());

		return new RunOutcome(numResourcesProcessed);
	}

	/**
	 * Adds 3 extensions to the `binary.meta` element.
	 *
	 * 1. the _exportId provided at request time
	 * 2. the job_id of the job instance.
	 * 3. the resource type of the resources contained in the binary
	 */
	private void addMetadataExtensionsToBinary(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ExpandedResourcesList> theStepExecutionDetails,
			ExpandedResourcesList expandedResources,
			IBaseBinary binary) {
		// Note that this applies only to hl7.org structures, so these extensions will not be added
		// to DSTU2 structures
		if (binary.getMeta() instanceof IBaseHasExtensions) {
			IBaseHasExtensions meta = (IBaseHasExtensions) binary.getMeta();

			// export identifier, potentially null.
			String exportIdentifier = theStepExecutionDetails.getParameters().getExportIdentifier();
			if (!StringUtils.isBlank(exportIdentifier)) {
				IBaseExtension<?, ?> exportIdentifierExtension = meta.addExtension();
				exportIdentifierExtension.setUrl(JpaConstants.BULK_META_EXTENSION_EXPORT_IDENTIFIER);
				exportIdentifierExtension.setValue(myFhirContext.newPrimitiveString(exportIdentifier));
			}

			// job id
			IBaseExtension<?, ?> jobExtension = meta.addExtension();
			jobExtension.setUrl(JpaConstants.BULK_META_EXTENSION_JOB_ID);
			jobExtension.setValue(myFhirContext.newPrimitiveString(
					theStepExecutionDetails.getInstance().getInstanceId()));

			// resource type
			IBaseExtension<?, ?> typeExtension = meta.addExtension();
			typeExtension.setUrl(JpaConstants.BULK_META_EXTENSION_RESOURCE_TYPE);
			typeExtension.setValue(myFhirContext.newPrimitiveString(expandedResources.getResourceType()));
		} else {
			ourLog.warn(
					"Could not attach metadata extensions to binary resource, as this binary metadata does not support extensions");
		}
	}

	/**
	 * Returns an output stream writer
	 * (exposed for testing)
	 */
	protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
		return new OutputStreamWriter(theOutputStream, Constants.CHARSET_UTF8);
	}
}
