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

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionOperationEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class FhirSystemDaoDstu2Test {

    private static ClassPathXmlApplicationContext ourCtx;
    private static FhirContext ourFhirContext;
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2Test.class);
    private static IFhirResourceDao<Patient> ourPatientDao;
    private static IFhirSystemDao<Bundle> ourSystemDao;
    private static IFhirResourceDao<Observation> ourObservationDao;

    @Test
    public void testTransactionCreateMatchUrlWithOneMatch() {
        String methodName = "testTransactionCreateMatchUrlWithOneMatch";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.setId("Patient/" + methodName);
        IdDt id = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got it: {}", id);

        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(3, resp.getEntry().size());

        Entry respEntry = resp.getEntry().get(1);
        assertEquals(Constants.STATUS_HTTP_200_OK + "", respEntry.getTransactionResponse().getStatus());
        assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
        assertEquals("1", respEntry.getTransactionResponse().getEtag().get(0).getValue());

        respEntry = resp.getEntry().get(2);
        assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
        assertThat(respEntry.getTransactionResponse().getLocation(), containsString("Observation/"));
        assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("/_history/1"));
        assertEquals("1", respEntry.getTransactionResponse().getEtag().get(0).getValue());

        o = (Observation) ourObservationDao.read(new IdDt(respEntry.getTransactionResponse().getLocationElement()));
        assertEquals(id.toVersionless(), o.getSubject().getReference());
        assertEquals("1", o.getId().getVersionIdPart());

    }


    @Test
    public void testTransactionReadAndSearch() {
        String methodName = "testTransactionReadAndSearch";

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.setId("Patient/" + methodName);
        IdDt idv1 = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got id: {}", idv1);

        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Family Name");
        p.setId("Patient/" + methodName);
        IdDt idv2 = ourPatientDao.update(p).getId();
        ourLog.info("Updated patient, got id: {}", idv2);

        Bundle request = new Bundle();
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualified().getValue());
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Bundle resp = ourSystemDao.transaction(request);

        assertEquals(4, resp.getEntry().size());

        Entry nextEntry;

        nextEntry = resp.getEntry().get(1);
        assertEquals(Patient.class, nextEntry.getResource().getClass());
        assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());

        nextEntry = resp.getEntry().get(2);
        assertEquals(Patient.class, nextEntry.getResource().getClass());
        assertEquals(idv1.toUnqualified(), nextEntry.getResource().getId().toUnqualified());

        nextEntry = resp.getEntry().get(3);
        assertEquals(Bundle.class, nextEntry.getResource().getClass());

        Bundle respBundle = (Bundle) nextEntry.getResource();
        assertEquals(1, respBundle.getTotal().intValue());
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

        Bundle request = new Bundle();
        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        try {
            ourSystemDao.transaction(request);
            fail();
        } catch (PreconditionFailedException e) {
            assertThat(e.getMessage(), containsString("with match URL \"Patient"));
        }
    }

    @Test
    public void testTransactionCreateMatchUrlWithZeroMatch() {
        String methodName = "testTransactionCreateMatchUrlWithZeroMatch";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(3, resp.getEntry().size());

        Entry respEntry = resp.getEntry().get(1);
        assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
        String patientId = respEntry.getTransactionResponse().getLocation();
        assertThat(patientId, not(endsWith("Patient/" + methodName + "/_history/1")));
        assertThat(patientId, (endsWith("/_history/1")));
        assertThat(patientId, (containsString("Patient/")));
        assertEquals("1", respEntry.getTransactionResponse().getEtag().get(0).getValue());

        respEntry = resp.getEntry().get(2);
        assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
        assertThat(respEntry.getTransactionResponse().getLocation(), containsString("Observation/"));
        assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("/_history/1"));
        assertEquals("1", respEntry.getTransactionResponse().getEtag().get(0).getValue());

        o = (Observation) ourObservationDao.read(new IdDt(respEntry.getTransactionResponse().getLocationElement()));
        assertEquals(new IdDt(patientId).toUnqualifiedVersionless(), o.getSubject().getReference());
    }

    @Test
    public void testTransactionCreateNoMatchUrl() {
        String methodName = "testTransactionCreateNoMatchUrl";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(2, resp.getEntry().size());

        Entry respEntry = resp.getEntry().get(1);
        assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
        String patientId = respEntry.getTransactionResponse().getLocation();
        assertThat(patientId, not(containsString("test")));
    }

    @Test
    public void testTransactionDeleteMatchUrlWithOneMatch() {
        String methodName = "testTransactionDeleteMatchUrlWithOneMatch";

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        IdDt id = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got it: {}", id);

        Bundle request = new Bundle();
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(2, resp.getEntry().size());

        Entry nextEntry = resp.getEntry().get(1);
        assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + "", nextEntry.getTransactionResponse().getStatus());

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

        IBundleProvider history = ourPatientDao.history(id, null);
        assertEquals(2, history.size());

        assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get(history.getResources(0, 0).get(0)));
        assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get(history.getResources(0, 0).get(0)).getValue());
        assertNull(ResourceMetadataKeyEnum.DELETED_AT.get(history.getResources(1, 1).get(0)));

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

        Bundle request = new Bundle();
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        try {
            ourSystemDao.transaction(request);
            fail();
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage(), containsString("resource with match URL \"Patient?"));
        }
    }

    @Test
    public void testTransactionDeleteByResourceId() {
        String methodName = "testTransactionDeleteByResourceId";

        Patient p1 = new Patient();
        p1.addIdentifier().setSystem("urn:system").setValue(methodName);
        IdDt id1 = ourPatientDao.create(p1).getId();
        ourLog.info("Created patient, got it: {}", id1);

        Patient p2 = new Patient();
        p2.addIdentifier().setSystem("urn:system").setValue(methodName);
        p2.setId("Patient/" + methodName);
        IdDt id2 = ourPatientDao.create(p2).getId();
        ourLog.info("Created patient, got it: {}", id2);

        Bundle request = new Bundle();

        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id1.getIdPart());
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id2.getIdPart());

        ourPatientDao.read(id1.toVersionless());
        ourPatientDao.read(id2.toVersionless());

        Bundle resp = ourSystemDao.transaction(request);

        assertEquals(3, resp.getEntry().size());
        assertEquals("204", resp.getEntry().get(1).getTransactionResponse().getStatus());
        assertEquals("204", resp.getEntry().get(2).getTransactionResponse().getStatus());

        try {
            ourPatientDao.read(id1.toVersionless());
            fail();
        } catch (ResourceGoneException e) {
            // good
        }

        try {
            ourPatientDao.read(id2.toVersionless());
            fail();
        } catch (ResourceGoneException e) {
            // good
        }

    }

    @Test
    public void testTransactionDeleteMatchUrlWithZeroMatch() {
        String methodName = "testTransactionDeleteMatchUrlWithZeroMatch";

        Bundle request = new Bundle();
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        try {
            ourSystemDao.transaction(request);
            fail();
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage(), containsString("resource matching URL \"Patient?"));
        }
    }

    @Test
    public void testTransactionDeleteNoMatchUrl() {
        String methodName = "testTransactionDeleteNoMatchUrl";

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.setId("Patient/" + methodName);
        IdDt id = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got it: {}", id);

        Bundle request = new Bundle();
        request.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Bundle res = ourSystemDao.transaction(request);
        assertEquals(2, res.getEntry().size());

        assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + "", res.getEntry().get(1).getTransactionResponse().getStatus());

        try {
            ourPatientDao.read(id.toVersionless());
            fail();
        } catch (ResourceGoneException e) {
            // ok
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void testTransactionFailsWithDuplicateIds() {
        Bundle request = new Bundle();

        Patient patient1 = new Patient();
        patient1.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
        patient1.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
        request.addEntry().setResource(patient1).getTransaction().setMethod(HTTPVerbEnum.POST);

        Patient patient2 = new Patient();
        patient2.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
        patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
        request.addEntry().setResource(patient2).getTransaction().setMethod(HTTPVerbEnum.POST);

        ourSystemDao.transaction(request);
    }

    @Test
    public void testTransactionUpdateMatchUrlWithOneMatch() {
        String methodName = "testTransactionUpdateMatchUrlWithOneMatch";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        IdDt id = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got it: {}", id);

        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(3, resp.getEntry().size());

        Entry nextEntry = resp.getEntry().get(1);
        assertEquals("200", nextEntry.getTransactionResponse().getStatus());
        assertThat(nextEntry.getTransactionResponse().getLocation(), not(containsString("test")));
        assertEquals(id.toVersionless(), p.getId().toVersionless());
        assertNotEquals(id, p.getId());
        assertThat(p.getId().toString(), endsWith("/_history/2"));

        nextEntry = resp.getEntry().get(1);
        assertEquals("" + Constants.STATUS_HTTP_200_OK, nextEntry.getTransactionResponse().getStatus());
        assertThat(nextEntry.getTransactionResponse().getLocation(), not(emptyString()));

        nextEntry = resp.getEntry().get(2);
        o = ourObservationDao.read(new IdDt(nextEntry.getTransactionResponse().getLocation()));
        assertEquals(id.toVersionless(), o.getSubject().getReference());

    }

    @Test
    public void testTransactionUpdateMatchUrlWithTwoMatch() {
        String methodName = "testTransactionUpdateMatchUrlWithTwoMatch";
        Bundle request = new Bundle();

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
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        try {
            ourSystemDao.transaction(request);
            fail();
        } catch (PreconditionFailedException e) {
            assertThat(e.getMessage(), containsString("with match URL \"Patient"));
        }
    }

    @Test
    public void testTransactionUpdateMatchUrlWithZeroMatch() {
        String methodName = "testTransactionUpdateMatchUrlWithZeroMatch";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addName().addFamily("Hello");
        IdDt id = ourPatientDao.create(p).getId();

        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId(id);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference(id);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(3, resp.getEntry().size());

        Entry nextEntry = resp.getEntry().get(1);
        assertEquals(Constants.STATUS_HTTP_201_CREATED + "", nextEntry.getTransactionResponse().getStatus());

        assertThat(nextEntry.getTransactionResponse().getLocation(), not(containsString("test")));
        assertNotEquals(id.toVersionless(), new IdDt(nextEntry.getTransactionResponse().getLocation()).toVersionless());

        assertThat(nextEntry.getTransactionResponse().getLocation(), endsWith("/_history/1"));

        nextEntry = resp.getEntry().get(1);
        assertEquals("" + Constants.STATUS_HTTP_201_CREATED, nextEntry.getTransactionResponse().getStatus());

        nextEntry = resp.getEntry().get(2);
        o = ourObservationDao.read(new IdDt(nextEntry.getTransactionResponse().getLocation()));
        assertEquals(id.toVersionless(), o.getSubject().getReference());

    }


    @Test
    public void testTransactionUpdateNoMatchUrl() {
        String methodName = "testTransactionUpdateNoMatchUrl";
        Bundle request = new Bundle();

        Patient p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.setId("Patient/" + methodName);
        IdDt id = ourPatientDao.create(p).getId();
        ourLog.info("Created patient, got it: {}", id);

        p = new Patient();
        p.addIdentifier().setSystem("urn:system").setValue(methodName);
        p.addName().addFamily("Hello");
        p.setId("Patient/" + methodName);
        request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient/" + id.getIdPart());

        Observation o = new Observation();
        o.getName().setText("Some Observation");
        o.getSubject().setReference("Patient/" + methodName);
        request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

        Bundle resp = ourSystemDao.transaction(request);
        assertEquals(3, resp.getEntry().size());

        Entry nextEntry = resp.getEntry().get(1);
        assertEquals("200", nextEntry.getTransactionResponse().getStatus());

        assertThat(nextEntry.getTransactionResponse().getLocation(), (containsString("test")));
        assertEquals(id.toVersionless(), new IdDt(nextEntry.getTransactionResponse().getLocation()).toVersionless());
        assertNotEquals(id, new IdDt(nextEntry.getTransactionResponse().getLocation()));
        assertThat(nextEntry.getTransactionResponse().getLocation(), endsWith("/_history/2"));

        nextEntry = resp.getEntry().get(2);
        assertEquals("" + Constants.STATUS_HTTP_201_CREATED, nextEntry.getTransactionResponse().getStatus());

        o = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getTransactionResponse().getLocation()));
        assertEquals(id.toVersionless(), o.getSubject().getReference());

    }

    //
    //
    // /**
    // * Issue #55
    // */
    // @Test
    // public void testTransactionWithCidIds() throws Exception {
    // Bundle request = new Bundle();
    //
    // Patient p1 = new Patient();
    // p1.setId("cid:patient1");
    // p1.addIdentifier().setSystem("system").setValue("testTransactionWithCidIds01");
    // res.add(p1);
    //
    // Observation o1 = new Observation();
    // o1.setId("cid:observation1");
    // o1.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds02");
    // o1.setSubject(new ResourceReferenceDt("Patient/cid:patient1"));
    // res.add(o1);
    //
    // Observation o2 = new Observation();
    // o2.setId("cid:observation2");
    // o2.getIdentifier().setSystem("system").setValue("testTransactionWithCidIds03");
    // o2.setSubject(new ResourceReferenceDt("Patient/cid:patient1"));
    // res.add(o2);
    //
    // ourSystemDao.transaction(res);
    //
    // assertTrue(p1.getId().getValue(), p1.getId().getIdPart().matches("^[0-9]+$"));
    // assertTrue(o1.getId().getValue(), o1.getId().getIdPart().matches("^[0-9]+$"));
    // assertTrue(o2.getId().getValue(), o2.getId().getIdPart().matches("^[0-9]+$"));
    //
    // assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
    // assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
    //
    // }
    //
    // @Test
    // public void testTransactionWithDelete() throws Exception {
    // Bundle request = new Bundle();
    //
    // /*
    // * Create 3
    // */
    //
    // List<IResource> res;
    // res = new ArrayList<IResource>();
    //
    // Patient p1 = new Patient();
    // p1.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
    // res.add(p1);
    //
    // Patient p2 = new Patient();
    // p2.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
    // res.add(p2);
    //
    // Patient p3 = new Patient();
    // p3.addIdentifier().setSystem("urn:system").setValue("testTransactionWithDelete");
    // res.add(p3);
    //
    // ourSystemDao.transaction(res);
    //
    // /*
    // * Verify
    // */
    //
    // IBundleProvider results = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system",
    // "testTransactionWithDelete"));
    // assertEquals(3, results.size());
    //
    // /*
    // * Now delete 2
    // */
    //
    // request = new Bundle();
    // res = new ArrayList<IResource>();
    // List<IResource> existing = results.getResources(0, 3);
    //
    // p1 = new Patient();
    // p1.setId(existing.get(0).getId());
    // ResourceMetadataKeyEnum.DELETED_AT.put(p1, InstantDt.withCurrentTime());
    // res.add(p1);
    //
    // p2 = new Patient();
    // p2.setId(existing.get(1).getId());
    // ResourceMetadataKeyEnum.DELETED_AT.put(p2, InstantDt.withCurrentTime());
    // res.add(p2);
    //
    // ourSystemDao.transaction(res);
    //
    // /*
    // * Verify
    // */
    //
    // IBundleProvider results2 = ourPatientDao.search(Patient.SP_IDENTIFIER, new TokenParam("urn:system",
    // "testTransactionWithDelete"));
    // assertEquals(1, results2.size());
    // List<IResource> existing2 = results2.getResources(0, 1);
    // assertEquals(existing2.get(0).getId(), existing.get(2).getId());
    //
    // }

    @AfterClass
    public static void afterClass() {
        ourCtx.close();
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void beforeClass() {
        ourCtx = new ClassPathXmlApplicationContext("hapi-fhir-server-resourceproviders-dstu2.xml", "fhir-jpabase-spring-test-config.xml");
        ourFhirContext = ourCtx.getBean(FhirContext.class);
        assertEquals(FhirVersionEnum.DSTU2, ourFhirContext.getVersion().getVersion());
        ourPatientDao = ourCtx.getBean("myPatientDaoDstu2", IFhirResourceDao.class);
        ourObservationDao = ourCtx.getBean("myObservationDaoDstu2", IFhirResourceDao.class);
        ourSystemDao = ourCtx.getBean("mySystemDaoDstu2", IFhirSystemDao.class);
    }

}
