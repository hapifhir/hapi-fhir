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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-6
class PatientInlineMatchUrlPreCreationServiceTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private PatientInlineMatchUrlPreCreationService mySvc;

	@BeforeEach
	void setUp() {
		FhirContextSearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);
		MatchUrlService matchUrlService = new MatchUrlService(ourFhirContext, searchParamRegistry);
		mySvc = new PatientInlineMatchUrlPreCreationService(ourFhirContext, matchUrlService);
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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		assertThatThrownBy(() -> mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle))
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
		assertThatThrownBy(() -> mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle))
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
		assertThatThrownBy(() -> mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle))
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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

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
		mySvc.conditionallyCreatePatientsForInlineMatchUrls(bundle);

		// Verify the Patient entry is a POST with ifNoneExist
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(ourFhirContext, bundle);
		BundleEntryParts patientEntry = entries.get(0);

		assertThat(patientEntry.getMethod().name()).isEqualTo("POST");
		assertThat(patientEntry.getConditionalUrl()).isEqualTo("Patient?identifier=http://system|value1");
	}
}
