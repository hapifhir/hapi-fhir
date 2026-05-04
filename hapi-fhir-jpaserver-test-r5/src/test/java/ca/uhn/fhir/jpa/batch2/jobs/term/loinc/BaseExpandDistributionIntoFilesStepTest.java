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
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.TermTestUtil;
import ca.uhn.fhir.jpa.term.ZipCollectionBuilder;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.RandomUtils;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.DISTRIBUTION_FILE_ATTACHMENT_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseExpandDistributionIntoFilesStepTest {
	public static final String MY_INSTANCE_ID = "my-instance-id";

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private IJobDataSink<TerminologyFileSetJson> myDataSink;
	@Mock
	private IJobStepWorker<LoincJobImportParameters, VoidModel, TerminologyFileSetJson> myHandlerStep0;
	@Mock
	private ITerminologyImportFileHandlerStep<LoincJobImportParameters, TerminologyFileSetJson, TerminologyFileSetJson> myHandlerStep1;
	@Mock
	private ITerminologyImportFileHandlerStep<LoincJobImportParameters, TerminologyFileSetJson, TerminologyFileSetJson> myHandlerStep2;
	@Mock
	private IJobStepWorker<LoincJobImportParameters, TerminologyFileSetJson, VoidModel> myHandlerStep3;

	@InjectMocks
	private MyBaseExpandDistributionIntoFilesStep myStep = new MyBaseExpandDistributionIntoFilesStep();

	@Captor
	private ArgumentCaptor<TerminologyFileSetJson> myTerminologyFileSetCaptor;

	@BeforeEach
	void setUp() {
		myStep.setChunkLineSizeForUnitTest(5);
	}

	@Test
	void testRun_HappyPath() throws IOException {
		mockJobPersistenceFetchDistributionFile();
		mockHandlerStep1();
		mockHandlerStep2();
		mockJobPersistenceStoreNewAttachment();

		// Test
		StepExecutionDetails<LoincJobImportParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		myStep.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(5)).accept(myTerminologyFileSetCaptor.capture());

		TerminologyFileSetJson fileSet = myTerminologyFileSetCaptor.getAllValues().get(0);
		assertNull(fileSet.getChunkAttachmentIdForCurrentStepId());
		assertThat(fileSet.getAndRemoveFutureChunkAttachmentIdsForStepId("step-1")).isEmpty();
		assertThat(fileSet.getAndRemoveFutureChunkAttachmentIdsForStepId("step-2")).containsExactly("ATT-5", "ATT-6");
		assertEquals("ATT-1", myTerminologyFileSetCaptor.getAllValues().get(1).getChunkAttachmentIdForCurrentStepId());
		assertEquals("ATT-2", myTerminologyFileSetCaptor.getAllValues().get(2).getChunkAttachmentIdForCurrentStepId());
		assertEquals("ATT-3", myTerminologyFileSetCaptor.getAllValues().get(3).getChunkAttachmentIdForCurrentStepId());
		assertEquals("ATT-4", myTerminologyFileSetCaptor.getAllValues().get(4).getChunkAttachmentIdForCurrentStepId());

	}

	@Test
	void testRun_NoChunksForStep1() throws IOException {
		mockJobPersistenceFetchDistributionFile();
		when(myHandlerStep1.canHandleFile(any(), any())).thenAnswer(t -> Optional.empty());
		mockHandlerStep2();
		mockJobPersistenceStoreNewAttachment();

		// Test
		StepExecutionDetails<LoincJobImportParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		myStep.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(1)).accept(myTerminologyFileSetCaptor.capture());

		TerminologyFileSetJson fileSet = myTerminologyFileSetCaptor.getAllValues().get(0);
		assertNull(fileSet.getChunkAttachmentIdForCurrentStepId());
		assertThat(fileSet.getAndRemoveFutureChunkAttachmentIdsForStepId("step-1")).isEmpty();
		assertThat(fileSet.getAndRemoveFutureChunkAttachmentIdsForStepId("step-2")).containsExactly("ATT-1", "ATT-2");
	}

	@Test
	void testRun_UnreadableDistributionFile() {
		// Setup
		when(myJobPersistence.fetchAttachmentByFilename(eq(MY_INSTANCE_ID), eq(DISTRIBUTION_FILE_ATTACHMENT_FILENAME))).thenAnswer(t -> {
			byte[] bytes = RandomUtils.secure().randomBytes(1000);
			return new AttachmentDetails(new ByteArrayInputStream(bytes), AttachmentContentTypeEnum.ZIP, DISTRIBUTION_FILE_ATTACHMENT_FILENAME);
		});

		// Test
		StepExecutionDetails<LoincJobImportParameters, VoidModel> stepExecutionDetails = newStepExecutionDetails();
		assertThatThrownBy(() -> myStep.run(stepExecutionDetails, myDataSink))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("Files to expand LOINC zip file: Cannot find zip signature within the file");
	}

	private void mockHandlerStep2() {
		when(myHandlerStep2.canHandleFile(any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(1, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER));
			}
			return Optional.empty();
		});
	}

	private void mockHandlerStep1() {
		when(myHandlerStep1.canHandleFile(any(), any())).thenAnswer(t -> {
			String fileName = t.getArgument(1, String.class);
			if (fileName.contains(LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT.getCode())) {
				return Optional.of(new ITerminologyImportFileHandlerStep.FileHandlingInstructions(LoincUploadPropertiesEnum.LOINC_FILE.getCode(), ITerminologyImportFileHandlerStep.FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER));
			}
			return Optional.empty();
		});
	}

	private void mockJobPersistenceFetchDistributionFile() throws IOException {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);

		// Load LOINC marked as version 2.67
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");

		when(myJobPersistence.fetchAttachmentByFilename(eq(MY_INSTANCE_ID), eq(DISTRIBUTION_FILE_ATTACHMENT_FILENAME))).thenAnswer(t -> {
			String fileName = t.getArgument(1, String.class);
			assertEquals(DISTRIBUTION_FILE_ATTACHMENT_FILENAME, fileName);
			byte[] bytes = files.getZipBytes();
			return new AttachmentDetails(new ByteArrayInputStream(bytes), AttachmentContentTypeEnum.ZIP, DISTRIBUTION_FILE_ATTACHMENT_FILENAME);
		});
	}

	private void mockJobPersistenceStoreNewAttachment() {
		AtomicInteger counter = new AtomicInteger(0);
		when(myJobPersistence.storeNewAttachment(any(), any())).thenAnswer(t -> "ATT-" + counter.incrementAndGet());
	}

	@Nonnull
	private StepExecutionDetails<LoincJobImportParameters, VoidModel> newStepExecutionDetails() {
		JobDefinition<LoincJobImportParameters> jobDefinition = JobDefinition.newBuilder()
			.setJobDefinitionId("job")
			.setJobDefinitionVersion(1)
			.setJobDescription("a job")
			.setParametersType(LoincJobImportParameters.class)
			.addFirstStep("step-0", "step-0", TerminologyFileSetJson.class, myHandlerStep0)
			.addIntermediateStep("step-1", "step-1", TerminologyFileSetJson.class, myHandlerStep1)
			.addIntermediateStep("step-2", "step-2", TerminologyFileSetJson.class, myHandlerStep2)
			.addLastStep("step-3", "step-3", myHandlerStep3)
			.build();

		LoincJobImportParameters jobParameters = new LoincJobImportParameters();
		JobInstance instance = new JobInstance();
		instance.setInstanceId(MY_INSTANCE_ID);
		return new StepExecutionDetails<>(jobParameters, null, instance, new WorkChunk(), myJobStepExecutionServices, jobDefinition, "step-0", "step-1");
	}

	private static class MyBaseExpandDistributionIntoFilesStep extends BaseExpandDistributionIntoFilesStep<LoincJobImportParameters, TerminologyFileSetJson> {

		@Override
		protected TerminologyFileSetJson newTerminologyFileSetJson() {
			return new TerminologyFileSetJson();
		}
	}
}
