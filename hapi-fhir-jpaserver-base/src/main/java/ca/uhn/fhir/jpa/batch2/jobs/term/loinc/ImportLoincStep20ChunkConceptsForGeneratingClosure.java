package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

public class ImportLoincStep20ChunkConceptsForGeneratingClosure extends BaseImportTerminologyStep implements IJobStepWorker<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> {

	@Autowired
	private ITermConceptDao myConceptDao;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink) throws JobExecutionFailedException {
		TerminologyFileSetJson data = theStepExecutionDetails.getData();

		ImportTerminologyMetadataAttachmentJson jobMetadata = getJobMetadata(theStepExecutionDetails.getInstance().getInstanceId());
		String stagingVersion = jobMetadata.getCodeSystemStagingVersionId();

		TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(jobMetadata.getLoincCodeSystem().getUrl(), stagingVersion);
		Validate.notNull(csv, "Could not find code system version %s for code system: %s", stagingVersion, jobMetadata.getLoincCodeSystem().getUrl());

		Stream<TermConcept.TermConceptPk> pids = myConceptDao.findPidsByCodeSystemVersion(csv);
		int pidCount = 0;
		TerminologyFileSetJson outputChunk = new TerminologyFileSetJson();
		for (Iterator<TermConcept.TermConceptPk> iter = pids.iterator(); iter.hasNext(); ) {
			outputChunk.getConceptPidsToGenerateClosureFor().add(iter.next().getId());
			pidCount++;

			// We use a chuink size of 500 so that we don't run out of memory
			// or exceed the maximum number of SQL parameters. If we decide to
			// increase this, make sure we don't exceed the maximum number of
			// SQL parameters.
			if (outputChunk.getConceptPidsToGenerateClosureFor().size() >= 500 || !iter.hasNext()) {
				theDataSink.accept(outputChunk);
				outputChunk = new TerminologyFileSetJson();
			}
		}

		// Emit statistics
		outputChunk = new TerminologyFileSetJson();
		outputChunk.getRecordsAddedCounter(theStepExecutionDetails.getCurrentStepId()).incrementOtherChanges(pidCount);
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, outputChunk);

		return RunOutcome.SUCCESS;
	}
}
