package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
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
import static org.junit.jupiter.api.Assertions.assertNull;

// Created by Claude Opus 4.7
class TransactionBundleNormalizerTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionBundleNormalizerTest.class);

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private TransactionBundleNormalizer mySvc;

	private StorageSettings myStorageSettings;

	@BeforeEach
	void setUp() {
		FhirContextSearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);
		MatchUrlService matchUrlService = new MatchUrlService(ourFhirContext, searchParamRegistry);
		myStorageSettings = new StorageSettings();
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		mySvc = new TransactionBundleNormalizer(
				ourFhirContext, matchUrlService, new TransactionProcessorVersionAdapterR4(), myStorageSettings);
	}

	private static TransactionDetails withSyntheticEntryCount(int theCount) {
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.putUserData(TransactionBundleNormalizer.SYNTHETIC_ENTRY_COUNT_KEY, theCount);
		return transactionDetails;
	}

	// Created by Claude Fable 5
	private static int recordedSyntheticEntryCount(TransactionDetails theTransactionDetails) {
		return theTransactionDetails.getUserData(TransactionBundleNormalizer.SYNTHETIC_ENTRY_COUNT_KEY);
	}

	@Test
	void testStripSyntheticResponseEntries_removesLeadingSyntheticEntries() {
		Bundle response = new Bundle();
		response.addEntry().setFullUrl("urn:uuid:synthetic-1");
		response.addEntry().setFullUrl("urn:uuid:synthetic-2");
		response.addEntry().setFullUrl("urn:uuid:original");

		mySvc.stripSyntheticResponseEntries(response, withSyntheticEntryCount(2));

		assertThat(response.getEntry()).hasSize(1);
		assertEquals("urn:uuid:original", response.getEntry().get(0).getFullUrl());
	}

	@Test
	void testStripSyntheticResponseEntries_zeroCountLeavesResponseUntouched() {
		Bundle response = new Bundle();
		response.addEntry().setFullUrl("urn:uuid:original");

		mySvc.stripSyntheticResponseEntries(response, withSyntheticEntryCount(0));

		assertThat(response.getEntry()).hasSize(1);
	}

	// Created by Claude Fable 5
	@Test
	void testStripSyntheticResponseEntries_noRecordedCountLeavesResponseUntouched() {
		Bundle response = new Bundle();
		response.addEntry().setFullUrl("urn:uuid:original");

		mySvc.stripSyntheticResponseEntries(response, new TransactionDetails());

		assertThat(response.getEntry()).hasSize(1);
	}

	/**
	 * A synthetic that actually created its placeholder must be reported on the first referencing entry's
	 * outcome before the synthetic response entry is stripped, matching the notification the resolver's
	 * auto-create path produces.
	 */
	// Created by Claude Fable 5
	@Test
	void testStripSyntheticResponseEntries_createdSynthetic_reportsPlaceholderOnFirstReferencer() {
		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		request.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();
		mySvc.normalize(request, transactionDetails);

		Bundle response = new Bundle();
		response.addEntry().getResponse().setStatus("201 Created").setLocation("Patient/synthetic-created/_history/1");
		Bundle.BundleEntryComponent obsResponse = response.addEntry();
		obsResponse.getResponse().setStatus("201 Created");
		obsResponse.getResponse().setOutcome(new OperationOutcome());

		mySvc.stripSyntheticResponseEntries(response, transactionDetails);

		assertThat(response.getEntry()).hasSize(1);
		OperationOutcome oo =
				(OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssueFirstRep().getDetails().getCodingFirstRep().getCode())
				.isEqualTo(StorageResponseCodeEnum.AUTOMATICALLY_CREATED_PLACEHOLDER_RESOURCE.name());
		IdType placeholderId = (IdType) oo.getIssueFirstRep()
				.getExtensionByUrl(HapiExtensions.EXTENSION_PLACEHOLDER_ID)
				.getValue();
		assertThat(placeholderId.getValue()).contains("Patient/synthetic-created");
	}

	// Created by Claude Fable 5
	@Test
	void testStripSyntheticResponseEntries_matchedSynthetic_addsNoIssue() {
		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		request.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();
		mySvc.normalize(request, transactionDetails);

		Bundle response = new Bundle();
		response.addEntry().getResponse().setStatus("200 OK").setLocation("Patient/already-there/_history/1");
		Bundle.BundleEntryComponent obsResponse = response.addEntry();
		obsResponse.getResponse().setStatus("201 Created");
		obsResponse.getResponse().setOutcome(new OperationOutcome());

		mySvc.stripSyntheticResponseEntries(response, transactionDetails);

		assertThat(response.getEntry()).hasSize(1);
		OperationOutcome oo =
				(OperationOutcome) response.getEntry().get(0).getResponse().getOutcome();
		assertThat(oo.getIssue()).as("matched synthetic created nothing to report").isEmpty();
	}

	// Created by Claude Fable 5
	@Test
	void testNormalize_recordsSyntheticEntryCountOnTransactionDetails() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		bundle.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();

		mySvc.normalize(bundle, transactionDetails);

		assertEquals(1, recordedSyntheticEntryCount(transactionDetails));
	}

	// Created by Claude Fable 5
	@Test
	void testNormalize_batchBundleLeftUntouched() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.BATCH);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		bundle.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();

		mySvc.normalize(bundle, transactionDetails);

		assertEquals(0, recordedSyntheticEntryCount(transactionDetails));
		assertThat(bundle.getEntry()).hasSize(1);
		assertNull(bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?identifier=http://foo|bar", obs.getSubject().getReference());
	}

	// Created by Claude Fable 5
	@Test
	void testNormalize_autoCreatePlaceholdersDisabledLeavesBundleUntouched() {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(false);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		bundle.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();

		mySvc.normalize(bundle, transactionDetails);

		assertEquals(0, recordedSyntheticEntryCount(transactionDetails));
		assertThat(bundle.getEntry()).hasSize(1);
		assertNull(bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?identifier=http://foo|bar", obs.getSubject().getReference());
	}

	// Created by Claude Fable 5
	@Test
	void testNormalize_inlineMatchUrlReferencesDisabledLeavesBundleUntouched() {
		myStorageSettings.setAllowInlineMatchUrlReferences(false);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		Observation obs = new Observation();
		obs.getSubject().setReference("Patient?identifier=http://foo|bar");
		bundle.addEntry().setResource(obs).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		TransactionDetails transactionDetails = new TransactionDetails();

		mySvc.normalize(bundle, transactionDetails);

		assertEquals(0, recordedSyntheticEntryCount(transactionDetails));
		assertThat(bundle.getEntry()).hasSize(1);
		assertNull(bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?identifier=http://foo|bar", obs.getSubject().getReference());
	}

	@ParameterizedTest
	@ArgumentsSource(SingleResourceRefScenarios.class)
	void testTransaction_singleResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		// fixed setup
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);
		TransactionDetails transactionDetails = new TransactionDetails();

		// then
		mySvc.normalize(requestBundle, transactionDetails);
		ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(requestBundle));

		// expectations
		assertThat(recordedSyntheticEntryCount(transactionDetails)).isEqualTo(theExpectedSyntheticCount);
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

		assertThatThrownBy(() -> mySvc.normalize(requestBundle, new TransactionDetails()))
				.isInstanceOf(theExpectedException)
				.hasMessage(theExpectedMessage);
	}

	@ParameterizedTest
	@ArgumentsSource(MultiResourceRefScenarios.class)
	void testTransaction_multiResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);
		TransactionDetails transactionDetails = new TransactionDetails();

		mySvc.normalize(requestBundle, transactionDetails);

		assertThat(recordedSyntheticEntryCount(transactionDetails)).isEqualTo(theExpectedSyntheticCount);
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

		TransactionDetails transactionDetails = new TransactionDetails();
		mySvc.normalize(bundle, transactionDetails);

		assertThat(recordedSyntheticEntryCount(transactionDetails)).isZero();
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

		mySvc.normalize(bundle, new TransactionDetails());

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

		mySvc.normalize(bundle, new TransactionDetails());

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

		mySvc.normalize(bundle, new TransactionDetails());

		assertThat(bundle.getEntryFirstRep().getFullUrl()).isEqualTo(existing);
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
	 * Assert that the synthetic entry at the given index is correctly created, and return its fullUrl.
	 */
	static String assertSyntheticEntryAt(
			Bundle theBundle,
			int theIndex,
			ResourceType theExpectedResourceType,
			String theExpectedMatchUrl,
			String theExpectedSystem,
			String theExpectedValue) {
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

		// assert the single identifier the placeholder carries
		List<Identifier> identifiers =
			ourFhirContext.newTerser().getAllPopulatedChildElementsOfType(resource, Identifier.class);
		assertThat(identifiers).hasSize(1);
		assertThat(identifiers.get(0).getSystem()).isEqualTo(theExpectedSystem);
		assertThat(identifiers.get(0).getValue()).isEqualTo(theExpectedValue);

		return entry.getFullUrl();
	}


}
