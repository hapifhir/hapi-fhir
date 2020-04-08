package ca.uhn.fhir.jpa.subscription.module.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.InMemorySubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.util.CoordCalculatorTest;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.*;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class InMemorySubscriptionMatcherR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InMemorySubscriptionMatcherR4Test.class);

	@Autowired
	SearchParamMatcher mySearchParamMatcher;
	@Autowired
	InMemorySubscriptionMatcher myInMemorySubscriptionMatcher;
	@Autowired
	SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	MatchUrlService myMatchUrlService;

	private void assertMatched(Resource resource, SearchParameterMap params) {
		InMemoryMatchResult result = match(resource, params);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY, mySubscriptionStrategyEvaluator.determineStrategy(getCriteria(resource, params)));
	}

	private void assertNotMatched(Resource resource, SearchParameterMap params) {
		InMemoryMatchResult result = match(resource, params);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertFalse(result.matched());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY, mySubscriptionStrategyEvaluator.determineStrategy(getCriteria(resource, params)));
	}

	private InMemoryMatchResult match(Resource theResource, SearchParameterMap theParams) {
		return match(getCriteria(theResource, theParams), theResource);
	}

	private String getCriteria(Resource theResource, SearchParameterMap theParams) {
		return theResource.getResourceType().name() + theParams.toNormalizedQueryString(myFhirContext);
	}

	private InMemoryMatchResult match(String criteria, Resource theResource) {
		ourLog.info("Criteria: <{}>", criteria);
		return mySearchParamMatcher.match(criteria, theResource, null);
	}

	private void assertUnsupported(Resource resource, SearchParameterMap theParams) {
		InMemoryMatchResult result = match(resource, theParams);
		assertFalse(result.supported());
		assertEquals(SubscriptionMatchingStrategy.DATABASE, mySubscriptionStrategyEvaluator.determineStrategy(getCriteria(resource, theParams)));
	}

	/*
	 The following tests are copied from FhirResourceDaoR4SearchNoFtTest
	  */

	@Test
	public void testChainReferenceUnsupported() {
		Encounter enc1 = new Encounter();
		IIdType pid1 = new IdType("Patient", 1L);
		enc1.getSubject().setReference(pid1.getValue());

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "foo|bar").setChain("identifier"));
		assertUnsupported(enc1, map);

		MedicationAdministration ma = new MedicationAdministration();
		IIdType mid1 = new IdType("Medication", 1L);
		ma.setMedication(new Reference(mid1));

		map = new SearchParameterMap();
		map.add(MedicationAdministration.SP_MEDICATION, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().add(new ReferenceParam("code", "04823543"))));
		assertUnsupported(ma, map);
	}

	@Test
	public void testHasParameterUnsupported() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");

		SearchParameterMap params = new SearchParameterMap();
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|FOO"));
		assertUnsupported(patient, params);
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
		CompositeParam<TokenParam, StringParam> val = new CompositeParam<>(v0, v1);
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
	public void testIdSupported() {
		Observation o1 = new Observation();
		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringParam("testSearchForUnknownAlphanumericId"));
		assertNotMatched(o1, params);
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
	public void testLocationPositionNotSupported() {
		Location loc = new Location();
		double latitude = CoordCalculatorTest.LATITUDE_UHN;
		double longitude = CoordCalculatorTest.LONGITUDE_UHN;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		double bigEnoughDistance = CoordCalculatorTest.DISTANCE_KM_CHIN_TO_UHN * 2;
		SearchParameterMap params = myMatchUrlService.translateMatchUrl(
			"Location?" +
				Location.SP_NEAR + "=" + CoordCalculatorTest.LATITUDE_CHIN + "|"
				+ CoordCalculatorTest.LONGITUDE_CHIN + "|" +
				bigEnoughDistance, myFhirContext.getResourceDefinition("Location"));
		assertUnsupported(loc, params);
	}

	@Test
	public void testSearchLastUpdatedParamUnsupported() {
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

	@Test
	public void testSearchNumberWrongParam() {
		MolecularSequence ir1 = new MolecularSequence();
		ir1.addVariant().setStart(1);

		SearchParameterMap params = new SearchParameterMap().add(MolecularSequence.SP_VARIANT_START, new NumberParam("1"));
		assertMatched(ir1, params);
		params = new SearchParameterMap().add(MolecularSequence.SP_VARIANT_END, new NumberParam("1"));
		assertNotMatched(ir1, params);
	}

	@Test
	public void testSearchPractitionerPhoneAndEmailParam() {
		String methodName = "testSearchPractitionerPhoneAndEmailParam";
		Practitioner patient = new Practitioner();
		patient.addName().setFamily(methodName);
		patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("123");

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_EMAIL, new TokenParam(null, "123"));
		assertNotMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_EMAIL, new TokenParam(null, "abc"));
		assertNotMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_PHONE, new TokenParam(null, "123"));
		assertMatched(patient, params);
	}

	@Test
	public void testSearchQuantityWrongParam() {
		Condition c1 = new Condition();
		c1.setAbatement(new Range().setLow(new SimpleQuantity().setValue(1L)).setHigh(new SimpleQuantity().setValue(1L)));
		SearchParameterMap params = new SearchParameterMap().setLoadSynchronous(true).add(Condition.SP_ABATEMENT_AGE, new QuantityParam("1"));
		assertMatched(c1, params);

		Condition c2 = new Condition();
		c2.setOnset(new Range().setLow(new SimpleQuantity().setValue(1L)).setHigh(new SimpleQuantity().setValue(1L)));

		params = new SearchParameterMap().add(Condition.SP_ONSET_AGE, new QuantityParam("1"));
		assertMatched(c2, params);
	}

	@Test
	public void testSearchResourceLinkWithChainUnsupported() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain01");
		IIdType patientId01 = new IdType("Patient", 1L);
		patient.setId(patientId01);

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain02");
		IIdType patientId02 = new IdType("Patient", 2L);
		patient02.setId(patientId02);

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));

		SearchParameterMap params = new SearchParameterMap().add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChain01"));
		assertUnsupported(obs01, params);
	}

	@Test
	public void testSearchResourceLinkWithTextLogicalId() {
		Patient patient = new Patient();
		String patientName01 = "testSearchResourceLinkWithTextLogicalId01";
		patient.setId(patientName01);
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient.addIdentifier().setSystem("urn:system").setValue(patientName01);
		IIdType patientId01 = new IdType("Patient", patientName01);

		Patient patient02 = new Patient();
		String patientName02 = "testSearchResourceLinkWithTextLogicalId02";
		patient02.setId(patientName02);
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient02.addIdentifier().setSystem("urn:system").setValue(patientName02);
		IIdType patientId02 = new IdType("Patient", patientName02);

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));

		SearchParameterMap params = new SearchParameterMap().add(Observation.SP_SUBJECT, new ReferenceParam(patientName01));
		assertMatched(obs01, params);
		assertNotMatched(obs02, params);

		params = new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId99"));
		assertNotMatched(obs01, params);
		assertNotMatched(obs02, params);

		params = new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("999999999999999"));
		assertNotMatched(obs01, params);
		assertNotMatched(obs02, params);
	}

	@Test
	public void testSearchReferenceInvalid() {
		Patient patient = new Patient();
		patient.setId("Patient/123");
		patient.addName().setFamily("FOO");
		patient.getManagingOrganization().setReference("urn:uuid:13720262-b392-465f-913e-54fb198ff954");

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Fam"));
		try {
			String criteria = params.toNormalizedQueryString(myFhirContext);
			CanonicalSubscription subscription = new CanonicalSubscription();
			subscription.setCriteriaString(criteria);
			subscription.setIdElement(new IdType("Subscription", 123L));
			ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, patient, ResourceModifiedMessage.OperationTypeEnum.CREATE);
			msg.setSubscriptionId("123");
			msg.setId(new IdType("Patient/ABC"));
			InMemoryMatchResult result = myInMemorySubscriptionMatcher.match(subscription, msg);
			fail();
		} catch (AssertionError e){
			assertEquals("Reference at managingOrganization is invalid: urn:uuid:13720262-b392-465f-913e-54fb198ff954", e.getMessage());
		}
	}

	@Test
	public void testReferenceAlias() {
		Observation obs = new Observation();
		obs.setId("Observation/123");
		obs.getSubject().setReference("Patient/123");

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Observation.SP_PATIENT, new ReferenceParam("Patient/123"));
		assertMatched(obs, params);
	}

	@Test
	public void testSearchResourceReferenceOnlyCorrectPath() {
		Organization org = new Organization();
		org.setActive(true);
		IIdType oid1 = new IdType("Organization", 1L);

		Task task = new Task();
		task.setRequester(new Reference(oid1));
		Task task2 = new Task();
		task2.setOwner(new Reference(oid1));

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(Task.SP_REQUESTER, new ReferenceParam(oid1.getValue()));
		assertMatched(task, map);
		assertNotMatched(task2, map);
	}

	@Test
	public void testSearchStringParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester_testSearchStringParam").addGiven("Joe");

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchStringParam"));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("FOO_testSearchStringParam"));
		assertNotMatched(patient, params);

		// Try with different casing

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("tester_testsearchstringparam"));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("TESTER_TESTSEARCHSTRINGPARAM"));
		assertMatched(patient, params);
	}

	@Test
	public void testSearchStringParamReallyLong() {
		String methodName = "testSearchStringParamReallyLong";
		String value = StringUtils.rightPad(methodName, 200, 'a');

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily(value);

		SearchParameterMap params;

		params = new SearchParameterMap();

		String substring = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		params.add(Patient.SP_FAMILY, new StringParam(substring));
		assertMatched(patient, params);
	}

	@Test
	public void testSearchStringParamWithNonNormalized() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
		Patient patient2 = new Patient();
		patient2.addIdentifier().setSystem("urn:system").setValue("002");
		patient2.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_GIVEN, new StringParam("testSearchStringParamWithNonNormalized_hora"));
		assertMatched(patient, params);
		assertMatched(patient2, params);
	}

	@Test
	public void testSearchTokenParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
			.setDisplay("testSearchTokenParamDisplay");

		Patient patient2 = new Patient();
		patient2.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient2.addName().setFamily("Tester").addGiven("testSearchTokenParam2");

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testSearchTokenParam001"));
			assertMatched(patient, map);
			assertNotMatched(patient2, map);
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam(null, "testSearchTokenParam001"));
			assertMatched(patient, map);
			assertNotMatched(patient2, map);
		}

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam("testSearchTokenParamSystem", "testSearchTokenParamCode"));
			assertMatched(patient, map);
			assertNotMatched(patient2, map);
		}

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamCode", true));
			assertUnsupported(patient, map);
		}


		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add("urn:system", "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			assertMatched(patient, map);
			assertMatched(patient2, map);
		}

		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add(null, "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			assertMatched(patient, map);
			assertMatched(patient2, map);
		}
	}

	@Test
	public void testSearchTokenParamNoValue() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
			.setDisplay("testSearchTokenParamDisplay");

		Patient patient2 = new Patient();
		patient2.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient2.addName().setFamily("Tester").addGiven("testSearchTokenParam2");

		Patient patient3 = new Patient();
		patient3.addIdentifier().setSystem("urn:system2").setValue("testSearchTokenParam002");
		patient3.addName().setFamily("Tester").addGiven("testSearchTokenParam2");

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", null));
			// Match 2
			assertMatched(patient, map);
			assertMatched(patient2, map);
			assertNotMatched(patient3, map);
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", ""));
			// Match 2
			assertMatched(patient, map);
			assertMatched(patient2, map);
			assertNotMatched(patient3, map);
		}
	}

	@Test
	public void testSearchTokenWithNotModifierUnsupported() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Patient.SP_GENDER, new TokenParam(null, "male"));
		assertMatched(patient, params);

		params = new SearchParameterMap();
		params.add(Patient.SP_GENDER, new TokenParam(null, "male").setModifier(TokenParamModifier.NOT));
		assertUnsupported(patient, params);
	}

	@Test
	public void testSearchTokenWrongParam() {
		Patient p1 = new Patient();
		p1.setGender(Enumerations.AdministrativeGender.MALE);

		Patient p2 = new Patient();
		p2.addIdentifier().setValue(Enumerations.AdministrativeGender.MALE.toCode());

		{
			SearchParameterMap map = new SearchParameterMap().add(Patient.SP_GENDER, new TokenParam(null, "male"));
			assertMatched(p1, map);
			assertNotMatched(p2, map);
		}
		{
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_IDENTIFIER, new TokenParam(null, "male"));
			assertNotMatched(p1, map);
		}
	}

	@Test
	public void testSearchUriWrongParam() {
		ValueSet v1 = new ValueSet();
		v1.getUrlElement().setValue("http://foo");

		ValueSet v2 = new ValueSet();
		v2.getExpansion().getIdentifierElement().setValue("http://foo");

		{
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://foo"));
			assertMatched(v1, map);
			assertNotMatched(v2, map);
		}
		{
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_EXPANSION, new UriParam("http://foo"));
			assertNotMatched(v1, map);
			assertMatched(v2, map);
		}
	}

	@Test
	public void testSearchValueQuantity() {
		String methodName = "testSearchValueQuantity";

		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("urn:foo").setCode(methodName + "code");
		Quantity q1 = new Quantity().setSystem("urn:bar:" + methodName).setCode(methodName + "units").setValue(10);
		o1.setValue(q1);
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("urn:foo").setCode(methodName + "code");
		Quantity q2 = new Quantity().setSystem("urn:bar:" + methodName).setCode(methodName + "units").setValue(5);
		o2.setValue(q2);

		SearchParameterMap map;
		QuantityParam param;

		map = new SearchParameterMap();
		param = new QuantityParam(null, new BigDecimal("10"), null, null);
		map.add(Observation.SP_VALUE_QUANTITY, param);
		assertMatched(o1, map);
		assertNotMatched(o2, map);

		map = new SearchParameterMap();
		param = new QuantityParam(null, new BigDecimal("10"), null, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		assertMatched(o1, map);
		assertNotMatched(o2, map);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(null, new BigDecimal("10"), "urn:bar:" + methodName, null);
		map.add(Observation.SP_VALUE_QUANTITY, param);
		assertMatched(o1, map);
		assertNotMatched(o2, map);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(null, new BigDecimal("10"), "urn:bar:" + methodName, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		assertMatched(o1, map);
		assertNotMatched(o2, map);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(null, new BigDecimal("1000"), "urn:bar:" + methodName, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		assertNotMatched(o1, map);
		assertNotMatched(o2, map);
	}

	@Test
	public void testSearchWithContainsUnsupported() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("ABCDEFGHIJK");

		SearchParameterMap map;

		// Contains = true
		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("FGHIJK").setContains(true));
		assertUnsupported(pt1, map);
	}

	@Test
	public void testSearchWithDate() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");

		Patient patient2 = new Patient();
		patient2.addIdentifier().setSystem("urn:system").setValue("002");
		patient2.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
		patient2.setBirthDateElement(new DateType("2011-01-01"));
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2011-01-01"));
			assertNotMatched(patient, params);
			assertMatched(patient2, params);
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2011-01-03"));
			assertNotMatched(patient, params);
			assertNotMatched(patient2, params);
		}
	}

	@Test
	public void testSearchWithIncludesIgnored() {
		String methodName = "testSearchWithIncludes";
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");

		{
			// No includes
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			assertMatched(patient, params);
		}
		{
			// Named include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION.asNonRecursive());
			assertMatched(patient, params);
		}
		{
			// Named include with parent non-recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asNonRecursive());
			assertMatched(patient, params);
		}
		{
			// Named include with parent recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
			assertMatched(patient, params);
		}
		{
			// * include non recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asNonRecursive());
			assertMatched(patient, params);
		}
		{
			// * include recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
			assertMatched(patient, params);
		}
		{
			// Irrelevant include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Encounter.INCLUDE_EPISODE_OF_CARE);
			assertMatched(patient, params);
		}
	}

	@Test
	public void testSearchWithSecurityAndProfileParamsUnsupported() {
		String methodName = "testSearchWithSecurityAndProfileParams";

		Organization org = new Organization();
		org.getNameElement().setValue("FOO");
		org.getMeta().addSecurity("urn:taglist", methodName + "1a", null);
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_security", new TokenParam("urn:taglist", methodName + "1a"));
			assertUnsupported(org, params);
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_profile", new UriParam("http://" + methodName));
			assertUnsupported(org, params);
		}
	}

	@Test
	public void testSearchWithTagParameterUnsupported() {
		String methodName = "testSearchWithTagParameter";

		Organization org = new Organization();
		org.getNameElement().setValue("FOO");
		org.getMeta().addTag("urn:taglist", methodName + "1a", null);
		org.getMeta().addTag("urn:taglist", methodName + "1b", null);

		{
			// One tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "1a"));
			assertUnsupported(org, params);
		}
	}

	@Test
	public void testSearchWithVeryLongUrlLonger() {
		Patient p = new Patient();
		p.addName().setFamily("A1");


		SearchParameterMap map = new SearchParameterMap();
		StringOrListParam or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		for (int i = 0; i < 50; i++) {
			or.addOr(new StringParam(StringUtils.leftPad("", 200, (char) ('A' + i))));
		}
		map.add(Patient.SP_NAME, or);
		assertMatched(p, map);

		map = new SearchParameterMap();
		or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam("A1"));
		for (int i = 0; i < 50; i++) {
			or.addOr(new StringParam(StringUtils.leftPad("", 200, (char) ('A' + i))));
		}
		map.add(Patient.SP_NAME, or);
		assertMatched(p, map);
	}

	@Test
	public void testDateSearchParametersShouldBeTimezoneIndependent() {

		List<Observation> nlist = new ArrayList<>();
		nlist.add(createObservationWithEffective("NO1", "2011-01-02T23:00:00-11:30"));
		nlist.add(createObservationWithEffective("NO2", "2011-01-03T00:00:00+01:00"));

		List<Observation> ylist = new ArrayList<>();
		ylist.add(createObservationWithEffective("YES01", "2011-01-02T00:00:00-11:30"));
		ylist.add(createObservationWithEffective("YES02", "2011-01-02T00:00:00-10:00"));
		ylist.add(createObservationWithEffective("YES03", "2011-01-02T00:00:00-09:00"));
		ylist.add(createObservationWithEffective("YES04", "2011-01-02T00:00:00-08:00"));
		ylist.add(createObservationWithEffective("YES05", "2011-01-02T00:00:00-07:00"));
		ylist.add(createObservationWithEffective("YES06", "2011-01-02T00:00:00-06:00"));
		ylist.add(createObservationWithEffective("YES07", "2011-01-02T00:00:00-05:00"));
		ylist.add(createObservationWithEffective("YES08", "2011-01-02T00:00:00-04:00"));
		ylist.add(createObservationWithEffective("YES09", "2011-01-02T00:00:00-03:00"));
		ylist.add(createObservationWithEffective("YES10", "2011-01-02T00:00:00-02:00"));
		ylist.add(createObservationWithEffective("YES11", "2011-01-02T00:00:00-01:00"));
		ylist.add(createObservationWithEffective("YES12", "2011-01-02T00:00:00Z"));
		ylist.add(createObservationWithEffective("YES13", "2011-01-02T00:00:00+01:00"));
		ylist.add(createObservationWithEffective("YES14", "2011-01-02T00:00:00+02:00"));
		ylist.add(createObservationWithEffective("YES15", "2011-01-02T00:00:00+03:00"));
		ylist.add(createObservationWithEffective("YES16", "2011-01-02T00:00:00+04:00"));
		ylist.add(createObservationWithEffective("YES17", "2011-01-02T00:00:00+05:00"));
		ylist.add(createObservationWithEffective("YES18", "2011-01-02T00:00:00+06:00"));
		ylist.add(createObservationWithEffective("YES19", "2011-01-02T00:00:00+07:00"));
		ylist.add(createObservationWithEffective("YES20", "2011-01-02T00:00:00+08:00"));
		ylist.add(createObservationWithEffective("YES21", "2011-01-02T00:00:00+09:00"));
		ylist.add(createObservationWithEffective("YES22", "2011-01-02T00:00:00+10:00"));
		ylist.add(createObservationWithEffective("YES23", "2011-01-02T00:00:00+11:00"));


		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_DATE, new DateParam("2011-01-02"));

		for (Observation obs : nlist) {
			assertNotMatched(obs, map);
		}
		for (Observation obs : ylist) {
			ourLog.info("Obs {} has time {}", obs.getId(), obs.getEffectiveDateTimeType().getValue().toString());
			assertMatched(obs, map);
		}
	}

	private Observation createObservationWithEffective(String theId, String theEffective) {
		Observation obs = new Observation();
		obs.setId(theId);
		obs.setEffective(new DateTimeType(theEffective));
		return obs;
	}

	@Test
	public void testSearchWithVeryLongUrlShorter() {
		Patient p = new Patient();
		p.addName().setFamily("A1");

		SearchParameterMap map = new SearchParameterMap();
		StringOrListParam or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'A')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'B')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'C')));
		map.add(Patient.SP_NAME, or);

		assertMatched(p, map);

		map = new SearchParameterMap();
		or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'A')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'B')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'C')));
		map.add(Patient.SP_NAME, or);
		assertMatched(p, map);
	}
}
