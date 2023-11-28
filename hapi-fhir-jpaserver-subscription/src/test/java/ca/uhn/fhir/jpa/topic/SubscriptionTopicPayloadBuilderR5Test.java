package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicPayloadBuilderR5Test {
	private static final String TEST_TOPIC_URL = "test-builder-topic-url";
	FhirContext ourFhirContext = FhirContext.forR5Cached();
	@Test
	public void testBuildPayloadDelete() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");

		// run
		Bundle payload = (Bundle)svc.buildPayload(List.of(encounter), subscription, TEST_TOPIC_URL, RestOperationTypeEnum.DELETE);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(1, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.DELETE, payload.getEntry().get(1).getRequest().getMethod());
	}

	@Test
	public void testBuildPayloadUpdate() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");

		// run
		Bundle payload = (Bundle)svc.buildPayload(List.of(encounter), subscription, TEST_TOPIC_URL, RestOperationTypeEnum.UPDATE);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(2, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());
		assertEquals("Encounter", resources.get(1).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.PUT, payload.getEntry().get(1).getRequest().getMethod());
	}

	@Test
	public void testBuildPayloadCreate() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");

		// run
		Bundle payload = (Bundle)svc.buildPayload(List.of(encounter), subscription, TEST_TOPIC_URL, RestOperationTypeEnum.CREATE);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(2, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());
		assertEquals("Encounter", resources.get(1).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.POST, payload.getEntry().get(1).getRequest().getMethod());
	}

	@ParameterizedTest
	@CsvSource({
		"create",
		"update",
		"delete"
	})
	public void testBuildPayloadIdOnly(String theRestOperationType) {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		CanonicalSubscription sub = new CanonicalSubscription();
		sub.setTopicSubscription(true);
		sub.getTopicSubscription().setContent(Subscription.SubscriptionPayloadContent.IDONLY);
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");

		// run
		Bundle payload = (Bundle)svc.buildPayload(List.of(encounter), subscription, TEST_TOPIC_URL, RestOperationTypeEnum.forCode(theRestOperationType));

		// verify SubscriptionStatus entry
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(1, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());

		// verify notificationEvent.focus
		assertEquals(1, ((SubscriptionStatus)resources.get(0)).getNotificationEvent().size());
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent= ((SubscriptionStatus)resources.get(0)).getNotificationEventFirstRep();
		assertTrue(notificationEvent.hasFocus());
		assertEquals(encounter.getId(), notificationEvent.getFocus().getReference());

		// verify Encounter entry
		assertEquals(2,payload.getEntry().size());
		assertNull(payload.getEntry().get(1).getResource());
		assertEquals("Encounter/1",payload.getEntry().get(1).getFullUrl());
		assertNotNull(payload.getEntry().get(1).getRequest());
		assertEquals(Bundle.HTTPVerb.POST, payload.getEntry().get(1).getRequest().getMethod());
		assertEquals("Encounter", payload.getEntry().get(1).getRequest().getUrl());
	}
}
