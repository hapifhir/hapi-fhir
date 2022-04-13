package ca.uhn.fhir.jpa.subscription.match.deliver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.match.deliver.message.SubscriptionDeliveringMessageSubscriber;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseSubscriptionDeliverySubscriberTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliverySubscriberTest.class);

	private SubscriptionDeliveringRestHookSubscriber mySubscriber;
	private SubscriptionDeliveringMessageSubscriber myMessageSubscriber;
	private final FhirContext myCtx = FhirContext.forR4();

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Mock
	private IChannelFactory myChannelFactory;
	@Mock
	private IChannelProducer myChannelProducer;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IRestfulClientFactory myRestfulClientFactory;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IGenericClient myGenericClient;

	@BeforeEach
	public void before() {
		mySubscriber = new SubscriptionDeliveringRestHookSubscriber();
		mySubscriber.setFhirContextForUnitTest(myCtx);
		mySubscriber.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySubscriber.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);

		myMessageSubscriber = new SubscriptionDeliveringMessageSubscriber(myChannelFactory);
		myMessageSubscriber.setFhirContextForUnitTest(myCtx);
		myMessageSubscriber.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		myMessageSubscriber.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);

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

		Patient patient = generatePatient();

		CanonicalSubscription subscription = generateSubscription();

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

		Patient patient = generatePatient();

		CanonicalSubscription subscription = generateSubscription();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		when(myGenericClient.update()).thenThrow(new InternalErrorException("FOO"));

		try {
			mySubscriber.handleMessage(new ResourceDeliveryJsonMessage(payload));
			fail();
		} catch (MessagingException e) {
			assertEquals(Msg.code(2) + "Failure handling subscription payload for subscription: Subscription/123; nested exception is ca.uhn.fhir.rest.server.exceptions.InternalErrorException: FOO", e.getMessage());
		}

		verify(myGenericClient, times(1)).update();
	}

	@Test
	public void testRestHookDeliveryFails_InterceptorDealsWithIt() {
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_DELIVERY_FAILED), any())).thenReturn(false);

		Patient patient = generatePatient();

		CanonicalSubscription subscription = generateSubscription();

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

		Patient patient = generatePatient();

		CanonicalSubscription subscription = generateSubscription();

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

	@Test
	public void testSerializeDeliveryMessageWithRequestPartition() throws JsonProcessingException {
		CanonicalSubscription subscription = generateSubscription();
		Patient patient = generatePatient();

		ResourceDeliveryMessage message = new ResourceDeliveryMessage();
		message.setPartitionId(RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1)));
		message.setSubscription(subscription);
		message.setPayload(myCtx, patient, EncodingEnum.JSON);
		message.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		ResourceDeliveryJsonMessage jsonMessage = new ResourceDeliveryJsonMessage(message);
		String jsonString = jsonMessage.asJson();

		ourLog.info(jsonString);


		// Assert that the partitionID is being serialized in JSON
		assertThat(jsonString, containsString("\"partitionDate\":[2020,1,1]"));
		assertThat(jsonString, containsString("\"partitionIds\":[123]"));
	}

	@Test
	public void testSerializeDeliveryMessageWithNoPartition() throws JsonProcessingException {
		CanonicalSubscription subscription = generateSubscription();
		Patient patient = generatePatient();

		ResourceDeliveryMessage message = new ResourceDeliveryMessage();
		message.setSubscription(subscription);
		message.setPayload(myCtx, patient, EncodingEnum.JSON);
		message.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		ResourceDeliveryJsonMessage jsonMessage = new ResourceDeliveryJsonMessage(message);
		String jsonString = jsonMessage.asJson();

		ourLog.info(jsonString);

		assertThat(jsonString, containsString("\"operationType\":\"CREATE"));
		assertThat(jsonString, containsString("\"canonicalSubscription\":"));

		// Assert that the default partitionID is being generated and is being serialized in JSON
		assertThat(jsonString, containsString("\"allPartitions\":false"));
		assertThat(jsonString, containsString("\"partitionIds\":[null]"));
	}

	@Test
	public void testDeliveryMessageWithPartition() throws URISyntaxException {
		RequestPartitionId thePartitionId = RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1));
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_MESSAGE_DELIVERY), any())).thenReturn(false);
		when(myChannelFactory.getOrCreateProducer(any(), any(), any())).thenReturn(myChannelProducer);

		CanonicalSubscription subscription = generateSubscription();
		Patient patient = generatePatient();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);
		payload.setPartitionId(thePartitionId);

		myMessageSubscriber.handleMessage(payload);
		verify(myChannelFactory).getOrCreateProducer(any(), any(), any());
		ArgumentCaptor<ResourceModifiedJsonMessage> captor = ArgumentCaptor.forClass(ResourceModifiedJsonMessage.class);
		verify(myChannelProducer).send(captor.capture());
		final List<ResourceModifiedJsonMessage> params = captor.getAllValues();
		assertEquals(thePartitionId, params.get(0).getPayload().getPartitionId());
	}

	@Test
	public void testSerializeLegacyDeliveryMessage() throws JsonProcessingException {
		String legacyDeliveryMessageJson = "{\"headers\":{\"retryCount\":0,\"customHeaders\":{}},\"payload\":{\"operationType\":\"CREATE\",\"canonicalSubscription\":{\"id\":\"Subscription/123\",\"endpointUrl\":\"http://example.com/fhir\",\"payload\":\"application/fhir+json\"},\"payload\":\"{\\\"resourceType\\\":\\\"Patient\\\",\\\"active\\\":true}\"}}";

		ResourceDeliveryJsonMessage jsonMessage = ResourceDeliveryJsonMessage.fromJson(legacyDeliveryMessageJson);

		ourLog.info(jsonMessage.getPayload().getRequestPartitionId().asJson());

		assertNotNull(jsonMessage.getPayload().getRequestPartitionId());
		assertEquals(jsonMessage.getPayload().getRequestPartitionId().toJson(), RequestPartitionId.defaultPartition().toJson());
	}

	@NotNull
	private Patient generatePatient() {
		Patient patient = new Patient();
		patient.setActive(true);
		return patient;
	}

	@NotNull
	private CanonicalSubscription generateSubscription() {
		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");
		return subscription;
	}

}
