package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ServerFeaturesTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;

	@Before
	public void before() {
		servlet.setServerAddressStrategy(new IncomingRequestAddressStrategy());
	}

	@Test
	public void testAcceptHeaderAtom() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_ATOM_XML);
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

	}

	@Test
	public void testAcceptHeaderJson() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("\"identifier\":"));

	}

	/**
	 * Header value should be application/xml+fhir or application/json+fhir but
	 * we should also accept application/xml and application/json 
	 */
	@Test
	public void testAcceptHeaderNonFhirTypes() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_XML);
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_JSON);
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("\"identifier\":"));

	}

	/**
	 * Header value should be application/xml+fhir or application/json+fhir but
	 * we should also accept text/xml and text/json 
	 */
	@Test
	public void testAcceptHeaderNonFhirTypesNonStandard() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/xml");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/json");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("\"identifier\":"));

	}

	@Test
	public void testAcceptHeaderWithMultiple() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/plain, " + Constants.CT_FHIR_XML);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/plain, " + Constants.CT_ATOM_XML);
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));
	}

	@Test
	public void testAcceptHeaderWithMultipleJson() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/plain, " + Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("\"identifier\":"));

	}

	@Test
	public void testAcceptHeaderWithPrettyPrint() throws Exception {
		HttpGet httpGet;
		CloseableHttpResponse status;
		String responseContent;

		
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON + "; pretty=true; q=1.0, " + Constants.CT_FHIR_XML + "; pretty=true; q=0.9");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, (containsString("\"identifier\":")));
		assertThat(responseContent, (containsString(",\n")));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON + "; pretty=true" + ", " + Constants.CT_FHIR_XML + "; pretty=true");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, not(containsString("\"identifier\":")));
		assertThat(responseContent, (containsString(">\n")));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_XML + "; pretty=true");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier>\n   "));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON + "; pretty=true");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("\",\n"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON + "; pretty=true; q=1.0" + ", " + Constants.CT_FHIR_XML + "; pretty=true; q=0.9");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, (containsString("\"identifier\":")));
		assertThat(responseContent, (containsString(",\n")));
	}

	@Test
	public void testAcceptHeaderXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_XML);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

	}

	@Test
	public void testHardcodedAddressStrategy() throws Exception {

		servlet.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://foo/bar"));

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=1");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<id>http://foo/bar/Patient/1"));

	}

	@Test
	public void testInternalErrorIfNoId() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=findPatientsWithNoIdSpecified");
		httpGet.addHeader("Accept", Constants.CT_FHIR_XML + "; pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, StringContains.containsString("ID"));

	}

	@Test
	public void testPrettyPrint() throws Exception {
		/*
		 * Not specified
		 */

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

		/*
		 * Disabled
		 */

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=false");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, StringContains.containsString("<identifier><use"));

		/*
		 * Enabled
		 */

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=true");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(responseContent, IsNot.not(StringContains.containsString("<identifier><use")));

	}

	@Test
	public void testSearchReturnWithAbsoluteIdSpecified() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=findPatientsWithAbsoluteIdSpecified");
		httpGet.addHeader("Accept", Constants.CT_FHIR_XML + "; pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		Bundle bundle = servlet.getFhirContext().newXmlParser().parseBundle(responseContent);
		assertEquals(2, bundle.size());

		assertEquals("http://absolute.com/Patient/123", bundle.getEntries().get(0).getId().getValue());
		assertEquals("http://absolute.com/Patient/123/_history/22", bundle.getEntries().get(0).getLinkSelf().getValue());

		assertEquals("http://foo.com/Organization/222", bundle.getEntries().get(1).getId().getValue());
		assertEquals("http://foo.com/Organization/222/_history/333", bundle.getEntries().get(1).getLinkSelf().getValue());
	}

	@Test
	public void testSearchWithWildcardRetVal() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/?_query=searchWithWildcardRetVal");
		httpGet.addHeader("Accept", Constants.CT_FHIR_XML + "; pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, StringContains.containsString("searchWithWildcardRetVal"));

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
		servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
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

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.getGender().setText("M");
			patient.getId().setValue("1");
			return patient;
		}

		@Search(queryName = "findPatientsWithAbsoluteIdSpecified")
		public List<Patient> findPatientsWithAbsoluteIdSpecified() {
			Patient p = new Patient();
			p.addIdentifier().setSystem("foo");
			p.setId("http://absolute.com/Patient/123/_history/22");

			Organization o = new Organization();
			o.setId("http://foo.com/Organization/222/_history/333");
			p.getManagingOrganization().setResource(o);

			return Collections.singletonList(p);
		}

		@Search(queryName = "findPatientsWithNoIdSpecified")
		public List<Patient> findPatientsWithNoIdSpecified() {
			Patient p = new Patient();
			p.addIdentifier().setSystem("foo");
			return Collections.singletonList(p);
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new IdentifierDt());
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanNameDt());
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getGender().setText("F");
				patient.getId().setValue("2");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdDt theId) {
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Search()
		public List<Patient> getResourceById(@RequiredParam(name = "_id") String theId) {
			Patient patient = getIdToPatient().get(theId);
			if (patient != null) {
				return Collections.singletonList(patient);
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search(queryName = "searchWithWildcardRetVal")
		public List<? extends IResource> searchWithWildcardRetVal() {
			Patient p = new Patient();
			p.setId("1234");
			p.addName().addFamily("searchWithWildcardRetVal");
			return Collections.singletonList(p);
		}

	}

}
