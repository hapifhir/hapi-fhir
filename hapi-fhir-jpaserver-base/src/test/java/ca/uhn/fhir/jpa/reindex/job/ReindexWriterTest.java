package ca.uhn.fhir.jpa.reindex.job;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReindexWriterTest {

	@Mock
	private DaoConfig myDaoConfig;
	@Mock
	private PlatformTransactionManager myPlatformTransactionManager;
	@Mock
	ResourceReindexer myResourceReindexer;

	@InjectMocks
	private ReindexWriter myReindexWriter;

	@Test
	public void testReindexSplitsPidList() throws Exception {
		when(myDaoConfig.getReindexBatchSize()).thenReturn(5);
		when(myDaoConfig.getReindexThreadCount()).thenReturn(4);

		List<Long> pidList = new ArrayList<>();
		int count = 20;
		for (long i = 0; i < count; ++i) {
			pidList.add(i);
		}
		List<List<Long>> pidListList = new ArrayList<>();
		pidListList.add(pidList);
		myReindexWriter.write(pidListList);

		verify(myResourceReindexer, atMost(count)).reindexResourceEntity(any());
		verifyNoMoreInteractions(myResourceReindexer);
	}
}
