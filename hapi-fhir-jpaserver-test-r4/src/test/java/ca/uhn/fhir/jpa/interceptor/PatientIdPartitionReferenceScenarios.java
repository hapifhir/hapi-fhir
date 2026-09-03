package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptorR4Test.ALTERNATE_DEFAULT_ID;

/**
 * Transaction bundle scenarios for
 * {@code PatientIdPartitionInterceptorR4Test#testTransaction_allReferenceScenarios}: each argument set is
 * (allPartitionSearchSupported, display name, request bundle JSON, explanation — logged during the test so
 * it reaches the test output instead of staying buried in this file — per-entry expectations). When the flag
 * is off, the transaction machinery cannot fall back to an all-partitions write transaction, so only
 * entries routable purely from their own content (client-assigned Patient ids, direct references) can
 * ingest — scenarios that instead depend on pre-fetch resolution or hook-minted ids declare the rejection
 * they must produce as an optional fourth row element and run through
 * {@link RejectedWithoutAllPartitionSearch} instead of the false-mode success pass.
 */
// Created by claude-fable-5
class PatientIdPartitionReferenceScenarios implements ArgumentsProvider {

	/**
	 * Rejection for bundles whose first unroutable entry is an id-less Patient: without the
	 * all-partitions fallback the hook never gets a chance to mint or stamp an id.
	 */
	private static String allPartitionSearchOffModeRejectIdlessPatient() {
		return "HAPI-1321: Patient resource IDs must be client-assigned in patient compartment mode, "
				+ "or server id strategy must be UUID";
	}

	/**
	 * Rejection for bundles whose first unroutable entry is a referencer whose reference (inline
	 * match URL or urn placeholder) is only resolvable after pre-fetch.
	 */
	private static String allPartitionSearchOffModeRejectNoCompartment(String theResourceType) {
		return "HAPI-1326: Resource of type " + theResourceType
				+ " has no values placing it in the Patient compartment";
	}

	/**
	 * Creates the pre-existing resources every scenario row assumes, the drivers run this before each
	 * scenario.
	 */
	static void createTestFixture(ITestDataBuilder theTestData) {
		theTestData.createPatient(
				theTestData.withId("pat1"),
				theTestData.withIdentifier("old-sys", "existingPat1Ident1"),
				theTestData.withIdentifier("new-sys", "existingPat1Ident2"));
		theTestData.createPatient(
				theTestData.withId("pat2"), theTestData.withIdentifier("old-sys", "existingPat2Ident1"));
		theTestData.createObservation(
				theTestData.withId("obsFix"),
				theTestData.withSubject("Patient/pat1"),
				theTestData.withIdentifier("observation-system", "obsExisting"));
		theTestData.createEncounter(
				theTestData.withId("encFix"),
				theTestData.withSubject("Patient/pat1"),
				theTestData.withIdentifier("enc-sys", "encExisting"));
	}

	/** How an entry's stored partition is derived and checked. */
	// Created by Claude Fable 5
	enum PartitionExpectation {
		/** Exact partition id, computed at provider time ({@code expectedPartition}). */
		EXACT,
		/** Same partition as the response entry at {@code samePartitionAsEntryIndex}. */
		SAME_AS_ENTRY,
		/** Compartment of the resource's own server-assigned id: hash of the response location's id part. */
		OWN_ID,
		/** Compartment of the stored resource's own subject: hash of the subject reference's id part. */
		OWN_SUBJECT
	}

	/**
	 * Expectation for a single transaction response entry (in input order). {@code partitionExpectation}
	 * selects how the stored partition is derived; {@code expectedPartition} and
	 * {@code samePartitionAsEntryIndex} feed the {@code EXACT} and {@code SAME_AS_ENTRY} modes respectively.
	 * {@code createdPlaceholderType} non-null means the entry's outcome must report exactly one auto-created
	 * placeholder of that type; null means it must report none.
	 */
	record ExpectedEntry(
			String resourceType,
			StorageResponseCodeEnum outcome,
			PartitionExpectation partitionExpectation,
			Integer expectedPartition,
			Integer samePartitionAsEntryIndex,
			String createdPlaceholderType) {

		/** This entry's outcome must report exactly one auto-created placeholder of the given type. */
		ExpectedEntry reportingCreatedPlaceholder(String thePlaceholderType) {
			return new ExpectedEntry(
					resourceType,
					outcome,
					partitionExpectation,
					expectedPartition,
					samePartitionAsEntryIndex,
					thePlaceholderType);
		}
	}

