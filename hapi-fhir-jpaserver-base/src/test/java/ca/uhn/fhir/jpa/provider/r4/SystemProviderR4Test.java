package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.rp.r4.*;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.junit.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SystemProviderR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderR4Test.class);
	private static RestfulServer ourRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;

	@SuppressWarnings("deprecation")
	@After
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
	}

	@Before
	public void before() {
		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
	}

	@Before
	public void beforeStartServer() throws Exception {
		if (ourRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			QuestionnaireResourceProviderR4 questionnaireRp = new QuestionnaireResourceProviderR4();
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
			ServiceRequestResourceProvider diagnosticOrderRp = new ServiceRequestResourceProvider();
			diagnosticOrderRp.setDao(myServiceRequestDao);
			PractitionerResourceProvider practitionerRp = new PractitionerResourceProvider();
			practitionerRp.setDao(myPractitionerDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp, locationRp, binaryRp, diagnosticReportRp, diagnosticOrderRp, practitionerRp);

			restServer.setPlainProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forR4();
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
			ourRestServer = restServer;
		}

		ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		ourRestServer.setPagingProvider(myPagingProvider);
	}

	@Test
	public void testEverythingReturnsCorrectBundleType() throws Exception {
		ourRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);
		ourRestServer.setPagingProvider(new FifoMemoryPagingProvider(1).setDefaultPageSize(10));
		ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
		ourRestServer.registerInterceptor(interceptor);

		for (int i = 0; i < 11; i++) {
			Patient p = new Patient();
			p.addName().setFamily("Name" + i);
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
			assertEquals(BundleType.SEARCHSET, responseBundle.getTypeElement().getValue());

		} finally {
			http.close();
		}

		ourRestServer.unregisterInterceptor(interceptor);
	}

	@Test
	public void testEverythingReturnsCorrectFormatInPagingLink() throws Exception {
		ourRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);
		ourRestServer.setPagingProvider(new FifoMemoryPagingProvider(1).setDefaultPageSize(10));
		ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
		ourRestServer.registerInterceptor(interceptor);

		for (int i = 0; i < 11; i++) {
			Patient p = new Patient();
			p.addName().setFamily("Name" + i);
			ourClient.create().resource(p).execute();
		}

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);

		try {
			String response = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(response, containsString("_format=json"));
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			http.close();
		}

		ourRestServer.unregisterInterceptor(interceptor);
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
	public void testMarkResourcesForReindexing() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/$mark-all-resources-for-reindexing");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
			;
		}

		get = new HttpGet(ourServerBase + "/$perform-reindexing-pass");
		http = ourHttpClient.execute(get);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
			;
		}

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testResponseUsesCorrectContentType() throws Exception {
		ourRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet get = new HttpGet(ourServerBase);
//		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		assertThat(http.getFirstHeader("Content-Type").getValue(), containsString("application/fhir+json"));
	}


	@Transactional(propagation = Propagation.NEVER)
	@Test
	public void testSuggestKeywords() throws Exception {

		Patient patient = new Patient();
		patient.addName().setFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		obs.getSubject().setReferenceElement(ptId);
		IIdType obsId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.setId(obsId);
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		myObservationDao.update(obs, mySrd);

		// Try to wait for the indexing to complete
		waitForSize(2, ()-> fetchSuggestionCount(ptId));

		HttpGet get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything&searchParam=_content&text=zxc&_pretty=true&_format=xml");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			assertEquals(200, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);

			Parameters parameters = ourCtx.newXmlParser().parseResource(Parameters.class, output);
			assertEquals(2, parameters.getParameter().size());
			assertEquals("keyword", parameters.getParameter().get(0).getPart().get(0).getName());
			assertEquals(("ZXCVBNM"), ((StringType) parameters.getParameter().get(0).getPart().get(0).getValue()).getValueAsString());
			assertEquals("score", parameters.getParameter().get(0).getPart().get(1).getName());
			assertEquals(("1.0"), ((DecimalType) parameters.getParameter().get(0).getPart().get(1).getValue()).getValueAsString());

		} finally {
			http.close();
		}
	}

	private Number fetchSuggestionCount(IIdType thePtId) throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + thePtId.getIdPart() + "/$everything&searchParam=_content&text=zxc&_pretty=true&_format=xml");
		try (CloseableHttpResponse http = ourHttpClient.execute(get)) {
			assertEquals(200, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			Parameters parameters = ourCtx.newXmlParser().parseResource(Parameters.class, output);
			return parameters.getParameter().size();
		}
	}

	@Test
	public void testSuggestKeywordsInvalid() throws Exception {
		Patient patient = new Patient();
		patient.addName().setFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		myObservationDao.create(obs, mySrd);

		HttpGet get = new HttpGet(ourServerBase + "/$suggest-keywords");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'context' must be provided"));
		} finally {
			http.close();
		}

		get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything");
		http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'searchParam' must be provided"));
		} finally {
			http.close();
		}

		get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything&searchParam=aa");
		http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'text' must be provided"));
		} finally {
			http.close();
		}

	}

	@Test
	public void testTransactionCount() throws Exception {
		for (int i = 0; i < 20; i++) {
			Patient p = new Patient();
			p.addName().setFamily("PATIENT_" + i);
			myPatientDao.create(p, mySrd);
		}

		Bundle req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?_summary=count");
		Bundle resp = ourClient.transaction().withBundle(req).execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(1, resp.getEntry().size());
		Bundle respSub = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(20, respSub.getTotal());
		assertEquals(0, respSub.getEntry().size());
	}

	@Test
	public void testCountCache() {
		Patient patient = new Patient();
		patient.addName().setFamily("Unique762");
		myPatientDao.create(patient, mySrd);
		Bundle resp1 = (Bundle) ourClient.search().byUrl("Patient?name=Unique762&_summary=count").execute();
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp1));
		assertEquals(1, resp1.getTotal());
		Bundle resp2 = (Bundle) ourClient.search().byUrl("Patient?name=Unique762&_summary=count").execute();
		assertEquals(1, resp2.getTotal());
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp2));
	}

	@Test
	public void testTransactionCreateWithPreferHeader() throws Exception {

		Patient p = new Patient();
		p.setActive(true);

		Bundle req;
		Bundle resp;

		// No prefer header
		req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		resp = ourClient.transaction().withBundle(req).execute();
		assertEquals(null, resp.getEntry().get(0).getResource());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());

		// Prefer return=minimal
		mySimpleHeaderInterceptor.setHeaderName(Constants.HEADER_PREFER);
		mySimpleHeaderInterceptor.setHeaderValue(Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL);
		req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		resp = ourClient.transaction().withBundle(req).execute();
		assertEquals(null, resp.getEntry().get(0).getResource());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());

		// Prefer return=representation
		mySimpleHeaderInterceptor.setHeaderName(Constants.HEADER_PREFER);
		mySimpleHeaderInterceptor.setHeaderValue(Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		resp = ourClient.transaction().withBundle(req).execute();
		assertEquals(Patient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
	}


	@Test
	public void testTransactionReSavesPreviouslyDeletedResources() throws IOException {

		for (int i = 0; i < 10; i++) {
			ourLog.info("** Beginning pass {}", i);

			Bundle input = myFhirCtx.newJsonParser().parseResource(Bundle.class, IOUtils.toString(getClass().getResourceAsStream("/r4/createdeletebundle.json"), Charsets.UTF_8));
			ourClient.transaction().withBundle(input).execute();

			myPatientDao.read(new IdType("Patient/Patient1063259"));


			SearchParameterMap params = new SearchParameterMap();
			params.add("subject", new ReferenceParam("Patient1063259"));
			params.setLoadSynchronous(true);
			IBundleProvider result = myDiagnosticReportDao.search(params);
			assertEquals(1, result.size().intValue());

			deleteAllOfType("Binary");
			deleteAllOfType("Location");
			deleteAllOfType("DiagnosticReport");
			deleteAllOfType("Observation");
			deleteAllOfType("ServiceRequest");
			deleteAllOfType("Practitioner");
			deleteAllOfType("Patient");
			deleteAllOfType("Organization");

			try {
				myPatientDao.read(new IdType("Patient/Patient1063259"));
				fail();
			} catch (ResourceGoneException e) {
				// good
			}

			result = myDiagnosticReportDao.search(params);
			assertEquals(0, result.size().intValue());

		}

	}

	private void deleteAllOfType(String theType) {
		BundleUtil.toListOfResources(myFhirCtx, ourClient.search().forResource(theType).execute())
			.forEach(t -> {
				ourClient.delete().resourceById(t.getIdElement()).execute();
			});
	}

	@Test
	public void testTransactionDeleteWithDuplicateDeletes() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addName().setFamily("van de Heuvelcx85ioqWJbI").addGiven("Pietercx85ioqWJbI");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourClient.read().resource(Patient.class).withId(id);

		Bundle inputBundle = new Bundle();
		inputBundle.setType(BundleType.TRANSACTION);
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(id.getValue());
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(id.getValue());
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?name=Pietercx85ioqWJbI");
		String input = myFhirCtx.newXmlParser().encodeResourceToString(inputBundle);

		HttpPost req = new HttpPost(ourServerBase + "?_pretty=true");
		req.setEntity(new StringEntity(input, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));

		CloseableHttpResponse resp = ourHttpClient.execute(req);
		try {
			String encoded = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(encoded);

			assertThat(encoded, containsString("transaction-response"));

			Bundle response = myFhirCtx.newXmlParser().parseResource(Bundle.class, encoded);
			assertEquals(3, response.getEntry().size());

		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

		try {
			ourClient.read().resource(Patient.class).withId(id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testTransactionFromBundle() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {

		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdType id1_1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Provenance", id1_1.getResourceType());
		IdType id1_2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());
		IdType id1_3 = new IdType(resp.getEntry().get(2).getResponse().getLocation());
		IdType id1_4 = new IdType(resp.getEntry().get(3).getResponse().getLocation());

		/*
		 * Same bundle!
		 */

		bundleRes = SystemProviderR4Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdType id2_1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType id2_2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());
		IdType id2_3 = new IdType(resp.getEntry().get(2).getResponse().getLocation());
		IdType id2_4 = new IdType(resp.getEntry().get(3).getResponse().getLocation());

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
	@Ignore
	public void testTransactionFromBundle3() throws Exception {

		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/grahame-transaction.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle4() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/simone_bundle.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
		Bundle bundleResp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdType id = new IdType(bundleResp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", id.getResourceType());
		assertTrue(id.hasIdPart());
		assertTrue(id.isIdPartValidLong());
		assertTrue(id.hasVersionIdPart());
		assertTrue(id.isVersionIdPartValidLong());
	}

	@Test
	public void testTransactionFromBundle5() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/simone_bundle2.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		try {
			ourClient.transaction().withBundle(bundle).prettyPrint().execute();
			fail();
		} catch (InvalidRequestException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals("Invalid placeholder ID found: uri:uuid:bb0cd4bc-1839-4606-8c46-ba3069e69b1d - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", oo.getIssue().get(0).getDiagnostics());
			assertEquals("processing", oo.getIssue().get(0).getCode().toCode());
		}
	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/simone_bundle3.xml");
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
			p.addName().setFamily("PATIENT_" + i);
			myPatientDao.create(p, mySrd);
		}

		Bundle req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?");
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
	public void testTransactionWithIncompleteBundle() throws Exception {
		Patient patient = new Patient();
		patient.setGender(AdministrativeGender.MALE);

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle.addEntry().setResource(patient);

		try {
			ourClient.transaction().withBundle(bundle).prettyPrint().execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.toString(), containsString("missing or invalid HTTP Verb"));
		}
	}

	@Test
	public void testTransactionWithInlineConditionalUrl() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addName().setFamily("van de Heuvelcx85ioqWJbI").addGiven("Pietercx85ioqWJbI");
		myPatientDao.create(p, mySrd);

		Organization o = new Organization();
		o.addIdentifier().setSystem("urn:oid:2.16.840.1.113883.2.4.6.1").setValue("07-8975469");
		myOrganizationDao.create(o, mySrd);

		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"    <id value=\"20160113160203\"/>\n" +
			"    <type value=\"transaction\"/>\n" +
			"    <entry>\n" +
			"        <fullUrl value=\"urn:uuid:c72aa430-2ddc-456e-7a09-dea8264671d8\"/>\n" +
			"        <resource>\n" +
			"            <Encounter>\n" +
			"                <identifier>\n" +
			"                    <use value=\"official\"/>\n" +
			"                    <system value=\"http://healthcare.example.org/identifiers/encounter\"/>\n" +
			"                    <value value=\"845962.8975469\"/>\n" +
			"                </identifier>\n" +
			"                <status value=\"in-progress\"/>\n" +
			"                <class value=\"inpatient\"/>\n" +
			"                <patient>\n" +
			"                    <reference value=\"Patient?family=van%20de%20Heuvelcx85ioqWJbI&amp;given=Pietercx85ioqWJbI\"/>\n" +
			"                </patient>\n" +
			"                <serviceProvider>\n" +
			"                    <reference value=\"Organization?identifier=urn:oid:2.16.840.1.113883.2.4.6.1|07-8975469\"/>\n" +
			"                </serviceProvider>\n" +
			"            </Encounter>\n" +
			"        </resource>\n" +
			"        <request>\n" +
			"            <method value=\"POST\"/>\n" +
			"            <url value=\"Encounter\"/>\n" +
			"        </request>\n" +
			"    </entry>\n" +
			"</Bundle>";
		//@formatter:off

		HttpPost req = new HttpPost(ourServerBase);
		req.setEntity(new StringEntity(input, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));

		CloseableHttpResponse resp = ourHttpClient.execute(req);
		try {
			String encoded = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(encoded);

			assertThat(encoded, containsString("transaction-response"));
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

	}

	/**
	 * FOrmat has changed, source is no longer valid
	 */
	@Test
	@Ignore
	public void testValidateUsingIncomingResources() throws Exception {
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.addValidatorModule(val);
		interceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		interceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		ourRestServer.registerInterceptor(interceptor);
		try {

			InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/questionnaire-sdc-profile-example-ussg-fht.xml");
			String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);

			HttpPost req = new HttpPost(ourServerBase);
			req.setEntity(new StringEntity(bundleStr, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));

			CloseableHttpResponse resp = ourHttpClient.execute(req);
			try {
				String encoded = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info(encoded);

				//@formatter:off
				assertThat(encoded, containsString("Questionnaire/54127-6/_history/"));
				//@formatter:on

				for (Header next : resp.getHeaders(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_NAME)) {
					ourLog.info(next.toString());
				}
			} finally {
				IOUtils.closeQuietly(resp.getEntity().getContent());
			}
		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}
	}

	@Test()
	public void testEndpointInterceptorIsCalledForTransaction() {
		// Just to get this out of the way
		ourClient.forceConformanceCheck();

		// Register an interceptor on the response
		AtomicBoolean called = new AtomicBoolean(false);
		Object interceptor = new Object() {
			@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
			public void outgoing() {
				called.set(true);
			}
		};
		ourRestServer.getInterceptorService().registerInterceptor(interceptor);
		try {

			Patient p = new Patient();
			p.addName().setFamily("Test");

			Bundle b = new Bundle();
			b.setType(Bundle.BundleType.TRANSACTION);
			b.addEntry()
				.setResource(p)
				.setFullUrl("Patient")
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");

			ourClient.transaction().withBundle(b).execute();

			assertTrue(called.get());

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(interceptor);

		}
	}



	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
