package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.subscription.util.SubscriptionRulesInterceptor;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.jpa.topic.R4SubscriptionTopicBuilder;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static ca.uhn.fhir.interceptor.api.Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubscriptionRulesInterceptorR4Test extends BaseSubscriptionsR4Test {

	@ParameterizedTest
	@ValueSource(strings = {"REQUESTED", "ACTIVE"})
	public void testCriteriaFilter(Subscription.SubscriptionStatus theInitialStatus) throws InterruptedException {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		PointcutLatch latch = new PointcutLatch(SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		myInterceptorRegistry.registerAnonymousInterceptor(SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, latch);

		// Test
		Subscription subsGood = new Subscription();
		subsGood.setId("GOOD");
		subsGood.setStatus(theInitialStatus);
		subsGood.setCriteria("Observation?identifier=123"); // acceptable
		subsGood.getChannel().setEndpoint("http://localhost:8888");
		subsGood.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		latch.setExpectedCount(1);
		mySubscriptionDao.update(subsGood, mySrd);
		latch.awaitExpected();

		Subscription subsBad = new Subscription();
		subsBad.setId("BAD");
		subsBad.setStatus(theInitialStatus);
		subsBad.setCriteria("Observation?"); // not acceptable
		subsBad.getChannel().setEndpoint("http://localhost:8888");
		subsBad.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		assertThatThrownBy(()-> mySubscriptionDao.update(subsBad, mySrd))
			.hasMessageContaining("Criteria is not permitted on this server: Observation?");

		// Verify
		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subsGood.getStatus());
		assertNull(subsGood.getReason());

		assertThat(mySubscriptionRegistry.getAll()).hasSize(1);
	}

	@ParameterizedTest
	@ValueSource(strings = {"REQUESTED", "ACTIVE"})
	public void testValidateEndpoint(Subscription.SubscriptionStatus theInitialStatus) throws InterruptedException {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.setValidateRestHookEndpointIsReachable(true);
		registerInterceptor(interceptor);

		PointcutLatch latch = new PointcutLatch(SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
		myInterceptorRegistry.registerAnonymousInterceptor(SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, latch);

		// Test
		Subscription subsGood = new Subscription();
		subsGood.setId("GOOD");
		subsGood.setStatus(theInitialStatus);
		subsGood.setCriteria("Observation?identifier=123");
		subsGood.getChannel().setEndpoint(ourRestfulServer.getBaseUrl()); // reachable
		subsGood.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		latch.setExpectedCount(1);
		mySubscriptionDao.update(subsGood, mySrd);
		latch.awaitExpected();

		Subscription subsBad = new Subscription();
		subsBad.setId("BAD");
		subsBad.setStatus(theInitialStatus);
		subsBad.setCriteria("Observation?identifier=123");
		subsBad.getChannel().setEndpoint("http://this-does-not-exist"); // can't reach
		subsBad.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		assertThatThrownBy(() -> mySubscriptionDao.update(subsBad, mySrd))
			.hasMessageContaining("REST HOOK endpoint is not reachable: http://this-does-not-exist");

		// Verify
		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subsGood.getStatus());
		assertNull(subsGood.getReason());

		assertThat(mySubscriptionRegistry.getAll()).hasSize(1);
	}

	@Test
	public void testR4SubscriptionTopic_NotAcceptable() {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		R4SubscriptionTopicBuilder builder = new R4SubscriptionTopicBuilder()
			.setUrl("http://foo")
			.setStatus(Enumerations.PublicationStatus.ACTIVE)
			.addResourceTrigger()
			.setResourceTriggerResource("Encounter")
			.addResourceTriggerQueryCriteria()
			.setResourceTriggerQueryCriteriaCurrent("Encounter?");
		Basic subscriptionTopic = builder.build();

		// Test and Verify
		assertThatThrownBy(()->myBasicDao.create(subscriptionTopic, mySrd))
			.hasMessageContaining("Criteria is not permitted on this server: Encounter?");
	}

	@Test
	public void testR4SubscriptionTopic_Acceptable() {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		R4SubscriptionTopicBuilder builder = new R4SubscriptionTopicBuilder()
			.setUrl("http://foo")
			.setStatus(Enumerations.PublicationStatus.ACTIVE)
			.addResourceTrigger()
			.setResourceTriggerResource("Encounter")
			.addResourceTriggerQueryCriteria()
			.setResourceTriggerQueryCriteriaCurrent("Encounter?code=active");
		Basic subscriptionTopic = builder.build();

		// Test and Verify
		assertDoesNotThrow(()->myBasicDao.create(subscriptionTopic, mySrd));
	}

	@Test
	public void testR4SubscriptionTopic_NotSubscriptionTopic() {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		Basic subscriptionTopic = new Basic();
		subscriptionTopic.getCode().setText("Hello");

		// Test and Verify
		assertDoesNotThrow(()->myBasicDao.create(subscriptionTopic, mySrd));
	}


}
