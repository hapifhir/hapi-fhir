package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.annotation.Compartment;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
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
	 * GL-8718: In R5 (and R4), Observation has a "patient" search parameter that is
	 * in the Patient compartment. getMembershipCompartmentsForSearchParameter() must
	 * recognize it so that MDM auto-expansion fires for ?patient= searches on Observation.
	 *
	 * The R5 Observation "patient" SP has the path
	 * "Observation.subject.where(resolve() is Patient)" and its @SearchParamDefinition
	 * annotation's providesMembershipIn() includes the Patient compartment. However,
	 * because the annotation compartment name for R5 resources has the prefix
	 * "Base FHIR compartment definition for ", getCleansedCompartmentName() must strip
	 * it — and shouldCompartmentIncludeSP() must correctly match the compartment name
	 * against the definition name. Currently the "patient" SP on Observation is NOT
	 * returned by getSearchParamsForCompartmentName("Patient"), causing MDM
	 * auto-expansion to silently skip ?patient= searches.
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
	}

	// Created by claude-sonnet-4-6
	/**
	 * GL-8718 invariant: for every resource where the proposed fix adds "patient" to the
	 * Patient compartment, the underlying base field that "patient" aliases must ALREADY
	 * be in the Patient compartment. Additionally, for direct "ResourceName.patient"
	 * references, the {@code @SearchParamDefinition} annotation's
	 * {@code providesMembershipIn()} must confirm Patient compartment membership — so the
	 * fix does not over-include resources whose spec annotation disagrees.
	 *
	 * Covers all supported FHIR versions automatically via {@link FhirVersionEnum#values()}.
	 *
	 * <p>Alias SP path (e.g. {@code Observation.subject.where(resolve() is Patient)}):
	 * the SP whose path matches the stripped base (e.g. {@code Observation.subject}) must
	 * itself already be in {@code getSearchParamsForCompartmentName("Patient")}.
	 *
	 * <p>Direct reference path ({@code ResourceName.patient}): the
	 * {@code @SearchParamDefinition} field annotation on the model class must declare
	 * {@code providesMembershipIn} containing a {@code @Compartment(name="Patient")} entry.
	 * R5 model classes come from {@code org.hl7.fhir.core} (a dependency) and may be
	 * abstract or unavailable — those are skipped gracefully.
	 *
	 * <p>Before the fix this test vacuously passes for resources where "patient" is not yet
	 * in the compartment. After the fix the assertions become live, catching an invalid fix
	 * before it ships.
	 */
	@Test
	void testPatientSpCompartmentMembershipAlignedWithBaseField() {
		SoftAssertions softly = new SoftAssertions();

		// Collect a FhirContext for every FHIR version that can be instantiated.
		// When R6 or later versions are added to FhirVersionEnum, they are picked up
		// automatically without any change to this test.
		List<FhirContext> contexts = Arrays.stream(FhirVersionEnum.values())
			.map(v -> {
				try {
					return FhirContext.forCached(v);
				} catch (Exception e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		for (FhirContext ctx : contexts) {
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

				// Strip .where(resolve() is Patient) to find the aliased base path.
				// e.g. "Observation.subject.where(resolve() is Patient)" → "Observation.subject"
				String basePath = path.replace(".where(resolve() is Patient)", "").trim();

				// Resolve the Patient compartment membership for this resource.
				List<RuntimeSearchParam> compartmentParams =
					resourceDef.getSearchParamsForCompartmentName("Patient");
				Set<String> compartmentParamNames = compartmentParams.stream()
					.map(RuntimeSearchParam::getName)
					.collect(Collectors.toSet());

				// ---------------------------------------------------------------
				// CASE 1: Direct reference — "ResourceName.patient" (no aliasing).
				// Validate using the @SearchParamDefinition annotation on the model
				// class field, which is generated from the FHIR spec and is
				// independent of our SearchParameterUtil fix.
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
					boolean annotationSaysPatientCompartment = Arrays.stream(patientAnnotation.providesMembershipIn())
						.anyMatch(c -> "Patient".equalsIgnoreCase(c.name()));

					// If our fix puts "patient" in the compartment but the spec annotation
					// disagrees → flag it as potential over-inclusion by a general rule.
					if (compartmentParamNames.contains("patient") && !annotationSaysPatientCompartment) {
						softly.fail(
							"%s %s: fix adds 'patient' SP to Patient compartment but @SearchParamDefinition "
								+ "providesMembershipIn does not include Patient — potential over-inclusion by general rule",
							fhirVersion,
							resourceName);
					}
					continue;
				}

				// ---------------------------------------------------------------
				// CASE 2: Defensive fallthrough — path contains no ".where(resolve() is Patient)"
				// and is not "ResourceName.patient", so neither pattern applies.
				// In practice this never fires for a SP named "patient": every real FHIR
				// "patient" SP is either a direct Resource.patient field (Case 1) or an alias
				// via .where(resolve() is Patient) (Case 3). Skip rather than false-fail in
				// case a future path format breaks that assumption.
				// ---------------------------------------------------------------
				if (basePath.equals(path)) {
					continue;
				}

				// ---------------------------------------------------------------
				// CASE 3: Alias SP — "ResourceName.someField.where(resolve() is Patient)".
				// If "patient" is in the compartment, verify the aliased base SP is too.
				// ---------------------------------------------------------------
				if (!compartmentParamNames.contains("patient")) {
					// Fix hasn't added "patient" to this resource yet — nothing to assert.
					continue;
				}

				// "patient" IS in the compartment for this resource. Now verify that the base
				// SP (whose path the "patient" SP aliases) is also in the compartment.
				final String finalBasePath = basePath;
				List<RuntimeSearchParam> baseSps = resourceDef.getSearchParams().stream()
					.filter(sp -> {
						String spPath = sp.getPath();
						return spPath != null && (spPath.equals(finalBasePath) || spPath.contains(finalBasePath));
					})
					.collect(Collectors.toList());

				if (baseSps.isEmpty()) {
					// No SP found whose path covers the base path — skip rather than false-fail
					// (the base field may not be a searchable SP on every resource).
					continue;
				}

				for (RuntimeSearchParam baseSp : baseSps) {
					softly.assertThat(compartmentParamNames)
						.as("%s %s.patient aliases base SP '%s' (path: %s) — base SP must also be "
							+ "in Patient compartment when 'patient' is added",
							fhirVersion, resourceName, baseSp.getName(), baseSp.getPath())
						.contains(baseSp.getName());
				}
			}
		}

		softly.assertAll();
	}

}
