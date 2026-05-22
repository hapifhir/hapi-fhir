package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nonnull;
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
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newLoincCsvParser;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseImportLoincStep<CT>
	implements ITerminologyImportFileHandlerStep<
	ImportLoincJobParameters, ImportLoincFileSetJson, ImportLoincFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStep.class);

	@Autowired
	private IJobPersistence myJobPersistence;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
		ImportLoincJobParameters theJobParameters, String theFileName) {

		Properties jobProperties = getJobProperties(theStepExecutionDetails);

		for (LoincFileNameSpecification loincFileNameSpecification : getFilesToProcess(theStepExecutionDetails)) {
			if (loincFileNameSpecification.matchFileName(jobProperties, theFileName)) {
				return Optional.of(
					new FileHandlingInstructions(theFileName, getFileHandlingType()));
			}
		}

		return Optional.empty();
	}

	@Nonnull
	@Override
	public FileHandlingType getFileHandlingType() {
		return FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS;
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
		@Nonnull StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails,
		@Nonnull IJobDataSink<ImportLoincFileSetJson> theDataSink)
		throws JobExecutionFailedException {
		ImportLoincFileSetJson data = theStepExecutionDetails.getData();
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);

		CodeSystem codeSystemToPopulate = new CodeSystem();
		codeSystemToPopulate.setUrl(data.getLoincCodeSystem().getUrl());
		codeSystemToPopulate.setVersion(data.getCodeSystemStagingVersionId());

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
					handleRecord(theStepExecutionDetails, jobParameters, codeExtractionContext, record, codeSystemToPopulate, data, sourceFilename);
				}

			} catch (IOException e) {
				// FIXME: add code
				throw new JobExecutionFailedException(
					Msg.code(1) + "Failed to read file attachment: " + e.getMessage(), e);
			}

			syncToDb(codeExtractionContext, codeSystemToPopulate, theStepExecutionDetails);

		}

		BaseExpandDistributionIntoFilesStep.submitChunksForNextStep(theStepExecutionDetails, theDataSink, data);

		return RunOutcome.SUCCESS;
	}

	private void syncConceptsToDb(@Nonnull StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, CodeSystem codeSystemToPopulate) {
		if (codeSystemToPopulate.hasConcept()) {

			int conceptCount = codeSystemToPopulate.getConcept().size();
			ourLog.atInfo()
				.setMessage("Storing {} concepts")
				.addArgument(conceptCount)
				.log();

			Callable<UploadStatistics> uploader = () -> myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulate);
			UploadStatistics uploadStatistics = executeInNewTransactionWithRetry(uploader, theStepExecutionDetails);

			TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter = getRecordsAddedCounter(theStepExecutionDetails);
			recordsAddedCounter.incrementConceptsAdded(uploadStatistics.getAddedConceptCount());
			recordsAddedCounter.incrementConceptLinksAdded(uploadStatistics.getAddedConceptLinkCount());
			recordsAddedCounter.incrementPropertiesAdded(uploadStatistics.getAddedPropertyCount());
			recordsAddedCounter.incrementDesignationsAdded(uploadStatistics.getAddedDesignationCount());

		}
	}

	protected <T> T executeInNewTransactionWithRetry(Callable<T> theFunction, StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		int retryCount = 0;
		while(true) {
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
		CT theCodeExtractionContext,
		CodeSystem theCodeSystemToPopulate,
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {

		syncConceptsToDb(theStepExecutionDetails, theCodeSystemToPopulate);

	}

	protected abstract CT newContextObject(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails);

	@Nonnull
	protected abstract List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails);

	protected abstract void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		CT theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename);

	protected TerminologyFileSetJson.RecordsAddedCounter getRecordsAddedCounter(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {

		ImportLoincFileSetJson data = theStepExecutionDetails.getData();
		String currentStepId = theStepExecutionDetails.getCurrentStepId();
		return data.getRecordsAddedCounter(currentStepId);
	}

	protected record LoincFileNameSpecification(
		LoincUploadPropertiesEnum propertyName, List<LoincUploadPropertiesEnum> defaultValues,
		Predicate<String> fileNameTester) {

		protected LoincFileNameSpecification(LoincUploadPropertiesEnum propertyName, LoincUploadPropertiesEnum... defaultValue) {
			this(propertyName, Arrays.asList(defaultValue), null);
		}

		protected LoincFileNameSpecification(Predicate<String> fileNameTester) {
			this(null, null, fileNameTester);
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
