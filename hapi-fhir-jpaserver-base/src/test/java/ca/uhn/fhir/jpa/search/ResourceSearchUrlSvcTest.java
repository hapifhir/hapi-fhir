package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import static ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc.DELETE_PAGE_SIZE;
import static ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc.MAX_DELETE_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-fable-5
@ExtendWith(MockitoExtension.class)
class ResourceSearchUrlSvcTest {

	@RegisterExtension
	private final LogbackTestExtension myLogCapture = new LogbackTestExtension(ResourceSearchUrlSvc.class, Level.DEBUG);

	@Mock
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	private ResourceSearchUrlSvc mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new ResourceSearchUrlSvc(
				null, myResourceSearchUrlDao, null, null, null, new NoopTransactionManager());
	}

	@Test
	void deleteEntriesOlderThan_staleSetNeverDrains_stopsAtMaxPages() {
		// simulate a pathological non-draining table: every page reads back a full slice
		List<Long> fullPage = LongStream.range(0, DELETE_PAGE_SIZE).boxed().toList();
		AtomicInteger findCount = new AtomicInteger();
		when(myResourceSearchUrlDao.findStaleIds(any(Date.class), any(Pageable.class))).thenAnswer(invocation -> {
			if (findCount.incrementAndGet() > MAX_DELETE_PAGES) {
				throw new IllegalStateException("Loop did not stop at MAX_DELETE_PAGES");
			}
			return new SliceImpl<>(fullPage, PageRequest.of(0, DELETE_PAGE_SIZE), true);
		});
		when(myResourceSearchUrlDao.deleteByResIds(anyList())).thenReturn(DELETE_PAGE_SIZE);

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(MAX_DELETE_PAGES)).findStaleIds(any(Date.class), any(Pageable.class));
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("Reached the maximum of");
	}

	@Test
	void deleteEntriesOlderThan_staleSetDrains_deletesAllPagesWithoutWarning() {
		List<Long> fullPage = LongStream.range(0, DELETE_PAGE_SIZE).boxed().toList();
		List<Long> lastPage = List.of(1L, 2L, 3L);
		when(myResourceSearchUrlDao.findStaleIds(any(Date.class), any(Pageable.class)))
				.thenReturn(new SliceImpl<>(fullPage, PageRequest.of(0, DELETE_PAGE_SIZE), true))
				.thenReturn(new SliceImpl<>(lastPage, PageRequest.of(0, DELETE_PAGE_SIZE), false));
		when(myResourceSearchUrlDao.deleteByResIds(fullPage)).thenReturn(DELETE_PAGE_SIZE);
		when(myResourceSearchUrlDao.deleteByResIds(lastPage)).thenReturn(lastPage.size());

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(2)).findStaleIds(any(Date.class), any(Pageable.class));
		verify(myResourceSearchUrlDao).deleteByResIds(fullPage);
		verify(myResourceSearchUrlDao).deleteByResIds(lastPage);
		assertThat(myLogCapture.getLogEvents(theEvent -> theEvent.getLevel() == Level.WARN)).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasDebugMessage("Deleted page of");
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage("Deleted 1803 SearchUrls");
	}

	private static class NoopTransactionManager extends AbstractPlatformTransactionManager {
		@Override
		protected Object doGetTransaction() {
			return new Object();
		}

		@Override
		protected void doBegin(Object theTransaction, TransactionDefinition theDefinition) {
			// nothing to begin
		}

		@Override
		protected void doCommit(DefaultTransactionStatus theStatus) {
			// nothing to commit
		}

		@Override
		protected void doRollback(DefaultTransactionStatus theStatus) {
			// nothing to roll back
		}
	}
}
