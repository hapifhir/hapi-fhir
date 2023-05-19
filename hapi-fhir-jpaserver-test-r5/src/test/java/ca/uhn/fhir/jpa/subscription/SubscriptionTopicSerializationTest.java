package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r5.model.Subscription;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionTopicSerializationTest {
	FhirContext ourFhirContext = FhirContext.forR5Cached();

	@Test
	void testSubscriptionDerialization() {
		@Language("json")
		String input = """
			{
			  "resourceType": "Subscription",
			  "id": "2",
			  "status": "active",
			  "topic": "http://example.com/topic/test",
			  "reason": "Monitor new neonatal function (note, age will be determined by the monitor)",
			  "filterBy": [ {
			    "resourceType": "Encounter",
			    "filterParameter": "participation-type",
			    "comparator": "eq",
			    "value": "PRPF"
			  } ],
			  "channelType": {
			    "system": "http://terminology.hl7.org/CodeSystem/subscription-channel-type",
			    "code": "rest-hook"
			  },
			  "endpoint": "http://localhost:57333/fhir/context",
			  "contentType": "application/fhir+json"
			}
			""";

		Subscription subscription = ourFhirContext.newJsonParser().parseResource(Subscription.class, input);
		assertEquals("Encounter", subscription.getFilterByFirstRep().getResourceType());

		// Also test the other direction
		String serialized = ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(subscription);
		assertEquals(input.trim(), serialized);
	}
}
