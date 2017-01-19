package ca.uhn.fhir.rest.server.interceptor;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.ExceptionTest;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class ExceptionHandlingInterceptorTest {

	private static ExceptionHandlingInterceptor myInterceptor;
	private static final String OPERATION_OUTCOME_DETAILS = "OperationOutcomeDetails";
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static Class<? extends Exception> ourExceptionType;
	private static boolean ourGenerateOperationOutcome;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	
	@Before
	public void before() {
		ourGenerateOperationOutcome = false;
		ourExceptionType=null;
		
		myInterceptor.setReturnStackTracesForExceptionTypes(Throwable.class);
	}

	@Test
	public void testInternalError() throws Exception {
		myInterceptor.setReturnStackTracesForExceptionTypes(Throwable.class);
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwInternalError=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("Exception Text"));
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), (StringContains.containsString("InternalErrorException: Exception Text")));
		}
	}

	@Test
	public void testInternalErrorFormatted() throws Exception {
		myInterceptor.setReturnStackTracesForExceptionTypes(Throwable.class);
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?throwInternalError=aaa&_format=true");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) servlet.getFhirContext().newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), StringContains.containsString("Exception Text"));
			assertThat(oo.getIssueFirstRep().getDetails().getValue(), (StringContains.containsString("InternalErrorException: Exception Text")));
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
		servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		myInterceptor = new ExceptionHandlingInterceptor();
		servlet.registerInterceptor(myInterceptor);
	}


	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {


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

		@Search
		public List<Patient> throwInternalError(@RequiredParam(name = "throwInternalError") StringParam theParam) {
			throw new InternalErrorException("Exception Text");
		}

		@Search()
		public List<Patient> throwUnprocessableEntity(@RequiredParam(name = "throwUnprocessableEntity") StringParam theParam) {
			throw new UnprocessableEntityException("Exception Text");
		}

		@Search
		public List<Patient> throwUnprocessableEntityWithMultipleMessages(@RequiredParam(name = "throwUnprocessableEntityWithMultipleMessages") StringParam theParam) {
			throw new UnprocessableEntityException("message1", "message2", "message3");
		}

	}

}
