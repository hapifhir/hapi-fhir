package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

// These tests are copied from FhirResourceDaoR4SearchNoFtTest
// TODO KHS Load an app config that doesn't load the database
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class SubscriptionMatcherInMemoryTest {
	@Autowired
	SubscriptionMatcherInMemory mySubscriptionMatcherInMemory;
	@Autowired
	FhirContext myContext;

	private SubscriptionMatchResult match(IBaseResource resource, SearchParameterMap params) {
		String criteria = params.toNormalizedQueryString(myContext);
		return mySubscriptionMatcherInMemory.match(criteria, resource);
	}

	private void assertUnsupported(IBaseResource resource, SearchParameterMap params) {
		assertFalse(match(resource, params).supported());
	}

	private void assertMatched(IBaseResource resource, SearchParameterMap params) {
		SubscriptionMatchResult result = match(resource, params);
		assertTrue(result.supported());
		assertTrue(result.matched());
	}

	private void assertNotMatched(IBaseResource resource, SearchParameterMap params) {
		SubscriptionMatchResult result = match(resource, params);
		assertTrue(result.supported());
		assertFalse(result.matched());
	}

	@Test
	public void testReferenceUnsupported() {
		Encounter enc1 = new Encounter();
		enc1.getSubject().setReference("1");

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "foo|bar").setChain("identifier"));
		assertUnsupported(enc1, params);
	}

	@Test
	public void testSearchCode() {
		Subscription subs = new Subscription();
		subs.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(Subscription.SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");

		SearchParameterMap params = new SearchParameterMap();
		assertMatched(subs, params);

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode()));
		assertMatched(subs, params);

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode() + "2"));
		assertNotMatched(subs, params);
//		// Wrong param
		params = new SearchParameterMap();
		params.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionChannelType.WEBSOCKET.toCode()));
		assertNotMatched(subs, params);
	}

	@Test
	public void testSearchCompositeUnsupported() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o1.setValue(new StringType("testSearchCompositeParamS01"));

		TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
		StringParam v1 = new StringParam("testSearchCompositeParamS01");
		CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
		SearchParameterMap params = new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_STRING, val);
		assertUnsupported(o1, params);
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

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;
		QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 150, "http://bar", "code1");
		SearchParameterMap params = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
		assertUnsupported(o1, params);
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

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		QuantityParam v1 = new QuantityParam(null, 150, "http://bar", "code1");
		SearchParameterMap params = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
		assertMatched(o1, params);
	}

	@Test
	public void testIdNotSupported() {
		Observation o1 = new Observation();
		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringParam("testSearchForUnknownAlphanumericId"));
		assertUnsupported(o1, params);
	}

	@Test
	public void testLanguageNotSupported() {
		Patient patient = new Patient();
		patient.getLanguageElement().setValue("en_CA");
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("testSearchLanguageParam").addGiven("Joe");
		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(IAnyResource.SP_RES_LANGUAGE, new StringParam("en_CA"));
		assertUnsupported(patient, params);
	}

	@Test
	public void testSearchLastUpdatedParamUnsupported() throws InterruptedException {
		String methodName = "testSearchLastUpdatedParam";
		DateTimeType today = new DateTimeType(new Date(), TemporalPrecisionEnum.DAY);
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily(methodName).addGiven("Joe");
		SearchParameterMap params = new SearchParameterMap();
		params.setLastUpdated(new DateRangeParam(today, null));
		assertUnsupported(patient, params);
	}

	@Test
	public void testSearchNameParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Fam"));
		assertMatched(patient, params);

		// Given name shouldn't return for family param
		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Giv"));
		assertNotMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_NAME, new StringParam("testSearchNameParam01Fam"));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_NAME, new StringParam("testSearchNameParam01Giv"));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Foo"));
		assertNotMatched(patient, params);
	}

	@Test
	public void testSearchNumberParam() {
		RiskAssessment risk = new RiskAssessment();
		risk.addIdentifier().setSystem("foo").setValue("testSearchNumberParam01");
		risk.addPrediction().setProbability(new DecimalType(2));

		SearchParameterMap params;
		params = new SearchParameterMap().add(RiskAssessment.SP_PROBABILITY, new NumberParam(">1"));
		assertUnsupported(risk, params);

		params = new SearchParameterMap().add(RiskAssessment.SP_PROBABILITY, new NumberParam("<1"));
		assertUnsupported(risk, params);

		params = new SearchParameterMap().add(RiskAssessment.SP_PROBABILITY, new NumberParam("2"));
		assertMatched(risk, params);

		params = new SearchParameterMap().add(RiskAssessment.SP_PROBABILITY, new NumberParam("3"));
		assertNotMatched(risk, params);
	}
}
