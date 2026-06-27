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
	public static final String STEP_ID_INITIATE_JOB = "initiate-job";
	public static final String STEP_ID_EXPAND_CONCEPTS_INCLUDE = "expand-concepts-include";
	public static final String STEP_ID_EXPAND_CONCEPTS_EXCLUDE = "expand-concepts-exclude";
	public static final String STEP_ID_WRITE_CONCEPTS_INCLUDE = "write-concepts-include";
	public static final String STEP_ID_WRITE_CONCEPTS_EXCLUDE = "write-concepts-exclude";
	public static final String JOB_ID_PRE_EXPAND_VALUESET = "PRE_EXPAND_VALUESET";
	public static final String STEP_ID_LOAD_ALL_CONCEPT_IDS = "load-all-concept-ids";
	public static final String STEP_ID_COMPACT_CONCEPTS = "compact-concepts";

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
			.addFirstStep(STEP_ID_INITIATE_JOB, "Create work packages for ValueSet expansion", ExpandConceptsWorkChunkJson.class, expandValueSetStep1InitiateJobStep())
			.addIntermediateStep(STEP_ID_EXPAND_CONCEPTS_INCLUDE, "Expand ValueSet.compose.include section into concept lists", ExpandConceptsWorkChunkJson.class, expandValueSetStep2ExpandConceptsIncludeStep())
			.addIntermediateStep(STEP_ID_EXPAND_CONCEPTS_EXCLUDE, "Expand ValueSet.compose.exclude section into concept lists", WriteConceptsWorkChunkJson.class, expandValueSetStep3ExpandConceptsExcludeStep())
			.addIntermediateStep(STEP_ID_WRITE_CONCEPTS_INCLUDE, "Write concepts for ValueSet.compose.include section", WriteConceptsWorkChunkJson.class, expandValueSetStep4WriteConceptsIncludeStep())
			.addIntermediateStep(STEP_ID_WRITE_CONCEPTS_EXCLUDE, "Write concepts ValueSet.compose.exclude section", LoadAllConceptIdsWorkChunkJson.class, expandValueSetStep5WriteConceptsExcludeStep())
			.addIntermediateStep(STEP_ID_LOAD_ALL_CONCEPT_IDS, "Fetch all concept IDs in the ValueSet", CompactConceptsWorkChunkJson.class, preExpandValueSetStep6LoadAllConceptIdsStep())
			.addIntermediateStep(STEP_ID_COMPACT_CONCEPTS, "Count and compact concept orders", ExpandValueSetStepOutcomeJson.class, preExpandValueSetStep7CompactConceptsStep())
			.addFinalReducerStep(STEP_ID_GENERATE_REPORT, "Generate Report", PreExpandValueSetResultJson.class, expandValueSetStep8GenerateReportStep())
			.setStepWeightForProgressCalculator(STEP_ID_GENERATE_REPORT, 0.01)
			.build();
	}

	@Bean
	public IJobParametersValidator<PreExpandValueSetParameters> preExpandValueSetJobParametersValidator() {
		return new PreExpandValueSetParametersValidator();
	}

	@Bean
	public Step1InitiateJob expandValueSetStep1InitiateJobStep() {
		return new Step1InitiateJob();
	}

	@Bean
	public Step2And3ExpandConceptsStep<ExpandConceptsWorkChunkJson> expandValueSetStep2ExpandConceptsIncludeStep() {
		return new Step2And3ExpandConceptsStep<>(true);
	}

	@Bean
	public Step2And3ExpandConceptsStep<WriteConceptsWorkChunkJson> expandValueSetStep3ExpandConceptsExcludeStep() {
		return new Step2And3ExpandConceptsStep<>(false);
	}

	@Bean
	public Step4And5WriteConceptsStep<WriteConceptsWorkChunkJson> expandValueSetStep4WriteConceptsIncludeStep() {
		return new Step4And5WriteConceptsStep<>(true);
	}

	@Bean
	public Step4And5WriteConceptsStep<LoadAllConceptIdsWorkChunkJson> expandValueSetStep5WriteConceptsExcludeStep() {
		return new Step4And5WriteConceptsStep<>(false);
	}

	@Bean
	public Step6LoadAllConceptIdsStep preExpandValueSetStep6LoadAllConceptIdsStep() {
		return new Step6LoadAllConceptIdsStep();
	}

	@Bean
	public Step7CompactConceptsStep preExpandValueSetStep7CompactConceptsStep() {
		return new Step7CompactConceptsStep();
	}

	@Bean
	public Step8GenerateReportStep expandValueSetStep8GenerateReportStep() {
		return new Step8GenerateReportStep(myValidationSupport, myTermReadSvc, myTermValueSetStorageSvc);
	}


}
