package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionOperationEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class FhirSystemDaoTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static FhirContext ourFhirContext;
	private static IFhirResourceDao<Location> ourLocationDao;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoTest.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirSystemDao ourSystemDao;

	@Test
	public void testGetResourceCounts() {
		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testGetResourceCountsO01");
		ourObservationDao.create(obs);

		Map<String, Long> oldCounts = ourSystemDao.getResourceCounts();

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testGetResourceCountsP01");
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
	public void testHistory() throws Exception {
		Date start = new Date();
		Thread.sleep(10);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testHistory");
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
	public void testPersistWithSimpleLink() {
		Patient patient = new Patient();
		patient.setId(new IdDt("Patient/testPersistWithSimpleLinkP01"));
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
		patient.addName().addFamily("Tester").addGiven("Joe");

		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/testPersistWithSimpleLinkP01"));

		ourSystemDao.transaction(Arrays.asList((IResource) patient, obs));

		String patientId = (patient.getId().getIdPart());
		String obsId = (obs.getId().getIdPart());

		// assertThat(patientId, greaterThan(0L));
		// assertEquals(patientVersion, 1L);
		// assertThat(obsId, greaterThan(patientId));
		// assertEquals(obsVersion, 1L);

		// Try to search

		IBundleProvider obsResults = ourObservationDao.search(Observation.SP_NAME, new IdentifierDt("urn:system", "testPersistWithSimpleLinkO01"));
		assertEquals(1, obsResults.size());

		IBundleProvider patResults = ourPatientDao.search(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "testPersistWithSimpleLinkP01"));
		assertEquals(1, obsResults.size());

		IdDt foundPatientId = patResults.getResources(0, 1).get(0).getId();
		ResourceReferenceDt subject = obs.getSubject();
		assertEquals(foundPatientId.getIdPart(), subject.getReference().getIdPart());

		// Update

		patient = (Patient) patResults.getResources(0, 1).get(0);
		obs = (Observation) obsResults.getResources(0, 1).get(0);
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
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

	@Test
	public void testTagOperationss() throws Exception {

		TagList preSystemTl = ourSystemDao.getAllTags();

		TagList tl1 = new TagList();
		tl1.addTag("testGetAllTagsScheme1", "testGetAllTagsTerm1", "testGetAllTagsLabel1");
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo").setValue("testGetAllTags01");
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
	public void testTransactionCreateMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.CREATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.NOOP, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertEquals(id, p.getId());

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(id.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionCreateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionCreateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.CREATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		try {
			ourSystemDao.transaction(Arrays.asList((IResource) p, o));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionCreateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionCreateMatchUrlWithZeroMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.CREATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().getIdPart(), not(containsString("test")));

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(p.getId().toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionCreateNoMatchUrl() {
		String methodName = "testTransactionCreateNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.CREATE);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p));
		assertEquals(2, resp.size());
		p = (Patient) resp.get(1);

		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().getIdPart(), not(containsString("test")));
	}

	@Test
	public void testTransactionDeleteMatchUrlWithOneMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.DELETE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p));
		assertEquals(2, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.DELETE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		try {
			ourPatientDao.read(id.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			ourPatientDao.read(new IdDt("Patient/" + methodName));
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

	}

	@Test
	public void testTransactionDeleteMatchUrlWithTwoMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.DELETE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		try {
			ourSystemDao.transaction(Arrays.asList((IResource) p, o));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionDeleteMatchUrlWithZeroMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithZeroMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "ZZZ");
		p.addName().addFamily("Hello");
		IdDt id = ourPatientDao.create(p).getId();

		p = new Patient();
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		IdDt id2 = ourPatientDao.create(p).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.DELETE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p));
		assertEquals(2, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.DELETE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().toVersionless().toString(), (containsString("test")));
		assertThat(p.getId().toString(), endsWith("/_history/2"));
		assertEquals(id2.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id2, p.getId());

		try {
			ourPatientDao.read(id2.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		Patient found = ourPatientDao.read(id);
		assertEquals(id, found.getId());

	}

	@Test
	public void testTransactionDeleteNoMatchUrl() {
		String methodName = "testTransactionDeleteNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.DELETE);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p));
		assertEquals(2, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.DELETE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get(p).getValue());

		try {
			ourPatientDao.read(id.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}
	}

	@Test(expected = InvalidRequestException.class)
	public void testTransactionFailsWithDuplicateIds() {
		Patient patient1 = new Patient();
		patient1.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
		patient1.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");

		Patient patient2 = new Patient();
		patient2.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
		patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");

		ourSystemDao.transaction(Arrays.asList((IResource) patient1, patient2));
	}

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = FhirSystemDaoTest.class.getResourceAsStream("/bundle.json");
		Bundle bundle = ourFhirContext.newJsonParser().parseBundle(new InputStreamReader(bundleRes));
		List<IResource> res = bundle.toListOfResources();

		ourSystemDao.transaction(res);

		Patient p1 = (Patient) res.get(0);
		String id = p1.getId().getValue();
		ourLog.info("ID: {}", id);
		assertThat(id, not(containsString("5556918")));
		assertThat(id, not(equalToIgnoringCase("")));
	}

	@Test
	public void testTransactionUpdateMatchUrlWithOneMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.UPDATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.UPDATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(id.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.UPDATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		try {
			ourSystemDao.transaction(Arrays.asList((IResource) p, o));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionUpdateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithZeroMatch";

		Patient p = new Patient();
		p.addName().addFamily("Hello");
		IdDt id = ourPatientDao.create(p).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId(id);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.UPDATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference(id);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.UPDATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(p.getId().toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateMatchUrlWithZeroMatchAndNotPreExisting() {
		String methodName = "testTransactionUpdateMatchUrlWithZeroMatchAndNotPreExisting";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.UPDATE);
		ResourceMetadataKeyEnum.LINK_SEARCH.put(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().toVersionless().toString(), containsString("test"));
		assertThat(p.getId().toString(), endsWith("/_history/1"));

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(p.getId().toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoMatchUrl() {
		String methodName = "testTransactionUpdateNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IdDt id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.put(p, BundleEntryTransactionOperationEnum.UPDATE);

		Observation o = new Observation();
		o.getName().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);

		List<IResource> resp = ourSystemDao.transaction(Arrays.asList((IResource) p, o));
		assertEquals(3, resp.size());

		p = (Patient) resp.get(1);
		assertEquals(BundleEntryTransactionOperationEnum.UPDATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(p));
		assertThat(p.getId().toVersionless().toString(), containsString("test"));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		o = (Observation) resp.get(2);
		assertEquals(BundleEntryTransactionOperationEnum.CREATE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_OPERATION.get(o));
		assertEquals(id.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoOperationSpecified() throws Exception {
		List<IResource> res = new ArrayList<IResource>();

		Patient p1 = new Patient();
		p1.getId().setValue("testTransactionWithUpdateXXX01");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithUpdate01");
		res.add(p1);

		Observation p2 = new Observation();
		p2.getId().setValue("testTransactionWithUpdateXXX02");
		p2.getIdentifier().setSystem("system").setValue("testTransactionWithUpdate02");
		p2.setSubject(new ResourceReferenceDt("Patient/testTransactionWithUpdateXXX01"));
		res.add(p2);

		ourSystemDao.transaction(res);

		assertFalse(p1.getId().isEmpty());
		assertFalse(p2.getId().isEmpty());
		assertEquals("testTransactionWithUpdateXXX01", p1.getId().getIdPart());
		assertEquals("testTransactionWithUpdateXXX02", p2.getId().getIdPart());
		assertNotEquals("testTransactionWithUpdateXXX01", p1.getId().getVersionIdPart());
		assertNotEquals("testTransactionWithUpdateXXX02", p2.getId().getVersionIdPart());
		assertEquals(p1.getId().toUnqualified().toVersionless(), p2.getSubject().getReference());

		IdDt p1id = p1.getId().toUnqualified().toVersionless();
		IdDt p1idWithVer = p1.getId().toUnqualified();
		IdDt p2id = p2.getId().toUnqualified().toVersionless();
		IdDt p2idWithVer = p2.getId().toUnqualified();

		/*
		 * Make some changes
		 */
		
		res = new ArrayList<IResource>();

		p1 = new Patient();
		p1.getId().setValue("testTransactionWithUpdateXXX01");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithUpdate01");
		p1.addName().addFamily("Name1");
		res.add(p1);

		p2 = new Observation();
		p2.getId().setValue("testTransactionWithUpdateXXX02");
		p2.getIdentifier().setSystem("system").setValue("testTransactionWithUpdate02");
		p2.setSubject(new ResourceReferenceDt("Patient/testTransactionWithUpdateXXX01"));
		p2.addReferenceRange().setHigh(new QuantityDt(123L));
		res.add(p2);
		
		List<IResource> results = ourSystemDao.transaction(res);

		assertEquals(p1id, results.get(1).getId().toUnqualified().toVersionless());
		assertEquals(p2id, results.get(2).getId().toUnqualified().toVersionless());
		assertNotEquals(p1idWithVer, results.get(1).getId().toUnqualified());
		assertNotEquals(p2idWithVer, results.get(2).getId().toUnqualified());

	}

	/**
	 * Issue #55
	 */
	@Test
	public void testTransactionWithCidIds() throws Exception {
		List<IResource> res = new ArrayList<IResource>();

		Patient p1 = new Patient();
		p1.setId("cid:patient1");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithCidIds01");
		res.add(p1);

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds02");
		o1.setSubject(new ResourceReferenceDt("Patient/cid:patient1"));
		res.add(o1);

		Observation o2 = new Observation();
		o2.setId("cid:observation2");
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
	public void testTransactionWithDelete() throws Exception {

		/*
		 * Create 3
		 */

		List<IResource> res;
		res = new ArrayList<IResource>();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
		res.add(p1);

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
		res.add(p2);

		Patient p3 = new Patient();
		p3.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
		res.add(p3);

		ourSystemDao.transaction(res);

		/*
		 * Verify
		 */

		IBundleProvider results = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testTransactionWithDelete"));
		assertEquals(3, results.size());

		/*
		 * Now delete 2
		 */

		res = new ArrayList<IResource>();
		List<IResource> existing = results.getResources(0, 3);

		p1 = new Patient();
		p1.setId(existing.get(0).getId());
		ResourceMetadataKeyEnum.DELETED_AT.put(p1, InstantDt.withCurrentTime());
		res.add(p1);

		p2 = new Patient();
		p2.setId(existing.get(1).getId());
		ResourceMetadataKeyEnum.DELETED_AT.put(p2, InstantDt.withCurrentTime());
		res.add(p2);

		ourSystemDao.transaction(res);

		/*
		 * Verify
		 */

		IBundleProvider results2 = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testTransactionWithDelete"));
		assertEquals(1, results2.size());
		List<IResource> existing2 = results2.getResources(0, 1);
		assertEquals(existing2.get(0).getId(), existing.get(2).getId());

	}

	@AfterClass
	public static void afterClass() {
		ourCtx.close();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() {
		ourCtx = new ClassPathXmlApplicationContext("fhir-jpabase-spring-test-config.xml");
		ourFhirContext = ourCtx.getBean(FhirContext.class);
		ourPatientDao = ourCtx.getBean("myPatientDao", IFhirResourceDao.class);
		ourObservationDao = ourCtx.getBean("myObservationDao", IFhirResourceDao.class);
		ourLocationDao = ourCtx.getBean("myLocationDao", IFhirResourceDao.class);
		ourSystemDao = ourCtx.getBean("mySystemDao", IFhirSystemDao.class);
	}

}
