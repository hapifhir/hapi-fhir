package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
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
	public static final Date DATE_3 = new InstantType("2022-01-03T00:00:00Z").getValue();
	public static final Date DATE_4 = new InstantType("2022-01-04T00:00:00Z").getValue();
	public static final Date DATE_END = new InstantType("2022-02-01T00:00:00Z").getValue();

	@Mock
	private IResourceReindexSvc myResourceReindexSvc;

	@Mock
	private IJobDataSink<ResourceIdListWorkChunkJson> mySink;

	private LoadIdsStep mySvc;

	@BeforeEach
	public void before() {
		mySvc = new LoadIdsStep(myResourceReindexSvc);
	}

	@Captor
	private ArgumentCaptor<ResourceIdListWorkChunkJson> myChunkIdsCaptor;

	@Test
	public void testGenerateSteps() {
		ReindexJobParameters parameters = new ReindexJobParameters();
		ReindexChunkRangeJson range = new ReindexChunkRangeJson();
		range.setStart(DATE_1).setEnd(DATE_END);
		String instanceId = "instance-id";
		String chunkId = "chunk-id";
		StepExecutionDetails<ReindexJobParameters, ReindexChunkRangeJson> details = new StepExecutionDetails<>(parameters, range, instanceId, chunkId);

		// First Execution

		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_1), eq(DATE_END), eq(DEFAULT_PAGE_SIZE), isNull(), isNull()))
			.thenReturn(createIdChunk(0L, 20000L, DATE_2));
		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_2), eq(DATE_END), eq(DEFAULT_PAGE_SIZE), isNull(), isNull()))
			.thenReturn(createIdChunk(20000L, 40000L, DATE_3));
		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_3), eq(DATE_END), eq(DEFAULT_PAGE_SIZE), isNull(), isNull()))
			.thenReturn(createIdChunk(40000L, 40040L, DATE_3));

		mySvc.run(details, mySink);

		verify(mySink, times(41)).accept(myChunkIdsCaptor.capture());
		for (int i = 0; i < 40; i++) {
			assertEquals(createIdChunk(i * 1000, (i * 1000) + 1000).toString(), myChunkIdsCaptor.getAllValues().get(i).toString());
		}
		assertEquals(createIdChunk(40000, 40040).toString(), myChunkIdsCaptor.getAllValues().get(40).toString());

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
		List<ResourcePersistentId> ids = new ArrayList<>();
		List<String> resourceTypes = new ArrayList<>();
		for (long i = idLow; i < idHigh; i++) {
			ids.add(new ResourcePersistentId(i));
		}
		IResourcePidList chunk = new HomogeneousResourcePidList("Patient", ids, lastDate);
		return chunk;
	}

}
