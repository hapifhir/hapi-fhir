package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.TermTestUtil;
import ca.uhn.fhir.jpa.term.ZipCollectionBuilder;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.RandomUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep1ExpandDistributionIntoFilesStepTest {

	public static final String MY_INSTANCE_ID = "my-instance-id";
	@InjectMocks
	private final ImportLoincStep1ExpandDistributionIntoFilesStep myStep = new ImportLoincStep1ExpandDistributionIntoFilesStep();
	@Mock
	private IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Mock
	private IFhirResourceDao<ValueSet> myValueSetDao;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private StepExecutionDetails<ImportLoincJobParameters, VoidModel> myStepExecutionDetails;
	@Captor
	private ArgumentCaptor<CodeSystem> myCodeSystemCaptor;
	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private IJobDataSink<TerminologyFileSetJson> myDataSink;
	@Mock
	private IJobStepWorker<ImportLoincJobParameters, VoidModel, TerminologyFileSetJson> myHandlerStep0;
	@Mock
	private ITerminologyImportFileHandlerStep<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> myHandlerStep1;
	@Mock
	private ITerminologyImportFileHandlerStep<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> myHandlerStep2;
	@Mock
	private ITerminologyImportFileHandlerStep<ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> myHandlerStep3;
	@Mock
	private IJobStepWorker<ImportLoincJobParameters, TerminologyFileSetJson, VoidModel> myHandlerStep4;
	@Captor
	private ArgumentCaptor<String> myStepIdCaptor;
	@Captor
	private ArgumentCaptor<TerminologyFileSetJson> myTerminologyFileSetCaptor;
	@Captor
	private ArgumentCaptor<AttachmentDetails> myAttachmentDetailsCaptor;

	@BeforeEach
	void setUp() {
		myStep.setChunkLineSizeForUnitTest(5);
	}

	@Test
	void testProcess_HappyPath() {
		mockCodeSystemStorageStartStaging();
		mockJobPersistenceFetchDistributionFile();
		mockHandlerStep1();
		mockHandlerStep2();
		mockHandlerStep3();
		mockJobStepExecutionServices();
		when(myDaoRegistry.getResourceDao(eq("CodeSystem"))).thenReturn(myCodeSystemDao);
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);
		AtomicInteger attachmentCounter = mockJobPersistenceStoreNewAttachment();

		// Test
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		myStep.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(8)).acceptForFutureStep(myStepIdCaptor.capture(), myTerminologyFileSetCaptor.capture());
		List<String> emittedChunks = renderEmittedChunks();
		assertThat(emittedChunks).containsExactly(
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-1]",
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-2]",
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-3]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv | ATT-4]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv | ATT-5]",
			"step-3 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/AnswerFile/AnswerList.csv | ATT-6]",
			"step-3 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/AnswerFile/AnswerList.csv | ATT-7]",
			"step-3 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/AnswerFile/AnswerList.csv | ATT-8]");

		assertEquals(8, attachmentCounter.get());

		verify(myJobPersistence, times(8)).storeNewAttachment(any(), myAttachmentDetailsCaptor.capture());
		assertEquals("Loinc.csv_0-4", myAttachmentDetailsCaptor.getAllValues().get(0).getFilename());
		assertEquals("Loinc.csv_5-9", myAttachmentDetailsCaptor.getAllValues().get(1).getFilename());
	}

	@Test
	void testProcess_NoIdSpecified() {
		// Setup
		when(myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(any(), any())).thenReturn(new ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse("a-b-c-d"));
		when(myStepExecutionDetails.newSystemRequestDetails()).thenReturn(new SystemRequestDetails());
		when(myDaoRegistry.getResourceDao(eq("CodeSystem"))).thenReturn(myCodeSystemDao);
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://loinc.org");
		TerminologyFileSetJson fileSet = new TerminologyFileSetJson();
		ImportLoincJobParameters jobParameters = new ImportLoincJobParameters();
		jobParameters.setVersionId("1.23");
		myStep.handleSynchronous(myStepExecutionDetails, theDataSink, "loinc.xml", toBytes(cs), jobParameters, fileSet);

		// Verify
		assertNotNull(fileSet.getLoincCodeSystem());
		assertEquals("loinc-1.23", fileSet.getLoincCodeSystem().getIdElement().getIdPart());
		assertEquals("1.23", fileSet.getLoincCodeSystem().getVersion());
		assertEquals("EXTERNAL_COPYRIGHT_NOTICE", fileSet.getLoincCodeSystem().getProperty().get(0).getCode());
		assertEquals(CodeSystem.PropertyType.STRING, fileSet.getLoincCodeSystem().getProperty().get(0).getType());

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), nullable(RequestDetails.class));
		assertSame(fileSet.getLoincCodeSystem(), myCodeSystemCaptor.getValue());
	}

	@Test
	void testProcess_IdAndVersionSpecified() {
		// Setup
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://loinc.org");
		cs.setId("loinc");

		ImportLoincJobParameters jobParameters = new ImportLoincJobParameters();
		jobParameters.setVersionId("1.234");

		when(myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(any(), any())).thenReturn(new ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse("a-b-c-d"));
		when(myStepExecutionDetails.newSystemRequestDetails()).thenReturn(new SystemRequestDetails());
		when(myDaoRegistry.getResourceDao(eq("CodeSystem"))).thenReturn(myCodeSystemDao);
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		TerminologyFileSetJson fileSet = new TerminologyFileSetJson();
		myStep.handleSynchronous(myStepExecutionDetails, theDataSink, "loinc.xml", toBytes(cs), jobParameters, fileSet);

		// Verify
		assertNotNull(fileSet.getLoincCodeSystem());
		assertEquals("loinc-1.234", fileSet.getLoincCodeSystem().getIdElement().getIdPart());
		assertEquals("1.234", fileSet.getLoincCodeSystem().getVersion());
		assertEquals("EXTERNAL_COPYRIGHT_NOTICE", fileSet.getLoincCodeSystem().getProperty().get(0).getCode());
		assertEquals(CodeSystem.PropertyType.STRING, fileSet.getLoincCodeSystem().getProperty().get(0).getType());

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), nullable(RequestDetails.class));
		assertSame(fileSet.getLoincCodeSystem(), myCodeSystemCaptor.getValue());

		verify(myTermCodeSystemStorageSvc, times(1)).startStagingCodeSystemVersion(eq("http://loinc.org"), eq("1.234"));
	}

	private void mockJobStepExecutionServices() {
		when(myJobStepExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());
	}

	@Test
	void testProcess_TwoStepsUseSameFile() {
		mockCodeSystemStorageStartStaging();
		mockJobPersistenceFetchDistributionFile();
		mockHandlerStep1();
		mockHandlerStep2_SameFileAsStep1();
		AtomicInteger attachmentCounter = mockJobPersistenceStoreNewAttachment();
		mockJobStepExecutionServices();
		when(myDaoRegistry.getResourceDao(eq("CodeSystem"))).thenReturn(myCodeSystemDao);
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		myStep.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(6)).acceptForFutureStep(myStepIdCaptor.capture(), myTerminologyFileSetCaptor.capture());
		List<String> emittedChunks = renderEmittedChunks();
		assertThat(emittedChunks).containsExactly(
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-1]",
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-2]",
			"step-1 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-3]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-1]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-2]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/LoincTable/Loinc.csv | ATT-3]"
		);

	}

	@Test
	void testProcess_NoChunksForStep1() {
		mockCodeSystemStorageStartStaging();
		mockJobPersistenceFetchDistributionFile();
		when(myHandlerStep1.canHandleFile(any(), any(), any())).thenAnswer(t -> Optional.empty());
		mockHandlerStep2();
		mockJobPersistenceStoreNewAttachment();
		mockJobStepExecutionServices();
		when(myDaoRegistry.getResourceDao(eq("CodeSystem"))).thenReturn(myCodeSystemDao);
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		myStep.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(2)).acceptForFutureStep(myStepIdCaptor.capture(), myTerminologyFileSetCaptor.capture());
		List<String> emittedChunks = renderEmittedChunks();
		assertThat(emittedChunks).containsExactly(
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv | ATT-1]",
			"step-2 -> Chunk[SnomedCT_Release_INT_20160131_Full/Terminology/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv | ATT-2]"
		);

	}

	@Test
	void testProcess_UnreadableDistributionFile() {
		// Setup
		when(myJobPersistence.fetchAttachmentByFilename(eq(MY_INSTANCE_ID), eq(FILENAME_LOINC_DISTRIBUTION_FILE))).thenAnswer(t -> {
			byte[] bytes = RandomUtils.secure().randomBytes(1000);
			return new AttachmentDetails(new ByteArrayInputStream(bytes), AttachmentContentTypeEnum.ZIP, FILENAME_LOINC_DISTRIBUTION_FILE);
		});

		// Test
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		assertThatThrownBy(() -> myStep.run(stepExecutionDetails, myDataSink))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("Files to expand LOINC zip file: Cannot find zip signature within the file");
	}

	private void mockCodeSystemStorageStartStaging() {
		when(myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(any(), any())).thenReturn(new ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse("my-staging-version"));
	}

	private void mockHandlerStep1() {
		when(myHandlerStep1.canHandleFile(any(), any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(2, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS));
			}
			return Optional.empty();
		});
	}

	private void mockHandlerStep2() {
		when(myHandlerStep2.canHandleFile(any(), any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(2, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS));
			}
			return Optional.empty();
		});
	}

	private void mockHandlerStep3() {
		when(myHandlerStep3.canHandleFile(any(), any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(2, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS));
			}
			return Optional.empty();
		});
	}

	private void mockHandlerStep2_SameFileAsStep1() {
		when(myHandlerStep2.canHandleFile(any(), any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(2, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS));
			}
			return Optional.empty();
		});
	}

	private void mockJobPersistenceFetchDistributionFile() {
		Consumer<ZipCollectionBuilder> populator = files -> {
			try {
				TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
		// Load LOINC marked as version 2.67

		mockJobPersistenceFetchDistributionFile(populator);
	}

	private void mockJobPersistenceFetchDistributionFile(Consumer<ZipCollectionBuilder> populator) {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		populator.accept(files);

		when(myJobPersistence.fetchAttachmentByFilename(eq(MY_INSTANCE_ID), eq(FILENAME_LOINC_DISTRIBUTION_FILE))).thenAnswer(t -> {
			String fileName = t.getArgument(1, String.class);
			assertEquals(FILENAME_LOINC_DISTRIBUTION_FILE, fileName);
			byte[] bytes = files.getZipBytes();
			return new AttachmentDetails(new ByteArrayInputStream(bytes), AttachmentContentTypeEnum.ZIP, FILENAME_LOINC_DISTRIBUTION_FILE);
		});
	}

	private AtomicInteger mockJobPersistenceStoreNewAttachment() {
		AtomicInteger counter = new AtomicInteger(0);
		when(myJobPersistence.storeNewAttachment(any(), any())).thenAnswer(t -> "ATT-" + counter.incrementAndGet());
		return counter;
	}

	@Nonnull
	private StepExecutionDetails<ImportLoincJobParameters, VoidModel> newStepExecutionDetails() {
		JobDefinition<ImportLoincJobParameters> jobDefinition = JobDefinition.newBuilder()
			.setJobDefinitionId("job")
			.setJobDefinitionVersion(1)
			.setJobDescription("a job")
			.setParametersType(ImportLoincJobParameters.class)
			.addFirstStep("step-0", "step-0", TerminologyFileSetJson.class, myHandlerStep0)
			.addIntermediateStep("step-1", "step-1", TerminologyFileSetJson.class, myHandlerStep1)
			.addIntermediateStep("step-2", "step-2", TerminologyFileSetJson.class, myHandlerStep2)
			.addIntermediateStep("step-3", "step-3", TerminologyFileSetJson.class, myHandlerStep3)
			.addLastStep("step-4", "step-4", myHandlerStep4)
			.build();

		ImportLoincJobParameters jobParameters = new ImportLoincJobParameters();
		jobParameters.setVersionId("1.23");
		JobInstance instance = new JobInstance();
		instance.setInstanceId(MY_INSTANCE_ID);
		return new StepExecutionDetails<>(jobParameters, null, instance, new WorkChunk(), myJobStepExecutionServices, jobDefinition, "step-0", "step-1");
	}

	private byte[] toBytes(CodeSystem theCs) {
		return FhirContext.forR4Cached().newXmlParser().encodeResourceToString(theCs).getBytes(StandardCharsets.UTF_8);
	}

	@Nonnull
	private List<String> renderEmittedChunks() {
		return BaseImportLoincStepTest.renderEmittedChunks(myStepIdCaptor, myTerminologyFileSetCaptor);
	}


}
