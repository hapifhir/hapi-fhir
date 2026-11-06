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
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AbstractInputStream;
import org.apache.http.entity.InputStreamEntity;
import org.hl7.fhir.r5.model.CodeType;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_UPLOAD_TERMINOLOGY_START_JOB;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.LOINC_MAX_SIZE;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.LOINC_PROPERTIES_MAX_SIZE;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_JOB_ATTACHMENT_ID;
import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.RESP_PARAM_OUTCOME;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.LOINC_URI;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.SCT_URI;
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
import static org.mockito.Mockito.doAnswer;
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
	private IJobCoordinator myJobCoordinator;

	@Mock
	private IJobPersistence myJobPersistence;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@RegisterExtension
	private final RestfulServerExtension myServerExtension = new RestfulServerExtension(myContext)
		.withServer(t -> {
			assert myContext != null;
			assert myJobCoordinator != null;
			t.registerProvider(new TerminologyUploaderProvider(myContext, myJobCoordinator, myJobPersistence));
		});

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

	/**
	 * Make sure we throw a useful error if the user tries to use the old
	 * method.
	 */
	@Test
	void testApplyCodeSystemDeltaAdd() {
		// Test
		assertThatThrownBy(() ->
			myServerExtension
				.getFhirClient()
				.operation()
				.onType("CodeSystem")
				.named(OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withNoParameters(Parameters.class)
				.execute()
		).isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("The $apply-codesystem-delta-add operation has been removed. To upload terminology, see the $hapi.fhir.upload-terminology.create-job operation.");
	}

	/**
	 * Make sure we throw a useful error if the user tries to use the old
	 * method.
	 */
	@Test
	void testApplyCodeSystemDeltaRemove() {
		// Test
		assertThatThrownBy(() ->
			myServerExtension
				.getFhirClient()
				.operation()
				.onType("CodeSystem")
				.named(OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE)
				.withNoParameters(Parameters.class)
				.execute()
		).isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("The $apply-codesystem-delta-remove operation has been removed. To upload terminology, see the $hapi.fhir.upload-terminology.create-job operation.");
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		ADD      , ADD
		REMOVE   , REMOVE
		SNAPSHOT , SNAPSHOT
		         , SNAPSHOT
		"""
	)
	void testUploadTerminologyCreateJob_DifferentModes(String theModeParameter, ImportTerminologyModeEnum theExpectedMode) {
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
			.andParameter(TerminologyUploaderProvider.PARAM_MODE, new CodeType(theModeParameter))
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

		ImportTerminologyJobParameters parameters = myStartRequestCaptor.getValue().getParameters(ImportTerminologyJobParameters.class);
		assertEquals(theExpectedMode, parameters.getMode());
	}

	@Test
	void testUploadTerminologyCreateJob_InvalidMode() {
		// Test
		assertThatThrownBy(()->myServerExtension
			.getFhirClient()
			.operation()
			.onType("CodeSystem")
			.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo"))
			.andParameter(TerminologyUploaderProvider.PARAM_VERSION, new StringType("1.2"))
			.andParameter(TerminologyUploaderProvider.PARAM_MODE, new CodeType("FOO"))
			.execute())
			// Verify
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("Invalid value for parameter mode: FOO");
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		http://loinc.org|1.0         ,   ADD
		http://loinc.org|1.0         ,   REMOVE
		http://snomed.info/sct|1.0   ,   ADD
		http://snomed.info/sct|1.0   ,   REMOVE
		"""
	)
	void testUploadTerminologyCreateJob_BlockDeltasForStandardTerminology(String theUrl, String theMode) {
		assertThatThrownBy(() -> myServerExtension
			.getFhirClient()
			.operation()
			.onType("CodeSystem")
			.named(OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(theUrl))
			.andParameter(TerminologyUploaderProvider.PARAM_MODE, new CodeType(theMode))
			.execute())
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Delta operations are not supported for terminology:");
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
	void testUploadTerminologyCreateJob_MakeCurrent(String theMakeCurrent, boolean theExpectDontMakeCurrent) {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId("my-instance-id");
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_CREATE_JOB +
			"?system=" + UrlUtil.escapeUrlParam("http://loinc.org|1.2.3") + getIfNull(theMakeCurrent, "");
		myServerExtension.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.method("POST")
			.assertStatus(200);

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
	void testUploadTerminologyAttachFile() {
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
			assertEquals(12_345, bytes.length);
			return "my-attachment-id-" + bytes.length + "-bytes";
		});

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		String responseBody = myServerExtension.fhirRequest(path).post(leftPad("", 12_345), Constants.CT_TEXT)
			.assertStatus(200)
			.getBody();
		Parameters responseParameters = myContext.newJsonParser().parseResource(Parameters.class, responseBody);
		ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(responseParameters));

		// Verify
		verify(myJobPersistence, times(1)).storeNewAttachment(eq("my-instance-id"), myAttachmentDetailsCaptor.capture());
		assertEquals(TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE, myAttachmentDetailsCaptor.getValue().getFilename());
		assertEquals(AttachmentContentTypeEnum.PROPERTIES, myAttachmentDetailsCaptor.getValue().getContentType());
		assertEquals(LOINC_PROPERTIES_MAX_SIZE, myAttachmentDetailsCaptor.getValue().getMaximumSize().orElseThrow().intValue());

		assertEquals("my-attachment-id-12345-bytes", responseParameters.getParameter(PARAM_JOB_ATTACHMENT_ID).getValue().toString());
		assertThat(responseParameters.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Attachment with ID[my-attachment-id-12345-bytes] has been stored for job with ID[my-instance-id]"
		);
	}

	@Test
	void testUploadTerminologyAttachFile_AppendToExistingAttachment() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);
		when(myJobPersistence.fetchAttachmentById(any(), any())).thenReturn(AttachmentDetails
			.newBuilder()
				.withNoMaximumSize()
				.withBytes(new byte[0])
				.withContentType(AttachmentContentTypeEnum.ZIP)
				.withFilename(FILENAME_LOINC_DISTRIBUTION_FILE)
			.build());
		doAnswer(i -> {
			assertEquals("my-instance-id", i.getArgument(0));
			assertEquals("my-attachment-id", i.getArgument(1));

			AttachmentDetails attachment = i.getArgument(2, AttachmentDetails.class);
			if (attachment == null) {
				return "no-attachment";
			}
			byte[] bytes = IOUtils.toByteArray(attachment.getInputStream());
			ourLog.info("Attachment received with length: {}", bytes.length);
			assertEquals(12_345, bytes.length);
			return null;
		}).when(myJobPersistence).appendToAttachment(any(), any(), any());

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_APPEND_TO_JOB_ATTACHMENT_ID + "=" + "my-attachment-id";
		String responseBody = myServerExtension.fhirRequest(path).post(leftPad("", 12_345), Constants.CT_TEXT)
			.assertStatus(200)
			.getBody();
		Parameters responseParameters = myContext.newJsonParser().parseResource(Parameters.class, responseBody);
		ourLog.info("Response: {}", myContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(responseParameters));

		// Verify
		verify(myJobPersistence, times(1)).appendToAttachment(eq("my-instance-id"), eq("my-attachment-id"), myAttachmentDetailsCaptor.capture());
		assertEquals(LOINC_MAX_SIZE, myAttachmentDetailsCaptor.getValue().getMaximumSize().orElseThrow().intValue());

		assertThat(responseParameters.getParameter(RESP_PARAM_OUTCOME).getValue().toString()).contains(
			"Successfully appended to attachment"
		);
	}

	@Test
	void testUploadTerminologyAttachFile_JobInWrongStatus() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.QUEUED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		myServerExtension.fhirRequest(path).post(leftPad("", 12_345), Constants.CT_TEXT)
			.assertStatus(400)
			.assertBodyContains("Job is not in BUILDING status: QUEUED");

	}

	@Test
	void testUploadTerminologyAttachFile_JobOfWrongType() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId("AA" + ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=" + TerminologyConstants.FILENAME_LOINC_UPLOAD_PROPERTIES_FILE;
		myServerExtension.fhirRequest(path).post(leftPad("", 12_345), Constants.CT_TEXT)
			.assertStatus(400)
			.assertBodyContains("Can't attach files to this job");

	}

	@Test
	void testUploadTerminologyAttachFile_UnknownFilename() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id" +
			"&" + TerminologyUploaderProvider.PARAM_FILENAME + "=foo.txt";
		myServerExtension.fhirRequest(path).post(leftPad("", 12_345), Constants.CT_TEXT)
			.assertStatus(400)
			.assertBodyContains("File named \\\"foo.txt\\\" is not valid for import LOINC job");

	}

	@ParameterizedTest
	@ValueSource(strings = {
		OPERATION_UPLOAD_TERMINOLOGY_ATTACH_FILE,
		OPERATION_UPLOAD_TERMINOLOGY_START_JOB,
		OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS
	})
	void testUploadTerminology_NoJobInstanceParamValue(String theOperationName) {
		// Test
		String path = "/CodeSystem/" + theOperationName +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=";
		HttpTestRequest request = myServerExtension.fhirRequest(path);
		if (theOperationName.equals(OPERATION_UPLOAD_TERMINOLOGY_START_JOB)) {
			request.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		}
		request.post(new Parameters())
			.assertStatus(400)
			.assertBodyContains("No value provided for mandatory parameter: jobInstanceId");

	}

	@Test
	void testUploadTerminologyStartJob() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		HttpTestResponse response = myServerExtension.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.method("POST")
			.assertStatus(202);

		// Verify
		response.assertBodyContains("$hapi.fhir.upload-terminology.start-job job has been accepted. Poll for status at the following URL: http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.poll-for-status?jobInstanceId=my-instance-id");

		String contentLocation = response.getHeader(Constants.HEADER_CONTENT_LOCATION);
		assertThat(contentLocation).isEqualTo("http://localhost:" + myServerExtension.getPort() + "/CodeSystem/$hapi.fhir.upload-terminology.poll-for-status?jobInstanceId=my-instance-id");

	}

	@Test
	void testUploadTerminologyStartJob_NoRespondAsync() {
		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		myServerExtension.fhirRequest(path).method("POST")
			.assertStatus(400)
			.assertBodyContains("Must request async processing for $hapi.fhir.upload-terminology.start-job");

	}

	@Test
	void testUploadTerminologyStartJob_WrongStatus() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.QUEUED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		myServerExtension.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.method("POST")
			.assertStatus(400)
			.assertBodyContains("Job is not in BUILDING status: QUEUED");

	}

	@Test
	void testUploadTerminologyStartJob_WrongJobType() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId("AAA" + ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_START_JOB +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		myServerExtension.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.method("POST")
			.assertStatus(400)
			.assertBodyContains("Can't start job of this type");

	}

	@Test
	void testUploadTerminologyPollForStatus() {
		// Setup
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId("my-instance-id");
		jobInstance.setStatus(StatusEnum.COMPLETED);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);
		jobInstance.setReport(toUploadTerminologyReport("This is the report contents"));
		when(myJobCoordinator.getInstance(eq("my-instance-id"))).thenReturn(jobInstance);

		// Test
		String path = "/CodeSystem/" + OPERATION_UPLOAD_TERMINOLOGY_POLL_FOR_STATUS +
			"?" + TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID + "=my-instance-id";
		myServerExtension.fhirRequest(path).method("POST")
			.assertStatus(200)
			.assertBodyContains("\"diagnostics\": \"This is the report contents\"");

	}

	private String toUploadTerminologyReport(String theReportContents) {
		ImportTerminologyResultJson retVal = new ImportTerminologyResultJson();
		retVal.setReport(theReportContents);
		return JsonUtil.serialize(retVal);
	}

}
