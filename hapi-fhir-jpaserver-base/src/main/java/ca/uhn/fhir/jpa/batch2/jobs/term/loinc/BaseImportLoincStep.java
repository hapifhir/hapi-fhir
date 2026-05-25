package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newLoincCsvParser;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_CODE_SYSTEM_URL;

public abstract class BaseImportLoincStep<CT> extends BaseImportTerminologyStep
		implements ITerminologyImportFileHandlerStep<
				ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStep.class);

	@Autowired
	private IJobPersistence myJobPersistence;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
			StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
			ImportLoincJobParameters theJobParameters,
			String theFileName) {

		Properties jobProperties = getJobProperties(theStepExecutionDetails);

		for (LoincFileNameSpecification loincFileNameSpecification : getFilesToProcess(theStepExecutionDetails)) {
			if (loincFileNameSpecification.matchFileName(jobProperties, theFileName)) {
				return Optional.of(new FileHandlingInstructions(loincFileNameSpecification.fileHandlingType()));
			}
		}

		return Optional.empty();
	}

	protected Properties getJobProperties(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();
		Properties retVal = jobParameters.getJobProperties();
		if (retVal == null) {
			String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
			retVal = new Properties();
			try {
				String filename = LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode();
				AttachmentDetails attachment = myJobPersistence.fetchAttachmentByFilename(instanceId, filename);
				retVal.load(attachment.getInputStream());
			} catch (ResourceNotFoundException | IOException e) {
				// no properties file was provided
			}

			jobParameters.setJobProperties(retVal);
		}
		return retVal;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);

		ImportTerminologyMetadataAttachmentJson jobMetadata = getJobMetadata(jobInstanceId);

		CodeSystem codeSystemToPopulate = new CodeSystem();
		codeSystemToPopulate.setUrl(jobMetadata.getCodeSystem().getUrl());
		codeSystemToPopulate.setVersion(jobMetadata.getCodeSystemStagingVersionId());

		String attachmentId = null;
		String sourceFilename = null;
		if (data.getChunkForCurrentStep() != null) {
			attachmentId = data.getChunkForCurrentStep().getAttachmentId();
			sourceFilename = data.getChunkForCurrentStep().getSourceFilename();
		}
		if (isNotBlank(attachmentId)) {

			AttachmentDetails attachment = myJobPersistence.fetchAttachmentById(jobInstanceId, attachmentId);
			try (InputStream inputStream = attachment.getInputStream()) {
				InputStreamReader reader = new InputStreamReader(
						BOMInputStream.builder().setInputStream(inputStream).get(), StandardCharsets.UTF_8);
				CSVParser csvReader = newLoincCsvParser(reader);
				for (CSVRecord record : csvReader.getRecords()) {
					handleRecord(
							theStepExecutionDetails,
							jobMetadata,
							jobParameters,
							codeExtractionContext,
							record,
							codeSystemToPopulate,
							data,
							sourceFilename);
				}

			} catch (IOException e) {
				throw new JobExecutionFailedException(
						Msg.code(2941) + "Failed to read file attachment: " + e.getMessage(), e);
			}

			syncToDb(jobMetadata, codeExtractionContext, codeSystemToPopulate, theStepExecutionDetails);
		}

		if (!data.getStepIdToRecordsAdded().isEmpty()
				|| !data.getResourcesToActivate().isEmpty()) {
			TerminologyFileSetJson counterWorkChunk = new TerminologyFileSetJson();
			counterWorkChunk.getStepIdToRecordsAdded().putAll(data.getStepIdToRecordsAdded());
			counterWorkChunk.getResourcesToActivate().addAll(data.getResourcesToActivate());
			theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, counterWorkChunk);
		}

		return RunOutcome.SUCCESS;
	}

	private void syncConceptsToDb(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			CodeSystem codeSystemToPopulate) {
		if (codeSystemToPopulate.hasConcept()) {

			int conceptCount = codeSystemToPopulate.getConcept().size();
			ourLog.atInfo()
					.setMessage("Storing {} concepts")
					.addArgument(conceptCount)
					.log();

			Callable<UploadStatistics> uploader =
					() -> myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulate);
			UploadStatistics uploadStatistics = executeInNewTransactionWithRetry(uploader, theStepExecutionDetails);

			TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter =
					getRecordsAddedCounter(theStepExecutionDetails);
			recordsAddedCounter.incrementConceptsAdded(uploadStatistics.getAddedConceptCount());
			recordsAddedCounter.incrementConceptLinksAdded(uploadStatistics.getAddedConceptLinkCount());
			recordsAddedCounter.incrementPropertiesAdded(uploadStatistics.getAddedPropertyCount());
			recordsAddedCounter.incrementDesignationsAdded(uploadStatistics.getAddedDesignationCount());
		}
	}

	protected <T> T executeInNewTransactionWithRetry(
			Callable<T> theFunction,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		int retryCount = 0;
		while (true) {
			try {
				return myTransactionService
						.withSystemRequestOnDefaultPartition()
						.execute(theFunction);
			} catch (ResourceVersionConflictException e) {
				// FIXME: add test
				retryCount++;
				int maxRetries = 10;
				if (retryCount > maxRetries) {
					throw e;
				}
				ourLog.atWarn()
						.setMessage("Failed to save terminology for step {}, retry {}/{} in 5 seconds: {}")
						.addArgument(theStepExecutionDetails.getCurrentStepId())
						.addArgument(retryCount)
						.addArgument(maxRetries)
						.addArgument(e.getMessage())
						.log();
				sleepAtLeast(5 * DateUtils.MILLIS_PER_SECOND);
			}
		}
	}

	/**
	 * Invoked after all CSV rows have been processed but before the CodeSystem is submitted for storage.
	 * Subclasses may override, but they should call this super-method too.
	 */
	protected void syncToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {

		syncConceptsToDb(theStepExecutionDetails, theCodeSystemToPopulate);
	}

	// FIXME: this gets called repeatedly, we'd better cache it somewhere
	public Map<String, CodeSystem.PropertyType> getPropertyNameToType(
			ImportTerminologyMetadataAttachmentJson theJobMetadata) {
		Map<String, CodeSystem.PropertyType> propertyNameToType = new HashMap<>();
		for (CodeSystem.PropertyComponent nextProperty :
				theJobMetadata.getCodeSystem().getProperty()) {
			String nextPropertyCode = nextProperty.getCode();
			CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
			if (isNotBlank(nextPropertyCode)) {
				propertyNameToType.put(nextPropertyCode, nextPropertyType);
			}
		}
		return propertyNameToType;
	}

	protected abstract CT newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails);

	@Nonnull
	protected abstract List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails);

	protected abstract void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			CT theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename);

	protected TerminologyFileSetJson.RecordsAddedCounter getRecordsAddedCounter(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {

		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		String currentStepId = theStepExecutionDetails.getCurrentStepId();
		return data.getRecordsAddedCounter(currentStepId);
	}

	@Nullable
	IValidationSupport.LookupCodeResult lookupPreExistingConcept(
			ImportTerminologyMetadataAttachmentJson theJobMetadata, String propertyCodeValue) {
		String version = theJobMetadata.getCodeSystemStagingVersionId();
		LookupCodeRequest request =
				new LookupCodeRequest(LOINC_GENERIC_CODE_SYSTEM_URL + "|" + version, propertyCodeValue);
		IValidationSupport.LookupCodeResult lookupResponse =
				myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		return lookupResponse;
	}

	protected record LoincFileNameSpecification(
			FileHandlingType fileHandlingType,
			LoincUploadPropertiesEnum propertyName,
			List<LoincUploadPropertiesEnum> defaultValues,
			Predicate<String> fileNameTester) {

		protected LoincFileNameSpecification(
				FileHandlingType theFileHandlingType,
				LoincUploadPropertiesEnum thePropertyName,
				LoincUploadPropertiesEnum... theDefaultValue) {
			this(theFileHandlingType, thePropertyName, Arrays.asList(theDefaultValue), null);
		}

		protected LoincFileNameSpecification(
				FileHandlingType theFileHandlingType, Predicate<String> theFileNameTester) {
			this(theFileHandlingType, null, null, theFileNameTester);
		}

		public boolean matchFileName(Properties theJobProperties, String theFileName) {
			boolean matches = false;
			if (propertyName() != null) {
				String propertyName = propertyName().getCode();
				String fileName = theJobProperties.getProperty(propertyName, null);
				if (isNotBlank(fileName)) {
					matches = theFileName.endsWith(fileName);
				} else {
					for (LoincUploadPropertiesEnum nextDefault : defaultValues()) {
						matches |= theFileName.endsWith(nextDefault.getCode());
					}
				}
			} else if (this.fileNameTester() != null) {
				matches = this.fileNameTester().test(theFileName);
			}
			return matches;
		}
	}
}
