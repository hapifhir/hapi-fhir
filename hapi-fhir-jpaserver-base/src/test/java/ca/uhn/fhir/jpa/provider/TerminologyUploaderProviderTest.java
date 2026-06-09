package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_JOB_ATTACHMENT_ID;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.RESP_PARAM_OUTCOME;
import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.SCT_URI;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("LoggingSimilarMessage")
@TestMethodOrder(value = MethodOrderer.MethodName.class)
@ExtendWith(MockitoExtension.class)
class TerminologyUploaderProviderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyUploaderProviderTest.class);

	private final FhirContext myContext = FhirContext.forR5Cached();

	@Mock
	private ITermLoaderSvc myTerminologyLoaderSvc;

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Mock
	private IJobPersistence myJobPersistence;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@RegisterExtension
	private final RestfulServerExtension myServerExtension = new RestfulServerExtension(myContext)
		.withServer(t -> {
			assert myContext != null;
			assert myTerminologyLoaderSvc != null;
			assert myJobCoordinator != null;
			t.registerProvider(new TerminologyUploaderProvider(myContext, myTerminologyLoaderSvc, myJobCoordinator, myJobPersistence));
		});

	@RegisterExtension
	private final HttpClientExtension myHttpClient = new HttpClientExtension();

	@Captor
	private ArgumentCaptor<AttachmentDetails> myAttachmentDetailsCaptor;

	/**
	 * Make sure we throw a useful error if the user tries to use the old
	 * method.
	 */
	@Test
	void testUploadExternalCodeSystem() {
		// Test
		Attachment attachment = new Attachment();
		attachment.setData(new byte[] { 0x41, 0x41, 0x41, 0x41 });
		attachment.setUrl("http://foo.com/loinc.csv");

		assertThatThrownBy(() ->
			myServerExtension
				.getFhirClient()
				.operation()
				.onType("CodeSystem")
				.named(OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM)
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(LOINC_URI))
				.andParameter(TerminologyUploaderProvider.PARAM_FILE, attachment)
				.execute()
		).isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("The $upload-external-code-system operation has been removed. To upload terminology, see the $hapi.fhir.upload-terminology.create-job operation.");
	}

	@Test
	void testUploadTerminologyCreateJob_Custom() {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId("my-instance-id");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		// Test
		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onType("CodeSystem")
			.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo"))
			.andParameter(TerminologyUploaderProvider.PARAM_VERSION, new StringType("1.2"))
			.execute();

		// Verify
		verify(myJobCoordinator, times(1)).startInstance(notNull(), myStartRequestCaptor.capture());
		assertEquals(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY, myStartRequestCaptor.getValue().getJobDefinitionId());

		ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		assertThat(response.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Upload Custom Terminology Job has been created and is in BUILDING state with ID[my-instance-id]",
			"and then start the job using the http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.start-job operation."
		);
		assertEquals("my-instance-id", response.getParameter(TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID).getValue().toString());
	}

	@Test
	void testUploadTerminologyCreateJob_Loinc() {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId("my-instance-id");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		// Test
		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onType("CodeSystem")
			.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(LOINC_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_VERSION, new StringType("2.69"))
			.execute();

		// Verify
		verify(myJobCoordinator, times(1)).startInstance(notNull(), myStartRequestCaptor.capture());
		assertEquals(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC, myStartRequestCaptor.getValue().getJobDefinitionId());

		ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		assertThat(response.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Upload LOINC Job has been created and is in BUILDING state with ID[my-instance-id]",
			"You can now upload the distribution file(s) (loinc.zip, loincupload.properties) to the job using the http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.attach-file operation",
			"and then start the job using the http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.start-job operation."
		);
		assertEquals("my-instance-id", response.getParameter(TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID).getValue().toString());
	}

	@Test
	void testUploadTerminologyCreateJob_SnomedCt() {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId("my-instance-id");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		// Test
		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onType("CodeSystem")
			.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(SCT_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_VERSION, new StringType("20260501T120000Z"))
			.execute();

		// Verify
		verify(myJobCoordinator, times(1)).startInstance(notNull(), myStartRequestCaptor.capture());
		assertEquals(ImportSnomedCtJobAppCtx.JOB_ID_IMPORT_TERM_SNOMED_CT, myStartRequestCaptor.getValue().getJobDefinitionId());

		ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		assertThat(response.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Upload SNOMED CT Job has been created and is in BUILDING state with ID[my-instance-id]",
			"to the job using the http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.attach-file operation",
			"and then start the job using the http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.start-job operation."
		);
		assertEquals("my-instance-id", response.getParameter(TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID).getValue().toString());
	}

	@Test
	void testUploadTerminologyCreateJob_NoCodeSystem() {
		// Test
		assertThatThrownBy(() ->
			myServerExtension
				.getFhirClient()
				.operation()
				.onType("CodeSystem")
				.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
				.withNoParameters(Parameters.class)
				.execute()
		).isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Missing required parameter: system");
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		&makeCurrent=false  , true
		&makeCurrent=true   , false
		                    , false
		""")
	void testUploadTerminologyCreateJob_MakeCurrent(String theMakeCurrent, boolean theExpectDontMakeCurrent) throws IOException {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId("my-instance-id");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB +
			"?system=" + UrlUtil.escapeUrlParam("http://loinc.org|1.2.3") + getIfNull(theMakeCurrent, "");
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
		}

		// Verify
		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		assertEquals(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC, startRequest.getJobDefinitionId());
		assertEquals("1.2.3", startRequest.getParameters(ImportTerminologyJobParameters.class).getVersionId());
		if (theExpectDontMakeCurrent) {
			assertTrue(startRequest.getParameters(ImportTerminologyJobParameters.class).getDontMakeCurrent());
		} else {
			assertNull(startRequest.getParameters(ImportTerminologyJobParameters.class).getDontMakeCurrent());
		}
	}

	@Test
	void testUploadTerminologyAttachFile() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);
		when(myJobPersistence.storeNewAttachment(any(), any())).thenAnswer(i -> {
			AttachmentDetails attachment = i.getArgument(1, AttachmentDetails.class);
			if (attachment == null) {
				return "no-attachment";
			}
			byte[] bytes = IOUtils.toByteArray(attachment.getInputStream());
			ourLog.info("Attachment received with length: {}", bytes.length);
			return "my-attachment-id-" + bytes.length + "-bytes";
		});

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(leftPad("", 12_345), ContentType.TEXT_PLAIN));

		Parameters responseParameters;
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			InputStream contentInputStream = response.getEntity().getContent();
			Reader contentReader = new InputStreamReader(contentInputStream, StandardCharsets.UTF_8);
			responseParameters = myContext.newJsonParser().parseResource(Parameters.class, contentReader);
			ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(responseParameters));
		}

		// Verify
		verify(myJobPersistence, times(1)).storeNewAttachment(eq("my-instance-id"), myAttachmentDetailsCaptor.capture());
		assertEquals(TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE, myAttachmentDetailsCaptor.getValue().getFilename());
		assertEquals(AttachmentContentTypeEnum.PROPERTIES, myAttachmentDetailsCaptor.getValue().getContentType());

		assertEquals("my-attachment-id-12345-bytes", responseParameters.getParameter(PARAM_JOB_ATTACHMENT_ID).getValue().toString());
		assertThat(responseParameters.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Attachment with ID[my-attachment-id-12345-bytes] has been stored for job with ID[my-instance-id]"
		);
	}

	@Test
	void testUploadTerminologyAttachFile_JobInWrongStatus() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.QUEUED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(leftPad("", 12_345), ContentType.TEXT_PLAIN));

		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("Job is not in BUILDING status: QUEUED");
		}

	}

	@Test
	void testUploadTerminologyAttachFile_JobOfWrongType() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId("AA" + ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(leftPad("", 12_345), ContentType.TEXT_PLAIN));

		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("Can't attach files to this job");
		}

	}

	@Test
	void testUploadTerminologyAttachFile_UnknownFilename() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=foo.txt";
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(leftPad("", 12_345), ContentType.TEXT_PLAIN));

		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("File named \\\"foo.txt\\\" is not valid for import LOINC job");
		}

	}

	@Test
	void testUploadTerminologyStartJob() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(Constants.STATUS_HTTP_202_ACCEPTED, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("$hapi.fhir.upload-terminology.start-job job has been accepted. Poll for status at the following URL: http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.poll-for-status?jobInstanceId=my-instance-id");

			String contentLocation = response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue();
			assertEquals("http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.poll-for-status?jobInstanceId=my-instance-id", contentLocation);
		}

	}

	@Test
	void testUploadTerminologyStartJob_NoRespondAsync() throws IOException {
		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpPost post = new HttpPost(url);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("Must request async processing for $hapi.fhir.upload-terminology.start-job");
		}

	}

	@Test
	void testUploadTerminologyStartJob_WrongStatus() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.QUEUED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("Job is not in BUILDING status: QUEUED");
		}

	}

	@Test
	void testUploadTerminologyStartJob_WrongJobType() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId("AAA" + ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("Can't start job of this type");
		}

	}

	@Test
	void testUploadTerminologyPollForStatus() throws IOException {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.COMPLETED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		jobInstance.setReport(toUploadTerminologyReport("This is the report contents"));
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String url = myServerExtension.getBaseUrl() + "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpPost post = new HttpPost(url);
		try (CloseableHttpResponse response = myHttpClient.execute(post)) {

			// Verify
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString).contains("\"diagnostics\": \"This is the report contents\"");
		}

	}

	private String toUploadTerminologyReport(String theReportContents) {
		ImportTerminologyResultJson retVal = new ImportTerminologyResultJson();
		retVal.setReport(theReportContents);
		return JsonUtil.serialize(retVal);
	}

}
