package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.channel.subscription.ISubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SubscriptionRegistryTest {

	@Mock
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;

	@Mock
	private ISubscriptionDeliveryChannelNamer mySubscriptionDeliveryChannelNamer;

	@Mock
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@InjectMocks
	private SubscriptionRegistry mySubscriptionRegistry;

	private Subscription createSubscription(Extension... theExtensions) {
		Subscription subscription = new Subscription();
		subscription.setId("123");
		subscription.setCriteria("Patient");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		Subscription.SubscriptionChannelComponent channel
			= new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		subscription.setChannel(channel);

		if (theExtensions != null) {
			for (Extension ex : theExtensions) {
				channel.addExtension(ex);
			}
		}
		return subscription;
	}

	private CanonicalSubscription getCanonicalSubscriptionFromSubscription(Subscription theSubscription) {
		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setStatus(theSubscription.getStatus());
		subscription.setCriteriaString(theSubscription.getCriteria());

		Subscription.SubscriptionChannelComponent channel = theSubscription.getChannel();
		HashMap<String, List<String>> extensions = new HashMap<String, List<String>>();

		for (Extension ex : channel.getExtension()) {
			if (!extensions.containsKey(ex.getUrl())) {
				extensions.put(ex.getUrl(), new ArrayList<>());
			}
			extensions.get(ex.getUrl()).add(ex.getValueAsPrimitive().getValueAsString());
		}
		subscription.setChannelExtensions(extensions);

		return subscription;
	}

	/**
	 * Will mock the subscription canonicalizer with the provided subscription
	 * and the channel namer with the provided name.
	 *
	 * @param theSubscription
	 * @param theName
	 */
	private void mockSubscriptionCanonicalizerAndChannelNamer(Subscription theSubscription, String theName) {
		Mockito.when(mySubscriptionCanonicalizer.canonicalize(Mockito.any(Subscription.class)))
			.thenReturn(getCanonicalSubscriptionFromSubscription(theSubscription));
		Mockito.when(mySubscriptionDeliveryChannelNamer.nameFromSubscription(Mockito.any(CanonicalSubscription.class)))
			.thenReturn(theName);
	}

	/**
	 * Verifies an ActiveSubscription was registered, and passes it back
	 * for further verification.
	 * Also verifies that the interceptor was called.
	 */
	private ActiveSubscription verifySubscriptionIsRegistered() {
		ArgumentCaptor<ActiveSubscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(ActiveSubscription.class);
		Mockito.verify(mySubscriptionChannelRegistry)
			.add(subscriptionArgumentCaptor.capture());
		Mockito.verify(myInterceptorBroadcaster)
			.callHooks(Mockito.any(Pointcut.class), Mockito.any(HookParams.class));
		return subscriptionArgumentCaptor.getValue();
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithRetry_createsAsExpected() {
		// init
		String channelName = "subscription-test";
		int retryCount = 2;

		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.EX_RETRY_COUNT);
		retryExtension.setValue(new IntegerType(retryCount));

		Subscription subscription = createSubscription(retryExtension);

		// when
		mockSubscriptionCanonicalizerAndChannelNamer(subscription, channelName);

		// test
		boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		Assertions.assertTrue(registered);
		ActiveSubscription activeSubscription = verifySubscriptionIsRegistered();
		Assertions.assertNotNull(activeSubscription.getRetryConfigurationParameters());
		Assertions.assertEquals(channelName, activeSubscription.getChannelName());
		Assertions.assertEquals(retryCount, activeSubscription.getRetryConfigurationParameters().getRetryCount());
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithoutRetry_createsAsExpected() {
		// init
		String channelName = "subscription-test";

		Subscription subscription = createSubscription();

		// when
		mockSubscriptionCanonicalizerAndChannelNamer(subscription, channelName);

		// test
		boolean created = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		Assertions.assertTrue(created);
		ActiveSubscription activeSubscription = verifySubscriptionIsRegistered();
		Assertions.assertNull(activeSubscription.getRetryConfigurationParameters());
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithBadRetry_createsAsExpected() {
		// init
		String channelName = "subscription-test";
		int retryCount = -1; // invalid retry count -> no retries created

		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.EX_RETRY_COUNT);
		retryExtension.setValue(new IntegerType(retryCount));

		Subscription subscription = createSubscription(retryExtension);

		// when
		mockSubscriptionCanonicalizerAndChannelNamer(subscription, channelName);

		// test
		boolean created = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		Assertions.assertTrue(created);
		ActiveSubscription activeSubscription = verifySubscriptionIsRegistered();
		Assertions.assertNull(activeSubscription.getRetryConfigurationParameters());
		Assertions.assertEquals(channelName, activeSubscription.getChannelName());
	}
}
