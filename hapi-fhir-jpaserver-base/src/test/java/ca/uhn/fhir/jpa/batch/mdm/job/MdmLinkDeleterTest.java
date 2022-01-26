package ca.uhn.fhir.jpa.batch.mdm.job;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MdmLinkDeleterTest {
	@Mock
	private DaoConfig myDaoConfig;
	@Mock
	private PlatformTransactionManager myPlatformTransactionManager;
	@Mock
	private IMdmLinkDao myMdmLinkDao;

	@Captor
	ArgumentCaptor<List<Long>> myPidListCaptor;

	@InjectMocks
	private MdmLinkDeleter myMdmLinkDeleter;

	@Test
	public void testMdmLinkDeleterSplitsPidList() throws Exception {
		int threadCount = 4;
		int batchSize = 5;
		when(myDaoConfig.getReindexBatchSize()).thenReturn(batchSize);
		when(myDaoConfig.getReindexThreadCount()).thenReturn(threadCount);

		List<Long> allPidsList = new ArrayList<>();
		int count = threadCount*batchSize;
		for (long i = 0; i < count; ++i) {
			allPidsList.add(i);
		}
		myMdmLinkDeleter.process(allPidsList);

		verify(myMdmLinkDao, times(threadCount)).findAllById(myPidListCaptor.capture());
		verify(myMdmLinkDao, times(threadCount)).deleteAll(anyList());
		verifyNoMoreInteractions(myMdmLinkDao);
		List<List<Long>> pidListList = myPidListCaptor.getAllValues();
		assertEquals(threadCount, pidListList.size());
		for (List<Long> pidList : pidListList) {
			assertEquals(batchSize, pidList.size());
		}
	}

}
