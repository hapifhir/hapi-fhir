package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionTopicCanonicalizerTest {
	@Test
	public void testCanonicalizeTopic() {
		SubscriptionTopic topic = new SubscriptionTopic();
		topic.setId("123");
		topic.setStatus(Enumerations.PublicationStatus.ACTIVE);
		org.hl7.fhir.r5.model.SubscriptionTopic canonicalized = SubscriptionTopicCanonicalizer.canonicalizeTopic(FhirContext.forR4BCached(), topic);
		assertEquals("123", canonicalized.getId());
		assertEquals(org.hl7.fhir.r5.model.Enumerations.PublicationStatus.ACTIVE, canonicalized.getStatus());
	}
}
