package ca.uhn.fhir.jpa.dao;

import static org.junit.Assert.*;

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
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

public class FhirResourceDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static Date ourTestStarted;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourTestStarted = new Date();
		ourCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");
		ourPatientDao = ourCtx.getBean("myPatientDao", IFhirResourceDao.class);
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

	
	@Test
	public void testTagsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList= new TagList();
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
		assertEquals(1,search.size());
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
