package ca.uhn.fhir.jpa.subscription.websocket;

import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR5Test;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.util.WebsocketSubscriptionClient;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.awaitility.Awaitility.await;

/**
 * Test {@link ca.uhn.fhir.jpa.subscription.match.deliver.websocket.SubscriptionWebsocketHandler} with different content types.
 */
public class WebsocketWithSubscriptionIdR5Test extends BaseSubscriptionsR5Test {
	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(WebsocketWithSubscriptionIdR5Test.class);

	@RegisterExtension
	private final WebsocketSubscriptionClient myWebsocketClientExtension =
		new WebsocketSubscriptionClient(() -> myServer, () -> mySubscriptionSettings);

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@Override
	@BeforeEach
	public void before() {
		// Register interceptor
		mySubscriptionTestUtil.registerWebSocketInterceptor();
		mySubscriptionTestUtil.registerSubscriptionLoggingInterceptor();

		// Given a subscription topic
		SubscriptionTopic subscriptionTopic = new SubscriptionTopic();
		subscriptionTopic.setUrl("Topic/123");
		subscriptionTopic.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = subscriptionTopic.addResourceTrigger();
		trigger.setResource("Patient");
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);
		myClient.create().resource(subscriptionTopic).execute();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		// Unregister interceptor
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
		myWebsocketClientExtension.afterEach(null);
	}

	@Test
	public void testSubscriptionMessagePayloadContentIsNull() {
		// Given a subscription
		Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContent(null);
		subscription.setTopic("Topic/123");
		subscription.getChannelType().setCode("websocket");
		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		String subscriptionId = methodOutcome.getId().getIdPart();

		// When
		myWebsocketClientExtension.bind(subscriptionId);

		// And
		// Trigger resource creation
		Patient patient = new Patient();
		patient.setActive(true);
		myClient.create().resource(patient).execute();

		// Then
		List<String> messages = myWebsocketClientExtension.getMessages();
		await().until(() -> !messages.isEmpty());

		// Log it
		ourLog.info("Messages: {}", messages);

		// Verify a ping message shall be returned
		Assertions.assertTrue(messages.contains("ping " + subscriptionId));
	}

	@Test
	public void testSubscriptionMessagePayloadContentIsEmpty() {
		// Given a subscription
		Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContent(Subscription.SubscriptionPayloadContent.fromCode("empty"));
		subscription.setTopic("Topic/123");
		subscription.getChannelType().setCode("websocket");
		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		String subscriptionId = methodOutcome.getId().getIdPart();

		// When
		myWebsocketClientExtension.bind(subscriptionId);

		// And
		// Trigger resource creation
		Patient patient = new Patient();
		patient.setActive(true);
		myClient.create().resource(patient).execute();

		// Then
		List<String> messages = myWebsocketClientExtension.getMessages();
		await().until(() -> !messages.isEmpty());

		// Log it
		ourLog.info("Messages: {}", messages);

		// Verify a ping message shall be returned
		Assertions.assertTrue(messages.contains("ping " + subscriptionId));
	}

	@Test
	public void testSubscriptionMessagePayloadContentIsIdOnly() {
		// Given a subscription
		Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContent(Subscription.SubscriptionPayloadContent.fromCode("id-only"));
		subscription.setTopic("Topic/123");
		subscription.getChannelType().setCode("websocket");
		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		String subscriptionId = methodOutcome.getId().getIdPart();

		// When
		myWebsocketClientExtension.bind(subscriptionId);

		// And
		// Trigger resource creation
		Patient patient = new Patient();
		patient.setActive(true);
		myClient.create().resource(patient).execute();

		// Then
		List<String> messages = myWebsocketClientExtension.getMessages();
		await().until(() -> messages.size() > 1);

		// Log it
		ourLog.info("Messages: {}", messages);

		// Verify UUID shall be returned
		Assertions.assertTrue(messages.contains("bound " + subscriptionId));
		Assertions.assertNotNull(UUID.fromString(messages.get(1)));
	}

	@Test
	public void testSubscriptionMessagePayloadContentIsFullResource() {
		// Given a subscription
		Subscription subscription = new Subscription();
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscription.setContent(Subscription.SubscriptionPayloadContent.fromCode("full-resource"));
		subscription.setTopic("Topic/123");
		subscription.getChannelType().setCode("websocket");
		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		String subscriptionId = methodOutcome.getId().getIdPart();

		// When
		myWebsocketClientExtension.bind(subscriptionId);

		// And
		// Trigger resource creation
		Patient patient = new Patient();
		patient.setActive(true);
		myClient.create().resource(patient).execute();

		// Then
		List<String> messages = myWebsocketClientExtension.getMessages();
		await().until(() -> messages.size() > 1);

		// Log it
		ourLog.info("Messages: {}", messages);

		// Verify Bundle resource shall be returned
		Assertions.assertTrue(messages.contains("bound " + subscriptionId));
		Assertions.assertNotNull(myFhirContext.newJsonParser().parseResource(messages.get(1)));
	}


}
