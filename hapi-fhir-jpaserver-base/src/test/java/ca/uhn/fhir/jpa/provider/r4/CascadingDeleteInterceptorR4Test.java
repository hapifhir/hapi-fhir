package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
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
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
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

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.getSubject().setReference(myPatientId.getValue());
		myObservationId = ourClient.create().resource(o).execute().getId().toUnqualifiedVersionless();

		DiagnosticReport dr = new DiagnosticReport();
		dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
		dr.addResult().setReference(myObservationId.getValue());
		myDiagnosticReportId = ourClient.create().resource(dr).execute().getId().toUnqualifiedVersionless();
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

		HttpDelete delete = new HttpDelete(ourServerBase + "/" + myPatientId.getValue() + "?_cascade=true&_pretty=true");
		delete.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String deleteResponse = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", deleteResponse);
			assertThat(deleteResponse, containsString("Cascaded delete to 2 resources: [" + myDiagnosticReportId + "/_history/1, " + myObservationId + "/_history/1]"));
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
