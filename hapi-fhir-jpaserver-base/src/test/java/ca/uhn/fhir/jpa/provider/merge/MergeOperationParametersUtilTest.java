package ca.uhn.fhir.jpa.provider.merge;

// Created by claude-sonnet-4-5

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MergeOperationParametersUtilTest {

	private FhirContext myFhirContext;
	private Parameters myInputParameters;
	private OperationOutcome myOperationOutcome;
	private Patient myUpdatedPatient;
	private Task myTask;

	@BeforeEach
	void setUp() {
		myFhirContext = FhirContext.forR4Cached();

		// Create test input parameters
		myInputParameters = new Parameters();
		myInputParameters.addParameter().setName("source-patient").setValue(new Reference("Patient/source"));
		myInputParameters.addParameter().setName("target-patient").setValue(new Reference("Patient/target"));

		// Create test operation outcome
		myOperationOutcome = new OperationOutcome();
		myOperationOutcome.addIssue()
				.setSeverity(OperationOutcome.IssueSeverity.INFORMATION)
				.setCode(OperationOutcome.IssueType.INFORMATIONAL)
				.setDiagnostics("Merge completed successfully");

		// Create test updated patient
		myUpdatedPatient = new Patient();
		myUpdatedPatient.setId("Patient/target");
		myUpdatedPatient.setActive(true);

		// Create test task
		myTask = new Task();
		myTask.setId("Task/merge-123");
		myTask.setStatus(Task.TaskStatus.COMPLETED);
	}

	@Test
	void testBuildOutputParameters_withMergeOperationOutcome_allComponentsPresent() {
		// Arrange
		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		mergeOutcome.setOperationOutcome(myOperationOutcome);
		mergeOutcome.setUpdatedTargetResource(myUpdatedPatient);
		mergeOutcome.setTask(myTask);
		mergeOutcome.setHttpStatusCode(200);

		// Act
		Parameters result = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext, mergeOutcome, myInputParameters);

		// Assert
		assertThat(result).isNotNull();

		// Verify input parameter
		assertThat(ParametersUtil.getNamedParameters(myFhirContext, result,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT))
				.hasSize(1);

		// Verify operation outcome
		OperationOutcome outcome = (OperationOutcome) ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME)
				.orElseThrow();
		assertThat(outcome.getIssueFirstRep().getDiagnostics()).isEqualTo("Merge completed successfully");

		// Verify result patient
		Patient resultPatient = (Patient) ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT)
				.orElseThrow();
		assertThat(resultPatient.getId()).isEqualTo("Patient/target");

		// Verify task
		Task resultTask = (Task) ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK)
				.orElseThrow();
		assertThat(resultTask.getId()).isEqualTo("Task/merge-123");
	}

	@Test
	void testBuildOutputParameters_withMergeOperationOutcome_optionalComponentsNull() {
		// Arrange - outcome with null updatedTargetResource and task (preview mode)
		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		mergeOutcome.setOperationOutcome(myOperationOutcome);
		mergeOutcome.setUpdatedTargetResource(null);
		mergeOutcome.setTask(null);
		mergeOutcome.setHttpStatusCode(200);

		// Act
		Parameters result = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext, mergeOutcome, myInputParameters);

		// Assert
		assertThat(result).isNotNull();

		// Verify input parameter present
		assertThat(ParametersUtil.getNamedParameters(myFhirContext, result,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT))
				.hasSize(1);

		// Verify operation outcome present
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME))
				.isPresent();

		// Verify result patient NOT present
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT))
				.isEmpty();

		// Verify task NOT present
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK))
				.isEmpty();
	}

	@Test
	void testBuildOutputParameters_withIndividualComponents_allComponentsPresent() {
		// Act
		Parameters result = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext,
				myOperationOutcome,
				myUpdatedPatient,
				myTask,
				myInputParameters);

		// Assert
		assertThat(result).isNotNull();

		// Verify all components present
		assertThat(ParametersUtil.getNamedParameters(myFhirContext, result,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT))
				.hasSize(1);
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME))
				.isPresent();
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT))
				.isPresent();
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK))
				.isPresent();
	}

	@Test
	void testBuildOutputParameters_withIndividualComponents_optionalComponentsNull() {
		// Act
		Parameters result = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext,
				myOperationOutcome,
				null, // no updated patient
				null, // no task
				myInputParameters);

		// Assert
		assertThat(result).isNotNull();

		// Verify required components present
		assertThat(ParametersUtil.getNamedParameters(myFhirContext, result,
				ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT))
				.hasSize(1);
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME))
				.isPresent();

		// Verify optional components NOT present
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT))
				.isEmpty();
		assertThat(ParametersUtil.getNamedParameterResource(
				myFhirContext, result, ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK))
				.isEmpty();
	}

	@Test
	void testBuildOutputParameters_bothMethodsProduceSameResult() {
		// Arrange
		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		mergeOutcome.setOperationOutcome(myOperationOutcome);
		mergeOutcome.setUpdatedTargetResource(myUpdatedPatient);
		mergeOutcome.setTask(myTask);
		mergeOutcome.setHttpStatusCode(200);

		// Act - call both methods
		Parameters resultFromOutcome = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext, mergeOutcome, myInputParameters);

		Parameters resultFromComponents = (Parameters) MergeOperationParametersUtil.buildMergeOperationOutputParameters(
				myFhirContext,
				myOperationOutcome,
				myUpdatedPatient,
				myTask,
				myInputParameters);

		// Assert - both should produce equivalent results
		assertThat(resultFromOutcome.getParameter()).hasSameSizeAs(resultFromComponents.getParameter());
		assertThat(resultFromOutcome.getParameter().get(0).getName())
				.isEqualTo(resultFromComponents.getParameter().get(0).getName());
		assertThat(resultFromOutcome.getParameter().get(1).getName())
				.isEqualTo(resultFromComponents.getParameter().get(1).getName());
		assertThat(resultFromOutcome.getParameter().get(2).getName())
				.isEqualTo(resultFromComponents.getParameter().get(2).getName());
		assertThat(resultFromOutcome.getParameter().get(3).getName())
				.isEqualTo(resultFromComponents.getParameter().get(3).getName());
	}
}
