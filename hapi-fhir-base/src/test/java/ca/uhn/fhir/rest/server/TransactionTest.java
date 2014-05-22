package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class TransactionTest {

	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx = new FhirContext();

	@Test
	public void testTransaction() throws Exception {
		Bundle b= new Bundle();
		
		Patient p1 = new Patient();
		p1.getId().setValue("1");
		b.addEntry().setResource(p1);

		Patient p2 = new Patient();
		p2.getId().setValue("2");
		b.addEntry().setResource(p2);
		
		BundleEntry deletedEntry = b.addEntry();
		deletedEntry.setId(new IdDt("3"));
		deletedEntry.setDeleted(new InstantDt());
		
		String bundleString = ourCtx.newXmlParser().encodeBundleToString(b);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_ATOM_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = new FhirContext().newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.size());

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.setProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyProvider  {

		@Transaction
		public List<IResource> transaction(@TransactionParam List<IResource> theResources) {
			return theResources;
		}

	
	}

}
