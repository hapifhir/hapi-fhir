package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobInstanceParameters;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Base64Utils;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FetchFilesStepTest {

	private final BulkImportFileServlet myBulkImportFileServlet = new BulkImportFileServlet();
	@RegisterExtension
	private final HttpServletExtension myHttpServletExtension = new HttpServletExtension()
		.withServlet(myBulkImportFileServlet);
	private final FetchFilesStep mySvc = new FetchFilesStep();

	@Mock
	private IJobDataSink myJobDataSink;

	@Test
	public void testFetchWithBasicAuth() {

		// Setup

		myBulkImportFileServlet.registerFile(() -> new StringReader("{\"resourceType\":\"Patient\"}"));

		JobInstanceParameters parameters = JobInstanceParameters
			.newBuilder()
			.withEntry(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0")
			.withEntry(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS, "admin:password")
			.build();
		StepExecutionDetails details = new StepExecutionDetails(parameters, null);

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		assertEquals(1, myHttpServletExtension.getRequestHeaders().size());

		String expectedAuthHeader = "Authorization: Basic " + Base64Utils.encodeToString("admin:password".getBytes(StandardCharsets.UTF_8));
		assertThat(myHttpServletExtension.toString(), myHttpServletExtension.getRequestHeaders().get(0), hasItem(expectedAuthHeader));
	}

	@Test
	public void testFetchWithBasicAuth_SplitIntoBatches() {

		// Setup

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			b.append("{\"resourceType\":\"Patient\"}").append("\n");
		}
		String resource = b.toString();
		myBulkImportFileServlet.registerFile(() -> new StringReader(resource));

		JobInstanceParameters parameters = JobInstanceParameters
			.newBuilder()
			.withEntry(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0")
			.withEntry(BulkImport2AppCtx.PARAM_MAXIMUM_BATCH_RESOURCE_COUNT, "3")
			.build();
		StepExecutionDetails details = new StepExecutionDetails(parameters, null);

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		verify(myJobDataSink, times(4)).accept(any());

	}

	@Test
	public void testFetchWithBasicAuth_InvalidCredential() {

		// Setup

		myBulkImportFileServlet.registerFile(() -> new StringReader("{\"resourceType\":\"Patient\"}"));

		JobInstanceParameters parameters = JobInstanceParameters
			.newBuilder()
			.withEntry(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0")
			.withEntry(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS, "admin")
			.build();
		StepExecutionDetails details = new StepExecutionDetails(parameters, null);

		// Test & Verify

		assertThrows(JobExecutionFailedException.class, () -> mySvc.run(details, myJobDataSink));
	}
}
