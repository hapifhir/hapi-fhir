package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.rp.r4.BinaryResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.DiagnosticReportResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.LocationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.PractitionerResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.PractitionerRoleResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ServiceRequestResourceProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizedList;
import ca.uhn.fhir.rest.server.interceptor.auth.SearchNarrowingInterceptor;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;


public class SystemProviderR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderR4Test.class);
	private static RestfulServer ourRestServer;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static Server ourServer;
	private static String ourServerBase;
	private IGenericClient myClient;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;

	@Autowired
	private DeleteExpungeProvider myDeleteExpungeProvider;

	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		myClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setDeleteExpungeEnabled(new JpaStorageSettings().isDeleteExpungeEnabled());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new JpaStorageSettings().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
	}

	@BeforeEach
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

			PractitionerRoleResourceProvider practitionerRoleRp = new PractitionerRoleResourceProvider();
			practitionerRoleRp.setDao(myPractitionerRoleDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp, locationRp, binaryRp, diagnosticReportRp, diagnosticOrderRp, practitionerRp, practitionerRoleRp);

			restServer.registerProviders(mySystemProvider, myDeleteExpungeProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forR4Cached();
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
			ourRestServer = restServer;
		}

		myClient = ourCtx.newRestfulGenericClient(ourServerBase);
		myClient.setLogRequestAndResponse(true);
		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		myClient.registerInterceptor(mySimpleHeaderInterceptor);

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
			myClient.create().resource(p).execute();
		}

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		get.addHeader("Accept", "application/xml+fhir");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		try {
			String response = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(response).doesNotContain("_format");
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
			myClient.create().resource(p).execute();
		}

		HttpGet get = new HttpGet(ourServerBase + "/Patient/$everything");
		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);

		try {
			String response = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertThat(response).contains("_format=json");
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
		OperationDefinition op = myClient.read(OperationDefinition.class, "-s-get-resource-counts");
		assertEquals("get-resource-counts", op.getCode());
	}

	@Test
	public void testMarkResourcesForReindexing() throws Exception {
		HttpRequestBase post = new HttpPost(ourServerBase + "/$mark-all-resources-for-reindexing");
		CloseableHttpResponse http = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
		}

		post = new HttpPost(ourServerBase + "/$perform-reindexing-pass");
		http = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
		}

	}

	@Test
	public void testMarkResourcesForReindexingTyped() throws Exception {

		HttpPost post = new HttpPost(ourServerBase + "/$mark-all-resources-for-reindexing?type=Patient");
		post.setEntity(new ResourceEntity(myFhirContext, new Parameters().addParameter("type", new CodeType("Patient"))));
		CloseableHttpResponse http = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(200, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
		}

		post = new HttpPost(ourServerBase + "/$mark-all-resources-for-reindexing?type=FOO");
		post.setEntity(new ResourceEntity(myFhirContext, new Parameters().addParameter("type", new CodeType("FOO"))));
		http = ourHttpClient.execute(post);
		try {
			String output = IOUtils.toString(http.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(output);
			assertEquals(400, http.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(http);
		}

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testResponseUsesCorrectContentType() throws Exception {
		ourRestServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet get = new HttpGet(ourServerBase);
//		get.addHeader("Accept", "application/xml, text/html");
		CloseableHttpResponse http = ourHttpClient.execute(get);
		assertThat(http.getFirstHeader("Content-Type").getValue()).contains("application/fhir+json");
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
		Bundle resp = myClient.transaction().withBundle(req).execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertThat(resp.getEntry()).hasSize(1);
		Bundle respSub = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(20, respSub.getTotal());
		assertThat(respSub.getEntry()).isEmpty();
	}

	@Test
	public void testCountCache() {
		Patient patient = new Patient();
		patient.addName().setFamily("Unique762");
		myPatientDao.create(patient, mySrd);
		Bundle resp1 = (Bundle) myClient.search().byUrl("Patient?name=Unique762&_summary=count").execute();
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp1));
		assertEquals(1, resp1.getTotal());
		Bundle resp2 = (Bundle) myClient.search().byUrl("Patient?name=Unique762&_summary=count").execute();
		assertEquals(1, resp2.getTotal());
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp2));
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
		resp = myClient.transaction().withBundle(req).execute();
		assertNull(resp.getEntry().get(0).getResource());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());

		// Prefer return=minimal
		mySimpleHeaderInterceptor.setHeaderName(Constants.HEADER_PREFER);
		mySimpleHeaderInterceptor.setHeaderValue(Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL);
		req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		resp = myClient.transaction().withBundle(req).execute();
		assertNull(resp.getEntry().get(0).getResource());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());

		// Prefer return=representation
		mySimpleHeaderInterceptor.setHeaderName(Constants.HEADER_PREFER);
		mySimpleHeaderInterceptor.setHeaderValue(Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		req = new Bundle();
		req.setType(BundleType.TRANSACTION);
		req.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");
		resp = myClient.transaction().withBundle(req).execute();
		assertEquals(Patient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
	}


	@Test
	public void testTransactionReSavesPreviouslyDeletedResources() throws IOException {

		for (int i = 0; i < 10; i++) {
			ourLog.info("** Beginning pass {}", i);

			Bundle input = myFhirContext.newJsonParser().parseResource(Bundle.class, IOUtils.toString(getClass().getResourceAsStream("/r4/createdeletebundle.json"), Charsets.UTF_8));
			myClient.transaction().withBundle(input).execute();

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
		BundleUtil.toListOfResources(myFhirContext, myClient.search().forResource(theType).execute())
			.forEach(t -> {
				myClient.delete().resourceById(t.getIdElement()).execute();
			});
	}

	@Test
	public void testTransactionDeleteWithDuplicateDeletes() throws Exception {
		myStorageSettings.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addName().setFamily("van de Heuvelcx85ioqWJbI").addGiven("Pietercx85ioqWJbI");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		myClient.read().resource(Patient.class).withId(id);

		Bundle inputBundle = new Bundle();
		inputBundle.setType(BundleType.TRANSACTION);
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(id.getValue());
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(id.getValue());
		inputBundle.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?name=Pietercx85ioqWJbI");
		String input = myFhirContext.newXmlParser().encodeResourceToString(inputBundle);

		HttpPost req = new HttpPost(ourServerBase + "?_pretty=true");
		req.setEntity(new StringEntity(input, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));

		CloseableHttpResponse resp = ourHttpClient.execute(req);
		try {
			String encoded = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(encoded);

			assertThat(encoded).contains("transaction-response");

			Bundle response = myFhirContext.newXmlParser().parseResource(Bundle.class, encoded);
			assertThat(response.getEntry()).hasSize(3);

		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

		try {
			myClient.read().resource(Patient.class).withId(id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * Ensure that the interceptor is called an appropriate number of times
	 */
	@Test
	public void testBatchWithMultipleConditionalCreates() {

		AtomicInteger counter0 = new AtomicInteger(0);
		AtomicInteger counter1 = new AtomicInteger(0);
		AtomicInteger counter2 = new AtomicInteger(0);

		class MyAnonymousInterceptor0 implements IAnonymousInterceptor {

			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				int count = counter0.incrementAndGet();
				ourLog.info("counter0 have been called {} times", count);
			}
		}

		class MyAnonymousInterceptor1 implements IAnonymousInterceptor {

			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				int count = counter1.incrementAndGet();
				ourLog.info("counter1 have been called {} times", count);
			}
		}

		class MySearchNarrowingInterceptor extends SearchNarrowingInterceptor {
			private static final Logger ourLog = LoggerFactory.getLogger(MySearchNarrowingInterceptor.class);


			@Override
			protected AuthorizedList buildAuthorizedList(RequestDetails theRequestDetails) {
				int count = counter2.incrementAndGet();
				ourLog.info("Have been called {} times", count);
				return super.buildAuthorizedList(theRequestDetails);
			}
		}

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.setType("batch");

		for (int i = 0; i < 5; i++) {
			Practitioner p0 = new Practitioner();
			p0.addIdentifier().setSystem("sys").setValue("p" + i);
			bb.addTransactionCreateEntry(p0).conditional("Practitioner?identifier=sys|p" + i);
		}

		// Avoid the auto-CS fetching affecting counts
		myClient.capabilities().ofType(CapabilityStatement.class).execute();

		Bundle input = (Bundle) bb.getBundle();

		MyAnonymousInterceptor0 interceptor0 = new MyAnonymousInterceptor0();
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor0);
		MyAnonymousInterceptor1 interceptor1 = new MyAnonymousInterceptor1();
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED, interceptor1);
		MySearchNarrowingInterceptor interceptor2 = new MySearchNarrowingInterceptor();
		interceptor2.setNarrowConditionalUrls(true);
		ourRestServer.getInterceptorService().registerInterceptor(interceptor2);
		try {
			myClient.transaction().withBundle(input).execute();
			assertEquals(1, counter0.get());
			assertEquals(1, counter1.get());
			assertEquals(1, counter2.get());

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(interceptor1);
		}
	}


	@Test
	public void testTransactionWithModifyingInterceptor() {

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Patient pt = new Patient();
		pt.setId("A");
		pt.setActive(true);
		bb.addTransactionUpdateEntry(pt);
		Bundle input = (Bundle) bb.getBundle();

		IAnonymousInterceptor interceptor = new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				Bundle transactionBundle = (Bundle) theArgs.get(IBaseBundle.class);

				Patient pt = new Patient();
				pt.setId("B");
				pt.setActive(false);
				transactionBundle
					.addEntry()
					.setResource(pt)
					.getRequest()
					.setMethod(HTTPVerb.PUT)
					.setUrl("Patient/B");

			}
		};

		Bundle output;
		try {
			myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_TRANSACTION_PROCESSING, interceptor);
			output = mySystemDao.transaction(mySrd, input);
		}finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}

		assertThat(output.getEntry()).hasSize(2);
		assertEquals("A", new IdType(output.getEntry().get(0).getResponse().getLocation()).getIdPart());
		assertEquals("B", new IdType(output.getEntry().get(1).getResponse().getLocation()).getIdPart());
	}


	@Test
	public void testTransactionFromBundle() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = myClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {

		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/transaction_link_patient_eve_temp.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = myClient.transaction().withBundle(bundle).prettyPrint().execute();
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
		response = myClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);

		resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		IdType id2_1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType id2_2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());
		IdType id2_3 = new IdType(resp.getEntry().get(2).getResponse().getLocation());
		IdType id2_4 = new IdType(resp.getEntry().get(3).getResponse().getLocation());

		assertThat(id2_1.toVersionless()).isNotEqualTo(id1_1.toVersionless());
		assertEquals("Provenance", id2_1.getResourceType());
		assertEquals(id1_2.toVersionless(), id2_2.toVersionless());
		assertEquals(id1_3.toVersionless(), id2_3.toVersionless());
		assertEquals(id1_4.toVersionless(), id2_4.toVersionless());
	}

	@Test
	public void testTransactionFromBundle4() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/simone_bundle.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		String response = myClient.transaction().withBundle(bundle).prettyPrint().execute();
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
			myClient.transaction().withBundle(bundle).prettyPrint().execute();
			fail();
		} catch (InvalidRequestException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals(Msg.code(533) + "Invalid placeholder ID found: uri:uuid:bb0cd4bc-1839-4606-8c46-ba3069e69b1d - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", oo.getIssue().get(0).getDiagnostics());
			assertEquals("processing", oo.getIssue().get(0).getCode().toCode());
		}
	}

	@Test
	@Disabled("Stress test only")
	public void testTransactionWithPlaceholderIds() {


		for (int pass = 0; pass < 10000; pass++) {
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			for (int i = 0; i < 100; i++) {
				Patient pt = new Patient();
				pt.setId(org.hl7.fhir.dstu3.model.IdType.newRandomUuid());
				pt.addIdentifier().setSystem("http://foo").setValue("val" + i);
				bb.addTransactionCreateEntry(pt);

				Observation obs = new Observation();
				obs.setId(org.hl7.fhir.dstu3.model.IdType.newRandomUuid());
				obs.setSubject(new Reference(pt.getId()));
				bb.addTransactionCreateEntry(obs);
			}
			Bundle bundle = (Bundle) bb.getBundle();
			ourLog.info("Starting pass {}", pass);
			mySystemDao.transaction(null, bundle);
		}

	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		myClient.transaction().withBundle(bundle).prettyPrint().execute();
		// try {
		// fail();		// } catch (InvalidRequestException e) {
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
		Bundle resp = myClient.transaction().withBundle(req).execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertThat(resp.getEntry()).hasSize(1);
		Bundle respSub = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals("self", respSub.getLink().get(0).getRelation());
		assertEquals(ourServerBase + "/Patient", respSub.getLink().get(0).getUrl());
		assertEquals("next", respSub.getLink().get(1).getRelation());
		assertThat(respSub.getLink().get(1).getUrl()).contains("/fhir/context?_getpages");
		assertThat(respSub.getEntry().get(0).getFullUrl()).startsWith(ourServerBase + "/Patient/");
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
			myClient.transaction().withBundle(bundle).prettyPrint().execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.toString()).contains("missing or invalid HTTP Verb");
		}
	}

	@Test
	public void testTransactionWithInlineConditionalUrl() throws Exception {
		myStorageSettings.setAllowInlineMatchUrlReferences(true);

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

			assertThat(encoded).contains("transaction-response");
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}

	}

	/**
	 * FOrmat has changed, source is no longer valid
	 */
	@Test
	@Disabled("input file needs to be upgraded to R4 format")
	public void testValidateUsingIncomingResources() throws Exception {
		FhirInstanceValidator val = new FhirInstanceValidator(myValidationSupport);
		RequestValidatingInterceptor interceptor = new RequestValidatingInterceptor();
		interceptor.addValidatorModule(val);
		interceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		interceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		ourRestServer.registerInterceptor(interceptor);
		try {

			InputStream bundleRes = SystemProviderR4Test.class.getResourceAsStream("/questionnaire-sdc-profile-example-ussg-fht.xml");
			String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);

			HttpPost req = new HttpPost(ourServerBase);
			req.setEntity(new StringEntity(bundleStr, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));

			CloseableHttpResponse resp = ourHttpClient.execute(req);
			try {
				String encoded = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info(encoded);

				//@formatter:off
				assertThat(encoded).contains("Questionnaire/54127-6/_history/");
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
		myClient.forceConformanceCheck();

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

			myClient.transaction().withBundle(b).execute();

			assertTrue(called.get());

		} finally {
			ourRestServer.getInterceptorService().unregisterInterceptor(interceptor);

		}
	}

	@Test
	public void testDeleteExpungeOperation() {
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setDeleteExpungeEnabled(true);

		// setup
		for (int i = 0; i < 12; ++i) {
			Patient patient = new Patient();
			patient.setActive(false);
			MethodOutcome result = myClient.create().resource(patient).execute();
		}
		Patient patientActive = new Patient();
		patientActive.setActive(true);
		IIdType pKeepId = myClient.create().resource(patientActive).execute().getId();

		Patient patientInactive = new Patient();
		patientInactive.setActive(false);
		IIdType pDelId = myClient.create().resource(patientInactive).execute().getId();

		Observation obsActive = new Observation();
		obsActive.setSubject(new Reference(pKeepId.toUnqualifiedVersionless()));
		IIdType oKeepId = myClient.create().resource(obsActive).execute().getId();

		Observation obsInactive = new Observation();
		obsInactive.setSubject(new Reference(pDelId.toUnqualifiedVersionless()));
		IIdType obsDelId = myClient.create().resource(obsInactive).execute().getId();

		DiagnosticReport diagActive = new DiagnosticReport();
		diagActive.setSubject(new Reference(pKeepId.toUnqualifiedVersionless()));
		IIdType dKeepId = myClient.create().resource(diagActive).execute().getId();

		DiagnosticReport diagInactive = new DiagnosticReport();
		diagInactive.setSubject(new Reference(pDelId.toUnqualifiedVersionless()));
		IIdType diagDelId = myClient.create().resource(diagInactive).execute().getId();

		// validate setup
		assertEquals(14, getAllResourcesOfType("Patient").getTotal());
		assertEquals(2, getAllResourcesOfType("Observation").getTotal());
		assertEquals(2, getAllResourcesOfType("DiagnosticReport").getTotal());

		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, "Observation?subject.active=false");
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, "DiagnosticReport?subject.active=false");
		int batchSize = 2;
		input.addParameter(ProviderConstants.OPERATION_DELETE_BATCH_SIZE, new IntegerType(batchSize));

		// execute

		Parameters response = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		String jobId = BatchHelperR4.jobIdFromBatch2Parameters(response);

		// validate

		myBatch2JobHelper.awaitJobCompletion(jobId);
		assertEquals(2, myBatch2JobHelper.getCombinedRecordsProcessed(jobId));

		// validate
		Bundle obsBundle = getAllResourcesOfType("Observation");
		List<Observation> observations = BundleUtil.toListOfResourcesOfType(myFhirContext, obsBundle, Observation.class);
		assertThat(observations).hasSize(1);
		assertEquals(oKeepId, observations.get(0).getIdElement());

		Bundle diagBundle = getAllResourcesOfType("DiagnosticReport");
		List<DiagnosticReport> diags = BundleUtil.toListOfResourcesOfType(myFhirContext, diagBundle, DiagnosticReport.class);
		assertThat(diags).hasSize(1);
		assertEquals(dKeepId, diags.get(0).getIdElement());
	}

	private Bundle getAllResourcesOfType(String theResourceName) {
		return myClient.search().forResource(theResourceName)
			.totalMode(SearchTotalModeEnum.ACCURATE)
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.returnBundle(Bundle.class).execute();
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

}
