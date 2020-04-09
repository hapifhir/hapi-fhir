package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class CascadingDeleteInterceptorR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CascadingDeleteInterceptorR4Test.class);
	private IIdType myDiagnosticReportId;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private IIdType myPatientId;
	private CascadingDeleteInterceptor myDeleteInterceptor;
	private IIdType myObservationId;
	private IIdType myConditionId;
	private IIdType myEncounterId;

	@Override
	@Before
	public void before() throws Exception {
		super.before();

		myDeleteInterceptor = new CascadingDeleteInterceptor(myDaoRegistry, myInterceptorBroadcaster);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterInterceptor(myDeleteInterceptor);
	}

	public void createResources() {
		Patient p = new Patient();
		p.setActive(true);
		myPatientId = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Encounter e = new Encounter();
		e.setSubject(new Reference(myPatientId));
		myEncounterId = ourClient.create().resource(e).execute().getId().toUnqualifiedVersionless();

		CarePlan cp = new CarePlan();
		cp.setEncounter(new Reference(myEncounterId));
		ourClient.create().resource(cp).execute();

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSubject( new Reference(myPatientId));
		o.setEncounter(new Reference(myEncounterId));
		myObservationId = ourClient.create().resource(o).execute().getId().toUnqualifiedVersionless();

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
		dr.addResult().setReference(myObservationId.getValue());
		dr.setEncounter(new Reference(myEncounterId));
		myDiagnosticReportId = ourClient.create().resource(dr).execute().getId().toUnqualifiedVersionless();

		Condition condition = new Condition();
		condition.setSubject(new Reference(myPatientId));
		condition.setAsserter(new Reference(myPatientId));
		condition.setEncounter(new Reference(myEncounterId));
		myConditionId = ourClient.create().resource(condition).execute().getId().toUnqualifiedVersionless();
	}

	@Test
	public void testDeleteWithNoInterceptorAndConstraints() {
		createResources();

		try {
			ourClient.delete().resourceById(myPatientId).execute();
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
			ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
	}

	@Test
	public void testDeleteWithInterceptorAndConstraints() {
		createResources();

		ourRestServer.getInterceptorService().registerInterceptor(myDeleteInterceptor);

		try {
			ourClient.delete().resourceById(myPatientId).execute();
			fail();
		} catch (ResourceVersionConflictException e) {
			String output = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome());
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
			ourClient.read().resource(Patient.class).withId(myPatientId).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
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
			ourClient.read().resource(Organization.class).withId(o0id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
		try {
			ourLog.info("Reading {}", o1id);
			ourClient.read().resource(Organization.class).withId(o1id).execute();
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
			ourClient.read().resource(Patient.class).withId(myPatientId).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
