package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MergeAppCtx {
	public static final String JOB_MERGE = "MERGE";

	@Bean
	public JobDefinition<MergeJobParameters> merge(
			ReplaceReferencesQueryIdsStep<MergeJobParameters> theMergeQueryIds,
			ReplaceReferenceUpdateStep<MergeJobParameters> theMergeUpdateStep,
			MergeUpdateTaskReducerStep theMergeUpdateTaskReducerStep) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_MERGE)
				.setJobDescription("Merge Resources")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(MergeJobParameters.class)
				.addFirstStep(
						"query-ids",
						"Query IDs of resources that link to the source resource",
						FhirIdListWorkChunkJson.class,
						theMergeQueryIds)
				.addIntermediateStep(
						"replace-references",
						"Update all references from pointing to source to pointing to target",
						ReplaceReferencePatchOutcomeJson.class,
						theMergeUpdateStep)
				.addFinalReducerStep(
						"update-task",
						"Waits for replace reference work to complete and updates Task.",
						ReplaceReferenceResultsJson.class,
						theMergeUpdateTaskReducerStep)
				.build();
	}

	@Bean
	public ReplaceReferencesQueryIdsStep<MergeJobParameters> mergeQueryIdsStep(
			HapiTransactionService theHapiTransactionService, IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReplaceReferencesQueryIdsStep(theHapiTransactionService, theBatch2DaoSvc);
	}

	@Bean
	public ReplaceReferenceUpdateStep<MergeJobParameters> mergeUpdateStep(
			FhirContext theFhirContext, ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc) {
		return new ReplaceReferenceUpdateStep(theFhirContext, theReplaceReferencesPatchBundleSvc);
	}

	@Bean
	public MergeUpdateTaskReducerStep mergeUpdateTaskStep(DaoRegistry theDaoRegistry) {
		return new MergeUpdateTaskReducerStep(theDaoRegistry);
	}
}
