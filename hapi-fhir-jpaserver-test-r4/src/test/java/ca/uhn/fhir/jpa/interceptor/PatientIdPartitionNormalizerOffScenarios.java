package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inCompartmentOfSelf;
import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inCompartmentOf;
import static ca.uhn.fhir.jpa.interceptor.PatientIdPartitionReferenceScenarios.inSamePartitionAsEntry;

/**
 * The transaction contract of patient-id partition mode when {@code autoCreatePlaceholderReferenceTargets}
 * is {@code false} — i.e. with the {@code TransactionBundleNormalizer} disabled (its gate requires the flag):
 * no synthetic entries, no placeholder auto-creation, no in-bundle match-URL binding. What still works:
 * inline match URLs that resolve to <i>existing</i> resources via pre-fetch (requires all-partition search
 * support), client-supplied urn placeholders (the post-prefetch hook needs no normalizer), client-assigned
 * ids, and conditional writes. What is rejected: inline match URLs that nothing existing satisfies — including
 * ones an in-bundle entry would have satisfied, since binding is a normalizer capability ({@link Rejected}).
 */
// Created by Claude Fable 5
class PatientIdPartitionNormalizerOffScenarios {

	static void createTestFixture(ITestDataBuilder theTestData) {
		theTestData.createPatient(
				theTestData.withId("pat1"),
				theTestData.withIdentifier("old-sys", "existingPat1Ident1"),
				theTestData.withIdentifier("new-sys", "existingPat1Ident2"));
		theTestData.createPatient(
				theTestData.withId("pat2"), theTestData.withIdentifier("old-sys", "existingPat2Ident1"));
	}

	// Created by Claude Fable 5
	static class Accepted implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
			return Stream.of(
				Arguments.of(
					"Create Observation | inline match URL resolves to existing patient, reference substituted",
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
					"Pre-fetch resolves the URL to pat1; the hook substitutes the literal reference. No synthetic.",
					List.of(
						inCompartmentOf("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, "pat1")
					)
				),
				Arguments.of(
					"Conditionally Create Patient + Observation | client-supplied urn placeholder",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"fullUrl": "urn:uuid:5b3f0a91-2f6c-4f27-9d84-c1a7e02b6633",
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [ { "system" : "old-sys", "value" : "condCreatePatient"} ]
									},
									"request" : { "method" : "POST", "url" : "Patient", "ifNoneExist" : "Patient?identifier=old-sys|condCreatePatient"}
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithUrnRef"} ],
										"subject" : { "reference" : "urn:uuid:5b3f0a91-2f6c-4f27-9d84-c1a7e02b6633" }
									},
									"request" : { "method" : "POST", "url" : "Observation"}
								}
							]
						}
						""",
					"The post-prefetch hook mints and rewrites off the client's urn fullUrl — no normalizer involved.",
					List.of(
						inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH),
						inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
					)
				),
				Arguments.of(
					"Update-as-create Patient + Observation | client-assigned id, direct reference",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Patient",
										"id" : "pat9",
										"identifier" : [ { "system" : "old-sys", "value" : "newPatient"} ]
									},
									"request" : { "method" : "PUT", "url" : "Patient/pat9"}
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"identifier" : [ { "system" : "observation-system", "value" : "obsWithDirectRef"} ],
										"subject" : { "reference" : "Patient/pat9" }
									},
									"request" : { "method" : "POST", "url" : "Observation"}
								}
							]
						}
						""",
					"Client-assigned ids and direct references need neither the normalizer nor pre-fetch.",
					List.of(
						inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_AS_CREATE, "pat9"),
						inSamePartitionAsEntry("Observation", StorageResponseCodeEnum.SUCCESSFUL_CREATE, 0)
					)
				),
				Arguments.of(
					"Conditionally Update Patient | matches existing, updated in place",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [ { "system" : "old-sys", "value" : "existingPat1Ident1"}, { "system" : "new-sys", "value" : "existingPat1Ident2"} ],
										"active" : true
									},
									"request" : { "method" : "PUT", "url" : "Patient?identifier=old-sys|existingPat1Ident1"}
								}
							]
						}
						""",
					"No normalizer-assigned fullUrl: the hook's id-less-matched-conditional-update branch stamps pat1.",
					List.of(
						inCompartmentOf("Patient", StorageResponseCodeEnum.SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH, "pat1")
					)
				),
				Arguments.of(
					"Create Patient | bare POST, UUID server id strategy",
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
					"The post-prefetch hook mints a UUID id and rewrites the POST; no normalizer involved.",
					List.of(
						inCompartmentOfSelf("Patient", StorageResponseCodeEnum.SUCCESSFUL_CREATE)
					)
				)
			);
		}
	}

	/** Bundles the normalizer would have accepted (or that need it) but that must fail with it disabled. */
	// Created by Claude Fable 5
	static class Rejected implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
			return Stream.of(
				Arguments.of(
					"Create Observation | inline match URL with no match — no placeholder auto-creation",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Observation",
										"subject" : { "reference" : "Patient?identifier=old-sys|no-such-patient" }
									},
									"request" : { "method" : "POST", "url" : "Observation"}
								}
							]
						}
						""",
					"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment"
				),
				Arguments.of(
					"Create Patient + Observation | in-bundle inline match URL binding requires the normalizer",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Patient",
										"identifier" : [ { "system" : "old-sys", "value" : "inBundlePatient"} ]
									},
									"request" : { "method" : "POST", "url" : "Patient"}
								}, {
									"resource" : {
										"resourceType" : "Observation",
										"subject" : { "reference" : "Patient?identifier=old-sys|inBundlePatient" }
									},
									"request" : { "method" : "POST", "url" : "Observation"}
								}
							]
						}
						""",
					// Pre-fetch only sees committed data; the in-bundle patient cannot satisfy the URL without
					// the normalizer's in-bundle index.
					"HAPI-1326: Resource of type Observation has no values placing it in the Patient compartment"
				),
				Arguments.of(
					"Create Observation | direct reference to a nonexistent patient — no placeholder auto-creation",
					"""
						{ "resourceType" : "Bundle", "type" : "transaction",
							"entry" : [
								{
									"resource" : {
										"resourceType" : "Observation",
										"subject" : { "reference" : "Patient/patNoPh" }
									},
									"request" : { "method" : "POST", "url" : "Observation"}
								}
							]
						}
						""",
					// The direct id still routes the entry to patNoPh's compartment; reference resolution then
					// finds no target and placeholder auto-creation is off.
					"HAPI-1094: Resource Patient/patNoPh not found, specified in path: Observation.subject"
				)
			);
		}
	}
}
