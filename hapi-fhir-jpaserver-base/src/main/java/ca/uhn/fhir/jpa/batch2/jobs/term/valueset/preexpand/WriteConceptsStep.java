package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_GENERATE_REPORT;

public class WriteConceptsStep<OT extends IModelJson> implements IJobStepWorker<PreExpandValueSetParameters, WriteConceptsWorkChunkJson, OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(WriteConceptsStep.class);

	private final boolean myInclude;

	@Autowired
	private ITermValueSetStorageSvc myTermValueSetStorageSvc;

	public WriteConceptsStep(boolean theInclude) {
		myInclude = theInclude;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, WriteConceptsWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<OT> theDataSink) throws JobExecutionFailedException {

		WriteConceptsWorkChunkJson data = theStepExecutionDetails.getData();

		int startingOrder = data.getStartingOrder();
		int startingOrderOffset = data.getStartingOrderOffset();
		ValueSet delta = data.getValueSet();

		ourLog.atInfo()
			.setMessage("Writing {} concepts as {} with starting order {} and offset {}")
			.addArgument(delta.getExpansion().getContains().size())
			.addArgument(myInclude ? "INCLUDE" : "EXCLUDE")
			.addArgument(startingOrder)
			.addArgument(startingOrderOffset)
			.log();

		UploadStatistics statistics;
		if (myInclude) {
			statistics = myTermValueSetStorageSvc.addConceptsToExpansion(delta, startingOrder + startingOrderOffset);
		} else {
			statistics = myTermValueSetStorageSvc.removeConceptsFromExpansion(delta);
		}

		TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter = new TerminologyFileSetJson.RecordsAddedCounter();
		recordsAddedCounter.increment(statistics);

		ExpandValueSetStepOutcomeJson outcome = new ExpandValueSetStepOutcomeJson();
		outcome.setStartingOrder(startingOrder);
		outcome.setStagingVersion(data.getStagingVersion());
		outcome.setSourceCompose(data.getValueSet().getCompose());
		outcome.setRecordsAddedCounter(recordsAddedCounter);
		theDataSink.acceptForFutureStep(STEP_ID_GENERATE_REPORT, outcome);

		return RunOutcome.SUCCESS;
	}

}
