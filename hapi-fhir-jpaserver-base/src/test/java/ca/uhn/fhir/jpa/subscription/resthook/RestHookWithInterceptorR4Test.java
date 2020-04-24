package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.util.SubscriptionDebugLogInterceptor;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Test the rest-hook subscriptions
 */
@ContextConfiguration(classes = {RestHookWithInterceptorR4Test.MyTestCtxConfig.class})
public class RestHookWithInterceptorR4Test extends BaseSubscriptionsR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(RestHookWithInterceptorR4Test.class);
	private static boolean ourNextModifyResourceId;
	private static boolean ourNextBeforeRestHookDeliveryReturn;
	private static boolean ourHitBeforeRestHookDelivery;
	private static boolean ourNextAfterRestHookDeliveryReturn;
	private static boolean ourHitAfterRestHookDelivery;
	private static boolean ourNextAddHeader;
	private static FhirContext ourCtx = FhirContext.forR4();

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@After
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
	}

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		ourNextModifyResourceId = false;
		ourNextAddHeader = false;
		ourNextBeforeRestHookDeliveryReturn = true;
		ourNextAfterRestHookDeliveryReturn = true;
		ourHitBeforeRestHookDelivery = false;
		ourHitAfterRestHookDelivery = false;

		myInterceptorRegistry.registerInterceptor(myTestInterceptor);
	}

	@Test
	public void testBeforeRestHookDelivery_ModifyResourceId() throws Exception {
		ourNextModifyResourceId = true;

		// Create a subscription
		CountDownLatch registerLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		createSubscription("Observation?status=final", "application/fhir+json");
		registerLatch.await(10, TimeUnit.SECONDS);

		// Creating a matching resource
		CountDownLatch deliveryLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);
		sendObservation();
		deliveryLatch.await(10, TimeUnit.SECONDS);

		assertEquals(0, ourCreatedObservations.size());
		assertEquals(1, ourUpdatedObservations.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
		assertEquals("Observation/A", ourUpdatedObservations.get(0).getId());
		assertTrue(ourHitBeforeRestHookDelivery);
		assertTrue(ourHitAfterRestHookDelivery);
	}

	@Test
	public void testBeforeRestHookDelivery_AddHeader() throws Exception {
		ourNextAddHeader = true;

		// Create a subscription
		CountDownLatch registerLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		createSubscription("Observation?status=final", "application/fhir+json");
		registerLatch.await(10, TimeUnit.SECONDS);

		// Creating a matching resource
		CountDownLatch deliveryLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);
		sendObservation();
		deliveryLatch.await(10, TimeUnit.SECONDS);

		assertEquals(0, ourCreatedObservations.size());
		assertEquals(1, ourUpdatedObservations.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
		assertTrue(ourHitBeforeRestHookDelivery);
		assertTrue(ourHitAfterRestHookDelivery);
		assertThat(ourHeaders, hasItem("X-Foo: Bar"));
	}


	@Test
	public void testAttributesAreCopiedAlongPipeline() throws Exception {
		AttributeCarryingInterceptor interceptor = new AttributeCarryingInterceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);
		try {

			// Create a subscription
			CountDownLatch registerLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
			createSubscription("Observation?status=final", "application/fhir+json");
			registerLatch.await(10, TimeUnit.SECONDS);

			// Creating a matching resource
			sendObservation();

			interceptor.getFinishedLatch().await(10, TimeUnit.SECONDS);
			ResourceDeliveryMessage lastDelivery = interceptor.getLastDelivery();
			assertTrue(lastDelivery.getAttribute("ATTR1").isPresent());
			assertTrue(lastDelivery.getAttribute("ATTR2").isPresent());
			assertTrue(lastDelivery.getAttribute("ATTRBLANK").isPresent());
			assertEquals("Some value 1", lastDelivery.getAttribute("ATTR1").get());
			assertEquals("Some value 2", lastDelivery.getAttribute("ATTR2").get());
			assertEquals("", lastDelivery.getAttribute("ATTRBLANK").get());
			assertEquals(false, lastDelivery.getAttribute("ATTRNONEXISTENT").isPresent());

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testBeforeRestHookDelivery_AbortDelivery() throws Exception {
		ourNextBeforeRestHookDeliveryReturn = false;

		// Create a subscription
		CountDownLatch registerLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		createSubscription("Observation?status=final", "application/fhir+json");
		registerLatch.await(10, TimeUnit.SECONDS);

		sendObservation();

		Thread.sleep(1000);
		assertEquals(0, ourUpdatedObservations.size());
	}


	@Test
	public void testDeliveryFailed() throws Exception {
		ourNextBeforeRestHookDeliveryReturn = false;

		// Create a subscription
		CountDownLatch registerLatch = registerLatchHookInterceptor(1, Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		Subscription subscription = newSubscription("Observation?status=final", "application/fhir+json");
		subscription.getChannel().setEndpoint("http://localhost:" + ourListenerPort + "/this/url/does/not/exist"); // this better not succeed!

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		registerLatch.await(10, TimeUnit.SECONDS);

		CountDownLatch latch = new CountDownLatch(1);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_DELIVERY_FAILED, (thePointcut, params) -> {
			latch.countDown();
		});

		sendObservation();

		latch.await(10, TimeUnit.SECONDS);
	}


	protected Observation sendObservation() {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
		observation.setId(methodOutcome.getId());
		return observation;
	}

	@Test
	public void testDebugLoggingInterceptor() throws Exception {
		List<String> messages = new ArrayList<>();
		Logger loggerMock = mock(Logger.class);
		doAnswer(t -> {
			Object msg = t.getArguments()[0];
			Object[] args = Arrays.copyOfRange(t.getArguments(), 1, t.getArguments().length);
			String formattedMessage = MessageFormatter.arrayFormat((String) msg, args).getMessage();
			messages.add(formattedMessage);
			return null;
		}).when(loggerMock).debug(any(), ArgumentMatchers.<Object[]>any());

		SubscriptionDebugLogInterceptor interceptor = new SubscriptionDebugLogInterceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);
		SubscriptionDebugLogInterceptor interceptor2 = new SubscriptionDebugLogInterceptor(t -> loggerMock, Level.DEBUG);
		myInterceptorRegistry.registerInterceptor(interceptor2);
		try {

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

			sendObservation(code, "SNOMED-CT");
			waitForQueueToDrain();

			// Should see two subscription notifications
			waitForSize(0, ourCreatedObservations);
			waitForSize(3, ourUpdatedObservations);

			ourLog.info("Messages:\n  " + messages.stream().collect(Collectors.joining("\n  ")));

			assertThat(messages.get(messages.size() - 1), matchesPattern("Finished delivery of resource Observation.*"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
			myInterceptorRegistry.unregisterInterceptor(interceptor2);
		}
	}

	@Interceptor
	public static class AttributeCarryingInterceptor {

		private ResourceDeliveryMessage myLastDelivery;
		private CountDownLatch myFinishedLatch = new CountDownLatch(1);

		public CountDownLatch getFinishedLatch() {
			return myFinishedLatch;
		}

		public ResourceDeliveryMessage getLastDelivery() {
			return myLastDelivery;
		}

		@Hook(Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED)
		public void onSubmit(ResourceModifiedMessage theMessage) {
			theMessage.setAttribute("ATTR1", "Some value 1");
			theMessage.setAttribute("ATTR2", "Some value 2");
			theMessage.setAttribute("ATTRBLANK", "");
		}

		@Hook(Pointcut.SUBSCRIPTION_AFTER_DELIVERY)
		public void afterDelivery(ResourceDeliveryMessage theMessage) {
			Validate.isTrue(myLastDelivery == null);
			myLastDelivery = theMessage;
			myFinishedLatch.countDown();
		}
	}

	@Autowired
	private IInterceptorService myInterceptorRegistry;

	@Autowired
	private MyTestInterceptor myTestInterceptor;

	@Configuration
	static class MyTestCtxConfig {


		@Bean
		public MyTestInterceptor interceptor() {
			return new MyTestInterceptor();
		}

	}

	/**
	 * Interceptor class
	 */
	@Interceptor
	public static class MyTestInterceptor {

		/**
		 * Constructor
		 */
		public MyTestInterceptor() {
			ourLog.info("Creating interceptor");
		}

		@Hook(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY)
		public boolean beforeRestHookDelivery(ResourceDeliveryMessage theDeliveryMessage, CanonicalSubscription theSubscription) {
			if (ourNextModifyResourceId) {
				theDeliveryMessage.getPayload(ourCtx).setId(new IdType("Observation/A"));
			}
			if (ourNextAddHeader) {
				theSubscription.addHeader("X-Foo: Bar");
			}

			ourHitBeforeRestHookDelivery = true;
			return ourNextBeforeRestHookDeliveryReturn;
		}

		@Hook(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY)
		public void afterRestHookDelivery(ResourceDeliveryMessage theDeliveryMessage, CanonicalSubscription theSubscription) {
			ourHitAfterRestHookDelivery = true;
		}

	}


}
