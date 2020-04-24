package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.jpa.config.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestR4Test extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR4Test.class);

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@After
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		ourLog.info("@After");
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
	}

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload);
		createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testUpdatesHaveCorrectMetadata() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		/*
		 * Send version 1
		 */

		Observation obs = sendObservation(code, "SNOMED-CT");
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		waitForQueueToDrain();
		int idx = 0;
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(idx));
		assertEquals("1", ourUpdatedObservations.get(idx).getIdElement().getVersionIdPart());
		assertEquals("1", ourUpdatedObservations.get(idx).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", ourUpdatedObservations.get(idx).getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		obs.getIdentifierFirstRep().setSystem("foo").setValue("2");
		myObservationDao.update(obs);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		waitForQueueToDrain();
		idx++;
		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(idx));
		assertEquals("2", ourUpdatedObservations.get(idx).getIdElement().getVersionIdPart());
		assertEquals("2", ourUpdatedObservations.get(idx).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", ourUpdatedObservations.get(idx).getIdentifierFirstRep().getValue());
	}

	@Test
	public void testPlaceholderReferencesInTransactionAreResolvedCorrectly() throws Exception {

		String payload = "application/fhir+json";
		String code = "1000000050";
		String criteria1 = "Observation?";
		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		// Create a transaction that should match
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.getIdentifierFirstRep().setSystem("foo").setValue("AAA");
		bundle.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		Observation observation = new Observation();
		observation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getSubject().setReference(patient.getId());
		bundle.addEntry().setResource(observation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");

		// Send the transaction
		mySystemDao.transaction(null, bundle);

		waitForSize(1, ourUpdatedObservations);

		assertThat(ourUpdatedObservations.get(0).getSubject().getReference(), matchesPattern("Patient/[0-9]+"));
	}

	@Test
	public void testUpdatesHaveCorrectMetadataUsingTransactions() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		/*
		 * Send version 1
		 */

		Observation observation = new Observation();
		observation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(observation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
		Bundle responseBundle = mySystemDao.transaction(null, bundle);

		Observation obs = myObservationDao.read(new IdType(responseBundle.getEntry().get(0).getResponse().getLocation()));

		// Should see 1 subscription notification
		waitForQueueToDrain();
		int idx = 0;
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(idx));
		assertEquals("1", ourUpdatedObservations.get(idx).getIdElement().getVersionIdPart());
		assertEquals("1", ourUpdatedObservations.get(idx).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", ourUpdatedObservations.get(idx).getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		observation = new Observation();
		observation.setId(obs.getId());
		observation.getIdentifierFirstRep().setSystem("foo").setValue("2");
		observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setResource(observation).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl(obs.getIdElement().toUnqualifiedVersionless().getValue());
		mySystemDao.transaction(null, bundle);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		waitForQueueToDrain();
		idx++;
		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(idx));
		assertEquals("2", ourUpdatedObservations.get(idx).getIdElement().getVersionIdPart());
		assertEquals("2", ourUpdatedObservations.get(idx).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourUpdatedObservations.get(idx).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", ourUpdatedObservations.get(idx).getIdentifierFirstRep().getValue());
	}

	@Test
	public void testRepeatedDeliveries() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		for (int i = 0; i < 100; i++) {
			Observation observation = new Observation();
			observation.getIdentifierFirstRep().setSystem("foo").setValue("ID" + i);
			observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
			observation.setStatus(Observation.ObservationStatus.FINAL);
			myObservationDao.create(observation);
		}

		waitForSize(100, ourUpdatedObservations);
	}


	@Test
	public void testSubscriptionRegistryLoadsSubscriptionsFromDatabase() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		// Manually unregister all subscriptions
		mySubscriptionRegistry.unregisterAllSubscriptions();
		waitForActivatedSubscriptionCount(0);

		// Force a reload
		mySubscriptionLoader.doSyncSubscriptionsForUnitTest();

		// Send a matching observation
		Observation observation = new Observation();
		observation.getIdentifierFirstRep().setSystem("foo").setValue("ID");
		observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(observation);

		waitForSize(1, ourUpdatedObservations);
	}

	@Test
	public void testActiveSubscriptionShouldntReActivate() throws Exception {
		String criteria = "Observation?code=111111111&_format=xml";
		String payload = "application/fhir+json";
		createSubscription(criteria, payload);

		waitForActivatedSubscriptionCount(1);
		for (int i = 0; i < 5; i++) {
			int changes = this.mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
			assertEquals(0, changes);
		}
	}

	@Test
	public void testRestHookSubscriptionNoopUpdateDoesntTriggerNewDelivery() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload);
		createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		Observation obs = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		// Send an update with no changes
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		ourClient.update().resource(obs).execute();

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);


	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDisableVersionIdInDelivery() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		waitForActivatedSubscriptionCount(0);
		Subscription subscription1 = createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);


		ourLog.info("** About to send observation");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		IdType idElement = ourUpdatedObservations.get(0).getIdElement();
		assertEquals(observation1.getIdElement().getIdPart(), idElement.getIdPart());
		// VersionId is present
		assertEquals(observation1.getIdElement().getVersionIdPart(), idElement.getVersionIdPart());

		subscription1
			.getChannel()
			.addExtension(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS, new BooleanType("true"));
		ourLog.info("** About to update subscription");

		int modCount = (int) myCountingInterceptor.getSentCount("Subscription");
		ourClient.update().resource(subscription1).execute();
		waitForSize(modCount + 2, () -> myCountingInterceptor.getSentCount("Subscription"), () -> myCountingInterceptor.toString());

		ourLog.info("** About to send observation");
		Observation observation2 = sendObservation(code, "SNOMED-CT");

		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(1));

		idElement = ourUpdatedObservations.get(1).getIdElement();
		assertEquals(observation2.getIdElement().getIdPart(), idElement.getIdPart());
		// Now VersionId is stripped
		assertEquals(null, idElement.getVersionIdPart());
	}

	@Test
	public void testRestHookSubscriptionDoesntGetLatestVersionByDefault() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		waitForActivatedSubscriptionCount(0);
		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation observation = sendObservation(code, "SNOMED-CT");
		assertEquals("1", observation.getIdElement().getVersionIdPart());
		assertNull(observation.getNoteFirstRep().getText());

		observation.getNoteFirstRep().setText("changed");
		MethodOutcome methodOutcome = ourClient.update().resource(observation).execute();
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", observation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();


		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);

		Observation observation1 = ourUpdatedObservations.get(0);
		Observation observation2 = ourUpdatedObservations.get(1);

		assertEquals("1", observation1.getIdElement().getVersionIdPart());
		assertNull(observation1.getNoteFirstRep().getText());
		assertEquals("2", observation2.getIdElement().getVersionIdPart());
		assertEquals("changed", observation2.getNoteFirstRep().getText());
	}

	@Test
	public void testRestHookSubscriptionGetsLatestVersionWithFlag() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		waitForActivatedSubscriptionCount(0);

		Subscription subscription = newSubscription(criteria1, payload);
		subscription
			.getChannel()
			.addExtension(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION, new BooleanType("true"));
		ourClient.create().resource(subscription).execute();

		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation observation = sendObservation(code, "SNOMED-CT");
		assertEquals("1", observation.getIdElement().getVersionIdPart());
		assertNull(observation.getNoteFirstRep().getText());

		observation.getNoteFirstRep().setText("changed");
		MethodOutcome methodOutcome = ourClient.update().resource(observation).execute();
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", observation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();


		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);

		Observation observation1 = ourUpdatedObservations.get(0);
		Observation observation2 = ourUpdatedObservations.get(1);

		assertEquals("2", observation1.getIdElement().getVersionIdPart());
		assertEquals("changed", observation1.getNoteFirstRep().getText());
		assertEquals("2", observation2.getIdElement().getVersionIdPart());
		assertEquals("changed", observation2.getNoteFirstRep().getText());
	}

	@Test
	public void testRestHookSubscriptionApplicationJson() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload);
		Subscription subscription2 = createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		assertEquals("1", ourUpdatedObservations.get(0).getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForActivatedSubscriptionCount(1);

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see only one subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(observation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDatabase() throws Exception {
		// Same test as above, but now run it using database matching
		myDaoConfig.setEnableInMemorySubscriptionMatching(false);
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload);
		Subscription subscription2 = createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		assertEquals("1", ourUpdatedObservations.get(0).getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForQueueToDrain();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see only one subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(observation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testRestHookSubscriptionApplicationXml() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload);
		Subscription subscription2 = createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		ourLog.info("** About to send obervation");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(observation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testSubscriptionTriggerViaSubscription() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send obervation");

		Observation observation = new Observation();
		observation.addIdentifier().setSystem("foo").setValue("bar1");
		observation.setId(IdType.newRandomUuid().getValue());
		CodeableConcept codeableConcept = new CodeableConcept()
			.addCoding(new Coding().setCode(code).setSystem("SNOMED-CT"));
		observation.setCode(codeableConcept);
		observation.setStatus(Observation.ObservationStatus.FINAL);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("foo").setValue("bar2");
		patient.setId(IdType.newRandomUuid().getValue());
		patient.setActive(true);
		observation.getSubject().setReference(patient.getId());

		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		requestBundle.addEntry()
			.setResource(observation)
			.setFullUrl(observation.getId())
			.getRequest()
			.setUrl("Obervation?identifier=foo|bar1")
			.setMethod(Bundle.HTTPVerb.PUT);
		requestBundle.addEntry()
			.setResource(patient)
			.setFullUrl(patient.getId())
			.getRequest()
			.setUrl("Patient?identifier=foo|bar2")
			.setMethod(Bundle.HTTPVerb.PUT);
		ourClient.transaction().withBundle(requestBundle).execute();

		// Should see 1 subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));

		Observation obs = ourUpdatedObservations.get(0);
		ourLog.info("Observation content: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obs));
	}

	@Test
	public void testUpdateSubscriptionToMatchLater() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteriaBad = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		ourLog.info("** About to create non-matching subscription");

		Subscription subscription2 = createSubscription(criteriaBad, payload);

		ourLog.info("** About to send observation that wont match");

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Criteria didn't match, shouldn't see any updates
		waitForQueueToDrain();
		Thread.sleep(1000);
		assertEquals(0, ourUpdatedObservations.size());

		Subscription subscriptionTemp = ourClient.read().resource(Subscription.class).withId(subscription2.getId()).execute();
		Assert.assertNotNull(subscriptionTemp);
		String criteriaGood = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		subscriptionTemp.setCriteria(criteriaGood);
		ourLog.info("** About to update subscription");
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		ourLog.info("** About to send Observation 2");
		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see a subscription notification this time
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// No more matches
		Thread.sleep(1000);
		assertEquals(1, ourUpdatedObservations.size());
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload);
		Subscription subscription2 = createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionInvalidCriteria() throws Exception {
		String payload = "application/xml";

		String criteria1 = "Observation?codeeeee=SNOMED-CT";

		try {
			createSubscription(criteria1, payload);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Invalid subscription criteria submitted: Observation?codeeeee=SNOMED-CT Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
		}
	}

	@Test
	public void testSubscriptionWithHeaders() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		// Add some headers, and we'll also turn back to requested status for fun
		Subscription subscription = createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		subscription.getChannel().addHeader("X-Foo: FOO");
		subscription.getChannel().addHeader("X-Bar: BAR");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		ourClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
		assertThat(ourHeaders, hasItem("X-Foo: FOO"));
		assertThat(ourHeaders, hasItem("X-Bar: BAR"));
	}

	@Test
	public void testDisableSubscription() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		Subscription subscription = createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		// Disable
		subscription.setStatus(Subscription.SubscriptionStatus.OFF);
		ourClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		// Send another object
		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

	}

	@Test(expected = UnprocessableEntityException.class)
	public void testInvalidProvenanceParam() {
		String payload = "application/fhir+json";
		String criteriabad = "Provenance?activity=http://hl7.org/fhir/v3/DocumentCompletion%7CAU";
		Subscription subscription = newSubscription(criteriabad, payload);
		ourClient.create().resource(subscription).execute();
	}

	@Test(expected = UnprocessableEntityException.class)
	public void testInvalidProcedureRequestParam() {
		String payload = "application/fhir+json";
		String criteriabad = "ProcedureRequest?intent=instance-order&category=Laboratory";
		Subscription subscription = newSubscription(criteriabad, payload);
		ourClient.create().resource(subscription).execute();
	}

	@Test(expected = UnprocessableEntityException.class)
	public void testInvalidBodySiteParam() {
		String payload = "application/fhir+json";
		String criteriabad = "BodySite?accessType=Catheter";
		Subscription subscription = newSubscription(criteriabad, payload);
		ourClient.create().resource(subscription).execute();
	}

	@Test
	public void testGoodSubscriptionPersists() {
		assertEquals(0, subscriptionCount());
		String payload = "application/fhir+json";
		String criteriaGood = "Patient?gender=male";
		Subscription subscription = newSubscription(criteriaGood, payload);
		ourClient.create().resource(subscription).execute();
		await().until(() -> subscriptionCount() == 1);
	}

	/**
	 * Make sure we don't activate a subscription if its type is incorrect
	 */
	@Test
	public void testSubscriptionDoesntActivateIfRestHookIsNotEnabled() throws InterruptedException {
		Set<org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType> existingSupportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();
		myDaoConfig.clearSupportedSubscriptionTypesForUnitTest();
		try {

			Subscription subscription = newSubscription("Observation?", "application/fhir+json");
			IIdType id = ourClient.create().resource(subscription).execute().getId().toUnqualifiedVersionless();

			Thread.sleep(1000);
			subscription = ourClient.read().resource(Subscription.class).withId(id).execute();
			assertEquals(Subscription.SubscriptionStatus.REQUESTED, subscription.getStatus());

		} finally {
			existingSupportedSubscriptionTypes.forEach(t -> myDaoConfig.addSupportedSubscriptionType(t));
		}
	}


	private int subscriptionCount() {
		IBaseBundle found = ourClient.search().forResource(Subscription.class).cacheControl(new CacheControlDirective().setNoCache(true)).execute();
		return toUnqualifiedVersionlessIdValues(found).size();
	}

	@Test
	public void testSubscriptionWithNoStatusIsRejected() {
		Subscription subscription = newSubscription("Observation?", "application/json");
		subscription.setStatus(null);

		try {
			ourClient.create().resource(subscription).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not process submitted Subscription - Subscription.status must be populated on this server"));
		}
	}


	@Test
	public void testBadSubscriptionDoesntPersist() {
		assertEquals(0, subscriptionCount());
		String payload = "application/fhir+json";
		String criteriaBad = "BodySite?accessType=Catheter";
		Subscription subscription = newSubscription(criteriaBad, payload);
		try {
			ourClient.create().resource(subscription).execute();
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
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();
		createSubscription(criteria, "application/json");
		waitForActivatedSubscriptionCount(1);

		{
			Observation bodySite = new Observation();
			bodySite.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("Catheter"));
			MethodOutcome methodOutcome = ourClient.create().resource(bodySite).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(1, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("PD Catheter"));
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("XXX"));
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}

	}


}
