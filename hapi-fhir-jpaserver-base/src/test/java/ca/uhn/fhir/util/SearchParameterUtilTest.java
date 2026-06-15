package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
	 * Closed-world contract for {@link SearchParameterUtil#getMembershipCompartmentsForSearchParameter}:
	 * for every search parameter on every resource type in every buildable FHIR context, the function
	 * must never return a compartment that the {@code @SearchParamDefinition} annotation does not
	 * declare in {@code providesMembershipIn} — with exactly one allowed exception, the "patient"
	 * alias rule (GL-8718), and only when that addition is justified by one of:
	 *
	 * <ul>
	 *   <li><b>Device special case</b>: path is exactly {@code Device.patient} (deliberate spec
	 *       override, hapi-fhir issue #6536);</li>
	 *   <li><b>Narrowed or aliased base SP</b>: the patient SP covers the same field as another SP
	 *       on the same resource — either narrowing it (a segment of its path equals
	 *       {@code <base path>.where(resolve() is Patient)}, e.g. Observation.patient narrows
	 *       Observation.subject) or aliasing it exactly (identical path segment, e.g. R5
	 *       Coverage.patient aliases Coverage.beneficiary) — and that base SP itself declares
	 *       Patient compartment membership.</li>
	 * </ul>
	 *
	 * and never when the resource's "patient" SP is security-excluded via
	 * {@link SearchParameterUtil#RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT}
	 * (e.g. List.patient, hapi-fhir issue #7118).
	 *
	 * <p>This catches over-adds such as: weakening the {@code .where(resolve() is Patient)} path
	 * gate, or a future resource whose patient SP narrows a non-member base SP while an unrelated
	 * SP on the same resource provides Patient membership.
	 *
	 * <p>Declared memberships must also never be dropped for reference-type SPs.
	 */
//	@Test
	@ParameterizedTest
	@MethodSource("allBuildableFhirContexts")
	void testCompartmentMembershipMatchesAnnotationsForEverySearchParam(FhirContext theCtx) {
//	void testCompartmentMembershipMatchesAnnotationsForEverySearchParam() {
//		FhirContext theCtx = FhirContext.forR5Cached();
		String fhirVersion = theCtx.getVersion().getVersion().name();
		List<String> violations = new ArrayList<>();

		for (String resourceName : theCtx.getResourceTypes()) {
			@SuppressWarnings("unchecked")
			Class<? extends IBase> resourceClass =
				(Class<? extends IBase>) theCtx.getResourceDefinition(resourceName).getImplementingClass();

			for (Field field : resourceClass.getFields()) {
				SearchParamDefinition sp = field.getAnnotation(SearchParamDefinition.class);
				if (sp == null) {
					continue;
				}

				Set<String> actual =
					SearchParameterUtil.getMembershipCompartmentsForSearchParameter(resourceClass, sp);
				Set<String> declared = cleansedDeclaredCompartmentNames(sp);

				if ("reference".equalsIgnoreCase(sp.type()) && !actual.containsAll(declared)) {
					violations.add(String.format(
						"%s %s.%s: declared compartment membership dropped: declared=%s actual=%s",
						fhirVersion, resourceName, sp.name(), declared, actual));
				}

				Set<String> extras = new HashSet<>(actual);
				extras.removeAll(declared);
				if (extras.isEmpty()) {
					continue;
				}

				boolean isJustifiedPatientAlias = extras.equals(Set.of("Patient"))
					&& "patient".equalsIgnoreCase(sp.name())
					&& !SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT
						.getOrDefault(resourceName, Collections.emptySet())
						.contains("patient")
					&& ("Device.patient".equals(sp.path())
						|| coversAPatientCompartmentBaseSp(resourceName, resourceClass, sp));

				if (!isJustifiedPatientAlias) {
					violations.add(String.format(
						"%s %s.%s (path=%s): compartment membership %s is not declared in "
							+ "providesMembershipIn and is not a justified patient-alias addition",
						fhirVersion, resourceName, sp.name(), sp.path(), extras));
				}
			}
		}

		assertThat(violations).isEmpty();
	}

	private static Set<String> cleansedDeclaredCompartmentNames(SearchParamDefinition theSp) {
		return Arrays.stream(theSp.providesMembershipIn())
			.map(c -> cleanseCompartmentName(c.name()))
			.collect(Collectors.toSet());
	}
	
	// Mirrors SearchParameterUtil.getCleansedCompartmentName (private): the R5 structures
	// declare compartments as "Base FHIR compartment definition for <Name>".
	private static String cleanseCompartmentName(String theName) {
		String prefix = "Base FHIR compartment definition for ";
		return theName.startsWith(prefix) ? theName.substring(prefix.length()) : theName;
	}

	/**
	 * True if {@code thePatientSp} covers the same field as another SP on the same resource that
	 * itself declares Patient compartment membership. Two forms (segments of pipe-delimited paths
	 * are compared individually, restricted to segments of this resource):
	 * <ul>
	 *   <li><b>Narrowing</b>: a patient-SP segment equals a base-SP segment suffixed with
	 *       {@code .where(resolve() is Patient)} (e.g. Observation.patient vs Observation.subject);</li>
	 *   <li><b>Exact alias</b>: a patient-SP segment equals a base-SP segment verbatim (e.g. R5
	 *       Coverage.patient and Coverage.beneficiary both have path {@code Coverage.beneficiary};
	 *       R5 DeviceAssociation.patient and DeviceAssociation.subject share the identical
	 *       where-clause path).</li>
	 * </ul>
	 */
	private static boolean coversAPatientCompartmentBaseSp(
			String theResourceName, Class<? extends IBase> theResourceClass, SearchParamDefinition thePatientSp) {
		Set<String> patientPathSegments = pathSegments(theResourceName, thePatientSp.path());
		return Arrays.stream(theResourceClass.getFields())
			.map(f -> f.getAnnotation(SearchParamDefinition.class))
			//all sps of res type
			.filter(spd -> spd != null && !"patient".equalsIgnoreCase(spd.name()))
			// non null + not the patient SP
			.filter(spd -> cleansedDeclaredCompartmentNames(spd).contains("Patient"))
			// filter by spd's that declare patient compartment membership
			.flatMap(spd -> pathSegments(theResourceName, spd.path()).stream())
			// of those, split spd.path to list of paths
			// then path must be of type alias (base+resolve) OR patient SP path contains the base
			.anyMatch(baseSegment ->
				patientPathSegments.contains(baseSegment + ".where(resolve() is Patient)")
					|| patientPathSegments.contains(baseSegment));
	}

	/**
	 * Splits a (possibly pipe-delimited multi-resource) SP path into segments, keeping only the
	 * segments that belong to the given resource type.
	 */
	private static Set<String> pathSegments(String theResourceName, String thePath) {
		return Arrays.stream(thePath.split("\\|"))
			.map(String::trim)
			.filter(segment -> segment.startsWith(theResourceName + "."))
			.collect(Collectors.toSet());
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
