package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseFinalizeStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.TreeMap;

public class Step8GenerateReportStep extends BaseFinalizeStep<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson, PreExpandValueSetResultJson> implements IReductionStepWorker<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson, PreExpandValueSetResultJson> {

	private final ITermValueSetStorageSvc myTermValueSetStorageSvc;
	private final ITermReadSvc myTermReadSvc;
	private final IValidationSupport myValidationSupport;
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();
	private final TreeMap<Integer, TerminologyFileSetJson.RecordsAddedCounter> myComposeOrderToCounter = new TreeMap<>();
	private final TreeMap<Integer, ValueSet.ValueSetComposeComponent> myComposeOrderToCompose = new TreeMap<>();

	private String myStagingVersion;
	private String myFailureMessage;

	public Step8GenerateReportStep(IValidationSupport theValidationSupport, ITermReadSvc theTermReadSvc, ITermValueSetStorageSvc theTermValueSetStorageSvc) {
		myValidationSupport = theValidationSupport;
		myTermReadSvc = theTermReadSvc;
		myTermValueSetStorageSvc = theTermValueSetStorageSvc;
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theChunkDetails) {
		ExpandValueSetStepOutcomeJson data = theChunkDetails.getData();

		if (data.getSourceCompose() != null) {
			int startingOrder = data.getStartingOrder();
			myComposeOrderToCompose.computeIfAbsent(startingOrder, t -> data.getSourceCompose());

			TerminologyFileSetJson.RecordsAddedCounter existingCounter = myComposeOrderToCounter.computeIfAbsent(startingOrder, t -> new TerminologyFileSetJson.RecordsAddedCounter());
			existingCounter.copyFrom(data.getRecordsAddedCounter());

			super.accumulateStatistics(data.getRecordsAddedCounter());
		}

		myStagingVersion = data.getStagingVersion();

		if (data.getFailureMessage() != null) {
			myFailureMessage = data.getFailureMessage();
		}

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theStepExecutionDetails, @Nonnull IJobDataSink<PreExpandValueSetResultJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {
		String url = theStepExecutionDetails.getParameters().getCanonicalUrl().url();
		String version = theStepExecutionDetails.getParameters().getCanonicalUrl().versionId().orElse(null);

		if (myFailureMessage != null) {
			myTermValueSetStorageSvc.dropStagingVersion(url, myStagingVersion);
			myTermValueSetStorageSvc.markValueSetAsFailedToExpand(url, version);

			// FIXME: add code
			throw new JobExecutionFailedException(Msg.code(1) + myFailureMessage);
		}

		myTermValueSetStorageSvc.activateStagingVersion(url, myStagingVersion);

		String report = createReport(theStepExecutionDetails);

		PreExpandValueSetResultJson result = new PreExpandValueSetResultJson();
		result.setReport(report);

		theDataSink.accept(result);

		/*
		 * Invalidate local caches. This doesn't actually completely help in a cluster,
		 * but in that case we can expect that the cache will time out eventually anyhow.
		 * This is mostly for unit tests and small local testing scenarios.
		 */
		myValidationSupport.invalidateCaches();
		myTermReadSvc.invalidateCaches();

		return RunOutcome.SUCCESS;
	}

	@Override
	public IReductionStepWorker<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson, PreExpandValueSetResultJson> newInstance() {
		return new Step8GenerateReportStep(myValidationSupport, myTermReadSvc, myTermValueSetStorageSvc);
	}


	@Nonnull
	@Override
	protected List<String> getReportTitleLines(StepExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theStepExecutionDetails) {
		PreExpandValueSetParameters parameters = theStepExecutionDetails.getParameters();
		return List.of(
			"ValueSet Expansion Report",
			"URL: " + parameters.getCanonicalUrl().url(),
			"Version: " + parameters.getCanonicalUrl().versionId().orElse("(none)")
		);
	}

	@Override
	protected void appendAdditionalInfo(StepExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theStepExecutionDetails, StringBuilder theReportBuilder) {
		for (Integer order : myComposeOrderToCompose.keySet()) {
			ValueSet.ValueSetComposeComponent compose = myComposeOrderToCompose.get(order);
			TerminologyFileSetJson.RecordsAddedCounter counter = myComposeOrderToCounter.get(order);

			addDivider(theReportBuilder);
			theReportBuilder.append("Compose: ");
			theReportBuilder.append(myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).encodeToString(compose));
			theReportBuilder.append("\n");
			appendCounts(counter, theReportBuilder, 1);
		}
	}

	protected void appendNoChangesMessage(StringBuilder theReportBuilder) {
		theReportBuilder.append("No concepts matched\n");
	}

}
