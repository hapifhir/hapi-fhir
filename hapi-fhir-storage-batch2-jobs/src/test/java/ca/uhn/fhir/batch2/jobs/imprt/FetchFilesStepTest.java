package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static ca.uhn.fhir.rest.api.Constants.CT_APP_NDJSON;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON;
import static ca.uhn.fhir.rest.api.Constants.CT_JSON;
import static ca.uhn.fhir.rest.api.Constants.CT_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FetchFilesStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);
	public static final String CHUNK_ID = "chunk-id";

	private final ContentTypeHeaderModifiableBulkImportFileServlet myBulkImportFileServlet = new ContentTypeHeaderModifiableBulkImportFileServlet();
	@RegisterExtension
	private final HttpServletExtension myHttpServletExtension = new HttpServletExtension()
		.withServlet(myBulkImportFileServlet);
	private final FetchFilesStep mySvc = new FetchFilesStep();

	@Mock
	private IJobDataSink<NdJsonFileJson> myJobDataSink;

	@ParameterizedTest
	@ValueSource(strings = {CT_FHIR_NDJSON, CT_FHIR_JSON, CT_FHIR_JSON_NEW, CT_APP_NDJSON, CT_JSON, CT_TEXT})
	public void testFetchWithBasicAuth(String theHeaderContentType) {

		// Setup
		myBulkImportFileServlet.setHeaderContentTypeValue(theHeaderContentType);
		String index = myBulkImportFileServlet.registerFileByContents("{\"resourceType\":\"Patient\"}");

		BulkImportJobParameters parameters = new BulkImportJobParameters()
			.addNdJsonUrl(myHttpServletExtension.getBaseUrl() + "/download?index=" + index)
			.setHttpBasicCredentials("admin:password");
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test

		mySvc.run(details, myJobDataSink);

		// Verify

		assertThat(myHttpServletExtension.getRequestHeaders()).hasSize(1);

		String expectedAuthHeader = "Authorization: Basic " + Base64.getEncoder().encodeToString("admin:password".getBytes(StandardCharsets.UTF_8));
		assertThat(myHttpServletExtension.getRequestHeaders().get(0)).as(myHttpServletExtension.toString()).contains(expectedAuthHeader);
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
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

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
		StepExecutionDetails<BulkImportJobParameters, VoidModel> details = new StepExecutionDetails<>(parameters, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID));

		// Test & Verify

		assertThatExceptionOfType(JobExecutionFailedException.class).isThrownBy(() -> mySvc.run(details, myJobDataSink));
	}

	public static class ContentTypeHeaderModifiableBulkImportFileServlet extends BulkImportFileServlet{

		public String myContentTypeValue;


		public void setHeaderContentTypeValue(String theContentTypeValue) {
			myContentTypeValue = theContentTypeValue;
		}

		@Override
		public String getHeaderContentType() {
			return Objects.nonNull(myContentTypeValue) ? myContentTypeValue : super.getHeaderContentType();
		}
	}

}
