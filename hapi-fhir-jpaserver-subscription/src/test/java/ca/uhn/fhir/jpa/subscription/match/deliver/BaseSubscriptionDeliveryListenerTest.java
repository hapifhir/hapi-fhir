package ca.uhn.fhir.jpa.subscription.match.deliver;

import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailListener;
import ca.uhn.fhir.jpa.subscription.match.deliver.message.SubscriptionDeliveringMessageListener;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookListener;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseSubscriptionDeliveryListenerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliveryListenerTest.class);

	private SubscriptionDeliveringRestHookListener mySubscriptionDeliveringRestHookListener;
	private SubscriptionDeliveringMessageListener mySubscriptionDeliveringMessageListener;
	private SubscriptionDeliveringEmailListener mySubscriptionDeliveringEmailListener;
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Mock
	private IBrokerClient myBrokerClient;
	@Mock
	private IChannelProducer<ResourceModifiedMessage> myChannelProducer; // Producer for the subscription target channel

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IRestfulClientFactory myRestfulClientFactory;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private IGenericClient myGenericClient;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao myResourceDao;

	@Mock
	private MatchUrlService myMatchUrlService;

	@Mock
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	@Mock
	IDefaultPartitionSettings myDefaultPartitionSettings;

	@Mock
	private IEmailSender myEmailSender;

	@BeforeEach
	public void before() {
		mySubscriptionDeliveringRestHookListener = new SubscriptionDeliveringRestHookListener();
		mySubscriptionDeliveringRestHookListener.setFhirContextForUnitTest(myCtx);
		mySubscriptionDeliveringRestHookListener.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySubscriptionDeliveringRestHookListener.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);

		mySubscriptionDeliveringMessageListener = new SubscriptionDeliveringMessageListener(myBrokerClient, myDefaultPartitionSettings);
		mySubscriptionDeliveringMessageListener.setFhirContextForUnitTest(myCtx);
		mySubscriptionDeliveringMessageListener.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySubscriptionDeliveringMessageListener.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);
		mySubscriptionDeliveringMessageListener.setDaoRegistryForUnitTest(myDaoRegistry);
		mySubscriptionDeliveringMessageListener.setMatchUrlServiceForUnitTest(myMatchUrlService);
		mySubscriptionDeliveringMessageListener.setResourceModifiedMessagePersistenceSvcForUnitTest(myResourceModifiedMessagePersistenceSvc);
		myCtx.setRestfulClientFactory(myRestfulClientFactory);
		when(myRestfulClientFactory.newGenericClient(any())).thenReturn(myGenericClient);

		mySubscriptionDeliveringEmailListener = new SubscriptionDeliveringEmailListener(myEmailSender);
		mySubscriptionDeliveringEmailListener.setFhirContextForUnitTest(myCtx);
		mySubscriptionDeliveringEmailListener.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		mySubscriptionDeliveringEmailListener.setSubscriptionRegistryForUnitTest(mySubscriptionRegistry);
		mySubscriptionDeliveringEmailListener.setResourceModifiedMessagePersistenceSvcForUnitTest(myResourceModifiedMessagePersistenceSvc);
	}

	@Test
	public void testSubscriptionWithNoIs() {
		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(new CanonicalSubscription());

		// Nothing should happen
		mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));
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

		mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));

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
			mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));
			fail("");
		} catch (MessagingException e) {
			assertEquals(Msg.code(2) + "Failure handling subscription payload for subscription: Subscription/123", e.getMessage());
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
		mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));

		verify(myGenericClient, times(1)).update();
	}

	@Test
	public void testMessageSubscriber_PermitsInterceptorsToModifyOutgoingEnvelope() throws URISyntaxException {

		//Given: We setup mocks, and have this mock interceptor inject those headers.
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams argument = t.getArgument(1, HookParams.class);
			ResourceModifiedJsonMessage resourceModifiedJsonMessage = argument.get(ResourceModifiedJsonMessage.class);
			resourceModifiedJsonMessage.getHapiHeaders().getCustomHeaders().put("foo", List.of("bar", "bar2"));
			return true;
		});
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_MESSAGE_DELIVERY), any())).thenReturn(false);
		when(myBrokerClient.getOrCreateProducer(any(), any(), any())).thenAnswer(t -> myChannelProducer);

		CanonicalSubscription subscription = generateSubscription();
		Patient patient = generatePatient();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		//When: We submit the message for delivery
		mySubscriptionDeliveringMessageListener.handleMessage(payload);

		//Then: The receiving channel should also receive the custom headers.
		ArgumentCaptor<ResourceModifiedJsonMessage> captor = ArgumentCaptor.forClass(ResourceModifiedJsonMessage.class);
		verify(myChannelProducer).send(captor.capture());
		final List<ResourceModifiedJsonMessage> messages = captor.getAllValues();
		assertThat(messages).hasSize(1);
		ResourceModifiedJsonMessage receivedMessage = messages.get(0);
		Collection<String> foo = (Collection<String>) receivedMessage.getHapiHeaders().getCustomHeaders().get("foo");

		assertThat(foo).containsExactlyInAnyOrder("bar", "bar2");
	}

	@Test
	public void testMessageSubscriptionWithPayloadSearchMode() throws URISyntaxException {
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY), ArgumentMatchers.any(HookParams.class))).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_MESSAGE_DELIVERY), any())).thenReturn(false);
		when(myBrokerClient.getOrCreateProducer(any(), any(), any())).thenAnswer(t -> myChannelProducer);
		when(myDaoRegistry.getResourceDao(anyString())).thenReturn(myResourceDao);
		when(myMatchUrlService.translateMatchUrl(any(), any(), any())).thenReturn(new SearchParameterMap());

		Patient p1 = generatePatient();
		Patient p2 = generatePatient();

		IBundleProvider bundleProvider = new SimpleBundleProvider(List.of(p1,p2));
		when(myResourceDao.search(any(), any())).thenReturn(bundleProvider);

		CanonicalSubscription subscription = generateSubscription();
		subscription.setPayloadSearchCriteria("Patient?_include=*");

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, p1, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		mySubscriptionDeliveringMessageListener.handleMessage(payload);

		ArgumentCaptor<ResourceModifiedJsonMessage> captor = ArgumentCaptor.forClass(ResourceModifiedJsonMessage.class);
		verify(myChannelProducer).send(captor.capture());
		final List<ResourceModifiedJsonMessage> messages = captor.getAllValues();
		assertThat(messages).hasSize(1);

		ResourceModifiedMessage receivedMessage = messages.get(0).getPayload();
		assertEquals(receivedMessage.getPayloadId(), "Bundle");

		Bundle receivedBundle = (Bundle) receivedMessage.getResource(myCtx);
		assertThat(receivedBundle.getEntry()).hasSize(2);
		assertEquals(p1.getIdElement().getValue(), receivedBundle.getEntry().get(0).getResource().getIdElement().getValue());
		assertEquals(p2.getIdElement().getValue(), receivedBundle.getEntry().get(1).getResource().getIdElement().getValue());

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

		mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));

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
		assertThat(jsonString).contains("\"partitionDate\":[2020,1,1]");
		assertThat(jsonString).contains("\"partitionIds\":[123]");
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

		assertThat(jsonString).contains("\"operationType\":\"CREATE");
		assertThat(jsonString).contains("\"canonicalSubscription\":");

		// Assert that the default partitionID is being generated and is being serialized in JSON
		assertThat(jsonString).contains("\"allPartitions\":false");
		assertThat(jsonString).contains("\"partitionIds\":[null]");
	}

	@Test
	public void testDeliveryMessageWithPartition() throws URISyntaxException {
		RequestPartitionId thePartitionId = RequestPartitionId.fromPartitionId(123, LocalDate.of(2020, 1, 1));
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY), any())).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.SUBSCRIPTION_AFTER_MESSAGE_DELIVERY), any())).thenReturn(false);
		when(myBrokerClient.getOrCreateProducer(any(), any(), any())).thenAnswer(t -> myChannelProducer);

		CanonicalSubscription subscription = generateSubscription();
		Patient patient = generatePatient();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);
		payload.setPartitionId(thePartitionId);

		mySubscriptionDeliveringMessageListener.handleMessage(payload);
		verify(myBrokerClient).getOrCreateProducer(any(), any(), any());
		ArgumentCaptor<ResourceModifiedJsonMessage> captor = ArgumentCaptor.forClass(ResourceModifiedJsonMessage.class);
		verify(myChannelProducer).send(captor.capture());
		final List<ResourceModifiedJsonMessage> params = captor.getAllValues();
		assertEquals(thePartitionId, params.get(0).getPayload().getPartitionId());
	}

	@Test
	public void testSerializeLegacyDeliveryMessage() throws JsonProcessingException {
		String legacyDeliveryMessageJson = "{\"headers\":{\"retryCount\":0,\"customHeaders\":{}},\"payload\":{\"operationType\":\"CREATE\",\"canonicalSubscription\":{\"id\":\"Subscription/123\",\"endpointUrl\":\"http://example.com/fhir\",\"payload\":\"application/fhir+json\"},\"payload\":\"{\\\"resourceType\\\":\\\"Patient\\\",\\\"active\\\":true}\"}}";

		ResourceDeliveryJsonMessage jsonMessage = ResourceDeliveryJsonMessage.fromJson(legacyDeliveryMessageJson);

		ourLog.info(jsonMessage.getPayload().getPartitionId().asJson());

		assertNotNull(jsonMessage.getPayload().getPartitionId());
		assertEquals(jsonMessage.getPayload().getPartitionId().toJson(), RequestPartitionId.defaultPartition().toJson());
	}

	@Test
	public void testRestHookDeliveryFails_raisedExceptionShouldNotIncludeSubmittedResource() {
		when(myInterceptorBroadcaster.callHooks(any(), any())).thenReturn(true);

		String familyName = "FAMILY";

		Patient patient = generatePatient();
		patient.addName().setFamily(familyName);
		CanonicalSubscription subscription = generateSubscription();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		when(myGenericClient.update()).thenThrow(new InternalErrorException("FOO"));

		try {
			mySubscriptionDeliveringRestHookListener.handleMessage(new ResourceDeliveryJsonMessage(payload));
			fail("");
		} catch (MessagingException e) {
			String messageExceptionAsString = e.toString();
			assertFalse(messageExceptionAsString.contains(familyName));
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {"message", "email"})
	public void testMessageAndEmailSubscriber_whenPayloadIsNull_shouldTryInflateMessage(String theSubscriber) {
		// setup
		when(myInterceptorBroadcaster.callHooks(any(), any())).thenReturn(true);

		Patient patient = generatePatient();

		CanonicalSubscription subscription = generateSubscription();

		ResourceDeliveryMessage payload = new ResourceDeliveryMessage();
		payload.setSubscription(subscription);
		payload.setPayload(myCtx, patient, EncodingEnum.JSON);
		payload.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);

		// mock the inflated message
		when(myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessageOrNull(any())).thenReturn(any());

		// this will null out the payload but keep the resource id and version.
		payload.setPayloadToNull();

		// execute & verify
        switch (theSubscriber) {
            case "message" ->
                    assertThrows(MessagingException.class, () -> mySubscriptionDeliveringMessageListener.handleMessage(new ResourceDeliveryJsonMessage(payload)));
            case "email" ->
                    assertThrows(MessagingException.class, () -> mySubscriptionDeliveringEmailListener.handleMessage(new ResourceDeliveryJsonMessage(payload)));
        }

		verify(myResourceModifiedMessagePersistenceSvc, times(1)).inflatePersistedResourceModifiedMessageOrNull(any());
	}

	@Nonnull
	private Patient generatePatient() {
		Patient patient = new Patient();
		patient.setActive(true);
		return patient;
	}

	@Nonnull
	private CanonicalSubscription generateSubscription() {
		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setIdElement(new IdType("Subscription/123"));
		subscription.setEndpointUrl("http://example.com/fhir");
		subscription.setPayloadString("application/fhir+json");
		return subscription;
	}

}
