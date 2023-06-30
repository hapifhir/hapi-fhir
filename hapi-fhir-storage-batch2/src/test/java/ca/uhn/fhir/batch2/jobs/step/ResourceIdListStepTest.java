package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlListJobParameters;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
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
	private static final int LIST_SIZE = 2345;

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

	private List<TypedResourcePid> myIdList = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		myResourceIdListStep = new ResourceIdListStep<>(myIdChunkProducer);
		// TODO: parameterize this algorithm and pass in different list sizes from each @Test or @ParameterizedTest
		for (int id = 0; id < LIST_SIZE; id++) {
			IResourcePersistentId theId = mock(IResourcePersistentId.class);
			when(theId.toString()).thenReturn(Integer.toString(id + 1));
			TypedResourcePid typedId = new TypedResourcePid("Patient", theId);
			myIdList.add(typedId);
		}
	}

	@Test
	void testResourceIdListBatchSizeLimit() {
		when(myStepExecutionDetails.getData()).thenReturn(myData);
		when(myParameters.getBatchSize()).thenReturn(LIST_SIZE);
		when(myStepExecutionDetails.getParameters()).thenReturn(myParameters);
		HomogeneousResourcePidList homogeneousResourcePidList = mock(HomogeneousResourcePidList.class);
		when(homogeneousResourcePidList.getTypedResourcePids()).thenReturn(myIdList);
		when(homogeneousResourcePidList.getLastDate()).thenReturn(new Date());
		when(myIdChunkProducer.fetchResourceIdsPage(any(), any(), any(), any(), any()))
			.thenReturn(homogeneousResourcePidList);

		doAnswer(i -> {
			ResourceIdListWorkChunkJson list = i.getArgument(0);
			Assertions.assertTrue(list.size() <= ResourceIdListStep.MAX_BATCH_OF_IDS,
				"Id batch size should never exceed "+ResourceIdListStep.MAX_BATCH_OF_IDS);
			return null;
		}).when(myDataSink).accept(any(ResourceIdListWorkChunkJson.class));

		final RunOutcome run = myResourceIdListStep.run(myStepExecutionDetails, myDataSink);
		assertNotEquals(null, run);

		verify(myDataSink, times(5)).accept(myDataCaptor.capture());

		final List<ResourceIdListWorkChunkJson> allDataChunks = myDataCaptor.getAllValues();

		assertEquals(5, allDataChunks.size());

		assertEquals(500, allDataChunks.get(0).size());
		assertEquals(500, allDataChunks.get(1).size());
		assertEquals(500, allDataChunks.get(2).size());
		assertEquals(500, allDataChunks.get(3).size());
		assertEquals(345, allDataChunks.get(4).size());
	}
}
