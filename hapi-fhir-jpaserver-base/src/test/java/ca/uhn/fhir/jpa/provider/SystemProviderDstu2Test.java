package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.rp.dstu2.BinaryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.DiagnosticOrderResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.DiagnosticReportResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.LocationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.PatientResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu2.PractitionerResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SystemProviderDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderDstu2Test.class);
	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;

	@BeforeEach
	public void beforeStartServer() throws Exception {
		if (myRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			QuestionnaireResourceProviderDstu2 questionnaireRp = new QuestionnaireResourceProviderDstu2();
			questionnaireRp.setDao(myQuestionnaireDao);

			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(myObservationDao);

			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(myOrganizationDao);

			LocationResourceProvider locationRp = new LocationResourceProvider();
			locationRp.setDao(myLocationDao);

			BinaryResourceProvider binaryRp = new BinaryResourceProvider();
			binaryRp.setDao(myBinaryDao);

			DiagnosticReportResourceProvider diagnosticReportRp = new DiagnosticReportResourceProvider();
			diagnosticReportRp.setDao(myDiagnosticReportDao);
			DiagnosticOrderResourceProvider diagnosticOrderRp = new DiagnosticOrderResourceProvider();
			diagnosticOrderRp.setDao(myDiagnosticOrderDao);
			PractitionerResourceProvider practitionerRp = new PractitionerResourceProvider();
			practitionerRp.setDao(myPractitionerDao);


			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(10));
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp, binaryRp, locationRp, diagnosticReportRp, diagnosticOrderRp, practitionerRp);

			restServer.setPlainProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forDstu2Cached();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
            int myPort = JettyUtil.getPortForStartedServer(ourServer);
            ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
			HttpClientBuilder builder = HttpClientBuilder.create();
			builder.setConnectionManager(connectionManager);
			ourHttpClient = builder.build();

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
			ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
			ourClient.setLogRequestAndResponse(true);
			myRestServer = restServer;
		}
	}

	@Test
	public void testEverythingReturnsCorrectFormatInPagingLink() throws Exception {
		myRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);
		myRestServer.setPagingProvider(new FifoMemoryPagingProvider(1).setDefaultPageSize(10));
		ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
		myRestServer.registerInterceptor(interceptor);

		for (int i = 0; i < 11; i++) {
			Patient p = new Patient();
			p.addName().addFamily("Name" + i);
			ourClient.create().resource(p).execute();
		}

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			String response = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(response, (containsString("_format=json")));
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			http.close();
		}

		myRestServer.unregisterInterceptor(interceptor);
	}

	@Test
	public void testEverythingReturnsCorrectBundleType() throws Exception {
		myRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);
		myRestServer.setPagingProvider(new FifoMemoryPagingProvider(1).setDefaultPageSize(10));
		ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
		myRestServer.registerInterceptor(interceptor);

		for (int i = 0; i < 11; i++) {
			Patient p = new Patient();
			p.addName().addFamily("Name" + i);
			ourClient.create().resource(p).execute();
		}

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		get.addHeader("Accept", "application/xml+fhir");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			String response = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(response, not(containsString("_format")));
			assertEquals(200, http.getStatusLine().getStatusCode());

			Bundle responseBundle = ourCtx.newXmlParser().parseResource(Bundle.class, response);
			assertEquals(BundleTypeEnum.SEARCH_RESULTS, responseBundle.getTypeElement().getValueAsEnum());

		} finally {
			http.close();
		}

		myRestServer.unregisterInterceptor(interceptor);
	}

	@Test
	public void testEverythingType() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			http.close();
		}
	}


	@Test
	public void testGetOperationDefinition() {
		OperationDefinition op = ourClient.read(OperationDefinition.class, "-s-get-resource-counts");
		assertEquals("get-resource-counts", op.getCode());
	}

	@Test
	@Disabled
	public void testTransactionReSavesPreviouslyDeletedResources() throws IOException {

		for (int i = 0; i < 10; i++) {
			ourLog.info("** Beginning pass {}", i);

			Bundle input = myFhirContext.newJsonParser().parseResource(Bundle.class, IOUtils.toString(getClass().getResourceAsStream("/dstu2/createdeletebundle.json"), Charsets.UTF_8));
			ourClient.transaction().withBundle(input).execute();

			myPatientDao.read(new IdType("Patient/Patient1063259"));

			deleteAllOfType("Binary");
			deleteAllOfType("Location");
			deleteAllOfType("DiagnosticReport");
			deleteAllOfType("Observation");
			deleteAllOfType("DiagnosticOrder");
			deleteAllOfType("Practitioner");
			deleteAllOfType("Patient");
			deleteAllOfType("Organization");

			try {
				myPatientDao.read(new IdType("Patient/Patient1063259"));
				fail();
			} catch (ResourceGoneException e) {
				// good
			}

		}

	}

	private void deleteAllOfType(String theType) {
		BundleUtil.toListOfResources(myFhirContext, ourClient.search().forResource(theType).execute())
			.forEach(t -> {
				ourClient.delete().resourceById(t.getIdElement()).execute();
			});
	}

	@Test
	public void testTransactionFromBundle() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdDt id1_1 = new IdDt(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Provenance", id1_1.getResourceType());
		IdDt id1_2 = new IdDt(resp.getEntry().get(1).getResponse().getLocation());
		IdDt id1_3 = new IdDt(resp.getEntry().get(2).getResponse().getLocation());
		IdDt id1_4 = new IdDt(resp.getEntry().get(3).getResponse().getLocation());

		/*
		 * Same bundle!
		 */

		bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdDt id2_1 = new IdDt(resp.getEntry().get(0).getResponse().getLocation());
		IdDt id2_2 = new IdDt(resp.getEntry().get(1).getResponse().getLocation());
		IdDt id2_3 = new IdDt(resp.getEntry().get(2).getResponse().getLocation());
		IdDt id2_4 = new IdDt(resp.getEntry().get(3).getResponse().getLocation());

		assertNotEquals(id1_1.toVersionless(), id2_1.toVersionless());
		assertEquals("Provenance", id2_1.getResourceType());
		assertEquals(id1_2.toVersionless(), id2_2.toVersionless());
		assertEquals(id1_3.toVersionless(), id2_3.toVersionless());
		assertEquals(id1_4.toVersionless(), id2_4.toVersionless());
	}

	/**
	 * This is Gramahe's test transaction - it requires some set up in order to work
	 */
	@Test
	@Disabled
	public void testTransactionFromBundle3() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/grahame-transaction.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle4() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
		Bundle bundleResp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdDt id = new IdDt(bundleResp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", id.getResourceType());
		assertTrue(id.hasIdPart());
		assertTrue(id.isIdPartValidLong());
		assertTrue(id.hasVersionIdPart());
		assertTrue(id.isVersionIdPartValidLong());
	}

	@Test
	public void testTransactionFromBundle5() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle2.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		try {
			ourClient.transaction().withBundle(bundle).prettyPrint().execute();
			fail();
		} catch (InvalidRequestException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals(Msg.code(533) + "Invalid placeholder ID found: uri:uuid:bb0cd4bc-1839-4606-8c46-ba3069e69b1d - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", oo.getIssue().get(0).getDiagnostics());
			assertEquals("processing", oo.getIssue().get(0).getCode());
		}
	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		// try {
		// fail();
		// } catch (InvalidRequestException e) {
		// OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
		// assertEquals("Invalid placeholder ID found: uri:uuid:bb0cd4bc-1839-4606-8c46-ba3069e69b1d - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", oo.getIssue().get(0).getDiagnostics());
		// assertEquals("processing", oo.getIssue().get(0).getCode());
		// }
	}

	@Test
	public void testTransactionSearch() throws Exception {
		for (int i = 0; i < 20; i++) {
			Patient p = new Patient();
			p.addName().addFamily("PATIENT_" + i);
			myPatientDao.create(p, mySrd);
		}

		Bundle req = new Bundle();
		req.setType(BundleTypeEnum.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?");
		Bundle resp = ourClient.transaction().withBundle(req).execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(1, resp.getEntry().size());
		Bundle respSub = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals("self", respSub.getLink().get(0).getRelation());
		assertEquals(ourServerBase + "/Patient", respSub.getLink().get(0).getUrl());
		assertEquals("next", respSub.getLink().get(1).getRelation());
		assertThat(respSub.getLink().get(1).getUrl(), containsString("/fhir/context?_getpages"));
		assertThat(respSub.getEntry().get(0).getFullUrl(), startsWith(ourServerBase + "/Patient/"));
		assertEquals(Patient.class, respSub.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testTransactionCount() throws Exception {
		for (int i = 0; i < 20; i++) {
			Patient p = new Patient();
			p.addName().addFamily("PATIENT_" + i);
			myPatientDao.create(p, mySrd);
		}

		Bundle req = new Bundle();
		req.setType(BundleTypeEnum.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerbEnum.GET).setUrl("Patient?_summary=count");
		Bundle resp = ourClient.transaction().withBundle(req).execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(1, resp.getEntry().size());
		Bundle respSub = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(20, respSub.getTotal().intValue());
		assertEquals(0, respSub.getEntry().size());
	}

	@Test
	public void testMarkResourcesForReindexing() throws Exception {
		HttpPost post = new HttpPost(ourServerBase + "/$mark-all-resources-for-reindexing");
		CloseableHttpResponse http = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}


}
