package ca.uhn.fhir.cli;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.BaseRestServerHelper;
import ca.uhn.fhir.test.utilities.RestServerDstu3Helper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.FhirValidator;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_CONCEPTS_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_HIERARCHY_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.ICD10CM_URI;
import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.LOINC_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadTerminologyCommandTest extends ConsoleOutputCapturingBaseTest{
	private static final Logger ourLog = LoggerFactory.getLogger(UploadTerminologyCommandTest.class);

	private static final String FHIR_VERSION_DSTU3 = "DSTU3";
	private static final String FHIR_VERSION_R4 = "R4";
	private static final String MY_INSTANCE_ID = "my-instance-id";
	private static final String MY_ATTACHMENT_ID = "my-attachment-id";
	private FhirContext myCtx;
	private final String myConceptsFileName = "target/concepts.csv";
	private final File myConceptsFile = new File(myConceptsFileName);
	private final String myHierarchyFileName = "target/hierarchy.csv";
	private final File myHierarchyFile = new File(myHierarchyFileName);
	private final String myCodeSystemFileName = "target/codesystem.json";
	private final File myCodeSystemFile = new File(myCodeSystemFileName);
	private final String myTextFileName = "target/hello.txt";
	private final File myTextFile = new File(myTextFileName);
	private final File myPropertiesFile = new File(myTextFileName);
	private File myArchiveFile;
	private String myArchiveFileName;
	private final String myICD10FileName = new File("src/test/resources").getAbsolutePath() + "/icd10cm_tabular_2021.xml";

	@Mock
	private IJobCoordinator myJobCoordinator;
	@Mock
	private IJobPersistence myJobPersistence;

	static {
		HapiSystemProperties.enableTestMode();
	}

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestDetails;
	@Captor
	private ArgumentCaptor<AttachmentDetails> myAttachmentDetails;


	static Stream<Arguments> paramsProvider(){
		return Stream.of(
			// [0] theFhirVersion, [1] theIncludeTls
			Arguments.arguments(FHIR_VERSION_DSTU3, true),
			Arguments.arguments(FHIR_VERSION_DSTU3, false),
			Arguments.arguments(FHIR_VERSION_R4, true),
			Arguments.arguments(FHIR_VERSION_R4, false)
		);
	}

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = RestServerR4Helper.newInitialized();
	@RegisterExtension
	public final RestServerDstu3Helper myRestServerDstu3Helper = RestServerDstu3Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	private BaseRestServerHelper myBaseRestServerHelper;

	@BeforeEach
	public void beforeEach(TestInfo testInfo) throws Exception {
		writeConceptAndHierarchyFiles();
		if (testInfo.getDisplayName().contains(FHIR_VERSION_DSTU3)) {
			myCtx = FhirContext.forDstu3();
			myRestServerDstu3Helper.registerProvider(new TerminologyUploaderProvider(myCtx, myJobCoordinator, myJobPersistence));
			myBaseRestServerHelper = myRestServerDstu3Helper;
		} else if (testInfo.getDisplayName().contains(FHIR_VERSION_R4) || testInfo.getDisplayName().endsWith("()") || testInfo.getDisplayName().endsWith("(File)")) {
			myCtx = FhirContext.forR4();
			myRestServerR4Helper.registerProvider(new TerminologyUploaderProvider(myCtx, myJobCoordinator, myJobPersistence));
			myBaseRestServerHelper = myRestServerR4Helper;
		}
	}

	@AfterEach
	public void afterEach() {
		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
		FileUtils.deleteQuietly(myCodeSystemFile);
		FileUtils.deleteQuietly(myTextFile);
		FileUtils.deleteQuietly(myArchiveFile);
		FileUtils.deleteQuietly(myPropertiesFile);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAdd(String theFhirVersion, boolean theIncludeTls) {
		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		JobInstanceStartRequest startRequest = myStartRequestDetails.getValue();
		ImportTerminologyJobParameters jobParameters = startRequest.getParameters(ImportTerminologyJobParameters.class);
		assertEquals(ImportTerminologyModeEnum.ADD, jobParameters.getMode());

		verify(myJobPersistence, times(2)).storeNewAttachment(any(), any());
		verify(myJobCoordinator, times(1)).enqueueBuildingJobForExecution(any());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddUsingCodeSystemResource(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.dstu3.model.CodeSystem cs = new org.hl7.fhir.dstu3.model.CodeSystem();
				cs.addConcept().setCode("CODE").setDisplay("Display");
				myCtx.newJsonParser().encodeResourceToWriter(cs, w);
			}
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.r4.model.CodeSystem cs = new org.hl7.fhir.r4.model.CodeSystem();
				cs.addConcept().setCode("CODE").setDisplay("Display");
				myCtx.newJsonParser().encodeResourceToWriter(cs, w);
			}
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myCodeSystemFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myJobPersistence, times(1)).storeNewAttachment(any(), any());
		verify(myJobCoordinator, times(1)).enqueueBuildingJobForExecution(any());
	}

	@ParameterizedTest
	@MethodSource("testModifyingSizeLimitConvertsCorrectlyR4Params")
	public void testModifyingSizeLimitConvertsCorrectlyR4(String theInput, long theExpectedBytes) {

		UploadTerminologyCommand uploadTerminologyCommand = new UploadTerminologyCommand();
		uploadTerminologyCommand.setTransferSizeLimitHuman(theInput);
		long bytes = uploadTerminologyCommand.getTransferSizeLimit();
		assertEquals(theExpectedBytes, bytes);

	}

	static Object[] testModifyingSizeLimitConvertsCorrectlyR4Params() {
		return new Object[] {
			new Object[] { "1GB", 1024L * 1024L * 1024L },
			new Object[] { "1 GB", 1024L * 1024L * 1024L },
			new Object[] { "1 gb", 1024L * 1024L * 1024L },
			new Object[] { "500KB", 1024L * 500L },
			new Object[] { "500kB", 1024L * 500L },
			new Object[] { "500 kB", 1024L * 500L },
			new Object[] { "10MB", 1024L * 1024L * 10L }
		};
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddUsingCompressedFile(String theFhirVersion, boolean theIncludeTls) throws IOException {
		writeArchiveFile(myConceptsFile, myHierarchyFile);

		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myArchiveFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myJobPersistence, times(1)).storeNewAttachment(any(), any());
		verify(myJobCoordinator, times(1)).enqueueBuildingJobForExecution(any());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddInvalidFileName(String theFhirVersion, boolean theIncludeTls) {
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
					"-v", theFhirVersion,
					"-m", "ADD",
					"-u", "http://foo",
					"-d", myConceptsFileName + "/foo.csv",
					"-d", myHierarchyFileName
				},
				"-t", theIncludeTls, myBaseRestServerHelper
			));
			fail();
		} catch (Error e) {
			assertThat(e.toString().replace('\\', '/')).contains("File does not exist or can't be read: target/concepts.csv/foo.csv");
		}
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaRemove(String theFhirVersion, boolean theIncludeTls) {
		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "REMOVE",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		JobInstanceStartRequest startRequest = myStartRequestDetails.getValue();
		ImportTerminologyJobParameters jobParameters = startRequest.getParameters(ImportTerminologyJobParameters.class);
		assertEquals(ImportTerminologyModeEnum.REMOVE, jobParameters.getMode());

		verify(myJobPersistence, times(2)).storeNewAttachment(any(), any());
		verify(myJobCoordinator, times(1)).enqueueBuildingJobForExecution(any());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSnapshot(String theFhirVersion, boolean theIncludeTls) {
		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		// Verify
		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		assertEquals(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY, myStartRequestDetails.getValue().getJobDefinitionId());

		verify(myJobPersistence, times(2)).storeNewAttachment(any(), myAttachmentDetails.capture());
		assertEquals(CUSTOM_CONCEPTS_FILE, myAttachmentDetails.getAllValues().get(0).getFilename());
		assertEquals(CUSTOM_HIERARCHY_FILE, myAttachmentDetails.getAllValues().get(1).getFilename());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSnapshotLargeFile(String theFhirVersion, boolean theIncludeTls) {
		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName,
				"-s", "10MB"
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		// Verify
		verify(myJobPersistence, times(2)).storeNewAttachment(eq(MY_INSTANCE_ID), any());
		verifyNoMoreInteractions(myJobPersistence);

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		assertEquals(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY, myStartRequestDetails.getValue().getJobDefinitionId());
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSnapshotLargeFile_SplitIntoChunks(String theFhirVersion, boolean theIncludeTls) throws IOException {
		// Setup
		ByteArrayOutputStream receivedDataBuffer = new ByteArrayOutputStream();
		mockJobCoordinatorForStartingJob(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY, receivedDataBuffer);

		when(myJobPersistence.fetchAttachmentById(eq(MY_INSTANCE_ID), eq(MY_ATTACHMENT_ID))).thenReturn(AttachmentDetails
			.newBuilder()
				.withFilename(myConceptsFileName)
				.withContentType(AttachmentContentTypeEnum.CSV)
				.withNoMaximumSize()
				.withBytes(new byte[0])
				.build());

		doAnswer(t->{
			AttachmentDetails details = t.getArgument(2, AttachmentDetails.class);
			byte[] bytes = IOUtils.toByteArray(details.getInputStream());
			ourLog.info("Appending attachment request received {} bytes", bytes.length);
			receivedDataBuffer.write(bytes);
			return MY_ATTACHMENT_ID;
		}).when(myJobPersistence).appendToAttachment(any(), any(), any());

		// Rewrite the concepts file as exactly 1 MB
		byte[] expectedBytes = RandomTextUtils.newSecureRandomAlphaNumericString((int) FileUtils.ONE_MB).getBytes(StandardCharsets.UTF_8);
		FileUtils.deleteQuietly(myConceptsFile);
		try (FileOutputStream fileOutputStream = new FileOutputStream(myConceptsFile)) {
			fileOutputStream.write(expectedBytes);
		}

		// Test
		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-s", "400KB"
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		// Verify
		verify(myJobPersistence, times(1)).storeNewAttachment(eq(MY_INSTANCE_ID), any());
		verify(myJobPersistence, times(2)).appendToAttachment(eq(MY_INSTANCE_ID), eq(MY_ATTACHMENT_ID), any());
		verifyNoMoreInteractions(myJobPersistence);

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		assertEquals(ImportCustomTerminologyJobAppCtx.JOB_ID_IMPORT_CUSTOM_TERMINOLOGY, myStartRequestDetails.getValue().getJobDefinitionId());

		byte[] actualBytes = receivedDataBuffer.toByteArray();
		assertArrayEquals(expectedBytes, actualBytes);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadLoinc(String theFhirVersion, boolean theIncludeTls, @TempDir File theTempDir) throws IOException {

		File tempFile = new File(theTempDir, "loinc.zip");
		tempFile.deleteOnExit();
		try (FileWriter w = new FileWriter(tempFile, StandardCharsets.UTF_8, false)) {
			w.append("12345");
		}

		JobInstance jobInstance = mockJobCoordinatorForStartingJob(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-u", LOINC_URI + "|1.23",
				"-d", tempFile.getAbsolutePath()
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		// Verify
		assertEquals(StatusEnum.COMPLETED, jobInstance.getStatus());

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		JobInstanceStartRequest startRequest = myStartRequestDetails.getValue();
		ImportTerminologyJobParameters params = startRequest.getParameters(ImportTerminologyJobParameters.class);
		assertNull(params.getDontMakeCurrent());
	}

	@Nonnull
	private JobInstance mockJobCoordinatorForStartingJob(String theJobDefinition) {
		return mockJobCoordinatorForStartingJob(theJobDefinition, null);
	}

	@Nonnull
	private JobInstance mockJobCoordinatorForStartingJob(String theJobDefinition, @Nullable ByteArrayOutputStream theReceivedDataBuffer) {
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId(MY_INSTANCE_ID);
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(theJobDefinition);

		StopWatch sw = new StopWatch();
		doAnswer(t->{
			if (jobInstance.getStatus() == StatusEnum.IN_PROGRESS) {
				if (sw.getMillis() > 250) {
					ImportTerminologyResultJson result = new ImportTerminologyResultJson();
					result.setReport("This is the report line 1\nThis is the report line 2");

					jobInstance.setStatus(StatusEnum.COMPLETED);
					jobInstance.setReport(JsonUtil.serialize(result));
				}
			}
			return jobInstance;
		}).when(myJobCoordinator).getInstance(eq(MY_INSTANCE_ID));

		doAnswer(t->{
			jobInstance.setStatus(StatusEnum.IN_PROGRESS);
			jobInstance.setProgress(0.55);
			return null;
		}).when(myJobCoordinator).enqueueBuildingJobForExecution(any());

		when(myJobPersistence.storeNewAttachment(any(), any())).thenAnswer(t->{
			AttachmentDetails details = t.getArgument(1, AttachmentDetails.class);
			byte[] bytes = IOUtils.toByteArray(details.getInputStream());
			ourLog.info("Store aggachment request received {} bytes", bytes.length);
			if (theReceivedDataBuffer != null) {
				theReceivedDataBuffer.write(bytes);
			}
			return MY_ATTACHMENT_ID;
		});

		return jobInstance;
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadLoinc_DontMakeCurrent(String theFhirVersion, boolean theIncludeTls, @TempDir File theTempDir) throws IOException {

		File tempFile = new File(theTempDir, "loinc.zip");
		tempFile.deleteOnExit();
		try (FileWriter w = new FileWriter(tempFile, StandardCharsets.UTF_8, false)) {
			w.append("12345");
		}

		JobInstance jobInstance = mockJobCoordinatorForStartingJob(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-u", LOINC_URI + "|1.23",
				"-d", tempFile.getAbsolutePath(),
				"--dont-make-current"
			}, "-t", theIncludeTls
			, myBaseRestServerHelper
		));

		// Verify
		assertEquals(StatusEnum.COMPLETED, jobInstance.getStatus());

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestDetails.capture());
		JobInstanceStartRequest startRequest = myStartRequestDetails.getValue();
		ImportTerminologyJobParameters params = startRequest.getParameters(ImportTerminologyJobParameters.class);
		assertTrue(params.getDontMakeCurrent());
	}

	@Test
	public void testUploadLoinc_InvalidFilename(@TempDir File theTempDir) throws IOException {

		File tempFile = new File(theTempDir, "blah.json");
		try (FileWriter w = new FileWriter(tempFile, StandardCharsets.UTF_8, false)) {
			w.append("12345");
		}

		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId(MY_INSTANCE_ID);
		jobInstance.setStatus(StatusEnum.BUILDING);
		jobInstance.setJobDefinitionId(ImportLoincJobAppCtx.JOB_ID_IMPORT_TERM_LOINC);

		StopWatch sw = new StopWatch();
		doAnswer(t->{
			if (jobInstance.getStatus() == StatusEnum.IN_PROGRESS) {
				if (sw.getMillis() > 2000) {
					ImportTerminologyResultJson result = new ImportTerminologyResultJson();
					result.setReport("This is the report line 1\nThis is the report line 2");

					jobInstance.setStatus(StatusEnum.COMPLETED);
					jobInstance.setReport(JsonUtil.serialize(result));
				}
			}
			return jobInstance;
		}).when(myJobCoordinator).getInstance(eq(MY_INSTANCE_ID));

		assertThatThrownBy(()->App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", "r4",
				"-u", LOINC_URI + "|1.23",
				"-d", tempFile.getAbsolutePath(),
				"--dont-make-current"
			}, "-t", false
			, myBaseRestServerHelper
		)))
			.isInstanceOf(CommandFailureException.class);

		// Verify
		assertThat(getConsoleOutput()).contains("HAPI-2959: Failed to attach file \"" + tempFile.getName()+ "\" to job, got HTTP 400 Bad Request: HAPI-2953: File named \"" + tempFile.getName() + "\" is not valid for import LOINC job");
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadICD10UsingCompressedFile(String theFhirVersion, boolean theIncludeTls) {
		uploadICD10UsingCompressedFile(theFhirVersion, theIncludeTls);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadTerminologyWithEndpointValidation(String theFhirVersion, boolean theIncludeTls) {
		RequestValidatingInterceptor requestValidatingInterceptor = createRequestValidatingInterceptor();
		myBaseRestServerHelper.registerInterceptor(requestValidatingInterceptor);

		uploadICD10UsingCompressedFile(theFhirVersion, theIncludeTls);
	}


	private void uploadICD10UsingCompressedFile(String theFhirVersion, boolean theIncludeTls) {
		mockJobCoordinatorForStartingJob(ImportIcdJobAppCtx.JOB_ID_IMPORT_ICD_10);

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-u", ICD10CM_URI,
				"-d", myICD10FileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

	}

	private RequestValidatingInterceptor createRequestValidatingInterceptor(){
		FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(myCtx);
		ValidationSupportChain validationSupport = new ValidationSupportChain(
			new DefaultProfileValidationSupport(myCtx),
			new InMemoryTerminologyServerValidationSupport(myCtx),
			new CommonCodeSystemsTerminologyService(myCtx)
		);

		fhirInstanceValidator.setValidationSupport(validationSupport);
		FhirValidator fhirValidator = myCtx.newValidator();
		fhirValidator.registerValidatorModule(fhirInstanceValidator);

		RequestValidatingInterceptor requestValidatingInterceptor = new RequestValidatingInterceptor();
		requestValidatingInterceptor.setValidatorModules(List.of(fhirInstanceValidator));
		return requestValidatingInterceptor;
	}

	private synchronized void writeConceptAndHierarchyFiles() throws IOException {
		if (!myConceptsFile.exists()) {
			try (FileWriter w = new FileWriter(myConceptsFile, false)) {
				w.append("CODE,DISPLAY\n");
				w.append("ANIMALS,Animals\n");
				w.append("CATS,Cats\n");
				w.append("DOGS,Dogs\n");
			}
		}
		if (!myHierarchyFile.exists()) {
			try (FileWriter w = new FileWriter(myHierarchyFile, false)) {
				w.append("PARENT,CHILD\n");
				w.append("ANIMALS,CATS\n");
				w.append("ANIMALS,DOGS\n");
			}
		}
	}

	private void writeArchiveFile(File... theFiles) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, Charsets.UTF_8);

		for (File next : theFiles) {
			ZipEntry nextEntry = new ZipEntry(UploadTerminologyCommand.stripPath(next.getAbsolutePath()));
			zipOutputStream.putNextEntry(nextEntry);
			try (FileInputStream fileInputStream = new FileInputStream(next)) {
				IOUtils.copy(fileInputStream, zipOutputStream);
			}
		}

		zipOutputStream.flush();
		zipOutputStream.close();

		myArchiveFile = File.createTempFile("temp", ".zip");
		myArchiveFile.deleteOnExit();
		myArchiveFileName = myArchiveFile.getAbsolutePath();
		try (FileOutputStream fos = new FileOutputStream(myArchiveFile, false)) {
			fos.write(byteArrayOutputStream.toByteArray());
		}
	}
}
