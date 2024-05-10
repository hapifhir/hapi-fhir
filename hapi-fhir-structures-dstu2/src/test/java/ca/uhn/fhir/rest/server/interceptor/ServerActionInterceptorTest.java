package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServerActionInterceptorTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private IServerInterceptor myInterceptor;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerInterceptor(new ResponseHighlighterInterceptor())
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyObservationResourceProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		myInterceptor = mock(InterceptorAdapter.class);
		ourServer.registerInterceptor(myInterceptor);

		when(myInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.outgoingResponse(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myInterceptor.outgoingResponse(any(RequestDetails.class), any(ResponseDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
	}

	@AfterEach
	public void after() {
		ourServer.unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testRead() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.READ), detailsCapt.capture());

		RequestDetails details = detailsCapt.getValue();
		assertEquals("Patient/123", details.getId().getValue());
	}

	@Test
	public void testVRead() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/_history/456");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.VREAD), detailsCapt.capture());

		RequestDetails details = detailsCapt.getValue();
		assertEquals("Patient/123/_history/456", details.getId().getValue());
	}

	@Test
	public void testCreate() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		ourServer.getFhirClient().create().resource(patient).execute();

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());

		RequestDetails details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceName());
		assertEquals(Patient.class, details.getResource().getClass());
		assertEquals("FAMILY", ((Patient) details.getResource()).getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testCreateWhereMethodHasNoResourceParam() throws Exception {
		Observation observation = new Observation();
		observation.getCode().setText("OBSCODE");
		ourServer.getFhirClient().create().resource(observation).execute();

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());

		RequestDetails details = detailsCapt.getValue();
		assertEquals("Observation", details.getResourceName());
		assertEquals(Observation.class, details.getResource().getClass());
		assertEquals("OBSCODE", ((Observation) details.getResource()).getCode().getText());
	}

	@Test
	public void testUpdate() throws Exception {
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		patient.setId("Patient/123");
		ourServer.getFhirClient().update().resource(patient).execute();

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.UPDATE), detailsCapt.capture());

		RequestDetails details = detailsCapt.getValue();
		assertEquals("Patient", details.getResourceName());
		assertEquals("Patient/123", details.getId().getValue());
		assertEquals(Patient.class, details.getResource().getClass());
		assertEquals("FAMILY", ((Patient) details.getResource()).getName().get(0).getFamily().get(0).getValue());
		assertEquals("Patient/123", ((Patient) details.getResource()).getId().getValue());
	}

	@Test
	public void testHistorySystem() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_SYSTEM), detailsCapt.capture());
	}

	@Test
	public void testHistoryType() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_TYPE), detailsCapt.capture());
		assertEquals("Patient", detailsCapt.getValue().getResourceName());
	}

	@Test
	public void testHistoryInstance() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/_history");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		ArgumentCaptor<RequestDetails> detailsCapt = ArgumentCaptor.forClass(RequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.HISTORY_INSTANCE), detailsCapt.capture());
		assertEquals("Patient", detailsCapt.getValue().getResourceName());
		assertEquals("Patient/123", detailsCapt.getValue().getId().getValue());
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class PlainProvider {

		@History()
		public List<IBaseResource> history() {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList((IBaseResource) retVal);
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			return retVal;
		}

		@History()
		public List<Patient> history() {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList(retVal);
		}

		@History()
		public List<Patient> history(@IdParam IdDt theId) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return Collections.singletonList(retVal);
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient thePatient) {
			Patient retVal = new Patient();
			retVal.setId("Patient/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}

	}


	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}


		@Create()
		public MethodOutcome create(@ResourceParam String theBody) {
			Observation retVal = new Observation();
			retVal.setId("Observation/123/_history/2");
			return new MethodOutcome(retVal.getId());
		}
	}


}
