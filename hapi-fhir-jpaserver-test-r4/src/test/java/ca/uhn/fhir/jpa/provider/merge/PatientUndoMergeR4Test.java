package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.jpa.merge.AbstractMergeTestScenario;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.merge.PatientMergeTestScenario;
import ca.uhn.fhir.merge.PatientMergeOperationInputParameterNames;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
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

	private MergeOperationTestHelper myHelperUsingPatientMergeEndpoint;

	@BeforeEach
	void beforePatientUndoMerge() {
		myHelperUsingPatientMergeEndpoint = new MergeOperationTestHelper(
				myClient, myBatch2JobHelper, myFhirContext, myLinkServiceFactory, myDaoRegistry,
				"$merge", new PatientMergeOperationInputParameterNames());
	}

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
			MergeOperationTestHelper mergeHelper = theUsePatientMergeEndpoint
					? myHelperUsingPatientMergeEndpoint : myHelper;
			MergeTestParameters mergeParams = scenario.buildMergeOperationParameters(theUseIdForSource, theUseIdForTarget);
			mergeHelper.callMergeOperation("Patient", mergeParams, false);

			// Execute undo-merge
			Parameters undoParams = theUsePatientSpecificUndoParams
					? scenario.buildUndoMergeParameters(theUseIdForSource, theUseIdForTarget, myHelperUsingPatientMergeEndpoint.getParameterNames())
					: scenario.buildUndoMergeParameters(theUseIdForSource, theUseIdForTarget, myHelper.getParameterNames());
			Parameters undoMergeOutParams = myHelper.callUndoMergeOperation("Patient", undoParams);

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
}
