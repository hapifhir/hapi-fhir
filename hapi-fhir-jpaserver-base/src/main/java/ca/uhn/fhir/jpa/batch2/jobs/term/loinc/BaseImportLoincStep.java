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
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newLoincCsvParser;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseImportLoincStep<CT>
		implements ITerminologyImportFileHandlerStep<
	ImportLoincJobParameters, ImportLoincFileSetJson, ImportLoincFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStep.class);

	@Autowired
	private IJobPersistence myJobPersistence;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
		ImportLoincJobParameters theJobParameters, String theFileName) {

		Properties jobProperties = getJobProperties(theStepExecutionDetails);

		for (LoincFileNameSpecification loincFileNameSpecification : getFilesToProcess()) {
			boolean matches = false;

			if (loincFileNameSpecification.propertyName() != null) {
				String propertyName = loincFileNameSpecification.propertyName().getCode();
				String defaultFileName = loincFileNameSpecification.defaultValue().getCode();
				String fileName = jobProperties.getProperty(propertyName, defaultFileName);
				matches = theFileName.endsWith(fileName);
			} else if (loincFileNameSpecification.fileNamePattern() != null) {
				matches = loincFileNameSpecification.fileNamePattern().matcher(theFileName).matches();
			}

			if (matches) {
				return Optional.of(
						new FileHandlingInstructions(theFileName, FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER));
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
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ImportLoincFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		ImportLoincFileSetJson data = theStepExecutionDetails.getData();
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);
		CodeSystem codeSystemToPopulate = new CodeSystem();

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

			afterCsvProcessingComplete(codeExtractionContext, codeSystemToPopulate, theStepExecutionDetails);

			/*
			 * Store Concepts
			 */
			if (codeSystemToPopulate.hasConcept()) {

				int conceptCount = codeSystemToPopulate.getConcept().size();
				ourLog.atInfo()
						.setMessage("Added LOINC Answer List links to {} concepts")
						.addArgument(conceptCount)
						.log();

				UploadStatistics uploadStatistics = myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulate);

				TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter = getRecordsAddedCounter(theStepExecutionDetails);
				recordsAddedCounter.incrementConceptsAdded(uploadStatistics.getAddedConceptCount());
				recordsAddedCounter.incrementConceptLinksAdded(uploadStatistics.getAddedConceptLinkCount());
				recordsAddedCounter.incrementPropertiesAdded(uploadStatistics.getAddedPropertyCount());
				recordsAddedCounter.incrementDesignationsAdded(uploadStatistics.getAddedDesignationCount());

			}
		}

		BaseExpandDistributionIntoFilesStep.submitChunksForNextStep(theStepExecutionDetails, theDataSink, data);

		return RunOutcome.SUCCESS;
	}

	/**
	 * Invoked after all CSV rows have been processed but before the CodeSystem is submitted for storage
	 */
	protected void afterCsvProcessingComplete(
			CT theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		// nothing
	}

	protected abstract CT newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails);

	@Nonnull
	protected abstract List<LoincFileNameSpecification> getFilesToProcess();

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
			LoincUploadPropertiesEnum propertyName, LoincUploadPropertiesEnum defaultValue, Pattern fileNamePattern) {

		protected LoincFileNameSpecification(LoincUploadPropertiesEnum propertyName, LoincUploadPropertiesEnum defaultValue) {
			this(propertyName, defaultValue, null);
		}

		protected LoincFileNameSpecification(Pattern fileNamePattern) {
			this(null, null, fileNamePattern);
		}
	}
}
