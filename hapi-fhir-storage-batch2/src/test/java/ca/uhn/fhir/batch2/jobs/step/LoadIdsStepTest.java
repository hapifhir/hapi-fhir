package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlListJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.r4.model.InstantType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.step.ResourceIdListStep.DEFAULT_PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoadIdsStepTest {

	public static final Date DATE_1 = new InstantType("2022-01-01T00:00:00Z").getValue();
	public static final Date DATE_2 = new InstantType("2022-01-02T00:00:00Z").getValue();
	public static final Date DATE_END = new InstantType("2022-02-01T00:00:00Z").getValue();

	@Mock
	private IBatch2DaoSvc myBatch2DaoSvc;

	@Mock
	private IJobDataSink<ResourceIdListWorkChunkJson> mySink;

	private LoadIdsStep mySvc;

	@BeforeEach
	public void before() {
		mySvc = new LoadIdsStep(myBatch2DaoSvc);
	}

	@Captor
	private ArgumentCaptor<ResourceIdListWorkChunkJson> myChunkIdsCaptor;

	@Test
	public void testGenerateSteps() {
		PartitionedUrlListJobParameters parameters = new PartitionedUrlListJobParameters();
		PartitionedUrlChunkRangeJson range = new PartitionedUrlChunkRangeJson();
		range.setStart(DATE_1).setEnd(DATE_END);
		String instanceId = "instance-id";
		JobInstance jobInstance = JobInstance.fromInstanceId(instanceId);
		String chunkId = "chunk-id";
		StepExecutionDetails<PartitionedUrlListJobParameters, PartitionedUrlChunkRangeJson> details = new StepExecutionDetails<>(parameters, range, jobInstance, chunkId);

		// First Execution

		when(myBatch2DaoSvc.fetchResourceIdsPage(eq(DATE_1), eq(DATE_END), eq(DEFAULT_PAGE_SIZE), isNull(), isNull()))
			.thenReturn(createIdChunk(0L, 20000L, DATE_2));

		mySvc.run(details, mySink);

		final int expectedLoops = 40;
		verify(mySink, times(40)).accept(myChunkIdsCaptor.capture());

		final List<ResourceIdListWorkChunkJson> allCapturedValues = myChunkIdsCaptor.getAllValues();
		for (int i = 0; i < expectedLoops ; i++) {
			String expected = createIdChunk(i * 500, (i * 500) + 500).toString();
			String actual = allCapturedValues.get(i).toString();
			assertEquals(expected, actual);
		}
		final ResourceIdListWorkChunkJson expectedIdChunk = createIdChunk(19500, 20000);
		assertEquals(expectedIdChunk.toString(), allCapturedValues.get(expectedLoops -1).toString());
	}

	@Nonnull
	private ResourceIdListWorkChunkJson createIdChunk(int theLow, int theHigh) {
		ResourceIdListWorkChunkJson retVal = new ResourceIdListWorkChunkJson();
		for (long i = theLow; i < theHigh; i++) {
			retVal.addTypedPid("Patient", i);
		}
		return retVal;
	}

	@Nonnull
	private IResourcePidList createIdChunk(long idLow, long idHigh, Date lastDate) {
		List<IResourcePersistentId> ids = new ArrayList<>();
		List<String> resourceTypes = new ArrayList<>();
		for (long i = idLow; i < idHigh; i++) {
			ids.add(JpaPid.fromId(i));
		}
		IResourcePidList chunk = new HomogeneousResourcePidList("Patient", ids, lastDate, null);
		return chunk;
	}

}
