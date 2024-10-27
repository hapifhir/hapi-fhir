package ca.uhn.fhir.jpa.subscription.async;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.message.TestQueueConsumerHandler;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContextConfiguration(classes = {AsyncSubscriptionMessageSubmissionIT.SpringConfig.class})
public class AsyncSubscriptionMessageSubmissionIT extends BaseSubscriptionsR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncSubscriptionMessageSubmissionIT.class);

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

	@Autowired
	private IResourceModifiedDao myResourceModifiedDao;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(new SubscriptionSettings().isTriggerSubscriptionsForNonVersioningChanges());
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
	public void runDeliveryPass_withManyResources_isBatchedAndKeepsResourceUsageDown() throws JsonProcessingException, InterruptedException {
		// setup
		myLogbackTestExtension.setUp(Level.DEBUG);

		String resourceType = "Patient";
		int factor = 5;
		int numberOfResourcesToCreate = factor * AsyncResourceModifiedSubmitterSvc.MAX_LIMIT;

		ResourceModifiedEntity entity = new ResourceModifiedEntity();
		entity.setResourceType(resourceType);
		PersistedResourceModifiedMessageEntityPK rpm = new PersistedResourceModifiedMessageEntityPK();
		rpm.setResourceVersion("1");
		entity.setResourceModifiedEntityPK(rpm);

		// we reuse the same exact msg content to avoid
		// the slowdown of serializing it over and over
		SystemRequestDetails details = new SystemRequestDetails();
		// create a large number of resources
		for (int i = 0; i < numberOfResourcesToCreate; i++) {
			Patient resource = new Patient();
			resource.setId(resourceType + "/" + (1 + i));
			myPatientDao.create(resource, details);
		}

		assertEquals(numberOfResourcesToCreate, myResourceModifiedDao.count());

		// test
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		// verification
		waitForQueueToDrain();
		assertCountOfResourcesNeedingSubmission(0);

		List<ILoggingEvent> events = myLogbackTestExtension.getLogEvents(e -> {
			return e.getLevel() == Level.DEBUG && e.getFormattedMessage().contains("Attempting to submit");
		});
		assertEquals(factor, events.size());
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

		assertEquals(aCode, coding.getCode());
		assertEquals(aSystem, coding.getSystem());

	}

	private void assertCountOfResourcesNeedingSubmission(int theExpectedCount) {
		assertThat(myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(
			Pageable.unpaged()))
			.hasSize(theExpectedCount);
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
		assertThat(myQueueConsumerHandler.getMessages()).hasSize(1);
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = myQueueConsumerHandler.getMessages().get(0);
		ResourceModifiedMessage payload = resourceModifiedJsonMessage.getPayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		myQueueConsumerHandler.clearMessages();
		return resource;
	}

	private void assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(int expectedCount) {
		assertThat(myQueueConsumerHandler.getMessages()).hasSize(expectedCount);
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
