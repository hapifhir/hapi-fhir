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
		assertThat(payload.getEntry().size()).isEqualTo(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertThat(resources.size()).isEqualTo(1);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertThat(encounterEntry.getResource()).isNull();
		assertThat(encounterEntry.getFullUrl()).isNull();
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
		assertThat(payload.getEntry().size()).isEqualTo(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertThat(resources.size()).isEqualTo(2);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertThat(resources.get(1).getResourceType().name()).isEqualTo("Encounter");
		assertThat(resources.get(1)).isEqualTo(myEncounter);
		assertThat(encounterEntry.getFullUrl()).isEqualTo(theFullUrl);
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
		assertThat(payload.getEntry().size()).isEqualTo(2);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertThat(resources.size()).isEqualTo(1);

        // verify SubscriptionStatus.notificationEvent.focus
        verifySubscriptionStatusNotificationEvent(resources.get(0));

        // verify Encounter entry
        Bundle.BundleEntryComponent encounterEntry = payload.getEntry().get(1);
		assertThat(encounterEntry.getResource()).isNull();
		assertThat(encounterEntry.getFullUrl()).isEqualTo(theFullUrl);
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
		assertThat(payload.getEntry().size()).isEqualTo(1);
        List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertThat(resources.size()).isEqualTo(1);

		// verify SubscriptionStatus.notificationEvent.focus
		assertThat(resources.get(0).getResourceType().name()).isEqualTo("SubscriptionStatus");
		assertThat(((SubscriptionStatus) resources.get(0)).getNotificationEvent().size()).isEqualTo(1);
        SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent =
                ((SubscriptionStatus) resources.get(0)).getNotificationEventFirstRep();
		assertThat(notificationEvent.hasFocus()).isFalse();
    }

    private void verifyRequestParameters(Bundle.BundleEntryComponent theEncounterEntry,
                                         String theHttpMethod, String theRequestUrl) {
		assertThat(theEncounterEntry.getRequest()).isNotNull();
		assertThat(theEncounterEntry.getRequest().getMethod().name()).isEqualTo(theHttpMethod);
		assertThat(theEncounterEntry.getRequest().getUrl()).isEqualTo(theRequestUrl);
    }

    private void verifySubscriptionStatusNotificationEvent(Resource theResource) {
		assertThat(theResource.getResourceType().name()).isEqualTo("SubscriptionStatus");
		assertThat(((SubscriptionStatus) theResource).getNotificationEvent().size()).isEqualTo(1);
        SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent =
                ((SubscriptionStatus) theResource).getNotificationEventFirstRep();
		assertThat(notificationEvent.hasFocus()).isTrue();
		assertThat(notificationEvent.getFocus().getReference()).isEqualTo(myEncounter.getId());
    }
}
