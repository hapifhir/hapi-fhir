package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
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

import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class BinaryCreator implements Consumer<ConvertedFile> {
	private static final Logger ourLog = getLogger(BinaryCreator.class);

	private final StepExecutionDetails<BulkExportJobParameters, ResourceIdList> myStepExecutionDetails;
	private final IJobDataSink<BulkExportBinaryFileId> myDataSink;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;

	public BinaryCreator(
			FhirContext theContext,
			DaoRegistry theDaoRegistry,
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			IJobDataSink<BulkExportBinaryFileId> theDataSink) {
		myFhirContext = theContext;
		myDaoRegistry = theDaoRegistry;
		this.myStepExecutionDetails = theStepExecutionDetails;
		this.myDataSink = theDataSink;
	}

	@Override
	public void accept(ConvertedFile theConvertedFile) throws JobExecutionFailedException {
		//			int batchSize = theExpandedResourcesList.getStringifiedResources().size();
		//			ourLog.info("Writing {} resources to binary file", batchSize);

		//			myNumResourcesProcessed += batchSize;

		IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

		IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);

		addMetadataExtensionsToBinary(myStepExecutionDetails, theConvertedFile, binary);
		binary.setContent(theConvertedFile.getBytes());

		//		int processedRecordsCount = 0;
		//		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		////			try (OutputStreamWriter streamWriter = getStreamWriter(outputStream)) {
		//////				for (String stringified : theConvertedFile.getStringifiedResources()) {
		//////					streamWriter.append(stringified);
		//////					streamWriter.append("\n");
		//////					processedRecordsCount++;
		//////				}
		////				outputStream.append(theConvertedFile.getBytes());
		////				streamWriter.flush();
		////				outputStream.flush();
		////			}
		//			binary.setContent(theConvertedFile.getBytes());
		//		} catch (IOException ex) {
		//			String errorMsg = String.format(
		//				"Failure to process resource of type %s : %s",
		//				theExpandedResourcesList.getResourceType(), ex.getMessage());
		//			ourLog.error(errorMsg);
		//
		//			throw new JobExecutionFailedException(Msg.code(2431) + errorMsg);
		//		}

		BulkExportJobParameters jobParameters = myStepExecutionDetails.getParameters();

		// TODO -
		binary.setContentType(theConvertedFile.getMimeType());

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
				RequestDetails requestDetails = myStepExecutionDetails.newSystemRequestDetails();
				IBaseBinary output = binaryDao.read(binary.getIdElement(), requestDetails, true);
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

		ourLog.info(
				"Writing Bulk Export Binary resource with ID: Binary/{}",
				binary.getIdElement().getIdPart());

		RequestDetails srd = newRequestDetails(myStepExecutionDetails, jobParameters);
		DaoMethodOutcome outcome = binaryDao.update(binary, srd);
		IIdType id = outcome.getId();

		BulkExportBinaryFileId bulkExportBinaryFileId = new BulkExportBinaryFileId();
		bulkExportBinaryFileId.setBinaryId(id.getValueAsString());
		bulkExportBinaryFileId.setResourceType(theConvertedFile.getResourceType());
		myDataSink.accept(bulkExportBinaryFileId);

		ourLog.info("Binary writing complete for resources of type {}.", theConvertedFile.getResourceType());
	}

	/**
	 * Adds 3 extensions to the `binary.meta` element.
	 * <p>
	 * 1. the _exportId provided at request time
	 * 2. the job_id of the job instance.
	 * 3. the resource type of the resources contained in the binary
	 */
	private void addMetadataExtensionsToBinary(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			ConvertedFile theFile,
			IBaseBinary binary) {
		// Note that this applies only to hl7.org structures, so these extensions will not be added
		// to DSTU2 structures
		if (binary.getMeta() instanceof IBaseHasExtensions meta) {
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
			typeExtension.setValue(myFhirContext.newPrimitiveString(theFile.getResourceType()));
		} else {
			ourLog.warn(
					"Could not attach metadata extensions to binary resource, as this binary metadata does not support extensions");
		}
	}

	private RequestDetails newRequestDetails(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			BulkExportJobParameters jobParameters) {
		return theStepExecutionDetails.newSystemRequestDetails();
	}
}
