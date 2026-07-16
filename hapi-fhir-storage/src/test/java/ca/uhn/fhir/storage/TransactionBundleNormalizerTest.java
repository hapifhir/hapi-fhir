package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Created by Claude Opus 4.7
class TransactionBundleNormalizerTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionBundleNormalizerTest.class);

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private TransactionBundleNormalizer mySvc;

	@BeforeEach
	void setUp() {
		FhirContextSearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);
		MatchUrlService matchUrlService = new MatchUrlService(ourFhirContext, searchParamRegistry);
		mySvc = new TransactionBundleNormalizer(
				ourFhirContext, matchUrlService, new TransactionProcessorVersionAdapterR4());
	}

	@Test
	void testStripSyntheticResponseEntries_removesLeadingSyntheticEntries() {
		Bundle response = new Bundle();
		response.addEntry().setFullUrl("urn:uuid:synthetic-1");
		response.addEntry().setFullUrl("urn:uuid:synthetic-2");
		response.addEntry().setFullUrl("urn:uuid:original");

		mySvc.stripSyntheticResponseEntries(response, 2);

		assertThat(response.getEntry()).hasSize(1);
		assertEquals("urn:uuid:original", response.getEntry().get(0).getFullUrl());
	}

	@Test
	void testStripSyntheticResponseEntries_zeroCountLeavesResponseUntouched() {
		Bundle response = new Bundle();
		response.addEntry().setFullUrl("urn:uuid:original");

		mySvc.stripSyntheticResponseEntries(response, 0);

		assertThat(response.getEntry()).hasSize(1);
	}

	@ParameterizedTest
	@ArgumentsSource(SingleResourceRefScenarios.class)
	void testTransaction_singleResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		// fixed setup
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		// then
		int actualSyntheticCount = mySvc.normalize(requestBundle);
		ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(requestBundle));

		// expectations
		assertThat(actualSyntheticCount).isEqualTo(theExpectedSyntheticCount);
		assertNotNull(requestBundle);
		assertNotNull(theAssertions);
		theAssertions.accept(requestBundle);
	}

	@ParameterizedTest
	@ArgumentsSource(SingleResourceRefThrowScenarios.class)
	void testTransaction_singleResourceRefThrowScenarios(
			String theComment,
			String theBundle,
			Class<? extends Throwable> theExpectedException,
			String theExpectedMessage) {
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		assertThatThrownBy(() -> mySvc.normalize(requestBundle))
				.isInstanceOf(theExpectedException)
				.hasMessage(theExpectedMessage);
	}

	@ParameterizedTest
	@ArgumentsSource(MultiResourceRefScenarios.class)
	void testTransaction_multiResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		int actualSyntheticCount = mySvc.normalize(requestBundle);

		assertThat(actualSyntheticCount).isEqualTo(theExpectedSyntheticCount);
		assertNotNull(requestBundle);
		assertNotNull(theAssertions);
		theAssertions.accept(requestBundle);
	}

	@Test
	void testNormalize_assignsUrnUuidFullUrlToEntriesLackingOne() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry()
				.setResource(new Patient().addIdentifier(new Identifier().setSystem("sys").setValue("p1")))
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");
		bundle.addEntry()
				.setResource(new Observation().addIdentifier(new Identifier().setSystem("sys").setValue("o1")))
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Observation");

		int syntheticCount = mySvc.normalize(bundle);

		assertThat(syntheticCount).isZero();
		assertThat(bundle.getEntry())
				.allSatisfy(entry -> assertThat(entry.getFullUrl()).startsWith("urn:uuid:"));
	}

	@Test
	void testNormalize_reusesUrnResourceIdAsFullUrl() {
		String urnId = "urn:uuid:11111111-1111-1111-1111-111111111111";
		Patient patient = new Patient();
		patient.setId(urnId);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		mySvc.normalize(bundle);

		assertThat(bundle.getEntryFirstRep().getFullUrl()).isEqualTo(urnId);
	}

	@Test
	void testNormalize_usesResourceIdAsFullUrlForClientAssignedIds() {
		Patient patient = new Patient();
		patient.setId("Patient/237643");
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Patient/237643");

		mySvc.normalize(bundle);

		// A urn fullUrl here would displace the concrete id as the entry's identity in the
		// transaction processor (reference substitution keys, duplicate-id detection)
		assertThat(bundle.getEntryFirstRep().getFullUrl()).isEqualTo("Patient/237643");
	}

	@Test
	void testNormalize_doesNotOverwriteExistingFullUrl() {
		String existing = "urn:uuid:22222222-2222-2222-2222-222222222222";
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry()
				.setFullUrl(existing)
				.setResource(new Patient())
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");

		mySvc.normalize(bundle);

		assertThat(bundle.getEntryFirstRep().getFullUrl()).isEqualTo(existing);
	}

	@Test
	void testNormalize_inlineRefResolvesToExplicitInBundlePatient_noSynthetic() {
		// An explicit (unconditional) in-bundle Patient + an Observation whose inline match URL targets that
		// Patient's identifier should resolve to the Patient's fullUrl WITHOUT minting a synthetic conditional-create.
		String patientFullUrl = "urn:uuid:33333333-3333-3333-3333-333333333333";
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry()
				.setFullUrl(patientFullUrl)
				.setResource(new Patient().addIdentifier(new Identifier().setSystem("sys").setValue("p7")))
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");
		bundle.addEntry()
				.setResource(new Observation().setSubject(new Reference("Patient?identifier=sys|p7")))
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Observation");

		int syntheticCount = mySvc.normalize(bundle);

		assertThat(syntheticCount).isZero();
		assertThat(bundle.getEntry()).hasSize(2);
		Observation obs = (Observation) bundle.getEntry().get(1).getResource();
		assertThat(obs.getSubject().getReference()).isEqualTo(patientFullUrl);
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

	static int findSyntheticEntryIndex(
			List<Bundle.BundleEntryComponent> theEntries, String theMatchUrl) {
		for (int i = 0; i < theEntries.size(); i++) {
			if (theMatchUrl.equals(theEntries.get(i).getRequest().getIfNoneExist())) {
				return i;
			}
		}
		throw new AssertionError("No synthetic entry found for match URL: " + theMatchUrl);
	}

	/**
	 * Assert that the source entry at the given index is of the expected class and that
	 * the given reference accessor returns the expected reference value.
	 */
	static <T extends Resource> void assertSourceEntryAt(
			Bundle theBundle,
			int theSourceIndex,
			Class<T> theSourceClass,
			String theExpectedReference,
			Function<T, String> theReferenceAccessor) {
		Bundle.BundleEntryComponent source = theBundle.getEntry().get(theSourceIndex);
		assertThat(source.getResource()).isInstanceOf(theSourceClass);

		T sourceResource = theSourceClass.cast(source.getResource());
		assertEquals(theExpectedReference, theReferenceAccessor.apply(sourceResource));
	}

	/**
	 * Expected (system, value) pair for an identifier on a synthetic placeholder.
	 */
	record SysVal(String system, String value) {}

	/**
	 * Convenience overload for the common single-identifier case.
	 */
	static String assertSyntheticEntryAt(
			Bundle theBundle,
			int theIndex,
			ResourceType theExpectedResourceType,
			String theExpectedMatchUrl,
			String theExpectedSystem,
			String theExpectedValue) {
		return assertSyntheticEntryAt(
				theBundle,
				theIndex,
				theExpectedResourceType,
				theExpectedMatchUrl,
				List.of(new SysVal(theExpectedSystem, theExpectedValue)));
	}

	/**
	 * Assert that the synthetic entry at the given index is correctly created, and return its fullUrl.
	 */
	static String assertSyntheticEntryAt(
			Bundle theBundle,
			int theIndex,
			ResourceType theExpectedResourceType,
			String theExpectedMatchUrl,
			List<SysVal> theExpectedIdentifiers) {
		Bundle.BundleEntryComponent entry = theBundle.getEntry().get(theIndex);

		// assert resource type
		assertThat(entry.getResource().getResourceType()).isEqualTo(theExpectedResourceType);

		// assert conditional create
		Bundle.BundleEntryRequestComponent request = entry.getRequest();
		assertThat(request.getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
		assertThat(request.getIfNoneExist()).isEqualTo(theExpectedMatchUrl);

		// assert fullUrl is present
		assertThat(entry.getFullUrl()).startsWith("urn:uuid:");

		// assert placeholder extension
		DomainResource resource = (DomainResource) entry.getResource();
		assertThat(resource.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)
			.getValueAsPrimitive()
			.getValueAsString())
			.isEqualTo("true");

		// assert identifiers — count matches list size, each pair present (order-agnostic)
		List<Identifier> identifiers =
			ourFhirContext.newTerser().getAllPopulatedChildElementsOfType(resource, Identifier.class);
		assertThat(identifiers).hasSize(theExpectedIdentifiers.size());
		List<SysVal> actualIdentifiers = identifiers.stream()
				.map(id -> new SysVal(id.getSystem(), id.getValue()))
				.toList();
		assertThat(actualIdentifiers).containsExactlyInAnyOrderElementsOf(theExpectedIdentifiers);

		return entry.getFullUrl();
	}


}
