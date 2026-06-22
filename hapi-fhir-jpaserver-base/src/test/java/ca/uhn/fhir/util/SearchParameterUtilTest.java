package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
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
	 * {@code Observation.subject.where(resolve() is Patient)}) that belongs in the Patient
	 * compartment for both R4 and R5. Verifies that
	 * {@code getSearchParamsForCompartmentName("Patient")} includes the {@code patient} SP
	 * so that MDM auto-expansion fires for {@code ?patient=} searches on Observation.
	 */
	@Test
	void testPatientSpIsInPatientCompartmentForObservation() {
		// R4: Observation "patient" SP should be in the Patient compartment
		FhirContext r4Ctx = FhirContext.forR4Cached();
		List<RuntimeSearchParam> r4CompartmentParams =
			r4Ctx.getResourceDefinition("Observation").getSearchParamsForCompartmentName("Patient");
		assertThat(r4CompartmentParams)
			.as("R4 Observation Patient compartment search params")
			.extracting(RuntimeSearchParam::getName)
			.contains("patient");

		// R5: Observation "patient" SP should also be in the Patient compartment
		FhirContext r5Ctx = FhirContext.forR5Cached();
		List<RuntimeSearchParam> r5CompartmentParams =
			r5Ctx.getResourceDefinition("Observation").getSearchParamsForCompartmentName("Patient");
		assertThat(r5CompartmentParams)
			.as("R5 Observation Patient compartment search params")
			.extracting(RuntimeSearchParam::getName)
			.contains("patient");

		// List.patient must not be added to the Patient compartment by the new alias-path rule
		// (security exclusion per RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT, issue #7118).
		// Note: RuntimeResourceDefinition.sealAndInitialize() independently propagates compartment
		// membership to SPs that share the same path prefix (via massagePathForCompartmentSimilarity),
		// so List.patient MAY appear in getSearchParamsForCompartmentName("Patient") because
		// List.subject (path "List.subject") and List.patient (path "List.subject.where(resolve()
		// is Patient)") have the same massaged base path. That is a separate pre-existing mechanism
		// and is not what this test guards against.
		// This assertion checks getMembershipCompartmentsForSearchParameter directly: it must return
		// an empty set for R4 List.patient, confirming the alias block does NOT add Patient membership.
		@SuppressWarnings("unchecked")
		Class<? extends IBase> r4ListClass =
			(Class<? extends IBase>) r4Ctx.getResourceDefinition("List").getImplementingClass();
		SearchParamDefinition r4ListPatientAnnotation = null;
		for (Field f : r4ListClass.getFields()) {
			SearchParamDefinition spd = f.getAnnotation(SearchParamDefinition.class);
			if (spd != null && "patient".equalsIgnoreCase(spd.name())
					&& spd.path().contains(".where(resolve() is Patient)")) {
				r4ListPatientAnnotation = spd;
				break;
			}
		}
		assertThat(r4ListPatientAnnotation)
			.as("R4 ListResource should have a patient SP with alias path")
			.isNotNull();
		Set<String> r4ListPatientCompartments =
			SearchParameterUtil.getMembershipCompartmentsForSearchParameter(r4ListClass, r4ListPatientAnnotation);
		assertThat(r4ListPatientCompartments)
			.as("getMembershipCompartmentsForSearchParameter must return empty for R4 List.patient "
				+ "— the omit-map guard must block the alias-path rule from adding Patient compartment "
				+ "membership (security exclusion, issue #7118)")
			.doesNotContain("Patient");
	}


	// Created by Claude Fable 5
	static Stream<FhirContext> allBuildableFhirContexts() {
		// Any FhirVersionEnum value whose structures are on the classpath is included
		// automatically — R6 or later versions are picked up without changes here.
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
	 * Broad, version-agnostic guard on {@link SearchParameterUtil#getMembershipCompartmentsForSearchParameter}.
	 * For every search parameter on every resource type in every buildable FHIR context it asserts three
	 * invariants whose expectations come from the {@code @SearchParamDefinition} annotations (the FHIR
	 * spec), NOT from re-running the rule under test:
	 *
	 * <ul>
	 *   <li><b>No-drop</b>: a reference-type SP never loses a compartment it declares in
	 *       {@code providesMembershipIn}.</li>
	 *   <li><b>Only-Patient</b>: anything HAPI adds beyond the spec-declared compartments is exactly
	 *       {@code {Patient}} and nothing else.</li>
	 *   <li><b>Only-patient-named / omit</b>: only a SP named {@code patient} may gain that addition,
	 *       and never one excluded via
	 *       {@link SearchParameterUtil#RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT}
	 *       (e.g. List.patient, hapi-fhir issue #7118).</li>
	 * </ul>
	 *
	 * <p>This bounds the blast radius of any change (no non-Patient compartment added, no non-patient
	 * SP affected, omit respected). It deliberately does NOT verify that a given {@code patient} SP's
	 * Patient membership is itself <em>correct</em> for that resource — doing so would mean
	 * re-implementing the production rule in the test. That correctness is checked independently, with
	 * hand-derived expected values, by {@link #testPatientSpPatientCompartmentMembership}.
	 */
	@ParameterizedTest
	@MethodSource("allBuildableFhirContexts")
	void testCompartmentMembershipMatchesAnnotationsForEverySearchParam(FhirContext theCtx) {
		String fhirVersion = theCtx.getVersion().getVersion().name();
		SoftAssertions softly = new SoftAssertions();

		for (String resourceName : theCtx.getResourceTypes()) {
			@SuppressWarnings("unchecked")
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
				if (RestSearchParameterTypeEnum.forCode(
					sp.type().toLowerCase()).equals(RestSearchParameterTypeEnum.REFERENCE)) {
					softly.assertThat(actualMembership.containsAll(membershipBySpec))
						.as(String.format(
							"SP %s.%s for FHIR version %s - Compartment membership dropped: membershipBySpec=%s, actualMembership=%s",
							resourceName, sp.name(), fhirVersion, membershipBySpec, actualMembership))
						.isTrue();
				}

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

				// Only a SP named "patient" may gain Patient compartment membership. Whether that
				// membership is correct for this specific resource is verified independently, with
				// hand-derived expectations, by testPatientSpPatientCompartmentMembership — we do not
				// re-derive the production rule here.
				if (!sp.name().equals("patient")) {
					softly.fail("SP %s.%s for FHIR version %s - HAPI code added this non-patient SP to the %s compartment.",
						resourceName, sp.name(), fhirVersion, compartmentsAddedByHapi);
				}
			}
		}

		softly.assertAll();
	}

	/**
	 * Given the SP definition, return a list of the compartments it provides membership in
	 */
	private static Set<String> getDeclaredCompartmentNames(SearchParamDefinition theSp) {
		return Arrays.stream(theSp.providesMembershipIn())
			.map(c -> SearchParameterUtil.getCleansedCompartmentName(c.name()))
			.collect(Collectors.toSet());
	}

	// Created by Claude Fable 5
	static Stream<Arguments> patientSpCompartmentExpectations() {
		// Hand-derived expectations (from the FHIR spec / GL-8718 reasoning), NOT recomputed by the
		// production rule. The NEGATIVE rows are the load-bearing ones: they trip if a refactor
		// over-broadens the rule (e.g. "add every patient SP to the Patient compartment").
		return Stream.of(
			// Negatives — must NOT be in the Patient compartment.
			// R5 SupplyRequest.patient maps to SupplyRequest.deliverFor; the only Patient-member base SP
			// is "subject" (path SupplyRequest.deliverTo), a DIFFERENT field — so it must stay out.
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

	/**
	 * Independent example-based check of the GL-8718 alias rule: for a hand-picked set of resources,
	 * assert whether their {@code patient} SP is in the Patient compartment, using expected values
	 * derived from the FHIR spec — not recomputed by the production algorithm. This is what guards
	 * against an accidental over-broadening of the rule during a refactor (the negative rows flip),
	 * complementing the broad invariants in
	 * {@link #testCompartmentMembershipMatchesAnnotationsForEverySearchParam}.
	 *
	 * <p>Asserts against {@code getMembershipCompartmentsForSearchParameter} directly rather than
	 * {@code getSearchParamsForCompartmentName}, whose separate path-similarity propagation can
	 * independently surface {@code List.patient} and would obscure what this test checks.
	 */
	@ParameterizedTest
	@MethodSource("patientSpCompartmentExpectations")
	void testPatientSpPatientCompartmentMembership(
			FhirVersionEnum theFhirVersion, String theResourceType, boolean theExpectedInPatientCompartment) {
		FhirContext ctx = FhirContext.forCached(theFhirVersion);
		@SuppressWarnings("unchecked")
		Class<? extends IBase> resourceClass =
			ctx.getResourceDefinition(theResourceType).getImplementingClass();

		SearchParamDefinition patientSp = Arrays.stream(resourceClass.getFields())
			.map(field -> field.getAnnotation(SearchParamDefinition.class))
			.filter(spd -> spd != null && spd.name().equalsIgnoreCase("patient"))
			.findFirst().orElseThrow();

		Set<String> membership =
			SearchParameterUtil.getMembershipCompartmentsForSearchParameter(resourceClass, patientSp);
		assertThat(membership.contains("Patient"))
			.as("%s %s.patient (path=%s) in Patient compartment", theFhirVersion, theResourceType, patientSp.path())
			.isEqualTo(theExpectedInPatientCompartment);
	}

	// Created by claude-sonnet-4-6
	/**
	 * Snapshot test: asserts that the set of resource types in the Patient compartment does not
	 * change unexpectedly. If a future change silently adds a new resource type to the Patient
	 * compartment, this test fails and requires explicit acknowledgment and update of the
	 * expected set.
	 *
	 * <p>Run once without the expected set to discover the actual set, then hardcode it here.
	 * Both R4 and R5 are checked because they have different resource type sets.
	 */
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

}
