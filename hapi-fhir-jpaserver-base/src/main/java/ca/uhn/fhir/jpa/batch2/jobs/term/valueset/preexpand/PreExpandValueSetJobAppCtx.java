package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import org.apache.commons.lang3.Validate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreExpandValueSetJobAppCtx {

	public static final String STEP_ID_GENERATE_REPORT = "generate-report";
	public static final String STEP_ID_CREATE_EXPANSION_WORK_CHUNKS = "create-expansion-work-chunks";
	public static final String STEP_ID_EXPAND_CONCEPTS_INCLUDE = "expand-concepts-include";
	public static final String STEP_ID_EXPAND_CONCEPTS_EXCLUDE = "expand-concepts-exclude";
	public static final String STEP_ID_WRITE_CONCEPTS_INCLUDE = "write-concepts-include";
	public static final String STEP_ID_WRITE_CONCEPTS_EXCLUDE = "write-concepts-exclude";
	public static final String JOB_ID_PRE_EXPAND_VALUESET = "PRE_EXPAND_VALUESET";
	public static final String STEP_ID_LOAD_ALL_CONCEPT_IDS = "load-all-concept-ids";

	private final ITermValueSetStorageSvc myTermValueSetStorageSvc;
	private final ITermReadSvc myTermReadSvc;
	private final IValidationSupport myValidationSupport;

	public PreExpandValueSetJobAppCtx(IValidationSupport theValidationSupport, ITermReadSvc theTermReadSvc, ITermValueSetStorageSvc theTermValueSetStorageSvc) {
		Validate.notNull(theTermReadSvc, "theTermReadSvc must not be null");
		Validate.notNull(theTermValueSetStorageSvc, "theTermValueSetStorageSvc must not be null");
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
		myValidationSupport = theValidationSupport;
		myTermReadSvc = theTermReadSvc;
		myTermValueSetStorageSvc = theTermValueSetStorageSvc;
	}

	@Bean
	public JobDefinition<PreExpandValueSetParameters> preExpandValueSetJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_ID_PRE_EXPAND_VALUESET)
			.setJobDefinitionVersion(1)
			.setJobDescription("Pre-calculates ValueSet expansion and stores the results in the database")
			.setParametersType(PreExpandValueSetParameters.class)
			.setParametersValidator(preExpandValueSetJobParametersValidator())
			.gatedExecution()
			.addFirstStep(STEP_ID_CREATE_EXPANSION_WORK_CHUNKS, "Create work packages for ValueSet expansion", ExpandConceptsWorkChunkJson.class, expandValueSetCreateExpansionWorkChunksStep())
			.addIntermediateStep(STEP_ID_EXPAND_CONCEPTS_INCLUDE, "Expand ValueSet.compose.include section into concept lists", ExpandConceptsWorkChunkJson.class, expandValueSetExpandConceptsIncludeStep())
			.addIntermediateStep(STEP_ID_EXPAND_CONCEPTS_EXCLUDE, "Expand ValueSet.compose.exclude section into concept lists", WriteConceptsWorkChunkJson.class, expandValueSetExpandConceptsExcludeStep())
			.addIntermediateStep(STEP_ID_WRITE_CONCEPTS_INCLUDE, "Write concepts for ValueSet.compose.include section", WriteConceptsWorkChunkJson.class, expandValueSetWriteConceptsIncludeStep())
			.addIntermediateStep(STEP_ID_WRITE_CONCEPTS_EXCLUDE, "Write concepts ValueSet.compose.exclude section", LoadAllConceptIdsWorkChunkJson.class, expandValueSetWriteConceptsExcludeStep())
			.addIntermediateStep(STEP_ID_LOAD_ALL_CONCEPT_IDS, "Fetch all concept IDs in the ValueSet", CompactConceptsWorkChunkJson.class, preExpandValueSetLoadAllConceptIdsStep())
			.addIntermediateStep("compact-concepts", "Count and compact concept orders", ExpandValueSetStepOutcomeJson.class, preExpandValueSetCompactConceptsStep())
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
	public ExpandConceptsStep<ExpandConceptsWorkChunkJson> expandValueSetExpandConceptsIncludeStep() {
		return new ExpandConceptsStep<>(true);
	}

	@Bean
	public ExpandConceptsStep<WriteConceptsWorkChunkJson> expandValueSetExpandConceptsExcludeStep() {
		return new ExpandConceptsStep<>(false);
	}

	@Bean
	public WriteConceptsStep<WriteConceptsWorkChunkJson> expandValueSetWriteConceptsIncludeStep() {
		return new WriteConceptsStep<>(true);
	}

	@Bean
	public WriteConceptsStep<LoadAllConceptIdsWorkChunkJson> expandValueSetWriteConceptsExcludeStep() {
		return new WriteConceptsStep<>(false);
	}

	@Bean
	public LoadAllConceptIdsStep preExpandValueSetLoadAllConceptIdsStep() {
		return new LoadAllConceptIdsStep();
	}

	@Bean
	public CompactConceptsStep preExpandValueSetCompactConceptsStep() {
		return new CompactConceptsStep();
	}

	@Bean
	public GenerateReportStep expandValueSetGenerateReportStep() {
		return new GenerateReportStep(myValidationSupport, myTermReadSvc, myTermValueSetStorageSvc);
	}


}
