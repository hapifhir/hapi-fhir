package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class FhirSystemDaoDstu2Test extends BaseJpaTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static FhirContext ourFhirContext;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2Test.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirSystemDao<Bundle> ourSystemDao;

	private void deleteEverything() {
		FhirSystemDaoDstu2Test.doDeleteEverything(ourSystemDao);
	}

	@Test
	public void testSystemMetaOperation() {
		deleteEverything();

		MetaDt meta = ourSystemDao.metaGetOperation();
		List<CodingDt> published = meta.getTag();
		assertEquals(0, published.size());

		String methodName = "testSystemMetaOperation";
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			TagList tagList = new TagList();
			tagList.addTag(null, "Dog", "Puppies");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/1"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			id1 = ourPatientDao.create(patient).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			TagList tagList = new TagList();
			tagList.addTag("http://foo", "Cat", "Kittens");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/2"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			ourPatientDao.create(patient);
		}

		meta = ourSystemDao.metaGetOperation();
		published = meta.getTag();
		assertEquals(2, published.size());
		assertEquals(null, published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<CodingDt> secLabels = meta.getSecurity();
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<UriDt> profiles = meta.getProfile();
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		ourPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog");
		ourPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1");
		ourPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1");

		meta = ourSystemDao.metaGetOperation();
		published = meta.getTag();
		assertEquals(1, published.size());
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertEquals(1, secLabels.size());
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertEquals(1, profiles.size());
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	@Test
	public void testTransactionCreateMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = ourPatientDao.update(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_200_OK + "", respEntry.getTransactionResponse().getStatus());
		assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getTransactionResponse().getEtag());

		respEntry = resp.getEntry().get(2);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
		assertThat(respEntry.getTransactionResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getTransactionResponse().getEtag());

		o = (Observation) ourObservationDao.read(new IdDt(respEntry.getTransactionResponse().getLocationElement()));
		assertEquals(id.toVersionless(), o.getSubject().getReference());
		assertEquals("1", o.getId().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionCreateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = ourPatientDao.create(p).getId();
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
		o.getCode().setText("Some Observation");
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
		o.getCode().setText("Some Observation");
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
		assertEquals("1", respEntry.getTransactionResponse().getEtag());

		respEntry = resp.getEntry().get(2);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + "", respEntry.getTransactionResponse().getStatus());
		assertThat(respEntry.getTransactionResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getTransactionResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getTransactionResponse().getEtag());

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
	public void testTransactionCreateWithInvalidReferenceNumeric() {
		String methodName = "testTransactionCreateWithInvalidReferenceNumeric";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("Organization/9999999999999999");
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST);

		try {
			ourSystemDao.transaction(request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/9999999999999999 not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceTextual() {
		String methodName = "testTransactionCreateWithInvalidReferenceTextual";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("Organization/" + methodName);
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.POST);

		try {
			ourSystemDao.transaction(request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/" + methodName + " not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionDeleteByResourceId() {
		String methodName = "testTransactionDeleteByResourceId";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = ourPatientDao.create(p1).getId();
		ourLog.info("Created patient, got it: {}", id1);

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.setId("Patient/" + methodName);
		IIdType id2 = ourPatientDao.update(p2).getId();
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
	public void testTransactionDeleteMatchUrlWithOneMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = ourPatientDao.create(p).getId();
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

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 0).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 0).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(1, 1).get(0)));

	}

	@Test
	public void testTransactionDeleteMatchUrlWithTwoMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = ourPatientDao.create(p).getId();
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
		IIdType id = ourPatientDao.update(p).getId();
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
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundleStr = IOUtils.toString(bundleRes);
		Bundle bundle = ourFhirContext.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = ourSystemDao.transaction(bundle);

		ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		OperationOutcome oo = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(oo.getIssue().get(0).getDetailsElement().getValue(), containsString("Transaction completed"));

		assertThat(resp.getEntry().get(1).getTransactionResponse().getLocation(), startsWith("Patient/a555-44-4444/_history/"));
		assertThat(resp.getEntry().get(2).getTransactionResponse().getLocation(), startsWith("Patient/temp6789/_history/"));
		assertThat(resp.getEntry().get(3).getTransactionResponse().getLocation(), startsWith("Organization/GHH/_history/"));

		Patient p = ourPatientDao.read(new IdDt("Patient/a555-44-4444/_history/1"));
		assertEquals("Patient/temp6789", p.getLink().get(0).getOther().getReference().getValue());
	}

	@Test
	public void testTransactionReadAndSearch() {
		String methodName = "testTransactionReadAndSearch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = ourPatientDao.update(p).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = ourPatientDao.update(p).getId();
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
	public void testTransactionSearchWithCount() {
		String methodName = "testTransactionSearchWithCount";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = ourPatientDao.update(p).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = ourPatientDao.update(p).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=1");
		Bundle resp = ourSystemDao.transaction(request);

		assertEquals(2, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertThat(respBundle.getTotal().intValue(), greaterThan(0));

		// Invalid _count
		
		request = new Bundle();
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=GKJGKJG");
		try {
		ourSystemDao.transaction(request);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), ("Invalid _count value: GKJGKJG"));
		}

		// Empty _count
		
		request = new Bundle();
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=");
		respBundle = ourSystemDao.transaction(request);
		assertThat(respBundle.getEntry().size(), greaterThan(0));
	}

	@Test
	public void testTransactionReadWithIfNoneMatch() {
		String methodName = "testTransactionReadWithIfNoneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = ourPatientDao.update(p).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = ourPatientDao.update(p).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv1.getVersionIdPart() + "\"");
		request.addEntry().getTransaction().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv2.getVersionIdPart() + "\"");
		
		Bundle resp = ourSystemDao.transaction(request);

		assertEquals(4, resp.getEntry().size());

		Entry nextEntry;

		nextEntry = resp.getEntry().get(1);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200", nextEntry.getTransactionResponse().getStatus());

		nextEntry = resp.getEntry().get(2);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200", nextEntry.getTransactionResponse().getStatus());

		nextEntry = resp.getEntry().get(3);
		assertNull(nextEntry.getResource());
		assertEquals("304", nextEntry.getTransactionResponse().getStatus());
	}

	@Test
	public void testTransactionUpdateMatchUrlWithOneMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = ourPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
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
		IIdType id = ourPatientDao.create(p).getId();
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
		o.getCode().setText("Some Observation");
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
		IIdType id = ourPatientDao.create(p).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId(methodName);
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getTransaction().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + "", nextEntry.getTransactionResponse().getStatus());
		IdDt patientId = new IdDt(nextEntry.getTransactionResponse().getLocation());

		assertThat(nextEntry.getTransactionResponse().getLocation(), not(containsString("test")));
		assertNotEquals(id.toVersionless(), patientId.toVersionless());

		assertThat(patientId.getValue(), endsWith("/_history/1"));

		nextEntry = resp.getEntry().get(2);
		o = ourObservationDao.read(new IdDt(nextEntry.getTransactionResponse().getLocation()));
		assertEquals(patientId.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoMatchUrl() {
		String methodName = "testTransactionUpdateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = ourPatientDao.update(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getTransaction().setMethod(HTTPVerbEnum.PUT).setUrl("Patient/" + id.getIdPart());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
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

	@Test
	public void testTransactionWithRelativeOidIds() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleTypeEnum.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.setId("cid:observation2");
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = ourSystemDao.transaction(res);
		
		ourLog.info(ourFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		
		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(4, resp.getEntry().size());

		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());
		
		OperationOutcome outcome = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(outcome.getIssue().get(1).getDetails(), containsString("Placeholder resource ID \"urn:oid:0.1.2.3\" was replaced with permanent ID \"Patient/"));
		
		assertTrue(resp.getEntry().get(1).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(1).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(2).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(3).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(3).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getTransactionResponse().getLocation()));
		o2 = ourObservationDao.read(new IdDt(resp.getEntry().get(3).getTransactionResponse().getLocation()));
		assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
		assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));

	}

	/**
	 * This is not the correct way to do it, but we'll allow it to be lenient
	 */
	@Test
	public void testTransactionWithRelativeOidIdsQualified() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleTypeEnum.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.setId("cid:observation2");
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getTransaction().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = ourSystemDao.transaction(res);
		
		ourLog.info(ourFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		
		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(4, resp.getEntry().size());

		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());
		
		OperationOutcome outcome = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(outcome.getIssue().get(1).getDetails(), containsString("Placeholder resource ID \"urn:oid:0.1.2.3\" was replaced with permanent ID \"Patient/"));
		
		assertTrue(resp.getEntry().get(1).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(1).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(2).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(3).getTransactionResponse().getLocation(), new IdDt(resp.getEntry().get(3).getTransactionResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getTransactionResponse().getLocation()));
		o2 = ourObservationDao.read(new IdDt(resp.getEntry().get(3).getTransactionResponse().getLocation()));
		assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
		assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));

	}

	@AfterClass
	public static void afterClass() throws SQLException {
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

	static void doDeleteEverything(IFhirSystemDao<Bundle> systemDao) {
		IBundleProvider all = systemDao.history(null);
		List<IBaseResource> allRes = all.getResources(0, all.size());
		for (IBaseResource iResource : allRes) {
			if (ResourceMetadataKeyEnum.DELETED_AT.get((IResource) iResource) == null) {
				ourLog.info("Deleting: {}", iResource.getIdElement());

				Bundle b = new Bundle();
				b.setType(BundleTypeEnum.TRANSACTION);
				String url = iResource.getIdElement().toVersionless().getValue();
				b.addEntry().getTransaction().setMethod(HTTPVerbEnum.DELETE).setUrl(url);
				systemDao.transaction(b);
			}
		}

		systemDao.deleteAllTagsOnServer();
	}

}
