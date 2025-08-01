package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.provider.r5.BaseResourceProviderR5Test;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.util.SubscriptionRulesInterceptor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubscriptionRulesInterceptorR5Test extends BaseResourceProviderR5Test {

	@Test
	public void testCriteriaFilter() {
		// Setup
		SubscriptionRulesInterceptor interceptor = new SubscriptionRulesInterceptor(myFhirContext, mySubscriptionSettings, myPartitionSettings);
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
		subsGood.setEndpoint("http://localhost:8888");
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
		Assertions.assertTrue(topicCreateException.getMessage().contains("Criteria is not permitted on this server: Observation?"));

		subsGood = mySubscriptionDao.read(new IdType("Subscription/GOOD"), mySrd);
		assertEquals(Enumerations.SubscriptionStatusCodes.REQUESTED, subsGood.getStatus());
		assertNull(subsGood.getReason());
	}

}
