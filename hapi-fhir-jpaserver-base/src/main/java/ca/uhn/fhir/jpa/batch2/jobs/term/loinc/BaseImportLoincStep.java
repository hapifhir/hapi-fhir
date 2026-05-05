package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newLoincCsvParser;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseImportLoincStep<CT> implements ITerminologyImportFileHandlerStep<LoincJobImportParameters, ImportLoincFileSetJson, ImportLoincFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStep.class);

	@Autowired
	private IJobPersistence myJobPersistence;
	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;


	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(LoincJobImportParameters theJobParameters, String theFileName) {
		String loincFileName = theJobParameters.getProperties().getProperty(provideFileNamePropertyFileKey().getCode(), provideFileNameDefault().getCode());
		if (theFileName.endsWith(loincFileName)) {
			return Optional.of(new FileHandlingInstructions(provideFileNamePropertyFileKey().getCode(), FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER));
		}
		return Optional.empty();
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<ImportLoincFileSetJson> theDataSink) throws JobExecutionFailedException {
		ImportLoincFileSetJson data = theStepExecutionDetails.getData();
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
		LoincJobImportParameters jobParameters = theStepExecutionDetails.getParameters();

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);
		CodeSystem codeSystemToPopulate = new CodeSystem();

		String attachmentId = data.getChunkAttachmentIdForCurrentStepId();
		if (isNotBlank(attachmentId)) {

			AttachmentDetails attachment = myJobPersistence.fetchAttachmentById(jobInstanceId, attachmentId);
			try (InputStream inputStream = attachment.getInputStream()) {
				InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
				CSVParser csvReader = newLoincCsvParser(reader);
				for (CSVRecord record : csvReader.getRecords()) {
					handleRecord(jobParameters, codeExtractionContext, record, codeSystemToPopulate, data);
				}

			} catch (IOException e) {
				// FIXME: add code
				throw new JobExecutionFailedException(Msg.code(1) + "Failed to read file attachment: " + e.getMessage(), e);
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

				myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulate);
			}

		}

		BaseExpandDistributionIntoFilesStep.submitChunksForNextStep(theStepExecutionDetails, theDataSink, data);

		return RunOutcome.SUCCESS;
	}

	/**
	 * Invoked after all CSV rows have been processed but before the CodeSystem is submitted for storage
	 */
	protected void afterCsvProcessingComplete(CT theCodeExtractionContext, CodeSystem theCodeSystemToPopulate, StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		// nothing
	}

	protected abstract CT newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails);


	@Nonnull
	protected abstract LoincUploadPropertiesEnum provideFileNameDefault();

	@Nonnull
	protected abstract LoincUploadPropertiesEnum provideFileNamePropertyFileKey();

	protected abstract void handleRecord(LoincJobImportParameters theJobParameters, CT theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData);
}
