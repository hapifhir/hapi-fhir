package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.test.utilities.server.ResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BlockingContentR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BlockingContentR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	public ResourceProviderExtension myPatientProviderRule = new ResourceProviderExtension(ourServerRule, new SystemProvider());

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

//			InputStream inputStream = new BlockingInputStream(resourceAsString.getBytes(Charsets.UTF_8));
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
				ourLog.info(IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
			}

		}
	}

	private static class BlockingInputStream extends InputStream {
		private final ByteArrayInputStream myWrap;
		private int myByteCount = 0;

		public BlockingInputStream(byte[] theBytes) {
			myWrap = new ByteArrayInputStream(theBytes);
		}

		@Override
		public int read() throws IOException {
			if (myByteCount++ == 10) {
				ourLog.info("About to block...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					ourLog.warn("Interrupted", e);
				}
			}
			return myWrap.read();
		}
	}


	public static class SystemProvider {

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput) {
			return theInput;
		}

	}

}
