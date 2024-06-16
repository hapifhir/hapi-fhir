package ca.uhn.fhir.jpa.subscription.resthook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR5Test;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestR5IT extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR5IT.class);
	public static final String OBS_CODE = "1000000050";
	public static final String OBS_CODE2 = OBS_CODE + "111";
	private static final String CUSTOM_URL = "http://custom.topic.url";

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		ourLog.info("@AfterEach");
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
	}

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription1 = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, Constants.CT_FHIR_XML_NEW);

		Subscription subscription = postSubscription(subscription1);
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservationExpectDelivery();

		// Should see 1 subscription notification
		awaitUntilReceivedTransactionCount(1);

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation, 1L);
		assertEquals(Enumerations.ObservationStatus.FINAL, obs.getStatus());
		assertEquals(sentObservation.getIdElement(), obs.getIdElement());
	}

	@Nonnull
	private Observation sendObservationExpectDelivery(int theCount) throws InterruptedException {
		return sendObservation(OBS_CODE, "SNOMED-CT", true, theCount);
	}

	@Nonnull
	private Observation sendObservationExpectDelivery() throws InterruptedException {
		return sendObservation(OBS_CODE, "SNOMED-CT", true, 1);
	}

	@Test
	public void testUpdatesHaveCorrectMetadata() throws Exception {

		createSubscriptionTopic();

		Subscription subscription = createMatchingTopicSubscription();

		/*
		 * Send version 1
		 */

		Observation sentObservation = sendObservationExpectDelivery();
		sentObservation = myObservationDao.read(sentObservation.getIdElement().toUnqualifiedVersionless(), mySrd);

		// Should see 1 subscription notification
		awaitUntilReceivedTransactionCount(1);

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 1L);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		assertEquals("1", receivedObs.getIdElement().getVersionIdPart());
		assertEquals("1", receivedObs.getMeta().getVersionId());
		assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", receivedObs.getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("2");
		updateResource(sentObservation, true);
		sentObservation = myObservationDao.read(sentObservation.getIdElement().toUnqualifiedVersionless(), mySrd);

		// Should see a second subscription notification
		awaitUntilReceivedTransactionCount(2);

		receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 2L);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		assertEquals("2", receivedObs.getIdElement().getVersionIdPart());
		assertEquals("2", receivedObs.getMeta().getVersionId());
		assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", receivedObs.getIdentifierFirstRep().getValue());
	}

	@Nonnull
	private Subscription createMatchingTopicSubscription() throws Exception {
		Subscription subscription = createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);
		return subscription;
	}

	@Test
	public void testPlaceholderReferencesInTransactionAreResolvedCorrectly() throws Exception {
		createSubscriptionTopic();

		Subscription subscription = createMatchingTopicSubscription();

		// Create a transaction that should match
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.getIdentifierFirstRep().setSystem("foo").setValue("AAA");
		bundle.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		Observation sentObservation = new Observation();
		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		sentObservation.getCode().addCoding().setCode(OBS_CODE).setSystem("SNOMED-CT");
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);
		sentObservation.getSubject().setReference(patient.getId());
		bundle.addEntry().setResource(sentObservation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");

		// Send the transaction
		sendTransaction(bundle, true);

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 1L);

		assertThat(receivedObs.getSubject().getReference()).matches("Patient/[0-9]+");
	}

	@Test
	public void testUpdatesHaveCorrectMetadataUsingTransactions() throws Exception {
		createSubscriptionTopic();

		Subscription subscription = createMatchingTopicSubscription();

		/*
		 * Send version 1
		 */

		Observation sentObservation = new Observation();
		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		sentObservation.getCode().addCoding().setCode(OBS_CODE).setSystem("SNOMED-CT");
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(sentObservation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		// Send the transaction
		Bundle responseBundle = sendTransaction(bundle, true);
		awaitUntilReceivedTransactionCount(1);

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 1L);

		Observation obs = myObservationDao.read(new IdType(responseBundle.getEntry().get(0).getResponse().getLocation()), mySrd);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		assertEquals("1", receivedObs.getIdElement().getVersionIdPart());
		assertEquals("1", receivedObs.getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", receivedObs.getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		sentObservation = new Observation();
		sentObservation.setId(obs.getId());
		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("2");
		sentObservation.getCode().addCoding().setCode(OBS_CODE).setSystem("SNOMED-CT");
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);
		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(sentObservation).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl(obs.getIdElement().toUnqualifiedVersionless().getValue());
		// Send the transaction
		sendTransaction(bundle, true);
		awaitUntilReceivedTransactionCount(2);

		receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 2L);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless(), mySrd);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		assertEquals("2", receivedObs.getIdElement().getVersionIdPart());
		assertEquals("2", receivedObs.getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", receivedObs.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testRepeatedDeliveries() throws Exception {
		createSubscriptionTopic();

		createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);

		mySubscriptionTopicsCheckedLatch.setExpectedCount(100);
		mySubscriptionDeliveredLatch.setExpectedCount(100);
		// WIP STR5 I don't know the answer to this, but should the server be bunching these up into a single delivery?
		for (int i = 0; i < 100; i++) {
			Observation observation = new Observation();
			observation.getIdentifierFirstRep().setSystem("foo").setValue("ID" + i);
			observation.getCode().addCoding().setCode(OBS_CODE).setSystem("SNOMED-CT");
			observation.setStatus(Enumerations.ObservationStatus.FINAL);
			myObservationDao.create(observation, mySrd);
		}
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		mySubscriptionDeliveredLatch.awaitExpected();
	}

	@Test
	public void testActiveSubscriptionShouldntReActivate() throws Exception {
		createSubscriptionTopic();

		createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		for (int i = 0; i < 5; i++) {
			int changes = this.mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
			assertEquals(0, changes);
		}
	}

	@Nonnull
	private Subscription createTopicSubscription() throws InterruptedException {
		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, Constants.CT_FHIR_JSON_NEW);

		return postSubscription(subscription);
	}

	private void createSubscriptionTopic() throws InterruptedException {
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);
	}

	@Test
	public void testRestHookSubscriptionNoopUpdateDoesntTriggerNewDelivery() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription = createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservationExpectDelivery();

		awaitUntilReceivedTransactionCount(1);

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation, 1L);

		// Should see 1 subscription notification
		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		// Send an update with no changes
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myObservationDao.update(obs, mySrd);

		// TODO KHS replace this sleep with a latch on http request processed
		Thread.sleep(1000);

		// Should be no further deliveries
		awaitUntilReceivedTransactionCount(1);
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDisableVersionIdInDelivery() throws Exception {
		createSubscriptionTopic();

		waitForActivatedSubscriptionCount(0);
		Subscription subscription = createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");
		Observation sentObservation1 = sendObservationExpectDelivery();

		awaitUntilReceivedTransactionCount(1);

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation1, 1L);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		IdType idElement = obs.getIdElement();
		assertEquals(sentObservation1.getIdElement().getIdPart(), idElement.getIdPart());
		// VersionId is present
		assertEquals(sentObservation1.getIdElement().getVersionIdPart(), idElement.getVersionIdPart());

		subscription
			.addExtension(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS, new BooleanType("true"));
		ourLog.info("** About to update subscription");

		ourLog.info("** About to send another...");
		updateResource(subscription, false);

		ourLog.info("** About to send observation");
		Observation sentObservation2 = sendObservationExpectDelivery();

		awaitUntilReceivedTransactionCount(2);

		Observation obs2 = assertBundleAndGetObservation(subscription, sentObservation2, 2L);

		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		idElement = obs2.getIdElement();
		assertEquals(sentObservation2.getIdElement().getIdPart(), idElement.getIdPart());
		// Now VersionId is stripped
		assertNull(idElement.getVersionIdPart());
	}

	@Test
	public void testRestHookSubscriptionDoesntGetLatestVersionByDefault() throws Exception {
		createSubscriptionTopic();

		createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation sentObservation = sendObservation(OBS_CODE, "SNOMED-CT", false);
		assertEquals("1", sentObservation.getIdElement().getVersionIdPart());
		assertNull(sentObservation.getNoteFirstRep().getText());

		sentObservation.getNoteFirstRep().setText("changed");

		DaoMethodOutcome methodOutcome = updateResource(sentObservation, false);
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", sentObservation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		mySubscriptionDeliveredLatch.setExpectedCount(2);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionDeliveredLatch.awaitExpected();

		awaitUntilReceivedTransactionCount(2);

		Observation observation1 = getReceivedObservations().stream()
			.filter(t -> "1".equals(t.getIdElement().getVersionIdPart()))
			.findFirst()
			.orElseThrow();
		Observation observation2 = getReceivedObservations().stream()
			.filter(t -> "2".equals(t.getIdElement().getVersionIdPart()))
			.findFirst()
			.orElseThrow();

		assertEquals("1", observation1.getIdElement().getVersionIdPart());
		assertNull(observation1.getNoteFirstRep().getText());
		assertEquals("2", observation2.getIdElement().getVersionIdPart());
		assertEquals("changed", observation2.getNoteFirstRep().getText());
	}

	@Test
	public void testRestHookSubscriptionGetsLatestVersionWithFlag() throws Exception {
		createSubscriptionTopic();

		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		subscription
			.addExtension(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION, new BooleanType("true"));
		postSubscription(subscription);
		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation sentObservation = sendObservation(OBS_CODE, "SNOMED-CT", false);
		assertEquals("1", sentObservation.getIdElement().getVersionIdPart());
		assertNull(sentObservation.getNoteFirstRep().getText());

		sentObservation.getNoteFirstRep().setText("changed");
		DaoMethodOutcome methodOutcome = updateResource(sentObservation, false);
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", sentObservation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		mySubscriptionDeliveredLatch.setExpectedCount(2);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionDeliveredLatch.awaitExpected();

		assertTrue(getReceivedObservations().stream().allMatch(t -> "2".equals(t.getIdElement().getVersionIdPart())));
		assertTrue(getReceivedObservations().stream().anyMatch(t -> "changed".equals(t.getNoteFirstRep().getText())));
	}

	@Test
	public void testRestHookSubscriptionApplicationJson() throws Exception {
		ourLog.info(">>>1 Creating topics");
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		ourLog.info(">>>2 Creating subscriptions");
		Subscription subscription1 = createTopicSubscription();
		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE2, Constants.CT_FHIR_JSON_NEW);

		Subscription subscription2 = postSubscription(subscription);
		waitForActivatedSubscriptionCount(2);

		ourLog.info(">>>3 Send obs");
		Observation sentObservation1 = sendObservationExpectDelivery();
		awaitUntilReceivedTransactionCount(1);
		Observation receivedObs = assertBundleAndGetObservation(subscription1, sentObservation1, 1L);
		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		assertEquals("1", receivedObs.getIdElement().getVersionIdPart());

		// Update the OBS_CODE2 subscription to subscribe to OBS_CODE
		Subscription subscriptionTemp = myClient.read().resource(Subscription.class).withId(subscription2.getId()).execute();
		assertNotNull(subscriptionTemp);
		subscriptionTemp.setTopic(subscription1.getTopic());
		ourLog.info(">>>4 Update sub");
		updateResource(subscriptionTemp, false);

		ourLog.info(">>>5 Send obs");
		Observation observation2 = sendObservationExpectDelivery(2);

		awaitUntilReceivedTransactionCount(3);

		ourLog.info(">>>6 Delete sub");
		deleteSubscription(subscription2);
		waitForActivatedSubscriptionCount(1);

		ourLog.info(">>>7 Send obs");
		IdType observationTemp3Id = sendObservationExpectDelivery().getIdElement().toUnqualifiedVersionless();

		// Should see only one subscription notification
		awaitUntilReceivedTransactionCount(4);

		Observation observation3 = myClient.read().resource(Observation.class).withId(observationTemp3Id).execute();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(OBS_CODE + "111");
		coding.setSystem("SNOMED-CT");
		ourLog.info(">>>8 Send obs");
		updateResource(observation3, true);

		// Should see one subscription notification even though the new version doesn't match, the old version still does and our subscription topic
		// is configured to match if either the old version matches or the new version matches
		awaitUntilReceivedTransactionCount(5);

		Observation observation3a = myClient.read().resource(Observation.class).withId(observationTemp3Id).execute();

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(OBS_CODE);
		coding1.setSystem("SNOMED-CT");
		ourLog.info(">>>9 Send obs");
		updateResource(observation3a, true);

		// Should see exactly one subscription notification
		awaitUntilReceivedTransactionCount(6);

		assertThat(subscription2.getId()).isNotEqualTo(subscription1.getId());
		assertThat(sentObservation1.getId()).isNotEmpty();
		assertThat(observation2.getId()).isNotEmpty();
	}

	private void deleteSubscription(Subscription subscription2) throws InterruptedException {
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
	}

	private void awaitUntilReceivedTransactionCount(int theExpected) {
		String list = getReceivedObservations().stream()
			.map(t -> t.getIdElement().toUnqualifiedVersionless().getValue() + " " + t.getCode().getCodingFirstRep().getCode())
			.collect(Collectors.joining(", "));
		String errorMessage = "Expected " + theExpected + " transactions, have " + getSystemProviderCount() + ": " + list;
		await(errorMessage).until(() -> getSystemProviderCount() == theExpected);
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDatabase() throws Exception {
		// Same test as above, but now run it using database matching
		mySubscriptionSettings.setEnableInMemorySubscriptionMatching(false);
		testRestHookSubscriptionApplicationJson();
	}

	@Nonnull
	private Subscription createTopicSubscription(String theTopicUrlSuffix) throws InterruptedException {
		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + theTopicUrlSuffix, Constants.CT_FHIR_JSON_NEW);

		return postSubscription(subscription);
	}

	@Test
	public void testSubscriptionTriggerViaSubscription() throws Exception {
		createSubscriptionTopic();

		Subscription subscription1 = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, Constants.CT_FHIR_XML_NEW);

		Subscription subscription = postSubscription(subscription1);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");

		Observation sentObservation = new Observation();
		sentObservation.addIdentifier().setSystem("foo").setValue("bar1");
		sentObservation.setId(IdType.newRandomUuid().getValue());
		CodeableConcept codeableConcept = new CodeableConcept()
			.addCoding(new Coding().setCode(OBS_CODE).setSystem("SNOMED-CT"));
		sentObservation.setCode(codeableConcept);
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("foo").setValue("bar2");
		patient.setId(IdType.newRandomUuid().getValue());
		patient.setActive(true);
		sentObservation.getSubject().setReference(patient.getId());

		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		requestBundle.addEntry()
			.setResource(sentObservation)
			.setFullUrl(sentObservation.getId())
			.getRequest()
			.setUrl("Observation?identifier=foo|bar1")
			.setMethod(Bundle.HTTPVerb.PUT);
		requestBundle.addEntry()
			.setResource(patient)
			.setFullUrl(patient.getId())
			.getRequest()
			.setUrl("Patient?identifier=foo|bar2")
			.setMethod(Bundle.HTTPVerb.PUT);

		sendTransaction(requestBundle, true);

		// Should see 1 subscription notification
		awaitUntilReceivedTransactionCount(1);
		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation, 1L);
		assertEquals(Constants.CT_FHIR_XML_NEW, getLastSystemProviderContentType());

		ourLog.debug("Observation content: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(receivedObs));
	}

	@Test
	public void testUpdateSubscriptionToMatchLater() throws Exception {
		SubscriptionTopic subscriptionTopic = createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(1);

		ourLog.info("** About to create non-matching subscription");

		Subscription subscription1 = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE2, Constants.CT_FHIR_XML_NEW);

		Subscription subscription = postSubscription(subscription1);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation that wont match");

		sendObservation(OBS_CODE, "SNOMED-CT", false);
		awaitUntilReceivedTransactionCount(0);

		ourLog.info("** About to update subscription topic");
		SubscriptionTopic subscriptionTopicTemp = myClient.read(SubscriptionTopic.class, subscriptionTopic.getId());
		assertNotNull(subscriptionTopicTemp);
		setSubscriptionTopicCriteria(subscriptionTopicTemp, "Observation?code=SNOMED-CT|" + OBS_CODE);
		updateResource(subscriptionTopicTemp, false);

		ourLog.info("** About to send Observation 2");
		sendObservationExpectDelivery();

		// Should see a subscription notification this time
		awaitUntilReceivedTransactionCount(1);

		deleteSubscription(subscription);

		sendObservation(OBS_CODE, "SNOMED-CT", false);

		// No more matches
		awaitUntilReceivedTransactionCount(1);
	}

	private static void setSubscriptionTopicCriteria(SubscriptionTopic subscriptionTopicTemp, String theCriteria) {
		subscriptionTopicTemp.getResourceTriggerFirstRep().getQueryCriteria().setCurrent(theCriteria);
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription3 = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, Constants.CT_FHIR_XML_NEW);

		postSubscription(subscription3);
		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE2, Constants.CT_FHIR_XML_NEW);

		postSubscription(subscription);
		waitForActivatedSubscriptionCount(2);

		sendObservationExpectDelivery();

		// Should see 1 subscription notification
		awaitUntilReceivedTransactionCount(1);
		assertEquals(Constants.CT_FHIR_XML_NEW, getLastSystemProviderContentType());
	}

	@Test
	public void testRestHookTopicSubscriptionInvalidTopic() throws Exception {
		try {
			createTopicSubscription(OBS_CODE);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(2322) + "No SubscriptionTopic exists with topic: " + SUBSCRIPTION_TOPIC_TEST_URL + OBS_CODE, e.getMessage());
		}
	}

	@Test
	public void testRestHookSubscriptionTopicInvalidCriteria() throws Exception {
		try {
			createSubscriptionTopicWithCriteria("Observation?codeeeee=SNOMED-CT");
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(2339) + "Invalid SubscriptionTopic criteria 'Observation?codeeeee=SNOMED-CT' in SubscriptionTopic.resourceTrigger.queryCriteria.current: HAPI-0488: Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
		}
	}

	@Nonnull
	private SubscriptionTopic createSubscriptionTopicWithCriteria(String theCriteria) throws InterruptedException {
		SubscriptionTopic subscriptionTopic = buildSubscriptionTopic(CUSTOM_URL);
		setSubscriptionTopicCriteria(subscriptionTopic, theCriteria);
		return createSubscriptionTopic(subscriptionTopic);
	}

	@Test
	public void testDisableSubscription() throws Exception {
		createSubscriptionTopic();

		Subscription subscription = createTopicSubscription();
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservationExpectDelivery();

		// Should see 1 subscription notification
		awaitUntilReceivedTransactionCount(1);
		assertBundleAndGetObservation(subscription, sentObservation, 1L);

		// Disable
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.OFF);
		updateResource(subscription, false);

		// Send another observation
		sendObservation(OBS_CODE, "SNOMED-CT", false);

		// Should see no new delivery
		awaitUntilReceivedTransactionCount(1);
	}

	@Test
	public void testInvalidProvenanceParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String criteriabad = "Provenance?foo=https://hl7.org/fhir/v3/DocumentCompletion%7CAU";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	@Test
	public void testInvalidProcedureRequestParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String criteriabad = "ProcedureRequest?intent=instance-order&category=Laboratory";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	@Test
	public void testInvalidBodySiteParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String criteriabad = "BodySite?accessType=Catheter";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	@Test
	public void testGoodSubscriptionPersists() throws Exception {
		createSubscriptionTopic();

		assertEquals(0, subscriptionCount());
		createTopicSubscription();
		waitForActivatedSubscriptionCount(1);
		assertEquals(1, subscriptionCount());
	}

	private int subscriptionCount() {
		IBaseBundle found = myClient.search().forResource(Subscription.class).cacheControl(new CacheControlDirective().setNoCache(true)).execute();
		return toUnqualifiedVersionlessIdValues(found).size();
	}

	@Test
	public void testSubscriptionTopicWithNoStatusIsRejected() throws InterruptedException {
		SubscriptionTopic subscriptionTopic = buildSubscriptionTopic(OBS_CODE);
		subscriptionTopic.setStatus(null);

		try {
			createSubscriptionTopic(subscriptionTopic);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Can not process submitted SubscriptionTopic - SubscriptionTopic.status must be populated on this server");
		}
	}


	@Test
	public void testBadSubscriptionTopicDoesntPersist() throws InterruptedException {
		assertEquals(0, subscriptionCount());
		String criteriaBad = "BodySite?accessType=Catheter";
		try {
			createSubscriptionTopicWithCriteria(criteriaBad);
		} catch (UnprocessableEntityException e) {
			ourLog.info("Expected exception", e);
		}
		assertEquals(0, subscriptionCount());
	}

	@Test
	public void testCustomSearchParam() throws Exception {
		String criteria = "Observation?accessType=Catheter,PD%20Catheter";

		SearchParameter sp = new SearchParameter();
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		sp.setCode("accessType");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Observation.extension('Observation#accessType')");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		mySearchParameterDao.create(sp, mySrd);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		mySearchParamRegistry.forceRefresh();
		createSubscriptionTopicWithCriteria(criteria);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(CUSTOM_URL);
		waitForActivatedSubscriptionCount(1);

		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("Catheter"));
			createResource(observation, true);
			awaitUntilReceivedTransactionCount(1);
			assertBundleAndGetObservation(subscription, observation, 1L);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("PD Catheter"));
			createResource(observation, true);
			awaitUntilReceivedTransactionCount(2);
			assertBundleAndGetObservation(subscription, observation, 2L);
		}
		{
			Observation observation = new Observation();
			createResource(observation, false);
			awaitUntilReceivedTransactionCount(2);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("XXX"));
			createResource(observation, false);
			awaitUntilReceivedTransactionCount(2);
		}
	}

	private Observation assertBundleAndGetObservation(Subscription subscription, Observation sentObservation, Long theExpectedEventNumber) {
		Bundle receivedBundle = getLastSystemProviderBundle();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertThat(resources).hasSize(2);

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentObservation, ss, theExpectedEventNumber);

		return (Observation) resources.get(1);
	}

	private SubscriptionTopic createObservationSubscriptionTopic(String theCode) throws InterruptedException {
		SubscriptionTopic subscriptionTopic = buildSubscriptionTopic(theCode);
		return createSubscriptionTopic(subscriptionTopic);
	}

	@Nonnull
	private static SubscriptionTopic buildSubscriptionTopic(String theCode) {
		SubscriptionTopic retval = new SubscriptionTopic();
		retval.setUrl(SUBSCRIPTION_TOPIC_TEST_URL + theCode);
		retval.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = retval.addResourceTrigger();
		trigger.setResource("Observation");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria = trigger.getQueryCriteria();
		queryCriteria.setCurrent("Observation?code=SNOMED-CT|" + theCode);
		queryCriteria.setRequireBoth(false);
		return retval;
	}

	private Observation sendObservation(String theCode, String theSystem, boolean theExpectDelivery) throws InterruptedException {
		return sendObservation(theCode, theSystem, theExpectDelivery, 1);
	}

	private Observation sendObservation(String theCode, String theSystem, boolean theExpectDelivery, int theCount) throws InterruptedException {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		observation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		Coding coding = codeableConcept.addCoding();
		coding.setCode(theCode);
		coding.setSystem(theSystem);

		observation.setStatus(Enumerations.ObservationStatus.FINAL);

		IIdType id = createResource(observation, theExpectDelivery, theCount);
		observation.setId(id);

		return observation;
	}

}
