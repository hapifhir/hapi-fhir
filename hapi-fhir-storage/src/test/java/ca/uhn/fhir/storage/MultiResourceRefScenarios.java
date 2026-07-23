package ca.uhn.fhir.storage;

import ca.uhn.fhir.storage.TransactionBundleNormalizerTest.SysVal;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.storage.TransactionBundleNormalizerTest.assertSourceEntryAt;
import static ca.uhn.fhir.storage.TransactionBundleNormalizerTest.assertSyntheticEntryAt;
import static ca.uhn.fhir.storage.TransactionBundleNormalizerTest.bundleAssert;
import static ca.uhn.fhir.storage.TransactionBundleNormalizerTest.findSyntheticEntryIndex;
import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-7
class MultiResourceRefScenarios implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
		return Stream.of(
			Arguments.of(
				"two sources sharing the same inline match URL | single placeholder via dedup",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(3, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient,
							"Patient?identifier=http://system|value1", "http://system", "value1");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 2, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"two sources with distinct inline match URLs | two placeholders added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							},
							{
								"resource" : {
									"resourceType" : "Encounter",
									"subject" : { "reference": "Patient?identifier=http://system|value2" }
								},
								"request" : { "method" : "POST", "url" : "Encounter" }
							}
						]
					}
					""",
				2,
				bundleAssert(4, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
					int val1Idx = findSyntheticEntryIndex(entries.subList(0, 2), "Patient?identifier=http://system|value1");
					int val2Idx = findSyntheticEntryIndex(entries.subList(0, 2), "Patient?identifier=http://system|value2");

					String val1Urn = assertSyntheticEntryAt(theBundle, val1Idx, ResourceType.Patient,
							"Patient?identifier=http://system|value1", "http://system", "value1");
					String val2Urn = assertSyntheticEntryAt(theBundle, val2Idx, ResourceType.Patient,
							"Patient?identifier=http://system|value2", "http://system", "value2");

					assertSourceEntryAt(theBundle, 2, Observation.class, val1Urn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 3, Encounter.class, val2Urn, enc -> enc.getSubject().getReference());
				})
			),
			Arguments.of(
				"two sources mixing inline match URL and direct reference | only the inline one is rewritten",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient/123" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(3, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient,
							"Patient?identifier=http://system|value1", "http://system", "value1");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 2, Observation.class, "Patient/123", obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"DELETE and GET entries (no resource bodies) | both skipped, bundle unchanged",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"request" : { "method" : "DELETE", "url" : "Patient?identifier=system|value" }
							},
							{
								"request" : { "method" : "GET", "url" : "Observation/123" }
							}
						]
					}
					""",
				0,
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
					// DELETE entry unchanged
					assertThat(entries.get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.DELETE);
					assertThat(entries.get(0).getRequest().getUrl()).isEqualTo("Patient?identifier=system|value");
					assertThat(entries.get(0).getResource()).isNull();
					// GET entry unchanged
					assertThat(entries.get(1).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.GET);
					assertThat(entries.get(1).getRequest().getUrl()).isEqualTo("Observation/123");
					assertThat(entries.get(1).getResource()).isNull();
				})
			),
			Arguments.of(
				"two sources each with multiple refs | shared ref dedup + distinct refs each get own placeholder",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|A" },
									"performer" : [{ "reference": "Patient?identifier=system|B" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|A" },
									"performer" : [{ "reference": "Patient?identifier=system|C" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				3,
				bundleAssert(5, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
					int aIdx = findSyntheticEntryIndex(entries.subList(0, 3), "Patient?identifier=system|A");
					int bIdx = findSyntheticEntryIndex(entries.subList(0, 3), "Patient?identifier=system|B");
					int cIdx = findSyntheticEntryIndex(entries.subList(0, 3), "Patient?identifier=system|C");

					String aUrn = assertSyntheticEntryAt(theBundle, aIdx, ResourceType.Patient,
							"Patient?identifier=system|A", "system", "A");
					String bUrn = assertSyntheticEntryAt(theBundle, bIdx, ResourceType.Patient,
							"Patient?identifier=system|B", "system", "B");
					String cUrn = assertSyntheticEntryAt(theBundle, cIdx, ResourceType.Patient,
							"Patient?identifier=system|C", "system", "C");

					// Obs1 at index 3: subject=A, performer=B
					assertSourceEntryAt(theBundle, 3, Observation.class, aUrn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 3, Observation.class, bUrn, obs -> obs.getPerformerFirstRep().getReference());
					// Obs2 at index 4: subject=A (same placeholder), performer=C
					assertSourceEntryAt(theBundle, 4, Observation.class, aUrn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 4, Observation.class, cUrn, obs -> obs.getPerformerFirstRep().getReference());
				})
			),
			Arguments.of(
				"conditional POST Patient + Observation with matching inline match URL | reuse existing fullUrl, no synthetic added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:patient-1",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val1" }],
									"name" : [{ "family" : "Doe", "given" : ["Jane"] }]
								},
								"request" : {
									"method" : "POST",
									"url" : "Patient",
									"ifNoneExist" : "Patient?identifier=sys|val1"
								}
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();

					// Entry 0: user's Patient is unchanged
					Bundle.BundleEntryComponent patientEntry = entries.get(0);
					assertThat(patientEntry.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(patientEntry.getFullUrl()).isEqualTo("urn:uuid:patient-1");
					assertThat(patientEntry.getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
					assertThat(patientEntry.getRequest().getIfNoneExist()).isEqualTo("Patient?identifier=sys|val1");

					Patient patient = (Patient) patientEntry.getResource();
					// user-supplied body preserved
					assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("Doe");
					assertThat(patient.getIdentifierFirstRep().getSystem()).isEqualTo("sys");
					assertThat(patient.getIdentifierFirstRep().getValue()).isEqualTo("val1");
					// NOT marked as a synthetic placeholder
					assertThat(patient.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)).isNull();

					// Entry 1: Observation's subject rewritten to the existing Patient's fullUrl
					assertSourceEntryAt(theBundle, 1, Observation.class, "urn:uuid:patient-1",
							obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"conditional POST Patient (no fullUrl) + Observation with matching inline match URL | assign fullUrl, reuse, no synthetic added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val1" }],
									"name" : [{ "family" : "Doe", "given" : ["Jane"] }]
								},
								"request" : {
									"method" : "POST",
									"url" : "Patient",
									"ifNoneExist" : "Patient?identifier=sys|val1"
								}
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();

					// Entry 0: user's Patient unchanged except fullUrl gets assigned
					Bundle.BundleEntryComponent patientEntry = entries.get(0);
					assertThat(patientEntry.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(patientEntry.getFullUrl()).startsWith("urn:uuid:");
					String assignedFullUrl = patientEntry.getFullUrl();
					assertThat(patientEntry.getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
					assertThat(patientEntry.getRequest().getIfNoneExist()).isEqualTo("Patient?identifier=sys|val1");

					Patient patient = (Patient) patientEntry.getResource();
					// user-supplied body preserved
					assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("Doe");
					assertThat(patient.getIdentifierFirstRep().getSystem()).isEqualTo("sys");
					assertThat(patient.getIdentifierFirstRep().getValue()).isEqualTo("val1");
					// NOT marked as a synthetic placeholder
					assertThat(patient.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)).isNull();

					// Entry 1: Observation's subject rewritten to the assigned fullUrl
					assertSourceEntryAt(theBundle, 1, Observation.class, assignedFullUrl,
							obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"conditional PUT Patient (upsert by match URL) + Observation with matching inline match URL | reuse existing fullUrl, no synthetic added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:patient-1",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val1" }],
									"name" : [{ "family" : "Doe", "given" : ["Jane"] }]
								},
								"request" : {
									"method" : "PUT",
									"url" : "Patient?identifier=sys|val1"
								}
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();

					// Entry 0: user's PUT Patient unchanged
					Bundle.BundleEntryComponent patientEntry = entries.get(0);
					assertThat(patientEntry.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(patientEntry.getFullUrl()).isEqualTo("urn:uuid:patient-1");
					assertThat(patientEntry.getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PUT);
					assertThat(patientEntry.getRequest().getUrl()).isEqualTo("Patient?identifier=sys|val1");

					Patient patient = (Patient) patientEntry.getResource();
					// user-supplied body preserved
					assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("Doe");
					assertThat(patient.getIdentifierFirstRep().getSystem()).isEqualTo("sys");
					assertThat(patient.getIdentifierFirstRep().getValue()).isEqualTo("val1");
					// NOT marked as a synthetic placeholder
					assertThat(patient.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)).isNull();

					// Entry 1: Observation's subject rewritten to the PUT Patient's fullUrl
					assertSourceEntryAt(theBundle, 1, Observation.class, "urn:uuid:patient-1",
							obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"conditional PUT Patient (urn id, no fullUrl) + Observation mixing direct urn ref and inline match URL | reuse resource.id as placeholder",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"id" : "urn:uuid:patient-1",
									"identifier" : [{ "system" : "sys", "value" : "val1" }],
									"name" : [{ "family" : "Doe", "given" : ["Jane"] }]
								},
								"request" : {
									"method" : "PUT",
									"url" : "Patient?identifier=sys|val1"
								}
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "urn:uuid:patient-1" },
									"performer" : [{ "reference": "Patient?identifier=sys|val1" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();

					// Entry 0: user's Patient — fullUrl is set equal to its urn resource.id (we copy the urn id
					// into fullUrl rather than generate a new one: identity-preserving, and lets Patient ID
					// Partition mode key on it).
					Bundle.BundleEntryComponent patientEntry = entries.get(0);
					assertThat(patientEntry.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(patientEntry.getFullUrl()).isEqualTo("urn:uuid:patient-1");
					assertThat(patientEntry.getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PUT);
					assertThat(patientEntry.getRequest().getUrl()).isEqualTo("Patient?identifier=sys|val1");

					Patient patient = (Patient) patientEntry.getResource();
					assertThat(patient.getIdElement().getValue()).isEqualTo("urn:uuid:patient-1");
					assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("Doe");
					assertThat(patient.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)).isNull();

					// Entry 1: Observation — direct urn ref stays untouched, inline match URL rewritten to
					// the same urn (so both refs converge on the Patient via the same placeholder URI).
					assertSourceEntryAt(theBundle, 1, Observation.class, "urn:uuid:patient-1",
							obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 1, Observation.class, "urn:uuid:patient-1",
							obs -> obs.getPerformerFirstRep().getReference());
				})
			),
			Arguments.of(
				"realistic mix | conditional POST Patient (no fullUrl) + 2 Observations referencing Patient and Encounter inline",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "patient-1" }],
									"name" : [{ "family" : "Smith", "given" : ["Alice"] }]
								},
								"request" : {
									"method" : "POST",
									"url" : "Patient",
									"ifNoneExist" : "Patient?identifier=sys|patient-1"
								}
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|patient-1" },
									"encounter" : { "reference": "Encounter?identifier=sys|enc-1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|patient-1" },
									"encounter" : { "reference": "Encounter?identifier=sys|enc-1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(4, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();

					// Entry 0: synthetic Encounter placeholder (added by the normalizer)
					String encounterUrn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Encounter,
							"Encounter?identifier=sys|enc-1", "sys", "enc-1");

					// Entry 1: user's Patient preserved; fullUrl auto-assigned since none provided
					Bundle.BundleEntryComponent patientEntry = entries.get(1);
					assertThat(patientEntry.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(patientEntry.getFullUrl()).startsWith("urn:uuid:");
					String patientUrn = patientEntry.getFullUrl();
					assertThat(patientEntry.getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
					assertThat(patientEntry.getRequest().getIfNoneExist()).isEqualTo("Patient?identifier=sys|patient-1");

					Patient patient = (Patient) patientEntry.getResource();
					assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("Smith");
					assertThat(patient.getIdentifierFirstRep().getSystem()).isEqualTo("sys");
					assertThat(patient.getIdentifierFirstRep().getValue()).isEqualTo("patient-1");
					assertThat(patient.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)).isNull();

					// Entry 2: Obs1 — subject rewritten to user's Patient urn, encounter to synthetic Encounter urn
					assertSourceEntryAt(theBundle, 2, Observation.class, patientUrn,
							obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 2, Observation.class, encounterUrn,
							obs -> obs.getEncounter().getReference());

					// Entry 3: Obs2 — same refs (dedup across resources, both targets)
					assertSourceEntryAt(theBundle, 3, Observation.class, patientUrn,
							obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 3, Observation.class, encounterUrn,
							obs -> obs.getEncounter().getReference());
				})
			),
			Arguments.of(
				"Practitioner carrying the identifier a Patient match URL asks for | type mismatch, no bind, synthetic added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Practitioner",
									"identifier" : [{ "system" : "sys", "value" : "val" }]
								},
								"request" : { "method" : "POST", "url" : "Practitioner" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(3, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient,
							"Patient?identifier=sys|val", "sys", "val");

					// The Practitioner's identifier must not satisfy a Patient match URL
					Bundle.BundleEntryComponent practitionerEntry = theBundle.getEntry().get(1);
					assertThat(practitionerEntry.getResource().getResourceType()).isEqualTo(ResourceType.Practitioner);
					assertThat(urn).isNotEqualTo(practitionerEntry.getFullUrl());

					assertSourceEntryAt(theBundle, 2, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"two in-bundle Patients carrying the same identifier | ref binds the first, no synthetic",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:patient-a",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val" }]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"fullUrl" : "urn:uuid:patient-b",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val" }]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(3, theBundle -> assertSourceEntryAt(theBundle, 2, Observation.class, "urn:uuid:patient-a",
						obs -> obs.getSubject().getReference()))
			),
			Arguments.of(
				"multi-and-group match URL, in-bundle Patient carries both identifiers | never binds, synthetic added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"fullUrl" : "urn:uuid:patient-1",
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [
										{ "system" : "sys", "value" : "val1" },
										{ "system" : "sys", "value" : "val2" }
									]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=sys|val1&identifier=sys|val2" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(3, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient,
							"Patient?identifier=sys|val1&identifier=sys|val2",
							List.of(new SysVal("sys", "val1"), new SysVal("sys", "val2")));
					assertThat(urn).isNotEqualTo("urn:uuid:patient-1");
					assertSourceEntryAt(theBundle, 2, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			)
		);
	}
}
