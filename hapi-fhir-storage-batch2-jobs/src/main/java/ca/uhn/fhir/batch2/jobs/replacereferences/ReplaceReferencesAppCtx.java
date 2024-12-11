package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplaceReferencesAppCtx {
	public static final String JOB_REPLACE_REFERENCES = "REPLACE_REFERENCES";

	@Bean
	public JobDefinition<ReplaceReferencesJobParameters> replaceReferencesJobDefinition(
			ReplaceReferencesQueryIdsStep<ReplaceReferencesJobParameters> theReplaceReferencesQueryIds,
			ReplaceReferenceUpdateStep<ReplaceReferencesJobParameters> theReplaceReferenceUpdateStep,
			ReplaceReferenceUpdateTaskReducerStep<ReplaceReferencesJobParameters>
					theReplaceReferenceUpdateTaskReducerStep) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REPLACE_REFERENCES)
				.setJobDescription("Replace References")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(ReplaceReferencesJobParameters.class)
				.addFirstStep(
						"query-ids",
						"Query IDs of resources that link to the source resource",
						FhirIdListWorkChunkJson.class,
						theReplaceReferencesQueryIds)
				.addIntermediateStep(
						"replace-references",
						"Update all references from pointing to source to pointing to target",
						ReplaceReferencePatchOutcomeJson.class,
						theReplaceReferenceUpdateStep)
				.addFinalReducerStep(
						"update-task",
						"Waits for replace reference work to complete and updates Task.",
						ReplaceReferenceResultsJson.class,
						theReplaceReferenceUpdateTaskReducerStep)
				.build();
	}

	@Bean
	public ReplaceReferencesQueryIdsStep<ReplaceReferencesJobParameters> replaceReferencesQueryIdsStep(
			HapiTransactionService theHapiTransactionService, IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReplaceReferencesQueryIdsStep<>(theHapiTransactionService, theBatch2DaoSvc);
	}

	@Bean
	public ReplaceReferenceUpdateStep<ReplaceReferencesJobParameters> replaceReferenceUpdateStep(
			FhirContext theFhirContext, ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc) {
		return new ReplaceReferenceUpdateStep<>(theFhirContext, theReplaceReferencesPatchBundleSvc);
	}

	@Bean
	public ReplaceReferenceUpdateTaskReducerStep<ReplaceReferencesJobParameters> replaceReferenceUpdateTaskStep(
			DaoRegistry theDaoRegistry) {
		return new ReplaceReferenceUpdateTaskReducerStep<>(theDaoRegistry);
	}
}
