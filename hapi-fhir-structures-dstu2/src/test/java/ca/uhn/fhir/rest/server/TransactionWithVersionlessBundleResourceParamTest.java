package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
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
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionWithVersionlessBundleResourceParamTest {


	private static CloseableHttpClient ourClient;

	
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionWithVersionlessBundleResourceParamTest.class);
	private static int ourPort;
	private static boolean ourReturnOperationOutcome;
	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourReturnOperationOutcome = false;
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

		String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		assertEquals(1, bundle.getEntry().size());

		Entry entry0 = bundle.getEntry().get(0);
		assertEquals("Patient/81/_history/91", entry0.getResponse().getLocation());

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


	public static class DummyProvider {

		@Transaction
		public IBaseBundle transaction(@TransactionParam Bundle theResources) {
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
