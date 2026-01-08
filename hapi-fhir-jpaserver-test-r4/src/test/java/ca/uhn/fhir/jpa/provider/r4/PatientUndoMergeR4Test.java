package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.merge.AbstractMergeTestScenario;
import ca.uhn.fhir.jpa.merge.PatientMergeTestScenario;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Integration tests for generic undo-merge operations on Patient resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericUndoMergeR4Test}.
 */
public class PatientUndoMergeR4Test extends AbstractGenericUndoMergeR4Test<Patient> {

	@Nonnull
	@Override
	protected AbstractMergeTestScenario<Patient> createScenario() {
		return new PatientMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd, myHelper);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Patient";
	}

	/**
	 * Tests for undo-merge compatibility across different merge endpoints.
	 *
	 * <p>Patient resources can be merged using two different endpoints:
	 * <ul>
	 *   <li><b>Patient/$merge</b> - The FHIR-standard endpoint using patient-specific parameter names
	 *       (source-patient, target-patient, source-patient-identifier, target-patient-identifier)</li>
	 *   <li><b>Patient/$hapi.fhir.merge</b> - The generic HAPI endpoint using generic parameter names
	 *       (source-resource, target-resource, source-resource-identifier, target-resource-identifier)</li>
	 * </ul>
	 *
	 * <p>The Provenance resource created during merge stores the input parameters using whichever
	 * parameter names were used. These tests verify that the $hapi.fhir.undo-merge operation can:
	 * <ul>
	 *   <li>Find and undo a merge for patients regardless of which merge endpoint was used</li>
	 *   <li>Accept both patient-specific and generic parameter names when called on Patient</li>
	 * </ul>
	 */
	@Nested
	@DisplayName("Undo-Merge Cross-Endpoint Compatibility")
	class CrossEndpointCompatibilityTests {

		/**
		 * Tests all valid combinations of merge endpoint and undo-merge parameter names.
		 * The case of generic endpoint + generic undo params is already tested in the base class.
		 */
		@ParameterizedTest(name = "patientMergeEndpoint={0}, patientUndoParams={1}, deleteSource={2}, useIdForSource={3}, useIdForTarget={4}")
		@CsvSource({
			// Patient/$merge endpoint + generic undo params
			"true,  false, false, true,  true",
			"true,  false, false, true,  false",
			"true,  false, false, false, true",
			"true,  false, false, false, false",
			"true,  false, true,  true,  true",
			"true,  false, true,  true,  false",
			"true,  false, true,  false, true",
			"true,  false, true,  false, false",
			// Patient/$merge endpoint + patient-specific undo params
			"true,  true,  false, true,  true",
			"true,  true,  false, true,  false",
			"true,  true,  false, false, true",
			"true,  true,  false, false, false",
			"true,  true,  true,  true,  true",
			"true,  true,  true,  true,  false",
			"true,  true,  true,  false, true",
			"true,  true,  true,  false, false",
			// Generic endpoint + patient-specific undo params
			"false, true,  false, true,  true",
			"false, true,  false, true,  false",
			"false, true,  false, false, true",
			"false, true,  false, false, false",
			"false, true,  true,  true,  true",
			"false, true,  true,  true,  false",
			"false, true,  true,  false, true",
			"false, true,  true,  false, false"
			// (Generic endpoint + generic undo params is tested in base class)
		})
		void testUndoMerge_CrossEndpointCompatibility(
				boolean theUsePatientMergeEndpoint,
				boolean theUsePatientSpecificUndoParams,
				boolean theDeleteSource,
				boolean theUseIdForSource,
				boolean theUseIdForTarget) {
			// Setup
			AbstractMergeTestScenario<Patient> scenario = createScenario()
				.withMultipleReferencingResources()
				.withDeleteSource(theDeleteSource);
			scenario.persistTestData();

			Patient sourceBeforeMerge = scenario.readSourceResource();
			Patient targetBeforeMerge = scenario.readTargetResource();

			// Execute merge
			if (theUsePatientMergeEndpoint) {
				callPatientSpecificMerge(scenario, theUseIdForSource, theUseIdForTarget);
			} else {
				scenario.callMergeOperation(theUseIdForSource, theUseIdForTarget);
			}

			// Execute undo-merge
			Parameters undoMergeOutParams;
			if (theUsePatientSpecificUndoParams) {
				Parameters undoParams = buildPatientSpecificParams(scenario, theUseIdForSource, theUseIdForTarget);
				undoMergeOutParams = myHelper.callUndoMergeOperation("Patient", undoParams);
			} else {
				undoMergeOutParams = scenario.callUndoMergeOperation(theUseIdForSource, theUseIdForTarget);
			}

			// Validate
			int expectedResourceCount = scenario.getTotalReferenceCount() + 2;
			validateSuccessOutcome(undoMergeOutParams, expectedResourceCount);

			Patient sourceAfterUnmerge = scenario.readSourceResource();
			Patient targetAfterUnmerge = scenario.readTargetResource();
			assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceBeforeMerge, sourceAfterUnmerge);
			assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetBeforeMerge, targetAfterUnmerge);

			scenario.assertReferencesNotUpdated();
		}
	}

	/**
	 * Calls the Patient/$merge operation.
	 */
	private void callPatientSpecificMerge(
			AbstractMergeTestScenario<Patient> theScenario,
			boolean theUseIdForSource,
			boolean theUseIdForTarget) {

		Parameters parameters = buildPatientSpecificParams(theScenario, theUseIdForSource, theUseIdForTarget);
		// Call Patient/$merge endpoint
		myClient.operation()
			.onType("Patient")
			.named("$merge")
			.withParameters(parameters)
			.returnResourceType(Parameters.class)
			.execute();
	}

	/**
	 * Builds undo-merge parameters using patient-specific parameter names.
	 */
	private Parameters buildPatientSpecificParams(
			AbstractMergeTestScenario<Patient> theScenario,
			boolean theUseIdForSource,
			boolean theUseIdForTarget) {

		Parameters undoParams = new Parameters();

		if (theUseIdForSource) {
			undoParams.addParameter()
				.setName("source-patient")
				.setValue(new Reference(theScenario.getVersionlessSourceId()));
		} else {
			for (Identifier identifier : theScenario.getSourceIdentifiers()) {
				undoParams.addParameter()
					.setName("source-patient-identifier")
					.setValue(identifier);
			}
		}

		if (theUseIdForTarget) {
			undoParams.addParameter()
				.setName("target-patient")
				.setValue(new Reference(theScenario.getVersionlessTargetId()));
		} else {
			for (Identifier identifier : theScenario.getTargetIdentifiers()) {
				undoParams.addParameter()
					.setName("target-patient-identifier")
					.setValue(identifier);
			}
		}

		return undoParams;
	}
}
