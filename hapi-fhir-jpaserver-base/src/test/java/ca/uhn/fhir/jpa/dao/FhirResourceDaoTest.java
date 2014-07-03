package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
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
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;

public class FhirResourceDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoTest.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static Date ourTestStarted;

	private static IFhirResourceDao<Encounter> ourEncounterDao;
	private static FhirContext ourFhirCtx;

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

		Organization org = ourFhirCtx.newJsonParser().parseResource(Organization.class,inputStr);
		ourOrganizationDao.create(org);
		
		val = ourOrganizationDao.searchForIds("name", new StringParam("P"));
		assertEquals(initial+1, val.size());
		
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

		List<Patient> found = toList(ourPatientDao.search(Patient.SP_BIRTHDATE, new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		assertEquals(1, found.size());

		// If this throws an exception, that would be an acceptable outcome as well..
		found = toList(ourPatientDao.search(Patient.SP_BIRTHDATE + "AAAA", new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
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
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new QuantityDt(111));

		ourObservationDao.create(obs);

		List<Observation> found = toList(ourObservationDao.search("value-quantity", new QuantityDt(111)));
		assertEquals(1, found.size());

		found = toList(ourObservationDao.search("value-quantity", new QuantityDt(112)));
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
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(AdministrativeGenderCodesEnum.M.getSystem(), "M"));
		found = toList(ourPatientDao.search(map));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().getIdPartAsLong().longValue());

		map = new SearchParameterMap();
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(AdministrativeGenderCodesEnum.M.getSystem(), "F"));
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
	public void testSearchNameParam() {
		IdDt id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");
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
	public void testDatePeriodParamEndOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier("testDatePeriodParam", "02");
			enc.getPeriod().getEnd().setValueAsString("2001-01-02");
			ourEncounterDao.create(enc);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		List<Encounter> encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(ourEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
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

	@SuppressWarnings("unchecked")
	private <T extends IResource> List<T> toList(IBundleProvider theSearch) {
		return (List<T>) theSearch.getResources(0, theSearch.size());
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
	public void testSearchStringParamWithNonNormalized() {
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_höra");
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
	public void testTagsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList = new TagList();
		tagList.addTag(null, "Dog", "Puppies");
		tagList.addTag("http://foo", "Cat", "Kittens");
		patient.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);

		MethodOutcome outcome = ourPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Patient retrieved = ourPatientDao.read(outcome.getId());
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
		assertNotEquals(outcome.getVersionId(), outcome2.getVersionId());

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
		IdDt p2id = ourPatientDao.create(p2).getId();

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

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourTestStarted = new Date();
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
