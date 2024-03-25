package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.model.api.IModelJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobInstanceProcessorTest {

	private static final String JOB_ID = "job_id";
	private static final String SECOND_STEP_ID = "second";

	public static class TestJobInstanceProcessor extends JobInstanceProcessor {

		private PagingChunkIterator mySpy;

		public TestJobInstanceProcessor(IJobPersistence theJobPersistence, BatchJobSender theBatchJobSender, String theInstanceId, JobChunkProgressAccumulator theProgressAccumulator, IReductionStepExecutorService theReductionStepExecutorService, JobDefinitionRegistry theJobDefinitionRegistry, PlatformTransactionManager theTransactionManager) {
			super(theJobPersistence, theBatchJobSender, theInstanceId, theProgressAccumulator, theReductionStepExecutorService, theJobDefinitionRegistry, theTransactionManager);
		}

		public void setSpy(PagingChunkIterator theSpy) {
			mySpy = theSpy;
		}

		@Override
		protected PagingChunkIterator getReadyChunks(String theInstanceId) {
			if (mySpy == null) {
				return super.getReadyChunks(theInstanceId);
			}
			return mySpy;
		}
	}

	@Mock
	private IJobPersistence myJobPersistence;

	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;

	private TestJobInstanceProcessor myJobInstanceProcessor;

	@Test
	public void testLargeBatchesForEnqueue_pagesCorrectly() {
		// setup
		int batchSize = 2;
		int total = 20;
		String instanceId = "instanceId";
		JobInstance instance = new JobInstance();
		instance.setInstanceId(instanceId);
		instance.setStatus(StatusEnum.QUEUED);
		List<WorkChunkMetadata> workchunks = new ArrayList<>();
		Set<String> expectedIds = new HashSet<>();
		for (int i = 0; i < total; i++) {
			String id = "id" + i;
			workchunks.add(createWorkChunkMetadata(instanceId).setId(id));
			expectedIds.add(id);
		}

		// create out object to test (it's not a bean)
		myJobInstanceProcessor = new TestJobInstanceProcessor(
			myJobPersistence,
			mock(BatchJobSender.class),
			instanceId,
			mock(JobChunkProgressAccumulator.class),
			mock(IReductionStepExecutorService.class),
			myJobDefinitionRegistry,
			null
		);

		// we need to spy our iterator so we don't use 1000s in this test
		JobInstanceProcessor.PagingChunkIterator chunkIterator = spy(myJobInstanceProcessor.getReadyChunks(instanceId));

		// mock
		when(myJobPersistence.fetchInstance(eq(instanceId)))
			.thenReturn(Optional.of(instance));
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(any()))
			.thenReturn((JobDefinition<IModelJson>) createDefinition());

		when(chunkIterator.getPageSize())
			.thenReturn(batchSize);
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(anyInt(), eq(batchSize), eq(instanceId), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenAnswer((args) -> {
				int pageNum = args.getArgument(0);
				int pageSize = args.getArgument(1);
				assertEquals(batchSize, pageSize);
				int indexLow = pageNum * pageSize;
				int indexHigh = Math.min(indexLow + pageNum + pageSize, workchunks.size());
				List<WorkChunkMetadata> subList = workchunks.subList(indexLow, indexHigh);

				Page<WorkChunkMetadata> page = new PageImpl<>(subList,
					PageRequest.of(pageNum, pageSize),
					total);
				return page;
			});
		myJobInstanceProcessor.setSpy(chunkIterator);

		// test
		myJobInstanceProcessor.process();

		// verify
		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		verify(myJobPersistence, times(total))
			.enqueueWorkChunkForProcessing(idCaptor.capture(), any());
		assertEquals(total, idCaptor.getAllValues().size());
		Set<String> actualIds = new HashSet<>(idCaptor.getAllValues());
		assertEquals(expectedIds.size(), actualIds.size());
	}

	private WorkChunkMetadata createWorkChunkMetadata(String theInstanceId) {
		WorkChunkMetadata metadata = new WorkChunkMetadata();
		metadata.setInstanceId(theInstanceId);
		metadata.setJobDefinitionId(JOB_ID);
		metadata.setJobDefinitionVersion(1);
		metadata.setId("id");
		metadata.setTargetStepId(SECOND_STEP_ID);
		metadata.setStatus(WorkChunkStatusEnum.READY);
		metadata.setSequence(0);
		return metadata;
	}

	private JobDefinition<?> createDefinition() {
		return JobDefinition.newBuilder()
			.setParametersType(IModelJson.class)
			.setJobDefinitionId(JOB_ID)
			.setJobDefinitionVersion(1)
			.setJobDescription("a descriptive description")
			.addFirstStep(
				"first",
				"first description",
				IModelJson.class,
				(details, sink) -> {
					sink.accept(new IModelJson() {

					});
					return RunOutcome.SUCCESS;
				})
			.addIntermediateStep(
				SECOND_STEP_ID,
				"second description",
				IModelJson.class,
				(details, sink) -> {
					sink.accept(new IModelJson() {
					});
					return RunOutcome.SUCCESS;
				}
			)
			.addLastStep(
				"last",
				"last description",
				(details, sink) -> {
					sink.accept(new VoidModel());
					return RunOutcome.SUCCESS;
				}
			)
			.build();
	}
}
