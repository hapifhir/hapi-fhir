package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static ca.uhn.fhir.batch2.util.Batch2Utils.BATCH_START_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChunkProducerTest {
	@Mock
	private IBatch2DaoSvc myBatch2DaoSvc;
	@InjectMocks
	private ChunkProducer myChunkProducer;

	@Test
	public void fetchResourceIdStream_worksAsExpected() {
		// setup
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		String url = "Patient?";
		ChunkRangeJson chunkRangeJson = new ChunkRangeJson(BATCH_START_DATE, new Date()).setPartitionId(partitionId).setUrl(url);
		IResourcePidStream stream = mock(IResourcePidStream.class);
		when(myBatch2DaoSvc.fetchResourceIdStream(any(), any(), any(), any())).thenReturn(stream);

		// test
		IResourcePidStream actualStream = myChunkProducer.fetchResourceIdStream(chunkRangeJson);

		// verify
		assertThat(actualStream).isSameAs(stream);
		ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
		ArgumentCaptor<RequestPartitionId> partitionCaptor = ArgumentCaptor.forClass(RequestPartitionId.class);
		ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
		verify(myBatch2DaoSvc).fetchResourceIdStream(dateCaptor.capture(), dateCaptor.capture(), partitionCaptor.capture(), urlCaptor.capture());
		assertThat(dateCaptor.getAllValues()).containsExactly(chunkRangeJson.getStart(), chunkRangeJson.getEnd());
		assertThat(partitionCaptor.getValue()).isEqualTo(partitionId);
		assertThat(urlCaptor.getValue()).isEqualTo(url);

	}
}