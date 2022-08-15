package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestInteraction;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionWithBundleResourceParamTest {


	private static CloseableHttpClient ourClient;

	
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionWithBundleResourceParamTest.class);
	private static int ourPort;
	private static boolean ourReturnOperationOutcome;
	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourReturnOperationOutcome = false;
	}

	@Test
	public void testConformance() {
		ourCtx.getRestfulClientFactory().setSocketTimeout(500000);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		Conformance rest = client.fetchConformance().ofType(Conformance.class).execute();
		boolean supportsTransaction = false;
		for (RestInteraction next : rest.getRest().get(0).getInteraction()) {
			if (next.getCodeElement().getValueAsEnum() == SystemRestfulInteractionEnum.TRANSACTION) {
				supportsTransaction = true;
			}
		}
		
		assertTrue(supportsTransaction);
	}

	@Test
	public void testTransactionWithJsonRequest() throws Exception {
		Bundle b = new Bundle();
		InstantDt nowInstant = InstantDt.withCurrentTime();

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		Entry entry = b.addEntry();
		p1.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getId().setValue("2");
		entry.setResource(p2);

		Entry deletedEntry = b.addEntry();
		deletedEntry.getRequest().setMethod(HTTPVerbEnum.DELETE);
		deletedEntry.getRequest().setUrl("http://base.com/Patient/123");

		String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
//		httpPost.addHeader("Accept", Constants.CT_ATOM_XML + "; pretty=true");
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());

		Entry entry0 = bundle.getEntry().get(0);
		assertEquals("Patient/81/_history/91", entry0.getResponse().getLocation());

		Entry entry1 = bundle.getEntry().get(1);
		assertEquals( "Patient/82/_history/92", entry1.getResponse().getLocation());

		Entry entry2 = bundle.getEntry().get(2);
		assertEquals("Patient/123/_history/93", entry2.getResponse().getLocation());
	}
	
	@Test
	public void testTransactionWithOperationOutcome() throws Exception {
		ourReturnOperationOutcome = true;

		Bundle b = new Bundle();
		InstantDt nowInstant = InstantDt.withCurrentTime();

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		Entry entry = b.addEntry();
		p1.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getId().setValue("2");
		entry.setResource(p2);

		Entry deletedEntry = b.addEntry();
		deletedEntry.getRequest().setMethod(HTTPVerbEnum.DELETE);
		deletedEntry.getRequest().setUrl(new IdDt("Patient/3"));

		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		String base = "http://localhost:" + ourPort + "/";
		HttpPost httpPost = new HttpPost(base);
		httpPost.addHeader("Accept", Constants.CT_FHIR_XML + "; pretty=true");
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(4, bundle.getEntry().size());

		assertEquals(OperationOutcome.class, bundle.getEntry().get(0).getResource().getClass());

		Entry entry0 = bundle.getEntry().get(1);
		assertEquals("Patient/81/_history/91", entry0.getResponse().getLocation());

		Entry entry1 = bundle.getEntry().get(2);
		assertEquals("Patient/82/_history/92", entry1.getResponse().getLocation());

		Entry entry2 = bundle.getEntry().get(3);
		assertEquals( "Patient/3/_history/93", entry2.getResponse().getLocation());
	}

	@Test
	public void testTransactionWithXmlRequest() throws Exception {
		Bundle b = new Bundle();
		InstantDt nowInstant = InstantDt.withCurrentTime();

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		Entry entry = b.addEntry();
		p1.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getId().setValue("2");
		entry.setResource(p2);

		Entry deletedEntry = b.addEntry();
		deletedEntry.getRequest().setMethod(HTTPVerbEnum.DELETE);
		deletedEntry.getRequest().setUrl("http://base.com/Patient/123");

		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());

		Entry entry0 = bundle.getEntry().get(0);
		assertEquals("Patient/81/_history/91", entry0.getResponse().getLocation());

		Entry entry1 = bundle.getEntry().get(1);
		assertEquals( "Patient/82/_history/92", entry1.getResponse().getLocation());

		Entry entry2 = bundle.getEntry().get(2);
		assertEquals("Patient/123/_history/93", entry2.getResponse().getLocation());
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyProvider patientProvider = new DummyProvider();
		RestfulServer server = new RestfulServer(ourCtx);
		server.setProviders(patientProvider);

		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder handler = new ServletHolder();
		handler.setServlet(server);
		proxyHandler.addServlet(handler, "/*");

		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(500000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyProvider {

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theResources) {
			Bundle retVal = new Bundle();

			if (ourReturnOperationOutcome) {
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDetails("AAAAA");
				retVal.addEntry().setResource(oo);
			}

			int index = 1;
			for (Entry nextEntry : theResources.getEntry()) {
				String newId = "8" + Integer.toString(index);
				if (nextEntry.getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.DELETE) {
					newId = new IdDt(nextEntry.getRequest().getUrlElement()).getIdPart();
				}
				IdDt newIdDt = (new IdDt("Patient", newId, "9" + Integer.toString(index)));
				retVal.addEntry().getResponse().setLocation(newIdDt.getValue());
				index++;
			}

			return retVal;
		}

	}

}
