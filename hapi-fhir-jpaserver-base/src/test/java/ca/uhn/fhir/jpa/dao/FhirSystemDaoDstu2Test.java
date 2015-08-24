package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryResponse;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

public class FhirSystemDaoDstu2Test extends BaseJpaTest {

	private static ClassPathXmlApplicationContext ourCtx;
	private static FhirContext ourFhirContext;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2Test.class);
	private static IFhirResourceDao<Observation> ourObservationDao;
	private static IFhirResourceDao<Patient> ourPatientDao;
	private static IFhirSystemDao<Bundle> ourSystemDao;
	private static IServerInterceptor ourInterceptor;

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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(2);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) ourObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()));
		assertEquals(id.toVersionless(), o.getSubject().getReference());
		assertEquals("1", o.getId().getVersionIdPart());

	}

	@Test
	public void testTransactionWithInvalidType() {
		Bundle request = new Bundle();
		request.setType(BundleTypeEnum.SEARCH_RESULTS);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

		try {
			ourSystemDao.transaction(request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

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

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		ourLog.info(ourFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		EntryResponse respEntry;

		// Bundle.entry[0] is operation outcome
		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals(IssueSeverityEnum.INFORMATION, ((OperationOutcome)resp.getEntry().get(0).getResource()).getIssue().get(0).getSeverityElement().getValueAsEnum());
		assertThat(((OperationOutcome)resp.getEntry().get(0).getResource()).getIssue().get(0).getDiagnostics(), startsWith("Batch completed in "));

		// Bundle.entry[1] is create response
		assertEquals(OperationOutcome.class, resp.getEntry().get(1).getResource().getClass());
		assertEquals(IssueSeverityEnum.INFORMATION, ((OperationOutcome)resp.getEntry().get(1).getResource()).getIssue().get(0).getSeverityElement().getValueAsEnum());
		assertThat(((OperationOutcome)resp.getEntry().get(1).getResource()).getIssue().get(0).getDiagnostics(), startsWith("Batch sub-request completed in"));
		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/"));
		
		// Bundle.entry[2] is failed read response
		assertEquals(OperationOutcome.class, resp.getEntry().get(2).getResource().getClass());
		assertEquals(IssueSeverityEnum.ERROR, ((OperationOutcome)resp.getEntry().get(2).getResource()).getIssue().get(0).getSeverityElement().getValueAsEnum());
		assertEquals("Resource Patient/THIS_ID_DOESNT_EXIST is not known", ((OperationOutcome)resp.getEntry().get(2).getResource()).getIssue().get(0).getDiagnostics());
		assertEquals("404 Not Found", resp.getEntry().get(2).getResponse().getStatus());

		// Check POST
		respEntry = resp.getEntry().get(1).getResponse();
		assertThat(respEntry.getStatus(), startsWith("201"));
		IdDt createdId = new IdDt(respEntry.getLocation());
		assertEquals("Patient", createdId.getResourceType());
		ourPatientDao.read(createdId); // shouldn't fail

		// Check GET
		respEntry = resp.getEntry().get(2).getResponse();
		assertThat(respEntry.getStatus(), startsWith("404"));

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

		try {
			ourSystemDao.transaction(request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(),
					"Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl01\". Does transaction request contain duplicates?");
		}
	}

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
			ourSystemDao.transaction(request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(),
					"Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl02\". Does transaction request contain duplicates?");
		}
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(3, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(endsWith("Patient/" + methodName + "/_history/1")));
		assertThat(patientId, (endsWith("/_history/1")));
		assertThat(patientId, (containsString("Patient/")));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(2);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = (Observation) ourObservationDao.read(new IdDt(respEntry.getResponse().getLocationElement()));
		assertEquals(new IdDt(patientId).toUnqualifiedVersionless(), o.getSubject().getReference());
	}

	@Test
	public void testTransactionCreateNoMatchUrl() {
		String methodName = "testTransactionCreateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(2, resp.getEntry().size());

		Entry respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(containsString("test")));

		/*
		 * Interceptor should have been called once for the transaction, and once for the
		 * embedded operation
		 */
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.TRANSACTION), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Bundle", details.getResourceType());
		assertEquals(Bundle.class, details.getResource().getClass());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertNotNull(details.getId());
		assertEquals("Patient", details.getResourceType());
		assertEquals(Patient.class, details.getResource().getClass());
		
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.POST);

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

		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id1.getIdPart());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient/" + id2.getIdPart());

		ourPatientDao.read(id1.toVersionless());
		ourPatientDao.read(id2.toVersionless());

		Bundle resp = ourSystemDao.transaction(request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(2, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", nextEntry.getResponse().getStatus());

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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle res = ourSystemDao.transaction(request);
		assertEquals(2, res.getEntry().size());

		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", res.getEntry().get(1).getResponse().getStatus());

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
		request.addEntry().setResource(patient1).getRequest().setMethod(HTTPVerbEnum.POST);

		Patient patient2 = new Patient();
		patient2.setId(new IdDt("Patient/testTransactionFailsWithDusplicateIds"));
		patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
		request.addEntry().setResource(patient2).getRequest().setMethod(HTTPVerbEnum.POST);

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

		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/a555-44-4444/_history/"));
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Patient/temp6789/_history/"));
		assertThat(resp.getEntry().get(3).getResponse().getLocation(), startsWith("Organization/GHH/_history/"));

		Patient p = ourPatientDao.read(new IdDt("Patient/a555-44-4444/_history/1"));
		assertEquals("Patient/temp6789", p.getLink().get(0).getOther().getReference().getValue());
	}

	@Test
	public void testTransactionFromBundleJosh() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/josh-bundle.json");
		String bundleStr = IOUtils.toString(bundleRes);
		Bundle bundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = ourSystemDao.transaction(bundle);

		ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		OperationOutcome oo = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(oo.getIssue().get(0).getDiagnostics(), containsString("Transaction completed"));

		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("201 Created", resp.getEntry().get(2).getResponse().getStatus());
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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualified().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

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
		
		/*
		 * Interceptor should have been called once for the transaction, and once for the
		 * embedded operation
		 */
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.TRANSACTION), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals("Bundle", details.getResourceType());
		assertEquals(Bundle.class, details.getResource().getClass());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.READ), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals(idv1.toUnqualifiedVersionless(), details.getId());
		assertEquals("Patient", details.getResourceType());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.VREAD), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals(idv1.toUnqualified(), details.getId());
		assertEquals("Patient", details.getResourceType());

		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(ourInterceptor, times(1)).incomingRequestPreHandled(eq(RestOperationTypeEnum.SEARCH_TYPE), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceType());

		
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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=1");
		Bundle resp = ourSystemDao.transaction(request);

		assertEquals(2, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertThat(respBundle.getTotal().intValue(), greaterThan(0));

		// Invalid _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=GKJGKJG");
		try {
			ourSystemDao.transaction(request);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), ("Invalid _count value: GKJGKJG"));
		}

		// Empty _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=");
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
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv1.getVersionIdPart() + "\"");
		request.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv2.getVersionIdPart() + "\"");

		Bundle resp = ourSystemDao.transaction(request);

		assertEquals(4, resp.getEntry().size());

		Entry nextEntry;

		nextEntry = resp.getEntry().get(1);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(2);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getId().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(3);
		assertNull(nextEntry.getResource());
		assertEquals("304 Not Modified", nextEntry.getResponse().getStatus());
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(emptyString()));

		nextEntry = resp.getEntry().get(2);
		o = ourObservationDao.read(new IdDt(nextEntry.getResponse().getLocation()));
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());
		IdDt patientId = new IdDt(nextEntry.getResponse().getLocation());

		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertNotEquals(id.toVersionless(), patientId.toVersionless());

		assertThat(patientId.getValue(), endsWith("/_history/1"));

		nextEntry = resp.getEntry().get(2);
		o = ourObservationDao.read(new IdDt(nextEntry.getResponse().getLocation()));
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
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.PUT).setUrl("Patient/" + id.getIdPart());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerbEnum.POST);

		Bundle resp = ourSystemDao.transaction(request);
		assertEquals(3, resp.getEntry().size());

		Entry nextEntry = resp.getEntry().get(1);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		assertThat(nextEntry.getResponse().getLocation(), (containsString("test")));
		assertEquals(id.toVersionless(), new IdDt(nextEntry.getResponse().getLocation()).toVersionless());
		assertNotEquals(id, new IdDt(nextEntry.getResponse().getLocation()));
		assertThat(nextEntry.getResponse().getLocation(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(2);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());

		o = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getResponse().getLocation()));
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
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.setId("cid:observation2");
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = ourSystemDao.transaction(res);

		ourLog.info(ourFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(4, resp.getEntry().size());

		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());

		OperationOutcome outcome = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(outcome.getIssue().get(1).getDiagnostics(), containsString("Placeholder resource ID \"urn:oid:0.1.2.3\" was replaced with permanent ID \"Patient/"));

		assertTrue(resp.getEntry().get(1).getResponse().getLocation(), new IdDt(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getResponse().getLocation(), new IdDt(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(3).getResponse().getLocation(), new IdDt(resp.getEntry().get(3).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getResponse().getLocation()));
		o2 = ourObservationDao.read(new IdDt(resp.getEntry().get(3).getResponse().getLocation()));
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
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.setId("cid:observation2");
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new ResourceReferenceDt("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Observation");

		Bundle resp = ourSystemDao.transaction(res);

		ourLog.info(ourFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleTypeEnum.TRANSACTION_RESPONSE, resp.getTypeElement().getValueAsEnum());
		assertEquals(4, resp.getEntry().size());

		assertEquals(OperationOutcome.class, resp.getEntry().get(0).getResource().getClass());

		OperationOutcome outcome = (OperationOutcome) resp.getEntry().get(0).getResource();
		assertThat(outcome.getIssue().get(1).getDiagnostics(), containsString("Placeholder resource ID \"urn:oid:0.1.2.3\" was replaced with permanent ID \"Patient/"));

		assertTrue(resp.getEntry().get(1).getResponse().getLocation(), new IdDt(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(2).getResponse().getLocation(), new IdDt(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));
		assertTrue(resp.getEntry().get(3).getResponse().getLocation(), new IdDt(resp.getEntry().get(3).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"));

		o1 = ourObservationDao.read(new IdDt(resp.getEntry().get(2).getResponse().getLocation()));
		o2 = ourObservationDao.read(new IdDt(resp.getEntry().get(3).getResponse().getLocation()));
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
		ourInterceptor = mock(IServerInterceptor.class);

		DaoConfig daoConfig = ourCtx.getBean(DaoConfig.class);
		daoConfig.setInterceptors(ourInterceptor);

	}

	@Before
	public void before() {
		reset(ourInterceptor);
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
				b.addEntry().getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl(url);
				systemDao.transaction(b);
			}
		}

		systemDao.deleteAllTagsOnServer();
	}

}
