package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriptionLoaderFhirClientTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
    
	@Test
	public void testSubscriptionLoaderFhirClient() throws InterruptedException {
		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(makeActiveSubscription(criteria1, payload, ourListenerServerBase));
		subs.add(makeActiveSubscription(criteria2, payload, ourListenerServerBase));

        mySubscriptionActivatedPost.setExpectedCount(2);
		initSubscriptionLoader(subs, "uuid");
        mySubscriptionActivatedPost.awaitExpected();

        ourObservationListener.setExpectedCount(1);
		sendObservation(myCode, "SNOMED-CT");
        ourObservationListener.awaitExpected();

		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testSubscriptionLoaderFhirClientSubscriptionNotActive() throws InterruptedException {
		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(makeActiveSubscription(criteria1, payload, ourListenerServerBase).setStatus(Subscription.SubscriptionStatus.REQUESTED));
		subs.add(makeActiveSubscription(criteria2, payload, ourListenerServerBase).setStatus(Subscription.SubscriptionStatus.REQUESTED));

		initSubscriptionLoader(subs, "uuid");

		sendObservation(myCode, "SNOMED-CT");

		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourUpdatedObservations);
	}
}
