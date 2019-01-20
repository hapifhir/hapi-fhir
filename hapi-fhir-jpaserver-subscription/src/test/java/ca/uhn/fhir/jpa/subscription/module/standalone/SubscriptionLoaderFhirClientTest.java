package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SubscriptionLoaderFhirClientTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	@Test
	public void testSubscriptionLoaderFhirClient() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED, t-> latch.countDown());

		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(returnedActiveSubscription(criteria1, payload, ourListenerServerBase));
		subs.add(returnedActiveSubscription(criteria2, payload, ourListenerServerBase));

		IBundleProvider bundle = new SimpleBundleProvider(new ArrayList<>(subs), "uuid");
		initSubscriptionLoader(bundle);

		sendObservation(myCode, "SNOMED-CT");
		latch.await(10, TimeUnit.SECONDS);

		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testSubscriptionLoaderFhirClientSubscriptionNotActive() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		myInterceptorRegistry.registerAnonymousHookForUnitTest(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED, t-> latch.countDown());

		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(returnedActiveSubscription(criteria1, payload, ourListenerServerBase).setStatus(Subscription.SubscriptionStatus.REQUESTED));
		subs.add(returnedActiveSubscription(criteria2, payload, ourListenerServerBase).setStatus(Subscription.SubscriptionStatus.REQUESTED));

		IBundleProvider bundle = new SimpleBundleProvider(new ArrayList<>(subs), "uuid");
		initSubscriptionLoader(bundle);

		sendObservation(myCode, "SNOMED-CT");
		latch.await(10, TimeUnit.SECONDS);

		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourUpdatedObservations);
	}
}
