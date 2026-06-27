package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Step1InitiateJob
		implements IJobStepWorker<PreExpandValueSetParameters, VoidModel, ExpandConceptsWorkChunkJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(Step1InitiateJob.class);

	/**
	 * Because we're handling each ValueSet.compose section individually, we need to
	 * pre-allocate a range of concept orders to put into {@link TermValueSetConcept#getOrder()}.
	 * We'll assume that no individual compose section will produce more than 1 million concepts,
	 * which should be a safe bet.
	 */
	private static final int MAX_CONCEPTS_PER_COMPOSE = 1_000_000;

	@Autowired
	private ITermValueSetStorageSvc myTermValueSetStorageSvc;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PreExpandValueSetParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<ExpandConceptsWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {

		PreExpandValueSetParameters parameters = theStepExecutionDetails.getParameters();
		String url = parameters.getUrl();
		String version = parameters.getVersion();
		String stagingVersion = myTermValueSetStorageSvc.startStagingVersion(url, version);

		UrlUtil.CanonicalUrlParts canonicalUrl = UrlUtil.parseCanonicalUrl(url, version);
		IBaseResource inputVs = myValidationSupport.fetchValueSet(canonicalUrl.toString());
		ValueSet valueSetCanonical = myVersionCanonicalizer.valueSetToCanonical(inputVs);

		ValueSet.ValueSetComposeComponent compose = valueSetCanonical.getCompose();
		IntCounter orderOffset = new IntCounter(0);
		int count = handleIncludeOrExclude(
				parameters, theDataSink, url, stagingVersion, compose.getInclude(), true, orderOffset);
		count += handleIncludeOrExclude(
				parameters, theDataSink, url, stagingVersion, compose.getExclude(), false, orderOffset);

		ourLog.atInfo()
				.setMessage("Generated {} expansion work chunks for ValueSet[url={}, version={}]")
				.addArgument(count)
				.addArgument(canonicalUrl.url())
				.addArgument(canonicalUrl.versionId())
				.log();

		// The load IDs step just needs to fire once, so we send a single notification
		LoadAllConceptIdsWorkChunkJson loadConceptIdsWorkChunk = new LoadAllConceptIdsWorkChunkJson();
		loadConceptIdsWorkChunk.setStagingVersionId(stagingVersion);
		theDataSink.acceptForFutureStep(
				PreExpandValueSetJobAppCtx.STEP_ID_LOAD_ALL_CONCEPT_IDS, loadConceptIdsWorkChunk);

		// Just in case no concepts match at all, we want to still send at least one chunk to the
		// report generation reducer step so that it knows the staging version
		ExpandValueSetStepOutcomeJson expandValueSetStepOutcomeJson = new ExpandValueSetStepOutcomeJson();
		expandValueSetStepOutcomeJson.setStagingVersion(stagingVersion);
		theDataSink.acceptForFutureStep(
				PreExpandValueSetJobAppCtx.STEP_ID_GENERATE_REPORT, expandValueSetStepOutcomeJson);

		return RunOutcome.SUCCESS;
	}

	private static int handleIncludeOrExclude(
			PreExpandValueSetParameters theParameters,
			@Nonnull IJobDataSink<ExpandConceptsWorkChunkJson> theDataSink,
			String theStagingUrl,
			String theStagingVersion,
			List<ValueSet.ConceptSetComponent> theSourceIncludesOrExcludes,
			boolean theInclude,
			IntCounter theOrderOffset) {
		int retVal = 0;
		for (ValueSet.ConceptSetComponent include : theSourceIncludesOrExcludes) {
			ExpandConceptsWorkChunkJson workChunk = new ExpandConceptsWorkChunkJson();
			workChunk.setCompose(include);
			workChunk.setStagingUrl(theStagingUrl);
			workChunk.setStagingVersion(theStagingVersion);

			workChunk.setStartingOrder(theOrderOffset.get());
			theOrderOffset.increment(MAX_CONCEPTS_PER_COMPOSE);

			ourLog.atInfo()
					.setMessage("Generating {} work chunk for System[{}] in ValueSet[url={}, version={}]")
					.addArgument(theInclude ? "INCLUDE" : "EXCLUDE")
					.addArgument(include.getSystem())
					.addArgument(theParameters.getCanonicalUrl().url())
					.addArgument(theParameters.getCanonicalUrl().versionId())
					.log();

			String stepId = theInclude
					? PreExpandValueSetJobAppCtx.STEP_ID_EXPAND_CONCEPTS_INCLUDE
					: PreExpandValueSetJobAppCtx.STEP_ID_EXPAND_CONCEPTS_EXCLUDE;
			theDataSink.acceptForFutureStep(stepId, workChunk);
			retVal++;
		}

		return retVal;
	}
}
