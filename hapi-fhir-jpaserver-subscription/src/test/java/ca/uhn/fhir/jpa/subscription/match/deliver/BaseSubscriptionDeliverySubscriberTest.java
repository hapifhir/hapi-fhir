package ca.uhn.fhir.jpa.subscription.match.deliver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseSubscriptionDeliverySubscriberTest {

	private SubscriptionDeliveringRestHookSubscriber mySubscriber;
	private FhirContext myCtx = FhirContext.forR4();

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IRestfulClientFactory myRestfulClientFactory;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IGenericClient myGenericClient;

	@Before
	public void before() {
		mySubscriber = new SubscriptionDeliveringRestHookSubscriber();
		mySubscriber.setFhirContextForUnitTest(myCtx);
		mySubscriber.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySubscriber.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);

		myCtx.setRestfulClientFactory(myRestfulClientFactory);
		when(myRestfulClientFactory.newGenericClient(any())).thenReturn(myGenericClient);
	}

	@Test
	public void testWrongTypeIgnored() {
		Message<String> message = new GenericMessage<>("HELLO");
		// Nothing should happen
		mySubscriber.handleMessage(message);
	}

	@Test
	public void testSubscriptionWithNoIs() {
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(new CanonicalSubscription());

		// Nothing should happen
		mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));
	}

	@Test
	public void testRestHookDeliverySuccessful() {
		when(myInterceptorBroadcaster.callHooks(any(), any())).thenReturn(true);

		Patient patient = new Patient();
		patient.setActive(true);

		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));

		verify(myGenericClient, times(1)).update();
	}

	@Test
	public void testRestHookDeliveryFails_ShouldRollBack() {
		when(myInterceptorBroadcaster.callHooks(any(), any())).thenReturn(true);

		Patient patient = new Patient();
		patient.setActive(true);

		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		when(myGenericClient.update()).thenThrow(new InternalErrorException("FOO"));

		try {
			mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));
			fail();
		} catch (MessagingException e) {
			assertEquals("Failure handling subscription payload for subscription: Subscription/123; nested exception is ca.uhn.fhir.rest.server.exceptions.InternalErrorException: FOO", e.getMessage());
		}

		verify(myGenericClient, times(1)).update();
	}

	@Test
	public void testRestHookDeliveryFails_InterceptorDealsWithIt() {
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_DELIVERY_FAILED), any())).thenReturn(false);

		Patient patient = new Patient();
		patient.setActive(true);

		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		when(myGenericClient.update()).thenThrow(new InternalErrorException("FOO"));

		// This shouldn't throw an exception
		mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));

		verify(myGenericClient, times(1)).update();
	}

	@Test
	public void testRestHookDeliveryAbortedByInterceptor() {
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY), any())).thenReturn(false);

		Patient patient = new Patient();
		patient.setActive(true);

		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));

		verify(myGenericClient, times(0)).update();
	}

	@Test
	public void testInterceptorBroadcasterAbortsDelivery() {

	}

}
