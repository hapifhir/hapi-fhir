package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SearchParamLoaderTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final int MOCK_FHIR_CLIENT_FAILURES = 5;
	@Autowired
	private MockFhirClientSearchParamProvider myMockFhirClientSearchParamProvider;
	@Autowired
	private BaseSearchParamRegistry mySearchParamRegistry;

	@Before
	public void setFailCount() {
		myMockFhirClientSearchParamProvider.setFailCount(MOCK_FHIR_CLIENT_FAILURES);
	}

	@After
	public void restoreFailCount() {
		myMockFhirClientSearchParamProvider.setFailCount(0);
	}

	@Before
	public void zeroRetryDelay() {
		mySearchParamRegistry.setSecondsBetweenRetriesForTesting(0);
	}

	@After
	public void restoreRetryDelay() {
		mySearchParamRegistry.setSecondsBetweenRetriesForTesting(mySearchParamRegistry.INITIAL_SECONDS_BETWEEN_RETRIES);
	}

	@Test
	public void testSubscriptionLoaderFhirClientDown() throws Exception {
		String criteria = "BodySite?accessType=Catheter,PD%20Catheter";

		SearchParameter sp = new SearchParameter();
		sp.addBase("BodySite");
		sp.setCode("accessType");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("BodySite.extension('BodySite#accessType')");
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);

		IBundleProvider bundle = new SimpleBundleProvider(Arrays.asList(sp), "uuid");
		initSearchParamRegistry(bundle);
		assertEquals(0, myMockFhirClientSearchParamProvider.getFailCount());
	}
}
