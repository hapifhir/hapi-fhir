package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class TransactionWithBundleParamTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionWithBundleParamTest.class);
	private static int ourPort;
	private static boolean ourReturnOperationOutcome;

	private static Server ourServer;
	
	
	
	@Before
	public void before() {
		ourReturnOperationOutcome = false;
	}
	
	@Test
	public void testTransaction() throws Exception {
		Bundle b = new Bundle();
		InstantDt nowInstant = InstantDt.withCurrentTime();
		
		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntry entry = b.addEntry();
		p1.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getId().setValue("2");
		entry.setResource(p2);
		
		BundleEntry deletedEntry = b.addEntry();
		deletedEntry.setDeletedResourceId(new IdDt("Patient/3"));
		deletedEntry.setDeleted(nowInstant);
		
		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(bundleString);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.addHeader("Accept", Constants.CT_ATOM_XML + "; pretty=true"); 
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_ATOM_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		ourLog.info(responseContent);
		
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.size());

		BundleEntry entry0 = bundle.getEntries().get(0);
		assertEquals("http://localhost:" + ourPort + "/Patient/81/_history/91", entry0.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/81/_history/91", entry0.getLinkSelf().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getLinkAlternate().getValue());
		
		BundleEntry entry1 = bundle.getEntries().get(1);
		assertEquals("http://localhost:" + ourPort + "/Patient/82/_history/92", entry1.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/82/_history/92", entry1.getLinkSelf().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/2", entry1.getLinkAlternate().getValue());

		BundleEntry entry2 = bundle.getEntries().get(2);
		assertEquals("http://localhost:" + ourPort + "/Patient/3/_history/93", entry2.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/3/_history/93", entry2.getLinkSelf().getValue());
		assertEquals(nowInstant.getValueAsString(), entry2.getDeletedAt().getValueAsString());
}
	
	
	@Test
	public void testTransactionWithOperationOutcome() throws Exception {
		ourReturnOperationOutcome = true;
		
		Bundle b = new Bundle();
		InstantDt nowInstant = InstantDt.withCurrentTime();
		
		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntry entry = b.addEntry();
		p1.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getId().setValue("2");
		entry.setResource(p2);
		
		BundleEntry deletedEntry = b.addEntry();
		deletedEntry.setDeletedResourceId(new IdDt("Patient/3"));
		deletedEntry.setDeleted(nowInstant);
		
		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(bundleString);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.addHeader("Accept", Constants.CT_ATOM_XML + "; pretty=true"); 
		httpPost.setEntity(new StringEntity(bundleString, ContentType.create(Constants.CT_ATOM_XML, "UTF-8")));
		HttpResponse status = ourClient.execute(httpPost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		ourLog.info(responseContent);
		
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(4, bundle.size());

		assertEquals(OperationOutcome.class, bundle.getEntries().get(0).getResource().getClass());
		assertEquals("OperationOutcome (no ID)", bundle.getEntries().get(0).getTitle().getValue());
		
		BundleEntry entry0 = bundle.getEntries().get(1);
		assertEquals("http://localhost:" + ourPort + "/Patient/81/_history/91", entry0.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/81/_history/91", entry0.getLinkSelf().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/1", entry0.getLinkAlternate().getValue());
		
		BundleEntry entry1 = bundle.getEntries().get(2);
		assertEquals("http://localhost:" + ourPort + "/Patient/82/_history/92", entry1.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/82/_history/92", entry1.getLinkSelf().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/2", entry1.getLinkAlternate().getValue());

		BundleEntry entry2 = bundle.getEntries().get(3);
		assertEquals("http://localhost:" + ourPort + "/Patient/3/_history/93", entry2.getResource().getId().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/3/_history/93", entry2.getLinkSelf().getValue());
		assertEquals(nowInstant.getValueAsString(), entry2.getDeletedAt().getValueAsString());
}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
	
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyProvider patientProvider = new DummyProvider();
		RestfulServer server = new RestfulServer(ourCtx);
		server.setProviders(patientProvider);
		
		org.eclipse.jetty.servlet.ServletContextHandler proxyHandler = new org.eclipse.jetty.servlet.ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder handler = new ServletHolder();
		handler.setServlet(server);
		proxyHandler.addServlet(handler, "/*");

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
		public List<IResource> transaction(@TransactionParam Bundle theResources) {
			int index=1;
			for (IResource next : theResources.toListOfResources()) {
				String newId = "8"+Integer.toString(index);
				if (next.getResourceMetadata().containsKey(ResourceMetadataKeyEnum.DELETED_AT)) {
					newId = next.getId().getIdPart();
				}
				next.setId(new IdDt("Patient", newId, "9"+Integer.toString(index)));
				index++;
			}
			
			List<IResource> retVal = theResources.toListOfResources();
			if (ourReturnOperationOutcome) {
				retVal = new ArrayList<IResource>();
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDetails("AAAAA");
				retVal.add(oo);
				retVal.addAll(theResources.toListOfResources());
			}
			
			return retVal;
		}

	
	}

}
