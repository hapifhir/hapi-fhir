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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;

public class StringParameterTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;

	private static Server ourServer;

	@Test
	public void testContains() {
		StringParam sp = new StringParam("VAL");
		sp.setContains(true);
		assertEquals(":contains", sp.getQueryParameterQualifier());
		
		sp = new StringParam("VAL");
		sp.setValueAsQueryToken(":contains", "VAL");
		assertEquals(true, sp.isContains());
		assertEquals("VAL", sp.getValue());
	}
	
	@Test
	public void testRawString() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?plain=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?plain=BBB");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(0, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
	}

	@Test
	public void testSearchExactMatch() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str:exact=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str:exact=AAA");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(0, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str:exact=BBB");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(0, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
	}

	@Test
	public void testSearchExactMatchOptional() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?ccc:exact=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
	}

	@Test
	public void testSearchNormalMatch() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str=AAA");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str=BBB");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(0, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
		}
	}

	@Test
	public void testSearchWithFormatAndPretty() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?str=aaa&_format=xml&_pretty=true");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(1, ourCtx.newXmlParser().parseBundle(responseContent).getEntries().size());
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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setFhirContext(ourCtx);
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
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search
		public List<Patient> findPatientByString(@RequiredParam(name = "plain") String theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			if (theParam.toLowerCase().equals("aaa")) {
				Patient patient = new Patient();
				patient.setId("1");
				retVal.add(patient);
			}

			return retVal;
		}

		@Search
		public List<Patient> findPatientByStringParam(@RequiredParam(name = "str") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			if (theParam.isExact() && theParam.getValue().equals("aaa")) {
				Patient patient = new Patient();
				patient.setId("1");
				retVal.add(patient);
			}
			if (!theParam.isExact() && theParam.getValue().toLowerCase().equals("aaa")) {
				Patient patient = new Patient();
				patient.setId("2");
				retVal.add(patient);
			}

			return retVal;
		}

		@Search
		public List<Patient> findPatientWithOptional(@OptionalParam(name = "ccc") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			if (theParam.isExact() && theParam.getValue().equals("aaa")) {
				Patient patient = new Patient();
				patient.setId("1");
				retVal.add(patient);
			}
			if (!theParam.isExact() && theParam.getValue().toLowerCase().equals("aaa")) {
				Patient patient = new Patient();
				patient.setId("2");
				retVal.add(patient);
			}

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
