package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
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

		ListMultimap<String, JobInstanceParameter> parameters = ArrayListMultimap.create();
		parameters.put(BulkImport2AppCtx.PARAM_NDJSON_URL, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0"));
		parameters.put(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, "admin:password"));
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
		ListMultimap<String, JobInstanceParameter> parameters = ArrayListMultimap.create();

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			b.append("{\"resourceType\":\"Patient\"}").append("\n");
		}
		String resource = b.toString();
		myBulkImportFileServlet.registerFile(() -> new StringReader(resource));

		parameters.put(BulkImport2AppCtx.PARAM_NDJSON_URL, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0"));
		parameters.put(BulkImport2AppCtx.PARAM_MAXIMUM_BATCH_SIZE, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, "3"));
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

		ListMultimap<String, JobInstanceParameter> parameters = ArrayListMultimap.create();
		parameters.put(BulkImport2AppCtx.PARAM_NDJSON_URL, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, myHttpServletExtension.getBaseUrl() + "/download?index=0"));
		parameters.put(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS, new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, "admin"));
		StepExecutionDetails details = new StepExecutionDetails(parameters, null);

		// Test & Verify

		assertThrows(JobExecutionFailedException.class, () ->	mySvc.run(details, myJobDataSink));
	}
}
