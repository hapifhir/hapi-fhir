package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.model.config.PartitionSettings.CrossPartitionReferenceMode;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inAnyPartitionExceptDefault;
import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inCompartmentOf;
import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inCompartmentOfDistinctFrom;
import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inDefaultPartition;

/**
 * Transaction-bundle scenarios where a reference crosses partition boundaries: patient-compartment entries
 * referencing default-partition entries (ancillary resources), default-partition entries referencing
 * patient compartments, and references between two different patient compartments. Runs with the same
 * fixture as {@link PatientIdPartitionReferenceScenarios} (pat1/pat2 pre-created, UUID server ids,
 * auto-create placeholders), once per cross-partition reference mode ({@code NOT_ALLOWED} and
 * {@code ALLOWED_UNQUALIFIED}) — within a transaction, reference targets are pre-resolved on their own
 * read partition, so these bundles ingest identically in either mode.
 *
 * TODO-TG: Pining this behaviour for now. In future, may need to change this so that it is rejected in
 * 	NOT_ALLOWED mode.
 */
// Created by Claude Fable 5
class PatientIdPartitionCrossPartitionScenarios implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
		return Stream.of(CrossPartitionReferenceMode.NOT_ALLOWED, CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED)
			.flatMap(mode -> scenarios().map(scenario -> {
				Object[] args = scenario.get();
				return Arguments.of(mode, args[0], args[1], args[2]);
			}));
	}

	private Stream<Arguments> scenarios() {
		return Stream.of(
			Arguments.of(
				"Create Practitioner + Patient referencing it | compartment entry referencing default-partition entry",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:0b6a3f5c-3c86-46f7-9c62-11b45e7f2a91",
								"resource" : {
									"resourceType" : "Practitioner",
									"identifier" : [ { "system" : "practitioner-system", "value" : "prac1"} ]
								},
								"request" : { "method" : "POST", "url" : "Practitioner"}
							}, {
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ],
									"generalPractitioner" : [ { "reference" : "urn:uuid:0b6a3f5c-3c86-46f7-9c62-11b45e7f2a91" } ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// Practitioner (ancillary) → default partition. Patient's generalPractitioner crosses partitions:
				// the urn is substituted with the Practitioner's concrete id.
				List.of(
					inDefaultPartition("Practitioner", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inAnyPartitionExceptDefault("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Create Organization + Observation | inline match URL subject and cross-partition performer",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl": "urn:uuid:8f3a2e17-6f5d-4f89-b2ea-52c9d1a5b0c4",
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "org-perf"} ],
									"name" : "Performing Lab"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs-perf"} ],
									"subject" : { "reference" : "Patient?identifier=old-sys|ident1" },
									"performer" : [ { "reference" : "urn:uuid:8f3a2e17-6f5d-4f89-b2ea-52c9d1a5b0c4" } ]
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Subject inline match URL → synthetic (pat1 NOP); 1 stripped. Performer keeps its reference
				// to the default-partition Organization in the same entry the normalizer rewrote.
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Create Organization + Observation | performer inline match URL binding the in-bundle Organization entry",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Organization",
									"identifier" : [ { "system" : "org-sys", "value" : "org-inb"} ],
									"name" : "In-Bundle Lab"
								},
								"request" : { "method" : "POST", "url" : "Organization"}
							}, {
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "observation-system", "value" : "obs-inb"} ],
									"subject" : { "reference" : "Patient/pat1" },
									"performer" : [ { "reference" : "Organization?identifier=org-sys|org-inb" } ]
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				// Performer match URL binds to the in-bundle Organization entry's normalizer-assigned fullUrl
				// (no synthetic minted) — a cross-partition reference resolved through the in-bundle index.
				List.of(
					inDefaultPartition("Organization", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
				)
			),
			Arguments.of(
				"Create Group + Patient | default-partition entry referencing patient compartments",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Group",
									"type" : "person",
									"actual" : true,
									"member" : [
										{ "entity" : { "reference" : "Patient/pat1" } },
										{ "entity" : { "reference" : "urn:uuid:6c2f4b8a-9d1e-4c35-8a7f-3e5d2b190c44" } }
									]
								},
								"request" : { "method" : "POST", "url" : "Group"}
							}, {
								"fullUrl": "urn:uuid:6c2f4b8a-9d1e-4c35-8a7f-3e5d2b190c44",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// Group is always stored in the default partition; its members reference pat1's compartment and
				// the new patient's — the reverse direction (default → compartment).
				List.of(
					inDefaultPartition("Group", StorageResponseCodeEnum.SUCCESSFUL_CREATE),
					inAnyPartitionExceptDefault("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Create Patient linking another patient | inline match URL to pat2, compartment-to-compartment",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ],
									"link" : [
										{ "other" : { "reference" : "Patient?identifier=old-sys|ident2" }, "type" : "seealso" }
									]
								},
								"request" : { "method" : "POST", "url" : "Patient"}
							}
						]
					}
					""",
				// link.other inline match URL → synthetic conditional-create NOPs against pat2 in pat2's
				// partition (stripped), and the minted patient carries the rewritten outbound link. With a random
				// minted UUID the placement can only be pinned to "not default" — the deterministic
				// cross-compartment claim is pinned by the update-as-create variant below.
				List.of(
					inAnyPartitionExceptDefault("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			),
			Arguments.of(
				"Update-as-create Patient linking another patient | inline match URL to pat2, compartment-to-compartment",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "pat3",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ],
									"link" : [
										{ "other" : { "reference" : "Patient?identifier=old-sys|ident2" }, "type" : "seealso" }
									]
								},
								"request" : { "method" : "PUT", "url" : "Patient/pat3"}
							}
						]
					}
					""",
				// Same link rewrite as above, but the client id pat3 makes the new patient's partition
				// deterministic — the assertion pins that it lands in its own compartment, distinct from pat2's:
				// a reference between two patient-compartment partitions, neither of them default.
				List.of(
					inCompartmentOfDistinctFrom("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE, "pat3", "pat2")
				)
			),
			Arguments.of(
				"Create Provenance targeting two patients | multi-compartment resource → default partition",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Provenance",
									"target" : [
										{ "reference" : "Patient/pat1" },
										{ "reference" : "Patient/pat2" }
									],
									"recorded" : "2026-01-01T00:00:00Z",
									"agent" : [ { "who" : { "display" : "ingest-system" } } ]
								},
								"request" : { "method" : "POST", "url" : "Provenance"}
							}
						]
					}
					""",
				// Two patient targets → no single compartment; the NON_UNIQUE_COMPARTMENT_IN_DEFAULT policy
				// routes the Provenance to the default partition, referencing into both compartments.
				List.of(
					inDefaultPartition("Provenance", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
				)
			)
		);
	}
}
