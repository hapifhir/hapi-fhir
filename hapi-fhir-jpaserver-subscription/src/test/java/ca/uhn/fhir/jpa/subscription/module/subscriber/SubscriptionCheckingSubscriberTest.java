package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests copied from jpa.subscription.resthook.RestHookTestDstu3Test
 */
public class SubscriptionCheckingSubscriberTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionCheckingSubscriberTest.class);

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(1);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionWithoutPayload() throws Exception {
		String payload = "";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code;
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		mySubscriptionAfterDelivery.setExpectedCount(1);
		ourObservationListener.setExpectedCount(0);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.clear();
		mySubscriptionAfterDelivery.awaitExpected();

		assertEquals(0, ourContentTypes.size());
	}

	@Test
	public void testReferenceWithDisplayOnly() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = makeActiveSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(subscription1, null, false);
		Subscription subscription2 = makeActiveSubscription(criteria2, payload, ourListenerServerBase);
		sendSubscription(subscription2, null, false);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(1);
		Observation observation = new Observation();
		IdType id = new IdType("Observation", nextId());
		observation.setId(id);

		// Reference has display only!
		observation.getSubject().setDisplay("Mr Jones");

		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem("SNOMED-CT");

		observation.setStatus(Observation.ObservationStatus.FINAL);

		sendResource(observation);
		ourObservationListener.awaitExpected();

		assertEquals(1, ourContentTypes.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

}
