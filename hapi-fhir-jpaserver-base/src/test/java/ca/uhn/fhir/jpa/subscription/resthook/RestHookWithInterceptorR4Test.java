package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.IInterceptorRegistry;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

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

	protected Observation sendObservation() {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
		observation.setId(methodOutcome.getId());
		return observation;
	}

	@Configuration
	static class MyTestCtxConfig {

		@Autowired
		private IInterceptorRegistry myInterceptorRegistry;

		@Bean
		public MyTestInterceptor interceptor() {
			MyTestInterceptor retVal = new MyTestInterceptor();
			myInterceptorRegistry.registerInterceptor(retVal);
			return retVal;
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
		public boolean afterRestHookDelivery(ResourceDeliveryMessage theDeliveryMessage, CanonicalSubscription theSubscription) {
			ourHitAfterRestHookDelivery = true;
			return ourNextAfterRestHookDeliveryReturn;
		}

	}


}
