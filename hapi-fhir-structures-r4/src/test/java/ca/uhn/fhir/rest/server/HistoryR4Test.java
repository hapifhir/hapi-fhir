package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(HistoryR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static DateRangeParam ourLastAt;
	private static InstantType ourLastSince;
	private static IPrimitiveType<Date> ourLastSince2;
	private static IPrimitiveType<String> ourLastSince3;
	private static IPrimitiveType<?> ourLastSince4;
	private static int ourPort;
	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourLastAt = null;
		ourLastSince = null;
		ourLastSince2 = null;
		ourLastSince3 = null;
		ourLastSince4 = null;
	}

	@Test
	public void testAt() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history?_at=gt2001&_at=lt2005");
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			assertEquals(ParamPrefixEnum.GREATERTHAN, ourLastAt.getLowerBound().getPrefix());
			assertEquals("2001", ourLastAt.getLowerBound().getValueAsString());
			assertEquals(ParamPrefixEnum.LESSTHAN, ourLastAt.getUpperBound().getPrefix());
			assertEquals("2005", ourLastAt.getUpperBound().getValueAsString());
		}
	}

	@Test
	public void testInstanceHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history?_pretty=true");
			String responseContent;
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/ih1/_history/1", bundle.getEntry().get(0).getResource().getId());
			assertEquals("http://localhost:" + ourPort + "/Patient/ih1/_history/2", bundle.getEntry().get(1).getResource().getId());

		}
	}

	@Test
	public void testServerHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history");
			String responseContent;
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/h1/_history/1", bundle.getEntry().get(0).getResource().getId());
			assertEquals("http://localhost:" + ourPort + "/Patient/h1/_history/2", bundle.getEntry().get(1).getResource().getId());

		}
	}

	@Test
	public void testSince() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/_history?_since=2005");
			String responseContent;
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			assertEquals(null, ourLastAt);
			assertEquals("2005", ourLastSince.getValueAsString());
			assertEquals("2005", ourLastSince2.getValueAsString());
			assertTrue(DateTimeType.class.equals(ourLastSince2.getClass()));
			assertEquals("2005", ourLastSince3.getValueAsString());
			assertTrue(StringType.class.equals(ourLastSince3.getClass()));
			assertEquals("2005", ourLastSince4.getValueAsString());
			assertTrue(StringType.class.equals(ourLastSince4.getClass()));
		}
	}

	@Test
	public void testTypeHistory() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_history");
			String responseContent;
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			assertNull(ourLastAt);

			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals(2, bundle.getEntry().size());
			assertEquals("http://localhost:" + ourPort + "/Patient/th1/_history/1", bundle.getEntry().get(0).getResource().getId());
			assertEquals("http://localhost:" + ourPort + "/Patient/th1/_history/2", bundle.getEntry().get(1).getResource().getId());

		}
	}

	/**
	 * We test this here because of bug 3- At one point VRead would "steal" instance history calls and handle them
	 */
	@Test
	public void testVread() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history/456");
			String responseContent;
			try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
				responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
				ourLog.info(responseContent);
				assertEquals(200, status.getStatusLine().getStatusCode());
			}

			Patient bundle = ourCtx.newXmlParser().parseResource(Patient.class, responseContent);
			assertEquals("vread", bundle.getNameFirstRep().getFamily());
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPlainProvider plainProvider = new DummyPlainProvider();
		DummyResourceProvider patientProvider = new DummyResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.registerProviders(plainProvider, patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyPlainProvider {

		@History
		public List<Patient> history(@Since InstantType theSince,
											  @Since IPrimitiveType<Date> theSince2,
											  @Since IPrimitiveType<String> theSince3,
											  @Since IPrimitiveType theSince4,
											  @At DateRangeParam theAt) {
			ourLastAt = theAt;
			ourLastSince = theSince;
			ourLastSince2 = theSince2;
			ourLastSince3 = theSince3;
			ourLastSince4 = theSince4;

			ArrayList<Patient> retVal = new ArrayList<>();

			Patient patient = new Patient();
			patient.setId("Patient/h1/_history/1");
			patient.addName().setFamily("history");
			retVal.add(patient);

			Patient patient2 = new Patient();
			patient2.setId("Patient/h1/_history/2");
			patient2.addName().setFamily("history");
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
		public List<Patient> instanceHistory(@IdParam IdType theId) {
			ArrayList<Patient> retVal = new ArrayList<>();

			Patient patient = new Patient();
			patient.setId("Patient/ih1/_history/1");
			patient.addName().setFamily("history");
			retVal.add(patient);

			Patient patient2 = new Patient();
			patient2.setId("Patient/ih1/_history/2");
			patient2.addName().setFamily("history");
			retVal.add(patient2);

			return retVal;
		}

		@History
		public List<Patient> typeHistory() {
			ArrayList<Patient> retVal = new ArrayList<>();

			Patient patient = new Patient();
			patient.setId("Patient/th1/_history/1");
			patient.addName().setFamily("history");
			retVal.add(patient);

			Patient patient2 = new Patient();
			patient2.setId("Patient/th1/_history/2");
			patient2.addName().setFamily("history");
			retVal.add(patient2);

			return retVal;
		}

		@Read(version = true)
		public Patient vread(@IdParam IdType theId) {
			Patient retVal = new Patient();
			retVal.addName().setFamily("vread");
			retVal.setId(theId);
			return retVal;
		}

	}

}
