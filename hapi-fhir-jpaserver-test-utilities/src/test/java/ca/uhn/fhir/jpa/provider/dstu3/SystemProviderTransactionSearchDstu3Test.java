package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.rp.dstu3.ObservationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.OrganizationResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.PatientResourceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemProviderTransactionSearchDstu3Test extends BaseJpaDstu3Test {

	private static RestfulServer myRestServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static CloseableHttpClient ourHttpClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderTransactionSearchDstu3Test.class);
	private static Server ourServer;
	private static String ourServerBase;
	private SimpleRequestHeaderInterceptor mySimpleHeaderInterceptor;


	
	@SuppressWarnings("deprecation")
	@AfterEach
	public void after() {
		ourClient.unregisterInterceptor(mySimpleHeaderInterceptor);
		myDaoConfig.setMaximumSearchResultCountInTransaction(new DaoConfig().getMaximumSearchResultCountInTransaction());
	}

	@BeforeEach
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
			restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

			restServer.setPlainProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");			

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourCtx = FhirContext.forDstu3Cached();
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
		
		myRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		myRestServer.setPagingProvider(myPagingProvider);

		mySimpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		ourClient.registerInterceptor(mySimpleHeaderInterceptor);
	}
	

	private List<String> create20Patients() {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Patient patient = new Patient();
			patient.setGender(AdministrativeGender.MALE);
			patient.addIdentifier().setSystem("urn:foo").setValue("A");
			patient.addName().setFamily("abcdefghijklmnopqrstuvwxyz".substring(i, i+1));
			String id = myPatientDao.create(patient).getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
		}
		return ids;
	}

	@Test
	public void testBatchWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();
		
		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=_id");
		
		myDaoConfig.setMaximumSearchResultCountInTransaction(100);
		
		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(null, respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
	}
	
	@Test
	public void testBatchWithGetNormalSearch() {
		List<String> ids = create20Patients();
		
		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=name");
		
		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
		
		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertEquals(5, respBundle.getEntry().size());
		actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(5, 10).toArray(new String[0])));
	}


	@Test
	public void testPatchUsingJsonPatch_Transaction() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContentType(ca.uhn.fhir.rest.api.Constants.CT_JSON_PATCH);
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		// Note that we don't set the type
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue());

		HttpPost post = new HttpPost(ourServerBase);
		String encodedRequest = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ourLog.info("Requet:\n{}", encodedRequest);
		post.setEntity(new StringEntity(encodedRequest, ContentType.parse(ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(responseString, containsString("\"resourceType\":\"Bundle\""));
		}

		Patient newPt = ourClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}



	@Test
	public void testTransactionWithGetHardLimitLargeSynchronous() {
		List<String> ids = create20Patients();
		
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=_id");
		
		myDaoConfig.setMaximumSearchResultCountInTransaction(100);
		
		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		assertEquals(null, respBundle.getLink("next"));
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
	}
	
	@Test
	public void testTransactionWithGetNormalSearch() {
		List<String> ids = create20Patients();
		
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?_count=5&_sort=name");
		
		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(1, output.getEntry().size());
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		assertEquals(5, respBundle.getEntry().size());
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
		
		String nextPageLink = respBundle.getLink("next").getUrl();
		output = ourClient.loadPage().byUrl(nextPageLink).andReturnBundle(Bundle.class).execute();
		respBundle = output;
		assertEquals(5, respBundle.getEntry().size());
		actualIds = toIds(respBundle);
		assertThat(actualIds, contains(ids.subList(5, 10).toArray(new String[0])));
	}

	/**
	 * 30 searches in one Transaction! Whoa!
	 */
	@Test
	public void testTransactionWithManyGets() {
		List<String> ids = create20Patients();

		
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		for (int i = 0; i < 30; i++) {
			input
				.addEntry()
				.getRequest()
				.setMethod(HTTPVerb.GET)
				.setUrl("Patient?_count=5&identifier=urn:foo|A,AAAAA" + i);
		}
		
		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(30, output.getEntry().size());
		for (int i = 0; i < 30; i++) {
			Bundle respBundle = (Bundle) output.getEntry().get(i).getResource();
			assertEquals(5, respBundle.getEntry().size());
			assertThat(respBundle.getLink("next").getUrl(), not(nullValue()));
			List<String> actualIds = toIds(respBundle);
			assertThat(actualIds, contains(ids.subList(0, 5).toArray(new String[0])));
		}
	}

	@Test
	public void testTransactionGetStartsWithSlash() {
		IIdType patientId = ourClient.create().resource(new Patient()).execute().getId().toUnqualifiedVersionless();

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);
		input.setId("bundle-batch-test");
		input.addEntry().getRequest().setMethod(HTTPVerb.GET)
			.setUrl("/Patient?_id="+patientId.getIdPart());

		Bundle output = ourClient.transaction().withBundle(input).execute();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
		assertThat(output.getEntryFirstRep().getResponse().getStatus(), startsWith("200"));
		Bundle respBundle = (Bundle) output.getEntry().get(0).getResource();
		List<String> actualIds = toIds(respBundle);
		assertThat(actualIds, containsInAnyOrder(patientId.getValue()));
	}

	private List<String> toIds(Bundle theRespBundle) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (BundleEntryComponent next : theRespBundle.getEntry()) {
			retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

}
