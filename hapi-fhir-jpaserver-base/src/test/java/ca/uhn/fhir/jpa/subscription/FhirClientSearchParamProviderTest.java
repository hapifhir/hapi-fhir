package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.standalone.FhirClientSearchParamProvider;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;


public class FhirClientSearchParamProviderTest extends BaseSubscriptionsR4Test {
	@Autowired
	ISearchParamProvider origSearchParamProvider;

	@Before
	public void useFhirClientSearchParamProvider() {
		mySearchParamRegistry.setSearchParamProviderForUnitTest(new FhirClientSearchParamProvider(ourClient));
	}

	@After
	public void revert() {
		mySearchParamRegistry.setSearchParamProviderForUnitTest(origSearchParamProvider);
	}

	@Test
	public void testCustomSearchParam() throws Exception {
		String criteria = "Observation?accessType=Catheter,PD%20Catheter";

		SearchParameter sp = new SearchParameter();
		sp.addBase("Observation");
		sp.setCode("accessType");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Observation.extension('Observation#accessType')");
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();
		createSubscription(criteria, "application/json");
		waitForActivatedSubscriptionCount(1);

		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("Catheter"));
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(1, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("PD Catheter"));
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}
		{
			Observation observation = new Observation();
			observation.addExtension().setUrl("Observation#accessType").setValue(new Coding().setCode("XXX"));
			MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();
			assertEquals(true, methodOutcome.getCreated());
			waitForQueueToDrain();
			waitForSize(2, ourUpdatedObservations);
		}
	}
}
