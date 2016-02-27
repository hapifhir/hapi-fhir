package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DiagnosticOrder;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Immunization;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationOrder;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.dstu3.model.Substance;
import org.hl7.fhir.dstu3.model.TemporalPrecisionEnum;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

@SuppressWarnings("unchecked")
public class FhirResourceDaoDstu3SearchNoFtTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3SearchNoFtTest.class);
	
	@Test
	public void testCodeSearch() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = mySubscriptionDao.create(subs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		SearchParameterMap map = new SearchParameterMap();
		map.add(Subscription.SP_TYPE, new TokenParam(null, SubscriptionChannelType.WEBSOCKET.toCode()));
		map.add(Subscription.SP_STATUS, new TokenParam(null, SubscriptionStatus.ACTIVE.toCode()));
		assertThat(toUnqualifiedVersionlessIds(mySubscriptionDao.search(map)), contains(id));
	}

	@Test
	public void testEverythingTimings() throws Exception {
		String methodName = "testEverythingIncludesBackReferences";
		
		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Medication med = new Medication();
		med.getCode().setText(methodName);
		IIdType medId = myMedicationDao.create(med, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Patient pat = new Patient();
		pat.addAddress().addLine(methodName);
		pat.getManagingOrganization().setReferenceElement(orgId);
		IIdType patId = myPatientDao.create(pat, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.addAddress().addLine(methodName);
		pat2.getManagingOrganization().setReferenceElement(orgId);
		IIdType patId2 = myPatientDao.create(pat2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		MedicationOrder mo = new MedicationOrder();
		mo.getPatient().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationOrderDao.create(mo, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		IBundleProvider resp = myPatientDao.patientTypeEverything(request, null, null, null, null, null, new ServletRequestDetails());
		assertThat(toUnqualifiedVersionlessIds(resp), containsInAnyOrder(orgId, medId, patId, moId, patId2));

		request = mock(HttpServletRequest.class);
		resp = myPatientDao.patientInstanceEverything(request, patId, null, null, null, null, null, new ServletRequestDetails());
		assertThat(toUnqualifiedVersionlessIds(resp), containsInAnyOrder(orgId, medId, patId, moId));
	}

	@Test
	public void testIndexNoDuplicatesDate() {
		DiagnosticOrder order = new DiagnosticOrder();
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-11T11:12:12Z"));
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-11T11:12:12Z"));
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-11T11:12:12Z"));
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-12T11:12:12Z"));
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-12T11:12:12Z"));
		order.addItem().addEvent().setDateTimeElement(new DateTimeType("2011-12-12T11:12:12Z"));
		
		IIdType id = myDiagnosticOrderDao.create(order, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myDiagnosticOrderDao.search(DiagnosticOrder.SP_ITEM_DATE, new DateParam("2011-12-12T11:12:12Z")));
		assertThat(actual, contains(id));
		
		Class<ResourceIndexedSearchParamDate> type = ResourceIndexedSearchParamDate.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
	}
	
	@Test
	public void testIndexNoDuplicatesNumber() {
		Immunization res = new Immunization();
		res.addVaccinationProtocol().setDoseSequence(1);
		res.addVaccinationProtocol().setDoseSequence(1);
		res.addVaccinationProtocol().setDoseSequence(1);
		res.addVaccinationProtocol().setDoseSequence(2);
		res.addVaccinationProtocol().setDoseSequence(2);
		res.addVaccinationProtocol().setDoseSequence(2);
		
		IIdType id = myImmunizationDao.create(res, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myImmunizationDao.search(Immunization.SP_DOSE_SEQUENCE, new NumberParam("1")));
		assertThat(actual, contains(id));
		
		Class<ResourceIndexedSearchParamNumber> type = ResourceIndexedSearchParamNumber.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
	}
	
	@Test
	public void testIndexNoDuplicatesQuantity() {
		Substance res = new Substance();
		res.addInstance().getQuantity().setSystem("http://foo").setCode("UNIT").setValue(123);
		res.addInstance().getQuantity().setSystem("http://foo").setCode("UNIT").setValue(123);
		res.addInstance().getQuantity().setSystem("http://foo2").setCode("UNIT2").setValue(1232);
		res.addInstance().getQuantity().setSystem("http://foo2").setCode("UNIT2").setValue(1232);
		
		IIdType id = mySubstanceDao.create(res, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Class<ResourceIndexedSearchParamQuantity> type = ResourceIndexedSearchParamQuantity.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(mySubstanceDao.search(Substance.SP_QUANTITY, new QuantityParam((ParamPrefixEnum)null, 123, "http://foo", "UNIT")));
		assertThat(actual, contains(id));
	}
	
	@Test
	public void testIndexNoDuplicatesReference() {
		Practitioner pract =new Practitioner();
		pract.setId("Practitioner/somepract");
		pract.getName().addFamily("SOME PRACT");
		myPractitionerDao.update(pract, new ServletRequestDetails());
		Practitioner pract2 =new Practitioner();
		pract2.setId("Practitioner/somepract2");
		pract2.getName().addFamily("SOME PRACT2");
		myPractitionerDao.update(pract2, new ServletRequestDetails());
		
		DiagnosticOrder res = new DiagnosticOrder();
		res.addEvent().setActor(new Reference("Practitioner/somepract"));
		res.addEvent().setActor(new Reference("Practitioner/somepract"));
		res.addEvent().setActor(new Reference("Practitioner/somepract2"));
		res.addEvent().setActor(new Reference("Practitioner/somepract2"));
		
		IIdType id = myDiagnosticOrderDao.create(res, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Class<ResourceLink> type = ResourceLink.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myDiagnosticOrderDao.search(DiagnosticOrder.SP_ACTOR, new ReferenceParam("Practitioner/somepract")));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesString() {
		Patient p = new Patient();
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("456 Fake Street");
		p.addAddress().addLine("456 Fake Street");
		p.addAddress().addLine("456 Fake Street");
		
		IIdType id = myPatientDao.create(p, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Class<ResourceIndexedSearchParamString> type = ResourceIndexedSearchParamString.class;
		List<ResourceIndexedSearchParamString> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myPatientDao.search(Patient.SP_ADDRESS, new StringParam("123 Fake Street")));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesToken() {
		Patient res = new Patient();
		res.addIdentifier().setSystem("http://foo1").setValue("123");
		res.addIdentifier().setSystem("http://foo1").setValue("123");
		res.addIdentifier().setSystem("http://foo2").setValue("1234");
		res.addIdentifier().setSystem("http://foo2").setValue("1234");
		
		IIdType id = myPatientDao.create(res, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Class<ResourceIndexedSearchParamToken> type = ResourceIndexedSearchParamToken.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("http://foo1", "123")));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesUri() {
		ConceptMap res = new ConceptMap();
		res.addElement().addTarget().addDependsOn().setElement("http://foo");
		res.addElement().addTarget().addDependsOn().setElement("http://foo");
		res.addElement().addTarget().addDependsOn().setElement("http://bar");
		res.addElement().addTarget().addDependsOn().setElement("http://bar");
		
		IIdType id = myConceptMapDao.create(res, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		Class<ResourceIndexedSearchParamUri> type = ResourceIndexedSearchParamUri.class;
		List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
		ourLog.info(toStringMultiline(results));
		assertEquals(2, results.size());
		
		List<IIdType> actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(ConceptMap.SP_DEPENDSON, new UriParam("http://foo")));
		assertThat(actual, contains(id));
	}

	@Test
	public void testSearchAll() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, new ServletRequestDetails());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester").addGiven("John");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		List<IBaseResource> patients = toList(myPatientDao.search(params));
		assertTrue(patients.size() >= 2);
	}
	
	@Test
	public void testSearchByIdParam() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId();
		}
		IIdType id2;
		{
			Organization patient = new Organization();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myOrganizationDao.create(patient, new ServletRequestDetails()).getId();
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put("_id", new StringParam(id1.getIdPart()));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params.put("_id", new StringParam("9999999999999999"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params.put("_id", new StringParam(id2.getIdPart()));
		assertEquals(0, toList(myPatientDao.search(params)).size());

	}

	@Test
	public void testSearchByIdParamAnd() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;
		StringAndListParam param;
		
		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());
		
		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam("9999999999999")));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());
		
		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("9999999999999")));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());
		
	}

	@Test
	public void testSearchByIdParamOr() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		long betweenTime = System.currentTimeMillis();
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1, id2));

		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id1.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam("999999999999")));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		// With lastupdated
		
		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		params.setLastUpdated(new DateRangeParam(new Date(betweenTime), null));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id2));

	}

	@Test
	public void testSearchByIdParamWrongType() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Organization patient = new Organization();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myOrganizationDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

	}

	@Test
	public void testSearchCompositeParam() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o1.setValue(new StringType("testSearchCompositeParamS01"));
		IIdType id1 = myObservationDao.create(o1, new ServletRequestDetails()).getId();

		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o2.setValue(new StringType("testSearchCompositeParamS02"));
		IIdType id2 = myObservationDao.create(o2, new ServletRequestDetails()).getId();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS01");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
			IBundleProvider result = myObservationDao.search(Observation.SP_CODE_VALUE_STRING, val);
			assertEquals(1, result.size());
			assertEquals(id1.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS02");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
			IBundleProvider result = myObservationDao.search(Observation.SP_CODE_VALUE_STRING, val);
			assertEquals(1, result.size());
			assertEquals(id2.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		}
	}

	@Test
	public void testSearchCompositeParamDate() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o1.setValue(new Period().setStartElement(new DateTimeType("2001-01-01T11:11:11")));
		IIdType id1 = myObservationDao.create(o1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o2.setValue(new Period().setStartElement(new DateTimeType("2001-01-01T12:12:12")));
		IIdType id2 = myObservationDao.create(o2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam("2001-01-01T11:11:11");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<TokenParam, DateParam>(v0, v1);
			IBundleProvider result = myObservationDao.search(Observation.SP_CODE_VALUE_DATE, val);
			assertEquals(1, result.size());
			assertEquals(id1.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			// TODO: this should also work with ">2001-01-01T15:12:12" since the two times only have a lower bound
			DateParam v1 = new DateParam(">2001-01-01T10:12:12");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<TokenParam, DateParam>(v0, v1);
			IBundleProvider result = myObservationDao.search(Observation.SP_CODE_VALUE_DATE, val);
			assertEquals(2, result.size());
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));
		}

	}

	
	/**
	 * #222
	 */
	@Test
	public void testSearchForDeleted() {

		{
			Patient patient = new Patient();
			patient.setId("TEST");
			patient.setLanguageElement(new CodeType("TEST"));
			patient.addName().addFamily("TEST");
			patient.addIdentifier().setSystem("TEST").setValue("TEST");
			myPatientDao.update(patient, new ServletRequestDetails());
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put("_id", new StringParam("TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params.put("_language", new StringParam("TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params.put(Patient.SP_IDENTIFIER, new TokenParam("TEST", "TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params.put(Patient.SP_NAME, new StringParam("TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		myPatientDao.delete(new IdType("Patient/TEST"), new ServletRequestDetails());
		
		params = new HashMap<String, IQueryParameterType>();
		params.put("_id", new StringParam("TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params.put("_language", new StringParam("TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params.put(Patient.SP_IDENTIFIER, new TokenParam("TEST", "TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params.put(Patient.SP_NAME, new StringParam("TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());


	}

	@Test
	public void testSearchForUnknownAlphanumericId() {
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add("_id", new StringParam("testSearchForUnknownAlphanumericId"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(0, retrieved.size());
		}
	}

	@Test
	public void testSearchLanguageParam() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.getLanguageElement().setValue("en_CA");
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("Joe");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId();
		}
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.getLanguageElement().setValue("en_US");
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("John");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId();
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(IAnyResource.SP_RES_LANGUAGE, new StringParam("en_CA"));
			List<IBaseResource> patients = toList(myPatientDao.search(params));
			assertEquals(1, patients.size());
			assertEquals(id1.toUnqualifiedVersionless(), patients.get(0).getIdElement().toUnqualifiedVersionless());
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(IAnyResource.SP_RES_LANGUAGE, new StringParam("en_US"));
			List<Patient> patients = toList(myPatientDao.search(params));
			assertEquals(1, patients.size());
			assertEquals(id2.toUnqualifiedVersionless(), patients.get(0).getIdElement().toUnqualifiedVersionless());
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(IAnyResource.SP_RES_LANGUAGE, new StringParam("en_GB"));
			List<Patient> patients = toList(myPatientDao.search(params));
			assertEquals(0, patients.size());
		}
	}
	
	@Test
	public void testSearchLanguageParamAndOr() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.getLanguageElement().setValue("en_CA");
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("Joe");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		
		Date betweenTime = new Date();
		
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.getLanguageElement().setValue("en_US");
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("John");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_LANGUAGE, new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("en_US")));
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1, id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_LANGUAGE, new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("en_US")));
			params.setLastUpdated(new DateRangeParam(betweenTime, null));
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_LANGUAGE, new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("ZZZZZ")));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("ZZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("")).addOr(new StringParam(null)));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_id", new StringParam(id1.getIdPart()));
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("")).addOr(new StringParam(null)));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			StringAndListParam and = new StringAndListParam();
			and.addAnd(new StringOrListParam().addOr(new StringParam("en_CA")).addOr(new StringParam("ZZZZ")));
			and.addAnd(new StringOrListParam().addOr(new StringParam("")).addOr(new StringParam(null)));
			params.add(IAnyResource.SP_RES_LANGUAGE, and);
			params.add("_id", new StringParam(id1.getIdPart()));
			assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));
		}

	}

	@Test
	public void testSearchLastUpdatedParam() throws InterruptedException {
		String methodName = "testSearchLastUpdatedParam";

		int sleep = 100;
		Thread.sleep(sleep);

		DateTimeType beforeAny = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		IIdType id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily(methodName).addGiven("Joe");
			id1a = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName + "XXXX").addGiven("Joe");
			id1b = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		Thread.sleep(1100);
		DateTimeType beforeR2 = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		Thread.sleep(1100);

		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily(methodName).addGiven("John");
			id2 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeAny, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeR2, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id2));
			assertThat(patients, not(hasItems(id1a, id1b)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeAny, beforeR2));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients.toString(), patients, not(hasItems(id2)));
			assertThat(patients.toString(), patients, (hasItems(id1a, id1b)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(null, beforeR2));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, (hasItems(id1a, id1b)));
			assertThat(patients, not(hasItems(id2)));
		}
	}

	@Test
	public void testSearchLastUpdatedParamWithComparator() throws InterruptedException {
		IIdType id0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id0 = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		int sleep = 100;
		
		long start = System.currentTimeMillis();
		Thread.sleep(sleep);

		IIdType id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1a = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1b = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		
		ourLog.info("Res 1: {}", myPatientDao.read(id0, new ServletRequestDetails()).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("Res 2: {}", myPatientDao.read(id1a, new ServletRequestDetails()).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("Res 3: {}", myPatientDao.read(id1b, new ServletRequestDetails()).getMeta().getLastUpdatedElement().getValueAsString());
		
		
		Thread.sleep(sleep);
		long end = System.currentTimeMillis();
		
		SearchParameterMap map;
		Date startDate = new Date(start);
		Date endDate = new Date(end);
		DateTimeType startDateTime = new DateTimeType(startDate, TemporalPrecisionEnum.MILLI);
		DateTimeType endDateTime = new DateTimeType(endDate, TemporalPrecisionEnum.MILLI);
		
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(startDateTime, endDateTime));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));
		
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, startDateTime), new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, endDateTime)));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));
		
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, startDateTime), new DateParam(ParamPrefixEnum.LESSTHAN, endDateTime)));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));

		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(QuantityCompararatorEnum.GREATERTHAN, startDateTime.getValue()), new DateParam(QuantityCompararatorEnum.LESSTHAN, myPatientDao.read(id1b, new ServletRequestDetails()).getMeta().getLastUpdatedElement().getValue())));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a));
	}
	@Test
	public void testSearchNameParam() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");
			id1 = myPatientDao.create(patient, new ServletRequestDetails()).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("testSearchNameParam02Fam").addGiven("testSearchNameParam02Giv");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Fam"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		// Given name shouldn't return for family param
		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Giv"));
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_NAME, new StringParam("testSearchNameParam01Fam"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_NAME, new StringParam("testSearchNameParam01Giv"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Foo"));
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	/**
	 * TODO: currently this doesn't index, we should get it working
	 */
	@Test
	public void testSearchNearParam() {
		{
			Location loc = new Location();
			loc.getPosition().setLatitude(43.7);
			loc.getPosition().setLatitude(79.4);
			myLocationDao.create(loc, new ServletRequestDetails());
		}
	}
	
	
	@Test
	public void testSearchNumberParam() {
		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("foo").setValue("testSearchNumberParam01");
		e1.getLength().setSystem(BaseHapiFhirDao.UCUM_NS).setCode("min").setValue(4.0 * 24 * 60);
		IIdType id1 = myEncounterDao.create(e1, new ServletRequestDetails()).getId();

		Encounter e2 = new Encounter();
		e2.addIdentifier().setSystem("foo").setValue("testSearchNumberParam02");
		e2.getLength().setSystem(BaseHapiFhirDao.UCUM_NS).setCode("year").setValue(2.0);
		IIdType id2 = myEncounterDao.create(e2, new ServletRequestDetails()).getId();
		{
			IBundleProvider found = myEncounterDao.search(Encounter.SP_LENGTH, new NumberParam(">2"));
			assertEquals(2, found.size());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1.toUnqualifiedVersionless(), id2.toUnqualifiedVersionless()));
		}
		{
			IBundleProvider found = myEncounterDao.search(Encounter.SP_LENGTH, new NumberParam("<1"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myEncounterDao.search(Encounter.SP_LENGTH, new NumberParam("4"));
			assertEquals(1, found.size());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1.toUnqualifiedVersionless()));
		}
	}


	@Test
	public void testSearchParamChangesType() {
		String name = "testSearchParamChangesType";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().addFamily(name);
			id = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam(name));
		List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, contains(id));

		Patient patient = new Patient();
		patient.addIdentifier().setSystem(name).setValue(name);
		patient.setId(id);
		myPatientDao.update(patient, new ServletRequestDetails());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam(name));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, not(contains(id)));

	}
	
	@Test
	public void testSearchPractitionerPhoneAndEmailParam() {
		String methodName = "testSearchPractitionerPhoneAndEmailParam";
		IIdType id1;
		{
			Practitioner patient = new Practitioner();
			patient.getName().addFamily(methodName);
			patient.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("123");
			id1 = myPractitionerDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Practitioner patient = new Practitioner();
			patient.getName().addFamily(methodName);
			patient.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue("abc");
			id2 = myPractitionerDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		Map<String, IQueryParameterType> params;
		List<IIdType> patients;

		params = new HashMap<String, IQueryParameterType>();
		params.put(Practitioner.SP_FAMILY, new StringParam(methodName));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(2, patients.size());
		assertThat(patients, containsInAnyOrder(id1, id2));

		params = new HashMap<String, IQueryParameterType>();
		params.put(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.put(Practitioner.SP_EMAIL, new TokenParam(null, "abc"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(1, patients.size());
		assertThat(patients, containsInAnyOrder(id2));

		params = new HashMap<String, IQueryParameterType>();
		params.put(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.put(Practitioner.SP_EMAIL, new TokenParam(null, "123"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(0, patients.size());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.put(Practitioner.SP_PHONE, new TokenParam(null, "123"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(1, patients.size());
		assertThat(patients, containsInAnyOrder(id1));

	}

	@Test
	public void testSearchResourceLinkWithChain() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain01");
		IIdType patientId01 = myPatientDao.create(patient, new ServletRequestDetails()).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain02");
		IIdType patientId02 = myPatientDao.create(patient02, new ServletRequestDetails()).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, new ServletRequestDetails()).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, new ServletRequestDetails()).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new Reference(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, new ServletRequestDetails()).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChain01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(Observation.SP_PATIENT, new ReferenceParam(patientId01.getIdPart())));
		assertEquals(1, result.size());

		result = toList(myObservationDao.search(Observation.SP_PATIENT, new ReferenceParam(patientId01.getIdPart())));
		assertEquals(1, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "999999999999")));
		assertEquals(0, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChainXX")));
		assertEquals(2, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "testSearchResourceLinkWithChainXX")));
		assertEquals(2, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "|testSearchResourceLinkWithChainXX")));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchResourceLinkWithChainDouble() {
		String methodName = "testSearchResourceLinkWithChainDouble";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId01 = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setManagingOrganization(new Reference(orgId01));
		IIdType locParentId = myLocationDao.create(locParent, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setPartOf(new Reference(locParentId));
		IIdType locChildId = myLocationDao.create(locChild, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Location locGrandchild = new Location();
		locGrandchild.setPartOf(new Reference(locChildId));
		IIdType locGrandchildId = myLocationDao.create(locGrandchild, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		IBundleProvider found;
		ReferenceParam param;

		found = myLocationDao.search("organization", new ReferenceParam(orgId01.getIdPart()));
		assertEquals(1, found.size());
		assertEquals(locParentId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(orgId01.getIdPart());
		param.setChain("organization");
		found = myLocationDao.search("partof", param);
		assertEquals(1, found.size());
		assertEquals(locChildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(orgId01.getIdPart());
		param.setChain("partof.organization");
		found = myLocationDao.search("partof", param);
		assertEquals(1, found.size());
		assertEquals(locGrandchildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(methodName);
		param.setChain("partof.organization.name");
		found = myLocationDao.search("partof", param);
		assertEquals(1, found.size());
		assertEquals(locGrandchildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
	}

	@Test
	public void testSearchResourceLinkWithChainWithMultipleTypes() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypesXX");
		IIdType patientId01 = myPatientDao.create(patient, new ServletRequestDetails()).getId();

		Location loc01 = new Location();
		loc01.getNameElement().setValue("testSearchResourceLinkWithChainWithMultipleTypes01");
		IIdType locId01 = myLocationDao.create(loc01, new ServletRequestDetails()).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, new ServletRequestDetails()).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(locId01));
		IIdType obsId02 = myObservationDao.create(obs02, new ServletRequestDetails()).getId();

		ourLog.info("P1[{}] L1[{}] Obs1[{}] Obs2[{}]", new Object[] { patientId01, locId01, obsId01, obsId02 });

		List<Observation> result;
		
		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("Patient", Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01")));
		assertEquals(2, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesXX")));
		assertEquals(1, result.size());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesYY")));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchResourceLinkWithTextLogicalId() {
		Patient patient = new Patient();
		patient.setId("testSearchResourceLinkWithTextLogicalId01");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalId01");
		IIdType patientId01 = myPatientDao.update(patient, new ServletRequestDetails()).getId();

		Patient patient02 = new Patient();
		patient02.setId("testSearchResourceLinkWithTextLogicalId02");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalId02");
		IIdType patientId02 = myPatientDao.update(patient02, new ServletRequestDetails()).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, new ServletRequestDetails()).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, new ServletRequestDetails()).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new Reference(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, new ServletRequestDetails()).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		try {
			myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId99"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		/*
		 * TODO: it's kind of weird that we throw a 404 for textual IDs that don't exist, but just return an empty list
		 * for numeric IDs that don't exist
		 */

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999999")));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchStringParam() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("Joe");
			myPatientDao.create(patient, new ServletRequestDetails());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("John");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringParam("Tester_testSearchStringParam"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(2, patients.size());

		params.put(Patient.SP_FAMILY, new StringParam("FOO_testSearchStringParam"));
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchStringParamReallyLong() {
		String methodName = "testSearchStringParamReallyLong";
		String value = StringUtils.rightPad(methodName, 200, 'a');

		IIdType longId;
		IIdType shortId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily(value);
			longId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			shortId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		String substring = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		params.put(Patient.SP_FAMILY, new StringParam(substring));
		IBundleProvider found = myPatientDao.search(params);
		assertEquals(1, toList(found).size());
		assertThat(toUnqualifiedVersionlessIds(found), contains(longId));
		assertThat(toUnqualifiedVersionlessIds(found), not(contains(shortId)));

	}

	@Test
	public void testSearchStringParamWithNonNormalized() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			myPatientDao.create(patient, new ServletRequestDetails());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_GIVEN, new StringParam("testSearchStringParamWithNonNormalized_hora"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(2, patients.size());

		StringParam parameter = new StringParam("testSearchStringParamWithNonNormalized_hora");
		parameter.setExact(true);
		params.put(Patient.SP_GIVEN, parameter);
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchTokenParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().addFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem").setDisplay("testSearchTokenParamDisplay");
		myPatientDao.create(patient, new ServletRequestDetails());

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient.addName().addFamily("Tester").addGiven("testSearchTokenParam2");
		myPatientDao.create(patient, new ServletRequestDetails());

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testSearchTokenParam001"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(1, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam(null, "testSearchTokenParam001"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(1, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam("testSearchTokenParamSystem", "testSearchTokenParamCode"));
			assertEquals(1, myPatientDao.search(map).size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamCode", true));
			assertEquals(0, myPatientDao.search(map).size());
		}
		{
			// Complete match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamComText", true));
			assertEquals(1, myPatientDao.search(map).size());
		}
		{
			// Left match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamcomtex", true));
			assertEquals(1, myPatientDao.search(map).size());
		}
		{
			// Right match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamComTex", true));
			assertEquals(1, myPatientDao.search(map).size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add("urn:system", "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(2, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add(null, "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(2, retrieved.size());
		}
	}

	@Test
	@Ignore
	public void testSearchUnknownContentParam() {
		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("fulltext"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _content", e.getMessage());
		}
	}

	@Test
	@Ignore
	public void testSearchUnknownTextParam() {
		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_TEXT, new StringParam("fulltext"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _text", e.getMessage());
		}
	}

	@Test
	public void testSearchValueQuantity() {
		String methodName = "testSearchValueQuantity";

		QuantityParam param;
		Set<Long> found;
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), null, null);
		found = myObservationDao.searchForIds("value-quantity", param);
		int initialSize = found.size();

		Observation o = new Observation();
		o.getCode().addCoding().setSystem("urn:foo").setCode(methodName + "code");
		Quantity q = new Quantity().setSystem("urn:bar:" + methodName).setCode(methodName + "units").setValue(100);
		o.setValue(q);

		myObservationDao.create(o, new ServletRequestDetails());

		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), null, null);
		found = myObservationDao.searchForIds("value-quantity", param);
		assertEquals(1 + initialSize, found.size());

		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), null, methodName + "units");
		found = myObservationDao.searchForIds("value-quantity", param);
		assertEquals(1, found.size());

		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), "urn:bar:" + methodName, null);
		found = myObservationDao.searchForIds("value-quantity", param);
		assertEquals(1, found.size());

		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), "urn:bar:" + methodName, methodName + "units");
		found = myObservationDao.searchForIds("value-quantity", param);
		assertEquals(1, found.size());

	}

	@Test
	public void testSearchWithEmptySort() {
		SearchParameterMap criteriaUrl = new SearchParameterMap();
		DateRangeParam range = new DateRangeParam();
		range.setLowerBound(new DateParam(ParamPrefixEnum.GREATERTHAN, 1000000));
		range.setUpperBound(new DateParam(ParamPrefixEnum.LESSTHAN, 2000000));
		criteriaUrl.setLastUpdated(range);
		criteriaUrl.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		IBundleProvider results = myObservationDao.search(criteriaUrl);
		assertEquals(0, results.size());
	}

	@Test
	public void testSearchWithIncludes() {
		String methodName = "testSearchWithIncludes";
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId();
		}
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			IIdType orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId();

			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			myPatientDao.create(patient, new ServletRequestDetails());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_" + methodName + "_P2").addGiven("John");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		{
			// No includes
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			List<IBaseResource> patients = toList(myPatientDao.search(params));
			assertEquals(1, patients.size());
		}
		{
			// Named include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// Named include with parent non-recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// Named include with parent recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(3, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
			assertEquals(Organization.class, patients.get(2).getClass());
		}
		{
			// * include non recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// * include recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(3, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
			assertEquals(Organization.class, patients.get(2).getClass());
		}
		{
			// Irrelevant include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Encounter.INCLUDE_INDICATION);
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(1, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithIncludesParameterNoRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_ID, new StringParam(orgId.getIdPart()));
			params.addInclude(Organization.INCLUDE_PARTOF.asNonRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(resources, contains(orgId, parentOrgId));
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithIncludesParameterRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_ID, new StringParam(orgId.getIdPart()));
			params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			ourLog.info(resources.toString());
			assertThat(resources, contains(orgId, parentOrgId, parentParentOrgId));
		}
	}

	@Test
	public void testSearchWithIncludesStarNoRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(new Include("*").asNonRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(resources, contains(patientId, orgId));
		}
	}

	@Test
	public void testSearchWithIncludesStarRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(new Include("*").asRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(resources, contains(patientId, orgId, parentOrgId, parentParentOrgId));
		}
	}

	/**
	 * Test for #62
	 */
	@Test
	public void testSearchWithIncludesThatHaveTextId() {
		{
			Organization org = new Organization();
			org.setId("testSearchWithIncludesThatHaveTextIdid1");
			org.getNameElement().setValue("testSearchWithIncludesThatHaveTextId_O1");
			IIdType orgId = myOrganizationDao.update(org, new ServletRequestDetails()).getId();
			assertThat(orgId.getValue(), endsWith("Organization/testSearchWithIncludesThatHaveTextIdid1/_history/1"));

			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_testSearchWithIncludesThatHaveTextId_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			myPatientDao.create(patient, new ServletRequestDetails());
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testSearchWithIncludesThatHaveTextId_P2").addGiven("John");
			myPatientDao.create(patient, new ServletRequestDetails());
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		params.addInclude(Patient.INCLUDE_ORGANIZATION);
		IBundleProvider search = myPatientDao.search(params);
		List<IBaseResource> patients = toList(search);
		assertEquals(2, patients.size());
		assertEquals(Patient.class, patients.get(0).getClass());
		assertEquals(Organization.class, patients.get(1).getClass());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());

	}

	@Test
	public void testSearchWithMissingDate() {
		IIdType orgId = myOrganizationDao.create(new Organization(), new ServletRequestDetails()).getId();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		// Date Param
		{
			HashMap<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			DateParam param = new DateParam();
			param.setMissing(false);
			params.put(Patient.SP_BIRTHDATE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			DateParam param = new DateParam();
			param.setMissing(true);
			params.put(Patient.SP_BIRTHDATE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithMissingQuantity() {
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.setValue(new Quantity(123));
			notMissing = myObservationDao.create(obs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		// Quantity Param
		{
			HashMap<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			QuantityParam param = new QuantityParam();
			param.setMissing(false);
			params.put(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			QuantityParam param = new QuantityParam();
			param.setMissing(true);
			params.put(Observation.SP_VALUE_QUANTITY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithMissingReference() {
		IIdType orgId = myOrganizationDao.create(new Organization(), new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		// Reference Param
		{
			HashMap<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			ReferenceParam param = new ReferenceParam();
			param.setMissing(false);
			params.put(Patient.SP_ORGANIZATION, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			ReferenceParam param = new ReferenceParam();
			param.setMissing(true);
			params.put(Patient.SP_ORGANIZATION, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
			assertThat(patients, not(containsInRelativeOrder(orgId)));
		}
	}

	@Test
	public void testSearchWithMissingString() {
		IIdType orgId = myOrganizationDao.create(new Organization(), new ServletRequestDetails()).getId();
		IIdType notMissing;
		IIdType missing;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			notMissing = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		// String Param
		{
			HashMap<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			StringParam param = new StringParam();
			param.setMissing(false);
			params.put(Patient.SP_FAMILY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			StringParam param = new StringParam();
			param.setMissing(true);
			params.put(Patient.SP_FAMILY, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithNoResults() {
		Device dev = new Device();
		dev.addIdentifier().setSystem("Foo");
		myDeviceDao.create(dev, new ServletRequestDetails());

		IBundleProvider value = myDeviceDao.search(new SearchParameterMap());
		ourLog.info("Initial size: " + value.size());
		for (IBaseResource next : value.getResources(0, value.size())) {
			ourLog.info("Deleting: {}", next.getIdElement());
			myDeviceDao.delete((IIdType) next.getIdElement(), new ServletRequestDetails());
		}

		value = myDeviceDao.search(new SearchParameterMap());
		if (value.size() > 0) {
			ourLog.info("Found: " + (value.getResources(0, 1).get(0).getIdElement()));
			fail(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(value.getResources(0, 1).get(0)));
		}
		assertEquals(0, value.size());

		List<IBaseResource> res = value.getResources(0, 0);
		assertTrue(res.isEmpty());

	}

	@Test
	public void testSearchWithSecurityAndProfileParams() {
		String methodName = "testSearchWithSecurityAndProfileParams";

		IIdType tag1id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addSecurity("urn:taglist", methodName + "1a", null);
			tag1id = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		IIdType tag2id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addProfile("http://" + methodName);
			tag2id = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_security", new TokenParam("urn:taglist", methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_profile", new UriParam("http://" + methodName));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag2id));
		}
	}

	@Test
	public void testSearchWithTagParameter() {
		String methodName = "testSearchWithTagParameter";

		IIdType tag1id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "1a", null);
			org.getMeta().addTag("urn:taglist", methodName + "1b", null);
			tag1id = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		
		Date betweenDate = new Date();
		
		IIdType tag2id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "2a",null);
			org.getMeta().addTag("urn:taglist", methodName + "2b",null);
			tag2id = myOrganizationDao.create(org, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}

		{
			// One tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			// Code only
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam(null, methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			// Or tags
			SearchParameterMap params = new SearchParameterMap();
			TokenOrListParam orListParam = new TokenOrListParam();
			orListParam.add(new TokenParam("urn:taglist", methodName + "1a"));
			orListParam.add(new TokenParam("urn:taglist", methodName + "2a"));
			params.add("_tag", orListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id, tag2id));
		}
		{
			// Or tags with lastupdated
			SearchParameterMap params = new SearchParameterMap();
			TokenOrListParam orListParam = new TokenOrListParam();
			orListParam.add(new TokenParam("urn:taglist", methodName + "1a"));
			orListParam.add(new TokenParam("urn:taglist", methodName + "2a"));
			params.add("_tag", orListParam);
			params.setLastUpdated(new DateRangeParam(betweenDate, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag2id));
		}
		// TODO: get multiple/AND working
		{
			// And tags
			SearchParameterMap params = new SearchParameterMap();
			TokenAndListParam andListParam = new TokenAndListParam();
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1a"));
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "2a"));
			params.add("_tag", andListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertEquals(0, patients.size());
		}

		{
			// And tags
			SearchParameterMap params = new SearchParameterMap();
			TokenAndListParam andListParam = new TokenAndListParam();
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1a"));
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1b"));
			params.add("_tag", andListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}

	}
	
	@Test
	public void testSearchWithToken() {
		IIdType notMissing;
		IIdType missing;
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("001");
			missing = myObservationDao.create(obs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("002");
			obs.getCode().addCoding().setSystem("urn:system").setCode("002");
			notMissing = myObservationDao.create(obs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		}
		// Token Param
		{
			HashMap<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			TokenParam param = new TokenParam();
			param.setMissing(false);
			params.put(Observation.SP_CODE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, not(containsInRelativeOrder(missing)));
			assertThat(patients, containsInRelativeOrder(notMissing));
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			TokenParam param = new TokenParam();
			param.setMissing(true);
			params.put(Observation.SP_CODE, param);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myObservationDao.search(params));
			assertThat(patients, containsInRelativeOrder(missing));
			assertThat(patients, not(containsInRelativeOrder(notMissing)));
		}
	}

	@Test
	public void testSearchWithUriParamAbove() throws Exception {
		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		myValueSetDao.create(vs1, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
	
		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType id2 = myValueSetDao.create(vs2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		ValueSet vs3 = new ValueSet();
		vs3.setUrl("http://hl7.org/foo/bar/baz");
		IIdType id3 = myValueSetDao.create(vs3, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar/baz/boz").setQualifier(UriParamQualifierEnum.ABOVE));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2, id3));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar/baz").setQualifier(UriParamQualifierEnum.ABOVE));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2, id3));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar").setQualifier(UriParamQualifierEnum.ABOVE));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type").setQualifier(UriParamQualifierEnum.ABOVE));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org").setQualifier(UriParamQualifierEnum.ABOVE));
		assertThat(toUnqualifiedVersionlessIds(result), empty());
	}
	
	@Test
	public void testSearchWithUriParam() throws Exception {
		Class<ValueSet> type = ValueSet.class;
		String resourceName = "/valueset-dstu2.json";
		ValueSet vs = loadResourceFromClasspath(type, resourceName);
		IIdType id1 = myValueSetDao.update(vs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType id2 = myValueSetDao.create(vs2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		IBundleProvider result;
		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type"));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));
		
		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));
	}

	@Test
	public void testSearchWithUriParamBelow() throws Exception {
		Class<ValueSet> type = ValueSet.class;
		String resourceName = "/valueset-dstu2.json";
		ValueSet vs = loadResourceFromClasspath(type, resourceName);
		IIdType id1 = myValueSetDao.update(vs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType id2 = myValueSetDao.create(vs2, new ServletRequestDetails()).getId().toUnqualifiedVersionless();
		
		IBundleProvider result;

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));

		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/foo").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2));
	
		result = myValueSetDao.search(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/baz").setQualifier(UriParamQualifierEnum.BELOW));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder());
	}
	
	private String toStringMultiline(List<?> theResults) {
		StringBuilder b = new StringBuilder();
		for (Object next : theResults) {
			b.append('\n');
			b.append(" * ").append(next.toString());
		}
		return b.toString();
	}

	
}
