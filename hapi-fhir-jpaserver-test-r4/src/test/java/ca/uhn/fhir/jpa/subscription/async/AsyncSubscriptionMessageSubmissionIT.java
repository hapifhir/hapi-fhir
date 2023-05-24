package ca.uhn.fhir.jpa.subscription.async;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.SimulateNetworkFailureOnChannelInterceptor;
import ca.uhn.fhir.jpa.subscription.asynch.AsyncResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.message.TestQueueConsumerHandler;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class AsyncSubscriptionMessageSubmissionIT extends BaseSubscriptionsR4Test {

	@Autowired
	ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;

	@Autowired
	AsyncResourceModifiedSubmitterSvc myAsyncResourceModifiedSubmitterSvc;

	@Autowired
	private SubscriptionChannelFactory myChannelFactory;

	private TestQueueConsumerHandler<ResourceModifiedJsonMessage> handler;

	private SimulateNetworkFailureOnChannelInterceptor mySimulateNetworkFailureChannelInterceptor = new SimulateNetworkFailureOnChannelInterceptor();
	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		myStorageSettings.setTriggerSubscriptionsForNonVersioningChanges(new JpaStorageSettings().isTriggerSubscriptionsForNonVersioningChanges());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
	}

	@BeforeEach
	public void beforeRegisterRestHookListenerAndSchedulePoisonPillInterceptor() {
		mySubscriptionTestUtil.registerMessageInterceptor();

		// register poison pill interceptor
		myResourceModifiedSubmitterSvc.startIfNeeded();
		myResourceModifiedSubmitterSvc.getProcessingChannelForUnitTest().addInterceptor(mySimulateNetworkFailureChannelInterceptor);

		IChannelReceiver receiver = myChannelFactory.newMatchingReceivingChannel("my-queue-name", new ChannelConsumerSettings());
		handler = new TestQueueConsumerHandler();
		receiver.subscribe(handler);

		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
	}

	@Test
	// the purpose of this test is to assert that a resource matching a given subscription will still
	// be delivered despite a processing channel encountering issues accessing the underlying broker.
	public void testAsynchronousDeliveryOfResourceMatchingASubscription_willSucceed() throws Exception {

		createSubscriptionWithCriteria("[Observation]");

		waitForActivatedSubscriptionCount(1);

		Observation obs = sendObservation("zoop", "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();

		//Should receive at our queue receiver
		IBaseResource resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(resource, instanceOf(Observation.class));

		// simulate a network link failure for the upcoming resource update
		mySimulateNetworkFailureChannelInterceptor.simulateNetworkOutage();

		obs.setStatus(Observation.ObservationStatus.CORRECTED);

		myObservationDao.update(obs, new SystemRequestDetails());

		// the observation was updated but the resourceModifiedMessage did not reach the queue due to the network outage
		assertThat(getQueueCount(), equalTo(0));

		// simulate network operational
		mySimulateNetworkFailureChannelInterceptor.simulateNetWorkOperational();

		// perform asynchronous delivery of message
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		waitForQueueToDrain();

		//Should receive at our queue receiver
		resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(resource, instanceOf(Observation.class));
		Observation receivedObs = (Observation) resource;
		assertThat(receivedObs.getStatus(), equalTo(Observation.ObservationStatus.CORRECTED));

		// assert that all persisted resourceModifiedMessage were deleted, ie, were processed
		assertThat(myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(), hasSize(0));

	}

	private Subscription createSubscriptionWithCriteria(String theCriteria) {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setPayload("application/fhir+json");
		channel.setEndpoint("channel:my-queue-name");

		subscription.setChannel(channel);
		postOrPutSubscription(subscription);
		return subscription;
	}

	private IBaseResource fetchSingleResourceFromSubscriptionTerminalEndpoint() {
		assertThat(handler.getMessages().size(), is(equalTo(1)));
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = handler.getMessages().get(0);
		ResourceModifiedMessage payload = resourceModifiedJsonMessage.getPayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		handler.clearMessages();
		return resource;
	}

	private int getQueueCount(){
		return handler.getMessages().size();
	}
}
