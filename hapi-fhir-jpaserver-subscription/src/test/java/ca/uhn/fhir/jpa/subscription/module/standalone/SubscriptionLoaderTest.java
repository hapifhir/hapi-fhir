package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriptionLoaderTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final int MOCK_FHIR_CLIENT_FAILURES = 5;
	@Autowired
	private MockFhirClientSubscriptionProvider myMockFhirClientSubscriptionProvider;
	@Autowired
	private SubscriptionLoader mySubscriptionLoader;

	@Before
	public void setFailCount() {
		myMockFhirClientSubscriptionProvider.setFailCount(MOCK_FHIR_CLIENT_FAILURES);
	}

	@After
	public void restoreFailCount() {
		myMockFhirClientSubscriptionProvider.setFailCount(0);
	}

	@Before
	public void zeroRetryDelay() {
		mySubscriptionLoader.setSecondsBetweenRetriesForTesting(0);
	}

	@After
	public void restoreRetryDelay() {
		mySubscriptionLoader.setSecondsBetweenRetriesForTesting(BaseSearchParamRegistry.INITIAL_SECONDS_BETWEEN_RETRIES);
	}

	@Test
	public void testSubscriptionLoaderFhirClientDown() throws Exception {
		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(returnedActiveSubscription(criteria1, payload, ourListenerServerBase));
		subs.add(returnedActiveSubscription(criteria2, payload, ourListenerServerBase));

		IBundleProvider bundle = new SimpleBundleProvider(new ArrayList<>(subs), "uuid");
		initSubscriptionLoader(bundle);
		assertEquals(0, myMockFhirClientSubscriptionProvider.getFailCount());
	}
}
