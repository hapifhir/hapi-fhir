package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreExpandValueSetJobAppCtx {

	public static final String STEP_ID_GENERATE_REPORT = "generate-report";
	public static final String STEP_ID_CALCULATE_CONCEPT_CLOSURE = "calculate-concept-closure";
	public static final String STEP_ID_LOAD_ALL_CONCEPT_IDS = "load-all-concept-ids";
	public static final String STEP_ID_CREATE_EXPANSION_WORK_CHUNKS = "create-expansion-work-chunks";
	public static final String STEP_ID_HANDLE_COMPOSE_INCLUDE = "handle-compose-include";
	public static final String STEP_ID_HANDLE_COMPOSE_EXCLUDE = "handle-compose-exclude";
	public static final String JOB_ID_PRE_EXPAND_VALUESET = "PRE_EXPAND_VALUESET";

	@Bean
	public JobDefinition<PreExpandValueSetParameters> preExpandValueSetJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_ID_PRE_EXPAND_VALUESET)
			.setJobDefinitionVersion(1)
			.setJobDescription("Pre-calculates ValueSet expansion and stores the results in the database")
			.setParametersType(PreExpandValueSetParameters.class)
			.setParametersValidator(preExpandValueSetJobParametersValidator())
			.gatedExecution()
			.addFirstStep(STEP_ID_CREATE_EXPANSION_WORK_CHUNKS, "Create work packages for ValueSet expansion", ExpansionWorkChunkJson.class, expandValueSetCreateExpansionWorkChunksStep())
			.addIntermediateStep(STEP_ID_HANDLE_COMPOSE_INCLUDE, "Handle ValueSet.compose.include section", ExpansionWorkChunkJson.class, expandValueSetHandleComposeIncludeStep())
			.addIntermediateStep(STEP_ID_HANDLE_COMPOSE_EXCLUDE, "Handle ValueSet.compose.exclude section", VoidModel.class, expandValueSetHandleComposeExcludeStep())
			.addIntermediateStep(STEP_ID_LOAD_ALL_CONCEPT_IDS, "Load all concept IDs for closure calculation", GenerateClosurePidsChunkJson.class, expandValueSetGenerateClosuresFetchIdsStep())
			.setStepWeightForProgressCalculator(STEP_ID_LOAD_ALL_CONCEPT_IDS, 0.1)
			.addIntermediateStep(STEP_ID_CALCULATE_CONCEPT_CLOSURE, "Calculate concept closure", ExpandValueSetStepOutcomeJson.class, expandValueSetGenerateClosuresWriteClosuresStep())
			.setStepWeightForProgressCalculator(STEP_ID_CALCULATE_CONCEPT_CLOSURE, 0.2)
			.addFinalReducerStep(STEP_ID_GENERATE_REPORT, "Generate Report", PreExpandValueSetResultJson.class, expandValueSetGenerateReportStep())
			.setStepWeightForProgressCalculator(STEP_ID_GENERATE_REPORT, 0.01)
			.build();
	}

	@Bean
	public IJobParametersValidator<PreExpandValueSetParameters> preExpandValueSetJobParametersValidator() {
		return new PreExpandValueSetParametersValidator();
	}

	@Bean
	public CreateExpansionWorkChunksStep expandValueSetCreateExpansionWorkChunksStep() {
		return new CreateExpansionWorkChunksStep();
	}

	@Bean
	public IncludeOrExcludeConceptsStep<ExpansionWorkChunkJson> expandValueSetHandleComposeIncludeStep() {
		return new IncludeOrExcludeConceptsStep<>(true);
	}

	@Bean
	public IncludeOrExcludeConceptsStep<VoidModel> expandValueSetHandleComposeExcludeStep() {
		return new IncludeOrExcludeConceptsStep<>(false);
	}

	@Bean
	public CalculateValueSetConceptClosureLoadPidsStep expandValueSetGenerateClosuresFetchIdsStep() {
		return new CalculateValueSetConceptClosureLoadPidsStep();
	}

	@Bean
	public CalculateValueSetConceptClosureGenerateClosureStep expandValueSetGenerateClosuresWriteClosuresStep() {
		return new CalculateValueSetConceptClosureGenerateClosureStep();
	}

	@Bean
	public GenerateReportStep expandValueSetGenerateReportStep() {
		return new GenerateReportStep();
	}


}
