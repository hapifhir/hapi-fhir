package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Appointment;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryRequest;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryResponse;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirSystemDaoDstu2Test extends BaseJpaDstu2SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2Test.class);

	@Test
	public void testSystemMetaOperation() {

		MetaDt meta = mySystemDao.metaGetOperation(mySrd);
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

			id1 = myPatientDao.create(patient, mySrd).getId();
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

			myPatientDao.create(patient, mySrd);
		}

		meta = mySystemDao.metaGetOperation(mySrd);
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

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1", mySrd);

		meta = mySystemDao.metaGetOperation(mySrd);
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
	public void testTransactionBatchWithFailingRead() {
		String methodName = "testTransactionBatchWithFailingRead";
		Bundle request = new Bundle();
		request.setType(BundleTypeEnum.BATCH);

		Patient p = new Patient();
		p.addName().addFamily(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient/THIS_ID_DOESNT_EXIST");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());
		assertEquals(BundleTypeEnum.BATCH_RESPONSE, resp.getTypeElement().getValueAsEnum());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		EntryResponse respEntry;

		// Bundle.entry[1] is create response
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), startsWith("Patient/"));

		// Bundle.entry[2] is failed read response
		assertEquals(OperationOutcome.class, resp.getEntry().get(1).getResource().getClass());
		assertEquals(IssueSeverityEnum.ERROR, ((OperationOutcome) resp.getEntry().get(1).getResource()).getIssue().get(0).getSeverityElement().getValueAsEnum());
		assertEquals(Msg.code(2001) + "Resource Patient/THIS_ID_DOESNT_EXIST is not known", ((OperationOutcome) resp.getEntry().get(1).getResource()).getIssue().get(0).getDiagnostics());
		assertEquals("404 Not Found", resp.getEntry().get(1).getResponse().getStatus());

		// Check POST
		respEntry = resp.getEntry().get(0).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		IdDt createdId = new IdDt(respEntry.getLocation());
		assertEquals("Patient", createdId.getResourceType());
		myPatientDao.read(createdId, mySrd); // shouldn't fail

		// Check GET
		respEntry = resp.getEntry().get(1).getResponse();
		assertThat(respEntry.getStatus(), startsWith("404"));

	}

	/**
	 * See #638
	 */
	@Test
	public void testTransactionBug638() throws Exception {
		String input = ClasspathUtil.loadResource("/bug638.xml");
		Bundle request = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		Bundle resp = mySystemDao.transaction(mySrd, request);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(18, resp.getEntry().size());
	}

	@Test
	public void testTransactionCreateMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) myObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless(), o.getSubject().getReference());
		assertEquals("1", o.getId().getVersionIdPart());

	}

	/**
	 * ?identifier=
	 */
	@Test
	public void testTransactionCreateMatchUrlWithOneMatchNoResourceName1() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) myObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless(), o.getSubject().getReference());
		assertEquals("1", o.getId().getVersionIdPart());

	}

	/**
	 * identifier=
	 */
	@Test
	public void testTransactionCreateMatchUrlWithOneMatchNoResourceName2() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) myObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless(), o.getSubject().getReference());
		assertEquals("1", o.getId().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionCreateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(2, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(endsWith("Patient/" + methodName + "/_history/1")));
		assertThat(patientId, (endsWith("/_history/1")));
		assertThat(patientId, (containsString("Patient/")));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) myObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(new IdDt(patientId).toUnqualifiedVersionless(), o.getSubject().getReference());
	}

	@Test
	public void testTransactionCreateWithDuplicateMatchUrl01() {
		String methodName = "testTransactionCreateWithDuplicateMatchUrl01";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		myCaptureQueriesListener.clear();
		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(2008) + "Unable to process Transaction - Request contains multiple anonymous entries (Bundle.entry.fullUrl not populated) with conditional URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl01\". Does transaction request contain duplicates?", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateWithDuplicateMatchUrl02() {
		String methodName = "testTransactionCreateWithDuplicateMatchUrl02";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), Msg.code(542) + "Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl02\". Does transaction request contain duplicates?");
		}
	}

	@Test
	public void testTransactionCreateWithInvalidMatchUrl() {
		String methodName = "testTransactionCreateWithInvalidMatchUrl";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		EntryRequest entry = request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			entry.setIfNoneExist("Patient?identifier   identifier" + methodName);
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1744) + "Failed to parse match URL[Patient?identifier   identifiertestTransactionCreateWithInvalidMatchUrl] - URL is invalid (must not contain spaces)", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?identifier=");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(518) + "Invalid match URL[Patient?identifier=] - URL has no search parameters", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?foo=bar");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(488) + "Failed to parse match URL[Patient?foo=bar] - Resource type Patient does not have a parameter with name: foo", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceNumeric() {
		String methodName = "testTransactionCreateWithInvalidReferenceNumeric";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("Organization/9999999999999999");
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
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
		IIdType id1 = myPatientDao.create(p1, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id1);

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.setId("Patient/" + methodName);
		IIdType id2 = myPatientDao.update(p2, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id2);

		Bundle request = new Bundle();

		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id1.getIdPart());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id2.getIdPart());

		myPatientDao.read(id1.toVersionless(), mySrd);
		myPatientDao.read(id2.toVersionless(), mySrd);

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(2, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());

		try {
			myPatientDao.read(id1.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.read(id2.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetFirst() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReference(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReference(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it first in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl(pid.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Observation?identifier=" + methodName);
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetLast() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReference(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReference(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it last in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Observation?identifier=" + methodName);
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl(pid.getValue());
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
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
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", nextEntry.getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdDt("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, null, mySrd);
		assertEquals(2, history.size().intValue());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 1).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 1).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(1, 2).get(0)));

	}

	@Test
	public void testTransactionDeleteMatchUrlWithTwoMatch() {
		myDaoConfig.setAllowMultipleDelete(false);

		String methodName = "testTransactionDeleteMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("resource with match URL \"Patient?"));
		}
	}

	@Test
	public void testTransactionDeleteMatchUrlWithZeroMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithZeroMatch";

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		// try {
		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());

		// fail();
		// } catch (ResourceNotFoundException e) {
		// assertThat(e.getMessage(), containsString("resource matching URL \"Patient?"));
		// }
	}

	@Test
	public void testTransactionDeleteNoMatchUrl() {
		String methodName = "testTransactionDeleteNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle res = mySystemDao.transaction(mySrd, request);
		assertEquals(1, res.getEntry().size());

		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", res.getEntry().get(0).getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}
	}

	@Test
	public void testTransactionDoesntUpdateUnchangesResourceWithPlaceholderIds() {
		Bundle output, input;
		Bundle.EntryResponse respEntry;
		IdType createdPatientId;
		SearchParameterMap map;
		IBundleProvider search;

		input = new Bundle();

		/*
		 * Create a transaction with a patient and an observation using
		 * placeholder IDs in them
		 */
		Patient pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		Observation obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerbEnum.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Both resrouces should be created and have version 1
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		createdPatientId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdPatientId.getResourceType());
		assertEquals("1", createdPatientId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		IdType createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference().getValue());

		/*
		 * Now run the same transaction, which should not make any changes this time
		 * around
		 */

		input = new Bundle();
		pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerbEnum.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Should still have version 1 of both resources
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should still work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference().getValue());

		/*
		 * Now run the transaction, but this time with an actual
		 * change to the Observation
		 */

		input = new Bundle();
		pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.setStatus(ObservationStatusEnum.FINAL);
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerbEnum.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Observation should now be version 2
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("2", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should still work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference().getValue());
		assertEquals(ObservationStatusEnum.FINAL.getCode(), obs.getStatus());

	}

	@Test
	public void testTransactionFailsWithDuplicateIds() {
		Bundle request = new Bundle();

		Patient patient1 = new Patient();
		patient1.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
		patient1.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
		request.addEntry().setResource(patient1).getRequest().setMethod(HTTPVerbEnum.POST);

		Patient patient2 = new Patient();
		patient2.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
		patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
		request.addEntry().setResource(patient2).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(535) + "Transaction bundle contains multiple resources with ID: Patient/testTransactionFailsWithDusplicateIds", e.getMessage());
		}
	}

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundleStr = IOUtils.toString(bundleRes);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertThat(resp.getEntry().get(0).getResponse().getLocation(), startsWith("Patient/a555-44-4444/_history/"));
		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/temp6789/_history/"));
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Organization/GHH/_history/"));

		Patient p = myPatientDao.read(new IdDt("Patient/a555-44-4444/_history/1"), mySrd);
		assertEquals("Patient/temp6789", p.getLink().get(0).getOther().getReference().getValue());
	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes);
		Bundle output = mySystemDao.transaction(mySrd, myFhirContext.newXmlParser().parseResource(Bundle.class, bundle));
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
	}

	@Test
	public void testTransactionFromBundleJosh() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/josh-bundle.json");
		String bundleStr = IOUtils.toString(bundleRes);
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
	}

	@Test
	public void testTransactionOrdering() {
		String methodName = "testTransactionOrdering";

		//@formatter:off
		/*
		 * Transaction Order, per the spec:
		 *
		 * Process any DELETE interactions
		 * Process any POST interactions
		 * Process any PUT interactions
		 * Process any GET interactions
		 *
		 * This test creates a transaction bundle that includes
		 * these four operations in the reverse order and verifies
		 * that they are invoked correctly.
		 */
		//@formatter:off

		int pass = 0;
		IdDt patientPlaceholderId = IdDt.newRandomUuid();

		Bundle req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		Bundle resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);

		pass = 1;
		patientPlaceholderId = IdDt.newRandomUuid();

		req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);

	}

	private Bundle testTransactionOrderingCreateBundle(String methodName, int pass, IdDt patientPlaceholderId) {
		Bundle req = new Bundle();
		req.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?identifier=" + methodName);

		Observation obs = new Observation();
		obs.getSubject().setReference(patientPlaceholderId);
		obs.addIdentifier().setValue(methodName);
		obs.getCode().setText(methodName + pass);
		req.addEntry().setResource(obs).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Observation?identifier=" + methodName);

		Patient pat = new Patient();
		pat.addIdentifier().setValue(methodName);
		pat.addName().addFamily(methodName + pass);
		req.addEntry().setResource(pat).setFullUrl(patientPlaceholderId.getValue()).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		req.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=" + methodName);
		return req;
	}

	private void testTransactionOrderingValidateResponse(int pass, Bundle resp) {
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertEquals(4, resp.getEntry().size());
		assertEquals("200 OK", resp.getEntry().get(0).getResponse().getStatus());
		if (pass == 0) {
			assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/1"));
		} else {
			assertEquals("200 OK", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/2"));
		}
		assertEquals("201 Created", resp.getEntry().get(2).getResponse().getStatus());
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Patient/"));
		assertEquals("204 No Content", resp.getEntry().get(3).getResponse().getStatus());

		Bundle respGetBundle = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(1, respGetBundle.getEntry().size());
		assertEquals("testTransactionOrdering" + pass, ((Patient) respGetBundle.getEntry().get(0).getResource()).getNameFirstRep().getFamilyFirstRep().getValue());
		assertThat(respGetBundle.getLink("self").getUrl(), endsWith("/Patient?identifier=testTransactionOrdering"));
	}

	@Test
	public void testTransactionReadWithIfNoneMatch() {
		String methodName = "testTransactionReadWithIfNoneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv1.getVersionIdPart() + "\"");
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv2.getVersionIdPart() + "\"");

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());

		Entry nextEntry;

		nextEntry = resp.getEntry().get(0);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(1);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(2);
		assertEquals("304 Not Modified", nextEntry.getResponse().getStatus());
		assertNull(nextEntry.getResource());
	}

	@Test
	public void testTransactionSearchWithCount() {
		String methodName = "testTransactionSearchWithCount";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=1&_total=accurate");
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(1, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(0);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertThat(respBundle.getTotal().intValue(), greaterThan(0));

		// Invalid _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=GKJGKJG");
		try {
			mySystemDao.transaction(mySrd, request);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), ("Invalid _count value: GKJGKJG"));
		}

		// Empty _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=");
		respBundle = mySystemDao.transaction(mySrd, request);
		assertThat(respBundle.getEntry().size(), greaterThan(0));
	}

	@Test
	public void testTransactionSingleEmptyResource() {

		Bundle request = new Bundle();
		request.setType(BundleTypeEnum.SEARCH_RESULTS);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(527) + "Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionUpdateMatchUrlWithOneMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		Entry nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertThat(p.getId().toString(), endsWith("/_history/2"));
		assertNotEquals(id, p.getId());

		nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(emptyString()));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdDt(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithTwoMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
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
		IIdType id = myPatientDao.create(p, mySrd).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());
		IdDt patientId = new IdDt(nextEntry.getResponse().getLocation());

		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertNotEquals(id.toVersionless(), patientId.toVersionless());

		assertThat(patientId.getValue(), endsWith("/_history/1"));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdDt(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(patientId.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoMatchUrl() {
		String methodName = "testTransactionUpdateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient/" + id.getIdPart());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		assertThat(nextEntry.getResponse().getLocation(), (containsString("test")));
		assertEquals(id.toVersionless(), new IdDt(nextEntry.getResponse().getLocation()).toVersionless());
		assertNotEquals(id, new IdDt(nextEntry.getResponse().getLocation()));
		assertThat(nextEntry.getResponse().getLocation(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());

		o = myObservationDao.read(new IdDt(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateResourceNewVersionCreatedWhenDataChanges() {

		Bundle request = new Bundle();
		String patientId = "Patient/IShouldUpdate";
		Patient p = new Patient();
		p.addName().addFamily("Hello");
		p.setId(patientId);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl(patientId);

		Bundle initialBundleResponse = mySystemDao.transaction(mySrd, request);
		assertEquals(1, initialBundleResponse.getEntry().size());
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(initialBundleResponse));

		Entry initialBundleResponseEntry = initialBundleResponse.getEntry().get(0);
		assertEquals("201 Created", initialBundleResponseEntry.getResponse().getStatus());
		assertThat(initialBundleResponseEntry.getResponse().getEtag(), is(equalTo("1")));

		p.addName().addFamily("AnotherName");

		Bundle secondBundleResponse = mySystemDao.transaction(mySrd, request);
		assertEquals(1, secondBundleResponse.getEntry().size());
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(secondBundleResponse));

		Entry secondBundleResponseEntry = secondBundleResponse.getEntry().get(0);
		assertEquals("200 OK", secondBundleResponseEntry.getResponse().getStatus());
		assertThat(secondBundleResponseEntry.getResponse().getEtag(), is(equalTo("2")));
	}

	@Test
	public void testTransactionUpdateResourceNewVersionNotCreatedWhenDataNotChanged() {

		Bundle request = new Bundle();
		String patientId = "Patient/IShouldNotUpdate";
		Patient p = new Patient();
		p.addName().addFamily("Hello");
		p.setId(patientId);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl(patientId);

		Bundle initialBundleResponse = mySystemDao.transaction(mySrd, request);
		assertEquals(1, initialBundleResponse.getEntry().size());
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(initialBundleResponse));

		Entry initialBundleResponseEntry = initialBundleResponse.getEntry().get(0);
		assertEquals("201 Created", initialBundleResponseEntry.getResponse().getStatus());
		assertThat(initialBundleResponseEntry.getResponse().getEtag(), is(equalTo("1")));

		Bundle secondBundleResponse = mySystemDao.transaction(mySrd, request);
		assertEquals(1, secondBundleResponse.getEntry().size());
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(secondBundleResponse));

		Entry secondBundleResponseEntry = secondBundleResponse.getEntry().get(0);
		assertEquals("200 OK", secondBundleResponseEntry.getResponse().getStatus());
		assertThat(secondBundleResponseEntry.getResponse().getEtag(), is(equalTo("1")));
	}

	@Test
	public void testTransactionWhichFailsPersistsNothing() {

		// Run a transaction which points to that practitioner
		// in a field that isn't allowed to refer to a practitioner
		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.TRANSACTION);

		Patient pt = new Patient();
		pt.setId("PT");
		pt.setActive(true);
		pt.addName().addFamily("FAMILY");
		input.addEntry()
			.setResource(pt)
			.getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient/PT");

		Observation obs = new Observation();
		obs.setId("OBS");
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.addPerformer().setReference("Practicioner/AAAAA");
		input.addEntry()
			.setResource(obs)
			.getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Observation/OBS");

		try {
			mySystemDao.transaction(mySrd, input);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Resource type 'Practicioner' is not valid for this path"));
		}

		assertThat(myResourceTableDao.findAll(), empty());
		assertThat(myResourceIndexedSearchParamStringDao.findAll(), empty());

	}

	/**
	 * From a message from David Hay
	 */
	@Test
	public void testTransactionWithAppointments() {
		Patient p = new Patient();
		p.addName().addFamily("family");
		final IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		//@formatter:on
		String input = "{\n" +
			"    \"resourceType\": \"Bundle\",\n" +
			"    \"type\": \"transaction\",\n" +
			"    \"entry\": [\n" +
			"        {\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Appointment\",\n" +
			"                \"status\": \"pending\",\n" +
			"                \"type\": {\"text\": \"Cardiology\"},\n" +
			"                \"description\": \"Investigate Angina\",\n" +
			"                \"start\": \"2016-04-30T18:48:29+12:00\",\n" +
			"                \"end\": \"2016-04-30T19:03:29+12:00\",\n" +
			"                \"minutesDuration\": 15,\n" +
			"                \"participant\": [\n" +
			"                    {\n" +
			"                        \"actor\": {\"display\": \"Clarence cardiology clinic\"},\n" +
			"                        \"status\": \"accepted\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"actor\": {\"reference\": \"Patient/" + id.getIdPart() + "\"},\n" +
			"                        \"status\": \"accepted\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"text\": {\n" +
			"                    \"status\": \"generated\",\n" +
			"                    \"div\": \"<div><div>Investigate Angina<\\/div><div>Clarence cardiology clinic<\\/div><\\/div>\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Appointment\"\n" +
			"            }\n" +
			"        },\n" +
			"        {\n" +
			"            \"resource\": {\n" +
			"                \"resourceType\": \"Appointment\",\n" +
			"                \"status\": \"pending\",\n" +
			"                \"type\": {\"text\": \"GP Visit\"},\n" +
			"                \"description\": \"Routine checkup\",\n" +
			"                \"start\": \"2016-05-03T18:48:29+12:00\",\n" +
			"                \"end\": \"2016-05-03T19:03:29+12:00\",\n" +
			"                \"minutesDuration\": 15,\n" +
			"                \"participant\": [\n" +
			"                    {\n" +
			"                        \"actor\": {\"display\": \"Dr Dave\"},\n" +
			"                        \"status\": \"accepted\"\n" +
			"                    },\n" +
			"                    {\n" +
			"                        \"actor\": {\"reference\": \"Patient/" + id.getIdPart() + "\"},\n" +
			"                        \"status\": \"accepted\"\n" +
			"                    }\n" +
			"                ],\n" +
			"                \"text\": {\n" +
			"                    \"status\": \"generated\",\n" +
			"                    \"div\": \"<div><div>Routine checkup<\\/div><div>Dr Dave<\\/div><\\/div>\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"request\": {\n" +
			"                \"method\": \"POST\",\n" +
			"                \"url\": \"Appointment\"\n" +
			"            }\n" +
			"        }\n" +
			"    ]\n" +
			"}";
		//@formatter:on

		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		Bundle outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(outputBundle));
	}

	@Test
	public void testTransactionWithInvalidType() {
		Bundle request = new Bundle();
		request.setType(BundleTypeEnum.SEARCH_RESULTS);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(527) + "Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithNullReference() {
		Patient p = new Patient();
		p.addName().addFamily("family");
		final IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Bundle inputBundle = new Bundle();

		//@formatter:off
		Patient app0 = new Patient();
		app0.addName().addFamily("NEW PATIENT");
		String placeholderId0 = IdDt.newRandomUuid().getValue();
		inputBundle
			.addEntry()
			.setResource(app0)
			.setFullUrl(placeholderId0)
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("Patient");
		//@formatter:on

		//@formatter:off
		Appointment app1 = new Appointment();
		app1.addParticipant().getActor().setReference(id);
		inputBundle
			.addEntry()
			.setResource(app1)
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("Appointment");
		//@formatter:on

		//@formatter:off
		Appointment app2 = new Appointment();
		app2.addParticipant().getActor().setDisplay("NO REF");
		app2.addParticipant().getActor().setDisplay("YES REF").setReference(placeholderId0);
		inputBundle
			.addEntry()
			.setResource(app2)
			.getRequest()
			.setMethod(HTTPVerbEnum.POST)
			.setUrl("Appointment");
		//@formatter:on

		Bundle outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		assertEquals(3, outputBundle.getEntry().size());
		IdDt id0 = new IdDt(outputBundle.getEntry().get(0).getResponse().getLocation());
		IdDt id1 = new IdDt(outputBundle.getEntry().get(1).getResponse().getLocation());
		IdDt id2 = new IdDt(outputBundle.getEntry().get(2).getResponse().getLocation());

		app2 = myAppointmentDao.read(id2, mySrd);
		assertEquals("NO REF", app2.getParticipant().get(0).getActor().getDisplay().getValue());
		assertEquals(null, app2.getParticipant().get(0).getActor().getReference().getValue());
		assertEquals("YES REF", app2.getParticipant().get(1).getActor().getDisplay().getValue());
		assertEquals(id0.toUnqualifiedVersionless().getValue(), app2.getParticipant().get(1).getActor().getReference().getValue());
	}

	/**
	 * Per a message on the mailing list
	 */
	@Test
	public void testTransactionWithPostDoesntUpdate() throws Exception {

		// First bundle (name is Joshua)

		String input = IOUtils.toString(getClass().getResource("/dstu3-post1.xml"), StandardCharsets.UTF_8);
		Bundle request = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		Bundle response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id = response.getEntry().get(0).getResponse().getLocation();

		// Now the second (name is Adam, shouldn't get used)

		input = IOUtils.toString(getClass().getResource("/dstu3-post2.xml"), StandardCharsets.UTF_8);
		request = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("200 OK", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id2 = response.getEntry().get(0).getResponse().getLocation();
		assertEquals(id, id2);

		Patient patient = myPatientDao.read(new IdType(id), mySrd);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals("Joshua", patient.getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testTransactionWithReferenceToCreateIfNoneExist() {
		Bundle bundle = new Bundle();
		bundle.setType(BundleTypeEnum.TRANSACTION);

		Medication med = new Medication();
		IdDt medId = IdDt.newRandomUuid();
		med.setId(medId);
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		MedicationOrder mo = new MedicationOrder();
		mo.setMedication(new ResourceReferenceDt(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getId().getValue()).getRequest().setMethod(HTTPVerbEnum.POST);

		ourLog.info("Request:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		Bundle outcome = mySystemDao.transaction(mySrd, bundle);
		ourLog.info("Response:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		IdDt medId1 = new IdDt(outcome.getEntry().get(0).getResponse().getLocation());
		IdDt medOrderId1 = new IdDt(outcome.getEntry().get(1).getResponse().getLocation());

		/*
		 * Again!
		 */

		bundle = new Bundle();
		bundle.setType(BundleTypeEnum.TRANSACTION);

		med = new Medication();
		medId = IdDt.newRandomUuid();
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		mo = new MedicationOrder();
		mo.setMedication(new ResourceReferenceDt(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getId().getValue()).getRequest().setMethod(HTTPVerbEnum.POST);

		outcome = mySystemDao.transaction(mySrd, bundle);

		IdDt medId2 = new IdDt(outcome.getEntry().get(0).getResponse().getLocation());
		IdDt medOrderId2 = new IdDt(outcome.getEntry().get(1).getResponse().getLocation());

		assertTrue(medId1.isIdPartValidLong());
		assertTrue(medId2.isIdPartValidLong());
		assertTrue(medOrderId1.isIdPartValidLong());
		assertTrue(medOrderId2.isIdPartValidLong());

		assertEquals(medId1, medId2);
		assertNotEquals(medOrderId1, medOrderId2);
	}

	@Test
	public void testTransactionWithRelativeOidIds() throws Exception {
		Bundle res = new Bundle();
		res.setType(BundleTypeEnum.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(3, resp.getEntry().size());

		assertTrue(new IdDt(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(0).getResponse().getLocation());
		assertTrue(new IdDt(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(1).getResponse().getLocation());
		assertTrue(new IdDt(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(2).getResponse().getLocation());

		o1 = myObservationDao.read(new IdDt(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdDt(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
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
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(3, resp.getEntry().size());

		assertTrue(new IdDt(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(0).getResponse().getLocation());
		assertTrue(new IdDt(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(1).getResponse().getLocation());
		assertTrue(new IdDt(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(2).getResponse().getLocation());

		o1 = myObservationDao.read(new IdDt(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdDt(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
		assertThat(o1.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));
		assertThat(o2.getSubject().getReference().getValue(), endsWith("Patient/" + p1.getId().getIdPart()));

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
	public void testTransactionWithReplacement() {
		byte[] bytes = new byte[] {0, 1, 2, 3, 4};

		Binary binary = new Binary();
		binary.setId(IdDt.newRandomUuid());
		binary.setContent(bytes);
		binary.setContentType("application/pdf");

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(IdDt.newRandomUuid());

		AttachmentDt attachment = new AttachmentDt();
		attachment.setContentType("application/pdf");
		attachment.getUrlElement().setValueAsString(binary.getId().getValueAsString()); // this one has substitution
		dr.addPresentedForm(attachment);

		AttachmentDt attachment2 = new AttachmentDt();
		attachment2.getUrlElement().setValueAsString(IdDt.newRandomUuid().getValue()); // this one has no subscitution
		dr.addPresentedForm(attachment2);

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(BundleTypeEnum.TRANSACTION);

		Entry binaryEntry = new Bundle.Entry();
		binaryEntry.setResource(binary).setFullUrl(binary.getId()).getRequest().setUrl("Binary").setMethod(HTTPVerbEnum.POST);
		transactionBundle.addEntry(binaryEntry);

		Entry drEntry = new Entry();
		drEntry.setResource(dr).setFullUrl(dr.getId()).getRequest().setUrl("DiagnosticReport").setMethod(HTTPVerbEnum.POST);
		transactionBundle.addEntry(drEntry);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(transactionBundle));

		Bundle transactionResp = mySystemDao.transaction(mySrd, transactionBundle);

		assertEquals(2, transactionResp.getEntry().size());

		// Validate Binary
		binary = myBinaryDao.read(new IdType(transactionResp.getEntry().get(0).getResponse().getLocation()));
		assertArrayEquals(bytes, binary.getContent());

		// Validate DiagnosticReport
		dr = myDiagnosticReportDao.read(new IdType(transactionResp.getEntry().get(1).getResponse().getLocation()));
		assertEquals(binary.getIdElement().toUnqualifiedVersionless().getValue(), dr.getPresentedForm().get(0).getUrl());
		assertEquals(attachment2.getUrl(), dr.getPresentedForm().get(1).getUrl());
	}


}
