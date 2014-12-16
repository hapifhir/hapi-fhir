package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class FhirResourceDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Encounter> ourEncounterDao;
	private static FhirContext ourFhirCtx;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoTest.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;

	@Test
	public void testChoiceParamConcept() {
		Observation o1 = new Observation();
		o1.getName().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(new CodeableConceptDt("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IdDt id1 = ourObservationDao.create(o1).getId();

		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_CONCEPT, new TokenParam("testChoiceParam01CCS", "testChoiceParam01CCV"));
			assertEquals(1, found.size());
			assertEquals(id1, found.getResources(0, 1).get(0).getId());
		}
	}

	@Test
	public void testChoiceParamDate() {
		Observation o2 = new Observation();
		o2.getName().addCoding().setSystem("foo").setCode("testChoiceParam02");
		o2.setValue(new PeriodDt().setStart(new DateTimeDt("2001-01-01")).setEnd(new DateTimeDt("2001-01-03")));
		IdDt id2 = ourObservationDao.create(o2).getId();

		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_DATE, new DateParam("2001-01-02"));
			assertEquals(1, found.size());
			assertEquals(id2, found.getResources(0, 1).get(0).getId());
		}
	}

	@Test
	public void testChoiceParamQuantity() {
		Observation o3 = new Observation();
		o3.getName().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new QuantityDt(QuantityCompararatorEnum.GREATERTHAN, 123.0, "foo", "bar"));
		IdDt id3 = ourObservationDao.create(o3).getId();

		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam(">100", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getId());
		}
		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("<100", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0001", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getId());
		}
		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("~120", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getId());
		}
	}

	@Test
	public void testChoiceParamString() {

		Observation o4 = new Observation();
		o4.getName().addCoding().setSystem("foo").setCode("testChoiceParam04");
		o4.setValue(new StringDt("testChoiceParam04Str"));
		IdDt id4 = ourObservationDao.create(o4).getId();

		{
			IBundleProvider found = ourObservationDao.search(Observation.SP_VALUE_STRING, new StringParam("testChoiceParam04Str"));
			assertEquals(1, found.size());
			assertEquals(id4, found.getResources(0, 1).get(0).getId());
		}
	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new ResourceReferenceDt("Patient/99999999"));
		try {
			ourPatientDao.create(patient);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("99999 not found"));
		}

	}

	@Test
	public void testDatePeriodParamEndOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier("testDatePeriodParam", "02");
			enc.getPeriod().getEnd().setValueAsString("2001-01-02");
			ourEncounterDao.create(enc);
		}
		SearchParameterMap params;
		List<Encounter> encs;

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		// encs = toList(ourEncounterDao.search(params));
		// assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-03", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDatePeriodParamStartAndEnd() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier("testDatePeriodParam", "03");
			enc.getPeriod().getStart().setValueAsString("2001-01-02");
			enc.getPeriod().getEnd().setValueAsString("2001-01-03");
			ourEncounterDao.create(enc);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		List<Encounter> encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-02", "2001-01-06"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-05"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-05", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDatePeriodParamStartOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier("testDatePeriodParam", "01");
			enc.getPeriod().getStart().setValueAsString("2001-01-02");
			ourEncounterDao.create(enc);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		List<Encounter> encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-03", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDelete() {
		int initialHistory = ourPatientDao.history(null).size();

		IdDt id1;
		IdDt id2;
		IdDt id2b;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("Tester_testDelete").addGiven("Joe");
			id1 = ourPatientDao.create(patient).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("Tester_testDelete").addGiven("John");
			id2 = ourPatientDao.create(patient).getId();
		}
		{
			Patient patient = ourPatientDao.read(id2);
			patient.addIdentifier("ZZZZZZZ", "ZZZZZZZZZ");
			id2b = ourPatientDao.update(patient, id2).getId();
		}
		ourLog.info("ID1:{}   ID2:{}   ID2b:{}", new Object[] { id1, id2, id2b });

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("Tester_testDelete"));
		List<Patient> patients = toList(ourPatientDao.search(params));
		assertEquals(2, patients.size());

		ourPatientDao.delete(id1);

		patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());

		ourPatientDao.read(id1);
		try {
			ourPatientDao.read(id1.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		IBundleProvider history = ourPatientDao.history(null);
		assertEquals(4 + initialHistory, history.size());
		List<IResource> resources = history.getResources(0, 4);
		assertNotNull(resources.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.DELETED_AT));

		try {
			ourPatientDao.delete(id2);
			fail();
		} catch (InvalidRequestException e) {
			// good
		}

		ourPatientDao.delete(id2.toVersionless());

		patients = toList(ourPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testIdParam() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = ourPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Date now = new Date();

		{
			Patient retrieved = ourPatientDao.read(outcome.getId());
			InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			assertTrue(published.before(now));
			assertTrue(updated.before(now));
		}

		/*
		 * This ID points to a patient, so we should not be able to return othe types with it
		 */
		try {
			ourEncounterDao.read(outcome.getId());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			ourEncounterDao.read(new IdDt(outcome.getId().getIdPart()));
			fail();
		} catch (ResourceNotFoundException e) {
			// expected
		}

		// Now search by _id
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(ourPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			List<Patient> ret = toList(ourPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(ourPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam("000"));
			List<Patient> ret = toList(ourPatientDao.search(paramMap));
			assertEquals(0, ret.size());
		}
	}

	@Test
	public void testOrganizationName() {

		//@formatter:off
		String inputStr = "{\"resourceType\":\"Organization\",\n" + 
				"                \"extension\":[\n" + 
				"                    {\n" + 
				"                        \"url\":\"http://fhir.connectinggta.ca/Profile/organization#providerIdPool\",\n" + 
				"                        \"valueUri\":\"urn:oid:2.16.840.1.113883.3.239.23.21.1\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"text\":{\n" + 
				"                    \"status\":\"empty\",\n" + 
				"                    \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/organization</div>\"\n" + 
				"                },\n" + 
				"                \"identifier\":[\n" + 
				"                    {\n" + 
				"                        \"use\":\"official\",\n" + 
				"                        \"label\":\"HSP 2.16.840.1.113883.3.239.23.21\",\n" + 
				"                        \"system\":\"urn:cgta:hsp_ids\",\n" + 
				"                        \"value\":\"urn:oid:2.16.840.1.113883.3.239.23.21\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"name\":\"Peterborough Regional Health Centre\"\n" + 
				"            }\n" + 
				"        }";
		//@formatter:on

		Set<Long> val = ourOrganizationDao.searchForIds("name", new StringParam("P"));
		int initial = val.size();

		Organization org = ourFhirCtx.newJsonParser().parseResource(Organization.class, inputStr);
		ourOrganizationDao.create(org);

		val = ourOrganizationDao.searchForIds("name", new StringParam("P"));
		assertEquals(initial + 1, val.size());

	}

	@Test
	public void testPersistResourceLink() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testPersistResourceLink01");
		IdDt patientId01 = ourPatientDao.create(patient).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier("urn:system", "testPersistResourceLink02");
		IdDt patientId02 = ourPatientDao.create(patient02).getId();

		Observation obs01 = new Observation();
		obs01.setApplies(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();

		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(patientId02));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt drId01 = ourDiagnosticReportDao.create(dr01).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getIdPart())));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getId().getIdPart());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getIdPart())));
		assertEquals(1, result.size());
		assertEquals(obsId02.getIdPart(), result.get(0).getId().getIdPart());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999")));
		assertEquals(0, result.size());

	}

	@Test
	public void testPersistSearchParamDate() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.setBirthDate(new DateTimeDt("2001-01-01"));

		ourPatientDao.create(patient);

		List<Patient> found = toList(ourPatientDao.search(Patient.SP_BIRTHDATE, new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		assertEquals(1, found.size());

		// If this throws an exception, that would be an acceptable outcome as well..
		found = toList(ourPatientDao.search(Patient.SP_BIRTHDATE + "AAAA", new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParamObservationString() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new StringDt("AAAABBBB"));

		ourObservationDao.create(obs);

		List<Observation> found = toList(ourObservationDao.search("value-string", new StringDt("AAAABBBB")));
		assertEquals(1, found.size());

		found = toList(ourObservationDao.search("value-string", new StringDt("AAAABBBBCCC")));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new QuantityDt(111));

		ourObservationDao.create(obs);

		List<Observation> found = toList(ourObservationDao.search("value-quantity", new QuantityDt(111)));
		assertEquals(1, found.size());

		found = toList(ourObservationDao.search("value-quantity", new QuantityDt(112)));
		assertEquals(1, found.size());

		found = toList(ourObservationDao.search("value-quantity", new QuantityDt(212)));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParams() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001testPersistSearchParams");
		patient.getGender().setValueAsEnum(AdministrativeGenderCodesEnum.M);
		patient.addName().addFamily("Tester").addGiven("JoetestPersistSearchParams");

		MethodOutcome outcome = ourPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		long id = outcome.getId().getIdPartAsLong();

		IdentifierDt value = new IdentifierDt("urn:system", "001testPersistSearchParams");
		List<Patient> found = toList(ourPatientDao.search(Patient.SP_IDENTIFIER, value));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().getIdPartAsLong().longValue());

		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "M"));
		// assertEquals(1, found.size());
		// assertEquals(id, found.get(0).getId().asLong().longValue());
		//
		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "F"));
		// assertEquals(0, found.size());

		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new IdentifierDt(AdministrativeGenderCodesEnum.M.getSystem(), "M"));
		found = toList(ourPatientDao.search(map));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().getIdPartAsLong().longValue());

		map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new IdentifierDt(AdministrativeGenderCodesEnum.M.getSystem(), "F"));
		found = toList(ourPatientDao.search(map));
		assertEquals(0, found.size());

	}

	@Test
	public void testSearchAll() {
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("Tester").addGiven("Joe");
			ourPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("Tester").addGiven("John");
			ourPatientDao.create(patient);
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		List<Patient> patients = toList(ourPatientDao.search(params));
		assertTrue(patients.size() >= 2);
	}

	@Test
	public void testHistoryByForcedId() {
		IdDt idv1;
		IdDt idv2;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "testHistoryByForcedId");
			patient.addName().addFamily("Tester").addGiven("testHistoryByForcedId");
			patient.setId("Patient/testHistoryByForcedId");
			idv1 = ourPatientDao.create(patient).getId();

			patient.addName().addFamily("Tester").addGiven("testHistoryByForcedIdName2");
			idv2 = ourPatientDao.update(patient, idv1.toUnqualifiedVersionless()).getId();
		}

		List<Patient> patients = toList(ourPatientDao.history(idv1.toVersionless(), null));
		assertTrue(patients.size() == 2);
		// Newest first
		assertEquals("Patient/testHistoryByForcedId/_history/2", patients.get(0).getId().toUnqualified().getValue());
		assertEquals("Patient/testHistoryByForcedId/_history/1", patients.get(1).getId().toUnqualified().getValue());
	}

	@Test
	public void testSearchByIdParam() {
		IdDt id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			id1 = ourPatientDao.create(patient).getId();
		}
		IdDt id2;
		{
			Organization patient = new Organization();
			patient.addIdentifier("urn:system", "001");
			id2 = ourOrganizationDao.create(patient).getId();
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put("_id", new StringDt(id1.getIdPart()));
		assertEquals(1, toList(ourPatientDao.search(params)).size());

		params.put("_id", new StringDt("9999999999999999"));
		assertEquals(0, toList(ourPatientDao.search(params)).size());

		params.put("_id", new StringDt(id2.getIdPart()));
		assertEquals(0, toList(ourPatientDao.search(params)).size());

	}

	@Test
	public void testSearchCompositeParam() {
		Observation o1 = new Observation();
		o1.getName().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o1.setValue(new StringDt("testSearchCompositeParamS01"));
		IdDt id1 = ourObservationDao.create(o1).getId();

		Observation o2 = new Observation();
		o2.getName().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o2.setValue(new StringDt("testSearchCompositeParamS02"));
		IdDt id2 = ourObservationDao.create(o2).getId();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS01");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
			IBundleProvider result = ourObservationDao.search(Observation.SP_NAME_VALUE_STRING, val);
			assertEquals(1, result.size());
			assertEquals(id1.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getId().toUnqualifiedVersionless());
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS02");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<TokenParam, StringParam>(v0, v1);
			IBundleProvider result = ourObservationDao.search(Observation.SP_NAME_VALUE_STRING, val);
			assertEquals(1, result.size());
			assertEquals(id2.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getId().toUnqualifiedVersionless());
		}
	}

	@Test
	public void testSearchCompositeParamDate() {
		Observation o1 = new Observation();
		o1.getName().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o1.setValue(new PeriodDt().setStart(new DateTimeDt("2001-01-01T11:11:11")));
		IdDt id1 = ourObservationDao.create(o1).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getName().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o2.setValue(new PeriodDt().setStart(new DateTimeDt("2001-01-01T12:12:12")));
		IdDt id2 = ourObservationDao.create(o2).getId().toUnqualifiedVersionless();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam("2001-01-01T11:11:11");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<TokenParam, DateParam>(v0, v1);
			IBundleProvider result = ourObservationDao.search(Observation.SP_NAME_VALUE_DATE, val);
			assertEquals(1, result.size());
			assertEquals(id1.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getId().toUnqualifiedVersionless());
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			// TODO: this should also work with ">2001-01-01T15:12:12" since the two times only have a lower bound
			DateParam v1 = new DateParam(">2001-01-01T10:12:12");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<TokenParam, DateParam>(v0, v1);
			IBundleProvider result = ourObservationDao.search(Observation.SP_NAME_VALUE_DATE, val);
			assertEquals(2, result.size());
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));
		}

	}

	@Test
	public void testSearchForUnknownAlphanumericId() {
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add("_id", new StringParam("testSearchForUnknownAlphanumericId"));
			IBundleProvider retrieved = ourPatientDao.search(map);
			assertEquals(0, retrieved.size());
		}
	}

	@Test
	public void testSearchNameParam() {
		IdDt id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");
			ResourceMetadataKeyEnum.TITLE.put(patient, "P1TITLE");
			id1 = ourPatientDao.create(patient).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("testSearchNameParam02Fam").addGiven("testSearchNameParam02Giv");
			ourPatientDao.create(patient);
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Fam"));
		List<Patient> patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getId().getIdPart());
		assertEquals("P1TITLE", ResourceMetadataKeyEnum.TITLE.get(patients.get(0)));

		// Given name shouldn't return for family param
		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Giv"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(0, patients.size());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_NAME, new StringDt("testSearchNameParam01Fam"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getId().getIdPart());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_NAME, new StringDt("testSearchNameParam01Giv"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getId().getIdPart());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Foo"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchNumberParam() {
		Encounter e1 = new Encounter();
		e1.addIdentifier("foo", "testSearchNumberParam01");
		e1.getLength().setSystem(BaseFhirDao.UCUM_NS).setCode("min").setValue(4.0 * 24 * 60);
		IdDt id1 = ourEncounterDao.create(e1).getId();

		Encounter e2 = new Encounter();
		e2.addIdentifier("foo", "testSearchNumberParam02");
		e2.getLength().setSystem(BaseFhirDao.UCUM_NS).setCode("year").setValue(2.0);
		IdDt id2 = ourEncounterDao.create(e2).getId();
		{
			IBundleProvider found = ourEncounterDao.search(Encounter.SP_LENGTH, new NumberParam(">2"));
			assertEquals(2, found.size());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1.toUnqualifiedVersionless(), id2.toUnqualifiedVersionless()));
		}
		{
			IBundleProvider found = ourEncounterDao.search(Encounter.SP_LENGTH, new NumberParam("<1"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = ourEncounterDao.search(Encounter.SP_LENGTH, new NumberParam("2"));
			assertEquals(1, found.size());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1.toUnqualifiedVersionless()));
		}
	}

	@Test
	public void testSearchResourceLinkWithChain() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testSearchResourceLinkWithChainXX");
		patient.addIdentifier("urn:system", "testSearchResourceLinkWithChain01");
		IdDt patientId01 = ourPatientDao.create(patient).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier("urn:system", "testSearchResourceLinkWithChainXX");
		patient02.addIdentifier("urn:system", "testSearchResourceLinkWithChain02");
		IdDt patientId02 = ourPatientDao.create(patient02).getId();

		Observation obs01 = new Observation();
		obs01.setApplies(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();

		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(patientId02));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt drId01 = ourDiagnosticReportDao.create(dr01).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChain01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getId().getIdPart());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "999999999999")));
		assertEquals(0, result.size());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChainXX")));
		assertEquals(2, result.size());

	}

	@Test
	public void testSearchResourceLinkWithTextLogicalId() {
		Patient patient = new Patient();
		patient.setId("testSearchResourceLinkWithTextLogicalId01");
		patient.addIdentifier("urn:system", "testSearchResourceLinkWithTextLogicalIdXX");
		patient.addIdentifier("urn:system", "testSearchResourceLinkWithTextLogicalId01");
		IdDt patientId01 = ourPatientDao.create(patient).getId();

		Patient patient02 = new Patient();
		patient02.setId("testSearchResourceLinkWithTextLogicalId02");
		patient02.addIdentifier("urn:system", "testSearchResourceLinkWithTextLogicalIdXX");
		patient02.addIdentifier("urn:system", "testSearchResourceLinkWithTextLogicalId02");
		IdDt patientId02 = ourPatientDao.create(patient02).getId();

		Observation obs01 = new Observation();
		obs01.setApplies(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();

		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(patientId02));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt drId01 = ourDiagnosticReportDao.create(dr01).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getId().getIdPart());

		try {
			ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId99"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		/*
		 * TODO: it's kind of weird that we throw a 404 for textual IDs that don't exist, but just return an empty list for numeric IDs that don't exist
		 */

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999999")));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchResourceLinkWithChainWithMultipleTypes() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypesXX");
		IdDt patientId01 = ourPatientDao.create(patient).getId();

		Location loc01 = new Location();
		loc01.getName().setValue("testSearchResourceLinkWithChainWithMultipleTypes01");
		IdDt locId01 = ourLocationDao.create(loc01).getId();

		Observation obs01 = new Observation();
		obs01.setApplies(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();

		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(locId01));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		ourLog.info("P1[{}] L1[{}] Obs1[{}] Obs2[{}]", new Object[] { patientId01, locId01, obsId01, obsId02 });

		List<Observation> result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01")));
		assertEquals(2, result.size());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesXX")));
		assertEquals(1, result.size());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesYY")));
		assertEquals(0, result.size());

		result = toList(ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("Patient", Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01")));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getId().getIdPart());

	}

	@Test
	public void testSearchStringParam() {
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("Joe");
			ourPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("Tester_testSearchStringParam").addGiven("John");
			ourPatientDao.create(patient);
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("Tester_testSearchStringParam"));
		List<Patient> patients = toList(ourPatientDao.search(params));
		assertEquals(2, patients.size());

		params.put(Patient.SP_FAMILY, new StringDt("FOO_testSearchStringParam"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchLanguageParam() {
		IdDt id1;
		{
			Patient patient = new Patient();
			patient.getLanguage().setValue("en_CA");
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("Joe");
			id1 = ourPatientDao.create(patient).getId();
		}
		IdDt id2;
		{
			Patient patient = new Patient();
			patient.getLanguage().setValue("en_US");
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("testSearchLanguageParam").addGiven("John");
			id2 = ourPatientDao.create(patient).getId();
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(Patient.SP_RES_LANGUAGE, new StringParam("en_CA"));
			List<Patient> patients = toList(ourPatientDao.search(params));
			assertEquals(1, patients.size());
			assertEquals(id1.toUnqualifiedVersionless(), patients.get(0).getId().toUnqualifiedVersionless());
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(Patient.SP_RES_LANGUAGE, new StringParam("en_US"));
			List<Patient> patients = toList(ourPatientDao.search(params));
			assertEquals(1, patients.size());
			assertEquals(id2.toUnqualifiedVersionless(), patients.get(0).getId().toUnqualifiedVersionless());
		}
		{
			Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
			params.put(Patient.SP_RES_LANGUAGE, new StringParam("en_GB"));
			List<Patient> patients = toList(ourPatientDao.search(params));
			assertEquals(0, patients.size());
		}

	}

	@Test
	public void testSearchStringParamWithNonNormalized() {
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			ourPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			ourPatientDao.create(patient);
		}

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_GIVEN, new StringDt("testSearchStringParamWithNonNormalized_hora"));
		List<Patient> patients = toList(ourPatientDao.search(params));
		assertEquals(2, patients.size());

		StringParam parameter = new StringParam("testSearchStringParamWithNonNormalized_hora");
		parameter.setExact(true);
		params.put(Patient.SP_GIVEN, parameter);
		patients = toList(ourPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchTokenParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().addFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
				.setDisplay("testSearchTokenParamDisplay");
		ourPatientDao.create(patient);

		patient = new Patient();
		patient.addIdentifier("urn:system", "testSearchTokenParam002");
		patient.addName().addFamily("Tester").addGiven("testSearchTokenParam2");
		ourPatientDao.create(patient);

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "testSearchTokenParam001"));
			IBundleProvider retrieved = ourPatientDao.search(map);
			assertEquals(1, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new IdentifierDt(null, "testSearchTokenParam001"));
			IBundleProvider retrieved = ourPatientDao.search(map);
			assertEquals(1, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new IdentifierDt("testSearchTokenParamSystem", "testSearchTokenParamCode"));
			assertEquals(1, ourPatientDao.search(map).size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamCode", true));
			assertEquals(0, ourPatientDao.search(map).size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamComText", true));
			assertEquals(1, ourPatientDao.search(map).size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add(new IdentifierDt("urn:system", "testSearchTokenParam001"));
			listParam.add(new IdentifierDt("urn:system", "testSearchTokenParam002"));
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = ourPatientDao.search(map);
			assertEquals(2, retrieved.size());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add(new IdentifierDt(null, "testSearchTokenParam001"));
			listParam.add(new IdentifierDt("urn:system", "testSearchTokenParam002"));
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = ourPatientDao.search(map);
			assertEquals(2, retrieved.size());
		}
	}

	@Test
	public void testSearchWithIncludes() {
		{
			Organization org = new Organization();
			org.getName().setValue("testSearchWithIncludes_O1");
			IdDt orgId = ourOrganizationDao.create(org).getId();

			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("Tester_testSearchWithIncludes_P1").addGiven("Joe");
			patient.getManagingOrganization().setReference(orgId);
			ourPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("Tester_testSearchWithIncludes_P2").addGiven("John");
			ourPatientDao.create(patient);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringDt("Tester_testSearchWithIncludes_P1"));
		params.addInclude(Patient.INCLUDE_MANAGINGORGANIZATION);
		IBundleProvider search = ourPatientDao.search(params);
		List<IResource> patients = toList(search);
		assertEquals(2, patients.size());
		assertEquals(Patient.class, patients.get(0).getClass());
		assertEquals(Organization.class, patients.get(1).getClass());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringDt("Tester_testSearchWithIncludes_P1"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());

	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() throws Exception {
		String name = "測試醫院";
		Organization org = new Organization();
		org.setName(new String(name.getBytes(), "UTF-8"));
		org.addIdentifier("urn:system", "testStoreUtf8Characters_01");
		IdDt orgId = ourOrganizationDao.create(org).getId();

		Organization returned = ourOrganizationDao.read(orgId);
		String val = ourFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);

		ourLog.info(val);
		assertThat(val, containsString("<name value=\"測試醫院\"/>"));
	}

	/**
	 * Test for #62
	 */
	@Test
	public void testSearchWithIncludesThatHaveTextId() {
		{
			Organization org = new Organization();
			org.setId("testSearchWithIncludesThatHaveTextId_id1");
			org.getName().setValue("testSearchWithIncludesThatHaveTextId_O1");
			IdDt orgId = ourOrganizationDao.create(org).getId();
			assertThat(orgId.getValue(), endsWith("Organization/testSearchWithIncludesThatHaveTextId_id1/_history/1"));

			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("Tester_testSearchWithIncludesThatHaveTextId_P1").addGiven("Joe");
			patient.getManagingOrganization().setReference(orgId);
			ourPatientDao.create(patient);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("Tester_testSearchWithIncludesThatHaveTextId_P2").addGiven("John");
			ourPatientDao.create(patient);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringDt("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		params.addInclude(Patient.INCLUDE_MANAGINGORGANIZATION);
		IBundleProvider search = ourPatientDao.search(params);
		List<IResource> patients = toList(search);
		assertEquals(2, patients.size());
		assertEquals(Patient.class, patients.get(0).getClass());
		assertEquals(Organization.class, patients.get(1).getClass());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringDt("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		patients = toList(ourPatientDao.search(params));
		assertEquals(1, patients.size());

	}

	@Test
	public void testSearchWithNoResults() {
		IBundleProvider value = ourDeviceDao.search(new SearchParameterMap());
		for (IResource next : value.getResources(0, value.size())) {
			ourDeviceDao.delete(next.getId());
		}

		value = ourDeviceDao.search(new SearchParameterMap());
		assertEquals(0, value.size());

		List<IResource> res = value.getResources(0, 0);
		assertTrue(res.isEmpty());

	}

	@Test
	public void testSort() {
		Patient p = new Patient();
		p.addIdentifier("urn:system", "testSort001");
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		IdDt id1 = ourPatientDao.create(p).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier("urn:system", "testSort001");
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		IdDt id3 = ourPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier("urn:system", "testSort001");
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		IdDt id2 = ourPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier("urn:system", "testSort001");
		IdDt id4 = ourPatientDao.create(p).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testSort001"));
		pm.setSort(new SortSpec(Patient.SP_FAMILY));
		List<IdDt> actual = toUnqualifiedVersionlessIds(ourPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));
	}

	@Test
	public void testStoreUnversionedResources() {
		Organization o1 = new Organization();
		o1.getName().setValue("AAA");
		IdDt o1id = ourOrganizationDao.create(o1).getId();
		assertTrue(o1id.hasVersionIdPart());

		Patient p1 = new Patient();
		p1.addName().addFamily("AAAA");
		p1.getManagingOrganization().setReference(o1id);
		IdDt p1id = ourPatientDao.create(p1).getId();

		p1 = ourPatientDao.read(p1id);

		assertFalse(p1.getManagingOrganization().getReference().hasVersionIdPart());
		assertEquals(o1id.toUnqualifiedVersionless(), p1.getManagingOrganization().getReference().toUnqualifiedVersionless());
	}

	@Test
	public void testStringParamWhichIsTooLong() {

		Organization org = new Organization();
		String str = "testStringParamLong__lvdaoy843s89tll8gvs89l4s3gelrukveilufyebrew8r87bv4b77feli7fsl4lv3vb7rexloxe7olb48vov4o78ls7bvo7vb48o48l4bb7vbvx";
		str = str + str;
		org.getName().setValue(str);

		assertThat(str.length(), greaterThan(ResourceIndexedSearchParamString.MAX_LENGTH));

		Set<Long> val = ourOrganizationDao.searchForIds("name", new StringParam("P"));
		int initial = val.size();

		ourOrganizationDao.create(org);

		val = ourOrganizationDao.searchForIds("name", new StringParam("P"));
		assertEquals(initial + 0, val.size());

		val = ourOrganizationDao.searchForIds("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH)));
		assertEquals(initial + 1, val.size());

		try {
			ourOrganizationDao.searchForIds("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH + 1)));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}
	}

	@Test
	public void testTagsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList = new TagList();
		tagList.addTag(null, "Dog", "Puppies");
		// Add this twice
		tagList.addTag("http://foo", "Cat", "Kittens");
		tagList.addTag("http://foo", "Cat", "Kittens");
		patient.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);

		MethodOutcome outcome = ourPatientDao.create(patient);
		IdDt patientId = outcome.getId();
		assertNotNull(patientId);
		assertFalse(patientId.isEmpty());

		Patient retrieved = ourPatientDao.read(patientId);
		TagList published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals(2, published.size());
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());

		List<Patient> search = toList(ourPatientDao.search(Patient.SP_IDENTIFIER, patient.getIdentifierFirstRep()));
		assertEquals(1, search.size());
		retrieved = search.get(0);
		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());

		ourPatientDao.addTag(patientId, "http://foo", "Cat", "Kittens");
		ourPatientDao.addTag(patientId, "http://foo", "Cow", "Calves");

		retrieved = ourPatientDao.read(patientId);
		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals(3, published.size());
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());
		assertEquals("Cow", published.get(2).getTerm());
		assertEquals("Calves", published.get(2).getLabel());
		assertEquals("http://foo", published.get(2).getScheme());

	}

	@Test
	public void testTokenParamWhichIsTooLong() {

		String longStr1 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamString.MAX_LENGTH + 100);
		String longStr2 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamString.MAX_LENGTH + 100);

		Organization org = new Organization();
		org.getName().setValue("testTokenParamWhichIsTooLong");
		org.getType().addCoding().setSystem(longStr1).setCode(longStr2);

		String subStr1 = longStr1.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		String subStr2 = longStr2.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		Set<Long> val = ourOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, subStr2));
		int initial = val.size();

		ourOrganizationDao.create(org);

		val = ourOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, subStr2));
		assertEquals(initial + 1, val.size());

		try {
			ourOrganizationDao.searchForIds("type", new IdentifierDt(longStr1, subStr2));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}

		try {
			ourOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, longStr2));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}
	}

	@Test
	public void testUpdateAndGetHistoryResource() throws InterruptedException {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = ourPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		assertEquals("1", outcome.getId().getVersionIdPart());

		Date now = new Date();
		Patient retrieved = ourPatientDao.read(outcome.getId());
		InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published.before(now));
		assertTrue(updated.before(now));

		Thread.sleep(1000);

		retrieved.getIdentifierFirstRep().setValue("002");
		MethodOutcome outcome2 = ourPatientDao.update(retrieved, outcome.getId());
		assertEquals(outcome.getId().getIdPart(), outcome2.getId().getIdPart());
		assertNotEquals(outcome.getId().getVersionIdPart(), outcome2.getId().getVersionIdPart());

		assertEquals("2", outcome2.getId().getVersionIdPart());

		Date now2 = new Date();

		Patient retrieved2 = ourPatientDao.read(outcome.getId().toVersionless());

		assertEquals("2", retrieved2.getId().getVersionIdPart());
		assertEquals("002", retrieved2.getIdentifierFirstRep().getValue().getValue());
		InstantDt published2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published2.before(now));
		assertTrue(updated2.after(now));
		assertTrue(updated2.before(now2));

		Thread.sleep(2000);

		/*
		 * Get history
		 */

		IBundleProvider historyBundle = ourPatientDao.history(outcome.getId(), null);

		assertEquals(2, historyBundle.size());

		List<IResource> history = historyBundle.getResources(0, 2);
		assertEquals("1", history.get(1).getId().getVersionIdPart());
		assertEquals("2", history.get(0).getId().getVersionIdPart());
		assertEquals(published, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(published, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("001", ((Patient) history.get(1)).getIdentifierFirstRep().getValue().getValue());
		assertEquals(published2, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated2, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("002", ((Patient) history.get(0)).getIdentifierFirstRep().getValue().getValue());

	}

	@Test
	public void testUpdateMaintainsSearchParams() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier("urn:system", "testUpdateMaintainsSearchParamsAAA");
		p1.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsAAA");
		IdDt p1id = ourPatientDao.create(p1).getId();

		Patient p2 = new Patient();
		p2.addIdentifier("urn:system", "testUpdateMaintainsSearchParamsBBB");
		p2.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsBBB");
		ourPatientDao.create(p2).getId();

		Set<Long> ids = ourPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsAAA"));
		assertEquals(1, ids.size());
		assertThat(ids, contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getNameFirstRep().getGivenFirstRep().setValue("testUpdateMaintainsSearchParamsBBB");
		MethodOutcome update2 = ourPatientDao.update(p1, p1id);
		IdDt p1id2 = update2.getId();

		ids = ourPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsAAA"));
		assertEquals(0, ids.size());

		ids = ourPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsBBB"));
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = ourPatientDao.read(p1id);
		assertEquals("testUpdateMaintainsSearchParamsAAA", p1.getNameFirstRep().getGivenAsSingleString());

		p1 = ourPatientDao.read(p1id2);
		assertEquals("testUpdateMaintainsSearchParamsBBB", p1.getNameFirstRep().getGivenAsSingleString());

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier("urn:system", "testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdDt p1id = ourPatientDao.create(p1).getId();

		Organization p2 = new Organization();
		p2.getName().setValue("testUpdateRejectsInvalidTypes");
		try {
			ourOrganizationDao.update(p2, new IdDt("Organization/" + p1id.getIdPart()));
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			ourOrganizationDao.update(p2, new IdDt("Patient/" + p1id.getIdPart()));
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

	}

	@Test
	public void testUpdateRejectsIdWhichPointsToForcedId() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier("urn:system", "testUpdateRejectsIdWhichPointsToForcedId01");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId01");
		p1.setId("ABABA");
		IdDt p1id = ourPatientDao.create(p1).getId();
		assertEquals("ABABA", p1id.getIdPart());

		Patient p2 = new Patient();
		p2.addIdentifier("urn:system", "testUpdateRejectsIdWhichPointsToForcedId02");
		p2.addName().addFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId02");
		IdDt p2id = ourPatientDao.create(p2).getId();
		long p1longId = p2id.getIdPartAsLong() - 1;

		try {
			ourPatientDao.read(new IdDt("Patient/" + p1longId));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		try {
			ourPatientDao.update(p1, new IdDt("Patient/" + p1longId));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}

	@Test
	public void testReadForcedIdVersionHistory() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier("urn:system", "testReadVorcedIdVersionHistory01");
		p1.setId("testReadVorcedIdVersionHistory");
		IdDt p1id = ourPatientDao.create(p1).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1id.getIdPart());

		p1.addIdentifier("urn:system", "testReadVorcedIdVersionHistory02");
		IdDt p1idv2 = ourPatientDao.update(p1, p1id).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1idv2.getIdPart());

		assertNotEquals(p1id.getValue(), p1idv2.getValue());

		Patient v1 = ourPatientDao.read(p1id);
		assertEquals(1, v1.getIdentifier().size());

		Patient v2 = ourPatientDao.read(p1idv2);
		assertEquals(2, v2.getIdentifier().size());

	}

	@SuppressWarnings("unchecked")
	private <T extends IResource> List<T> toList(IBundleProvider theSearch) {
		return (List<T>) theSearch.getResources(0, theSearch.size());
	}

	private List<IdDt> toUnqualifiedVersionlessIds(IBundleProvider theFound) {
		List<IdDt> retVal = new ArrayList<IdDt>();
		for (IResource next : theFound.getResources(0, theFound.size())) {
			retVal.add(next.getId().toUnqualifiedVersionless());
		}
		return retVal;
	}

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourCtx = new ClassPathXmlApplicationContext("fhir-jpabase-spring-test-config.xml");
		ourPatientDao = ourCtx.getBean("myPatientDao", IFhirResourceDao.class);
		ourObservationDao = ourCtx.getBean("myObservationDao", IFhirResourceDao.class);
		ourDiagnosticReportDao = ourCtx.getBean("myDiagnosticReportDao", IFhirResourceDao.class);
		ourDeviceDao = ourCtx.getBean("myDeviceDao", IFhirResourceDao.class);
		ourOrganizationDao = ourCtx.getBean("myOrganizationDao", IFhirResourceDao.class);
		ourLocationDao = ourCtx.getBean("myLocationDao", IFhirResourceDao.class);
		ourEncounterDao = ourCtx.getBean("myEncounterDao", IFhirResourceDao.class);
		ourFhirCtx = new FhirContext();
	}

}
