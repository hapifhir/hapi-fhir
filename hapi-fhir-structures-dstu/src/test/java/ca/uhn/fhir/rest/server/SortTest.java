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
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SortTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;
	private static Server ourServer;
	
	@Test
	public void testNoSort() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals(1, p.getName().size());
		assertEquals("Hello", p.getNameFirstRep().getFamilyFirstRep().getValue());
	}

	@Test
	public void testSingleSort() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_sort=given");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(1, bundle.size());

			Patient p = bundle.getResources(Patient.class).get(0);
			assertEquals(2, p.getName().size());
			assertEquals("Hello", p.getNameFirstRep().getFamilyFirstRep().getValue());
			assertEquals("given|null", p.getName().get(1).getFamilyFirstRep().getValue());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_sort:asc=given");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(1, bundle.size());

			Patient p = bundle.getResources(Patient.class).get(0);
			assertEquals(2, p.getName().size());
			assertEquals("Hello", p.getNameFirstRep().getFamilyFirstRep().getValue());
			assertEquals("given|ASC", p.getName().get(1).getFamilyFirstRep().getValue());
		}
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_sort:desc=given");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(1, bundle.size());

			Patient p = bundle.getResources(Patient.class).get(0);
			assertEquals(2, p.getName().size());
			assertEquals("Hello", p.getNameFirstRep().getFamilyFirstRep().getValue());
			assertEquals("given|DESC", p.getName().get(1).getFamilyFirstRep().getValue());
		}

	}

	@Test
	public void testSortChain() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?name=Hello&_sort=given&_sort=family&_sort=name");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
			assertEquals(1, bundle.size());

			Patient p = bundle.getResources(Patient.class).get(0);
			assertEquals(4, p.getName().size());
			assertEquals("Hello", p.getNameFirstRep().getFamilyFirstRep().getValue());
			assertEquals("given|null", p.getName().get(1).getFamilyFirstRep().getValue());
			assertEquals("family|null", p.getName().get(2).getFamilyFirstRep().getValue());
			assertEquals("name|null", p.getName().get(3).getFamilyFirstRep().getValue());
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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
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
		public List<Patient> findPatient(@RequiredParam(name = Patient.SP_NAME) StringDt theName, @Sort SortSpec theSort) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient p = new Patient();
			p.setId("1");
			p.addName().addFamily().setValue(theName.getValue());
			SortSpec sort = theSort;
			while (sort != null) {
				p.addName().addFamily().setValue(sort.getParamName() + "|" + sort.getOrder());
				sort = sort.getChain();
			}

			retVal.add(p);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
