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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class HistoryDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static DateRangeParam ourLastAt;

	private static InstantDt ourLastSince;
	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLastAt = null;
		ourLastSince = null;
	}

	@Test
	public void testSince() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history?_since=2005");
			HttpResponse status = ourClient.execute(httpGet);
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(null, ourLastAt);
			assertEquals("2005", ourLastSince.getValueAsString());
		}
	}

	@Test
	public void testAt() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history?_at=gt2001&_at=lt2005");
			HttpResponse status = ourClient.execute(httpGet);
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals(ParamPrefixEnum.GREATERTHAN, ourLastAt.getLowerBound().getPrefix());
			assertEquals("2001", ourLastAt.getLowerBound().getValueAsString());
			assertEquals(ParamPrefixEnum.LESSTHAN, ourLastAt.getUpperBound().getPrefix());
			assertEquals("2005", ourLastAt.getUpperBound().getValueAsString());
		}
	}

	@Test
	public void testInstanceHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/ih1/_history/1", bundle.getEntry().get(0).getResource().getId().getValue());
			assertEquals("http://localhost:" + ourPort + "/Patient/ih1/_history/2", bundle.getEntry().get(1).getResource().getId().getValue());

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

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/h1/_history/1", bundle.getEntry().get(0).getResource().getId().getValue());
			assertEquals("http://localhost:" + ourPort + "/Patient/h1/_history/2", bundle.getEntry().get(1).getResource().getId().getValue());

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

			assertNull(ourLastAt);

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/th1/_history/1", bundle.getEntry().get(0).getResource().getId().getValue());
			assertEquals("http://localhost:" + ourPort + "/Patient/th1/_history/2", bundle.getEntry().get(1).getResource().getId().getValue());

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
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
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

	public static class DummyPlainProvider {

		@History
		public List<Patient> history(@Since InstantDt theSince, @At DateRangeParam theAt) {
			ourLastAt = theAt;
			ourLastSince = theSince;

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
		public Class<? extends Patient> getResourceType() {
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

		@Read(version = true)
		public Patient vread(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.addName().addFamily("vread");
			retVal.setId(theId);
			return retVal;
		}

	}

}
