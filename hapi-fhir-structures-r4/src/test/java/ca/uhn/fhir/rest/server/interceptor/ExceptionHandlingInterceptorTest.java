package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.OutgoingFailureResponse;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionHandlingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionHandlingInterceptorTest.class);

	@RegisterExtension
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.withDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider());
	@RegisterExtension
	private static final HttpClientExtension ourClient = new HttpClientExtension();

	private ExceptionHandlingInterceptor myInterceptor;
	private static final String OPERATION_OUTCOME_DETAILS = "OperationOutcomeDetails";
	private static Class<? extends Exception> ourExceptionType;
	private static boolean ourGenerateOperationOutcome;

	@BeforeEach
	public void beforeEach() {
		ourGenerateOperationOutcome = false;
		ourExceptionType=null;

		myInterceptor = new ExceptionHandlingInterceptor();
		myInterceptor.setReturnStackTracesForExceptionTypes(Throwable.class);
		ourServer.registerInterceptor(myInterceptor);
	}

	@Test
	public void testInternalError() throws Exception {
		myInterceptor.setReturnStackTracesForExceptionTypes(Throwable.class);
		{
			HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?throwInternalError=aaa");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Exception Text");
			assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("InternalErrorException: Exception Text");
		}
	}

	@Test
	public void ExceptionHandlingInterceptor_HandlesFailure_WhenWriting() throws IOException {

		//Given: We have an interceptor which causes a failure after the response output stream has been started.
		ProblemGeneratingInterceptor interceptor = new ProblemGeneratingInterceptor();
		ourServer.registerInterceptor(interceptor);

		//When: We make a request to the server, triggering this exception to be thrown on an otherwise successful request
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?succeed=true");
		httpGet.setHeader("Accept-encoding", "gzip");
		HttpResponse status = ourClient.execute(httpGet);
		ourServer.unregisterInterceptor(interceptor);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(500, status.getStatusLine().getStatusCode());
		OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");
	}

	@Test
	public void ExceptionHandlingInterceptor_ReturnsHttpResponseCode_WhenExceptionThrown() throws IOException {

		//Given: We have an interceptor which throws an Exception
		ProblemGeneratingInterceptor problemInterceptor = new ProblemGeneratingInterceptor();
		ourServer.registerInterceptor(problemInterceptor);

		AlterHttpResponseCodeInterceptorToValid404Value alterHttpResponseCodeInterceptorToValid404Value =
			 new AlterHttpResponseCodeInterceptorToValid404Value();
		AlterHttpResponseCodeInterceptorToInvalid999Value alterHttpResponseCodeInterceptorToInvalid999Value =
			 new AlterHttpResponseCodeInterceptorToInvalid999Value();
		//When: We make a request to the server, triggering this exception to be thrown on an otherwise successful request
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?succeed=true");
		httpGet.setHeader("Accept-encoding", "gzip");
		HttpResponse status = ourClient.execute(httpGet);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(500, status.getStatusLine().getStatusCode());
		OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");

		//When: We add an Interceptor which will return an alternate Http Response Code, it gets returned to the caller
		ourServer.registerInterceptor(alterHttpResponseCodeInterceptorToValid404Value);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?succeed=true");
		httpGet.setHeader("Accept-encoding", "gzip");
		status = ourClient.execute(httpGet);
		ourServer.unregisterInterceptor(alterHttpResponseCodeInterceptorToValid404Value);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(HttpStatus.NOT_FOUND.value(), status.getStatusLine().getStatusCode());
		oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");

		//When: We add an Interceptor which will return an invalid Http Response Code, the original value is returned to the caller
		ourServer.registerInterceptor(alterHttpResponseCodeInterceptorToInvalid999Value);
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?succeed=true");
		httpGet.setHeader("Accept-encoding", "gzip");
		status = ourClient.execute(httpGet);
		ourServer.unregisterInterceptor(problemInterceptor);
		ourServer.unregisterInterceptor(alterHttpResponseCodeInterceptorToInvalid999Value);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(500, status.getStatusLine().getStatusCode());
		oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");
	}

	@Test
	public void testInternalErrorFormatted() throws Exception {
		{
			HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?throwInternalError=aaa&_format=true");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(500, status.getStatusLine().getStatusCode());
			OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
			assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Exception Text");
			assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("InternalErrorException: Exception Text");
		}
	}



	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class ProblemGeneratingInterceptor {
		@Hook(Pointcut.SERVER_OUTGOING_WRITER_CREATED)
		public void intercept(RequestDetails theRequestDetails) throws IOException {
			if (theRequestDetails.getUserData().get("writer_exception") == null) {
				theRequestDetails.getUserData().put("writer_exception", "called");
				throw new IOException("Simulated IOException");
			}
		}
	}

	public static class AlterHttpResponseCodeInterceptorToValid404Value {
		@Hook(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME)
		public void intercept(RequestDetails theRequestDetails, IBaseOperationOutcome theResponse, OutgoingFailureResponse theOutgoingFailureResponse) throws IOException {
			theOutgoingFailureResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
		}
	}

	public static class AlterHttpResponseCodeInterceptorToInvalid999Value {
		@Hook(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME)
		public void intercept(RequestDetails theRequestDetails, IBaseOperationOutcome theResponse, OutgoingFailureResponse theOutgoingFailureResponse) throws IOException {
			theOutgoingFailureResponse.setStatusCode(999);
		}
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			OperationOutcome oo = null;
			if (ourGenerateOperationOutcome) {
				oo = new OperationOutcome();
				oo.addIssue().setDiagnostics(OPERATION_OUTCOME_DETAILS);
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
		@Search
		public List<Patient> succeed(@RequiredParam(name = "succeed") StringParam theParam) {
			Patient p = new Patient();
			p.setId("Patient/123");
			return List.of(p);
		}
	}


}
