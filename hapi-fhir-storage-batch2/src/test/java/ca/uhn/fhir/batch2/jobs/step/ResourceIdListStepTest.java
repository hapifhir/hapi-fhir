package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.ListWrappingPidStream;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceIdListStepTest {
	@Mock
	private IIdChunkProducer<ChunkRangeJson> myIdChunkProducer;
	@Mock
	private StepExecutionDetails<PartitionedUrlJobParameters, ChunkRangeJson> myStepExecutionDetails;
	@Mock
	private IJobDataSink<ResourceIdListWorkChunkJson> myDataSink;
	@Mock
	private ChunkRangeJson myData;

	@Captor
	private ArgumentCaptor<ResourceIdListWorkChunkJson> myDataCaptor;

	private ResourceIdListStep<PartitionedUrlJobParameters> myResourceIdListStep;

	@BeforeEach
	void beforeEach() {
		myResourceIdListStep = new ResourceIdListStep<>(myIdChunkProducer);
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 100, 500, 501, 2345, 10500})
	void testResourceIdListBatchSizeLimit(int theListSize) {
		List<JpaPid> idList = generateIdList(theListSize);
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);

		PartitionedUrlJobParameters parameters = new PartitionedUrlJobParameters();

		when(myStepExecutionDetails.getData()).thenReturn(myData);
		parameters.setBatchSize(500);

		when(myStepExecutionDetails.getParameters()).thenReturn(parameters);
		IResourcePidStream resourcePidStream = new ListWrappingPidStream<>(
			new HomogeneousResourcePidList<>("Patient", idList, null, partitionId));
		if (theListSize > 0) {
			// Ensure none of the work chunks exceed MAX_BATCH_OF_IDS in size:
			doAnswer(i -> {
				ResourceIdListWorkChunkJson list = i.getArgument(0);
				Assertions.assertTrue(list.size() <= ResourceIdListStep.MAX_BATCH_OF_IDS,
					"Id batch size should never exceed " + ResourceIdListStep.MAX_BATCH_OF_IDS);
				return null;
			}).when(myDataSink).accept(any(ResourceIdListWorkChunkJson.class));
		}
		when(myIdChunkProducer.fetchResourceIdStream(any())).thenReturn(resourcePidStream);

		final RunOutcome run = myResourceIdListStep.run(myStepExecutionDetails, myDataSink);
		assertThat(run).isNotEqualTo(null);

		// The work should be divided into chunks of MAX_BATCH_OF_IDS in size (or less, but never more):
		int expectedBatchCount = (int) Math.ceil((float) theListSize / ResourceIdListStep.MAX_BATCH_OF_IDS);
		verify(myDataSink, times(expectedBatchCount)).accept(myDataCaptor.capture());
		final List<ResourceIdListWorkChunkJson> allDataChunks = myDataCaptor.getAllValues();
		assertThat(allDataChunks).hasSize(expectedBatchCount);

		// Ensure that all chunks except the very last one are MAX_BATCH_OF_IDS in length
		for (int i = 0; i < expectedBatchCount - 1; i++) {
			ResourceIdListWorkChunkJson dataChunk = allDataChunks.get(i);
			assertEquals(ResourceIdListStep.MAX_BATCH_OF_IDS, dataChunk.size());
			assertEquals(partitionId, dataChunk.getRequestPartitionId());
		}

		// The very last chunk should be whatever is left over (if there is a remainder):
		int expectedLastBatchSize = theListSize % ResourceIdListStep.MAX_BATCH_OF_IDS;
		expectedLastBatchSize = (expectedLastBatchSize == 0) ? ResourceIdListStep.MAX_BATCH_OF_IDS : expectedLastBatchSize;
		if (!allDataChunks.isEmpty()) {
			assertEquals(expectedLastBatchSize, allDataChunks.get(allDataChunks.size() - 1).size());
		}
	}

	@ParameterizedTest
	@CsvSource(textBlock =
		//  limitResourceCount, availableIds, batchSize
		"""
			100,                222,          500
			100,                222,          50
			100,                20,           500
			100,                20,           0
			999,                999,          999
			""")
	void testLimitResourceCount(int theLimitResourceCount, int theAvailableIds, int theBatchSize) {
		// Setup
		List<JpaPid> idList = generateIdList(theAvailableIds);
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);

		when(myStepExecutionDetails.getData()).thenReturn(myData);

		PartitionedUrlJobParameters parameters = new PartitionedUrlJobParameters();
		parameters.setBatchSize(theBatchSize);
		parameters.setLimitResourceCount(theLimitResourceCount);
		when(myStepExecutionDetails.getParameters()).thenReturn(parameters);
		IResourcePidStream resourcePidStream = new ListWrappingPidStream<>(
			new HomogeneousResourcePidList<>("Patient", idList, null, partitionId));

		when(myIdChunkProducer.fetchResourceIdStream(any())).thenReturn(resourcePidStream);

		// Test
		final RunOutcome run = myResourceIdListStep.run(myStepExecutionDetails, myDataSink);
		assertNotNull(run);

		// Verify
		verify(myDataSink, atLeast(1)).accept(myDataCaptor.capture());
		List<TypedPidJson> expectedPids = idList
			.subList(0, Math.min(theLimitResourceCount, theAvailableIds))
			.stream()
			.map(t -> new TypedPidJson("Patient", 1, t.getId().toString()))
			.toList();
		List<ResourceIdListWorkChunkJson> allChunks = myDataCaptor.getAllValues();
		int expectedBatchSize = theBatchSize;
		if (expectedBatchSize < 1) {
			expectedBatchSize = 1;
		}
		for (var chunk : allChunks) {
			assertThat(chunk.getTypedPids()).asList().hasSizeBetween(0, expectedBatchSize);
		}
		List<TypedPidJson> actualPids = allChunks
			.stream()
			.flatMap(t -> t.getTypedPids().stream())
			.toList();
		assertThat(actualPids).containsExactlyElementsOf(expectedPids);

	}

	private List<JpaPid> generateIdList(int theListSize) {
		List<JpaPid> idList = new ArrayList<>();
		for (long id = 0; id < theListSize; id++) {
			JpaPid pid = JpaPid.fromId(id + 1, 1);
			idList.add(pid);
		}
		return idList;
	}
}
