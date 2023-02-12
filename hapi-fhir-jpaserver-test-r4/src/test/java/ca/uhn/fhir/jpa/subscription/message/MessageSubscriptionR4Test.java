package ca.uhn.fhir.jpa.subscription.message;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test the rest-hook subscriptions
 */
public class MessageSubscriptionR4Test extends BaseSubscriptionsR4Test {
	@Autowired
	private SubscriptionChannelFactory myChannelFactory ;
	private static final Logger ourLog = LoggerFactory.getLogger(MessageSubscriptionR4Test.class);
	private TestQueueConsumerHandler<ResourceModifiedJsonMessage> handler;

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		myDaoConfig.setTriggerSubscriptionsForNonVersioningChanges(new DaoConfig().isTriggerSubscriptionsForNonVersioningChanges());
	}
	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerMessageInterceptor();

		IChannelReceiver receiver = myChannelFactory.newMatchingReceivingChannel("my-queue-name", new ChannelConsumerSettings());
		handler = new TestQueueConsumerHandler();
		receiver.subscribe(handler);
	}

	private Subscription createObservationSubscription() {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria("[Observation]");

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setPayload("application/fhir+json");
		channel.setEndpoint("channel:my-queue-name");

		subscription.setChannel(channel);
		postOrPutSubscription(subscription);
		return subscription;
	}


	private static Stream<Arguments> sourceTypes() {
		return Stream.of(
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, "explicit-source", null, "explicit-source"),
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.REQUEST_ID, null, null, null)
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI, "explicit-source", "request-id", "explicit-source"),
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, "explicit-source", "request-id", "explicit-source#request-id"),
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI, "explicit-source", null, "explicit-source"),
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, null, "request-id", "#request-id"),
			Arguments.of(DaoConfig.StoreMetaSourceInformationEnum.REQUEST_ID, "explicit-source", "request-id", "#request-id"),
		);
	}
	@ParameterizedTest
	@MethodSource("sourceTypes")
	public void testCreateUpdateAndPatchRetainCorrectSourceThroughDelivery(DaoConfig.StoreMetaSourceInformationEnum theStorageStyle, String theExplicitSource, String theRequestId, String theExpectedSourceValue) throws Exception {
		myDaoConfig.setStoreMetaSourceInformation(theStorageStyle);
		createObservationSubscription();

		waitForActivatedSubscriptionCount(1);

		Observation obs = sendObservation("zoop", "SNOMED-CT", theExplicitSource, theRequestId);

		//Quick validation source stored.
		Observation readObs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());
		assertThat(readObs.getMeta().getSource(), is(equalTo(theExpectedSourceValue)));

		// Should see 1 subscription notification
		waitForQueueToDrain();

		//Should receive at our queue receiver
		Observation receivedObs = fetchSingleObservationFromSubscriptionTerminalEndpoint();
		assertThat(receivedObs.getMeta().getSource(), is(equalTo(theExpectedSourceValue)));
	}
	private Observation fetchSingleObservationFromSubscriptionTerminalEndpoint() {
		assertThat(handler.getMessages().size(), is(equalTo(1)));
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = handler.getMessages().get(0);
		ResourceModifiedMessage payload = resourceModifiedJsonMessage.getPayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		Observation receivedObs = (Observation) resource;
		handler.clearMessages();
		return receivedObs;
	}


}
