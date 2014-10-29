package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.net.UrlEscapers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = new FhirContext();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchTest.class);
	private static int ourPort;

	private static Server ourServer;

	@Test
	public void testOmitEmptyOptionalParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals(null, p.getNameFirstRep().getFamilyFirstRep().getValue());
	}

	@Test
	public void testReturnLinks() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findWithLinks");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("AAANamed", p.getIdentifierFirstRep().getValue().getValue());
		assertEquals("http://foo/Patient?_id=1", bundle.getEntries().get(0).getLinkSearch().getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/9988", bundle.getEntries().get(0).getLinkAlternate().getValue());

		assertEquals("http://foo/Patient?_id=1", ResourceMetadataKeyEnum.LINK_SEARCH.get(p));
		assertEquals("http://localhost:" + ourPort + "/Patient/9988", ResourceMetadataKeyEnum.LINK_ALTERNATE.get(p));

	}

	@Test
	public void testSearchById() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("idaaa", p.getNameFirstRep().getFamilyAsSingleString());
		assertEquals("IDAAA (identifier123)", bundle.getEntries().get(0).getTitle().getValue());
	}

	@Test
	public void testSearchWithOrList() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?findPatientWithOrList=aaa,bbb");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("aaa", p.getIdentifier().get(0).getValue().getValue());
		assertEquals("bbb", p.getIdentifier().get(1).getValue().getValue());
	}
	
	@Test
	public void testSearchWithTokenParameter() throws Exception {
		String token = UrlEscapers.urlFragmentEscaper().asFunction().apply("http://www.dmix.gov/vista/2957|301");
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?tokenParam="+token);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("http://www.dmix.gov/vista/2957", p.getNameFirstRep().getFamilyAsSingleString());
		assertEquals("301", p.getNameFirstRep().getGivenAsSingleString());
	}
	
	
	@Test
	public void testSearchByPost() throws Exception {
		HttpPost filePost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");

		// add parameters to the post method
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("_id", "aaa"));

		UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, "UTF-8");
		filePost.setEntity(sendentity);

		HttpResponse status = ourClient.execute(filePost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("idaaa", p.getNameFirstRep().getFamilyAsSingleString());
		assertEquals("IDAAA (identifier123)", bundle.getEntries().get(0).getTitle().getValue());
	}

	@Test
	public void testSearchCompartment() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/fooCompartment");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("fooCompartment", p.getIdentifierFirstRep().getValue().getValue());
		assertThat(bundle.getEntries().get(0).getId().getValue(), containsString("Patient/123"));
	}

	@Test
	public void testSearchGetWithUnderscoreSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/_search?subject%3APatient=100&name=3141-9%2C8302-2%2C8287-5%2C39156-5");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Observation p = bundle.getResources(Observation.class).get(0);
		assertEquals("Patient/100", p.getSubject().getReference().toString());
		assertEquals(4, p.getName().getCoding().size());
		assertEquals("3141-9", p.getName().getCoding().get(0).getCode().getValue());
		assertEquals("8302-2", p.getName().getCoding().get(1).getCode().getValue());

	}

	@Test
	public void testSpecificallyNamedQueryGetsPrecedence() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?AAA=123");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		Patient p = bundle.getResources(Patient.class).get(0);
		assertEquals("AAA", p.getIdentifierFirstRep().getValue().getValue());

		// Now the named query

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findPatientByAAA&AAA=123");

		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		p = bundle.getResources(Patient.class).get(0);
		assertEquals("AAANamed", p.getIdentifierFirstRep().getValue().getValue());
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
		RestfulServer servlet = new RestfulServer();
		servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		servlet.setResourceProviders(patientProvider, new DummyObservationResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

		@Search
		public Observation search(@RequiredParam(name = "subject") ReferenceParam theSubject, @RequiredParam(name = "name") TokenOrListParam theName) {
			Observation o = new Observation();
			o.setId("1");

			o.getSubject().setReference(theSubject.getResourceType() + "/" + theSubject.getIdPart());
			for (BaseCodingDt next : theName.getListAsCodings()) {
				o.getName().getCoding().add(new CodingDt(next));
			}

			return o;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search(compartmentName = "fooCompartment")
		public List<Patient> compartment(@IdParam IdDt theId) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId(theId);
			patient.addIdentifier("system", "fooCompartment");
			retVal.add(patient);
			return retVal;
		}

		@Search
		public List<Patient> findPatient(@RequiredParam(name = "_id") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "identifier123");
			if (theParam != null) {
				patient.addName().addFamily("id" + theParam.getValue());
			}
			retVal.add(patient);
			return retVal;
		}

		@Search
		public List<Patient> findPatientByAAA01(@RequiredParam(name = "AAA") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "AAA");
			retVal.add(patient);
			return retVal;
		}

		@Search(queryName = "findPatientByAAA")
		public List<Patient> findPatientByAAA02Named(@OptionalParam(name = "AAA") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "AAANamed");
			retVal.add(patient);
			return retVal;
		}

		@Search()
		public List<Patient> findPatientWithOrList(@RequiredParam(name = "findPatientWithOrList") StringOrListParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			for (StringParam next : theParam.getValuesAsQueryTokens()) {
				patient.addIdentifier("system", next.getValue());
			}
			retVal.add(patient);
			return retVal;
		}

		
		@Search()
		public List<Patient> findPatientWithToken(@RequiredParam(name = "tokenParam") TokenParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addName().addFamily(theParam.getSystem()).addGiven(theParam.getValue());
			retVal.add(patient);
			return retVal;
		}

		
		@Search(queryName = "findWithLinks")
		public List<Patient> findWithLinks() {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "AAANamed");
			ResourceMetadataKeyEnum.LINK_SEARCH.put(patient, ("http://foo/Patient?_id=1"));
			ResourceMetadataKeyEnum.LINK_ALTERNATE.put(patient, ("Patient/9988"));
			retVal.add(patient);
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
