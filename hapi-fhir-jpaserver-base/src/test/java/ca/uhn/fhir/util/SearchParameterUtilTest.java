package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	private final FhirContext myCtx = FhirContext.forR5Cached();

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


	@ParameterizedTest
	@ValueSource(
		strings = {
			"AuditEvent.encounter | CarePlan.encounter | ChargeItem.encounter | ",
			"AuditEvent.encounter | CarePlan.encounter or ChargeItem.encounter | ",
			"AuditEvent.encounter or CarePlan.encounter or ChargeItem.encounter or "
		}
	)
	void testSplitSearchParameterExpressions(String input) {
		String[] split = SearchParameterUtil.splitSearchParameterExpressions(input);
		assertThat(Arrays.asList(split)).asList().contains(
			"AuditEvent.encounter", "CarePlan.encounter", "ChargeItem.encounter"
		);

	}

	@ParameterizedTest
	@CsvSource({
		/* ******************************************************************************************
		 * ResourceType, Path,                                         SearchParameter Name, Reverse, Expected
		 ********************************************************************************************/
		"  Patient,      AllergyIntolerance.patient,                   patient,              false,   false",
		"  Patient,      AllergyIntolerance.patient,                   patient,              true,    false",
		"  Patient,      Patient.generalPractitioner,                  general-practitioner, false,   false",
		"  ValueSet,     ValueSet.url,                                 url,                  false,   true",
		"  ValueSet,     ValueSet.relatedArtifact.resource,            predecessor,          false,   true",
		"  ValueSet,     ValueSet.relatedArtifact.resourceReference,   predecessor,          false,   false",
		"  ValueSet,     ValueSet.invalid-name,                        predecessor,          false,   true",
		"  AuditEvent,   AuditEvent.extension('http://foo').value.ofType(Reference),  predecessor, false,  false",
		// Some invalid paths
		"  ValueSet,     ValueSet,                                     url,                  false,   true",
		"  ValueSet,     Blah.blah,                                    url,                  true,    true",
		"  ValueSet,     ValueSet.,                                    url,                  false,   true",
		"  ValueSet,     ValueSet..,                                   url,                  false,   true",
		"  ValueSet,     ValueSet.relatedArtifact.,                    url,                  false,   true",
		"  ValueSet,     ValueSet.relatedArtifact..,                   url,                  false,   true",
		"  ValueSet,     ValueSet.url.foo,                             url,                  false,   true",
	})
	public void testReferencePathCouldPotentiallyReferenceCanonicalElement(String resourceType, String path, String paramName, boolean reverse, boolean expected) {

		// Setup
		RuntimeSearchParam param = myCtx.getResourceDefinition(resourceType).getSearchParam(paramName);

		// Test
		boolean actual = SearchParameterUtil.referencePathCouldPotentiallyReferenceCanonicalElement(myCtx, resourceType, path, reverse);

		// Verify
		assertEquals(expected, actual);
	}

	// Created by Claude Opus 4.7
	@ParameterizedTest
	@ValueSource(strings = {"Resource", "DomainResource", "CanonicalResource", "MetadataResource"})
	void testIsAbstractResourceBase_returnsTrueForAbstractBases(String theBase) {
		assertTrue(SearchParameterUtil.isAbstractResourceBase(theBase));
	}

	// Created by Claude Opus 4.7
	// FHIR resource type codes are case-sensitive per the ResourceType valueset; wrong-case
	// variants must not match.
	@ParameterizedTest
	@ValueSource(strings = {
		"Patient", "Observation", "Practitioner", "ValueSet", "StructureDefinition",
		"resource", "RESOURCE", "domainresource", "DOMAINRESOURCE",
		"canonicalresource", "CANONICALRESOURCE", "metadataresource", "METADATARESOURCE"
	})
	void testIsAbstractResourceBase_returnsFalseForConcreteOrMiscasedBases(String theBase) {
		assertFalse(SearchParameterUtil.isAbstractResourceBase(theBase));
	}

	// Created by Claude Opus 4.7
	@Test
	void testIsAbstractResourceBase_returnsFalseForEmptyString() {
		assertFalse(SearchParameterUtil.isAbstractResourceBase(""));
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withResourceBase_returnsEveryConcreteType() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("Resource"));
		assertThat(result).containsExactlyInAnyOrderElementsOf(myCtx.getResourceTypes());
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withDomainResourceBase_returnsOnlyDomainResourceDerivedTypes() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("DomainResource"));
		// DomainResource-derived (common examples)
		assertThat(result).contains("Patient", "Observation", "Practitioner", "Encounter");
		// Non-DomainResource types (extend Resource directly)
		assertThat(result).doesNotContain("Bundle", "Binary", "Parameters");
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withCanonicalResourceBase_returnsOnlyCanonicalResourceDerivedTypes() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("CanonicalResource"));
		// CanonicalResource-derived
		assertThat(result).contains("StructureDefinition", "ValueSet", "CodeSystem", "SearchParameter");
		// Not CanonicalResource-derived (plain DomainResource)
		assertThat(result).doesNotContain("Patient", "Observation", "Bundle");
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withMetadataResourceBase_returnsOnlyMetadataResourceDerivedTypes() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("MetadataResource"));
		// MetadataResource-derived
		assertThat(result).contains("Library", "Measure", "PlanDefinition", "ActivityDefinition");
		// CanonicalResource but not MetadataResource (no status/date metadata block)
		assertThat(result).doesNotContain("StructureDefinition");
		// Not CanonicalResource at all
		assertThat(result).doesNotContain("Patient", "Bundle");
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withMixedConcreteAndAbstractBase_unionsConcreteAndExpandedTypes() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("Patient", "DomainResource"));
		// Patient is preserved, DomainResource expands to all DomainResource-derived concrete types
		assertThat(result).contains("Patient", "Observation", "Practitioner");
		assertThat(result).doesNotContain("Bundle", "Binary");
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withOnlyConcreteBases_returnsInputUnchanged() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of("Patient", "Observation"));
		assertThat(result).containsExactly("Patient", "Observation");
	}

	// Created by Claude Opus 4.7
	@Test
	void testExpandBaseAsStrings_withEmptyList_returnsEmpty() {
		List<String> result = SearchParameterUtil.expandBaseWhenNeeded(myCtx, List.of());
		assertThat(result).isEmpty();
	}

}
