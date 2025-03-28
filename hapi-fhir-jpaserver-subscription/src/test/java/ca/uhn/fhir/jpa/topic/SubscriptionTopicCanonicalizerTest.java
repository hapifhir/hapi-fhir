package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r5.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicCanonicalizerTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	void testCanonicalizeR4BTopic() {
		org.hl7.fhir.r4b.model.SubscriptionTopic topic = new org.hl7.fhir.r4b.model.SubscriptionTopic();
		topic.setId("123");
		topic.setStatus(org.hl7.fhir.r4b.model.Enumerations.PublicationStatus.ACTIVE);
		org.hl7.fhir.r5.model.SubscriptionTopic canonicalized = SubscriptionTopicCanonicalizer.canonicalizeTopic(FhirContext.forR4BCached(), topic);
		assertEquals("123", canonicalized.getId());
		assertEquals(PublicationStatus.ACTIVE, canonicalized.getStatus());
	}

	@Test
	void testCanonicalizeR4Topic() {
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

	@Test
	void testCanonicalizeR4TopicBuiltWithBuilder() throws ParseException {
		// Create a SubscriptionTopic using the R4SubscriptionTopicBuilder
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		R4SubscriptionTopicBuilder builder = new R4SubscriptionTopicBuilder()
			.setId("test-topic-id")
			.setUrl("http://example.org/topic-url")
			.setVersion("1.0.0")
			.setName("Test Topic Name")
			.setTitle("Test Topic Title")
			.setDate(sdf.parse("2023-06-01"))
			.setDescription("This is a test topic description")
			.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		
		// Add resource trigger with all supported methods
		builder.addResourceTrigger()
			.setResourceTriggerDescription("Test resource trigger description")
			.setResourceTriggerResource("Patient")
			.addResourceTriggerSupportedInteraction("create")
			.addResourceTriggerSupportedInteraction("update")
			.setResourceTriggerFhirPathCriteria("Patient.active = true")
			// Add query criteria - this is the part that might have a bug
			.addResourceTriggerQueryCriteria()
			.setResourceTriggerQueryCriteriaPrevious("Patient?active=false")
			.setResourceTriggerQueryCriteriaCurrent("Patient?active=true")
			.setResourceTriggerQueryCriteriaRequireBoth(true);
		
		// Add can-filter-by
		builder.addCanFilterBy()
			.setCanFilterByDescription("Test can filter by description")
			.setCanFilterByResource("Patient")
			.setCanFilterByParameter("name");
		
		// Add notification shape
		builder.addNotificationShape()
			.setNotificationShapeResource("Patient")
			.addNotificationShapeInclude("Patient:organization")
			.addNotificationShapeRevInclude("Observation:subject");
		
		// Build the topic
		Basic builtTopic = builder.build();
		
		// Output the JSON for debugging
		String json = ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(builtTopic);
		System.out.println("Built Topic JSON:\n" + json);
		
		// Canonicalize the topic
		SubscriptionTopic canonicalized = SubscriptionTopicCanonicalizer.canonicalizeTopic(ourFhirContext, builtTopic);
		
		// Verify basic properties
		assertEquals("test-topic-id", canonicalized.getId());
		assertEquals(PublicationStatus.ACTIVE, canonicalized.getStatus());
		assertEquals("http://example.org/topic-url", canonicalized.getUrl());
		assertEquals("1.0.0", canonicalized.getVersion());
		assertEquals("Test Topic Name", canonicalized.getName());
		assertEquals("Test Topic Title", canonicalized.getTitle());
		// Date formatting may include timezone, so we'll just check if it starts with the right date
		assertTrue(canonicalized.getDateElement().getValueAsString().startsWith("2023-06-01"));
		assertEquals("This is a test topic description", canonicalized.getDescription());
		
		// Verify resource trigger
		List<SubscriptionTopic.SubscriptionTopicResourceTriggerComponent> resourceTriggers = canonicalized.getResourceTrigger();
		assertEquals(1, resourceTriggers.size());
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = resourceTriggers.get(0);
		assertEquals("Test resource trigger description", trigger.getDescription());
		assertEquals("Patient", trigger.getResource());
		assertEquals(2, trigger.getSupportedInteraction().size());
		assertEquals("create", trigger.getSupportedInteraction().get(0).getValue().toCode());
		assertEquals("update", trigger.getSupportedInteraction().get(1).getValue().toCode());
		assertEquals("Patient.active = true", trigger.getFhirPathCriteria());
		
		// Verify query criteria component
		assertNotNull(trigger.getQueryCriteria(), "QueryCriteria component should not be null");
		assertEquals("Patient?active=false", trigger.getQueryCriteria().getPrevious());
		assertEquals("Patient?active=true", trigger.getQueryCriteria().getCurrent());
		assertTrue(trigger.getQueryCriteria().getRequireBoth());
		
		// Verify can-filter-by
		List<SubscriptionTopic.SubscriptionTopicCanFilterByComponent> canFilterBy = canonicalized.getCanFilterBy();
		assertEquals(1, canFilterBy.size());
		SubscriptionTopic.SubscriptionTopicCanFilterByComponent filterBy = canFilterBy.get(0);
		assertEquals("Test can filter by description", filterBy.getDescription());
		assertEquals("Patient", filterBy.getResource());
		assertEquals("name", filterBy.getFilterParameter());
		
		// Verify notification shape
		List<SubscriptionTopic.SubscriptionTopicNotificationShapeComponent> notificationShapes = canonicalized.getNotificationShape();
		assertEquals(1, notificationShapes.size());
		SubscriptionTopic.SubscriptionTopicNotificationShapeComponent shape = notificationShapes.get(0);
		assertEquals("Patient", shape.getResource());
		assertEquals(1, shape.getInclude().size());
		assertEquals("Patient:organization", shape.getInclude().get(0).getValue());
		assertEquals(1, shape.getRevInclude().size());
		assertEquals("Observation:subject", shape.getRevInclude().get(0).getValue());
		
		// Check if the queryCriteria extension exists in the original Basic resource before canonicalization
		boolean queryCriteriaFound = false;
		for (Extension extension : builtTopic.getExtension()) {
			if (extension.getUrl().contains("resourceTrigger")) {
				for (Extension resourceTriggerExt : extension.getExtension()) {
					if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA.equals(resourceTriggerExt.getUrl())) {
						queryCriteriaFound = true;
						
						// Verify the query criteria extension contents
						boolean previousExtFound = false;
						boolean currentExtFound = false;
						boolean requireBothExtFound = false;
						
						for (Extension queryCriteriaExt : resourceTriggerExt.getExtension()) {
							if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_PREVIOUS.equals(queryCriteriaExt.getUrl())) {
								assertEquals("Patient?active=false", queryCriteriaExt.getValue().primitiveValue());
								previousExtFound = true;
							} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_CURRENT.equals(queryCriteriaExt.getUrl())) {
								assertEquals("Patient?active=true", queryCriteriaExt.getValue().primitiveValue());
								currentExtFound = true;
							} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_QUERY_CRITERIA_REQUIRE_BOTH.equals(queryCriteriaExt.getUrl())) {
								assertEquals("true", queryCriteriaExt.getValue().primitiveValue());
								requireBothExtFound = true;
							}
						}
						
						assertTrue(previousExtFound, "Previous query criteria extension not found");
						assertTrue(currentExtFound, "Current query criteria extension not found");
						assertTrue(requireBothExtFound, "RequireBoth extension not found");
					}
				}
			}
		}
		
		assertTrue(queryCriteriaFound, "QueryCriteria extension not found in the built topic");
	}
}
