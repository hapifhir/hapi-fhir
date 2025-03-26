package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.ResourceUtil;
import ca.uhn.fhir.util.StreamUtil;
import org.hl7.fhir.r5.model.Enumerations.PublicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionTopicCanonicalizerTest {
	@Test
	public void testCanonicalizeR4BTopic() {
		org.hl7.fhir.r4b.model.SubscriptionTopic topic = new org.hl7.fhir.r4b.model.SubscriptionTopic();
		topic.setId("123");
		topic.setStatus(org.hl7.fhir.r4b.model.Enumerations.PublicationStatus.ACTIVE);
		org.hl7.fhir.r5.model.SubscriptionTopic canonicalized = SubscriptionTopicCanonicalizer.canonicalizeTopic(FhirContext.forR4BCached(), topic);
		assertEquals("123", canonicalized.getId());
		assertEquals(PublicationStatus.ACTIVE, canonicalized.getStatus());
	}

	@Test
	public void testCanonicalizeR4Topic() {
		String basicResourceString = ClasspathUtil.loadResource("R4SubscriptionTopic.json");
		FhirContext fhirContext = FhirContext.forR4Cached();
		org.hl7.fhir.r4.model.Basic topic = fhirContext.newJsonParser().parseResource(org.hl7.fhir.r4.model.Basic.class, basicResourceString);
		org.hl7.fhir.r5.model.SubscriptionTopic canonicalized = SubscriptionTopicCanonicalizer.canonicalizeTopic(fhirContext, topic);
		assertEquals("r4-encounter-complete", canonicalized.getId());
		assertEquals(PublicationStatus.DRAFT, canonicalized.getStatus());
	}
}
