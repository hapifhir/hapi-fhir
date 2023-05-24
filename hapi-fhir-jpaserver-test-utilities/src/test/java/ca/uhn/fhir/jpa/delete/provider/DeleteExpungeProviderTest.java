package ca.uhn.fhir.jpa.delete.provider;

import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_DELETE_CASCADE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_DELETE_CASCADE_MAX_ROUNDS;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_DELETE_EXPUNGE_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteExpungeProviderTest {

	@Mock
	private IDeleteExpungeJobSubmitter myJobSubmitter;
	private static final FhirContext myCtx = FhirContext.forR4Cached();

	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(myCtx)
		.withServer(t->t.registerProvider(new DeleteExpungeProvider(myCtx, myJobSubmitter)));
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	@Captor
	private ArgumentCaptor<Integer> myBatchSizeCaptor;
	@Captor
	private ArgumentCaptor<List<String>> myUrlsCaptor;
	@Captor
	private ArgumentCaptor<Boolean> myCascadeCaptor;
	@Captor
	private ArgumentCaptor<Integer> myCascadeMaxRoundsCaptor;

	@Test
	public void testSupplyingNoUrlsProvidesValidErrorMessage() throws IOException {
			HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_DELETE_EXPUNGE);
			try(CloseableHttpResponse execute = myClient.execute(post)) {
				String body = IOUtils.toString(execute.getEntity().getContent(), Charset.defaultCharset());
				assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(400)));
				assertThat(body, is(containsString("At least one `url` parameter to $delete-expunge must be provided.")));
			}
	}


	@Test
	public void testDeleteExpunge() throws IOException {
		// Setup
		when(myJobSubmitter.submitJob(any(), any(), anyBoolean(), any(), any())).thenReturn("ABCDEFG");

		String url = myServer.getBaseUrl() +
			"/" +
			ProviderConstants.OPERATION_DELETE_EXPUNGE +
			"?" + OPERATION_DELETE_EXPUNGE_URL + "=Patient?_id=123" +
			"&" + OPERATION_DELETE_CASCADE + "=true" +
			"&" + OPERATION_DELETE_CASCADE_MAX_ROUNDS + "=2";
		HttpPost post = new HttpPost(url);

		// Test
		try(CloseableHttpResponse execute = myClient.execute(post)) {
			String body = IOUtils.toString(execute.getEntity().getContent(), Charset.defaultCharset());
			assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(200)));
			assertThat(body, is(containsString("At least one `url` parameter to $delete-expunge must be provided.")));
		}

		// Verify
		verify(myJobSubmitter, times(1)).submitJob(myBatchSizeCaptor.capture(), myUrlsCaptor.capture(), myCascadeCaptor.capture(), myCascadeMaxRoundsCaptor.capture(), isNotNull());
		assertNull(myBatchSizeCaptor.getValue());
		assertThat(myUrlsCaptor.getValue(), contains("Patient?_id=123"));
		assertTrue(myCascadeCaptor.getValue());
		assertEquals(2, myCascadeMaxRoundsCaptor.getValue());
	}


}
