package ca.uhn.fhir.jpa.subscription.async;

import ca.uhn.fhir.broker.TestMessageListenerWithLatch;
import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookListener;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContextConfiguration(classes = {AsyncSubscriptionMessageSubmissionIT.SpringConfig.class})
public class AsyncSubscriptionMessageSubmissionIT extends BaseSubscriptionsR4Test {

	/**
	 * A page of persisted rows small enough to stay well under the default
	 * {@link SubscriptionSettings#getSubscriptionSubmissionBatchSize()}, so that a single delivery pass fetches all of
	 * them in one page.
	 */
	private static final int NUMBER_OF_ROWS_TO_SEED = 5;

	/**
	 * A batch size deliberately smaller than {@link #NUMBER_OF_ROWS_TO_SEED} so that a single delivery pass has to
	 * drain the seeded rows over several batches.
	 */
	private static final int SMALL_SUBMISSION_BATCH_SIZE = 2;

	private static final String RESOURCE_MODIFIED_TABLE_NAME = "HFJ_RESOURCE_MODIFIED";

	@RegisterExtension
	public LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(AsyncResourceModifiedSubmitterSvc.class.getName(), Level.DEBUG);

	@SpyBean
	IResourceModifiedConsumer myResourceModifiedConsumer;

	@Autowired
	AsyncResourceModifiedSubmitterSvc myAsyncResourceModifiedSubmitterSvc;

	@Autowired
	private SubscriptionChannelFactory myChannelFactory;

	@Autowired SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@Autowired
	StoppableSubscriptionDeliveringRestHookListener myStoppableSubscriptionDeliveringRestHookListener;
	private TestMessageListenerWithLatch<ResourceModifiedJsonMessage, ResourceModifiedMessage> myTestMessageListenerWithLatchWithLatch;

	@Autowired
	private IResourceModifiedDao myResourceModifiedDao;
	private IChannelConsumer<ResourceModifiedMessage> myConsumer;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookListener() {
		myStoppableSubscriptionDeliveringRestHookListener.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookListener.resume();
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(new SubscriptionSettings().isTriggerSubscriptionsForNonVersioningChanges());
		mySubscriptionSettings.setSubscriptionSubmissionBatchSize(new SubscriptionSettings().getSubscriptionSubmissionBatchSize());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
		myConsumer.close();
	}

	@BeforeEach
	public void beforeRegisterRestHookListenerAndSchedulePoisonPillInterceptor() {
		mySubscriptionTestUtil.registerMessageInterceptor();

		myTestMessageListenerWithLatchWithLatch = new TestMessageListenerWithLatch<>(ResourceModifiedJsonMessage.class, ResourceModifiedMessage.class);
		myConsumer = myChannelFactory.newMatchingConsumer("my-queue-name", myTestMessageListenerWithLatchWithLatch, new ChannelConsumerSettings());

		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
	}

	@Test
	public void testSpringInjects_BeanOfTypeSubscriptionMatchingInterceptor_whenBeanDeclarationIsOverwrittenLocally(){
		assertFalse(mySubscriptionMatcherInterceptor instanceof SynchronousSubscriptionMatcherInterceptor);
	}

	@Test
	public void runDeliveryPass_withManyResources_isBatchedAndKeepsResourceUsageDown() throws InterruptedException {
		// setup
		String resourceType = "Patient";
		int factor = 5;
		int numberOfResourcesToCreate = factor * mySubscriptionSettings.getSubscriptionSubmissionBatchSize();

		ResourceModifiedEntity entity = new ResourceModifiedEntity();
		PersistedResourceModifiedMessageEntityPK rpm = new PersistedResourceModifiedMessageEntityPK();
		rpm.setResourceVersion("1");
		rpm.setResourceType(resourceType);
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

		List<ILoggingEvent> events = myLogbackTestExtension.getLogEvents(e -> e.getLevel() == Level.DEBUG && e.getFormattedMessage().contains("Attempting to submit"));
		assertEquals(factor, events.size());
	}

