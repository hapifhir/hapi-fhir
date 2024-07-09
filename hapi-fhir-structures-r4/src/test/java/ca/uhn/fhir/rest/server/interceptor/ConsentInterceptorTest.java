package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOperationStatusEnum;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import com.helger.commons.collection.iterate.EmptyEnumeration;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsentInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ConsentInterceptorTest.class);
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private int myPort;
	private static final DummyPatientResourceProvider ourPatientProvider = new DummyPatientResourceProvider(ourCtx);
	private static final DummySystemProvider ourSystemProvider = new DummySystemProvider();

	@RegisterExtension
	static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(ourPatientProvider)
		.registerProvider(ourSystemProvider)
		.withPagingProvider(new FifoMemoryPagingProvider(10));

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private IConsentService myConsentSvc;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private IConsentService myConsentSvc2;
	private ConsentInterceptor myInterceptor;
	@Captor
	private ArgumentCaptor<BaseServerResponseException> myExceptionCaptor;
	private IGenericClient myFhirClient;

	@AfterEach
	public void after() {
		ourServer.unregisterInterceptor(myInterceptor);
	}

	@BeforeEach
	public void before() {
		myPort = ourServer.getPort();
		myFhirClient = ourServer.getFhirClient();

		myInterceptor = new ConsentInterceptor(myConsentSvc);

		ourServer.registerInterceptor(myInterceptor);
		ourPatientProvider.clear();
	}

	@Test
	public void testOutcomeSuccess() throws IOException {
		Patient patientA = new Patient();
		patientA.setId("Patient/A");
		patientA.setActive(true);
		patientA.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientA.addIdentifier().setSystem("SYSTEM").setValue("VALUEA");
		ourPatientProvider.store(patientA);

		Patient patientB = new Patient();
		patientB.setId("Patient/B");
		patientB.setActive(true);
		patientB.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientB.addIdentifier().setSystem("SYSTEM").setValue("VALUEB");
		ourPatientProvider.store(patientB);

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
	}

	@Test
	public void testTotalModeIgnoredForConsentQueries() throws IOException {
		Patient patientA = new Patient();
		patientA.setId("Patient/A");
		patientA.setActive(true);
		patientA.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientA.addIdentifier().setSystem("SYSTEM").setValue("VALUEA");
		ourPatientProvider.store(patientA);

		Patient patientB = new Patient();
		patientB.setId("Patient/B");
		patientB.setActive(true);
		patientB.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientB.addIdentifier().setSystem("SYSTEM").setValue("VALUEB");
		ourPatientProvider.store(patientB);

		HttpGet httpGet;

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_total=accurate");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(400, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains(Msg.code(2037) + "_total=accurate is not permitted on this server");
		}

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_total=estimated");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).doesNotContain("\"total\"");
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_total=none");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).doesNotContain("\"total\"");
		}
	}

	@Test
	public void testSummaryModeIgnoredTotalForConsentQueries() throws IOException {
		Patient patientA = new Patient();
		patientA.setId("Patient/A");
		patientA.setActive(true);
		patientA.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientA.addIdentifier().setSystem("SYSTEM").setValue("VALUEA");
		ourPatientProvider.store(patientA);

		Patient patientB = new Patient();
		patientB.setId("Patient/B");
		patientB.setActive(true);
		patientB.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientB.addIdentifier().setSystem("SYSTEM").setValue("VALUEB");
		ourPatientProvider.store(patientB);

		HttpGet httpGet;

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_summary=count");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(400, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains(Msg.code(2038) + "_summary=count is not permitted on this server");
		}

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_summary=data");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).doesNotContain("\"total\"");
		}

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.AUTHORIZED);
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_summary=data");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains("\"total\"");
		}

	}

	@Test
	public void testMetadataCallHasChecksSkipped() throws IOException{
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/metadata");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/$meta");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		verify(myConsentSvc, timeout(2000).times(0)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).completeOperationSuccess(any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}


	@Test
	public void testSearch_ShouldProcessCanSeeResourcesReturnsFalse() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.shouldProcessCanSeeResource(any(),any())).thenReturn(false);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.AUTHORIZED);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains("PTA");
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}


	@Test
	public void testSearch_SeeResourceAuthorizesOuterBundle() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.AUTHORIZED);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains("PTA");
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}


	@Test
	public void testSearch_SeeResourceRejectsOuterBundle_ProvidesOperationOutcome() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t->{
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("A DIAG");
			return new ConsentOutcome(ConsentOperationStatusEnum.REJECT, oo);
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).contains("A DIAG");
		}

		verify(myConsentSvc, timeout(10000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(10000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(10000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(10000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(10000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);

	}

	@Test
	public void testSearch_SeeResourceRejectsOuterBundle_ProvidesNothing() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.REJECT);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(204, status.getStatusLine().getStatusCode());
			assertNull(status.getEntity());
			assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(3)).willSeeResource(any(), any(), any()); // the two patients + the bundle
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSearch_SeeResourceRejectsInnerResource_ProvidesOperationOutcome() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		reset(myConsentSvc);
		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if ("PTA".equals(resource.getIdElement().getIdPart())) {
				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setDiagnostics("A DIAG");
				return new ConsentOutcome(ConsentOperationStatusEnum.REJECT, oo);
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle response = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(OperationOutcome.class, response.getEntry().get(0).getResource().getClass());
			assertEquals("A DIAG", ((OperationOutcome) response.getEntry().get(0).getResource()).getIssue().get(0).getDiagnostics());
			assertEquals(Patient.class, response.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", response.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, timeout(1000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(1000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(1000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(1000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSearch_SeeResourceReplacesInnerResource() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if (resource.getIdElement().getIdPart().equals("PTA")) {
				Patient replacement = new Patient();
				replacement.setId("PTA");
				replacement.addIdentifier().setSystem("REPLACEMENT");
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, replacement);
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle response = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
			assertEquals("PTA", response.getEntry().get(0).getResource().getIdElement().getIdPart());
			assertEquals("REPLACEMENT", ((Patient) response.getEntry().get(0).getResource()).getIdentifierFirstRep().getSystem());
			assertEquals(Patient.class, response.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", response.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(4)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testSearch_SeeResourceModifiesInnerResource() throws IOException {
		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			if (resource.getIdElement().getIdPart().equals("PTA")) {
				((Patient)resource).addIdentifier().setSystem("REPLACEMENT");
			}
			return ConsentOutcome.PROCEED;
		});

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle response = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
			assertEquals("PTA", response.getEntry().get(0).getResource().getIdElement().getIdPart());
			assertEquals("REPLACEMENT", ((Patient) response.getEntry().get(0).getResource()).getIdentifierFirstRep().getSystem());
			assertEquals(Patient.class, response.getEntry().get(1).getResource().getClass());
			assertEquals("PTB", response.getEntry().get(1).getResource().getIdElement().getIdPart());
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testPage_SeeResourceReplacesInnerResource() throws IOException {
		Patient pta = (Patient) new Patient().setActive(true).setId("PTA");
		pta.addIdentifier().setSystem("OldSystemA");
		ourPatientProvider.store(pta);
		Patient ptb = (Patient) new Patient().setActive(false).setId("PTB");
		ptb.addIdentifier().setSystem("OldSystemB");
		ourPatientProvider.store(ptb);

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenReturn(ConsentOutcome.PROCEED);

		String nextPageLink;
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?_count=1");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			nextPageLink = bundle.getLink(Constants.LINK_NEXT).getUrl();
		}

		// Now perform a page request
		reset(myConsentSvc);
		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t->{
			IBaseResource resource = (IBaseResource) t.getArguments()[1];
			ourLog.info(resource.getIdElement().getIdPart() + " == PTB");
			if (resource.getIdElement().getIdPart().equals("PTB")) {
				Patient replacement = new Patient();
				replacement.setId("PTB");
				replacement.addIdentifier().setSystem("REPLACEMENT");
				return new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, replacement);
			}
			return ConsentOutcome.PROCEED;
		});

		httpGet = new HttpGet(nextPageLink);
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			Bundle response = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());
			assertEquals("PTB", response.getEntry().get(0).getResource().getIdElement().getIdPart());
			assertEquals("REPLACEMENT", ((Patient) response.getEntry().get(0).getResource()).getIdentifierFirstRep().getSystem());
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
	}


	@Test
	public void testTwoServices_FirstRejectsCanSee() {
		myInterceptor.registerConsentService(myConsentSvc2);

		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.REJECT);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertNull(response.getTotalElement().getValue());
		assertThat(response.getEntry()).isEmpty();

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc2, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
		verifyNoMoreInteractions(myConsentSvc2);
	}

	@Test
	public void testTwoServices_ShouldProcessCanSeeResourcesReturnsFalse_FirstSvcOnly() throws IOException {

		// Setup

		myInterceptor.registerConsentService(myConsentSvc2);

		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));
		ourPatientProvider.store((Patient) new Patient().setActive(false).setId("PTB"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.shouldProcessCanSeeResource(any(),any())).thenReturn(false);
		when(myConsentSvc2.shouldProcessCanSeeResource(any(),any())).thenReturn(true);
		when(myConsentSvc2.canSeeResource(any(),any(),any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.AUTHORIZED);
		when(myConsentSvc2.willSeeResource(any(RequestDetails.class), any(IBaseResource.class), any())).thenAnswer(t-> ConsentOutcome.REJECT);

		// Execute

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {

			// Verify

			assertEquals(200, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
			assertThat(responseContent).doesNotContain("\"entry\"");
		}

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).canSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(2)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(3)).willSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(2)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
	}

	@Test
	public void testTwoServices_FirstAuthorizesCanSee() {
		myInterceptor.registerConsentService(myConsentSvc2);

		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.AUTHORIZED);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertNull(response.getTotalElement().getValue());
		assertThat(response.getEntry()).hasSize(1);

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc2, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
		verifyNoMoreInteractions(myConsentSvc2);
	}

	@Test
	public void testTwoServices_SecondRejectsCanSee() {
		myInterceptor.registerConsentService(myConsentSvc2);

		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.REJECT);
		when(myConsentSvc.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.willSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertNull(response.getTotalElement().getValue());
		assertThat(response.getEntry()).isEmpty();

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc2, timeout(2000).times(1)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
		verifyNoMoreInteractions(myConsentSvc2);
	}

	@Test
	public void testTwoServices_ModificationsInWillSee() {
		myInterceptor.registerConsentService(myConsentSvc2);

		ourPatientProvider.store((Patient) new Patient().setActive(true).setId("PTA"));

		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.canSeeResource(any(), any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.willSeeResource(any(), any(Patient.class), any())).thenAnswer(t->new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, t.getArgument(1, Patient.class).addIdentifier(new Identifier().setSystem("FOO"))));
		when(myConsentSvc2.willSeeResource(any(), any(Patient.class), any())).thenAnswer(t->new ConsentOutcome(ConsentOperationStatusEnum.PROCEED, t.getArgument(1, Patient.class).addIdentifier(new Identifier().setSystem("FOO"))));
		when(myConsentSvc.willSeeResource(any(), any(Bundle.class), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc2.willSeeResource(any(), any(Bundle.class), any())).thenReturn(ConsentOutcome.PROCEED);

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertNull(response.getTotalElement().getValue());
		assertThat(response.getEntry()).hasSize(1);

		Patient patient = (Patient) response.getEntry().get(0).getResource();
		assertThat(patient.getIdentifier()).hasSize(2);

		verify(myConsentSvc, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).startOperation(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).shouldProcessCanSeeResource(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, timeout(2000).times(2)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc2, timeout(2000).times(2)).willSeeResource(any(), any(), any()); // On bundle
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc2, timeout(2000).times(1)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verify(myConsentSvc2, timeout(2000).times(0)).completeOperationFailure(any(), any(), any());
		verifyNoMoreInteractions(myConsentSvc);
		verifyNoMoreInteractions(myConsentSvc2);
	}

	@Mock
	private HttpServletRequest myRequest;
	@Mock
	private HttpServletResponse myResponse;
	@Mock
	private PrintWriter myWriter;
	private HashMap<String, String> myHeaders;

	private void initRequestMocks() {
		myHeaders = new HashMap<>();
		myHeaders.put(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);

		when(myRequest.getRequestURI()).thenReturn("/Patient");
		when(myRequest.getRequestURL()).thenReturn(new StringBuffer(ourServer.getBaseUrl() + "/Patient"));
		when(myRequest.getHeader(any())).thenAnswer(t -> {
			String header = t.getArgument(0, String.class);
			String value = myHeaders.get(header);
			ourLog.info("Request for header '{}' produced: {}", header, value);
			return value;
		});
		when(myRequest.getHeaders(any())).thenAnswer(t -> {
			String header = t.getArgument(0, String.class);
			String value = myHeaders.get(header);
			ourLog.info("Request for header '{}' produced: {}", header, value);
			if (value != null) {
				return new IteratorEnumeration<>(Collections.singleton(value).iterator());
			}
			return new EmptyEnumeration<>();
		});
	}

	/**
	 * Based on the class from Spring Test with the same name
	 */
	public static class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream mySourceStream;
		private boolean myFinished = false;

		public void setExceptionOnClose(boolean theExceptionOnClose) {
			myExceptionOnClose = theExceptionOnClose;
		}

		private boolean myExceptionOnClose = false;

		public DelegatingServletInputStream(InputStream sourceStream) {
			Assert.notNull(sourceStream, "Source InputStream must not be null");
			this.mySourceStream = sourceStream;
		}

		@Override
		public int read() throws IOException {
			int data = this.mySourceStream.read();
			if (data == -1) {
				this.myFinished = true;
			}

			return data;
		}

		@Override
		public int available() throws IOException {
			return this.mySourceStream.available();
		}

		@Override
		public void close() throws IOException {
			super.close();
			this.mySourceStream.close();
			if (myExceptionOnClose) {
				throw new IOException("Failed!");
			}
		}

		@Override
		public boolean isFinished() {
			return this.myFinished;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}
	}

	@Test
	public void testOutcomeException() throws IOException {
		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?searchThrowNullPointerException=1");

		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", responseContent);
		}

		verify(myConsentSvc, timeout(2000).times(0)).completeOperationSuccess(any(), any());
		verify(myConsentSvc, timeout(2000).times(1)).completeOperationFailure(any(), myExceptionCaptor.capture(), any());

		assertEquals(Msg.code(389) + "Failed to call access method: java.lang.NullPointerException: A MESSAGE", myExceptionCaptor.getValue().getMessage());
	}


	@Test
	public void testNoServicesRegistered() {
		myInterceptor.unregisterConsentService(myConsentSvc);

		Patient patientA = new Patient();
		patientA.setId("Patient/A");
		patientA.setActive(true);
		patientA.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientA.addIdentifier().setSystem("SYSTEM").setValue("VALUEA");
		ourPatientProvider.store(patientA);

		Patient patientB = new Patient();
		patientB.setId("Patient/B");
		patientB.setActive(true);
		patientB.addName().setFamily("FAMILY").addGiven("GIVEN");
		patientB.addIdentifier().setSystem("SYSTEM").setValue("VALUEB");
		ourPatientProvider.store(patientB);

		Bundle response = myFhirClient
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(2, response.getTotal());
	}

	@Nested
	class CacheUsage {
		@Mock ICachedSearchDetails myCachedSearchDetails;
		ServletRequestDetails myRequestDetails = new ServletRequestDetails();

		@Test
		void testAuthorizedRequestsMayBeCachedAndUseCache() {
			when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.AUTHORIZED);
			myInterceptor.interceptPreHandled(myRequestDetails);

			assertThat(myInterceptor.interceptPreCheckForCachedSearch(myRequestDetails)).as("AUTHORIZED requests can use cache").isTrue();

			myInterceptor.interceptPreSearchRegistered(myRequestDetails, myCachedSearchDetails);
			verify(myCachedSearchDetails, never()).setCannotBeReused();
		}

		@Test
		void testCanSeeResourceFilteredRequestsMayNotBeCachedNorUseCache() {
			when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
			when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
			when(myConsentSvc.shouldProcessCanSeeResource(any(), any())).thenReturn(true);
			when(myConsentSvc2.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
			when(myConsentSvc2.shouldProcessCanSeeResource(any(), any())).thenReturn(false);
			myInterceptor.registerConsentService(myConsentSvc2);
			myInterceptor.interceptPreHandled(myRequestDetails);

			assertThat(myInterceptor.interceptPreCheckForCachedSearch(myRequestDetails)).as("PROCEED requests can not use cache").isFalse();

			myInterceptor.interceptPreSearchRegistered(myRequestDetails, myCachedSearchDetails);
			verify(myCachedSearchDetails).setCannotBeReused();
		}

		@Test
		void testRequestsWithNoCanSeeFilteringMayBeCachedAndUseCache() {
			when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
			when(myConsentSvc.shouldProcessCanSeeResource(any(), any())).thenReturn(false);
			myInterceptor.interceptPreHandled(myRequestDetails);

			assertThat(myInterceptor.interceptPreCheckForCachedSearch(myRequestDetails)).as("PROCEED requests that promise not to filter can not use cache").isTrue();

			myInterceptor.interceptPreSearchRegistered(myRequestDetails, myCachedSearchDetails);
			verify(myCachedSearchDetails, never()).setCannotBeReused();
		}
	}



	public static class DummyPatientResourceProvider extends HashMapResourceProvider<Patient> {

		public DummyPatientResourceProvider(FhirContext theFhirContext) {
			super(theFhirContext, Patient.class);
		}

		/*
		 * searchThrowNullPointerException
		 */
		@Search
		public List<IBaseResource> searchWithWildcardRetVal(@RequiredParam(name = "searchThrowNullPointerException") StringParam theValue) {
			throw new NullPointerException("A MESSAGE");
		}

	}

	private static class DummySystemProvider{

		@Operation(name = "$meta", idempotent = true, returnParameters = {
			@OperationParam(name = "return", typeName = "Meta")
		})
		public IBaseParameters meta(ServletRequestDetails theRequestDetails) {
			Parameters retval = new Parameters();
			retval.addParameter("Meta", "Yes");
			return retval;
		}
	}
}
