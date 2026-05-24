package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

public class ImportLoincStep21GenerateConceptClosures implements IJobStepWorker<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep21GenerateConceptClosures.class);

	@Autowired
	private ITermConceptDao myConceptDao;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private EntityManager myEntityManager;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink) throws JobExecutionFailedException {
		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		StopWatch sw = new StopWatch();

		Integer defaultPartitionId = myPartitionSettings.getDefaultPartitionId();
		List<TermConcept.TermConceptPk> ids = data
			.getConceptPidsToGenerateClosureFor()
			.stream()
			.map(t -> new TermConcept.TermConceptPk(t, defaultPartitionId))
			.toList();

		List<TermConcept> concepts = myConceptDao.findAllById(ids);
		for (TermConcept concept : concepts) {
			concept.setParentPids(null);
			concept.prePersist();
			myEntityManager.merge(concept);
		}

		myEntityManager.flush();

		ourLog.atInfo()
			.setMessage("Calculated hierarchy closure for {} concepts in {}. {}/sec")
			.addArgument(ids.size())
			.addArgument(sw)
			.addArgument(sw.formatThroughput(ids.size(), TimeUnit.SECONDS))
			.log();

		// Emit statistics
		TerminologyFileSetJson outputChunk = new TerminologyFileSetJson();
		outputChunk.getRecordsAddedCounter(theStepExecutionDetails.getCurrentStepId()).incrementOtherChanges(ids.size());
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, outputChunk);

		return RunOutcome.SUCCESS;
	}
}
