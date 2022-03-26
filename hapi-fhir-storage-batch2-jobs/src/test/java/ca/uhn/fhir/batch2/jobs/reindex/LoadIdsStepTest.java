package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.r4.model.InstantType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private IJobDataSink<ReindexChunkIds> mySink;

	@InjectMocks
	private LoadIdsStep mySvc;

	@Captor
	private ArgumentCaptor<ReindexChunkIds> myChunkIdsCaptor;

	@Test
	public void testGenerateSteps() {
		ReindexJobParameters parameters = new ReindexJobParameters();
		ReindexChunkRange range = new ReindexChunkRange()
			.setStart(DATE_1)
			.setEnd(DATE_END);
		String instanceId = "instance-id";
		String chunkId = "chunk-id";
		StepExecutionDetails<ReindexJobParameters, ReindexChunkRange> details = new StepExecutionDetails<>(parameters, range, instanceId, chunkId);

		// First Execution

		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_1), eq(DATE_END), isNull(), isNull()))
			.thenReturn(createIdChunk(0L, 20000L, DATE_2));
		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_2), eq(DATE_END), isNull(), isNull()))
			.thenReturn(createIdChunk(20000L, 40000L, DATE_3));
		when(myResourceReindexSvc.fetchResourceIdsPage(eq(DATE_3), eq(DATE_END), isNull(), isNull()))
			.thenReturn(createIdChunk(40000L, 40040L, DATE_3));

		mySvc.run(details, mySink);
		verify(mySink, times(41)).accept(myChunkIdsCaptor.capture());
		for (int i = 0; i < 40; i++) {
			assertEquals(createIdChunk(i * 1000, (i * 1000) + 1000).toString(), myChunkIdsCaptor.getAllValues().get(i).toString());
		}
		assertEquals(createIdChunk(40000, 40040).toString(), myChunkIdsCaptor.getAllValues().get(40).toString());

	}

	@Nonnull
	private ReindexChunkIds createIdChunk(int theLow, int theHigh) {
		ReindexChunkIds retVal = new ReindexChunkIds();
		for (int i = theLow; i < theHigh; i++) {
			retVal.getIds().add(new ReindexChunkIds.Id().setResourceType("Patient").setId(Integer.toString(i)));
		}
		return retVal;
	}

	@Nonnull
	private IResourceReindexSvc.IdChunk createIdChunk(long idLow, long idHigh, Date lastDate) {
		List<ResourcePersistentId> ids = new ArrayList<>();
		List<String> resourceTypes = new ArrayList<>();
		for (long i = idLow; i < idHigh; i++) {
			ids.add(new ResourcePersistentId(i));
			resourceTypes.add("Patient");
		}
		IResourceReindexSvc.IdChunk chunk = new IResourceReindexSvc.IdChunk(ids, resourceTypes, lastDate);
		return chunk;
	}

}
