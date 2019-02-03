package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriber;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

public class StandaloneSubscriptionMessageHandlerTest extends BaseSubscriptionDstu3Test {

	@Autowired
	StandaloneSubscriptionMessageHandler myStandaloneSubscriptionMessageHandler;
	@Autowired
	FhirContext myFhirContext;
	@MockBean
	SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;
	@MockBean
	SubscriptionRegistry mySubscriptionRegistry;

	@Test
	public void activeSubscriptionIsRegistered() {
		Subscription subscription = makeActiveSubscription("testCriteria", "testPayload", "testEndpoint");
		ResourceModifiedMessage message = new ResourceModifiedMessage(myFhirContext, subscription, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage jsonMessage = new ResourceModifiedJsonMessage(message);
		myStandaloneSubscriptionMessageHandler.handleMessage(jsonMessage);
		Mockito.verify(mySubscriptionRegistry, never()).unregisterSubscription(any());
		Mockito.verify(mySubscriptionRegistry).registerSubscriptionUnlessAlreadyRegistered(any());
		Mockito.verify(mySubscriptionMatchingSubscriber).matchActiveSubscriptionsAndDeliver(any());
	}

	@Test
	public void requestedSubscriptionNotRegistered() {
		Subscription subscription = makeSubscriptionWithStatus("testCriteria", "testPayload", "testEndpoint", Subscription.SubscriptionStatus.REQUESTED);
		ResourceModifiedMessage message = new ResourceModifiedMessage(myFhirContext, subscription, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage jsonMessage = new ResourceModifiedJsonMessage(message);
		myStandaloneSubscriptionMessageHandler.handleMessage(jsonMessage);
		Mockito.verify(mySubscriptionRegistry, never()).unregisterSubscription(any());
		Mockito.verify(mySubscriptionRegistry, never()).registerSubscriptionUnlessAlreadyRegistered(any());
		Mockito.verify(mySubscriptionMatchingSubscriber).matchActiveSubscriptionsAndDeliver(any());
	}

	@Test
	public void deleteSubscription() {
		Subscription subscription = makeSubscriptionWithStatus("testCriteria", "testPayload", "testEndpoint", Subscription.SubscriptionStatus.REQUESTED);
		ResourceModifiedMessage message = new ResourceModifiedMessage(myFhirContext, subscription, ResourceModifiedMessage.OperationTypeEnum.DELETE);
		ResourceModifiedJsonMessage jsonMessage = new ResourceModifiedJsonMessage(message);
		myStandaloneSubscriptionMessageHandler.handleMessage(jsonMessage);
		Mockito.verify(mySubscriptionRegistry).unregisterSubscription(any());
		Mockito.verify(mySubscriptionRegistry, never()).registerSubscriptionUnlessAlreadyRegistered(any());
		Mockito.verify(mySubscriptionMatchingSubscriber, never()).matchActiveSubscriptionsAndDeliver(any());
	}
}
