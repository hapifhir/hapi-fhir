package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlListJobParameters;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceIdListStepTest {
	// wipmb review
	@Mock
	private IIdChunkProducer<PartitionedUrlChunkRangeJson> myIdChunkProducer;
	@Mock
	private StepExecutionDetails<PartitionedUrlListJobParameters, PartitionedUrlChunkRangeJson> myStepExecutionDetails;
	@Mock
	private IJobDataSink<ResourceIdListWorkChunkJson> myDataSink;
	@Mock
	private PartitionedUrlChunkRangeJson myData;
	@Mock
	private PartitionedUrlListJobParameters myParameters;

	@Captor
	private ArgumentCaptor<ResourceIdListWorkChunkJson> myDataCaptor;

	private ResourceIdListStep<PartitionedUrlListJobParameters, PartitionedUrlChunkRangeJson> myResourceIdListStep;

	@BeforeEach
	void beforeEach() {
		myResourceIdListStep = new ResourceIdListStep<>(myIdChunkProducer);
	}

	// wipmb can we delete this?
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 100, 500, 501, 2345, 10500})
	void testResourceIdListBatchSizeLimit(int theListSize) {
		List<TypedResourcePid> idList = generateIdList(theListSize);
		when(myStepExecutionDetails.getData()).thenReturn(myData);
		when(myParameters.getBatchSize()).thenReturn(500);
		when(myStepExecutionDetails.getParameters()).thenReturn(myParameters);
		IResourcePidStream mockStream = mock(IResourcePidStream.class);
		when(mockStream.getTypedResourcePidStream()).thenReturn(idList.stream());
		if (theListSize > 0) {
			// Ensure none of the work chunks exceed MAX_BATCH_OF_IDS in size:
			doAnswer(i -> {
				ResourceIdListWorkChunkJson list = i.getArgument(0);
				Assertions.assertTrue(list.size() <= ResourceIdListStep.MAX_BATCH_OF_IDS,
					"Id batch size should never exceed " + ResourceIdListStep.MAX_BATCH_OF_IDS);
				return null;
			}).when(myDataSink).accept(any(ResourceIdListWorkChunkJson.class));
		}
		// wipmb update to stream
		when(myIdChunkProducer.fetchResourceIdStream(any(), any(), any(), any()))
			.thenReturn(mockStream);

		final RunOutcome run = myResourceIdListStep.run(myStepExecutionDetails, myDataSink);
		assertNotEquals(null, run);

		// The work should be divided into chunks of MAX_BATCH_OF_IDS in size (or less, but never more):
		int expectedBatchCount = (int) Math.ceil((float) theListSize / ResourceIdListStep.MAX_BATCH_OF_IDS);
		verify(myDataSink, times(expectedBatchCount)).accept(myDataCaptor.capture());
		final List<ResourceIdListWorkChunkJson> allDataChunks = myDataCaptor.getAllValues();
		assertEquals(expectedBatchCount, allDataChunks.size());

		// Ensure that all chunks except the very last one are MAX_BATCH_OF_IDS in length
		for (int i = 0; i < expectedBatchCount - 1; i++) {
			assertEquals(ResourceIdListStep.MAX_BATCH_OF_IDS, allDataChunks.get(i).size());
		}

		// The very last chunk should be whatever is left over (if there is a remainder):
		int expectedLastBatchSize = theListSize % ResourceIdListStep.MAX_BATCH_OF_IDS;
		expectedLastBatchSize = (expectedLastBatchSize == 0) ? ResourceIdListStep.MAX_BATCH_OF_IDS : expectedLastBatchSize;
		if (!allDataChunks.isEmpty()) {
			assertEquals(expectedLastBatchSize, allDataChunks.get(allDataChunks.size() - 1).size());
		}
	}

	private List<TypedResourcePid> generateIdList(int theListSize) {
		List<TypedResourcePid> idList = new ArrayList<>();
		for (int id = 0; id < theListSize; id++) {
			IResourcePersistentId<?> theId = mock(IResourcePersistentId.class);
			when(theId.toString()).thenReturn(Integer.toString(id + 1));
			TypedResourcePid typedId = new TypedResourcePid("Patient", theId);
			idList.add(typedId);
		}
		return idList;
	}
}