	@Test
	// the purpose of this test is to assert that a single delivery pass amortizes the cost of draining a page of
	// persisted rows: every row is delivered, and the page is removed from HFJ_RESOURCE_MODIFIED with a single
	// DELETE statement rather than one statement per row.
	void runDeliveryPass_withPageOfPersistedRows_drainsRowsInOneBatchAndDeliversEveryMessage() throws Exception {
		String aCode = "zoop";
		String aSystem = "SNOMED-CT";

		// given a MESSAGE subscription whose terminal endpoint is the queue our test listener consumes
		createAndSubmitSubscriptionWithCriteria("[Observation]");
		waitForActivatedSubscriptionCount(1);

		// drain everything the subscription create and activation left behind so that we measure only our own rows
		waitForQueueToDrain();
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		waitForQueueToDrain();
		myResourceModifiedDao.deleteAll();
		assertCountOfResourcesNeedingSubmission(0);
		myTestMessageListenerWithLatchWithLatch.clear();

		List<String> expectedPayloadIds = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS_TO_SEED; i++) {
			Observation observation = sendObservation(aCode, aSystem);
			expectedPayloadIds.add(observation.getIdElement().toUnqualifiedVersionless().getValue());
		}
		assertThat(myResourceModifiedDao.count()).isEqualTo(NUMBER_OF_ROWS_TO_SEED);

		// when
		myCaptureQueriesListener.clear();
		myTestMessageListenerWithLatchWithLatch.setExpectedCount(NUMBER_OF_ROWS_TO_SEED);
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		myTestMessageListenerWithLatchWithLatch.awaitExpected();

		// then - every row was drained
		assertThat(myResourceModifiedDao.count()).isZero();

		// then - every message reached the real subscription terminal endpoint
		assertThat(myTestMessageListenerWithLatchWithLatch.getReceivedMessages())
			.extracting(theMessage -> theMessage.getPayload().getPayloadId())
			.containsExactlyInAnyOrderElementsOf(expectedPayloadIds);

