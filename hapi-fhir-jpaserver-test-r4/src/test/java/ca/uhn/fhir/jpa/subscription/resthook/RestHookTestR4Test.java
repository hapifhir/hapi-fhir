package ca.uhn.fhir.jpa.subscription.resthook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicDispatcher;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.awaitility.Awaitility.await;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestR4Test extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RestHookTestR4Test.class);
	public static final String TEST_PATIENT_ID = "topic-test-patient-id";
	public static final String PATIENT_REFERENCE = "Patient/" + TEST_PATIENT_ID;

	@Autowired
	ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;
	@Autowired(required = false)
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	SubscriptionTopicDispatcher mySubscriptionTopicDispatcher;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		ourLog.info("@AfterEach");
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(new SubscriptionSettings().isTriggerSubscriptionsForNonVersioningChanges());
	}

	@Test
	public void testSubscriptionTopicRegistryBean() {
		// This bean should not exist in R4
		assertNull(mySubscriptionTopicRegistry);
	}

	/**
	 * Make sure that if we delete a subscription, then reinstate it with a criteria
	 * that changes the database mode we don't store both versioning modes
	 */
	@Test
	public void testReuseSubscriptionIdWithDifferentDatabaseMode() throws Exception {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);

		String payload = "application/fhir+json";
		IdType id = createSubscription("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER", payload, null, "sub").getIdElement().toUnqualifiedVersionless();
		waitForActivatedSubscriptionCount(1);

		Subscription subscription = mySubscriptionDao.read(id, mySrd);
		assertThat(subscription.getMeta().getTag()).hasSize(1);
		assertEquals("DATABASE", subscription.getMeta().getTag().get(0).getCode());

		mySubscriptionDao.delete(id, mySrd);
		waitForActivatedSubscriptionCount(0);

		payload = "application/fhir+json";
		id = createSubscription("Observation?", payload, null, "sub").getIdElement().toUnqualifiedVersionless();
		waitForActivatedSubscriptionCount(1);

		subscription = mySubscriptionDao.read(id, mySrd);
		assertThat(subscription.getMeta().getTag()).hasSize(1);
		assertEquals("IN_MEMORY", subscription.getMeta().getTag().get(0).getCode());
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
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

		Observation obs = sendObservation(code, "SNOMED-CT", "http://source-system.com", null);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getMeta().getVersionId());
		assertEquals("http://source-system.com", ourObservationProvider.getStoredResources().get(0).getMeta().getSource());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdentifierFirstRep().getValue());

		/*
		 * Send version 2
		 */

		obs.getIdentifierFirstRep().setSystem("foo").setValue("2");
		obs.getMeta().setSource("http://other-source");
		myObservationDao.update(obs);
		obs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(2);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getMeta().getVersionId());
		assertEquals("http://other-source", ourObservationProvider.getStoredResources().get(0).getMeta().getSource());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getIdentifierFirstRep().getValue());
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

		ourObservationProvider.waitForUpdateCount(1);

		assertThat(ourObservationProvider.getStoredResources().get(0).getSubject().getReference()).matches("Patient/[0-9]+");
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdentifierFirstRep().getValue());

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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(2);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getMeta().getVersionId());
		assertEquals(obs.getMeta().getLastUpdatedElement().getValueAsString(), ourObservationProvider.getStoredResources().get(0).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("2", ourObservationProvider.getStoredResources().get(0).getIdentifierFirstRep().getValue());
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

		ourObservationProvider.waitForUpdateCount(100);
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
		assertEquals(0, mySubscriptionRegistry.size());

		// Force a reload
		mySubscriptionLoader.doSyncSubscriptionsForUnitTest();

		// Send a matching observation
		Observation observation = new Observation();
		observation.getIdentifierFirstRep().setSystem("foo").setValue("ID");
		observation.getCode().addCoding().setCode(code).setSystem("SNOMED-CT");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		myObservationDao.create(observation);

		ourObservationProvider.waitForUpdateCount(1);
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
	public void testRestHookSubscriptionMetaAddDoesntTriggerNewDelivery() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload);
		createSubscription(criteria2, payload);
		waitForActivatedSubscriptionCount(2);

		ourLog.info("Sending an Observation");
		Observation obs = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		// Send a meta-add
		ourLog.info("Sending a meta-add");
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myClient.meta().add().onResource(obs.getIdElement()).meta(new Meta().addTag("http://blah", "blah", null)).execute();

		obs = myClient.read().resource(Observation.class).withId(obs.getIdElement().toUnqualifiedVersionless()).execute();
		Coding tag = obs.getMeta().getTag("http://blah", "blah");
		assertNotNull(tag);

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

		// Send a meta-delete
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myClient.meta().delete().onResource(obs.getIdElement()).meta(new Meta().addTag("http://blah", "blah", null)).execute();

		obs = myClient.read().resource(Observation.class).withId(obs.getIdElement().toUnqualifiedVersionless()).execute();
		tag = obs.getMeta().getTag("http://blah", "blah");
		assertNull(tag);

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

	}

	@Test
	public void testRestHookSubscriptionMetaAddDoesTriggerNewDeliveryIfConfiguredToDoSo() throws Exception {
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(true);

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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		// Send a meta-add
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myClient.meta().add().onResource(obs.getIdElement()).meta(new Meta().addTag("http://blah", "blah", null)).execute();

		obs = myClient.read().resource(Observation.class).withId(obs.getIdElement().toUnqualifiedVersionless()).execute();
		Coding tag = obs.getMeta().getTag("http://blah", "blah");
		assertNotNull(tag);

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(3);

		// Send a meta-delete
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myClient.meta().delete().onResource(obs.getIdElement()).meta(new Meta().addTag("http://blah", "blah", null)).execute();

		obs = myClient.read().resource(Observation.class).withId(obs.getIdElement().toUnqualifiedVersionless()).execute();
		tag = obs.getMeta().getTag("http://blah", "blah");
		assertNull(tag);

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(5);

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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		// Send an update with no changes
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		myClient.update().resource(obs).execute();

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);


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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		IdType idElement = ourObservationProvider.getStoredResources().get(0).getIdElement();
		assertEquals(observation1.getIdElement().getIdPart(), idElement.getIdPart());
		// VersionId is present
		assertEquals(observation1.getIdElement().getVersionIdPart(), idElement.getVersionIdPart());

		subscription1
			.getChannel()
			.addExtension(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS, new BooleanType("true"));
		ourLog.info("** About to update subscription");

		int modCount = myCountingInterceptor.getSentCount("Subscription");
		myClient.update().resource(subscription1).execute();
		waitForSize(modCount + 2, () -> myCountingInterceptor.getSentCount("Subscription"), () -> myCountingInterceptor.toString());

		ourLog.info("** About to send observation");
		Observation observation2 = sendObservation(code, "SNOMED-CT");

		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(2);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(1));

		idElement = ourObservationProvider.getResourceUpdates().get(1).getIdElement();
		assertEquals(observation2.getIdElement().getIdPart(), idElement.getIdPart());
		// Now VersionId is stripped
		assertNull(idElement.getVersionIdPart());
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
		MethodOutcome methodOutcome = myClient.update().resource(observation).execute();
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", observation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();


		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(2);

		Observation observation1 = ourObservationProvider.getResourceUpdates().stream().filter(t->t.getIdElement().getVersionIdPart().equals("1")).findFirst().orElseThrow(()->new IllegalArgumentException());
		Observation observation2 = ourObservationProvider.getResourceUpdates().stream().filter(t->t.getIdElement().getVersionIdPart().equals("2")).findFirst().orElseThrow(()->new IllegalArgumentException());

		assertEquals("1", observation1.getIdElement().getVersionIdPart());
		assertNull(observation1.getNoteFirstRep().getText());
		assertEquals("2", observation2.getIdElement().getVersionIdPart());
		assertEquals("changed", observation2.getNoteFirstRep().getText());
	}


	@ParameterizedTest
	@ValueSource(strings = {"[*]", "[Observation]", "Observation?"})
	public void RestHookSubscriptionWithPayloadSendsDeleteRequest(String theCriteria) throws Exception {
		String payload = "application/json";

		Extension sendDeleteMessagesExtension = new Extension()
			.setUrl(EX_SEND_DELETE_MESSAGES)
			.setValue(new BooleanType(true));

		waitForActivatedSubscriptionCount(0);
		createSubscription(theCriteria, payload, sendDeleteMessagesExtension);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");
		Observation observation = sendObservation("OB-01", "SNOMED-CT");
		assertEquals("1", observation.getIdElement().getVersionIdPart());
		ourObservationProvider.waitForUpdateCount(1);

		ourLog.info("** About to delete observation");
		myObservationDao.delete(IdDt.of(observation).toUnqualifiedVersionless());
		ourObservationProvider.waitForDeleteCount(1);
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
			.addExtension(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION, new BooleanType("true"));
		myClient.create().resource(subscription).execute();

		waitForActivatedSubscriptionCount(1);

		myStoppableSubscriptionDeliveringRestHookSubscriber.pause();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(countDownLatch);

		ourLog.info("** About to send observation");
		Observation observation = sendObservation(code, "SNOMED-CT");
		assertEquals("1", observation.getIdElement().getVersionIdPart());
		assertNull(observation.getNoteFirstRep().getText());

		observation.getNoteFirstRep().setText("changed");
		MethodOutcome methodOutcome = myClient.update().resource(observation).execute();
		assertEquals("2", methodOutcome.getId().getVersionIdPart());
		assertEquals("changed", observation.getNoteFirstRep().getText());

		// Wait for our two delivery channel threads to be paused
		assertTrue(countDownLatch.await(5L, TimeUnit.SECONDS));
		// Open the floodgates!
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();


		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(2);

		Observation observation1 = ourObservationProvider.getResourceUpdates().get(0);
		Observation observation2 = ourObservationProvider.getResourceUpdates().get(1);

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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(3);

		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForActivatedSubscriptionCount(1);

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see only one subscription notification
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		myClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		myClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(5);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertThat(observation1.getId()).isNotEmpty();
		assertThat(observation2.getId()).isNotEmpty();
	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDatabase() throws Exception {
		// Same test as above, but now run it using database matching
		mySubscriptionSettings.setEnableInMemorySubscriptionMatching(false);
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		assertEquals("1", ourObservationProvider.getStoredResources().get(0).getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(3);

		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForQueueToDrain();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see only one subscription notification
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		myClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		myClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(5);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertThat(observation1.getId()).isNotEmpty();
		assertThat(observation2.getId()).isNotEmpty();
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

		ourLog.info("** About to send observation");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);
		subscriptionTemp.setCriteria(criteria1);
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(3);

		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		myClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(4);

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		myClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(5);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertThat(observation1.getId()).isNotEmpty();
		assertThat(observation2.getId()).isNotEmpty();
	}

	@Test
	public void testRestHookSubscriptionStarCriteria() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "[*]";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		sendObservation(code, "SNOMED-CT");
		sendPatient();

		waitForQueueToDrain();

		// Should see 1 subscription notification for each type
		ourObservationProvider.waitForCreateCount(0);
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		ourPatientProvider.waitForCreateCount(0);
		ourPatientProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(1));

	}


	@Test
	public void testRestHookSubscriptionMultiTypeCriteria() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "[Observation,Patient]";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		sendOrganization();
		sendObservation(code, "SNOMED-CT");
		sendPatient();

		waitForQueueToDrain();

		// Should see 1 subscription notification for each type
		ourObservationProvider.waitForCreateCount(0);
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		ourPatientProvider.waitForCreateCount(0);
		ourPatientProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(1));
		ourOrganizationProvider.waitForCreateCount(0);
		ourOrganizationProvider.waitForUpdateCount(0);

	}

	@Test
	public void testSubscriptionTriggerViaSubscription() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		createSubscription(criteria1, payload);
		waitForActivatedSubscriptionCount(1);

		ourLog.info("** About to send observation");

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
			.setUrl("Observation?identifier=foo|bar1")
			.setMethod(Bundle.HTTPVerb.PUT);
		requestBundle.addEntry()
			.setResource(patient)
			.setFullUrl(patient.getId())
			.getRequest()
			.setUrl("Patient?identifier=foo|bar2")
			.setMethod(Bundle.HTTPVerb.PUT);
		myClient.transaction().withBundle(requestBundle).execute();

		// Should see 1 subscription notification
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourRestfulServer.getRequestContentTypes().get(0));

		Observation obs = ourObservationProvider.getStoredResources().get(0);
		ourLog.debug("Observation content: {}", myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(obs));
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
		assertEquals(0, ourObservationProvider.getCountUpdate());

		Subscription subscriptionTemp = myClient.read().resource(Subscription.class).withId(subscription2.getId()).execute();
		assertNotNull(subscriptionTemp);
		String criteriaGood = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		subscriptionTemp.setCriteria(criteriaGood);
		ourLog.info("** About to update subscription");
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		ourLog.info("** About to send Observation 2");
		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see a subscription notification this time
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

		myClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// No more matches
		Thread.sleep(1000);
		assertEquals(1, ourObservationProvider.getCountUpdate());
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourRestfulServer.getRequestContentTypes().get(0));
	}

	@Test
	public void testRestHookSubscriptionInvalidCriteria() throws Exception {
		String payload = "application/xml";

		String criteria1 = "Observation?codeeeee=SNOMED-CT";

		try {
			createSubscription(criteria1, payload);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(9) + "Invalid subscription criteria submitted: Observation?codeeeee=SNOMED-CT " + Msg.code(488) + "Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
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
		myClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);
		assertEquals(CT_FHIR_JSON_NEW, ourRestfulServer.getRequestContentTypes().get(0));
		assertThat(ourRestfulServer.getRequestHeaders().get(0)).contains("X-Foo: FOO");
		assertThat(ourRestfulServer.getRequestHeaders().get(0)).contains("X-Bar: BAR");
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
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

		// Disable
		subscription.setStatus(Subscription.SubscriptionStatus.OFF);
		myClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		// Send another object
		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		assertEquals(0, ourObservationProvider.getCountCreate());
		ourObservationProvider.waitForUpdateCount(1);

	}

	@Test
	public void testInvalidProvenanceParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String payload = "application/fhir+json";
			String criteriabad = "Provenance?activity=http://hl7.org/fhir/v3/DocumentCompletion%7CAU";
			Subscription subscription = newSubscription(criteriabad, payload);
			myClient.create().resource(subscription).execute();
		});
	}

	@Test
	public void testInvalidProcedureRequestParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String payload = "application/fhir+json";
			String criteriabad = "ProcedureRequest?intent=instance-order&category=Laboratory";
			Subscription subscription = newSubscription(criteriabad, payload);
			myClient.create().resource(subscription).execute();
		});
	}

	@Test
	public void testInvalidBodySiteParam() {
		assertThatExceptionOfType(UnprocessableEntityException.class).isThrownBy(() -> {
			String payload = "application/fhir+json";
			String criteriabad = "BodySite?accessType=Catheter";
			Subscription subscription = newSubscription(criteriabad, payload);
			myClient.create().resource(subscription).execute();
		});
	}

	@Test
	public void testGoodSubscriptionPersists() {
		assertEquals(0, subscriptionCount());
		String payload = "application/fhir+json";
		String criteriaGood = "Patient?gender=male";
		Subscription subscription = newSubscription(criteriaGood, payload);
		myClient.create().resource(subscription).execute();
		await().until(() -> subscriptionCount() == 1);
	}

	/**
	 * Make sure we don't activate a subscription if its type is incorrect
	 */
	@Test
	public void testSubscriptionDoesntActivateIfRestHookIsNotEnabled() throws InterruptedException {
		Set<org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType> existingSupportedSubscriptionTypes = mySubscriptionSettings.getSupportedSubscriptionTypes();
		mySubscriptionSettings.clearSupportedSubscriptionTypesForUnitTest();
		try {

			Subscription subscription = newSubscription("Observation?", "application/fhir+json");
			IIdType id = myClient.create().resource(subscription).execute().getId().toUnqualifiedVersionless();

			Thread.sleep(1000);
			subscription = myClient.read().resource(Subscription.class).withId(id).execute();
			assertEquals(Subscription.SubscriptionStatus.REQUESTED, subscription.getStatus());

		} finally {
			existingSupportedSubscriptionTypes.forEach(t -> mySubscriptionSettings.addSupportedSubscriptionType(t));
		}
	}


	private int subscriptionCount() {
		IBaseBundle found = myClient.search().forResource(Subscription.class).cacheControl(new CacheControlDirective().setNoCache(true)).execute();
		return toUnqualifiedVersionlessIdValues(found).size();
	}

	@Test
	public void testSubscriptionWithNoStatusIsRejected() {
		Subscription subscription = newSubscription("Observation?", "application/json");
		subscription.setStatus(null);

		try {
			myClient.create().resource(subscription).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Can not process submitted Subscription - Subscription.status must be populated on this server");
		}
	}


	@Test
	public void testBadSubscriptionDoesntPersist() {
		assertEquals(0, subscriptionCount());
		String payload = "application/fhir+json";
		String criteriaBad = "BodySite?accessType=Catheter";
		Subscription subscription = newSubscription(criteriaBad, payload);
		try {
			myClient.create().resource(subscription).execute();
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
			MethodOutcome methodOutcome = myClient.create().resource(bodySite).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			ourObservationProvider.waitForUpdateCount(1);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("PD Catheter"));
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			ourObservationProvider.waitForUpdateCount(2);
		}
		{
			Observation observation = new Observation();
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			ourObservationProvider.waitForUpdateCount(2);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("XXX"));
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			ourObservationProvider.waitForUpdateCount(2);
		}

	}


	@Test
	public void testDeliverSearchResult() throws Exception {
		{
			Subscription subscription = newSubscription("Observation?", "application/json");
			subscription.addExtension(HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA, new StringType("Observation?_id=${matched_resource_id}&_include=*"));
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(subscription));
			MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
			mySubscriptionIds.add(methodOutcome.getId());
			waitForActivatedSubscriptionCount(1);
		}

		{
			Patient patient = new Patient();
			patient.setActive(true);
			IIdType patientId = myClient.create().resource(patient).execute().getId();

			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("Catheter"));
			observation.getSubject().setReferenceElement(patientId.toUnqualifiedVersionless());
			MethodOutcome methodOutcome = myClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());

			waitForQueueToDrain();

			ourTransactionProvider.waitForTransactionCount(1);

			Bundle xact = ourTransactionProvider.getTransactions().get(0);
			assertThat(xact.getEntry()).hasSize(2);
		}

	}

	@Test
	public void testRestHoodTopicSubscription_withEmptyPayloadContent_generateCorrectPayload() throws Exception {
		String payloadContent = "empty";

		// execute
		Bundle bundle = createAndDispatchTopicSubscription(payloadContent);

		// verify Bundle size
		assertThat(bundle.getEntry()).hasSize(1);
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Resource.class);
		assertThat(resources).hasSize(1);

		// verify SubscriptionStatus.notificationEvent.focus
		Optional<Parameters.ParametersParameterComponent> focus = getNotificationEventFocus(resources);
		assertFalse(focus.isPresent());
	}

	@Test
	public void testRestHoodTopicSubscription_withIdOnlyPayloadContent_generateCorrectPayload() throws Exception {
		String payloadContent = "id-only";

		// execute
		Bundle bundle = createAndDispatchTopicSubscription(payloadContent);

		// verify Bundle size
		assertThat(bundle.getEntry()).hasSize(2);
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Resource.class);
		assertThat(resources).hasSize(1);

		// verify SubscriptionStatus.notificationEvent.focus
		Optional<Parameters.ParametersParameterComponent> focus = getNotificationEventFocus(resources);
		assertThat(focus).isPresent();
		assertEquals(TEST_PATIENT_ID, ((Reference) focus.get().getValue()).getReference());

		// verify Patient Entry
		Bundle.BundleEntryComponent patientEntry = bundle.getEntry().get(1);
		validateRequestParameters(patientEntry);
		Patient bundlePatient = (Patient) patientEntry.getResource();
		assertNull(bundlePatient);
	}

	@Test
	public void testRestHoodTopicSubscription_withFullResourcePayloadContent_generateCorrectPayload() throws Exception {
		String payloadContent = "full-resource";

		// execute
		Bundle bundle = createAndDispatchTopicSubscription(payloadContent);

		// verify Bundle size
		assertThat(bundle.getEntry()).hasSize(2);
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Resource.class);
		assertThat(resources).hasSize(2);

		// verify SubscriptionStatus.notificationEvent.focus
		Optional<Parameters.ParametersParameterComponent> focus = getNotificationEventFocus(resources);
		assertThat(focus).isPresent();
		assertEquals(PATIENT_REFERENCE, ((Reference) focus.get().getValue()).getReference());

		// verify Patient Entry
		Bundle.BundleEntryComponent patientEntry = bundle.getEntry().get(1);
		validateRequestParameters(patientEntry);
		Patient bundlePatient = (Patient) patientEntry.getResource();
		assertTrue(bundlePatient.getActive());
		assertEquals(Enumerations.AdministrativeGender.FEMALE, bundlePatient.getGender());
	}

	private Bundle createAndDispatchTopicSubscription(String thePayloadContent) throws Exception {
		Subscription subscription = SubscriptionTestDataHelper.buildR4TopicSubscriptionWithContent(thePayloadContent);
		subscription.setIdElement(null);
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(CT_FHIR_JSON_NEW);
		channel.setEndpoint(ourRestfulServer.getBaseUrl());

		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		mySubscriptionIds.add(methodOutcome.getId());
		waitForActivatedSubscriptionCount(1);

		Patient patient = new Patient();
		patient.setId(TEST_PATIENT_ID);
		patient.setActive(true);
		patient.setGender(Enumerations.AdministrativeGender.FEMALE);

		int count = mySubscriptionTopicDispatcher.dispatch(SubscriptionTestDataHelper.TEST_TOPIC, List.of(patient), RestOperationTypeEnum.CREATE);
		assertEquals(1, count);

		waitForQueueToDrain();

		ourTransactionProvider.waitForTransactionCount(1);

		return ourTransactionProvider.getTransactions().get(0);
	}

	private Optional<Parameters.ParametersParameterComponent> getNotificationEventFocus(List<Resource> theResources) {
		assertEquals("Parameters", theResources.get(0).getResourceType().name());
		Parameters parameters = (Parameters) theResources.get(0);
		Parameters.ParametersParameterComponent notificationEvent = parameters.getParameter("notification-event");
		assertNotNull(notificationEvent);
		return notificationEvent.getPart().stream()
				.filter(part -> part.getName().equals("focus"))
				.findFirst();
	}

	private void validateRequestParameters(Bundle.BundleEntryComponent thePatientEntry) {
		assertNotNull(thePatientEntry.getRequest());
		assertEquals("POST", thePatientEntry.getRequest().getMethod().name());
		assertEquals("Patient", thePatientEntry.getRequest().getUrl());
	}
}
