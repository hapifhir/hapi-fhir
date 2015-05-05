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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class FhirSystemDaoDstu1Test {

    private static ClassPathXmlApplicationContext ourCtx;
    private static FhirContext ourFhirContext;
    private static IFhirResourceDao<Location> ourLocationDao;
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu1Test.class);
    private static IFhirResourceDao<Observation> ourObservationDao;
    private static IFhirResourceDao<Patient> ourPatientDao;
    private static IFhirSystemDao<List<IResource>> ourSystemDao;

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
        patient.setId(pid);
        IdDt newpid = ourPatientDao.update(patient).getId();

        Thread.sleep(10);
        patient.setId(pid);
        IdDt newpid2 = ourPatientDao.update(patient).getId();

        Thread.sleep(10);
        patient.setId(pid);
        IdDt newpid3 = ourPatientDao.update(patient).getId();

        IBundleProvider values = ourSystemDao.history(start);
        assertEquals(4, values.size());

        List<IBaseResource> res = values.getResources(0, 4);
        assertEquals(newpid3, res.get(0).getIdElement());
        assertEquals(newpid2, res.get(1).getIdElement());
        assertEquals(newpid, res.get(2).getIdElement());
        assertEquals(pid.toUnqualifiedVersionless(), res.get(3).getIdElement().toUnqualifiedVersionless());

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

        IIdType foundPatientId = patResults.getResources(0, 1).get(0).getIdElement();
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
        o1.setId(o1id);
        IdDt o1id2 = ourObservationDao.update(o1).getId();
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

        ourObservationDao.removeTag(o1id2, TagTypeEnum.TAG, "testGetAllTagsScheme2", "testGetAllTagsTerm2");
        tags2 = ourObservationDao.getTags(o1id2);
        assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
        assertNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

        tags2 = ourObservationDao.getTags(o1id);
        assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
        assertNotNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

		/*
		 * Add a tag
		 */
        ourObservationDao.addTag(o1id2, TagTypeEnum.TAG, "testGetAllTagsScheme3", "testGetAllTagsTerm3", "testGetAllTagsLabel3");
        tags2 = ourObservationDao.getTags(o1id2);
        assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
        assertNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));
        assertNotNull(tags2.getTag("testGetAllTagsScheme3", "testGetAllTagsTerm3"));
        assertEquals("testGetAllTagsLabel3", tags2.getTag("testGetAllTagsScheme3", "testGetAllTagsTerm3").getLabel());

        tags2 = ourObservationDao.getTags(o1id);
        assertNull(tags2.getTag("testGetAllTagsScheme1", "testGetAllTagsTerm1"));
        assertNotNull(tags2.getTag("testGetAllTagsScheme2", "testGetAllTagsTerm2"));

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

        InputStream bundleRes = FhirSystemDaoDstu1Test.class.getResourceAsStream("/bundle-dstu1.xml");
        Bundle bundle = ourFhirContext.newXmlParser().parseBundle(new InputStreamReader(bundleRes));
        List<IResource> res = bundle.toListOfResources();

        ourSystemDao.transaction(res);

        Patient p1 = (Patient) res.get(0);
        String id = p1.getId().getValue();
        ourLog.info("ID: {}", id);
        assertThat(id, not(equalToIgnoringCase("74635")));
        assertThat(id, not(equalToIgnoringCase("")));
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
        List<IBaseResource> existing = results.getResources(0, 3);

        p1 = new Patient();
        p1.setId(existing.get(0).getIdElement());
        ResourceMetadataKeyEnum.DELETED_AT.put(p1, InstantDt.withCurrentTime());
        res.add(p1);

        p2 = new Patient();
        p2.setId(existing.get(1).getIdElement());
        ResourceMetadataKeyEnum.DELETED_AT.put(p2, InstantDt.withCurrentTime());
        res.add(p2);

        ourSystemDao.transaction(res);

		/*
		 * Verify
		 */

        IBundleProvider results2 = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testTransactionWithDelete"));
        assertEquals(1, results2.size());
        List<IBaseResource> existing2 = results2.getResources(0, 1);
        assertEquals(existing2.get(0).getIdElement(), existing.get(2).getIdElement());

    }

    @AfterClass
    public static void afterClass() {
        ourCtx.close();
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void beforeClass() {
        ourCtx = new ClassPathXmlApplicationContext("hapi-fhir-server-resourceproviders-dstu1.xml", "fhir-jpabase-spring-test-config.xml");
        ourFhirContext = ourCtx.getBean(FhirContext.class);
        ourPatientDao = ourCtx.getBean("myPatientDaoDstu1", IFhirResourceDao.class);
        ourObservationDao = ourCtx.getBean("myObservationDaoDstu1", IFhirResourceDao.class);
        ourLocationDao = ourCtx.getBean("myLocationDaoDstu1", IFhirResourceDao.class);
        ourSystemDao = ourCtx.getBean("mySystemDaoDstu1", IFhirSystemDao.class);
    }

}
