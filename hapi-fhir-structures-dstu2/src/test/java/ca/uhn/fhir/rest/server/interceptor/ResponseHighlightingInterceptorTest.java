package ca.uhn.fhir.rest.server.interceptor;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.PortUtil;

public class ResponseHighlightingInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlightingInterceptorTest.class);
	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testHighlightNormalResponse() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(Constants.HEADER_ACCEPT)).thenReturn("text/html");

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		RequestDetails reqDetails = new RequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		reqDetails.setServer(new RestfulServer());
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
	}
	
	
	@Test
	public void testHighlightException() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(Constants.HEADER_ACCEPT)).thenReturn("text/html");

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		RequestDetails reqDetails = new RequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		reqDetails.setServer(new RestfulServer());
		reqDetails.setServletRequest(req);

		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new Issue().setDiagnostics("Hello")));
		
		assertFalse(ic.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>OperationOutcome</span>"));
	}
	
	private static final FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testGetRoot() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "This is the base URL of FHIR server. Unable to handle this request, as it does not contain a resource type or operation name."));
		
	}
	

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithWildcardRetVal&_summary=count");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, not(containsString("entry")));
	}

	@Test
	public void testGetInvalidResource() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Foobar/123");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle"));
		
	}
	

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.registerInterceptor(new ResponseHighlighterInterceptor());
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

		@Search(queryName = "searchWithWildcardRetVal")
		public List<? extends IResource> searchWithWildcardRetVal() {
			Patient p = new Patient();
			p.setId("1234");
			p.addName().addFamily("searchWithWildcardRetVal");
			return Collections.singletonList(p);
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

		@Search(queryName = "findPatientsWithNoIdSpecified")
		public List<Patient> findPatientsWithNoIdSpecified() {
			Patient p = new Patient();
			p.addIdentifier().setSystem("foo");
			return Collections.singletonList(p);
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

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.getId().setValue("1");
			return patient;
		}

	}


}