		// then - the whole page was removed with a single DELETE, not one DELETE per row
		List<SqlQuery> resourceModifiedDeletes = myCaptureQueriesListener.getDeleteQueries().stream()
			.filter(theQuery -> theQuery.getSql(false, false).toUpperCase(Locale.ROOT).contains(RESOURCE_MODIFIED_TABLE_NAME))
			.toList();
		assertThat(resourceModifiedDeletes).hasSize(1);
		// myCaptureQueriesListener collapses identical SQL that Hibernate JDBC-batches into a single SqlQuery whose
		// getSize() is the number of statements in the batch. Without this assertion a delete that still runs once per
		// row, but inside one batched transaction, would collapse to one SqlQuery and pass the check above while
		// performing N round trips - exactly the defect this test exists to catch.
		assertThat(resourceModifiedDeletes.get(0).getSize()).isEqualTo(1);
	}

	@Test
	// the purpose of this test is to assert that the configured submission batch size is what drives how many rows a
	// single delivery pass drains at a time: a batch size smaller than the number of waiting rows must make the pass
	// drain them over several batches, while still emptying the table.
	void runDeliveryPass_withSmallConfiguredBatchSize_drainsPersistedRowsInSeveralBatches() throws Exception {
		// given a configured batch size smaller than the number of rows waiting for submission
		mySubscriptionSettings.setSubscriptionSubmissionBatchSize(SMALL_SUBMISSION_BATCH_SIZE);
		myResourceModifiedDao.deleteAll();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		for (int i = 0; i < NUMBER_OF_ROWS_TO_SEED; i++) {
			myPatientDao.create(new Patient(), requestDetails);
		}
		assertThat(myResourceModifiedDao.count()).isEqualTo(NUMBER_OF_ROWS_TO_SEED);

		// when
		myCaptureQueriesListener.clear();
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		// then - the pass still drains every row
		assertThat(myResourceModifiedDao.count()).isZero();

		// then - the rows left HFJ_RESOURCE_MODIFIED one batch at a time.  The very same rows are removed by a single
		// DELETE when the batch size is left at its default (see
		// runDeliveryPass_withPageOfPersistedRows_drainsRowsInOneBatchAndDeliversEveryMessage), so this count is only
		// reachable if the configured value really is the page and batch size the delivery pass works with.
		int expectedNumberOfBatches =
			(NUMBER_OF_ROWS_TO_SEED + SMALL_SUBMISSION_BATCH_SIZE - 1) / SMALL_SUBMISSION_BATCH_SIZE;
		List<SqlQuery> resourceModifiedDeletes = myCaptureQueriesListener.getDeleteQueries().stream()
			.filter(theQuery -> theQuery.getSql(false, false).toUpperCase(Locale.ROOT).contains(RESOURCE_MODIFIED_TABLE_NAME))
			.toList();
		assertThat(resourceModifiedDeletes).hasSize(expectedNumberOfBatches);

		waitForQueueToDrain();
	}

	@Test
	// the purpose of this test is to assert that no persisted row is lost when the broker rejects a message part way
	// through a delivery pass: every row must still be in the database afterwards, and a later pass must drain them.
	void runDeliveryPass_whenBrokerRejectsMessageMidBatch_leavesEveryRowInTheDatabaseAndNextPassDrainsThem() throws Exception {
		// given N persisted rows and no subscription, so nothing reaches the matching channel while we seed
		myResourceModifiedDao.deleteAll();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		List<String> expectedPayloadIds = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS_TO_SEED; i++) {
			DaoMethodOutcome outcome = myPatientDao.create(new Patient(), requestDetails);
			expectedPayloadIds.add(outcome.getId().toUnqualifiedVersionless().getValue());
		}
		assertThat(myResourceModifiedDao.count()).isEqualTo(NUMBER_OF_ROWS_TO_SEED);

		// given a broker that rejects the third message it is handed
		RecordAndFailNthMessageInterceptor failingInterceptor = new RecordAndFailNthMessageInterceptor(3);
		LinkedBlockingChannel matchingChannel = mySubscriptionTestUtil.getMatchingChannel();
		matchingChannel.addInterceptor(failingInterceptor);

		// when the pass runs against the failing broker
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		// then - nothing may be lost: every row is still waiting for submission
		assertThat(myResourceModifiedDao.count()).isEqualTo(NUMBER_OF_ROWS_TO_SEED);
		assertCountOfResourcesNeedingSubmission(NUMBER_OF_ROWS_TO_SEED);

		// when the broker recovers and the next pass runs
		failingInterceptor.disableFailure();
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		// then - the table drains and every payload reached the matching channel across the two passes
		assertThat(myResourceModifiedDao.count()).isZero();
		assertThat(failingInterceptor.getObservedPayloadIds()).containsAll(expectedPayloadIds);

		waitForQueueToDrain();
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
		myTestMessageListenerWithLatchWithLatch.setExpectedCount(1);
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		myTestMessageListenerWithLatchWithLatch.awaitExpected();

		//then
		assertCountOfResourcesNeedingSubmission(0);
		assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(1);

		Observation observation = (Observation) fetchSingleResourceFromSubscriptionTerminalEndpoint();
		Coding coding = observation.getCode().getCodingFirstRep();

		assertEquals(aCode, coding.getCode());
		assertEquals(aSystem, coding.getSystem());

	}

	@ParameterizedTest
	@ValueSource(strings = {"[Observation]","Observation?identifier=1"})
	// the purpose of this test is to assert that a resource matching a given subscription is
	// delivered asynchronously to the subscription processing pipeline.
	public void testAsynchronousDeliveryOfDeletedResourceMatchingASubscription_willSucceed(String theCriteria) throws Exception {
		String aCode = "zoop";
		String aSystem = "SNOMED-CT";

		// given
		Observation obs = sendObservation(aCode, aSystem);
		List<Extension> deletedResourceExtension = Collections.singletonList(new Extension()
			.setUrl(EX_SEND_DELETE_MESSAGES)
			.setValue(new BooleanType(true)));
		createAndSubmitSubscriptionWithCriteria(theCriteria, deletedResourceExtension);
		waitForActivatedSubscriptionCount(1);

		// when
		DaoMethodOutcome outcome =  myObservationDao.delete(obs.getIdElement(), mySrd );

		assertCountOfResourcesNeedingSubmission(2);  // the subscription and the observation
		assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(0);

		// since scheduled tasks are disabled during tests, let's trigger a submission
		// just like the AsyncResourceModifiedProcessingSchedulerSvc would.
		myTestMessageListenerWithLatchWithLatch.setExpectedCount(1);
		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		List<HookParams> hookParams =  myTestMessageListenerWithLatchWithLatch.awaitExpected();

		//then
		assertCountOfResourcesNeedingSubmission(0);
		assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(1);

		Observation observation = (Observation) fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertEquals("1", observation.getIdElement().getVersionIdPart());

	}

	private void assertCountOfResourcesNeedingSubmission(int theExpectedCount) {
		assertThat(myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(
			Pageable.unpaged()))
			.hasSize(theExpectedCount);
	}

	private Subscription createAndSubmitSubscriptionWithCriteria(String theCriteria) {
		return createAndSubmitSubscriptionWithCriteria(theCriteria, null);
	}

	private Subscription createAndSubmitSubscriptionWithCriteria(String theCriteria, List<Extension> extensions) {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setPayload("application/fhir+json");
		channel.setEndpoint("channel:my-queue-name");

		if(extensions != null && !extensions.isEmpty()) {
			subscription.setExtension(extensions);
			channel.setExtension(extensions);
		}

		subscription.setChannel(channel);
		createOrUpdateSubscription(subscription);

		myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();

		return subscription;
	}


	private IBaseResource fetchSingleResourceFromSubscriptionTerminalEndpoint() {
		assertThat(myTestMessageListenerWithLatchWithLatch.getReceivedMessages()).hasSize(1);
		ResourceModifiedMessage payload = myTestMessageListenerWithLatchWithLatch.getLastReceivedMessagePayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		myTestMessageListenerWithLatchWithLatch.clear();
		return resource;
	}

	private void assertCountOfResourcesReceivedAtSubscriptionTerminalEndpoint(int expectedCount) {
		assertThat(myTestMessageListenerWithLatchWithLatch.getReceivedMessages()).hasSize(expectedCount);
	}

	/**
	 * Records the payload id of every message handed to the matching channel and, until
	 * {@link #disableFailure()} is called, rejects the n-th one the way a broker that refuses a publish would.
	 * Spring rethrows a {@link MessageDeliveryException} raised from <code>preSend</code> unchanged, which is
	 * exactly the failure the production submitter catches.
	 */
	private static class RecordAndFailNthMessageInterceptor implements ChannelInterceptor {
		private final int myFailOnMessageNumber;
		private final AtomicInteger mySeenCount = new AtomicInteger();
		private final Set<String> myObservedPayloadIds = ConcurrentHashMap.newKeySet();
		private volatile boolean myFailureEnabled = true;

		RecordAndFailNthMessageInterceptor(int theFailOnMessageNumber) {
			myFailOnMessageNumber = theFailOnMessageNumber;
		}

		@Override
		public Message<?> preSend(Message<?> theMessage, MessageChannel theChannel) {
			int seen = mySeenCount.incrementAndGet();

			if (theMessage instanceof ResourceModifiedJsonMessage resourceModifiedJsonMessage) {
				myObservedPayloadIds.add(resourceModifiedJsonMessage.getPayload().getPayloadId());
			}

			if (myFailureEnabled && seen == myFailOnMessageNumber) {
				throw new MessageDeliveryException(theMessage, "Simulated broker rejection of message " + seen);
			}

			return theMessage;
		}

		void disableFailure() {
			myFailureEnabled = false;
		}

		Set<String> getObservedPayloadIds() {
			return myObservedPayloadIds;
		}
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
