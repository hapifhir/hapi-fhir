package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class HistoryTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx= FhirContext.forDstu1();
	private static int ourPort;

	private static Server ourServer;

	@Test
	public void testInstanceHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(2, bundle.getEntries().size());
			assertEquals("http://localhost:" + ourPort +"/Patient/ih1/_history/1", bundle.getEntries().get(0).getLinkSelf().getValue());
			assertEquals("http://localhost:" + ourPort +"/Patient/ih1/_history/2", bundle.getEntries().get(1).getLinkSelf().getValue());
			
		}
	}

	@Test
	public void testServerHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(2, bundle.getEntries().size());
			assertEquals("http://localhost:" + ourPort +"/Patient/h1/_history/1", bundle.getEntries().get(0).getLinkSelf().getValue());
			assertEquals("http://localhost:" + ourPort +"/Patient/h1/_history/2", bundle.getEntries().get(1).getLinkSelf().getValue());
			
		}
	}
	
	@Test
	public void testTypeHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(2, bundle.getEntries().size());
			assertEquals("http://localhost:" + ourPort +"/Patient/th1/_history/1", bundle.getEntries().get(0).getLinkSelf().getValue());
			assertEquals("http://localhost:" + ourPort +"/Patient/th1/_history/2", bundle.getEntries().get(1).getLinkSelf().getValue());
			
		}
	}

	/**
	 * We test this here because of bug 3- At one point VRead would "steal" instance history calls and handle them 
	 */
	@Test
	public void testVread() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history/456");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			
			assertEquals(200, status.getStatusLine().getStatusCode());
			
			Patient bundle = ourCtx.newXmlParser().parseResource(Patient.class, responseContent);
			assertEquals("vread", bundle.getNameFirstRep().getFamilyFirstRep().getValue());
		}
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPlainProvider plainProvider = new DummyPlainProvider();
		DummyResourceProvider patientProvider = new DummyResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPlainProviders(plainProvider);
		servlet.setResourceProviders(patientProvider);
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
	public static class DummyPlainProvider {
		
		@History
		public List<Patient> history(@Since InstantDt theSince) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

				Patient patient = new Patient();
				patient.setId("Patient/h1/_history/1");
				patient.addName().addFamily("history");
				retVal.add(patient);

				Patient patient2 = new Patient();
				patient2.setId("Patient/h1/_history/2");
				patient2.addName().addFamily("history");
				retVal.add(patient2);

			return retVal;
		}

		


	}
	
	
	public static class DummyResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@History
		public List<Patient> instanceHistory(@IdParam IdDt theId) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

				Patient patient = new Patient();
				patient.setId("Patient/ih1/_history/1");
				patient.addName().addFamily("history");
				retVal.add(patient);

				Patient patient2 = new Patient();
				patient2.setId("Patient/ih1/_history/2");
				patient2.addName().addFamily("history");
				retVal.add(patient2);

			return retVal;
		}

		@History
		public List<Patient> typeHistory() {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

				Patient patient = new Patient();
				patient.setId("Patient/th1/_history/1");
				patient.addName().addFamily("history");
				retVal.add(patient);

				Patient patient2 = new Patient();
				patient2.setId("Patient/th1/_history/2");
				patient2.addName().addFamily("history");
				retVal.add(patient2);

			return retVal;
		}
		
		@Read(version=true)
		public Patient vread(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.addName().addFamily("vread");
			retVal.setId(theId);
			return retVal;
		}

	
	}

}
