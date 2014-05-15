package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;

public class FhirResourceDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static Date ourTestStarted;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourTestStarted = new Date();
		ourCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");
		ourPatientDao = ourCtx.getBean("myPatientDao", IFhirResourceDao.class);
		ourObservationDao = ourCtx.getBean("myObservationDao", IFhirResourceDao.class);
	}

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

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
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoTest.class);
	
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
		obs01.setSubject(new ResourceReferenceDt(Patient.class, patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();
		
		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(Patient.class, patientId02));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}]",new Object[] {patientId01,patientId02,obsId01,obsId02});
		
		List<Observation> result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getValue()));
		assertEquals(1,result.size());
		assertEquals(obsId01,result.get(0).getId());
		
		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getValue()));
		assertEquals(1,result.size());
		assertEquals(obsId02,result.get(0).getId());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999"));
		assertEquals(0,result.size());
	
	}
	
	@Test
	public void testSearchResourceLinkWithChain() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testSearchResourceLinkWithChain");
		IdDt patientId01 = ourPatientDao.create(patient).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier("urn:system", "testSearchResourceLinkWithChain");
		IdDt patientId02 = ourPatientDao.create(patient02).getId();
	
		Observation obs01 = new Observation();
		obs01.setApplies(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(Patient.class, patientId01));
		IdDt obsId01 = ourObservationDao.create(obs01).getId();
		
		Observation obs02 = new Observation();
		obs02.setApplies(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(Patient.class, patientId02));
		IdDt obsId02 = ourObservationDao.create(obs02).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}]",new Object[] {patientId01,patientId02,obsId01,obsId02});
		
		List<Observation> result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getValue()));
		assertEquals(1,result.size());
		assertEquals(obsId01,result.get(0).getId());
		
		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getValue(), Patient.SP_IDENTIFIER));
		assertEquals(1,result.size());
		assertEquals(obsId02,result.get(0).getId());

		result = ourObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999", Patient.SP_IDENTIFIER));
		assertEquals(0,result.size());
	
	}
	
	@Test
	public void testPersistSearchParamObservationString() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new StringDt("AAAABBBB"));
		
		ourObservationDao.create(obs);
		
		List<Observation> found = ourObservationDao.search("value-string", new StringDt("AAAABBBB"));
		assertEquals(1,found.size());
	
		found = ourObservationDao.search("value-string", new StringDt("AAAABBBBCCC"));
		assertEquals(0,found.size());
		
	}
	
	
	@Test
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new QuantityDt(111));
		
		ourObservationDao.create(obs);
		
		List<Observation> found = ourObservationDao.search("value-quantity", new QuantityDt(111));
		assertEquals(1,found.size());
	
		found = ourObservationDao.search("value-quantity", new QuantityDt(112));
		assertEquals(0,found.size());
		
	}

	@Test
	public void testPersistSearchParamDate() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "001");
		patient.setBirthDate(new DateTimeDt("2001-01-01"));
		
		ourPatientDao.create(patient);
		
		List<Patient> found = ourPatientDao.search("birthdate", new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01"));
		assertEquals(1,found.size());
	
		
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

		found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "M"));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().asLong().longValue());

		found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "F"));
		assertEquals(0, found.size());

		Map<String, List<List<IQueryParameterType>>> map = new HashMap<>();
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(null, "M"));
		found = ourPatientDao.searchWithAndOr(map);
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().asLong().longValue());

		map = new HashMap<>();
		map.put(Patient.SP_IDENTIFIER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_IDENTIFIER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_IDENTIFIER).get(0).add(new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.put(Patient.SP_GENDER, new ArrayList<List<IQueryParameterType>>());
		map.get(Patient.SP_GENDER).add(new ArrayList<IQueryParameterType>());
		map.get(Patient.SP_GENDER).get(0).add(new IdentifierDt(null, "F"));
		found = ourPatientDao.searchWithAndOr(map);
		assertEquals(0, found.size());

	}

	@Test
	public void testTagsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList = new TagList();
		tagList.addTag("Dog", "Puppies", null);
		tagList.addTag("Cat", "Kittens", "http://foo");
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

		Map<String, IQueryParameterType> params = new HashMap<>();
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(2, patients.size());
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

		Map<String, IQueryParameterType> params = new HashMap<>();
		params.put(Patient.SP_FAMILY, new StringDt("Tester_testSearchStringParam"));
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(2, patients.size());

		params.put(Patient.SP_FAMILY, new StringDt("FOO_testSearchStringParam"));
		patients = ourPatientDao.search(params);
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchNameParam() {
		IdDt id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "001");
			patient.addName().addFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");
			id1=ourPatientDao.create(patient).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier("urn:system", "002");
			patient.addName().addFamily("testSearchNameParam02Fam").addGiven("testSearchNameParam02Giv");
			ourPatientDao.create(patient);
		}

		Map<String, IQueryParameterType> params = new HashMap<>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Fam"));
		List<Patient> patients = ourPatientDao.search(params);
		assertEquals(1, patients.size());
		assertEquals(id1, patients.get(0).getId());

		params = new HashMap<>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Giv"));
		patients = ourPatientDao.search(params);
		assertEquals(1, patients.size());
		assertEquals(id1, patients.get(0).getId());

		params = new HashMap<>();
		params.put(Patient.SP_FAMILY, new StringDt("testSearchNameParam01Foo"));
		patients = ourPatientDao.search(params);
		assertEquals(0, patients.size());

	}
	
	@Test
	public void testUpdateAndGetHistoryResource() throws InterruptedException {
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

		Thread.sleep(1000);

		retrieved.getIdentifierFirstRep().setValue("002");
		MethodOutcome outcome2 = ourPatientDao.update(retrieved, outcome.getId());
		assertEquals(outcome.getId(), outcome2.getId());
		assertNotEquals(outcome.getVersionId(), outcome2.getVersionId());

		Date now2 = new Date();

		Patient retrieved2 = ourPatientDao.read(outcome.getId());
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
		assertEquals(published, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated, history.get(0).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("001", history.get(0).getIdentifierFirstRep().getValue().getValue());
		assertEquals(published2, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED));
		assertEquals(updated2, history.get(1).getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("002", history.get(1).getIdentifierFirstRep().getValue().getValue());

	}

}
