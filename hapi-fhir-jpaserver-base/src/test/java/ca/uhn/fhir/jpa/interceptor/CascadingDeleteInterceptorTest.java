package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CascadingDeleteInterceptorTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CascadingDeleteInterceptorTest.class);
	private IIdType myDiagnosticReportId;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private IIdType myPatientId;
	@Autowired
	private CascadingDeleteInterceptor myDeleteInterceptor;
	private IIdType myObservationId;
	private IIdType myConditionId;
	private IIdType myEncounterId;
	@Autowired
	private OverridePathBasedReferentialIntegrityForDeletesInterceptor myOverridePathBasedReferentialIntegrityForDeletesInterceptor;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterInterceptor(myDeleteInterceptor);
	}

	public void createResources() {
		Patient p = new Patient();
		p.setActive(true);
		myPatientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Encounter e = new Encounter();
		e.setSubject(new Reference(myPatientId));
		myEncounterId = myClient.create().resource(e).execute().getId().toUnqualifiedVersionless();

		CarePlan cp = new CarePlan();
		cp.setEncounter(new Reference(myEncounterId));
		myClient.create().resource(cp).execute();

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSubject(new Reference(myPatientId));
		o.setEncounter(new Reference(myEncounterId));
		myObservationId = myClient.create().resource(o).execute().getId().toUnqualifiedVersionless();

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
		dr.addResult().setReference(myObservationId.getValue());
		dr.setEncounter(new Reference(myEncounterId));
		myDiagnosticReportId = myClient.create().resource(dr).execute().getId().toUnqualifiedVersionless();

		Condition condition = new Condition();
		condition.setSubject(new Reference(myPatientId));
		condition.setAsserter(new Reference(myPatientId));
		condition.setEncounter(new Reference(myEncounterId));
		myConditionId = myClient.create().resource(condition).execute().getId().toUnqualifiedVersionless();
	}

	@Test
	public void testDeleteWithInterceptorVerifyTheRequestGetsPassedToDao() throws IOException {
		// The whole and ONLY point of this Cascade Delete Unit Test is to make sure that a non-NULL RequestDetails param
		// is passed to the dao.read() method from inside the CascadingDeleteInterceptor.handleDeleteConflicts() method
		// For details see: https://gitlab.com/simpatico.ai/cdr/-/issues/1643
		DaoRegistry mockDaoRegistry = mock(DaoRegistry.class);
		IFhirResourceDao mockResourceDao = mock (IFhirResourceDao.class);
		IBaseResource mockResource = mock(IBaseResource.class);
		CascadingDeleteInterceptor aDeleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, mockDaoRegistry, myInterceptorBroadcaster);
		ourRestServer.getInterceptorService().unregisterInterceptor(myDeleteInterceptor);
		ourRestServer.getInterceptorService().registerInterceptor(aDeleteInterceptor);
		when(mockDaoRegistry.getResourceDao(any(String.class))).thenReturn(mockResourceDao);
		when(mockResourceDao.read(any(IIdType.class), any(RequestDetails.class))).thenReturn(mockResource);
		ArgumentCaptor<RequestDetails> theRequestDetailsCaptor = ArgumentCaptor.forClass(RequestDetails.class);

		Patient p = new Patient();
		p.setActive(true);
		myPatientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();
		Encounter e = new Encounter();
		e.setSubject(new Reference(myPatientId));
		myEncounterId = myClient.create().resource(e).execute().getId().toUnqualifiedVersionless();

		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPatientId.getValue() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
		}

		verify(mockResourceDao).read(any(IIdType.class), theRequestDetailsCaptor.capture());
		List<RequestDetails> capturedRequestDetailsParam = theRequestDetailsCaptor.getAllValues();
		for (RequestDetails requestDetails : capturedRequestDetailsParam) {
			assertNotNull(requestDetails);
		}
	}

	@Test
	public void testDeleteWithNoInterceptorAndConstraints() {
		createResources();

		try {
			myClient.delete().resourceById(myPatientId).execute();
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testDeleteWithNoRequestObject() {
		createResources();

		myInterceptorRegistry.registerInterceptor(myDeleteInterceptor);

		try {
			myPatientDao.delete(myPatientId);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), containsString("because at least one resource has a reference to this resource"));
		}
	}

	@Test
	public void testDeleteWithInterceptorAndConstraints() {
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		try {
			myClient.delete().resourceById(myPatientId).execute();
			fail();
		} catch (ResourceVersionConflictException e) {
			String output = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome());
			ourLog.info(output);
			assertThat(output, containsString("Note that cascading deletes are not active for this request. You can enable cascading deletes"));
		}
	}

	@Test
	public void testDeleteCascading() throws IOException {
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPatientId.getValue() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			assertThat(deleteResponse, containsString("Cascaded delete to "));
		}

		try {
			ourLog.info("Reading {}", myPatientId);
			myClient.read().resource(Patient.class).withId(myPatientId).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteCascadingWithOverridePathBasedReferentialIntegrityForDeletesInterceptorAlsoRegistered() throws IOException {
		ourRestServer.getInterceptorService().registerInterceptor(myOverridePathBasedReferentialIntegrityForDeletesInterceptor);
		try {

			createResources();

			ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

			HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPatientId.getValue() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
			delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
			try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
				assertEquals(200, response.getStatusLine().getStatusCode());
				String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info("Response: {}", deleteResponse);
				assertThat(deleteResponse, containsString("Cascaded delete to "));
			}

			try {
				ourLog.info("Reading {}", myPatientId);
				myClient.read().resource(Patient.class).withId(myPatientId).execute();
				fail();
			} catch (ResourceGoneException e) {
				// good
			}

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(myOverridePathBasedReferentialIntegrityForDeletesInterceptor);
		}
	}

	@Test
	public void testDeleteCascadingWithCircularReference() throws IOException {

		Organization o0 = new Organization();
		o0.setName("O0");
		IIdType o0id = myOrganizationDao.create(o0).getId().toUnqualifiedVersionless();

		Organization o1 = new Organization();
		o1.setName("O1");
		o1.getPartOf().setReference(o0id.getValue());
		IIdType o1id = myOrganizationDao.create(o1).getId().toUnqualifiedVersionless();

		o0.getPartOf().setReference(o1id.getValue());
		myOrganizationDao.update(o0);

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		HttpDelete delete = new HttpDelete(ourServerBase + "/Organization/" + o0id.getIdPart() + "?" + Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE + "&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			assertThat(deleteResponse, containsString("Cascaded delete to "));
		}

		try {
			ourLog.info("Reading {}", o0id);
			myClient.read().resource(Organization.class).withId(o0id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			ourLog.info("Reading {}", o1id);
			myClient.read().resource(Organization.class).withId(o1id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testDeleteCascadingByHeader() throws IOException {
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPatientId.getValue() + "?_pretty=true");
		delete.addHeader(Constants.HEADER_CASCADE, Constants.CASCADE_DELETE);
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			assertThat(deleteResponse, containsString("Cascaded delete to "));
		}

		try {
			ourLog.info("Reading {}", myPatientId);
			myClient.read().resource(Patient.class).withId(myPatientId).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}
}
