package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
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
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ReferenceParameterTest {

	private static CloseableHttpClient ourClient;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReferenceParameterTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static ReferenceParam ourLastRefParam;

	@Before
	public void before() {
		ourLastRefParam = null;
	}

	@Test
	public void testParamTypesInConformanceStatement() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Conformance conf = ourCtx.newXmlParser().parseResource(Conformance.class, responseContent);

		RestResource res = conf.getRestFirstRep().getResource().get(2);
		assertEquals("Patient", res.getType().getValue());

		RestResourceSearchParam param = res.getSearchParamFirstRep();
		assertEquals(Patient.SP_PROVIDER, param.getName().getValue());

		assertEquals(1, param.getTarget().size());
		assertEquals(ResourceTypeEnum.ORGANIZATION, param.getTarget().get(0).getValueAsEnum());
	}

	@Test
	public void testReadReturnVersionedReferenceInResponse() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/22");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, responseContent);

		assertThat(status.getFirstHeader("Content-Location").getValue(), containsString("Patient/22/_history/33"));

		assertEquals("44", p.getManagingOrganization().getReference().getIdPart());
		assertEquals(null, p.getManagingOrganization().getReference().getVersionIdPart());
	}

	@Test
	public void testSearchReturnVersionedReferenceInResponse() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findPatientWithVersion");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		List<BundleEntry> entries = ourCtx.newXmlParser().parseBundle(responseContent).getEntries();
		assertEquals(2, entries.size());
		Patient p = (Patient) entries.get(0).getResource();

		assertEquals(0, p.getContained().getContainedResources().size());
		assertEquals("22", p.getId().getIdPart());
		assertEquals("33", p.getId().getVersionIdPart());

		assertEquals("44", p.getManagingOrganization().getReference().getIdPart());
		assertEquals(null, p.getManagingOrganization().getReference().getVersionIdPart());
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName1() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof=po123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"thePartOfId po123 null\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName2() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof.name=poname");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"thePartOfName poname\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName3() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof=po123&partof.name=poname");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"thePartOfId po123 null\""));
		assertThat(responseContent, containsString("value=\"thePartOfName poname\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName4() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof.fooChain=po123&partof.name=poname");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"thePartOfId po123 fooChain\""));
		assertThat(responseContent, containsString("value=\"thePartOfName poname\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName5() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof.bar=po123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"theBarId po123 bar\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName6() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof:Organization.bar=po123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"theBarId Organization/po123 bar\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName7() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization?partof:Organization=po123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("value=\"thePartOfId Organization/po123 null\""));
	}

	@Test
	public void testSearchWithMultipleParamsOfTheSameName8() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Location?partof=po123");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchWithValue() throws Exception {
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

	@Test
	public void testReferenceParamViewToken() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?provider.name=" + URLEncoder.encode("foo|bar", "UTF-8"));
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo|bar", ourLastRefParam.getValue());
		assertEquals("foo", ourLastRefParam.toTokenParam(ourCtx).getSystem());
		assertEquals("bar", ourLastRefParam.toTokenParam(ourCtx).getValue());
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

	@Test
	public void testSearchWithValueAndType() throws Exception {
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

	@Test
	public void testSearchWithValueAndTypeAndChain() throws Exception {
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
        servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		servlet.setResourceProviders(patientProvider, new DummyOrganizationResourceProvider(), new DummyLocationResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	public static class DummyLocationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Location.class;
		}

		//@formatter:off
		@Search
		public List<Location> searchByNameWithDifferentChain(
				@OptionalParam(name = "partof", chainWhitelist= {"bar"}) ReferenceParam theBarId) {
			//@formatter:on

			ArrayList<Location> retVal = new ArrayList<Location>();
			if (theBarId != null) {
				Location loc = new Location();
				loc.setId("1");
				loc.getName().setValue("theBarId " + theBarId.getValue() + " " + theBarId.getChain());
				retVal.add(loc);
			}
			if (retVal.isEmpty()) {
				ourLog.info("No values for bar - Going to fail");
				throw new InternalErrorException("No Values for bar");
			}

			return retVal;
		}

	}

	public static class DummyOrganizationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Organization.class;
		}

		/**
		 * https://github.com/jamesagnew/hapi-fhir/issues/19
		 */
		//@formatter:off
		@Search
		public List<Organization> searchByName(
				@OptionalParam(name = "partof", chainWhitelist= {"", "fooChain"}) ReferenceParam thePartOfId, 
				@OptionalParam(name = "partof.name") StringParam thePartOfName) {
			//@formatter:on

			ArrayList<Organization> retVal = new ArrayList<Organization>();
			if (thePartOfId != null) {
				Organization org = new Organization();
				org.setId("1");
				org.getName().setValue("thePartOfId " + thePartOfId.getValue() + " " + thePartOfId.getChain());
				retVal.add(org);
			}
			if (thePartOfName != null) {
				Organization org = new Organization();
				org.setId("2");
				org.getName().setValue("thePartOfName " + thePartOfName.getValue());
				retVal.add(org);
			}
			if (retVal.isEmpty()) {
				ourLog.info("No values for foo - Going to fail");
				throw new InternalErrorException("No Values for foo");
			}

			return retVal;
		}

		//@formatter:off
		@Search
		public List<Organization> searchByNameWithDifferentChain(
				@OptionalParam(name = "partof", chainWhitelist= {"bar"}) ReferenceParam theBarId) {
			//@formatter:on

			ArrayList<Organization> retVal = new ArrayList<Organization>();
			if (theBarId != null) {
				Organization org = new Organization();
				org.setId("1");
				org.getName().setValue("theBarId " + theBarId.getValue() + " " + theBarId.getChain());
				retVal.add(org);
			}
			if (retVal.isEmpty()) {
				ourLog.info("No values for bar - Going to fail");
				throw new InternalErrorException("No Values for bar");
			}

			return retVal;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient createPatient() {
			Patient p = new Patient();
			p.setId("Patient/22/_history/33");
			p.addIdentifier("urn:foo", "findPatientWithVersion");

			Observation org = new Observation();
			org.setId("Observation/44/_history/55");
			p.setManagingOrganization(new ResourceReferenceDt(org));
			return p;
		}

		@Search
		public List<Patient> findPatient(@OptionalParam(name = Patient.SP_PROVIDER, targetTypes = { Organization.class }) ReferenceParam theParam) {
			ourLastRefParam = theParam;

			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient p = new Patient();
			p.setId("1");
			p.addName().addFamily("0" + theParam.getValueAsQueryToken(ourCtx));
			p.addName().addFamily("1" + defaultString(theParam.getResourceType()));
			p.addName().addFamily("2" + defaultString(theParam.getChain()));
			retVal.add(p);

			return retVal;
		}

		@Search(queryName = "findPatientWithVersion")
		public List<Patient> findPatientWithVersion() {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient p = createPatient();

			retVal.add(p);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			return createPatient();
		}

	}

}
