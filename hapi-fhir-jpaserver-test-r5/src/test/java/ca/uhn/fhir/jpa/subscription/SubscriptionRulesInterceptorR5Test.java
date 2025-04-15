package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.subscription.util.SubscriptionRulesInterceptor;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubscriptionRulesInterceptorR5Test extends BaseSubscriptionsR5Test {

	@Test
	public void testCriteriaFilter() {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings);
		interceptor.addAllowedCriteriaPattern(SubscriptionRulesInterceptor.CRITERIA_WITH_AT_LEAST_ONE_PARAM);
		registerInterceptor(interceptor);

		// Test
		SubscriptionTopic topicGood = new SubscriptionTopic();
		topicGood.setId("GOOD");
		topicGood.setUrl("http://good");
		topicGood.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent topicGoodTrigger = topicGood.addResourceTrigger()
			.setResource("Patient")
			.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE)
			.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		topicGoodTrigger.getQueryCriteria()
			.setCurrent("Observation?code=http://foo|123") // acceptable
			.setRequireBoth(false);
		mySubscriptionTopicDao.update(topicGood, mySrd);

		Subscription subsGood = new Subscription();
		subsGood.setId("GOOD");
		subsGood.setStatus(Enumerations.SubscriptionStatusCodes.REQUESTED);
		subsGood.getChannelType()
			.setSystem(CanonicalSubscriptionChannelType.RESTHOOK.getSystem())
			.setCode(CanonicalSubscriptionChannelType.RESTHOOK.toCode());
		subsGood.setTopic("http://good");
		subsGood.setContentType(Constants.CT_FHIR_JSON_NEW);
		subsGood.setEndpoint("http://localhost" + ourListenerPort);
		mySubscriptionDao.update(subsGood, mySrd);

		SubscriptionTopic topicBad = new SubscriptionTopic();
		topicBad.setId("BAD");
		topicBad.setUrl("http://bad");
		topicBad.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent topicBadTrigger = topicBad.addResourceTrigger()
			.setResource("Patient")
			.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE)
			.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		topicBadTrigger.getQueryCriteria()
			.setCurrent("Observation?") // not acceptable
			.setRequireBoth(false);
		PreconditionFailedException topicCreateException = assertThrows(PreconditionFailedException.class, () -> mySubscriptionTopicDao.update(topicBad, mySrd));

		// Verify
		await().until(() -> mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd).getStatus(), t -> t == Enumerations.SubscriptionStatusCodes.ACTIVE);
		assertEquals("Criteria is not permitted on this server: Observation?", topicCreateException.getMessage());

		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Enumerations.SubscriptionStatusCodes.ACTIVE, subsGood.getStatus());
		assertNull(subsGood.getReason());

		assertThat(mySubscriptionRegistry.getAll()).hasSize(1);
	}

}
