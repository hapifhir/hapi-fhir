package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Created by claude-opus-4-6
class InlineMatchUrlBundleSyntaxTransformerServiceTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private InlineMatchUrlBundleSyntaxTransformerService mySvc;

	@BeforeEach
	void setUp() {
		FhirContextSearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);
		MatchUrlService matchUrlService = new MatchUrlService(ourFhirContext, searchParamRegistry);
		mySvc = new InlineMatchUrlBundleSyntaxTransformerService(ourFhirContext, matchUrlService);
	}

	static List<Arguments> referenceScenarioSupplier() {
		return List.of(
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
				bundleAssert(2, theBundle -> {
					List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
					Bundle.BundleEntryComponent entry1 = entries.get(0);
					Bundle.BundleEntryComponent entry2 = entries.get(1);

					// order check
					assertThat(entry1.getResource().getResourceType()).isEqualTo(ResourceType.Patient);
					assertThat(entry2.getResource().getResourceType()).isEqualTo(ResourceType.Observation);

					// placeholder id check
					String replacedReference = ((Observation) entry2.getResource()).getSubject().getReference();
					assertEquals(entry1.getFullUrl(), replacedReference);
				})
			),
			Arguments.of(
				"create Observation | local reference to existing patient",
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
				bundleAssert(1)
			),
			Arguments.of(
				"create Observation | placeholder reference to unconditional new patient",
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
				bundleAssert(2)
			),
			Arguments.of(
				"create Observation | placeholder reference to unconditional new patient | reverse order",
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
				bundleAssert(2)
			),
			Arguments.of(
				"create Observation | placeholder reference to conditional-create of existing patient",
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
				bundleAssert(2)
			),
			Arguments.of(
				"create Observation with logical reference to existing patient",
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
				bundleAssert(1)
			),
			Arguments.of(
				"conditional-update Observation with logical reference to existing patient",
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
				bundleAssert(1)
			),
			Arguments.of(
				"Observation with logical reference to patient in bundle with redundant conditional create",
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
				bundleAssert(2)
			)
		);
	}


	@ParameterizedTest
	@MethodSource("referenceScenarioSupplier()")
	void testTransaction_allReferenceScenarios(String theComment, String theBundle, Consumer<Bundle> theAssertions) {
		// fixed setup
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		// then
		mySvc.transform(requestBundle);

		// expectations
		assertNotNull(requestBundle);
		assertNotNull(theAssertions);
		theAssertions.accept(requestBundle);
	}

	@SafeVarargs
	static Consumer<Bundle> bundleAssert(int theExpectedSize, Consumer<Bundle>... theOtherAssertions) {
		return theBundle -> {
			assertThat(theBundle.getEntry()).size().isEqualTo(theExpectedSize);
			for (Consumer<Bundle> theAssertion : theOtherAssertions) {
				theAssertion.accept(theBundle);
			}
		};
	}

	@Test
	void ensurePatientsForInlineMatchUrls_observationWithInlineMatchUrl_addsPatientEntry() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient?identifier=http://system|value1"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(2);

		// New Patient entry should be first
		BundleEntryParts patientEntry = entries.get(0);
		assertThat(ourFhirContext.getResourceType(patientEntry.getResource())).isEqualTo("Patient");
		assertThat(patientEntry.getConditionalUrl()).isEqualTo("Patient?identifier=http://system|value1");

		Patient patient = (Patient) patientEntry.getResource();
		assertThat(patient.getIdentifier()).hasSize(1);
		assertThat(patient.getIdentifierFirstRep().getSystem()).isEqualTo("http://system");
		assertThat(patient.getIdentifierFirstRep().getValue()).isEqualTo("value1");

		// Original Observation entry should be second
		BundleEntryParts observationEntry = entries.get(1);
		assertThat(ourFhirContext.getResourceType(observationEntry.getResource())).isEqualTo("Observation");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_multipleResourcesSameMatchUrl_addsSinglePatientEntry() {
		// Setup
		Observation obs1 = new Observation();
		obs1.setSubject(new Reference("Patient?identifier=http://system|value1"));

		Observation obs2 = new Observation();
		obs2.setSubject(new Reference("Patient?identifier=http://system|value1"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(obs1);
		bundleBuilder.addTransactionCreateEntry(obs2);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - only 1 Patient entry added (deduplication)
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(3);

		long patientEntries = entries.stream()
			.filter(e -> "Patient".equals(ourFhirContext.getResourceType(e.getResource())))
			.count();
		assertThat(patientEntries).isEqualTo(1);
	}

	@Test
	void ensurePatientsForInlineMatchUrls_multipleDistinctMatchUrls_addsMultiplePatientEntries() {
		// Setup
		Observation obs1 = new Observation();
		obs1.setSubject(new Reference("Patient?identifier=http://system|value1"));

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference("Patient?identifier=http://system|value2"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(obs1);
		bundleBuilder.addTransactionCreateEntry(encounter);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - 2 Patient entries added
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(4);

		// Both Patient entries should be at the beginning
		assertThat(ourFhirContext.getResourceType(entries.get(0).getResource())).isEqualTo("Patient");
		assertThat(ourFhirContext.getResourceType(entries.get(1).getResource())).isEqualTo("Patient");
		assertThat(entries.get(0).getConditionalUrl()).isEqualTo("Patient?identifier=http://system|value1");
		assertThat(entries.get(1).getConditionalUrl()).isEqualTo("Patient?identifier=http://system|value2");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_noInlineMatchUrls_bundleUnchanged() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient/123"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - no entries added
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(1);
		assertThat(ourFhirContext.getResourceType(entries.get(0).getResource())).isEqualTo("Observation");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_patientEntryWithInlineMatchUrl_skipped() {
		// Setup - Patient entries should be skipped, not treated as compartment references
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://system").setValue("value1");

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(patient);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - no entries added
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(1);
	}

	@Test
	void ensurePatientsForInlineMatchUrls_emptyBundle_noChange() {
		// Setup
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		// Execute
		mySvc.transform(bundle);

		// Verify
		assertThat(bundle.getEntry()).isEmpty();
	}

	@Test
	void ensurePatientsForInlineMatchUrls_identifierWithoutSystem_throwsPreconditionFailed() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient?identifier=value1"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute & Verify
		assertThatThrownBy(() -> mySvc.transform(bundle))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("must have both a system and a value");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_identifierWithBlankValue_throwsPreconditionFailed() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient?identifier=http://system|"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute & Verify
		assertThatThrownBy(() -> mySvc.transform(bundle))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("must have both a system and a value");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_multipleSearchParams_throwsPreconditionFailed() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient?identifier=http://system|value1&active=true"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute & Verify
		assertThatThrownBy(() -> mySvc.transform(bundle))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("Can not include multiple parameters");
	}

	@Test
	void ensurePatientsForInlineMatchUrls_nonPatientCompartmentReference_ignored() {
		// Setup - a reference that starts with "Patient?" but is not a compartment reference
		// won't be picked up by getCompartmentReferencesForResource.
		// Use a resource with a non-compartment reference field.
		Observation observation = new Observation();
		// Observation.subject is a compartment reference, but Observation.focus is not
		observation.addFocus(new Reference("Patient?identifier=http://system|value1"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - no Patient entry added because focus is not a patient compartment reference
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(1);
	}

	@Test
	void ensurePatientsForInlineMatchUrls_mixOfInlineAndDirectReferences_onlyInlineProcessed() {
		// Setup
		Observation obs1 = new Observation();
		obs1.setSubject(new Reference("Patient?identifier=http://system|value1"));

		Observation obs2 = new Observation();
		obs2.setSubject(new Reference("Patient/123"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(obs1);
		bundleBuilder.addTransactionCreateEntry(obs2);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify - only 1 Patient entry added for the inline match URL
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		assertThat(entries).hasSize(3);

		long patientEntries = entries.stream()
			.filter(e -> "Patient".equals(ourFhirContext.getResourceType(e.getResource())))
			.count();
		assertThat(patientEntries).isEqualTo(1);
	}

	@Test
	void ensurePatientsForInlineMatchUrls_createdPatientEntryHasPostMethodAndConditionalUrl() {
		// Setup
		Observation observation = new Observation();
		observation.setSubject(new Reference("Patient?identifier=http://system|value1"));

		BundleBuilder bundleBuilder = new BundleBuilder(ourFhirContext);
		bundleBuilder.addTransactionCreateEntry(observation);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// Execute
		mySvc.transform(bundle);

		// Verify the Patient entry is a POST with ifNoneExist
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		BundleEntryParts patientEntry = entries.get(0);

		assertThat(patientEntry.getMethod().name()).isEqualTo("POST");
		assertThat(patientEntry.getConditionalUrl()).isEqualTo("Patient?identifier=http://system|value1");
	}
}
