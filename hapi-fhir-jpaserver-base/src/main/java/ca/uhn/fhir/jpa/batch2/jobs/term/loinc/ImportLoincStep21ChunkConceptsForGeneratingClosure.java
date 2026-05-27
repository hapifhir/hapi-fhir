package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

public class ImportLoincStep21ChunkConceptsForGeneratingClosure extends BaseImportTerminologyStep
		implements IJobStepWorker<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson>,
				ITerminologyImportFileHandlerStep<
						ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> {

	@Autowired
	private ITermConceptDao myConceptDao;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		myTxService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> generateClosures(theStepExecutionDetails, theDataSink));

		return RunOutcome.SUCCESS;
	}

	private void generateClosures(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink) {
		HapiTransactionService.requireTransaction();

		ImportTerminologyMetadataAttachmentJson jobMetadata =
				getJobMetadata(theStepExecutionDetails.getInstance().getInstanceId());
		String stagingVersion = jobMetadata.getCodeSystemStagingVersionId();

		TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(
				jobMetadata.getCodeSystem().getUrl(), stagingVersion);
		Validate.notNull(
				csv,
				"Could not find code system version %s for code system: %s",
				stagingVersion,
				jobMetadata.getCodeSystem().getUrl());

		Stream<TermConcept.TermConceptPk> pids = myConceptDao.findPidsByCodeSystemVersion(csv);
		int pidCount = 0;
		TerminologyFileSetJson outputChunk = new TerminologyFileSetJson();
		for (Iterator<TermConcept.TermConceptPk> iter = pids.iterator(); iter.hasNext(); ) {
			outputChunk.getConceptPidsToGenerateClosureFor().add(iter.next().getId());
			pidCount++;

			if (outputChunk.getConceptPidsToGenerateClosureFor().size() >= 2000 || !iter.hasNext()) {
				theDataSink.accept(outputChunk);
				outputChunk = new TerminologyFileSetJson();
			}
		}

		// Emit statistics
		outputChunk = new TerminologyFileSetJson();
		outputChunk
				.getRecordsAddedCounter(theStepExecutionDetails.getCurrentStepId())
				.incrementOtherChanges(pidCount);
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, outputChunk);
	}

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
			StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
			ImportLoincJobParameters theJobParameters,
			String theFileName) {
		// This step doesn't process any files
		return Optional.empty();
	}
}
