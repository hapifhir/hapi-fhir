package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * We can't test this class from hapi-fhir-base due to the dependency restrictions (see the IMPORTANT NOT in the pom.xml
 * That's why we're testing the class from this module.
 */
class SearchParameterUtilTest {
	private static final String COVERAGE_BENEFICIARY = "Coverage.beneficiary";
	private static final String PATIENT_LINK_OTHER = "Patient.link.other";
	private static final String RESEARCH_SUBJECT_INDIVIDUAL = "ResearchSubject.individual";

	private static final String SUPER_LONG_FHIR_PATH = "Account.subject.where(resolve() is Patient) | AdverseEvent.subject.where(resolve() is Patient) | AllergyIntolerance.patient | Appointment.participant.actor.where(resolve() is Patient) | Appointment.subject.where(resolve() is Patient) | AppointmentResponse.actor.where(resolve() is Patient) | AuditEvent.patient | Basic.subject.where(resolve() is Patient) | BodyStructure.patient | CarePlan.subject.where(resolve() is Patient) | CareTeam.subject.where(resolve() is Patient) | ChargeItem.subject.where(resolve() is Patient) | Claim.patient | ClaimResponse.patient | ClinicalImpression.subject.where(resolve() is Patient) | Communication.subject.where(resolve() is Patient) | CommunicationRequest.subject.where(resolve() is Patient) | Composition.subject.where(resolve() is Patient) | Condition.subject.where(resolve() is Patient) | Consent.subject.where(resolve() is Patient) | Contract.subject.where(resolve() is Patient) | Coverage.beneficiary | CoverageEligibilityRequest.patient | CoverageEligibilityResponse.patient | DetectedIssue.subject.where(resolve() is Patient) | DeviceRequest.subject.where(resolve() is Patient) | DeviceUsage.patient | DiagnosticReport.subject.where(resolve() is Patient) | DocumentReference.subject.where(resolve() is Patient) | Encounter.subject.where(resolve() is Patient) | EnrollmentRequest.candidate | EpisodeOfCare.patient | ExplanationOfBenefit.patient | FamilyMemberHistory.patient | Flag.subject.where(resolve() is Patient) | Goal.subject.where(resolve() is Patient) | GuidanceResponse.subject.where(resolve() is Patient) | ImagingSelection.subject.where(resolve() is Patient) | ImagingStudy.subject.where(resolve() is Patient) | Immunization.patient | ImmunizationEvaluation.patient | ImmunizationRecommendation.patient | Invoice.subject.where(resolve() is Patient) | List.subject.where(resolve() is Patient) | MeasureReport.subject.where(resolve() is Patient) | MedicationAdministration.subject.where(resolve() is Patient) | MedicationDispense.subject.where(resolve() is Patient) | MedicationRequest.subject.where(resolve() is Patient) | MedicationStatement.subject.where(resolve() is Patient) | MolecularSequence.subject.where(resolve() is Patient) | NutritionIntake.subject.where(resolve() is Patient) | NutritionOrder.subject.where(resolve() is Patient) | Observation.subject.where(resolve() is Patient) | Person.link.target.where(resolve() is Patient) | Procedure.subject.where(resolve() is Patient) | Provenance.patient | QuestionnaireResponse.subject.where(resolve() is Patient) | RelatedPerson.patient | RequestOrchestration.subject.where(resolve() is Patient) | ResearchSubject.subject.where(resolve() is Patient) | RiskAssessment.subject.where(resolve() is Patient) | ServiceRequest.subject.where(resolve() is Patient) | Specimen.subject.where(resolve() is Patient) | SupplyDelivery.patient | SupplyRequest.deliverFor | Task.for.where(resolve() is Patient) | VisionPrescription.patient";

	public static Stream<Arguments> fhirVersionAndResourceType() {
		return Stream.of(
			Arguments.of(FhirVersionEnum.DSTU3, "Coverage", COVERAGE_BENEFICIARY),
			Arguments.of(FhirVersionEnum.R4, "Coverage", COVERAGE_BENEFICIARY),
			Arguments.of(FhirVersionEnum.R4B, "Coverage", COVERAGE_BENEFICIARY),
			Arguments.of(FhirVersionEnum.R5, "Coverage", SUPER_LONG_FHIR_PATH),
			Arguments.of(FhirVersionEnum.DSTU3, "Patient", PATIENT_LINK_OTHER),
			Arguments.of(FhirVersionEnum.R4, "Patient", PATIENT_LINK_OTHER),
			Arguments.of(FhirVersionEnum.R4B, "Patient", PATIENT_LINK_OTHER),
			Arguments.of(FhirVersionEnum.R5, "Patient", PATIENT_LINK_OTHER),
			Arguments.of(FhirVersionEnum.DSTU3, "ResearchSubject", RESEARCH_SUBJECT_INDIVIDUAL),
			Arguments.of(FhirVersionEnum.R4, "ResearchSubject", RESEARCH_SUBJECT_INDIVIDUAL),
			Arguments.of(FhirVersionEnum.R4B, "ResearchSubject", RESEARCH_SUBJECT_INDIVIDUAL),
			Arguments.of(FhirVersionEnum.R5, "ResearchSubject", SUPER_LONG_FHIR_PATH)
		);
	}

	@ParameterizedTest
	@MethodSource("fhirVersionAndResourceType")
	void getOnlyPatientSearchParamForResourceType(FhirVersionEnum theFhirVersion, String theResourceType, String theExpectedPath) {
		final Optional<RuntimeSearchParam> optRuntimeSearchParam = SearchParameterUtil.getOnlyPatientSearchParamForResourceType(FhirContext.forCached(theFhirVersion), theResourceType);

		assertTrue(optRuntimeSearchParam.isPresent());
		final RuntimeSearchParam runtimeSearchParam = optRuntimeSearchParam.get();
		assertEquals(theExpectedPath, runtimeSearchParam.getPath());
	}
}
