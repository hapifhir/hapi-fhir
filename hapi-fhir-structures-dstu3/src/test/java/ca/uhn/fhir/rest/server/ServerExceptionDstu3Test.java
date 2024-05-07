package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerExceptionDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerExceptionDstu3Test.class);
	public static Exception ourException;
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@AfterEach
	public void after() {
		ourException = null;
	}

	@Test
	public void testAddHeadersNotFound() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setCode(IssueType.BUSINESSRULE);

		ourException = new ResourceNotFoundException("SOME MESSAGE")
			.addResponseHeader("X-Foo", "BAR BAR");


		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertEquals("BAR BAR", status.getFirstHeader("X-Foo").getValue());
			assertThat(status.getFirstHeader("X-Powered-By").getValue()).contains("HAPI FHIR");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testResponseUsesCorrectEncoding() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome
			.addIssue()
			.setCode(IssueType.PROCESSING)
			.setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setDiagnostics("El nombre está vacío");

		ourException = new InternalErrorException("Error", operationOutcome);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=json");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			byte[] responseContentBytes = IOUtils.toByteArray(status.getEntity().getContent());
			String responseContent = new String(responseContentBytes, Charsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);
			assertThat(responseContent).contains("El nombre está vacío");
		}

	}

	@Test
	public void testMethodThrowsNonHapiUncheckedExceptionHandledCleanly() throws Exception {

		ourException = new NullPointerException("Hello");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=json");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			byte[] responseContentBytes = IOUtils.toByteArray(status.getEntity().getContent());
			String responseContent = new String(responseContentBytes, Charsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);
			assertThat(responseContent).contains("\"diagnostics\":\"" + Msg.code(389) + "Failed to call access method: java.lang.NullPointerException: Hello\"");
		}

	}

	@Test
	public void testMethodThrowsNonHapiCheckedExceptionHandledCleanly() throws Exception {

		ourException = new IOException("Hello");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=json");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			byte[] responseContentBytes = IOUtils.toByteArray(status.getEntity().getContent());
			String responseContent = new String(responseContentBytes, Charsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);
			assertThat(responseContent).contains("\"diagnostics\":\"" + Msg.code(389) + "Failed to call access method: java.io.IOException: Hello\"");
		}

	}

	@Test
	public void testInterceptorThrowsNonHapiUncheckedExceptionHandledCleanly() throws Exception {

		ourServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				throw new NullPointerException("Hello");
			}
		});

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=json");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			byte[] responseContentBytes = IOUtils.toByteArray(status.getEntity().getContent());
			String responseContent = new String(responseContentBytes, Charsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);
			assertThat(responseContent).contains("\"diagnostics\":\"Hello\"");
		}

		ourServer.getInterceptorService().unregisterAllInterceptors();

	}


	@Test
	public void testPostWithNoBody() throws IOException {

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("Location").getValue()).contains("Patient/123");
		}

	}


	@Test
	public void testAuthorize() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setCode(IssueType.BUSINESSRULE);

		ourException = new AuthenticationException().addAuthenticateHeaderForRealm("REALM");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(401, status.getStatusLine().getStatusCode());
			assertEquals("Basic realm=\"REALM\"", status.getFirstHeader("WWW-Authenticate").getValue());
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> search() throws Exception {
			if (ourException == null) {
				return Collections.emptyList();
			}
			throw ourException;
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			Validate.isTrue(thePatient == null);
			return new MethodOutcome().setId(new IdType("Patient/123"));
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
