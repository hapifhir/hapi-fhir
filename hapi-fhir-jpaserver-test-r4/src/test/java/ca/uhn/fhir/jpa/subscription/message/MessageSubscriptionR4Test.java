package ca.uhn.fhir.jpa.subscription.message;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessagePK;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.HEADER_META_SNAPSHOT_MODE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the rest-hook subscriptions
 */
public class MessageSubscriptionR4Test extends BaseSubscriptionsR4Test {
	@Autowired
	private SubscriptionChannelFactory myChannelFactory ;
	private static final Logger ourLog = LoggerFactory.getLogger(MessageSubscriptionR4Test.class);
	private TestQueueConsumerHandler<ResourceModifiedJsonMessage> handler;

	@Autowired
	IResourceModifiedDao myResourceModifiedDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		mySubscriptionSettings.setTriggerSubscriptionsForNonVersioningChanges(new SubscriptionSettings().isTriggerSubscriptionsForNonVersioningChanges());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
	}

	@Override
    @BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerMessageInterceptor();

		IChannelReceiver receiver = myChannelFactory.newMatchingReceivingChannel("my-queue-name", new ChannelConsumerSettings());
		handler = new TestQueueConsumerHandler();
		receiver.subscribe(handler);
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

	private static Stream<Arguments> sourceTypes() {
		return Stream.of(
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, "explicit-source", null, "explicit-source"),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.REQUEST_ID, null, null, null),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI, "explicit-source", "request-id", "explicit-source"),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, "explicit-source", "request-id", "explicit-source#request-id"),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI, "explicit-source", null, "explicit-source"),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID, null, "request-id", "#request-id"),
			Arguments.of(JpaStorageSettings.StoreMetaSourceInformationEnum.REQUEST_ID, "explicit-source", "request-id", "#request-id")
		);
	}

	@ParameterizedTest
	@MethodSource("sourceTypes")
	public void testCreateUpdateAndPatchRetainCorrectSourceThroughDelivery(JpaStorageSettings.StoreMetaSourceInformationEnum theStorageStyle, String theExplicitSource, String theRequestId, String theExpectedSourceValue) throws Exception {
		myStorageSettings.setStoreMetaSourceInformation(theStorageStyle);
		createSubscriptionWithCriteria("[Observation]");

		waitForActivatedSubscriptionCount(1);

		Observation obs = sendObservation("zoop", "SNOMED-CT", theExplicitSource, theRequestId);

		//Quick validation source stored.
		Observation readObs = myObservationDao.read(obs.getIdElement().toUnqualifiedVersionless());
		assertEquals(theExpectedSourceValue, readObs.getMeta().getSource());

		// Should see 1 subscription notification
		waitForQueueToDrain();

		//Should receive at our queue receiver
		IBaseResource resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(resource).isInstanceOf(Observation.class);
		Observation receivedObs = (Observation) resource;
		assertEquals(theExpectedSourceValue, receivedObs.getMeta().getSource());
	}


	private static Stream<Arguments> metaTagsSource(){
		List<Header> snapshotModeHeader = asList(new Header(HEADER_META_SNAPSHOT_MODE, "TAG"));

		return Stream.of(
				Arguments.of(asList("tag-1","tag-2"), asList("tag-3"), asList("tag-1","tag-2","tag-3"), emptyList()),
				Arguments.of(asList("tag-1","tag-2"), asList("tag-1","tag-2","tag-3"), asList("tag-1","tag-2","tag-3"), emptyList()),
				Arguments.of(emptyList(), asList("tag-1","tag-2"), asList("tag-1","tag-2"), emptyList()),
//				Arguments.of(asList("tag-1","tag-2"), emptyList(), asList("tag-1","tag-2"), emptyList()), // will not trigger an update since tags are merged
				Arguments.of(asList("tag-1","tag-2"), emptyList(), emptyList(), snapshotModeHeader),
				Arguments.of(asList("tag-1","tag-2"), asList("tag-3"), asList("tag-3"), snapshotModeHeader),
				Arguments.of(asList("tag-1","tag-2","tag-3"), asList("tag-1","tag-2"), asList("tag-1","tag-2"), snapshotModeHeader),
				Arguments.of(asList("tag-1","tag-2","tag-3"), asList("tag-2","tag-3"), asList("tag-2","tag-3"), snapshotModeHeader),
				Arguments.of(asList("tag-1","tag-2","tag-3"), asList("tag-1","tag-3"), asList("tag-1","tag-3"), snapshotModeHeader)
				);
	}
	@ParameterizedTest
	@MethodSource("metaTagsSource")
	public void testUpdateResource_withHeaderSnapshotMode_willRetainCorrectMetaTagsThroughDelivery(List<String> theTagsForCreate, List<String> theTagsForUpdate, List<String> theExpectedTags, List<Header> theHeaders) throws Exception {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		createSubscriptionWithCriteria("[Patient]");

		waitForActivatedSubscriptionCount(1);

		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().setTag(toSimpleCodingList(theTagsForCreate));

		IIdType id = myClient.create().resource(patient).execute().getId();

		waitForQueueToDrain();

		Patient receivedPatient = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(receivedPatient.getMeta().getTag()).hasSize(theTagsForCreate.size());

		patient = new Patient();
		patient.setId(id);
		patient.setActive(true);
		patient.getMeta().setTag(toSimpleCodingList(theTagsForUpdate));

		maybeAddHeaderInterceptor(myClient, theHeaders);

		myClient.update().resource(patient).execute();

		waitForQueueToDrain();

		receivedPatient = fetchSingleResourceFromSubscriptionTerminalEndpoint();;

		ourLog.info(getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(receivedPatient));

		List<String> receivedTagList = toSimpleTagList(receivedPatient.getMeta().getTag());
		assertThat(receivedTagList).containsExactlyInAnyOrderElementsOf(theExpectedTags);

	}

	@Test
	public void testMethodFindAllOrdered_willReturnAllPersistedResourceModifiedMessagesOrderedByCreatedTime(){
		Date now = new Date();
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();

		// given
		Patient patient = sendPatient();
		Organization organization = sendOrganization();

		ResourceModifiedMessage patientResourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		ResourceModifiedMessage organizationResourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, organization, BaseResourceMessage.OperationTypeEnum.CREATE);

		IPersistedResourceModifiedMessage patientPersistedMessage = myResourceModifiedMessagePersistenceSvc.persist(patientResourceModifiedMessage);
		IPersistedResourceModifiedMessage organizationPersistedMessage = myResourceModifiedMessagePersistenceSvc.persist(organizationResourceModifiedMessage);

		// when
		Page<IPersistedResourceModifiedMessage> allPersisted = myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(
			Pageable.unpaged()
		);

		// then
		assertOnPksAndOrder(allPersisted.stream().toList(), List.of(patientPersistedMessage, organizationPersistedMessage));
	}

	@Test
	public void testMethodDeleteByPK_whenEntityExists_willDeleteTheEntityAndReturnTrue(){
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();

		// given
		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		Patient patient = sendPatient();

		ResourceModifiedMessage patientResourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, patient, BaseResourceMessage.OperationTypeEnum.CREATE);
		IPersistedResourceModifiedMessage persistedResourceModifiedMessage = myResourceModifiedMessagePersistenceSvc.persist(patientResourceModifiedMessage);

		// when
		boolean wasDeleted = transactionTemplate.execute(tx -> myResourceModifiedMessagePersistenceSvc.deleteByPK(persistedResourceModifiedMessage.getPersistedResourceModifiedMessagePk()));

		// then
		assertTrue(wasDeleted);
		assertThat(myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(Pageable.unpaged()))
			.hasSize(0);
	}

	@Test
	public void testMethodDeleteByPK_whenEntityDoesNotExist_willReturnFalse(){
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();

		// given
		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		IPersistedResourceModifiedMessagePK nonExistentResourceWithPk = PersistedResourceModifiedMessageEntityPK.with("one", "one");

		// when
		boolean wasDeleted = transactionTemplate.execute(tx -> myResourceModifiedMessagePersistenceSvc.deleteByPK(nonExistentResourceWithPk));

		// then
		assertFalse(wasDeleted);
	}

	@Test
	public void testMethodInflatePersistedResourceModifiedMessage_whenGivenResourceModifiedMessageWithEmptyPayload_willEqualOriginalMessage() {
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
		// setup
		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		Observation obs = sendObservation("zoop", "SNOMED-CT", "theExplicitSource", "theRequestId");

		ResourceModifiedMessage originalResourceModifiedMessage = createResourceModifiedMessage(obs);
		ResourceModifiedMessage resourceModifiedMessageWithEmptyPayload = createResourceModifiedMessage(obs);
		resourceModifiedMessageWithEmptyPayload.setPayloadToNull();

		transactionTemplate.execute(tx -> {

			myResourceModifiedMessagePersistenceSvc.persist(originalResourceModifiedMessage);

			// execute
			ResourceModifiedMessage restoredResourceModifiedMessage = myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessage(resourceModifiedMessageWithEmptyPayload);

			// verify
			assertEquals(toJson(originalResourceModifiedMessage), toJson(restoredResourceModifiedMessage));
			assertEquals(originalResourceModifiedMessage, restoredResourceModifiedMessage);

			return null;
		});

	}

	private ResourceModifiedMessage createResourceModifiedMessage(Observation theObservation){
		ResourceModifiedMessage retVal = new ResourceModifiedMessage(myFhirContext, theObservation, BaseResourceMessage.OperationTypeEnum.CREATE);
		retVal.setSubscriptionId("subId");
		retVal.setTransactionId("txId");
		retVal.setMessageKey("messageKey");
		retVal.setMediaType("json");
		retVal.setAttribute("attKey", "attValue");
		retVal.setPartitionId(RequestPartitionId.allPartitions());
		return retVal;
	}
	private void maybeAddHeaderInterceptor(IGenericClient theClient, List<Header> theHeaders) {
		if(theHeaders.isEmpty()){
			return;
		}

		AdditionalRequestHeadersInterceptor additionalRequestHeadersInterceptor = new AdditionalRequestHeadersInterceptor();

		theHeaders.forEach(aHeader ->
			additionalRequestHeadersInterceptor
				.addHeaderValue(
					aHeader.getName(),
					aHeader.getValue()
				)
		);
		theClient.registerInterceptor(additionalRequestHeadersInterceptor);
	}

	private List<Coding> toSimpleCodingList(List<String> theTags) {
		return theTags.stream().map(theString -> new Coding().setCode(theString)).collect(Collectors.toList());
	}

	private List<String> toSimpleTagList(List<Coding> theTags) {
		return theTags.stream().map(t -> t.getCode()).collect(Collectors.toList());
	}

	private static Coding toSimpleCode(String theCode){
		return new Coding().setCode(theCode);
	}

	private <T> T fetchSingleResourceFromSubscriptionTerminalEndpoint() {
		assertThat(handler.getMessages()).hasSize(1);
		ResourceModifiedJsonMessage resourceModifiedJsonMessage = handler.getMessages().get(0);
		ResourceModifiedMessage payload = resourceModifiedJsonMessage.getPayload();
		String payloadString = payload.getPayloadString();
		IBaseResource resource = myFhirContext.newJsonParser().parseResource(payloadString);
		handler.clearMessages();
		return (T) resource;
	}

	private static String toJson(Object theRequest) {
		try {
			return new ObjectMapper().writer().writeValueAsString(theRequest);
		} catch (JsonProcessingException theE) {
			throw new AssertionError("Failure during serialization: " + theE);
		}
	}

	private static void assertOnPksAndOrder(List<IPersistedResourceModifiedMessage> theFetchedResourceModifiedMessageList, List<IPersistedResourceModifiedMessage> theCompareToList ){
		assertThat(theFetchedResourceModifiedMessageList).hasSize(theCompareToList.size());

		List<IPersistedResourceModifiedMessagePK> fetchedPks = theFetchedResourceModifiedMessageList
			.stream()
			.map(IPersistedResourceModifiedMessage::getPersistedResourceModifiedMessagePk)
			.collect(Collectors.toList());

		List<IPersistedResourceModifiedMessagePK> compareToPks = theCompareToList
			.stream()
			.map(IPersistedResourceModifiedMessage::getPersistedResourceModifiedMessagePk)
			.collect(Collectors.toList());

		assertEquals(fetchedPks, compareToPks);

	}
}
