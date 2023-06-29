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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceIdListStepTest {
	private static final int LIST_SIZE = 1500;

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

	private ResourceIdListStep<PartitionedUrlListJobParameters, PartitionedUrlChunkRangeJson> myResourceIdListStep;

	private List<TypedResourcePid> idList = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		myResourceIdListStep = new ResourceIdListStep<>(myIdChunkProducer);
		for (int id = 0; id <= LIST_SIZE; id++) {
			IResourcePersistentId theId = mock(IResourcePersistentId.class);
			when(theId.toString()).thenReturn(Integer.toString(id + 1));
			TypedResourcePid typedId = new TypedResourcePid("Patient", theId);
			idList.add(typedId);
		}
	}

	@Test
	void testMe() {
		when(myStepExecutionDetails.getData()).thenReturn(myData);
		when(myParameters.getBatchSize()).thenReturn(LIST_SIZE);
		when(myStepExecutionDetails.getParameters()).thenReturn(myParameters);
		Date someDate = new Date();
		//final HomogeneousResourcePidList homogeneousResourcePidList = new HomogeneousResourcePidList(ResourceType.Patient.name(), ids, someDate, null);
		HomogeneousResourcePidList homogeneousResourcePidList = mock(HomogeneousResourcePidList.class);
		when(homogeneousResourcePidList.getTypedResourcePids()).thenReturn(idList);
		when(homogeneousResourcePidList.getLastDate()).thenReturn(someDate);
		when(myIdChunkProducer.fetchResourceIdsPage(any(), any(), any(), any(), any()))
			.thenReturn(homogeneousResourcePidList);

		final RunOutcome run = myResourceIdListStep.run(myStepExecutionDetails, myDataSink);
		Assertions.assertNotEquals(null, run);
	}
}
