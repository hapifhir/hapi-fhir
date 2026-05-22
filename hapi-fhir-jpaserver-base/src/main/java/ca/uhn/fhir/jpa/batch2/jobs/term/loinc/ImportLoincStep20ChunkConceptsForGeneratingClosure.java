package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.stream.Stream;

public class ImportLoincStep20ChunkConceptsForGeneratingClosure implements IJobStepWorker<ImportLoincJobParameters, ImportLoincFileSetJson, ImportLoincFileSetJson> {

	@Autowired
	private ITermConceptDao myConceptDao;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<ImportLoincFileSetJson> theDataSink) throws JobExecutionFailedException {

		ImportLoincFileSetJson data = theStepExecutionDetails.getData();
		if (data.getChunkForCurrentStep() != null) {

			String stagingVersion = data.getCodeSystemStagingVersionId();

			TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(data.getLoincCodeSystem().getUrl(), stagingVersion);
			Validate.notNull(csv, "Could not find code system version %s for code system: %s", stagingVersion, data.getLoincCodeSystem().getUrl());

			Stream<TermConcept.TermConceptPk> pids = myConceptDao.findPidsByCodeSystemVersion(csv);
			for (Iterator<TermConcept.TermConceptPk> iter = pids.iterator(); iter.hasNext(); ) {

			}

		}

		BaseExpandDistributionIntoFilesStep.submitChunksForNextStep(theStepExecutionDetails, theDataSink, data);

		return RunOutcome.SUCCESS;
	}
}
