package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionHandlingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionHandlingInterceptorTest.class);

	@RegisterExtension
	private static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.withDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider());

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
			String responseContent = ourServer.fhirRequest("/Patient?throwInternalError=aaa").get().assertStatus(500).getBody();
			ourLog.info(responseContent);
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
		HttpTestResponse response = ourServer.fhirRequest("/Patient?succeed=true")
			.withHeader("Accept-encoding", "gzip")
			.get();
		ourServer.unregisterInterceptor(interceptor);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		String responseContent = response.assertStatus(500).getBody();
		ourLog.info(responseContent);
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
		//When: We make a request to the server, triggering this exception to be thrown on an otherwise successful request
		HttpTestResponse response = ourServer.fhirRequest("/Patient?succeed=true")
			.withHeader("Accept-encoding", "gzip")
			.get();

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		String responseContent = response.assertStatus(500).getBody();
		ourLog.info(responseContent);
		OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");

		//When: We add an Interceptor which will return an alternate Http Response Code, it gets returned to the caller
		ourServer.registerInterceptor(alterHttpResponseCodeInterceptorToValid404Value);
		response = ourServer.fhirRequest("/Patient?succeed=true")
			.withHeader("Accept-encoding", "gzip")
			.get();
		ourServer.unregisterInterceptor(alterHttpResponseCodeInterceptorToValid404Value);

		//Then: This should still return an OperationOutcome, and not explode with an HTML IllegalState response.
		responseContent = response.assertStatus(HttpStatus.NOT_FOUND.value()).getBody();
		ourLog.info(responseContent);
		oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
		ourLog.debug(ourCtx.newXmlParser().encodeResourceToString(oo));
		assertThat(oo.getIssueFirstRep().getDiagnosticsElement().getValue()).contains("Simulated IOException");

	}

	@Test
	public void testInternalErrorFormatted() throws Exception {
		{
			String responseContent = ourServer.fhirRequest("/Patient?throwInternalError=aaa&_format=true").get().assertStatus(500).getBody();
			ourLog.info(responseContent);
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
		public void intercept(RequestDetails theRequestDetails, ResponseDetails theOutgoingFailureResponse) {
			theOutgoingFailureResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
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
