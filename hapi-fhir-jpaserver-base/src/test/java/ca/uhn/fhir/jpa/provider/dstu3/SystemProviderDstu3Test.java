package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.rp.dstu3.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.PatientResourceProvider;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.validation.ResultSeverityEnum;

public class SystemProviderDstu3Test extends BaseJpaDstu3Test {

	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderDstu3Test.class);
	private static Server ourServer;
	private static String ourServerBase;

	@Before
	public void beforeStartServer() throws Exception {
		if (myRestServer == null) {
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);

			QuestionnaireResourceProviderDstu3 questionnaireRp = new QuestionnaireResourceProviderDstu3();
			questionnaireRp.setDao(myQuestionnaireDao);

			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(myObservationDao);

			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(myOrganizationDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(10));
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

			restServer.setPlainProviders(mySystemProvider);

			int myPort = RandomServerPortProvider.findFreePort();
			ourServer = new Server(myPort);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forDstu3();
			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			ourServer.start();

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

	@SuppressWarnings("deprecation")
	@After
	public void after() {
		myRestServer.setUseBrowserFriendlyContentTypes(true);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testResponseUsesCorrectContentType() throws Exception {
		myRestServer.setUseBrowserFriendlyContentTypes(true);
		myRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet get = new HttpGet(ourServerBase);
//		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		assertThat(http.getFirstHeader("Content-Type").getValue(), containsString("application/json+fhir"));
	}
	

	
	@Test
	public void testValidateUsingIncomingResources() throws Exception {
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.addValidatorModule(val);
		interceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		interceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myRestServer.registerInterceptor(interceptor);
		try {
	
			InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/questionnaire-sdc-profile-example-ussg-fht.xml");
			String bundleStr = IOUtils.toString(bundleRes);
	
			HttpPost req = new HttpPost(ourServerBase);
			req.setEntity(new StringEntity(bundleStr, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));
			
			CloseableHttpResponse resp = ourHttpClient.execute(req);
	
			String encoded = IOUtils.toString(resp.getEntity().getContent());
			IOUtils.closeQuietly(resp.getEntity().getContent());
			ourLog.info(encoded);
	
			//@formatter:off
			assertThat(encoded, containsString("Questionnaire/54127-6/_history/")); 
			//@formatter:on

			for (Header next : resp.getHeaders(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_NAME)) {
				ourLog.info(next.toString());
			}
			
		} finally {
			myRestServer.unregisterInterceptor(interceptor);
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
			String response = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(response);
			assertThat(response, not(containsString("_format")));
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
			String response = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(response);
			assertThat(response, not(containsString("_format")));
			assertEquals(200, http.getStatusLine().getStatusCode());
			
			Bundle responseBundle = ourCtx.newXmlParser().parseResource(Bundle.class, response);
			assertEquals(BundleType.COLLECTION, responseBundle.getTypeElement().getValue());
			
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

	@Transactional(propagation=Propagation.NEVER)
	@Test
	public void testSuggestKeywords() throws Exception {
		
		Patient patient = new Patient();
		patient.addName().addFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		obs.getSubject().setReferenceElement(ptId);
		IIdType obsId = myObservationDao.create(obs, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.setId(obsId);
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		myObservationDao.update(obs, new ServletRequestDetails());
		
		HttpGet get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything&searchParam=_content&text=zxc&_pretty=true&_format=xml");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			assertEquals(200, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(output);
			
			Parameters parameters = ourCtx.newXmlParser().parseResource(Parameters.class, output);
			assertEquals(2, parameters.getParameter().size());
			assertEquals("keyword", parameters.getParameter().get(0).getPart().get(0).getName());
			assertEquals(("ZXCVBNM"), ((StringType)parameters.getParameter().get(0).getPart().get(0).getValue()).getValueAsString());
			assertEquals("score", parameters.getParameter().get(0).getPart().get(1).getName());
			assertEquals(("1.0"), ((DecimalType)parameters.getParameter().get(0).getPart().get(1).getValue()).getValueAsString());
			
		} finally {
			http.close();
		}
	}

	@Test
	public void testSuggestKeywordsInvalid() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("testSuggest");
		IIdType ptId = myPatientDao.create(patient, new ServletRequestDetails()).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(ptId);
		obs.getCode().setText("ZXCVBNM ASDFGHJKL QWERTYUIOPASDFGHJKL");
		myObservationDao.create(obs, new ServletRequestDetails());
		
		HttpGet get = new HttpGet(ourServerBase + "/$suggest-keywords");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'context' must be provided"));
		} finally {
			http.close();
		}
		
		get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything");
		http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'searchParam' must be provided"));
		} finally {
			http.close();
		}

		get = new HttpGet(ourServerBase + "/$suggest-keywords?context=Patient/" + ptId.getIdPart() + "/$everything&searchParam=aa");
		http = ourHttpClient.execute(get);
		try {
			assertEquals(400, http.getStatusLine().getStatusCode());
			String output = IOUtils.toString(http.getEntity().getContent());
			ourLog.info(output);
			assertThat(output, containsString("Parameter 'text' must be provided"));
		} finally {
			http.close();
		}

	}

	@Test
	public void testGetOperationDefinition() {
		OperationDefinition op = ourClient.read(OperationDefinition.class, "get-resource-counts");
		assertEquals("$get-resource-counts", op.getCode());
	}

	@Test
	public void testTransactionFromBundle() throws Exception {
		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
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
			assertThat(e.toString(), containsString(""));
		}
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {

		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		String bundle = IOUtils.toString(bundleRes);
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

		bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		bundle = IOUtils.toString(bundleRes);
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
	// @Test
	public void testTransactionFromBundle3() throws Exception {

		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/grahame-transaction.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle4() throws Exception {
		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/simone_bundle.xml");
		String bundle = IOUtils.toString(bundleRes);
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
		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/simone_bundle2.xml");
		String bundle = IOUtils.toString(bundleRes);
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
		InputStream bundleRes = SystemProviderDstu3Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes);
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
		for (int i = 0; i < 20; i ++) {
			Patient p = new Patient();
			p.addName().addFamily("PATIENT_" + i);
			myPatientDao.create(p, new ServletRequestDetails());
		}
		
		Bundle req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?");
		Bundle resp = ourClient.transaction().withBundle(req).execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		
		assertEquals(1, resp.getEntry().size());
		Bundle respSub = (Bundle)resp.getEntry().get(0).getResource();
		assertEquals("self", respSub.getLink().get(0).getRelation());
		assertEquals(ourServerBase + "/Patient", respSub.getLink().get(0).getUrl());
		assertEquals("next", respSub.getLink().get(1).getRelation());
		assertThat(respSub.getLink().get(1).getUrl(), containsString("/fhir/context?_getpages"));
		assertThat(respSub.getEntry().get(0).getFullUrl(), startsWith(ourServerBase + "/Patient/"));
		assertEquals(Patient.class, respSub.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testTransactionCount() throws Exception {
		for (int i = 0; i < 20; i ++) {
			Patient p = new Patient();
			p.addName().addFamily("PATIENT_" + i);
			myPatientDao.create(p, new ServletRequestDetails());
		}
		
		Bundle req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?_summary=count");
		Bundle resp = ourClient.transaction().withBundle(req).execute();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		
		assertEquals(1, resp.getEntry().size());
		Bundle respSub = (Bundle)resp.getEntry().get(0).getResource();
		assertEquals(20, respSub.getTotal());
		assertEquals(0, respSub.getEntry().size());
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

}
