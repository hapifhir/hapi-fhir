package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

// These tests are copied from FhirResourceDaoR4SearchNoFtTest
public class SubscriptionMatcherInMemoryTest extends BaseJpaR4Test {
	@Autowired
	SubscriptionMatcherInMemory mySubscriptionMatcherInMemory;

	private SubscriptionMatchResult match(IBaseResource resource, SearchParameterMap map) {
		String criteria = map.toNormalizedQueryString(myFhirCtx);
		return mySubscriptionMatcherInMemory.match(criteria, resource);
	}

	@Test
	public void testReferenceUnsupported() {
		Encounter enc1 = new Encounter();
		enc1.getSubject().setReference("1");

		SearchParameterMap map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "foo|bar").setChain("identifier"));
		assertFalse(match(enc1, map).supported());
	}

	@Test
	public void testSearchCode() {
		Subscription subs = new Subscription();
		subs.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(Subscription.SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");

		SearchParameterMap map = new SearchParameterMap();
		assertTrue(match(subs, map).matched());

		map = new SearchParameterMap();
		map.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		map.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode()));
		assertTrue(match(subs, map).matched());

		map = new SearchParameterMap();
		map.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		map.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode() + "2"));
		assertFalse(match(subs, map).matched());
//		// Wrong param
		map = new SearchParameterMap();
		map.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		assertFalse(match(subs, map).matched());
	}

	@Test
	public void testSearchCompositeUnsupported() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o1.setValue(new StringType("testSearchCompositeParamS01"));

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS01");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_STRING, val);
			assertFalse(match(o1, map).supported());
		}
	}

	@Test
	public void testComponentQuantityWithPrefixUnsupported() {
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		{
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 150, "http://bar", "code1");
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
			assertFalse(match(o1, map).supported());
		}
	}


	@Test
	public void testComponentQuantityEquals() {
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(150));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(150));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		{
			QuantityParam v1 = new QuantityParam(null, 150, "http://bar", "code1");
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
			assertTrue(match(o1, map).matched());
		}
	}
}
