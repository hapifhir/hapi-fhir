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

import ca.uhn.fhir.model.api.IQueryParameterType;
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
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

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

	@Test
	public void testPersistAndReadResource() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = ourPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Date now = new Date();

		Patient retrieved = ourPatientDao.read(outcome.getId());
		InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published.before(now));
		assertTrue(updated.before(now));
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

		List<Observation> result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getUnqualifiedId()));
		assertEquals(1, result.size());
		assertEquals(obsId01.getUnqualifiedId(), result.get(0).getId().getUnqualifiedId());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getUnqualifiedId()));
		assertEquals(1, result.size());
		assertEquals(obsId02.getUnqualifiedId(), result.get(0).getId().getUnqualifiedId());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999"));
		assertEquals(0, result.size());

	}

	@Test
	public void testPersistSearchParamDate() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.setBirthDate(new DateTimeDt("2001-01-01"));

		ourPatientDao.create(patient);

		List<Patient> found = ourPatientDao.search("birthdate", new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01"));
		assertEquals(1, found.size());

	}

	@Test
	public void testPersistSearchParamObservationString() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new StringDt("AAAABBBB"));

		ourObservationDao.create(obs);

		List<Observation> found = ourObservationDao.search("value-string", new StringDt("AAAABBBB"));
		assertEquals(1, found.size());

		found = ourObservationDao.search("value-string", new StringDt("AAAABBBBCCC"));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new QuantityDt(111));

		ourObservationDao.create(obs);

		List<Observation> found = ourObservationDao.search("value-quantity", new QuantityDt(111));
		assertEquals(1, found.size());

		found = ourObservationDao.search("value-quantity", new QuantityDt(112));
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

		long id = outcome.getId().asLong();

		IdentifierDt value = new IdentifierDt("urn:system", "001testPersistSearchParams");
		List<Patient> found = ourPatientDao.search(Patient.SP_IDENTIFIER, value);
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().asLong().longValue());

//		found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "M"));
//		assertEquals(1, found.size());
//		assertEquals(id, found.get(0).getId().asLong().longValue());
//
//		found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "F"));
//		assertEquals(0, found.size());

		SearchParameterMap map = new SearchParameterMap();
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(null, "M"));
		found = ourPatientDao.search(map);
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().asLong().longValue());

		map = new SearchParameterMap();
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(null, "F"));
		found = ourPatientDao.search(map);
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
		List<Patient> patients = ourPatientDao.search(params);
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
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(1, patients.size());
		assertEquals(id1.getUnqualifiedId(), patients.get(0).getId().getUnqualifiedId());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Giv"));
		patients = ourPatientDao.search(params);
		assertEquals(1, patients.size());
		assertEquals(id1.getUnqualifiedId(), patients.get(0).getId().getUnqualifiedId());

		params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Foo"));
		patients = ourPatientDao.search(params);
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

		List<Observation> result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "testSearchResourceLinkWithChain01"));
		assertEquals(1, result.size());
		assertEquals(obsId01.getUnqualifiedId(), result.get(0).getId().getUnqualifiedId());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "999999999999"));
		assertEquals(0, result.size());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "testSearchResourceLinkWithChainXX"));
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

		List<Observation> result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01"));
		assertEquals(2, result.size());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesXX"));
		assertEquals(1, result.size());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesYY"));
		assertEquals(0, result.size());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("Patient", Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01"));
		assertEquals(1, result.size());
		assertEquals(obsId01.getUnqualifiedId(), result.get(0).getId().getUnqualifiedId());

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
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(2, patients.size());

		params.put(Patient.SP_FAMILY, new StringDt("FOO_testSearchStringParam"));
		patients = ourPatientDao.search(params);
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
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(2, patients.size());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringDt("Tester_testSearchWithIncludes_P1"));
		patients = ourPatientDao.search(params);
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
		List<Encounter> encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = ourEncounterDao.search(params);
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-03",null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = ourEncounterDao.search(params);
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
		List<Encounter> encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = ourEncounterDao.search(params);
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam( "2001-01-03",null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = ourEncounterDao.search(params);
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
		List<Encounter> encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		 params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-02", "2001-01-06"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		 encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-05"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = ourEncounterDao.search(params);
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = ourEncounterDao.search(params);
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam( "2001-01-05",null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = ourEncounterDao.search(params);
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
		params.put(Patient.SP_FAMILY, new StringDt("testSearchStringParamWithNonNormalized_hora"));
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(2, patients.size());

		StringParam parameter = new StringParam("testSearchStringParamWithNonNormalized_hora");
		parameter.setExact(true);
		params.put(Patient.SP_FAMILY, parameter);
		patients = ourPatientDao.search(params);
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

		List<Patient> search = ourPatientDao.search(Patient.SP_IDENTIFIER, patient.getIdentifierFirstRep());
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

		assertEquals("1", outcome.getId().getUnqualifiedVersionId());
		
		Date now = new Date();
		Patient retrieved = ourPatientDao.read(outcome.getId());
		InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published.before(now));
		assertTrue(updated.before(now));

		Thread.sleep(1000);

		retrieved.getIdentifierFirstRep().setValue("002");
		MethodOutcome outcome2 = ourPatientDao.update(retrieved, outcome.getId());
		assertEquals(outcome.getId().getUnqualifiedId(), outcome2.getId().getUnqualifiedId());
		assertNotEquals(outcome.getId().getUnqualifiedVersionId(), outcome2.getId().getUnqualifiedVersionId());
		assertNotEquals(outcome.getVersionId(), outcome2.getVersionId());

		assertEquals("2", outcome2.getId().getUnqualifiedVersionId());

		Date now2 = new Date();

		Patient retrieved2 = ourPatientDao.read(outcome.getId().withoutVersion());

		assertEquals("2", retrieved2.getId().getUnqualifiedVersionId());
		assertEquals("002", retrieved2.getIdentifierFirstRep().getValue().getValue());
		InstantDt published2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published2.before(now));
		assertTrue(updated2.after(now));
		assertTrue(updated2.before(now2));

		/*
		 * Get history
		 */

		List<Patient> history = ourPatientDao.history(outcome.getId());

		assertEquals(2, history.size());
		assertEquals("1", history.get(0).getId().getUnqualifiedVersionId());
		assertEquals("2", history.get(1).getId().getUnqualifiedVersionId());
		assertEquals(published, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(published, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("001", history.get(0).getIdentifierFirstRep().getValue().getValue());
		assertEquals(published2, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated2, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("002", history.get(1).getIdentifierFirstRep().getValue().getValue());

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
		assertThat(ids, contains(p1id.asLong()));

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
	}

}
