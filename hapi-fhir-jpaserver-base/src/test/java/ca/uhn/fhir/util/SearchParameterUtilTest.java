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
	 * Complementary cross-check: whenever {@code getSearchParamsForCompartmentName("Patient")}
	 * includes the {@code patient} SP for a resource, that inclusion must be consistent with the
	 * FHIR spec annotation. This test is now a complementary validator — the production logic in
	 * {@code getMembershipCompartmentsForSearchParameter()} embeds the same invariant via the
	 * annotation scan (Gate 2 of the two-gate rule), but this test independently verifies the
	 * end result across all FHIR versions using a different code path. It remains valuable as:
	 * a regression guard if production logic changes in the future; an independent validator
	 * that the annotation scan is working correctly end-to-end; and the catch for future FHIR
	 * versions (R6+) that introduce a new pattern not yet considered.
	 *
	 * <p>Two cases are checked per FHIR version:
	 *
	 * <p>Alias SP (e.g. {@code Observation.subject.where(resolve() is Patient)}): the SP
	 * whose path the {@code patient} SP aliases (e.g. {@code subject}) must itself already
	 * be in {@code getSearchParamsForCompartmentName("Patient")}.
	 *
	 * <p>Direct reference ({@code ResourceName.patient}): the {@code @SearchParamDefinition}
	 * annotation on the model class must declare {@code providesMembershipIn} containing a
	 * Patient compartment entry. R5 model classes from {@code org.hl7.fhir.core} that are
	 * abstract or unannotated are skipped gracefully.
	 */
	@ParameterizedTest
	@MethodSource("allBuildableFhirContexts")
	void testPatientSpCompartmentMembershipAlignedWithBaseField(FhirContext ctx) {
		SoftAssertions softly = new SoftAssertions();
		String fhirVersion = ctx.getVersion().getVersion().name();

		for (String resourceName : ctx.getResourceTypes()) {
			RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(resourceName);

			RuntimeSearchParam patientSp = resourceDef.getSearchParam("patient");
			if (patientSp == null) {
				continue;
			}
			if (patientSp.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				continue;
			}

			String path = patientSp.getPath();
			if (path == null || path.isBlank()) {
				continue;
			}

			// In R5/R4B the "patient" SP annotation path is a pipe-delimited multi-resource
			// expression (e.g. "Account.subject.where(resolve() is Patient) | ... |
			// Observation.subject.where(resolve() is Patient) | ..."). Extract only the
			// segment that starts with "ResourceName." before stripping the alias clause,
			// so that Case 3 gets a resource-specific path rather than a mangled
			// multi-resource string that matches nothing.
			String workingPath = path;
			if (path.contains("|")) {
				workingPath = Arrays.stream(path.split("\\|"))
					.map(String::trim)
					.filter(s -> s.startsWith(resourceName + "."))
					.findFirst()
					.orElse(path); // fall back to full path if no segment matches
			}

			// Strip .where(resolve() is Patient) to find the aliased base path.
			// e.g. "Observation.subject.where(resolve() is Patient)" → "Observation.subject"
			String basePath = workingPath.replace(".where(resolve() is Patient)", "").trim();

			List<RuntimeSearchParam> compartmentParams =
				resourceDef.getSearchParamsForCompartmentName("Patient");
			Set<String> compartmentParamNames = compartmentParams.stream()
				.map(RuntimeSearchParam::getName)
				.collect(Collectors.toSet());

			// ---------------------------------------------------------------
			// CASE 1: Direct reference — "ResourceName.patient" (no aliasing).
			// Validate using the @SearchParamDefinition annotation on the model
			// class field, which is generated from the FHIR spec.
			// ---------------------------------------------------------------
			String directPath = resourceName + ".patient";
			if (basePath.equals(directPath)) {
				// R5 (and other versions using org.hl7.fhir.core model classes) may
				// expose abstract or inaccessible implementing classes — skip those.
				Class<?> resourceClass = resourceDef.getImplementingClass();
				if (resourceClass == null || resourceClass.isInterface() || resourceClass.isAnnotation()) {
					continue;
				}

				// Find the @SearchParamDefinition field annotation where name = "patient".
				// The annotation is placed on public static final String SP_PATIENT fields.
				SearchParamDefinition patientAnnotation = null;
				Class<?> cls = resourceClass;
				while (cls != null && cls != Object.class) {
					for (Field field : cls.getDeclaredFields()) {
						SearchParamDefinition spd = field.getAnnotation(SearchParamDefinition.class);
						if (spd != null && "patient".equalsIgnoreCase(spd.name())) {
							patientAnnotation = spd;
							break;
						}
					}
					if (patientAnnotation != null) {
						break;
					}
					cls = cls.getSuperclass();
				}

				if (patientAnnotation == null) {
					// No annotation found — model class may be synthetic or from an
					// external dependency without annotations. Skip gracefully.
					continue;
				}

				// Check whether the annotation declares Patient compartment membership.
				// R4B and R5 annotations use the prefix "Base FHIR compartment definition for "
				// before the compartment name — strip it before comparing, mirroring
				// SearchParameterUtil.getCleansedCompartmentName().
				boolean annotationSaysPatientCompartment = Arrays.stream(patientAnnotation.providesMembershipIn())
					.anyMatch(c -> {
						String name = c.name();
						if (name.startsWith("Base FHIR compartment definition for ")) {
							name = name.substring("Base FHIR compartment definition for ".length());
						}
						return "Patient".equalsIgnoreCase(name);
					});

				// Device is excluded from the over-inclusion check because HAPI FHIR 8.0.0
				// deliberately added it to the Patient compartment against the base spec.
				// See https://github.com/hapifhir/hapi-fhir/issues/6536.
				if ("Device".equals(resourceName)) {
					continue;
				}

				// If "patient" is in the compartment but the spec annotation disagrees,
				// flag it as potential over-inclusion by the general alias rule.
				if (compartmentParamNames.contains("patient") && !annotationSaysPatientCompartment) {
					softly.fail(
						"%s %s: 'patient' SP is in Patient compartment but @SearchParamDefinition "
							+ "providesMembershipIn does not include Patient — potential over-inclusion",
						fhirVersion,
						resourceName);
				}
				continue;
			}

			// ---------------------------------------------------------------
			// CASE 2: Defensive fallthrough — the working path segment contains no
			// ".where(resolve() is Patient)" and is not "ResourceName.patient", so neither
			// the direct-reference nor the alias pattern applies.
			// In practice this never fires for a SP named "patient": every real FHIR
			// "patient" SP is either a direct Resource.patient field (Case 1) or an alias
			// via .where(resolve() is Patient) (Case 3). Skip rather than false-fail in
			// case a future path format breaks that assumption.
			// ---------------------------------------------------------------
			if (basePath.equals(workingPath)) {
				continue;
			}

			// ---------------------------------------------------------------
			// CASE 3: Alias SP — the working path contains ".where(resolve() is Patient)"
			// and is not a direct "ResourceName.patient" path.
			//
			// Security exclusion invariant: if this resource+SP is in
			// RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT, then
			// getMembershipCompartmentsForSearchParameter must NOT return "Patient" for it.
			// Note: getSearchParamsForCompartmentName("Patient") may still include the SP
			// via RuntimeResourceDefinition.sealAndInitialize()'s path-similarity propagation
			// (which adds SPs sharing the same path prefix with compartment members). That
			// is a separate pre-existing mechanism not guarded here. This test verifies only
			// that the alias-path rule itself is correctly gated by the omit map.
			// ---------------------------------------------------------------
			Set<String> omittedSpsForResource =
				SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT
					.getOrDefault(resourceName, Collections.emptySet());
			if (omittedSpsForResource.contains("patient")) {
				// Verify getMembershipCompartmentsForSearchParameter directly — this is the
				// layer guarded by the omit-map check, not getSearchParamsForCompartmentName.
				SearchParamDefinition patientAnnotation = null;
				for (Field f : resourceDef.getImplementingClass().getFields()) {
					SearchParamDefinition spd = f.getAnnotation(SearchParamDefinition.class);
					if (spd != null && "patient".equalsIgnoreCase(spd.name())
							&& spd.path().contains(".where(resolve() is Patient)")) {
						patientAnnotation = spd;
						break;
					}
				}
				if (patientAnnotation != null) {
					Set<String> directCompartments =
						SearchParameterUtil.getMembershipCompartmentsForSearchParameter(
							resourceDef.getImplementingClass(), patientAnnotation);
					softly.assertThat(directCompartments)
						.as("%s %s: getMembershipCompartmentsForSearchParameter must return empty "
							+ "for security-excluded patient SP — omit-map guard is missing or broken",
							fhirVersion, resourceName)
						.doesNotContain("Patient");
				}
				continue;
			}

			if (!compartmentParamNames.contains("patient")) {
				// "patient" is not in this resource's compartment — nothing to assert.
				continue;
			}

			// Confirm inclusion was via the alias mechanism (the path segment for this
			// resource must contain the ".where(resolve() is Patient)" clause).
			softly.assertThat(workingPath)
				.as("%s %s: 'patient' SP is in Patient compartment but its path segment "
					+ "'%s' does not contain '.where(resolve() is Patient)' — "
					+ "unexpected inclusion mechanism",
					fhirVersion, resourceName, workingPath)
				.contains(".where(resolve() is Patient)");
		}

		softly.assertAll();
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
