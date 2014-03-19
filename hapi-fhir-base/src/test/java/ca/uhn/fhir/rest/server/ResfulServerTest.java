package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.testutil.RandomServerPortProvider;
import ca.uhn.fhir.util.ExtensionConstants;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResfulServerTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResfulServerTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);
		ourCtx = new FhirContext(Patient.class);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();
		ServerProfileProvider profProvider=new ServerProfileProvider(ourCtx);

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(new DummyRestfulServer(patientProvider,profProvider));
		proxyHandler.addServletWithMapping(servletHolder, "/");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();


	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@Test
	public void testSearchWithIncludes() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?withIncludes=include1&_include=include2&_include=include3");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("include1", patient.getCommunication().get(0).getText().getValue());
		assertEquals("include2", patient.getAddress().get(0).getLine().get(0).getValue());
		assertEquals("include3", patient.getAddress().get(1).getLine().get(0).getValue());
	}

	@Test
	public void testSearchAllProfiles() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Profile?");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
//		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Bundle bundle = parser.parseBundle(responseContent);

		ourLog.info("Response:\n{}", parser.encodeBundleToString(bundle));
		
	}
	
	
	public void testGetMetadata() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
//		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		Conformance bundle = parser.parseResource(Conformance.class, responseContent);

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true);
		String enc = p.encodeResourceToString(bundle);
		ourLog.info("Response:\n{}", enc);
		assertTrue(enc.contains(ExtensionConstants.CONF_ALSO_CHAIN));
		
		 p = ourCtx.newXmlParser().setPrettyPrint(true);
		 enc = p.encodeResourceToString(bundle);
		ourLog.info("Response:\n{}", enc);
		assertTrue(enc.contains(ExtensionConstants.CONF_ALSO_CHAIN));

	}
	
	@Test
	public void testSearchWithOptionalParam() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name1=AAA");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("AAA", patient.getName().get(0).getFamily().get(0).getValue());
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		/*
		 * Now with optional value populated
		 */

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name1=AAA&name2=BBB");
		status = ourClient.execute(httpGet);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("AAA", patient.getName().get(0).getFamily().get(0).getValue());
		assertEquals("BBB", patient.getName().get(0).getGiven().get(0).getValue());

	}

	@Test
	public void testSearchByMultipleIdentifiers() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?ids=urn:aaa%7Caaa,urn:bbb%7Cbbb");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("urn:aaa|aaa", patient.getIdentifier().get(1).getValueAsQueryToken());
		assertEquals("urn:bbb|bbb", patient.getIdentifier().get(2).getValueAsQueryToken());
	}

	@Test
	public void testSearchByDob() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?dob=2011-01-02");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("NONE", patient.getIdentifier().get(1).getValue().getValue());
		assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

		/*
		 * With comparator
		 */
		
		 httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?dob=%3E%3D2011-01-02");
		 status = ourClient.execute(httpGet);

		 responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		 bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		 patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals(">=", patient.getIdentifier().get(1).getValue().getValue());
		assertEquals("2011-01-02", patient.getIdentifier().get(2).getValue().getValue());

	}
	
	@Test
	public void testSearchByParamIdentifier() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=urn:hapitest:mrns%7C00001");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		/**
		 * Alternate form
		 */
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?identifier=urn:hapitest:mrns%7C00001");
		status = ourClient.execute(httpPost);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		/**
		 * failing form
		 */
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/_search?identifier=urn:hapitest:mrns%7C00001");
		status = ourClient.execute(httpGet);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(404, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testSearchAll() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");
		status = ourClient.execute(httpPost);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(2, bundle.getEntries().size());

	}

	@Test
	public void testFormatParamXml() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

	}

	@Test
	public void testFormatParamJson() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		// TODO: enable once JSON parser is written
		// Patient patient = (Patient)
		// ourCtx.newJsonParser().parseResource(responseContent);
		// assertEquals("PatientOne",
		// patient.getName().get(0).getGiven().get(0).getValue());

	}

	@Test
	public void testGetById() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		/*
		 * Different ID
		 */

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.debug("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientTwo", patient.getName().get(0).getGiven().get(0).getValue());

		/*
		 * Bad ID
		 */

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/9999999");
		status = ourClient.execute(httpGet);

		responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.debug("Response was:\n{}", responseContent);

		assertEquals(404, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testGetByVersionId() throws Exception {

		// HttpPost httpPost = new HttpPost("http://localhost:" + ourPort +
		// "/Patient/1");
		// httpPost.setEntity(new StringEntity("test",
		// ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/999");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(responseContent);
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());
		assertEquals("999", patient.getName().get(0).getText().getValue());

	}

}
