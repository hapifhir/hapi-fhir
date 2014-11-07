package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.AssertionFailedError;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ExceptionTest {

	private static CloseableHttpClient ourClient;
	private static boolean ourGenerateOperationOutcome;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionTest.class);
	private static int ourPort;
	private static Server ourServer;

	private static RestfulServer servlet;

	@Before
	public void before() {
		ourGenerateOperationOutcome = false;
		ourExceptionType=null;
	}

	@Test
	public void testThrowUnprocessableEntityWithMultipleMessages() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwUnprocessableEntityWithMultipleMessages=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(422, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("message1"));
			assertEquals(3, oo.getIssue().size());
		}
	}
	
	@Test
	public void testInternalError() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwInternalError=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("InternalErrorException: Exception Text"));
		}
	}

	@Test
	public void testResourceReturning() throws Exception {
		// No OO
		{
			ourExceptionType=ResourceNotFoundException.class;
			ourGenerateOperationOutcome=false;
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(404, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("Resource Patient/123 is not known"));
		}
		// Yes OO
		{
			ourExceptionType=ResourceNotFoundException.class;
			ourGenerateOperationOutcome=true;
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(404, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString(OPERATION_OUTCOME_DETAILS));
		}
	}

	@Test
	public void testInternalErrorFormatted() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwInternalError=aaa&_format=true");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("InternalErrorException: Exception Text"));
		}
	}

	@Test
	public void testInternalErrorJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwInternalError=aaa&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(500, status.getStatusLine().getStatusCode());
		OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newJsonParser().parseResource(responseContent);
		assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("InternalErrorException: Exception Text"));
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
		servlet = new RestfulServer();
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
	
	
	private static Class<? extends Exception> ourExceptionType;

	private static final String OPERATION_OUTCOME_DETAILS = "OperationOutcomeDetails";
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {


		@Search
		public List<Patient> throwInternalError(@RequiredParam(name = "throwInternalError") StringParam theParam) {
			throw new InternalErrorException("Exception Text");
		}

		@Search
		public List<Patient> throwUnprocessableEntityWithMultipleMessages(@RequiredParam(name = "throwUnprocessableEntityWithMultipleMessages") StringParam theParam) {
			throw new UnprocessableEntityException("message1", "message2", "message3");
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			OperationOutcome oo = null;
			if (ourGenerateOperationOutcome) {
				oo = new OperationOutcome();
				oo.addIssue().setDetails(OPERATION_OUTCOME_DETAILS);
			}
			
			if (ourExceptionType == ResourceNotFoundException.class) {
				throw new ResourceNotFoundException(theId, oo);
			}else {
				throw new AssertionFailedError("Unknown exception type: " + ourExceptionType);
			}
			
		}

	}

}
