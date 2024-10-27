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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionTopicPayloadBuilderR5Test {
    private static final String TEST_TOPIC_URL = "test-builder-topic-url";
    FhirContext ourFhirContext = FhirContext.forR5Cached();
    private SubscriptionTopicPayloadBuilder myStPayloadBuilder;
    private Encounter myEncounter;
    private CanonicalSubscription myCanonicalSubscription;
    private ActiveSubscription myActiveSubscription;

    @BeforeEach
    void before() {
        myStPayloadBuilder = new SubscriptionTopicPayloadBuilder(ourFhirContext);
        myEncounter = new Encounter();
        myEncounter.setId("Encounter/1");
        myCanonicalSubscription = new CanonicalSubscription();
        myCanonicalSubscription.setTopicSubscription(true);
        myActiveSubscription = new ActiveSubscription(myCanonicalSubscription, "test");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "full-resource",
            "" // payload content not provided
    })
    public void testBuildPayload_deleteWithFullResourceContent_returnsCorrectPayload(String thePayloadContent) {
        // setup
        Subscription.SubscriptionPayloadContent payloadContent =
                Subscription.SubscriptionPayloadContent.fromCode(thePayloadContent);
        myCanonicalSubscription.getTopicSubscription().setContent(payloadContent);

        // run
        Bundle payload = (Bundle) myStPayloadBuilder.buildPayload(List.of(myEncounter), myActiveSubscription, TEST_TOPIC_URL, RestOperationTypeEnum.DELETE);

			// verify Bundle size
			assertThat(payload.getEntry()).hasSize(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
			assertThat(resources).hasSize(1);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertNull(encounterEntry.getResource());
		assertNull(encounterEntry.getFullUrl());
        verifyRequestParameters(encounterEntry, Bundle.HTTPVerb.DELETE.name(), "Encounter/1");
    }

    @ParameterizedTest
    @CsvSource({
            "create, POST  , full-resource, Encounter/1, Encounter",
            "update, PUT   , full-resource, Encounter/1, Encounter/1",
            "create, POST  , 			  , Encounter/1, Encounter",
            "update, PUT   ,              , Encounter/1, Encounter/1",
    })
    public void testBuildPayload_createUpdateWithFullResourceContent_returnsCorrectPayload(String theRestOperationType,
                                                                                           String theHttpMethod,
                                                                                           String thePayloadContent,
                                                                                           String theFullUrl,
                                                                                           String theRequestUrl) {
        // setup
        Subscription.SubscriptionPayloadContent payloadContent =
                Subscription.SubscriptionPayloadContent.fromCode(thePayloadContent);

        myCanonicalSubscription.getTopicSubscription().setContent(payloadContent);
        RestOperationTypeEnum restOperationType = RestOperationTypeEnum.forCode(theRestOperationType);

        // run
        Bundle payload = (Bundle) myStPayloadBuilder.buildPayload(List.of(myEncounter), myActiveSubscription, TEST_TOPIC_URL, restOperationType);

			// verify Bundle size
			assertThat(payload.getEntry()).hasSize(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
			assertThat(resources).hasSize(2);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertEquals("Encounter", resources.get(1).getResourceType().name());
		assertEquals(myEncounter, resources.get(1));
		assertEquals(theFullUrl, encounterEntry.getFullUrl());
        verifyRequestParameters(encounterEntry, theHttpMethod, theRequestUrl);
    }

    @ParameterizedTest
    @CsvSource({
            "create, POST  , Encounter/1, Encounter",
            "update, PUT   , Encounter/1, Encounter/1",
            "delete, DELETE, 			, Encounter/1"
    })
    public void testBuildPayload_withIdOnlyContent_returnsCorrectPayload(String theRestOperationType,
                                                                         String theHttpMethod, String theFullUrl,
                                                                         String theRequestUrl) {
        // setup
        myCanonicalSubscription.getTopicSubscription().setContent(Subscription.SubscriptionPayloadContent.IDONLY);
        RestOperationTypeEnum restOperationType = RestOperationTypeEnum.forCode(theRestOperationType);

        // run
        Bundle payload = (Bundle) myStPayloadBuilder.buildPayload(List.of(myEncounter), myActiveSubscription, TEST_TOPIC_URL, restOperationType);

			// verify Bundle size
			assertThat(payload.getEntry()).hasSize(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
			assertThat(resources).hasSize(1);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertNull(encounterEntry.getResource());
		assertEquals(theFullUrl, encounterEntry.getFullUrl());
        verifyRequestParameters(encounterEntry, theHttpMethod, theRequestUrl);
    }

    @ParameterizedTest
    @CsvSource({
            "create",
            "update",
            "delete"
    })
    public void testBuildPayload_withEmptyContent_returnsCorrectPayload(String theRestOperationType) {
        // setup
        myCanonicalSubscription.getTopicSubscription().setContent(Subscription.SubscriptionPayloadContent.EMPTY);
        RestOperationTypeEnum restOperationType = RestOperationTypeEnum.forCode(theRestOperationType);

        // run
        Bundle payload = (Bundle) myStPayloadBuilder.buildPayload(List.of(myEncounter), myActiveSubscription, TEST_TOPIC_URL, restOperationType);

			// verify Bundle size
			assertThat(payload.getEntry()).hasSize(1);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
			assertThat(resources).hasSize(1);

		// verify SubscriptionStatus.notificationEvent.focus
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());
			assertThat(((SubscriptionStatus) resources.get(0)).getNotificationEvent()).hasSize(1);
        SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent =
                ((SubscriptionStatus) resources.get(0)).getNotificationEventFirstRep();
		assertFalse(notificationEvent.hasFocus());
    }

    private void verifyRequestParameters(Bundle.BundleEntryComponent theEncounterEntry,
                                         String theHttpMethod, String theRequestUrl) {
		assertNotNull(theEncounterEntry.getRequest());
		assertEquals(theHttpMethod, theEncounterEntry.getRequest().getMethod().name());
		assertEquals(theRequestUrl, theEncounterEntry.getRequest().getUrl());
    }

    private void verifySubscriptionStatusNotificationEvent(Resource theResource) {
		assertEquals("SubscriptionStatus", theResource.getResourceType().name());
			assertThat(((SubscriptionStatus) theResource).getNotificationEvent()).hasSize(1);
        SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent =
                ((SubscriptionStatus) theResource).getNotificationEventFirstRep();
		assertTrue(notificationEvent.hasFocus());
		assertEquals(myEncounter.getId(), notificationEvent.getFocus().getReference());
    }
}
