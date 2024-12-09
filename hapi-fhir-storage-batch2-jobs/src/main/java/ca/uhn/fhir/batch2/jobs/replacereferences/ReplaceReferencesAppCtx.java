package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplaceReferencesAppCtx {
	private static final String JOB_REPLACE_REFERENCES = "REPLACE_REFERENCES";

	@Bean
	public JobDefinition<ReplaceReferencesJobParameters> bulkImport2JobDefinition(
			ReplaceReferencesQueryIdsStep theReplaceReferencesQueryIds,
			ReplaceReferenceUpdateStep theReplaceReferenceUpdateStep,
			ReplaceReferenceUpdateTaskStep theReplaceReferenceUpdateTaskStep) {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_REPLACE_REFERENCES)
				.setJobDescription("Replace References")
				.setJobDefinitionVersion(1)
				.setParametersType(ReplaceReferencesJobParameters.class)
				.addFirstStep(
						"query-ids",
						"Query IDs of resources that link to the source resource",
						ResourceIdListWorkChunkJson.class,
						theReplaceReferencesQueryIds)
				.addIntermediateStep(
						"replace-references",
						"Update all references from pointing to source to pointing to target",
						ReplaceReferenceResults.class,
						theReplaceReferenceUpdateStep)
				.addLastStep(
						"update-task",
						"Waits for replace reference work to complete and updates Task.",
						theReplaceReferenceUpdateTaskStep)
				.build();
	}

	@Bean
	public ReplaceReferencesQueryIdsStep replaceReferencesQueryIdsStep() {
		return new ReplaceReferencesQueryIdsStep();
	}

	@Bean
	public ReplaceReferenceUpdateStep replaceReferenceUpdateStep() {
		return new ReplaceReferenceUpdateStep();
	}

	@Bean
	public ReplaceReferenceUpdateTaskStep replaceReferenceUpdateTaskStep() {
		return new ReplaceReferenceUpdateTaskStep();
	}
}
