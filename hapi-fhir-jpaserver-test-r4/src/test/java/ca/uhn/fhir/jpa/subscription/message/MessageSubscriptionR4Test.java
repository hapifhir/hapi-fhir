package ca.uhn.fhir.jpa.subscription.message;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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
	StoppableSubscriptionDeliveringRestHookSubscriber myStoppableSubscriptionDeliveringRestHookSubscriber;

	@AfterEach
	public void cleanupStoppableSubscriptionDeliveringRestHookSubscriber() {
		myStoppableSubscriptionDeliveringRestHookSubscriber.setCountDownLatch(null);
		myStoppableSubscriptionDeliveringRestHookSubscriber.unPause();
		myStorageSettings.setTriggerSubscriptionsForNonVersioningChanges(new JpaStorageSettings().isTriggerSubscriptionsForNonVersioningChanges());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
	}

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
		assertThat(readObs.getMeta().getSource(), is(equalTo(theExpectedSourceValue)));

		// Should see 1 subscription notification
		waitForQueueToDrain();

		//Should receive at our queue receiver
		IBaseResource resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(resource, instanceOf(Observation.class));
		Observation receivedObs = (Observation) resource;
		assertThat(receivedObs.getMeta().getSource(), is(equalTo(theExpectedSourceValue)));
	}

	@Test
	public void testUpdateResourceRetainCorrectMetaTagsThroughDelivery() throws Exception {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		createSubscriptionWithCriteria("[Patient]");

		waitForActivatedSubscriptionCount(1);

		// Create Patient with two meta tags
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addTag().setSystem("http://www.example.com/tags").setCode("tag-1");
		patient.getMeta().addTag().setSystem("http://www.example.com/tags").setCode("tag-2");

		IIdType id = myClient.create().resource(patient).execute().getId();

		// Should see 1 subscription notification for CREATE
		waitForQueueToDrain();

		// Should receive two meta tags
		IBaseResource resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		assertThat(resource, instanceOf(Patient.class));
		Patient receivedPatient = (Patient) resource;
		assertThat(receivedPatient.getMeta().getTag().size(), is(equalTo(2)));

		// Update the previous Patient and add one more tag
		patient = new Patient();
		patient.setId(id);
		patient.setActive(true);
		patient.getMeta().getTag().add(new Coding().setSystem("http://www.example.com/tags").setCode("tag-3"));
		myClient.update().resource(patient).execute();

		waitForQueueToDrain();

		// Should receive all three meta tags
		List<String> expected = List.of("tag-1", "tag-2", "tag-3");
		resource = fetchSingleResourceFromSubscriptionTerminalEndpoint();
		receivedPatient = (Patient) resource;
		List<Coding> receivedTagList = receivedPatient.getMeta().getTag();
		ourLog.info(getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(receivedPatient));
		assertThat(receivedTagList.size(), is(equalTo(3)));
		List<String> actual = receivedTagList.stream().map(t -> t.getCode()).sorted().collect(Collectors.toList());
		assertTrue(expected.equals(actual));
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

}
