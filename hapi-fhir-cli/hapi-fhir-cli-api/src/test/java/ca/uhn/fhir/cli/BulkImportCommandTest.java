package ca.uhn.fhir.cli;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportReportJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.InstantType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkImportCommandTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportCommandTest.class);
	public static final String REPORT_TEXT = "Bulk Import Report\n-------------------\nThis is the text!";

	static {
		HapiSystemProperties.enableTestMode();
		HapiSystemProperties.enableUnitTestMode();
	}

	@RegisterExtension
	public HttpClientExtension myHttpClientExtension = new HttpClientExtension();
	@Mock(strictness = Mock.Strictness.LENIENT)
	private IJobCoordinator myJobCoordinator;
	private final BulkDataImportProvider myProvider = new BulkDataImportProvider();
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx, myProvider)
		.registerInterceptor(new LoggingInterceptor());
	private Path myTempDir;
	@RegisterExtension
	private LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(BulkImportCommand.class);

	@BeforeEach
	public void beforeEach() throws IOException {
		myProvider.setFhirContext(myCtx);
		myProvider.setJobCoordinator(myJobCoordinator);
		myProvider.setRequestPartitionHelperService(myRequestPartitionHelperSvc);
		myTempDir = Files.createTempDirectory("hapifhir");
		ourLog.info("Created temp directory: {}", myTempDir);
	}

	@AfterEach
	public void afterEach() throws IOException {
		ourLog.info("Deleting temp directory: {}", myTempDir);
		FileUtils.deleteDirectory(myTempDir.toFile());
	}

	private Batch2JobStartResponse createJobStartResponse(String theId) {
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(theId);
		return response;
	}

	@Test
	public void testBulkImport() throws IOException {

		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.COMPLETED)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"))
			.setReport(createSerializedReport());
		when(myJobCoordinator.getInstance(eq("THE-JOB-ID"))).thenReturn(jobInfo);

		String fileContents1 = "{\"resourceType\":\"Observation\"}\n{\"resourceType\":\"Observation\"}";
		String fileContents2 = "{\"resourceType\":\"Patient\"}\n{\"resourceType\":\"Patient\"}";
		writeNdJsonFileToTempDirectory(fileContents1, "file1.json");
		writeNdJsonFileToTempDirectory(fileContents2, "file2.json");

		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(any(), any())).thenReturn(RequestPartitionId.allPartitions());

		// Reverse order because Patient should be first
		when(myJobCoordinator.startInstance(any(), any())).thenAnswer(new StartRequestFetchFilesVerifier(fileContents2, fileContents1));

		// Run the command
		App.main(new String[]{
			BulkImportCommand.BULK_IMPORT,
			"--" + BaseCommand.FHIR_VERSION_PARAM_LONGOPT, "r4",
			"--" + BulkImportCommand.PORT, "0",
			"--" + BulkImportCommand.SOURCE_DIRECTORY, myTempDir.toAbsolutePath().toString(),
			"--" + BulkImportCommand.TARGET_BASE, myRestfulServerExtension.getBaseUrl()
		});

		assertThat(myRestfulServerExtension.getRequestContentTypes()).containsExactly(
			null, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON_NEW
		);

		verify(myJobCoordinator, timeout(10000).times(1)).startInstance(any(), any());

		ILoggingEvent message = myLogbackTestExtension.findLogEventWithFormattedMessage(REPORT_TEXT).orElseThrow();
		assertThat(message.getFormattedMessage()).startsWith("Output:");
	}

	private static String createSerializedReport() {
		BulkImportReportJson report = new BulkImportReportJson();
		report.setReportMsg(REPORT_TEXT);
		String serializedReport = JsonUtil.serialize(report);
		return serializedReport;
	}

	@Test
	public void testBulkImport_GzippedFile() throws IOException {

		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.COMPLETED)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"));

		when(myJobCoordinator.getInstance(eq("THE-JOB-ID"))).thenReturn(jobInfo);

		String fileContents1 = "{\"resourceType\":\"Observation\"}\n{\"resourceType\":\"Observation\"}";
		String fileContents2 = "{\"resourceType\":\"Patient\"}\n{\"resourceType\":\"Patient\"}";
		writeNdJsonFileToTempDirectory(fileContents1, "file1.json.gz");
		writeNdJsonFileToTempDirectory(fileContents2, "file2.json.gz");

		when(myJobCoordinator.startInstance(any(), any()))
			.thenReturn(createJobStartResponse("THE-JOB-ID"));
		when(myJobCoordinator.startInstance(any(), any())).thenAnswer(new StartRequestFetchFilesVerifier(fileContents2, fileContents1));

		// Run the command
		App.main(new String[]{
			BulkImportCommand.BULK_IMPORT,
			"--" + BaseCommand.FHIR_VERSION_PARAM_LONGOPT, "r4",
			"--" + BulkImportCommand.PORT, "0",
			"--" + BulkImportCommand.SOURCE_DIRECTORY, myTempDir.toAbsolutePath().toString(),
			"--" + BulkImportCommand.TARGET_BASE, myRestfulServerExtension.getBaseUrl()
		});

		assertThat(myRestfulServerExtension.getRequestContentTypes()).containsExactly(
			null, Constants.CT_FHIR_JSON_NEW, Constants.CT_FHIR_JSON_NEW
		);

		verify(myJobCoordinator, times(1)).startInstance(any(), any());
	}

	@Test
	public void testBulkImport_FAILED() throws IOException {

		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.FAILED)
			.setErrorMessage("This is the error message")
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"))
			.setReport(createSerializedReport());

		when(myJobCoordinator.getInstance(eq("THE-JOB-ID"))).thenReturn(jobInfo);

		String fileContents1 = "{\"resourceType\":\"Observation\"}\n{\"resourceType\":\"Observation\"}";
		String fileContents2 = "{\"resourceType\":\"Patient\"}\n{\"resourceType\":\"Patient\"}";
		writeNdJsonFileToTempDirectory(fileContents1, "file1.json");
		writeNdJsonFileToTempDirectory(fileContents2, "file2.json");

		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(any(), any())).thenReturn(RequestPartitionId.allPartitions());
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(createJobStartResponse("THE-JOB-ID"));

		// Run the command
		App.main(new String[]{
			BulkImportCommand.BULK_IMPORT,
			"--" + BaseCommand.FHIR_VERSION_PARAM_LONGOPT, "r4",
			"--" + BulkImportCommand.PORT, "0",
			"--" + BulkImportCommand.SOURCE_DIRECTORY, myTempDir.toAbsolutePath().toString(),
			"--" + BulkImportCommand.TARGET_BASE, myRestfulServerExtension.getBaseUrl()
		});

		ILoggingEvent message = myLogbackTestExtension.findLogEventWithFormattedMessage("Job is in FAILED state").orElseThrow();
		assertThat(message.getFormattedMessage()).contains("Last error: This is the error message");

		message = myLogbackTestExtension.findLogEventWithFormattedMessage("Output:").orElseThrow();
		assertThat(message.getFormattedMessage()).contains(REPORT_TEXT);
	}

	private String fetchFile(String url) throws IOException {
		String outcome;
		try (CloseableHttpResponse response = myHttpClientExtension.getClient().execute(new HttpGet(url))) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		}
		return outcome;
	}

	private void writeNdJsonFileToTempDirectory(String theContents, String theFileName) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(new File(myTempDir.toFile(), theFileName), false)) {
			OutputStream os = fos;
			if (theFileName.endsWith(".gz")) {
				os = new GZIPOutputStream(os);
			}
			try (Writer w = new OutputStreamWriter(os)) {
				w.append(theContents);
			}
		}
	}
	private Date parseDate(String theString) {
		return new InstantType(theString).getValue();
	}


	private class StartRequestFetchFilesVerifier implements Answer<Object> {
		private final String[] myFileContents;

		public StartRequestFetchFilesVerifier(String... theFileContents) {
			myFileContents = theFileContents;
		}

		@Override
		public Object answer(InvocationOnMock t) throws Throwable {
			JobInstanceStartRequest startRequest = t.getArgument(1, JobInstanceStartRequest.class);
			BulkImportJobParameters jobParameters = startRequest.getParameters(BulkImportJobParameters.class);

			assertThat(jobParameters.getNdJsonUrls()).hasSize(myFileContents.length);
			for (int i = 0; i < myFileContents.length; i++) {
				assertEquals(myFileContents[i], BulkImportCommandTest.this.fetchFile(jobParameters.getNdJsonUrls().get(i)));
			}

			return BulkImportCommandTest.this.createJobStartResponse("THE-JOB-ID");
		}
	}
}
