package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Device;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class FhirSystemDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoTest.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirResourceDao<Device> ourDeviceDao;
	private static IFhirResourceDao<DiagnosticReport> ourDiagnosticReportDao;
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static Date ourTestStarted;
	private static IFhirSystemDao ourSystemDao;

	@Test
	public void testHistory() throws Exception {
		Date start = new Date();
		Thread.sleep(10);

		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testHistory");
		patient.addName().addFamily("Tester").addGiven("Joe");
		IdDt pid = ourPatientDao.create(patient).getId().toVersionless();

		Thread.sleep(10);
		IdDt newpid = ourPatientDao.update(patient, pid).getId();

		Thread.sleep(10);
		IdDt newpid2 = ourPatientDao.update(patient, pid).getId();

		Thread.sleep(10);
		IdDt newpid3 = ourPatientDao.update(patient, pid).getId();

		IBundleProvider values = ourSystemDao.history(start);
		assertEquals(4, values.size());

		List<IResource> res = values.getResources(0, 4);
		assertEquals(newpid3, res.get(0).getId());
		assertEquals(newpid2, res.get(1).getId());
		assertEquals(newpid, res.get(2).getId());
		assertEquals(pid.toUnqualifiedVersionless(), res.get(3).getId().toUnqualifiedVersionless());

		Location loc = new Location();
		loc.getAddress().addLine("AAA");
		IdDt lid = ourLocationDao.create(loc).getId();

		Location loc2 = new Location();
		loc2.getAddress().addLine("AAA");
		ourLocationDao.create(loc2).getId();

		Thread.sleep(2000);
		
		values = ourLocationDao.history(start);
		assertEquals(2, values.size());

		values = ourLocationDao.history(lid.getIdPartAsLong(), start);
		assertEquals(1, values.size());

	}

	@Test
	public void testTagOperationss() throws Exception {

		TagList preSystemTl = ourSystemDao.getAllTags();

		TagList tl1 = new TagList();
		tl1.addTag("testGetAllTagsScheme1", "testGetAllTagsTerm1", "testGetAllTagsLabel1");
		Patient p1 = new Patient();
		p1.addIdentifier("foo", "testGetAllTags01");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, tl1);
		ourPatientDao.create(p1);

		TagList tl2 = new TagList();
		tl2.addTag("testGetAllTagsScheme2", "testGetAllTagsTerm2", "testGetAllTagsLabel2");
		Observation o1 = new Observation();
		o1.getName().setText("testGetAllTags02");
		ResourceMetadataKeyEnum.TAG_LIST.put(o1, tl2);
		IdDt o1id = ourObservationDao.create(o1).getId();
		assertTrue(o1id.getVersionIdPart() != null);

		TagList postSystemTl = ourSystemDao.getAllTags();
		assertEquals(preSystemTl.size() + 2, postSystemTl.size());
		assertEquals("testGetAllTagsLabel1", postSystemTl.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1").getLabel());

		TagList tags = ourPatientDao.getAllResourceTags();
		assertEquals("testGetAllTagsLabel1", tags.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1").getLabel());
		assertNull(tags.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

		TagList tags2 = ourObservationDao.getTags(o1id);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertEquals("testGetAllTagsLabel2", tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2").getLabel());

		o1.getResourceMetadata().remove(ResourceMetadataKeyEnum.TAG_LIST);
		IdDt o1id2 = ourObservationDao.update(o1, o1id).getId();
		assertTrue(o1id2.getVersionIdPart() != null);

		tags2 = ourObservationDao.getTags(o1id);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertEquals("testGetAllTagsLabel2", tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2").getLabel());

		tags2 = ourObservationDao.getTags(o1id2);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertNotNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

		/*
		 * Remove a tag from a version
		 */

		ourObservationDao.removeTag(o1id2, "testGetAllTagsScheme2", "testGetAllTagsTerm2");
		tags2 = ourObservationDao.getTags(o1id2);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

		tags2 = ourObservationDao.getTags(o1id);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertNotNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

		/*
		 * Add a tag
		 */
		ourObservationDao.addTag(o1id2, "testGetAllTagsScheme3", "testGetAllTagsTerm3", "testGetAllTagsLabel3");
		tags2 = ourObservationDao.getTags(o1id2);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));
		assertNotNull(tags2.getTag("testGetAllTagsScheme3", "testGetAllTagsTerm3"));
		assertEquals("testGetAllTagsLabel3", tags2.getTag("testGetAllTagsScheme3", "testGetAllTagsTerm3").getLabel());

		tags2 = ourObservationDao.getTags(o1id);
		assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
		assertNotNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

	}

	@Test
	public void testTransactionWithUpdate() throws Exception {
		List<IResource> res = new ArrayList<IResource>();

		Patient p1 = new Patient();
		p1.getId().setValue("testTransactionWithUpdateXXX01");
		p1.addIdentifier("system", "testTransactionWithUpdate01");
		res.add(p1);

		Observation p2 = new Observation();
		p2.getId().setValue("testTransactionWithUpdateXXX02");
		p2.getIdentifier().setSystem("system").setValue("testTransactionWithUpdate02");
		p2.setSubject(new ResourceReferenceDt("Patient/testTransactionWithUpdateXXX01"));
		res.add(p2);

		ourSystemDao.transaction(res);

		assertFalse(p1.getId().isEmpty());
		assertNotEquals("testTransactionWithUpdateXXX01", p1.getId().getVersionIdPart());
		assertFalse(p2.getId().isEmpty());
		assertNotEquals("testTransactionWithUpdateXXX02", p2.getId().getVersionIdPart());
		assertEquals(p1.getId().toUnqualified().toVersionless(), p2.getSubject().getReference());

		IdDt p1id = p1.getId().toUnqualified().toVersionless();
		IdDt p1idWithVer = p1.getId().toUnqualified();
		IdDt p2id = p2.getId().toUnqualified().toVersionless();
		IdDt p2idWithVer = p2.getId().toUnqualified();

		p1.addName().addFamily("Name1");
		p1.setId(p1.getId().toUnqualified().toVersionless());

		p2.addReferenceRange().setHigh(123L);
		p2.setId(p2.getId().toUnqualified().toVersionless());

		ourSystemDao.transaction(res);

		assertEquals(p1id, p1.getId().toUnqualified().toVersionless());
		assertEquals(p2id, p2.getId().toUnqualified().toVersionless());
		assertNotEquals(p1idWithVer, p1.getId().toUnqualified());
		assertNotEquals(p2idWithVer, p2.getId().toUnqualified());

	}

	/**
	 * Issue #55
	 */
	@Test
	public void testTransactionWithCidIds() throws Exception {
		List<IResource> res = new ArrayList<IResource>();

		Patient p1 = new Patient();
		p1.setId("cid:patient1");
		p1.addIdentifier("system", "testTransactionWithCidIds01");
		res.add(p1);

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds02");
		o1.setSubject(new ResourceReferenceDt("Patient/cid:patient1"));
		res.add(o1);

		Observation o2 = new Observation();
		o2.setId("cid:observation1");
		o2.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds03");
		o2.setSubject(new ResourceReferenceDt("Patient/cid:patient1"));
		res.add(o2);

		ourSystemDao.transaction(res);

		assertTrue(p1.getId().getValue(), p1.getId().getIdPart().matches("^[0-9]+$"));
		assertTrue(o1.getId().getValue(), o1.getId().getIdPart().matches("^[0-9]+$"));
		assertTrue(o2.getId().getValue(), o2.getId().getIdPart().matches("^[0-9]+$"));
		
		assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
		assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
		
	}
	
	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = FhirSystemDaoTest.class.getResourceAsStream("/bundle.json");
		Bundle bundle = new FhirContext().newJsonParser().parseBundle(new InputStreamReader(bundleRes));
		List<IResource> res = bundle.toListOfResources();

		ourSystemDao.transaction(res);

		Patient p1 = (Patient) res.get(0);
		String id = p1.getId().getValue();
		ourLog.info("ID: {}", id);
		assertThat(id, not(containsString("5556918")));
		assertThat(id, not(equalToIgnoringCase("")));
	}

	@Test
	public void testPersistWithSimpleLink() {
		Patient patient = new Patient();
		patient.setId(new IdDt("Patient/testPersistWithSimpleLinkP01"));
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP01");
		patient.addName().addFamily("Tester").addGiven("Joe");

		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/testPersistWithSimpleLinkP01"));

		ourSystemDao.transaction(Arrays.asList((IResource) patient, obs));

		String patientId = (patient.getId().getIdPart());
		String patientVersion = (patient.getId().getVersionIdPart());
		String obsId = (obs.getId().getIdPart());
		String obsVersion = (obs.getId().getVersionIdPart());

//		assertThat(patientId, greaterThan(0L));
//		assertEquals(patientVersion, 1L);
//		assertThat(obsId, greaterThan(patientId));
//		assertEquals(obsVersion, 1L);

		// Try to search

		IBundleProvider obsResults = ourObservationDao.search(Observation.SP_NAME, new IdentifierDt("urn:system", "testPersistWithSimpleLinkO01"));
		assertEquals(1, obsResults.size());

		IBundleProvider patResults = ourPatientDao.search(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "testPersistWithSimpleLinkP01"));
		assertEquals(1, obsResults.size());

		IdDt foundPatientId = patResults.getResources(0, 1).get(0).getId();
		ResourceReferenceDt subject = obs.getSubject();
		assertEquals(foundPatientId.getIdPart(), subject.getReference().getIdPart());

		// Update

		patient = (Patient) patResults.getResources(0,1).get(0);
		obs = (Observation) obsResults.getResources(0, 1).get(0);
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP02");
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO02");

		ourSystemDao.transaction(Arrays.asList((IResource) patient, obs));

		String patientId2 = (patient.getId().getIdPart());
		String patientVersion2 = (patient.getId().getVersionIdPart());
		String obsId2 = (obs.getId().getIdPart());
		String obsVersion2 = (obs.getId().getVersionIdPart());

		assertEquals(patientId, patientId2);
		assertEquals(patientVersion2, "2");
		assertEquals(obsId, obsId2);
		assertEquals(obsVersion2, "2");

	}

	@Test
	public void testGetResourceCounts() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testGetResourceCountsO01");
		ourObservationDao.create(obs);

		Map<String, Long> oldCounts = ourSystemDao.getResourceCounts();

		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testGetResourceCountsP01");
		patient.addName().addFamily("Tester").addGiven("Joe");
		ourPatientDao.create(patient);

		Map<String, Long> newCounts = ourSystemDao.getResourceCounts();

		if (oldCounts.containsKey("Patient")) {
			assertEquals(oldCounts.get("Patient") + 1, (long) newCounts.get("Patient"));
		} else {
			assertEquals(1L, (long) newCounts.get("Patient"));
		}

		assertEquals((long) oldCounts.get("Observation"), (long) newCounts.get("Observation"));

	}

	@Test
	public void testPersistWithUnknownId() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/999998888888"));

		try {
			ourSystemDao.transaction(Arrays.asList((IResource) obs));
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Patient/999998888888 not found, specified in path: Observation.subject"));
		}

		obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/1.2.3.4"));

		try {
			ourSystemDao.transaction(Arrays.asList((IResource) obs));
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Patient/1.2.3.4 not found, specified in path: Observation.subject"));
		}

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
		ourSystemDao = ourCtx.getBean("mySystemDao", IFhirSystemDao.class);
	}

}
