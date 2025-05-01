package ca.uhn.fhir.batch2.jobs.expunge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteExpungeProviderTest {
	public static final String TEST_JOB_ID = "test-job-id";
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeProviderTest.class);

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public static RestfulServerExtension myServer = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();

	@Mock
	private IDeleteExpungeJobSubmitter myDeleteExpungeJobSubmitter;
	private DeleteExpungeProvider myProvider;

	@BeforeEach
	public void beforeEach() {
		myProvider = new DeleteExpungeProvider(ourCtx, myDeleteExpungeJobSubmitter);
		myServer.registerProvider(myProvider);
	}

	@AfterEach
	public void afterEach() {
		myServer.unregisterProvider(myProvider);
	}

	@Test
	public void testSupplyingNoUrlsProvidesValidErrorMessage() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_DELETE_EXPUNGE);
		try(CloseableHttpResponse execute = myClient.execute(post)) {
			String body = IOUtils.toString(execute.getEntity().getContent(), Charset.defaultCharset());
			assertEquals(400, execute.getStatusLine().getStatusCode());
			assertThat(body).contains("At least one `url` parameter to $delete-expunge must be provided.");
		}
	}

	@Test
	public void testDeleteExpunge() {
		// setup
		Parameters input = new Parameters();
		String url1 = "Observation?status=active";
		String url2 = "Patient?active=false";
		int batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url1);
		input.addParameter(ProviderConstants.OPERATION_DELETE_EXPUNGE_URL, url2);
		input.addParameter(ProviderConstants.OPERATION_DELETE_CASCADE, new BooleanType(true));
		input.addParameter(ProviderConstants.OPERATION_DELETE_CASCADE_MAX_ROUNDS, new IntegerType(44));
		input.addParameter(ProviderConstants.OPERATION_DELETE_BATCH_SIZE, new IntegerType(batchSize));

		when(myDeleteExpungeJobSubmitter.submitJob(any(), any(), anyBoolean(), any(), any())).thenReturn(TEST_JOB_ID);

		// Test
		Parameters response = myServer
			.getFhirClient()
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_DELETE_EXPUNGE)
			.withParameters(input)
			.execute();

		// Verify
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals(TEST_JOB_ID, BatchHelperR4.jobIdFromBatch2Parameters(response));

		verify(myDeleteExpungeJobSubmitter, times(1)).submitJob(
			eq(2401),
			eq(List.of(url1, url2)),
			eq(true),
			eq(44),
			any()
		);
	}

}
