package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

// These tests are copied from FhirResourceDaoR4SearchNoFtTest
public class SubscriptionMatcherInMemoryTest extends BaseJpaR4Test {
	@Autowired
	SubscriptionMatcherInMemory mySubscriptionMatcherInMemory;

	private SubscriptionMatchResult match(Subscription subs, SearchParameterMap params) {
		String criteria = params.toNormalizedQueryString(myFhirCtx);
		return mySubscriptionMatcherInMemory.match(criteria, subs);
	}

	@Test
	public void testSearchCode() {
		Subscription subs = new Subscription();
		subs.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(Subscription.SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");

		SearchParameterMap params = new SearchParameterMap();
		assertTrue(match(subs, params).matched());

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode()));
		assertTrue(match(subs, params).matched());

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode() + "2"));
		assertFalse(match(subs, params).matched());
//		// Wrong param
		params = new SearchParameterMap();
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		assertFalse(match(subs, params).matched());
	}
	@Test
	public void testReferenceUnsupported() {
		Encounter enc1 = new Encounter();
		enc1.getSubject().setReference("1");

		SearchParameterMap map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "foo|bar").setChain("identifier"));
		String criteria = map.toNormalizedQueryString(myFhirCtx);
		SubscriptionMatchResult result = mySubscriptionMatcherInMemory.match(criteria, enc1);
		assertThat(result.supported(), is(false));
	}

}
