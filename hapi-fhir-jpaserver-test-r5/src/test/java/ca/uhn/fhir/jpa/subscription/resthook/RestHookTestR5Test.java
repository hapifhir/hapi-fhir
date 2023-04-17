package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR5Test;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hamcrest.MatcherAssert;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestR5Test  extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR5Test.class);
	public static final String OBS_CODE = "1000000050";
	public static final String OBS_CODE2 = OBS_CODE + "111";


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

		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_XML_NEW);
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservation(OBS_CODE, "SNOMED-CT", true);

		// Should see 1 subscription notification
		assertEquals(1, getSystemProviderCount());

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation);
		assertEquals(Enumerations.ObservationStatus.FINAL, obs.getStatus());
		assertEquals(sentObservation.getIdElement(), obs.getIdElement());
	}

	@Test
	public void testUpdatesHaveCorrectMetadata() throws Exception {

		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);

		/*
		 * Send version 1
		 */

		Observation sentObservation = sendObservation(OBS_CODE, "SNOMED-CT");
		sentObservation = myObservationDao.read(sentObservation.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		assertEquals(1, getSystemProviderCount());

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation);

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		Assertions.assertEquals("1", receivedObs.getIdElement().getVersionIdPart());
		Assertions.assertEquals("1", receivedObs.getMeta().getVersionId());
		Assertions.assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals("1", receivedObs.getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("2");
		updateResource(sentObservation, true);
		sentObservation = myObservationDao.read(sentObservation.getIdElement().toUnqualifiedVersionless());

		// Should see a second subscription notification
		assertEquals(2, getSystemProviderCount());

		receivedObs = assertBundleAndGetObservation(subscription, sentObservation);

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		Assertions.assertEquals("2", receivedObs.getIdElement().getVersionIdPart());
		Assertions.assertEquals("2", receivedObs.getMeta().getVersionId());
		Assertions.assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals(sentObservation.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals("2", receivedObs.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testPlaceholderReferencesInTransactionAreResolvedCorrectly() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);

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

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation);

		MatcherAssert.assertThat(receivedObs.getSubject().getReference(), matchesPattern("Patient/[0-9]+"));
	}

	@Test
	public void testUpdatesHaveCorrectMetadataUsingTransactions() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);

		/*
		 * Send version 1
		 */

		Observation sentObservation = new Observation();
		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		sentObservation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(sentObservation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		// Send the transaction
		Bundle responseBundle = sendTransaction(bundle, true);
		assertEquals(1, getSystemProviderCount());

		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation);

		Observation obs = myObservationDao.read(new IdType(responseBundle.getEntry().get(0).getResponse().getLocation()));

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		Assertions.assertEquals("1", receivedObs.getIdElement().getVersionIdPart());
		Assertions.assertEquals("1", receivedObs.getMeta().getVersionId());
		Assertions.assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals("1", receivedObs.getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		sentObservation = new Observation();
		sentObservation.setId(obs.getId());
		sentObservation.getIdentifierFirstRep().setSystem("foo").setValue("2");
		sentObservation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		sentObservation.setStatus(Enumerations.ObservationStatus.FINAL);
		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(sentObservation).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl(obs.getIdElement().toUnqualifiedVersionless().getValue());
		// Send the transaction
		sendTransaction(bundle, true);
		assertEquals(2, getSystemProviderCount());

		receivedObs = assertBundleAndGetObservation(subscription, sentObservation);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());
		Assertions.assertEquals("2", receivedObs.getIdElement().getVersionIdPart());
		Assertions.assertEquals("2", receivedObs.getMeta().getVersionId());
		Assertions.assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), receivedObs.getMeta().getLastUpdatedElement().getValueAsString());
		Assertions.assertEquals("2", receivedObs.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testRepeatedDeliveries() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		createTopicSubscription(OBS_CODE);
		waitForActivatedSubscriptionCount(1);

		mySubscriptionTopicsCheckedLatch.setExpectedCount(100);
		mySubscriptionDeliveredLatch.setExpectedCount(100);
		// WIP STR5 I don't know the answer to this, but should we be bunching these up into a single delivery?
		for (int i = 0; i < 100; i++) {
			Observation observation = new Observation();
			observation.getIdentifierFirstRep().setSystem("foo").setValue("ID" + i);
			observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
			observation.setStatus(Enumerations.ObservationStatus.FINAL);
			myObservationDao.create(observation);
		}
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		mySubscriptionDeliveredLatch.awaitExpected();
	}

	@Test
	public void testActiveSubscriptionShouldntReActivate() throws Exception {
		// FIXME extract method
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		// FIXME extract method
		createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		for (int i = 0; i < 5; i++) {
			int changes = this.mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
			assertEquals(0, changes);
		}
	}

	@Test
	public void testRestHookSubscriptionNoopUpdateDoesntTriggerNewDelivery() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservation(OBS_CODE, "SNOMED-CT");

		assertEquals(1, getSystemProviderCount());

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation);

		// Should see 1 subscription notification
		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		// Send an update with no changes
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myObservationDao.update(obs, mySrd);

		Thread.sleep(1000);
		// Should be no further deliveries
		assertEquals(1, getSystemProviderCount());
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDisableVersionIdInDelivery() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		waitForActivatedSubscriptionCount(0);
		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");
		Observation sentObservation1 = sendObservation(code, "SNOMED-CT");

		assertEquals(1, getSystemProviderCount());

		Observation obs = assertBundleAndGetObservation(subscription, sentObservation1);

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

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
		Observation sentObservation2 = sendObservation(code, "SNOMED-CT");

		assertEquals(2, getSystemProviderCount());

		Observation obs2 = assertBundleAndGetObservation(subscription, sentObservation2);

		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		idElement =obs2.getIdElement();
		assertEquals(sentObservation2.getIdElement().getIdPart(), idElement.getIdPart());
		// Now VersionId is stripped
		assertEquals(null, idElement.getVersionIdPart());
	}

	@Test
	public void testRestHookSubscriptionDoesntGetLatestVersionByDefault() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation sentObservation = sendObservation(code, "SNOMED-CT", false);
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

		assertEquals(2, getSystemProviderCount());

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
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL, Constants.CT_FHIR_JSON_NEW);
		subscription
			.addExtension(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION, new BooleanType("true"));
		postSubscription(subscription);
		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation sentObservation = sendObservation(code, "SNOMED-CT", false);
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
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription1 = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		Subscription subscription2 = createTopicSubscription(OBS_CODE2, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(2);

		Observation sentObservation1 = sendObservation(code, "SNOMED-CT", true);
		assertEquals(1, getSystemProviderCount());
		Observation receivedObs = assertBundleAndGetObservation(subscription1, sentObservation1);
		assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		Assertions.assertEquals("1", receivedObs.getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);
		subscriptionTemp.setTopic(subscription1.getTopic());
		updateResource(subscriptionTemp, false);

		Observation observation2 = sendObservation(code, "SNOMED-CT");

		assertEquals(3, getSystemProviderCount());

		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		assertEquals(4, getSystemProviderCount());

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		updateResource(observation3, false);

		// Should see no subscription notification
		assertEquals(4, getSystemProviderCount());

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		updateResource(observation3a, true);

		// Should see only one subscription notification
		assertEquals(5, getSystemProviderCount());

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(sentObservation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDatabase() throws Exception {
		// Same test as above, but now run it using database matching
		myStorageSettings.setEnableInMemorySubscriptionMatching(false);
		testRestHookSubscriptionApplicationJson();
	}

	@Nonnull
	private Subscription createTopicSubscription(String theTopicUrlSuffix) throws InterruptedException {
		return createTopicSubscription(theTopicUrlSuffix, Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	public void testSubscriptionTriggerViaSubscription() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_XML_NEW);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");

		Observation sentObservation = new Observation();
		sentObservation.addIdentifier().setSystem("foo").setValue("bar1");
		sentObservation.setId(IdType.newRandomUuid().getValue());
		CodeableConcept codeableConcept = new CodeableConcept()
			.addCoding(new Coding().setCode(code).setSystem("SNOMED-CT"));
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
		assertEquals(1, getSystemProviderCount());
		Observation receivedObs = assertBundleAndGetObservation(subscription, sentObservation);
		assertEquals(Constants.CT_FHIR_XML_NEW, getLastSystemProviderContentType());

		ourLog.debug("Observation content: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(receivedObs));
	}

	@Test
	public void testUpdateSubscriptionToMatchLater() throws Exception {
		SubscriptionTopic subscriptionTopic = createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(1);

		ourLog.info("** About to create non-matching subscription");

		Subscription subscription = createTopicSubscription(OBS_CODE2, Constants.CT_FHIR_XML_NEW);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation that wont match");

		Observation observation1 = sendObservation(OBS_CODE, "SNOMED-CT", false);
		assertEquals(0, getSystemProviderCount());
		Assertions.assertEquals(0, BaseSubscriptionsR5Test.ourUpdatedObservations.size());

		ourLog.info("** About to update subscription topic");
		SubscriptionTopic subscriptionTopicTemp = myClient.read(SubscriptionTopic.class, subscriptionTopic.getId());
		assertNotNull(subscriptionTopicTemp);
		setSubscriptionTopicCriteria(subscriptionTopicTemp, "Observation?code=SNOMED-CT|" + OBS_CODE);
		updateResource(subscriptionTopicTemp, false);

		ourLog.info("** About to send Observation 2");
		Observation observation2 = sendObservation(OBS_CODE, "SNOMED-CT", true);

		// Should see a subscription notification this time
		assertEquals(1, getSystemProviderCount());

		myClient.delete().resourceById(new IdType("Subscription/" + subscription.getId())).execute();

		Observation observationTemp3 = sendObservation(OBS_CODE, "SNOMED-CT", false);

		// No more matches
		assertEquals(1, getSystemProviderCount());
	}

	private static void setSubscriptionTopicCriteria(SubscriptionTopic subscriptionTopicTemp, String theCriteria) {
		subscriptionTopicTemp.getResourceTriggerFirstRep().getQueryCriteria().setCurrent(theCriteria);
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		createObservationSubscriptionTopic(OBS_CODE2);
		waitForRegisteredSubscriptionTopicCount(2);

		Subscription subscription1 = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_XML_NEW);
		Subscription subscription2 = createTopicSubscription(OBS_CODE2, Constants.CT_FHIR_XML_NEW);
		waitForActivatedSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		assertEquals(1, getSystemProviderCount());
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
			// FIXME code
			assertEquals("HTTP 422 Unprocessable Entity: " +  "Invalid SubscriptionTopic criteria 'Observation?codeeeee=SNOMED-CT' in SubscriptionTopic.resourceTrigger.queryCriteria.current: HAPI-0488: Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
		}
	}

	@Nonnull
	private SubscriptionTopic createSubscriptionTopicWithCriteria(String theCriteria) throws InterruptedException {
		SubscriptionTopic subscriptionTopic = buildSubscriptionTopic("will-be-replaced");
		setSubscriptionTopicCriteria(subscriptionTopic, theCriteria);
		return createSubscriptionTopic(subscriptionTopic);
	}

	@Test
	public void testSubscriptionWithHeaders() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		// Add some headers, and we'll also turn back to requested status for fun
		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		subscription.addHeader("X-Foo: FOO");
		subscription.addHeader("X-Bar: BAR");
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.REQUESTED);
		updateResource(subscription, false);

		Observation sentObservation = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		assertEquals(1, getSystemProviderCount());
		Observation receivedObservation = assertBundleAndGetObservation(subscription, sentObservation);
		Assertions.assertEquals(Constants.CT_FHIR_JSON_NEW, getLastSystemProviderContentType());

		assertThat(getLastSystemProviderHeaders(), hasItem("X-Foo: FOO"));
		assertThat(getLastSystemProviderHeaders(), hasItem("X-Bar: BAR"));
	}

	@Test
	public void testDisableSubscription() throws Exception {
		String code = OBS_CODE;
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
		waitForActivatedSubscriptionCount(1);

		Observation sentObservation = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		assertEquals(1, getSystemProviderCount());
		Observation receivedObservation = assertBundleAndGetObservation(subscription, sentObservation);

		// Disable
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.OFF);
		updateResource(subscription, false);

		// Send another observation
		sendObservation(OBS_CODE, "SNOMED-CT", false);

		// Should see no new delivery
		assertEquals(1, getSystemProviderCount());
	}

	@Test
	public void testInvalidProvenanceParam() {
		assertThrows(UnprocessableEntityException.class, () -> {
			String criteriabad = "Provenance?foo=http://hl7.org/fhir/v3/DocumentCompletion%7CAU";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	@Test
	public void testInvalidProcedureRequestParam() {
		assertThrows(UnprocessableEntityException.class, () -> {
			String criteriabad = "ProcedureRequest?intent=instance-order&category=Laboratory";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	@Test
	public void testInvalidBodySiteParam() {
		assertThrows(UnprocessableEntityException.class, () -> {
			String criteriabad = "BodySite?accessType=Catheter";
			createSubscriptionTopicWithCriteria(criteriabad);
		});
	}

	// FIXME pass up to here

	@Test
	public void testGoodSubscriptionPersists() throws Exception {
		createObservationSubscriptionTopic(OBS_CODE);
		waitForRegisteredSubscriptionTopicCount(1);

		assertEquals(0, subscriptionCount());
		Subscription subscription = createTopicSubscription(OBS_CODE, Constants.CT_FHIR_JSON_NEW);
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
			assertThat(e.getMessage(), containsString("Can not process submitted SubscriptionTopic - SubscriptionTopic.status must be populated on this server"));
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
		sp.addBase("Observation");
		sp.setCode("accessType");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Observation.extension('Observation#accessType')");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();
		createTopicSubscription(OBS_CODE, "application/json");
		waitForActivatedSubscriptionCount(1);

		{
			Observation bodySite = new Observation();
			bodySite.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("Catheter"));
			MethodOutcome methodOutcome = myClient.create().resource(bodySite).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(1, BaseSubscriptionsR5Test.ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("PD Catheter"));
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, BaseSubscriptionsR5Test.ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, BaseSubscriptionsR5Test.ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("XXX"));
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, BaseSubscriptionsR5Test.ourUpdatedObservations);
		}

	}

	private Observation assertBundleAndGetObservation(Subscription subscription, Observation sentObservation) {
		Bundle receivedBundle = getLastSystemProviderBundle();
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirCtx, receivedBundle);
		assertEquals(2, resources.size());

		SubscriptionStatus ss = (SubscriptionStatus) resources.get(0);
		validateSubscriptionStatus(subscription, sentObservation, ss);

		Observation obs = (Observation) resources.get(1);
		return obs;
	}

	private SubscriptionTopic createObservationSubscriptionTopic(String theCode) throws InterruptedException {
		SubscriptionTopic retval = buildSubscriptionTopic(theCode);
		createSubscriptionTopic(retval);

		return retval;
	}

	@Nonnull
	private static SubscriptionTopic buildSubscriptionTopic(String theCode) {
		SubscriptionTopic retval = new SubscriptionTopic();
		retval.setUrl(SUBSCRIPTION_TOPIC_TEST_URL+ theCode);
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


	private Subscription createTopicSubscription(String theTopicUrlSuffix, String thePayload) throws InterruptedException {
		// WIP STR5 will likely require matching TopicSubscription
		Subscription subscription = newTopicSubscription(SUBSCRIPTION_TOPIC_TEST_URL + theTopicUrlSuffix, thePayload);

		return postSubscription(subscription);
	}

	// FIXME KHS remove this method
	private Observation sendObservation(String theCode, String theSystem) throws InterruptedException {
		return sendObservation(theCode, theSystem, true);
	}

	private Observation sendObservation(String theCode, String theSystem, boolean theExpectDelivery) throws InterruptedException {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		observation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		Coding coding = codeableConcept.addCoding();
		coding.setCode(theCode);
		coding.setSystem(theSystem);

		observation.setStatus(Enumerations.ObservationStatus.FINAL);

		IIdType id = createResource(observation, theExpectDelivery);
		observation.setId(id);

		return observation;
	}

}
