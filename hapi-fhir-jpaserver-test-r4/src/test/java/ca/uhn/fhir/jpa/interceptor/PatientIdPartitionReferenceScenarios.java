package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptorR4Test.ALTERNATE_DEFAULT_ID;

/**
 * Transaction bundle scenarios for
 * {@code PatientIdPartitionInterceptorR4Test#testTransaction_allReferenceScenarios}: each argument set is
 * (display name, request bundle JSON, per-entry expectations).
 */
// Created by claude-fable-5
class PatientIdPartitionReferenceScenarios implements ArgumentsProvider {

	/**
	 * Expectation for a single transaction response entry (in input order).
	 * The two nullable partition fields encode three cases:
	 * <ul>
	 *   <li>{@code expectedPartition} (non-null) — exact partition id</li>
	 *   <li>{@code sameAsEntryIndex} (non-null) — must co-locate with that response entry</li>
	 *   <li>both null — "any compartment", partition must be {@code > 0}</li>
	 * </ul>
	 */
	record ExpectedEntry(
			String resourceType,
			StorageResponseCodeEnum outcome,
			Integer expectedPartition,
			Integer sameAsEntryIndex) {}

	/** Resource in the configured default partition (ALTERNATE_DEFAULT_ID = -1). */
	static ExpectedEntry inDefaultPartition(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, ALTERNATE_DEFAULT_ID, null);
	}

	/** Resource in the compartment of the patient whose id-part is {@code thePatientIdPart}. */
	static ExpectedEntry inCompartmentOf(String theType, StorageResponseCodeEnum theOutcome, String thePatientIdPart) {
		int partition = PatientIdPartitionInterceptor.defaultPartitionAlgorithm(thePatientIdPart);
		return new ExpectedEntry(theType, theOutcome, partition, null);
	}

	/** Resource must co-locate with the response entry at {@code theOtherEntryIndex}. */
	static ExpectedEntry inSamePartitionAsEntry(String theType, StorageResponseCodeEnum theOutcome, int theOtherEntryIndex) {
		return new ExpectedEntry(theType, theOutcome, null, theOtherEntryIndex);
	}

	/** Resource must be in some patient-compartment partition (partition {@code > 0}). */
	static ExpectedEntry inAnyCompartment(String theType, StorageResponseCodeEnum theOutcome) {
		return new ExpectedEntry(theType, theOutcome, null, null);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
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
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// The hook rewrites the POST to a direct PUT with a minted UUID id; the restored outcome is a plain create.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Conditionally Create Patient | no match → created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}
						]
					}
					""",
				// Rewritten to a conditional PUT with a minted body id; the restored outcome is the POST-origin code.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"Conditionally Create Patient | matches existing → no-op create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}
						]
					}
					""",
				// Matched conditional POSTs are left untouched → native no-op create outcome.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"Conditionally Update Patient | matches existing, identical body → no-change update",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"}, { "system" : "new-sys", "value" : "newId1"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|ident1"}
							}
						]
					}
					""",
				// Matched conditional PUT stays conditional (matched id stamped on the body) → native no-change outcome.
				List.of(
					inCompartmentOf(
						"Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE, "pat1")
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat1"}
							}
						]
					}
					""",
				// Non-rewritten direct PUT-by-id: the restore hook must leave it a plain update, not a create.
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
									"identifier" : [ { "system" : "old-sys", "value" : "ident2"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat2"}
							}
						]
					}
					""",
				// Identical to the stored pat2: a no-change update; the restore hook must preserve the no-change code.
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
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient/pat1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Direct Patient/pat1 reference → Observation in pat1's compartment. No normalizer involved.
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
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Inline match URL → normalizer prepends synthetic conditional-create (pat1 exists → NOP).
				// 1 synthetic stripped; response has 1 entry. Observation in pat1's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Create Observation | inline match URL reference to a new patient → synthetic conditional create",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=new-sys|new-val" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Inline match URL → synthetic conditional-create for new-sys|new-val (doesn't exist → creates with UUID).
				// 1 synthetic stripped; response has 1 entry. Observation in the new patient's compartment.
				List.of(
					inAnyCompartment("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Create Observation ×2 | inline match URL references to two existing patients, cross-partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs2"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident2" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Two inline match URLs → two synthetics prepended (both NOP: pat1 and pat2 exist). Both stripped.
				// obs1 → pat1's compartment; obs2 → pat2's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat2")
				)
			),
			Arguments.of(
				"Create Observation ×2 | same existing patient via two different identifiers → two synthetics, co-located",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsTwoIdentA"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsTwoIdentB"} ],
									"subject" : { "reference" : "Patient?identifier=new-sys|newId1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Distinct index keys → two synthetics, both match pat1 (no-op, stripped) → co-located, no duplicate.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Conditionally Update Observation | inline match URL reference to existing patient, no obs match → created",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1"}
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obs1"}
							}
						]
					}
					""",
				// Inline match URL → synthetic for pat1 (NOP). 1 synthetic stripped.
				// Conditional PUT Observation: obs1 doesn't exist → creates new.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
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
				// Observation subject = Patient/pat1 (direct reference, no inline match URL).
				// No match found → creates new Observation in pat1's compartment.
				List.of(
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
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
									"subject" : { "reference" : "Patient?identifier=old-sys|identChain" }
								},
								"request" : { "method" : "POST", "url" : "Encounter"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsChain"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|identChain" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Both inline match URLs → one shared synthetic (de-duplicated by normalizer). identChain doesn't exist → creates with UUID.
				// 1 synthetic stripped; response has 2 entries. Both in the new patient's compartment.
				List.of(
					inAnyCompartment("Encounter", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// The hook assigns the patient a minted UUID id and substitutes the urn subject → same compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"Create Observation + Patient | placeholder reference, patient entry second",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
							    "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// Input order [Obs, Patient]; response preserves order.
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient conditional create: identNew doesn't exist → creates with server-assigned UUID.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"Create Observation + Conditionally Create Patient | placeholder reference, patient entry second",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							},
							{
								"fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|identNew"}
							}
						]
					}
					""",
				// Patient conditional create: identNew doesn't exist → creates with server-assigned UUID.
				// Input order preserved in response: [0]=Observation, [1]=Patient.
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient conditional create: ident1=pat1 exists → NOP (200 OK).
				// The post-preFetch hook substitutes the Observation's urn subject → Patient/pat1 before create.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "c11" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC11" } ],
									"subject" : { "reference" : "urn:uuid:c1111111-1111-1111-1111-111111111111" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC11" }
							}
						]
					}
					""",
				// The hook assigns the unconditional patient an id and substitutes the urn ref → Observation routes to its compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "c12" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|c12" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC12" } ],
									"subject" : { "reference" : "urn:uuid:c1222222-2222-2222-2222-222222222222" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC12" }
							}
						]
					}
					""",
				// The conditional patient has no id at routing time (allPartitions fallback); the hook resolves the urn ref after preFetch.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "condPutUrnNew"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|condPutUrnNew"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondPutUrnNew"} ],
									"subject" : { "reference" : "urn:uuid:cd0a1111-1111-1111-1111-111111111111" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Conditional PUT with no match → created with a minted id; the urn subject substitutes to it.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondPutUrnMatch"} ],
									"subject" : { "reference" : "urn:uuid:cd0b2222-2222-2222-2222-222222222222" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Conditional PUT matches pat1 → update; the urn subject substitutes to the matched id.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs1"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obs1"}
							}
						]
					}
					""",
				// Normalizer rewrites Obs subject (inline match URL) using Patient conditional-create entry's fullUrl.
				// Patient: NOP (ident1=pat1 exists). Obs: PUT no match → creates new.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, "pat1")
				)
			),
			Arguments.of(
				"Conditionally Create Patient + Conditionally Update Observation | inline match URL binds to in-bundle entry, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newCreate"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newCreate"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCC"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newCreate" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsCC"}
							}
						]
					}
					""",
				// Normalizer rewrites Obs subject using Patient conditional-create entry's fullUrl.
				// Patient creates new (newCreate doesn't exist). Obs conditional PUT: obsCC doesn't exist → creates.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"Create Observation + Conditionally Create Patient | inline match URL binds to the later in-bundle patient entry",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsRevBind"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|revBind" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "revBind"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|revBind"}
							}
						]
					}
					""",
				// The identifier index is order-independent: the inline ref binds to the later Patient entry, no synthetic.
				List.of(
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH)
				)
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | inline match URL binds to in-bundle entry, patient matches existing",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "ident1"} ],
									"active" : true
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|ident1"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondUpdMatched"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Normalizer rewrites Obs subject using Patient conditional-update entry's fullUrl.
				// Patient PUT matches pat1 → update (200). Obs in pat1's compartment.
				List.of(
					inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1"),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Conditionally Update Patient + Observation | inline match URL binds to in-bundle entry, patient is new",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "brand-new-cu"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|brand-new-cu"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsCondUpdNew"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|brand-new-cu" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Patient PUT: brand-new-cu doesn't exist → creates with server-assigned UUID. Obs references it.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"Create Patient + Observation | inline match URL binds to the unconditional in-bundle patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c7" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC7" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|c7" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				// The identifier index includes unconditional entries: the ref binds to the in-bundle Patient,
				// no synthetic is minted, and the Observation co-locates with it.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
				)
			),
			Arguments.of(
				"Create Patient + Conditionally Update Observation | inline match URL binds to the unconditional in-bundle patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "c13" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsC13" } ],
									"subject" : { "reference" : "Patient?identifier=old-sys|c13" }
								},
								"request" : { "method" : "PUT", "url" : "Observation?identifier=observation-system|obsC13" }
							}
						]
					}
					""",
				// Same unconditional-entry binding, with a conditional-PUT Observation.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH, 0)
				)
			),
			Arguments.of(
				"Conditionally Create Patient ×2 + Observation ×2 | two new patients, cross-partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newA"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newA"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "newB"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|newB"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsA"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newA" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsB"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|newB" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Normalizer rewrites ObsA/ObsB subjects using PatA/PatB fullUrls. Both patients created new.
				// All 4 entries remain in response. Cross-partition writes land in each patient's own compartment.
				List.of(
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inAnyCompartment("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0),
					inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 1)
				)
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
									"identifier" : [ { "system" : "old-sys", "value" : "uac"} ]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat-uac"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obsUac"} ],
									"subject" : { "reference" : "Patient/pat-uac" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// pat-uac doesn't exist → explicit-id PUT creates it (update-as-create); direct ref co-locates.
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
									"identifier" : [ { "system" : "org-sys", "value" : "org1"} ],
									"name" : "Acme Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}
						]
					}
					""",
				// Organization is non-compartment → goes to default partition (-1 = ALTERNATE_DEFAULT_ID).
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
									"identifier" : [ { "system" : "org-sys", "value" : "org-mixed"} ],
									"name" : "Mixed Bundle Hospital"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs-mixed"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" }
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Organization → default partition. Obs inline match URL → synthetic (pat1 NOP); 1 stripped.
				// Obs in pat1's compartment.
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			)
		);
	}
}
