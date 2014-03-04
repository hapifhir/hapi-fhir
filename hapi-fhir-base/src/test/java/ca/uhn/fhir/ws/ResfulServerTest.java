package ca.uhn.fhir.ws;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResfulServerTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResfulServerTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static DefaultHttpClient ourClient;
	private static FhirContext ourCtx;

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(new DummyRestfulServer(patientProvider));
		proxyHandler.addServletWithMapping(servletHolder, "/");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault(), 5000, TimeUnit.MILLISECONDS);
		ourClient = new DefaultHttpClient(connectionManager);
		
		ourCtx = new FhirContext(Patient.class);
		
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
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
		
		Patient patient = (Patient)bundle.getEntries().get(0).getResource();
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());


	}
	
	@Test
	public void testGetById() throws Exception {

//		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/1");
//		httpPost.setEntity(new StringEntity("test", ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.debug("Response was:\n{}", responseContent);
		
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



}
