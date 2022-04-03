package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
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

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	private final BulkImportFileServlet myBulkImportFileServlet = new BulkImportFileServlet();
	@RegisterExtension
	private final HttpServletExtension myHttpServletExtension = new HttpServletExtension()
		.withServlet(myBulkImportFileServlet);
	private final FetchFilesStep mySvc = new FetchFilesStep();

	@Mock
	private IJobDataSink<NdJsonFileJson> myJobDataSink;

	@Test
	public void testFetchWithBasicAuth() {

		// Setup

		String index = myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Patient\"}");

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setHttpBasicCredentials("admin:password");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, INSTANCE_ID, CHUNK_ID);

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
		String index = myBulkImportFileServlet.registerFileByContents(resource);

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setMaxBatchResourceCount(3);
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, INSTANCE_ID, CHUNK_ID);

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		verify(myJobDataSink, times(4)).accept(any(NdJsonFileJson.class));

	}

	@Test
	public void testFetchWithBasicAuth_InvalidCredential() {

		// Setup

		String index = myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Patient\"}");

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setHttpBasicCredentials("admin");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, INSTANCE_ID, CHUNK_ID);

		// Test & Verify

		assertThrows(JobExecutionFailedException.class, () -> mySvc.run(details, myJobDataSink));
	}
}