	/** Resource in the configured default partition (ALTERNATE_DEFAULT_ID = -1). */
	static ExpectedEntry inDefaultPartition(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(
				theType, theOutcome, PartitionExpectation.EXACT, ALTERNATE_DEFAULT_ID, null, null);
	}

	/** Resource in the compartment of the patient whose id-part is {@code thePatientIdPart}. */
	static ExpectedEntry inCompartmentOf(String theType, StorageResponseCodeEnum theOutcome, String thePatientIdPart) {
		int partition = PatientIdPartitionInterceptor.defaultPartitionAlgorithm(thePatientIdPart);
		return new ExpectedEntry(theType, theOutcome, PartitionExpectation.EXACT, partition, null, null);
	}

	/**
	 * Resource in the compartment of {@code thePatientIdPart}, which must hash to a different partition than
	 * {@code theOtherPatientIdPart} — pins a genuinely cross-compartment placement. Fails at provider time if
	 * the two ids collide, so a future id change cannot silently void the cross-compartment claim.
	 */
	// Created by Claude Fable 5
	static ExpectedEntry inCompartmentOfDistinctFrom(
			String theType, StorageResponseCodeEnum theOutcome, String thePatientIdPart, String theOtherPatientIdPart) {
		int partition = PatientIdPartitionInterceptor.defaultPartitionAlgorithm(thePatientIdPart);
		int otherPartition = PatientIdPartitionInterceptor.defaultPartitionAlgorithm(theOtherPatientIdPart);
		// otherPartition feeds only this guard: once the two ids are known to hash apart, asserting the
		// exact partition below already implies "not in the other patient's partition".
		if (partition == otherPartition) {
			throw new IllegalArgumentException("Ids '%s' and '%s' hash to the same partition (%d); pick a different id"
					.formatted(thePatientIdPart, theOtherPatientIdPart, partition));
		}
		return new ExpectedEntry(theType, theOutcome, PartitionExpectation.EXACT, partition, null, null);
	}

	/**
	 * Resource must co-locate with the response entry at {@code theOtherEntryIndex}. Complementary to
	 * {@link #inCompartmentOfOwnSubject}: co-location pins that the resource followed the intended entry,
	 * while own-subject pins correct placement for whatever the resource ended up referencing.
	 */
	static ExpectedEntry inSamePartitionAsEntry(String theType, StorageResponseCodeEnum theOutcome, int theOtherEntryIndex) {
		return new ExpectedEntry(theType, theOutcome, PartitionExpectation.SAME_AS_ENTRY, null, theOtherEntryIndex, null);
	}

