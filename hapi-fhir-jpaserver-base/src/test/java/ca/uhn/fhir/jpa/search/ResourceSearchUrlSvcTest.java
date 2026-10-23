package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
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
import org.springframework.data.domain.Limit;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import static ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc.MAX_DELETE_PAGES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Created by claude-fable-5
@ExtendWith(MockitoExtension.class)
class ResourceSearchUrlSvcTest {

	@RegisterExtension
	private final LogbackTestExtension myLogCapture = new LogbackTestExtension(ResourceSearchUrlSvc.class, Level.DEBUG);

	private static final int PAGE_SIZE = JpaStorageSettings.DEFAULT_SEARCH_URL_CLEANUP_BATCH_SIZE;

	@Mock
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	private final JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	private ResourceSearchUrlSvc mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new ResourceSearchUrlSvc(
				null, myResourceSearchUrlDao, null, null, null, myStorageSettings, new NoopTransactionManager());
	}

	@Test
	void deleteEntriesOlderThan_staleSetNeverDrains_stopsAtMaxPages() {
		// simulate a pathological non-draining table: every page reads back full
		List<Long> fullPage = LongStream.range(0, PAGE_SIZE).boxed().toList();
		AtomicInteger findCount = new AtomicInteger();
		when(myResourceSearchUrlDao.findStaleIdsSkipLocked(any(Date.class), any(Limit.class)))
				.thenAnswer(invocation -> {
					if (findCount.incrementAndGet() > MAX_DELETE_PAGES) {
						throw new IllegalStateException("Loop did not stop at MAX_DELETE_PAGES");
					}
					return fullPage;
				});
		when(myResourceSearchUrlDao.deleteByResIds(anyList())).thenReturn(PAGE_SIZE);

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(MAX_DELETE_PAGES))
			.findStaleIdsSkipLocked(any(Date.class), any(Limit.class));
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("Reached the maximum of");
	}

	@Test
	void deleteEntriesOlderThan_staleSetDrains_deletesAllPagesWithoutWarning() {
		List<Long> fullPage = LongStream.range(0, PAGE_SIZE).boxed().toList();
		List<Long> lastPage = List.of(1L, 2L, 3L);
		when(myResourceSearchUrlDao.findStaleIdsSkipLocked(any(Date.class), any(Limit.class)))
				.thenReturn(fullPage)
				.thenReturn(lastPage);
		when(myResourceSearchUrlDao.deleteByResIds(fullPage)).thenReturn(PAGE_SIZE);
		when(myResourceSearchUrlDao.deleteByResIds(lastPage)).thenReturn(lastPage.size());

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(2)).findStaleIdsSkipLocked(any(Date.class), any(Limit.class));
		verify(myResourceSearchUrlDao).deleteByResIds(fullPage);
		verify(myResourceSearchUrlDao).deleteByResIds(lastPage);
		assertThat(myLogCapture.getLogEvents(theEvent -> theEvent.getLevel() == Level.WARN)).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture)
				.hasDebugMessage("Deleted page of")
				.hasInfoMessage("Deleted 1003 SearchUrls");
	}

	@Test
	void deleteEntriesOlderThan_nothingStale_logsAtDebugOnly() {
		when(myResourceSearchUrlDao.findStaleIdsSkipLocked(any(Date.class), any(Limit.class)))
				.thenReturn(List.of());

		mySvc.deleteEntriesOlderThan(new Date());

		assertThat(myLogCapture.getLogEvents(theEvent -> theEvent.getLevel() == Level.INFO))
				.isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasDebugMessage("Deleted 0 SearchUrls");
	}

	@Test
	void deleteEntriesOlderThan_batchSizeConfigured_usesItAsPageLimit() {
		myStorageSettings.setSearchUrlCleanupBatchSize(2);
		List<Long> fullPage = List.of(1L, 2L);
		List<Long> lastPage = List.of(3L);
		when(myResourceSearchUrlDao.findStaleIdsSkipLocked(any(Date.class), eq(Limit.of(2))))
				.thenReturn(fullPage)
				.thenReturn(lastPage);
		when(myResourceSearchUrlDao.deleteByResIds(fullPage)).thenReturn(fullPage.size());
		when(myResourceSearchUrlDao.deleteByResIds(lastPage)).thenReturn(lastPage.size());

		mySvc.deleteEntriesOlderThan(new Date());

		verify(myResourceSearchUrlDao, times(2)).findStaleIdsSkipLocked(any(Date.class), eq(Limit.of(2)));
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage("Deleted 3 SearchUrls");
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
