package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InterceptorDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static Patient ourLastPatient;
	private IServerInterceptor myInterceptor1;
	private IServerInterceptor myInterceptor2;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@AfterEach
	public void after() {
		ourServer.getInterceptorService().unregisterAllInterceptors();
	}

	@BeforeEach
	public void before() {
		myInterceptor1 = mock(IServerInterceptor.class);
		myInterceptor2 = mock(IServerInterceptor.class);
	}

	private String createInput() {
		return "{\n" +
			"   \"resourceType\":\"Patient\",\n" +
			"   \"id\":\"1855669\",\n" +
			"   \"meta\":{\n" +
			"      \"versionId\":\"1\",\n" +
			"      \"lastUpdated\":\"2016-02-18T07:41:35.953-05:00\"\n" +
			"   },\n" +
			"   \"active\":true\n" +
			"}";
	}


	@Test
	public void testServerPreHandledOnOperationCapturesResource() throws IOException {

		AtomicReference<IBaseResource> resource = new AtomicReference<>();
		IAnonymousInterceptor interceptor = (thePointcut, theArgs) -> {
			RequestDetails requestDetails = theArgs.get(RequestDetails.class);
			resource.set(requestDetails.getResource());
		};

		ourServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);
		try {
			Parameters p = new Parameters();
			p.addParameter().setName("limit").setValue(new IntegerType(123));
			String input = ourCtx.newJsonParser().encodeResourceToString(p);

			ourServer.fhirRequest("/Patient/$postOperation").post(input, "application/fhir+json").assertStatus(200);
		} finally {
			ourServer.unregisterInterceptor(interceptor);
		}

		assertNotNull(resource.get());
	}


	@Test
	public void testModifyResponse() throws IOException {
		InterceptorAdapter interceptor = new InterceptorAdapter() {
			@Override
			public boolean outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
				Patient retVal = new Patient();
				retVal.setId(theResponseDetails.getResponseResource().getIdElement());
				retVal.addName().setFamily("NAME1");
				theResponseDetails.setResponseResource(retVal);
				theResponseDetails.setResponseCode(202);
				return true;
			}
		};
		ourServer.registerInterceptor(interceptor);
		try {

			HttpTestResponse httpResponse = ourServer.fhirRequest("/Patient/1").get().assertStatus(202);
			String response = httpResponse.getBody();
			assertThat(response).contains("NAME1");
			assertEquals("Accepted", httpResponse.getReasonPhrase());

		} finally {
			ourServer.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testResourceResponseIncluded() throws Exception {
		ourServer.getRestfulServer().setInterceptors(myInterceptor1, myInterceptor2);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		when(myInterceptor2.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor2.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		doAnswer(t -> {
			RestOperationTypeEnum type = (RestOperationTypeEnum) t.getArguments()[0];
			RequestDetails det = (RequestDetails) t.getArguments()[1];
			return null;
		}).when(myInterceptor1).incomingRequestPreHandled(any(), any());

		String input = createInput();

		ourServer.fhirRequest("/Patient/$validate").post(input, Constants.CT_FHIR_JSON);

		InOrder order = inOrder(myInterceptor1, myInterceptor2);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor2, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<RequestDetails> arTypeCapt = ArgumentCaptor.forClass(RequestDetails.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor2, times(1)).incomingRequestPreHandled(nullable(RestOperationTypeEnum.class), nullable(RequestDetails.class));

		assertEquals(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE, opTypeCapt.getValue());
		assertNotNull(arTypeCapt.getValue().getResource());
	}

	@Test
	public void testExceptionInProcessingCompletedNormally() throws Exception {
		ourServer.getRestfulServer().setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(input, Constants.CT_FHIR_JSON);
		assertThat(response.getStatusCode()).isBetween(200, 201);
	}

	@Test
	public void testResponseWithNothing() throws Exception {
		ourServer.getRestfulServer().setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		ourServer.fhirRequest("/Patient").post(input, Constants.CT_FHIR_JSON).assertStatus(201);

		InOrder order = inOrder(myInterceptor1);
		verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<RequestDetails> arTypeCapt = ArgumentCaptor.forClass(RequestDetails.class);
		ArgumentCaptor<ServletRequestDetails> rdCapt = ArgumentCaptor.forClass(ServletRequestDetails.class);
		ArgumentCaptor<OperationOutcome> resourceCapt = ArgumentCaptor.forClass(OperationOutcome.class);
		verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), resourceCapt.capture());

		assertThat(resourceCapt.getAllValues()).hasSize(1);
		assertNull(resourceCapt.getAllValues().get(0));
//		assertEquals("", rdCapt.getAllValues().get(0).get)
	}

	@Test
	public void testResponseWithOperationOutcome() throws Exception {
		ourServer.getRestfulServer().setInterceptors(myInterceptor1);

		when(myInterceptor1.incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(OperationOutcome.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(ResponseDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(IBaseResource.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor1.outgoingResponse(nullable(RequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class))).thenReturn(true);

		String input = createInput();

		ourServer.fhirRequest("/Patient/$validate").post(input, Constants.CT_FHIR_JSON);

		InOrder order = inOrder(myInterceptor1);
		order.verify(myInterceptor1, times(1)).incomingRequestPreProcessed(nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		order.verify(myInterceptor1, times(1)).incomingRequestPostProcessed(nullable(ServletRequestDetails.class), nullable(HttpServletRequest.class), nullable(HttpServletResponse.class));
		ArgumentCaptor<RestOperationTypeEnum> opTypeCapt = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		ArgumentCaptor<RequestDetails> arTypeCapt = ArgumentCaptor.forClass(RequestDetails.class);
		ArgumentCaptor<OperationOutcome> resourceCapt = ArgumentCaptor.forClass(OperationOutcome.class);
		order.verify(myInterceptor1, times(1)).incomingRequestPreHandled(opTypeCapt.capture(), arTypeCapt.capture());
		order.verify(myInterceptor1, times(1)).outgoingResponse(nullable(ServletRequestDetails.class), resourceCapt.capture());

		assertThat(resourceCapt.getAllValues()).hasSize(1);
		assertEquals(OperationOutcome.class, resourceCapt.getAllValues().get(0).getClass());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testServerOperationInterceptorAdapterMethods() {
		ServerOperationInterceptorAdapter i = new ServerOperationInterceptorAdapter();
		i.resourceCreated(null, null);
		i.resourceDeleted(null, null);
		i.resourceUpdated(null, null);
		i.resourceUpdated(null, null, null);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource) {
			ourLastPatient = theResource;
			return new MethodOutcome().setCreated(true);
		}

		@Operation(name = "$postOperation")
		public Parameters postOperation(
			@OperationParam(name = "limit") IntegerType theLimit
		) {
			return new Parameters();
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			retVal.addName().setFamily("NAME0");
			return retVal;
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}

	}

}
