package ca.uhn.fhir.jpa.subscription.async;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.message.TestQueueConsumerHandler;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContextConfiguration(classes = {AsyncSubscriptionMessageSubmissionIT.SpringConfig.class})
public class AsyncSubscriptionMessageSubmissionIT extends BaseSubscriptionsR4Test {

	@SpyBean
	IResourceModifiedConsumer myResourceModifiedConsumer;

	@Autowired
	AsyncResourceModifiedSubmitterSvc myAsyncResourceModifiedSubmitterSvc;

	@Autowired
	private SubscriptionChannelFactory myChannelFactory;

	@Autowired SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;
	private TestQueueConsumerHandler<ResourceModifiedJsonMessage> myQueueConsumerHandler;

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

		IChannelReceiver receiver = myChannelFactory.newMatchingReceivingChannel("my-queue-name", new ChannelConsumerSettings());
		myQueueConsumerHandler = new TestQueueConsumerHandler();
		receiver.subscribe(myQueueConsumerHandler);

		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
	}

	@Test
	public void testSpringInjects_BeanOfTypeSubscriptionMatchingInterceptor_whenBeanDeclarationIsOverwrittenLocally(){
		assertFalse(mySubscriptionMatcherInterceptor instanceof SynchronousSubscriptionMatcherInterceptor);
	}

	@Test
	// the purpose of this test is to assert that a resource matching a given subscription is
	// delivered asynchronously to the subscription processing pipeline.
	public void testAsynchronousDeliveryOfResourceMatchingASubscription_willSucceed() throws Exception {
		String aCode = "zoop";
		String aSystem = "SNOMED-CT";
		// given
		createAndSubmitSubscriptionWithCriteria("[Observation]");
		waitForActivatedSubscriptionCount(1);

		// when
		Observation obs = sendObservation(aCode, aSystem);

		assertCountOfResourcesNeedingSubmission(2);  // the subscription and the observation
		assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(0);

		// since scheduled tasks are disabled during tests, let's trigger a submission
		// just like the AsyncResourceModifiedProcessingSchedulerSvc would.
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		//then
		waitForQueueToDrain();
		assertCountOfResourcesNeedingSubmission(0);
		assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(1);

		Observation observation = (Observation) fetchSingleResourceFromSubscriptionTerminalEndpoint();
		Coding coding = observation.getCode().getCodingFirstRep();

		assertThat(coding.getCode(), equalTo(aCode));
		assertThat(coding.getSystem(), equalTo(aSystem));

	}

	private void assertCountOfResourcesNeedingSubmission(int theExpectedCount) {
		assertThat(myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(), hasSize(theExpectedCount));
	}

	private Subscription createAndSubmitSubscriptionWithCriteria(String theCriteria) {
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

		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		return subscription;
	}


	private IBaseResource fetchSingleResourceFromSubscriptionTerminalEndpoint() {
		assertThat(myQueueConsumerHandler.getMessages().size(), is(equalTo(1)));
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = myQueueConsumerHandler.getMessages().get(0);
		ResourceModifiedMessage payload = resourceModifiedJsonMessage.getPayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		myQueueConsumerHandler.clearMessages();
		return resource;
	}

	private void assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(int expectedCount) {
		assertThat(myQueueConsumerHandler.getMessages(), hasSize(expectedCount));
	}

	@Configuration
	public static class SpringConfig {

		@Primary
		@Bean
		public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
			return new SubscriptionMatcherInterceptor();
		}
	}

}
