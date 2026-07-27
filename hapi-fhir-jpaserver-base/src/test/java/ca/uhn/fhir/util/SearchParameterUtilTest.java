package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import org.assertj.core.api.SoftAssertions;
import org.hl7.fhir.instance.model.api.IBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

	// Created by claude-sonnet-4-6
	/**
	 * Observation has a "patient" search parameter (path:
	 * {@code Observation.subject.where(resolve() is Patient)}) that should belong in the Patient
	 * compartment for both R4 and R5.
	 */
	@Test
	void testPatientSpIsInPatientCompartmentForObservation() {
		FhirContext r4Ctx = FhirContext.forR4Cached();
		List<RuntimeSearchParam> r4CompartmentParams =
			r4Ctx.getResourceDefinition("Observation").getSearchParamsForCompartmentName("Patient");
		assertThat(r4CompartmentParams)
			.as("R4 Observation.patient SP missing from Patient compartment")
			.extracting(RuntimeSearchParam::getName)
			.contains("patient");

		FhirContext r5Ctx = FhirContext.forR5Cached();
		List<RuntimeSearchParam> r5CompartmentParams =
			r5Ctx.getResourceDefinition("Observation").getSearchParamsForCompartmentName("Patient");
		assertThat(r5CompartmentParams)
			.as("R5 Observation.patient SP missing from Patient compartment")
			.extracting(RuntimeSearchParam::getName)
			.contains("patient");
	}


	// Created by Claude Fable 5
	static Stream<FhirContext> allBuildableFhirContexts() {
		// Any FhirVersionEnum value whose structures are on the classpath is included
		// automatically — allows testing of future FHIR versions without change.
		return Arrays.stream(FhirVersionEnum.values())
			.map(v -> {
				try {
					return FhirContext.forCached(v);
				} catch (Exception e) {
					return null;
				}
			})
			.filter(Objects::nonNull);
	}

	// Created by Claude Fable 5
	/**
	 * Broad guard on {@link SearchParameterUtil#getMembershipCompartmentsForSearchParameter} since it now
	 * programmatically adds the "patient" search parameter to the patient compartment.
	 * Compares SPs against its original {@code @SearchParamDefinition} to assert several invariants:
	 *
	 * <ul>
	 *   <li>No SP compartment memberships were unintentionally dropped, compared to the spec</li>
	 *   <li>Our fix does not add SPs that are part of the pre-existing OMIT map</li>
	 *   <li>Our fix only ever adds the "patient" SP to the Patient compartment</li>
	 * </ul>
	 *
	 * <p>This bounds the blast radius of any change (no non-Patient compartment added, no non-patient
	 * SP affected, omit respected). It deliberately does NOT verify that a given {@code patient} SP's
	 * Patient membership is itself <em>correct</em> for that resource — doing so would mean
	 * re-implementing the production rule in the test. That correctness is checked independently, with
	 * a few hand-derived expected values, by {@link #testPatientSp_PatientCompartmentMembership}.
	 */
	@ParameterizedTest
	@MethodSource("allBuildableFhirContexts")
	void testCompartmentMembershipMatchesAnnotationsForEverySearchParam(FhirContext theCtx) {
		String fhirVersion = theCtx.getVersion().getVersion().name();
		SoftAssertions softly = new SoftAssertions();

		for (String resourceName : theCtx.getResourceTypes()) {
			Class<? extends IBase> resourceClass =
				theCtx.getResourceDefinition(resourceName).getImplementingClass();

			for (Field field : resourceClass.getFields()) {
				SearchParamDefinition sp = field.getAnnotation(SearchParamDefinition.class);
				if (sp == null) {
					continue;
				}

				Set<String> actualMembership =
					SearchParameterUtil.getMembershipCompartmentsForSearchParameter(resourceClass, sp);
				Set<String> membershipBySpec = getDeclaredCompartmentNames(sp);

				// Ensure that no compartment memberships were unexpectedly dropped.
				softly.assertThat(actualMembership.containsAll(membershipBySpec))
					.as(String.format(
						"SP %s.%s for FHIR version %s - Compartment membership dropped: membershipBySpec=%s, actualMembership=%s",
						resourceName, sp.name(), fhirVersion, membershipBySpec, actualMembership))
					.isTrue();

				Set<String> compartmentsAddedByHapi = new HashSet<>(actualMembership);
				compartmentsAddedByHapi.removeAll(membershipBySpec);

				// Ensure that after adding 'patient' SP to the Patient Compartment, we still respected the OMIT map
				Set<String> omitMapForResourceType = SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT
					.getOrDefault(resourceName, Collections.emptySet());
				if (omitMapForResourceType.contains(sp.name())) {
					softly.assertThat(compartmentsAddedByHapi)
						.as("SP %s.%s for FHIR version %s is in the Patient Compartment, but SP should be omitted.",
							resourceName, sp.name(), fhirVersion)
						.doesNotContain("Patient");
				}

				// The HAPI code added no additional compartment memberships
				if (compartmentsAddedByHapi.isEmpty()) {
					continue;
				}

				// We only add Patient Compartment membership (and no other compartment)
				// beyond what the FHIR spec says.
				softly.assertThat(compartmentsAddedByHapi)
					.as("SP %s.%s for FHIR version %s - HAPI code added the SP to more than just the Patient " +
							"compartment: %s.",
						resourceName, sp.name(), fhirVersion, compartmentsAddedByHapi)
					.containsExactly("Patient");

				// In the current implementation, HAPI only has special cases for adding the "patient" SP
				// to the Patient compartment.
				if (!sp.name().equals("patient")) {
					softly.fail("SP %s.%s for FHIR version %s - HAPI code added this non-patient SP to the %s compartment.",
						resourceName, sp.name(), fhirVersion, compartmentsAddedByHapi);
				}
			}
		}

		softly.assertAll();
	}

	// Created by Claude Fable 5
	static Stream<Arguments> patientSp_patientCompartmentMembershipExpectations() {
		// Hand-derived expectations (from the FHIR spec). The NEGATIVE rows trip if a refactor
		// over-broadens the rule (e.g. "add every patient SP to the Patient compartment").
		return Stream.of(
			// Negatives — must NOT be in the Patient compartment.
			// R5 SupplyRequest.patient maps to SupplyRequest.deliverFor, which is not in the Patient compartment.
			// Instead, a different SP (SupplyRequest.deliverTo) declares membership — so it must stay out.
			Arguments.of(FhirVersionEnum.R5, "SupplyRequest", false),
			// List.patient is security-excluded via the OMIT map (hapi-fhir issue #7118).
			Arguments.of(FhirVersionEnum.R4, "List", false),
			Arguments.of(FhirVersionEnum.R5, "List", false),
			// Sequence.patient is a direct SP that doesn't declare Patient-compartment membership
			Arguments.of(FhirVersionEnum.DSTU3, "Sequence", false),

			// Positives — must be in the Patient compartment.
			// Observation.patient narrows Observation.subject (a Patient-member base SP).
			Arguments.of(FhirVersionEnum.R4, "Observation", true),
			Arguments.of(FhirVersionEnum.R5, "Observation", true),
			// R5 Coverage.patient is an exact alias of Coverage.beneficiary (a Patient-member base SP).
			Arguments.of(FhirVersionEnum.R5, "Coverage", true),
			// Device.patient is a deliberate special case (hapi-fhir issue #6536).
			Arguments.of(FhirVersionEnum.R4, "Device", true)
		);
	}

	@ParameterizedTest
	@MethodSource("patientSp_patientCompartmentMembershipExpectations")
	void testPatientSp_PatientCompartmentMembership(
			FhirVersionEnum theFhirVersion, String theResourceType, boolean theExpectedInPatientCompartment) {
		FhirContext ctx = FhirContext.forCached(theFhirVersion);
		Class<? extends IBase> resourceClass =
			ctx.getResourceDefinition(theResourceType).getImplementingClass();

		SearchParamDefinition patientSp = Arrays.stream(resourceClass.getFields())
			.map(field -> field.getAnnotation(SearchParamDefinition.class))
			.filter(spd -> spd != null && spd.name().equalsIgnoreCase("patient"))
			.findFirst().orElseThrow();

		Set<String> membership =
			SearchParameterUtil.getMembershipCompartmentsForSearchParameter(resourceClass, patientSp);
		assertThat(membership.contains("Patient"))
			.as("Expected %s %s.patient to %s be in the Patient compartment", theFhirVersion, theResourceType,
				theExpectedInPatientCompartment ? "" : "NOT")
			.isEqualTo(theExpectedInPatientCompartment);
	}

	// Created by claude-sonnet-4-6
	@Test
	void testNoNewResourceTypesAddedToPatientCompartment() {
		Set<String> r4ExpectedTypes = Set.of(
				"Account", "AdverseEvent", "AllergyIntolerance", "Appointment", "AppointmentResponse",
				"AuditEvent", "Basic", "BodyStructure", "CarePlan", "CareTeam", "ChargeItem", "Claim",
				"ClaimResponse", "ClinicalImpression", "Communication", "CommunicationRequest",
				"Composition", "Condition", "Consent", "Coverage", "CoverageEligibilityRequest",
				"CoverageEligibilityResponse", "DetectedIssue", "Device", "DeviceRequest",
				"DeviceUseStatement", "DiagnosticReport", "DocumentManifest", "DocumentReference",
				"Encounter", "EnrollmentRequest", "EpisodeOfCare", "ExplanationOfBenefit",
				"FamilyMemberHistory", "Flag", "Goal", "Group", "ImagingStudy",
				"Immunization", "ImmunizationEvaluation", "ImmunizationRecommendation", "Invoice",
				"List", "MeasureReport", "Media", "MedicationAdministration", "MedicationDispense",
				"MedicationRequest", "MedicationStatement", "MolecularSequence", "NutritionOrder",
				"Observation", "Patient", "Person", "Procedure", "Provenance",
				"QuestionnaireResponse", "RelatedPerson", "RequestGroup", "ResearchSubject",
				"RiskAssessment", "Schedule", "ServiceRequest", "Specimen", "SupplyDelivery",
				"SupplyRequest", "VisionPrescription");

		Set<String> r5ExpectedTypes = Set.of(
				"Account", "AdverseEvent", "AllergyIntolerance", "Appointment", "AppointmentResponse",
				"AuditEvent", "Basic", "BiologicallyDerivedProductDispense", "BodyStructure",
				"CarePlan", "CareTeam", "ChargeItem", "Claim", "ClaimResponse", "ClinicalImpression",
				"Communication", "CommunicationRequest", "Composition", "Condition", "Consent",
				"Contract", "Coverage", "CoverageEligibilityRequest", "CoverageEligibilityResponse",
				"DetectedIssue", "DeviceAssociation", "DeviceRequest", "DeviceUsage",
				"DiagnosticReport", "DocumentReference", "Encounter", "EncounterHistory",
				"EnrollmentRequest", "EpisodeOfCare", "ExplanationOfBenefit", "FamilyMemberHistory",
				"Flag", "GenomicStudy", "Goal", "Group", "GuidanceResponse", "ImagingSelection",
				"ImagingStudy", "Immunization", "ImmunizationEvaluation", "ImmunizationRecommendation",
				"Invoice", "List", "MeasureReport", "MedicationAdministration", "MedicationDispense",
				"MedicationRequest", "MedicationStatement", "MolecularSequence", "NutritionIntake",
				"NutritionOrder", "Observation", "Patient", "Person", "Procedure", "Provenance",
				"QuestionnaireResponse", "RelatedPerson", "RequestOrchestration", "ResearchSubject",
				"RiskAssessment", "Schedule", "ServiceRequest", "Specimen", "SupplyDelivery",
				"SupplyRequest", "Task", "VisionPrescription");

		FhirContext r4Ctx = FhirContext.forR4Cached();
		Set<String> r4ActualTypes = r4Ctx.getResourceTypes().stream()
				.filter(type -> !r4Ctx.getResourceDefinition(type)
						.getSearchParamsForCompartmentName("Patient")
						.isEmpty())
				.collect(Collectors.toSet());

		FhirContext r5Ctx = FhirContext.forR5Cached();
		Set<String> r5ActualTypes = r5Ctx.getResourceTypes().stream()
				.filter(type -> !r5Ctx.getResourceDefinition(type)
						.getSearchParamsForCompartmentName("Patient")
						.isEmpty())
				.collect(Collectors.toSet());

		assertThat(r4ActualTypes)
				.as("R4 Patient compartment resource types — update this set if FHIR spec changes")
				.containsExactlyInAnyOrderElementsOf(r4ExpectedTypes);

		assertThat(r5ActualTypes)
				.as("R5 Patient compartment resource types — update this set if FHIR spec changes")
				.containsExactlyInAnyOrderElementsOf(r5ExpectedTypes);
	}

	/**
	 * Given the SP definition, return a list of the compartments it provides membership in
	 */
	private static Set<String> getDeclaredCompartmentNames(SearchParamDefinition theSp) {
		return Arrays.stream(theSp.providesMembershipIn())
			.map(c -> SearchParameterUtil.getCleansedCompartmentName(c.name()))
			.collect(Collectors.toSet());
	}

}
