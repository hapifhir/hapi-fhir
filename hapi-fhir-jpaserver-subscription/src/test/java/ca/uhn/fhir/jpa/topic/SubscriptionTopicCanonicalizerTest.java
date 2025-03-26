package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
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
		
		// Basic properties
		assertEquals("r4-encounter-complete", canonicalized.getId());
		assertEquals(PublicationStatus.DRAFT, canonicalized.getStatus());
		assertEquals("http://hl7.org/fhir/uv/subscriptions-backport/SubscriptionTopic/r4-encounter-complete", canonicalized.getUrl());
		assertEquals("1.2.0", canonicalized.getVersion());
		assertEquals("R4 example of a basic-converted subscription topic for completed encounters.", canonicalized.getName());
		assertEquals("Backported SubscriptionTopic: R4 Encounter Complete", canonicalized.getTitle());
		assertEquals("2019-01-01", canonicalized.getDateElement().getValueAsString());
		assertEquals("R4 example of a subscription topic for completed encounters.", canonicalized.getDescription());
		
		// Resource Trigger
		assertEquals(1, canonicalized.getResourceTrigger().size());
		assertEquals("Triggered when an encounter is completed.", canonicalized.getResourceTrigger().get(0).getDescription());
		assertEquals("http://hl7.org/fhir/StructureDefinition/Encounter", canonicalized.getResourceTrigger().get(0).getResource());
		assertEquals(2, canonicalized.getResourceTrigger().get(0).getSupportedInteraction().size());
		assertEquals("create", canonicalized.getResourceTrigger().get(0).getSupportedInteraction().get(0).getValue().toCode());
		assertEquals("update", canonicalized.getResourceTrigger().get(0).getSupportedInteraction().get(1).getValue().toCode());
		assertEquals("(%previous.id.empty() or (%previous.status != 'finished')) and (%current.status = 'finished')", 
			canonicalized.getResourceTrigger().get(0).getFhirPathCriteria());
		
		// Can Filter By
		assertEquals(2, canonicalized.getCanFilterBy().size());
		assertEquals("Filter based on the subject of an encounter.", canonicalized.getCanFilterBy().get(0).getDescription());
		assertEquals("Encounter", canonicalized.getCanFilterBy().get(0).getResource());
		assertEquals("subject", canonicalized.getCanFilterBy().get(0).getFilterParameter());
		
		assertEquals("Filter based on the group membership of the subject of an encounter.", canonicalized.getCanFilterBy().get(1).getDescription());
		assertEquals("Encounter", canonicalized.getCanFilterBy().get(1).getResource());
		assertEquals("_in", canonicalized.getCanFilterBy().get(1).getFilterParameter());
		
		// Notification Shape
		assertEquals(1, canonicalized.getNotificationShape().size());
		assertEquals("Encounter", canonicalized.getNotificationShape().get(0).getResource());
		assertEquals(7, canonicalized.getNotificationShape().get(0).getInclude().size());
		assertEquals("Encounter:patient&iterate=Patient.link", canonicalized.getNotificationShape().get(0).getInclude().get(0).getValue());
		assertEquals(1, canonicalized.getNotificationShape().get(0).getRevInclude().size());
		assertEquals("Encounter:subject", canonicalized.getNotificationShape().get(0).getRevInclude().get(0).getValue());
	}
}
