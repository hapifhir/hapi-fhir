package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreExpandValueSetJobAppCtx {

	@Bean
	public JobDefinition<PreExpandValueSetParameters> preExpandValueSetJobDefinition() {
		return JobDefinition.newBuilder()
			.setParametersType(PreExpandValueSetParameters.class)
			.setParametersValidator(preExpandValueSetJobParametersValidator())
			.setJobDefinitionId("PRE_EXPAND_VALUESET")
			.setJobDefinitionVersion(1)
			.addFirstStep("create-expansion-work-chunks", "Create work packages for ValueSet expansion", ExpansionWorkChunkJson.class, expandValueSetStep1CreateExpansionWorkChunks())
			.addIntermediateStep("handle-compose-include", "Handle ValueSet.compose.include section", ExpansionWorkChunkJson.class, expandValueSetHandleComposeInclude())
			.addIntermediateStep("handle-compose-exclude", "Handle ValueSet.compose.exclude section", ExpansionWorkChunkJson.class, expandValueSetHandleComposeExclude())
	}

	@Bean
	public IJobParametersValidator<PreExpandValueSetParameters> preExpandValueSetJobParametersValidator() {
		return new PreExpandValueSetJobParametersValidator();
	}

	@Bean
	public Step1CreateExpansionWorkChunks expandValueSetStep1CreateExpansionWorkChunks() {
		return new Step1CreateExpansionWorkChunks();
	}

	@Bean
	public IncludeOrExcludeConceptsStep<ExpansionWorkChunkJson> expandValueSetHandleComposeInclude() {
		return new IncludeOrExcludeConceptsStep<>(true);
	}

	@Bean
	public IncludeOrExcludeConceptsStep<ExpansionWorkChunkJson> expandValueSetHandleComposeExclude() {
		return new IncludeOrExcludeConceptsStep<>(false);
	}


}
