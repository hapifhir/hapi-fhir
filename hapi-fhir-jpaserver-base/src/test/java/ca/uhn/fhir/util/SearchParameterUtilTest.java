package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
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

	// Created by claude-sonnet-4-6
	static Stream<FhirContext> allBuildableFhirContexts() {
		// Any FhirVersionEnum value that can be instantiated is included automatically —
		// R6 or later versions are picked up without changes here.
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

	// Created by claude-sonnet-4-6
	/**
	 * Two-part cross-check spanning all buildable FHIR versions:
	 *
	 * <ol>
	 *   <li>{@link #assertOmitMapBlocksCompartmentInclusion}: for resources in the omit map (e.g.
	 *       Group, List), {@code getMembershipCompartmentsForSearchParameter} must return empty —
	 *       the omit-map guard must block the alias-path rule.</li>
	 *   <li>{@link #assertBaseSpAnnotationJustifiesCompartmentInclusion}: for every other resource
	 *       whose {@code patient} SP appears in {@code getSearchParamsForCompartmentName("Patient")},
	 *       the <em>specific</em> base SP named in the path (e.g. {@code subject} for
	 *       {@code Observation.subject.where(resolve() is Patient)}) must carry
	 *       {@code providesMembershipIn=Patient} in its {@code @SearchParamDefinition}. For
	 *       direct-reference resources (e.g. {@code AllergyIntolerance.patient}) that is the
	 *       {@code patient} SP itself. Device is excluded (deliberate spec override, issue #6536).
	 *   </li>
	 * </ol>
	 */
	@ParameterizedTest
	@MethodSource("allBuildableFhirContexts")
	void testPatientSpCompartmentMembershipAlignedWithBaseField(FhirContext ctx) {
		SoftAssertions softly = new SoftAssertions();
		String fhirVersion = ctx.getVersion().getVersion().name();

		for (String resourceName : ctx.getResourceTypes()) {
			RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(resourceName);

			RuntimeSearchParam patientSp = resourceDef.getSearchParam("patient");
			if (patientSp == null) continue;
			if (patientSp.getParamType() != RestSearchParameterTypeEnum.REFERENCE) continue;

			Set<String> omittedSpsForResource =
				SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT
					.getOrDefault(resourceName, Collections.emptySet());
			if (omittedSpsForResource.contains("patient")) {
				assertOmitMapBlocksCompartmentInclusion(softly, fhirVersion, resourceName, resourceDef);
				continue;
			}

			Set<String> compartmentParamNames = resourceDef.getSearchParamsForCompartmentName("Patient").stream()
				.map(RuntimeSearchParam::getName)
				.collect(Collectors.toSet());
			if (!compartmentParamNames.contains("patient")) continue;
			if ("Device".equals(resourceName)) continue;

			Class<?> resourceClass = resourceDef.getImplementingClass();
			if (resourceClass == null || resourceClass.isInterface() || resourceClass.isAnnotation()) continue;

			assertBaseSpAnnotationJustifiesCompartmentInclusion(
				softly, fhirVersion, resourceName, resourceDef, patientSp);
		}

		softly.assertAll();
	}

	/**
	 * Asserts that {@code getMembershipCompartmentsForSearchParameter} returns empty for the
	 * {@code patient} SP of a resource in the omit map. This verifies the omit-map guard blocks
	 * the alias-path rule from adding Patient compartment membership (security exclusion, issue #7118).
	 */
	private static void assertOmitMapBlocksCompartmentInclusion(
			SoftAssertions softly, String fhirVersion, String resourceName,
			RuntimeResourceDefinition resourceDef) {

		Arrays.stream(resourceDef.getImplementingClass().getFields())
			.map(f -> f.getAnnotation(SearchParamDefinition.class))
			.filter(spd -> spd != null && "patient".equalsIgnoreCase(spd.name())
				&& spd.path().contains(".where(resolve() is Patient)"))
			.findFirst()
			.ifPresent(patientAnnotation -> {
				Set<String> directCompartments =
					SearchParameterUtil.getMembershipCompartmentsForSearchParameter(
						resourceDef.getImplementingClass(), patientAnnotation);
				softly.assertThat(directCompartments)
					.as("%s %s: getMembershipCompartmentsForSearchParameter must return empty "
						+ "for security-excluded patient SP — omit-map guard is missing or broken",
						fhirVersion, resourceName)
					.doesNotContain("Patient");
			});
	}

	/**
	 * Asserts that the <em>specific</em> SP responsible for the {@code patient} SP's compartment
	 * membership declares {@code providesMembershipIn=Patient} in its {@code @SearchParamDefinition}.
	 * Two annotation locations are accepted:
	 * <ul>
	 *   <li>The SP whose single-resource {@code path()} exactly matches the extracted base field path
	 *       (e.g. the {@code subject} SP with path {@code Observation.subject}) — the R4/DSTU3
	 *       convention. Note: the SP name may differ from the field name (e.g. EnrollmentRequest's
	 *       {@code subject} SP covers {@code EnrollmentRequest.candidate}).</li>
	 *   <li>The {@code patient} SP itself — the R5 convention, where the combined cross-resource SP
	 *       carries the annotation directly.</li>
	 * </ul>
	 * If no SP annotation covers the extracted base field path AND the {@code patient} SP does not
	 * carry {@code providesMembershipIn=Patient}, the resource's compartment inclusion is via a
	 * different mechanism (e.g. path-similarity propagation in {@code sealAndInitialize}) that
	 * cannot be verified annotation-wise — the resource is skipped.
	 * Complex sub-paths (e.g. {@code participant.actor}) are also skipped.
	 */
	private static void assertBaseSpAnnotationJustifiesCompartmentInclusion(
			SoftAssertions softly, String fhirVersion, String resourceName,
			RuntimeResourceDefinition resourceDef, RuntimeSearchParam patientSp) {
		Optional<String> maybeBaseFieldPath = extractBaseFieldPath(resourceName, patientSp.getPath());
		if (maybeBaseFieldPath.isEmpty()) return;
		String baseFieldPath = maybeBaseFieldPath.get();

		// Collect all annotations once. Multiple SPs may share the same path (e.g. EnrollmentRequest
		// has both "patient" and "subject" SPs with path "EnrollmentRequest.candidate"), so we use
		// anyMatch rather than findFirst to avoid missing the one that carries providesMembershipIn.
		List<SearchParamDefinition> allAnnotations = Arrays.stream(resourceDef.getImplementingClass().getFields())
			.map(f -> f.getAnnotation(SearchParamDefinition.class))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		// Case 1: the patient param narrows some base param,
		// and the base path provides patient compartment membership
		boolean byPathJustified = allAnnotations.stream()
			.anyMatch(spd -> baseFieldPath.equals(spd.path()) && spAnnotationHasPatientCompartment(spd));

		// Case 2: the patient param itself provides patient compartment membership
		boolean patientSpaJustified = allAnnotations.stream()
			.filter(spd -> "patient".equalsIgnoreCase(spd.name()))
			.anyMatch(spd -> spAnnotationHasPatientCompartment(spd));

		// If no SP annotation covers the base field path at all AND the patient SP annotation also
		// has no providesMembershipIn, the compartment inclusion is via path-similarity propagation
		// in sealAndInitialize — we cannot verify it annotation-wise, so skip.
		// SupplyRequest.patient --> path is SupplyRequest.deliverFor
		// but only SupplyRequest.deliverTo provides patient membership
		boolean anySpCoversBasePath = allAnnotations.stream()
			.anyMatch(spd -> baseFieldPath.equals(spd.path()));
		if (!anySpCoversBasePath && !patientSpaJustified) return;

		softly.assertThat(byPathJustified || patientSpaJustified)
			.as("%s %s: 'patient' SP in Patient compartment but neither the SP covering '%s' "
				+ "nor the 'patient' SP annotation declares providesMembershipIn=Patient",
				fhirVersion, resourceName, baseFieldPath)
			.isTrue();
	}

	private static boolean spAnnotationHasPatientCompartment(SearchParamDefinition spd) {
		if (spd == null) return false;
		return Arrays.stream(spd.providesMembershipIn())
			.anyMatch(c -> {
				String name = c.name();
				if (name.startsWith("Base FHIR compartment definition for ")) {
					name = name.substring("Base FHIR compartment definition for ".length());
				}
				return "Patient".equalsIgnoreCase(name);
			});
	}

	/**
	 * Extracts the base field path (e.g. {@code "Observation.subject"}) from the {@code patient}
	 * SP's path for the given resource. Returns empty if the path segment is complex (multi-step
	 * sub-path like {@code participant.actor}) or if no resource-specific segment can be found.
	 *
	 * <p>Examples:
	 * {@code "Observation.subject.where(resolve() is Patient)"} → {@code "Observation.subject"};
	 * {@code "AllergyIntolerance.patient"} → {@code "AllergyIntolerance.patient"};
	 * {@code "EnrollmentRequest.candidate"} → {@code "EnrollmentRequest.candidate"};
	 * {@code "Appointment.participant.actor.where(...)"} → empty (complex sub-path).
	 * Pipe-delimited R5/R4B paths are handled by finding the segment starting with
	 * {@code resourceName + "."}.
	 */
	private static Optional<String> extractBaseFieldPath(String resourceName, String patientSpPath) {
		// For pipe-delimited R5/R4B paths, find the segment for this specific resource
		String segment = Arrays.stream(patientSpPath.split("\\|"))
			.map(String::trim)
			.filter(s -> s.startsWith(resourceName + "."))
			.findFirst()
			.orElse(patientSpPath.trim());

		// Strip .where(...) suffix
		int whereIdx = segment.indexOf(".where(");
		if (whereIdx >= 0) {
			segment = segment.substring(0, whereIdx);
		}

		String prefix = resourceName + ".";
		if (!segment.startsWith(prefix)) return Optional.empty();
		String fieldPart = segment.substring(prefix.length());

		// Complex sub-path like "participant.actor" — can't map to a single SP field
		if (fieldPart.contains(".")) return Optional.empty();

		return fieldPart.isEmpty() ? Optional.empty() : Optional.of(segment);
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
