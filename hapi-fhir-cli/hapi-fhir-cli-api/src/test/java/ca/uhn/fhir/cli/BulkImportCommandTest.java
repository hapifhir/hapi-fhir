package ca.uhn.fhir.cli;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportJobParameters;
import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkImportCommandTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportCommandTest.class);

	static {
		System.setProperty("test", "true");
	}

	@RegisterExtension
	public HttpClientExtension myHttpClientExtension = new HttpClientExtension();
	@Mock
	private IJobCoordinator myJobCoordinator;
	private final BulkDataImportProvider myProvider = new BulkDataImportProvider();
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx, myProvider)
		.registerInterceptor(new LoggingInterceptor());
	private Path myTempDir;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartCaptor;

	@BeforeEach
	public void beforeEach() throws IOException {
		myProvider.setFhirContext(myCtx);
		myProvider.setJobCoordinator(myJobCoordinator);
		myTempDir = Files.createTempDirectory("hapifhir");
		ourLog.info("Created temp directory: {}", myTempDir);
	}

	@AfterEach
	public void afterEach() throws IOException {
		ourLog.info("Deleting temp directory: {}", myTempDir);
		FileUtils.deleteDirectory(myTempDir.toFile());
		BulkImportCommand.setEndNowForUnitTest(true);
	}

	@Test
	public void testBulkImport() throws IOException {

		String fileContents1 = "{\"resourceType\":\"Observation\"}\n{\"resourceType\":\"Observation\"}";
		String fileContents2 = "{\"resourceType\":\"Patient\"}\n{\"resourceType\":\"Patient\"}";
		writeNdJsonFileToTempDirectory(fileContents1, "file1.json");
		writeNdJsonFileToTempDirectory(fileContents2, "file2.json");

		when(myJobCoordinator.startInstance(any())).thenReturn("THE-JOB-ID");

		// Start the command in a separate thread
		new Thread(() -> App.main(new String[]{
			BulkImportCommand.BULK_IMPORT,
			"--" + BaseCommand.FHIR_VERSION_PARAM_LONGOPT, "r4",
			"--" + BulkImportCommand.PORT, "0",
			"--" + BulkImportCommand.SOURCE_DIRECTORY, myTempDir.toAbsolutePath().toString(),
			"--" + BulkImportCommand.TARGET_BASE, myRestfulServerExtension.getBaseUrl()
		})).start();

		ourLog.info("Waiting for initiation requests");
		await().until(() -> myRestfulServerExtension.getRequestContentTypes().size(), equalTo(2));
		ourLog.info("Initiation requests complete");

		verify(myJobCoordinator, timeout(10000).times(1)).startInstance(myStartCaptor.capture());

		JobInstanceStartRequest startRequest = myStartCaptor.getValue();
		BulkImportJobParameters jobParameters = startRequest.getParameters(BulkImportJobParameters.class);

		// Reverse order because Patient should be first
		assertEquals(2, jobParameters.getNdJsonUrls().size());
		assertEquals(fileContents2, fetchFile(jobParameters.getNdJsonUrls().get(0)));
		assertEquals(fileContents1, fetchFile(jobParameters.getNdJsonUrls().get(1)));
	}

	@Test
	public void testBulkImport_GzippedFile() throws IOException {

		String fileContents1 = "{\"resourceType\":\"Observation\"}\n{\"resourceType\":\"Observation\"}";
		String fileContents2 = "{\"resourceType\":\"Patient\"}\n{\"resourceType\":\"Patient\"}";
		writeNdJsonFileToTempDirectory(fileContents1, "file1.json.gz");
		writeNdJsonFileToTempDirectory(fileContents2, "file2.json.gz");

		when(myJobCoordinator.startInstance(any())).thenReturn("THE-JOB-ID");

		// Start the command in a separate thread
		new Thread(() -> App.main(new String[]{
			BulkImportCommand.BULK_IMPORT,
			"--" + BaseCommand.FHIR_VERSION_PARAM_LONGOPT, "r4",
			"--" + BulkImportCommand.PORT, "0",
			"--" + BulkImportCommand.SOURCE_DIRECTORY, myTempDir.toAbsolutePath().toString(),
			"--" + BulkImportCommand.TARGET_BASE, myRestfulServerExtension.getBaseUrl()
		})).start();

		ourLog.info("Waiting for initiation requests");
		await().until(() -> myRestfulServerExtension.getRequestContentTypes().size(), equalTo(2));
		ourLog.info("Initiation requests complete");

		verify(myJobCoordinator, timeout(10000).times(1)).startInstance(myStartCaptor.capture());

		JobInstanceStartRequest startRequest = myStartCaptor.getValue();
		BulkImportJobParameters jobParameters = startRequest.getParameters(BulkImportJobParameters.class);

		// Reverse order because Patient should be first
		assertEquals(2, jobParameters.getNdJsonUrls().size());
		assertEquals(fileContents2, fetchFile(jobParameters.getNdJsonUrls().get(0)));
		assertEquals(fileContents1, fetchFile(jobParameters.getNdJsonUrls().get(1)));
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


}
