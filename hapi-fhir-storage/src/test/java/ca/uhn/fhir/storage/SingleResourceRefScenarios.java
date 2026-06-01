package ca.uhn.fhir.storage;

import ca.uhn.fhir.storage.InlineMatchUrlBundleSyntaxTransformerServiceTest.SysVal;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.storage.InlineMatchUrlBundleSyntaxTransformerServiceTest.assertSourceEntryAt;
import static ca.uhn.fhir.storage.InlineMatchUrlBundleSyntaxTransformerServiceTest.assertSyntheticEntryAt;
import static ca.uhn.fhir.storage.InlineMatchUrlBundleSyntaxTransformerServiceTest.bundleAssert;
import static ca.uhn.fhir.storage.InlineMatchUrlBundleSyntaxTransformerServiceTest.findSyntheticEntryIndex;
import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-7
class SingleResourceRefScenarios implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
		return Stream.of(
			Arguments.of(
				"unconditional create Observation | subject referencing a patient",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"identifier" : [ { "system" : "old-sys", "value" : "identNew"} ],
									"subject" : {
										"reference": "Patient?identifier=system|value"
										}
								},
								"request" : { "method" : "POST", "url" : "Observation"}
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"conditional update (PUT) Observation | subject referencing a patient via inline match URL",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"id" : "obs-abc",
									"subject" : { "reference": "Patient?identifier=system|value" }
								},
								"request" : { "method" : "PUT", "url" : "Observation/obs-abc" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"Observation with two distinct inline match URL references | both placeholders added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|val1" },
									"performer" : [{ "reference": "Patient?identifier=system|val2" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				2,
				bundleAssert(3, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
					int val1Idx = findSyntheticEntryIndex(entries.subList(0, 2), "Patient?identifier=system|val1");
					int val2Idx = findSyntheticEntryIndex(entries.subList(0, 2), "Patient?identifier=system|val2");
					Bundle.BundleEntryComponent obsEntry = entries.get(2);

					String val1Urn = assertSyntheticEntryAt(theBundle, val1Idx, ResourceType.Patient, "Patient?identifier=system|val1", "system", "val1");
					String val2Urn = assertSyntheticEntryAt(theBundle, val2Idx, ResourceType.Patient, "Patient?identifier=system|val2", "system", "val2");
					assertThat(obsEntry.getResource().getResourceType()).isEqualTo(ResourceType.Observation);

					Observation obs = (Observation) obsEntry.getResource();
					assertThat(obs.getSubject().getReference()).isEqualTo(val1Urn);
					assertThat(obs.getPerformerFirstRep().getReference()).isEqualTo(val2Urn);
				})
			),
			Arguments.of(
				"same inline match URL referenced twice in one source | single placeholder, both refs rewritten to same fullUrl",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|value" },
									"performer" : [{ "reference": "Patient?identifier=system|value" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getPerformerFirstRep().getReference());
				})
			),
			Arguments.of(
				"multiple identifier values in one match URL | placeholder has multiple identifiers",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|val1&identifier=system|val2" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient,
							"Patient?identifier=system|val1&identifier=system|val2",
							List.of(new SysVal("system", "val1"), new SysVal("system", "val2")));
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"non-Patient target type | Observation.subject referencing a Group via inline match URL",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Group?identifier=system|grp1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Group, "Group?identifier=system|grp1", "system", "grp1");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
				})
			),
			Arguments.of(
				"non-compartment reference field (Observation.focus) | inline match URL still processed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"focus" : [{ "reference": "Patient?identifier=system|value" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getFocusFirstRep().getReference());
				})
			),
			Arguments.of(
				"mix of inline match URL and direct reference | only inline reference rewritten",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=system|value" },
									"performer" : [{ "reference": "Practitioner/prac-123" }]
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Observation.class, urn, obs -> obs.getSubject().getReference());
					Observation obs = (Observation) theBundle.getEntry().get(1).getResource();
					assertThat(obs.getPerformerFirstRep().getReference()).isEqualTo("Practitioner/prac-123");
				})
			),
			Arguments.of(
				"Patient source | link.other referencing another Patient via inline match URL",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"link" : [
										{
											"other" : { "reference": "Patient?identifier=system|value" },
											"type" : "replaces"
										}
									]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							}
						]
					}
					""",
				1,
				bundleAssert(2, theBundle -> {
					String urn = assertSyntheticEntryAt(theBundle, 0, ResourceType.Patient, "Patient?identifier=system|value", "system", "value");
					assertSourceEntryAt(theBundle, 1, Patient.class, urn, p -> p.getLinkFirstRep().getOther().getReference());
				})
			),
			Arguments.of(
				"empty bundle | unchanged",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction" }
					""",
				0,
				bundleAssert(0)
			),
			Arguments.of(
				"no inline match URL | bundle unchanged",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
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
				0,
				bundleAssert(1, theBundle -> assertSourceEntryAt(
						theBundle, 0, Observation.class,
						"Patient/123",
						obs -> obs.getSubject().getReference()))
			),
			Arguments.of(
				"absolute-URL form reference | ignored, no placeholder added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "http://example.org/fhir/Patient?identifier=system|value" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(1, theBundle -> assertSourceEntryAt(
						theBundle, 0, Observation.class,
						"http://example.org/fhir/Patient?identifier=system|value",
						obs -> obs.getSubject().getReference()))
			),
			Arguments.of(
				"https absolute-URL form reference | ignored, no placeholder added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "https://example.org/fhir/Patient?identifier=system|value" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(1, theBundle -> assertSourceEntryAt(
						theBundle, 0, Observation.class,
						"https://example.org/fhir/Patient?identifier=system|value",
						obs -> obs.getSubject().getReference()))
			),
			Arguments.of(
				"urn:uuid reference | ignored, no placeholder added",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "urn:uuid:already-a-placeholder" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				0,
				bundleAssert(1, theBundle -> assertSourceEntryAt(
						theBundle, 0, Observation.class,
						"urn:uuid:already-a-placeholder",
						obs -> obs.getSubject().getReference()))
			)
		);
	}
}