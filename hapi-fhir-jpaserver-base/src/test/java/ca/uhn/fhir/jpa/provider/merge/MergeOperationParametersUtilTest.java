package ca.uhn.fhir.jpa.provider.merge;

// Created by claude-sonnet-4-5

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MergeOperationParametersUtilTest {

	private FhirContext myFhirContext;
	private Parameters myInputParameters;
	private OperationOutcome myOperationOutcome;
	private Patient myUpdatedPatient;
	private Task myTask;
	private final List<IProvenanceAgent> myProvenanceAgents = Collections.emptyList();

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

	@Nested
	class InputParamsFromParametersTest {

		private static final int RESOURCE_LIMIT = 1000;

		@Test
		void testFromParameters_withAllParameters_success() {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
			parameters.addParameter().setName("preview").setValue(new BooleanType(true));
			parameters.addParameter().setName("delete-source").setValue(new BooleanType(true));

			Patient resultPatient = new Patient();
			resultPatient.setId("Patient/789");
			parameters.addParameter().setName("result-patient").setResource(resultPatient);

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);
			
			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getSourceResource()).isNotNull();
			assertThat(result.getSourceResource().getReferenceElement().getValue()).isEqualTo("Patient/123");
			assertThat(result.getTargetResource()).isNotNull();
			assertThat(result.getTargetResource().getReferenceElement().getValue()).isEqualTo("Patient/456");
			assertThat(result.getPreview()).isTrue();
			assertThat(result.getDeleteSource()).isTrue();
			assertThat(result.getResultResource()).isNotNull();
			assertThat(result.getOriginalInputParameters()).isNotNull();
			assertThat(result.getResourceLimit()).isEqualTo(RESOURCE_LIMIT);
		}

		@Test
		void testFromParameters_withReferences_success() {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/source-123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/target-456"));

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getSourceResource())
				.isNotNull()
				.returns("Patient/source-123", r -> r.getReferenceElement().getValue());
			assertThat(result.getTargetResource())
				.isNotNull()
				.returns("Patient/target-456", r -> r.getReferenceElement().getValue());
			assertThat(result.getPreview()).isFalse(); // default is false
			assertThat(result.getDeleteSource()).isFalse(); // default is false
			assertThat(result.getResultResource()).isNull();
			assertThat(result.getOriginalInputParameters()).isNotNull();
		}

		@Test
		void testFromParameters_withIdentifiers_success() {
			// Arrange
			Parameters parameters = new Parameters();

			Identifier sourceIdentifier = new Identifier();
			sourceIdentifier.setSystem("http://example.com/mrn");
			sourceIdentifier.setValue("12345");
			parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier);

			Identifier targetIdentifier = new Identifier();
			targetIdentifier.setSystem("http://example.com/mrn");
			targetIdentifier.setValue("67890");
			parameters.addParameter().setName("target-patient-identifier").setValue(targetIdentifier);

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getSourceIdentifiers()).isNotNull();
			assertThat(result.getSourceIdentifiers()).hasSize(1);
			CanonicalIdentifier sourceCanonical = result.getSourceIdentifiers().get(0);
			assertThat(sourceCanonical.getSystemElement().getValue()).isEqualTo("http://example.com/mrn");
			assertThat(sourceCanonical.getValueElement().getValue()).isEqualTo("12345");

			assertThat(result.getTargetIdentifiers()).isNotNull();
			assertThat(result.getTargetIdentifiers()).hasSize(1);
			CanonicalIdentifier targetCanonical = result.getTargetIdentifiers().get(0);
			assertThat(targetCanonical.getSystemElement().getValue()).isEqualTo("http://example.com/mrn");
			assertThat(targetCanonical.getValueElement().getValue()).isEqualTo("67890");
		}

		@Test
		void testFromParameters_withMultipleIdentifiers_success() {
			// Arrange
			Parameters parameters = new Parameters();

			Identifier sourceIdentifier1 = new Identifier();
			sourceIdentifier1.setSystem("http://example.com/mrn");
			sourceIdentifier1.setValue("12345");
			parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier1);

			Identifier sourceIdentifier2 = new Identifier();
			sourceIdentifier2.setSystem("http://example.com/ssn");
			sourceIdentifier2.setValue("999-99-9999");
			parameters.addParameter().setName("source-patient-identifier").setValue(sourceIdentifier2);

			Identifier targetIdentifier = new Identifier();
			targetIdentifier.setSystem("http://example.com/mrn");
			targetIdentifier.setValue("67890");
			parameters.addParameter().setName("target-patient-identifier").setValue(targetIdentifier);

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result).isNotNull();
			assertThat(result.getSourceIdentifiers()).hasSize(2);
			assertThat(result.getTargetIdentifiers()).hasSize(1);
		}

		@Test
		void testFromParameters_withPreviewTrue_setsFlag() {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
			parameters.addParameter().setName("preview").setValue(new BooleanType(true));

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result.getPreview()).isTrue();
		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		void testFromParameters_withDeleteSourceTrue_setsFlag(boolean theDeleteSourceParam) {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
			parameters.addParameter().setName("delete-source").setValue(new BooleanType(theDeleteSourceParam));

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result.getDeleteSource()).isEqualTo(theDeleteSourceParam);
		}

		@Test
		void testFromParameters_withDeleteSourceFalse_doesNotSetFlag() {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));
			parameters.addParameter().setName("delete-source").setValue(new BooleanType(false));

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result.getDeleteSource()).isFalse();
		}

		@Test
		void testFromParameters_withResultPatient_storesResource() {
			// Arrange
			Parameters parameters = new Parameters();
			parameters.addParameter().setName("source-patient").setValue(new Reference("Patient/123"));
			parameters.addParameter().setName("target-patient").setValue(new Reference("Patient/456"));

			Patient resultPatient = new Patient();
			resultPatient.setId("Patient/result");
			resultPatient.addName().setFamily("ResultFamily");
			parameters.addParameter().setName("result-patient").setResource(resultPatient);

			// Act
			MergeOperationInputParameters result = MergeOperationParametersUtil.inputParamsFromParameters(myFhirContext, parameters, RESOURCE_LIMIT, myProvenanceAgents);

			// Assert
			assertThat(result.getResultResource()).isNotNull();
			assertThat(result.getResultResource()).isInstanceOf(Patient.class);
			Patient storedPatient = (Patient) result.getResultResource();
			assertThat(storedPatient.getIdElement().getValue()).isEqualTo("Patient/result");
			assertThat(storedPatient.getName()).hasSize(1);
			assertThat(storedPatient.getName().get(0).getFamily()).isEqualTo("ResultFamily");
		}
	}
}