	/**
	 * Resource in the compartment of its own server-assigned id — the partition must equal the hash of the
	 * id part in the entry's response location. For Patient entries whose id is minted during the
	 * transaction and so unknowable at provider time.
	 */
	// Created by Claude Fable 5
	static ExpectedEntry inCompartmentOfSelf(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, PartitionExpectation.OWN_ID, null, null, null);
	}

	/**
	 * Resource in the compartment of its own stored subject — the partition must equal the hash of the id
	 * part of the subject reference read back from the stored resource. For compartment resources whose
	 * patient's id is minted during the transaction and so unknowable at provider time.
	 */
	// Created by Claude Fable 5
	static ExpectedEntry inCompartmentOfOwnSubject(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, PartitionExpectation.OWN_SUBJECT, null, null, null);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
		// False mode only re-runs the scenarios whose expectations hold without the all-partitions
		// fallback; the annotated rest are covered by RejectedWithoutAllPartitionSearch.
		return Stream.of(true, false).flatMap(supported -> scenarios()
				.map(Arguments::get)
				.filter(args -> supported || args.length == 4)
				.map(args -> Arguments.of(supported, args[0], args[1], args[2], args[3])));
	}

	/**
	 * The scenarios that declare a false-mode rejection, as (display name, request bundle JSON, explanation,
	 * expected error) — for
	 * {@code PatientIdPartitionInterceptorR4Test#testTransaction_allReferenceScenarios_rejectedWithoutAllPartitionSearch}.
	 */
	// Created by Claude Fable 5
	static class RejectedWithoutAllPartitionSearch implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
			return scenarios()
					.map(Arguments::get)
					.filter(args -> args.length > 4)
					.map(args -> Arguments.of(args[0], args[1], args[2], args[4]));
		}
	}

	/**
	 * Bundles rejected even when all-partition search is supported, as (display name, request bundle JSON,
	 * explanation, expected error) — for
	 * {@code PatientIdPartitionInterceptorR4Test#testTransaction_allReferenceScenarios_rejectedWithAllPartitionSearch}.
	 * These rejections roll back completely.
	 */
	// Created by Claude Fable 5
	static class RejectedWithAllPartitionSearch implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
			return Stream.of(
				Arguments.of(
					"Create Patient + Observation | unconditional twin with a matching inline match URL, no existing match",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [ { "system" : "old-sys", "value" : "unmatchedTwinPatient" } ]
									},
									"request" : { "method" : "POST", "url" : "Patient" }
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef" } ],
										"subject" : { "reference" : "Patient?identifier=old-sys|unmatchedTwinPatient" }
									},
									"request" : { "method" : "POST", "url" : "Observation" }
								}
							]
						}
						""",
					"An unconditional POST never binds an inline match URL (references resolve against the store, not entry bodies), so the synthetic conditional create writes a placeholder — and the just-written twin is a second match for the conditional URL, which the post-write duplicate guard rejects. The client's remedies are a urn reference to the twin or a conditional create; the matched variant of this shape is a success scenario (Observation → existing patient).",
					"HAPI-0542: Unable to process Transaction - Request would cause multiple resources to match URL: "
							+ "\"Patient?identifier=old-sys|unmatchedTwinPatient\". Does transaction request contain duplicates?"
				),
				Arguments.of(
					"Create Patient + Observation | multi-identifier inline match URL",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [
											{ "system" : "old-sys", "value" : "multiGroupPatientIdent1" },
											{ "system" : "new-sys", "value" : "multiGroupPatientIdent2" }
										]
									},
									"request" : { "method" : "POST", "url" : "Patient" }
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef" } ],
										"subject" : { "reference" : "Patient?identifier=old-sys|multiGroupPatientIdent1&identifier=new-sys|multiGroupPatientIdent2" }
									},
									"request" : { "method" : "POST", "url" : "Observation" }
								}
							]
						}
						""",
					"The normalizer only supports single-identifier inline match URLs; a multi-and-group URL is rejected up front (like any other unsupported search parameter shape), before anything is written — even when an in-bundle patient carries both identifiers.",
					"HAPI-3025: Inline match URL matching only supports a single identifier in patient id partition mode: "
							+ "Patient?identifier=old-sys|multiGroupPatientIdent1&identifier=new-sys|multiGroupPatientIdent2"
				),
				// TODO-TG: Add support to inline match url non-patient placeholder creation.
				Arguments.of(
					"Create Patient + Observation | inline match URL to a nonexistent Encounter",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"fullUrl" : "urn:uuid:bbbb2222-2222-2222-2222-222222222222",
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [ { "system" : "old-sys", "value" : "newPatientWithEncUrl" } ]
									},
									"request" : { "method" : "POST", "url" : "Patient" }
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithDanglingEncUrl" } ],
										"subject" : { "reference" : "urn:uuid:bbbb2222-2222-2222-2222-222222222222" },
										"encounter" : { "reference" : "Encounter?identifier=enc-sys|danglingEnc" }
									},
									"request" : { "method" : "POST", "url" : "Observation" }
								}
							]
						}
						""",
					"The synthetic conditional create minted for a non-Patient compartment type carries only the match URL's identifier — no subject — so it cannot be routed to a compartment. Outside patient id partition mode the same bundle succeeds through the resolver's placeholder path; the equivalent direct-reference shape also succeeds in this mode (the auto-create-placeholder hook stamps the referencer's compartment). Candidate fix, tracked separately: give synthetic compartment-type placeholders their first referencer's subject.",
					"HAPI-1326: Resource of type Encounter has no values placing it in the Patient compartment"
				),
				Arguments.of(
					"Create Observation | inline match URL to an existing Encounter",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithEncUrl" } ],
										"subject" : { "reference" : "Patient/pat1" },
										"encounter" : { "reference" : "Encounter?identifier=enc-sys|encExisting" }
									},
									"request" : { "method" : "POST", "url" : "Observation" }
								}
							]
						}
						""",
					"Even a MATCHED non-Patient inline match URL fails: create-time partition determination runs before the conditional match can no-op, and the subject-less synthetic Encounter cannot be routed. (Patient synthetics survive the same ordering only because the UUID id strategy pre-assigns an id the Patient branch routes by.) Same candidate fix as the nonexistent-Encounter case.",
					"HAPI-1326: Resource of type Encounter has no values placing it in the Patient compartment"
				)
			);
		}
	}

	private static Stream<Arguments> scenarios() {
		// "two conditional-create Patients with the same identifier" is covered by the
		// testTransaction_*InBundle_dedup tests in PatientIdPartitionInterceptorR4Test.
		return Stream.of(
			// --- Patient-only bundles ---
			Arguments.of(
				"Create Patient | new patient, server-assigned UUID id",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				"The hook rewrites the POST to a direct PUT with a minted UUID id; the restored outcome is a plain create.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient | no match → created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}
						]
					}
					""",
				"Rewritten to a conditional PUT with a minted body id; the restored outcome is the POST-origin code.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient | matches existing → no-op create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}
						]
					}
					""",
				"Matched conditional POSTs are left untouched → native no-op create outcome against pat1.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Update Patient | matches existing, identical body → no-change update",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"}, { "system" : "new-sys", "value" : "existingPat1Ident2"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}
						]
					}
					""",
				"Matched conditional PUT stays conditional (matched id stamped on the body) → native no-change outcome.",
				List.of(
					inCompartmentOf(
						"Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Update Patient | explicit-id PUT to existing patient stays a plain update",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat1",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat1"}
							}
						]
					}
					""",
				"Non-rewritten direct PUT-by-id: the restore hook must leave it a plain update, not a create.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE, "pat1")
				)
			),
			Arguments.of(
				"Update Patient | explicit-id PUT with unchanged body stays a no-change update",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat2",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat2Ident1"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat2"}
							}
						]
					}
					""",
				"Identical to the stored pat2: a no-change update; the restore hook must preserve the no-change code.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE, "pat2")
				)
			),

			// --- Referencers only; target patient not in the bundle ---
			Arguments.of(
				"Create Observation | direct reference to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithDirectRef"} ],
									"subject" : { "reference" : "Patient/pat1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Direct Patient/pat1 reference → Observation in pat1's compartment. No normalizer involved.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Create Observation | inline match URL reference to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Inline match URL → normalizer prepends synthetic conditional-create (pat1 exists → NOP). 1 synthetic stripped; response has 1 entry. Observation in pat1's compartment.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Observation | inline match URL reference to a new patient → synthetic conditional create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=new-sys|syntheticCreatePatient" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Inline match URL → synthetic conditional-create for new-sys|syntheticCreatePatient (doesn't exist → creates with UUID). 1 synthetic stripped; response has 1 entry. Observation in the new patient's compartment.",
				List.of(
					inCompartmentOfOwnSubject("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
							.reportingCreatedPlaceholder("Patient")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Observation ×2 | inline match URL references to two existing patients, one partition slice each",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsForPat1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsForPat2"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat2Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Two inline match URLs → two synthetics prepended (both NOP: pat1 and pat2 exist). Both stripped. obsForPat1 → pat1's compartment; obsForPat2 → pat2's compartment.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat2")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Observation ×2 | same existing patient via two different identifiers → two synthetics, co-located",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsViaIdent1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsViaIdent2"} ],
									"subject" : { "reference" : "Patient?identifier=new-sys|existingPat1Ident2" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Distinct index keys → two synthetics, both match pat1 (no-op, stripped) → co-located, no duplicate.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Update Observation | inline match URL reference to existing patient, no obs match → created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1"}
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs"}
							}
						]
					}
					""",
				"Inline match URL → synthetic for pat1 (NOP). 1 synthetic stripped. Conditional PUT Observation: condUpdateObs doesn't exist → creates new.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Update Observation | direct patient reference inside the match URL itself",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference" : "Patient/pat1" },
									"code" : { "coding" : [{ "system" : "http://loinc.org", "code" : "9999-9" }] }
								},
								"request" : { "method" : "PUT", "url" : "Observation?subject=Patient/pat1&code=http://loinc.org|9999-9"}
							}
						]
					}
					""",
				"Observation subject = Patient/pat1 (direct reference, no inline match URL). No match found → creates new Observation in pat1's compartment.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"Conditionally Create Observation | inline match URL subject, no observation match → created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs"}
							}
						]
					}
					""",
				"The ifNoneExist URL pre-fetches while the subject is still an inline match URL; the synthetic for pat1 NOPs and is stripped. No observation matches → conditional create in pat1's compartment.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Observation | inline match URL subject, matches existing observation → no-op",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsExisting"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|obsExisting"}
							}
						]
					}
					""",
				"The ifNoneExist URL matches the fixture Observation in pat1's compartment → no-op create.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Update Observation | matches existing observation, changed body → updated in place",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsExisting"} ],
									"status" : "final",
									"subject" : { "reference" : "Patient/pat1" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsExisting"}
							}
						]
					}
					""",
				"The conditional PUT matches the fixture Observation, resolved against its existing partition.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"Conditionally Update Observation | matches existing observation, identical body → no-change update",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsExisting"} ],
									"subject" : { "reference" : "Patient/pat1" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsExisting"}
							}
						]
					}
					""",
				"Body identical to the fixture Observation → native no-change conditional-match outcome.",
				List.of(
					inCompartmentOf(
						"Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE, "pat1")
				)
			),
			Arguments.of(
				"Create Observation | direct reference to a nonexistent patient id → placeholder auto-created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithPlaceholderRef"} ],
									"subject" : { "reference" : "Patient/patNewDirect" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"The plain auto-placeholder shape: the id routes the entry, DaoResourceLinkResolver mints the placeholder Patient in the same compartment, and the response stays a single entry.",
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "patNewDirect")
							.reportingCreatedPlaceholder("Patient")
				)
			),
			Arguments.of(
				"Create Patient + Observation | direct reference to a nonexistent Encounter → non-Patient placeholder in the patient's compartment",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:aaaa1111-1111-1111-1111-111111111111",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatientWithEnc" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithDanglingEnc" } ],
									"subject" : { "reference" : "urn:uuid:aaaa1111-1111-1111-1111-111111111111" },
									"encounter" : { "reference" : "Encounter/enc-dangling" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				"A dangling direct reference to a non-Patient compartment type: DaoResourceLinkResolver mints the placeholder Encounter and the auto-create-placeholder hook stamps the referencing Observation's compartment onto it, so patient, observation, and placeholder all co-locate.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
							.reportingCreatedPlaceholder("Encounter")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Create Encounter + Observation | shared inline match URL → one deduped synthetic, new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Encounter",
									"status" : "finished",
									"class" : {
										"system" : "http://terminology.hl7.org/CodeSystem/v3-ActCode",
										"code" : "AMB",
										"display" : "ambulatory"
									},
									"subject" : { "reference" : "Patient?identifier=old-sys|sharedMatchUrlPatient" }
								},
								"request" : { "method" : "POST", "url" : "Encounter"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|sharedMatchUrlPatient" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Both inline match URLs → one shared synthetic (de-duplicated by normalizer). sharedMatchUrlPatient doesn't exist → creates with UUID. 1 synthetic stripped; response has 2 entries. Both in the new patient's compartment; only the first referencer reports the created placeholder.",
				List.of(
					inCompartmentOfOwnSubject("Encounter", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
							.reportingCreatedPlaceholder("Patient"),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectNoCompartment("Encounter")
			),

			// --- Patient + referencer, placeholder (urn) references ---
			Arguments.of(
				"Create Patient + Observation | placeholder reference",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"The hook assigns the patient a minted UUID id and substitutes the urn subject → same compartment.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Create Observation + Patient | placeholder reference, patient entry second",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
							    "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				"Input order [Obs, Patient]; response preserves order.",
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient + Observation | placeholder reference, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Patient conditional create: condCreatePatient doesn't exist → creates with server-assigned UUID.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Observation + Conditionally Create Patient | placeholder reference, patient entry second",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							},
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}
						]
					}
					""",
				"Patient conditional create: condCreatePatient doesn't exist → creates with server-assigned UUID. Input order preserved in response: [0]=Observation, [1]=Patient.",
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient + Observation | placeholder reference, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
							    "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Patient conditional create: existingPat1Ident1=pat1 exists → NOP (200 OK). The post-preFetch hook substitutes the Observation's urn subject → Patient/pat1 before create.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Patient + Conditionally Update Observation | placeholder reference",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:c1111111-1111-1111-1111-111111111111",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs" } ],
									"subject" : { "reference" : "urn:uuid:c1111111-1111-1111-1111-111111111111" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs" }
							}
						]
					}
					""",
				"The hook assigns the unconditional patient an id and substitutes the urn ref → Observation routes to its compartment.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | placeholder reference",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:c1222222-2222-2222-2222-222222222222",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs" } ],
									"subject" : { "reference" : "urn:uuid:c1222222-2222-2222-2222-222222222222" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs" }
							}
						]
					}
					""",
				"The conditional patient has no id at routing time (allPartitions fallback); the hook resolves the urn ref after preFetch.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | placeholder reference, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:cd0a1111-1111-1111-1111-111111111111",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condUpdatePatient"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|condUpdatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:cd0a1111-1111-1111-1111-111111111111" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Conditional PUT with no match → created with a minted id; the urn subject substitutes to it.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | placeholder reference, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:cd0b2222-2222-2222-2222-222222222222",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:cd0b2222-2222-2222-2222-222222222222" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Conditional PUT matches pat1 → update; the urn subject substitutes to the matched id.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Create Observation | placeholder reference, neither matches",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:e1533333-3333-3333-3333-333333333333",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:e1533333-3333-3333-3333-333333333333" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs"}
							}
						]
					}
					""",
				"The Observation's ifNoneExist URL pre-fetches while its subject is still the urn placeholder; both conditional creates find no match.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Update Patient + Observation | urn fullUrl on explicit-id PUT to existing patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:f2644444-4444-4444-4444-444444444444",
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat1",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:f2644444-4444-4444-4444-444444444444" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"The urn fullUrl differs from the explicit-id PUT url → the hook substitutes urn → Patient/pat1 without rewriting the entry; the update itself stays native.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Update-as-create Patient + Observation | urn fullUrl on explicit-id PUT with a new client id",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:a3755555-5555-5555-5555-555555555555",
								"resource" : {
									"resourceType" : "Patient",
									"id" : "patUacUrn",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/patUacUrn"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:a3755555-5555-5555-5555-555555555555" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Same substitution branch, update-as-create flavor: patUacUrn doesn't exist → created with the client id; the urn subject resolves to its deterministic compartment.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE, "patUacUrn"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "patUacUrn")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),

			// --- Placeholder (urn) references inside conditional URLs ---
			Arguments.of(
				"Conditionally Create Patient + Conditionally Create Observation | placeholder reference inside the observation's ifNoneExist, neither matches",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10001-0000-0000-0000-000000000001",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10001-0000-0000-0000-000000000001" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs&subject=urn:uuid:b7c10001-0000-0000-0000-000000000001"}
							}
						]
					}
					""",
				"The Observation's ifNoneExist embeds the Patient entry's placeholder fullUrl: the placeholder must resolve to the minted patient id before the conditional-create match search and the post-write URL verification run. Neither conditional matches → both create, co-located.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Create Patient + Conditionally Create Observation | placeholder reference inside the observation's ifNoneExist",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10002-0000-0000-0000-000000000002",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10002-0000-0000-0000-000000000002" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs&subject=urn:uuid:b7c10002-0000-0000-0000-000000000002"}
							}
						]
					}
					""",
				"Same conditional-URL placeholder, but the Patient is an unconditional create: the minted id must reach the Observation's ifNoneExist too.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Create Observation | placeholder in the observation's ifNoneExist, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10003-0000-0000-0000-000000000003",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10003-0000-0000-0000-000000000003" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs&subject=urn:uuid:b7c10003-0000-0000-0000-000000000003"}
							}
						]
					}
					""",
				"The Patient conditional create NOPs to pat1, so the placeholder in the Observation's ifNoneExist must resolve to Patient/pat1; the Observation conditional finds no match → created in pat1's compartment.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Create Observation | placeholder in the observation's ifNoneExist, both match existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10004-0000-0000-0000-000000000004",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsExisting"} ],
									"subject" : { "reference" : "urn:uuid:b7c10004-0000-0000-0000-000000000004" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|obsExisting&subject=urn:uuid:b7c10004-0000-0000-0000-000000000004"}
							}
						]
					}
					""",
				"With the placeholder resolved to Patient/pat1, the Observation's ifNoneExist matches the fixture Observation → native no-op conditional-match outcomes for both entries.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | placeholder inside the conditional update URL, no observation match",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10005-0000-0000-0000-000000000005",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10005-0000-0000-0000-000000000005" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs&subject=urn:uuid:b7c10005-0000-0000-0000-000000000005"}
							}
						]
					}
					""",
				"The placeholder sits in the conditional update URL rather than ifNoneExist; no observation matches → created in the new patient's compartment.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Observation + Conditionally Create Patient | placeholder in the observation's ifNoneExist, patient entry second",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10006-0000-0000-0000-000000000006" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs&subject=urn:uuid:b7c10006-0000-0000-0000-000000000006"}
							}, {
								"fullUrl" : "urn:uuid:b7c10006-0000-0000-0000-000000000006",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}
						]
					}
					""",
				"Same as the ifNoneExist-placeholder scenario but with the referencer first: processing order is determined by the sorter, not entry order, and the response must preserve input order.",
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, 1),
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Create Observation | percent-escaped placeholder in the observation's ifNoneExist",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10007-0000-0000-0000-000000000007",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condCreateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10007-0000-0000-0000-000000000007" }
								},
								"request" : { "method" : "POST", "url" : "Observation", "ifNoneExist" : "Observation?identifier=observation-system|condCreateObs&subject=urn%3Auuid%3Ab7c10007-0000-0000-0000-000000000007"}
							}
						]
					}
					""",
				"The placeholder in ifNoneExist is percent-escaped (urn%3Auuid%3A...), a form performIdSubstitutionsInMatchUrl explicitly supports; must behave exactly like the raw form.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | placeholder in the update URL, patient and observation match existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10008-0000-0000-0000-000000000008",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsExisting"} ],
									"status" : "final",
									"subject" : { "reference" : "urn:uuid:b7c10008-0000-0000-0000-000000000008" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsExisting&subject=urn:uuid:b7c10008-0000-0000-0000-000000000008"}
							}
						]
					}
					""",
				"Placeholder in the conditional update URL resolves to Patient/pat1 and the URL matches the fixture Observation; the changed body updates it in place.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Create Patient + Conditionally Update Observation | placeholder inside the conditional update URL",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c10009-0000-0000-0000-000000000009",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs"} ],
									"subject" : { "reference" : "urn:uuid:b7c10009-0000-0000-0000-000000000009" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs&subject=urn:uuid:b7c10009-0000-0000-0000-000000000009"}
							}
						]
					}
					""",
				"Unconditional patient create + placeholder inside the conditional update URL; no observation matches → created alongside the new patient.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient (bare spec-form ifNoneExist) + Observation | placeholder reference, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c1000a-0000-0000-0000-00000000000a",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:b7c1000a-0000-0000-0000-00000000000a" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"FHIR allows If-None-Exist to omit the type prefix; such bare URLs are never pre-fetched, so the machinery must not treat 'not pre-fetched' as 'no match' — the conditional create must still NOP to pat1 and the referencer must follow it.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient (leading-? ifNoneExist) + Observation | placeholder reference, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c1000c-0000-0000-0000-00000000000c",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
									"subject" : { "reference" : "urn:uuid:b7c1000c-0000-0000-0000-00000000000c" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"The leading-? form is a type-less variant the storage layer also accepts; like the bare form it must resolve to the existing patient rather than being treated as unmatched.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient ×2 (duplicate conditional URLs, distinct placeholders) + Observation ×2 | consolidated to one create, all co-located",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:b7c1000b-0000-0000-0000-000000000b01",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "dupUrnPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|dupUrnPatient"}
							}, {
								"fullUrl" : "urn:uuid:b7c1000b-0000-0000-0000-000000000b02",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "dupUrnPatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|dupUrnPatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsViaDup1"} ],
									"subject" : { "reference" : "urn:uuid:b7c1000b-0000-0000-0000-000000000b01" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsViaDup2"} ],
									"subject" : { "reference" : "urn:uuid:b7c1000b-0000-0000-0000-000000000b02" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Two conditional creates sharing one match URL but carrying distinct placeholder fullUrls consolidate to a single create; references to either placeholder resolve to the one created patient.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),

			// --- Patient + referencer, inline match URL references ---
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | inline match URL binds to in-bundle entry, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs"}
							}
						]
					}
					""",
				"Normalizer rewrites Obs subject (inline match URL) using Patient conditional-create entry's fullUrl. Patient: NOP (existingPat1Ident1=pat1 exists). Obs: PUT no match → creates new.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | inline match URL binds to in-bundle entry, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "inBundlePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|inBundlePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "condUpdateObs"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|inBundlePatient" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|condUpdateObs"}
							}
						]
					}
					""",
				"Normalizer rewrites Obs subject using Patient conditional-create entry's fullUrl. Patient creates new (inBundlePatient doesn't exist). Obs conditional PUT: condUpdateObs doesn't exist → creates.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Create Observation + Conditionally Create Patient | inline match URL binds to the later in-bundle patient entry",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|inBundlePatient" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "inBundlePatient"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|inBundlePatient"}
							}
						]
					}
					""",
				"The identifier index is order-independent: the inline ref binds to the later Patient entry, no synthetic.",
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | inline match URL binds to in-bundle entry, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|existingPat1Ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Normalizer rewrites Obs subject using Patient conditional-update entry's fullUrl. Patient PUT matches pat1 → update (200). Obs in pat1's compartment.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | inline match URL binds to in-bundle entry, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "inBundlePatient"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|inBundlePatient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|inBundlePatient" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Patient PUT: inBundlePatient doesn't exist → creates with server-assigned UUID. Obs references it.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Create Patient + Observation | inline match URL matches the existing patient, not the identical in-bundle twin",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				"References resolve against the store, never against entry bodies: the synthetic conditional create matches pat1 (no-op) and the Observation lands in pat1's compartment. The unconditional POST still creates a second patient carrying pat1's identifier, in its own compartment.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectIdlessPatient()
			),
			Arguments.of(
				"Conditionally Create Patient (body without the URL identifier) + Observation | inline match URL binds the same-URL conditional entry, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : { "resourceType" : "Patient" },
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|existingPat1Ident1" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				"Same conditional-URL binding; existingPat1Ident1 = pat1 exists → the conditional create no-ops and both entries land in pat1's compartment.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient ×2 (duplicates) + Observation | placeholder notice survives the consolidated response",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "dupCondPatient" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|dupCondPatient" }
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "dupCondPatient" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|dupCondPatient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef" } ],
									"subject" : { "reference" : "Patient?identifier=new-sys|unrelatedNewPatient" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				"The duplicate conditional create consolidates away — the response has one entry fewer than the request, shifting later response positions — and the placeholder-created notice must still land on the Observation.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inCompartmentOfOwnSubject("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
							.reportingCreatedPlaceholder("Patient")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Conditionally Create Patient ×2 + Observation ×2 | each observation follows its own new patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatientA"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newPatientA"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatientB"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newPatientB"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsForPatientA"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newPatientA" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsForPatientB"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newPatientB" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Normalizer rewrites ObsA/ObsB subjects using PatA/PatB fullUrls. Both patients created new. All 4 entries remain in response. Cross-partition writes land in each patient's own compartment.",
				List.of(
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1)
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			),
			Arguments.of(
				"Update Patient + Observation | update-as-create via new client id, direct reference",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat-uac",
									"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat-uac"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithDirectRef"} ],
									"subject" : { "reference" : "Patient/pat-uac" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"pat-uac doesn't exist → explicit-id PUT creates it (update-as-create); direct ref co-locates.",
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE, "pat-uac"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat-uac")
				)
			),

			// --- Non-compartment resources ---
			Arguments.of(
				"Create Organization | non-compartment resource → default partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "newOrg"} ],
									"name" : "Acme Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}
						]
					}
					""",
				"Organization is non-compartment → goes to default partition (-1 = ALTERNATE_DEFAULT_ID).",
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Create Organization + Observation | non-compartment and compartment entries mixed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "newOrg"} ],
									"name" : "Mixed Bundle Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsWithMatchUrlRef"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|existingPat1Ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				"Organization → default partition. Obs inline match URL → synthetic (pat1 NOP); 1 stripped. Obs in pat1's compartment.",
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				),
				allPartitionSearchOffModeRejectNoCompartment("Observation")
			)
		);
	}
}
