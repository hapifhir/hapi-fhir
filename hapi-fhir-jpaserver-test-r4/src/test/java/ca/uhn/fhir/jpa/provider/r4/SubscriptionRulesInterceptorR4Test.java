package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.subscription.util.SubscriptionRulesInterceptor;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionsR4Test;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubscriptionRulesInterceptorR4Test extends BaseSubscriptionsR4Test {

	@ParameterizedTest
	@ValueSource(strings = {"REQUESTED", "ACTIVE"})
	public void testCriteriaFilter(Subscription.SubscriptionStatus theInitialStatus) {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		// Test
		Subscription subsGood = new Subscription();
		subsGood.setId("GOOD");
		subsGood.setStatus(theInitialStatus);
		subsGood.setCriteria("Observation?identifier=123"); // acceptable
		subsGood.getChannel().setEndpoint("http://localhost:8888");
		subsGood.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionDao.update(subsGood, mySrd);

		Subscription subsBad = new Subscription();
		subsBad.setId("BAD");
		subsBad.setStatus(theInitialStatus);
		subsBad.setCriteria("Observation?"); // not acceptable
		subsBad.getChannel().setEndpoint("http://localhost:8888");
		subsBad.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		PreconditionFailedException e = assertThrows(PreconditionFailedException.class, () -> mySubscriptionDao.update(subsBad, mySrd));

		// Verify
		await().until(() -> mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd).getStatus(), t -> t == Subscription.SubscriptionStatus.ACTIVE);
		assertEquals("Criteria is not permitted on this server: Observation?", e.getMessage());

		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subsGood.getStatus());
		assertNull(subsGood.getReason());

		assertThat(mySubscriptionRegistry.getAll()).hasSize(1);
	}

	@ParameterizedTest
	@ValueSource(strings = {"REQUESTED", "ACTIVE"})
	public void testValidateEndpoint(Subscription.SubscriptionStatus theInitialStatus) {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.setValidateRestHookEndpointIsReachable(true);
		registerInterceptor(interceptor);

		// Test
		Subscription subsGood = new Subscription();
		subsGood.setId("GOOD");
		subsGood.setStatus(theInitialStatus);
		subsGood.setCriteria("Observation?identifier=123");
		subsGood.getChannel().setEndpoint(ourRestfulServer.getBaseUrl()); // reachable
		subsGood.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionDao.update(subsGood, mySrd);

		Subscription subsBad = new Subscription();
		subsBad.setId("BAD");
		subsBad.setStatus(theInitialStatus);
		subsBad.setCriteria("Observation?identifier=123");
		subsBad.getChannel().setEndpoint("http://this-does-not-exist"); // can't reach
		subsBad.getChannel().setType(Subscription.SubscriptionChannelType.RESTHOOK);
		PreconditionFailedException e = assertThrows(PreconditionFailedException.class, () -> mySubscriptionDao.update(subsBad, mySrd));

		// Verify
		await().until(() -> mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd).getStatus(), t -> t == Subscription.SubscriptionStatus.ACTIVE);
		assertEquals("REST HOOK endpoint is not reachable: http://this-does-not-exist", e.getMessage());

		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subsGood.getStatus());
		assertNull(subsGood.getReason());

		assertThat(mySubscriptionRegistry.getAll()).hasSize(1);
	}

}
