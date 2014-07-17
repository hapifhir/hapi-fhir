package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang3.StringUtils.*;
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
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ReferenceParameterTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx;

	@Test
	public void testSearchWithValue() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?" + Patient.SP_PROVIDER + "=123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Patient p = (Patient) entries.get(0).getResource();
			assertEquals("0123", p.getName().get(0).getFamilyFirstRep().getValue());
			assertEquals("1", p.getName().get(1).getFamilyFirstRep().getValue());
			assertEquals("2", p.getName().get(2).getFamilyFirstRep().getValue());
		}
	}

	@Test
	public void testSearchWithValueAndType() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?" + Patient.SP_PROVIDER + ":Organization=123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Patient p = (Patient) entries.get(0).getResource();
			assertEquals("0123", p.getName().get(0).getFamilyFirstRep().getValue());
			assertEquals("1Organization", p.getName().get(1).getFamilyFirstRep().getValue());
			assertEquals("2", p.getName().get(2).getFamilyFirstRep().getValue());
		}
	}

	@Test
	public void testSearchWithValueAndTypeAndChain() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?" + Patient.SP_PROVIDER + ":Organization.name=123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Patient p = (Patient) entries.get(0).getResource();
			assertEquals("0123", p.getName().get(0).getFamilyFirstRep().getValue());
			assertEquals("1Organization", p.getName().get(1).getFamilyFirstRep().getValue());
			assertEquals("2name", p.getName().get(2).getFamilyFirstRep().getValue());
		}
	}

	@Test
	public void testSearchWithValueAndChain() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?" + Patient.SP_PROVIDER + ".name=123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
			assertEquals(1, entries.size());
			Patient p = (Patient) entries.get(0).getResource();
			assertEquals("0123", p.getName().get(0).getFamilyFirstRep().getValue());
			assertEquals("1", p.getName().get(1).getFamilyFirstRep().getValue());
			assertEquals("2name", p.getName().get(2).getFamilyFirstRep().getValue());
		}
	}
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReferenceParameterTest.class);
	@Test
	public void testParamTypesInConformanceStatement() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		
		assertEquals(200, status.getStatusLine().getStatusCode());
		Conformance conf = ourCtx.newXmlParser().parseResource(Conformance.class,responseContent);
		
		RestResource res = conf.getRestFirstRep().getResourceFirstRep();
		assertEquals("Patient", res.getType().getValue());
		
		RestResourceSearchParam param = res.getSearchParamFirstRep();
		assertEquals(Patient.SP_PROVIDER, param.getName().getValue());
		
		assertEquals(1, param.getTarget().size());
		assertEquals(ResourceTypeEnum.ORGANIZATION, param.getTarget().get(0).getValueAsEnum());
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		ourCtx = servlet.getFhirContext();
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourCtx = servlet.getFhirContext();
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search
		public List<Patient> findPatient(@OptionalParam(name = Patient.SP_PROVIDER, targetTypes= {Organization.class}) ReferenceParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient p = new Patient();
			p.setId("1");
			p.addName().addFamily("0" + theParam.getValueAsQueryToken());
			p.addName().addFamily("1" + defaultString(theParam.getResourceType()));
			p.addName().addFamily("2" + defaultString(theParam.getChain()));
			retVal.add(p);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
