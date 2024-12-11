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
	public JobDefinition<ReplaceReferencesJobParameters> bulkImport2JobDefinition(
			ReplaceReferencesQueryIdsStep theReplaceReferencesQueryIds,
			ReplaceReferenceUpdateStep theReplaceReferenceUpdateStep,
			ReplaceReferenceUpdateTaskReducerStep theReplaceReferenceUpdateTaskReducerStep) {
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
	public ReplaceReferencesQueryIdsStep replaceReferencesQueryIdsStep(
			HapiTransactionService theHapiTransactionService, IBatch2DaoSvc theBatch2DaoSvc) {
		return new ReplaceReferencesQueryIdsStep(theHapiTransactionService, theBatch2DaoSvc);
	}

	@Bean
	public ReplaceReferenceUpdateStep replaceReferenceUpdateStep(
			FhirContext theFhirContext, ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc) {
		return new ReplaceReferenceUpdateStep(theFhirContext, theReplaceReferencesPatchBundleSvc);
	}

	@Bean
	public ReplaceReferenceUpdateTaskReducerStep replaceReferenceUpdateTaskStep(
			FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		return new ReplaceReferenceUpdateTaskReducerStep(theFhirContext, theDaoRegistry);
	}
}
