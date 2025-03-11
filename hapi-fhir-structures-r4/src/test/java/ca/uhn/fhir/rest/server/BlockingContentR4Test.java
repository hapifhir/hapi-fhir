package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.test.utilities.server.ResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class BlockingContentR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BlockingContentR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(ourCtx)
		.withIdleTimeout(5000);
	@RegisterExtension
	public ResourceProviderExtension myPatientProviderRule = new ResourceProviderExtension(ourServerRule, new SystemProvider());
	private volatile BaseServerResponseException myServerException;
	private IAnonymousInterceptor myErrorCaptureInterceptor;

	@BeforeEach
	public void beforeEach() {
		myErrorCaptureInterceptor = (thePointcut, theArgs) -> myServerException = theArgs.get(BaseServerResponseException.class);
		ourServerRule.registerAnonymousInterceptor(Pointcut.SERVER_HANDLE_EXCEPTION, myErrorCaptureInterceptor);
	}
	@AfterEach
	public void afterEach() {
		ourServerRule.unregisterInterceptor(myErrorCaptureInterceptor);
	}

	/**
	 * This test is just verifying that a client that is using an HTTP 100 continue followed
	 * by a network timeout generates a useful error message.
	 */
	@Test
	public void testCreateWith100Continue() throws Exception {
		Patient patient = new Patient();
		patient.setActive(true);

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry().setResource(patient).setFullUrl("Patient/").getRequest().setMethod(Bundle.HTTPVerb.POST);

		RequestConfig config = RequestConfig.custom().setExpectContinueEnabled(true).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		builder.setDefaultRequestConfig(config);
		try (CloseableHttpClient client = builder.build()) {

			String resourceAsString = ourCtx.newJsonParser().encodeResourceToString(input);

			byte[] bytes = resourceAsString.getBytes(Charsets.UTF_8);
			InputStream inputStream = new ByteArrayInputStream(bytes);
			HttpEntity entity = new InputStreamEntity(inputStream, ContentType.parse("application/fhir+json")){
				@Override
				public long getContentLength() {
					return bytes.length + 100;
				}
			};

			HttpPost post = new HttpPost("http://localhost:" + ourServerRule.getPort() + "/");
			post.setEntity(entity);
			try (CloseableHttpResponse resp = client.execute(post)) {
				ourLog.info(Arrays.asList(resp.getAllHeaders()).toString().replace(", ", "\n"));
				ourLog.info(resp.toString());
				await().until(()->myServerException != null);
			}

			assertThat(myServerException.toString()).contains("Idle timeout expired");

		}
	}


	public static class SystemProvider {

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput) {
			return theInput;
		}

	}

}
