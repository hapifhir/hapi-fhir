package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import ca.uhn.fhir.rest.api.Constants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Tests copied from jpa.subscription.resthook.RestHookTestDstu3Test
 */
public class SubscriptionMatchingSubscriberTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriberTest.class);

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		sendSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(criteria2, payload, ourListenerServerBase);

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

		sendSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(criteria2, payload, ourListenerServerBase);

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

		sendSubscription(criteria1, payload, ourListenerServerBase);
		sendSubscription(criteria2, payload, ourListenerServerBase);

		assertEquals(2, mySubscriptionRegistry.size());

		ourObservationListener.setExpectedCount(0);
		sendObservation(code, "SNOMED-CT");
		ourObservationListener.clear();

		assertEquals(0, ourContentTypes.size());
	}
}
